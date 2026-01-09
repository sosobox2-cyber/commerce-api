package com.cware.netshopping.panaver.v3.domain;


/**
 * 조회 기간 기준
 *
 */
public enum PeriodType {

	  SETTLE_CASEBYCASE_SETTLE_SCHEDULE_DATE("정산 예정일")
	, SETTLE_CASEBYCASE_SETTLE_BASIS_DATE("정산 기준일")
	, SETTLE_CASEBYCASE_SETTLE_COMPLETE_DATE("정산 완료일")
	, SETTLE_CASEBYCASE_PAY_DATE("결제일")
	, SETTLE_CASEBYCASE_TAXRETURN_BASIS_DATE("세금 신고 기준일")
	;

	private String codeName;

	private PeriodType(String codeName) {
		this.codeName = codeName;
	}

	public String codeName() {
		return codeName;
	}
}
