package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaWempBrand extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String brandCode;
	private String brandNo;
	private String brandName;
	private String remark;

	public String getBrandCode() { 
		return this.brandCode;
	}
	public String getBrandNo() { 
		return this.brandNo;
	}
	public String getBrandName() { 
		return this.brandName;
	}
	public String getRemark() { 
		return this.remark;
	}

	public void setBrandCode(String brandCode) { 
		this.brandCode = brandCode;
	}
	public void setBrandNo(String brandNo) { 
		this.brandNo = brandNo;
	}
	public void setBrandName(String brandName) { 
		this.brandName = brandName;
	}
	public void setRemark(String remark) { 
		this.remark = remark;
	}
}
