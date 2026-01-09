package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SiteIntValue {
	private Integer gmkt;

	private Integer iac;

	@JsonProperty("Gmkt")
	public Integer getGmkt() {
		return gmkt;
	}

	@JsonProperty("Gmkt")
	public void setGmkt(Integer gmkt) {
		this.gmkt = gmkt;
	}

	@JsonProperty("Iac")
	public Integer getIac() {
		return iac;
	}

	@JsonProperty("Iac")
	public void setIac(Integer iac) {
		this.iac = iac;
	}

	@Override
	public String toString() {
		return "SiteIntValue [gmkt=" + gmkt + ", iac=" + iac + "]";
	}

}
