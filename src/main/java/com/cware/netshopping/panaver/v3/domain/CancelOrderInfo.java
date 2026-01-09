package com.cware.netshopping.panaver.v3.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelOrderInfo {
	
	
	// 취소 승인일
	private Timestamp cancelApprovalDate;
	// 취소 완료일
	private Timestamp cancelCompletedDate;
	//취소 상세 사유
	private String cancelDetailedReason;
	//클레임 요청 사유
	private String cancelReason;
	//클레임 요청일
	private Timestamp claimRequestDate;
	//클레임 상태
	private String claimStatus;
	//환불 예정일
	private Timestamp refundExpectedDate;
	//환불 대기 사유
	private String refundStandbyReason;
	//환불 대기 상태
	private String refundStandbyStatus;
	//접수 채널
	private String requestChannel;
	
	public Timestamp getCancelApprovalDate() {
		return cancelApprovalDate;
	}
	public void setCancelApprovalDate(Timestamp cancelApprovalDate) {
		this.cancelApprovalDate = cancelApprovalDate;
	}
	public Timestamp getCancelCompletedDate() {
		return cancelCompletedDate;
	}
	public void setCancelCompletedDate(Timestamp cancelCompletedDate) {
		this.cancelCompletedDate = cancelCompletedDate;
	}
	public String getCancelDetailedReason() {
		return cancelDetailedReason;
	}
	public void setCancelDetailedReason(String cancelDetailedReason) {
		this.cancelDetailedReason = cancelDetailedReason;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public Timestamp getClaimRequestDate() {
		return claimRequestDate;
	}
	public void setClaimRequestDate(Timestamp claimRequestDate) {
		this.claimRequestDate = claimRequestDate;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public Timestamp getRefundExpectedDate() {
		return refundExpectedDate;
	}
	public void setRefundExpectedDate(Timestamp refundExpectedDate) {
		this.refundExpectedDate = refundExpectedDate;
	}
	public String getRefundStandbyReason() {
		return refundStandbyReason;
	}
	public void setRefundStandbyReason(String refundStandbyReason) {
		this.refundStandbyReason = refundStandbyReason;
	}
	public String getRefundStandbyStatus() {
		return refundStandbyStatus;
	}
	public void setRefundStandbyStatus(String refundStandbyStatus) {
		this.refundStandbyStatus = refundStandbyStatus;
	}
	public String getRequestChannel() {
		return requestChannel;
	}
	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
	}
	@Override
	public String toString() {
		return "CancelOrderInfo [cancelApprovalDate=" + cancelApprovalDate + ", cancelCompletedDate="
				+ cancelCompletedDate + ", cancelDetailedReason=" + cancelDetailedReason + ", cancelReason="
				+ cancelReason + ", claimRequestDate=" + claimRequestDate + ", claimStatus=" + claimStatus
				+ ", refundExpectedDate=" + refundExpectedDate + ", refundStandbyReason=" + refundStandbyReason
				+ ", refundStandbyStatus=" + refundStandbyStatus + ", requestChannel=" + requestChannel + "]";
	}
	
	
	

}
