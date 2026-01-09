package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Canceldt;

public class CanceldtVO extends Canceldt {
	private static final long serialVersionUID = 1L;

	private String entpCode;  //ENTP_CODE
	private String custName;  
	private String orgDoFlag;  //원주문의 Do_flag
	private long   orgSysLast; //원주문의 syslast
	
	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getOrgDoFlag() {
		return orgDoFlag;
	}

	public void setOrgDoFlag(String orgDoFlag) {
		this.orgDoFlag = orgDoFlag;
	}

	public long getOrgSysLast() {
		return orgSysLast;
	}

	public void setOrgSysLast(long orgSysLast) {
		this.orgSysLast = orgSysLast;
	}
	
}
