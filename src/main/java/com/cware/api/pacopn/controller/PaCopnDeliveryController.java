package com.cware.api.pacopn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PacopnorderlistVO;
import com.cware.netshopping.domain.model.Pacopnorderitemlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacopn.delivery.service.PaCopnDeliveryService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Api(value = "/pacopn/delivery", description="공통")
@Controller("com.cware.api.pacopn.PaCopnDeliveryController")
@RequestMapping(value = "/pacopn/delivery")
public class PaCopnDeliveryController extends AbstractController{
	
	@Resource(name = "com.cware.api.pacopn.PaCopnOrderController")
	private PaCopnOrderController paCopnOrderController;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pacopn.delivery.paCopnDeliveryService")
	private PaCopnDeliveryService paCopnDeliveryService;
	
	@ApiOperation(value = "목록조회", notes = "목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-list-day", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderListDay(HttpServletRequest request,
			@ApiParam(name = "fromDate", required = false, value = "시작일자[yyyMMdd]")   @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate"  , required = false, value = "종료일자[yyyMMdd]")   @RequestParam(value = "toDate"  , required = false) String toDate) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		
		String dupCheck  = "";
		String startDate = "";
		String endDate   = "";
		
		endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.GENERAL_DATE_FORMAT);
		
		paramMap.put("apiCode"   , "IF_PACOPNAPI_03_001");
		paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
		paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
		paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
		paramMap.put("startDate" , systemService.getSysdatetimeToString());
		paramMap.put("startDateQ", startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8));
		paramMap.put("endDateQ"  , endDate.substring(0, 4)   + "-" + endDate.substring(4, 6)   + "-" + endDate.substring(6, 8));
		
		try{
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			for(int i=0; i<2; i++){
				paramMap.put("status", (i == 0) ? "ACCEPT" : "INSTRUCT"); // ACCEPT: 결제완료, INSTRUCT: 상품준비중

				for(int count=0 ; count<Constants.PA_COPN_CONTRACT_CNT; count++) {
					
					paramMap.put("nextToken" , "start");
					if(count == 0) {
						paramMap.put("paName", Constants.PA_BROAD);
						paramMap.put("paCode", Constants.PA_COPN_BROAD_CODE);
					} else {
						paramMap.put("paName", Constants.PA_ONLINE);
						paramMap.put("paCode", Constants.PA_COPN_ONLINE_CODE);
					}
					
					while(!"".equals(paramMap.getString("nextToken"))){
						procOrderListDay(request, paramMap, apiInfo);
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
			
			paCopnOrderController.orderInputMain(request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문 조회 및 저장
	 * 목록 조회 > 상품준비중 처리
	 * @param request
	 * @param paramMap
	 * @param apiInfo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	private ParamMap procOrderListDay(HttpServletRequest request, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception{
		JsonObject responseObj         = null;
		List<JsonObject> orderList     = null;
		JsonObject order               = null;
		Gson gson = new Gson();
		
		URIBuilder builder = null;
		
		ResponseEntity<?> responseEntity = null;
		
		String[] apiKeys = null;
		
		String nextToken = paramMap.getString("nextToken");
		
		try{
			
			apiKeys = apiInfo.get(paramMap.getString("paName")).split(";");
			
			builder = new URIBuilder(apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0]))
			.addParameter("createdAtFrom", paramMap.getString("startDateQ"))
			.addParameter("createdAtTo"  , paramMap.getString("endDateQ"))
			.addParameter("status"       , paramMap.getString("status"))
			.addParameter("maxPerPage"   , Constants.PA_COPN_COUNT_PER_PAGE);
			
			if(nextToken != null && !"".equals(nextToken) && !"start".equals(nextToken)){
				builder.addParameter("nextToken", nextToken);
			}
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paramMap.getString("paName"), builder);
			
			if("200".equals(responseObj.get("code").getAsString())){
				orderList = (ArrayList<JsonObject>) gson.fromJson(responseObj.get("data").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());
				
				paramMap.put("nextToken", responseObj.get("nextToken").getAsString());
				if(orderList.size() > 0){
					for(int i=0; i<orderList.size(); i++){
						order = orderList.get(i).getAsJsonObject();
						
						// 수취인 연락처(안심번호) 미채번시 pass
						if("".equals(order.get("receiver").getAsJsonObject().get("safeNumber").getAsString())) continue;
						
						if("INSTRUCT".equals(paramMap.getString("status"))){
							//부분 출고 시 shipmentBoxId 값이 변경되어 주문이 두번 생성되는 이슈 차단.
							//상품 준비중 처리 시 쿠팡에서 에러 나오는 경우가 있어 다시 살림. 위에 케이스 해결하기 위에 안쪽에서 중복 체크
							savePaOrderList(paramMap, order);
							
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
						}else{
							responseEntity = shippingReadyProc(request, order.get("shipmentBoxId").getAsString(), paramMap.getString("paName"));
							if("200".equals(PropertyUtils.describe(responseEntity.getBody()).get("code"))){
								savePaOrderList(paramMap, order);
								
								paramMap.put("code"   , "200");
								paramMap.put("message", "OK");
							}else{
								paramMap.put("code"     , "500");
								paramMap.put("message"  , "상품준비중처리 실패");
							}
						}
					}
				}else{
					paramMap.put("code"     , "404");
					paramMap.put("message"  , getMessage("pa.no_return_data"));
					paramMap.put("nextToken", "");
				}
			}else{
				paramMap.put("code"     , "500");
				paramMap.put("message"  , getMessage("errors.api.system"));
				paramMap.put("nextToken", "");
			}
		}catch(Exception e){
			paramMap.put("code"     , "500");
			paramMap.put("message"  , e.getMessage());
			paramMap.put("nextToken", "");
			return paramMap;
		}
		return paramMap;
	}
	
	/**
	 * 주문 저장(중계 테이블)
	 * @param paramMap
	 * @param order
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	private String savePaOrderList(ParamMap paramMap, JsonObject order){
		List<JsonObject> orderItemList = null;
		JsonObject orderItem           = null;
		Gson gson = new Gson();
		
		List<Pacopnorderitemlist> paCopnOrderitemList = null;
		PacopnorderlistVO     paCopnOrder     = null;
		Pacopnorderitemlist paCopnOrderitem = null;
		
		Timestamp sysdateTime = null;
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		try {
			sysdateTime = DateUtil.toTimestamp(paramMap.getString("startDate"));
			
			paCopnOrder = new PacopnorderlistVO();
			
			paCopnOrder.setPaCode				 (paramMap.getString("paCode"));
			paCopnOrder.setShipmentBoxId         (order.get("shipmentBoxId").getAsString());
			paCopnOrder.setOrderId               (order.get("orderId").getAsString());
			paCopnOrder.setOrderedAt             (DateUtil.toTimestamp(order.get("orderedAt").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
			paCopnOrder.setOrdererName           (order.get("orderer").getAsJsonObject().get("name").getAsString());
			paCopnOrder.setOrdererEmail          (order.get("orderer").getAsJsonObject().get("email").getAsString());
			paCopnOrder.setOrdererSafeNumber     (order.get("orderer").getAsJsonObject().get("safeNumber").getAsString());
			paCopnOrder.setPaidAt                (DateUtil.toTimestamp(order.get("paidAt").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
			paCopnOrder.setStatus                (order.get("status").getAsString());
			paCopnOrder.setShippingPrice         (order.get("shippingPrice").isJsonNull() ? 0 : order.get("shippingPrice").getAsLong());
			paCopnOrder.setRemotePrice           (order.get("remotePrice").isJsonNull() ? 0 : order.get("remotePrice").getAsLong());
			paCopnOrder.setRemoteArea            ((order.get("remoteArea").getAsBoolean() == true) ? "1" : "0");
			paCopnOrder.setParcelPrintMessage    (order.get("parcelPrintMessage").getAsString());
			paCopnOrder.setSplitShipping         ((order.get("splitShipping").getAsBoolean() == true) ? "1" : "0");
			paCopnOrder.setAbleSplitShipping     ((order.get("ableSplitShipping").getAsBoolean() == true) ? "1" : "0");
			paCopnOrder.setReceiverName          (order.get("receiver").getAsJsonObject().get("name").getAsString());
			paCopnOrder.setReceiverSafeNumber    (order.get("receiver").getAsJsonObject().get("safeNumber").getAsString());
			paCopnOrder.setReceiverAddr1         (order.get("receiver").getAsJsonObject().get("addr1").getAsString());
			paCopnOrder.setReceiverAddr2         (order.get("receiver").getAsJsonObject().get("addr2").getAsString());
			paCopnOrder.setReceiverPostCode      (order.get("receiver").getAsJsonObject().get("postCode").getAsString().replaceAll("-", ""));
			paCopnOrder.setOverseaShippingInfoDTO(order.get("overseaShippingInfoDto").toString());
			paCopnOrder.setDeliveryCompanyName   (order.get("deliveryCompanyName").getAsString());
			paCopnOrder.setInvoiceNumber         (order.get("invoiceNumber").isJsonNull() ? "" : order.get("invoiceNumber").getAsString());
			//paCopnOrder.setInTrasitDateTime      (DateUtil.toTimestamp(order.get("inTrasitDateTime").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
			//paCopnOrder.setDeliveredDate         (DateUtil.toTimestamp(order.get("deliveredDate").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
			paCopnOrder.setRefer                 (order.get("refer").getAsString());
			paCopnOrder.setModifyDate            (sysdateTime);
			paCopnOrder.setInsertDate            (sysdateTime);
			
			
			/* Order Item Setting */
			orderItemList       = (ArrayList<JsonObject>) gson.fromJson(order.get("orderItems").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());
			paCopnOrderitemList = new ArrayList<Pacopnorderitemlist>();
			
			for(int j=0; j<orderItemList.size(); j++){
				orderItem = orderItemList.get(j).getAsJsonObject();
				
				paCopnOrderitem = new Pacopnorderitemlist();
				
				paCopnOrderitem.setShipmentBoxId             (order.get("shipmentBoxId").getAsString());
				paCopnOrderitem.setOrderId                   (order.get("orderId").getAsString());
				paCopnOrderitem.setItemSeq                   (ComUtil.lpad(Integer.toString(j+1), 3, "0"));
				paCopnOrderitem.setVendorItemId              (orderItem.get("vendorItemPackageId").getAsString());
				paCopnOrderitem.setVendorItemPackageName     (orderItem.get("vendorItemPackageName").getAsString());
				paCopnOrderitem.setProductId                 (orderItem.get("productId").getAsString());
				paCopnOrderitem.setVendorItemId              (orderItem.get("vendorItemId").getAsString());
				paCopnOrderitem.setVendorItemName            (orderItem.get("vendorItemName").getAsString());
				paCopnOrderitem.setShippingCount             (orderItem.get("shippingCount").isJsonNull() ? 0 : orderItem.get("shippingCount").getAsLong());
				paCopnOrderitem.setSalesPrice                (orderItem.get("salesPrice").isJsonNull() ? 0 : orderItem.get("salesPrice").getAsLong());
				paCopnOrderitem.setOrderPrice                (orderItem.get("orderPrice").isJsonNull() ? 0 : orderItem.get("orderPrice").getAsLong());
				paCopnOrderitem.setDiscountPrice             (orderItem.get("discountPrice").isJsonNull() ? 0 : orderItem.get("discountPrice").getAsLong());
				paCopnOrderitem.setExternalVendorSkuCode     (orderItem.get("externalVendorSkuCode").isJsonNull() ? "" : orderItem.get("externalVendorSkuCode").getAsString());
				paCopnOrderitem.setEtcInfoHeader             (orderItem.get("etcInfoHeader").isJsonNull() ? "" : orderItem.get("etcInfoHeader").getAsString());
				paCopnOrderitem.setEtcInfoValue              (orderItem.get("etcInfoValue").isJsonNull() ? "" : orderItem.get("etcInfoValue").getAsString());
				paCopnOrderitem.setEtcInfoValues             (orderItem.get("etcInfoValues").isJsonNull() ? "" : orderItem.get("etcInfoValues").getAsString());
				paCopnOrderitem.setSellerProductId           (orderItem.get("sellerProductId").isJsonNull() ? "" : orderItem.get("sellerProductId").getAsString());
				paCopnOrderitem.setSellerProductName         (orderItem.get("sellerProductName").isJsonNull() ? "" : orderItem.get("sellerProductName").getAsString());
				paCopnOrderitem.setSellerProductItemName     (orderItem.get("sellerProductItemName").isJsonNull() ? "" : orderItem.get("sellerProductItemName").getAsString());
				paCopnOrderitem.setFirstSellerProductItemName(orderItem.get("firstSellerProductItemName").isJsonNull() ? "" : orderItem.get("firstSellerProductItemName").getAsString());
				paCopnOrderitem.setCancelCount               (orderItem.get("cancelCount").isJsonNull() ? 0 : orderItem.get("cancelCount").getAsLong());
				paCopnOrderitem.setHoldCountForCancel        (orderItem.get("holdCountForCancel").isJsonNull() ? 0 : orderItem.get("holdCountForCancel").getAsLong());
				paCopnOrderitem.setEstimatedShippingDate     (DateUtil.toTimestamp(orderItem.get("estimatedShippingDate").getAsString(), DateUtil.COPN_DATE_FORMAT));
				//paCopnOrderitem.setPlannedShippingDate       (DateUtil.toTimestamp(orderItem.get("plannedShippingDate").getAsString(), DateUtil.COPN_DATE_FORMAT));
				//paCopnOrderitem.setInvoiceNumberUploadDate   (DateUtil.toTimestamp(orderItem.get("invoiceNumberUploadDate").getAsString(), DateUtil.COPN_T_DATETIME_FORMAT));
				paCopnOrderitem.setExtraProperties           (orderItem.get("extraProperties").toString());
				paCopnOrderitem.setPricingBadge              ((orderItem.get("pricingBadge").getAsBoolean() == true) ? "1" : "0");
				paCopnOrderitem.setUsedProduct               ((orderItem.get("usedProduct").getAsBoolean() == true) ? "1" : "0");
				//paCopnOrderitem.setConfirmDate               (DateUtil.toTimestamp(orderItem.get("confirmDate").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
				paCopnOrderitem.setDeliveryChargeTypeName    (orderItem.get("deliveryChargeTypeName").getAsString());
				paCopnOrderitem.setCanceled                  ((orderItem.get("canceled").getAsBoolean() == true) ? "1" : "0");
				paCopnOrderitem.setInsertDate                (sysdateTime);
				//즉시할인쿠폰 추가
				paCopnOrderitem.setInstantCouponDiscount	 (orderItem.get("instantCouponDiscount").isJsonNull() ? 0 : orderItem.get("instantCouponDiscount").getAsLong());
				
				paCopnOrderitemList.add(paCopnOrderitem);
			}
			
			rtnMsg = paCopnDeliveryService.savePaCopnOrderTx(paCopnOrder, paCopnOrderitemList);
			
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			e.printStackTrace();
			return rtnMsg;
		}
		return rtnMsg;
	}
	
	@ApiOperation(value = "상품준비중처리", notes="상품준비중처리")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-ready-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> shippingReadyProc(HttpServletRequest request,
			@ApiParam(name = "shipmentBoxId", required = true, value = "배송번호") @RequestParam(value = "shipmentBoxId", required = true) String shipmentBoxId,
			@ApiParam(name = "paName"  		, required = true, value = "paName") @RequestParam(value = "paName"  	  , required = true) String paName) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap = new ParamMap();
		
		JsonObject requestObj    = null;
		JsonObject responseObj   = null;
		JsonArray shipmentBoxIds = null;
		JsonObject data          = null;
		JsonArray responseList   = null;
		JsonObject response      = null;
		
		String[] apiKeys = null;
		
		String dupCheck = null;
		
		try{
			paramMap.put("apiCode"  , "IF_PACOPNAPI_03_002");
			paramMap.put("procId"   , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode"   , Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			log.info("[상품준비중처리] 02.API 중복실행 검사");
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			apiKeys = apiInfo.get(paName).split(";");
			
			shipmentBoxIds = new JsonArray();
			shipmentBoxIds.add(Long.valueOf(shipmentBoxId));
			
			requestObj = new JsonObject();
			requestObj.addProperty("vendorId", apiKeys[0]);
			requestObj.add("shipmentBoxIds", shipmentBoxIds);
			
			responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
					apiInfo.get("API_URL").replaceAll("#vendorId#", apiKeys[0])), "PATCH", new GsonBuilder().create().toJson(requestObj));
			
			if("200".equals(responseObj.get("code").getAsString())){
				data = responseObj.get("data").getAsJsonObject();
				
				if("0".equals(data.get("responseCode").getAsString())){
					responseList = data.get("responseList").getAsJsonArray();
					
					if(responseList.size() > 0){
						response = responseList.get(0).getAsJsonObject();
						
						if(response.get("succeed").getAsBoolean() == true){
							paramMap.put("code"   , "200");
							paramMap.put("message", "OK");
						}else{
							paramMap.put("code"   , "500");
							paramMap.put("message", getMessage("errors.api.system"));
						}
					}else{
						paramMap.put("code"   , "500");
						paramMap.put("message", getMessage("errors.no.select"));
					}
				}else{
					paramMap.put("code"   , "500");
					paramMap.put("message", data.get("responseMessage").getAsString());
				}
			}else{
				paramMap.put("code"   , "500");
				paramMap.put("message", responseObj.get("message").getAsString());
			}
				
		}catch(Exception e){
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "송장업로드처리", notes = "송장업로드처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-invoice")
	@ResponseBody
	public ResponseEntity<?> shippingInvoice(HttpServletRequest request) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		
		List<Object> beforeProcessingList = null;
		HashMap<String, Object> beforeProcessingInvoice = null; 
		
		String dupCheck = "";
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"  , "IF_PACOPNAPI_03_003");
			paramMap.put("procId"   , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode"   , Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			log.info("[송장업로드처리] 02.API 중복실행 검사");
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			beforeProcessingList = paCopnDeliveryService.selectBeforeInvoiceList();
			totalCnt = beforeProcessingList.size();
			
			if(beforeProcessingList.size() > 0){
				for(int i=0; i<beforeProcessingList.size(); i++){
					beforeProcessingInvoice = (HashMap<String, Object>) beforeProcessingList.get(i);
					
					resultMap = paCopnDeliveryService.shippingInvoiceProc(apiInfo, beforeProcessingInvoice);
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
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
		
		
		shippingInvoiceUpdate(request); //운송장 수정 (추후에 스프링 배치로 분리하던 현상태 유지하던 결정해야함)
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "송장업데이트처리", notes = "송장업데이트처리", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-invoice-update")
	@ResponseBody
	public ResponseEntity<?> shippingInvoiceUpdate(HttpServletRequest request) throws Exception{
		ParamMap paramMap  							= new ParamMap();
		HashMap<String, String> apiInfo 			= null;
		List<Map<String, Object>> slipUpdateList 	= null;
		String dupCheck 							= "";
		ParamMap resultMap 							= new ParamMap();
		int successCnt 								= 0;
		int totalCnt   								= 0;
		
		try{
			//1)Get API Info(URL ETC...)
			paramMap.put("apiCode"  	, "IF_PACOPNAPI_03_004");
			paramMap.put("procId"   	, Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode"	, Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode"   , Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("paName"   	, Constants.PA_BROAD);
			paramMap.put("startDate"	, systemService.getSysdatetimeToString());
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			//2)Select Shipping Update List
			slipUpdateList = paCopnDeliveryService.selectShippingUpdateList();
			totalCnt 	   = slipUpdateList.size();
			
			for(Map<String, Object> orderInvoice : slipUpdateList) {
				//3)Select Shipping Update Detail_List and Send Information to COPN
				resultMap = paCopnDeliveryService.shippingInvoiceUpdateProc(apiInfo, orderInvoice);	
				if(Constants.SAVE_SUCCESS.equals(resultMap.getString("rtnMsg"))) successCnt++;
			}
			
		}catch (Exception e) {
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);			
		}finally{
			
			try{
				paramMap.put("code"   	, (totalCnt == successCnt) ? "200" : "500");
				paramMap.put("message"	, "운송장 변경 - 대상 : " + totalCnt + "  성공 : "+ successCnt);
				paramMap.put("siteGb"	, "PACOPN");
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
	
	
	/*
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "송장업데이트처리(미사용)", notes = "송장업데이트처리(미사용)", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-invoice-update")
	@ResponseBody
	public ResponseEntity<?> shippingInvoiceUpdate(HttpServletRequest request) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		
		List<Object> beforeProcessingList = null;
		HashMap<String, Object> beforeProcessingInvoice = null; 
		
		String dupCheck = "";
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"  , "IF_PACOPNAPI_03_004");
			paramMap.put("procId"   , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode", Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode"   , Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("paName"   , Constants.PA_BROAD);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			beforeProcessingList = paCopnDeliveryService.selectBeforeInvoiceList();
			totalCnt = beforeProcessingList.size();
			
			if(beforeProcessingList.size() > 0){
				for(int i=0; i<beforeProcessingList.size(); i++){
					beforeProcessingInvoice = (HashMap<String, Object>) beforeProcessingList.get(i);
					
					resultMap = paCopnDeliveryService.shippingInvoiceProc(apiInfo, beforeProcessingInvoice);
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
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
	*/
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "장기미배송 배송완료 처리", notes = "장기미배송 배송완료 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-complete-proc")
	@ResponseBody
	public ResponseEntity<?> shippingCompleteProc(HttpServletRequest request) throws Exception{
		HashMap<String, String> apiInfo = null;
		ParamMap paramMap  = new ParamMap();
		ParamMap resultMap = new ParamMap();
		
		List<Object> shippingCompleteList = null;
		HashMap<String, Object> shippingComplete = null;
		
		String dupCheck = "";
		
		int totalCnt   = 0;
		int successCnt = 0;
		
		try{
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_008");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			paramMap.put("addFromDay", Constants.PA_COPN_SHIPPING_COMPLETE_ADD_FROM_DAY);
			paramMap.put("addToDay"  , Constants.PA_COPN_SHIPPING_COMPLETE_ADD_TO_DAY);
			log.info("[장기미배송 배송완료 처리] 02.API 중복실행 검사");
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			shippingCompleteList = paCopnDeliveryService.selectShippingComplete(paramMap);
			totalCnt = shippingCompleteList.size();
			
			if(shippingCompleteList.size() > 0){
				for(int i=0; i<shippingCompleteList.size(); i++){
					shippingComplete = (HashMap<String, Object>) shippingCompleteList.get(i);
					
					resultMap = paCopnDeliveryService.shippingCompleteProc(apiInfo, shippingComplete);
					if(Constants.SAVE_SUCCESS.equals(resultMap.getString("rtnMsg"))){
						successCnt++;
					}
				}
			}else{
				paramMap.put ("code"   , "404");
				paramMap.put("message", getMessage("errors.no.select"));
			}
		}catch(Exception e){
			paramMap.put("code"   , "500");
			paramMap.put("message", e.getMessage());
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
	
	@ApiOperation(value = "배송완료목록 조회", notes = "배송완료목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/shipping-complete-auto", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings({ "unchecked", "serial" })
	public ResponseEntity<?> shippingCompleteAuto(HttpServletRequest request) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = null;
		HashMap<String, String> retrieveInfo = null;
		
		String paName = null;
		String[] apiRetrieveKeys = null;
		String[] apiSaveKeys = null;
		
		JsonObject responseObj  = null;
		JsonObject delivery     = null;
		
		List<HashMap<String, Object>> completeList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object>       complete     = null;
		Paorderm paOrderm = null;
		
		StringBuffer buffer = new StringBuffer();
		
		String dupCheck  = "";
		String rtnMsg    = Constants.SAVE_SUCCESS;
		
		int totalCnt   = 0;
		int successCnt = 0;
		int waitCnt    = 0;
		
		try{
			
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_023");
			paramMap.put("procId"    , Constants.PA_COPN_PROC_ID);
			paramMap.put("broadCode" , Constants.PA_COPN_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_COPN_ONLINE_CODE);
			paramMap.put("startDate" , systemService.getSysdatetimeToString());
			
			retrieveInfo = systemService.selectPaApiInfo(paramMap);
			retrieveInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			paramMap.put("apiCode"   , "IF_PACOPNAPI_03_019");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			
			dupCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if("1".equals(dupCheck)) throw processException("msg.batch_process_duplicated", new String[]{paramMap.getString("apiCode")});
			
			completeList = paCopnDeliveryService.selectDeliveryCompleteList();
			
			totalCnt = completeList.size();
			
			if(totalCnt >= 1){
				for(int i=0; i<completeList.size(); i++){
					complete = (HashMap<String, Object>) completeList.get(i);
					
					if(complete.get("PA_CODE").toString().equals(Constants.PA_COPN_BROAD_CODE)) {
						paName = Constants.PA_BROAD;
					} else {
						paName = Constants.PA_ONLINE;
					}
					apiRetrieveKeys = retrieveInfo.get(paName).split(";");
					apiSaveKeys = retrieveInfo.get(paName).split(";");
					
					List<JsonObject> orderList     = null;
					JsonObject order               = null;
					List<JsonObject> orderItemList = null;
					Gson gson = new Gson();
					
					URIBuilder builder = null;
					
					try{						
						builder = new URIBuilder(retrieveInfo.get("API_URL").replaceAll("#vendorId#", apiRetrieveKeys[0]).replaceAll("#orderId#", complete.get("PA_ORDER_NO").toString()));
						
						responseObj = ComUtil.callPaCopnAPI(retrieveInfo, paName, builder);
						
						if("200".equals(responseObj.get("code").getAsString())){
							orderList = (ArrayList<JsonObject>) gson.fromJson(responseObj.get("data").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());
							
							if(orderList.size() > 0){
								for(int j=0; j<orderList.size(); j++){
									order = orderList.get(j).getAsJsonObject();
									
									orderItemList = (ArrayList<JsonObject>) gson.fromJson(order.get("orderItems").toString(), new TypeToken<ArrayList<JsonObject>>(){}.getType());
									for(int k=0; k<orderItemList.size(); k++) {
										if(complete.get("VENDOR_ITEM_ID").toString().equals(orderItemList.get(k).get("vendorItemId").getAsString())){
											
											responseObj = ComUtil.callPaCopnAPI(apiInfo, paName, new URIBuilder(
													apiInfo.get("API_URL")
													.replaceAll("#vendorId#"     , apiSaveKeys[0])
													.replaceAll("#shipmentBoxId#", order.get("shipmentBoxId").getAsString().toString())));
											
											if(("200".equals(responseObj.get("code").getAsString()) && "FINAL_DELIVERY".equals(responseObj.get("data").getAsJsonObject().get("status").getAsString())) ||
													("200".equals(responseObj.get("code").getAsString()) && "NONE_TRACKING".equals(responseObj.get("data").getAsJsonObject().get("status").getAsString())) ||
													("400".equals(responseObj.get("code").getAsString()) && "해당 주문이 취소 또는 반품되었습니다.".equals(responseObj.get("message").toString().replaceAll("\"", "")))){
												
												if("200".equals(responseObj.get("code").getAsString())){
													delivery = responseObj.get("data").getAsJsonObject();
												}
												
												paOrderm = new Paorderm();
												
												paOrderm.setPaOrderNo       ("400".equals(responseObj.get("code").getAsString()) ? complete.get("PA_ORDER_NO").toString() : NVL(delivery.get("orderId")));
												paOrderm.setPaOrderSeq      (complete.get("PA_ORDER_SEQ").toString());
												paOrderm.setPaShipNo        (complete.get("PA_SHIP_NO").toString());
												paOrderm.setProcDate        ("400".equals(responseObj.get("code").getAsString()) ? DateUtil.toTimestamp(paramMap.getString("startDate")) : DateUtil.toTimestamp(delivery.get("deliveredDate").getAsString(), DateUtil.COPN_DATETIME_FORMAT));
												paOrderm.setApiResultCode   (Constants.SAVE_SUCCESS);
												paOrderm.setApiResultMessage("400".equals(responseObj.get("code").getAsString()) ? "ALREADY_CANCEL_RETURN(취소/반품 처리로 트래킹 불가)" : 
													"FINAL_DELIVERY".equals(delivery.get("status").getAsString()) ? "배송완료 처리 성공" : "NONE_TRACKING(쿠팡추적불가)");
												paOrderm.setModifyId        (Constants.PA_COPN_PROC_ID);
												
												rtnMsg = paCopnDeliveryService.updateDeliveryCompleteTx(paOrderm);
												
												if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
													paramMap.put("code"   , "200");
													paramMap.put("message", "OK");
													successCnt++;
												}
											}else if("200".equals(responseObj.get("code").getAsString())){
												paramMap.put("code"   , "200");
												paramMap.put("message", "OK");
												waitCnt++;
											}else{
												paramMap.put("code"   , "500");
												paramMap.put("message", responseObj.get("message").toString());
												buffer.append((buffer.length() == 0 ? "PA_SHIP_NO: " : ", ") + complete.get("PA_SHIP_NO").toString());
											}
										}
									}
								}
							}else{
								paramMap.put("code", "404");
								paramMap.put("message", getMessage("pa.no_return_data"));
							}
						}else if ("400".equals(responseObj.get("code").getAsString()) && "해당 주문이 취소 또는 반품 되었습니다.".equals(responseObj.get("message").toString().replaceAll("\"", ""))) {
							paOrderm = new Paorderm();
							
							paOrderm.setPaOrderNo       (complete.get("PA_ORDER_NO").toString());
							paOrderm.setPaOrderSeq      (complete.get("PA_ORDER_SEQ").toString());
							paOrderm.setPaShipNo        (complete.get("PA_SHIP_NO").toString());
							paOrderm.setProcDate        (DateUtil.toTimestamp(paramMap.getString("startDate")));
							paOrderm.setApiResultCode   (Constants.SAVE_SUCCESS);
							paOrderm.setApiResultMessage("ALREADY_CANCEL_RETURN(취소/반품 처리로 트래킹 불가)");
							paOrderm.setModifyId        (Constants.PA_COPN_PROC_ID);
							
							rtnMsg = paCopnDeliveryService.updateDeliveryCompleteTx(paOrderm);
							
							if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
								paramMap.put("code"   , "200");
								paramMap.put("message", "OK");
								successCnt++;
							}
						}else{
							paramMap.put("code"     , "500");
							paramMap.put("message"  , responseObj.get("message").toString());
						}
					}catch(Exception e){
						paramMap.put("code"     , "500");
						paramMap.put("message"  , e.getMessage());
					}
				}
			}
			
		}catch(Exception e){
			paramMap.put("code"   , ("1".equals(dupCheck)) ? "490" : "500");
			paramMap.put("message", ("1".equals(dupCheck)) ? getMessage("errors.api.duplicate") : e.getMessage());
		}finally{
			try{
				if(!"490".equals(paramMap.getString("code")) && !"404".equals(paramMap.getString("code"))){
					paramMap.put("code"   , (totalCnt == successCnt + waitCnt) ? "200" : "500");
					paramMap.put("message", "전체: " + totalCnt + " | 성공: " + successCnt + " | 대기: "+ waitCnt + " | 실패: " + buffer.toString());
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
	
	private String NVL(JsonElement jsonElement){
		return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
	}
}