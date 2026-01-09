package com.cware.netshopping.pafaple.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleCancelList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String itemId;
	private String procFlag;
	private String cancelState;
	private String cancelStateName;
	private Timestamp cancelCreated;
	private String reasonTypeName;
	private String reasonType;
	private Timestamp autoCancelDate;
	private String autoCancelEmpName;
	private Timestamp approvalDate;
	private String approvalEmpName;
	private Timestamp refusalDate;
	private String refusalEmpName;
	private String withdrawYn;
	private Timestamp withdrawDate;
	private String cancelProcNote;
	private String cancelProcId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getCancelState() {
		return cancelState;
	}
	public void setCancelState(String cancelState) {
		this.cancelState = cancelState;
	}
	public String getCancelStateName() {
		return cancelStateName;
	}
	public void setCancelStateName(String cancelStateName) {
		this.cancelStateName = cancelStateName;
	}
	public Timestamp getCancelCreated() {
		return cancelCreated;
	}
	public void setCancelCreated(Timestamp cancelCreated) {
		this.cancelCreated = cancelCreated;
	}
	public String getReasonTypeName() {
		return reasonTypeName;
	}
	public void setReasonTypeName(String reasonTypeName) {
		this.reasonTypeName = reasonTypeName;
	}
	public Timestamp getAutoCancelDate() {
		return autoCancelDate;
	}
	public void setAutoCancelDate(Timestamp autoCancelDate) {
		this.autoCancelDate = autoCancelDate;
	}
	public String getAutoCancelEmpName() {
		return autoCancelEmpName;
	}
	public void setAutoCancelEmpName(String autoCancelEmpName) {
		this.autoCancelEmpName = autoCancelEmpName;
	}
	public Timestamp getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApprovalEmpName() {
		return approvalEmpName;
	}
	public void setApprovalEmpName(String approvalEmpName) {
		this.approvalEmpName = approvalEmpName;
	}
	public Timestamp getRefusalDate() {
		return refusalDate;
	}
	public void setRefusalDate(Timestamp refusalDate) {
		this.refusalDate = refusalDate;
	}
	public String getRefusalEmpName() {
		return refusalEmpName;
	}
	public void setRefusalEmpName(String refusalEmpName) {
		this.refusalEmpName = refusalEmpName;
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
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	
}