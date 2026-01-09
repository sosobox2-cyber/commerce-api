package com.cware.netshopping.pacopn.v2.domain;

public class ApiResponseMsg extends ResultMsg {

	String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ApiResponseMsg [data=" + data + ", status=" + status + ", code=" + code + ", message=" + message + "]";
	}

}
