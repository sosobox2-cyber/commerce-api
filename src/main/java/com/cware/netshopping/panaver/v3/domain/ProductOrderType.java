package com.cware.netshopping.panaver.v3.domain;


/**
 * 정산 대상 구분(상품 주문, 배송비, 기타 비용)
 *
 */
public enum ProductOrderType {

	  PROD_ORDER("상품 주문")
	, DELIVERY("배송비")
	, EXTRAFEE("기타 비용")
	, WITHDRAW("결제 수단 출금")
	, REFUND("구매자 환불")
	, PL_REFUND("후불 결제 환불")
	, DEDUCTION_RESTORE("기타 공제 환급")
	, PROD_PAY("상품 결제")
	, PURCHASE_REVIEW("텍스트 리뷰")
	, PREMIUM_PURCHASE_REVIEW("포토/동영상 리뷰")
	, REGULAR_PURCHASE_REVIEW("알림받기 리뷰 추가 적립")
	, ONE_MONTH_PURCHASE_REVIEW("한달사용 텍스트 리뷰")
	, ONE_MONTH_PREMIUM_PURCHASE_REVIEW("한달사용 포토/동영상 리뷰")
	, REVIEW("리뷰 적립")
	, ETC_COUPON("기타 할인")
	, QUICK_SETTLE("빠른정산")
	, QUANTITY_CANCEL("수량 취소")
	, DIFFERENCE_SETTLE("차액 정산")
	, DEPOSIT_SETTLE("보증금")
	, RENTAL_ORDER("렌탈 주문")
	, MANUAL_ORDER("수기 주문")
	, RENTAL_SCHEDULED_ORDER("월 렌탈료 주문")
	, PREFERENTIAL_COMMISSION("우대 수수료 환급")
	;

	private String codeName;

	private ProductOrderType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
