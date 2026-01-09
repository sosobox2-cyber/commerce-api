package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.message.ProductOrderInfoMsg;
import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub;
import com.cware.api.panaver.order.seller.SellerServiceStub.AccessCredentialsType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayedDispatchReasonType;
import com.cware.api.panaver.order.seller.SellerServiceStub.DeliveryMethodType;
import com.cware.api.panaver.order.seller.SellerServiceStub.GiftReceivingStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderResponseE;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.delivery.service.PaNaverDeliveryService;

@Controller("com.cware.api.panaver.PaNaverDeliveryController")
@RequestMapping("/panaver/delivery")
public class PaNaverDeliveryController extends AbstractController {
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaNaverDeliveryService paNaverDeliveryService;
		
	@Autowired
	private PaNaverInfoCommonController paNaverInfoCommonController;
	
	@ApiOperation(value = "발주 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "발주 처리/적재 성공"), 
    						@ApiResponse(code = 400, message = "발주 처리 요청 실패"),
    						@ApiResponse(code = 500, message = "오류가 발생하였습니다.") })
	@ApiImplicitParams({
        @ApiImplicitParam(name = "productOrderID", value = "상품주문번호", required = true, dataType = "string", paramType = "query", defaultValue = ""),
        @ApiImplicitParam(name = "checkReceiverAddressChanged", value = "배송지 정보 수정 여부", required = false, dataType = "boolean", paramType = "query", defaultValue = "")
	})
	@RequestMapping(value = "/order-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> requestOrderConfirmProc(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "productOrderID", required = true) ChangedProductOrderInfo changedProductOrderInfo,
			@RequestParam(value = "checkReceiverAddressChanged", required = false) boolean checkReceiverAddressChanged) throws Exception {
		
		
		String tx = "";
		ParamMap paramMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_003");
		
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			ResponseEntity<ProductOrderInfoMsg> responseMsg = paNaverInfoCommonController.retrieveOrderDetailInfo(httpServletRequest, changedProductOrderInfo.getProductOrderID());
			
			if(responseMsg.getStatusCode().equals(HttpStatus.OK) && responseMsg.getBody().getProductOrderInfo() != null) {
				if(responseMsg.getBody().getProductOrderInfo()[0].getProductOrder().getGiftReceivingStatus() == null || (responseMsg.getBody().getProductOrderInfo()[0].getProductOrder().getGiftReceivingStatus() != null && responseMsg.getBody().getProductOrderInfo()[0].getProductOrder().getGiftReceivingStatus().equals(GiftReceivingStatusType.RECEIVED))) {
					
					try {
						tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
						if (tx.equals("1")) {
							throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
						}
						
						paramMap.put("startDate", systemService.getSysdatetimeToString());
						
						//SellerServiceStub stub = new SellerServiceStub();
						PlaceProductOrderRequestE requestE = new PlaceProductOrderRequestE();
						PlaceProductOrderRequest request = new PlaceProductOrderRequest();
						AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
						NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("PlaceProductOrder");
						
						accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
						accessCredentialsType.setSignature(naverSignature.getSignature());
						accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
						
						request.setAccessCredentials(accessCredentialsType);
						request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
						request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
						request.setRequestID(UUID.randomUUID().toString());
						
						request.setProductOrderID(changedProductOrderInfo.getProductOrderID());
						
						requestE.setPlaceProductOrderRequest(request);
						
						PlaceProductOrderResponseE responseE = stub.placeProductOrder(requestE);
						
						if("SUCCESS".equals(responseE.getPlaceProductOrderResponse().getResponseType())) {
							//tapitracking.message에 requestId 넣기
							log.debug("GetProductOrderInfoList requestId :: {} succeed", responseE.getPlaceProductOrderResponse().getRequestID());
							
							if(paNaverDeliveryService.updateOrderChangePlaceOrderTx(responseMsg.getBody().getProductOrderInfo()[0], responseMsg.getBody().getNaverSignature(), changedProductOrderInfo) > 0) {
								paramMap.put("code", "200");
								paramMap.put("message", "발주 요청, 저장 성공 " + responseE.getPlaceProductOrderResponse().getRequestID() + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
								paramMap.put("resultCode", "00"); 
								paramMap.put("resultMessage", paramMap.get("message"));
							}
							else {
								paramMap.put("code", "500");
								paramMap.put("message", "발주 요청 성공, 저장 실패 " + responseE.getPlaceProductOrderResponse().getRequestID() + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
								paramMap.put("resultCode", "99");
								paramMap.put("resultMessage", paramMap.getString("message"));
								return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
							}
						}
						else {
							log.error("requestOrderConfirmProc requestId :: {} failed", responseE.getPlaceProductOrderResponse().getRequestID() + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
							paramMap.put("code", "400");
							paramMap.put("message",  "발주 처리 실패 (naverOrderID " + changedProductOrderInfo.getOrderID()+ ")" + " " + responseE.getPlaceProductOrderResponse().getRequestID() + " " + responseE.getPlaceProductOrderResponse().getError().getMessage());
							paramMap.put("resultCode", "99");
							paramMap.put("resultMessage", "requestOrderConfirmProc request failed");
							return new ResponseEntity<ResponseMsg>(new ResponseMsg("400", paramMap.getString("message")), HttpStatus.BAD_REQUEST);
						}
						
					} catch (Exception e) {
						log.error("message", e);
						paramMap.put("code", "500");
						paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
						paramMap.put("resultCode", "99");
						paramMap.put("resultMessage", paramMap.getString("message"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
					} finally {
						if(paramMap.getString("code") == null) {
							paramMap.put("code", "200");
							paramMap.put("message", "발주 처리 대상 없음");
							paramMap.put("resultCode", "00");
							paramMap.put("resultMessage", paramMap.getString("message"));
						}
						try {
							systemService.insertApiTrackingTx(httpServletRequest, paramMap);
						}
						catch(Exception e) {
							log.error("ApiTracking Insertion Error : {}", e.getMessage());
						}
						if (tx.equals("0")) {
							systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
						}
					}
				}
				else {
					if(paNaverDeliveryService.updateOrderChangePlaceOrderTx(responseMsg.getBody().getProductOrderInfo()[0], responseMsg.getBody().getNaverSignature(), changedProductOrderInfo) > 0) {
						paramMap.put("code", "200");
						paramMap.put("message", "선물미수락 저장 성공" + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
						paramMap.put("resultCode", "00"); 
						paramMap.put("resultMessage", paramMap.get("message"));
					}
					else {
						paramMap.put("code", "500");
						paramMap.put("message", "선물미수락 저장 실패" + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
						paramMap.put("resultCode", "99");
						paramMap.put("resultMessage", paramMap.getString("message"));
						return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
			else {
				log.error("GetProductOrderInfoList while placeOrder failed");
				paramMap.put("code", "400");
				paramMap.put("message", "발주처리 전 주문상세내역 조회 에러"  + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
				paramMap.put("resultCode", "99");
				paramMap.put("resultMessage", "발주처리 전 주문상세내역 조회 에러");
				return new ResponseEntity<ResponseMsg>(new ResponseMsg("400", paramMap.getString("message")), HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.error("requestOrderConfirmProc failed productOrderID : " + changedProductOrderInfo.getProductOrderID());
			paramMap.put("code", "500");
			paramMap.put("message", "발주 처리 실패" + "(naverOrderID " + changedProductOrderInfo.getOrderID()+ ")");
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", "발주 처리 실패");
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("400", paramMap.getString("message")), HttpStatus.BAD_REQUEST);
		}finally{
			
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", paramMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "발송 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "발송 처리/적재 성공"), 
    						@ApiResponse(code = 400, message = "발송 처리 요청 실패"),
    						@ApiResponse(code = 500, message = "오류가 발생하였습니다.") })
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest httpServletRequest) throws Exception {
		String tx = "";
		ParamMap paramMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_005");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		HashMap<String, Object> procMap = null;
		String trackingNumber = "";
		String dispatchDate ="";
		StringBuffer sb = new StringBuffer();
		
		SellerServiceStub stub = new SellerServiceStub();

		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}

			List<HashMap<String, Object>> slipProcList = paNaverDeliveryService.selectSlipProcList();

			for(int i=0; i<slipProcList.size(); i++){
				try{
					procMap = slipProcList.get(i);
//					SellerServiceStub stub = new SellerServiceStub();
					ShipProductOrderRequestE requestE = new ShipProductOrderRequestE();
					ShipProductOrderRequest request = new ShipProductOrderRequest();
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ShipProductOrder");

					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

					request.setAccessCredentials(accessCredentialsType);
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setRequestID(UUID.randomUUID().toString());

					request.setProductOrderID(procMap.get("PRODUCT_ORDER_ID").toString());
					request.setDeliveryMethodCode(DeliveryMethodType.DELIVERY);
					request.setDeliveryCompanyCode(procMap.get("DELIVERY_COMPANY_CODE").toString());

					trackingNumber = procMap.get("TRACKING_NUMBER").toString();
					if(null != trackingNumber && !trackingNumber.equals("")) request.setTrackingNumber(trackingNumber);
					dispatchDate = procMap.get("DISPATCH_DATE").toString();
					if(null != dispatchDate && !dispatchDate.equals("")) {
						Calendar dispatchCalendar = Calendar.getInstance();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
						dispatchCalendar.setTime(dateFormat.parse(dispatchDate));
						request.setDispatchDate(dispatchCalendar);
					}

					requestE.setShipProductOrderRequest(request);

					ShipProductOrderResponseE responseE = stub.shipProductOrder(requestE);

					if("SUCCESS".equals(responseE.getShipProductOrderResponse().getResponseType())) {
						paramMap.put("code", "00");
						paramMap.put("apiResultCode", "000000");
						paramMap.put("resultCode", "00");
						paramMap.put("message", "(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" + "requestID : " + responseE.getShipProductOrderResponse().getRequestID() + " 발송 처리 완료");
						paramMap.put("resultMessage", "발송 처리 성공");
						
						if(slipProcList != null && slipProcList.size() > 0){
							paNaverDeliveryService.updatePaOrdermResultTx(paramMap, procMap);							
						}else{
							log.info("Error msg : No Data Selected");
							break;
						}
					}else {
						requestE = new ShipProductOrderRequestE();
						request = new ShipProductOrderRequest();
						accessCredentialsType = new AccessCredentialsType();
						naverSignature = ComUtil.paNaverGenerateSignature("ShipProductOrder");

						accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
						accessCredentialsType.setSignature(naverSignature.getSignature());
						accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

						request.setAccessCredentials(accessCredentialsType);
						request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
						request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
						
						request.setRequestID(UUID.randomUUID().toString());
						request.setProductOrderID(procMap.get("PRODUCT_ORDER_ID").toString());
						request.setDeliveryMethodCode(DeliveryMethodType.DIRECT_DELIVERY);
//						request.setDeliveryCompanyCode("CH1");

						trackingNumber = procMap.get("SLIP_I_NO").toString();
						if(null != trackingNumber && !trackingNumber.equals("")) request.setTrackingNumber(trackingNumber);
						dispatchDate = procMap.get("DISPATCH_DATE").toString();
						if(null != dispatchDate && !dispatchDate.equals("")) {
							Calendar dispatchCalendar = Calendar.getInstance();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
							dispatchCalendar.setTime(dateFormat.parse(dispatchDate));
							request.setDispatchDate(dispatchCalendar);
						}

						requestE.setShipProductOrderRequest(request);

						responseE = stub.shipProductOrder(requestE);
						
						if("SUCCESS".equals(responseE.getShipProductOrderResponse().getResponseType())) {
							paramMap.put("code", "00");
							paramMap.put("apiResultCode", "000000");
							paramMap.put("resultCode", "00");
							paramMap.put("message", "requestID : " + responseE.getShipProductOrderResponse().getRequestID() + " 발송 처리 완료" + "(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")");
							paramMap.put("resultMessage", "발송 처리 성공");
							
							if(slipProcList != null && slipProcList.size() > 0){
								paNaverDeliveryService.updatePaOrdermResultTx(paramMap, procMap);							
							}else{
								log.info("Error msg : No Data Selected");
								break;
							}
						}
						else {
							log.info("Error msg : " + responseE.getShipProductOrderResponse().getError().getMessage());
							log.info(responseE.getShipProductOrderResponse().getRequestID() + " : 발송 처리 실패 ");
							sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" + responseE.getShipProductOrderResponse().getRequestID() + " : 발송 처리 실패 ");
							paramMap.put("code", "400");
						}
					}

				}catch(Exception e){
					log.info(procMap.get("MAPPING_SEQ").toString() + " : 발송 처리 실패 ");
					sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" +procMap.get("MAPPING_SEQ").toString() + " : 발송 처리 실패 ");
					continue;
				}
			}
		} catch (Exception e) {
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(paramMap.get("code") == null) {
					paramMap.put("code", "200");
					paramMap.put("message", "발송 대상 없음");
					paramMap.put("resultCode", "000000");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				else {
					paramMap.put("message", sb.toString());
				}
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", paramMap.getString("message")), HttpStatus.OK); 
	}
	
	//발송 지연
	@ApiOperation(value = "발송 지연 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/delivery-delay-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deliveryDelyProc(HttpServletRequest httpServletRequest) throws Exception {

		String tx = "";
		ParamMap paramMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_004");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		HashMap<String, Object> procMap = null;
		String dispatchDueDate ="";
		Paorderm paorderm = new Paorderm();
		StringBuffer sb = new StringBuffer();
		
		SellerServiceStub stub = new SellerServiceStub();

		try {
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			List<HashMap<String, Object>> delayProcList = paNaverDeliveryService.selectDelayProcList();
			for(int i=0; i<delayProcList.size(); i++){
				try{
					procMap = (HashMap<String, Object>) delayProcList.get(i);
//					SellerServiceStub stub = new SellerServiceStub();
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					DelayProductOrderRequest request = new DelayProductOrderRequest();
					DelayProductOrderRequestE requestE = new DelayProductOrderRequestE();
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("DelayProductOrder");
					
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

					request.setAccessCredentials(accessCredentialsType);
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setRequestID(UUID.randomUUID().toString());

					request.setProductOrderID(procMap.get("PRODUCT_ORDER_ID").toString());
					dispatchDueDate = procMap.get("DELY_DATE").toString();
					if(null != dispatchDueDate && !dispatchDueDate.equals("")) {
						Calendar dispatchCalendar = Calendar.getInstance();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
						dispatchCalendar.setTime(dateFormat.parse(dispatchDueDate));
						request.setDispatchDueDate(dispatchCalendar);
					}
					request.setDispatchDelayReasonCode(DelayedDispatchReasonType.ETC);
					request.setDispatchDelayDetailReason(procMap.get("REASON_DETAIL").toString());
					requestE.setDelayProductOrderRequest(request);
					DelayProductOrderResponseE responseE = stub.delayProductOrder(requestE);
					
					if("SUCCESS".equals(responseE.getDelayProductOrderResponse().getResponseType())) {
						paramMap.put("code", "200");
						paramMap.put("apiResultCode", "000000");
						paramMap.put("resultCode", "00");
						paramMap.put("resultMessage", "발송 지연 처리 완료");
						
						paramMap.put("slipDelayed", "1");
					}else{
						log.info("Error msg : " + responseE.getDelayProductOrderResponse().getError().getMessage());
						log.debug("deliveryDelyProc requestId :: {} failed", responseE.getDelayProductOrderResponse().getRequestID());
						paramMap.put("code", "400");
						paramMap.put("message", responseE.getDelayProductOrderResponse().getError().getMessage() + " " + responseE.getDelayProductOrderResponse().getRequestID());
						paramMap.put("resultCode", "99");
						paramMap.put("resultMessage", "DelayProductOrder request failed");
						log.info("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ") " + " : 발송 지연 처리 실패 ");
						sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ") " + " : 발송 지연 처리 실패 ");
					}
					paorderm.setMappingSeq(procMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("resultCode"));
					paorderm.setApiResultMessage(paramMap.getString("resultMessage"));
					

					paNaverDeliveryService.updatePaOrdermResultTx(paramMap, procMap);
				}catch(Exception e){
					log.info("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ") " + procMap.get("MAPPING_SEQ").toString() + " : 발송 지연 처리 실패 ");
					sb.append("(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ") " + procMap.get("MAPPING_SEQ").toString() + " : 발송 지연 처리 실패 ");
					continue;
				}
			}
		} catch (Exception e) {
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if(paramMap.get("code") == null) {
					paramMap.put("code", "200");
					paramMap.put("message", "발송 지연 안내 대상 없음");
					paramMap.put("resultCode", "000000");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				else {
					paramMap.put("message","(naverProductOrderID " + procMap.get("PRODUCT_ORDER_ID").toString() + ")" + sb.toString());
				}
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK); 
	}

}