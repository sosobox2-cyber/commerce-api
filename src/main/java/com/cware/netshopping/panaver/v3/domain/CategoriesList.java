package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class CategoriesList {
	
	// 카테고리 목록
	@JsonProperty("Data")
	private List<Categories> categories;	
	
	public List<Categories> getCategories() {
		return categories;
	}

	public void setCategories(List<Categories> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "CategoriesList [categories=" + categories + "]";
	}

}
