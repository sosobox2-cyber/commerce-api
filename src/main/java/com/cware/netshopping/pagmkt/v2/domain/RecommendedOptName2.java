package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RecommendedOptName2 {

	private String  koreanText; // 추천옵션항목 (국문)직접입력
	private Integer exposeLanguage; // 노출언어 선택 1: 한국어, 2: 영어 한글 표시, 3: 영어

	public String getKoreanText() {
		return koreanText;
	}
	
	public void setKoreanText(String koreanText) {
		this.koreanText = koreanText;
	}

	public Integer getExposeLanguage() {
		return exposeLanguage;
	}

	public void setExposeLanguage(Integer exposeLanguage) {
		this.exposeLanguage = exposeLanguage;
	}

	@Override
	public String toString() {
		return "RecommendedOptName2 [koreanText=" + koreanText + ", exposeLanguage=" + exposeLanguage + "]";
	}

}
