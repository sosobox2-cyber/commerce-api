package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Qty {
	private Integer gmkt; // G마켓
	private Integer iac; // 옥션

	public Qty(Integer stock) {
		this.gmkt = stock;
		this.iac = stock;
	}

	public Integer getGmkt() {
		return gmkt;
	}

	public void setGmkt(Integer gmkt) {
		this.gmkt = gmkt;
	}

	public Integer getIac() {
		return iac;
	}

	public void setIac(Integer iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "Qty [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
