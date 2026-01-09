package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Orderstock;

public class OrderstockVO extends Orderstock {
	private static final long serialVersionUID = 1L;

	private String preoutGb;

	public String getPreoutGb() {
		return preoutGb;
	}

	public void setPreoutGb(String preoutGb) {
		this.preoutGb = preoutGb;
	}
	
}
