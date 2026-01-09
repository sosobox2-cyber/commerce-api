package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ReturnEstimationResult {
	 private long totalProductPrice;
     private long totalDeliveryPrice;
     private long serviceCharge;
     private long discountAmount;
     private long finalPurchaseAmount;
     private long usePoint;
     private List<EachEstimations> eachEstimations;
     private PriceDelta priceDelta;
     
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
	public List<EachEstimations> getEachEstimations() {
		return eachEstimations;
	}
	public void setEachEstimations(List<EachEstimations> eachEstimations) {
		this.eachEstimations = eachEstimations;
	}
	public PriceDelta getPriceDelta() {
		return priceDelta;
	}
	public void setPriceDelta(PriceDelta priceDelta) {
		this.priceDelta = priceDelta;
	}
	
     
}
