package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Dcat {
	// 몰구분코드 [공통코드 : MALL_DVS_CD]
	// LTON : 롯데ON
	String mallCd;
	String lfDcatNo; // leaf전시카테고리번호

	public String getMallCd() {
		return mallCd;
	}

	public void setMallCd(String mallCd) {
		this.mallCd = mallCd;
	}

	public String getLfDcatNo() {
		return lfDcatNo;
	}

	public void setLfDcatNo(String lfDcatNo) {
		this.lfDcatNo = lfDcatNo;
	}

	@Override
	public String toString() {
		return "Dcat [mallCd=" + mallCd + ", lfDcatNo=" + lfDcatNo + "]";
	}

}
