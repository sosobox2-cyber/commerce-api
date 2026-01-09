package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Custtel extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String custNo;
	private String tel;
	
	
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
