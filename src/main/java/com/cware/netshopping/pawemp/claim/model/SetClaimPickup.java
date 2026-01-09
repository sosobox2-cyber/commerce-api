package com.cware.netshopping.pawemp.claim.model;

public class SetClaimPickup {
	private long claimBundleNo;
	private String claimType;
	private String shipMethod;
	private String parcelCompanyCode;
	private String invoiceNo;
	private String scheduleShipDate;
	public long getClaimBundleNo() {
		return claimBundleNo;
	}
	public void setClaimBundleNo(long claimBundleNo) {
		this.claimBundleNo = claimBundleNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
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
	
	
}
