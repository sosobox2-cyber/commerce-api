package com.cware.netshopping.pawemp.common.model;

/**
 * 위메프 공통 등록수정 결과 data
 * @author ttang
 *
 */
public class ReturnData {
	
	private long returnKey;
	private String returnMsg;
	
	public long getReturnKey() {
		return returnKey;
	}
	public void setReturnKey(long returnKey) {
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
		return "ReturnData [returnKey=" + returnKey + ", returnMsg="
				+ returnMsg + "]";
	}
	
	
}
