package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class CategoryOfferList {
	// 정보고시 목록
	@JsonProperty("Data")
	private List<CategoryOffer> categoryOffer;
	

	public List<CategoryOffer> getCategoryOffer() {
		return categoryOffer;
	}


	public void setCategoryOffer(List<CategoryOffer> categoryOffer) {
		this.categoryOffer = categoryOffer;
	}


	@Override
	public String toString() {
		return "CategoryOfferList [announcements=" + categoryOffer + "]";
	}
}
