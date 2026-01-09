package com.cware.netshopping.panaver.v3.domain;


/**
 * 클레임 상태
 *
 */
public enum ClaimStatusType {

	  CANCEL_REQUEST("취소 요청")
	, CANCELING("취소 처리 중")
	, CANCEL_DONE("취소 처리 완료")
	, CANCEL_REJECT("취소 철회")
	, RETURN_REQUEST("반품 요청")
	, EXCHANGE_REQUEST("교환 요청")
	, COLLECTING("수거 처리 중")
	, COLLECT_DONE("수거 완료")
	, EXCHANGE_REDELIVERING("교환 재배송 중")
	, RETURN_DONE("반품 완료")
	, EXCHANGE_DONE("교환 완료")
	, RETURN_REJECT("반품 철회")
	, EXCHANGE_REJECT("교환 철회")
	, PURCHASE_DECISION_HOLDBACK("구매 확정 보류")
	, PURCHASE_DECISION_REQUEST("구매 확정 요청")
	, PURCHASE_DECISION_HOLDBACK_RELEASE("구매 확정 보류 해제")
	, ADMIN_CANCELING("직권 취소 중")
	, ADMIN_CANCEL_DONE("직권 취소 완료")
	, ADMIN_CANCEL_REJECT("직권 취소 철회")
	;

	private String codeName;

	private ClaimStatusType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
