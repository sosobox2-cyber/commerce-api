package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MultiLanguage {
	String eng; // 영문
	String chi; // 중문
	String jpn; // 일문
	String kor; // 국문
	String promotion; // 프로모션용

	public MultiLanguage(String kor) {
		super();
		this.kor = kor;
	}

	public String getEng() {
		return eng;
	}

	public void setEng(String eng) {
		this.eng = eng;
	}

	public String getChi() {
		return chi;
	}

	public void setChi(String chi) {
		this.chi = chi;
	}

	public String getJpn() {
		return jpn;
	}

	public void setJpn(String jpn) {
		this.jpn = jpn;
	}

	public String getKor() {
		return kor;
	}

	public void setKor(String kor) {
		this.kor = kor;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	@Override
	public String toString() {
		return "MultiLanguage [eng=" + eng + ", chi=" + chi + ", jpn=" + jpn + ", kor=" + kor + ", promotion="
				+ promotion + "]";
	}

}
