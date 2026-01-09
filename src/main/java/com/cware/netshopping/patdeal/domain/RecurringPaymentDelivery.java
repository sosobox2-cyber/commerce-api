package com.cware.netshopping.patdeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecurringPaymentDelivery {
	
	private String cycleType ;
	private long cycle;
	private long date;
	
	public String getCycleType() {
		return cycleType;
	}
	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}
	public long getCycle() {
		return cycle;
	}
	public void setCycle(long cycle) {
		this.cycle = cycle;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "RecurringPaymentDelivery [cycleType=" + cycleType + ", cycle=" + cycle + ", date=" + date + "]";
	}

}
