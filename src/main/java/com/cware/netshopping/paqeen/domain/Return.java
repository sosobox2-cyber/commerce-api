package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class Return {

	private String ticketId;
	private ReturnAddress returnAddress;
	private String state;
	private String returnState;
	private String reason;
	private long createdAtMillis;
	private long confirmedAtMillis;
	private long withdrawnAtMillis;
	private long rejectedAtMillis;
	private String rejectReason;
	private long resolvedAtMillis;
	private ResolvedPriceDelta resolvedPriceDelta;
	private List<RefundPaymentInfo> refundPaymentInfos;
	private List<OrderItem> orderItems;
	private Boolean isCustomerNegligence;
	private String channel;
	private Order linkedOrder;
	private ReturnEstimationResult returnEstimationResult;
	private String auditorGroup;
	

    // Getter 및 Setter 추가
    
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

	public Boolean getIsCustomerNegligence() {
        return isCustomerNegligence;
    }

    public void setIsCustomerNegligence(Boolean isCustomerNegligence) {
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

	public ReturnAddress getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(ReturnAddress returnAddress) {
		this.returnAddress = returnAddress;
	}

	public String getReturnState() {
		return returnState;
	}

	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}

	public ReturnEstimationResult getReturnEstimationResult() {
		return returnEstimationResult;
	}

	public void setReturnEstimationResult(ReturnEstimationResult returnEstimationResult) {
		this.returnEstimationResult = returnEstimationResult;
	}

	public String getAuditorGroup() {
		return auditorGroup;
	}

	public void setAuditorGroup(String auditorGroup) {
		this.auditorGroup = auditorGroup;
	}

}
