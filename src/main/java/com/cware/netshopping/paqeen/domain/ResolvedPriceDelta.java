package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ResolvedPriceDelta {
    private Long totalProductPrice;
    private Long totalDeliveryPrice;
    private Long serviceCharge;
    private Long discountAmount;
    private Long finalPurchaseAmount;
    private Long usePoint;
    private List<CostElement> costElements;
    
	public Long getTotalProductPrice() {
		return totalProductPrice;
	}
	public void setTotalProductPrice(Long totalProductPrice) {
		this.totalProductPrice = totalProductPrice;
	}
	public Long getTotalDeliveryPrice() {
		return totalDeliveryPrice;
	}
	public void setTotalDeliveryPrice(Long totalDeliveryPrice) {
		this.totalDeliveryPrice = totalDeliveryPrice;
	}
	public Long getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(Long serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public Long getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Long discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Long getFinalPurchaseAmount() {
		return finalPurchaseAmount;
	}
	public void setFinalPurchaseAmount(Long finalPurchaseAmount) {
		this.finalPurchaseAmount = finalPurchaseAmount;
	}
	public Long getUsePoint() {
		return usePoint;
	}
	public void setUsePoint(Long usePoint) {
		this.usePoint = usePoint;
	}
	public List<CostElement> getCostElements() {
		return costElements;
	}
	public void setCostElements(List<CostElement> costElements) {
		this.costElements = costElements;
	}
	
}
