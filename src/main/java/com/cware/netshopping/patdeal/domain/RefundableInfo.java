package com.cware.netshopping.patdeal.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RefundableInfo {
	
	// 환불 불가능 항목 - [CANCEL, RETURN, EXCHANGE]
	private List<Object> nonRefundableInfo;
	// 환불가능 여부
	private String refundableYn;
	
	public List<Object> getNonRefundableInfo() {
		return nonRefundableInfo;
	}
	public void setNonRefundableInfo(List<Object> nonRefundableInfo) {
		this.nonRefundableInfo = nonRefundableInfo;
	}
	public String getRefundableYn() {
		return refundableYn;
	}
	public void setRefundableYn(String refundableYn) {
		this.refundableYn = refundableYn;
	}
	
	@Override
	public String toString() {
		return "RefundableInfo [nonRefundableInfo=" + nonRefundableInfo + ", refundableYn=" + refundableYn + "]";
	}
	
}
