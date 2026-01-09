package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Base {
   private String orderLineGroupId;
   private String lowestBaseDate;
   private long  commissionPercentage;
public String getOrderLineGroupId() {
	return orderLineGroupId;
}
public void setOrderLineGroupId(String orderLineGroupId) {
	this.orderLineGroupId = orderLineGroupId;
}
public String getLowestBaseDate() {
	return lowestBaseDate;
}
public void setLowestBaseDate(String lowestBaseDate) {
	this.lowestBaseDate = lowestBaseDate;
}
public long getCommissionPercentage() {
	return commissionPercentage;
}
public void setCommissionPercentage(long commissionPercentage) {
	this.commissionPercentage = commissionPercentage;
}
   
   
}
