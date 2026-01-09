package com.cware.partner.common.code;

/**
 * 코드분류
 * CODE_LGROUP
 *
 */
public enum CodeGroup {

	ORIGIN("B023"), // 원산지코드
	STOCK_RATE("O505"), // 제휴사별 주문가능 재고비율
	SALE_COND("O582"), // 제휴사별 판매조건
	APPROVAL_STATUS("O670") // 쿠팡상품승인상태
	;

	private String code;

	private CodeGroup(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
