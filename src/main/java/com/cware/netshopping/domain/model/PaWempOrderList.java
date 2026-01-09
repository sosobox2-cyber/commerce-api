package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

public class PaWempOrderList {
	private static final long serialVersionUID = 1L;
	
	private String paOrderNo;
	private String paShipNo;
	private Timestamp orderDate;
	private Timestamp payDate;
	private Timestamp originShipDate;
	private Timestamp orderConfirmDate;
	private Timestamp orderShipDate;
	private Timestamp shipCompleteDate;
	private String buyerName;
	private String buyerPhone;
	private long shipPrice;
	private String prepayType;
	private String shipType;
	private String deliveryStatus;
	private String deliveryMethod;
	private String deliveryMethodMessage;
	private Timestamp scheduleShipDate;
	private String delyComp;
	private String invoiceNo;
	private String receiverName;
	private String receiverPhone;
	private String customsPin;
	private String zipcode;
	private String baseAddr;
	private String detailAddr;
	private String deliveryMessage;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	
	public String getPaOrderNo() {
		return paOrderNo;
	}
	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}
	public String getPaShipNo() {
		return paShipNo;
	}
	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public Timestamp getPayDate() {
		return payDate;
	}
	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}
	public Timestamp getOriginShipDate() {
		return originShipDate;
	}
	public void setOriginShipDate(Timestamp originShipDate) {
		this.originShipDate = originShipDate;
	}
	public Timestamp getOrderConfirmDate() {
		return orderConfirmDate;
	}
	public void setOrderConfirmDate(Timestamp orderConfirmDate) {
		this.orderConfirmDate = orderConfirmDate;
	}
	public Timestamp getOrderShipDate() {
		return orderShipDate;
	}
	public void setOrderShipDate(Timestamp orderShipDate) {
		this.orderShipDate = orderShipDate;
	}
	public Timestamp getShipCompleteDate() {
		return shipCompleteDate;
	}
	public void setShipCompleteDate(Timestamp shipCompleteDate) {
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
	public String getPrepayType() {
		return prepayType;
	}
	public void setPrepayType(String prepayType) {
		this.prepayType = prepayType;
	}
	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public String getDeliveryMethodMessage() {
		return deliveryMethodMessage;
	}
	public void setDeliveryMethodMessage(String deliveryMethodMessage) {
		this.deliveryMethodMessage = deliveryMethodMessage;
	}
	public Timestamp getScheduleShipDate() {
		return scheduleShipDate;
	}
	public void setScheduleShipDate(Timestamp scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}
	public String getDelyComp() {
		return delyComp;
	}
	public void setDelyComp(String delyComp) {
		this.delyComp = delyComp;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getCustomsPin() {
		return customsPin;
	}
	public void setCustomsPin(String customsPin) {
		this.customsPin = customsPin;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getBaseAddr() {
		return baseAddr;
	}
	public void setBaseAddr(String baseAddr) {
		this.baseAddr = baseAddr;
	}
	public String getDetailAddr() {
		return detailAddr;
	}
	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}
	public String getDeliveryMessage() {
		return deliveryMessage;
	}
	public void setDeliveryMessage(String deliveryMessage) {
		this.deliveryMessage = deliveryMessage;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
