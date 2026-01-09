package com.cware.netshopping.pawemp.order.model;

public class SetClaimRejectRequest {

	private long claimBundleNo;
	private String claimType;
	private String rejectReasonCode;
	private String rejectReasonDetail;
	private String shipMethod;
	private String parcelCompanyCode;
	private String invoiceNo;
	private String scheduleShipDate;
	private String shipMethodMessage;
	
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
	public String getRejectReasonCode() {
		return rejectReasonCode;
	}
	public void setRejectReasonCode(String rejectReasonCode) {
		this.rejectReasonCode = rejectReasonCode;
	}
	public String getRejectReasonDetail() {
		return rejectReasonDetail;
	}
	public void setRejectReasonDetail(String rejectReasonDetail) {
		this.rejectReasonDetail = rejectReasonDetail;
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
