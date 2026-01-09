package com.cware.netshopping.common.log.domain;

import java.sql.Timestamp;

public class PaTransService {

	private String transCode;
	private String transType;
	private long transServiceNo;
	private String serviceName;
	private String serviceNote;
	private String successYn;
	private String resultCode;
	private String resultMsg;
	private long transBatchNo;
	private String paGroupCode;
	private Timestamp startDate;
	private Timestamp endDate;
	private String processId;
	private int status; // 1:처리, 0:대상아님(필터), -1:실패
	
	private String paCode;
	
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public long getTransServiceNo() {
		return transServiceNo;
	}
	public void setTransServiceNo(long transServiceNo) {
		this.transServiceNo = transServiceNo;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceNote() {
		return serviceNote;
	}
	public void setServiceNote(String serviceNote) {
		this.serviceNote = serviceNote;
	}
	public String getSuccessYn() {
		return successYn;
	}
	public void setSuccessYn(String successYn) {
		this.successYn = successYn;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public long getTransBatchNo() {
		return transBatchNo;
	}
	public void setTransBatchNo(long transBatchNo) {
		this.transBatchNo = transBatchNo;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	
	@Override
	public String toString() {
		return "PaTransService [transCode=" + transCode + ", transType=" + transType + ", transServiceNo="
				+ transServiceNo + ", serviceName=" + serviceName + ", serviceNote=" + serviceNote + ", successYn="
				+ successYn + ", resultCode=" + resultCode + ", resultMsg=" + resultMsg + ", transBatchNo="
				+ transBatchNo + ", paGroupCode=" + paGroupCode + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", processId=" + processId + ", status=" + status + ", paCode=" + paCode + "]";
	}

}
