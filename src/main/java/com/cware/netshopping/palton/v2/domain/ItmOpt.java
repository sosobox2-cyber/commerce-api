package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItmOpt {
	String optCd; // 옵션코드 [속성모듈 제공 항목]
	String optNm; // 옵션명 [속성모듈 제공 항목]
	String optValCd; // 옵션값코드 [속성모듈 제공 항목]
	String optVal; // 옵션값 [속성모듈 제공 항목]
	String dtlsVal; // 세부값

	public String getOptCd() {
		return optCd;
	}

	public void setOptCd(String optCd) {
		this.optCd = optCd;
	}

	public String getOptNm() {
		return optNm;
	}

	public void setOptNm(String optNm) {
		this.optNm = optNm;
	}

	public String getOptValCd() {
		return optValCd;
	}

	public void setOptValCd(String optValCd) {
		this.optValCd = optValCd;
	}

	public String getOptVal() {
		return optVal;
	}

	public void setOptVal(String optVal) {
		this.optVal = optVal;
	}

	public String getDtlsVal() {
		return dtlsVal;
	}

	public void setDtlsVal(String dtlsVal) {
		this.dtlsVal = dtlsVal;
	}

	@Override
	public String toString() {
		return "ItmOpt [optCd=" + optCd + ", optNm=" + optNm + ", optValCd=" + optValCd + ", optVal=" + optVal
				+ ", dtlsVal=" + dtlsVal + "]";
	}

}
