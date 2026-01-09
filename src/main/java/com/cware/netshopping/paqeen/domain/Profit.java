package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Profit {
	 private long commissionTarget;
     private long commission;
     private long sellerProfit;
     private long queenitProfit;
	public long getCommissionTarget() {
		return commissionTarget;
	}
	public void setCommissionTarget(long commissionTarget) {
		this.commissionTarget = commissionTarget;
	}
	public long getCommission() {
		return commission;
	}
	public void setCommission(long commission) {
		this.commission = commission;
	}
	public long getSellerProfit() {
		return sellerProfit;
	}
	public void setSellerProfit(long sellerProfit) {
		this.sellerProfit = sellerProfit;
	}
	public long getQueenitProfit() {
		return queenitProfit;
	}
	public void setQueenitProfit(long queenitProfit) {
		this.queenitProfit = queenitProfit;
	}
     
     
}
