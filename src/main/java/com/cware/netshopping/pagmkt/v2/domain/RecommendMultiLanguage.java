package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RecommendMultiLanguage {
	String englishText; 	  // 영문
	String chineseText; 	  // 중문
	String japaneseText;  // 일문
	String koreanText; 	  // 국문
	int exposeLanguage; // 노출언어 선택

	public RecommendMultiLanguage(String koreanText) {
		super();
		this.koreanText = koreanText;
	}
	
	public String getEnglishText() {
		return englishText;
	}
	
	public void setEnglishText(String englishText) {
		this.englishText = englishText;
	}
	
	public String getChineseText() {
		return chineseText;
	}
	
	public void setChineseText(String chineseText) {
		this.chineseText = chineseText;
	}
	
	public String getJapaneseText() {
		return japaneseText;
	}
	
	public void setJapaneseText(String japaneseText) {
		this.japaneseText = japaneseText;
	}
	
	public String getKoreanText() {
		return koreanText;
	}
	
	public void setKoreanText(String koreanText) {
		this.koreanText = koreanText;
	}
	
	public int getExposeLanguage() {
		return exposeLanguage;
	}
	
	public void setExposeLanguage(int exposeLanguage) {
		this.exposeLanguage = exposeLanguage;
	}
	

	@Override
	public String toString() {
		return "RecommendMultiLanguage [englishText=" + englishText + ", chineseText=" + chineseText + ", japaneseText=" + japaneseText + ", koreanText=" + koreanText 
				+ ", exposeLanguage=" + exposeLanguage + "]";
	}

}
