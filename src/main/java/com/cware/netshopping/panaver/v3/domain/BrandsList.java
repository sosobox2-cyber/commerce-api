package com.cware.netshopping.panaver.v3.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BrandsList {

	// 브랜드 목록
	@JsonProperty("Data")
	private List<Brands> brandsList;	
	
	public List<Brands> getBrandsList() {
		return brandsList;
	}

	public void setBrandsList(List<Brands> brandsList) {
		this.brandsList = brandsList;
	}

	@Override
	public String toString() {
		return "Brands [Brands=" + brandsList +"]";
	}

}
