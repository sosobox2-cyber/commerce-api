package com.cware.partner.common.code;

/**
 * 데이터변경사유 (TPACDCREASON)
 * 변경사유코드(CDC_REASON_CODE)
 */
public enum CdcReason {

	SALE_START("111"), // 상품입점
	MANUAL_START("115"), // 사용자가 임의로 판매재개
	EVENT_START("121"), // 행사입점
	EVENT_FORSALE("122"), // 행사재입점
	SOURCING_EXCEPT_START("131"), // 제외소싱상품예외입점/변경
	SALE_END("211"), // 판매종료(판매종료일도래)
	SALE_STATUS("212"), // 판매중단(판매상태변경)
	EVENT_END("221"), // 행사종료(행사종료일도래)
	EVENT_STATUS("222"), // 행사중단(미사용처리)
	TARGET_EXCEPT("311"), // 입점제외 (상품, 브랜드, 상품분류, 상품명)
	ENTP_EXCEPT("321"), // 입점제외 (업체)
	BRAND_EXCEPT("322"), // 입점제외 (업체의 브랜드)
	PRICE_APPLY("411"), // 가격적용
	PROMO_APPLY("421"), // 프로모션적용
	PROMO_END("422"), // 프로모션종료
	PROMO_STATUS("423"), // 프로모션상태변경
	PROMO_EXCEPT("424"), // 프로모션제외
	PROMO_PA_EXCEPT("425"), // 프로모션제휴제외(상품)
	PROMO_PA_EXCEPT_ENTP("426"), // 프로모션제휴제외(업체)
	PROMO_PA_EXCEPT_BRAND("427"), // 프로모션제휴제외(브랜드)
	PROMO_PA_EXCEPT_END("428"), // 프로모션제휴제외종료(상품)
	PROMO_PA_EXCEPT_ENTP_END("429"), // 프로모션제휴제외종료(업체)
	PROMO_PA_EXCEPT_BRAND_END("430"), // 프로모션제휴제외종료(브랜드)
	SHIP_COST_APPLY("431"), // 배송비적용
	PROMO_MARGIN_APPLY("441"), //프로모션최소마진행사적용
	PROMO_MARGIN_END("442"), //프로모션최소마진행사종료
	PROMO_MARGIN_STATUS("443"), //프로모션최소마진행사상태변경(중단)
	GOODS_MODIFY("511"), // 상품정보변경
	GOODSDT_MODIFY("521"), // 단품정보변경
	OFFER_MODIFY("531"), // 정보고시변경
	DESCRIBE_MODIFY("541"), // 기술서변경
	IMAGE_MODIFY("551"), // 상품이미지변경
	INFO_IMAGE_MODIFY("552"), // 상품이미지변경
	TDEAL_DTIMAGE_MODIFY("553"), // 티딜옵션별이미지변경
	ENTPUSER_MODIFY("561"), // 출고/회수지변경
	STOCK_APPLY("611"), // 최대주문가능수량적용
	STOCK_CHANGE("612"), // 주문재고변동
	NOTICE_APPLY("711"), // 공지사항적용
	NOTICE_END("712"), // 공지사항종료
	NOTICE_MODIFY("713"), // 공지사항변경
	NOTICE_EXCEPT("714"), // 공지사항제외
	ENTP_HOLIDAY_APPLY("715"), // 업체휴무일적용
	CATEGORY_MODIFY("811"), // 카테고리매핑변경
	EBAY_CATEGORY_MODIFY("812"), //이베이카테고리매핑변경
	TDEAL_EVENT_APPLY("911"), //티딜행사적용
	TDEAL_EVENT_END("912"), //티딜행사종료
	COPN_ATTRI_FORSALE("920"), //쿠팡구매옵션재입점(target 있는경우)
	COPN_ATTRI_FORSALE_TARGET("921") //쿠팡구매옵션재입점(target 없는경우)
	;

	private String code;

	private CdcReason(String code ) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
