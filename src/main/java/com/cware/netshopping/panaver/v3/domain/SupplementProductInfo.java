package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SupplementProductInfo {
	
	private String sortType;	
	private List<SupplementProducts> supplementProducts;	
	
	
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public List<SupplementProducts> getSupplementProducts() {
		return supplementProducts;
	}
	public void setSupplementProducts(List<SupplementProducts> supplementProducts) {
		this.supplementProducts = supplementProducts;
	}
	
	@Override
	public String toString() {
		return "SupplementProductInfo [sortType=" + sortType +  "supplementProducts=" + supplementProducts + "]";
	}
}
