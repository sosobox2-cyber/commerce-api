package com.cware.partner.common.code;

/**
 * 소싱매체 (J009)
 * SOURCING_MEDIA
 *
 */
public enum SourcingMedia {

	TV("01"), // 방송
	ONLINE("61"), // 쇼핑몰
	DATA("90") // 데이터
	;

	private String code;

	private SourcingMedia(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
