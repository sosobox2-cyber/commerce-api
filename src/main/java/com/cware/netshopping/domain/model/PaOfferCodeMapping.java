package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaOfferCodeMapping extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String paGroupCode; 
	private String paOfferType; 
	private String paOfferCode;
	private String offerType;
	private String offerCode;
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getPaOfferType() {
		return paOfferType;
	}
	public void setPaOfferType(String paOfferType) {
		this.paOfferType = paOfferType;
	}
	public String getPaOfferCode() {
		return paOfferCode;
	}
	public void setPaOfferCode(String paOfferCode) {
		this.paOfferCode = paOfferCode;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	
	
	
}