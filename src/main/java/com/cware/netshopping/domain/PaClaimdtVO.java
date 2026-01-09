package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Claimdt;

public class PaClaimdtVO extends Claimdt implements Cloneable{

	private static final long serialVersionUID = 1L;
	public PaClaimdtVO(){ super();}
	
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	private String cancelFlag;
	private String procNote;
	private String counselProcNote;
	private String stockCheckYn;
	private String claimType;
	private long orderQty;
	private long syslastOrg;
	private String receiverSeqOrg;
	private String doFlagOrg;
	private String mappingSeq;
	private String receiverPost;
	private String receiverName;
	private String receiverTel;
	private String receiverHp;
	private String receiverAddr;
	private String shipcostChargeYn;
	private String custDelyYn;
	private String entpCode;
	
	private String RcvrMailNo;
	private String RcvrBaseAddr;
	private String RcvrDtlsAddr;
	private String RcvrTypeAdd;
	private String RcvrMailNoSeq;
	
	private String newOrderWeq;
	
	
	public String getNewOrderWeq() {
		return newOrderWeq;
	}

	public void setNewOrderWeq(String newOrderWeq) {
		this.newOrderWeq = newOrderWeq;
	}

	public String getRcvrMailNoSeq() {
		return RcvrMailNoSeq;
	}

	public void setRcvrMailNoSeq(String rcvrMailNoSeq) {
		RcvrMailNoSeq = rcvrMailNoSeq;
	}

	public String getRcvrMailNo() {
		return RcvrMailNo;
	}

	public void setRcvrMailNo(String rcvrMailNo) {
		RcvrMailNo = rcvrMailNo;
	}

	public String getRcvrBaseAddr() {
		return RcvrBaseAddr;
	}

	public void setRcvrBaseAddr(String rcvrBaseAddr) {
		RcvrBaseAddr = rcvrBaseAddr;
	}

	public String getRcvrDtlsAddr() {
		return RcvrDtlsAddr;
	}

	public void setRcvrDtlsAddr(String rcvrDtlsAddr) {
		RcvrDtlsAddr = rcvrDtlsAddr;
	}

	public String getRcvrTypeAdd() {
		return RcvrTypeAdd;
	}

	public void setRcvrTypeAdd(String rcvrTypeAdd) {
		RcvrTypeAdd = rcvrTypeAdd;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getProcNote() {
		return procNote;
	}

	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}

	public String getCounselProcNote() {
		return counselProcNote;
	}

	public void setCounselProcNote(String counselProcNote) {
		this.counselProcNote = counselProcNote;
	}

	public String getStockCheckYn() {
		return stockCheckYn;
	}

	public void setStockCheckYn(String stockCheckYn) {
		this.stockCheckYn = stockCheckYn;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public long getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(long orderQty) {
		this.orderQty = orderQty;
	}

	public long getSyslastOrg() {
		return syslastOrg;
	}

	public void setSyslastOrg(long syslastOrg) {
		this.syslastOrg = syslastOrg;
	}

	public String getReceiverSeqOrg() {
		return receiverSeqOrg;
	}

	public void setReceiverSeqOrg(String receiverSeqOrg) {
		this.receiverSeqOrg = receiverSeqOrg;
	}

	public String getDoFlagOrg() {
		return doFlagOrg;
	}

	public void setDoFlagOrg(String doFlagOrg) {
		this.doFlagOrg = doFlagOrg;
	}

	public String getMappingSeq() {
		return mappingSeq;
	}

	public void setMappingSeq(String mappingSeq) {
		this.mappingSeq = mappingSeq;
	}

	public String getReceiverPost() {
		return receiverPost;
	}

	public void setReceiverPost(String receiverPost) {
		this.receiverPost = receiverPost;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getReceiverHp() {
		return receiverHp;
	}

	public void setReceiverHp(String receiverHp) {
		this.receiverHp = receiverHp;
	}

	public String getReceiverAddr() {
		return receiverAddr;
	}

	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}

	public String getShipcostChargeYn() {
		return shipcostChargeYn;
	}

	public void setShipcostChargeYn(String shipcostChargeYn) {
		this.shipcostChargeYn = shipcostChargeYn;
	}

	public String getCustDelyYn() {
		return custDelyYn;
	}

	public void setCustDelyYn(String custDelyYn) {
		this.custDelyYn = custDelyYn;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	
}
