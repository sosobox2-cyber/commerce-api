package com.cware.netshopping.paqeen.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingPolicy {
	
	private ShippingCostPolicy shippingCostPolicy;
	private ReturnCostPolicy returnCostPolicy;
	private LogisticsPolicy logisticsPolicy;
	
	public ShippingCostPolicy getShippingCostPolicy() {
		return shippingCostPolicy;
	}
	public void setShippingCostPolicy(ShippingCostPolicy shippingCostPolicy) {
		this.shippingCostPolicy = shippingCostPolicy;
	}
	public ReturnCostPolicy getReturnCostPolicy() {
		return returnCostPolicy;
	}
	public void setReturnCostPolicy(ReturnCostPolicy returnCostPolicy) {
		this.returnCostPolicy = returnCostPolicy;
	}
	public LogisticsPolicy getLogisticsPolicy() {
		return logisticsPolicy;
	}
	public void setLogisticsPolicy(LogisticsPolicy logisticsPolicy) {
		this.logisticsPolicy = logisticsPolicy;
	}

	
	
}
