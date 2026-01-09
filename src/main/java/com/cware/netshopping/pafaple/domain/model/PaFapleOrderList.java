package com.cware.netshopping.pafaple.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleOrderList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String orderId; // 주문ID (주문의 키 값인 주문번호)
	private Timestamp orderDate; // 주문일자 (형식:YYYY-MM-DD)
	private Timestamp createDate; // 주문서생성일자 (형식:YYYY-MM-DD)
	private String itemNo; // 품목번호
	private String itemId; // 주문순번 (한 주문에 여러 상품이 있을 경우 해당 순번)
	private String statusCd; // 주문상태 (지불완료)
	private String payMethod; // 결제수단(신용카드/현금/적립금)
	private String shopperName; // 구매자 성명
	private String optId; // 패션플러스 속성상품ID
	private String goodsNo; // 패션플러스 속성상품번호 (실적브랜드ID_제품번호_####)
	private String goodsName; // 상품명
	private String goodsAttr; // 속성
	private String color; // 색상
	private String size; // 사이즈
	private long quantity; // 구매수량
	private double iadjustCurrentprice; // 할인단가
	private double oadjustAdjustedprice; // 판매금액합계
	private double productPurchasePrice; // 공급단가
	private String shopperReceiptName; // 주문자 성명
	private String shopperReceiptStreet; // 주문자 주소
	private String shipToStreet; // 배송지 주소
	private String shipToZip; // 배송지 우편번호
	private String shipToName; // 수령자 성명
	private String shipToPhone; // 수령자 전화
	private String shipToMobile; // 수령자 휴대폰
	private String giftMessage; // 배송메세지
	private String giftName; // 경품명
	private String deliveryDate; // 배송접수일
	private String goalDeliDate; // 도착예상일
	private String etcInfo1; // 개인통관부호
	private String etcInfo2; // 해외배송일 경우 구매자 주민등록번호
	private double couponUseCust; // 쿠폰사용금액
	private Timestamp orderDateDetail; // 주문일시(주문일자의 시분초 단위까지 보여줌)
	private double senderFee; // 배송처별 배송료
	private String uitemId; // 상위품목 ID
	//private double product_list_price; // 소비자단가
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getShopperName() {
		return shopperName;
	}
	public void setShopperName(String shopperName) {
		this.shopperName = shopperName;
	}
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsAttr() {
		return goodsAttr;
	}
	public void setGoodsAttr(String goodsAttr) {
		this.goodsAttr = goodsAttr;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public double getIadjustCurrentprice() {
		return iadjustCurrentprice;
	}
	public void setIadjustCurrentprice(double iadjustCurrentprice) {
		this.iadjustCurrentprice = iadjustCurrentprice;
	}
	public double getOadjustAdjustedprice() {
		return oadjustAdjustedprice;
	}
	public void setOadjustAdjustedprice(double oadjustAdjustedprice) {
		this.oadjustAdjustedprice = oadjustAdjustedprice;
	}
	public double getProductPurchasePrice() {
		return productPurchasePrice;
	}
	public void setProductPurchasePrice(double productPurchasePrice) {
		this.productPurchasePrice = productPurchasePrice;
	}
	public String getShopperReceiptName() {
		return shopperReceiptName;
	}
	public void setShopperReceiptName(String shopperReceiptName) {
		this.shopperReceiptName = shopperReceiptName;
	}
	public String getShopperReceiptStreet() {
		return shopperReceiptStreet;
	}
	public void setShopperReceiptStreet(String shopperReceiptStreet) {
		this.shopperReceiptStreet = shopperReceiptStreet;
	}
	public String getShipToStreet() {
		return shipToStreet;
	}
	public void setShipToStreet(String shipToStreet) {
		this.shipToStreet = shipToStreet;
	}
	public String getShipToZip() {
		return shipToZip;
	}
	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}
	public String getShipToPhone() {
		return shipToPhone;
	}
	public void setShipToPhone(String shipToPhone) {
		this.shipToPhone = shipToPhone;
	}
	public String getShipToMobile() {
		return shipToMobile;
	}
	public void setShipToMobile(String shipToMobile) {
		this.shipToMobile = shipToMobile;
	}
	public String getGiftMessage() {
		return giftMessage;
	}
	public void setGiftMessage(String giftMessage) {
		this.giftMessage = giftMessage;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getGoalDeliDate() {
		return goalDeliDate;
	}
	public void setGoalDeliDate(String goalDeliDate) {
		this.goalDeliDate = goalDeliDate;
	}
	public String getEtcInfo1() {
		return etcInfo1;
	}
	public void setEtcInfo1(String etcInfo1) {
		this.etcInfo1 = etcInfo1;
	}
	public String getEtcInfo2() {
		return etcInfo2;
	}
	public void setEtcInfo2(String etcInfo2) {
		this.etcInfo2 = etcInfo2;
	}
	public double getCouponUseCust() {
		return couponUseCust;
	}
	public void setCouponUseCust(double couponUseCust) {
		this.couponUseCust = couponUseCust;
	}
	public Timestamp getOrderDateDetail() {
		return orderDateDetail;
	}
	public void setOrderDateDetail(Timestamp orderDateDetail) {
		this.orderDateDetail = orderDateDetail;
	}
	public double getSenderFee() {
		return senderFee;
	}
	public void setSenderFee(double senderFee) {
		this.senderFee = senderFee;
	}
	public String getUitemId() {
		return uitemId;
	}
	public void setUitemId(String uitemId) {
		this.uitemId = uitemId;
	}
	
}
