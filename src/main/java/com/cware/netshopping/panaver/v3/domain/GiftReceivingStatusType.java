package com.cware.netshopping.panaver.v3.domain;


/**
 * 선물 수락 상태 구분
 *
 */
public enum GiftReceivingStatusType {

	  WAIT_FOR_RECEIVING("수락 대기(배송지 입력 대기)")
	, RECEIVED("수락 완료")
	;

	private String codeName;

	private GiftReceivingStatusType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
