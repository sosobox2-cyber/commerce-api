package com.cware.netshopping.pawemp.goods.model;

/**
 * 위메프 상품 등록수정 결과 data
 */
public class ReturnData {
	
	private long productNo;
	private String returnMsg;
	
	public long getProductNo() {
		return productNo;
	}
	public void setProductNo(long productNo) {
		this.productNo = productNo;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}
