package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItypOpt {

	// 입력형옵션구분코드 [공통코드 : ITYP_OPT_DVS_CD]
	// NO 숫자
	// TXT 텍스트
	// DATE 달력형
	// TIME 시간선택형
	String itypOptDvsCd;

	// 입력형옵션명
	String itypOptNm;

	public String getItypOptDvsCd() {
		return itypOptDvsCd;
	}

	public void setItypOptDvsCd(String itypOptDvsCd) {
		this.itypOptDvsCd = itypOptDvsCd;
	}

	public String getItypOptNm() {
		return itypOptNm;
	}

	public void setItypOptNm(String itypOptNm) {
		this.itypOptNm = itypOptNm;
	}

	@Override
	public String toString() {
		return "ItypOpt [itypOptDvsCd=" + itypOptDvsCd + ", itypOptNm=" + itypOptNm + "]";
	}

}
