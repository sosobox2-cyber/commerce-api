package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.domain.model.PaFapleGoodsDt;

public class PaFapleGoodsDtVO extends PaFapleGoodsDt {
	private static final long serialVersionUID = 1L; 
	String color;
	String vendorOptId;
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getVendorOptId() {
		return vendorOptId;
	}
	public void setVendorOptId(String vendorOptId) {
		this.vendorOptId = vendorOptId;
	}
	
}
