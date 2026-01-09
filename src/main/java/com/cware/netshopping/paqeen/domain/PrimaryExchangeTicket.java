package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PrimaryExchangeTicket {
    private String ticketId;
    private String orderLineId;
    private boolean requiredDeposit;
    private Reason reason;
    private String processState;
    private List<ExchangeOption> exchangeOptions;
    private String lastProcessReason;
    private Long createdAtMillis;
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	public boolean isRequiredDeposit() {
		return requiredDeposit;
	}
	public void setRequiredDeposit(boolean requiredDeposit) {
		this.requiredDeposit = requiredDeposit;
	}
	public Reason getReason() {
		return reason;
	}
	public void setReason(Reason reason) {
		this.reason = reason;
	}
	public String getProcessState() {
		return processState;
	}
	public void setProcessState(String processState) {
		this.processState = processState;
	}
	public List<ExchangeOption> getExchangeOptions() {
		return exchangeOptions;
	}
	public void setExchangeOptions(List<ExchangeOption> exchangeOptions) {
		this.exchangeOptions = exchangeOptions;
	}
	public String getLastProcessReason() {
		return lastProcessReason;
	}
	public void setLastProcessReason(String lastProcessReason) {
		this.lastProcessReason = lastProcessReason;
	}
	public Long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(Long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
}
