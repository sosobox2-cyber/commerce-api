package com.cware.netshopping.panaver.v3.domain;


/**
 * 정산 상태 구분(정산, 정산 전 취소, 정산 후 취소)
 *
 */
public enum SettleType {

	  NORMAL_SETTLE_ORIGINAL("일반정산")
	, NORMAL_SETTLE_AFTER_CANCEL("정산 후 취소")
	, NORMAL_SETTLE_BEFORE_CANCEL("정산 전 취소")
	, QUICK_SETTLE_ORIGINAL("빠른정산")
	, QUICK_SETTLE_CANCEL("빠른정산 회수")
	, QUANTITY_CANCEL_DEDUCTION("수량 취소 정산(공제)")
	, QUANTITY_CANCEL_RESTORE("수량 취소 정산(환급)")
	, PURCHASE_CONFIRM("구매 확정")
	;

	private String codeName;

	private SettleType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
