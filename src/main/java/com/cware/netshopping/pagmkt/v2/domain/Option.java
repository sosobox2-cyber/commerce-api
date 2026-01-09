package com.cware.netshopping.pagmkt.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
	orderOpts 옵션(구버전 옵션 사용 안함)
	0: 옵션미사용
	1: 선택형(최대 20개까지 가능)/independent --------------사용
	2: 2개조합형(최대 200개까지 가능)/combination
	5: 텍스트형
	6: 선택형 + 텍스트형
	7 : 2개조합형 + 텍스트형

	* 옵션 타입이 선택형일 경우, independent / 2개 조합형일 경우, combination 항목값으로 필수 연동
	* 텍스트형(type : 5) 옵션 사용하는 경우 itemAddtionalInfo > orderOpts >
	* isStockManage(옵션재고관리여부)를 false로 연동
	* 옵션 가능개수는 추후 변경될 수 있음
**/

/**
	recommendedOpts 옵션 (현재 추천 옵션 사용)
	0 : 옵션 미사용
	1 : 선택형, independent로 입력
	2 : 2개 조합형, combination으로 입력
	3 : 3개 조합형, combination으로 입력
	4 : 계산형, calculation 로 입력
	5 : 텍스트형
	6 : 선택형 + 텍스트형
	7 : 2개 조합형 + 텍스트형
	8 : 3개 조합형 + 텍스트형
	9 : 선택형 + 계산형
	*선택형 50개, 조합형 500개까지 등록 가능
**/

@JsonInclude(Include.NON_NULL)
public class Option {

		
	private int type;

	// 옵션재고관리
	private Boolean isStockManage;

	// 선택형 옵션
	private List<Independent> independent;	

	// 조합형 옵션
	private Object combination;

	// 모델명 또는 각인문구 등 추가로 문구를 입력받고자 할 때 사용
	// 텍스트형 사용할 경우 필수 최대 20byte까지 입력 가능
	private Object text;
	
	// 추천 선택형 옵션
	private Independent recommendIndependent;
	
	// 추천 조합형 옵션
	private Combination recommendCombination;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@JsonProperty("isStockManage")
	public Boolean isStockManage() {
		return isStockManage;
	}

	@JsonProperty("isStockManage")
	public void setStockManage(Boolean isStockManage) {
		this.isStockManage = isStockManage;
	}

	public List<Independent> getIndependent() {
		return independent;
	}

	public void setIndependent(List<Independent> independent) {
		this.independent = independent;
	}

	public Object getCombination() {
		return combination;
	}

	public void setCombination(Object combination) {
		this.combination = combination;
	}

	public Object getText() {
		return text;
	}

	public void setText(Object text) {
		this.text = text;
	}
	
	@JsonProperty("independent")
	public Independent getRecommendIndependent() {
		return recommendIndependent;
	}
	
	@JsonProperty("independent")
	public void setRecommendIndependent(Independent recommendIndependent) {
		this.recommendIndependent = recommendIndependent;
	}
	
	@JsonProperty("combination")
	public Combination getRecommendCombination() {
		return recommendCombination;
	}
	
	@JsonProperty("combination")
	public void setRecommendCombination(Combination recommendCombination) {
		this.recommendCombination = recommendCombination;
	}

	@Override
	public String toString() {
		return "OrderOpts [type=" + type + ", isStockManage=" + isStockManage + ", independent=" + independent
				+ ", combination=" + combination + ", text=" + text + ", recommendIndependent=" + recommendIndependent 
				+ ", recommendCombination=" + recommendCombination +"]";
	}

}
