package com.cware.netshopping.domain.model;


import com.cware.framework.core.basic.AbstractModel;


public class Pa11stOriginMapping extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orgnTypDtlsCd; 
	private String originCode;
	
	public String getOrgnTypDtlsCd() {
		return orgnTypDtlsCd;
	}
	public void setOrgnTypDtlsCd(String orgnTypDtlsCd) {
		this.orgnTypDtlsCd = orgnTypDtlsCd;
	}
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	
}