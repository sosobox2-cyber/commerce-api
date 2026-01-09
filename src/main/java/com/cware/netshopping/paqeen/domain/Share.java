package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Share {
	private long sellerShare;
	private long sellerDutyDiscount;
	private long queenitShare;
	private long queenitDutyDiscount;
	public long getSellerShare() {
		return sellerShare;
	}
	public void setSellerShare(long sellerShare) {
		this.sellerShare = sellerShare;
	}
	public long getSellerDutyDiscount() {
		return sellerDutyDiscount;
	}
	public void setSellerDutyDiscount(long sellerDutyDiscount) {
		this.sellerDutyDiscount = sellerDutyDiscount;
	}
	public long getQueenitShare() {
		return queenitShare;
	}
	public void setQueenitShare(long queenitShare) {
		this.queenitShare = queenitShare;
	}
	public long getQueenitDutyDiscount() {
		return queenitDutyDiscount;
	}
	public void setQueenitDutyDiscount(long queenitDutyDiscount) {
		this.queenitDutyDiscount = queenitDutyDiscount;
	}
}
