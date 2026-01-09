package com.cware.netshopping.common.code;

/**
 * 대표제휴사코드 (O500)
 * PA_GROUP_CODE, 프로세스ID
 *
 */
public enum PaGroup {

	SK11ST("01", "EN11STAPI"), // 11번가
	GMARKET("02", "ENEBAYAPI"), // G마켓
	AUCTION("03", "ENEBAYAPI"), // 옥션
	NAVER("04", "ENNAVERAPI"), // 네이버
	CMNAVER("04", "CMNAVERAPI"), // 네이버
	COUPANG("05", "ENCOPNAPI"), // 쿠팡
	WEMP("06", "ENWEMPAPI"), // 위메프
	INTERPARK("07", "ENINTPAPI"), // 인터파크
	LOTTEON("08", "ENLTONAPI"), // 롯데온
	TMON("09", "ENTMONAPI"), // 티몬
	SSG("10", "ENSSGAPI"), // 쓱닷컴
	KAKAO("11", "ENKAKAOAPI"),  // 카카오
	HALF("12", "ENHALFAPI"),  // 하프클럽
	TDEAL("13", "ENTDEALAPI"),  // 티딜
	FAPLE("14", "ENFAPLEAPI"),  // 패션플러스 
	QEEN("15","ENQEENAPI")//퀸잇
	;

	private String code;
	private String processId;

	private PaGroup(String code, String processId) {
		this.code = code;
		this.processId = processId;
	}

	public String code() {
		return code;
	}

	public String processId() {
		return processId;
	}

}
