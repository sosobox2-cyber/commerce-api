package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderInfo {
	
	//클레임 상태
	private String claimStatus;
	//클레임 구분(CANCEL, RETURN, EXCHANGE, PURCHASE_DECISION_HOLDBACK 구매 확정 보류, ADMIN_CANCEL )
	private String claimType;
	//구매 확정일
	private Timestamp decisionDate;
	//발송 지연 상세 사유
	private String delayedDispatchDetailedReason;
	//발송 지연 사유 코드(PRODUCT_PREPARE 상품 준비중, CUSTOMER_REQUEST 고객 요청, CUSTOM_BUILD 주문제작, RESERVED_DISPATCH 예약발송, OVERSEA_DELIVERY 해외 배송, ETC 기타 )
	private String delayedDispatchReason;
	//배송비 최종 할인액
	private double deliveryDiscountAmount;
	//배송비 합계
	private double deliveryFeeAmount ;
	//배송비 정책(조건별 무료 등)
	private String deliveryPolicyType ;
	//배송 방법 코드
	//(DELIVERY, GDFW_ISSUE_SVC, VISIT_RECEIPT, DIRECT_DELIVERY, QUICK_SVC, NOTHING, RETURN_DESIGNATED, 
	// RETURN_DELIVERY, RETURN_INDIVIDUAL, RETURN_MERCHANT, UNKNOWN)
	private String expectedDeliveryMethod;
	//사은품
	private String freeGift;
	//가맹점 ID
	private String mallId;
	//옵션 코드
	private String optionCode;
	//옵션 금액
	private double optionPrice;
	//묶음배송 번호
	private String packageNumber;
	//발주 확인일
	private Timestamp placeOrderDate;
	//발주 상태(NOT_YET 발주 미확인, OK 발주 확인, CANCEL 발주 확인 해제)
	private String placeOrderStatus;
	//상품 종류(일반/추가 상품 구분)
	private String productClass;
	//상품별 할인액
	private String productDiscountAmount;
	//채널 상품 번호
	private String productId;
	//원상품 번호
	private String originalProductId;
	//채널 번호
	private String merchantChannelId;
	//상품명
	private String productName;
	//상품 옵션(옵션명)
	private String productOption;
	//상품 주문 번호
	private String productOrderId;
	//상품 주문 상태(PAYMENT_WAITING, PAYED,DELIVERING, DELIVERED, PURCHASE_DECIDED, EXCHANGED, CANCELED,RETURNED, CANCELED_BY_NOPAYMENT)
	private String productOrderStatus;
	//수량
	private Integer quantity;
	//지역별 추가 배송비
	private double sectionDeliveryFee;
	//판매자 상품 코드(판매자가 임의로 지정)
	private String sellerProductCode;
	//배송지(고객)
	private ShippingAddress shippingAddress;//object
	//발송 기한
	private Timestamp shippingDueDate;
	//배송비 형태(선불/착불/무료)
	private String shippingFeeType;
	//배송 메모
	private String shippingMemo;
	//출고지
	private TakingAddress takingAddress;//object
	//총 결제 금액(할인 적용 후 금액)
	private double totalPaymentAmount;
	//상품 주문 금액(할인 적용 전 금액)
	private double totalProductAmount;
	//상품 가격
	private double unitPrice;
	//판매자 부담 할인액
	private double sellerBurdenDiscountAmount;
	//수수료 과금 구분(결제 수수료/(구)판매 수수료/채널 수수료)
	private String commissionRatingType;
	//수수료 선결제 상태 구분(GENERAL_PRD 일반 상품, PRE_PAY_PRD_NO_PAY 선차감(차감 전), PRE_PAY_PRD_PAYED 선차감(차감 후))
	private String commissionPrePayStatus;
	//결제 수수료
	private double paymentCommission;
	//(구)판매 수수료
	private double saleCommission;
	//정산 예정 금액
	private double expectedSettlementAmount;
	//유입 경로(검색광고(SA)/공동구매/밴드/네이버 쇼핑/네이버 쇼핑 외)
	private String inflowPath;
	//옵션 상품이나 추가 상품 등록 시 자동 생성된 아이템 번호로, 옵션 상품, 추가 상품을 구분하는 고유한 값. OptionCode와 동일한 값을 입력합니다
	private String itemNo;
	//옵션 상품이나 추가 상품 등록 시 판매자가 별도로 입력한 옵션 관리 코드. 옵션 상품이나 추가 상품인 경우에 입력합니다
	private String optionManageCode;
	//판매자가 내부에서 사용하는 코드1
	private String sellerCustomCode1;
	//판매자가 내부에서 사용하는 코드2
	private String sellerCustomCode2;
	//클레임 번호
	private String claimId;
	//채널 수수료
	private double channelCommission;
	//구매자 개인통관고유부호. 구매 확정, 교환, 반품, 취소, 미결제 취소 상태의 거래 종료 주문에서는 노출되지 않습니다
	private String individualCustomUniqueCode;
	//상품별 즉시 할인 금액
	private double productImediateDiscountAmount;
	//상품별 상품 할인 쿠폰 금액
	private double productProductDiscountAmount;
	//상품별 복수 구매 할인 금액
	private double productMultiplePurchaseDiscountAmount;
	//판매자 부담 상품 할인 쿠폰 금액
	private double sellerBurdenImediateDiscountAmount;
	//판매자 부담 상품 할인 쿠폰 금액
	private double sellerBurdenProductDiscountAmount;
	//판매자 부담 복수 구매 할인 금액
	private double sellerBurdenMultiplePurchaseDiscountAmount;
	//네이버 쇼핑 매출 연동 수수료
	private double knowledgeShoppingSellingInterlockCommission;
	//선물 수락 상태 구분(WAIT_FOR_RECEIVING 수락 대기(배송지 입력 대기), RECEIVED 수락 완료)
	private String giftReceivingStatus;
	//판매자 부담 스토어 할인 금액
	private double sellerBurdenStoreDiscountAmount;
	//판매자 부담 복수 구매 할인 타입(IGNORE_QUANTITY,QUANTITY)
	private String sellerBurdenMultiplePurchaseDiscountType;
	//물류사 코드
	private String logisticsCompanyId;
	//물류센터 코드
	private String logisticsCenterId;
	//
	private HopeDelivery hopeDelivery;//object
	//배송 도착 보장 일시
	private Timestamp arrivalGuaranteeDate;
	//배송 속성 타입 코드(NORMAL, TODAY, OPTION_TODAY, HOPE, TODAY_ARRIVAL, DAWN_ARRIVAL, PRE_ORDER, ARRIVAL_GUARANTEE)
	private String deliveryAttributeType;
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Timestamp getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(Timestamp decisionDate) {
		this.decisionDate = decisionDate;
	}
	public String getDelayedDispatchDetailedReason() {
		return delayedDispatchDetailedReason;
	}
	public void setDelayedDispatchDetailedReason(String delayedDispatchDetailedReason) {
		this.delayedDispatchDetailedReason = delayedDispatchDetailedReason;
	}
	public String getDelayedDispatchReason() {
		return delayedDispatchReason;
	}
	public void setDelayedDispatchReason(String delayedDispatchReason) {
		this.delayedDispatchReason = delayedDispatchReason;
	}
	public double getDeliveryDiscountAmount() {
		return deliveryDiscountAmount;
	}
	public void setDeliveryDiscountAmount(double deliveryDiscountAmount) {
		this.deliveryDiscountAmount = deliveryDiscountAmount;
	}
	public double getDeliveryFeeAmount() {
		return deliveryFeeAmount;
	}
	public void setDeliveryFeeAmount(double deliveryFeeAmount) {
		this.deliveryFeeAmount = deliveryFeeAmount;
	}
	public String getDeliveryPolicyType() {
		return deliveryPolicyType;
	}
	public void setDeliveryPolicyType(String deliveryPolicyType) {
		this.deliveryPolicyType = deliveryPolicyType;
	}
	public String getExpectedDeliveryMethod() {
		return expectedDeliveryMethod;
	}
	public void setExpectedDeliveryMethod(String expectedDeliveryMethod) {
		this.expectedDeliveryMethod = expectedDeliveryMethod;
	}
	public String getFreeGift() {
		return freeGift;
	}
	public void setFreeGift(String freeGift) {
		this.freeGift = freeGift;
	}
	public String getMallId() {
		return mallId;
	}
	public void setMallId(String mallId) {
		this.mallId = mallId;
	}
	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public double getOptionPrice() {
		return optionPrice;
	}
	public void setOptionPrice(double optionPrice) {
		this.optionPrice = optionPrice;
	}
	public String getPackageNumber() {
		return packageNumber;
	}
	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}
	public Timestamp getPlaceOrderDate() {
		return placeOrderDate;
	}
	public void setPlaceOrderDate(Timestamp placeOrderDate) {
		this.placeOrderDate = placeOrderDate;
	}
	public String getPlaceOrderStatus() {
		return placeOrderStatus;
	}
	public void setPlaceOrderStatus(String placeOrderStatus) {
		this.placeOrderStatus = placeOrderStatus;
	}
	public String getProductClass() {
		return productClass;
	}
	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}
	public String getProductDiscountAmount() {
		return productDiscountAmount;
	}
	public void setProductDiscountAmount(String productDiscountAmount) {
		this.productDiscountAmount = productDiscountAmount;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOriginalProductId() {
		return originalProductId;
	}
	public void setOriginalProductId(String originalProductId) {
		this.originalProductId = originalProductId;
	}
	public String getMerchantChannelId() {
		return merchantChannelId;
	}
	public void setMerchantChannelId(String merchantChannelId) {
		this.merchantChannelId = merchantChannelId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductOption() {
		return productOption;
	}
	public void setProductOption(String productOption) {
		this.productOption = productOption;
	}
	public String getProductOrderId() {
		return productOrderId;
	}
	public void setProductOrderId(String productOrderId) {
		this.productOrderId = productOrderId;
	}
	public String getProductOrderStatus() {
		return productOrderStatus;
	}
	public void setProductOrderStatus(String productOrderStatus) {
		this.productOrderStatus = productOrderStatus;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public double getSectionDeliveryFee() {
		return sectionDeliveryFee;
	}
	public void setSectionDeliveryFee(double sectionDeliveryFee) {
		this.sectionDeliveryFee = sectionDeliveryFee;
	}
	public String getSellerProductCode() {
		return sellerProductCode;
	}
	public void setSellerProductCode(String sellerProductCode) {
		this.sellerProductCode = sellerProductCode;
	}
	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public Timestamp getShippingDueDate() {
		return shippingDueDate;
	}
	public void setShippingDueDate(Timestamp shippingDueDate) {
		this.shippingDueDate = shippingDueDate;
	}
	public String getShippingFeeType() {
		return shippingFeeType;
	}
	public void setShippingFeeType(String shippingFeeType) {
		this.shippingFeeType = shippingFeeType;
	}
	public String getShippingMemo() {
		return shippingMemo;
	}
	public void setShippingMemo(String shippingMemo) {
		this.shippingMemo = shippingMemo;
	}
	public TakingAddress getTakingAddress() {
		return takingAddress;
	}
	public void setTakingAddress(TakingAddress takingAddress) {
		this.takingAddress = takingAddress;
	}
	public double getTotalPaymentAmount() {
		return totalPaymentAmount;
	}
	public void setTotalPaymentAmount(double totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}
	public double getTotalProductAmount() {
		return totalProductAmount;
	}
	public void setTotalProductAmount(double totalProductAmount) {
		this.totalProductAmount = totalProductAmount;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getSellerBurdenDiscountAmount() {
		return sellerBurdenDiscountAmount;
	}
	public void setSellerBurdenDiscountAmount(double sellerBurdenDiscountAmount) {
		this.sellerBurdenDiscountAmount = sellerBurdenDiscountAmount;
	}
	public String getCommissionRatingType() {
		return commissionRatingType;
	}
	public void setCommissionRatingType(String commissionRatingType) {
		this.commissionRatingType = commissionRatingType;
	}
	public String getCommissionPrePayStatus() {
		return commissionPrePayStatus;
	}
	public void setCommissionPrePayStatus(String commissionPrePayStatus) {
		this.commissionPrePayStatus = commissionPrePayStatus;
	}
	public double getPaymentCommission() {
		return paymentCommission;
	}
	public void setPaymentCommission(double paymentCommission) {
		this.paymentCommission = paymentCommission;
	}
	public double getSaleCommission() {
		return saleCommission;
	}
	public void setSaleCommission(double saleCommission) {
		this.saleCommission = saleCommission;
	}
	public double getExpectedSettlementAmount() {
		return expectedSettlementAmount;
	}
	public void setExpectedSettlementAmount(double expectedSettlementAmount) {
		this.expectedSettlementAmount = expectedSettlementAmount;
	}
	public String getInflowPath() {
		return inflowPath;
	}
	public void setInflowPath(String inflowPath) {
		this.inflowPath = inflowPath;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getOptionManageCode() {
		return optionManageCode;
	}
	public void setOptionManageCode(String optionManageCode) {
		this.optionManageCode = optionManageCode;
	}
	public String getSellerCustomCode1() {
		return sellerCustomCode1;
	}
	public void setSellerCustomCode1(String sellerCustomCode1) {
		this.sellerCustomCode1 = sellerCustomCode1;
	}
	public String getSellerCustomCode2() {
		return sellerCustomCode2;
	}
	public void setSellerCustomCode2(String sellerCustomCode2) {
		this.sellerCustomCode2 = sellerCustomCode2;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public double getChannelCommission() {
		return channelCommission;
	}
	public void setChannelCommission(double channelCommission) {
		this.channelCommission = channelCommission;
	}
	public String getIndividualCustomUniqueCode() {
		return individualCustomUniqueCode;
	}
	public void setIndividualCustomUniqueCode(String individualCustomUniqueCode) {
		this.individualCustomUniqueCode = individualCustomUniqueCode;
	}
	public double getProductImediateDiscountAmount() {
		return productImediateDiscountAmount;
	}
	public void setProductImediateDiscountAmount(double productImediateDiscountAmount) {
		this.productImediateDiscountAmount = productImediateDiscountAmount;
	}
	public double getProductProductDiscountAmount() {
		return productProductDiscountAmount;
	}
	public void setProductProductDiscountAmount(double productProductDiscountAmount) {
		this.productProductDiscountAmount = productProductDiscountAmount;
	}
	public double getProductMultiplePurchaseDiscountAmount() {
		return productMultiplePurchaseDiscountAmount;
	}
	public void setProductMultiplePurchaseDiscountAmount(double productMultiplePurchaseDiscountAmount) {
		this.productMultiplePurchaseDiscountAmount = productMultiplePurchaseDiscountAmount;
	}
	public double getSellerBurdenImediateDiscountAmount() {
		return sellerBurdenImediateDiscountAmount;
	}
	public void setSellerBurdenImediateDiscountAmount(double sellerBurdenImediateDiscountAmount) {
		this.sellerBurdenImediateDiscountAmount = sellerBurdenImediateDiscountAmount;
	}
	public double getSellerBurdenProductDiscountAmount() {
		return sellerBurdenProductDiscountAmount;
	}
	public void setSellerBurdenProductDiscountAmount(double sellerBurdenProductDiscountAmount) {
		this.sellerBurdenProductDiscountAmount = sellerBurdenProductDiscountAmount;
	}
	public double getSellerBurdenMultiplePurchaseDiscountAmount() {
		return sellerBurdenMultiplePurchaseDiscountAmount;
	}
	public void setSellerBurdenMultiplePurchaseDiscountAmount(double sellerBurdenMultiplePurchaseDiscountAmount) {
		this.sellerBurdenMultiplePurchaseDiscountAmount = sellerBurdenMultiplePurchaseDiscountAmount;
	}
	public double getKnowledgeShoppingSellingInterlockCommission() {
		return knowledgeShoppingSellingInterlockCommission;
	}
	public void setKnowledgeShoppingSellingInterlockCommission(double knowledgeShoppingSellingInterlockCommission) {
		this.knowledgeShoppingSellingInterlockCommission = knowledgeShoppingSellingInterlockCommission;
	}
	public String getGiftReceivingStatus() {
		return giftReceivingStatus;
	}
	public void setGiftReceivingStatus(String giftReceivingStatus) {
		this.giftReceivingStatus = giftReceivingStatus;
	}
	public double getSellerBurdenStoreDiscountAmount() {
		return sellerBurdenStoreDiscountAmount;
	}
	public void setSellerBurdenStoreDiscountAmount(double sellerBurdenStoreDiscountAmount) {
		this.sellerBurdenStoreDiscountAmount = sellerBurdenStoreDiscountAmount;
	}
	public String getSellerBurdenMultiplePurchaseDiscountType() {
		return sellerBurdenMultiplePurchaseDiscountType;
	}
	public void setSellerBurdenMultiplePurchaseDiscountType(String sellerBurdenMultiplePurchaseDiscountType) {
		this.sellerBurdenMultiplePurchaseDiscountType = sellerBurdenMultiplePurchaseDiscountType;
	}
	public String getLogisticsCompanyId() {
		return logisticsCompanyId;
	}
	public void setLogisticsCompanyId(String logisticsCompanyId) {
		this.logisticsCompanyId = logisticsCompanyId;
	}
	public String getLogisticsCenterId() {
		return logisticsCenterId;
	}
	public void setLogisticsCenterId(String logisticsCenterId) {
		this.logisticsCenterId = logisticsCenterId;
	}
	public HopeDelivery getHopeDelivery() {
		return hopeDelivery;
	}
	public void setHopeDelivery(HopeDelivery hopeDelivery) {
		this.hopeDelivery = hopeDelivery;
	}
	public Timestamp getArrivalGuaranteeDate() {
		return arrivalGuaranteeDate;
	}
	public void setArrivalGuaranteeDate(Timestamp arrivalGuaranteeDate) {
		this.arrivalGuaranteeDate = arrivalGuaranteeDate;
	}
	public String getDeliveryAttributeType() {
		return deliveryAttributeType;
	}
	public void setDeliveryAttributeType(String deliveryAttributeType) {
		this.deliveryAttributeType = deliveryAttributeType;
	}
	@Override
	public String toString() {
		return "ProductOrderInfo [claimStatus=" + claimStatus + ", claimType=" + claimType + ", decisionDate="
				+ decisionDate + ", delayedDispatchDetailedReason=" + delayedDispatchDetailedReason
				+ ", delayedDispatchReason=" + delayedDispatchReason + ", deliveryDiscountAmount="
				+ deliveryDiscountAmount + ", deliveryFeeAmount=" + deliveryFeeAmount + ", deliveryPolicyType="
				+ deliveryPolicyType + ", expectedDeliveryMethod=" + expectedDeliveryMethod + ", freeGift=" + freeGift
				+ ", mallId=" + mallId + ", optionCode=" + optionCode + ", optionPrice=" + optionPrice
				+ ", packageNumber=" + packageNumber + ", placeOrderDate=" + placeOrderDate + ", placeOrderStatus="
				+ placeOrderStatus + ", productClass=" + productClass + ", productDiscountAmount="
				+ productDiscountAmount + ", productId=" + productId + ", originalProductId=" + originalProductId
				+ ", merchantChannelId=" + merchantChannelId + ", productName=" + productName + ", productOption="
				+ productOption + ", productOrderId=" + productOrderId + ", productOrderStatus=" + productOrderStatus
				+ ", quantity=" + quantity + ", sectionDeliveryFee=" + sectionDeliveryFee + ", sellerProductCode="
				+ sellerProductCode + ", shippingAddress=" + shippingAddress + ", shippingDueDate=" + shippingDueDate
				+ ", shippingFeeType=" + shippingFeeType + ", shippingMemo=" + shippingMemo + ", takingAddress="
				+ takingAddress + ", totalPaymentAmount=" + totalPaymentAmount + ", totalProductAmount="
				+ totalProductAmount + ", unitPrice=" + unitPrice + ", sellerBurdenDiscountAmount="
				+ sellerBurdenDiscountAmount + ", commissionRatingType=" + commissionRatingType
				+ ", commissionPrePayStatus=" + commissionPrePayStatus + ", paymentCommission=" + paymentCommission
				+ ", saleCommission=" + saleCommission + ", expectedSettlementAmount=" + expectedSettlementAmount
				+ ", inflowPath=" + inflowPath + ", itemNo=" + itemNo + ", optionManageCode=" + optionManageCode
				+ ", sellerCustomCode1=" + sellerCustomCode1 + ", sellerCustomCode2=" + sellerCustomCode2 + ", claimId="
				+ claimId + ", channelCommission=" + channelCommission + ", individualCustomUniqueCode="
				+ individualCustomUniqueCode + ", productImediateDiscountAmount=" + productImediateDiscountAmount
				+ ", productProductDiscountAmount=" + productProductDiscountAmount
				+ ", productMultiplePurchaseDiscountAmount=" + productMultiplePurchaseDiscountAmount
				+ ", sellerBurdenImediateDiscountAmount=" + sellerBurdenImediateDiscountAmount
				+ ", sellerBurdenProductDiscountAmount=" + sellerBurdenProductDiscountAmount
				+ ", sellerBurdenMultiplePurchaseDiscountAmount=" + sellerBurdenMultiplePurchaseDiscountAmount
				+ ", knowledgeShoppingSellingInterlockCommission=" + knowledgeShoppingSellingInterlockCommission
				+ ", giftReceivingStatus=" + giftReceivingStatus + ", sellerBurdenStoreDiscountAmount="
				+ sellerBurdenStoreDiscountAmount + ", sellerBurdenMultiplePurchaseDiscountType="
				+ sellerBurdenMultiplePurchaseDiscountType + ", logisticsCompanyId=" + logisticsCompanyId
				+ ", logisticsCenterId=" + logisticsCenterId + ", hopeDelivery=" + hopeDelivery
				+ ", arrivalGuaranteeDate=" + arrivalGuaranteeDate + ", deliveryAttributeType=" + deliveryAttributeType
				+ "]";
	}
	

}
