package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaBrand extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String paGroupCode;
	private String paBrandNo;
	private String paBrandName;
	private String rtnMsg;
	private String useYn;
	private String paCode;
	
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaBrandNo() {
		return paBrandNo;
	}
	public void setPaBrandNo(String paBrandNo) {
		this.paBrandNo = paBrandNo;
	}
	public String getPaBrandName() {
		return paBrandName;
	}
	public void setPaBrandName(String paBrandName) {
		this.paBrandName = paBrandName;
	}
	public String getRtnMsg() {
		return rtnMsg;
	}
	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	
}