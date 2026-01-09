package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimRequestReasonType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ExchangeInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.HoldbackStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderChangeType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReturnInfo;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.panaver.infocommon.service.PaNaverInfoCommonService;

@Controller("com.cware.api.panaver.PaNaverInfoCommonController")
@RequestMapping("/panaver/info")
public class PaNaverInfoCommonController extends AbstractController  {
	
	@Autowired
	private PaNaverAsyncController paNaverAsyncController;
	
	@Autowired
	private PaNaverDeliveryController paNaverDeliveryController;
	
	@Autowired
	private PaNaverCancelController paNaverCancelController;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaNaverInfoCommonService paNaverInfoCommonService;
		
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품 주문 변경내역 조회", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "상품 주문 변경내역 조회/적재 성공"), 
    						@ApiResponse(code = 400, message = "상품 주문 변경내역 조회 요청 실패"),
    						@ApiResponse(code = 500, message = "오류가 발생하였습니다.") })
	@ApiImplicitParams({
        @ApiImplicitParam(name = "fromDate", value = "조회시작일시", required = false, dataType = "string", paramType = "query", defaultValue = "20191126000000"),
        @ApiImplicitParam(name = "toDate", value = "조회종료일시", required = false, dataType = "string", paramType = "query", defaultValue = "20191127000000"),
        @ApiImplicitParam(name = "orderStatus", value = "주문상태(0:PAYED, 1:DISPATCHED, 2:CANCEL_REQUESTED, 3:RETURN_REQUESTED, 4:EXCHANGE_REQUESTED, 5:CANCELED, 6:RETURNED, 7:PAY_WAITING, 9:PURCHASE_DECIDED, 91:HOLDBACK_REQUESTED, 92:EXCHANGED)", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
        @ApiImplicitParam(name = "isExclusiveExecution", value = "API단독호출여부", required = true, dataType = "boolean", paramType = "query", defaultValue = "false")
	})
	@RequestMapping(value = "/order-change-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> retrieveOrderChangeList(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "orderStatus", required = false) int orderStatus) throws Exception {
		
		
		String tx = "";
		ParamMap paramMap = new ParamMap();
		ParamMap resultMap = new ParamMap();
		ResponseEntity<ResponseMsg> responseEntity = new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		NaverSignature naverSignature = null;
		SellerServiceStub stub = new SellerServiceStub();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		dateFormat.setLenient(false);
		List<ChangedProductOrderInfo> payedList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, canceledList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnedList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeCollectedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRejectList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRejectList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRejectList = new ArrayList<ChangedProductOrderInfo>()
									, adminCancelList = new ArrayList<ChangedProductOrderInfo>()
									, payWaitingList = new ArrayList<ChangedProductOrderInfo>()
									, purchaseDecidedList = new ArrayList<ChangedProductOrderInfo>();
		boolean isRequestDone = false;
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_002 os(" + String.valueOf(orderStatus) + ")");
		paramMap.put("fromDate", fromDate);
		paramMap.put("toDate", toDate);
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			GetChangedProductOrderListRequestE requestE = new GetChangedProductOrderListRequestE();
			GetChangedProductOrderListRequest request = new GetChangedProductOrderListRequest();
			naverSignature = ComUtil.paNaverGenerateSignature("GetChangedProductOrderList");
			accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
			accessCredentialsType.setSignature(naverSignature.getSignature());
			accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
			request.setAccessCredentials(accessCredentialsType);
			request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setRequestID(UUID.randomUUID().toString());
			request.setMallID(ConfigUtil.getString("PANAVER_MALL_ID"));
			Calendar from = Calendar.getInstance();
			Calendar to = Calendar.getInstance();
			if(fromDate != null && !fromDate.equals("")) from.setTime(dateFormat.parse(fromDate));
			else {
				from.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(systemService.getSysdatetimeToString()));
				from.add(Calendar.DATE, -1);
			}
			request.setInquiryTimeFrom(from);
			if(toDate != null && !toDate.equals("")) to.setTime(dateFormat.parse(toDate));
			else to.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(systemService.getSysdatetimeToString()));
			request.setInquiryTimeTo(to);
			
			switch(orderStatus) {
				case 0: request.setLastChangedStatusCode(ProductOrderChangeType.PAYED); break;
				case 1: request.setLastChangedStatusCode(ProductOrderChangeType.DISPATCHED); break;
				case 2: request.setLastChangedStatusCode(ProductOrderChangeType.CANCEL_REQUESTED); break;
				case 3: request.setLastChangedStatusCode(ProductOrderChangeType.RETURN_REQUESTED); break;
				case 4: request.setLastChangedStatusCode(ProductOrderChangeType.EXCHANGE_REQUESTED); break;
				case 5: request.setLastChangedStatusCode(ProductOrderChangeType.CANCELED); break;
				case 6: request.setLastChangedStatusCode(ProductOrderChangeType.RETURNED); break;
				case 7: request.setLastChangedStatusCode(ProductOrderChangeType.PAY_WAITING); break;
				case 8: request.setLastChangedStatusCode(ProductOrderChangeType.EXCHANGE_REDELIVERY_READY); break;
				case 9: request.setLastChangedStatusCode(ProductOrderChangeType.PURCHASE_DECIDED); break;
				case 91: request.setLastChangedStatusCode(ProductOrderChangeType.HOLDBACK_REQUESTED); break;
				case 92: request.setLastChangedStatusCode(ProductOrderChangeType.EXCHANGED); break;
				case 93: request.setLastChangedStatusCode(ProductOrderChangeType.PURCHASE_DECIDED); break;
			}
			
			while(!isRequestDone) {
				requestE.setGetChangedProductOrderListRequest(request);
				GetChangedProductOrderListResponseE responseE = stub.getChangedProductOrderList(requestE);
				
				if("SUCCESS".equals(responseE.getGetChangedProductOrderListResponse().getResponseType())) {
					isRequestDone = responseE.getGetChangedProductOrderListResponse().getHasMoreData() ? false : true;
					log.debug("GetChangedProductOrderList requestId :: {} succeed", responseE.getGetChangedProductOrderListResponse().getRequestID());
					if(responseE.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList() != null) {
						log.debug("retreived changed order count : {}", responseE.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList().length);
						
						resultMap = paNaverInfoCommonService.mergeChangeOrderListTx(responseE, fromDate, toDate);
						if(null != resultMap.get("payedList")) payedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payedList"));
						if(null != resultMap.get("cancelRejectList")) cancelRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRejectList"));
						if(null != resultMap.get("returnRejectList")) returnRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRejectList"));
						if(null != resultMap.get("exchangeRejectList")) exchangeRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRejectList"));
						if(null != resultMap.get("cancelRequestedList")) cancelRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRequestedList"));
						if(null != resultMap.get("returnRequestedList")) returnRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRequestedList"));
						if(null != resultMap.get("exchangeRequestedList")) exchangeRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRequestedList"));
						if(null != resultMap.get("canceledList")) canceledList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("canceledList"));
						if(null != resultMap.get("returnedList")) returnedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnedList"));
						if(null != resultMap.get("adminCancelList")) adminCancelList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("adminCancelList"));
						if(null != resultMap.get("exchangeCollectedList")) exchangeCollectedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeCollectedList"));
						if(null != resultMap.get("payWaitingList")) payWaitingList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payWaitingList"));
						if(null != resultMap.get("purchaseDecidedList")) purchaseDecidedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("purchaseDecidedList"));
						
						log.debug("saved changed order count : {}", resultMap.getInt("saveCnt"));
						paramMap.put("code", "200");
						if(paramMap.get("message") != null) {
							paramMap.put("message", paramMap.getString("message") + " + (" + String.valueOf(resultMap.getInt("saveCnt")) + ")");
						}
						else {
							paramMap.put("message", "주문 변경 내역 (" + String.valueOf(resultMap.getInt("saveCnt")) + ") 건 저장");
						}
						
						if(!isRequestDone) request.setInquiryTimeFrom(responseE.getGetChangedProductOrderListResponse().getMoreDataTimeFrom());
					}
					else {
						log.debug("retreived changed order count : 0");
						paramMap.put("code", "200");
						paramMap.put("message", "주문 변경 내역 (0) 건 저장");
					}
					paramMap.put("resultCode", "00");
					paramMap.put("resultMessage", paramMap.get("message"));
				}
				else {
					log.error("GetChangedProductOrderList requestId :: {} failed", responseE.getGetChangedProductOrderListResponse().getRequestID());
					paramMap.put("code", "400");
					paramMap.put("message", "GetChangedProductOrderList " + responseE.getGetChangedProductOrderListResponse().getError().getMessage());
					paramMap.put("resultCode", "99");
					responseEntity = new ResponseEntity<ResponseMsg>(new ResponseMsg("400", "Bad Request"), HttpStatus.BAD_REQUEST);
					isRequestDone = true;
				}
				
			}
			
			if(paramMap.getString("message").length() > 3950) paramMap.put("message", paramMap.getString("message").substring(0, 3950));
			paramMap.put("resultMessage", paramMap.getString("message"));
			
		} catch (Exception e) {
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			responseEntity = new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
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
		
		if(null != resultMap.get("payedList")) {
			for(ChangedProductOrderInfo payedInfo : payedList) {
				paNaverDeliveryController.requestOrderConfirmProc(httpServletRequest, payedInfo, false);
			}
		}
		if(null != resultMap.get("cancelRequestedList")) {
			for(ChangedProductOrderInfo cancelRequestedInfo : cancelRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, cancelRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paramMap.put("checkSameClaimType", "true");
					if(paNaverInfoCommonService.checkExistingExchangeClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", cancelRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.EXCHANGE_REQUEST.getValue());
						CancelInfo cancelInfo = new CancelInfo();
						CancelInfo originalCancelInfo = response.getBody().getProductOrderInfo()[0].getCancelInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						cancelInfo.setClaimRequestDate(g);
						cancelInfo.setCancelDetailedReason("취소철회 조회 중 새 취소 요청으로 인한 취소철회");
						cancelInfo.setCancelReason(ClaimRequestReasonType.ETC);
						cancelInfo.setClaimStatus(ClaimStatusType.CANCEL_REJECT);
						cancelInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.CANCEL);
						response.getBody().getProductOrderInfo()[0].setCancelInfo(cancelInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), cancelRequestedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
						response.getBody().getProductOrderInfo()[0].setCancelInfo(originalCancelInfo);
					}
					List<ParamMap> returnMap = paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), cancelRequestedInfo);
					if(returnMap.size() > 0) {
						paNaverCancelController.cancelApprovalProc(returnMap.get(0).getString("orderID"), returnMap.get(0).getString("productOrderID"),"0",httpServletRequest);
					}
				}
			}
		}
		if(null != resultMap.get("canceledList")) {
			for(ChangedProductOrderInfo canceledInfo : canceledList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, canceledInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), canceledInfo);
				}
			}
		}
		if(orderStatus == 3) {
			paNaverDeliveryController.slipOutProc(httpServletRequest);
		}
		if(null != resultMap.get("exchangeRequestedList")) {
			for(ChangedProductOrderInfo exchangeRequestedInfo : exchangeRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paramMap.put("checkSameClaimType", "false");
					if(paNaverInfoCommonService.checkExistingReturnClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", exchangeRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.RETURN_REQUEST.getValue());
						HashMap<String, String> returnShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ReturnInfo returnInfo = new ReturnInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						returnInfo.setClaimRequestDate(g);
						returnInfo.setReturnDetailedReason("교환 요청으로 인한 반품철회");
						returnInfo.setReturnReason(ClaimRequestReasonType.ETC);
						returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT);
						returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						returnInfo.setEtcFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						returnInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].setReturnInfo(returnInfo);
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.RETURN);
						if(paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo).size() > 0) {
							claimCancelMain(httpServletRequest);
						}
					}
					paramMap.put("checkSameClaimType", "true");
					if(paNaverInfoCommonService.checkExistingExchangeClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", exchangeRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.EXCHANGE_REQUEST.getValue());
						HashMap<String, String> exchangeShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ExchangeInfo exchangeInfo = new ExchangeInfo();
						ExchangeInfo originalExchangeInfo = response.getBody().getProductOrderInfo()[0].getExchangeInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						exchangeInfo.setClaimRequestDate(g);
						exchangeInfo.setExchangeDetailedReason("교환철회 미조회 중 새 교환 요청으로 인한 교환철회");
						exchangeInfo.setExchangeReason(ClaimRequestReasonType.ETC);
						exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT);
						exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						exchangeInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.EXCHANGE);
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(exchangeInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(originalExchangeInfo);
					}
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo);
				}
			}
		}
		if(null != resultMap.get("returnRequestedList")) {
			for(ChangedProductOrderInfo returnRequestedInfo : returnRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paramMap.put("checkSameClaimType", "false");
					if(paNaverInfoCommonService.checkExistingExchangeClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", returnRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.EXCHANGE_REQUEST.getValue());
						HashMap<String, String> exchangeShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ExchangeInfo exchangeInfo = new ExchangeInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						exchangeInfo.setClaimRequestDate(g);
						exchangeInfo.setExchangeDetailedReason("반품 요청으로 인한 교환철회");
						exchangeInfo.setExchangeReason(ClaimRequestReasonType.ETC);
						exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT);
						exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						exchangeInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.EXCHANGE);
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(exchangeInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
					}
					paramMap.put("checkSameClaimType", "true");
					if(paNaverInfoCommonService.checkExistingReturnClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", returnRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.RETURN_REQUEST.getValue());
						HashMap<String, String> returnShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ReturnInfo returnInfo = new ReturnInfo();
						ReturnInfo originalReturnInfo = new ReturnInfo();
						originalReturnInfo = response.getBody().getProductOrderInfo()[0].getReturnInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						returnInfo.setClaimRequestDate(g);
						returnInfo.setReturnDetailedReason("반품철회 미조회 중 새 반품 요청으로 인한 반품철회");
						returnInfo.setReturnReason(ClaimRequestReasonType.ETC);
						returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT);
						returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						returnInfo.setEtcFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						returnInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].setReturnInfo(returnInfo);
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.RETURN);
						if(paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo).size() > 0) {
							claimCancelMain(httpServletRequest);
						}
						response.getBody().getProductOrderInfo()[0].setReturnInfo(originalReturnInfo);
					}
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo);
				}
			}
		}
		if(null != resultMap.get("returnedList")) {
			for(ChangedProductOrderInfo returnedInfo : returnedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnedInfo);
				}
			}
		}
		if(null != resultMap.get("exchangeCollectedList")) {
			for(ChangedProductOrderInfo exchangeCollectedInfo : exchangeCollectedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeCollectedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeCollectedInfo);
				}
			}
		}
		if(null != resultMap.get("returnRejectList")) {
			for(ChangedProductOrderInfo returnRejectInfo : returnRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRejectInfo);
				}
			}
		}
		if(null != resultMap.get("exchangeRejectList")) {
			for(ChangedProductOrderInfo exchangeRejectInfo : exchangeRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRejectInfo);
				}
			}
		}
		if(null != resultMap.get("cancelRejectList")) {
			for(ChangedProductOrderInfo cancelRejectInfo : cancelRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, cancelRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), cancelRejectInfo);
				}
			}
		}
		if(null != resultMap.get("adminCancelList")) {
			for(ChangedProductOrderInfo adminCancelInfo : adminCancelList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, adminCancelInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), adminCancelInfo);
				}
			}
		}
//		if(null != resultMap.get("payWaitingList")) {
//			for(ChangedProductOrderInfo waitingList : payWaitingList) {
//				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, waitingList.getProductOrderID());
//				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
//					paNaverInfoCommonService.insertPayWaitingInfo(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), waitingList);
//				}
//			}
//		}
		if(null != resultMap.get("purchaseDecidedList")) {
			for(ChangedProductOrderInfo purchaseDecidedInfo : purchaseDecidedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, purchaseDecidedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paramMap.put("productOrderID", purchaseDecidedInfo.getProductOrderID());
					paramMap.put("orderID", purchaseDecidedInfo.getOrderID());
					HashMap<String, String> existingReturnInfo = paNaverInfoCommonService.selectExistingOngoingReturnInfo(paramMap);
					HashMap<String, String> existingExchangeInfo = paNaverInfoCommonService.selectExistingOngoingExchangeInfo(paramMap);
					if(existingReturnInfo != null) {
						ReturnInfo returnInfo = new ReturnInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						returnInfo.setClaimRequestDate(g);
						returnInfo.setReturnDetailedReason("구매확정으로 인한 반품철회");
						returnInfo.setReturnReason(ClaimRequestReasonType.ETC);
						returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT);
						returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(existingReturnInfo.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						returnInfo.setEtcFeeDemandAmount(ComUtil.objToInt(existingReturnInfo.get("ETC_FEE_DEMAND_AMOUNT")));
						returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(existingReturnInfo.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						returnInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].setReturnInfo(returnInfo);
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.RETURN);
						if(paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), purchaseDecidedInfo).size() > 0) {
							claimCancelMain(httpServletRequest);
						}
					}
					else if(existingExchangeInfo != null) {
						ExchangeInfo exchangeInfo = new ExchangeInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						exchangeInfo.setClaimRequestDate(g);
						exchangeInfo.setExchangeDetailedReason("구매확정으로 인한 교환철회");
						exchangeInfo.setExchangeReason(ClaimRequestReasonType.ETC);
						exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT);
						exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(existingExchangeInfo.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToInt(existingExchangeInfo.get("ETC_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(existingExchangeInfo.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						exchangeInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.EXCHANGE);
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(exchangeInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), purchaseDecidedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
					}
				}
			}
		}
		
		switch(orderStatus) {
		case 0:
			preOrderInputMain(httpServletRequest);
			orderInputMain(httpServletRequest);
			orderUpdateMain(httpServletRequest);
			cancelInputMain(httpServletRequest);
			reApplyChange(httpServletRequest);
			break;
		case 1:
			claimCancelMain(httpServletRequest);
			changeCancelMain(httpServletRequest);
			break;
		case 2:
			cancelInputMain(httpServletRequest);
			break;
		case 3:
			orderClaimMain(httpServletRequest);
			break;
		case 4:
			orderChangeMain(httpServletRequest);
			break;
		case 5:
			cancelInputMain(httpServletRequest);
			break;
		case 6:
			orderClaimMain(httpServletRequest);
			break;
//		case 7:
//			preOrderInputMain(httpServletRequest);
//			orderInputMain(httpServletRequest);
//			orderUpdateMain(httpServletRequest);
//			cancelInputMain(httpServletRequest);
//			break;
		}
		
		return responseEntity;
	}
	
	@ApiOperation(value = "상품 주문 상세 내역 조회", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "상품 주문 상세내역 조회 성공"), 
    						@ApiResponse(code = 400, message = "상품 주문 상세내역 조회 실패"),
    						@ApiResponse(code = 500, message = "오류가 발생하였습니다.") })
	@ApiImplicitParams({
        @ApiImplicitParam(name = "productOrderIDs", value = "상품주문번호", required = true, dataType = "string", paramType = "query", defaultValue = "")
	})
	@RequestMapping(value= "/order-detail-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ProductOrderInfoMsg> retrieveOrderDetailInfo(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "productOrderIDs", required = true) String productOrderIDs) throws Exception {
		
		ProductOrderInfoMsg responseMsg = new ProductOrderInfoMsg();
		String tx = "";
		ParamMap paramMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_001");
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			//Security.addProvider(new BouncyCastleProvider());
			AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
			GetProductOrderInfoListRequestE requestE = new GetProductOrderInfoListRequestE();
			GetProductOrderInfoListRequest request = new GetProductOrderInfoListRequest();
			//SellerServiceStub stub = new SellerServiceStub();
			
			NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("GetProductOrderInfoList");
			
			accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
			accessCredentialsType.setSignature(naverSignature.getSignature());
			accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
			
			request.setAccessCredentials(accessCredentialsType);
			request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setRequestID(UUID.randomUUID().toString());
			
			request.setProductOrderIDList(productOrderIDs.split(","));
			requestE.setGetProductOrderInfoListRequest(request);
			
			GetProductOrderInfoListResponseE responseE = stub.getProductOrderInfoList(requestE);
			
			if("SUCCESS".equals(responseE.getGetProductOrderInfoListResponse().getResponseType())) {
				paramMap.put("code", "200");
				paramMap.put("message", "변경내역 주문정보 조회 완료");
				paramMap.put("resultCode", "00");
				paramMap.put("resultMessage", paramMap.getString("message"));
				responseMsg.setCode(paramMap.getString("code"));
				responseMsg.setMessage(paramMap.getString("message"));
				responseMsg.setStatus(HttpStatus.OK.value());
				responseMsg.setSuccessYn("Y");
				responseMsg.setProductOrderInfo(responseE.getGetProductOrderInfoListResponse().getProductOrderInfoList());
				responseMsg.setNaverSignature(naverSignature);
			}
			else {
				log.debug("GetProductOrderInfoList requestId :: {} failed", responseE.getGetProductOrderInfoListResponse().getRequestID());
				paramMap.put("code", "400");
				paramMap.put("message", responseE.getGetProductOrderInfoListResponse().getError().getMessage());
				paramMap.put("resultCode", "99");
				paramMap.put("resultMessage", "GetProductOrderInfoList request failed");
				return new ResponseEntity<ProductOrderInfoMsg>(new ProductOrderInfoMsg("400", "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ProductOrderInfoMsg>(new ProductOrderInfoMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
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
		return new ResponseEntity<ProductOrderInfoMsg>(responseMsg, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	public void orderInputMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_INPUT";
		
		log.info("=========================== NAVER Order Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderInputTargetList = paNaverInfoCommonService.selectOrderInputTargetList();
			HashMap<String, String> hmSheet = null;
			
			int procCnt = orderInputTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, String>) orderInputTargetList.get(i);
					
					paNaverAsyncController.orderCreateAsync(request, hmSheet.get("PA_ORDER_NO"));
				
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Order Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void cancelInputMain(HttpServletRequest request) throws Exception{ 
		String duplicateCheck = "";
		String prg_id = "PANAVER_CANCEL_INPUT";
		
		log.info("=========================== NAVER Cancel Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> cancelInputTargetList = paNaverInfoCommonService.selectCancelInputTargetList();
			
			HashMap<String, Object> hmSheet = null;
			int procCnt = cancelInputTargetList.size();
			//asycController.cancelInputAsync(hmSheet, request);
	
			ParamMap cancelMap;
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) cancelInputTargetList.get(i);
					cancelMap = new ParamMap();
					cancelMap.put("orderID", hmSheet.get("PA_ORDER_NO").toString());
					cancelMap.put("productOrderID", hmSheet.get("PA_ORDER_SEQ").toString());
					paNaverAsyncController.cancelInputAsync(request, cancelMap);
				} catch (Exception e) {
					continue;
				}
			}
		     
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Cancel Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void orderClaimMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_CLAIM";

		log.info("=========================== NAVER Order Claim Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> orderClaimTargetList = paNaverInfoCommonService.selectOrderClaimTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = orderClaimTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList
							.get(i);

					paNaverAsyncController.returnInputAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Order Claim End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void claimCancelMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_CLAIM_CANCEL";

		log.info("=========================== NAVER Claim Cancel Create Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> claimCancelTargetList = paNaverInfoCommonService.selectClaimCancelTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = claimCancelTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) claimCancelTargetList.get(i);
					paNaverAsyncController.returnCancelAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if (duplicateCheck.equals("0")) {
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Claim Cancel Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void orderChangeMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_CHANGE";
		HashMap<String, Object> hmSheet = null;
		int procCnt = 0;
		log.info("=========================== NAVER Order Change Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderChangeTargetList = paNaverInfoCommonService.selectOrderChangeTargetList();
			
			procCnt = orderChangeTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
								
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderChangeTargetList.get(i);
					
					paNaverAsyncController.exchangeInputAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
				} catch (Exception e) {
					continue;
				}
			}
		
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Order Change End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void changeCancelMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PANAVER_CHANGE_CANCEL";
		HashMap<String, Object> hmSheet = null;
		
		log.info("=========================== NAVER Change Cancel Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> changeCancelTargetList = paNaverInfoCommonService.selectChangeCancelTargetList();
			int procCnt = changeCancelTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) changeCancelTargetList.get(i);
					
					paNaverAsyncController.exchangeCancelAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
					
					
				} catch (Exception e) {
					continue;
				}
			}
			
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Change Cancel End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	public void orderUpdateMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		HashMap<String, String> hmSheet = null;
		int failCount = 0;
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, "PANAVER_ORDER_UPDATE");
		paramMap.put("paGroupCode"	, "04");
		paramMap.put("siteGb", "PANAVER");
		String apiCode = paramMap.getString("apiCode");
		String isLocalYn = "";
		if (("0:0:0:0:0:0:0:1").equals(request.getRemoteHost())
				|| ("127.0.0.1").equals(request.getRemoteHost())) {
			isLocalYn = "Y";
		} else {
			isLocalYn = "N";
		}

		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			
			List<Object> preOrderInputTargetList = paNaverInfoCommonService.selectPreOrderUpdateTargetList();
			
			paramMap.put("code", "200");
			if(preOrderInputTargetList == null){
				paramMap.put("message", "선물수락 전처리 대상 없음");
			}
			else {
				paramMap.put("message", "선물수락 전처리 성공");
			}
			
			for(Object orderMap : preOrderInputTargetList){
				hmSheet = new HashMap<>();
				hmSheet = (HashMap<String, String>) orderMap;
				hmSheet.put("isLocalYn"	,	isLocalYn);
				try{
					if(paNaverInfoCommonService.selectUnAttendedCount(hmSheet.get("PA_ORDER_NO").toString()) == 0){
						paNaverAsyncController.preOrderUpdateAsync(request, hmSheet);
					}
				}catch(Exception e){
					if(paramMap.getString("code").equals("200")) {
						paramMap.put("code", "500");
						paramMap.put("message", "PA_ORDER_NO "+ hmSheet.get("PA_ORDER_NO").toString() + " 선물수락 전처리 실패");
					}
					else {
						paramMap.put("message", "PA_ORDER_NO " + hmSheet.get("PA_ORDER_NO").toString() + paramMap.getString("message"));
					}
					failCount++;
					continue;
				}
			}
			
			paramMap.put("message", paramMap.getString("message").length() > 3950 ? paramMap.getString("message").substring(0, 3950) : paramMap.getString("message"));
			paramMap.put("resultCode", "00");
			paramMap.put("resultMessage", paramMap.getString("message"));
			
		} catch (Exception se) {
			log.error(paramMap.getString("message"), se);
			paramMap.put("code", "500");
			paramMap.put("resultCode", "99");
			paramMap.put("message", "선물 수락 승인 전처리 실패 (" + failCount + ") 건");
			paramMap.put("resultMessage", paramMap.getString("message"));
		}finally {
			try{
				systemService.insertPaApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
		}				
	}
	
	@SuppressWarnings("unchecked")
	public void preOrderInputMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PANAVER_PREORDER_INPUT";
		HashMap<String, Object> hmSheet = null;
		
		log.info("=========================== NAVER Preorder Input Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> preorderTargetList = paNaverInfoCommonService.selectPreOrderInputTargetList();
			int procCnt = preorderTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) preorderTargetList.get(i);
					
					paNaverAsyncController.orderCreateAsync(request, hmSheet.get("PA_ORDER_NO").toString());
					
				} catch (Exception e) {
					continue;
				}
			}
			
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Preorder Input End =========================");
		}
		return;
	}
	
	public void preOrderCancelMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_PREORDER_CANCEL";
		HashMap<String, Object> hmSheet = null;
		
		log.info("=========================== NAVER Preorder Cancel Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<HashMap<String, Object>> preOrderCancelTargetList = paNaverInfoCommonService.selectNotTakenPresentList();
			int procCnt = preOrderCancelTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) preOrderCancelTargetList.get(i);
					
					paNaverInfoCommonService.saveCancelNotTakenPresent(hmSheet);
					
				} catch (Exception e) {
					continue;
				}
			}
			
		} catch (Exception e) {
			log.info("error msg : " + e.getMessage());
		} finally {
			if(duplicateCheck.equals("0")){
				systemService.checkCloseHistoryTx("end", prg_id);
			}
			log.info("=========================== NAVER Preorder Cancel End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "상품 주문 변경내역 미반영건 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "상품 주문 변경내역 미반영건 처리 성공"), 
    						@ApiResponse(code = 400, message = "상품 주문 변경내역 미반영건 조회 실패"),
    						@ApiResponse(code = 500, message = "오류가 발생하였습니다.") })
	@RequestMapping(value = "/re-apply-change", method = RequestMethod.GET)
	@ResponseBody
	public String reApplyChange(HttpServletRequest httpServletRequest) throws Exception {
		
		String message = "SUCCESS";
		String tx = "";
		ParamMap paramMap = new ParamMap();
		ParamMap resultMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "PANAVER_RE_APPLY_CHANGE");
		
		String fromDateStr;
		String toDateStr;
		boolean slipOutProcYn = false;
		int saveCnt = 0;
		List<ChangedProductOrderInfo> payedList = new ArrayList<ChangedProductOrderInfo>()
				, cancelRequestedList = new ArrayList<ChangedProductOrderInfo>()
				, canceledList = new ArrayList<ChangedProductOrderInfo>()
				, exchangeRequestedList = new ArrayList<ChangedProductOrderInfo>()
				, returnRequestedList = new ArrayList<ChangedProductOrderInfo>()
				, returnedList = new ArrayList<ChangedProductOrderInfo>()
				, exchangeCollectedList = new ArrayList<ChangedProductOrderInfo>()
				, returnRejectList = new ArrayList<ChangedProductOrderInfo>()
				, exchangeRejectList = new ArrayList<ChangedProductOrderInfo>()
				, cancelRejectList = new ArrayList<ChangedProductOrderInfo>()
				, adminCancelList = new ArrayList<ChangedProductOrderInfo>()
				, payWaitingList = new ArrayList<ChangedProductOrderInfo>();
		
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		NaverSignature naverSignature = null;
		SellerServiceStub stub = new SellerServiceStub();
		GetChangedProductOrderListRequestE requestE = new GetChangedProductOrderListRequestE();
		GetChangedProductOrderListRequest request = new GetChangedProductOrderListRequest();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		dateFormat.setLenient(false);
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			List<HashMap<String, String>> unappliedChangedInfoList = paNaverInfoCommonService.selectUnappliedChangedInfo();
			for(HashMap<String, String> unappliedChangedInfo : unappliedChangedInfoList) {
				try{
					//GetChangedProductOrderListRequestE requestE = new GetChangedProductOrderListRequestE();
					//GetChangedProductOrderListRequest request = new GetChangedProductOrderListRequest();
					naverSignature = ComUtil.paNaverGenerateSignature("GetChangedProductOrderList");
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
					request.setAccessCredentials(accessCredentialsType);
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setRequestID(UUID.randomUUID().toString());
					request.setMallID(ConfigUtil.getString("PANAVER_MALL_ID"));
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateFormat.parse(unappliedChangedInfo.get("LAST_CHANGED_DATE")));
					dateFormat.setCalendar(cal);
					fromDateStr = dateFormat.toPattern();
					request.setInquiryTimeFrom(cal);
					
					cal.add(Calendar.DAY_OF_MONTH, 1);
					dateFormat.setCalendar(cal);
					toDateStr = dateFormat.toPattern();
					request.setInquiryTimeTo(cal);
					
					switch(Integer.parseInt(unappliedChangedInfo.get("ORDER_STATUS"))) {
						case 0: request.setLastChangedStatusCode(ProductOrderChangeType.PAYED); break;
						case 1: request.setLastChangedStatusCode(ProductOrderChangeType.DISPATCHED); break;
						case 2: request.setLastChangedStatusCode(ProductOrderChangeType.CANCEL_REQUESTED); break;
						case 3: 
							request.setLastChangedStatusCode(ProductOrderChangeType.RETURN_REQUESTED);
							slipOutProcYn = true;
							break;
						case 4: request.setLastChangedStatusCode(ProductOrderChangeType.EXCHANGE_REQUESTED); break;
						case 5: request.setLastChangedStatusCode(ProductOrderChangeType.CANCELED); break;
						case 6: request.setLastChangedStatusCode(ProductOrderChangeType.RETURNED); break;
						case 7: request.setLastChangedStatusCode(ProductOrderChangeType.PAY_WAITING); break;
						case 8: request.setLastChangedStatusCode(ProductOrderChangeType.EXCHANGE_REDELIVERY_READY); break;
						case 9: break; //update set change_applied_yn = '1'
					}
				
					requestE.setGetChangedProductOrderListRequest(request);
					GetChangedProductOrderListResponseE responseE = stub.getChangedProductOrderList(requestE);
				
					if("SUCCESS".equals(responseE.getGetChangedProductOrderListResponse().getResponseType())) {
						log.debug("GetChangedProductOrderList requestId :: {} succeed", responseE.getGetChangedProductOrderListResponse().getRequestID());
						if(responseE.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList() != null) {
							log.debug("retreived re-appling order count : {}", responseE.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList().length);
							paramMap.put("orderID", unappliedChangedInfo.get("ORDER_ID"));
							paramMap.put("productOrderID", unappliedChangedInfo.get("PRODUCT_ORDER_ID"));
							paramMap.put("lastChangedStatus", unappliedChangedInfo.get("LAST_CHANGED_STATUS"));
							paNaverInfoCommonService.deleteUnappliedChangedInfo(paramMap);
							resultMap = paNaverInfoCommonService.mergeChangeOrderListTx(responseE, fromDateStr, toDateStr);
							if(null != resultMap.get("payedList")) payedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payedList"));
							if(null != resultMap.get("cancelRejectList")) cancelRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRejectList"));
							if(null != resultMap.get("returnRejectList")) returnRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRejectList"));
							if(null != resultMap.get("exchangeRejectList")) exchangeRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRejectList"));
							if(null != resultMap.get("cancelRequestedList")) cancelRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRequestedList"));
							if(null != resultMap.get("returnRequestedList")) returnRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRequestedList"));
							if(null != resultMap.get("exchangeRequestedList")) exchangeRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRequestedList"));
							if(null != resultMap.get("canceledList")) canceledList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("canceledList"));
							if(null != resultMap.get("returnedList")) returnedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnedList"));
							if(null != resultMap.get("adminCancelList")) adminCancelList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("adminCancelList"));
							if(null != resultMap.get("exchangeCollectedList")) exchangeCollectedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeCollectedList"));
							if(null != resultMap.get("payWaitingList")) payWaitingList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payWaitingList"));
							if(resultMap.getInt("saveCnt") > 0) saveCnt += resultMap.getInt("saveCnt");
						}
						else {
							resultMap = paNaverInfoCommonService.selectUnappliedOrderChange(unappliedChangedInfo.get("ORDER_ID").toString());
							if(null != resultMap && resultMap.getInt("saveCnt") > 0) {
								if(null != resultMap.get("payedList")) payedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payedList"));
								if(null != resultMap.get("cancelRejectList")) cancelRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRejectList"));
								if(null != resultMap.get("returnRejectList")) returnRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRejectList"));
								if(null != resultMap.get("exchangeRejectList")) exchangeRejectList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRejectList"));
								if(null != resultMap.get("cancelRequestedList")) cancelRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("cancelRequestedList"));
								if(null != resultMap.get("returnRequestedList")) returnRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnRequestedList"));
								if(null != resultMap.get("exchangeRequestedList")) exchangeRequestedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeRequestedList"));
								if(null != resultMap.get("canceledList")) canceledList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("canceledList"));
								if(null != resultMap.get("returnedList")) returnedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("returnedList"));
								if(null != resultMap.get("adminCancelList")) adminCancelList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("adminCancelList"));
								if(null != resultMap.get("exchangeCollectedList")) exchangeCollectedList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("exchangeCollectedList"));
								if(null != resultMap.get("payWaitingList")) payWaitingList.addAll((ArrayList<ChangedProductOrderInfo>) resultMap.get("payWaitingList"));
								if(resultMap.getInt("saveCnt") > 0) saveCnt += resultMap.getInt("saveCnt");
							}
							else {
								log.debug("retreived changed order count : 0");
								paramMap.put("code", "200");
								paramMap.put("message", "주문 변경 내역 미반영 (0) 건 조회");
							}
						}
						
						log.debug("re-appling order count : {}", saveCnt);
						paramMap.put("code", "200");
						if(paramMap.get("message") != null) {
							paramMap.put("message", paramMap.getString("message") + " + (" + String.valueOf(saveCnt) + ")");
						}
						else {
							paramMap.put("message", "주문 변경 내역 미반영 (" + String.valueOf(saveCnt) + ") 건 조회");
						}
						
						paramMap.put("resultCode", "00");
						paramMap.put("resultMessage", paramMap.get("message"));
					}
					else {
						log.error("GetChangedProductOrderList requestId :: {} failed", responseE.getGetChangedProductOrderListResponse().getRequestID());
						paramMap.put("code", "400");
						paramMap.put("message", "GetChangedProductOrderList " + responseE.getGetChangedProductOrderListResponse().getError().getMessage());
						paramMap.put("resultCode", "99");
					}
				}catch (Exception e) {
					log.error("message", e);
				}
			}
		} catch (Exception e) {
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			message = paramMap.getString("message");
		} finally {
			if(paramMap.get("code") == null) {
				paramMap.put("code", "200");
				paramMap.put("message", "미처리건 없음");
				paramMap.put("resultCode", "00");
				paramMap.put("resultMessage", paramMap.getString("message"));
			}
			if(paramMap.getString("message") != null) {
				message = paramMap.getString("message");
			}
			if(paramMap.getString("message").length() > 3950) paramMap.put("message", paramMap.getString("message").substring(0, 3950));
			if(paramMap.get("resultCode") == null) paramMap.put("resultCode", "00");
			if(paramMap.get("resultMessage") == null) paramMap.put("resultMessage", paramMap.getString("message"));
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
			 
		if(null != resultMap.get("payedList")) {
			for(ChangedProductOrderInfo payedInfo : payedList) {
				paNaverDeliveryController.requestOrderConfirmProc(httpServletRequest, payedInfo, false);
			}
		}
		if(null != resultMap.get("cancelRequestedList")) {
			for(ChangedProductOrderInfo cancelRequestedInfo : cancelRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, cancelRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && response.getBody().getProductOrderInfo()[0].getCancelInfo() != null) {
					List<ParamMap> returnMap = paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), cancelRequestedInfo);
					if(returnMap.size() > 0) {
						paNaverCancelController.cancelApprovalProc(returnMap.get(0).getString("orderID"), returnMap.get(0).getString("productOrderID"),"0",httpServletRequest);
					}
				}
			}
		}
		if(null != resultMap.get("canceledList")) {
			for(ChangedProductOrderInfo canceledInfo : canceledList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, canceledInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && response.getBody().getProductOrderInfo()[0].getCancelInfo() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), canceledInfo);
				}
			}
		}
		if(slipOutProcYn) {
			paNaverDeliveryController.slipOutProc(httpServletRequest);
		}
		if(null != resultMap.get("exchangeRequestedList")) {
			for(ChangedProductOrderInfo exchangeRequestedInfo : exchangeRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && response.getBody().getProductOrderInfo()[0].getExchangeInfo() != null) {
					if(response.getBody().getProductOrderInfo()[0].getExchangeInfo().getHoldbackStatus() != null && response.getBody().getProductOrderInfo()[0].getExchangeInfo().getHoldbackStatus().equals(HoldbackStatusType.HOLDBACK)) {
						continue;
					}
					paramMap.put("checkSameClaimType", "false");
					if(paNaverInfoCommonService.checkExistingReturnClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", exchangeRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.RETURN_REQUEST.getValue());
						HashMap<String, String> returnShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ReturnInfo returnInfo = new ReturnInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						returnInfo.setClaimRequestDate(g);
						returnInfo.setReturnDetailedReason("교환 요청으로 인한 반품철회");
						returnInfo.setReturnReason(ClaimRequestReasonType.ETC);
						returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT);
						returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						returnInfo.setEtcFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						returnInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].setReturnInfo(returnInfo);
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.RETURN);
						if(paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo).size() > 0) {
							claimCancelMain(httpServletRequest);
						}
					}
					paramMap.put("checkSameClaimType", "true");
					if(paNaverInfoCommonService.checkExistingExchangeClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", exchangeRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.EXCHANGE_REQUEST.getValue());
						HashMap<String, String> exchangeShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ExchangeInfo exchangeInfo = new ExchangeInfo();
						ExchangeInfo originalExchangeInfo = response.getBody().getProductOrderInfo()[0].getExchangeInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						exchangeInfo.setClaimRequestDate(g);
						exchangeInfo.setExchangeDetailedReason("교환철회 미조회 중 새 교환 요청으로 인한 교환철회");
						exchangeInfo.setExchangeReason(ClaimRequestReasonType.ETC);
						exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT);
						exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						exchangeInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.EXCHANGE);
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(exchangeInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(originalExchangeInfo);
					}
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRequestedInfo);
				}
			}
		}
		if(null != resultMap.get("returnRequestedList")) {
			for(ChangedProductOrderInfo returnRequestedInfo : returnRequestedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnRequestedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && response.getBody().getProductOrderInfo()[0].getReturnInfo() != null) {
					paramMap.put("checkSameClaimType", "false");
					if(paNaverInfoCommonService.checkExistingExchangeClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", returnRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.EXCHANGE_REQUEST.getValue());
						HashMap<String, String> exchangeShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ExchangeInfo exchangeInfo = new ExchangeInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						exchangeInfo.setClaimRequestDate(g);
						exchangeInfo.setExchangeDetailedReason("반품 요청으로 인한 교환철회");
						exchangeInfo.setExchangeReason(ClaimRequestReasonType.ETC);
						exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT);
						exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToInt(exchangeShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(exchangeShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						exchangeInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.EXCHANGE);
						response.getBody().getProductOrderInfo()[0].setExchangeInfo(exchangeInfo);
						if(paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo).size() > 0) {
							changeCancelMain(httpServletRequest);
						}
					}
					paramMap.put("checkSameClaimType", "true");
					if(paNaverInfoCommonService.checkExistingReturnClaim(httpServletRequest, response.getBody().getProductOrderInfo()[0], paramMap) > 0) {
						paramMap.put("productOrderID", returnRequestedInfo.getProductOrderID());
						paramMap.put("claimStatus", ClaimStatusType.RETURN_REQUEST.getValue());
						HashMap<String, String> returnShipcostMap = paNaverInfoCommonService.selectClaimShipcostInfo(paramMap);
						ReturnInfo returnInfo = new ReturnInfo();
						ReturnInfo originalReturnInfo = new ReturnInfo();
						originalReturnInfo = response.getBody().getProductOrderInfo()[0].getReturnInfo();
						Calendar g = Calendar.getInstance();
						g.setTime(new Date());
						returnInfo.setClaimRequestDate(g);
						returnInfo.setReturnDetailedReason("반품철회 미조회 중 새 반품 요청으로 인한 반품철회");
						returnInfo.setReturnReason(ClaimRequestReasonType.ETC);
						returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT);
						returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
						returnInfo.setEtcFeeDemandAmount(ComUtil.objToInt(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
						returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToInt(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
						returnInfo.setRequestChannel("API");
						response.getBody().getProductOrderInfo()[0].setReturnInfo(returnInfo);
						response.getBody().getProductOrderInfo()[0].getProductOrder().setClaimType(ClaimType.RETURN);
						if(paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo).size() > 0) {
							claimCancelMain(httpServletRequest);
						}
						response.getBody().getProductOrderInfo()[0].setReturnInfo(originalReturnInfo);
					}
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRequestedInfo);
				}
			}
		}
		if(null != resultMap.get("returnedList")) {
			for(ChangedProductOrderInfo returnedInfo : returnedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnedInfo);
				}
			}
		}
		if(null != resultMap.get("exchangeCollectedList")) {
			for(ChangedProductOrderInfo exchangeCollectedInfo : exchangeCollectedList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeCollectedInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeCollectedInfo);
				}
			}
		}
		if(null != resultMap.get("returnRejectList")) {
			for(ChangedProductOrderInfo returnRejectInfo : returnRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, returnRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertReturnInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), returnRejectInfo);
				}
			}
		}
		if(null != resultMap.get("exchangeRejectList")) {
			for(ChangedProductOrderInfo exchangeRejectInfo : exchangeRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, exchangeRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertExchangeInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), exchangeRejectInfo);
				}
			}
		}
		if(null != resultMap.get("cancelRejectList")) {
			for(ChangedProductOrderInfo cancelRejectInfo : cancelRejectList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, cancelRejectInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), cancelRejectInfo);
				}
			}
		}
		if(null != resultMap.get("adminCancelList")) {
			for(ChangedProductOrderInfo adminCancelInfo : adminCancelList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, adminCancelInfo.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertCancelInfoTx(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), adminCancelInfo);
				}
			}
		}
		if(null != resultMap.get("payWaitingList")) {
			for(ChangedProductOrderInfo waitingList : payWaitingList) {
				ResponseEntity<ProductOrderInfoMsg> response = this.retrieveOrderDetailInfo(httpServletRequest, waitingList.getProductOrderID());
				if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
					paNaverInfoCommonService.insertPayWaitingInfo(httpServletRequest, response.getBody().getProductOrderInfo(), response.getBody().getNaverSignature(), waitingList);
				}
			}
		}
	
		if(payedList.size() > 0 || payWaitingList.size() > 0) {
			preOrderInputMain(httpServletRequest);
			orderInputMain(httpServletRequest);
			orderUpdateMain(httpServletRequest);
			cancelInputMain(httpServletRequest);
		}
		if(cancelRequestedList.size() > 0 || cancelRejectList.size() > 0 || canceledList.size() > 0 || adminCancelList.size() > 0) {
			cancelInputMain(httpServletRequest);
		}
		if(returnRequestedList.size() > 0 || returnRejectList.size() > 0 || returnedList.size() > 0) {
			orderClaimMain(httpServletRequest);
		}
		if(exchangeCollectedList.size() > 0 || exchangeRequestedList.size() > 0 || exchangeRejectList.size() > 0) {
			orderChangeMain(httpServletRequest);
		}
			
		return message;
		
	}
			
}
