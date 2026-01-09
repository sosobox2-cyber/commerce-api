package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Pa11stclaimlist;

/**
 *
 * @author Commerceware
 *
 */
public class Pa11stclaimlistVO extends Pa11stclaimlist{
	private static final long serialVersionUID = 1L;
	private String dlvCstRespnClf; //배송비 부담여부
	
	private String paCode;

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getDlvCstRespnClf() {
		return dlvCstRespnClf;
	}

	public void setDlvCstRespnClf(String dlvCstRespnClf) {
		this.dlvCstRespnClf = dlvCstRespnClf;
	}
	
	
}