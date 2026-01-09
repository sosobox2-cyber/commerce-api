package com.cware.netshopping.patmon.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeliveryTemplate {
	String deliveryTemplateName; // 관리 배송템플릿 명

	// 묶음배송가능여부
	// 딜 등록시 사용한 배송템플릿 번호가 같더라도, 묶음배송가능여부가 false 이면, 딜별로 각각 배송비가 발생합니다
	Boolean bundledDeliveryAble;

	// 배송비 정책 타입
	// 무료배송 FREE 반품 배송비
	// 조건부무료배송 CONDITION 반품 배송비, deliveryFeeFreePrice는 무료배송이 되는 구매 금액을 의미
	// 선불 PER 선불배송비, 반품 배송비
	// 착불 AFTER 착불배송비, 반품 배송비
	String deliveryFeePolicy;

	Integer deliveryFee; // 배송비 금액 (원)

	// 조건부 무료배송 기준 금액 (원)
	// 배송비정책이 조건부무료배송(CONDITION)일때 필수
	Integer deliveryFeeFreePrice;

	// 배송 상품타입
	// 딜 등록시 productType과 반드시 일치
	// 배송 상품의 종류 (신선식품, 화물설치, 주문제작 등)
	String productType;

	String deliveryType; // 배송 타입 (당일/익일/예외/종료)

	// 당일발송시간
	// 배송타입이 당일(DD)배송인 경우 필수
	String ddayDeliveryTime;

	// 도서산간지역 배송가능 여부
	// default: false
	Boolean longDistanceDeliveryAvailable;

	// 도서산간지역 배송비 주문시 결제 여부
	// 도서산간 배송가능여부가 true인 경우 필수
	Boolean longDistanceDeliveryPrepay;

	// 도서산간 제주 지역 추가 배송비
	// 도서산간 배송가능여부와 도서산간 배송비 주문시결제여부가 true일 경우 필수
	// 최대 300000
	Integer longDistanceDeliveryFeeJeju;

	// 도서간간 제주 제외한 지역 추가 배송비
	// 도서산간 배송가능여부와 도서산간 배송비 주문시결제여부가 true일 경우 필수
	// 최대 300000
	Integer longDistanceDeliveryFeeExcludingJeju;

	// 도서산간차등 추가 배송비 최소금액
	// 도서산간 배송가능여부가 true이고, 도서산간 배송비 주문시결제여부가 false인경우 필수
	// 최대 300000
	Integer longDistanceDeliveryDiscriptionMin;

	// 도서산간차등 추가 배송비 최대금액
	// 도서산간 배송가능여부가 true이고, 도서산간 배송비 주문시결제여부가 false인경우 필수
	// 최대 300000
	Integer longDistanceDeliveryDiscriptionMax;

	// 파트너 배송지 번호
	String partnerDeliveryAddressNo;

	// 파트너 반송지 번호
	String partnerReturnAddressNo;

	// 티몬지정 반품택배
	// default: false
	Boolean tmonReturnCargoUsing;

	public String getDeliveryTemplateName() {
		return deliveryTemplateName;
	}

	public void setDeliveryTemplateName(String deliveryTemplateName) {
		this.deliveryTemplateName = deliveryTemplateName;
	}

	public Boolean getBundledDeliveryAble() {
		return bundledDeliveryAble;
	}

	public void setBundledDeliveryAble(Boolean bundledDeliveryAble) {
		this.bundledDeliveryAble = bundledDeliveryAble;
	}

	public String getDeliveryFeePolicy() {
		return deliveryFeePolicy;
	}

	public void setDeliveryFeePolicy(String deliveryFeePolicy) {
		this.deliveryFeePolicy = deliveryFeePolicy;
	}

	public Integer getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Integer deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Integer getDeliveryFeeFreePrice() {
		return deliveryFeeFreePrice;
	}

	public void setDeliveryFeeFreePrice(Integer deliveryFeeFreePrice) {
		this.deliveryFeeFreePrice = deliveryFeeFreePrice;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDdayDeliveryTime() {
		return ddayDeliveryTime;
	}

	public void setDdayDeliveryTime(String ddayDeliveryTime) {
		this.ddayDeliveryTime = ddayDeliveryTime;
	}

	public Boolean getLongDistanceDeliveryAvailable() {
		return longDistanceDeliveryAvailable;
	}

	public void setLongDistanceDeliveryAvailable(Boolean longDistanceDeliveryAvailable) {
		this.longDistanceDeliveryAvailable = longDistanceDeliveryAvailable;
	}

	public Boolean getLongDistanceDeliveryPrepay() {
		return longDistanceDeliveryPrepay;
	}

	public void setLongDistanceDeliveryPrepay(Boolean longDistanceDeliveryPrepay) {
		this.longDistanceDeliveryPrepay = longDistanceDeliveryPrepay;
	}

	public Integer getLongDistanceDeliveryFeeJeju() {
		return longDistanceDeliveryFeeJeju;
	}

	public void setLongDistanceDeliveryFeeJeju(Integer longDistanceDeliveryFeeJeju) {
		this.longDistanceDeliveryFeeJeju = longDistanceDeliveryFeeJeju;
	}

	public Integer getLongDistanceDeliveryFeeExcludingJeju() {
		return longDistanceDeliveryFeeExcludingJeju;
	}

	public void setLongDistanceDeliveryFeeExcludingJeju(Integer longDistanceDeliveryFeeExcludingJeju) {
		this.longDistanceDeliveryFeeExcludingJeju = longDistanceDeliveryFeeExcludingJeju;
	}

	public Integer getLongDistanceDeliveryDiscriptionMin() {
		return longDistanceDeliveryDiscriptionMin;
	}

	public void setLongDistanceDeliveryDiscriptionMin(Integer longDistanceDeliveryDiscriptionMin) {
		this.longDistanceDeliveryDiscriptionMin = longDistanceDeliveryDiscriptionMin;
	}

	public Integer getLongDistanceDeliveryDiscriptionMax() {
		return longDistanceDeliveryDiscriptionMax;
	}

	public void setLongDistanceDeliveryDiscriptionMax(Integer longDistanceDeliveryDiscriptionMax) {
		this.longDistanceDeliveryDiscriptionMax = longDistanceDeliveryDiscriptionMax;
	}

	public String getPartnerDeliveryAddressNo() {
		return partnerDeliveryAddressNo;
	}

	public void setPartnerDeliveryAddressNo(String partnerDeliveryAddressNo) {
		this.partnerDeliveryAddressNo = partnerDeliveryAddressNo;
	}

	public String getPartnerReturnAddressNo() {
		return partnerReturnAddressNo;
	}

	public void setPartnerReturnAddressNo(String partnerReturnAddressNo) {
		this.partnerReturnAddressNo = partnerReturnAddressNo;
	}

	public Boolean getTmonReturnCargoUsing() {
		return tmonReturnCargoUsing;
	}

	public void setTmonReturnCargoUsing(Boolean tmonReturnCargoUsing) {
		this.tmonReturnCargoUsing = tmonReturnCargoUsing;
	}

	@Override
	public String toString() {
		return "DeliveryTemplate [deliveryTemplateName=" + deliveryTemplateName + ", bundledDeliveryAble="
				+ bundledDeliveryAble + ", deliveryFeePolicy=" + deliveryFeePolicy + ", deliveryFee=" + deliveryFee
				+ ", deliveryFeeFreePrice=" + deliveryFeeFreePrice + ", productType=" + productType + ", deliveryType="
				+ deliveryType + ", ddayDeliveryTime=" + ddayDeliveryTime + ", longDistanceDeliveryAvailable="
				+ longDistanceDeliveryAvailable + ", longDistanceDeliveryPrepay=" + longDistanceDeliveryPrepay
				+ ", longDistanceDeliveryFeeJeju=" + longDistanceDeliveryFeeJeju
				+ ", longDistanceDeliveryFeeExcludingJeju=" + longDistanceDeliveryFeeExcludingJeju
				+ ", longDistanceDeliveryDiscriptionMin=" + longDistanceDeliveryDiscriptionMin
				+ ", longDistanceDeliveryDiscriptionMax=" + longDistanceDeliveryDiscriptionMax
				+ ", partnerDeliveryAddressNo=" + partnerDeliveryAddressNo + ", partnerReturnAddressNo="
				+ partnerReturnAddressNo + ", tmonReturnCargoUsing=" + tmonReturnCargoUsing + "]";
	}

}
