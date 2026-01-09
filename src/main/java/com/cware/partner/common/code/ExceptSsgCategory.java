package com.cware.partner.common.code;

/**
 * SSG 입점 제외 카테고리
 * - 생활화확제품 필수 인증 카테고리
 */
public enum ExceptSsgCategory {

	EXCETP1("1000027620"), // 유아용세제/섬유유연제
	EXCETP2("4000002103"), // 방향제
	EXCETP3("4000002098"), // 일반섬유유연제
	EXCETP4("4000002099"), // 프리미엄섬유유연제
	EXCETP5("1000021507"), // 시트/캡슐세제
	EXCETP6("4000002093"), // 일반세탁세제
	EXCETP7("4000002094"), // 드럼세탁세제
	EXCETP8("4000002096"), // 유아용세탁세제
	EXCETP9("1000020805"), // 다용도세제
	EXCETP10("1000027685"), // 유리세정제
	EXCETP11("4000002110"), // 생활세정제
	EXCETP12("4000002105"), // 섬유탈취제
	EXCETP13("4000002106"), // 제습가습제
	EXCETP14("4000002111"), // 표백제
	EXCETP15("4000000132"), // 베이비세제
	;

	private String code;

	private ExceptSsgCategory(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
