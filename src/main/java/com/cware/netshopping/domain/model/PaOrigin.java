package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaOrigin extends AbstractModel {
	
	private static final long serialVersionUID = 1L; 

	private String paGroupCode;
	private String paOriginCode;
	private String paOriginName;
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaOriginCode() {
		return paOriginCode;
	}
	public void setPaOriginCode(String paOriginCode) {
		this.paOriginCode = paOriginCode;
	}
	public String getPaOriginName() {
		return paOriginName;
	}
	public void setPaOriginName(String paOriginName) {
		this.paOriginName = paOriginName;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getRemark4() {
		return remark4;
	}
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
}