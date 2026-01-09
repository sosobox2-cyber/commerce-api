package com.cware.netshopping.pawemp.common.model;

public class SetParcelDelivery {
	
	private long bundleNo;
	private String parcelCompanyCode;
	private String invoiceNo;
	
	public long getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(long bundleNo) {
		this.bundleNo = bundleNo;
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
}
