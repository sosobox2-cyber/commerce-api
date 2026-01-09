package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DutyRatio {
	private Long queenitDutyRatio;
	private Long sellerDutyRatio;
	
	public Long getQueenitDutyRatio() {
		return queenitDutyRatio;
	}
	public void setQueenitDutyRatio(Long queenitDutyRatio) {
		this.queenitDutyRatio = queenitDutyRatio;
	}
	public Long getSellerDutyRatio() {
		return sellerDutyRatio;
	}
	public void setSellerDutyRatio(Long sellerDutyRatio) {
		this.sellerDutyRatio = sellerDutyRatio;
	}
}
