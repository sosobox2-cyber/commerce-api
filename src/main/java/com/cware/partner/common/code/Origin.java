package com.cware.partner.common.code;

public enum Origin {
	KOREA("0082", "한국"), ETC("9999", "기타");

	private String code;
	private String codeName;

	private Origin(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	public String code() {
		return code;
	}

	public String codeName() {
		return codeName;
	}
}
