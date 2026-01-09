package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaWempMaker extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String makerCode;
	private String makerNo;
	private String makerName;
	private String remark;

	public String getMakerCode() { 
		return this.makerCode;
	}
	public String getMakerNo() { 
		return this.makerNo;
	}
	public String getMakerName() { 
		return this.makerName;
	}
	public String getRemark() { 
		return this.remark;
	}

	public void setMakerCode(String makerCode) { 
		this.makerCode = makerCode;
	}
	public void setMakerNo(String makerNo) { 
		this.makerNo = makerNo;
	}
	public void setMakerName(String makerName) { 
		this.makerName = makerName;
	}
	public void setRemark(String remark) { 
		this.remark = remark;
	}
}
