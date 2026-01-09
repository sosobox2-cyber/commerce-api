package com.cware.api.pahalf.controller;

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
import com.cware.netshopping.pahalf.claim.service.PaHalfClaimService;
import com.cware.netshopping.pahalf.order.service.PaHalfOrderService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/pahalf/claim", description="하프클럽 CS")
@Controller("com.cware.api.pahalf.PaHalfClaimController")
@RequestMapping(value="/pahalf/claim")
public class PaHalfClaimController extends AbstractController{

	@Resource(name = "common.system.systemService")
	public SystemService systemService;
	 
	@Resource(name = "com.cware.api.pahalf.PaHalfAsyncController")
	public PaHalfAsyncController asyncController;
	
	@Resource(name = "pahalf.claim.paHalfClaimService")
	public PaHalfClaimService paHalfClaimService;
	
	@Resource(name = "pahalf.order.paHalfOrderService")
	private PaHalfOrderService paHalfOrderService;
	
	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;
	/**
	 * 하프클럽 CLAIM 내역 조회 (반품/교환)
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 CLAIM 내역 조회 (반품/교환)", notes = "하프클럽 CLAIM 내역 조회 (반품/교환)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> claimlList(
			@PathVariable("claimGb") String claimGb
		  , @RequestParam(value = "fromDate", required = false) String fromDate
		  , @RequestParam(value = "toDate", required = false) String toDate
		  , HttpServletRequest request
			) throws Exception {
		
		log.info("===== 하프클럽 클레임 내역 조회 (CLAIM_GB : {})API Start =====",claimGb);
		String endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		String startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateAsString() , -1, DateUtil.GENERAL_DATE_FORMAT);
		String prg_id 			= "";
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		String	paCode			= "";
		String  errorMsg		= "";
		String paClaimGb 		= "";
		Map<String, Object> resultMap			= null; 
		List<Map<String, Object>> dataList  	= null;
		
		
		try {		
			// =Step 0) API ClaimGb SETTING 
			switch (claimGb) {
			case "30":
				paClaimGb = "refund";
				prg_id	  = "IF_PAHALFAPI_04_002";
				break;
			case "31":
				paClaimGb = "rc";
				prg_id	  = "IF_PAHALFAPI_04_003";
				break;
			case "40":
				paClaimGb = "exchange";
				prg_id	  = "IF_PAHALFAPI_04_004";
				break;
			case "41":
				paClaimGb = "ec";		
				prg_id	  = "IF_PAHALFAPI_04_005";
				break;
			default:
				throw processException("msg.cannot_date", new String[] { " CLAIM_GB :: " + paClaimGb});
			}		
		
		
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
						
			// =Step 3) Parameter Setting
			apiDataMap.put("fromYMD"	, PaHalfComUtill.makeHalfDateFormat(startDate));
			apiDataMap.put("toYMD"		, PaHalfComUtill.makeHalfDateFormat(endDate));
			apiDataMap.put("siteCd"		, "1");
			apiDataMap.put("deliState"	, paClaimGb);
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
			
			//=Step 4) 하프클럽 통신 및 데이터 저장(TPAHALFORDERLIST, TPAORDERM)
			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				//ㄴ1.통신
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				
				//ㄴ2.실패 Tracking 처리
				if (!"200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) {
					errorMsg = errorMsg +"(" + paCode + ")" + PaHalfComUtill.getApiResult(resultMap).get("message");
					apiInfoMap.put("code"	, PaHalfComUtill.getApiResult(resultMap).get("code"));
					apiInfoMap.put("message", errorMsg);
					continue;//실패건은 스킵
				}
				//ㄴ3.INSERT TPAHALFORDERLIST, TPAORDERM
				dataList= (List<Map<String, Object>>)PaHalfComUtill.getApiData(resultMap, "result");
				savePaHalfClaim(dataList, claimGb, paCode);
			}
						
		}catch (Exception e) {
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderClaimMain(request, claimGb); //BO 데이터 생성
		log.info("===== 하프클럽 클레임 내역 조회 (CLAIM_GB : {})API End =====",claimGb);
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void savePaHalfClaim(List<Map<String, Object>> cancelList, String claimGb, String paCode) {
		for(Map<String, Object> cancel : cancelList) {
			try {
				cancel.put("paOrderGb"	, claimGb);
				cancel.put("paCode"		, paCode);
				paHalfClaimService.savePaHalfClaimTx(cancel);
			}catch (Exception e) {
				log.info("{} : {} 제휴주문번호: {}, paCode: {}, paOrderGb: {}" , "하프클럽 클레임 저장 오류", PaHalfComUtill.getErrorMessage(e),cancel.get("ordNo"),cancel.get("paCode"),cancel.get("paOrderGb"));
				continue;
			}
		}
	}
	
	public void orderClaimMain(HttpServletRequest request, String claimGb) throws Exception {
		
		ParamMap paramMap 	= new ParamMap();
		String   prg_id		= "";

		switch (claimGb) {		
		case "30":
			prg_id = "PAHALF_ORDER_CLAIM";
			break;
		case "31":
			prg_id = "PAHALF_CLAIM_CANCEL";
			break;
		case "40":
			prg_id = "PAHALF_ORDER_CHANGE";
			break;
		case "41":
			prg_id = "PAHALF_CHANGE_CANCEL";
			break;
		default :	
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paOrderGb"	, claimGb);
		paramMap.put("siteGb"		, Constants.PA_HALF_PROC_ID);
		
		try {
			
			paHalfConnectUtil.checkDuplication(prg_id, paramMap);
			paHalfConnectUtil.getApiInfo(prg_id, paramMap);
			
			// 2) Claim Target 추출
			List<HashMap<String, Object>> claimTargetList = paHalfOrderService.selectClaimTargetList(paramMap);
			
			for( HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb)) { //반품
						asyncController.orderClaimAsync(claim, request);
					}else if("31".equals(claimGb)) { //반취
						asyncController.claimCancelAsync(claim, request);
					}else if("40".equals(claimGb)) { //교환
						asyncController.orderChangeAsync(claim, request);
					}else if("41".equals(claimGb)) { //교취
						asyncController.changeCancelAsync(claim, request);	
					}
				}catch (Exception e) {
					log.info("{} : {} 주문번호: {}","하프클럽 클레임 생성 오류", PaHalfComUtill.getErrorMessage(e),claim.get("ORDER_NO"));
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","하프클럽 클레임 생성 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(paramMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, paramMap);	
		}
	}
	 
	
	/**
	 * 하프클럽 교환회수처리
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 교환회수처리", notes = "하프클럽 교환회수처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-proc", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> exchangeProc(HttpServletRequest request) throws Exception {		
			 
		log.info("===== 하프클럽 교환 회수 처리 API Start =====");

		String prg_id 					 = "IF_PAHALFAPI_03_005";
		ParamMap apiInfoMap				 = new ParamMap();
		Map<String, Object> resultMap	 = null; 
		Map<String, String> apiResultMap = null;
		List<Map<String,Object>> exchangeCompleteList = null;

		List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
		
		int totalCnt = 0;
		int failCnt  = 0;
		
		try {
			
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			exchangeCompleteList = paHalfClaimService.selectExchangeCompleteList();
			totalCnt = exchangeCompleteList.size();
			for(Map<String,Object> ec : exchangeCompleteList) {		
				try {
					apiInfoMap.put("paCode", ec.get("PA_CODE"));
					ec = (Map<String, Object>) PaHalfComUtill.replaceCamel(ec);
					tempList.clear();
					
					Map<String,Object> exchangeComplete = new HashMap<String, Object>();
					exchangeComplete.put("ordNo"    , ec.get("ordNo"));
					exchangeComplete.put("ordNoNm"  , ec.get("ordNoNm"));
					exchangeComplete.put("confirmYN", ec.get("confirmYN"));
					exchangeComplete.put("stateCd"  , ec.get("stateCd"));
					tempList.add(exchangeComplete);
					
					//통신
					resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, tempList);
					apiResultMap = PaHalfComUtill.getApiResult(resultMap);
					
					if (!"200".equals(apiResultMap.get("code"))) {
						throw processException("errors.process", new String[] { apiResultMap.get("message" ) });
					}
					ec.put("paDoFlag", "60");
					ec.put("message", "교환 회수 완료");
				
				}catch (Exception e) {
					failCnt++;
					ec.put("paDoFlag", "");
					ec.put("message",  PaHalfComUtill.getErrorMessage(e));
					log.info("{} : {} 주문번호: {}", "하프클럽 교환 회수 처리 오류", PaHalfComUtill.getErrorMessage(e),ec.get("ORDER_NO"));
				}
				paHalfClaimService.updatePaOrderm(ec);
			}
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 교환 회수 처리 - 실패 : " + failCnt + "건 전체 : " + totalCnt + "건"});
			
		} catch (Exception e) {
			log.info("{} : {}", "하프클럽 교환 회수 처리 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("===== 하프클럽 교환 회수 처리 API End =====");

		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	

	/**
	 * 반품처리
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품처리", notes = "반품처리", httpMethod = "GET", produces = "반품처리")
	@RequestMapping(value = "/refund-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> refundProc(HttpServletRequest request) throws Exception {
		
        log.info("===== 하프클럽 반품처리 API Start =====");
		String prg_id 					 = "IF_PAHALFAPI_03_004";
		ParamMap apiInfoMap 			 = new ParamMap();
		Map<String, Object> resultMap 	 = null;
		Map<String, String> apiResultMap = null;
		Map<String, String> apiDataMap = null;

		String code 	= "";
		String exceptionMessage = "";

		List<Map<String, Object>> apiDataList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> returnApprovalList = null;
		
		int totalCnt = 0;
		int failCnt  = 0;
		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			returnApprovalList = paHalfClaimService.selectReturnApprovalList();
			PaHalfComUtill.replaceCamelList(returnApprovalList);
			totalCnt = returnApprovalList.size();
			for(Map<String, Object> returnApproval : returnApprovalList) {
				try {
					
	                apiDataList.clear();
	                
	                Map<String,Object> returnApprovalMap = new HashMap<String, Object>();
	                returnApprovalMap.put("ordNo"    , returnApproval.get("ordNo"));
	                returnApprovalMap.put("ordNoNm"  , returnApproval.get("ordNoNm"));
	                returnApprovalMap.put("confirmYN", returnApproval.get("confirmYN"));
	                returnApprovalMap.put("stateCd"  , returnApproval.get("stateCd"));
					apiDataList.add(returnApprovalMap);
					
					//통신
					apiInfoMap.put("paCode", String.valueOf(returnApproval.get("paCode")));
					resultMap 	 = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList);
					apiResultMap = PaHalfComUtill.getApiResult(resultMap);
					apiDataMap   = PaHalfComUtill.getApiData(resultMap);
					
					//후처리
					code 	= apiResultMap.get("code");
					exceptionMessage = apiDataMap.get("exceptionMessage");
					exceptionMessage = (exceptionMessage == null) ? "" : exceptionMessage;
					
					if("200".equals(code) || exceptionMessage.replaceAll(" ", "").contains("이미회수처리")){
						returnApproval.put("paDoFlag", "60");
						returnApproval.put("message", "반품 승인 처리");

					}else {
						throw processException("errors.process", new String[] { apiResultMap.get("message" ) });
					}
					
				} catch (Exception e) {
					failCnt++;
					log.info("{} : {} 주문번호: {}","하프클럽 반품 처리 오류", PaHalfComUtill.getErrorMessage(e),returnApproval.get("orderNo"));
					returnApproval.put("paDoFlag", "");
					returnApproval.put("message", PaHalfComUtill.getErrorMessage(e));						
				}
				paHalfClaimService.updatePaOrderm(returnApproval);
			}
			
			if(failCnt > 0) throw processException("errors.detail", new String[] {"하프클럽 반품 처리 - 실패 : " + failCnt + "건 전체 : " + totalCnt + "건"});
			
		}
		catch (Exception e) {
			log.info("{} : {}","하프클럽 반품 처리 오류", PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		
		log.info("===== 하프클럽 반품처리 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

}
