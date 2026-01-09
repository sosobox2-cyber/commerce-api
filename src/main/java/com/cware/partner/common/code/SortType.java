package com.cware.partner.common.code;

/**
 * 정렬순서
 * SORT_TYPE
 *
 */
public enum SortType {

	REGISTERED("00"), // 등록순
	ALPHABETICAL ("05"), // 가다다순
	LOWPRICE("11") // 낮은가격순
	;

	private String code;

	private SortType(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
