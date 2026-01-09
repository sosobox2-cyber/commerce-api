package com.cware.netshopping.pafaple.domain;

import com.cware.netshopping.pafaple.domain.model.PaFapleExchangeList;

public class PaFapleExchangeListVO extends PaFapleExchangeList {

	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String paOrderGb;
	private String reasonType;
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	
}
