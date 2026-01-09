package com.cware.netshopping.domain.model;

public class PaHalfBrand extends PaBrand {
	
	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String returnNoYn;
	private String paDetailBrandNo;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getReturnNoYn() {
		return returnNoYn;
	}
	public void setReturnNoYn(String returnNoYn) {
		this.returnNoYn = returnNoYn;
	}
	public String getPaDetailBrandNo() {
		return paDetailBrandNo;
	}
	public void setPaDetailBrandNo(String paDetailBrandNo) {
		this.paDetailBrandNo = paDetailBrandNo;
	}
	
	

}
