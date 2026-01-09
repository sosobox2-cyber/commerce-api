package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayProducts {
	
	private String productNo;
	private String optionNo;
	private long orderCnt;
	private RecurringPaymentDelivery recurringPaymentDelivery; 
	private List<Object> rentalInfos; //무슨 정보 주는지 몰라서...
	private List<Object> optionInputs;
	
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(String optionNo) {
		this.optionNo = optionNo;
	}
	public long getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(long orderCnt) {
		this.orderCnt = orderCnt;
	}
	public RecurringPaymentDelivery getRecurringPaymentDelivery() {
		return recurringPaymentDelivery;
	}
	public void setRecurringPaymentDelivery(RecurringPaymentDelivery recurringPaymentDelivery) {
		this.recurringPaymentDelivery = recurringPaymentDelivery;
	}
	public List<Object> getRentalInfos() {
		return rentalInfos;
	}
	public void setRentalInfos(List<Object> rentalInfos) {
		this.rentalInfos = rentalInfos;
	}
	public List<Object> getOptionInputs() {
		return optionInputs;
	}
	public void setOptionInputs(List<Object> optionInputs) {
		this.optionInputs = optionInputs;
	}
	@Override
	public String toString() {
		return "PayProducts [productNo=" + productNo + ", optionNo=" + optionNo + ", orderCnt=" + orderCnt
				+ ", recurringPaymentDelivery=" + recurringPaymentDelivery + ", rentalInfos=" + rentalInfos
				+ ", optionInputs=" + optionInputs + "]";
	} 
	

	
	

	
}
