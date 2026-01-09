package com.cware.netshopping.panaver.v3.domain;


/**
 * 클레임 요청 사유
 *
 */
public enum ClaimRequestReasonType {

	  INTENT_CHANGED("구매 의사 취소")
	, COLOR_AND_SIZE("색상 및 사이즈 변경")
	, WRONG_ORDER("	다른 상품 잘못 주문")
	, PRODUCT_UNSATISFIED("서비스 불만족")
	, DELAYED_DELIVERY("배송 지연")
	, SOLD_OUT("상품 품절")
	, INCORRECT_INFO("상품 정보 상이")
	;

	private String codeName;

	private ClaimRequestReasonType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
