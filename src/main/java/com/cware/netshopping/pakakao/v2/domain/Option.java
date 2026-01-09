package com.cware.netshopping.pakakao.v2.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Option {

	// 옵션타입
	// NONE 옵션 없음
	// SIMPLE 독립형
	// COMBINATION 조합형
	// CUSTOM 작성형
	// SIMPLE_CUSTOM 독립형 + 작성형
	// COMBINATION_CUSTOM 조합형 + 작성형
	String type;

	// 독립형 리스팅 기준
	// CREATE 등록순
	// ABC 가나다순
	// LOW_PRICE 낮은 가격순
	// HIGH_PRICE 높은 가격순
	String simpleSortType;

	// 조합형 리스팅 기준
	// CREATE 등록순
	// ABC 가나다순
	// LOW_PRICE 낮은 가격순
	// HIGH_PRICE 높은 가격순
	String combinationSortType;

	// 독립형 옵션 리스트
	List<Simple> simples;

	// 조합형 옵션의 최종 리스트
	List<Combination> combinations;

	// 조합형 옵션 리스트
	List<CombinationAttribute> combinationAttributes;

	// 작성형 옵션 리스트
	List<Custom> customs;

	// 옵션 노출 정보
	OptionDisplay optionDisplay;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSimpleSortType() {
		return simpleSortType;
	}

	public void setSimpleSortType(String simpleSortType) {
		this.simpleSortType = simpleSortType;
	}

	public String getCombinationSortType() {
		return combinationSortType;
	}

	public void setCombinationSortType(String combinationSortType) {
		this.combinationSortType = combinationSortType;
	}

	public List<Simple> getSimples() {
		return simples;
	}

	public void setSimples(List<Simple> simples) {
		this.simples = simples;
	}

	public List<Combination> getCombinations() {
		return combinations;
	}

	public void setCombinations(List<Combination> combinations) {
		this.combinations = combinations;
	}

	public List<CombinationAttribute> getCombinationAttributes() {
		return combinationAttributes;
	}

	public void setCombinationAttributes(List<CombinationAttribute> combinationAttributes) {
		this.combinationAttributes = combinationAttributes;
	}

	public List<Custom> getCustoms() {
		return customs;
	}

	public void setCustoms(List<Custom> customs) {
		this.customs = customs;
	}

	public OptionDisplay getOptionDisplay() {
		return optionDisplay;
	}

	public void setOptionDisplay(OptionDisplay optionDisplay) {
		this.optionDisplay = optionDisplay;
	}

	@Override
	public String toString() {
		return "Option [type=" + type + ", simpleSortType=" + simpleSortType + ", combinationSortType="
				+ combinationSortType + ", simples=" + simples + ", combinations=" + combinations
				+ ", combinationAttributes=" + combinationAttributes + ", customs=" + customs + ", optionDisplay="
				+ optionDisplay + "]";
	}

}
