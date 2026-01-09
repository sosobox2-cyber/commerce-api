package com.cware.partner.common.code;

/**
 * 입점상태[O502]
 * PA_STATUS
 *
 */
public enum PaStatus {

	TARGET("00"), // 입점대상
	REQUEST("10"), // 입점요청
	REJECT("20"), // 입점반려
	COMPLETE("30"), // 입점완료
	DELETING("40"), // 삭제대기
	DELETED("50"), // 삭제완료
	SALESTOP("90") // 판매불가
	;

	private String code;

	private PaStatus(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
