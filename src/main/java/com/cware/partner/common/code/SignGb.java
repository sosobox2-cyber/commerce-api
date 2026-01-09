package com.cware.partner.common.code;

/**
 * 결제구분 (B024)
 * SIGN_GB
 *
 */
public enum SignGb {

	REGISTER("00"), // 등록
	REQUEST("20"), // 상신
	CANCEL("21"), // 상신취소
	APPROVAL("80"), // 팀장승인
	REJECT("81") // 팀장반려
	;

	private String code;

	private SignGb(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
