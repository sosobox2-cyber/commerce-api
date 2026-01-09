package com.cware.api.pacopn.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.cware.netshopping.domain.model.Pacopnclaimitemlist;
import com.cware.netshopping.domain.model.Pacopnclaimlist;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pacopn.claim.service.PaCopnClaimService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pacopn/claim", description="취소/반품")
@Controller("com.cware.api.pacopn.PaCopnClaimController")
@RequestMapping(value = "/pacopn/claim")
public class PaCopnClaimController extends AbstractController{
	
	@Resource(name = "com.cware.api.pacopn.paCopnAsyncController")
	private PaCopnAsyncController paCopnAsyncController;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.claim.paCopnClaimService")
	private PaCopnClaimService paCopnClaimService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@ApiOperation(value = "주문 상품 취소 처리", notes = "주문 상품 취소 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-ready-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> shippingReadyCancel(
			@ApiParam(name = "paShipNo"     , value = "배송번호"    , defaultValue = "") @RequestParam(value = "paShipNo"     , required = true) String paShipNo,
			@ApiParam(name = "paOrderNo"    , value = "주문번호"    , defaultValue = "") @RequestParam(value = "paOrderNo"    , required = true) String paOrderNo,
			@ApiParam(name = "paOrderSeq"   , value = "주문번호순번", defaultValue = "") @RequestParam(value = "paOrderSeq"    , required = true) String paOrderSeq,
			@ApiParam(name = "vendorItemId" , value = "상품번호"    , defaultValue = "") @RequestParam(value = "vendorItemId" , required = true) String vendorItemId,
			@ApiParam(name = "shippingCount", value = "주문갯수"    , defaultValue = "") @RequestParam(value = "shippingCount", required = true) String shippingCount,
			@ApiParam(name = "paCode"		, value = "paCode"		, defaultValue = "") @RequestParam(value = "paCode"		  , required = true) String paCode,
			HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj    = null;
		JsonArray  vendorItemIds = null;
		JsonArray  receiptCounts = null;
		JsonObject responseObj = null;
		
		String[] apiKeys = null;
		String paName = "";
		String loginId = "";
		
		String dupCheck = "";
		String rtnMsg   = Constants.SAVE_SUCCESS;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_007");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
				dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
				if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			if(paCode.equals(Constants.PA_COPN_BROAD_CODE)) {
				paName = Constants.PA_BROAD;
				loginId = ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID");
			}
			else {
				paName = Constants.PA_ONLINE;
				loginId = ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID");
			}
			apiKeys = apiInfo.get(paName).split(";");
			
			
			requestObj    = new JsonObject();
			vendorItemIds = new JsonArray();
			receiptCounts = new JsonArray();
			
			vendorItemIds.add(Long.valueOf(vendorItemId));
			receiptCounts.add(Long.valueOf(shippingCount));
			
			requestObj.addProperty("orderId"         , paOrderNo);
			requestObj.addProperty("bigCancelCode"   , "CANERR");
			requestObj.addProperty("middleCancelCode", "CCTTER");
			requestObj.addProperty("userId"          , loginId);
			requestObj.addProperty("vendorId"        , apiKeys[0]);
			requestObj.add("vendorItemIds", vendorItemIds);
			requestObj.add("receiptCounts", receiptCounts);
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL")
					.replaceAll("#vendorId#", apiKeys[0])
					.replaceAll("#orderId#" , paOrderNo)), "POST", new GsonBuilder().create().toJson(requestObj));
			
			if("SUCCESS".equals(responseObj.get("code").getAsString()) || "200".equals(responseObj.get("code").getAsString())){
				paramMap.put("code"   , "200");
				paramMap.put("message", "OK");
				paramMap.put("preCancelReason", "재고부족으로 인한 결품처리");
			}else{
				paramMap.put("code"     , "500");
				paramMap.put("message"  , responseObj.get("message").toString());
			}
			
			paramMap.put("paOrderNo"       , paOrderNo);
			paramMap.put("paShipNo"        , paShipNo);
			paramMap.put("paOrderSeq"      , paOrderSeq);
			paramMap.put("paCode"		   , paCode);
			paramMap.put("apiResultCode"   , "200".equals(paramMap.getString("code")) ? Constants.SAVE_SUCCESS : Constants.SAVE_FAIL);
			paramMap.put("apiResultMessage", "재고부족으로 인한 결품처리");
			
			rtnMsg = paCopnClaimService.saveOrderRejectProcTx(paramMap);
			if(!Constants.SAVE_SUCCESS.equals(rtnMsg)){
				paramMap.put("code"   , "404");
				paramMap.put("message", getMessage("errors.no.select") + rtnMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , "200");
					paramMap.put("message", "OK");
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	// to be used
	@ApiOperation(value = "판매중단처리", notes = "판매중단처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/soldOut-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj    = null;
		JsonArray  vendorItemIds = null;
		JsonArray  receiptCounts = null;
		JsonObject responseObj = null;
		
		String[] apiKeys = null;
		String paName = "";
		String loginId = "";
		
		String dupCheck = "";
		String rtnMsg   = Constants.SAVE_SUCCESS;
		
		HashMap<String, String> cancelMap = null;
		String msg = "";
		String paGroupCode = "05";
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_007_BO");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			paramMap.put("paGroupCode", paGroupCode);
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			//= Step 1) 판매취소 List 조회
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
			 if(cancelList.size() > 0){
				 for (int i = 0; i < cancelList.size(); i++) {
					 try {
						 cancelMap = (HashMap<String, String>)cancelList.get(i);
		                 
						 String paShipNo	  = ComUtil.objToStr(cancelMap.get("PA_SHIP_NO"));
						 String paOrderNo 	  = ComUtil.objToStr(cancelMap.get("PA_ORDER_NO"));
						 String paOrderSeq    = ComUtil.objToStr(cancelMap.get("PA_ORDER_SEQ"));
						 String vendorItemId  = ComUtil.objToStr(cancelMap.get("VENDOR_ITEM_ID"));
						 String shippingCount = ComUtil.objToStr(cancelMap.get("SHIPPING_COUNT"));
						 String paCode		  = ComUtil.objToStr(cancelMap.get("PA_CODE"));
						 
						 cancelMap.put("ORG_ORD_CAN_YN", ComUtil.objToStr(cancelMap.get("ORDER_CANCEL_YN")));
						 
						 if(paCode.equals(Constants.PA_COPN_BROAD_CODE)) {
						 	paName = Constants.PA_BROAD;
						 	loginId = ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID");
						 }
						 else {
						 	paName = Constants.PA_ONLINE;
						 	loginId = ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID");
						 }
						 apiKeys = apiInfo.get(paName).split(";");
						 
						 requestObj    = new JsonObject();
						 vendorItemIds = new JsonArray();
						 receiptCounts = new JsonArray();
						 
						 vendorItemIds.add(Long.valueOf(vendorItemId));
						 receiptCounts.add(Long.valueOf(shippingCount));
						 
						 requestObj.addProperty("orderId"         , paOrderNo);
						 requestObj.addProperty("bigCancelCode"   , "CANERR");
						 requestObj.addProperty("middleCancelCode", "CCTTER");
						 requestObj.addProperty("userId"          , loginId);
						 requestObj.addProperty("vendorId"        , apiKeys[0]);
						 requestObj.add("vendorItemIds", vendorItemIds);
						 requestObj.add("receiptCounts", receiptCounts);
						 
						 responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL")
						 		.replaceAll("#vendorId#", apiKeys[0])
						 		.replaceAll("#orderId#" , paOrderNo)), "POST", new GsonBuilder().create().toJson(requestObj));
						 
						 msg = responseObj.get("message").getAsString();
						 
					 } catch(Exception e) {
						 log.info(e.getMessage());
						 msg = e.getMessage();
						 continue;
					 } finally {
						 //tpaorderm order_cancel_yn update '10' or '90'
						 if("SUCCESS".equals(responseObj.get("code").getAsString()) || "200".equals(responseObj.get("code").getAsString())){
						 	cancelMap.put("ORDER_CANCEL_YN", "10");
						 	cancelMap.put("RSLT_MESSAGE", "판매취소 성공");
						 	paorderService.updateOrderCancelYnTx(cancelMap);
						 }else{
						 	cancelMap.put("ORDER_CANCEL_YN", "90");
						 	cancelMap.put("RSLT_MESSAGE", "판매취소 실패 "+ msg);
						 	paorderService.updateOrderCancelYnTx(cancelMap);
						 }
						 //상담생성 & 문자발송
						 paCommonService.saveOrderCancelCounselTx(cancelMap);
					 }
				}
			 }
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , "200");
					paramMap.put("message", "OK");
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	
	
	
	@ApiOperation(value = "반품요청목록조회", notes = "반품요청목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-list-day", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnListDay(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자[yyyMMdd]")   @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate"  , required = false, value = "종료일자[yyyMMdd]")   @RequestParam(value = "toDate"  , required = false) String toDate) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		
		String dupCheck  = "";
		String startDate = "";
		String endDate   = "";
		
		endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
		
		paramMap.put("apiCode"   , "IF_PACOPNAPI_03_009");
		paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
		paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate" , systemService.getSysdatetimeToString());
		paramMap.put("startDateQ", startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
		paramMap.put("endDateQ"  , endDate.substring(0, 4)   + "-" + endDate.substring(4, 6)   + "-" + endDate.substring(6, 8));
		
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for (int i=0; i<4; i++){
				if(i == 0) {
					paramMap.put("status", "RU");
				} else if (i == 1) {
					paramMap.put("status", "UC");
				} else if (i == 2) {
					paramMap.put("status", "CC");
				} else {
					paramMap.put("cancelType", "CANCEL");
				}
				
				for(int count=0; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
					paramMap.put("nextToken" , "start");
					
					if(count == 0) {
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
					}
					
					while(!"".equals(paramMap.getString("nextToken"))){
						procClaimListDay(paramMap, apiInfo);
						if(!"200".equals(paramMap.getString("code"))){
							break;
						}
					}
				}
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			//= DO_FLAG  < 30 : 취소처리  OR DO_FLAG = 30 이고 직권취소
			slipOutCancel(request);
			
			//= DO_FLAG >= 40 : 반품처리
			claimInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 반품요청 목록 조회 > 데이터 저장
	 * @param paramMap
	 * @param apiInfo
	 * @return
	 * @throws Exception
	 */
	private ParamMap procClaimListDay(ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception{
		JsonObject responseObj   = null;
		JsonArray  claimList     = null;
		JsonObject claim         = null;
		JsonArray  claimitemList = null;
		JsonObject claimitem     = null;
		
		List<Pacopnclaimitemlist> paCopnClaimitemList = null;
		Pacopnclaimlist paCopnClaim         = null;
		Pacopnclaimitemlist paCopnClaimitem = null;
		
		HashMap<String, String> claimMap = new HashMap<String, String>();

		URIBuilder builder = null;
		
		Timestamp sysdateTime = null;
		
		String[] apiKeys = null;
		
		String nextToken = paramMap.getString("nextToken");
		
		try{
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			//취소완료API 추가
			String parm = "";
			String parmVal = "";
			if (!"".equals(paramMap.getString("cancelType"))) {
				parm = "cancelType";
				parmVal = paramMap.getString("cancelType");
			} else {
				parm = "status";
				parmVal = paramMap.getString("status");
			}
			
			builder = new URIBuilder(apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0]))
			.addParameter("createdAtFrom", paramMap.getString("startDateQ"))
			.addParameter("createdAtTo"  , paramMap.getString("endDateQ"))
			.addParameter(parm       	 , parmVal)
			.addParameter("maxPerPage"   , Constants.PA_COPN_COUNT_PER_PAGE);
			
			if(nextToken != null && !"".equals(nextToken) && !"start".equals(nextToken)){
				builder.addParameter("nextToken", nextToken);
			}
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), builder);
			
			if("200".equals(responseObj.get("code").getAsString())){
				sysdateTime = DateUtil.toTimestamp(paramMap.getString("startDate"));
				
				claimList = responseObj.get("data").getAsJsonArray();
				
				paramMap.put("nextToken", responseObj.get("nextToken").getAsString());
				if(claimList.size() > 0){
					for(int i=0; i<claimList.size(); i++){
						boolean preCanceled = true;
						
						claim = claimList.get(i).getAsJsonObject();
						paCopnClaim = new Pacopnclaimlist();
						
						paCopnClaim.setPaOrderGb               ("30");
						paCopnClaim.setReceiptId               (claim.get("receiptId").getAsString());
						paCopnClaim.setOrderId                 (claim.get("orderId").getAsString());
						paCopnClaim.setPaymentId               (claim.get("paymentId").getAsString());
						paCopnClaim.setReceiptType             (claim.get("receiptType").getAsString());
						if("cancelType".equals(parm)){
							paCopnClaim.setReceiptStatus       (claim.get("receiptStatus").isJsonNull() ? "" : "RETURNS_COMPLETED"); //취소완료에서 null 일 수 있음
						} else {
							paCopnClaim.setReceiptStatus       (claim.get("receiptStatus").getAsString());
						}
						paCopnClaim.setCreatedAt               (DateUtil.toTimestamp(claim.get("createdAt").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
						paCopnClaim.setModifiedAt              (DateUtil.toTimestamp(claim.get("modifiedAt").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
						paCopnClaim.setRequesterName           (claim.get("requesterName").isJsonNull() ? "" : claim.get("requesterName").getAsString());
						paCopnClaim.setRequesterPhoneNumber    (claim.get("requesterPhoneNumber").isJsonNull() ? "" : claim.get("requesterPhoneNumber").getAsString().replaceAll("-", ""));
						paCopnClaim.setRequesterRealPhoneNumber(claim.get("requesterRealPhoneNumber").isJsonNull() ? "" : claim.get("requesterRealPhoneNumber").getAsString().replaceAll("-", ""));
						paCopnClaim.setRequesterAddress        (claim.get("requesterAddress").isJsonNull() ? "" : claim.get("requesterAddress").getAsString());
						paCopnClaim.setRequesterAddressDetail  (claim.get("requesterAddressDetail").isJsonNull() ? "" : claim.get("requesterAddressDetail").getAsString());
						paCopnClaim.setRequesterZipCode		   (claim.get("requesterZipCode").isJsonNull() ? "" : claim.get("requesterZipCode").getAsString());
						paCopnClaim.setCancelReasonCategory1   (claim.get("cancelReasonCategory1").getAsString());
						paCopnClaim.setCancelReasonCategory2   (claim.get("cancelReasonCategory2").getAsString());
						
						String reason = "";
						if(!claim.get("cancelReason").isJsonNull()) {
							reason = claim.get("cancelReason").getAsString();
							int len = reason.getBytes("UTF-8").length; //utf-8: 한글 3bytes
							if(len > 580) {
								reason = ComUtil.subStringUTFBytes(reason, 580) + "..[내용잘림]"; //subStringUTFBytes: 3bytes 계산
							}
						}
						paCopnClaim.setCancelReason(reason);
						
						paCopnClaim.setCancelCountSum          (claim.get("cancelCountSum").getAsLong());
						paCopnClaim.setReturnDeliveryId        (claim.get("returnDeliveryId").isJsonNull() ? "" :claim.get("returnDeliveryId").getAsString());
						paCopnClaim.setReturnDeliveryType      (claim.get("returnDeliveryType").isJsonNull() ? "" :claim.get("returnDeliveryType").getAsString());
						paCopnClaim.setReleaseStopStatus       (claim.get("releaseStopStatus").isJsonNull() ? "" :claim.get("releaseStopStatus").getAsString());
						paCopnClaim.setEnclosePrice            (claim.get("enclosePrice").isJsonNull() ? 0 : claim.get("enclosePrice").getAsLong());
						paCopnClaim.setFaultByType             (claim.get("faultByType").getAsString());
						paCopnClaim.setPreRefund               (claim.get("preRefund").getAsBoolean() ? "1" : "0");
						paCopnClaim.setReturnShippingCharge    (claim.get("returnShippingCharge").isJsonNull() ? Long.parseLong("0") : claim.get("returnShippingCharge").getAsLong());
						if(!claim.get("completeConfirmDate").isJsonNull() && !"".equals(claim.get("completeConfirmDate").getAsString())){
							paCopnClaim.setCompleteConfirmDate (DateUtil.toTimestamp(claim.get("completeConfirmDate").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
						}
						paCopnClaim.setCompleteConfirmType     (claim.get("completeConfirmType").getAsString());
						paCopnClaim.setReturnDeliveryDtos      (claim.get("returnDeliveryDtos").toString());
						paCopnClaim.setModifyDate              (sysdateTime);
						paCopnClaim.setInsertDate              (sysdateTime);
						
						claimitemList = claim.get("returnItems").getAsJsonArray();
						paCopnClaimitemList = new ArrayList<Pacopnclaimitemlist>();
						
						for(int j=0; j<claimitemList.size(); j++){
							claimitem = claimitemList.get(j).getAsJsonObject();
							
							paCopnClaimitem = new Pacopnclaimitemlist();
							paCopnClaimitem.setReceiptId            (claim.get("receiptId").getAsString());
							paCopnClaimitem.setOrderId              (claim.get("orderId").getAsString());
							paCopnClaimitem.setPaymentId            (claim.get("paymentId").getAsString());
							paCopnClaimitem.setVendorItemPackageId  (claimitem.get("vendorItemPackageId").isJsonNull() ? "" : claimitem.get("vendorItemPackageId").getAsString());
							paCopnClaimitem.setVendorItemPackageName(claimitem.get("vendorItemPackageName").isJsonNull() ? "" : claimitem.get("vendorItemPackageName").getAsString());
							paCopnClaimitem.setVendorItemId         (claimitem.get("vendorItemId").getAsString());
							paCopnClaimitem.setVendorItemName       (claimitem.get("vendorItemName").isJsonNull() ? "" : claimitem.get("vendorItemName").getAsString());
							paCopnClaimitem.setPurchaseCount        (claimitem.get("purchaseCount").isJsonNull() ? 0 : claimitem.get("purchaseCount").getAsLong());
							paCopnClaimitem.setCancelCount          (claimitem.get("cancelCount").isJsonNull() ? 0 : claimitem.get("cancelCount").getAsLong());
							paCopnClaimitem.setShipmentBoxId        (claimitem.get("shipmentBoxId").getAsString());
							paCopnClaimitem.setSellerProductId      (claimitem.get("sellerProductId").getAsString());
							paCopnClaimitem.setSellerProductName    (claimitem.get("sellerProductName").getAsString());
							paCopnClaimitem.setInsertDate           (sysdateTime);
							
							paramMap.put("orderId", paCopnClaimitem.getOrderId());
							paramMap.put("sellerProductId", paCopnClaimitem.getSellerProductId());
							paramMap.put("vendorItemId", paCopnClaimitem.getVendorItemId());
							//paramMap.put("shipmentBoxId", paCopnClaimitem.getShipmentBoxId());
							//map itemseq, 원래 shipmentboxid 
							claimMap = paCopnClaimService.selectOrgShipmentBoxId(paramMap);
							//String itemSeq = paCopnClaimService.selectPaCopnOrderItemSeq(paramMap);
							if(claimMap != null){
								paCopnClaimitem.setItemSeq              (claimMap.get("ITEM_SEQ").toString());
								paCopnClaimitem.setOrgShipmentBoxId     (claimMap.get("SHIPMENT_BOX_ID").toString());
							} else {
								preCanceled = false;
								break;
							}
							paCopnClaimitemList.add(paCopnClaimitem);
						}
						
						if(!preCanceled) {
							continue;
						} 
						
						paCopnClaimService.savePaCopnClaimTx(paCopnClaim, paCopnClaimitemList, paramMap);
						
						paramMap.put("code"   , "200");
						paramMap.put("message", "OK");
					}
					
					paramMap.put("code"   , "200");
					paramMap.put("message", "OK");
					
				}else{
					paramMap.put("code"     , "404");
					paramMap.put("message"  , getMessage("pa.no_return_data"));
					paramMap.put("nextToken", "");
				}
			}else{
				paramMap.put("code"     , "500");
				paramMap.put("message"  , responseObj.get("message").toString());
				paramMap.put("nextToken", "");
			}
		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			paramMap.put("nextToken", "");
		}
		return paramMap;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고중지완료", notes = "출고중지완료", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/slip-out-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutCancel(HttpServletRequest request) throws Exception{
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		List<Object> cancelList = null;
		HashMap<String, String> apiInfo = null;
		HashMap<String, Object> cancel = null;
		
		String dupCheck = "";
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_005");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			cancelList = paCopnClaimService.selectCancelList();
			totalCnt   = cancelList.size();
			
			if(cancelList.size() > 0){
				for(int i=0; i<cancelList.size(); i++){
					cancel = (HashMap<String, Object>) cancelList.get(i);
					resultMap = paCopnClaimService.makeCancelListProc(apiInfo, cancel);
					if(Constants.SAVE_SUCCESS.equals(resultMap.getString("rtnMsg"))){
						successCnt++;
					}
				}
			}else{
				paramMap.put("code"   , "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			
			cancelInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "쿠팡 취소생성", notes = "쿠팡 취소생성", httpMethod = "GET")
	@RequestMapping(value = "/cancel-input", method = RequestMethod.GET)
	@ResponseBody
	public void cancelInputMain(HttpServletRequest request) throws Exception{
		List<Object> cancelInputTargetList        = null;
		HashMap<String, Object> cancelInputTarget = null;
		
		String dupCheck = "";
		String prg_id   = "PACOPN_CANCEL_INPUT";
		
		log.info("=========================== COPN Cancel Create Start ===========================");
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{prg_id});
			
			cancelInputTargetList = paCopnClaimService.selectCancelInputTargetList();
			
			if(cancelInputTargetList.size() > 0){
				for(int i=0; i<cancelInputTargetList.size(); i++){
					try{
						cancelInputTarget = new HashMap<String, Object>();
						cancelInputTarget = (HashMap<String, Object>) cancelInputTargetList.get(i);
						
						paCopnAsyncController.cancelInputAsync(cancelInputTarget, request);
					}catch(Exception e){
						continue;
					}
				}
			}else{
				log.error("Error Msg: " + getMessage("errors.no.select"));
			}
		}catch(Exception e){
			log.error("error msg: " + e.getMessage());
		}finally{
			try {
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("[CloseHistoryTx Error] " + e.getMessage());
			}
			log.info("=========================== COPN Cancel Create  End  ===========================");
		}
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "이미출고처리", notes = "이미출고처리", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/already-slip-out", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> alreadySlipOut(HttpServletRequest request) throws Exception{
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		List<Object> claimList = null;
		HashMap<String, String> apiInfo = null;
		HashMap<String, Object> claim   = null;
		
		String dupCheck = "";
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_006");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			claimList = paCopnClaimService.selectClaimList();
			totalCnt  = claimList.size();
			if(claimList.size() > 0){
				for(int i=0; i<claimList.size(); i++){
					claim = (HashMap<String, Object>) claimList.get(i);
					
					resultMap = paCopnClaimService.makeClaimListProc(apiInfo, claim);
					if(Constants.SAVE_SUCCESS.equals(resultMap.getString("rtnMsg"))){
						successCnt++;
					}
				}
			}else{
				paramMap.put("code"   , "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품 생성", notes = "반품 생성", httpMethod = "GET")
	@RequestMapping(value = "/claim-input", method = RequestMethod.GET)
	@ResponseBody
	public void claimInputMain(HttpServletRequest request) throws Exception{
		List<Object> claimInputTargetList        = null;
		HashMap<String, Object> claimInputTarget = null;
		
		String dupCheck = "";
		String prg_id   = "PACOPN_CLAIM_INPUT";
		
		log.info("=========================== COPN Claim Create Start ===========================");
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{prg_id});
			
			paCopnClaimService.makeClaimPaorderm();
			
			claimInputTargetList = paCopnClaimService.selectOrderClaimTargetList();
			if(claimInputTargetList.size() > 0){
				for(int i=0; i<claimInputTargetList.size(); i++){
					try{
						claimInputTarget = new HashMap<String, Object>();
						claimInputTarget = (HashMap<String, Object>) claimInputTargetList.get(i);
						
						paCopnAsyncController.orderClaimAsync(claimInputTarget, request);
					}catch(Exception e){
						continue;
					}
				}
			}else{
				log.error("Error Msg: " + getMessage("errors.no.select"));
			}
		}catch(Exception e){
			log.error("error msg: " + e.getMessage());
		}finally{
			try {
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("[CloseHistoryTx Error] " + e.getMessage());
			}
			log.info("=========================== COPN Claim Create  End  ===========================");
		}
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "수거송장등록", notes = "수거송장등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-exchange-invoice", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnExchangeInvoice(HttpServletRequest request){
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		
		List<Object> pickupList        = null;
		HashMap<String, Object> pickup = null;
		
		String[] apiBroadKeys = null;
		String[] apiOnlineKeys = null;
		String[] apiKeys = null;
		String paName = null;
		
		String dupCheck = "";
		
		HashMap<String, String> pickupMap = new HashMap<String, String>();
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_010");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			apiBroadKeys = apiInfo.get(Constants.PA_BROAD).split(";");
			apiOnlineKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
			
			pickupList = paCopnClaimService.selectPickupList(); 
			totalCnt   = pickupList.size();
			
			if(pickupList.size() > 0){
				for(int i=0; i<pickupList.size(); i++){
					pickup = (HashMap<String, Object>) pickupList.get(i);
					
					pickupMap = paCopnClaimService.selectPickupDetailList(pickup.get("PA_CLAIM_NO").toString()); 
					
					requestObj = new JsonObject();
					
					requestObj.addProperty("returnExchangeDeliveryType", ("30".equals(pickup.get("PA_ORDER_GB").toString())) ? "RETURN" : "EXCHANGE");
					requestObj.addProperty("receiptId"          , Long.parseLong(pickup.get("PA_CLAIM_NO").toString()));
					requestObj.addProperty("deliveryCompanyCode", pickupMap.get("DELIVERY_COMPANY_CODE").toString());
					if(pickupMap.get("INVOICE_NO") == null || "".equals(pickupMap.get("INVOICE_NO").toString()) || !ComUtil.isNumber(pickupMap.get("INVOICE_NO").toString())){
						requestObj.addProperty("invoiceNumber"  , pickupMap.get("SUB_INVOICE_NO").toString());
						requestObj.addProperty("deliveryCompanyCode", "DIRECT");
					}else{
						requestObj.addProperty("invoiceNumber"  , pickupMap.get("INVOICE_NO").toString());
					}
					
					if(pickup.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
						apiKeys = apiBroadKeys;
						paName = Constants.PA_BROAD;
					}
					else {
						apiKeys = apiOnlineKeys;
						paName = Constants.PA_ONLINE;
					}
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
							apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
					
					if("200".equals(responseObj.get("code").getAsString())){
						executedRtn = paCopnClaimService.updatePickupResult(pickup);
						if(executedRtn > 0){
							successCnt++;
						}
					} else if(responseObj.get("message").toString().replace(" ","").indexOf("배송송장과같은송장번호") >= 0 || 
							  responseObj.get("message").toString().replace(" ","").indexOf("Thereturninvoiceformatisincorrect") >= 0 || //The return invoice format is incorrect
							  responseObj.get("message").toString().replace(" ","").indexOf("송장번호가유효하지")		>= 0 || 
							  responseObj.get("message").toString().replace(" ","").indexOf("반품송장형식이") 		>= 0) {
						
						requestObj = new JsonObject();
						
						requestObj.addProperty("returnExchangeDeliveryType", ("30".equals(pickup.get("PA_ORDER_GB").toString())) ? "RETURN" : "EXCHANGE");
						requestObj.addProperty("receiptId"          , Long.parseLong(pickup.get("PA_CLAIM_NO").toString()));
						requestObj.addProperty("deliveryCompanyCode", "DIRECT");
						requestObj.addProperty("invoiceNumber"  , pickupMap.get("SUB_INVOICE_NO").toString());
						
						responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
								apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
						
						if("200".equals(responseObj.get("code").getAsString())){
							executedRtn = paCopnClaimService.updatePickupResult(pickup);
							if(executedRtn > 0){
								successCnt++;
							}
						} else{
							paramMap.put("code"   , "500");
							paramMap.put("message", "PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + responseObj.get("message").getAsString());
							log.error("PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + responseObj.get("message").getAsString());
						}
						
					} else{
						paramMap.put("code"   , "500");
						paramMap.put("message", "PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + responseObj.get("message").getAsString());
						log.error("PA_CLAIM_NO : " + pickup.get("PA_CLAIM_NO").toString() + " | " + responseObj.get("message").getAsString());
					}
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
			
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system") + " : " + paramMap.getString("message"));
				}
				paramMap.put("siteGb","PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			
			returnReceiveConfirm(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품상품 입고 확인처리", notes = "반품상품 입고 확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-receive-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnReceiveConfirm(HttpServletRequest request){
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		
		List<Object> receiveList        = null;
		HashMap<String, Object> receive = null;
		
		String[] apiBroadKeys = null;
		String[] apiOnlineKeys = null;
		String[] apiKeys = null;
		String paName = null;
		
		String dupCheck = "";
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_011");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiBroadKeys = apiInfo.get(Constants.PA_BROAD).split(";");
			apiOnlineKeys = apiInfo.get(Constants.PA_ONLINE).split(";");			
			
			receiveList = paCopnClaimService.selectReceiveConfirmList();
			totalCnt   = receiveList.size();
			
			if(receiveList.size() > 0){
				for(int i=0; i<receiveList.size(); i++){
					receive = (HashMap<String, Object>) receiveList.get(i);
					
					if(receive.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
						apiKeys = apiBroadKeys;
						paName = Constants.PA_BROAD;
					}
					else {
						apiKeys = apiOnlineKeys;
						paName = Constants.PA_ONLINE;
					}
					
					requestObj = new JsonObject();
					
					requestObj.addProperty("vendorId" , apiKeys[0]);
					requestObj.addProperty("receiptId", Long.parseLong(receive.get("PA_CLAIM_NO").toString()));
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
							apiInfo.get("API_URL")
							.replaceAll("#vendorId#", apiKeys[0])
							.replaceAll("#receiptId#", receive.get("PA_CLAIM_NO").toString())), "PATCH", new GsonBuilder().create().toJson(requestObj));
					
					if("200".equals(responseObj.get("code").getAsString())){
						executedRtn = paCopnClaimService.updateReceiveConfirmResult(receive);
						if(executedRtn > 0){
							successCnt++;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").getAsString());
					}
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
			
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품요청 승인 처리", notes = "반품요청 승인 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-request-approval", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnRequestApproval(HttpServletRequest request){
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj  = null;
		JsonObject responseObj = null;
		
		List<Object> returnApprovalList        = null;
		HashMap<String, Object> returnApproval = null;
		
		String[] apiBroadKeys = null;
		String[] apiOnlineKeys = null;
		String[] apiKeys = null;
		String paName = null;
		
		String dupCheck = "";
		
		int totalCnt    = 0;
		int successCnt  = 0;
		int executedRtn = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_012");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiBroadKeys = apiInfo.get(Constants.PA_BROAD).split(";");
			apiOnlineKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
			
			returnApprovalList = paCopnClaimService.selectReturnApprovalList();
			totalCnt   = returnApprovalList.size();
			Long paProcQty = Long.valueOf("0");
			
			if(returnApprovalList.size() > 0){
				for(int i=0; i<returnApprovalList.size(); i++){
					returnApproval = (HashMap<String, Object>) returnApprovalList.get(i);
					
					if(i<returnApprovalList.size()-1) {
						HashMap<String, Object> nextReturnApproval = (HashMap<String, Object>) returnApprovalList.get(i+1);
						if(nextReturnApproval.get("PA_CLAIM_NO").toString().equals(returnApproval.get("PA_CLAIM_NO").toString())) {
							paProcQty += Long.parseLong(String.valueOf(returnApproval.get("PA_PROC_QTY")));
							continue;
						}
					}
					
					if(paProcQty.equals(Long.valueOf("0"))) {
						paProcQty = Long.parseLong(String.valueOf(returnApproval.get("PA_PROC_QTY")));
					}
					else {
						paProcQty += Long.parseLong(String.valueOf(returnApproval.get("PA_PROC_QTY")));
					}
					
					requestObj = new JsonObject();
					
					if(returnApproval.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
						apiKeys = apiBroadKeys;
						paName = Constants.PA_BROAD;
					}
					else {
						apiKeys = apiOnlineKeys;
						paName = Constants.PA_ONLINE;
					}
					
					requestObj.addProperty("vendorId"   , apiKeys[0]);
					requestObj.addProperty("receiptId"  , Long.parseLong(returnApproval.get("PA_CLAIM_NO").toString()));
					requestObj.addProperty("cancelCount", paProcQty);
					
					responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL")
							.replaceAll("#vendorId#", apiKeys[0])
							.replaceAll("#receiptId#", returnApproval.get("PA_CLAIM_NO").toString())), "PATCH", new GsonBuilder().create().toJson(requestObj));
					
					paProcQty = Long.valueOf("0");
					
					if( "200".equals(responseObj.get("code").getAsString()) ||
							("400".equals(responseObj.get("code").getAsString()) && "이미반품이완료되었습니다.".equals(responseObj.get("message").getAsString().replaceAll(" ", ""))) 
					   ||	("500".equals(responseObj.get("code").getAsString()) && "환불승인(대기)/접수철회요청된쿠폰의상태가승인할수없는상태이거나수량이맞지않습니다.".equals(responseObj.get("message").getAsString().replaceAll(" ", "")))
							){
						executedRtn = paCopnClaimService.updateReturnApprovalResult(returnApproval);
						if(executedRtn > 0){
							successCnt = successCnt + executedRtn;
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", responseObj.get("message").getAsString());
					}
				}
			}else{
				paramMap.put("code"     , "404");
				paramMap.put("message"  , getMessage("pa.no_return_data"));
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품철회 이력 조회", notes = "반품철회 이력 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-withdraw-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnWithdrawList(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj  = null;
		JsonArray  cancelIds   = null;
		JsonObject responseObj = null;
		JsonArray  data        = null;
		JsonObject withdraw    = null;
		
		List<Object> returnWithdrawList        = null;
		HashMap<String, Object> returnWithdraw = null;
		HashMap<String, Object> resultWithdraw = null;
		
		String[] apiBroadKeys = null;
		String[] apiOnlineKeys = null;
		String[] apiKeys = null;
		String paName = null;
		String paCode = null;
		
		String dupCheck = "";
		
		int totalCnt    = 0;
		int successCnt  = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_013");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			apiBroadKeys = apiInfo.get(Constants.PA_BROAD).split(";");
			apiOnlineKeys = apiInfo.get(Constants.PA_ONLINE).split(";");
			
			for(int count=0; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				if(count == 0) {
					apiKeys = apiBroadKeys;
					paName = Constants.PA_BROAD;
					paCode = Constants.PA_COPN_BROAD_CODE;
				}
				else {
					apiKeys = apiOnlineKeys;
					paName = Constants.PA_ONLINE;
					paCode = Constants.PA_COPN_ONLINE_CODE;
				}
				returnWithdrawList = paCopnClaimService.selectReturnWithdrawList(paCode);
				
				if(returnWithdrawList.size() > 0){
					for(int i=0; i<returnWithdrawList.size(); i++){
						try {
							
							if(i % 50 == 0){
								requestObj = new JsonObject();
								cancelIds  = new JsonArray();
							}
							
							returnWithdraw = (HashMap<String, Object>) returnWithdrawList.get(i);
							cancelIds.add(Long.parseLong(returnWithdraw.get("PA_CLAIM_NO").toString()));
							
							if((i % 50 == 49) || (i % 50 != 49 && i == (returnWithdrawList.size() - 1))){
								requestObj.add("cancelIds", cancelIds);
								responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "POST", new GsonBuilder().create().toJson(requestObj));
								
								if("200".equals(responseObj.get("code").getAsString())){
									data = responseObj.get("data").getAsJsonArray();
									
									for(int j=0; j<data.size(); j++){
										withdraw = data.get(j).getAsJsonObject();
										
										resultWithdraw = new HashMap<String, Object>();
										
										resultWithdraw.put("cancelId"  , withdraw.get("cancelId").getAsString());
										resultWithdraw.put("orderId"   , withdraw.get("orderId").getAsString());
										resultWithdraw.put("vendorId"  , withdraw.get("vendorId").getAsString());
										resultWithdraw.put("createdAt" , withdraw.get("createdAt").isJsonNull() ? DateUtil.toTimestamp(paramMap.getString("startDate")) : DateUtil.toTimestamp(withdraw.get("createdAt").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
										resultWithdraw.put("insertDate", DateUtil.toTimestamp(paramMap.getString("startDate")));
										resultWithdraw.put("paCode"	   , paCode);
										
										paCopnClaimService.saveWithdrawListTx(resultWithdraw);
										
									}
								}else{
									paramMap.put("code"   , "500");
									paramMap.put("message", responseObj.get("message").getAsString());
								}
							}
						}catch(Exception e) {
							paramMap.put("siteGb" , "PACOPN");
							paramMap.put("code"   , "500");
							paramMap.put("message", e.getMessage());
							systemService.insertApiTrackingTx(request, paramMap);
							continue;
						}
					}
				}else{
					paramMap.put("code"     , "404");
					paramMap.put("message"  , getMessage("pa.no_return_data"));
				}
			}
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt) ? "200" : "500");
					paramMap.put("message", (totalCnt == successCnt) ? "OK" : getMessage("errors.api.system"));
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			
			claimCancelMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품철회 생성", notes = "반품철회 생성", httpMethod = "GET")
	@RequestMapping(value = "/claim-cancel", method = RequestMethod.GET)
	@ResponseBody
	public void claimCancelMain(HttpServletRequest request) throws Exception{
		List<Object> claimCancelTargetList        = null;
		HashMap<String, Object> claimCancelTarget = null;
		
		String dupCheck = "";
		String prg_id   = "PACOPN_CLAIM_CANCEL";
		
		log.info("=========================== COPN Claim Cancel Create Start ===========================");
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{prg_id});
			
			claimCancelTargetList = paCopnClaimService.selectClaimCancelTargetList();
			if(claimCancelTargetList.size() > 0){
				for(int i=0; i<claimCancelTargetList.size(); i++){
					try{
						claimCancelTarget = new HashMap<String, Object>();
						claimCancelTarget = (HashMap<String, Object>) claimCancelTargetList.get(i);
						
						paCopnAsyncController.claimCancelAsync(claimCancelTarget, request);
					}catch(Exception e){
						continue;
					}
				}
			}else{
				log.error("Error Msg: " + getMessage("errors.no.select"));
			}
		}catch(Exception e){
			log.error("error msg: " + e.getMessage());
		}finally{
			try {
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", prg_id);
				}
			}catch(Exception e){
				log.error("[CloseHistoryTx Error] " + e.getMessage());
			}
			log.info("=========================== COPN Claim Cancel Create  End  ===========================");
		}
	}
	
	@ApiOperation(value = "직권취소/반품목록조회", notes = "직권취소/반품목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-complete-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnCompleteList(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자[yyyMMdd]")   @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate"  , required = false, value = "종료일자[yyyMMdd]")   @RequestParam(value = "toDate"  , required = false) String toDate) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		
		String dupCheck  = "";
		String startDate = "";
		String endDate   = "";
		
		endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
		
		paramMap.put("apiCode"   , "IF_PACOPNAPI_03_020");
		paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
		paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate" , systemService.getSysdatetimeToString());
		paramMap.put("startDateQ", startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
		paramMap.put("endDateQ"  , endDate.substring(0, 4)   + "-" + endDate.substring(4, 6)   + "-" + endDate.substring(6, 8));
		
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			paramMap.put("status"    , "CC");
			
			for(int count=0; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
				
				paramMap.put("nextToken" , "start");
				if(count == 0) {
					paramMap.put("paName", Constants.PA_BROAD);
					paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
				} else {
					paramMap.put("paName", Constants.PA_ONLINE);
					paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
				}
				
				while(!"".equals(paramMap.getString("nextToken"))){
					procClaimListDay(paramMap, apiInfo);
					if(!"200".equals(paramMap.getString("code"))){
						break;
					}
				}
			}
			
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문취소승인처리 IF_PA11STAPI_03_022
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "주문취소승인처리", notes = "주문취소승인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-approval-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProc(
			   @RequestParam(value = "paClaimNo",		required = true) String paClaimNo,
			   @RequestParam(value = "paProcQty",		required = true) String paProcQty,
			   @RequestParam(value = "paCode",		required = true) String paCode,
			   HttpServletRequest request) throws Exception{
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		
		//String dupCheck = "";
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_022");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());

			paramMap.put("PA_CLAIM_NO", paClaimNo);
			paramMap.put("paClaimNo", paClaimNo);
			paramMap.put("PA_PROC_QTY", paProcQty);
			paramMap.put("PA_CODE", paCode);
			
			//dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			//if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			resultMap = paCopnClaimService.makeCancelProc(apiInfo, paramMap);
			
			paramMap.put("code"   , (Constants.SAVE_SUCCESS.equals(resultMap.getString("result_code"))) ? "200" : "500");
			paramMap.put("message", resultMap.getString("result_text"));
									
		}catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message", e.getMessage());
			log.error(paramMap.getString("message"), e);
		}finally{
			try{
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				//if("0".equals(dupCheck)){
				//	systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				//}
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			log.info("03.취소승인 및 취소거부처리 API END");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 모바일 자동취소 (품절취소반품) IF_PACOPNAPI_03_025
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiIgnore
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderSoldOut(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		JsonObject requestObj    = null;
		JsonArray  vendorItemIds = null;
		JsonArray  receiptCounts = null;
		JsonObject responseObj = null;
		
		String[] apiKeys = null;
		String paName = "";
		String loginId = "";
		
		String dupCheck = "";
		
		String msg = "";
		String paGroupCode = "05";
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		log.info("===== 쿠팡 모바일 자동취소 (품절취소반품) API Start=====");
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_025");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			paramMap.put("paGroupCode", paGroupCode);
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			//= Step 1) 판매취소 List 조회
			List<HashMap<String, String>> cancelList = paCopnClaimService.selectPaMobileOrderAutoCancelList(paramMap);
			
			 if(cancelList.size() > 0){
				 for (HashMap<String, String> cancelMap : cancelList) {
					 try {
						 String paOrderNo 	  = ComUtil.objToStr(cancelMap.get("PA_ORDER_NO"));
						 String paOrderSeq    = ComUtil.objToStr(cancelMap.get("PA_ORDER_SEQ"));
						 String vendorItemId  = ComUtil.objToStr(cancelMap.get("VENDOR_ITEM_ID"));
						 String shippingCount = ComUtil.objToStr(cancelMap.get("SHIPPING_COUNT"));
						 String paCode		  = ComUtil.objToStr(cancelMap.get("PA_CODE"));
						 
						 if(paCode.equals(Constants.PA_COPN_BROAD_CODE)) {
						 	paName = Constants.PA_BROAD;
						 	loginId = ConfigUtil.getString("PA_COPN_BROAD_LOGIN_ID");
						 }
						 else {
						 	paName = Constants.PA_ONLINE;
						 	loginId = ConfigUtil.getString("PA_COPN_ONLINE_LOGIN_ID");
						 }
						 
						 map.put("PA_GROUP_CODE", paGroupCode);
						 map.put("PA_CODE", paCode);
						 map.put("PA_ORDER_NO", paOrderNo);
						 map.put("PA_ORDER_SEQ", paOrderSeq);
						 map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						 map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
					 	 map.put("PROC_ID", Constants.PA_COPN_PROC_ID);
						 
						 apiKeys = apiInfo.get(paName).split(";");
						 
						 requestObj    = new JsonObject();
						 vendorItemIds = new JsonArray();
						 receiptCounts = new JsonArray();
						 
						 vendorItemIds.add(Long.valueOf(vendorItemId));
						 receiptCounts.add(Long.valueOf(shippingCount));
						 
						 requestObj.addProperty("orderId"         , paOrderNo);
						 requestObj.addProperty("bigCancelCode"   , "CANERR");
						 requestObj.addProperty("middleCancelCode", "CCTTER"); // CCTTER 재고 연동 오류 : 재고 문제로 취소가 발생하는 경우
						 requestObj.addProperty("userId"          , loginId);
						 requestObj.addProperty("vendorId"        , apiKeys[0]);
						 requestObj.add("vendorItemIds", vendorItemIds);
						 requestObj.add("receiptCounts", receiptCounts);
						 
						 responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(apiInfo.get("API_URL")
						 		.replaceAll("#vendorId#", apiKeys[0])
						 		.replaceAll("#orderId#" , paOrderNo)), "POST", new GsonBuilder().create().toJson(requestObj));
						 
						 msg = responseObj.get("message").getAsString();
					 } catch(Exception e) {
						 log.info(e.getMessage());
						 msg = e.getMessage();
						 continue;
					 } finally {
						 if("SUCCESS".equals(responseObj.get("code").getAsString()) || "200".equals(responseObj.get("code").getAsString())){
							map.put("REMARK3_N", "10");
							map.put("RSLT_MESSAGE", "모바일자동취소 성공");
							paorderService.updateRemark3NTx(map);
							
							//상담생성 & 문자발송 & 상품품절처리
							paCommonService.savePaMobileOrderCancelTx(map);
						 }else{
							map.put("REMARK3_N", "90");
							map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + msg);
							paorderService.updateRemark3NTx(map);
						 }
					 }
				}
			 }
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , "200");
					paramMap.put("message", "OK");
				}
				paramMap.put("siteGb", "PACOPN");
				systemService.insertApiTrackingTx(request, paramMap);
				
				if("0".equals(dupCheck)){
					systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
				}
			}catch(Exception e){
				log.error("[ApiTracking | CloseHistory Tx Error] " + e.getMessage());
			}
			
			returnListDay(request,"","");
			log.info("===== 쿠팡 모바일 자동취소 (품절취소반품) API End=====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
