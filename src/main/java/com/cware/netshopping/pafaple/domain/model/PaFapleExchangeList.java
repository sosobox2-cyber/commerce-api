package com.cware.netshopping.pafaple.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleExchangeList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String itemId;
	private String paOrderGb;
	private String claimStatus;
	private String claimStatusName;
	private String claimId;
	private String exchangeOrderId;
	private String exchangeItemId;
	private long deliveryAmt;
	private String billToName;
	private int orderQuantity;
	private String reasonTypeName;
	private String returnInvoiceType;
	private String invoiceCoName;
	private String invoiceNo;
	private Timestamp createdReturn;
	private Timestamp createdRegist;
	private Timestamp modified;
	private String productName;
	private String sku;
	private String attrText;
	private long oadjustAdjustedprice;
	private String feeTarget;
	private String rateTypeName;
	private String regPath;
	private Timestamp deliveryDate;
	private String deliveryCoName;
	private String exInvoiceNo;
	private Timestamp dlvLastProcTime;
	private String exShipToName;
	private String exShipToMobile;
	private String exShipToZip;
	private String exShipToPhone;
	private String exShipToStreet;
	private Timestamp createdDefer;
	private String exchangeStepName;
	private Timestamp createdSend;
	private Timestamp createdWithdrawal;
	private String withdrawalReason;
	private String withdrawalEmpName;
	private String exSku;
	private String exName;
	private String exAttrText;
	private String exGoodsId;
	private String reShipToName;
	private String reShipToZipcode;
	private String reShipToMobile;
	private String reShipToPhone;
	private String reShipToStreet1;
	private String reShipToStreet2;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getPaOrderGb() {
		return paOrderGb;
	}
	public void setPaOrderGb(String paOrderGb) {
		this.paOrderGb = paOrderGb;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimStatusName() {
		return claimStatusName;
	}
	public void setClaimStatusName(String claimStatusName) {
		this.claimStatusName = claimStatusName;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getExchangeOrderId() {
		return exchangeOrderId;
	}
	public void setExchangeOrderId(String exchangeOrderId) {
		this.exchangeOrderId = exchangeOrderId;
	}
	public long getDeliveryAmt() {
		return deliveryAmt;
	}
	public void setDeliveryAmt(long deliveryAmt) {
		this.deliveryAmt = deliveryAmt;
	}
	public String getBillToName() {
		return billToName;
	}
	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getReasonTypeName() {
		return reasonTypeName;
	}
	public void setReasonTypeName(String reasonTypeName) {
		this.reasonTypeName = reasonTypeName;
	}
	public String getReturnInvoiceType() {
		return returnInvoiceType;
	}
	public void setReturnInvoiceType(String returnInvoiceType) {
		this.returnInvoiceType = returnInvoiceType;
	}
	public String getInvoiceCoName() {
		return invoiceCoName;
	}
	public void setInvoiceCoName(String invoiceCoName) {
		this.invoiceCoName = invoiceCoName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Timestamp getCreatedReturn() {
		return createdReturn;
	}
	public void setCreatedReturn(Timestamp createdReturn) {
		this.createdReturn = createdReturn;
	}
	public Timestamp getCreatedRegist() {
		return createdRegist;
	}
	public void setCreatedRegist(Timestamp createdRegist) {
		this.createdRegist = createdRegist;
	}
	public Timestamp getModified() {
		return modified;
	}
	public void setModified(Timestamp modified) {
		this.modified = modified;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getAttrText() {
		return attrText;
	}
	public void setAttrText(String attrText) {
		this.attrText = attrText;
	}
	public long getOadjustAdjustedprice() {
		return oadjustAdjustedprice;
	}
	public void setOadjustAdjustedprice(long oadjustAdjustedprice) {
		this.oadjustAdjustedprice = oadjustAdjustedprice;
	}
	public String getFeeTarget() {
		return feeTarget;
	}
	public void setFeeTarget(String feeTarget) {
		this.feeTarget = feeTarget;
	}
	public String getRateTypeName() {
		return rateTypeName;
	}
	public void setRateTypeName(String rateTypeName) {
		this.rateTypeName = rateTypeName;
	}
	public String getRegPath() {
		return regPath;
	}
	public void setRegPath(String regPath) {
		this.regPath = regPath;
	}
	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryCoName() {
		return deliveryCoName;
	}
	public void setDeliveryCoName(String deliveryCoName) {
		this.deliveryCoName = deliveryCoName;
	}
	public String getExInvoiceNo() {
		return exInvoiceNo;
	}
	public void setExInvoiceNo(String exInvoiceNo) {
		this.exInvoiceNo = exInvoiceNo;
	}
	public Timestamp getDlvLastProcTime() {
		return dlvLastProcTime;
	}
	public void setDlvLastProcTime(Timestamp dlvLastProcTime) {
		this.dlvLastProcTime = dlvLastProcTime;
	}
	public String getExShipToName() {
		return exShipToName;
	}
	public void setExShipToName(String exShipToName) {
		this.exShipToName = exShipToName;
	}
	public String getExShipToMobile() {
		return exShipToMobile;
	}
	public void setExShipToMobile(String exShipToMobile) {
		this.exShipToMobile = exShipToMobile;
	}
	public String getExShipToPhone() {
		return exShipToPhone;
	}
	public void setExShipToPhone(String exShipToPhone) {
		this.exShipToPhone = exShipToPhone;
	}
	public String getExShipToStreet() {
		return exShipToStreet;
	}
	public void setExShipToStreet(String exShipToStreet) {
		this.exShipToStreet = exShipToStreet;
	}
	public Timestamp getCreatedDefer() {
		return createdDefer;
	}
	public void setCreatedDefer(Timestamp createdDefer) {
		this.createdDefer = createdDefer;
	}
	public String getExchangeStepName() {
		return exchangeStepName;
	}
	public void setExchangeStepName(String exchangeStepName) {
		this.exchangeStepName = exchangeStepName;
	}
	public Timestamp getCreatedSend() {
		return createdSend;
	}
	public void setCreatedSend(Timestamp createdSend) {
		this.createdSend = createdSend;
	}
	public Timestamp getCreatedWithdrawal() {
		return createdWithdrawal;
	}
	public void setCreatedWithdrawal(Timestamp createdWithdrawal) {
		this.createdWithdrawal = createdWithdrawal;
	}
	public String getWithdrawalReason() {
		return withdrawalReason;
	}
	public void setWithdrawalReason(String withdrawalReason) {
		this.withdrawalReason = withdrawalReason;
	}
	public String getWithdrawalEmpName() {
		return withdrawalEmpName;
	}
	public void setWithdrawalEmpName(String withdrawalEmpName) {
		this.withdrawalEmpName = withdrawalEmpName;
	}
	public String getExSku() {
		return exSku;
	}
	public void setExSku(String exSku) {
		this.exSku = exSku;
	}
	public String getExName() {
		return exName;
	}
	public void setExName(String exName) {
		this.exName = exName;
	}
	public String getExAttrText() {
		return exAttrText;
	}
	public void setExAttrText(String exAttrText) {
		this.exAttrText = exAttrText;
	}
	public String getExGoodsId() {
		return exGoodsId;
	}
	public void setExGoodsId(String exGoodsId) {
		this.exGoodsId = exGoodsId;
	}
	public String getExchangeItemId() {
		return exchangeItemId;
	}
	public void setExchangeItemId(String exchangeItemId) {
		this.exchangeItemId = exchangeItemId;
	}
	public String getExShipToZip() {
		return exShipToZip;
	}
	public void setExShipToZip(String exShipToZip) {
		this.exShipToZip = exShipToZip;
	}
	public String getReShipToName() {
		return reShipToName;
	}
	public void setReShipToName(String reShipToName) {
		this.reShipToName = reShipToName;
	}
	public String getReShipToZipcode() {
		return reShipToZipcode;
	}
	public void setReShipToZipcode(String reShipToZipcode) {
		this.reShipToZipcode = reShipToZipcode;
	}
	public String getReShipToMobile() {
		return reShipToMobile;
	}
	public void setReShipToMobile(String reShipToMobile) {
		this.reShipToMobile = reShipToMobile;
	}
	public String getReShipToPhone() {
		return reShipToPhone;
	}
	public void setReShipToPhone(String reShipToPhone) {
		this.reShipToPhone = reShipToPhone;
	}
	public String getReShipToStreet1() {
		return reShipToStreet1;
	}
	public void setReShipToStreet1(String reShipToStreet1) {
		this.reShipToStreet1 = reShipToStreet1;
	}
	public String getReShipToStreet2() {
		return reShipToStreet2;
	}
	public void setReShipToStreet2(String reShipToStreet2) {
		this.reShipToStreet2 = reShipToStreet2;
	}
}