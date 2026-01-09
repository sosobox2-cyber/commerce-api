package com.cware.netshopping.pafaple.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Brand {

	@JsonProperty("PartnerLoginID")
	private String PartnerLoginID;
	
	@JsonProperty("BrandGrp")
	private Integer BrandGrp;
	
	@JsonProperty("BrandList")
	private List<BrandList> BrandList;

	@JsonProperty("PartnerLoginID")
	public String getPartnerLoginID() {
		return PartnerLoginID;
	}

	public void setPartnerLoginID(String partnerLoginID) {
		PartnerLoginID = partnerLoginID;
	}
	@JsonProperty("BrandGrp")
	public Integer getBrandGrp() {
		return BrandGrp;
	}

	public void setBrandGrp(Integer brandGrp) {
		BrandGrp = brandGrp;
	}
	@JsonProperty("BrandList")
	public List<BrandList> getBrandList() {
		return BrandList;
	}

	public void setBrandList(List<BrandList> brandList) {
		BrandList = brandList;
	}
	
	

}
