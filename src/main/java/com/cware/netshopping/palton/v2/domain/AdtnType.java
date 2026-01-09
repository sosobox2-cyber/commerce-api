package com.cware.netshopping.palton.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AdtnType {
	String adtnTypNo; // 추가유형번호
	String adtnTypNm; // 추가유형명
	Integer epsrPrirRnkg; // 노출우선순
	List<AdtnPd> adtnPdLst; // 추가상품목록

	public String getAdtnTypNo() {
		return adtnTypNo;
	}

	public void setAdtnTypNo(String adtnTypNo) {
		this.adtnTypNo = adtnTypNo;
	}

	public String getAdtnTypNm() {
		return adtnTypNm;
	}

	public void setAdtnTypNm(String adtnTypNm) {
		this.adtnTypNm = adtnTypNm;
	}

	public Integer getEpsrPrirRnkg() {
		return epsrPrirRnkg;
	}

	public void setEpsrPrirRnkg(Integer epsrPrirRnkg) {
		this.epsrPrirRnkg = epsrPrirRnkg;
	}

	public List<AdtnPd> getAdtnPdLst() {
		return adtnPdLst;
	}

	public void setAdtnPdLst(List<AdtnPd> adtnPdLst) {
		this.adtnPdLst = adtnPdLst;
	}

	@Override
	public String toString() {
		return "AdtnType [adtnTypNo=" + adtnTypNo + ", adtnTypNm=" + adtnTypNm + ", epsrPrirRnkg=" + epsrPrirRnkg
				+ ", adtnPdLst=" + adtnPdLst + "]";
	}

}
