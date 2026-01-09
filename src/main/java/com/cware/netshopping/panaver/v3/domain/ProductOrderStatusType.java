package com.cware.netshopping.panaver.v3.domain;


/**
 * 상품 주문 상태
 *
 */
public enum ProductOrderStatusType {

	  PAYMENT_WAITING("결제 대기")
	, PAYED("결제 완료")
	, DELIVERING("배송 중")
	, DELIVERED("배송 완료")
	, PURCHASE_DECIDED("구매 확정")
	, EXCHANGED("교환")
	, CANCELED("취소")
	, RETURNED("반품")
	, CANCELED_BY_NOPAYMENT("미결제 취소")
	;

	private String codeName;

	private ProductOrderStatusType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
