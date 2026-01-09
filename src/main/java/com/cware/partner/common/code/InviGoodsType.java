package com.cware.partner.common.code;

/**
 * 무형상품구분 (B701)
 * INVI_GOODS_TYPE
 *
 */
public enum InviGoodsType {

	NORMAL("00"), // 일반상품
	COUNSEL("10"), // 상담상품
	;

	private String code;

	private InviGoodsType(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
