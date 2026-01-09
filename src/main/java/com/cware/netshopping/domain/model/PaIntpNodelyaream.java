package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaIntpNodelyaream extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String paCode;
	private String noDelyAreaCode;
	private String prdInfoTmpltNo;
	private String prdInfoTmpltNm;
	private String deleteYn;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getNoDelyAreaCode() {
		return noDelyAreaCode;
	}
	public void setNoDelyAreaCode(String noDelyAreaCode) {
		this.noDelyAreaCode = noDelyAreaCode;
	}
	public String getPrdInfoTmpltNo() {
		return prdInfoTmpltNo;
	}
	public void setPrdInfoTmpltNo(String prdInfoTmpltNo) {
		this.prdInfoTmpltNo = prdInfoTmpltNo;
	}
	public String getPrdInfoTmpltNm() {
		return prdInfoTmpltNm;
	}
	public void setPrdInfoTmpltNm(String prdInfoTmpltNm) {
		this.prdInfoTmpltNm = prdInfoTmpltNm;
	}
	public String getDeleteYn() {
		return deleteYn;
	}
	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	
	
}
