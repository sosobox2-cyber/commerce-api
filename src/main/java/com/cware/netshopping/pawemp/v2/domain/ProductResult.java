package com.cware.netshopping.pawemp.v2.domain;

public class ProductResult {
	String productNo;
	String returnMsg;
	
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	@Override
	public String toString() {
		return "ProductResult [productNo=" + productNo + ", returnMsg=" + returnMsg + "]";
	}
	
}
	