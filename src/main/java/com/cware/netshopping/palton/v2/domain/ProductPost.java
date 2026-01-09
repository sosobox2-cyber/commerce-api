package com.cware.netshopping.palton.v2.domain;

import java.util.List;

public class ProductPost {
	String returnCode; // 결과코드
	String message; // 결과메세지
	List<ResultData> data; // 처리결과

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

	public List<ResultData> getData() {
		return data;
	}

	public void setData(List<ResultData> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProductPost [returnCode=" + returnCode + ", message=" + message + ", data=" + data + "]";
	}

}
