package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pacopnorderlist extends AbstractModel{
	private static final long serialVersionUID = 1L;
	
	private String shipmentBoxId;
	private String orderId;
	private String ordererName;
	private String ordererEmail;
	private String ordererSafeNumber;
	private String status;
	private String remoteArea;
	private String parcelPrintMessage;
	private String splitShipping;
	private String ableSplitShipping;
	private String receiverName;
	private String receiverSafeNumber;
	private String receiverAddr1;
	private String receiverAddr2;
	private String receiverPostCode;
	private String overseaShippingInfoDTO;
	private String deliveryCompanyName;
	private String invoiceNumber;
	private String refer;
	private String procFlag;
	
	private long shippingPrice;
	private long remotePrice;
	
	private Timestamp orderedAt;
	private Timestamp paidAt;
	private Timestamp inTrasitDateTime;
	private Timestamp deliveredDate;
	
	public String getShipmentBoxId() {
		return shipmentBoxId;
	}
	public void setShipmentBoxId(String shipmentBoxId) {
		this.shipmentBoxId = shipmentBoxId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrdererName() {
		return ordererName;
	}
	public void setOrdererName(String ordererName) {
		this.ordererName = ordererName;
	}
	public String getOrdererEmail() {
		return ordererEmail;
	}
	public void setOrdererEmail(String ordererEmail) {
		this.ordererEmail = ordererEmail;
	}
	public String getOrdererSafeNumber() {
		return ordererSafeNumber;
	}
	public void setOrdererSafeNumber(String ordererSafeNumber) {
		this.ordererSafeNumber = ordererSafeNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemoteArea() {
		return remoteArea;
	}
	public void setRemoteArea(String remoteArea) {
		this.remoteArea = remoteArea;
	}
	public String getParcelPrintMessage() {
		return parcelPrintMessage;
	}
	public void setParcelPrintMessage(String parcelPrintMessage) {
		this.parcelPrintMessage = parcelPrintMessage;
	}
	public String getSplitShipping() {
		return splitShipping;
	}
	public void setSplitShipping(String splitShipping) {
		this.splitShipping = splitShipping;
	}
	public String getAbleSplitShipping() {
		return ableSplitShipping;
	}
	public void setAbleSplitShipping(String ableSplitShipping) {
		this.ableSplitShipping = ableSplitShipping;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverSafeNumber() {
		return receiverSafeNumber;
	}
	public void setReceiverSafeNumber(String receiverSafeNumber) {
		this.receiverSafeNumber = receiverSafeNumber;
	}
	public String getReceiverAddr1() {
		return receiverAddr1;
	}
	public void setReceiverAddr1(String receiverAddr1) {
		this.receiverAddr1 = receiverAddr1;
	}
	public String getReceiverAddr2() {
		return receiverAddr2;
	}
	public void setReceiverAddr2(String receiverAddr2) {
		this.receiverAddr2 = receiverAddr2;
	}
	public String getReceiverPostCode() {
		return receiverPostCode;
	}
	public void setReceiverPostCode(String receiverPostCode) {
		this.receiverPostCode = receiverPostCode;
	}
	public String getOverseaShippingInfoDTO() {
		return overseaShippingInfoDTO;
	}
	public void setOverseaShippingInfoDTO(String overseaShippingInfoDTO) {
		this.overseaShippingInfoDTO = overseaShippingInfoDTO;
	}
	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}
	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getRefer() {
		return refer;
	}
	public void setRefer(String refer) {
		this.refer = refer;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public long getShippingPrice() {
		return shippingPrice;
	}
	public void setShippingPrice(long shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	public long getRemotePrice() {
		return remotePrice;
	}
	public void setRemotePrice(long remotePrice) {
		this.remotePrice = remotePrice;
	}
	public Timestamp getOrderedAt() {
		return orderedAt;
	}
	public void setOrderedAt(Timestamp orderedAt) {
		this.orderedAt = orderedAt;
	}
	public Timestamp getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(Timestamp paidAt) {
		this.paidAt = paidAt;
	}
	public Timestamp getInTrasitDateTime() {
		return inTrasitDateTime;
	}
	public void setInTrasitDateTime(Timestamp inTrasitDateTime) {
		this.inTrasitDateTime = inTrasitDateTime;
	}
	public Timestamp getDeliveredDate() {
		return deliveredDate;
	}
	public void setDeliveredDate(Timestamp deliveredDate) {
		this.deliveredDate = deliveredDate;
	}
}
