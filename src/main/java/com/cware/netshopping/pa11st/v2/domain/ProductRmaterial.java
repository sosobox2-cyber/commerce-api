package com.cware.netshopping.pa11st.v2.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRmaterial {

	// 원재료 상품명
	private String rmaterialNm;

	// 원료명
	private String ingredNm;

	// 원산지
	private String orgnCountry;

	// 함량
	private String content;

	public String getRmaterialNm() {
		return rmaterialNm;
	}

	public void setRmaterialNm(String rmaterialNm) {
		this.rmaterialNm = rmaterialNm;
	}

	public String getIngredNm() {
		return ingredNm;
	}

	public void setIngredNm(String ingredNm) {
		this.ingredNm = ingredNm;
	}

	public String getOrgnCountry() {
		return orgnCountry;
	}

	public void setOrgnCountry(String orgnCountry) {
		this.orgnCountry = orgnCountry;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ProductRmaterial [rmaterialNm=" + rmaterialNm + ", ingredNm=" + ingredNm + ", orgnCountry="
				+ orgnCountry + ", content=" + content + "]";
	}

}
