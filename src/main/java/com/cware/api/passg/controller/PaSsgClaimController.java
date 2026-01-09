package com.cware.api.passg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaSsgCancelListVO;
import com.cware.netshopping.domain.PaSsgClaimListVO;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.passg.claim.service.PaSsgClaimService;
import com.cware.netshopping.passg.util.PaSsgComUtill;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/passg/claim", description="SSG 취소/반품/교환")
@Controller
@RequestMapping(value = "/passg/claim")
public class PaSsgClaimController extends AbstractController {
	
	@Autowired
	PaSsgConnectUtil paSsgConnectUtil;
	@Autowired
	PaSsgClaimService paSsgClaimService;
	@Autowired
	PaSsgAsyncController paSsgAsyncController;
	@Autowired
	private SystemService systemService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;

	/**
	 * 취소신청 목록조회 new
	 * @param claimStatus
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    @ApiOperation(value = "취소신청 목록조회 API", notes = "20:취소등록 / 21:취소완료", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/cancel-list/{claimStatus}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> cancelList(
			@PathVariable("claimStatus") String claimStatus,
			@ApiParam(name = "fromDate", value = "기간시작일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate,
			HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> ssgDataList = null;
		Map<String, Object> 	  map 		   = new HashMap<String, Object>();
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		Map<String, Object> requestMap	= new HashMap<String, Object>();
		String apiCode      = "";
		String paCode       = "";
		String rtnMsg		= "";
		String dateTime 	= systemService.getSysdatetimeToString();
		
		try {
			switch (claimStatus) {
				case "20": //취소등록
					apiCode = "IF_PASSGAPI_03_001";
					break;
				case "21": //취소완료
					apiCode = "IF_PASSGAPI_03_010";
					break;
				default:
					throw new Exception("claimStatus is wrong "+ claimStatus+" ip:"+ request.getHeader("X-Forwarded-For")+ " "+request.getRemoteAddr());
			}
			
			paSsgConnectUtil.getApiInfo(apiCode, apiInfoMap);
			paSsgConnectUtil.checkDuplication(apiCode, apiInfoMap);
			
			fromDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT); // 조회시작일
			toDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString(); // 조회종료일
			
			if("20".equals(claimStatus)) {
				apiInfoMap.put("url", apiInfoMap.get("url").toString().replace("{perdStrDts}", fromDate).replace("{perdEndDts}", toDate));				
			}else {				
				requestMap.put("perdStrDts", fromDate);
				requestMap.put("perdEndDts", toDate);
				apiDataMap.put("request", requestMap);
			}
			
			for(int i = 0; i < Constants.PA_SSG_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_SSG_BROAD_CODE : Constants.PA_SSG_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				rtnMsg += paCode + ":";
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				Map<String,Object> result = (Map<String,Object>)map.get("result");
				
				if(!"00".equals(String.valueOf(result.get("resultCode")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += result.get("resultMessage")+"("+result.get("resultDesc")+")/";
					continue;
				}
				
				ssgDataList = new ArrayList<Map<String, Object>>();
				
				if("20".equals(claimStatus) && result.containsKey("resultData")) {
					if(result.get("resultData") instanceof Map<?, ?>) {
						ssgDataList.add((Map<String, Object>) result.get("resultData"));
					} else {
						ssgDataList = (List<Map<String, Object>>)result.get("resultData");
					}
				}else if ("21".equals(claimStatus) && result.containsKey("data")) {
					if(result.get("resultData") instanceof Map<?, ?>) {
						ssgDataList.add((Map<String, Object>) result.get("data"));
					} else {
						ssgDataList = (List<Map<String, Object>>)result.get("data");
					}
				}
				
				if(ssgDataList.size() < 1) {
					apiInfoMap.put("code", "404");
					rtnMsg += getMessage("pa.not_exists_process_list")+"/";
					continue;
				}
				
				for(Map<String, Object> m : ssgDataList) {
					try {
						m.put("paCode", paCode);
						m.put("paOrderGb", "20");
						if("21".equals(claimStatus)) {
							m.put("procFlag",  "10");
							//m.put("outBefClaimGb", "1"); -> doFlag 확인 후 setting	
						} else {
							m.put("procFlag",  "00");
							m.put("outBefClaimGb", "0");
						}
							
						PaSsgCancelListVO vo = new PaSsgCancelListVO();
							
						//= 제휴 테이블에 데이터를 저장 (TPASSGORDERLIST, TPAORDERM) 
						vo = (PaSsgCancelListVO) PaSsgComUtill.map2VO(m, PaSsgCancelListVO.class);
							
						vo.setInsertId(Constants.PA_SSG_PROC_ID);
						vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						if("21".equals(claimStatus)) {
							paSsgClaimService.saveSsgCancelCompleteListTx(vo, claimStatus);
						} else {
							paSsgClaimService.saveSsgCancelListTx(vo, claimStatus);
						}
						
					} catch(Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "ORD_NO:" + m.get("ordNo").toString() + ",ORD_ITEM_SEQ:" + m.get("ordItemSeq").toString() + " :: " + e.getMessage()+"/";
					}
				}
			}
				
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("20".equals(claimStatus)) {
			cancelConfirmProc(request);
			cancelInputMain(request);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 취소승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "취소승인처리", notes = "취소승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		String prg_id = "IF_PASSGAPI_03_011";
		ParamMap paramMap = new ParamMap();
		int doFlag = 0;
		String resultMsg = "";
		int executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, paramMap);
			paSsgConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<Map<String, Object>> cancelList = paSsgClaimService.selectPaSsgOrderCancelList();
			
			for(Map<String, Object> cancel : cancelList) {
				try {
					doFlag = Integer.parseInt(String.valueOf(cancel.get("DO_FLAG")));
					
					if( (doFlag >= 30 && !"".equals(String.valueOf(cancel.get("SLIP_NO"))) )
							|| (Integer.parseInt(String.valueOf(cancel.get("PARTIAL_QTY"))) > 0) ) { //SSG는 출고 시 자동 취소거부됨
						paramMap.put("procFlag", "20");
						paramMap.put("message", "취소거부완료");
						paramMap.put("orordNo", cancel.get("ORORD_NO"));
						paramMap.put("orordItemSeq", cancel.get("ORORD_ITEM_SEQ"));
						
						executedRtn = paSsgClaimService.updatePaSsgCancelList(paramMap);
						if(executedRtn < 1){
							paramMap.put("code", "500");
							resultMsg += "orordNo:"+cancel.get("ORORD_NO")+" orordItemSeq:"+cancel.get("ORORD_ITEM_SEQ") +" TPAORDERM(proc_flag) UPDATE Fail/";
							paramMap.put("message", resultMsg);
						}
					} /* else if(Integer.parseInt(String.valueOf(cancel.get("PARTIAL_QTY"))) > 0 ) {
						//업체지시 상태에서 부분취소 => 출고처리 => 고객 반품 접수 가능 => 반품 아닌 취소로 만들어줘야하는 케이스 발생 가능
						paramMap.put("procFlag", "09"); //거부로 쓸 flag 값 새로 채번
						paramMap.put("message", "취소거부대기");
						paramMap.put("orordNo", cancel.get("ORORD_NO"));
						paramMap.put("orordItemSeq", cancel.get("ORORD_ITEM_SEQ"));
						
						executedRtn = paSsgClaimService.updatePaSsgCancelList(paramMap);
						if(executedRtn < 1){
							paramMap.put("code", "500");
							resultMsg += "orordNo:"+cancel.get("ORORD_NO")+" orordItemSeq:"+cancel.get("ORORD_ITEM_SEQ") +" TPAORDERM(proc_flag) UPDATE Fail/";
							paramMap.put("message", resultMsg);
						}
					}*/ else if(doFlag < 30) {
						cancel.put("OUT_BEF_CLAIM_GB", "0"); // 출고전 취소
						cancelconfirm(request, cancel); // 취소 승인
					} else {
						continue; // BO 상담원 제휴취소승인처리 프로그램에서 처리
					}
				}catch (Exception e) {
					resultMsg = resultMsg + "/" + PaSsgComUtill.getErrorMessage(e);
					paramMap.put("code"	  , "500");
					paramMap.put("message", resultMsg);
				}
			}
		} catch(Exception e) {
			paSsgConnectUtil.checkException(paramMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, paramMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 취소승인처리
	 * @param cancel
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    @ApiOperation(value = "취소승인처리", notes = "취소승인처리", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = { 
    @ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
    @ApiResponse(code = 500, message = "시스템 오류")})
	private void cancelconfirm(HttpServletRequest request, Map<String, Object> cancel) throws Exception {
		if(cancel == null) return;
		
		String prg_id 			= "IF_PASSGAPI_03_011";
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap paramMap 		= new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		int	executedRtn			= 0;
		
		paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String outClaimGb  = cancel.get("OUT_BEF_CLAIM_GB").toString();
		String paCode 	   = cancel.get("PA_CODE").toString();
		String orordNo 	   = cancel.get("ORORD_NO").toString();
		String orordItemSeq  = cancel.get("ORORD_ITEM_SEQ").toString();
		
		apiInfoMap.put("url", apiInfoMap.get("url").toString().replace("{orordNo}", orordNo).replace("{orordItemSeq}", orordItemSeq));
		
		apiInfoMap.put("paCode", paCode);
		
		paramMap.put("paCode", paCode);
		paramMap.put("orordNo", orordNo);
		paramMap.put("orordItemSeq", orordItemSeq);
		paramMap.put("outBefClaimGb", outClaimGb);
		
		map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, null);
		Map<String,Object> result = (Map<String,Object>)map.get("result");
		
		if(!"00".equals(String.valueOf(result.get("resultCode")))) {
			apiInfoMap.put("code", "500");
			apiInfoMap.put("message", result.get("resultMessage")+"("+result.get("resultDesc")+")");
			if("대상이 없습니다.".equals(ComUtil.objToStr(result.get("resultMessage")))) { //SSG에서 철회 건 주지 않아 '대상이 없다' 경우 철회 건으로 생각하고 처리
				executedRtn = paSsgClaimService.updatePaSsgCancelList4Withdraw(paramMap);
				if(executedRtn < 1){
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", "TPASSGCANCELLIST(WITHDRAW_YN) UPDATE Fail");
				}
			}
		}else {
			paramMap.put("procFlag", "10");
			paramMap.put("message", "취소승인완료");
			
			executedRtn = paSsgClaimService.updatePaSsgCancelConfirmTx(paramMap);
			
			if(executedRtn < 1){
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", "TPAORDERM(proc_flag) UPDATE Fail");
			}
		}
		
		systemService.insertApiTrackingTx(request, apiInfoMap);
	}

	/**
	 * 반품/교환회수 목록 조회  API
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    @ApiOperation(value = "반품교환회수대상조회 ", notes = "30:반품/31:반품취소/45:교환회수/46:교환취소/60:반품완료", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET) //30  반품, 31 반품취소, 45교환회수, 46 교환취소, 60반품완료
	@ResponseBody
	public ResponseEntity<ResponseMsg> claimList(HttpServletRequest request,
			@PathVariable("claimGb") String claimGb,
			@ApiParam(name = "fromDate", value = "기간시작일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate
			) throws Exception {
		
		Map<String, Object> map 				= new HashMap<String, Object>();
		List<Map<String, Object>> ssgDataList 	= null;	
		String paCode							= "";
		String prg_id 							= "";
		String rtnMsg							= "";
		ParamMap apiInfoMap						= new ParamMap();
		ParamMap apiDataMap						= new ParamMap();
		Map<String, Object> requestExchangeTarget	= new HashMap<String, Object>();
		String dateTime = systemService.getSysdatetimeToString();
		
		try {
			
			//=1) API Parameter Setting
			prg_id = getClaimApiCode(claimGb);
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			fromDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT); // 조회시작일				
			toDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString(); // 조회종료일
			
			for(int j = 0; j < Constants.PA_SSG_CONTRACT_CNT ; j++) {
				ssgDataList 	= new ArrayList<Map<String,Object>>();
				paCode = (j==0 )? Constants.PA_SSG_BROAD_CODE : Constants.PA_SSG_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				rtnMsg += paCode + ":";
				
				setClaimListRequestParam(requestExchangeTarget, claimGb, fromDate, toDate);
				apiDataMap.put("requestExchangeTarget", requestExchangeTarget);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				Map<String,Object> result = (Map<String,Object>)map.get("result");
				
				if(!"00".equals(String.valueOf(result.get("resultCode")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += result.get("resultMessage")+"("+result.get("resultDesc")+")/";
					continue;
				}
				
				List<Map<String, Object>> exchangeTargets = ((List<Map<String, Object>>)result.get("exchangeTargets"));
				if("".equals(String.valueOf(exchangeTargets.get(0)))) continue;
				
				if(exchangeTargets.get(0).containsKey("exchangeTarget")) {
					if(exchangeTargets.get(0).get("exchangeTarget") instanceof Map<?, ?>) {
						ssgDataList.add((Map<String, Object>) exchangeTargets.get(0).get("exchangeTarget"));
					} else {
						ssgDataList = (List<Map<String, Object>>)exchangeTargets.get(0).get("exchangeTarget");
					}
				}
				
				if(ssgDataList.size() < 1) {
					apiInfoMap.put("code", "404");
					rtnMsg += getMessage("pa.not_exists_process_list")+"/";
					continue;
				}	
				
				for(Map<String, Object> m : ssgDataList) {
					try {
						PaSsgClaimListVO vo = new PaSsgClaimListVO();
						
						vo = (PaSsgClaimListVO) PaSsgComUtill.map2VO(m, PaSsgClaimListVO.class);
						
						vo.setPaCode(paCode);
						vo.setPaClaimGb("60".equals(claimGb)?"30":claimGb);
						vo.setPaOrderGb("60".equals(claimGb)?"30":claimGb);
						vo.setInsertId(Constants.PA_SSG_PROC_ID);
						vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						//= 제휴 테이블에 데이터를 저장 (TPASSGCLAIMLIST, TPAORDERM) 
						paSsgClaimService.saveSsgClaimListTx(vo);
						
					} catch(Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "ORD_NO:" + m.get("ordNo").toString() + ",ORD_ITEM_SEQ:" + m.get("ordItemSeq").toString() + " :: " + e.getMessage()+"/";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		//업체지시 상태에서 부분취소 => 출고처리 후에 고객 반품 접수 가능 => 반품 아닌 취소로 만들어줘야하는 케이스 발생 가능
		orderClaimMain(request, claimGb);	// BO데이터 생성
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 회수확인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "회수확인처리 ", notes = "회수확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/recovery/confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> claimConfirm(HttpServletRequest request) throws Exception {
		
		String 		prg_id 				= "IF_PASSGAPI_03_006";
		String		paDoFlag			= "55"; //회수확인
		String 		rtnMsg				= "";
		ParamMap	apiInfoMap	 		= new ParamMap();
		ParamMap	apiDataMap	 		= new ParamMap();
		ParamMap	paramMap			= new ParamMap();
		Map<String, Object> map 		= new HashMap<String, Object>() ;
		Map<String, Object> requestConfirmRcov = new HashMap<String, Object>();
		int			executedRtn			= 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("status","confirm");
			
			List<HashMap<String, Object>> claimConfirmList = paSsgClaimService.selectPaSsgRecoveryList(paramMap);
			
			for(Map<String, Object> claimMap : claimConfirmList) {
				try {
					
					if("0000033028".equals(claimMap.get("PA_DELY_GB").toString())) { //자체배송
						requestConfirmRcov.put("wblNo", "");			//운송장번호
						requestConfirmRcov.put("delicoVenId", "");	   //택배사
						requestConfirmRcov.put("shppTypeDtlCd", "14"); //업체자사배송
					} else {
						requestConfirmRcov.put("wblNo", claimMap.get("SLIP_NO").toString());			//운송장번호
						requestConfirmRcov.put("delicoVenId", claimMap.get("PA_DELY_GB").toString());	//택배사
						requestConfirmRcov.put("shppTypeDtlCd", "22");	//업체택배배송
					}
					
					requestConfirmRcov.put("shppNo", claimMap.get("PA_SHIP_NO"));
					requestConfirmRcov.put("shppSeq", claimMap.get("PA_SHIP_SEQ"));
					requestConfirmRcov.put("procItemQty", claimMap.get("PA_PROC_QTY"));
					requestConfirmRcov.put("resellPsblYn", claimMap.get("RESELL_PSBL_YN"));
					requestConfirmRcov.put("retImptMainCd", claimMap.get("RET_IMPT_MAIN_CD"));
					
					apiInfoMap.put("paCode",claimMap.get("PA_CODE").toString());
					
					apiDataMap.put("requestConfirmRcov", requestConfirmRcov);
					
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					Map<String,Object> result = (Map<String,Object>)map.get("result");
					
					if(!"00".equals(String.valueOf(result.get("resultCode")))) {
						if(String.valueOf(result.get("resultDesc")).contains("택배사와 운송장 번호는 필수 입력값 입니다.")) { // 자체배송처리로 다시 통신
							requestConfirmRcov.put("wblNo", "");		   //운송장번호
							requestConfirmRcov.put("delicoVenId", "");	   //택배사
							requestConfirmRcov.put("shppTypeDtlCd", "14"); //업체자사배송
							apiDataMap.put("requestConfirmRcov", requestConfirmRcov);
							
							map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
							Map<String,Object> reResult = (Map<String,Object>)map.get("result");
							
							if(String.valueOf(reResult.get("resultDesc")).contains("운송장번호는 숫자만 입력하세요.")){
								requestConfirmRcov.put("wblNo", claimMap.get("SLIP_I_NO").toString());
								requestConfirmRcov.put("delicoVenId", "0000033011");
								requestConfirmRcov.put("shppTypeDtlCd", "22");
								apiDataMap.put("requestConfirmRcov", requestConfirmRcov);
								
								map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
								Map<String,Object> result2 = (Map<String,Object>)map.get("result");
								
								if(!"00".equals(String.valueOf(result2.get("resultCode")))) {
									apiInfoMap.put("code", "500");
									rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " " + result2.get("resultMessage")+"("+result2.get("resultDesc")+")/";
									continue;
								} 
							}
							
							Map<String,Object> result2 = (Map<String,Object>)map.get("result");
							
							if(!"00".equals(String.valueOf(result2.get("resultCode")))) {
								apiInfoMap.put("code", "500");
								rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " " + result2.get("resultMessage")+"("+result2.get("resultDesc")+")/";
								continue;
							} 
						} else if(String.valueOf(result.get("resultDesc")).contains("운송장번호는 숫자만 입력하세요.")){
							requestConfirmRcov.put("wblNo", claimMap.get("SLIP_I_NO").toString());		   //운송장번호
							requestConfirmRcov.put("delicoVenId", "0000033011");	   //택배사
							requestConfirmRcov.put("shppTypeDtlCd", "22");
							apiDataMap.put("requestConfirmRcov", requestConfirmRcov);
							
							map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
							Map<String,Object> result2 = (Map<String,Object>)map.get("result");
							
							if(!"00".equals(String.valueOf(result2.get("resultCode")))) {
								apiInfoMap.put("code", "500");
								rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " " + result2.get("resultMessage")+"("+result2.get("resultDesc")+")/";
								continue;
							} 
						} else {
							apiInfoMap.put("code", "500");
							rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " " + result.get("resultMessage")+"("+result.get("resultDesc")+")/";
							continue;
						}
					}
					
					executedRtn = updatePaOrderMDoFlag(claimMap, paDoFlag, "회수확인", Constants.PA_SSG_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}

				}catch (Exception e) {
					rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") +" "+PaSsgComUtill.getErrorMessage(e)+"/";
					apiInfoMap.put("code", "500");
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		//claimRefuse(request, "30"); //반품거부
		claimComplete(request);
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 회수완료처리 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "회수완료처리  ", notes = "회수완료처리 ", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/recovery/complete", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> claimComplete(HttpServletRequest request) throws Exception {
		
		String 		prg_id 				= "IF_PASSGAPI_03_007";
		String		paDoFlag			= "60"; //회수완료
		String 		rtnMsg				= "";
		ParamMap	apiInfoMap	 		= new ParamMap();
		ParamMap	apiDataMap	 		= new ParamMap();
		ParamMap	paramMap	 		= new ParamMap();		
		Map<String, Object> map 		= new HashMap<String, Object>() ;
		Map<String, Object> requestConfirmRcov = new HashMap<String, Object>();
		int			executedRtn			= 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("status","complete");
			
			List<HashMap<String, Object>> claimCompleteList = paSsgClaimService.selectPaSsgRecoveryList(paramMap);
			
			for(Map<String, Object> claimMap : claimCompleteList) {
				try {
					
					if("0000033028".equals(claimMap.get("PA_DELY_GB").toString())) { //자체배송	
						requestConfirmRcov.put("wblNo", "");			//운송장번호
						requestConfirmRcov.put("delicoVenId", "");		//택배사
						requestConfirmRcov.put("shppTypeDtlCd", "14");  //업체자사배송
					} else {
						requestConfirmRcov.put("wblNo", claimMap.get("SLIP_NO").toString());			//운송장번호
						requestConfirmRcov.put("delicoVenId", claimMap.get("PA_DELY_GB").toString());	//택배사
						requestConfirmRcov.put("shppTypeDtlCd", "22");	//업체택배배송
					}
					
					requestConfirmRcov.put("shppNo", claimMap.get("PA_SHIP_NO"));
					requestConfirmRcov.put("shppSeq", claimMap.get("PA_SHIP_SEQ"));
					requestConfirmRcov.put("procItemQty", claimMap.get("PA_PROC_QTY"));
					requestConfirmRcov.put("resellPsblYn", claimMap.get("RESELL_PSBL_YN"));
					requestConfirmRcov.put("retImptMainCd", claimMap.get("RET_IMPT_MAIN_CD"));
					
					apiInfoMap.put("paCode",claimMap.get("PA_CODE").toString());
					
					apiDataMap.put("requestConfirmRcov", requestConfirmRcov);
					
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					Map<String,Object> result = (Map<String,Object>)map.get("result");
					
					if(!"00".equals(String.valueOf(result.get("resultCode")))) {
						apiInfoMap.put("code", "500");
						rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " : " + result.get("resultMessage")+"("+result.get("resultDesc")+")/";
						continue;
					}
					
					executedRtn = updatePaOrderMDoFlag(claimMap, paDoFlag, "회수완료", Constants.PA_SSG_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}

				}catch (Exception e) {
					rtnMsg += "배송번호:"+claimMap.get("PA_SHIP_NO") + " 배송순번:"+ claimMap.get("PA_SHIP_SEQ") +" "+PaSsgComUtill.getErrorMessage(e)+"/";
					apiInfoMap.put("code", "500");
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문 취소 승인 및 거절 (BO 호출용)
	 * @param ordNo
	 * @param ordItemSeq
	 * @param procFlag
	 * @param paCode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문 취소 승인 및 거절 (BO 호출용)", notes = "주문 취소 승인 및 거절 (BO 호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-approval-proc-bo", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProcBo(
			@RequestParam(value = "orordNo", 	 	  	  required = true) String orordNo, // SSG 주문번호
			@RequestParam(value = "orordItemSeq", 	  required = true) String orordItemSeq, // SSG 주문순번
			@RequestParam(value = "procFlag", 	  	  required = true) String procFlag,
			@RequestParam(value = "paCode", 		  required = true) String paCode,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String prg_id = "IF_PASSGAPI_03_014";
		
		paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String rtnCode = "200";
		String rtnMsg = "SUCCESS";
		int executedRtn = 0;
		
		try {
			
			apiInfoMap.put("url", apiInfoMap.get("url").toString().replace("{orordNo}", orordNo).replace("{orordItemSeq}", orordItemSeq));
			apiInfoMap.put("paCode", paCode);
			
			paramMap.put("paCode",			paCode);
			paramMap.put("orordNo",			orordNo);
			paramMap.put("orordItemSeq",	orordItemSeq);
			paramMap.put("procFlag",		procFlag);
			paramMap.put("outBefClaimGb",	"1");
			
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
			Map<String,Object> result = (Map<String,Object>)map.get("result");
			
			if(!"00".equals(String.valueOf(result.get("resultCode")))) {
				if("대상이 없습니다.".equals(ComUtil.objToStr(result.get("resultMessage")))) {
					executedRtn = paSsgClaimService.updatePaSsgCancelList4Withdraw(paramMap);
					if(executedRtn < 1){
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", "TPASSGCANCELLIST(WITHDRAW_YN) UPDATE Fail");
					}
				}else {
					rtnCode = "500";
					rtnMsg  = "orordNo : " + orordNo + " orordItemSeq : "+ orordItemSeq + " :: " +  String.valueOf(result.get("resultMessage"));
					
				}
			}else {
				paramMap.put("message", "취소승인완료");
				
				executedRtn = paSsgClaimService.updatePaSsgCancelConfirmTx(paramMap);
				
				if(executedRtn < 1){
					rtnCode = "500";
					rtnMsg = "orordNo : " + orordNo + " orordItemSeq : "+ orordItemSeq + " :: " + "TPAORDERM(proc_flag) UPDATE Fail";
				}
			}
			
		}catch (Exception e) {
			rtnCode = "500";
			rtnMsg = rtnMsg + PaSsgComUtill.getErrorMessage(e);
		}finally {
			apiInfoMap.put("code"	, rtnCode);
			apiInfoMap.put("message", rtnMsg);
			systemService.insertApiTrackingTx(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 품절취소반품
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "품절취소반품", notes = "품절취소반품", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/orderSoldOut", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String 		rtnMsg				= "";
		String prg_id = "IF_PASSGAPI_03_016";
		
		try {
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("broadCode", Constants.PA_SSG_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_SSG_ONLINE_CODE);
			paramMap.put("paGroupCode", Constants.PA_SSG_GROUP_CODE);
			
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
			if(cancelList.size() > 0) {
				for(Object cancelItem : cancelList) {
					HashMap<String, String> map = new HashMap<String, String>();
					try {
						HashMap<String, String> cancelMap = (HashMap<String, String>) cancelItem;
						ParamMap	apiDataMap	 		= new ParamMap();
						apiInfoMap.put("paCode",cancelMap.get("PA_CODE"));
						map.put("PA_GROUP_CODE", Constants.PA_SSG_GROUP_CODE);
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("SITE_GB", cancelMap.get("SITE_GB"));
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN"));
						
						Map<String, Object> requestNoSellRequestRegist = new HashMap<String, Object>();
						requestNoSellRequestRegist.put("shortgRsnCd", "09"); //08 상품정보오류, 09 결품
						requestNoSellRequestRegist.put("scEvnt", "I"); //I 등록, D 삭제
						requestNoSellRequestRegist.put("shortgProcDtlc", "판매자 취소(재고부족)"); 
						requestNoSellRequestRegist.put("itemId", cancelMap.get("ITEM_ID"));
						requestNoSellRequestRegist.put("shppNo", cancelMap.get("SHPP_NO"));
						requestNoSellRequestRegist.put("shppSeq", cancelMap.get("SHPP_SEQ"));
						apiDataMap.put("requestNoSellRequestRegist", requestNoSellRequestRegist);
						
						resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
						Map<String,Object> result = (Map<String,Object>)resultMap.get("result");
						
						if("00".equals(String.valueOf(result.get("resultCode"))) || String.valueOf(result.get("resultDesc")).contains("이미 판매불가로")) {
							map.put("ORDER_CANCEL_YN", "10");
							map.put("RSLT_MESSAGE", "판매취소 성공");
						} else {
							map.put("ORDER_CANCEL_YN", "90");
							map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
							map.put("ORDER_CANCEL_RESULT_MESSAGE", String.valueOf(result.get("resultDesc")));
						}
						
					} catch (Exception e) {
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
						rtnMsg += "pa_order_no:"+map.get("PA_ORDER_NO") + " "+"판매취소 실패(api 연동 실패) "+PaSsgComUtill.getErrorMessage(e)+"/";
						apiInfoMap.put("code", "500");

					}finally {
						paOrderService.updateOrderCancelYnTx(map);
						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);

		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);

		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 모바일 자동취소 (품절취소반품)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		String rtnMsg = "";
		String prg_id = "IF_PASSGAPI_03_017";
		HashMap<String, String> map = new HashMap<String, String>();
		String resultDesc = "";
		String errMsg = "";
		
		try {
			log.info("===== SSG 모바일 자동취소 (품절취소반품) API Start =====");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("broadCode", Constants.PA_SSG_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_SSG_ONLINE_CODE);
			paramMap.put("paGroupCode", Constants.PA_SSG_GROUP_CODE);
			
			List<HashMap<String, String>> cancelList = paSsgClaimService.selectPaMobileOrderAutoCancelList(paramMap);
			
			 if(cancelList.size() > 0){
				 for (HashMap<String, String> cancelMap : cancelList) {
					try {
						ParamMap apiDataMap = new ParamMap();
						apiInfoMap.put("paCode",cancelMap.get("PA_CODE"));
						map.put("PA_GROUP_CODE", Constants.PA_SSG_GROUP_CODE);
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
						map.put("PROC_ID", Constants.PA_SSG_PROC_ID);
						
						Map<String, Object> requestNoSellRequestRegist = new HashMap<String, Object>();
						requestNoSellRequestRegist.put("shortgRsnCd", "09"); //08 상품정보오류, 09 결품
						requestNoSellRequestRegist.put("scEvnt", "I"); //I 등록, D 삭제
						requestNoSellRequestRegist.put("shortgProcDtlc", "판매자 취소(재고부족)"); 
						requestNoSellRequestRegist.put("itemId", cancelMap.get("ITEM_ID"));
						requestNoSellRequestRegist.put("shppNo", cancelMap.get("SHPP_NO"));
						requestNoSellRequestRegist.put("shppSeq", cancelMap.get("SHPP_SEQ"));
						apiDataMap.put("requestNoSellRequestRegist", requestNoSellRequestRegist);
						
						resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
						Map<String,Object> result = (Map<String,Object>)resultMap.get("result");
						resultDesc = String.valueOf(result.get("resultDesc"));
						
						if("00".equals(String.valueOf(result.get("resultCode"))) || String.valueOf(result.get("resultDesc")).contains("이미 판매불가로")) {
							map.put("REMARK3_N", "10");
							map.put("RSLT_MESSAGE", "모바일자동취소 성공");
							paOrderService.updateRemark3NTx(map);
							
							//상담생성 & 문자발송 & 상품품절처리
							paCommonService.savePaMobileOrderCancelTx(map);
							saveMobileCancellist(cancelMap);
						} else {
							// 컬럼 크기가 4000byte임으로 안전하게 3byte(한글)로 나눠서 저장
							errMsg = resultDesc.length() > 1000 ? resultDesc.substring(0, 1000) : resultDesc; 
							map.put("REMARK3_N", "90");
							map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + errMsg);
							paOrderService.updateRemark3NTx(map);
						}
						
					} catch (Exception e) {
						rtnMsg += "pa_order_no:"+map.get("PA_ORDER_NO") + " "+"판매취소 실패(api 연동 실패) "+PaSsgComUtill.getErrorMessage(e)+"/";
						apiInfoMap.put("code", "500");

						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + PaSsgComUtill.getErrorMessage(e));
						paOrderService.updateRemark3NTx(map);
					}
				}
				orderClaimMain(request, "30");	// BO데이터 생성
			}
			apiInfoMap.put("message", rtnMsg);

		}catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
			log.info("===== SSG 모바일 자동취소 (품절취소반품) API End =====");
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	private void saveMobileCancellist(HashMap<String, String> cancelMap) throws Exception {
		String dateTime = DateUtil.toDateString("yyyy-MM-dd HH:mm:ss", systemService.getDate());
		PaSsgOrderListVO paSsgOrderList = null ;
		PaSsgCancelListVO paSsgCancelList = new PaSsgCancelListVO();
		
		paSsgCancelList.setOrordNo(cancelMap.get("PA_ORDER_NO").toString());
		paSsgCancelList.setOrordItemSeq(cancelMap.get("PA_ORDER_SEQ").toString());
		
		paSsgOrderList = paSsgClaimService.selectPaSsgOrderList(paSsgCancelList);
		
		paSsgCancelList.setPaCode(cancelMap.get("PA_CODE").toString());
		paSsgCancelList.setPaOrderGb("20");
		paSsgCancelList.setOrdNo(cancelMap.get("PA_ORDER_NO").toString());
		paSsgCancelList.setOrdItemSeq(cancelMap.get("PA_ORDER_SEQ").toString());
		paSsgCancelList.setClmRsnCd("202");
		paSsgCancelList.setClmRsnNm("품절");
		paSsgCancelList.setImptDivCd("20");
		paSsgCancelList.setWithdrawYn("0");
		paSsgCancelList.setProcFlag("10");
		paSsgCancelList.setOrdItemStatCd("180"); //주문상품상태 - 주문취소
		paSsgCancelList.setOrdRcpDts(paSsgOrderList.getOrdCmplDts());
		paSsgCancelList.setOrdCnclDts(dateTime);
		paSsgCancelList.setCnclItemQty(paSsgOrderList.getDircItemQty());
		paSsgCancelList.setDircItemQty(paSsgOrderList.getDircItemQty());
		paSsgCancelList.setInsertId(Constants.PA_SSG_PROC_ID);
		paSsgCancelList.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy-MM-dd HH:mm:ss"));
		paSsgCancelList.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy-MM-dd HH:mm:ss"));
		paSsgClaimService.saveSsgCancelCompleteListTx(paSsgCancelList, "21");
		
	}

	private String getClaimApiCode(String claimGb) {
		String prg_id = "";
		
		switch (claimGb) {
		case "30"://반품
			prg_id = "IF_PASSGAPI_03_002";
			break;			
			
		case "31"://반품취소
			prg_id = "IF_PASSGAPI_03_004";
			break;

		case "45"://교환회수
			prg_id = "IF_PASSGAPI_03_003";
			break;	
		
		case "46"://교환취소
			prg_id = "IF_PASSGAPI_03_005";
			break;
		
		case "60"://반품완료
			prg_id = "IF_PASSGAPI_03_015";
			break;
		}
	
		return prg_id;
	}
	
	private void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PASSG_CANCEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"	, prg_id);
		paramMap.put("paOrderGb", "20");
		paramMap.put("siteGb"	, Constants.PA_SSG_PROC_ID);
		
		try {
			paSsgConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<HashMap<String, Object>> cancelInputTargetList = paSsgClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelTargetList : cancelInputTargetList) {
				try {
					paSsgAsyncController.cancelInputAsync(cancelTargetList, request);
				} catch(Exception e) {
					log.error(prg_id + " - EE.주문 취소 내역 생성 오류", e);
					continue;
				}
			}
		} catch(Exception e) {
			paSsgConnectUtil.checkException(paramMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, paramMap);
		}
	}
	
	private void orderClaimMain(HttpServletRequest request, String claimGb) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		String prg_id = "";
		
		switch(claimGb) {
		case "60" :
		case "30" :
			prg_id = "PASSG_ORDER_CLAIM"; 
			break;
		case "31" :
			prg_id = "PASSG_CLAIM_CANCEL";
			break;
		case "45" :
			prg_id = "PASSG_ORDER_CHANGE";
			break;
		case "46" :
			prg_id = "PASSG_CHANGE_CANCEL";
			break;
		default :
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"  , prg_id);
		paramMap.put("paOrderGb", claimGb);
		paramMap.put("siteGb"	, Constants.PA_SSG_PROC_ID);
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, paramMap);
			paSsgConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<HashMap<String, Object>> claimTargetList = paSsgClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb) || "60".equals(claimGb)) {			// 빈품
						paSsgAsyncController.orderClaimAsync(claim, request);
					} else if("31".equals(claimGb)) {	// 반취
						paSsgAsyncController.claimCancelAsync(claim, request);
					} else if("45".equals(claimGb)) {	// 교환
						paSsgAsyncController.orderChangeAsync(claim, request);
					} else if("46".equals(claimGb)) {	// 교취
						paSsgAsyncController.changeCancelAsync(claim, request);
					}
				} catch(Exception e) {
					log.error("orderClaimMainError : "+e.getMessage());
					continue;
				}
			}
			
		} catch(Exception e) {
			paSsgConnectUtil.checkException(paramMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, paramMap);
		}
	}
	
	public int updatePaOrderMDoFlag(Map<String, Object> map, String doFlag, String resultMessage, String resultCode) throws Exception {
		
		int executedRtn = 0;
		if(doFlag == null) doFlag = String.valueOf(map.get("PA_DO_FLAG")).equals("null") ? "" : map.get("PA_DO_FLAG").toString();
		
		map.put("PA_DO_FLAG", doFlag);
		map.put("API_RESULT_CODE", resultCode);
		map.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paSsgClaimService.updatePaOrderMDoFlag(map);
		
		return executedRtn;
	}
	
	private void setClaimListRequestParam(Map<String, Object> requestExchangeTarget, String claimGb, String fromDate, String toDate) {
		String perdType							= "";
		String shppDivDtlCds					= "";
		
		if("30".equals(claimGb) || "45".equals(claimGb)) {
			perdType = "01"; //회수지시일(클레임신청일)
		}else if("31".equals(claimGb) || "46".equals(claimGb)) {
			perdType = "05"; //회수철회일
		}else if("60".equals(claimGb)) {//반품완료
			perdType = "02"; //회수완료일
		}else{ //거부
			perdType = "04"; //회수확인일
		}
		
		if("30".equals(claimGb) || "31".equals(claimGb) || "60".equals(claimGb)){
			shppDivDtlCds = "21"; //반품
		}else{
			shppDivDtlCds = "22"; //교환
		}

		if("30".equals(claimGb) || "45".equals(claimGb)) {//상세배송진행상태
			requestExchangeTarget.put("shppProgStatDtlCds", "61"); //회수지시
		}else if("31".equals(claimGb) || "46".equals(claimGb)) {
			requestExchangeTarget.put("shppProgStatDtlCds", "66"); //회수철회
		}else if("60".equals(claimGb)) {
			requestExchangeTarget.put("shppProgStatDtlCds", "64"); //회수완료
		}
		
		if("31".equals(claimGb) || "46".equals(claimGb)) {
			requestExchangeTarget.put("retProcStats", "60"); //60:회수철회
		}
		
		requestExchangeTarget.put("perdType", perdType); 
		requestExchangeTarget.put("perdStrDts", fromDate);
		requestExchangeTarget.put("perdEndDts", toDate);
		requestExchangeTarget.put("shppDivDtlCds", shppDivDtlCds);
	}

}
