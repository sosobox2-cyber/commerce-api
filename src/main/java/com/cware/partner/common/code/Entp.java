package com.cware.partner.common.code;

/**
 * 하드코딩된 업체코드값
 *
 */
public enum Entp {

	YIC("109153") //(주)와이아이씨컴퍼니 제휴사별 연동 제외처리시 사용
	;

	private String code;

	private Entp(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
