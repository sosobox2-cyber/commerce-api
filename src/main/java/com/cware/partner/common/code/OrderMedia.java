package com.cware.partner.common.code;

/**
 * 주문매체 (J001)
 * ORDER_MEDIA
 *
 */
public enum OrderMedia {

	CALL("11"), // 상담원
	ARS("40"), // ARS
	VISUAL_ARS("41"), // 보이는ARS
	ONDEMAND_ARS("42"), // 온디맨드ARS
	EASYPAY("45"), // 간편결제
	REMOTE_ORDER("46"), // 리모콘주문
	NUGUAPP("47"), // NUGUAPP
	KAKAO("48"), // KAKAO
	ONDEMAND_ARS_DATA("52"), // 온디맨드ARS데이터
	EASYPAY_DATA("55"), // 간편결제데이터
	REMOTE_ORDER_DATA("56"), // 리모콘주문데이터
	PCWEB("61"), // PCWeb
	MOBILE("62") // Mobile
	;

	private String code;

	private OrderMedia(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
