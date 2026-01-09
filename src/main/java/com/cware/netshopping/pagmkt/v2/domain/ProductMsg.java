package com.cware.netshopping.pagmkt.v2.domain;

import com.cware.netshopping.pacopn.v2.domain.ResultMsg;

public class ProductMsg extends ResultMsg {
	Product data;

	public Product getData() {
		return data;
	}

	public void setData(Product data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProductMsg [data=" + data + "]";
	}

}
