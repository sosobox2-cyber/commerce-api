package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DeliveryPolicy {
	private Long defaultPrice;
	private Long defaultReturnCost;
	private ShippingCostPolicy shippingCostPolicy;
	private ReturnCostPolicy returnCostPolicy;
	private LogisticsPolicy logisticsPolicy;
	private Long requiredAmountForFree;
	
	public Long getDefaultPrice() {
		return defaultPrice;
	}
	public void setDefaultPrice(Long defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	public Long getDefaultReturnCost() {
		return defaultReturnCost;
	}
	public void setDefaultReturnCost(Long defaultReturnCost) {
		this.defaultReturnCost = defaultReturnCost;
	}
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
	public Long getRequiredAmountForFree() {
		return requiredAmountForFree;
	}
	public void setRequiredAmountForFree(Long requiredAmountForFree) {
		this.requiredAmountForFree = requiredAmountForFree;
	}
	
}
