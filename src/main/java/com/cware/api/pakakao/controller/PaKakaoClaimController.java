package com.cware.api.pakakao.controller;

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
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pakakao.claim.service.PaKakaoClaimService;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/pakakao/claim", description="카카오 취소/반품/교환")
@Controller("com.cware.api.pakakao.PaKakaoClaimController")
@RequestMapping(value = "/pakakao/claim")
public class PaKakaoClaimController extends AbstractController {
	
	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	@Autowired
	private SystemService systemService;
	@Autowired
	private PaKakaoClaimService paKakaoClaimService;
	@Autowired
	private PaOrderService paOrderService; 
	@Autowired
	private PaKakaoAsyncController paKakaoAsyncController;
	@Autowired
	private PaKakaoDeliveryController paKakaoDeliveryController;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@ApiOperation(value = "취소 승인/거부 처리", notes = "취소 승인/거부 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public void cancelConfirmProc(HttpServletRequest request) throws Exception {
		
		String prg_id     = "IF_PAKAKAOAPI_04_001";
		ParamMap paramMap = new ParamMap();
		int doFlag 		  = 0;
		String resultMsg  = "";
		double holdTime   = 0;
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, paramMap);
			paKakaoConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<Map<String, Object>> cancelList = paKakaoClaimService.selectPaKakaoOrderCancelList();
			
			for(Map<String, Object> cancel : cancelList) {
				try {
					doFlag = Integer.parseInt(String.valueOf(cancel.get("DO_FLAG")));
					
					if(doFlag > 30 || "07".equals(cancel.get("PROC_FLAG").toString())) { 
						cancelRefuse(request, cancel); //취소 거부
					} else if(doFlag < 30) {
						cancelconfirm(request, cancel); // 취소 승인
					}else {
						//취소 요청일 + 5일 23:00 시 지났으면 취소승인
						holdTime = ComUtil.objToDouble(cancel.get("HOLD_TIME"));
						if(holdTime > 0) {
							cancelconfirm(request, cancel); //취소 승인
						}
					}
				}catch (Exception e) {
					resultMsg = resultMsg + "/" + PaKakaoComUtill.getErrorMessage(e);
					paramMap.put("code"	  , "500");
					paramMap.put("message", resultMsg);
				}
			}
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(paramMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, paramMap);
		}
		
	}
	
	@ApiOperation(value = "취소승인처리", notes = "취소승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	private void cancelconfirm(HttpServletRequest request, Map<String, Object> cancel) throws Exception {
		
		String prg_id 			   = "IF_PAKAKAOAPI_04_001";
		ParamMap apiInfoMap 	   = new ParamMap();
		ParamMap apiDataMap		   = new ParamMap();
		ParamMap errorMap		   = null;
		ParamMap paramMap 		   = new ParamMap();
		Map<String, Object> map    = new HashMap<String, Object>();
		ArrayList<String> orderIds = new ArrayList<String>();
		String rtnMsg			   = "";
		
		try {
			if(cancel == null) return;
			 
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			String paCode = cancel.get("PA_CODE").toString();
			String claimId = cancel.get("CLAIM_ID").toString();
			String id = cancel.get("ID").toString();
			
			apiInfoMap.put("paCode", paCode);
			
			apiDataMap.put("claimId" , claimId);
			orderIds.add(id);
			apiDataMap.put("orderIds", orderIds);
			
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) || !"200".equals(ComUtil.objToStr(map.get("code")))) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				
				apiInfoMap.put("code", "500");
				rtnMsg += "claimId" + ":" + claimId + " " + errorMap.get("errorMsg").toString() + " ";
				
			} else {
				paramMap.put("paCode", paCode);
				paramMap.put("claimId", claimId);
				paramMap.put("orderIds", id);
				paramMap.put("procFlag", "10");
				paramMap.put("message", "취소승인완료");
				
				paKakaoClaimService.updatePaKakaoCancelConfirmTx(paramMap);
			}
			
			apiInfoMap.put("message", rtnMsg);
			
		}catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			systemService.insertApiTrackingTx(request, apiInfoMap);
		}
	}
	
	@ApiOperation(value = "취소거부처리", notes = "취소거부처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	private void cancelRefuse(HttpServletRequest request, Map<String, Object> cancel) throws Exception {
		
		String prg_id 			   = "IF_PAKAKAOAPI_04_002";
		ParamMap apiInfoMap 	   = new ParamMap();
		ParamMap apiDataMap 	   = new ParamMap();
		ParamMap errorMap		   = null;
		ParamMap paramMap 		   = new ParamMap();
		Map<String, Object> map    = new HashMap<String, Object>();
		ArrayList<String> orderIds = new ArrayList<String>();
		
		try {
			
			if(cancel == null) return;
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode"		, cancel.get("PA_CODE").toString());
			
			paramMap.put("paCode"		, cancel.get("PA_CODE").toString());
			paramMap.put("claimId"		, cancel.get("CLAIM_ID").toString());
			paramMap.put("orderIds"		, cancel.get("ID").toString());
			
			apiDataMap.put("claimId"	, cancel.get("CLAIM_ID").toString());			
			orderIds.add(cancel.get("ID").toString());
			apiDataMap.put("orderIds"	, orderIds);
			
			if("DIRECT".equals(ComUtil.objToStr(cancel.get("PA_DELY_GB"))) || "".equals(ComUtil.objToStr(cancel.get("SLIP_NO")))) {
				apiDataMap.put("shippingMethod", "DIRECT"); 					
			} else {
				apiDataMap.put("shippingMethod"		  , "SHIPPING");
				apiDataMap.put("deliveryCompanyCode"  , cancel.get("PA_DELY_GB").toString());
				apiDataMap.put("invoiceNumber"		  , cancel.get("SLIP_NO").toString());
			}
			
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if( !"200".equals(ComUtil.objToStr(map.get("statusCode")))) {
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				
				if(errorMap.get("errorMsg").toString().indexOf("송장번호") == -1) {
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", errorMap.get("errorMsg").toString());
				} else {
					apiDataMap 	   = new ParamMap();
					apiDataMap.put("claimId"	, cancel.get("CLAIM_ID").toString());			
					apiDataMap.put("orderIds"	, orderIds);
					apiDataMap.put("shippingMethod", "DIRECT"); 
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", cancel.get("CLAIM_ID").toString() + " " + errorMap.get("errorMsg").toString());
					} else {
						paramMap.put("procFlag", "20");
						paramMap.put("message" , "취소거부완료");
						paKakaoClaimService.updatePaKakaoCancelListTx(paramMap);
						
						cancel.put("INVOICE_NO"	,  "DIRECT");
						cancel.put("TRANS_PA_DELY_GB"	,  "DIRECT");
						cancel.put("TRANS_INVOICE_NO"	,  "DIRECT");
						cancel.put("REMARK1_V"			, "취소거부 출고처리");
						cancel.put("TRANS_YN"			, "1");
						cancel.put("PA_GROUP_CODE"	,  "11");
						paOrderService.insertTpaSlipInfoTx(cancel);
					}
				}
				
			} else {
				paramMap.put("procFlag", "20");
				paramMap.put("message" , "취소거부완료");
				paKakaoClaimService.updatePaKakaoCancelListTx(paramMap);
				
				if("DIRECT".equals(apiDataMap.get("shippingMethod").toString())) {
					cancel.put("INVOICE_NO"	,  "DIRECT");
					cancel.put("TRANS_PA_DELY_GB"	,  "DIRECT");
					cancel.put("TRANS_INVOICE_NO"	,  "DIRECT");
				}else {
					cancel.put("INVOICE_NO"	,  cancel.get("SLIP_NO").toString());
					cancel.put("TRANS_PA_DELY_GB"	,  cancel.get("PA_DELY_GB").toString());
					cancel.put("TRANS_INVOICE_NO"	,  cancel.get("SLIP_NO").toString());
				}
				cancel.put("REMARK1_V"		, "취소거부 출고처리");
				cancel.put("TRANS_YN"		, "1");
				cancel.put("PA_GROUP_CODE"	,  "11");
				paOrderService.insertTpaSlipInfoTx(cancel);
			}
			
		}catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	
	//자체배송의 경우 회수 송장 입력 없이 바로 수거완료 처리
	@ApiOperation(value = "회수 송장 입력", notes = "회수 송장 입력", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/claim-invoice-insert/{claimGb}", method = RequestMethod.GET)   
	@ResponseBody
	public ResponseEntity<ResponseMsg> claimInvoiceInsert( 
			@PathVariable("claimGb") String claimGb, 		 //30:반품, 45:교환회수
			HttpServletRequest request) throws Exception {
		
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap		= null;
		ParamMap errorMap		= null;
		ParamMap paramMap		= new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> orderIds = null;
		String rtnMsg			= "";
		String prg_id 			= "30".equals(claimGb)?"IF_PAKAKAOAPI_04_004":"IF_PAKAKAOAPI_04_007";
		int    executedRtn		= 0;
		String paDoFlag			= "56";
		String apiResultMsg		= "회수송장입력";
		
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("claimGb", claimGb);
			List<Map<String, Object>> claimList = paKakaoClaimService.selectPaKakaoClaimInvTgList(paramMap);
			
			for(Map<String, Object> claim : claimList) {
				try {
					orderIds 	= new ArrayList<String>();
					orderIds.add(claim.get("PA_ORDER_SEQ").toString());
					
					apiInfoMap.put("paCode", claim.get("PA_CODE").toString());
					
					apiDataMap = new ParamMap();
					apiDataMap.put("claimId", claim.get("PA_CLAIM_NO").toString());
					apiDataMap.put("orderIds", orderIds);
					apiDataMap.put("deliveryCompanyCode", claim.get("PA_DELY_GB").toString());
					apiDataMap.put("invoiceNumber", claim.get("SLIP_NO").toString());
					
					if(!"DIRECT".equals(claim.get("PA_DELY_GB").toString()) && "0".equals(claim.get("PA_HOLD_YN").toString())) {
						map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
						
						if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
							
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							
							if(errorMap.get("errorMsg").toString().indexOf("유효한 송장번호") == -1) {
								apiInfoMap.put("code", "500");
								rtnMsg += claim.get("PA_CLAIM_NO").toString() + " " + errorMap.get("errorMsg").toString() + " ";
								continue;
							}
						}
					}
					
					executedRtn = updatePaOrderMDoFlag(claim, paDoFlag, apiResultMsg, Constants.PA_KAKAO_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO") + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}
					
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO")+ " " + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		claimCollectComplete(claimGb, request);
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "클레임 수거 완료", notes = "클레임 수거 완료", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@ResponseBody
	private ResponseEntity<ResponseMsg> claimCollectComplete(String claimGb, HttpServletRequest request) throws Exception { //30:반품, 45:교환회수
		
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		ParamMap errorMap		= null;
		ParamMap paramMap		= new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> orderIds = null;
		String rtnMsg			= "";
		String prg_id 			= "30".equals(claimGb)?"IF_PAKAKAOAPI_04_005":"IF_PAKAKAOAPI_04_008";
		int    executedRtn		= 0;
		String paCode	        = "";
		String paClaimNo	    = "";
		String paOrderSeq       = "";
		String paDoFlag			= "";
		String apiResultMsg		= "수거완료";
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("claimGb", claimGb);
			List<Map<String, Object>> claimList = paKakaoClaimService.selectPaKakaoClaimCollCmpTgList(paramMap);
			
			for(Map<String, Object> claim : claimList) {
				try {
					paCode	    = ComUtil.objToStr(claim.get("PA_CODE"));
					paClaimNo	= ComUtil.objToStr(claim.get("PA_CLAIM_NO"));
					paOrderSeq   = ComUtil.objToStr(claim.get("PA_ORDER_SEQ"));
					orderIds 	= new ArrayList<String>();
					orderIds.add(paOrderSeq);
					
					apiInfoMap.put("paCode"  , paCode);
					
					apiDataMap.put("claimId" , paClaimNo);
					apiDataMap.put("orderIds", orderIds);
					
					if("0".equals(claim.get("PA_HOLD_YN").toString())) {
						map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
							
						if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) || !"200".equals(ComUtil.objToStr(map.get("code")))) {
								
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							rtnMsg += "paClaimNo" + ":" + paClaimNo + " " + errorMap.get("errorMsg").toString() + " ";
								
							continue;
						}
					}
					
					paDoFlag = "30".equals(claimGb)?"59":"60";
					
					executedRtn = updatePaOrderMDoFlag(claim, paDoFlag, apiResultMsg, Constants.PA_KAKAO_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "claimId:"+paClaimNo + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}
					
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "claimId:"+paClaimNo+ " " + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("30".equals(claimGb)){
			returnComplete(request);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "반품 승인", notes = "반품 승인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@ResponseBody
	private ResponseEntity<ResponseMsg> returnComplete(HttpServletRequest request) throws Exception { //30:반품, 45:교환회수
		
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		ParamMap errorMap		= null;
		Map<String, Object> map = new HashMap<String, Object>();
		String rtnMsg			= "";
		String prg_id 			= "IF_PAKAKAOAPI_04_006";
		String paDoFlag			= "60";
		int    executedRtn		= 0;
		String paCode	        = "";
		String paClaimNo	    = "";
		String paOrderSeq        = "";
		ArrayList<String > orderIds = null;
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			List<Map<String, Object>> claimList = paKakaoClaimService.selectPaKakaoReturnCmpTgList();
			
			for(Map<String, Object> claim : claimList) {
				try {
					paCode	   = ComUtil.objToStr(claim.get("PA_CODE"));
					paClaimNo  = ComUtil.objToStr(claim.get("PA_CLAIM_NO"));
					paOrderSeq  = ComUtil.objToStr(claim.get("PA_ORDER_SEQ"));
					orderIds 	= new ArrayList<String>();
					orderIds.add(paOrderSeq);
					
					apiInfoMap.put("paCode"  , paCode);
					
					apiDataMap.put("claimId" , paClaimNo);
					apiDataMap.put("orderIds", orderIds);
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) || !"200".equals(ComUtil.objToStr(map.get("code")))) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						rtnMsg += "paClaimNo" + ":" + paClaimNo + " " + errorMap.get("errorMsg").toString() + " ";
						
						continue;
					}
					
					executedRtn = updatePaOrderMDoFlag(claim, paDoFlag, "반품완료", Constants.PA_KAKAO_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "claimId:"+paClaimNo + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}
					
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "claimId:"+paClaimNo+ " " + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "교환 재발송", notes = "교환 재발송", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/exchange-slip-out-proc", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> exchangeSlipOutProc(HttpServletRequest request) throws Exception {
		
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap		= null;
		ParamMap errorMap		= null;
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> orderIds = null;
		String rtnMsg			= "";
		String prg_id 			= "IF_PAKAKAOAPI_04_009";
		int    executedRtn		= 0;
		
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			List<Map<String, Object>> claimList = paKakaoClaimService.selectPaKakaoExchangeSlipOutList();
			
			for(Map<String, Object> claim : claimList) {
				try {

					apiInfoMap.put("paCode", claim.get("PA_CODE").toString());
					apiDataMap = new ParamMap();
					
					apiDataMap.put("claimId", claim.get("PA_CLAIM_NO").toString());

					orderIds 	= new ArrayList<String>();
					orderIds.add(claim.get("PA_ORDER_SEQ").toString());
					apiDataMap.put("orderIds", orderIds);
					
					if("DIRECT".equals(claim.get("PA_DELY_GB").toString())) { //자체배송 처리 필요
						apiDataMap.put("shippingMethod", "DIRECT"); //직접배송
					}else {
						apiDataMap.put("shippingMethod", "SHIPPING"); //택배배송
						apiDataMap.put("deliveryCompanyCode", claim.get("PA_DELY_GB").toString());
						apiDataMap.put("invoiceNumber", claim.get("SLIP_NO").toString());
					}
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if( !"200".equals(ComUtil.objToStr(map.get("statusCode")))) {
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						
						if(errorMap.get("errorMsg").toString().indexOf("송장정보가") == -1) {
							apiInfoMap.put("code", "500");
							rtnMsg += "claimId" + ":" + claim.get("PA_CLAIM_NO").toString() + " " + errorMap.get("errorMsg").toString() + " ";
							continue;
						} else {
							apiDataMap = new ParamMap();
							apiDataMap.put("claimId", claim.get("PA_CLAIM_NO").toString());
							apiDataMap.put("orderIds", orderIds);
							apiDataMap.put("shippingMethod", "DIRECT");
							
							map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
							if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
								
								errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
								
								apiInfoMap.put("code", "500");
								rtnMsg += "paClaimNo" + ":" + claim.get("PA_CLAIM_NO").toString() + errorMap.get("errorMsg").toString();
								continue;
							}
						}
					}
						
					executedRtn = updatePaOrderMDoFlag(claim, "40", "교환재배송", Constants.PA_KAKAO_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO") + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
						apiInfoMap.put("code", "500");
					}
					
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO")+ " " + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/*SK스토아 패널티 제외로 인해 사용 안함*/
	@ApiOperation(value = "반품/교환 보류 처리", notes = "반품/교환 보류 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/claim-hold-proc/{claimGb}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> claimHoldProc(
			@PathVariable("claimGb") String claimGb, 		//30:반품, 45:교환
			HttpServletRequest request) throws Exception {
		
		ParamMap apiInfoMap 	= new ParamMap();
		ParamMap apiDataMap		= null;
		ParamMap errorMap		= null;
		ParamMap paramMap		= new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> orderIds = null;
		String rtnMsg			= "";
		String prg_id 			= "30".equals(claimGb)?"IF_PAKAKAOAPI_04_013":"IF_PAKAKAOAPI_04_014";
		int    executedRtn		= 0;
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			paramMap.put("claimGb", claimGb);
			List<Map<String, Object>> claimList = paKakaoClaimService.selectPaKakaoClaimHoldTargetList(paramMap);
			
			for(Map<String, Object> claim : claimList) {
				try {

					apiInfoMap.put("paCode", claim.get("PA_CODE").toString());
					apiDataMap = new ParamMap();
					
					apiDataMap.put("claimId", claim.get("PA_CLAIM_NO").toString());

					orderIds 	= new ArrayList<String>();
					orderIds.add(claim.get("PA_ORDER_SEQ").toString());
					apiDataMap.put("orderIds", orderIds);
					apiDataMap.put("pendingCodeName", "30".equals(claimGb)?"DO_NOT_COLLECTED":"ETC");
					apiDataMap.put("pendingComment", "상품 미수령으로 인한 보류");
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
					
					if( !"200".equals(ComUtil.objToStr(map.get("statusCode")))) {
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
						apiInfoMap.put("code", "500");
						rtnMsg += "claimId" + ":" + claim.get("PA_CLAIM_NO").toString() + " " + errorMap.get("errorMsg").toString() + " ";
						continue;
					}
						
					executedRtn = updatePaOrderMHoldYn(claim, "1", "30".equals(claimGb)?"34":"44", "30".equals(claimGb)?"반품보류 성공":"교환보류 성공", Constants.PA_KAKAO_SUCCESS_CODE);
					if(executedRtn < 1){
						rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO") + " TPAORDERM UPDATE Fail : updatePaOrderMHoldYn/";
						apiInfoMap.put("code", "500");
					}
					
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "claimId:"+claim.get("PA_CLAIM_NO")+ " " + PaKakaoComUtill.getErrorMessage(e)+"/";
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
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
	@RequestMapping(value = "/cancel-confirm-proc-bo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProcBo(
			@RequestParam(value = "claimId", 	 	  required = true) String claimId, // 카카오 클레임 번호
			@RequestParam(value = "id"		, 	  	  required = true) String id, // 카카오 주문 번호(pa_order_seq)
			@RequestParam(value = "procFlag", 	  	  required = true) String procFlag,
			@RequestParam(value = "paCode", 		  required = true) String paCode,
			HttpServletRequest request) throws Exception{
		
		String prg_id     = "IF_PAKAKAOAPI_04_011";
		ParamMap apiInfoMap 	   = new ParamMap();
		ParamMap apiDataMap		   = new ParamMap();
		ParamMap errorMap		   = null;
		ParamMap paramMap 		   = new ParamMap();
		Map<String, Object> map    = new HashMap<String, Object>();
		ArrayList<String> orderIds = new ArrayList<String>();
		String rtnMsg			   = "";
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			
			apiDataMap.put("claimId" , claimId);
			orderIds.add(id);
			apiDataMap.put("orderIds", orderIds);
			
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
			
			if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				rtnMsg += "paClaimNo" + ":" + claimId + " " + errorMap.get("errorMsg").toString() + " ";
				
			}else {
				paramMap.put("paCode",			paCode);
				paramMap.put("claimId",			claimId);
				paramMap.put("orderIds",		id);
				paramMap.put("procFlag",		procFlag);
				paramMap.put("outBefClaimGb",	"1");
				paramMap.put("message", 		"취소승인완료");
				
				paKakaoClaimService.updatePaKakaoCancelConfirmTx(paramMap);
			}
			
			apiInfoMap.put("message", rtnMsg);
		}catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 카카오 교환 거부(BO 호출용)
	 * @param paCode
	 * @param claimId
	 * @param orderId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/exchange-refuse-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> refusalChange(
			@RequestParam(value = "paCode", 	  required = true) String paCode,
			@RequestParam(value = "claimId", 	  required = true) String claimId, 
			@RequestParam(value = "id", 	  	  required = true) String id,
			@RequestParam(value = "modifyId", 	  required = true) String modifyId,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> map    = null;
		ArrayList<String> orderIds = new ArrayList<String>();
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		ParamMap errorMap	= null;
		ParamMap paramMap	= new ParamMap();
		String   prg_id     = "";
		String   rtnMsg	    = "";
		int 	 excuteCnt  = 0; 
		
		try {
			prg_id = "IF_PAKAKAOAPI_04_012";
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			orderIds.add(id);

			apiInfoMap.put("paCode", paCode);
			apiDataMap.put("claimId", claimId);
			apiDataMap.put("orderIds", orderIds);

			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap); 

			if( !"200".equals(ComUtil.objToStr(map.get("statusCode"))) ) {
				
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				rtnMsg += "paClaimNo" + ":" + claimId + " " + errorMap.get("errorMsg").toString() + " ";
				
			}else {
				paramMap.put("paCode", paCode);
				paramMap.put("claimId", claimId);
				paramMap.put("orderIds",id);
				paramMap.put("preCanYn", "1");
				paramMap.put("changeFlag", "03");
				paramMap.put("modifyId", modifyId);
				paramMap.put("apiResultMessage", "재고부족 or 판매불가로 인한 교환거절 처리");
				paramMap.put("apiResultCode", "0000");
				
				excuteCnt = paKakaoClaimService.updatePreCanYn(paramMap);
				if(excuteCnt < 1) {
					throw processException("msg.cannot_save", new String[] { claimId+":TPAORDERM UPDATE(preCancelYn)" });
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 카카오 모바일자동취소 품절취소반품
	 * @return
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = " 모바일자동취소 품절취소반품", notes = " 모바일자동취소 품절취소반품", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request) throws Exception{
		
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		HashMap<String, String> map = new HashMap<String, String>();
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap errorMap	= null;
		String   prg_id     = "IF_PAKAKAOAPI_04_015";
		String url = "";
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			url = apiInfoMap.get("url").toString();
			
			// STEP1. 품절취소반품, 일괄취소반품 대상 조회 
			List<HashMap<String, String>> cancelList = paKakaoClaimService.selectPaMobileOrderAutoCancelList();
			for(HashMap<String, String> cancelItem : cancelList) {
				try {
					map.put("PA_GROUP_CODE", Constants.PA_SSG_GROUP_CODE);
					map.put("PA_CODE", cancelItem.get("PA_CODE"));
					map.put("PA_ORDER_NO", cancelItem.get("PA_ORDER_NO"));
					map.put("PA_ORDER_SEQ", cancelItem.get("PA_ORDER_SEQ"));
					map.put("ORDER_NO", cancelItem.get("ORDER_NO"));
					map.put("ORDER_G_SEQ", cancelItem.get("ORDER_G_SEQ"));
					map.put("PROC_ID", Constants.PA_KAKAO_PROC_ID);
					
					apiInfoMap.put("paCode", cancelItem.get("PA_CODE").toString());
					apiInfoMap.put("url", url.replace("{orderId}", cancelItem.get("PA_ORDER_SEQ").toString()));
					connectResult = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
					
					if(!"200".equals(ComUtil.objToStr(connectResult.get("statusCode"))) ) {
						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 ");
						paOrderService.updateRemark3NTx(map);
						
						errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(connectResult);
						
						cancelItem.put("preCanYn"			, "0");
						cancelItem.put("apiResultMessage"	, errorMap.get("errorMsg").toString());
						cancelItem.put("apiResultCode"		, "500");
						
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", errorMap.get("errorMsg").toString());
					} else {
						map.put("REMARK3_N", "10");
						map.put("RSLT_MESSAGE", "모바일자동취소 성공");
						paOrderService.updateRemark3NTx(map);
						
						//상담생성 & 문자발송 & 상품품절처리
						paCommonService.savePaMobileOrderCancelTx(map);
					}
				} catch(Exception e) {
					log.error(prg_id + " - 모바일자동취소 오류" + cancelItem.get("PA_ORDER_SEQ").toString(), e);
					map.put("REMARK3_N", "90");
					map.put("RSLT_MESSAGE", "모바일자동취소 실패 ");
					paOrderService.updateRemark3NTx(map);
					
					cancelItem.put("preCanYn"			, "0");
					cancelItem.put("apiResultMessage"	, PaKakaoComUtill.getErrorMessage(e));
					cancelItem.put("apiResultCode"		, "500");
					
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", cancelItem.get("PA_ORDER_SEQ").toString() + " 모바일자동취소 에러" );
				}
			}
			
			Thread.sleep(4000); // 4초 대기
			// STEP2. 변경 주문내역 조회 V2 API 호출
			paKakaoDeliveryController.modifyOrderListV2(request, "", "");
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	public void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PAKAKAO_CANCEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"	, prg_id);
		paramMap.put("paOrderGb", "20");
		paramMap.put("siteGb"	, Constants.PA_KAKAO_PROC_ID);
		
		try {
			paKakaoConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<HashMap<String, Object>> cancelInputTargetList = paKakaoClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> cancelTargetList : cancelInputTargetList) {
				try {
					paKakaoAsyncController.cancelInputAsync(cancelTargetList, request);
				} catch(Exception e) {
					log.error(prg_id + " - EE.주문 취소 내역 생성 오류", e);
					continue;
				}
			}
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(paramMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, paramMap);
		}
	}
	
	public void orderClaimMain(HttpServletRequest request, String claimGb) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		String prg_id = "";
		
		switch(claimGb) {
		case "30" :
			prg_id = "PAKAKAO_ORDER_CLAIM"; 
			break;
		case "31" :
			prg_id = "PAKAKAO_CLAIM_CANCEL";
			break;
		case "40" :
			prg_id = "PAKAKAO_ORDER_CHANGE";
			break;
		case "41" :
			prg_id = "PAKAKAO_CHANGE_CANCEL";
			break;
		default :
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"  , prg_id);
		paramMap.put("paOrderGb", claimGb);
		paramMap.put("siteGb"	, Constants.PA_KAKAO_PROC_ID);
		
		try {
			paKakaoConnectUtil.getApiInfo(prg_id, paramMap);
			paKakaoConnectUtil.checkDuplication(prg_id, paramMap);
			
			log.info("========================= 카카오 Order Claim (" + claimGb + ") START =========================");
			List<HashMap<String, Object>> claimTargetList = paKakaoClaimService.selectClaimTargetList(paramMap);
			
			for(HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb)) {			// 빈품
						paKakaoAsyncController.orderClaimAsync(claim, request);
					} else if("31".equals(claimGb)) {	// 반취
						paKakaoAsyncController.claimCancelAsync(claim, request);
					} else if("40".equals(claimGb)) {	// 교환
						paKakaoAsyncController.orderChangeAsync(claim, request);
					} else if("41".equals(claimGb)) {	// 교취
						paKakaoAsyncController.changeCancelAsync(claim, request);
					}
				} catch(Exception e) {
					log.error("orderClaimMainError : "+e.getMessage());
					continue;
				}
			}
			
			log.info("========================= 카카오 Order Claim (" + claimGb + ") END =========================");
		} catch(Exception e) {
			paKakaoConnectUtil.checkException(paramMap, e);
		} finally {
			paKakaoConnectUtil.closeApi(request, paramMap);
		}
	}
	
	private int updatePaOrderMDoFlag(Map<String, Object> map, String doFlag, String resultMessage, String resultCode) throws Exception {
		int executedRtn = 0;
		
		map.put("PA_DO_FLAG", doFlag);
		map.put("API_RESULT_CODE", resultCode);
		map.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paKakaoClaimService.updatePaOrderMDoFlag(map);
		
		return executedRtn;
	}
	
	private int updatePaOrderMHoldYn(Map<String, Object> map, String paHoldYn, String paHoldCode, String resultMessage, String resultCode) throws Exception {
		int executedRtn = 0;
		
		map.put("PA_HOLD_YN", paHoldYn);
		map.put("PA_HOLD_CODE", paHoldCode);
		map.put("API_RESULT_CODE", resultCode);
		map.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paKakaoClaimService.updatePaOrderMHoldYn(map);
		
		return executedRtn;
	}
	
}
