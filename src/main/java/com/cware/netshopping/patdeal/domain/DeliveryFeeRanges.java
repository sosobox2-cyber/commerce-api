package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DeliveryFeeRanges {
	
	// 해당 구간에서의 배송비
	private String deliveryAmt;
	// ~미만
	private long below;
	// ~이상
	private long aboveOrEqual;
	
	public String getDeliveryAmt() {
		return deliveryAmt;
	}
	public void setDeliveryAmt(String deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
	}
	public long getBelow() {
		return below;
	}
	public void setBelow(long below) {
		this.below = below;
	}
	public long getAboveOrEqual() {
		return aboveOrEqual;
	}
	public void setAboveOrEqual(long aboveOrEqual) {
		this.aboveOrEqual = aboveOrEqual;
	}
	
	@Override
	public String toString() {
		return "DeliveryFeeRanges [deliveryAmt=" + deliveryAmt + ", below=" + below + ", aboveOrEqual=" + aboveOrEqual
				+ "]";
	}
	
}
