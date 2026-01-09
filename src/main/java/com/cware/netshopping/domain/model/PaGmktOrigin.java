package com.cware.netshopping.domain.model;


import com.cware.framework.core.basic.AbstractModel;


public class PaGmktOrigin extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orgnTypCd;
	private String orgnTypDtlsCd; 
	private String areaName;
	private String detailAreaName;
	
	public String getOrgnTypCd() {
		return orgnTypCd;
	}
	public void setOrgnTypCd(String orgnTypCd) {
		this.orgnTypCd = orgnTypCd;
	}
	public String getOrgnTypDtlsCd() {
		return orgnTypDtlsCd;
	}
	public void setOrgnTypDtlsCd(String orgnTypDtlsCd) {
		this.orgnTypDtlsCd = orgnTypDtlsCd;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getDetailAreaName() {
		return detailAreaName;
	}
	public void setDetailAreaName(String detailAreaName) {
		this.detailAreaName = detailAreaName;
	}	
	
}