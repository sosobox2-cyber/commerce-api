package com.cware.netshopping.patmon.v2.domain;

public class DealUpdateResult {
	ResultData deal;
	ResultData dealOptions;

	public ResultData getDeal() {
		return deal;
	}

	public void setDeal(ResultData deal) {
		this.deal = deal;
	}

	public ResultData getDealOptions() {
		return dealOptions;
	}

	public void setDealOptions(ResultData dealOptions) {
		this.dealOptions = dealOptions;
	}

	@Override
	public String toString() {
		return "DealUpdateResult [deal=" + deal + ", dealOptions=" + dealOptions + "]";
	}

}
