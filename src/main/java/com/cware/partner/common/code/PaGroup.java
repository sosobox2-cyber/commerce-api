package com.cware.partner.common.code;

/**
 * 대표제휴사코드 (O500)
 * PA_GROUP_CODE, MEDIA_CODE, CODE_LGROUP(커미션정보가 저장된 코드키)
 *
 */
public enum PaGroup {

	SK11ST("01", "EX01", "O531"), // 11번가
	GMARKET("02", "EX02", "O579"), // G마켓
	AUCTION("03", "EX03", "O579"), // 옥션
	NAVER("04", "EX04", "O640"), // 네이버
	COUPANG("05", "EX05", "O669"), // 쿠팡
	WEMP("06", "EX06", "O680"), // 위메프
	INTERPARK("07", "EX08", "O696"), // 인터파크
	LOTTEON("08", "EX09", "O697"), // 롯데온
	TMON("09", "EX10", "O698"), // 티몬
	SSG("10", "EX11", "O699"), // 쓱닷컴
	KAKAO("11", "EX12", "O699" ), // 카카오쇼핑
	HALF("12", "EX13", "O700" ), //하프클럽
	TDEAL("13", "EX15", "O641" ) //티딜
	;

	private String code;
	private String mediaCode;
	private String commissionGroup;

	private PaGroup(String code, String mediaCode, String commissionGroup) {
		this.code = code;
		this.mediaCode = mediaCode;
		this.commissionGroup = commissionGroup;
	}

	public String code() {
		return code;
	}

	public String mediaCode() {
		return mediaCode;
	}

	public String commissionGroup() {
		return commissionGroup;
	}

}
