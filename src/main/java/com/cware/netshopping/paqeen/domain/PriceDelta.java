package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PriceDelta {
	 private long totalProductPrice;
     private long totalDeliveryPrice;
     private long serviceCharge;
     private long discountAmount;
     private long finalPurchaseAmount;
     private long usePoint;
     private List<CostElements> costElements;
     
	public long getTotalProductPrice() {
		return totalProductPrice;
	}
	public void setTotalProductPrice(long totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}
	public long getTotalDeliveryPrice() {
		return totalDeliveryPrice;
	}
	public void setTotalDeliveryPrice(long totalDeliveryPrice) {
		this.totalDeliveryPrice = totalDeliveryPrice;
	}
	public long getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(long serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public long getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(long discountAmount) {
		this.discountAmount = discountAmount;
	}
	public long getFinalPurchaseAmount() {
		return finalPurchaseAmount;
	}
	public void setFinalPurchaseAmount(long finalPurchaseAmount) {
		this.finalPurchaseAmount = finalPurchaseAmount;
	}
	public long getUsePoint() {
		return usePoint;
	}
	public void setUsePoint(long usePoint) {
		this.usePoint = usePoint;
	}
	public List<CostElements> getCostElements() {
		return costElements;
	}
	public void setCostElements(List<CostElements> costElements) {
		this.costElements = costElements;
	}
	
     
}
