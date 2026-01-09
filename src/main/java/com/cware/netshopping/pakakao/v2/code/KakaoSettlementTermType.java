package com.cware.netshopping.pakakao.v2.code;

/**
 * 카카오 주문상태
 * SettlementType
 */
public enum KakaoSettlementTermType {
	MONTHLY("Monthly", "월 1회"),
	SEMI_MONTHLY("SemiMonthly", "월 2회"),
	DAILY("Daily", "일 1회");

    private final String code;
    private final String description;

    KakaoSettlementTermType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** 외부 시스템 코드 (예: Daily) */
    public String getCode() {
        return code;
    }

    /** 코드에 대한 한글 설명 (예: 일 1회) */
    public String getDescription() {
        return description;
    }

    /**
     * 코드 문자열로 Enum 반환 (대소문자 무시)
     * @param code SettlementType 코드값
     * @return SettlementType (없으면 null)
     */
    public static KakaoSettlementTermType fromCode(String code) {
    	if (code == null) return null;
        for (KakaoSettlementTermType type : KakaoSettlementTermType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * @param code SettlementTermType 코드값
     * @return 설명 문자열 (없으면 code 리턴)
     */
    public static String getDescriptionByCode(String code) {
    	KakaoSettlementTermType type = fromCode(code);
        return (type != null) ? type.getDescription() : code;
    }


    @Override
    public String toString() {
        return code;
    }

}
