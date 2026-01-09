package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SiteValue {
	private Boolean gmkt; // G마켓
	private Boolean iac; // 옥션

	public Boolean isGmkt() {
		return gmkt;
	}

	public void setGmkt(Boolean gmkt) {
		this.gmkt = gmkt;
	}

	public Boolean isIac() {
		return iac;
	}

	public void setIac(Boolean iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "SiteValue [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
