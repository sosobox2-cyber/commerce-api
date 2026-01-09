package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Areafees {
	
	// 국가코드
	private String countryCd;
	// 지역별 추가배송비 명
	private String name;
	// 지역별 추가배송비 상세
	@JsonProperty("details")
	private List<AreaFeeDetail> detailList;
	
	public String getCountryCd() {
		return countryCd;
	}
	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AreaFeeDetail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<AreaFeeDetail> detailList) {
		this.detailList = detailList;
	}
	
	@Override
	public String toString() {
		return "Areafees [countryCd=" + countryCd + ", name=" + name + ", detailList=" + detailList + "]";
	}
	
}
