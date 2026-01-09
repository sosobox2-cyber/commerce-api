package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaKakaoSettlement extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	private String pakakaoSalesNo;           // 카카오정산NO
	private String paCode;                   // 제휴사코드[O501]
	private String paymentId;                // 결제번호
	private String deliveryAmountOriginId;   // 최초 배송비 번호
	private String id;                       // 주문번호
	private String settleType;               // 정산구분
	private String status;                   // 주문상태
	private String settleState;              // 정산확정상태
	private String settlementCycle;          // 정산주기
	private String settlementDate;           // 정산기준일
	private String settleConfirmDate;        // 정산확정예정일
	private String decidedAt;                // 구매결정일
	private String orderPaidAt;              // 주문결제일
	private String orderCancledAt;           // 주문 취소/환불일
	private String paidAt;                   // 결제일
	private String canceledAt;               // 취소/환불일
	private String channel;                  // 채널
	private String productId;                // 상품번호
	private String goodsName;                // 상품명
	private String modelName;                // 모델명
	private String goodsdtInfo;              // 옵션명
	private String sellerNo;                 // 판매자번호
	private String sellerName;               // 판매자명
	private String paymentType;              // 결제수단
	private Double productPrice;             // 상품주문금액
	private Double totalSellerDiscount;      // 판매자할인금액합계
	private Double sellerDiscount;           // 판매자즉시할인
	private Double shareDiscount;            // 소문내기판매자할인
	private Double talkdealDiscount;         // 톡딜할인
	private Double sellerDiscountCoupon;     // 판매자할인쿠폰
	private Double settlementFee;            // 정산기준금액
	private Double settlementBasicPrice;     // 결제금액
	private Double prepaidFee;               // 선불배송비
	private Double quantity;                 // 수량
	private Double baseFeePer;               // 기본수수료율
	private Double baseFee;                  // 기본수수료
	private Double displayFeePer;            // 노출추가수수료율
	private Double displayFee;               // 노출추가수수료
	private Double totalFeePer;              // 수수료율 합계
	private Double totalFee;                 // 수수료 합계
	private Double salesSettleAmount;        // 판매정산금액
	private Double totalKakaoDiscount;       // 카카오할인금액 합계
	private Double kakaoDiscount;            // 카카오즉시할인
	private Double kakaoShareDiscount;       // 소문내기카카오할인
	private Double kakaoCoupon;              // 카카오할인쿠폰
	private Double pointRewardPer;           // 포인트리워드율
	private Double pointReward;              // 포인트리워드금액
	private Double authDiscount;             // 인증할인
	private Double sellerCartDiscountCoupon; // 판매자장바구니할인쿠폰
	private Double returnPrepaidFee;         // 반품배송비
	public String getPakakaoSalesNo() {
		return pakakaoSalesNo;
	}
	public void setPakakaoSalesNo(String pakakaoSalesNo) {
		this.pakakaoSalesNo = pakakaoSalesNo;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getDeliveryAmountOriginId() {
		return deliveryAmountOriginId;
	}
	public void setDeliveryAmountOriginId(String deliveryAmountOriginId) {
		this.deliveryAmountOriginId = deliveryAmountOriginId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSettleState() {
		return settleState;
	}
	public void setSettleState(String settleState) {
		this.settleState = settleState;
	}
	public String getSettlementCycle() {
		return settlementCycle;
	}
	public void setSettlementCycle(String settlementCycle) {
		this.settlementCycle = settlementCycle;
	}
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public String getSettleConfirmDate() {
		return settleConfirmDate;
	}
	public void setSettleConfirmDate(String settleConfirmDate) {
		this.settleConfirmDate = settleConfirmDate;
	}
	public String getDecidedAt() {
		return decidedAt;
	}
	public void setDecidedAt(String decidedAt) {
		this.decidedAt = decidedAt;
	}
	public String getOrderPaidAt() {
		return orderPaidAt;
	}
	public void setOrderPaidAt(String orderPaidAt) {
		this.orderPaidAt = orderPaidAt;
	}
	public String getOrderCancledAt() {
		return orderCancledAt;
	}
	public void setOrderCancledAt(String orderCancledAt) {
		this.orderCancledAt = orderCancledAt;
	}
	public String getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(String paidAt) {
		this.paidAt = paidAt;
	}
	public String getCanceledAt() {
		return canceledAt;
	}
	public void setCanceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getGoodsdtInfo() {
		return goodsdtInfo;
	}
	public void setGoodsdtInfo(String goodsdtInfo) {
		this.goodsdtInfo = goodsdtInfo;
	}
	public String getSellerNo() {
		return sellerNo;
	}
	public void setSellerNo(String sellerNo) {
		this.sellerNo = sellerNo;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public Double getTotalSellerDiscount() {
		return totalSellerDiscount;
	}
	public void setTotalSellerDiscount(Double totalSellerDiscount) {
		this.totalSellerDiscount = totalSellerDiscount;
	}
	public Double getSellerDiscount() {
		return sellerDiscount;
	}
	public void setSellerDiscount(Double sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}
	public Double getShareDiscount() {
		return shareDiscount;
	}
	public void setShareDiscount(Double shareDiscount) {
		this.shareDiscount = shareDiscount;
	}
	public Double getTalkdealDiscount() {
		return talkdealDiscount;
	}
	public void setTalkdealDiscount(Double talkdealDiscount) {
		this.talkdealDiscount = talkdealDiscount;
	}
	public Double getSellerDiscountCoupon() {
		return sellerDiscountCoupon;
	}
	public void setSellerDiscountCoupon(Double sellerDiscountCoupon) {
		this.sellerDiscountCoupon = sellerDiscountCoupon;
	}
	public Double getSettlementFee() {
		return settlementFee;
	}
	public void setSettlementFee(Double settlementFee) {
		this.settlementFee = settlementFee;
	}
	public Double getSettlementBasicPrice() {
		return settlementBasicPrice;
	}
	public void setSettlementBasicPrice(Double settlementBasicPrice) {
		this.settlementBasicPrice = settlementBasicPrice;
	}
	public Double getPrepaidFee() {
		return prepaidFee;
	}
	public void setPrepaidFee(Double prepaidFee) {
		this.prepaidFee = prepaidFee;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getBaseFeePer() {
		return baseFeePer;
	}
	public void setBaseFeePer(Double baseFeePer) {
		this.baseFeePer = baseFeePer;
	}
	public Double getBaseFee() {
		return baseFee;
	}
	public void setBaseFee(Double baseFee) {
		this.baseFee = baseFee;
	}
	public Double getDisplayFeePer() {
		return displayFeePer;
	}
	public void setDisplayFeePer(Double displayFeePer) {
		this.displayFeePer = displayFeePer;
	}
	public Double getDisplayFee() {
		return displayFee;
	}
	public void setDisplayFee(Double displayFee) {
		this.displayFee = displayFee;
	}
	public Double getTotalFeePer() {
		return totalFeePer;
	}
	public void setTotalFeePer(Double totalFeePer) {
		this.totalFeePer = totalFeePer;
	}
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public Double getSalesSettleAmount() {
		return salesSettleAmount;
	}
	public void setSalesSettleAmount(Double salesSettleAmount) {
		this.salesSettleAmount = salesSettleAmount;
	}
	public Double getTotalKakaoDiscount() {
		return totalKakaoDiscount;
	}
	public void setTotalKakaoDiscount(Double totalKakaoDiscount) {
		this.totalKakaoDiscount = totalKakaoDiscount;
	}
	public Double getKakaoDiscount() {
		return kakaoDiscount;
	}
	public void setKakaoDiscount(Double kakaoDiscount) {
		this.kakaoDiscount = kakaoDiscount;
	}
	public Double getKakaoShareDiscount() {
		return kakaoShareDiscount;
	}
	public void setKakaoShareDiscount(Double kakaoShareDiscount) {
		this.kakaoShareDiscount = kakaoShareDiscount;
	}
	public Double getKakaoCoupon() {
		return kakaoCoupon;
	}
	public void setKakaoCoupon(Double kakaoCoupon) {
		this.kakaoCoupon = kakaoCoupon;
	}
	public Double getPointRewardPer() {
		return pointRewardPer;
	}
	public void setPointRewardPer(Double pointRewardPer) {
		this.pointRewardPer = pointRewardPer;
	}
	public Double getPointReward() {
		return pointReward;
	}
	public void setPointReward(Double pointReward) {
		this.pointReward = pointReward;
	}
	public Double getAuthDiscount() {
		return authDiscount;
	}
	public void setAuthDiscount(Double authDiscount) {
		this.authDiscount = authDiscount;
	}
	public Double getSellerCartDiscountCoupon() {
		return sellerCartDiscountCoupon;
	}
	public void setSellerCartDiscountCoupon(Double sellerCartDiscountCoupon) {
		this.sellerCartDiscountCoupon = sellerCartDiscountCoupon;
	}
	public Double getReturnPrepaidFee() {
		return returnPrepaidFee;
	}
	public void setReturnPrepaidFee(Double returnPrepaidFee) {
		this.returnPrepaidFee = returnPrepaidFee;
	}
}
