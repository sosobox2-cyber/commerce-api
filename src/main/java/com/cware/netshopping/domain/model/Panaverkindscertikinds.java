package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Panaverkindscertikinds extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String categoryId;
	private String certiCode;
	private String certiKindCode;
	private String certiKindName;
	private String insertId;
	private Timestamp insertDate;
	
	
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCertiKindCode() {
		return certiKindCode;
	}
	public void setCertiKindCode(String certiKindCode) {
		this.certiKindCode = certiKindCode;
	}
	public String getCertiKindName() {
		return certiKindName;
	}
	public void setCertiKindName(String certiKindName) {
		this.certiKindName = certiKindName;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
}
