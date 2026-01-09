package com.cware.netshopping.passg.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UitemEaitemOptnCac {
	String eaitemOptnCacNm1; // 상품별옵션계산명1
	String eaitemMinCacUnitVal1; // 상품별최소계산단위값1
	String eaitemMaxCacUnitVal1; // 상품별최대계산단위값1

	// 맞춤제작측정단위코드
	// 11 : mm
	// 12 : cm
	// 13 : m
	String cmakeMesurUnitCd1;
	String eaitemOptnCacNm2; // 상품별옵션계산명2
	Integer eaitemMinCacUnitVal2; // 상품별최소계산단위값2
	Integer eaitemMaxCacUnitVal2; // 상품별최대계산단위값2
	Integer purchUnitQty; // 구매단위수량
	Double punitSellprc; // 단가

	public String getEaitemOptnCacNm1() {
		return eaitemOptnCacNm1;
	}

	public void setEaitemOptnCacNm1(String eaitemOptnCacNm1) {
		this.eaitemOptnCacNm1 = eaitemOptnCacNm1;
	}

	public String getEaitemMinCacUnitVal1() {
		return eaitemMinCacUnitVal1;
	}

	public void setEaitemMinCacUnitVal1(String eaitemMinCacUnitVal1) {
		this.eaitemMinCacUnitVal1 = eaitemMinCacUnitVal1;
	}

	public String getEaitemMaxCacUnitVal1() {
		return eaitemMaxCacUnitVal1;
	}

	public void setEaitemMaxCacUnitVal1(String eaitemMaxCacUnitVal1) {
		this.eaitemMaxCacUnitVal1 = eaitemMaxCacUnitVal1;
	}

	public String getCmakeMesurUnitCd1() {
		return cmakeMesurUnitCd1;
	}

	public void setCmakeMesurUnitCd1(String cmakeMesurUnitCd1) {
		this.cmakeMesurUnitCd1 = cmakeMesurUnitCd1;
	}

	public String getEaitemOptnCacNm2() {
		return eaitemOptnCacNm2;
	}

	public void setEaitemOptnCacNm2(String eaitemOptnCacNm2) {
		this.eaitemOptnCacNm2 = eaitemOptnCacNm2;
	}

	public Integer getEaitemMinCacUnitVal2() {
		return eaitemMinCacUnitVal2;
	}

	public void setEaitemMinCacUnitVal2(Integer eaitemMinCacUnitVal2) {
		this.eaitemMinCacUnitVal2 = eaitemMinCacUnitVal2;
	}

	public Integer getEaitemMaxCacUnitVal2() {
		return eaitemMaxCacUnitVal2;
	}

	public void setEaitemMaxCacUnitVal2(Integer eaitemMaxCacUnitVal2) {
		this.eaitemMaxCacUnitVal2 = eaitemMaxCacUnitVal2;
	}

	public Integer getPurchUnitQty() {
		return purchUnitQty;
	}

	public void setPurchUnitQty(Integer purchUnitQty) {
		this.purchUnitQty = purchUnitQty;
	}

	public Double getPunitSellprc() {
		return punitSellprc;
	}

	public void setPunitSellprc(Double punitSellprc) {
		this.punitSellprc = punitSellprc;
	}

	@Override
	public String toString() {
		return "UitemEaitemOptnCac [eaitemOptnCacNm1=" + eaitemOptnCacNm1 + ", eaitemMinCacUnitVal1="
				+ eaitemMinCacUnitVal1 + ", eaitemMaxCacUnitVal1=" + eaitemMaxCacUnitVal1 + ", cmakeMesurUnitCd1="
				+ cmakeMesurUnitCd1 + ", eaitemOptnCacNm2=" + eaitemOptnCacNm2 + ", eaitemMinCacUnitVal2="
				+ eaitemMinCacUnitVal2 + ", eaitemMaxCacUnitVal2=" + eaitemMaxCacUnitVal2 + ", purchUnitQty="
				+ purchUnitQty + ", punitSellprc=" + punitSellprc + "]";
	}

}
