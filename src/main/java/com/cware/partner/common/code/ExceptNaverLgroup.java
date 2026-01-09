package com.cware.partner.common.code;

/**
 * 네이버입점 제외 분류코드 LGRUOP
 * 제휴연동 대상에서 제외되는 소싱코드
 * 10:패션의류, 15:언더웨어, 20:패션잡화, 65:스포츠/레저, 70:가전/디지털, 75: 문화/서비스, 82:사은품, 85:TV상품 제외
 */
public enum ExceptNaverLgroup {

	FASHION("10"), // 패션의류
	UNDERWARE("15"), // 언더웨어
	ACCESSORIES("20"), // 패션잡화
	SPORTS("65"), // 스포츠/레저
	DIGITAL("70"), // 가전/디지털
	SERVICE("75"), // 문화/서비스
	GIFT("82"), // 사은품
	TV("85"), // TV상품
	;

	private String code;

	private ExceptNaverLgroup(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}


}
