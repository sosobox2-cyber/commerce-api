package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class AreasList {
	
	// 배송비 지역 목록
	@JsonProperty("Data")
	private List<Areas> areas;

	public List<Areas> getAreas() {
		return areas;
	}

	public void setAreas(List<Areas> areas) {
		this.areas = areas;
	}

	@Override
	public String toString() {
		return "AreasList [areas=" + areas + "]";
	}

}
