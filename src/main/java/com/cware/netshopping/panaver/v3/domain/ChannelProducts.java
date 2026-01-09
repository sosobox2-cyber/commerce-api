package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ChannelProducts {

	// 원상품번호
	private long originProductNo;	
	// 채널 상품번호
	private long channelProductNo;
	// 채널 서비스 타입
	private String channelServiceType;
	// 카테고리 ID
	private String categoryId;
	// 상품명
	private String name;
	// 판매자 관리 코드
	private String sellerManagementCode;
	// 상품 판매 상태 코드
	private String statusType;
	// 채널 상품 전시 상태
	private String channelProductDisplayStatusType;
	// 판매가
	private double salePrice;
	// 할인가
	private double discountedPrice;
	// 재고 수량
	private double stockQuantity;
	// 네이버쇼핑 등록
	private boolean knowledgeShoppingProductRegistration;
	// 배송 속성
	private String deliveryAttributeType;
	// 기본 배송비
	private double deliveryFee;
	// 반품 배송비
	private double returnFee;
	// 교환 배송비
	private double exchangeFee;
	// 복수 구매 할인
	private double multiPurchaseDiscount;
	// 복수 구매 할인 단위
	private String multiPurchaseDiscountUnitType;
	// 상품 구매 포인트(판매자)
	private long sellerPurchasePoint;
	// 상품 구매 포인트(판매자) 할인 단위
	private String sellerPurchasePointUnitType;
	// 상품 구매 포인트(관리자)
	private long managerPurchasePoint;
	// 텍스트 리뷰 포인트
	private long textReviewPoint;
	// 포토/동영상 리뷰 포인트
	private long photoVideoReviewPoint;
	// 구매평 작성 시 포인트(알림받기)
	private long regularCustomerPoint;
	// 무이자 할부
	private int freeInterest;
	// 사은품
	private String gift;
	// 판매 시작 일시
	private String saleStartDate;
	// 판매 종료 일시
	private String saleEndDate;
	// 상품 등록일
	private String regDate;
	// 판매 상품 수정일
	private String modifiedDate;
	
	
	public long getOriginProductNo() {
		return originProductNo;
	}

	public void setOriginProductNo(long originProductNo) {
		this.originProductNo = originProductNo;
	}

	public long getChannelProductNo() {
		return channelProductNo;
	}

	public void setChannelProductNo(long channelProductNo) {
		this.channelProductNo = channelProductNo;
	}

	public String getChannelServiceType() {
		return channelServiceType;
	}

	public void setChannelServiceType(String channelServiceType) {
		this.channelServiceType = channelServiceType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSellerManagementCode() {
		return sellerManagementCode;
	}

	public void setSellerManagementCode(String sellerManagementCode) {
		this.sellerManagementCode = sellerManagementCode;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public String getChannelProductDisplayStatusType() {
		return channelProductDisplayStatusType;
	}

	public void setChannelProductDisplayStatusType(String channelProductDisplayStatusType) {
		this.channelProductDisplayStatusType = channelProductDisplayStatusType;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public double getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public boolean isKnowledgeShoppingProductRegistration() {
		return knowledgeShoppingProductRegistration;
	}

	public void setKnowledgeShoppingProductRegistration(boolean knowledgeShoppingProductRegistration) {
		this.knowledgeShoppingProductRegistration = knowledgeShoppingProductRegistration;
	}

	public String getDeliveryAttributeType() {
		return deliveryAttributeType;
	}

	public void setDeliveryAttributeType(String deliveryAttributeType) {
		this.deliveryAttributeType = deliveryAttributeType;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public double getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(double returnFee) {
		this.returnFee = returnFee;
	}

	public double getExchangeFee() {
		return exchangeFee;
	}

	public void setExchangeFee(double exchangeFee) {
		this.exchangeFee = exchangeFee;
	}

	public double getMultiPurchaseDiscount() {
		return multiPurchaseDiscount;
	}

	public void setMultiPurchaseDiscount(double multiPurchaseDiscount) {
		this.multiPurchaseDiscount = multiPurchaseDiscount;
	}

	public String getMultiPurchaseDiscountUnitType() {
		return multiPurchaseDiscountUnitType;
	}

	public void setMultiPurchaseDiscountUnitType(String multiPurchaseDiscountUnitType) {
		this.multiPurchaseDiscountUnitType = multiPurchaseDiscountUnitType;
	}

	public long getSellerPurchasePoint() {
		return sellerPurchasePoint;
	}

	public void setSellerPurchasePoint(long sellerPurchasePoint) {
		this.sellerPurchasePoint = sellerPurchasePoint;
	}

	public String getSellerPurchasePointUnitType() {
		return sellerPurchasePointUnitType;
	}

	public void setSellerPurchasePointUnitType(String sellerPurchasePointUnitType) {
		this.sellerPurchasePointUnitType = sellerPurchasePointUnitType;
	}

	public long getManagerPurchasePoint() {
		return managerPurchasePoint;
	}

	public void setManagerPurchasePoint(long managerPurchasePoint) {
		this.managerPurchasePoint = managerPurchasePoint;
	}

	public long getTextReviewPoint() {
		return textReviewPoint;
	}

	public void setTextReviewPoint(long textReviewPoint) {
		this.textReviewPoint = textReviewPoint;
	}

	public long getPhotoVideoReviewPoint() {
		return photoVideoReviewPoint;
	}

	public void setPhotoVideoReviewPoint(long photoVideoReviewPoint) {
		this.photoVideoReviewPoint = photoVideoReviewPoint;
	}

	public long getRegularCustomerPoint() {
		return regularCustomerPoint;
	}

	public void setRegularCustomerPoint(long regularCustomerPoint) {
		this.regularCustomerPoint = regularCustomerPoint;
	}

	public int getFreeInterest() {
		return freeInterest;
	}

	public void setFreeInterest(int freeInterest) {
		this.freeInterest = freeInterest;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public String getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "ChannelProducts [originProductNo=" + originProductNo + ", channelProductNo=" + channelProductNo + ", channelServiceType=" + channelServiceType + ", categoryId=" + categoryId 				
				+ "name=" + name + ", sellerManagementCode=" + sellerManagementCode + ", statusType=" + statusType + ", channelProductDisplayStatusType=" + channelProductDisplayStatusType 				
				+ "salePrice=" + salePrice + ", discountedPrice=" + discountedPrice + ", stockQuantity=" + stockQuantity + ", knowledgeShoppingProductRegistration=" + knowledgeShoppingProductRegistration
				+ "deliveryAttributeType=" + deliveryAttributeType + ", deliveryFee=" + deliveryFee + ", returnFee=" + returnFee + ", exchangeFee=" + exchangeFee 
				+ "multiPurchaseDiscount=" + multiPurchaseDiscount + ", multiPurchaseDiscountUnitType=" + multiPurchaseDiscountUnitType + ", sellerPurchasePoint=" + sellerPurchasePoint
				+ ", sellerPurchasePointUnitType=" + sellerPurchasePointUnitType 	+ "managerPurchasePoint=" + managerPurchasePoint + ", textReviewPoint=" + textReviewPoint + ", photoVideoReviewPoint="
				+ photoVideoReviewPoint + ", regularCustomerPoint=" + regularCustomerPoint 	+ "freeInterest=" + freeInterest + ", gift=" + gift + ", saleStartDate=" + saleStartDate + ", saleEndDate="
				+ saleEndDate + ", regDate=" + regDate + ", modifiedDate=" + modifiedDate + "]";
	}

}
