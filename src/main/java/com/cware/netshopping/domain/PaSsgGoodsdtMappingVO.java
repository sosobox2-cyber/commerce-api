package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;

public class PaSsgGoodsdtMappingVO extends PaSsgGoodsdtMapping {

	private static final long serialVersionUID = 1L;

	private String itemId;
	private String newTransQty;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getNewTransQty() {
		return newTransQty;
	}

	public void setNewTransQty(String newTransQty) {
		this.newTransQty = newTransQty;
	}

}
