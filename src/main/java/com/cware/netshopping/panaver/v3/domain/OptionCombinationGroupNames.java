package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OptionCombinationGroupNames {

	// 조합형 옵션명 1
	private String optionGroupName1;
	// 조합형 옵션명 2
	private String optionGroupName2;
	// 조합형 옵션명 3
	private String optionGroupName3;
	// 조합형 옵션명 4
	private String optionGroupName4;
	
	
	public String getOptionGroupName1() {
		return optionGroupName1;
	}

	public void setOptionGroupName1(String optionGroupName1) {
		this.optionGroupName1 = optionGroupName1;
	}

	public String getOptionGroupName2() {
		return optionGroupName2;
	}

	public void setOptionGroupName2(String optionGroupName2) {
		this.optionGroupName2 = optionGroupName2;
	}

	public String getOptionGroupName3() {
		return optionGroupName3;
	}

	public void setOptionGroupName3(String optionGroupName3) {
		this.optionGroupName3 = optionGroupName3;
	}

	public String getOptionGroupName4() {
		return optionGroupName4;
	}

	public void setOptionGroupName4(String optionGroupName4) {
		this.optionGroupName4 = optionGroupName4;
	}

	@Override
	public String toString() {
		return "OptionCombinationGroupNames [optionGroupName1=" + optionGroupName1 + ", optionGroupName2=" + optionGroupName2 + ", optionGroupName3=" + optionGroupName3 + ", optionGroupName4=" + optionGroupName4 + "]";
	}

}
