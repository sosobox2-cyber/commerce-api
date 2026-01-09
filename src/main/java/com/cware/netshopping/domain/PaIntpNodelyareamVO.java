package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaIntpNodelyaream;

public class PaIntpNodelyareamVO extends PaIntpNodelyaream {
	private static final long serialVersionUID = 1L; 

	private String mailNo;
	private String mailNoSeq;
	private String prdNo;
	private String applyYn;
	private String addr;
	private String dlvCnntAreaNo;
	
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getMailNoSeq() {
		return mailNoSeq;
	}
	public void setMailNoSeq(String mailNoSeq) {
		this.mailNoSeq = mailNoSeq;
	}
	public String getPrdNo() {
		return prdNo;
	}
	public void setPrdNo(String prdNo) {
		this.prdNo = prdNo;
	}
	public String getApplyYn() {
		return applyYn;
	}
	public void setApplyYn(String applyYn) {
		this.applyYn = applyYn;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getDlvCnntAreaNo() {
		return dlvCnntAreaNo;
	}
	public void setDlvCnntAreaNo(String dlvCnntAreaNo) {
		this.dlvCnntAreaNo = dlvCnntAreaNo;
	}
	
	
}
