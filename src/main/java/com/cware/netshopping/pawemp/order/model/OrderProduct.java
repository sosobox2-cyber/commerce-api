package com.cware.netshopping.pawemp.order.model;

import java.util.List;

import com.cware.netshopping.pawemp.common.model.OrderOption;

public class OrderProduct {
	
	private long orderNo;
	private long productNo;
	private String productName;
	private long productOriginPrice;
	private long productPrice;
	private long productCommissionPrice;
	private long productQty;
	private long wmpChargeDiscountPrice;
	private long sellerChargeDiscountPrice;
	private long cardChargeDiscountPrice;
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
	public long getProductOriginPrice() {
		return productOriginPrice;
	}
	public void setProductOriginPrice(long productOriginPrice) {
		this.productOriginPrice = productOriginPrice;
	}
	public long getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(long productPrice) {
		this.productPrice = productPrice;
	}
	public long getProductCommissionPrice() {
		return productCommissionPrice;
	}
	public void setProductCommissionPrice(long productCommissionPrice) {
		this.productCommissionPrice = productCommissionPrice;
	}
	public long getProductQty() {
		return productQty;
	}
	public void setProductQty(long productQty) {
		this.productQty = productQty;
	}
	public long getWmpChargeDiscountPrice() {
		return wmpChargeDiscountPrice;
	}
	public void setWmpChargeDiscountPrice(long wmpChargeDiscountPrice) {
		this.wmpChargeDiscountPrice = wmpChargeDiscountPrice;
	}
	public long getSellerChargeDiscountPrice() {
		return sellerChargeDiscountPrice;
	}
	public void setSellerChargeDiscountPrice(long sellerChargeDiscountPrice) {
		this.sellerChargeDiscountPrice = sellerChargeDiscountPrice;
	}
	public long getCardChargeDiscountPrice() {
		return cardChargeDiscountPrice;
	}
	public void setCardChargeDiscountPrice(long cardChargeDiscountPrice) {
		this.cardChargeDiscountPrice = cardChargeDiscountPrice;
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
