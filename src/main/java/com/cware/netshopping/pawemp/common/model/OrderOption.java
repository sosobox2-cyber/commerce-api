package com.cware.netshopping.pawemp.common.model;

public class OrderOption {
	
	private long orderOptionNo;
	private long optionNo;
	private String optionName;
	private long optionQty;
	private String sellerOptionCode;
	
	public long getOrderOptionNo() {
		return orderOptionNo;
	}
	public void setOrderOptionNo(long orderOptionNo) {
		this.orderOptionNo = orderOptionNo;
	}
	public long getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(long optionNo) {
		this.optionNo = optionNo;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public long getOptionQty() {
		return optionQty;
	}
	public void setOptionQty(long optionQty) {
		this.optionQty = optionQty;
	}
	public String getSellerOptionCode() {
		return sellerOptionCode;
	}
	public void setSellerOptionCode(String sellerOptionCode) {
		this.sellerOptionCode = sellerOptionCode;
	}
	
	
}
