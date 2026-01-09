package com.cware.partner.common.code;

/**
 * MD분류[TMDKIND]
 * MD_KIND
 *
 */
public enum MdKInd {

	MO_ETV("0032") // 모바일eTV
	;

	private String code;

	private MdKInd(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
