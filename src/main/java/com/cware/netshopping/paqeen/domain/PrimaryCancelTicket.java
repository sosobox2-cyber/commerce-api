package com.cware.netshopping.paqeen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class PrimaryCancelTicket {
    private String ticketId;
    private String state;
    private String reason;
    private Long createdAtMillis;
    private List<OrderItem> orderItems; // OrderItem 클래스로 정의
    private Boolean isCustomerNegligence;
    private String auditorGroup;
    private Long confirmedAtMillis;
    private Long withdrawnAtMillis; 
    private Long rejectedAtMillis;
    private String rejectReason;
    private Long resolvedAtMillis;
    private ResolvedPriceDelta resolvedPriceDelta; // ResolvedPriceDelta 클래스로 정의
    private List<RefundPaymentInfo> refundPaymentInfos; // RefundPaymentInfo 클래스로 정의
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
	public Long getCreatedAtMillis() {
		return createdAtMillis;
	}
	public void setCreatedAtMillis(Long createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getAuditorGroup() {
		return auditorGroup;
	}
	public void setAuditorGroup(String auditorGroup) {
		this.auditorGroup = auditorGroup;
	}
	public Long getConfirmedAtMillis() {
		return confirmedAtMillis;
	}
	public void setConfirmedAtMillis(Long confirmedAtMillis) {
		this.confirmedAtMillis = confirmedAtMillis;
	}
	public Long getWithdrawnAtMillis() {
		return withdrawnAtMillis;
	}
	public void setWithdrawnAtMillis(Long withdrawnAtMillis) {
		this.withdrawnAtMillis = withdrawnAtMillis;
	}
	public Long getRejectedAtMillis() {
		return rejectedAtMillis;
	}
	public void setRejectedAtMillis(Long rejectedAtMillis) {
		this.rejectedAtMillis = rejectedAtMillis;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public Long getResolvedAtMillis() {
		return resolvedAtMillis;
	}
	public void setResolvedAtMillis(Long resolvedAtMillis) {
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
	public Boolean getIsCustomerNegligence() {
		return isCustomerNegligence;
	}
	public void setIsCustomerNegligence(Boolean isCustomerNegligence) {
		this.isCustomerNegligence = isCustomerNegligence;
	}
	
}
