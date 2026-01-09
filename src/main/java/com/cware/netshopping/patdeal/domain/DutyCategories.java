package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DutyCategories {
	
	// 고시 항목명
	private String dutyCategoryName;
	// 고시 항목 번호
	private String dutyCategoryNo;
	// 고시 항목 설명
	private String dutyCategoryDescription;
	
	private List<DutyCategoryContents> dutyCategoryContents;

	public String getDutyCategoryName() {
		return dutyCategoryName;
	}

	public void setDutyCategoryName(String dutyCategoryName) {
		this.dutyCategoryName = dutyCategoryName;
	}

	public String getDutyCategoryNo() {
		return dutyCategoryNo;
	}

	public void setDutyCategoryNo(String dutyCategoryNo) {
		this.dutyCategoryNo = dutyCategoryNo;
	}

	public String getDutyCategoryDescription() {
		return dutyCategoryDescription;
	}

	public void setDutyCategoryDescription(String dutyCategoryDescription) {
		this.dutyCategoryDescription = dutyCategoryDescription;
	}

	public List<DutyCategoryContents> getDutyCategoryContents() {
		return dutyCategoryContents;
	}

	public void setDutyCategoryContents(List<DutyCategoryContents> dutyCategoryContents) {
		this.dutyCategoryContents = dutyCategoryContents;
	}

	@Override
	public String toString() {
		return "DutyCategories [dutyCategoryName=" + dutyCategoryName + ", dutyCategoryNo=" + dutyCategoryNo
				+ ", dutyCategoryDescription=" + dutyCategoryDescription + ", dutyCategoryContents="
				+ dutyCategoryContents + "]";
	}
	
}
