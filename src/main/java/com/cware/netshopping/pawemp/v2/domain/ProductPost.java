package com.cware.netshopping.pawemp.v2.domain;

public class ProductPost {
	String resultCode;
	ProductResult data;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public ProductResult getData() {
		return data;
	}

	public void setData(ProductResult data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProductPost [resultCode=" + resultCode + ", data=" + data + "]";
	}

}
