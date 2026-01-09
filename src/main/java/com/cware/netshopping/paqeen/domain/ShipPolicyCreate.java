package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipPolicyCreate {
	
	private String name;
	private boolean assorted;
	private List<String> brandCodes;
	private ShippingPolicy shippingPolicy;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAssorted() {
		return assorted;
	}
	public void setAssorted(boolean assorted) {
		this.assorted = assorted;
	}
	public List<String> getBrandCodes() {
		return brandCodes;
	}
	public void setBrandCodes(List<String> brandCodes) {
		this.brandCodes = brandCodes;
	}
	public ShippingPolicy getShippingPolicy() {
		return shippingPolicy;
	}
	public void setShippingPolicy(ShippingPolicy shippingPolicy) {
		this.shippingPolicy = shippingPolicy;
	}

	
	
}
