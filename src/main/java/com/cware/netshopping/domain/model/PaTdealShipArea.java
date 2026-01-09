package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealShipArea extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String areaNo;
	private String address;
	private String countryCd;
	private String defaultAreaYn;
	private String areaGb;
	
	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountryCd() {
		return countryCd;
	}
	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}
	public String getDefaultAreaYn() {
		return defaultAreaYn;
	}
	public void setDefaultAreaYn(String defaultAreaYn) {
		this.defaultAreaYn = defaultAreaYn;
	}
	public String getAreaGb() {
		return areaGb;
	}
	public void setAreaGb(String areaGb) {
		this.areaGb = areaGb;
	}
	
}