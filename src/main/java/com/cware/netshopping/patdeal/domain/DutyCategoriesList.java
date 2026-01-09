package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class DutyCategoriesList {
	
	// 카테고리 목록
	@JsonProperty("Data")
	private List<DutyCategories> dutyCategories;

	public List<DutyCategories> getDutyCategories() {
		return dutyCategories;
	}

	public void setDutyCategories(List<DutyCategories> dutyCategories) {
		this.dutyCategories = dutyCategories;
	}

	@Override
	public String toString() {
		return "DutyCategoriesList [dutyCategories=" + dutyCategories + "]";
	}

}
