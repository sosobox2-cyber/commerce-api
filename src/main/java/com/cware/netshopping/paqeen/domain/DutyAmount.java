package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DutyAmount {
	private Integer sellerAmount;
	private Integer queenitAmount;
	public Integer getSellerAmount() {
		return sellerAmount;
	}
	public void setSellerAmount(Integer sellerAmount) {
		this.sellerAmount = sellerAmount;
	}
	public Integer getQueenitAmount() {
		return queenitAmount;
	}
	public void setQueenitAmount(Integer queenitAmount) {
		this.queenitAmount = queenitAmount;
	}
}
