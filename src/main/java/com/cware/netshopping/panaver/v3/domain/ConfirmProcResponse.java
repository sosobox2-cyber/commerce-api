package com.cware.netshopping.panaver.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmProcResponse {
	
	private ConfirmProcResponseData data;
	
	private String timestamp;
	
	private String traceId;

	public ConfirmProcResponseData getData() {
		return data;
	}

	public void setData(ConfirmProcResponseData data) {
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

	@Override
	public String toString() {
		return "ConfirmProcResponse [data=" + data + ", timestamp=" + timestamp + ", traceId=" + traceId + "]";
	}

	
}
