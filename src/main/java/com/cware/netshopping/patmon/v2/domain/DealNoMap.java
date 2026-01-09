package com.cware.netshopping.patmon.v2.domain;

import java.util.Map;

public class DealNoMap {
	String vendorId;
	Map<String, Long> dealNo; // 연동딜번호: 티몬딜번호
	Map<String, Long> dealOptionNos; // 연동딜옵션번호: 티몬딜옵션번호
	String tmonDealNo; // 티몬딜번호

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public Map<String, Long> getDealNo() {
		return dealNo;
	}

	public void setDealNo(Map<String, Long> dealNo) {
		this.dealNo = dealNo;
	}

	public Map<String, Long> getDealOptionNos() {
		return dealOptionNos;
	}

	public void setDealOptionNos(Map<String, Long> dealOptionNos) {
		this.dealOptionNos = dealOptionNos;
	}

	public String getTmonDealNo() {
		return tmonDealNo;
	}

	public void setTmonDealNo(String tmonDealNo) {
		this.tmonDealNo = tmonDealNo;
	}

	@Override
	public String toString() {
		return "DealNoMap [vendorId=" + vendorId + ", dealNo=" + dealNo + ", dealOptionNos=" + dealOptionNos + "]";
	}

}
