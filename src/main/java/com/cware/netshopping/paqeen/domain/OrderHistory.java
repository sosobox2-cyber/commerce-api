package com.cware.netshopping.paqeen.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class OrderHistory {
	
	private String ordererId;
	private String ordererPhoneNumber;
	private OrderSummary orderSummary;
	
	public String getOrdererId() {
		return ordererId;
	}
	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}
	public String getOrdererPhoneNumber() {
		return ordererPhoneNumber;
	}
	public void setOrdererPhoneNumber(String ordererPhoneNumber) {
		this.ordererPhoneNumber = ordererPhoneNumber;
	}
	public OrderSummary getOrderSummary() {
		return orderSummary;
	}
	public void setOrderSummary(OrderSummary orderSummary) {
		this.orderSummary = orderSummary;
	}
	
	
	
}
