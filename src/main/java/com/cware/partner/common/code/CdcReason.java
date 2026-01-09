package com.cware.partner.common.code;

/**
 * 데이터변경사유 (TPACDCREASON)
 * 변경사유코드(CDC_REASON_CODE), 가중치(CDC_BOOSTING)
 */
public enum CdcReason {

	SALE_START("111", 20 ), // 상품입점
	EVENT_START("121", 20), // 행사입점
	SALE_END("211", 30), // 판매종료(판매종료일도래)
	SALE_STATUS("212", 30), // 판매중단(판매상태변경)
	EVENT_END("221", 30), // 행사종료(행사종료일도래)
	EVENT_STATUS("222", 30), // 행사중단(미사용처리)
	TARGET_EXCEPT("311", 30), // 입점제외 (상품, 브랜드, 상품분류, 상품명)
	ENTP_EXCEPT("321", 30), // 입점제외 (업체)
	BRAND_EXCEPT("322", 30), // 입점제외 (업체의 브랜드)
	PRICE_APPLY("411", 15), // 가격적용
	PROMO_APPLY("421", 15), // 프로모션적용
	PROMO_END("422", 15), // 프로모션종료
	PROMO_STATUS("423", 15), // 프로모션상태변경
	PROMO_EXCEPT("424", 15), // 프로모션제외
	PROMO_PA_EXCEPT("425", 15), // 프로모션제휴제외
	SHIP_COST_APPLY("431", 15), // 배송비적용
	GOODS_MODIFY("511", 10), // 상품정보변경
	GOODSDT_MODIFY("521", 10), // 단품정보변경
	OFFER_MODIFY("531", 5), // 정보고시변경
	DESCRIBE_MODIFY("541", 5), // 기술서변경
	IMAGE_MODIFY("551", 5), // 상품이미지변경
	INFO_IMAGE_MODIFY("552", 5), // 상품이미지변경
	ENTPUSER_MODIFY("561", 10), // 출고/회수지변경(미사용)
	STOCK_APPLY("611", 3), // 최대주문가능수량적용
	STOCK_CHANGE("612", 3) // 주문재고변동
	;

	private String code;
	private int boosting;

	private CdcReason(String code, int boosting ) {
		this.code = code;
		this.boosting = boosting;
	}

	public String code() {
		return code;
	}


	public int boosting() {
		return boosting;
	}

}
