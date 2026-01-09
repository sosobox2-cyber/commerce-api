package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class DeliveryFee {
	private RecallFee recallFee;
	private DeliverFee deliverFee;
	
	public RecallFee getRecallFee() {
		return recallFee;
	}
	public void setRecallFee(RecallFee recallFee) {
		this.recallFee = recallFee;
	}
	public DeliverFee getDeliverFee() {
		return deliverFee;
	}
	public void setDeliverFee(DeliverFee deliverFee) {
		this.deliverFee = deliverFee;
	}
}
