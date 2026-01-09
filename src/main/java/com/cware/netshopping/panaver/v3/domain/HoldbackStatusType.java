package com.cware.netshopping.panaver.v3.domain;


/**
 * 보류 상태
 *
 */
public enum HoldbackStatusType {

	  HOLDBACK("보류 중")
	, RELEASED("보류 해제")
	;

	private String codeName;

	private HoldbackStatusType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
