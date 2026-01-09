package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ClaimDeliveryInfo {
		
	// 반품 택배사 우선순위 타입
	private String returnDeliveryCompanyPriorityType;
	// 반품 배송비
	private int returnDeliveryFee;
	// 교환 배송비
	private int exchangeDeliveryFee;
	// 출고지 주소록 번호
	@JsonInclude(Include.NON_DEFAULT)
	private long shippingAddressId;
	// 반품/교환지 주소록 번호
	@JsonInclude(Include.NON_DEFAULT)
	private long returnAddressId;
	// 반품안심케어 설정
	private String freeReturnInsuranceYn;
	
	
	public String getReturnDeliveryCompanyPriorityType() {
		return returnDeliveryCompanyPriorityType;
	}

	public void setReturnDeliveryCompanyPriorityType(String returnDeliveryCompanyPriorityType) {
		this.returnDeliveryCompanyPriorityType = returnDeliveryCompanyPriorityType;
	}

	public int getReturnDeliveryFee() {
		return returnDeliveryFee;
	}

	public void setReturnDeliveryFee(int returnDeliveryFee) {
		this.returnDeliveryFee = returnDeliveryFee;
	}

	public int getExchangeDeliveryFee() {
		return exchangeDeliveryFee;
	}

	public void setExchangeDeliveryFee(int exchangeDeliveryFee) {
		this.exchangeDeliveryFee = exchangeDeliveryFee;
	}

	public long getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(long shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public long getReturnAddressId() {
		return returnAddressId;
	}

	public void setReturnAddressId(long returnAddressId) {
		this.returnAddressId = returnAddressId;
	}

	public String getFreeReturnInsuranceYn() {
		return freeReturnInsuranceYn;
	}

	public void setFreeReturnInsuranceYn(String freeReturnInsuranceYn) {
		this.freeReturnInsuranceYn = freeReturnInsuranceYn;
	}

	@Override
	public String toString() {
		return "ClaimDeliveryInfo [returnDeliveryCompanyPriorityType=" + returnDeliveryCompanyPriorityType +"returnDeliveryFee=" + returnDeliveryFee + "exchangeDeliveryFee=" + exchangeDeliveryFee + "shippingAddressId=" + shippingAddressId
				+ "returnAddressId=" + returnAddressId +"freeReturnInsuranceYn=" + freeReturnInsuranceYn +"]";
	}

}
