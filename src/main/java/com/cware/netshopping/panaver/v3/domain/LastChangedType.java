package com.cware.netshopping.panaver.v3.domain;


/**
 * 최종 변경 구분
 *
 */
public enum LastChangedType {

	  PAY_WAITING("결제 대기")
	, PAYED("결제 완료")
	, EXCHANGE_OPTION("옵션 변경") // 선물하기
	, DELIVERY_ADDRESS_CHANGED("배송지 변경")
	, GIFT_RECEIVED("선물 수락") // 선물하기
	, CLAIM_REJECTED("클레임 철회")
	, DISPATCHED("발송 처리")
	, CLAIM_REQUESTED("클레임 요청")
	, COLLECT_DONE("수거 완료")
	, CLAIM_HOLDBACK_RELEASED("클레임 보류 해제")
	, CLAIM_COMPLETED("클레임 완료")
	, PURCHASE_DECIDED("구매 확정")
	, HOPE_DELIVERY_INFO_CHANGED("배송 희망일 변경")
	, CLAIM_REDELIVERING("교환 재배송 처리")
	;

	private String codeName;

	private LastChangedType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
