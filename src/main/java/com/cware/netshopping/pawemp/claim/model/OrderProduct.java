package com.cware.netshopping.pawemp.claim.model;

import java.util.List;

import com.cware.netshopping.pawemp.common.model.OrderOption;

public class OrderProduct {
	private long orderNo;
	private long productNo;
	private String productName;
	private long productPrice;
	private long productQty;
	private String sellerProductCode;
	private List<OrderOption> orderOption;
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public long getProductNo() {
		return productNo;
	}
	public void setProductNo(long productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(long productPrice) {
		this.productPrice = productPrice;
	}
	public long getProductQty() {
		return productQty;
	}
	public void setProductQty(long productQty) {
		this.productQty = productQty;
	}
	public String getSellerProductCode() {
		return sellerProductCode;
	}
	public void setSellerProductCode(String sellerProductCode) {
		this.sellerProductCode = sellerProductCode;
	}
	public List<OrderOption> getOrderOption() {
		return orderOption;
	}
	public void setOrderOption(List<OrderOption> orderOption) {
		this.orderOption = orderOption;
	}
	
	
}
