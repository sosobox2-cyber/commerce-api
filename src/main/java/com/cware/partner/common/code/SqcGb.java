package com.cware.partner.common.code;

/**
 * QA결과 (B123)
 * SQC_GB, DESCRIBE_SQC_GB
 *
 */
public enum SqcGb {

	REQUEST("00"), // 의뢰
	RECEIPT("05"), // 진행
	HOLD("06"), // 보류
	FAIL("09"), // 불합격
	PASS("16"), // 합격
	EPASS("18"), // 예외합격
	RPASS("19") // 리스크합격
	;

	private String code;

	private SqcGb(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

}
