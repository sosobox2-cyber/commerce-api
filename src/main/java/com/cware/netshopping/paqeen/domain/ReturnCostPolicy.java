package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ReturnCostPolicy {
	private Long defaultCost;
	private Long jejuIsland;
	private Long backCountry;
	
	public Long getDefaultCost() {
		return defaultCost;
	}
	public void setDefaultCost(Long defaultCost) {
		this.defaultCost = defaultCost;
	}
	public Long getJejuIsland() {
		return jejuIsland;
	}
	public void setJejuIsland(Long jejuIsland) {
		this.jejuIsland = jejuIsland;
	}
	public Long getBackCountry() {
		return backCountry;
	}
	public void setBackCountry(Long backCountry) {
		this.backCountry = backCountry;
	}
}
