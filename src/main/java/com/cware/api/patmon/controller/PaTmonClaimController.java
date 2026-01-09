package com.cware.api.patmon.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.AESCrypto;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTmonCancelListVO;
import com.cware.netshopping.domain.PaTmonClaimListVO;
import com.cware.netshopping.domain.PaTmonRedeliveryListVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.patmon.claim.service.PaTmonClaimService;
import com.cware.netshopping.patmon.util.PaTmonComUtill;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/claim", description="티몬 취소/반품/교환")
@Controller
@RequestMapping(value = "/patmon/claim")
public class PaTmonClaimController extends AbstractController {
	
	@Autowired
	PaTmonConnectUtil paTmonConnectUtil;
	@Autowired
	PaTmonClaimService paTmonClaimService;
	@Autowired
	PaTmonAsyncController paTmonAsyncController;
	@Autowired
	private SystemService systemService;
	@Autowired
	private PaOrderService paOrderService;
	

	/**
	 * 취소 조회
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param searchTermGb
	 * @param page
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancel-list/{claimStatus}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> cancelList(
			@PathVariable("claimStatus") String claimStatus,
			@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> tmonDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> 	  map 		   = new HashMap<String, Object>();
		List<PaTmonCancelListVO>  paTmonCancelList = null;
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		String apiCode      = "";
		String paCode       = "";
		String rtnMsg		= "";
		String dateTime = systemService.getSysdatetimeToString();
		int page = 0;
		boolean hasNext = true;
		
		try {
			switch (claimStatus) {
			case "C1": //취소등록
				apiCode = "IF_PATMONAPI_04_001";
				break;
			case "C3": //취소완료
				apiCode = "IF_PATMONAPI_04_002";
				break;
			case "C8": //취소철회
				apiCode = "IF_PATMONAPI_04_003";
				break;
			default:
				throw new Exception("claimStatus is wrong");
			}
			
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "");
			fromDate  =  retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			paTmonConnectUtil.getApiInfo(apiCode, apiInfoMap);
			paTmonConnectUtil.checkDuplication(apiCode, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_TMON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				hasNext = true;
				page = 1;
				
				rtnMsg += paCode + "|";
				
				while(hasNext) {
					hasNext = false;
					
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{claimStatus}", claimStatus).replace("{page}", String.valueOf(page)));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
					
					if(!"null".equals(String.valueOf(map.get("error")))) {
						apiInfoMap.put("code", "500");
						rtnMsg += map.get("error")+"/";
						continue;
					}
					
					tmonDataList = (List<Map<String, Object>>)map.get("items");
					
					if(tmonDataList.size() < 1) {
						apiInfoMap.put("code", "404");
						rtnMsg += getMessage("pa.not_exists_process_list")+"/";
						continue;
					}
					
					for(Map<String, Object> m : tmonDataList) {
						try {
							paTmonCancelList = new ArrayList<PaTmonCancelListVO>();
							m.put("paCode", paCode);
							m.put("paOrderGb", "20");
							m.put("procFlag", "C3".equals(claimStatus) ? "10" : "00");
							m.put("outClaimGb", "0");
							
							PaTmonCancelListVO vo = new PaTmonCancelListVO();
							
							//= 제휴 테이블에 데이터를 저장 (TPATMONORDERLIST, TPAORDERM) 
							vo = (PaTmonCancelListVO) PaTmonComUtill.map2VO(m, PaTmonCancelListVO.class);
							
							vo.setInsertId(Constants.PA_TMON_PROC_ID);
							vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTmonCancelList = makeCancelList(vo);
							
							if("C1".equals(claimStatus) || "C3".equals(claimStatus)) {
								paTmonClaimService.saveTmonCancelListTx(paTmonCancelList);
							} else if ("C8".equals(claimStatus)) {
								paTmonClaimService.saveTmonWithdrawCancelList(paTmonCancelList);
							}
						} catch(Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_ORDER_NO : " + m.get("tmonOrderNo").toString() + " | " + e.getMessage()+"/";
						}
					}
					hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
					page++;
				}
			}		
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("C1".equals(claimStatus)) {
			cancelConfirmProc(request); // 주문승인 or 거절
			cancelInputMain(request);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	private List<PaTmonCancelListVO> makeCancelList(PaTmonCancelListVO vo) throws Exception{
		
		AESCrypto aesc = new AESCrypto();
		List<PaTmonCancelListVO> paTmonCancelListArray = new ArrayList<PaTmonCancelListVO>();
			
		vo.setUserId(aesc.decryption(vo.getUserId()).toString());
		vo.setUserName(aesc.decryption(vo.getUserName()).toString());
		
		ArrayList<Object> deals = vo.getClaimDeals();
		for(int i=0; i < deals.size(); i++) {
			Map<String, Object> dealMap	= (Map<String, Object>) deals.get(i);
			List<Map<String, Object>> dealOptions  = new ArrayList<Map<String,Object>>();
			dealOptions = (List<Map<String, Object>>)(dealMap.get("claimDealOptions"));
			
			for(int j=0; j<dealOptions.size(); j++) {
				PaTmonCancelListVO paTmonCancelList = (PaTmonCancelListVO)vo.clone();
				
				paTmonCancelList.setTmonDealNo(dealMap.get("tmonDealNo").toString());
				paTmonCancelList.setDealTitle(dealMap.get("dealTitle").toString());
				paTmonCancelList.setRequestReason(dealMap.get("requestReason").toString());
				paTmonCancelList.setRequestReasonDetail(dealMap.containsKey("requestReasonDetail")?dealMap.get("requestReasonDetail").toString():"");
				paTmonCancelList.setTmonDealOptionNo(dealOptions.get(j).get("tmonDealOptionNo").toString());
				paTmonCancelList.setDealOptionTitle(dealOptions.get(j).get("dealOptionTitle").toString());
				paTmonCancelList.setQty(ComUtil.objToLong(dealOptions.get(j).get("qty")));
				
				paTmonCancelListArray.add(paTmonCancelList);
			}
		}
		return paTmonCancelListArray;
	}
	
	/**
	 * 취소요청승인
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		String prg_id = "IF_PATMONAPI_04_005";
		ParamMap paramMap = new ParamMap();
		int doFlag = 0;
		double holdTime = 0;
		String resultMsg = "취소 실패 주문번호 목록 : ";
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, paramMap);
			paTmonConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<Map<String, Object>> cancelList = paTmonClaimService.selectPaTmonOrderCancelList();
			
			for(Map<String, Object> cancel : cancelList) {
				doFlag = Integer.parseInt(String.valueOf(cancel.get("DO_FLAG")));
				
				if(doFlag > 30 || ((BigDecimal)cancel.get("PARTIAL_QTY")).intValue() > 0) {
					try {
						cancelRefuse(request, cancel); //취소 거부
					} catch(Exception e) {
						resultMsg = resultMsg + "/" + PaTmonComUtill.getErrorMessage(e);
						paramMap.put("code"	  , "498");
						paramMap.put("message", resultMsg);
						
						continue;
					}
				} else if(doFlag < 30) {
					try {
						cancel.put("OUT_BEF_CLAIM_GB", "0"); // 출고전 취소
						cancelconfirm(request, cancel); // 취소 승인
					} catch(Exception e) {
						resultMsg = resultMsg + "/" + PaTmonComUtill.getErrorMessage(e);
						paramMap.put("code"	  , "498");
						paramMap.put("message", resultMsg);
						continue;
					}
				} else {
					//취소 요청일 + 1일 23:00 시에 일괄 환불됨.
					//취소 요청일 + 1일 22:00 시 지났으면 취소 거부처리 하기. 자체배송처리
					holdTime = ComUtil.objToDouble(cancel.get("HOLD_TIME"));
					if(holdTime > 0) {
						try {
							cancelRefuse(request, cancel); //취소 거부
						} catch(Exception e) {
							resultMsg = resultMsg + "/" + PaTmonComUtill.getErrorMessage(e);
							paramMap.put("code"	  , "498");
							paramMap.put("message", resultMsg);
							continue;
						}
					}
					
					continue; 
				}
			}
		} catch(Exception e) {
			paTmonConnectUtil.checkException(paramMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, paramMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 취소 승인
	 * @param cancel
	 * @throws Exception
	 */
	private void cancelconfirm(HttpServletRequest request, Map<String, Object> cancel) throws Exception {
		if(cancel == null) return;
		
		String prg_id = "IF_PATMONAPI_04_005";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		int	executedRtn		= 0;
		paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String outClaimGb  = cancel.get("OUT_BEF_CLAIM_GB").toString();
		String paCode 	   = cancel.get("PA_CODE").toString();
		String tmonOrderNo = cancel.get("TMON_ORDER_NO").toString();
		String claimNo 	   = cancel.get("CLAIM_NO").toString();
		
		String url = apiInfoMap.get("url").toString();
		
		apiInfoMap.put("paCode", paCode);
		apiInfoMap.put("url", url.replace("{claimNo}", claimNo));
		
		paramMap.put("paCode", paCode);
		paramMap.put("claimNo", claimNo);
		paramMap.put("tmonOrderNo", tmonOrderNo);
		paramMap.put("outBefClaimGb", outClaimGb);
		
		map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
		
		if(!"null".equals(String.valueOf(map.get("error")))){
			if(String.valueOf(map.get("error")).indexOf("완료된") > -1) {
				//완료된 취소승인 예외 처리
				paramMap.put("procFlag", "10");
				paramMap.put("message", "취소승인완료");
				
				executedRtn = paTmonClaimService.updatePaTmonCancelConfirmTx(paramMap);
				
				if(executedRtn < 1){
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", "TPAORDERM(proc_flag) UPDATE Fail");
				}
			} else {
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "TMON_CLAIM_NO : " + claimNo + " :: " +  String.valueOf(map.get("error")));				
			}
		} else {
			paramMap.put("procFlag", "10");
			paramMap.put("message", "취소승인완료");
			
			executedRtn = paTmonClaimService.updatePaTmonCancelConfirmTx(paramMap);
			
			if(executedRtn < 1){
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "TPAORDERM(proc_flag) UPDATE Fail");
			}
		}
		systemService.insertApiTrackingTx(request, apiInfoMap);
	}

	/**
	 * 취소 거부
	 * @param cancel
	 * @throws Exception
	 */
	private void cancelRefuse(HttpServletRequest request, Map<String, Object> cancel) throws Exception {
		if(cancel == null) return;
		
		String prg_id = "IF_PATMONAPI_04_006";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap bodyMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		Map<String, Object> slipMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String paCode 	   = cancel.get("PA_CODE").toString();
		String tmonOrderNo = cancel.get("TMON_ORDER_NO").toString();
		String claimNo 	   = cancel.get("CLAIM_NO").toString();
		String slipNo	   = cancel.get("SLIP_NO").toString();
		String tmonDelyCd  = cancel.get("PA_DELY_GB").toString();
		String url = apiInfoMap.get("url").toString();

		String transYn	   	 = "0";
		String transInvoice  = "";
		String transDelyGb	 = "";
		
		apiInfoMap.put("paCode"		, paCode);
		apiInfoMap.put("url"		, url.replace("{claimNo}", claimNo));
		
		paramMap.put("paCode"		, paCode);
		paramMap.put("claimNo"		, claimNo);
		paramMap.put("tmonOrderNo"	, tmonOrderNo);
		bodyMap.put("reason"		, "JCD1");	// 취소거부사유 : TCODE( O901 ) 참조
		// 취소거부사유가 JCD1 일 경우 : 송장번호와 택배사코드가 필수
		if("".equals(slipNo)) {
			//취소 접수 후 기간 지나 취소 거부처리 용도
			transDelyGb	  = "10099";
			transInvoice = dateformat.format(systemService.getSysdate());
			
			slipMap.put("deliveryCorp"	, transDelyGb);
			slipMap.put("invoiceNo"		, transInvoice);
		} else {
			transDelyGb  = tmonDelyCd;
			transInvoice = slipNo;
			
			slipMap.put("deliveryCorp"	, tmonDelyCd); // 택배사코드		
			slipMap.put("invoiceNo"		, slipNo); // 송장번호
		}
		
		bodyMap.put("deliveryInvoice", slipMap);
		map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
		
		if(!"null".equals(String.valueOf(map.get("error")))){
			if(map.get("error").toString().contains("형식에 맞지 않는 운송장번호입니다") || cancel.get("PA_DELY_GB").toString().equals("10099")) {
				slipMap = new HashMap<String, Object>();
				bodyMap = new ParamMap();
				bodyMap.put("reason", "JCD1");
				slipMap.put("deliveryCorp", "10099");
				slipMap.put("invoiceNo", dateformat.format(systemService.getSysdate()));
				bodyMap.put("deliveryInvoice", slipMap);
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
				
				transDelyGb  = "10099";
				transInvoice = dateformat.format(systemService.getSysdate());
				
				if(!"null".equals(String.valueOf(map.get("error")))) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", "TMON_CLAIM_NO : " + claimNo + " :: " +  String.valueOf(map.get("error")));
				} else {
					paramMap.put("message", "취소거부완료");
					paramMap.put("procFlag", "20");
					paTmonClaimService.updatePaTmonCancelListTx(paramMap);
					transYn = "1";
				}
			} else {
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "TMON_CLAIM_NO : " + claimNo + " :: " +  String.valueOf(map.get("error")));
			}
		} else {			
			paramMap.put("procFlag", "20");
			paramMap.put("message", "취소거부완료");
			paTmonClaimService.updatePaTmonCancelListTx(paramMap);
			transYn = "1";
		}
		
		if("1".equals(transYn)) { //TODO 리펙토링이 필요해보임..
			try {
				ParamMap claimMap = new ParamMap();
				claimMap.put("paCode"		, paCode);
				claimMap.put("paOrderNo"	, tmonOrderNo);
				claimMap.put("claimNo"		, claimNo);
				
				List<Map<String, Object>> mappingSeqList  = paTmonClaimService.selectMappingSeq(claimMap);
				
				for(Map<String, Object> mappingSeq : mappingSeqList ) {
					Map<String, Object> slipInfo = new HashMap<String, Object>();
					slipInfo.put("MAPPING_SEQ"		,  mappingSeq.get("MAPPING_SEQ"));
					slipInfo.put("PA_GROUP_CODE"	,  "09");
					slipInfo.put("PA_DELY_GB"		,  tmonDelyCd);
					slipInfo.put("INVOICE_NO"		,  slipNo);
					slipInfo.put("TRANS_PA_DELY_GB"	,  transDelyGb);
					slipInfo.put("TRANS_INVOICE_NO"	,  transInvoice);
					slipInfo.put("REMARK1_V"		, "취소거부 출고처리");
					slipInfo.put("TRANS_YN"			, transYn);
					paOrderService.insertTpaSlipInfoTx(slipInfo);				
				}
			}catch (Exception e) {
				log.error(e.toString());
			}
		}
		
		systemService.insertApiTrackingTx(request, apiInfoMap);
	}
	
	private void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PATMON_CANCEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"	, prg_id);
		paramMap.put("paOrderGb", "20");
		paramMap.put("siteGb"	, Constants.PA_TMON_PROC_ID);
		
		try {
			paTmonConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<HashMap<String, Object>> cancelInputTargetList = paTmonClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelTargetList : cancelInputTargetList) {
				try {
					paTmonAsyncController.cancelInputAsync(cancelTargetList, request);
				} catch(Exception e) {
					log.error(prg_id + " - EE.주문 취소 내역 생성 오류", e);
					continue;
				}
			}
		} catch(Exception e) {
			paTmonConnectUtil.checkException(paramMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, paramMap);
		}
	}
	
	/**
	 * 클레임 대상 수집 API
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "클레임 대상 수집", notes = "클레임 대상 수집", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET) //30  반품, 31 반품취소, 40 교환, 41 교환취소 
	@ResponseBody
	public ResponseEntity<ResponseMsg> claimList(HttpServletRequest request,
			@PathVariable("claimGb") String claimGb,
			@ApiParam(name="fromDate", 	 value="시작일", 	defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name="toDate", 	 value="종료일", 	defaultValue = "") @RequestParam(value = "toDate", required = false) String toDate
			) throws Exception {
		
		Map<String, Object> map 				= new HashMap<String, Object>() ;
		List<Map<String, Object>> tmonDataList 	= new ArrayList<Map<String,Object>>();	
		List<PaTmonClaimListVO>	paTmonClaimList	= null;
		String paCode							= "";
		String prg_id 							= "";
		String rtnMsg							= "";
		ParamMap apiInfoMap						= new ParamMap();
		String dateTime = systemService.getSysdatetimeToString();
		Boolean hasNext = false;
		int page = 0;
		
		try {
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "");
			fromDate  =  retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			//=1) API Parameter Setting
			prg_id = getClaimApiCode(claimGb);
			
			if("60".equals(claimGb)) {
				//직권환불완료건 반품생성처리
				claimGb = "30";
			}
			
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			for(int j = 0; j < Constants.PA_TMON_CONTRACT_CNT ; j++) {
				paCode = (j==0 )? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				rtnMsg += paCode + "|";
				
				hasNext = true;
				page = 1;
				
				while(hasNext) {
					hasNext = false;
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{page}", String.valueOf(page)));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
					
					if(!"null".equals(String.valueOf(map.get("error")))) {
						apiInfoMap.put("code", "500");
						rtnMsg += map.get("error")+"/";
						continue;
					}
					
					tmonDataList = (List<Map<String, Object>>)map.get("items");
					
					if(tmonDataList.size() < 1) {
						apiInfoMap.put("code", "404");
						rtnMsg += getMessage("pa.not_exists_process_list")+"/";
						continue;
					}
					
					for(Map<String, Object> m : tmonDataList) {
						try {
							PaTmonClaimListVO vo = new PaTmonClaimListVO();
							paTmonClaimList 	 = new ArrayList<PaTmonClaimListVO>();
							
							vo = (PaTmonClaimListVO) PaTmonComUtill.map2VO(m, PaTmonClaimListVO.class);
							
							vo.setPaCode(paCode);
							vo.setPaClaimGb(claimGb);
							vo.setPaOrderGb(claimGb);
							vo.setInsertId(Constants.PA_TMON_PROC_ID);
							vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTmonClaimList = makeClaimList(vo);
							
							//= 제휴 테이블에 데이터를 저장 (TPATMONCLAIMLIST, TPAORDERM) 
							paTmonClaimService.saveTmonClaimListTx(paTmonClaimList);
							
						} catch(Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "orderNo:" + m.get("tmonOrderNo").toString() + " claimNo : " + m.get("claimNo").toString() + " :: " + e.getMessage()+"/";
							log.error("claimNo : " + m.get("claimNo").toString() + " | " + e.getMessage());
						}
					}
					hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
					page++;
				}
				
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("30".equals(claimGb) || "40".equals(claimGb)) {
			returnInfoDetail(request, claimGb); //클레임 상세 정보 업데이트
		}
		orderClaimMain(request, claimGb);	// BO데이터 생성
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	

	/**
	 * 반품요청승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/return-confirm/{claimGb}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfirm(HttpServletRequest request,
			@PathVariable("claimGb") String claimGb) throws Exception {
		
		String 		prg_id 				= claimGb.equals("30") ? "IF_PATMONAPI_04_012" : "IF_PATMONAPI_04_016";
		String 		codeMsg				= claimGb.equals("30") ? "반품승인처리" : "교환완료";
		String		paDoFlag			= claimGb.equals("30") ? "60" : "80";
		ParamMap	apiInfoMap	 		= new ParamMap();
		ParamMap	apiDataMap	 		= new ParamMap();
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		String      flag				= claimGb.equals("30") ? "RETURN" : "EXCHANGE";
		String		resultMsg			= "";
		int			executedRtn			= 0;
		int			reject_cnt			= 0;
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			apiDataMap.put("flag"       , flag);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> returnConfirmList = paTmonClaimService.selectPaTmonReturnExchangeApprovalList(apiDataMap);
			
			for(Map<String, Object> returnMap : returnConfirmList) {
				try {
					if("approval".equals(returnMap.get("APPROVAL_FLAG"))) {
						Map<String, Object> returnDeliveryInvoice = new HashMap<String, Object>();
						SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
						
						apiInfoMap.put("url", url.replace("{claimNo}", returnMap.get("PA_CLAIM_NO").toString()));
						apiInfoMap.put("paCode", returnMap.get("PA_CODE"));
						
						//자체배송 :: 10099, SYSDATE
						returnDeliveryInvoice.put("deliveryCorp", returnMap.get("PA_DELY_GB").toString().equals("10099") ? "10099" : returnMap.get("PA_DELY_GB"));
						returnDeliveryInvoice.put("invoiceNo", 	  returnMap.get("PA_DELY_GB").toString().equals("10099") ? dateformat.format(systemService.getSysdate()) : returnMap.get("SLIP_NO"));
						
						if(claimGb.equals("30")) {
							apiDataMap.put("returnDeliveryInvoice", returnDeliveryInvoice);
						} else {
							apiDataMap.put("redeliveryInvoice", returnDeliveryInvoice);	// 교환 : 재배송송장
						}
			            
			            log.info(prg_id + " | 티몬" + codeMsg + " :: " + returnMap.get("PA_CLAIM_NO").toString());
			            resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
			            
			            if(!"null".equals(String.valueOf(resultMap.get("error")))){
			            	if(resultMap.get("error").toString().contains("형식에 맞지 않는 운송장번호입니다") 
			            			|| resultMap.get("error").toString().contains("택배사코드, 송장번호 모두 입력해주세요")
			            			|| resultMap.get("error").toString().contains("중복된 송장입니다")
			            			) {
			            		Map<String, Object> returnDeliveryInvoice2 = new HashMap<String, Object>();
								
								returnDeliveryInvoice2.put("deliveryCorp", "10099");
								returnDeliveryInvoice2.put("invoiceNo", dateformat.format(systemService.getSysdate()));
								
								if(claimGb.equals("30")) {
									apiDataMap.put("returnDeliveryInvoice", returnDeliveryInvoice2);
								} else {
									apiDataMap.put("redeliveryInvoice", returnDeliveryInvoice2);
								}
	
								resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
								
								if(!"null".equals(String.valueOf(resultMap.get("error")))) {
									executedRtn = updatePaOrderMDoFlag(returnMap, null, codeMsg + " 실패(자체배송) :: ("+String.valueOf(resultMap.get("error"))+")", "9999");
									resultMsg += returnMap.get("PA_CLAIM_NO").toString() +":"+ String.valueOf(resultMap.get("error"))+"/";

									apiInfoMap.put("code", "500");
								} else {
									executedRtn = updatePaOrderMDoFlag(returnMap, paDoFlag, codeMsg, Constants.PA_TMON_SUCCESS_CODE);
									if(executedRtn < 1){
										resultMsg += returnMap.get("PA_CLAIM_NO").toString() + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
										apiInfoMap.put("code", "500");
									}
								}
							} else if(resultMap.get("error").toString().contains("클레임 처리 완료된 건입니다.(C3)")) {
								executedRtn = updatePaOrderMDoFlag(returnMap, paDoFlag, "(C3)" + codeMsg, Constants.PA_TMON_SUCCESS_CODE);
								if(executedRtn < 1){
									resultMsg += returnMap.get("PA_CLAIM_NO").toString() + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
									apiInfoMap.put("code", "500");
								}
							} else {
								executedRtn = updatePaOrderMDoFlag(returnMap, null, codeMsg + " 실패 :: ("+String.valueOf(resultMap.get("error"))+")", "9999");
								resultMsg += returnMap.get("PA_CLAIM_NO").toString() +":"+ String.valueOf(resultMap.get("error"))+"/";
								apiInfoMap.put("code", "500");
							}
						} else {
							executedRtn = updatePaOrderMDoFlag(returnMap, paDoFlag, codeMsg, Constants.PA_TMON_SUCCESS_CODE);
							if(executedRtn < 1){
								resultMsg += returnMap.get("PA_CLAIM_NO").toString() + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
								apiInfoMap.put("code", "500");
							}
						} 
					}else if("reject".equals(returnMap.get("APPROVAL_FLAG"))) {
						try {
							returnRefuse(request, returnMap);
							reject_cnt++;
						}catch (Exception e) {
							resultMsg += "환불거절 실패::claim_no:"+returnMap.get("PA_CLAIM_NO").toString()+" "+PaTmonComUtill.getErrorMessage(e)+"/";
							apiInfoMap.put("code", "500");
						}
					}
				}catch (Exception e) {
					resultMsg += PaTmonComUtill.getErrorMessage(e)+"/";
					apiInfoMap.put("code", "500");
				}
			}
			
			//환불 거절 건 존재할 경우 SK테이블에 데이터 동기화
			if(reject_cnt > 0) orderClaimMain(request, "31");
			
			apiInfoMap.put("message", resultMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	private List<PaTmonClaimListVO> makeClaimList(PaTmonClaimListVO vo) throws Exception{
		
		AESCrypto aesc = new AESCrypto();
		List<PaTmonClaimListVO> paTmonClaimListArray = new ArrayList<PaTmonClaimListVO>();
					
		vo.setUserId(aesc.decryption(vo.getUserId()));
		vo.setUserName(aesc.decryption(vo.getUserName()));
		vo.setMobilePhone((vo.getReturnDeliveryMobile()!=null)?aesc.decryption(vo.getReturnDeliveryMobile()):""); //수거지 연락처
		vo.setConfirmer((vo.getConfirmer()!=null)?aesc.decryption(vo.getConfirmer()):"");
		vo.setReturnDeliveryName((vo.getReturnDeliveryName()!=null)?aesc.decryption(vo.getReturnDeliveryName()):"");
		vo.setReturnDeliveryZipCode((vo.getReturnDeliveryZipCode()!=null)?aesc.decryption(vo.getReturnDeliveryZipCode()):"");
		vo.setReturnDeliveryFirstAddress((vo.getReturnDeliveryFirstAddress()!=null)?aesc.decryption(vo.getReturnDeliveryFirstAddress()):"");
		vo.setReturnDeliverySecondAddress(vo.getReturnDeliverySecondAddress()!=null?aesc.decryption(vo.getReturnDeliverySecondAddress()):"");
		vo.setReturnDeliveryStreetAddress((vo.getReturnDeliveryStreetAddress()!=null)?aesc.decryption(vo.getReturnDeliveryStreetAddress()):"");
		
		if(vo.getOriginDeliveryInvoice() != null) {
			vo.setOriginDeliveryCorp(vo.getOriginDeliveryInvoice().get("deliveryCorp").toString());
			vo.setOriginInvoiceNo(vo.getOriginDeliveryInvoice().get("invoiceNo") != null ? vo.getOriginDeliveryInvoice().get("invoiceNo").toString() : "");
			vo.setOriginAdditionalInvoices((vo.getOriginDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getOriginDeliveryInvoice().get("additionalInvoices")):"");
		}
		if(vo.getReturnDeliveryInvoice() != null) {
			vo.setReturnDeliveryCorp(vo.getReturnDeliveryInvoice().get("deliveryCorp").toString());
			if(vo.getReturnDeliveryInvoice().containsKey("invoiceNo")) {
				vo.setReturnInvoiceNo(vo.getReturnDeliveryInvoice().get("invoiceNo") != null ? vo.getReturnDeliveryInvoice().get("invoiceNo").toString() : "");
			}
			vo.setReturnAdditionalInvoices((vo.getReturnDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getReturnDeliveryInvoice().get("additionalInvoices")):"");
		}
		if(vo.getReDeliveryInvoice() != null) {
			vo.setReDeliveryCorp(vo.getReDeliveryInvoice().get("deliveryCorp").toString());
			vo.setReInvoiceNo(vo.getReDeliveryInvoice().get("invoiceNo").toString() != null ? vo.getReDeliveryInvoice().get("invoiceNo").toString() : "");
			vo.setReAdditionalInvoices((vo.getReDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getReDeliveryInvoice().get("additionalInvoices")):"");
		}
		
		ArrayList<Object> claimDeals = vo.getClaimDeals();
		for(int i=0; i < claimDeals.size(); i++) {
			Map<String, Object> claimDealMap	= (Map<String, Object>) claimDeals.get(i);
			List<Map<String, Object>> claimDealOptions  = new ArrayList<Map<String,Object>>();
			claimDealOptions = (List<Map<String, Object>>)(claimDealMap.get("claimDealOptions"));		
			
			for(int j=0; j<claimDealOptions.size(); j++) {
				
				PaTmonClaimListVO paTmonClaimList = (PaTmonClaimListVO)vo.clone();
									
				paTmonClaimList.setTmonDealNo(claimDealMap.get("tmonDealNo").toString());
				paTmonClaimList.setDealTitle(claimDealMap.get("dealTitle").toString());
				paTmonClaimList.setRequestReason(claimDealMap.get("requestReason").toString());
				paTmonClaimList.setRequestReasonDetail(claimDealMap.containsKey("requestReasonDetail")?claimDealMap.get("requestReasonDetail").toString():"");
				
				paTmonClaimList.setTmonDealOptionNo(claimDealOptions.get(j).get("tmonDealOptionNo").toString());
				paTmonClaimList.setDealOptionTitle(claimDealOptions.get(j).get("dealOptionTitle").toString());
				paTmonClaimList.setQty(ComUtil.objToLong(claimDealOptions.get(j).get("qty")));
				
				paTmonClaimListArray.add(paTmonClaimList);
			}
		}
		
		return paTmonClaimListArray;
	}
	
	private String getClaimApiCode(String claimGb) {
		String prg_id = "";
		
		switch (claimGb) {
		case "30"://반품
			prg_id = "IF_PATMONAPI_04_008";
			break;			
			
		case "31"://반품취소
			prg_id = "IF_PATMONAPI_04_009";
			break;			
			
		case "40"://교환
			prg_id = "IF_PATMONAPI_04_010";
			break;			

		case "41"://교환취소
			prg_id = "IF_PATMONAPI_04_011";
			break;
		
		case "60"://반품
			prg_id = "IF_PATMONAPI_04_023";
			break;	
		}
		return prg_id;
	}
	
	private void orderClaimMain(HttpServletRequest request, String claimGb) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		String prg_id = "";
		
		switch(claimGb) {
		case "30" :
			prg_id = "PATMON_ORDER_CLAIM"; 
			break;
		case "31" :
			prg_id = "PATMON_CLAIM_CANCEL";
			break;
		case "40" :
			prg_id = "PATMON_ORDER_CHANGE";
			break;
		case "41" :
			prg_id = "PATMON_CHANGE_CANCEL";
			break;
		default :
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"  , prg_id);
		paramMap.put("paOrderGb", claimGb);
		paramMap.put("siteGb"	, Constants.PA_TMON_PROC_ID);
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, paramMap);
			paTmonConnectUtil.checkDuplication(prg_id, paramMap);
			
			log.info("========================= 티몬 Order Claim (" + claimGb + ") START =========================");
			List<HashMap<String, Object>> claimTargetList = paTmonClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb)) {			// 빈품
						paTmonAsyncController.orderClaimAsync(claim, request);
					} else if("31".equals(claimGb)) {	// 반취
						paTmonAsyncController.claimCancelAsync(claim, request);
					} else if("40".equals(claimGb)) {	// 교환
						paTmonAsyncController.orderChangeAsync(claim, request);
					} else if("41".equals(claimGb)) {	// 교취
						paTmonAsyncController.changeCancelAsync(claim, request);
					}
				} catch(Exception e) {
					log.error("orderClaimMainError : "+e.getMessage());
					continue;
				}
			}
			
			log.info("========================= 티몬 Order Claim (" + claimGb + ") END =========================");
		} catch(Exception e) {
			paTmonConnectUtil.checkException(paramMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, paramMap);
		}
	}
	
	/**
	 * 환불 상세 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "환불 상세 조회", notes = "환불 상세 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/return-detail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnInfoDetail(HttpServletRequest request, String claimGb) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "";
		String				executedRtn		= "";	
		String				rtnMsg			= "";
		
		try {
			prg_id = "30".equals(claimGb) ? "IF_PATMONAPI_04_013" : "IF_PATMONAPI_04_018";
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> claimConfirmlist =  paTmonClaimService.selectClaimDtTargetList(claimGb);
			for(int i=0; i<claimConfirmlist.size(); i++) {
				try {
					apiInfoMap.put("paCode",claimConfirmlist.get(i).get("PA_CODE").toString());
					apiInfoMap.put("url",url.replace("{claimNo}", claimConfirmlist.get(i).get("PA_CLAIM_NO").toString()));
					
					resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
					
					if(!"null".equals(String.valueOf(resultMap.get("error")))){
						apiInfoMap.put("code", "500");
						rtnMsg += claimConfirmlist.get(i).get("PA_CLAIM_NO").toString() + ":" + String.valueOf(resultMap.get("error"))+"/";
					} else {
						resultMap.put("paCode", claimConfirmlist.get(i).get("PA_CODE").toString());
						resultMap.put("paOrderGb", claimGb);
						resultMap.put("claimNo", claimConfirmlist.get(i).get("PA_CLAIM_NO").toString());
						
						PaTmonClaimListVO vo 	= new PaTmonClaimListVO();
						vo  = (PaTmonClaimListVO) PaTmonComUtill.map2VO(resultMap, PaTmonClaimListVO.class);
						
						executedRtn = paTmonClaimService.updatePaTmonClaimListDetailTx(vo);
							
						if(!executedRtn.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code", "500");
							rtnMsg += claimConfirmlist.get(i).get("PA_CLAIM_NO").toString() + ": TPAORDERM(pa_do_flag) UPDATE Fail"+"/";
						}
					}
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += claimConfirmlist.get(i).get("PA_CLAIM_NO").toString()+":"+e.getMessage()+"/";
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 환불 거부
	 * @param returnMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void returnRefuse(HttpServletRequest request, Map<String, Object> returnMap) throws Exception {
		if(returnMap == null) return;
		
		String prg_id = "IF_PATMONAPI_04_014";
		String rtnMsg = "";
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> compChkMap = new HashMap<String, Object>();
		paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String paCode 	 = returnMap.get("PA_CODE").toString();
		String paOrderNo = returnMap.get("PA_ORDER_NO").toString();
		String paOrderGb = returnMap.get("PA_ORDER_GB").toString();
		String paClaimNo = returnMap.get("PA_CLAIM_NO").toString();
		String url = apiInfoMap.get("url").toString();
		
		apiInfoMap.put("paCode", paCode);
		apiInfoMap.put("url", url.replace("{claimNo}", paClaimNo));
		
		paramMap.put("paCode", paCode);
		paramMap.put("paOrderNo", paOrderNo);
		paramMap.put("paOrderGb", paOrderGb);
		paramMap.put("paClaimNo", paClaimNo);
		apiDataMap.put("reason", "JRD5");	// 클레임거부사유 : 반품상품 미도착
		apiDataMap.put("reasonDetail", "반품상품 미도착으로 인한 거절");	 //상세 사유 10~50자 내외
		
		map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
		
		if(!"null".equals(String.valueOf(map.get("error")))) {
			
			if(map.get("error").toString().contains("완료된")) {
				List<Map<String, Object>> claimStatusList = new ArrayList<Map<String, Object>>();
				
				prg_id = "IF_PATMONAPI_04_024";
				paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
				
				url = apiInfoMap.get("url").toString();
				apiInfoMap.put("url", url.replace("{tmonOrderNo}", paOrderNo));
				
				compChkMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
				
				claimStatusList = (List<Map<String, Object>>)(compChkMap.get("Data"));
				for(Map<String, Object> claimStatusData : claimStatusList) {
					if(claimStatusData.get("claimStatus").equals("C3") && claimStatusData.get("claimNo").equals(paClaimNo)) {
						int executedRtn = 0;
						
						claimStatusData.put("PA_CLAIM_NO", paClaimNo);
						claimStatusData.put("PA_CODE", paCode);
						claimStatusData.put("PA_ORDER_NO", paOrderNo);
						claimStatusData.put("PA_ORDER_GB", "30");
						
						executedRtn = updatePaOrderMDoFlag(claimStatusData, "60", "회수확정", Constants.PA_TMON_SUCCESS_CODE);
						if(executedRtn < 1){
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_CLAIM_NO :" + paClaimNo + " TPAORDERM(pa_do_flag) UPDATE Fail/";
						}
					}
					apiInfoMap.put("message", rtnMsg);
				}
				
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "TMON_CLAIM_NO : " + paClaimNo + " :: " +  String.valueOf(map.get("error")));
			}
		} else {			
			paramMap.put("rejectYn", "1");
			paramMap.put("message", "환불거부 성공(7일 자동환불 거부)");
			paTmonClaimService.updatePaTmonReturnListTx(paramMap);
		}
		systemService.insertApiTrackingTx(request, apiInfoMap);
	}

	/**
	 * 교환 반송장 등록
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/return-invoice", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnInvoice(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap bodyMap	= null;
		String prg_id = "IF_PATMONAPI_04_015";
		String rtnMsg = "";
		int executedRtn = 0;
		
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> returnInvoiceTargetList = paTmonClaimService.selectReturnInvoiceTargetList();
			
			for(Map<String, Object> returnInvoiceTarget : returnInvoiceTargetList) {
				bodyMap = new ParamMap();
				
				apiInfoMap.put("paCode", returnInvoiceTarget.get("PA_CODE"));
				apiInfoMap.put("url", url.replace("{claimNo}", returnInvoiceTarget.get("PA_CLAIM_NO").toString()));
				
				bodyMap.put("deliveryCorp", returnInvoiceTarget.get("PA_DELY_GB"));
				bodyMap.put("invoiceNo", returnInvoiceTarget.get("PA_DELY_GB").equals("10099") ? dateformat.format(systemService.getSysdate()) : returnInvoiceTarget.get("SLIP_NO"));
				
				resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
				
				if(!"null".equals(String.valueOf(resultMap.get("error")))){
					if(resultMap.get("error").toString().contains("형식에 맞지 않는 운송장번호입니다")) {
						bodyMap = new ParamMap();
						bodyMap.put("deliveryCorp", "10099");
						bodyMap.put("invoiceNo", dateformat.format(systemService.getSysdate()));
						resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
						if(!"null".equals(String.valueOf(resultMap.get("error")))) {
							executedRtn = updatePaOrderMDoFlag(returnInvoiceTarget, null, "회수확정 실패", "8000");
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_CLAIM_NO : " + returnInvoiceTarget.get("PA_CLAIM_NO").toString() + " :: " +  String.valueOf(resultMap.get("error"))+"/";
						} else {
							executedRtn = updatePaOrderMDoFlag(returnInvoiceTarget, "60", "회수확정", Constants.PA_TMON_SUCCESS_CODE);
							if(executedRtn < 1){
								apiInfoMap.put("code", "500");
								rtnMsg += "TMON_CLAIM_NO:" + returnInvoiceTarget.get("PA_CLAIM_NO").toString()+" TPAORDERM(pa_do_flag) UPDATE Fail/";
							}
						}
					} else {
						executedRtn = updatePaOrderMDoFlag(returnInvoiceTarget, null, "회수확정 실패", "9999");
						apiInfoMap.put("code", "500");
						rtnMsg += "TMON_CLAIM_NO : " + returnInvoiceTarget.get("PA_CLAIM_NO").toString() + " :: " +  String.valueOf(resultMap.get("error"))+"/";
					}
				} else {
					executedRtn = updatePaOrderMDoFlag(returnInvoiceTarget, "60", "회수확정", Constants.PA_TMON_SUCCESS_CODE);
					if(executedRtn < 1){
						apiInfoMap.put("code", "500");
						rtnMsg += "TMON_CLAIM_NO:" + returnInvoiceTarget.get("PA_CLAIM_NO").toString()+" TPAORDERM(pa_do_flag) UPDATE Fail/";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 교환 거부 처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exchange-reject-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeRejectProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap bodyMap	= null;
		String prg_id = "";
		int executedRtn = 0;
		
		prg_id = "IF_PATMONAPI_04_017";
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> exchangeRefuseList = paTmonClaimService.selectPaTmonExchangeRefuseList();
			
			for(Map<String, Object> exchangeRefuseMap : exchangeRefuseList) {
				bodyMap = new ParamMap();
				
				apiInfoMap.put("paCode", exchangeRefuseMap.get("PA_CODE"));
				apiDataMap.put("paCode", exchangeRefuseMap.get("PA_CODE"));
				apiDataMap.put("tmonOrderNo", exchangeRefuseMap.get("PA_ORDER_NO").toString());
				apiDataMap.put("tmonClaimNo", exchangeRefuseMap.get("PA_CLAIM_NO").toString());
				apiInfoMap.put("url", url.replace("{claimNo}", exchangeRefuseMap.get("PA_CLAIM_NO").toString()));
				
				bodyMap.put("reason", "JXD3"); //재고부족으로 교환불가
				bodyMap.put("reasonDetail", "재고부족으로 인한 교환불가");
				
				resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
				
				if(!"null".equals(String.valueOf(resultMap.get("error")))){
					apiInfoMap.put("code"			, "500");
					apiInfoMap.put("message"		, "TMON_CLAIM_NO : " + exchangeRefuseMap.get("PA_CLAIM_NO").toString() + " :: " +  String.valueOf(resultMap.get("error")));
					apiDataMap.put("preCancelReason", resultMap.get("error"));
					apiDataMap.put("preCancelYn"	, "0");
				} else {
					apiDataMap.put("preCancelReason", "교환 거부 성공");
					apiDataMap.put("preCancelYn", "1");
				}
				executedRtn = paTmonClaimService.updatePaOrderM4ChangeRefualReuslt(apiDataMap);
				if(executedRtn < 1) throw processException("errors.process", new String[] {  "교환거부처리 UPDATE 오류 발생" });
			}
		} catch(Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private int updatePaOrderMDoFlag(Map<String, Object> paorderm, String doFlag, String resultMessage, String resultCode) throws Exception {
		
		int executedRtn = 0;
		if(doFlag == null) doFlag = String.valueOf(paorderm.get("PA_DO_FLAG")).equals("null") ? "" : paorderm.get("PA_DO_FLAG").toString();
		
		paorderm.put("PA_DO_FLAG", doFlag);
		paorderm.put("API_RESULT_CODE", resultCode);
		paorderm.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paTmonClaimService.updatePaOrderMDoFlag(paorderm);
		
		return executedRtn;
	}
	
	/**
	 * 주문 취소 승인 및 거절 (BO 호출용)
	 * @param claimNo
	 * @param tmonOrderNo
	 * @param deliveryNo
	 * @param tmonDealOprionNo
	 * @param procFlag
	 * @param paCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel-approval-proc-bo", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(
			@RequestParam(value = "claimNo", 	 	  required = true) String claimNo, // 티몬 클레임 번호
			@RequestParam(value = "tmonOrderNo", 	  required = true) String tmonOrderNo, // 티몬 주문 번호
			@RequestParam(value = "procFlag", 	  	  required = true) String procFlag,
			@RequestParam(value = "paCode", 		  required = true) String paCode,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String prg_id = "IF_PATMONAPI_04_019";
		
		paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String rtnCode = "200";
		String rtnMsg = "SUCCESS";
		int executedRtn = 0;
		
		try {
			String url = apiInfoMap.get("url").toString();
			apiInfoMap.put("paCode", paCode);
			apiInfoMap.put("url", url.replace("{claimNo}", claimNo));
			
			paramMap.put("paCode",			paCode);
			paramMap.put("claimNo",			claimNo);
			paramMap.put("tmonOrderNo",		tmonOrderNo);
			paramMap.put("procFlag",		procFlag);
			paramMap.put("outBefClaimGb",	"1");
			
			map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
			
			if(!"null".equals(String.valueOf(map.get("error")))) {
				if(String.valueOf(map.get("error")).indexOf("완료된") > -1) {
					//완료된 취소승인 예외 처리
					paramMap.put("procFlag", "10");
					paramMap.put("message", "취소승인완료");
					
					executedRtn = paTmonClaimService.updatePaTmonCancelConfirmTx(paramMap);
					
					if(executedRtn < 1) {
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", "TPAORDERM(proc_flag) UPDATE Fail");
					}
				} else {
					rtnCode = "500";
					rtnMsg = "TMON_CLAIM_NO : " + claimNo + " :: " +  String.valueOf(map.get("error"));
				}
			} else {
				paramMap.put("message", "취소승인완료");
				
				executedRtn = paTmonClaimService.updatePaTmonCancelConfirmTx(paramMap);
				
				if(executedRtn < 1){
					rtnCode = "500";
					rtnMsg = "TPAORDERM(proc_flag) UPDATE Fail";
				}
			}
			
		}catch (Exception e) {
			rtnCode = "500";
			rtnMsg = rtnMsg + PaTmonComUtill.getErrorMessage(e);
		}finally {
			apiInfoMap.put("code"	, rtnCode);
			apiInfoMap.put("message", rtnMsg);
			systemService.insertApiTrackingTx(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 교환보류설정/해제
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exchange-hold-proc/{status}", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> exchangeHoldProc(
			@PathVariable("status") String status,
			HttpServletRequest request) throws Exception {
		
		StringBuffer sb		= new StringBuffer();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap bodyMap = null;
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		Map<String, Object> returnDeliveryInvoice = null;
		Map<String, Object> subItems = null;
		List<Object> items = null;
		String prg_id = status.equals("hold") ? "IF_PATMONAPI_04_020" : "IF_PATMONAPI_04_021"; // hold : 보류설정 ,  release
		
		int executedRtn = 0;
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.getString("url").toString();
			
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
			try {
				apiInfoMap.put("flag", status.equals("hold") ? "HOLD" : "RELEASE");
				List<Map<String, Object>> exchangeHoldProcList = paTmonClaimService.selectPaTmonExchangeHoldProcList(apiInfoMap);
				
				if(exchangeHoldProcList.size() < 1) {
					apiInfoMap.put("code", "404");
					apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
					return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				}
				
				for(Map<String, Object> exchangeHoldProc : exchangeHoldProcList) {
					apiInfoMap.put("url", url.replace("{claimNo}", exchangeHoldProc.get("CLAIM_NO").toString()));
					apiInfoMap.put("paCode", exchangeHoldProc.get("PA_CODE").toString());
					
					if(status.equals("hold")) { // 보류 설정
						returnDeliveryInvoice = new HashMap<String, Object>();
						subItems 			  = new HashMap<String, Object>();
						bodyMap 			  = new ParamMap();
						items 				  = new ArrayList<Object>();
						returnDeliveryInvoice.put("deliveryCorp", exchangeHoldProc.get("PA_DELY_GB").equals("") ? "10099" : exchangeHoldProc.get("PA_DELY_GB")); // 태배사코드
						returnDeliveryInvoice.put("invoiceNo", returnDeliveryInvoice.get("deliveryCorp").equals("10099") ? dateformat.format(systemService.getSysdate()) : exchangeHoldProc.get("SLIP_NO"));
						
						bodyMap.put("returnDeliveryInvoice", returnDeliveryInvoice);
						
						subItems.put("tmonDealNo", exchangeHoldProc.get("TMON_DEAL_NO").toString());
						subItems.put("reason", "HXD7"); // 교환 진행중
						subItems.put("reasonDetail", "교환 진행중인 상품입니다."); // 교환보류 상세사유(10자 이상 50자 이내)
						
						items.add(subItems);
						
						bodyMap.put("items", items);
						
						resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, bodyMap);
					} else if(status.equals("release")) { // 보류 해제
						items = new ArrayList<Object>();
						items.add(exchangeHoldProc.get("TMON_DEAL_NO"));
						resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, items);
					}
					
					if(!"null".equals(String.valueOf(resultMap.get("error")))) {
						exchangeHoldProc.put("API_RESULT_CODE", "9999");
						exchangeHoldProc.put("API_RESULT_MESSAGE", resultMap.get("error").toString());
						exchangeHoldProc.put("HOLD_YN", status.equals("hold") ? "0" : "1");
						sb.append("PA_ORDER_NO : " + exchangeHoldProc.get("TMON_ORDER_NO") + " | " + String.valueOf(resultMap.get("error")) + " | ");
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", sb.toString());
					} else {
						exchangeHoldProc.put("API_RESULT_CODE", Constants.PA_TMON_SUCCESS_CODE);
						exchangeHoldProc.put("API_RESULT_MESSAGE", status.equals("hold") ? "교환보류설정 성공" : "교환보류해제 성공");
					}
					executedRtn = paTmonClaimService.updatePaTmonHoldYnTx(exchangeHoldProc);
					if(executedRtn < 1){
						sb.append("TPAORDERM(pa_hold_yn) UPDATE Fail | TMON_ORDER_NO :: " + exchangeHoldProc.get("TMON_ORDER_NO") + ", ");
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", sb.toString());
					}
				}
			} catch(Exception e) {
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "Fail : " + prg_id + " | " + e.getMessage());
				log.error("Fail : " + prg_id + " | " + e.getMessage());
			}
		} catch(Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 재배송 대상 수집
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/redelivery-list", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> redeliveryList(
			@RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> tmonDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> 	  map 		   = new HashMap<String, Object>();
		List<PaTmonRedeliveryListVO> paTmonReDeliverylList = null;
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		String apiCode      = "IF_PATMONAPI_04_022";
		String paCode       = "";
		String dateTime = systemService.getSysdatetimeToString();
		int page = 0;
		boolean hasNext = true;
		
		try {
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "");
			fromDate = retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			paTmonConnectUtil.getApiInfo(apiCode, apiInfoMap);
			paTmonConnectUtil.checkDuplication(apiCode, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			for(int i = 0; i < Constants.PA_TMON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				hasNext = true;
				page = 1;
				
				while(hasNext) {
					hasNext = false;
					
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{page}", String.valueOf(page)));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
					
					if(!"null".equals(String.valueOf(map.get("error")))) {
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", map.get("error"));
						continue;
					}
					
					tmonDataList = (List<Map<String, Object>>)map.get("items");
					
					if(tmonDataList.size() < 1) {
						apiInfoMap.put("code", "404");
						apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
						continue;
					}
					
					for(Map<String, Object> m : tmonDataList) {
						try {
							PaTmonRedeliveryListVO vo = new PaTmonRedeliveryListVO();
							paTmonReDeliverylList = new ArrayList<PaTmonRedeliveryListVO>();
							
							//= 제휴 테이블에 데이터를 저장 (TPATMONORDERLIST, TPAORDERM) 
							vo = (PaTmonRedeliveryListVO) PaTmonComUtill.map2VO(m, PaTmonRedeliveryListVO.class);
							
							vo.setPaCode(paCode);
							vo.setInsertId(Constants.PA_TMON_PROC_ID);
							vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTmonReDeliverylList = makeRedeliveryList(vo);
							
							paTmonClaimService.saveTmonRedelivaryListTx(paTmonReDeliverylList);
						} catch(Exception e) {
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", "TMON_ORDER_NO : " + m.get("tmonOrderNo").toString() + e.getMessage());
							log.error("claimNo : " + m.get("claimNo").toString() + " | " + e.getMessage());
						}
					}
					hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
					page++;
				}
			}			
		} catch(Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	private List<PaTmonRedeliveryListVO> makeRedeliveryList(PaTmonRedeliveryListVO vo) throws Exception{
		
		AESCrypto aesc = new AESCrypto();
		List<PaTmonRedeliveryListVO> paTmonRedeliveryListArray = new ArrayList<PaTmonRedeliveryListVO>();
					
		vo.setUserId(aesc.decryption(vo.getUserId()));
		vo.setUserName(aesc.decryption(vo.getUserName()));
		vo.setConfirmer((vo.getConfirmer()!=null)?aesc.decryption(vo.getConfirmer()):"");
		vo.setReturnDeliveryName((vo.getReturnDeliveryName()!=null)?aesc.decryption(vo.getReturnDeliveryName()):"");
		vo.setReturnDeliveryZipCode((vo.getReturnDeliveryZipCode()!=null)?aesc.decryption(vo.getReturnDeliveryZipCode()):"");
		vo.setReturnDeliveryFirstAddress((vo.getReturnDeliveryFirstAddress()!=null)?aesc.decryption(vo.getReturnDeliveryFirstAddress()):"");
		vo.setReturnDeliverySecondAddress(vo.getReturnDeliverySecondAddress()!=null?aesc.decryption(vo.getReturnDeliverySecondAddress()):"");
		vo.setReturnDeliveryStreetAddress((vo.getReturnDeliveryStreetAddress()!=null)?aesc.decryption(vo.getReturnDeliveryStreetAddress()):"");
		
		if(vo.getOriginDeliveryInvoice() != null) {
			vo.setOriginDeliveryCorp(vo.getOriginDeliveryInvoice().get("deliveryCorp").toString());
			vo.setOriginInvoiceNo(vo.getOriginDeliveryInvoice().get("invoiceNo") != null ? vo.getOriginDeliveryInvoice().get("invoiceNo").toString() : "");
			vo.setOriginAdditionalInvoices((vo.getOriginDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getOriginDeliveryInvoice().get("additionalInvoices")):"");
		}
		if(vo.getReturnDeliveryInvoice() != null) {
			vo.setReturnDeliveryCorp(vo.getReturnDeliveryInvoice().get("deliveryCorp").toString());
			if(vo.getReturnDeliveryInvoice().containsKey("invoiceNo")) {
				vo.setReturnInvoiceNo(vo.getReturnDeliveryInvoice().get("invoiceNo") != null ? vo.getReturnDeliveryInvoice().get("invoiceNo").toString() : "");
			}
			vo.setReturnAdditionalInvoices((vo.getReturnDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getReturnDeliveryInvoice().get("additionalInvoices")):"");
		}
		if(vo.getReDeliveryInvoice() != null) {
			vo.setReDeliveryCorp(vo.getReDeliveryInvoice().get("deliveryCorp").toString());
			vo.setReInvoiceNo(vo.getReDeliveryInvoice().get("invoiceNo").toString() != null ? vo.getReDeliveryInvoice().get("invoiceNo").toString() : "");
			vo.setReAdditionalInvoices((vo.getReDeliveryInvoice().get("additionalInvoices") != null)?ComUtil.getArrayToString((String[])vo.getReDeliveryInvoice().get("additionalInvoices")):"");
		}
		
		ArrayList<Object> reDeliveryDeals = vo.getClaimDeals();
		for(int i=0; i < reDeliveryDeals.size(); i++) {
			Map<String, Object> reDeliveryDealMap	= (Map<String, Object>) reDeliveryDeals.get(i);
			List<Map<String, Object>> reDeliveryDealOptions  = new ArrayList<Map<String,Object>>();
			reDeliveryDealOptions = (List<Map<String, Object>>)(reDeliveryDealMap.get("claimDealOptions"));		
			
			for(int j=0; j<reDeliveryDealOptions.size(); j++) {
				
				PaTmonRedeliveryListVO paTmonRedeliveryList = (PaTmonRedeliveryListVO)vo.clone();
									
				paTmonRedeliveryList.setTmonDealNo(reDeliveryDealMap.get("tmonDealNo").toString());
				paTmonRedeliveryList.setDealTitle(reDeliveryDealMap.get("dealTitle").toString());
				paTmonRedeliveryList.setRequestReason(reDeliveryDealMap.get("requestReason").toString());
				paTmonRedeliveryList.setRequestReasonDetail(reDeliveryDealMap.containsKey("requestReasonDetail")?reDeliveryDealMap.get("requestReasonDetail").toString():"");
				
				paTmonRedeliveryList.setTmonDealOptionNo(reDeliveryDealOptions.get(j).get("tmonDealOptionNo").toString());
				paTmonRedeliveryList.setDealOptionTitle(reDeliveryDealOptions.get(j).get("dealOptionTitle").toString());
				paTmonRedeliveryList.setQty(ComUtil.objToLong(reDeliveryDealOptions.get(j).get("qty")));
				
				paTmonRedeliveryListArray.add(paTmonRedeliveryList);
			}
		}
		
		return paTmonRedeliveryListArray;
	}
	
	/**
	 * 요청일자별 반품철회데이터 조회 API
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "요청일자별 반품철회데이터 조회", notes = "요청일자별 반품철회데이터 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/return-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> returnWithdrawList(HttpServletRequest request) throws Exception {
		
		Map<String, Object> map 				= new HashMap<String, Object>() ;
		List<Map<String, Object>> tmonDataList 	= new ArrayList<Map<String,Object>>();	
		List<PaTmonClaimListVO>	paTmonClaimList	= null;
		String paCode							= "";
		String rtnMsg							= "";
		String fromDate 						= "";
		String toDate 							= "";
		ParamMap apiInfoMap						= new ParamMap();
		String dateTime = systemService.getSysdatetimeToString();
		Boolean hasNext = false;
		int page = 0;
		
		try {
			String prg_id = "IF_PATMONAPI_04_025";
			
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			// 반품철회 확인 필요한 요청일자 조회
			List<String> dateList = paTmonClaimService.selectPaTmonReturnRequestedDateList();
			
			for(String date : dateList) {
				fromDate = date.substring(0,10) + "T" + "00:00:00";
				toDate   = date.substring(0,10) + "T" + "23:59:59";
				
				for(int j = 0; j < Constants.PA_TMON_CONTRACT_CNT ; j++) {
					paCode = (j==0 )? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
					apiInfoMap.put("paCode", paCode);
					
					hasNext = true;
					page = 1;
					
					while(hasNext) {
						hasNext = false;
						apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{page}", String.valueOf(page)));
						
						map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
						
						if(!"null".equals(String.valueOf(map.get("error")))) {
							apiInfoMap.put("code", "500");
							rtnMsg += paCode + "|" + map.get("error")+"/";
							continue;
						}
						
						tmonDataList = (List<Map<String, Object>>)map.get("items");
						
						if(tmonDataList.size() < 1) {
							apiInfoMap.put("code", "404");
							rtnMsg += paCode + "|" + getMessage("pa.not_exists_process_list")+"/";
							continue;
						}
						
						for(Map<String, Object> m : tmonDataList) {
							try {
								PaTmonClaimListVO vo = new PaTmonClaimListVO();
								paTmonClaimList 	 = new ArrayList<PaTmonClaimListVO>();
								
								vo = (PaTmonClaimListVO) PaTmonComUtill.map2VO(m, PaTmonClaimListVO.class);
								
								vo.setPaCode(paCode);
								vo.setPaClaimGb("31");
								vo.setPaOrderGb("31");
								vo.setInsertId(Constants.PA_TMON_PROC_ID);
								vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paTmonClaimList = makeClaimList(vo);
								
								//= 제휴 테이블에 데이터를 저장 (TPATMONCLAIMLIST, TPAORDERM) 
								paTmonClaimService.saveTmonClaimListTx(paTmonClaimList);
								
							} catch(Exception e) {
								apiInfoMap.put("code", "500");
								rtnMsg += "orderNo:" + m.get("tmonOrderNo").toString() + " claimNo : " + m.get("claimNo").toString() + " :: " + e.getMessage()+"/";
								log.error("claimNo : " + m.get("claimNo").toString() + " | " + e.getMessage());
							}
						}
						hasNext = "false".equals(map.get("hasNext").toString()) ? false : true;	
						page++;
					}
					
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderClaimMain(request, "31");	// BO데이터 생성
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
