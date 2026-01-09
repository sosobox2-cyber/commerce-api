package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Independent {

	private MultiLanguage name;
	private List<IndependentDetail> details;
	
	// 추천옵션 항목 추가
	private Integer recommendedOptNo; // 추천옵션코드
	private RecommendedOptName recommendedOptName;	//추천옵션명 (국문) 직접입력

	public MultiLanguage getName() {
		return name;
	}

	public void setName(MultiLanguage name) {
		this.name = name;
	}

	public List<IndependentDetail> getDetails() {
		return details;
	}

	public void setDetails(List<IndependentDetail> details) {
		this.details = details;
	}
	
	public Integer getRecommendedOptNo() {
		return recommendedOptNo;
	}

	public void setRecommendedOptNo(Integer recommendedOptNo) {
		this.recommendedOptNo = recommendedOptNo;
	}

	public RecommendedOptName getRecommendedOptName() {
		return recommendedOptName;
	}

	public void setRecommendedOptName(RecommendedOptName recommendedOptName) {
		this.recommendedOptName = recommendedOptName;
	}

	@Override
	public String toString() {
		return "Independent [name=" + name + ", details=" + details + ", recommendedOptNo="+ recommendedOptNo + ", recommendedOptName="
				+ recommendedOptName + "]";
	}

}
