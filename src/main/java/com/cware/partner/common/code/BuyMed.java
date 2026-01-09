package com.cware.partner.common.code;

/**
 * 매입방법[B020]
 * BUY_MED
 *
 */
public enum BuyMed {

	OWN_CENTER("11"), // 직매입센터입고분
	OWN_EXTERNAL("13"), // 직매입타창고분
	SELLER_CENTER("21"), // 위탁센터입고분
	SELLER_EXTERNAL("22") // 위탁업체창고분
	;

	private String code;

	private BuyMed(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
