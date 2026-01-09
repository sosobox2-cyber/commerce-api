package com.cware.partner.common.code;

/**
 * 모바일이용권구분
 * MOB_GIFT_GB
 *
 */
public enum MobGift {

	GIFTICON("00"), // 기프티콘
	GIFTCARD("10"), // 모바일상품권
	NONE("90") // 미대상
	;

	private String code;

	private MobGift(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
