package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class CancelList {

	private String ticketId;
	private String state;
	private String auditorGroup;
	private String reason;
	private long createdAtMillis;
	private long confirmedAtMillis;
	private long withdrawnAtMillis;
	private long rejectedAtMillis;
	private String rejectReason;
	private long resolvedAtMillis;
	private boolean isCustomerNegligence;
	private ResolvedPriceDelta resolvedPriceDelta;
	private List<RefundPaymentInfo> refundPaymentInfos;
	private List<OrderItem> orderItems;
	private String channel;
	private Order linkedOrder;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAuditorGroup() {
		return auditorGroup;
	}

	public void setAuditorGroup(String auditorGroup) {
		this.auditorGroup = auditorGroup;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getCreatedAtMillis() {
		return createdAtMillis;
	}

	public void setCreatedAtMillis(long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}

	public long getConfirmedAtMillis() {
		return confirmedAtMillis;
	}

	public void setConfirmedAtMillis(long confirmedAtMillis) {
		this.confirmedAtMillis = confirmedAtMillis;
	}

	public long getWithdrawnAtMillis() {
		return withdrawnAtMillis;
	}

	public void setWithdrawnAtMillis(long withdrawnAtMillis) {
		this.withdrawnAtMillis = withdrawnAtMillis;
	}

	public long getRejectedAtMillis() {
		return rejectedAtMillis;
	}

	public void setRejectedAtMillis(long rejectedAtMillis) {
		this.rejectedAtMillis = rejectedAtMillis;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public long getResolvedAtMillis() {
		return resolvedAtMillis;
	}

	public void setResolvedAtMillis(long resolvedAtMillis) {
		this.resolvedAtMillis = resolvedAtMillis;
	}

	public ResolvedPriceDelta getResolvedPriceDelta() {
		return resolvedPriceDelta;
	}

	public void setResolvedPriceDelta(ResolvedPriceDelta resolvedPriceDelta) {
		this.resolvedPriceDelta = resolvedPriceDelta;
	}

	public List<RefundPaymentInfo> getRefundPaymentInfos() {
		return refundPaymentInfos;
	}

	public void setRefundPaymentInfos(List<RefundPaymentInfo> refundPaymentInfos) {
		this.refundPaymentInfos = refundPaymentInfos;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public boolean isCustomerNegligence() {
		return isCustomerNegligence;
	}

	public void setCustomerNegligence(boolean isCustomerNegligence) {
		this.isCustomerNegligence = isCustomerNegligence;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Order getLinkedOrder() {
		return linkedOrder;
	}

	public void setLinkedOrder(Order linkedOrder) {
		this.linkedOrder = linkedOrder;
	}

}
