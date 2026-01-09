package com.cware.partner.common.code;

/**
 * 데몬처리관련 값
 *
 */
public enum Application {

	ID("ENCOPN") // 처리ID
	;

	private String code;

	private Application(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
