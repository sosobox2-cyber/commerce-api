package com.cware.partner.common.code;

/**
 * 네이버입점 제외 정보고시 타입코드 offertype
 * 제휴연동 대상에서 제외되는 정보고시 타입코드
 * 39:생활화학제품, 40:살생물제품, 41:주류 제외
 */
public enum ExceptNaverOfferType {

	BIOCHEMISTRY("39"), // 생활화학제품
	BIOCIDAL("40"), // 살생물제품
	ALCOHOLIC("41"), // 주류
	;

	private String code;

	private ExceptNaverOfferType(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}


}
