package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangedProductOrderInfoResponse {
	
	private ChangedProductOrderInfoResponseData data;
	
	private String timestamp;
	
	private String traceId;
	
	// 오류코드
	@JsonProperty("code")
	private String errorCode;

	private String message;

	public ChangedProductOrderInfoResponseData getData() {
		return data;
	}

	public void setData(ChangedProductOrderInfoResponseData data) {
		this.data = data;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChangedProductOrderInfoResponse [data=" + data + ", timestamp=" + timestamp + ", traceId=" + traceId
				+ ", errorCode=" + errorCode + ", message=" + message + "]";
	}
}
