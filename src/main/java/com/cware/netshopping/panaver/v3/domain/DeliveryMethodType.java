package com.cware.netshopping.panaver.v3.domain;


/**
 * 배송 방법 코드
 *
 */
public enum DeliveryMethodType {

	  DELIVERY("택배, 등기, 소포")
	, GDFW_ISSUE_SVC("굿스플로 송장 출력")
	, VISIT_RECEIPT("방문 수령")
	, DIRECT_DELIVERY("직접 전달")
	, QUICK_SVC("퀵서비스")
	, NOTHING("배송 없음")
	, RETURN_DESIGNATED("지정 반품 택배")
	, RETURN_DELIVERY("일반 반품 택배")
	, RETURN_INDIVIDUAL("직접 반송")
	, RETURN_MERCHANT("판매자 직접 수거")
	, UNKNOWN("알 수 없음(예외 처리에 사용)")
	;

	private String codeName;

	private DeliveryMethodType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
