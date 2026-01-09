package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnexchangeitemlist extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String paOrderGb;
	private String exchangeId;
	private String orderId;
	private String itemSeq;
	private String exchangeItemId;
	private String orderItemId;
	private String orderItemUnitPrice;
	private String orderItemName;
	private String orderPackageId;
	private String orderPackageName;
	private String targetItemId;
	private String targetItemUnitPrice;
	private String targetItemName;
	private String targetPackageId;
	private String targetPackageName;
	private long quantity;
	private String orderItemDeliveryComplete;
	private String orderItemReturnComplete;
	private String targetItemDeliveryComplete;
	private Timestamp createdAt;
	private Timestamp modifiedAt;
	private String originalShipmentBoxId;
	private Timestamp insertDate;

	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemSeq() {
		return itemSeq;
	}
	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}
	public String getExchangeItemId() {
		return exchangeItemId;
	}
	public void setExchangeItemId(String exchangeItemId) {
		this.exchangeItemId = exchangeItemId;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getOrderItemUnitPrice() {
		return orderItemUnitPrice;
	}
	public void setOrderItemUnitPrice(String orderItemUnitPrice) {
		this.orderItemUnitPrice = orderItemUnitPrice;
	}
	public String getOrderItemName() {
		return orderItemName;
	}
	public void setOrderItemName(String orderItemName) {
		this.orderItemName = orderItemName;
	}
	public String getOrderPackageId() {
		return orderPackageId;
	}
	public void setOrderPackageId(String orderPackageId) {
		this.orderPackageId = orderPackageId;
	}
	public String getOrderPackageName() {
		return orderPackageName;
	}
	public void setOrderPackageName(String orderPackageName) {
		this.orderPackageName = orderPackageName;
	}
	public String getTargetItemId() {
		return targetItemId;
	}
	public void setTargetItemId(String targetItemId) {
		this.targetItemId = targetItemId;
	}
	public String getTargetItemUnitPrice() {
		return targetItemUnitPrice;
	}
	public void setTargetItemUnitPrice(String targetItemUnitPrice) {
		this.targetItemUnitPrice = targetItemUnitPrice;
	}
	public String getTargetItemName() {
		return targetItemName;
	}
	public void setTargetItemName(String targetItemName) {
		this.targetItemName = targetItemName;
	}
	public String getTargetPackageId() {
		return targetPackageId;
	}
	public void setTargetPackageId(String targetPackageId) {
		this.targetPackageId = targetPackageId;
	}
	public String getTargetPackageName() {
		return targetPackageName;
	}
	public void setTargetPackageName(String targetPackageName) {
		this.targetPackageName = targetPackageName;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getOrderItemDeliveryComplete() {
		return orderItemDeliveryComplete;
	}
	public void setOrderItemDeliveryComplete(String orderItemDeliveryComplete) {
		this.orderItemDeliveryComplete = orderItemDeliveryComplete;
	}
	public String getOrderItemReturnComplete() {
		return orderItemReturnComplete;
	}
	public void setOrderItemReturnComplete(String orderItemReturnComplete) {
		this.orderItemReturnComplete = orderItemReturnComplete;
	}
	public String getTargetItemDeliveryComplete() {
		return targetItemDeliveryComplete;
	}
	public void setTargetItemDeliveryComplete(String targetItemDeliveryComplete) {
		this.targetItemDeliveryComplete = targetItemDeliveryComplete;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public String getOriginalShipmentBoxId() {
		return originalShipmentBoxId;
	}
	public void setOriginalShipmentBoxId(String originalShipmentBoxId) {
		this.originalShipmentBoxId = originalShipmentBoxId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
}
