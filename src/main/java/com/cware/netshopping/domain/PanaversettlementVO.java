package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.Panaversettlement;

public class PanaversettlementVO extends Panaversettlement {

	private String delYn;
	
	private Timestamp fromDate;
	
	private Timestamp toDate;
	
	private String paDoFlag;

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public Timestamp getFromDate() {
		return fromDate;
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public Timestamp getToDate() {
		return toDate;
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	public String getPaDoFlag() {
		return paDoFlag;
	}

	public void setPaDoFlag(String paDoFlag) {
		this.paDoFlag = paDoFlag;
	}
	
	
	
	
}
