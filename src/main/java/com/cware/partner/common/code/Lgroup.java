package com.cware.partner.common.code;

public enum Lgroup {

	FASHION("10"), // 패션의류
	UNDERWARE("15"), // 언더웨어
	ACCESSORIES("20"), // 패션잡화
	JEWERLY("25"), // 시계/쥬얼리
	BEAUTY("30"), // 뷰티
	FUNITURE("35"), // 가구/침구
	INTERIOR("40"), // 침구/인테리어
	KITCHEN("45"), // 주방용품
	HOUSEHOLD("50"), // 생활용품
	CHILD("55"), // 출산/유아동
	FOOD("60"), // 식품
	SPORTS("65"), // 스포츠/레저
	DIGITAL("70"), // 가전/디지털
	SERVICE("75"), // 문화/서비스
	GIFT("82"), // 사은품
	TV("85"), // TV상품
	ANIMAL("86"), // 반려동물
	STAFF("90") // 구성원
	;

	private String code;

	private Lgroup(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
