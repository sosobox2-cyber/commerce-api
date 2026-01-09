package com.cware.netshopping.domain.model;

import java.sql.Timestamp;
import java.util.Date;

import com.cware.framework.core.basic.AbstractModel;

public class PaQeenCancelList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String ticketId;
	private String orderId;
	private String groupId;
	private String orderItemId;
	private String state;
	private String reasonType;
	private String reason;
	private String rejectReason;
	private String auditorGroup;
	private Timestamp createdAtMillis;
	private Timestamp confirmedAtMillis;
	private Timestamp rejectedAtMillis;
	private Timestamp resolvedAtMillis;
	private Timestamp withdrawnAtMillis;
	private String isCustomerNegligence;
	private String procFlag;
	private String procNote;
	private String cancelProcNote;
	private String cancelProcId;
	private String withdrawYn;
	private Timestamp withdrawDate;
	private long quantity;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getAuditorGroup() {
		return auditorGroup;
	}

	public void setAuditorGroup(String auditorGroup) {
		this.auditorGroup = auditorGroup;
	}

	public Timestamp getCreatedAtMillis() {
		return createdAtMillis;
	}

	public void setCreatedAtMillis(Timestamp createdAtMillis) {
		this.createdAtMillis = createdAtMillis;
	}

	public Timestamp getConfirmedAtMillis() {
		return confirmedAtMillis;
	}

	public void setConfirmedAtMillis(Timestamp confirmedAtMillis) {
		this.confirmedAtMillis = confirmedAtMillis;
	}

	public Timestamp getRejectedAtMillis() {
		return rejectedAtMillis;
	}

	public void setRejectedAtMillis(Timestamp rejectedAtMillis) {
		this.rejectedAtMillis = rejectedAtMillis;
	}

	public Timestamp getResolvedAtMillis() {
		return resolvedAtMillis;
	}

	public void setResolvedAtMillis(Timestamp resolvedAtMillis) {
		this.resolvedAtMillis = resolvedAtMillis;
	}

	public Timestamp getWithdrawnAtMillis() {
		return withdrawnAtMillis;
	}

	public void setWithdrawnAtMillis(Timestamp withdrawnAtMillis) {
		this.withdrawnAtMillis = withdrawnAtMillis;
	}

	public String getIsCustomerNegligence() {
		return isCustomerNegligence;
	}

	public void setIsCustomerNegligence(String isCustomerNegligence) {
		this.isCustomerNegligence = isCustomerNegligence;
	}

	public String getProcFlag() {
		return procFlag;
	}

	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}

	public String getProcNote() {
		return procNote;
	}

	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}

	public String getCancelProcNote() {
		return cancelProcNote;
	}

	public void setCancelProcNote(String cancelProcNote) {
		this.cancelProcNote = cancelProcNote;
	}

	public String getCancelProcId() {
		return cancelProcId;
	}

	public void setCancelProcId(String cancelProcId) {
		this.cancelProcId = cancelProcId;
	}

	public String getWithdrawYn() {
		return withdrawYn;
	}

	public void setWithdrawYn(String withdrawYn) {
		this.withdrawYn = withdrawYn;
	}

	public Timestamp getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

}
