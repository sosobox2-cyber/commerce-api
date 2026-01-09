package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UitemAttr {
	String uitemCacOptnYn; // 단품계산옵션여부

	// 단품옵션선택유형1 (commCd : I536)
	// 10 : 드롭다운형 20 : 간편보기형 30 : 캘린더
	String uitemOptnChoiTypeCd1;

	// 단품옵션노출유형1 (commCd : I537)
	// 10 : 텍스트 20 : 날짜(YYYY-MM-DD)
	// 30 : 컬러칩 40 : 옵션이미지
	String uitemOptnExpsrTypeCd1;

	// 단품옵션선택유형2 (commCd : I536)
	// 10 : 드롭다운형 20 : 간편보기형 30 : 캘린더
	String uitemOptnChoiTypeCd2;

	// 단품옵션노출유형2 (commCd : I537)
	// 10 : 텍스트 20 : 날짜(YYYY-MM-DD)
	// 30 : 컬러칩 40 : 옵션이미지
	String uitemOptnExpsrTypeCd2;

	// 단품옵션선택유형3 (commCd : I536)
	// 10 : 드롭다운형 20 : 간편보기형 30 : 캘린더
	String uitemOptnChoiTypeCd3;

	// 단품옵션노출유형3 (commCd : I537)
	// 10 : 텍스트 20 : 날짜(YYYY-MM-DD)
	// 30 : 컬러칩 40 : 옵션이미지
	String uitemOptnExpsrTypeCd3;

	// 단품옵션선택유형4 (commCd : I536)
	// 10 : 드롭다운형 20 : 간편보기형 30 : 캘린더
	String uitemOptnChoiTypeCd4;

	// 단품옵션노출유형4 (commCd : I537)
	// 10 : 텍스트 20 : 날짜(YYYY-MM-DD)
	// 30 : 컬러칩 40 : 옵션이미지
	String uitemOptnExpsrTypeCd4;

	// 단품옵션선택유형5 (commCd : I536)
	// 10 : 드롭다운형 20 : 간편보기형 30 : 캘린더
	String uitemOptnChoiTypeCd5;

	// 단품옵션노출유형5 (commCd : I537)
	// 10 : 텍스트 20 : 날짜(YYYY-MM-DD)
	// 30 : 컬러칩 40 : 옵션이미지
	String uitemOptnExpsrTypeCd5;

	public String getUitemCacOptnYn() {
		return uitemCacOptnYn;
	}

	public void setUitemCacOptnYn(String uitemCacOptnYn) {
		this.uitemCacOptnYn = uitemCacOptnYn;
	}

	public String getUitemOptnChoiTypeCd1() {
		return uitemOptnChoiTypeCd1;
	}

	public void setUitemOptnChoiTypeCd1(String uitemOptnChoiTypeCd1) {
		this.uitemOptnChoiTypeCd1 = uitemOptnChoiTypeCd1;
	}

	public String getUitemOptnExpsrTypeCd1() {
		return uitemOptnExpsrTypeCd1;
	}

	public void setUitemOptnExpsrTypeCd1(String uitemOptnExpsrTypeCd1) {
		this.uitemOptnExpsrTypeCd1 = uitemOptnExpsrTypeCd1;
	}

	public String getUitemOptnChoiTypeCd2() {
		return uitemOptnChoiTypeCd2;
	}

	public void setUitemOptnChoiTypeCd2(String uitemOptnChoiTypeCd2) {
		this.uitemOptnChoiTypeCd2 = uitemOptnChoiTypeCd2;
	}

	public String getUitemOptnExpsrTypeCd2() {
		return uitemOptnExpsrTypeCd2;
	}

	public void setUitemOptnExpsrTypeCd2(String uitemOptnExpsrTypeCd2) {
		this.uitemOptnExpsrTypeCd2 = uitemOptnExpsrTypeCd2;
	}

	public String getUitemOptnChoiTypeCd3() {
		return uitemOptnChoiTypeCd3;
	}

	public void setUitemOptnChoiTypeCd3(String uitemOptnChoiTypeCd3) {
		this.uitemOptnChoiTypeCd3 = uitemOptnChoiTypeCd3;
	}

	public String getUitemOptnExpsrTypeCd3() {
		return uitemOptnExpsrTypeCd3;
	}

	public void setUitemOptnExpsrTypeCd3(String uitemOptnExpsrTypeCd3) {
		this.uitemOptnExpsrTypeCd3 = uitemOptnExpsrTypeCd3;
	}

	public String getUitemOptnChoiTypeCd4() {
		return uitemOptnChoiTypeCd4;
	}

	public void setUitemOptnChoiTypeCd4(String uitemOptnChoiTypeCd4) {
		this.uitemOptnChoiTypeCd4 = uitemOptnChoiTypeCd4;
	}

	public String getUitemOptnExpsrTypeCd4() {
		return uitemOptnExpsrTypeCd4;
	}

	public void setUitemOptnExpsrTypeCd4(String uitemOptnExpsrTypeCd4) {
		this.uitemOptnExpsrTypeCd4 = uitemOptnExpsrTypeCd4;
	}

	public String getUitemOptnChoiTypeCd5() {
		return uitemOptnChoiTypeCd5;
	}

	public void setUitemOptnChoiTypeCd5(String uitemOptnChoiTypeCd5) {
		this.uitemOptnChoiTypeCd5 = uitemOptnChoiTypeCd5;
	}

	public String getUitemOptnExpsrTypeCd5() {
		return uitemOptnExpsrTypeCd5;
	}

	public void setUitemOptnExpsrTypeCd5(String uitemOptnExpsrTypeCd5) {
		this.uitemOptnExpsrTypeCd5 = uitemOptnExpsrTypeCd5;
	}

	@Override
	public String toString() {
		return "UitemAttr [uitemCacOptnYn=" + uitemCacOptnYn + ", uitemOptnChoiTypeCd1=" + uitemOptnChoiTypeCd1
				+ ", uitemOptnExpsrTypeCd1=" + uitemOptnExpsrTypeCd1 + ", uitemOptnChoiTypeCd2=" + uitemOptnChoiTypeCd2
				+ ", uitemOptnExpsrTypeCd2=" + uitemOptnExpsrTypeCd2 + ", uitemOptnChoiTypeCd3=" + uitemOptnChoiTypeCd3
				+ ", uitemOptnExpsrTypeCd3=" + uitemOptnExpsrTypeCd3 + ", uitemOptnChoiTypeCd4=" + uitemOptnChoiTypeCd4
				+ ", uitemOptnExpsrTypeCd4=" + uitemOptnExpsrTypeCd4 + ", uitemOptnChoiTypeCd5=" + uitemOptnChoiTypeCd5
				+ ", uitemOptnExpsrTypeCd5=" + uitemOptnExpsrTypeCd5 + "]";
	}

}
