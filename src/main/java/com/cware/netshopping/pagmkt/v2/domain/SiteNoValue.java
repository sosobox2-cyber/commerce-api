package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SiteNoValue {
	private Integer gmkt;
	private Integer iac;

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
		return "SiteNoValue [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
