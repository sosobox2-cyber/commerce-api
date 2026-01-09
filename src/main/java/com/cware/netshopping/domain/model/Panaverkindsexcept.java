package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Panaverkindsexcept extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String categoryId;
	private String exceptCode;
	private String exceptName;

	public String getCategoryId() { 
		return this.categoryId;
	}
	public String getExceptCode() { 
		return this.exceptCode;
	}
	public String getExceptName() { 
		return this.exceptName;
	}

	public void setCategoryId(String categoryId) { 
		this.categoryId = categoryId;
	}
	public void setExceptCode(String exceptCode) { 
		this.exceptCode = exceptCode;
	}
	public void setExceptName(String exceptName) { 
		this.exceptName = exceptName;
	}
}
