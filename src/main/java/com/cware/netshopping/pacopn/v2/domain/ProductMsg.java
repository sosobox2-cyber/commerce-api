package com.cware.netshopping.pacopn.v2.domain;

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
		return "ProductMsg [status=" + status + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}

}
