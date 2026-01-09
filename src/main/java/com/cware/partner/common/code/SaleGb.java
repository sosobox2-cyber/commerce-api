package com.cware.partner.common.code;

/**
 * 판매구분[B032]
 * SALE_GB
 *
 */
public enum SaleGb {

	FORSALE("00"), // 진행
	SOLDOUT("05"), // 품절중단
	SUSPEND("11"), // 판매중단
	EOS("19") // 판매종료
	;

	private String code;

	private SaleGb(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
