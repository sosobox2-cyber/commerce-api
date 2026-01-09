package com.cware.netshopping.paqeen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =true)
public class RecallDeliveryProgress {
    private String recallDeliveryVendor;
    private String recallDeliveryVendorNumber;
    private boolean requestable;
    private String recallState;
    private long recallDeliveryCompletedAtMillis;
    private String recallDeliveryTraceUrl;
    
	public String getRecallDeliveryVendor() {
		return recallDeliveryVendor;
	}
	public void setRecallDeliveryVendor(String recallDeliveryVendor) {
		this.recallDeliveryVendor = recallDeliveryVendor;
	}
	public String getRecallDeliveryVendorNumber() {
		return recallDeliveryVendorNumber;
	}
	public void setRecallDeliveryVendorNumber(String recallDeliveryVendorNumber) {
		this.recallDeliveryVendorNumber = recallDeliveryVendorNumber;
	}
	public boolean isRequestable() {
		return requestable;
	}
	public void setRequestable(boolean requestable) {
		this.requestable = requestable;
	}
	public String getRecallState() {
		return recallState;
	}
	public void setRecallState(String recallState) {
		this.recallState = recallState;
	}
	public long getRecallDeliveryCompletedAtMillis() {
		return recallDeliveryCompletedAtMillis;
	}
	public void setRecallDeliveryCompletedAtMillis(long recallDeliveryCompletedAtMillis) {
		this.recallDeliveryCompletedAtMillis = recallDeliveryCompletedAtMillis;
	}
	public String getRecallDeliveryTraceUrl() {
		return recallDeliveryTraceUrl;
	}
	public void setRecallDeliveryTraceUrl(String recallDeliveryTraceUrl) {
		this.recallDeliveryTraceUrl = recallDeliveryTraceUrl;
	}
}
