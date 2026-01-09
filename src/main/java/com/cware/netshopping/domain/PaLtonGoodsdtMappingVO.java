package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaLtonGoodsdtMapping;

public class PaLtonGoodsdtMappingVO extends PaLtonGoodsdtMapping  {
	
	private static final long serialVersionUID = 1L;
	
	private String spdNo;
	private String newTransQty;
		
	public String getSpdNo() {
		return spdNo;
	}
	public void setSpdNo(String spdNo) {
		this.spdNo = spdNo;
	}
	public String getNewTransQty() {
		return newTransQty;
	}
	public void setNewTransQty(String newTransQty) {
		this.newTransQty = newTransQty;
	}
		
}
