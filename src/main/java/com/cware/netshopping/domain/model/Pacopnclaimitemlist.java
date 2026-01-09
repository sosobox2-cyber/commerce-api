package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnclaimitemlist extends AbstractModel{
	private static final long serialVersionUID = 1L;
	
	private String receiptId;
	private String orderId;
	private String paymentId;
	private String itemSeq;
	private String vendorItemPackageId;
	private String vendorItemPackageName;
	private String vendorItemId;
	private String vendorItemName;
	private String shipmentBoxId;
	private String sellerProductId;
	private String sellerProductName;
	
	private long purchaseCount;
	private long cancelCount;
	private String orgShipmentBoxId;
	
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getItemSeq() {
		return itemSeq;
	}
	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}
	public String getVendorItemPackageId() {
		return vendorItemPackageId;
	}
	public void setVendorItemPackageId(String vendorItemPackageId) {
		this.vendorItemPackageId = vendorItemPackageId;
	}
	public String getVendorItemPackageName() {
		return vendorItemPackageName;
	}
	public void setVendorItemPackageName(String vendorItemPackageName) {
		this.vendorItemPackageName = vendorItemPackageName;
	}
	public String getVendorItemId() {
		return vendorItemId;
	}
	public void setVendorItemId(String vendorItemId) {
		this.vendorItemId = vendorItemId;
	}
	public String getVendorItemName() {
		return vendorItemName;
	}
	public void setVendorItemName(String vendorItemName) {
		this.vendorItemName = vendorItemName;
	}
	public String getShipmentBoxId() {
		return shipmentBoxId;
	}
	public void setShipmentBoxId(String shipmentBoxId) {
		this.shipmentBoxId = shipmentBoxId;
	}
	public String getSellerProductId() {
		return sellerProductId;
	}
	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}
	public String getSellerProductName() {
		return sellerProductName;
	}
	public void setSellerProductName(String sellerProductName) {
		this.sellerProductName = sellerProductName;
	}
	public long getPurchaseCount() {
		return purchaseCount;
	}
	public void setPurchaseCount(long purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	public long getCancelCount() {
		return cancelCount;
	}
	public void setCancelCount(long cancelCount) {
		this.cancelCount = cancelCount;
	}
	public String getOrgShipmentBoxId() {
		return orgShipmentBoxId;
	}
	public void setOrgShipmentBoxId(String orgShipmentBoxId) {
		this.orgShipmentBoxId = orgShipmentBoxId;
	}
}
