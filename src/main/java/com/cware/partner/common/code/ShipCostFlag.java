package com.cware.partner.common.code;

/**
 * 고객부담배송비정책[B604]
 * SHIP_COST_FLAG
 *
 */
public enum ShipCostFlag {

	BASEAMT("CN"), // 조건부
	FREE("FR"), // 무료배송
	GOODS("ID"), // 상품별
	BASEAMT_CODE("PL"), // 개별조건부
	QTY("QN") // 수량단위
	;

	private String code;

	private ShipCostFlag(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
