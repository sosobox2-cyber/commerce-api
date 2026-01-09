package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipPolicyUpdate {
	
	private ShippingPolicy shippingPolicy;
	
	public ShippingPolicy getShippingPolicy() {
		return shippingPolicy;
	}
	public void setShippingPolicy(ShippingPolicy shippingPolicy) {
		this.shippingPolicy = shippingPolicy;
	}

	
	
}
