package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Panavergoodskinds extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String categoryId;
	private String categoryName;
	private String categoryFullName;
	private String last;

	public String getCategoryId() { 
		return this.categoryId;
	}
	public String getCategoryName() { 
		return this.categoryName;
	}
	public String getCategoryFullName() { 
		return this.categoryFullName;
	}
	public String getLast() { 
		return this.last;
	}

	public void setCategoryId(String categoryId) { 
		this.categoryId = categoryId;
	}
	public void setCategoryName(String categoryName) { 
		this.categoryName = categoryName;
	}
	public void setCategoryFullName(String categoryFullName) { 
		this.categoryFullName = categoryFullName;
	}
	public void setLast(String last) { 
		this.last = last;
	}
}
