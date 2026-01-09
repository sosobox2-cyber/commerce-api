package com.cware.api.paintp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqListVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpDeliveryCompleteListVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderListVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderResultVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderVO;
import com.cware.netshopping.paintp.order.service.PaIntpOrderService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Controller
@RequestMapping(value = "/paintp/order")
public class PaIntpOrderController extends AbstractController {
	
	private transient static Logger log = LoggerFactory.getLogger(PaIntpOrderController.class);
	
	@Autowired
	private PaIntpAsyncController paIntpAsyncController;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaIntpOrderService paIntpOrderService;
	
	@Autowired
	private PaIntpComUtil paIntpComUtil;
	
	/**
	 * 주문목록조회1(인터파크에서 결제완료 후 출고지시 된 주문 목록)
	 * <pre>
	 * - 조회시작 및 종료시간 간격은 최대 조회 기간인 1일치
	 * </pre>
	 * 
	 * @param fromDate 시작일자, optional, default = SYSDATE-1, format = yyyyMMddHHmmss
	 * @param toDate   종료일자, optional, default = SYSDATE,   format = yyyyMMddHHmmss
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order-list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request
			) throws Exception {
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		String prg_id = "IF_PAINTPAPI_03_001";
		String duplicateCheck = "";
		String startDate = "";
		String endDate = "";
		String paCode = "";
		String errorMsg = "";
		
		endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
		startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("code", "200");
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info(prg_id + " - 02.파라미터 검증, [fromDate={}, toDate={}]", fromDate, toDate);
			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				log.info(prg_id + " - 03.주문내역조회1 API 호출");
				params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				params.put("sc.supplyEntrNo", apiInfo.get("INTP_ENTP_CODE").toString());
				params.put("sc.strDate", startDate);
				params.put("sc.endDate", endDate);
				
				PaIntpOrderListVO orderList = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderListVO.class, paCode);
				
//				log.info("04.주문내역조회1 API 호출 결과=" + orderList);
				if (orderList.getOrderList() == null && orderList.getResult().getMessage().contains("요청한 데이터가 존재하지않습니다.")) {
					paramMap.put("code", "200"); //조회 내역만 없지 에러는 아님(통신 성공)
					errorMsg += paCode + " : " + orderList.getResult().getMessage() + " | ";
				} else if(!Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(orderList.getResult().getCode())) {
					paramMap.put("code", "400");
					errorMsg += paCode + " : " + orderList.getResult().getMessage() + " | ";
				}
			}
			paramMap.put("message", errorMsg.equals("") ? "OK" : errorMsg);
		}
		catch ( Exception e ) {
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}
		finally {
			log.info(prg_id + " - 05.프로그램 중복 실행 검사 [end]");
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			log.info(prg_id + " - 06.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
		}
		
		log.info(prg_id + " - 07. 주문내역조회2(발주확인) 호출");
		orderConfirmList(startDate, endDate, request);

		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문목록조회2(발주 확인된 목록)
	 * 
	 * @param fromDate 시작일자, optional, default = SYSDATE-1, format = yyyyMMddHHmmss
	 * @param toDate   종료일자, optional, default = SYSDATE,   format = yyyyMMddHHmmss
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order-confirm-list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderConfirmList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request
			) throws Exception {
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		String prg_id = "IF_PAINTPAPI_03_002";
		String duplicateCheck = "";
		String resultCode = "";
		String resultMsg = "";
		String paCode = "";
		String errorMsg = "";
		
		StringBuffer sb = new StringBuffer();
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck))	throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info(prg_id + " - 02.파라미터 검증, [fromDate={}, toDate={}]", fromDate, toDate);
			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				log.info(prg_id + " - 03.주문내역조회2 API 호출");
				params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				params.put("sc.supplyEntrNo", apiInfo.get("INTP_ENTP_CODE").toString());
				params.put("sc.strDate", fromDate);
				params.put("sc.endDate", toDate);
				
				PaIntpOrderListVO orderConfirmList = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderListVO.class, paCode);
				
//				log.info("04.주문내역조회2 API 호출 결과=" + orderConfirmList);
				if (orderConfirmList.getOrderList() != null && orderConfirmList.getResult() != null && Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(orderConfirmList.getResult().getCode())) {
					
					List<PaIntpOrderVO> orderList = orderConfirmList.getOrderList();
					
					for (PaIntpOrderVO orderVO : orderList) {
						try {
							resultMsg = paIntpOrderService.saveOrderConfirmListTx(orderVO, paCode);
							if(!resultMsg.equals(Constants.SAVE_SUCCESS) || resultMsg == null) {
								resultCode = "400";
								sb.append(orderVO.getOrdNo() + ", ");
								continue;
							} else {
								resultCode = "200";
								resultMsg  = "OK";
							}
						} catch(Exception e) {
							resultCode = "400";
							sb.append(orderVO.getOrdNo() + ", ");
							continue;
						}
					}
					paramMap.put("code", resultCode);
					paramMap.put("message", resultMsg + " | PA_ORDER_NO : " + sb.toString());
				} else if(!Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(orderConfirmList.getResult().getCode())){
					errorMsg += paCode + " : " + orderConfirmList.getResult().getMessage() + " | ";
					paramMap.put("code", "400"); //통신 오류
					paramMap.put("message", errorMsg);
				} else {
					paramMap.put("code", "200"); //조회 내역만 없지 에러는 아님(통신 성공)
					paramMap.put("message", "OK");
				}
				continue;
			}
		}
		catch ( Exception e ) {
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(duplicateCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
			log.error(resultMsg, e);
		}
		finally {
			
			log.info(prg_id + " - 05.프로그램 중복 실행 검사 [end]");
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			log.info(prg_id + " - 06.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
		}
		
		log.info(prg_id + " - 07. SK스토아 주문내역 생성");
		orderInputMain(request);
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 인터파크 주문 연동 내역에 대한 SK스토아 주문 생성 - 배치 유형 method
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/order-input", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PAINTP_ORDER_INPUT";
		String duplicateCheck = "";
		int procCnt = 0;
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info(prg_id + " - 02.주문내역 생성 서비스 호출");
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			List<PaIntpTargetVO> orderInputTargetList = paIntpOrderService.selectOrderInputTargetList(limitCount);
			
			procCnt = orderInputTargetList.size();
			if (orderInputTargetList != null) {
				for(int i = 0; i < procCnt; i++) {
					try {
						String paOrderNo = "";
						int targetCnt = 0;
						
						PaIntpTargetVO targetVo = orderInputTargetList.get(i);
						paOrderNo = targetVo.getPaOrderNo();
						targetCnt = (int) targetVo.getTargetCnt();
						
						paIntpAsyncController.orderInputAsync(paOrderNo, targetCnt, request);
					}
					catch ( Exception e ) {
						log.error(prg_id + " - 주문 내역 생성 오류", e);
						continue;
					}
				}
			}
		}
		catch ( Exception e ) {
			log.error("error msg : " + e.getMessage());
		}
		finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			
			log.info(prg_id + " - 03.프로그램 중복 실행 검사 [End]");
		}
	}
	
	/**
	 * 발송처리 대상 조회후 배송시작 API 호출
	 * 
	 * @param request
	 * @param paOrderNo
	 * @param paOrderSeq
	 * @param searchTermGb
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody

	public ResponseEntity<?> slipOutProc(HttpServletRequest request,
			@ApiParam(name = "paOrderNo", value = "주문번호", defaultValue = "") @RequestParam(value = "paOrderNo", required = false) String paOrderNo,
			@ApiParam(name = "paOrderSeq", value = "주문순번", defaultValue = "") @RequestParam(value = "paOrderSeq", required = false) String paOrderSeq,
			@RequestParam(value="searchTermGb"	, required=false, defaultValue = "") String searchTermGb) throws Exception{

		HashMap<String, String> apiInfo = new HashMap<String, String>();
		List<Map<String,String>> targetList = new ArrayList<Map<String,String>>();
		Map<String, String> params = new HashMap<>();
		Map<String,String> targetVo = null;
		
		String prg_id = "IF_PAINTPAPI_03_003";
		String duplicateCheck = "";
		String resultCode = "200";
		String resultMsg = "";
		String paCode = "";

		String apiResultCode = "";
		String apiResultMessage = "";
		String msg = "";
		
		StringBuffer sb = new StringBuffer();
		
		int totalCnt = 0;
		int procCount = 0;
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			if(!searchTermGb.equals("1")){
				duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		    	if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});		    	
		    }
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			log.info(prg_id + " - 02.발송처리 대상 조회");
			paramMap.put("paOrderNo", paOrderNo);
			paramMap.put("paOrderSeq", paOrderSeq);
			targetList = paIntpOrderService.selectSlipOutProcList(paramMap);
			totalCnt = targetList.size();
			
			if(totalCnt < 1) {
				log.info("slipOutProc no data found. skip");
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
			
			log.info(prg_id + " - 03.배송시작 API 호출 및 발송처리 데이터 처리");
			for(int i = 0; i < totalCnt; i++) {
				targetVo = targetList.get(i);
				try {
					params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
					params.put("sc.ordclmNo", 	   targetVo.get("PA_ORDER_NO"));
					params.put("sc.ordSeq",   	   targetVo.get("PA_ORDER_SEQ"));
					params.put("sc.delvDt", 	   targetVo.get("OUT_CLOSE_DATE"));
					params.put("sc.delvEntrNo",    targetVo.get("PA_DELY_GB"));
					params.put("sc.invoNo", 	   targetVo.get("SLIP_NO"));
					params.put("sc.optPrdTp", 	   "01");
					params.put("sc.optOrdSeqList", targetVo.get("PA_ORDER_SEQ"));
					paCode = targetVo.get("PA_CODE");

					PaIntpOrderResultVO result = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderResultVO.class, paCode);
					
					if (result != null) {
						apiResultCode = result.getResult().getCode();
						apiResultMessage = result.getResult().getMessage();
					}
					
					if (Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(apiResultCode) || apiResultMessage.indexOf("이미") > -1) {
						int excuteCnt = 0;
						resultMsg = apiResultMessage;
	
						paramMap.put("code", resultCode);
						paramMap.put("message", resultMsg);

						//출고처리 성공 TPAORDERM UPDATE
						procCount++;
						excuteCnt = paIntpOrderService.updateSlipOutProcTx(targetVo);
						if(excuteCnt < 1) {
						    throw processException("errors.process", new String[] { "TPAORDERM SUCCESS-UPDATE 오류 발생" });
						}
					} else {
						int excuteCnt = 0;
						resultCode = "304";
						resultMsg = apiResultMessage;
	
						paramMap.put("code", resultCode);
						paramMap.put("message", resultMsg);
						
						Map<String,String> slipOutprocFail = new HashMap<String,String>();

						slipOutprocFail.put("paCode",   targetVo.get("PA_CODE"));
						slipOutprocFail.put("paOrdNo",  targetVo.get("PA_ORDER_NO"));
						slipOutprocFail.put("paOrdSeq", targetVo.get("PA_ORDER_SEQ"));
						slipOutprocFail.put("message",  resultMsg);

						//출고처리 실패 TPAORDERM UPDATE
						excuteCnt = paIntpOrderService.updateSlipOutProcFailTx(slipOutprocFail);
						if(excuteCnt < 1) {
						    throw processException("errors.process", new String[] { "TPAORDERM FAIL-UPDATE 오류 발생" });
						} 
					}
				} catch(Exception e) {
					log.info(targetVo.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " + e.getMessage() + "|");
					sb.append(targetVo.get("PA_SHIP_NO").toString() + ": 발송처리 fail - " + e.getMessage() + "|");
				}
			}
		} catch(Exception e) {
			msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + "|";
			paramMap.put("code"   , ("1".equals(duplicateCheck)) ? "490" : "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? msg+e.getMessage().substring(0, 3950) : msg+e.getMessage());
			log.error(msg + " : " + paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(!paramMap.getString("code").equals("500")) {
					msg = "대상건수:" + totalCnt + ", 성공건수:" + procCount + " | ";
					
					//대상건수 모두 성공하였을 경우
					if(totalCnt == procCount){
						paramMap.put("code","200");
					} else {
						paramMap.put("code","500");
					}
					paramMap.put("message", msg + sb.toString());
				}
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMessage", paramMap.getString("message"));
			systemService.insertApiTrackingTx(request, paramMap);
			} catch(Exception e) {
				log.error("ApiTracking Insert Error : " + e.getMessage());
			}
			log.info("05.프로그램 중복 실행 검사 [end, PRG_ID={}]", prg_id);
			if(!searchTermGb.equals("1")){
    		   if(duplicateCheck.equals("0")){
    			 systemService.checkCloseHistoryTx("end", prg_id);
    		   }    			 
    		}
		}
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문취소요청조회
	 * 
	 * @param fromDate 시작일자, optional, default = SYSDATE-1, format = yyyyMMddHHmmss
	 * @param toDate   종료일자, optional, default = SYSDATE,   format = yyyyMMddHHmmss
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> cancelList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request) throws Exception {
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		String prg_id= "IF_PAINTPAPI_03_005";
		String duplicateCheck = "";
		String startDate = "";
		String endDate = "";
		
		StringBuffer sb = new StringBuffer();
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		String resultCode = "200";
		String resultMsg = "OK";
		String errorCode = "200";
		String errorMsg = "";
		String paCode = "";
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
			startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			
			log.info(prg_id + " - 02.주문취소요청내역조회 및 저장 서비스 호출");
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				
				params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				params.put("sc.supplyEntrNo", apiInfo.get("INTP_ENTP_CODE").toString());
				params.put("sc.strDate", startDate);
				params.put("sc.endDate", endDate);

				PaIntpCancelReqListVO cancelListVO = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpCancelReqListVO.class, paCode);
				
//				log.info("인터파크 API 호출 결과={}" + cancelListVO);
				if (cancelListVO.getOrderList() != null && cancelListVO.getResult() != null && Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(cancelListVO.getResult().getCode())) {
					log.info("주문취소요청내역 저장");
					for (PaIntpCancelReqVO cancelVO : cancelListVO.getOrderList()) {
						try {
							//주문취소요청/철회 내역 저장
							paIntpOrderService.saveCancelRequestOrWithdrawListTx(cancelVO);
						} catch(Exception e) {
							resultCode = "400";
							resultMsg = e.getMessage();
							sb.append(cancelVO.getOrdNo() + ", ");
							continue;
						}
					}
				} else {
					errorMsg += paCode + " : " + cancelListVO.getResult().getMessage() + " | ";
				}
			}
			
		}
		catch ( Exception e ) {
			resultCode = ("1".equals(duplicateCheck) ? "490" : "500");
			resultMsg = "1".equals(duplicateCheck) ? getMessage("errors.api.duplicate") : e.getMessage();
		    log.error(resultMsg, e);
		}
		finally {
			if("400".equals(resultCode)) {
				paramMap.put("code", resultCode);
				paramMap.put("message", resultMsg +" | PA_ORDER_NO : " + sb.toString() + errorMsg);
			} else {
				paramMap.put("code", errorCode);
				paramMap.put("message", errorMsg);	
			}
			log.info(prg_id + " - 04.프로그램 중복 실행 검사 [end]");
			if("0".equals(duplicateCheck)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			
			log.info(prg_id + " - 05.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
		}
		
		log.info(prg_id + " - 06.주문취소승인 및 거부처리");
		cancelConfirmProc(request);
		
		log.info(prg_id + " - 07.취소요청내역 생성");
		cancelInputMain(request);

		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문취소요청 승인 및 거부처리
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		String prg_id = "IF_PAINTPAPI_03_006";
		String duplicateCheck = "";
		String resultCode = "200";
		String resultMsg = "OK";
		String apiResultCode = "";
		String apiResultMessage = "";
		
		int targetCnt = 0;
		int successCnt = 0;
		StringBuffer sb = new StringBuffer();
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		
			log.info(prg_id + " - 02. 인터파크 취소요청 접수 내역 조회");
			List<PaIntpCancellist> cancelList = paIntpOrderService.selectPaIntpOrderCancelList();
			
			log.info(prg_id + " - 03. 인터파크 취소요청 승인 및 거부 처리");
			if (cancelList != null && !cancelList.isEmpty()) {
				targetCnt = cancelList.size();
				for(PaIntpCancellist cancel : cancelList) {
					Map<String, String> params = new HashMap<>();
					int doFlag 		 = 0;
					String paCode 	 = cancel.getPaCode();
					String ordNo     = cancel.getPaOrderNo();
					String ordSeq    = cancel.getOrdSeq();
					String clmreqSeq = cancel.getClmreqSeq();
					String optPrdTp  = cancel.getOptPrdTp();
					
					doFlag = Integer.parseInt(cancel.getDoFlag());
					
					if(doFlag < 30) {// 취소승인대상 : DO_FLAG 30 이하
						try {
							params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
							params.put("sc.ordclmNo", ordNo);
							params.put("sc.ordSeq", ordSeq);
							params.put("sc.clmReqSeq", clmreqSeq);
							params.put("sc.optPrdTp", optPrdTp);
							params.put("sc.optOrdSeqList", ordSeq);

							PaIntpOrderResultVO result = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderResultVO.class, paCode);

							if (result != null && Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(result.getResult().getCode())) {
								String saveResultCode = paIntpOrderService.saveCancelConfirmTx(cancel, "0");
								if(saveResultCode.equals(Constants.SAVE_SUCCESS)) {
									successCnt++;
								} else {
									log.info("confirmCancelInfo insert TPAORDERM fail, PA_ORDER_NO:" + ordNo);
									sb.append("confirmCancelInfo insert TPAORDERM fail, PA_ORDER_NO:" + ordNo);
								}
							}
						} catch (Exception e) {
							log.error("취소승인대상 승인처리 오류", e);
						} 
					} else if(doFlag > 30 && StringUtils.hasText(cancel.getSlipNo())) {// 취소거부대상 : 별도의 취소거부 API 없음, 배송시작 API 호출 시 자동 거부 처리
						try {
							apiResultMessage = "취소 요청 중 배송시작처리";
							ResponseEntity<?> result = slipOutProc(request, ordNo, ordSeq, "1"); //배송시작 API 호출
							if("200".equals(String.valueOf(result.getStatusCode())) || "404".equals(PropertyUtils.describe(result.getBody()).get("code").toString())) {
								String saveResultCode = paIntpOrderService.updatePaOrdermCancelRefusalTx(ordNo, ordSeq, clmreqSeq, apiResultCode, apiResultMessage);
								if(saveResultCode.equals(Constants.SAVE_SUCCESS)) {
									successCnt++;
								} else {
									log.info("rejectCancelInfo update TPAORDERM fail, PA_ORDER_NO:" + ordNo);
									sb.append("rejectCancelInfo update TPAORDERM fail, PA_ORDER_NO:" + ordNo);
								}
							}
						} catch(Exception e) {
							log.error("취소거부대상 처리 오류", e);
						}
					}
				}
			} else {
				log.info("cancelConfirmProc no data found. skip");
				paramMap.put("code", "404");
				paramMap.put("message", getMessage("errors.no.select"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}
		catch ( Exception e ) {
			if("1".equals(duplicateCheck)) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			sb.append(e.getMessage());
		    log.error("대상건수:" + targetCnt + ", 성공건수:" + successCnt + "|");
		}
		finally {
			if (targetCnt == successCnt) {
				paramMap.put("code", resultCode);
			}
			resultMsg = "대상건수:" + targetCnt + ", 성공건수:" + successCnt + "|";
			paramMap.put("message", resultMsg);
			
			log.info(prg_id + " - 04.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
			
			log.info(prg_id + " - 05.프로그램 중복 실행 검사 [end, PRG_ID={}]", prg_id);
			if("0".equals(duplicateCheck)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주무취소 데이터 생성
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-input", method = RequestMethod.GET)
	@ResponseBody
	public void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PAINTP_CANCEL_INPUT";
		String duplicateCheck = "";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info(prg_id + " - 02.주문취소내역 생성 서비스 호출");
			List<PaIntpTargetVO> cancelInputTargetList = paIntpOrderService.selectCancelInputTargetList();
			
			if (cancelInputTargetList != null) {
				for (PaIntpTargetVO cancelTargetList : cancelInputTargetList ) {
					try {
						//외부 API 를 호출하기에 건별로 transaction을 갖도록 controller를 호출 하도록 개발됨. 개별 rollback
						paIntpAsyncController.cancelInputAsync(cancelTargetList, request);
					}
					catch ( Exception e ) {
						log.error(prg_id + " - EE.주문 취소 내역 생성 오류", e);
						continue;
					}
				}
			}
		}
		catch ( Exception e ) {
		    log.error(prg_id + " - EE.주문취소내역 생성 오류", e);
		    throw e;
		}
		finally {
			log.info(prg_id + " - 03.프로그램 중복 실행 검사 [end]");
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
	}
	
	/**
	 * 주문취소승인처리 (BO 전용)
	 * 
	 * @param ordNo		인터파크 주문번호
	 * @param ordSeq	인터파크 주문순번
	 * @param clmreqSeq	인터파크 클레임요청순번
	 * @param paCode	제휴사 코드
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-approval-proc-bo", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProc(
			@RequestParam(value = "ordNo") String ordNo,
			@RequestParam(value = "ordSeq") String ordSeq,
			@RequestParam(value = "clmreqSeq") String clmreqSeq,
			@RequestParam(value = "paCode") String paCode,
			HttpServletRequest request) throws Exception {
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		String prg_id = "IF_PAINTPAPI_03_009";
		String resultCode = "200";
		String resultMsg = "OK";
		
		PaIntpCancellist cancelApprovalList = null;
		
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
			params.put("sc.ordclmNo", ordNo);
			params.put("sc.ordSeq", ordSeq);
			params.put("sc.clmReqSeq", clmreqSeq);
			params.put("sc.optPrdTp", "01");
			params.put("sc.optOrdSeqList", ordSeq);
			
			log.info(prg_id + " - 01.인터파크 취소요청 승인 API 호출");
			PaIntpOrderResultVO procResult = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpOrderResultVO.class, paCode);
			
			if (procResult != null && 
					( Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(procResult.getResult().getCode()) ||
					"이미 주문취소 요청승인".equals(procResult.getResult().getMessage()))) {
				
				log.info(prg_id + " - 02.인터파크 취소요청 내역 조회");
				cancelApprovalList = paIntpOrderService.selectOrgItemInfoByCancelInfo(ordNo, ordSeq, clmreqSeq);
				
				log.info(prg_id + " - 03.인터파크 취소요청 승인 내역 저장");
				String saveResultCode = paIntpOrderService.saveCancelConfirmTx(cancelApprovalList, "1");
				if(!saveResultCode.equals(Constants.SAVE_SUCCESS)) {
					resultCode = "500";
					resultMsg = "[BO]취소요청 승인처리 오류";
					log.info("confirmCancelInfo insert TPAORDERM fail, PA_ORDER_NO:" + ordNo);
				}
			}
		} catch(Exception e) {
				paramMap.put("code", "500");
				paramMap.put("message", e.getMessage());
		} finally {
			paramMap.put("code", resultCode);
			paramMap.put("message", resultMsg);
			
			log.info(prg_id + " - 04.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
		}
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송완료리스트
	 * 
	 * @param fromDate 시작일자, optional, default = SYSDATE-1, format = yyyyMMddHHmmss
	 * @param toDate   종료일자, optional, default = SYSDATE,   format = yyyyMMddHHmmss
	 * @param request
	 */
	@RequestMapping(value = "/delivery-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderCompleteList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request) throws Exception {
		
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params = new HashMap<>();
		
		String prg_id = "IF_PAINTPAPI_03_008";
		String duplicateCheck = "";
		String resultCode = "200";
		String resultMsg = "";
		String paCode = "";
		String endDate = "";
		String startDate = "";
		
		int currntPagNum = 0; //응답값 : 현재 페이지
		int totPagNum = 0; // 응답값 : 총 페이지
		int pageSize = 1000; //페이지당 건수 : 최대 1000까지 가능
		endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
		startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("broadCode", Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb", Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				int pageNum  = 1; //조회하려는 페이지번호(초기 1)
				boolean existsCounsel = true;
				
				log.info(prg_id + " - 02.배송완료리스트 API 호출");
				while(existsCounsel) {
					params.put("sc.entrId", ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
					params.put("sc.supplyEntrNo", apiInfo.get("INTP_ENTP_CODE").toString());
					params.put("sc.strDate", startDate);
					params.put("sc.endDate", endDate);
					params.put("sc.pageNo", String.valueOf(pageNum));
					params.put("sc.rowCnt", String.valueOf(pageSize));
					
					PaIntpDeliveryCompleteListVO resultList = paIntpComUtil.apiGetObjectByProgramId(apiInfo, params, PaIntpDeliveryCompleteListVO.class, paCode);

					if(resultList != null) {
						resultCode   = resultList.getResult().getCode();
						resultMsg  	 = resultList.getResult().getMessage();
						if(resultList.getPageInfo() != null) {
							currntPagNum = resultList.getPageInfo().getCurrentPageNum();
							totPagNum 	 = resultList.getPageInfo().getTotPageNum();
						}
						
						if (Constants.PA_INTP_ORDER_RESULT_SUCCESS.equals(resultCode)) {// 인터파크 배송완료리스트 API 통신 성공
							if (resultList.getOrderList() != null && !resultList.getOrderList().isEmpty()) {
								log.info("03.인터파크 배송완료정보 저장");
								for (PaIntpOrderVO complete : resultList.getOrderList()) {
									paIntpOrderService.saveDeliveryComplete(complete);
								}
								if(currntPagNum != totPagNum) {
									pageNum++;
								} else {
									log.info("마지막 페이지 조회 완료");
									existsCounsel = false;
								}
							} else {
								existsCounsel = false;
							}
						} else {//인터파크 배송완료리스트 API 통신 성공, 인터파크 내 처리 오류
							existsCounsel = false;
						}
					} else { // 통신 자체 오류(time out)
						existsCounsel = false;
					}
				} //while
			}
		} catch(Exception e) {
			paramMap.put("code", "500");
			resultMsg = e.getMessage();
		} finally {
			paramMap.put("code", resultCode);
			paramMap.put("message", resultMsg);
			
			log.info(prg_id + " - 04.API trace information save 처리");
			systemService.insertApiTrackingTx(request, paramMap);
			
			log.info(prg_id + " - 05.프로그램 중복 실행 검사 [end, PRG_ID={}]", prg_id);
			if("0".equals(duplicateCheck)) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
		}
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
}
