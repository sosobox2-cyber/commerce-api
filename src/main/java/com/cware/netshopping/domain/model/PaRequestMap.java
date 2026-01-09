package com.cware.netshopping.domain.model;


public class PaRequestMap {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L; 

	private String paCode;
	private String reqApiCode;
	private String reqUrl;
	private String reqHeader;
	private String requestMap;
	private String responseMap;
	private String remark;
	private String insertDate;
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getReqApiCode() {
		return reqApiCode;
	}
	public void setReqApiCode(String reqApiCode) {
		this.reqApiCode = reqApiCode;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	public String getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}
	public String getRequestMap() {
		return requestMap;
	}
	public void setRequestMap(String requestMap) {
		this.requestMap = requestMap;
	}
	public String getResponseMap() {
		return responseMap;
	}
	public void setResponseMap(String responseMap) {
		this.responseMap = responseMap;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
}