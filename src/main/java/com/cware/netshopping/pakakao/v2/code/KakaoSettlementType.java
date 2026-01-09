package com.cware.netshopping.pakakao.v2.code;

/**
 * 카카오 주문상태
 * SettlementType
 */
public enum KakaoSettlementType {
	DELIVERY_ITEM_DECISION("DeliveryItemDecision", "배송상품 구매결정"),
    DELIVERY_ITEM_DECISION_CANCEL("DeliveryItemDecisionCancel", "배송상품 구매결정취소"),
    DELIVERY_ITEM_DECISION_REFUND("DeliveryItemDecisionRefund", "배송상품 구매결정환불"),
    DELIVERY_AMOUNT_DECISION("DeliveryAmountDecision", "배송비 확정"),
    DELIVERY_AMOUNT_DECISION_CANCEL("DeliveryAmountDecisionCancel", "배송비 확정취소"),
    DELIVERY_AMOUNT_DECISION_REFUND("DeliveryAmountDecisionRefund", "배송비 확정환불"),
    DELIVERY_ITEM_DECISION_CLEANING("DeliveryItemDecisionCleaning", "배송상품 구매결정상계"),
    DELIVERY_ITEM_DECISION_REPROCESS("DeliveryItemDecisionReprocess", "배송상품 구매결정재집계");

    private final String code;
    private final String description;

    KakaoSettlementType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** 외부 시스템 코드 (예: DeliveryItemDecision) */
    public String getCode() {
        return code;
    }

    /** 코드에 대한 한글 설명 (예: 배송상품 구매결정) */
    public String getDescription() {
        return description;
    }

    /**
     * 코드 문자열로 Enum 반환 (대소문자 무시)
     * @param code SettlementType 코드값
     * @return SettlementType (없으면 null)
     */
    public static KakaoSettlementType fromCode(String code) {
    	if (code == null) return null;
        for (KakaoSettlementType type : KakaoSettlementType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * @param code SettlementType 코드값
     * @return 설명 문자열 (없으면 code 리턴)
     */
    public static String getDescriptionByCode(String code) {
    	KakaoSettlementType type = fromCode(code);
    	return (type != null) ? type.getDescription() : code;
    }


    @Override
    public String toString() {
        return code;
    }

}
