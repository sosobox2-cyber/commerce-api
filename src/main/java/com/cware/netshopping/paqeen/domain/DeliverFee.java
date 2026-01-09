package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DeliverFee {
	private Long normal;
	private Long jeju;
	private Long backCountry;
	
	public Long getNormal() {
		return normal;
	}
	public void setNormal(Long normal) {
		this.normal = normal;
	}
	public Long getJeju() {
		return jeju;
	}
	public void setJeju(Long jeju) {
		this.jeju = jeju;
	}
	public Long getBackCountry() {
		return backCountry;
	}
	public void setBackCountry(Long backCountry) {
		this.backCountry = backCountry;
	}
	
}
