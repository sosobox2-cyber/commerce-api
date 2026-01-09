package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Promocounsel;


/**
 *
 * @author Commerceware
 *
 */
public class PromocounselVO extends Promocounsel {
	private static final long serialVersionUID = 1L;
	
    
	private long limitQty;
	private String doType;
	private String bigo;


	public String getDoType() {
		return doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public String getBigo() {
		return bigo;
	}

	public void setBigo(String bigo) {
		this.bigo = bigo;
	}

	public long getLimitQty() {
		return limitQty;
	}

	public void setLimitQty(long limitQty) {
		this.limitQty = limitQty;
	}


}