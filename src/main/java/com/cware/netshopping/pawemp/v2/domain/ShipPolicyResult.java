package com.cware.netshopping.pawemp.v2.domain;

public class ShipPolicyResult {
	String returnKey;
	String returnMsg;

	public String getReturnKey() {
		return returnKey;
	}

	public void setReturnKey(String returnKey) {
		this.returnKey = returnKey;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@Override
	public String toString() {
		return "ShipPolicyResult [returnKey=" + returnKey + ", returnMsg=" + returnMsg + "]";
	}

}
