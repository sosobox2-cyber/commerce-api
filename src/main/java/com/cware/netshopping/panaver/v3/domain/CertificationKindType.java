package com.cware.netshopping.panaver.v3.domain;


/**
 * 인증 정보 종류 코드(구 SOAP과 신규 커머스 API와 코드 변동에 따른 적용)
 *
 */
public enum CertificationKindType {

	  KC ("KC_CERTIFICATION") //KC 인증
	, KC_CERTIFICATION ("KC_CERTIFICATION") //KC 인증
	, CHI ("CHILD_CERTIFICATION")//어린이제품 인증
	, CHILD_CERTIFICATION("CHILD_CERTIFICATION")//어린이제품 인증
	, GRN ("GREEN_PRODUCTS")//친환경 인증
	, GREEN_PRODUCTS ("GREEN_PRODUCTS")//친환경 인증
	, OVERSEAS ("OVERSEAS") //구매대행
	, PARALLEL_IMPORT ("PARALLEL_IMPORT") //병행수입
	, ETC ("ETC") //기타 인증
	;

	private String codeName;

	private CertificationKindType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
