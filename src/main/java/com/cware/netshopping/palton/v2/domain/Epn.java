package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Epn {
	// 상품설명유형코드 [공통코드 : PD_EPN_TYP_CD]
	// DSCRP 상품기술서
	// AS_CNTS A/S내용설명
	// PRCTN 주의사항설명
	String pdEpnTypCd;

	// 내용
	// html입력시 사용한다.
	String cnts;

	public String getPdEpnTypCd() {
		return pdEpnTypCd;
	}

	public void setPdEpnTypCd(String pdEpnTypCd) {
		this.pdEpnTypCd = pdEpnTypCd;
	}

	public String getCnts() {
		return cnts;
	}

	public void setCnts(String cnts) {
		this.cnts = cnts;
	}

	@Override
	public String toString() {
		return "Epn [pdEpnTypCd=" + pdEpnTypCd + ", cnts=" + cnts + "]";
	}

}
