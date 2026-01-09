package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class BrandsList {
	
	// 브랜드 목록
	private List<Brands> brands;
	private List<Brands> failureBrands;

	public List<Brands> getFailureBrands() {
		return failureBrands;
	}

	public void setFailureBrands(List<Brands> failureBrands) {
		this.failureBrands = failureBrands;
	}

	public List<Brands> getBrands() {
		return brands;
	}

	public void setBrands(List<Brands> brands) {
		this.brands = brands;
	}

	@Override
	public String toString() {
		return "BrandsList [brands=" + brands + ", failureBrands=" + failureBrands + "]";
	}


	

}
