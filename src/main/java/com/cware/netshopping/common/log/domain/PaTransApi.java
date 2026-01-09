package com.cware.netshopping.common.log.domain;

import java.sql.Timestamp;

public class PaTransApi {

	private String transCode;
	private String transType;
	private long transApiNo;
	private String apiName;
	private String apiUrl;
	private String apiNote;
	private String requestHeader;
	private String requestPayload;
	private Timestamp requestDate;
	private String responsePayload;
	private String successYn;
	private String resultCode;
	private String resultMsg;
	private long transServiceNo;
	private String paGroupCode;
	private String processId;

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

	public long getTransApiNo() {
		return transApiNo;
	}

	public void setTransApiNo(long transApiNo) {
		this.transApiNo = transApiNo;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiNote() {
		return apiNote;
	}

	public void setApiNote(String apiNote) {
		this.apiNote = apiNote;
	}

	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
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

	public long getTransServiceNo() {
		return transServiceNo;
	}

	public void setTransServiceNo(long transServiceNo) {
		this.transServiceNo = transServiceNo;
	}

	public String getPaGroupCode() {
		return paGroupCode;
	}

	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	public String toString() {
		return "PaTransApi [transCode=" + transCode + ", transType=" + transType + ", transApiNo=" + transApiNo
				+ ", apiName=" + apiName + ", apiUrl=" + apiUrl + ", apiNote=" + apiNote
				+ ", requestHeader=" + requestHeader + ", requestPayload=" + requestPayload + ", requestDate="
				+ requestDate + ", responsePayload=" + responsePayload + ", successYn=" + successYn + ", resultCode="
				+ resultCode + ", resultMsg=" + resultMsg + ", transServiceNo=" + transServiceNo + ", paGroupCode="
				+ paGroupCode + ", processId=" + processId + "]";
	}

}
