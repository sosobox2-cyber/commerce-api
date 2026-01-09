package com.cware.partner.common.code;

/**
 * 쿠팡상품상태[O670]
 *APPROVAL_STATUS
 *
 */
public enum ApprovalStatus {
	TARGET("00"), // 등록
	SAVED("05"), // 임시저장
	REQUEST("10"), // 심사중
	WAIT("15"), // 승인대기중
	REJECT("20"), // 승인반려
	PATIALCOMP("25"), // 부분승인완료
	COMPLETE("30") // 승인완료
	;

	private String code;

	private ApprovalStatus(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
