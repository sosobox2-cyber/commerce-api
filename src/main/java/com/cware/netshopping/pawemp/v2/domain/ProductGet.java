package com.cware.netshopping.pawemp.v2.domain;

public class ProductGet {
	String resultCode;
	Product data;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Product getData() {
		return data;
	}

	public void setData(Product data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProductGet [resultCode=" + resultCode + ", data=" + data + "]";
	}

}
