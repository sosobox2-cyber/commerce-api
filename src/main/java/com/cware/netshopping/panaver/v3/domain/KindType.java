package com.cware.netshopping.panaver.v3.domain;


/**
 * 네이버 인증 종류 코드
 *
 */
public enum KindType {

	  KC_CERTIFICATION("KC 인증 대상")
	, CHILD_CERTIFICATION("어린이제품 인증 대상")
	, GREEN_PRODUCTS("친환경 인증 대상")
	, PARALLEL_IMPORT("병행수입")
	, OVERSEAS("구매대행")
	, ETC("기타 인증 유형")
	;

	private String codeName;

	private KindType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
