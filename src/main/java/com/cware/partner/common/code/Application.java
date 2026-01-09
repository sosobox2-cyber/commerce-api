package com.cware.partner.common.code;

/**
 * 데몬처리관련 값
 *
 */
public enum Application {

	ID("ENSYNC") // 처리ID (Enhanced-Sync)
	;

	private String code;

	private Application(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
