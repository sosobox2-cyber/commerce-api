package com.cware.netshopping.pakakao.v2.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Delivery {

	// 필수 배송방법
	// DELIVERY 택배, 소포, 등기
	// DIRECT 직접배송(화물배달)
	// NONE 이용권/e쿠폰
	String deliveryMethodType;

	// 묶음가능 여부
	boolean bundleGroupAvailable;

	// 배송비 유형
	// FREE 무료
	// PAID 유료
	// CONDITIONAL_FREE 조건부 무료
	// QUANTITY_PAID 수량별 부과
	String deliveryFeeType;

	// 기본 배송비
	BigDecimal baseFee;

	// 무료조건 금액
	BigDecimal freeConditionalAmount;

	// 반복부과수량
	Integer repeatQuantity;

	// 배송비 결제방법
	// COLLECT 착불
	// PREPAID 선결제
	// COLLECT_OR_PREPAID 착불 또는 선결제
	String deliveryFeePaymentType;

	// 반품배송비
	BigDecimal returnDeliveryFee;

	// 교환배송비
	BigDecimal exchangeDeliveryFee;

	// 출고지 주소번호
	String shippingAddressId;

	// 반품교환지 주소번호
	String returnAddressId;

	// AS 연락처
	String asPhoneNumber;

	// AS 안내
	String asGuideWords;

	// 방문수령 사용여부
	boolean usePickUpDelivery;

	// 방문수령지 주소번호
	Long pickUpAddressId;

	// 퀵서비스 사용여부
	boolean useQuickDelivery;

	// 퀵서비스 안내문구
	String quickGuideWords;

	// 취소안내
	String cancelGuidance;

	// 도서산간 배송 가능 여부
	boolean availableIsolatedArea;

	// 도서산간 추가 배송비 지정 여부
	boolean useIsolatedAreaFee;

	// 제주지역 추가 배송비
	BigDecimal jejuAreaAdditionalFee;

	// 도서산간 추가 배송비
	BigDecimal isolatedAreaAdditionalFee;

	public String getDeliveryMethodType() {
		return deliveryMethodType;
	}

	public void setDeliveryMethodType(String deliveryMethodType) {
		this.deliveryMethodType = deliveryMethodType;
	}

	public boolean isBundleGroupAvailable() {
		return bundleGroupAvailable;
	}

	public void setBundleGroupAvailable(boolean bundleGroupAvailable) {
		this.bundleGroupAvailable = bundleGroupAvailable;
	}

	public String getDeliveryFeeType() {
		return deliveryFeeType;
	}

	public void setDeliveryFeeType(String deliveryFeeType) {
		this.deliveryFeeType = deliveryFeeType;
	}

	public BigDecimal getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(BigDecimal baseFee) {
		this.baseFee = baseFee;
	}

	public BigDecimal getFreeConditionalAmount() {
		return freeConditionalAmount;
	}

	public void setFreeConditionalAmount(BigDecimal freeConditionalAmount) {
		this.freeConditionalAmount = freeConditionalAmount;
	}

	public Integer getRepeatQuantity() {
		return repeatQuantity;
	}

	public void setRepeatQuantity(Integer repeatQuantity) {
		this.repeatQuantity = repeatQuantity;
	}

	public String getDeliveryFeePaymentType() {
		return deliveryFeePaymentType;
	}

	public void setDeliveryFeePaymentType(String deliveryFeePaymentType) {
		this.deliveryFeePaymentType = deliveryFeePaymentType;
	}

	public BigDecimal getReturnDeliveryFee() {
		return returnDeliveryFee;
	}

	public void setReturnDeliveryFee(BigDecimal returnDeliveryFee) {
		this.returnDeliveryFee = returnDeliveryFee;
	}

	public BigDecimal getExchangeDeliveryFee() {
		return exchangeDeliveryFee;
	}

	public void setExchangeDeliveryFee(BigDecimal exchangeDeliveryFee) {
		this.exchangeDeliveryFee = exchangeDeliveryFee;
	}

	public String getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(String shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public String getReturnAddressId() {
		return returnAddressId;
	}

	public void setReturnAddressId(String returnAddressId) {
		this.returnAddressId = returnAddressId;
	}

	public String getAsPhoneNumber() {
		return asPhoneNumber;
	}

	public void setAsPhoneNumber(String asPhoneNumber) {
		this.asPhoneNumber = asPhoneNumber;
	}

	public String getAsGuideWords() {
		return asGuideWords;
	}

	public void setAsGuideWords(String asGuideWords) {
		this.asGuideWords = asGuideWords;
	}

	public boolean isUsePickUpDelivery() {
		return usePickUpDelivery;
	}

	public void setUsePickUpDelivery(boolean usePickUpDelivery) {
		this.usePickUpDelivery = usePickUpDelivery;
	}

	public Long getPickUpAddressId() {
		return pickUpAddressId;
	}

	public void setPickUpAddressId(Long pickUpAddressId) {
		this.pickUpAddressId = pickUpAddressId;
	}

	public boolean isUseQuickDelivery() {
		return useQuickDelivery;
	}

	public void setUseQuickDelivery(boolean useQuickDelivery) {
		this.useQuickDelivery = useQuickDelivery;
	}

	public String getQuickGuideWords() {
		return quickGuideWords;
	}

	public void setQuickGuideWords(String quickGuideWords) {
		this.quickGuideWords = quickGuideWords;
	}

	public String getCancelGuidance() {
		return cancelGuidance;
	}

	public void setCancelGuidance(String cancelGuidance) {
		this.cancelGuidance = cancelGuidance;
	}

	public boolean isAvailableIsolatedArea() {
		return availableIsolatedArea;
	}

	public void setAvailableIsolatedArea(boolean availableIsolatedArea) {
		this.availableIsolatedArea = availableIsolatedArea;
	}

	public boolean isUseIsolatedAreaFee() {
		return useIsolatedAreaFee;
	}

	public void setUseIsolatedAreaFee(boolean useIsolatedAreaFee) {
		this.useIsolatedAreaFee = useIsolatedAreaFee;
	}

	public BigDecimal getJejuAreaAdditionalFee() {
		return jejuAreaAdditionalFee;
	}

	public void setJejuAreaAdditionalFee(BigDecimal jejuAreaAdditionalFee) {
		this.jejuAreaAdditionalFee = jejuAreaAdditionalFee;
	}

	public BigDecimal getIsolatedAreaAdditionalFee() {
		return isolatedAreaAdditionalFee;
	}

	public void setIsolatedAreaAdditionalFee(BigDecimal isolatedAreaAdditionalFee) {
		this.isolatedAreaAdditionalFee = isolatedAreaAdditionalFee;
	}

	@Override
	public String toString() {
		return "Delivery [deliveryMethodType=" + deliveryMethodType + ", bundleGroupAvailable=" + bundleGroupAvailable
				+ ", deliveryFeeType=" + deliveryFeeType + ", baseFee=" + baseFee + ", freeConditionalAmount="
				+ freeConditionalAmount + ", repeatQuantity=" + repeatQuantity + ", deliveryFeePaymentType="
				+ deliveryFeePaymentType + ", returnDeliveryFee=" + returnDeliveryFee + ", exchangeDeliveryFee="
				+ exchangeDeliveryFee + ", shippingAddressId=" + shippingAddressId + ", returnAddressId="
				+ returnAddressId + ", asPhoneNumber=" + asPhoneNumber + ", asGuideWords=" + asGuideWords
				+ ", usePickUpDelivery=" + usePickUpDelivery + ", pickUpAddressId=" + pickUpAddressId
				+ ", useQuickDelivery=" + useQuickDelivery + ", quickGuideWords=" + quickGuideWords
				+ ", cancelGuidance=" + cancelGuidance + ", availableIsolatedArea=" + availableIsolatedArea
				+ ", useIsolatedAreaFee=" + useIsolatedAreaFee + ", jejuAreaAdditionalFee=" + jejuAreaAdditionalFee
				+ ", isolatedAreaAdditionalFee=" + isolatedAreaAdditionalFee + "]";
	}

}
