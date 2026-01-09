package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ShippingPolicy {

	// 묶음배송비 정책 번호
	// 수정[PUT] / 조회[GET]일 경우만 필수
	// 등록시 response로 자동채번된 번호
	private String policyNo;

	// 묶음배송비 정책 구분
	// 1 : 무료
	// 2 : 유료
	// 3 : 조건부
	private int feeType;

	// 배송비금액
	private int fee;

	// 배송비 선결제여부
	// 고객에게 노출되는 정보로 배송비를 선결제로 받을 것인지 여부
	// true : 선결제
	// false : 선결제아님
	private boolean isPrepayment;

	// 착불여부
	// 고객에게 노출되는 정보로 배송비를 착불로 받을 것인지 여부
	// true : 착불
	// false : 착불아님
	private boolean isCashOnDelivery;

	// 출하지번호
	// 출하지가 같은 상품끼리 묶음배송비 설정됨
	// 출하지가 같은 상품 중, 묶음배송비의 가장 작은 배송비 기준으로 부과됨
	private String placeNo;

	// 기본배송비여부
	// true : 기본배송비 정책
	// false: 기본배송비 정책 아님
	private boolean isDefault;

	// 조건부 기준금액
	private List<ShippingFee> shippingFee;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public int getFeeType() {
		return feeType;
	}

	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	@JsonProperty("isPrepayment")
	public boolean isPrepayment() {
		return isPrepayment;
	}

	public void setPrepayment(boolean isPrepayment) {
		this.isPrepayment = isPrepayment;
	}

	@JsonProperty("isCashOnDelivery")
	public boolean isCashOnDelivery() {
		return isCashOnDelivery;
	}

	public void setCashOnDelivery(boolean isCashOnDelivery) {
		this.isCashOnDelivery = isCashOnDelivery;
	}

	public String getPlaceNo() {
		return placeNo;
	}

	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}

	@JsonProperty("isDefault")
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public List<ShippingFee> getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(List<ShippingFee> shippingFee) {
		this.shippingFee = shippingFee;
	}

	@Override
	public String toString() {
		return "ShippingPolicy [policyNo=" + policyNo + ", feeType=" + feeType + ", fee=" + fee + ", isPrepayment="
				+ isPrepayment + ", isCashOnDelivery=" + isCashOnDelivery + ", placeNo=" + placeNo + ", isDefault="
				+ isDefault + ", shippingFee=" + shippingFee + "]";
	}

}
