package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaMaker extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String paGroupCode;
	private String paMakerNo;
	private String paMakerName;
	private String rtnMsg;
	private String useYn;
	
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaMakerNo() {
		return paMakerNo;
	}
	public void setPaMakerNo(String paMakerNo) {
		this.paMakerNo = paMakerNo;
	}
	public String getPaMakerName() {
		return paMakerName;
	}
	public void setPaMakerName(String paMakerName) {
		this.paMakerName = paMakerName;
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
	
}