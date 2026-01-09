package com.cware.partner.common.code;

/**
 * 제휴사코드 (O501)
 * PA_CODE, PA_GROUP_CODE
 *
 */
public enum PaCode {

	SK11ST_TV("11", "01"), // 11번가 방송
	SK11ST_ONLINE("12", "01"), // 11번가 온라인
	EBAY_TV("21", "02"), // 이베이 방송 (그룹코느는 지마켓으로 처리)
	EBAY_ONLINE("22", "02"), // 이베이 온라인
	NAVER("41", "04"), // 네이버
	COUPANG_TV("51", "05"), // 쿠팡 방송
	COUPANG_ONLINE("52", "05"), // 쿠팡 온라인
	WEMP_TV("61", "06"), // 위메프 방송
	WEMP_ONLINE("62", "06"), // 위메프 온라인
	INTERPARK_TV("71", "07"), // 인터파크 방송
	INTERPARK_ONLINE("72", "07"), // 인터파크 온라인
	LOTTEON_TV("81", "08"), // 롯데온 방송
	LOTTEON_ONLINE("82", "08"), // 롯데온 온라인
	TMON_TV("91", "09"), // 티몬 방송
	TMON_ONLINE("92", "09"), // 티몬 온라인
	SSG_TV("A1", "10"), // 쓱닷컴 방송
	SSG_ONLINE("A2", "10"), // 쓱닷컴 온라인
	KAKAO_TV("B1", "11"), // 카카오쇼핑 방송
	KAKAO_ONLINE("B2", "11"), // 카카오쇼핑 온라인
	HALF_TV("C1","12"), //하프클럽 방송
	HALF_ONLINE("C2", "12"), //하프클럽 온라인
	TDEAL_TV("D1", "13"), //티딜 방송
	TDEAL_ONLINE("D2", "13"), //티딜 온라인
	FAPLE_TV("E1", "14"), //패션플러스 방송
	FAPLE_ONLINE("E2", "14"), //패션플러스 온라인
	QEEN_TV("F1", "15"), //퀸잇 방송
	QEEN_ONLINE("F2", "15") //퀸잇 온라인
	;

	private String code;
	private String groupCode;

	private PaCode(String code, String groupCode) {
		this.code = code;
		this.groupCode = groupCode;
	}

	public String code() {
		return code;
	}

	public String groupCode() {
		return groupCode;
	}
}
