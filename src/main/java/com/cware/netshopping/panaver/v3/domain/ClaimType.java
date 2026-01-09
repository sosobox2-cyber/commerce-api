package com.cware.netshopping.panaver.v3.domain;


/**
 * 클레임 구분
 *
 */
public enum ClaimType {

	  CANCEL("취소")
	, RETURN("반품")
	, EXCHANGE("교환")
	, PURCHASE_DECISION_HOLDBACK("구매 확정 보류")
	, ADMIN_CANCEL("직권 취소")
	;

	private String codeName;

	private ClaimType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
