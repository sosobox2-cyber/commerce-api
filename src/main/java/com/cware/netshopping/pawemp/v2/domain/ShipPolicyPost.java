package com.cware.netshopping.pawemp.v2.domain;

public class ShipPolicyPost {
	String resultCode;
	ShipPolicyResult data;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public ShipPolicyResult getData() {
		return data;
	}

	public void setData(ShipPolicyResult data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ShipPolicyPost [resultCode=" + resultCode + ", data=" + data + "]";
	}

}
