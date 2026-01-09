package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class DisplayCategoriesList {
	
	// 카테고리 목록
	@JsonProperty("Data")
	private List<DisplayCategories> displayCategories;

	public List<DisplayCategories> getDisplayCategories() {
		return displayCategories;
	}

	public void setDisplayCategories(List<DisplayCategories> displayCategories) {
		this.displayCategories = displayCategories;
	}

	@Override
	public String toString() {
		return "DisplayCategoriesList [displayCategories=" + displayCategories + "]";
	}

}
