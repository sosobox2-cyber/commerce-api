package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DiscountDutyAmount {
	 private Long sellerAmount;
	 private Long queenitAmount;
	 
	public Long getSellerAmount() {
		return sellerAmount;
	}
	public void setSellerAmount(Long sellerAmount) {
		this.sellerAmount = sellerAmount;
	}
	public Long getQueenitAmount() {
		return queenitAmount;
	}
	public void setQueenitAmount(Long queenitAmount) {
		this.queenitAmount = queenitAmount;
	}
}
