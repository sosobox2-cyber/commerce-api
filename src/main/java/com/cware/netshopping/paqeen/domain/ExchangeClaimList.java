package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeClaimList {

	private long requestedAtMillis;
	private String ticketId;
	private String groupId;
	private String orderId;
	private String uid;
	private String phoneNumber;
	private DeliveryRequest deliveryRequest;
	private ExchangeDeliveryFee exchangeDeliveryFee;
	private ImposedExchangeFee imposedExchangeFee;
	private String processState;
	private boolean requiredDeposit;
	private String lastProcessReason;
	private DeliveryProgress deliveryProgress;
	private String rejectedInspectAfterProcessType;
	private String note;
	private List<TicketItem> ticketItems;
	private Long updatedAtMillis;

	public long getRequestedAtMillis() {
		return requestedAtMillis;
	}

	public void setRequestedAtMillis(long requestedAtMillis) {
		this.requestedAtMillis = requestedAtMillis;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	public ExchangeDeliveryFee getExchangeDeliveryFee() {
		return exchangeDeliveryFee;
	}

	public void setExchangeDeliveryFee(ExchangeDeliveryFee exchangeDeliveryFee) {
		this.exchangeDeliveryFee = exchangeDeliveryFee;
	}

	public ImposedExchangeFee getImposedExchangeFee() {
		return imposedExchangeFee;
	}

	public void setImposedExchangeFee(ImposedExchangeFee imposedExchangeFee) {
		this.imposedExchangeFee = imposedExchangeFee;
	}

	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}

	public boolean isRequiredDeposit() {
		return requiredDeposit;
	}

	public void setRequiredDeposit(boolean requiredDeposit) {
		this.requiredDeposit = requiredDeposit;
	}

	public String getLastProcessReason() {
		return lastProcessReason;
	}

	public void setLastProcessReason(String lastProcessReason) {
		this.lastProcessReason = lastProcessReason;
	}

	public DeliveryProgress getDeliveryProgress() {
		return deliveryProgress;
	}

	public void setDeliveryProgress(DeliveryProgress deliveryProgress) {
		this.deliveryProgress = deliveryProgress;
	}

	public String getRejectedInspectAfterProcessType() {
		return rejectedInspectAfterProcessType;
	}

	public void setRejectedInspectAfterProcessType(String rejectedInspectAfterProcessType) {
		this.rejectedInspectAfterProcessType = rejectedInspectAfterProcessType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<TicketItem> getTicketItems() {
		return ticketItems;
	}

	public void setTicketItems(List<TicketItem> ticketItems) {
		this.ticketItems = ticketItems;
	}

	public Long getUpdatedAtMillis() {
		return updatedAtMillis;
	}

	public void setUpdatedAtMillis(Long updatedAtMillis) {
		this.updatedAtMillis = updatedAtMillis;
	}

}
