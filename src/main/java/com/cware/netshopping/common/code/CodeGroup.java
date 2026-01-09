package com.cware.netshopping.common.code;

/**
 * 코드분류
 * CODE_LGROUP
 *
 */
public enum CodeGroup {

	EBAY_POLICY_EXCEPT_ENTP("O584"), // 이베이 발송정책 예외 업체
	EBAY_POLICY_EXCEPT_LMSD("O585") // 이베이 발송정책 예외 중분류
	;

	private String code;

	private CodeGroup(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
