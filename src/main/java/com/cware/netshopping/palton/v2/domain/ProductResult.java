package com.cware.netshopping.palton.v2.domain;

public class ProductResult {
	String returnCode; // 결과코드
	String message; // 결과메세지
	Product data; // 데이터 리스트

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Product getData() {
		return data;
	}

	public void setData(Product data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProductResult [returnCode=" + returnCode + ", message=" + message + ", data=" + data + "]";
	}

}
