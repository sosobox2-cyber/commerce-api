package com.cware.netshopping.pafaple.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaFapleClaimList extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String itemId;
	private String paOrderGb;
	private String processFlag;
	private String processFlagName;
	private String sendBackId;
	private String sendBackSeq;
	private Timestamp receiptDate;
	private int quantity;
	private String returningType;
	private String reasonTypeName;
	private String chargeRateType;
	private long chargeRate;
	private long fe2;
	private long fe3;
	private String recallCharge;
	private String goodsFlow;
	private String deliveryName;
	private String invoiceNo;
	private Timestamp invoiceDate;
	private String dlvLastDay;
	private String processDate2;
	private String returnDeferYn;
	private Timestamp holdDate;
	private String holdType;
	private String processDate;
	private String processEmpN;
	private String isReProcYn;
	private Timestamp reProcCreated;
	private String reCompleteEmpN;
	private String isRecallCancelYn;
	private Timestamp cancelCreated;
	private String claimId;
	
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
	public String getProcessFlag() {
		return processFlag;
	}
	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}
	public String getProcessFlagName() {
		return processFlagName;
	}
	public void setProcessFlagName(String processFlagName) {
		this.processFlagName = processFlagName;
	}
	public String getSendBackId() {
		return sendBackId;
	}
	public void setSendBackId(String sendBackId) {
		this.sendBackId = sendBackId;
	}
	public String getSendBackSeq() {
		return sendBackSeq;
	}
	public void setSendBackSeq(String sendBackSeq) {
		this.sendBackSeq = sendBackSeq;
	}
	public Timestamp getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Timestamp receiptDate) {
		this.receiptDate = receiptDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getReturningType() {
		return returningType;
	}
	public void setReturningType(String returningType) {
		this.returningType = returningType;
	}
	public String getReasonTypeName() {
		return reasonTypeName;
	}
	public void setReasonTypeName(String reasonTypeName) {
		this.reasonTypeName = reasonTypeName;
	}
	public String getChargeRateType() {
		return chargeRateType;
	}
	public void setChargeRateType(String chargeRateType) {
		this.chargeRateType = chargeRateType;
	}
	public long getChargeRate() {
		return chargeRate;
	}
	public void setChargeRate(long chargeRate) {
		this.chargeRate = chargeRate;
	}
	public long getFe2() {
		return fe2;
	}
	public void setFe2(long fe2) {
		this.fe2 = fe2;
	}
	public long getFe3() {
		return fe3;
	}
	public void setFe3(long fe3) {
		this.fe3 = fe3;
	}
	public String getRecallCharge() {
		return recallCharge;
	}
	public void setRecallCharge(String recallCharge) {
		this.recallCharge = recallCharge;
	}
	public String getGoodsFlow() {
		return goodsFlow;
	}
	public void setGoodsFlow(String goodsFlow) {
		this.goodsFlow = goodsFlow;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getDlvLastDay() {
		return dlvLastDay;
	}
	public void setDlvLastDay(String dlvLastDay) {
		this.dlvLastDay = dlvLastDay;
	}
	public String getProcessDate2() {
		return processDate2;
	}
	public void setProcessDate2(String processDate2) {
		this.processDate2 = processDate2;
	}
	public String getReturnDeferYn() {
		return returnDeferYn;
	}
	public void setReturnDeferYn(String returnDeferYn) {
		this.returnDeferYn = returnDeferYn;
	}
	public Timestamp getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(Timestamp holdDate) {
		this.holdDate = holdDate;
	}
	public String getHoldType() {
		return holdType;
	}
	public void setHoldType(String holdType) {
		this.holdType = holdType;
	}
	public String getProcessDate() {
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	public String getProcessEmpN() {
		return processEmpN;
	}
	public void setProcessEmpN(String processEmpN) {
		this.processEmpN = processEmpN;
	}
	public String getIsReProcYn() {
		return isReProcYn;
	}
	public void setIsReProcYn(String isReProcYn) {
		this.isReProcYn = isReProcYn;
	}
	public Timestamp getReProcCreated() {
		return reProcCreated;
	}
	public void setReProcCreated(Timestamp reProcCreated) {
		this.reProcCreated = reProcCreated;
	}
	public String getReCompleteEmpN() {
		return reCompleteEmpN;
	}
	public void setReCompleteEmpN(String reCompleteEmpN) {
		this.reCompleteEmpN = reCompleteEmpN;
	}
	public String getIsRecallCancelYn() {
		return isRecallCancelYn;
	}
	public void setIsRecallCancelYn(String isRecallCancelYn) {
		this.isRecallCancelYn = isRecallCancelYn;
	}
	public Timestamp getCancelCreated() {
		return cancelCreated;
	}
	public void setCancelCreated(Timestamp cancelCreated) {
		this.cancelCreated = cancelCreated;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

}