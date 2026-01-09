package com.cware.netshopping.pawemp.order.model;

public class SetOrderDeliveryRequest {
	
	private long bundleNo;
	private String shipMethod;
	private String parcelCompanyCode;
	private String invoiceNo;
	private String scheduleShipDate;
	private String shipMethodMessage;
	
	public long getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(long bundleNo) {
		this.bundleNo = bundleNo;
	}
	public String getShipMethod() {
		return shipMethod;
	}
	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}
	public String getParcelCompanyCode() {
		return parcelCompanyCode;
	}
	public void setParcelCompanyCode(String parcelCompanyCode) {
		this.parcelCompanyCode = parcelCompanyCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getScheduleShipDate() {
		return scheduleShipDate;
	}
	public void setScheduleShipDate(String scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}
	public String getShipMethodMessage() {
		return shipMethodMessage;
	}
	public void setShipMethodMessage(String shipMethodMessage) {
		this.shipMethodMessage = shipMethodMessage;
	}
}
