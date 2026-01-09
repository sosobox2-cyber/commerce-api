package com.cware.netshopping.common.code;

/**
 * 판매상태[O503]
 * PA_SALE_GB
 *
 */
public enum PaSaleGb {

	REQUEST("10"), // 입점요청
	ERROR("11"), // 정보고시 동기화에러
	FORSALE("20"), // 판매중
	SUSPEND("30"), // 판매중지
	SOLDOUT("35"), // 품절
	EOS("40") // 영구판매중지
	;

	private String code;

	private PaSaleGb(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
