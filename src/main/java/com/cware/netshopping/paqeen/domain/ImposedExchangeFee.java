package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class ImposedExchangeFee {
	private DeliveryFee deliveryFee;
	private ImposedExchangeFeeToUser imposedExchangeFeeToUser;
	private ImposedExchangeFeeToTicket imposedExchangeFeeToTicket;
	private Boolean isAppliedExchangeDeliveryFeeBenefit;
	
	public DeliveryFee getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(DeliveryFee deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public ImposedExchangeFeeToUser getImposedExchangeFeeToUser() {
		return imposedExchangeFeeToUser;
	}
	public void setImposedExchangeFeeToUser(ImposedExchangeFeeToUser imposedExchangeFeeToUser) {
		this.imposedExchangeFeeToUser = imposedExchangeFeeToUser;
	}
	public ImposedExchangeFeeToTicket getImposedExchangeFeeToTicket() {
		return imposedExchangeFeeToTicket;
	}
	public void setImposedExchangeFeeToTicket(ImposedExchangeFeeToTicket imposedExchangeFeeToTicket) {
		this.imposedExchangeFeeToTicket = imposedExchangeFeeToTicket;
	}
	public Boolean getIsAppliedExchangeDeliveryFeeBenefit() {
		return isAppliedExchangeDeliveryFeeBenefit;
	}
	public void setIsAppliedExchangeDeliveryFeeBenefit(Boolean isAppliedExchangeDeliveryFeeBenefit) {
		this.isAppliedExchangeDeliveryFeeBenefit = isAppliedExchangeDeliveryFeeBenefit;
	}
	
}

