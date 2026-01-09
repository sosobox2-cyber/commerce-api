package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaNaverOrigin extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String originCode;
	private String originName;
	
	public String getOrignCode() {
		return originCode;
	}
	public void setOrignCode(String originCode) {
		this.originCode = originCode;
	}
	public String getOrignName() {
		return originName;
	}
	public void setOrignName(String originName) {
		this.originName = originName;
	}
	

	
}
