package com.cware.netshopping.panaver.v3.domain;


/**
 * 클레임 배송비 결제 방법
 *
 */
public enum ClaimDeliveryFeePayMethodType {

	  UNCLAIMED("미청구(반품안심케어 대상)")
	, ADDITIONAL_PAYMENT("지금 결제함-추가결제")
	, REFUND_DEDUCTION("환불금에서 차감")
	, DIRECT_REMIITANCE("판매자에게 직접 송금하거나 상품에 동봉")
	;

	private String codeName;

	private ClaimDeliveryFeePayMethodType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
