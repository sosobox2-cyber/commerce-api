package com.cware.api.panaver.controller.v3;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.message.v3.ChangedProductOrderInfoListMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.panaver.v3.domain.CancelOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ClaimStatusType;
import com.cware.netshopping.panaver.v3.domain.ClaimType;
import com.cware.netshopping.panaver.v3.domain.ExchangeOrderInfo;
import com.cware.netshopping.panaver.v3.domain.LastChangedType;
import com.cware.netshopping.panaver.v3.domain.OrderInfo;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;
import com.cware.netshopping.panaver.v3.domain.ReturnOrderInfo;
import com.cware.netshopping.panaver.v3.service.PaNaverV3CancelService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;
import com.cware.netshopping.panaver.v3.service.PaNaverV3InfoCommonService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("com.cware.api.panaver.v3.PaNaverV3InfoCommonController")
@RequestMapping(path = "/panaver/v3/info")
public class PaNaverV3InfoCommonController extends AbstractController {
	
	@Resource(name = "com.cware.api.panaver.v3.PaNaverV3AsycController")
	private PaNaverV3AsyncController paNaverV3AsyncController;
	
	@Resource(name = "panaver.v3.infocommon.paNaverV3InfoCommonService")
	private PaNaverV3InfoCommonService paNaverV3InfoCommonService;
	
	@Resource(name = "panaver.v3.cancel.paNaverV3CancelService")
	private PaNaverV3CancelService paNaverV3CancelService;
	
	@Resource(name = "panaver.v3.delivery.paNaverV3DeliveryService")
	private PaNaverV3DeliveryService paNaverV3DeliveryService;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	/**
	 * 변경 상품 주문 내역 조회
	 * 
	 * @param request
	 * @param lastChangedType
	 * @param fromDate
	 * @param toDate
	 * @param procId
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "변경 상품 주문 내역 조회", notes = "변경 상품 주문 내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-change-list")
	public ResponseEntity<?> changedProductOrderInfoList(HttpServletRequest request,
			@ApiParam(name = "lastChangedType", required = true, value = "최종 변경 구분") @RequestParam String lastChangedType,
			@ApiParam(name = "fromDate", required = false, value = "조회 시작 일시") @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", required = false, value = "조회 종료 일시") @RequestParam(value = "toDate", required = false, defaultValue = "") String toDate,
			@ApiParam(name = "productOrderId", required = false, value = "상품주문번호") @RequestParam(value = "productOrderId", required = false, defaultValue = "") String productOrderId,
			@ApiParam(name = "limitCount", required = false, value = "조회 응답 개수 제한") @RequestParam(value = "limitCount", required = false, defaultValue = "300") int limitCount,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId) throws Exception {
		
		/** 1) 네이버 변경 상품 주문 내역 조회 **/
		ChangedProductOrderInfoListMsg result = paNaverV3InfoCommonService.getChangedProductOrderInfoList(lastChangedType, fromDate, toDate, productOrderId, limitCount, procId, request);
		
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode()) && !result.getProductOrderInfoList().isEmpty()) {
			/** 2) 주문/클레임 케이스별 제휴 데이터 생성 **/
			procChangedProductOrderInfoList(result.getProductOrderInfoList(), procId, request);	
		}
		
		/** 3) 주문/클레임 데이터 생성 (비동기) **/
		createOrderInputAsync(lastChangedType, request);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 변경 상품 주문 내역 미반영건 처리
	 * 
	 * @param request
	 * @param lastChangedType
	 * @param productOrderId
	 * @param procId
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "변경 상품 주문 내역 미반영건 처리", notes = "변경 상품 주문 내역 미반영건 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/re-apply-change")
	public ResponseEntity<?> reApplyChange(HttpServletRequest request, 
			@ApiParam(name = "productOrderId", required = false, value = "상품 주문 번호") @RequestParam(value = "productOrderId", required = false) String productOrderId,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId) throws Exception {
		
		// 변경 상품 주문 내역 미반영건 조회 (TPANAVERORDERCHANGE.CHANGED_APPLIED_YN)
		// 각 주문별로 네이버 변경 상품 주문 내역 조회 API 재조회 (최종변경구분 && 최종변경일시 기준)
		// 데이터 존재시, 기존 데이터로 재처리
		// 데이터 미존재시, 미반영건 대상 제외 처리 (주문상태가 변경된 경우)
		/** 1) 변경 상품 주문 내역 미반영건 조회 **/
		ChangedProductOrderInfoListMsg result = paNaverV3InfoCommonService.getUnappliedChangedProductOrderInfoList(productOrderId, procId, request);
		
		if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode()) && !result.getProductOrderInfoList().isEmpty()) {
			/** 2) 주문/클레임 케이스별 제휴 데이터 생성 **/
			procChangedProductOrderInfoList(result.getProductOrderInfoList(), procId, request);	
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	/**
	 * 상품 주문 상세 내역 조회
	 * 
	 * @param productOrderIds	
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value = "상품 주문 상세 내역 조회", notes = "상품 주문 상세 내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "조회된 내역이 없습니다."),
			@ApiResponse(code = 490, message = "동일 배치 실행"),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@GetMapping(value="/order-detail-info")
	public ResponseEntity<?> getOrderDetailInfo(HttpServletRequest request,
			@ApiParam(name = "productOrderIds", required = true, value = "상품 주문 번호") @RequestParam(value = "productOrderIds", required = true) String productOrderIds,
			@ApiParam(name = "procId", required = false, value = "처리자 ID") @RequestParam(value = "procId", required = false, defaultValue = "PANAVER") String procId) throws Exception {

		ProductOrderInfoMsg result = paNaverV3InfoCommonService.orderDetailInfo(productOrderIds, procId, request);
		
		return new ResponseEntity<ProductOrderInfoMsg>(result, HttpStatus.OK);
	}
	
	/**
	 * 주문/클레임 케이스별 네이버 API 호출 및 제휴 데이터 생성
	 * 
	 * @param productOrderInfoList
	 * @param procId
	 * @param request
	 * @throws Exception 
	 */
	private void procChangedProductOrderInfoList(List<ChangedProductOrderInfo> changedProductOrderInfoList, String procId, HttpServletRequest request) throws Exception {
		ProductOrderInfoMsg orderDetailResult = null; // 상품 주문 상세내역 조회 응답
		ProductOrderInfoAll orderDetailInfo = null; // 상품 주문 상세내역 정보

		for (ChangedProductOrderInfo order: changedProductOrderInfoList) {
			ResponseMsg result = new ResponseMsg("", "");
			
			try {
				Thread.sleep(500); // 네이버 호출량 제어 (1초당 2건)
				
				/** 결제완료/배송지변경/선물하기 수락 > 네이버 발주 처리 (결제완료 > 상품준비중) **/
				// 1) 발주 처리 (네이버)
				// 2) 발주 처리된 주문은 변경 내용 반영여부 업데이트 (0 -> 1) > 발주실패건은 미반영건 조회시 재처리
				if (order.getLastChangedType().equals(LastChangedType.PAYED.toString()) 
						|| order.getLastChangedType().equals(LastChangedType.DELIVERY_ADDRESS_CHANGED.toString())
						|| order.getLastChangedType().equals(LastChangedType.GIFT_RECEIVED.toString())) {
					/** 1) 발주 처리  **/
					result = paNaverV3DeliveryService.orderConfirmProc(order.getProductOrderId(), procId, request);
	
					if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) continue;	
				}

				/** 클레임 요청/철회/완료 > 제휴 클레임 데이터 생성 **/
				// 1) 상품 주문 상세내역 조회 (네이버)
				// 2) 각 주문/클레임 케이스별 처리 (제휴 데이터 생성 및 네이버 API 호출)
				// 3) 정상 처리된 주문은 변경 내용 반영여부 업데이트 (0 -> 1) > 비정상건은 미반영건 조회시 재처리
				/** 1) 상품 주문 상세내역 조회 **/
				orderDetailResult = paNaverV3InfoCommonService.orderDetailInfo(order.getProductOrderId(), procId, request);
				
				if (orderDetailResult != null && String.valueOf(HttpStatus.OK.value()).equals(orderDetailResult.getCode()) && !orderDetailResult.getProductOrderInfoList().isEmpty()) {
					orderDetailInfo = orderDetailResult.getProductOrderInfoList().get(0);

					String orderId = orderDetailInfo.getOrder().getOrderId();
					String productOrderId = orderDetailInfo.getProductOrder().getProductOrderId();
					String claimType = orderDetailInfo.getProductOrder().getClaimType();
					String claimId = orderDetailInfo.getProductOrder().getClaimId();
					
					if (order.getClaimStatus() != null) {
						/** 2) 주문/클레임 케이스별 처리 **/
						switch (LastChangedType.valueOf(order.getLastChangedType())) {   
						    // 클레임 요청 (취소/반품/교환)
						    case CLAIM_REQUESTED: 
					    		// 기존 클레임 요청 접수건 체크 > 클레임 철회 데이터 생성
					    		checkExistingClaim(order, orderDetailInfo, request);
					    		
								if (order.getClaimStatus().equals(ClaimStatusType.CANCEL_REQUEST.toString())) {	// 취소요청								
									// 취소 내역 상세 적재
									ProductOrderInfoAll cancelApprovalTarget = paNaverV3InfoCommonService.insertCancelInfoTx(orderDetailInfo);
									
									// 취소 승인 요청 (네이버)
									if (cancelApprovalTarget != null) {
										result = paNaverV3CancelService.approvalCancel(orderId, productOrderId, "0", procId, request);
									
										if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) continue;
									}
								} else if (order.getClaimStatus().equals(ClaimStatusType.RETURN_REQUEST.toString())) { // 반품요청
									paNaverV3InfoCommonService.insertReturnInfoTx(orderDetailInfo);
								} else if (order.getClaimStatus().equals(ClaimStatusType.EXCHANGE_REQUEST.toString())) { // 교환요청
									paNaverV3InfoCommonService.insertExchangeInfoTx(orderDetailInfo); 
								}
						    	break;
						    	
						    // 클레임 철회 (취소/반품/교환)
						    case CLAIM_REJECTED: 		
								if (order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT.toString())) { // 취소철회
									paNaverV3InfoCommonService.insertCancelInfoTx(orderDetailInfo);
								} else if (order.getClaimStatus().equals(ClaimStatusType.RETURN_REJECT.toString())) { // 반품철회
									paNaverV3InfoCommonService.insertReturnInfoTx(orderDetailInfo);
								} else if (order.getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT.toString())) { // 교환철회
									paNaverV3InfoCommonService.insertExchangeInfoTx(orderDetailInfo); 
								}
						    	break;
						    	
						    // 클레임 완료 (취소/반품)
						    case CLAIM_COMPLETED: 	
								if (order.getClaimStatus().equals(ClaimStatusType.CANCEL_DONE.toString()) 
										|| order.getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE.toString())) { // 취소완료, 직권취소완료(반품접수)
									paNaverV3InfoCommonService.insertCancelInfoTx(orderDetailInfo); 
								} else if (order.getClaimStatus().equals(ClaimStatusType.RETURN_DONE.toString())) { // 반품완료
									paNaverV3InfoCommonService.insertReturnInfoTx(orderDetailInfo); 
								}
						        break;
						        
						    // 발송처리
						    case DISPATCHED: 
						    	// 취소 철회 데이터 생성 여부 체크 (TPANAVERCLAIMLIST)
						    	if (paNaverV3InfoCommonService.selectExistingCancelReject(orderId, productOrderId, claimId) > 0) break;
						    	
						    	if (order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT.toString())) { // 취소철회
						    		paNaverV3InfoCommonService.insertCancelInfoTx(orderDetailInfo); // 출고건 발송처리 호출 > 자동 취소철회 > 취소 내역 상세 적재
								}
						        break;
						        
						    // 구매확정 (클레임 철회 미생성건)
						    case PURCHASE_DECIDED:	
						    	// 클레임 철회 데이터 생성 여부 체크 (TPAORDERM)
						    	if (paNaverV3InfoCommonService.selectExistingClaimReject(orderId, productOrderId, claimType, claimId) > 0) break;	
						    	
						    	if (order.getClaimStatus().equals(ClaimStatusType.RETURN_REJECT.toString())) { // 반품철회
						    		paNaverV3InfoCommonService.insertReturnInfoTx(orderDetailInfo); // 반품철회(고객) > 구매확정(고객) > 구매확정 조회(반품철회 조회전) > 반품 철회내역 적재
								} else if (order.getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT.toString())) { // 교환철회
						    		paNaverV3InfoCommonService.insertExchangeInfoTx(orderDetailInfo); // 교환철회(고객) > 구매확정(고객) > 구매확정 조회(교환철회 조회전) > 교환 철회내역 적재
								}
						        break;
						        
						    default:
						        break;
					    }
					}
					
					/** 3) 변경 내역 반영여부 업데이트 (TPANAVERORDERCHANGE.CHANGE_APPLIED_YN) **/
					if (paNaverV3InfoCommonService.updateChangeApplied(order) < 0) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
					}
				} else {
					log.error("제휴 클레임 데이터 생성전, 네이버 상품 상세내역 조회 실패");
				}
			} catch(Exception e) {
				log.error("message", e);
			}
		}
	}
	
	private void checkExistingClaim(ChangedProductOrderInfo order, ProductOrderInfoAll orderDetailInfo, HttpServletRequest request) throws Exception {
		ProductOrderInfoAll claimRejectInfo = new ProductOrderInfoAll(); // 철회 데이터
		String productOrderId = order.getProductOrderId();
		String claimType = order.getClaimType();
		String claimStatus = "";
		
		ProductOrderInfo productOrderInfo = new ProductOrderInfo();
		productOrderInfo.setProductOrderId(orderDetailInfo.getProductOrder().getProductOrderId());
		productOrderInfo.setClaimId(orderDetailInfo.getProductOrder().getClaimId());
		productOrderInfo.setQuantity(orderDetailInfo.getProductOrder().getQuantity());
		productOrderInfo.setProductId(orderDetailInfo.getProductOrder().getProductId());
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderDetailInfo.getOrder().getOrderId());
		
		// 1) 기존 클레임 요청 접수건 철회 (고객)
		// 2) 철회 데이터 조회 및 생성전, 신규 클레임 요청 (고객)
		// 3) 신규 클레임 요청 조회 (변경상품주문내역 조회)
		// 4) 기존 클레임 요청 철회 데이터 저장
		switch(ClaimType.valueOf(claimType)) {
			case CANCEL:
				// 취소 진행여부 확인
				claimStatus = paNaverV3InfoCommonService.getExistingClaimCount(productOrderId, ClaimType.CANCEL.toString(), productOrderInfo.getClaimId());
				
				if (claimStatus != null && !claimStatus.isEmpty()) {
					// 기존 클레임 요청 철회정보 세팅
					CancelOrderInfo cancelInfo = new CancelOrderInfo();
					cancelInfo.setClaimRequestDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					cancelInfo.setCancelDetailedReason("취소철회 미조회 중 새 취소 요청으로 인한 취소철회"); // 취소철회 > 신규 취소요청 (철회 데이터 수집전, 신규 취소요청한 케이스)
					cancelInfo.setCancelReason("ETC");
					cancelInfo.setClaimStatus(ClaimStatusType.CANCEL_REJECT.toString());
					cancelInfo.setRequestChannel("API"); // 기취소 여부 체크시 사용 (PRE_CANCEL_YN = 1)
					
					productOrderInfo.setClaimType(ClaimType.CANCEL.toString());
					productOrderInfo.setClaimStatus(ClaimStatusType.CANCEL_REJECT.toString());
					
					claimRejectInfo.setProductOrder(productOrderInfo);
					claimRejectInfo.setCancelOrder(cancelInfo);
					claimRejectInfo.setOrder(orderInfo);
					
					// 기존 취소요청 철회 데이터 저장 (TPANAVERCLAIMLIST INSERT, TPANAVERORDERLIST UPDATE)
					paNaverV3InfoCommonService.insertCancelInfoTx(claimRejectInfo);
				}
				break;
				
			case RETURN:
				// 반품 진행여부 확인
				claimStatus = paNaverV3InfoCommonService.getExistingClaimCount(productOrderId, ClaimType.RETURN.toString(), productOrderInfo.getClaimId());
				
				if (claimStatus!= null && !claimStatus.isEmpty()) {
					// 기존 클레임 배송비 정보 조회
					HashMap<String, String> returnShipcostMap = paNaverV3InfoCommonService.selectClaimShipcostInfo(productOrderId, claimStatus);
					
					// 기존 클레임 요청 철회정보 세팅
					ReturnOrderInfo returnInfo = new ReturnOrderInfo();
					returnInfo.setClaimRequestDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					returnInfo.setReturnDetailedReason("반품철회 미조회 중 새 반품 요청으로 인한 반품철회");
					returnInfo.setReturnReason("ETC");
					returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT.toString());
					returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
					returnInfo.setEtcFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
					returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
					returnInfo.setCollectDeliveryMethod(ComUtil.objToStr(returnShipcostMap.get("COLLECT_DELIVERY_METHOD")));
					returnInfo.setRequestChannel("API"); // 기취소 여부 체크시 사용 (PRE_CANCEL_YN = 1)

					productOrderInfo.setClaimType(ClaimType.RETURN.toString());
					productOrderInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT.toString());
					
					claimRejectInfo.setProductOrder(productOrderInfo);
					claimRejectInfo.setReturnOrder(returnInfo);
					claimRejectInfo.setOrder(orderInfo);
					
					// 기존 반품요청 철회 데이터 저장
					if (paNaverV3InfoCommonService.insertReturnInfoTx(claimRejectInfo) != null) {
						claimCancelMain(request); // 반품 철회 실데이터 생성 (비동기)
					}
				}
				
				// 교환 진행여부 확인
				claimStatus = paNaverV3InfoCommonService.getExistingClaimCount(productOrderId, ClaimType.EXCHANGE.toString(), productOrderInfo.getClaimId());
				
				if (claimStatus != null && !claimStatus.isEmpty()) {
					// 기존 클레임 배송비 정보 조회
					HashMap<String, String> returnShipcostMap = paNaverV3InfoCommonService.selectClaimShipcostInfo(productOrderId, claimStatus);
					
					// 기존 클레임 요청 철회정보 세팅
					ExchangeOrderInfo exchangeInfo = new ExchangeOrderInfo();
					exchangeInfo.setClaimRequestDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					exchangeInfo.setExchangeDetailedReason("반품 요청으로 인한 교환철회");
					exchangeInfo.setExchangeReason("ETC");
					exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT.toString());
					exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
					exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
					exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
					exchangeInfo.setRequestChannel("API"); // 기취소 여부 체크시 사용 (PRE_CANCEL_YN = 1)
	
					productOrderInfo.setClaimType(ClaimType.EXCHANGE.toString());
					productOrderInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT.toString());
					
					claimRejectInfo.setProductOrder(productOrderInfo);
					claimRejectInfo.setExchangeOrder(exchangeInfo);
					claimRejectInfo.setOrder(orderInfo);
					
					// 기존 교환요청 철회 데이터 저장
					if (paNaverV3InfoCommonService.insertExchangeInfoTx(claimRejectInfo) != null) {
						changeCancelMain(request); // 교환 철회 실데이터 생성 (비동기)
					}
				}
				break;
				
			case EXCHANGE:
				// 반품 진행여부 확인
				claimStatus = paNaverV3InfoCommonService.getExistingClaimCount(productOrderId, ClaimType.RETURN.toString(), productOrderInfo.getClaimId());
				
				if (claimStatus!=null && !claimStatus.isEmpty()) {
					// 기존 클레임 배송비 정보 조회
					HashMap<String, String> returnShipcostMap = paNaverV3InfoCommonService.selectClaimShipcostInfo(productOrderId, claimStatus);
					
					// 기존 클레임 요청 철회정보 세팅
					ReturnOrderInfo returnInfo = new ReturnOrderInfo();
					returnInfo.setClaimRequestDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					returnInfo.setReturnDetailedReason("교환 요청으로 인한 반품철회");
					returnInfo.setReturnReason("ETC");
					returnInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT.toString());
					returnInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
					returnInfo.setEtcFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
					returnInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
					returnInfo.setCollectDeliveryMethod(ComUtil.objToStr(returnShipcostMap.get("COLLECT_DELIVERY_METHOD")));
					returnInfo.setRequestChannel("API"); // 기취소 여부 체크시 사용 (PRE_CANCEL_YN = 1)
					
					productOrderInfo.setClaimType(ClaimType.RETURN.toString());
					productOrderInfo.setClaimStatus(ClaimStatusType.RETURN_REJECT.toString());
					
					claimRejectInfo.setProductOrder(productOrderInfo);
					claimRejectInfo.setReturnOrder(returnInfo);
					claimRejectInfo.setOrder(orderInfo);
					
					// 기존 반품요청 철회 데이터 저장
					if (paNaverV3InfoCommonService.insertReturnInfoTx(claimRejectInfo) != null) {
						claimCancelMain(request); // 반품 철회 실데이터 생성 (비동기)
					}
				}
				
				// 교환 진행여부 확인
				claimStatus = paNaverV3InfoCommonService.getExistingClaimCount(productOrderId, ClaimType.EXCHANGE.toString(), productOrderInfo.getClaimId());
				
				if (claimStatus != null && !claimStatus.isEmpty()) {
					// 기존 클레임 배송비 정보 조회
					HashMap<String, String> returnShipcostMap = paNaverV3InfoCommonService.selectClaimShipcostInfo(productOrderId, claimStatus);
					
					// 기존 클레임 요청 철회정보 세팅
					ExchangeOrderInfo exchangeInfo = new ExchangeOrderInfo();
					exchangeInfo.setClaimRequestDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					exchangeInfo.setExchangeDetailedReason("교환철회 미조회 중 새 교환 요청으로 인한 교환철회");
					exchangeInfo.setExchangeReason("ETC");
					exchangeInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT.toString());
					exchangeInfo.setClaimDeliveryFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DEMAND_AMOUNT")));
					exchangeInfo.setEtcFeeDemandAmount(ComUtil.objToDouble(returnShipcostMap.get("ETC_FEE_DEMAND_AMOUNT")));
					exchangeInfo.setClaimDeliveryFeeDiscountAmount(ComUtil.objToDouble(returnShipcostMap.get("DELIVERY_FEE_DISCOUNT_AMOUNT")));
					exchangeInfo.setRequestChannel("API"); // 기취소 여부 체크시 사용 (PRE_CANCEL_YN = 1)
					
					productOrderInfo.setClaimType(ClaimType.EXCHANGE.toString());
					productOrderInfo.setClaimStatus(ClaimStatusType.EXCHANGE_REJECT.toString());
					
					claimRejectInfo.setProductOrder(productOrderInfo);
					claimRejectInfo.setExchangeOrder(exchangeInfo);
					claimRejectInfo.setOrder(orderInfo);	
					
					// 기존 교환요청 철회 데이터 저장
					if (paNaverV3InfoCommonService.insertExchangeInfoTx(claimRejectInfo) != null) {
						changeCancelMain(request); // 교환 철회 실데이터 생성 (비동기)
					}
				}
				break;
				
			default:
				break;
		}
	}

	/**
	 * 주문/클레임 데이터 생성 (비동기)
	 * 
	 * @param lastChangedType
	 * @param request
	 * @throws Exception 
	 */
	private void createOrderInputAsync(String lastChangedType, HttpServletRequest request) throws Exception {
		
		// 변경 상품주문 내역 조회시 최종 변경 구분(lastChangedType)별로 실데이터 생성 대상을 조회하여 주문/클레임 실데이터를 비동기로 생성
		// 실데이터 생성 대상 > 변경 상품주문 내역 조회결과 반영건 (제휴 데이터 생성건)
		switch (LastChangedType.valueOf(lastChangedType)) {
			case PAYED: case DELIVERY_ADDRESS_CHANGED: case GIFT_RECEIVED:
				orderInputMain(request); // 주문접수 데이터 생성
				break;
				
			case CLAIM_REQUESTED: 
				cancelInputMain(request); // 취소요청
				orderClaimMain(request); // 반품요청
				orderChangeMain(request); // 교환요청
				break;
				
			case CLAIM_REJECTED: case PURCHASE_DECIDED:
				claimCancelMain(request); // 반품철회(반품취소)
				changeCancelMain(request); // 교환철회(교환회취)
				break;
				
			case CLAIM_COMPLETED:
				cancelInputMain(request); // 취소완료건
				orderClaimMain(request); // 반품완료건
				break;
				
			default:
				break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void orderInputMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_INPUT";
		
		log.info("=========================== NAVER V3 Order Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderInputTargetList = paNaverV3InfoCommonService.selectOrderInputTargetList();
			HashMap<String, String> hmSheet = null;
			
			int procCnt = orderInputTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, String>) orderInputTargetList.get(i);
					
					paNaverV3AsyncController.orderCreateAsync(request, hmSheet.get("PA_ORDER_NO"));
				
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
			log.info("=========================== NAVER V3 Order Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void cancelInputMain(HttpServletRequest request) throws Exception{ 
		String duplicateCheck = "";
		String prg_id = "PANAVER_CANCEL_INPUT";
		
		log.info("=========================== NAVER V3 Cancel Create Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> cancelInputTargetList = paNaverV3InfoCommonService.selectCancelInputTargetList();
			
			HashMap<String, Object> hmSheet = null;
			int procCnt = cancelInputTargetList.size();
	
			ParamMap cancelMap;
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) cancelInputTargetList.get(i);
					cancelMap = new ParamMap();
					cancelMap.put("orderId", hmSheet.get("PA_ORDER_NO").toString());
					cancelMap.put("productOrderId", hmSheet.get("PA_ORDER_SEQ").toString());
					paNaverV3AsyncController.cancelInputAsync(request, cancelMap);
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
			log.info("=========================== NAVER V3 Cancel Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void orderClaimMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_CLAIM";

		log.info("=========================== NAVER V3 Order Claim Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> orderClaimTargetList = paNaverV3InfoCommonService.selectOrderClaimTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = orderClaimTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList.get(i);

					paNaverV3AsyncController.returnInputAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
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
			log.info("=========================== NAVER V3 Order Claim End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void orderChangeMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PANAVER_ORDER_CHANGE";
		HashMap<String, Object> hmSheet = null;
		int procCnt = 0;
		
		log.info("=========================== NAVER V3Order Change Start =========================");	
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderChangeTargetList = paNaverV3InfoCommonService.selectOrderChangeTargetList();
			
			procCnt = orderChangeTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{			
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderChangeTargetList.get(i);
					
					paNaverV3AsyncController.exchangeInputAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
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
			log.info("=========================== NAVER V3 Order Change End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void claimCancelMain(HttpServletRequest request) throws Exception {
		String duplicateCheck = "";
		String prg_id = "PANAVER_CLAIM_CANCEL";

		log.info("=========================== NAVER V3 Claim Cancel Create Start =========================");
		try {
			// = 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated",
						new String[] { prg_id });
			List<Object> claimCancelTargetList = paNaverV3InfoCommonService.selectClaimCancelTargetList();

			HashMap<String, Object> hmSheet = null;
			int procCnt = claimCancelTargetList.size();

			for (int i = 0; procCnt > i; i++) {
				try {
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) claimCancelTargetList.get(i);
					
					paNaverV3AsyncController.returnCancelAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
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
			log.info("=========================== NAVER V3 Claim Cancel Create End =========================");
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void changeCancelMain(HttpServletRequest request) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PANAVER_CHANGE_CANCEL";
		HashMap<String, Object> hmSheet = null;
		
		log.info("=========================== NAVER V3 Change Cancel Start =========================");
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> changeCancelTargetList = paNaverV3InfoCommonService.selectChangeCancelTargetList();
			int procCnt = changeCancelTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{		
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) changeCancelTargetList.get(i);
					
					paNaverV3AsyncController.exchangeCancelAsync(request, hmSheet.get("PA_ORDER_NO").toString(), hmSheet.get("PA_ORDER_SEQ").toString(), hmSheet.get("PA_CLAIM_NO").toString());
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
			log.info("=========================== NAVER V3 Change Cancel End =========================");
		}
		return;
	}
}
