package com.cware.netshopping.panaver.v3.domain;


/**
 * 네이버 예외 카테고리 코드
 *
 */
public enum ExceptionalCategoryType {

	  E_COUPON("E쿠폰")
	, ADULT("성인")
	, MARINE_PRODUCTS("수산물")
	, REVIEW_UNEXPOSE("구매평 미노출")
	, CHILD_CERTIFICATION("어린이제품 인증 대상")
	, ORIGINAREA_PRODUCTS("원산지 입력 대상")
	, GREEN_PRODUCTS("친환경 인증 대상")
	, KC_CERTIFICATION("KC 인증 대상")
	, SAFE_CRITERION("안전기준준수대상")
	, AFFILIATE("어필리에이트")
	, TRADITIONAL_ALCOHOL("전통주")
	, OPTION_PRICE("옵션가 제한 예외 카테고리")
	, BOOK("도서_일반")
	, BOOK_EBOOK("도서_E북")
	, BOOK_AUDIO("도서_오디오북")
	, BOOK_MAGAZINE("도서_잡지")
	, BOOK_USED("도서_중고")
	, BOOK_OVERSEAS("도서_해외")
	, BOOK_FREE("도서_정가제free")
	, PERFORMANCE("문화비_소득공제")
	, FREE_RETURN_INSURANCE("반품안심케어")
	, REGULAR_SUBSCRIPTION("정기구독")
	, RENTAL_SUBSCRIPTION("렌탈")
	;

	private String codeName;

	private ExceptionalCategoryType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
