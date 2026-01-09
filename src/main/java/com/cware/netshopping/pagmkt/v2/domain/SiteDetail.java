package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SiteDetail {

	private SiteProduct gmkt;

	private SiteProduct iac;

	public SiteProduct getGmkt() {
		return gmkt;
	}

	public void setGmkt(SiteProduct gmkt) {
		this.gmkt = gmkt;
	}

	public SiteProduct getIac() {
		return iac;
	}

	public void setIac(SiteProduct iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "SiteDetail [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
