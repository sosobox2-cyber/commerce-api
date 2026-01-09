package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AreaFeeDetail {
	
	// 지역번호
	private String areaNo;
	// 추가배송비
	private double extraDeliveryAmt;
	
	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public double getExtraDeliveryAmt() {
		return extraDeliveryAmt;
	}
	public void setExtraDeliveryAmt(double extraDeliveryAmt) {
		this.extraDeliveryAmt = extraDeliveryAmt;
	}
	
	@Override
	public String toString() {
		return "FeeDetail [areaNo=" + areaNo + ", extraDeliveryAmt=" + extraDeliveryAmt + "]";
	}
	
}
