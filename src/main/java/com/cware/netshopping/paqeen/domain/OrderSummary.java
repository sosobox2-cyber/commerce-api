package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class OrderSummary {
	
	private String orderId;
    private List<ItemGroupForSeller> itemGroupsForSeller;
    private List<DiscountApplication> discountApplications;
    private long confirmedAtMillis;
    private long createdAtMillis;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<ItemGroupForSeller> getItemGroupsForSeller() {
		return itemGroupsForSeller;
	}
	public void setItemGroupsForSeller(List<ItemGroupForSeller> itemGroupsForSeller) {
		this.itemGroupsForSeller = itemGroupsForSeller;
	}
	public List<DiscountApplication> getDiscountApplications() {
		return discountApplications;
	}
	public void setDiscountApplications(List<DiscountApplication> discountApplications) {
		this.discountApplications = discountApplications;
	}
	public long getConfirmedAtMillis() {
		return confirmedAtMillis;
	}
	public void setConfirmedAtMillis(long confirmedAtMillis) {
		this.confirmedAtMillis = confirmedAtMillis;
	}
	public long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
}
