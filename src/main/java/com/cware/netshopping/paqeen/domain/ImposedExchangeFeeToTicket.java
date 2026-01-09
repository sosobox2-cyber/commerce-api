package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ImposedExchangeFeeToTicket {
	private DeliveryFee deliveryFee;

	public DeliveryFee getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(DeliveryFee deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
}
