package com.cware.netshopping.common.code;

/**
 * 이베이발송정책[O583] POLICY_TYPE
 *
 */
public enum PolicyType {

	ONEDAY("A"), // 당일배송
	INORDER("B"), // 순차발송
	GLOBAL("C"), // 해외발송
	REQDAY("D"), // 요청일발송
	CUSTOM("E"), // 주문제작발송
	NOTAPPOINT("F") // 발송일미정
	;

	private String code;

	private PolicyType(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}
}