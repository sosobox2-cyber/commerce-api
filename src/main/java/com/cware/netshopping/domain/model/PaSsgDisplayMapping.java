package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaSsgDisplayMapping extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String paLmsdKey;
	private String siteNo;
	private String dispCtgId;
	
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public String getDispCtgId() {
		return dispCtgId;
	}
	public void setDispCtgId(String dispCtgId) {
		this.dispCtgId = dispCtgId;
	}
}