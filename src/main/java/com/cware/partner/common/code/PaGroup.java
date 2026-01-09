package com.cware.partner.common.code;

/**
 * 대표제휴사코드 (O500)
 * PA_GROUP_CODE, MEDIA_CODE
 *
 */
public enum PaGroup {

	SK11ST("01", "EX01"), // 11번가
	GMARKET("02", "EX02"), // G마켓
	AUCTION("03", "EX03"), // 옥션
	NAVER("04", "EX04"), // 네이버
	COUPANG("05", "EX05"), // 쿠팡
	WEMP("06", "EX06"), // 위메프
	INTERPARK("07", "EX08"), // 인터파크
	LOTTEON("08", "EX09"), // 롯데온
	TMON("09", "EX10"), // 티몬
	SSG("10", "EX11"), // 쓱닷컴
	KAKAO("11", "EX12"), // 카카오
	HALF("12","EX13"), // 하프클럽
	TDEAL("13","EX15"), // 티딜
	FAPLE("14","EX16"), // 패션플러스
	QEEN("15","EX17") // 퀸잇
	;

	private String code;
	private String mediaCode;

	private PaGroup(String code, String mediaCode) {
		this.code = code;
		this.mediaCode = mediaCode;
	}

	public String code() {
		return code;
	}

	public String mediaCode() {
		return mediaCode;
	}

}
