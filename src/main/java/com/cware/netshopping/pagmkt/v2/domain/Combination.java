package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Combination {

	private MultiLanguage name1;
	private MultiLanguage name2;
	private List<IndependentDetail> details;	
	
	// 추천옵션 항목 추가
	private Integer recommendedOptNo1; // 추천옵션코드1
	private Integer recommendedOptNo2; // 추천옵션코드2
	private RecommendedOptName1 recommendedOptName1; // 추천옵션명 (국문) 직접입력1
	private RecommendedOptName2 recommendedOptName2; // 추천옵션명 (국문) 직접입력2	
	private List<CombinationDetail> combinationDetail;

	public MultiLanguage getName1() {
		return name1;
	}

	public void setName1(MultiLanguage name1) {
		this.name1 = name1;
	}

	public MultiLanguage getName2() {
		return name2;
	}

	public void setName2(MultiLanguage name2) {
		this.name2 = name2;
	}

	public List<IndependentDetail> getDetails() {
		return details;
	}

	public void setDetails(List<IndependentDetail> details) {
		this.details = details;
	}
	
	public Integer getRecommendedOptNo1() {
		return recommendedOptNo1;
	}

	public void setRecommendedOptNo1(Integer recommendedOptNo1) {
		this.recommendedOptNo1 = recommendedOptNo1;
	}

	public Integer getRecommendedOptNo2() {
		return recommendedOptNo2;
	}

	public void setRecommendedOptNo2(Integer recommendedOptNo2) {
		this.recommendedOptNo2 = recommendedOptNo2;
	}

	public RecommendedOptName1 getRecommendedOptName1() {
		return recommendedOptName1;
	}

	public void setRecommendedOptName1(RecommendedOptName1 recommendedOptName1) {
		this.recommendedOptName1 = recommendedOptName1;
	}

	public RecommendedOptName2 getRecommendedOptName2() {
		return recommendedOptName2;
	}

	public void setRecommendedOptName2(RecommendedOptName2 recommendedOptName2) {
		this.recommendedOptName2 = recommendedOptName2;
	}
	
	@JsonProperty("details")
	public List<CombinationDetail> getCombinationDetail() {
		return combinationDetail;
	}
	
	@JsonProperty("details")
	public void setCombinationDetail(List<CombinationDetail> combinationDetail) {
		this.combinationDetail = combinationDetail;
	}

	@Override
	public String toString() {
		return "Combination [name1=" + name1 + ", name2=" + name2 + ", details=" + details + ", recommendedOptNo1=" + recommendedOptNo1 
				+ ", recommendedOptNo2=" + recommendedOptNo2 + ", recommendedOptName1=" + recommendedOptName1 
				+ ", recommendedOptName2=" + recommendedOptName2 + ", combinationDetail=" + combinationDetail + "]";
	}

}
