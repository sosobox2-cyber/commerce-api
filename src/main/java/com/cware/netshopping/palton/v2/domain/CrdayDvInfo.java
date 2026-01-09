package com.cware.netshopping.palton.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CrdayDvInfo {
	// 주문마감시간 [HH24MI ex) 1000]
	// 당일배송가능여부가 Y인 경우 필수값
	String odCloseTm;

	public String getOdCloseTm() {
		return odCloseTm;
	}

	public void setOdCloseTm(String odCloseTm) {
		this.odCloseTm = odCloseTm;
	}

	@Override
	public String toString() {
		return "CrdayDvInfo [odCloseTm=" + odCloseTm + "]";
	}

}
