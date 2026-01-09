package com.cware.netshopping.pawemp.claim.model;

import com.cware.netshopping.pawemp.common.model.ShipAddress;


public class Delivery {
	private String shipStatus;
	private String shipMethod;
	private String scheduleShipDate;
	private String parcelCompany;
	private String invoiceNo;
	private String name;
	private String phone;
	private ShipAddress shipAddress;
	public String getShipStatus() {
		return shipStatus;
	}
	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}
	public String getShipMethod() {
		return shipMethod;
	}
	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}
	public String getScheduleShipDate() {
		return scheduleShipDate;
	}
	public void setScheduleShipDate(String scheduleShipDate) {
		this.scheduleShipDate = scheduleShipDate;
	}
	public String getParcelCompany() {
		return parcelCompany;
	}
	public void setParcelCompany(String parcelCompany) {
		this.parcelCompany = parcelCompany;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ShipAddress getShipAddress() {
		return shipAddress;
	}
	public void setShipAddress(ShipAddress shipAddress) {
		this.shipAddress = shipAddress;
	}
	
	
}
