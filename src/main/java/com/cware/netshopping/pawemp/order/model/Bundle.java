package com.cware.netshopping.pawemp.order.model;

import java.util.List;

public class Bundle {
	
	private long bundleNo;
	private long purchaseNo;
	private String orderDate;
	private String payDate;
	private String originShipDate;
	private String orderConfirmDate;
	private String orderShippingDate;
	private String shipCompleteDate;
	private String buyerName;
	private String buyerPhone;
	private long shipPrice;
	private String prepayment;
	private String shipType;
	private OrderDelivery delivery;
	private List<OrderProduct> orderProduct;
	
	public long getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(long bundleNo) {
		this.bundleNo = bundleNo;
	}
	public long getPurchaseNo() {
		return purchaseNo;
	}
	public void setPurchaseNo(long purchaseNo) {
		this.purchaseNo = purchaseNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getOriginShipDate() {
		return originShipDate;
	}
	public void setOriginShipDate(String originShipDate) {
		this.originShipDate = originShipDate;
	}
	public String getOrderConfirmDate() {
		return orderConfirmDate;
	}
	public void setOrderConfirmDate(String orderConfirmDate) {
		this.orderConfirmDate = orderConfirmDate;
	}
	public String getOrderShippingDate() {
		return orderShippingDate;
	}
	public void setOrderShippingDate(String orderShippingDate) {
		this.orderShippingDate = orderShippingDate;
	}
	public String getShipCompleteDate() {
		return shipCompleteDate;
	}
	public void setShipCompleteDate(String shipCompleteDate) {
		this.shipCompleteDate = shipCompleteDate;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerPhone() {
		return buyerPhone;
	}
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	public long getShipPrice() {
		return shipPrice;
	}
	public void setShipPrice(long shipPrice) {
		this.shipPrice = shipPrice;
	}
	public String getPrepayment() {
		return prepayment;
	}
	public void setPrepayment(String prepayment) {
		this.prepayment = prepayment;
	}
	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	public OrderDelivery getDelivery() {
		return delivery;
	}
	public void setDelivery(OrderDelivery delivery) {
		this.delivery = delivery;
	}
	public List<OrderProduct> getOrderProduct() {
		return orderProduct;
	}
	public void setOrderProduct(List<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}
	
}
