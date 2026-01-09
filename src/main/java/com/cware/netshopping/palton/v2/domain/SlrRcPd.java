package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SlrRcPd {
	String slrRcSpdNo; // 셀러추천판매자상품번호
	String slrRcSitmNo; // 셀러추천판매자단품번호
	Integer epsrPrirRnkg; // 노출우선순위

	public String getSlrRcSpdNo() {
		return slrRcSpdNo;
	}

	public void setSlrRcSpdNo(String slrRcSpdNo) {
		this.slrRcSpdNo = slrRcSpdNo;
	}

	public String getSlrRcSitmNo() {
		return slrRcSitmNo;
	}

	public void setSlrRcSitmNo(String slrRcSitmNo) {
		this.slrRcSitmNo = slrRcSitmNo;
	}

	public Integer getEpsrPrirRnkg() {
		return epsrPrirRnkg;
	}

	public void setEpsrPrirRnkg(Integer epsrPrirRnkg) {
		this.epsrPrirRnkg = epsrPrirRnkg;
	}

	@Override
	public String toString() {
		return "SlrRcPd [slrRcSpdNo=" + slrRcSpdNo + ", slrRcSitmNo=" + slrRcSitmNo + ", epsrPrirRnkg=" + epsrPrirRnkg
				+ "]";
	}

}
