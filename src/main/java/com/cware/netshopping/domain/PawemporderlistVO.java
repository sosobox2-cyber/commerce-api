package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaWempOrderList;

/**
 *
 * @author Commerceware
 *
 */
public class PawemporderlistVO extends PaWempOrderList{
	private static final long serialVersionUID = 1L;

	private String paCode;

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	
}