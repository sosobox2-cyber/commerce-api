package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class OptionInfo {
		
	// 단독형 옵션 정렬 순서
	private String simpleOptionSortType;
	// 단독형 옵션	
	private List<OptionSimple> optionSimple;
	// 직접 입력형 옵션
	private List<OptionCustom> optionCustom;
	// 조합형 옵션 정렬 순서
	private String optionCombinationSortType;
	// 조합형 옵션명 목록
	private OptionCombinationGroupNames optionCombinationGroupNames;
	// 조합형 옵션
	private List<OptionCombinations> optionCombinations;
	// 표준형 옵션 그룹
	private List<StandardOptionGroups> standardOptionGroups;	
	// 표준형 옵션
	private List<OptionStandards> optionStandards;	
	// 옵션 재고 수량 관리 사용 여부
	private String useStockManagement;

	
	public String getSimpleOptionSortType() {
		return simpleOptionSortType;
	}

	public void setSimpleOptionSortType(String simpleOptionSortType) {
		this.simpleOptionSortType = simpleOptionSortType;
	}

	public List<OptionSimple> getOptionSimple() {
		return optionSimple;
	}

	public void setOptionSimple(List<OptionSimple> optionSimple) {
		this.optionSimple = optionSimple;
	}

	public List<OptionCustom> getOptionCustom() {
		return optionCustom;
	}

	public void setOptionCustom(List<OptionCustom> optionCustom) {
		this.optionCustom = optionCustom;
	}

	public String getOptionCombinationSortType() {
		return optionCombinationSortType;
	}

	public void setOptionCombinationSortType(String optionCombinationSortType) {
		this.optionCombinationSortType = optionCombinationSortType;
	}

	public OptionCombinationGroupNames getOptionCombinationGroupNames() {
		return optionCombinationGroupNames;
	}

	public void setOptionCombinationGroupNames(OptionCombinationGroupNames optionCombinationGroupNames) {
		this.optionCombinationGroupNames = optionCombinationGroupNames;
	}

	public List<OptionCombinations> getOptionCombinations() {
		return optionCombinations;
	}

	public void setOptionCombinations(List<OptionCombinations> optionCombinations) {
		this.optionCombinations = optionCombinations;
	}

	public List<StandardOptionGroups> getStandardOptionGroups() {
		return standardOptionGroups;
	}

	public void setStandardOptionGroups(List<StandardOptionGroups> standardOptionGroups) {
		this.standardOptionGroups = standardOptionGroups;
	}

	public List<OptionStandards> getOptionStandards() {
		return optionStandards;
	}

	public void setOptionStandards(List<OptionStandards> optionStandards) {
		this.optionStandards = optionStandards;
	}

	public String getUseStockManagement() {
		return useStockManagement;
	}

	public void setUseStockManagement(String useStockManagement) {
		this.useStockManagement = useStockManagement;
	}

	@Override
	public String toString() {
		return "OptionInfo [simpleOptionSortType=" + simpleOptionSortType +"optionSimple=" + optionSimple + "optionCustom=" + optionCustom + "optionCombinationSortType=" + optionCombinationSortType	
				+ "optionCombinationGroupNames=" + optionCombinationGroupNames +"optionCombinations=" + optionCombinations + "standardOptionGroups=" + standardOptionGroups + "optionStandards=" + optionStandards	
				+ "useStockManagement=" + useStockManagement +"]";
	}

}
