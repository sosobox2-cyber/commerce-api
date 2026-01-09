package com.cware.netshopping.pakakao.v2.code;

/**
 * 카카오 주문상태
 * SettlementType
 */
public enum KakaoPayMainMethod {
	MOBILE("Mobile", "휴대폰 결제"),
    CARD("Card", "신용카드"),
    KAKAO_BANK_WALLET_CARD("KakaoBankWalletCard", "뱅크월렛 카카오"),
    KAKAO_BANK_WALLET_CARD_GIFT("KakaoBankWalletCardGift", "뱅크월렛 카카오"),
    KAKAO_BANK_WALLET_MONEY("KakaoBankWalletMoney", "뱅크월렛 카카오"),
    KAKAO_BANK_WALLET_MONEY_GIFT("KakaoBankWalletMoneyGift", "뱅크월렛 카카오"),
    KAKAO_SIMPLE_PAYMENT("KakaoSimplePayment", "카드 간편결제"),
    KAKAO_SIMPLE_MOBILE("KakaoSimpleMobile", "휴대폰 간편결제"),
    KAKAO_EASY_BANK("KakaoEasyBank", "카카오머니 결제"),
    KAKAO_PAY_EASY_CARD("KakaoPayEasyCard", "카카오페이 카드결제"),
    KAKAO_PAY_EASY_POINT("KakaoPayEasyPoint", "현금(카카오머니)"),
    BANK_ACCOUNT("BankAccount", "무통장입금"),
    KAKAO_POINT("KakaoPoint", "포인트"),
    FOREIGN_CARD("ForeignCard", "해외신용카드"),
    SAMSUNG_PAY_CARD("SamsungPayCard", "신용카드");

    private final String code;
    private final String description;

    KakaoPayMainMethod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** 외부 시스템 코드 (예: Card) */
    public String getCode() {
        return code;
    }

    /** 코드에 대한 한글 설명 (예: 신용카드) */
    public String getDescription() {
        return description;
    }

    /**
     * 코드 문자열로 Enum 반환 (대소문자 무시)
     * @param code SettlementType 코드값
     * @return SettlementType (없으면 null)
     */
    public static KakaoPayMainMethod fromCode(String code) {
    	if (code == null) return null;
        for (KakaoPayMainMethod type : KakaoPayMainMethod.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * @param code PayMainMethod 코드값
     * @return 설명 문자열 (없으면 code 리턴)
     */
    public static String getDescriptionByCode(String code) {
        KakaoPayMainMethod type = fromCode(code);
        return (type != null) ? type.getDescription() : code;
    }


    @Override
    public String toString() {
        return code;
    }

}
