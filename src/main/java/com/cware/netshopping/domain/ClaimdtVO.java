package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Claimdt;

public class ClaimdtVO extends  Claimdt implements Cloneable{

	private static final long serialVersionUID = 1L;
	public ClaimdtVO(){ super();}
	
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
	private String claimSubject;//귀책사유주체(입점용)
	private String claimShppFlag; // 상태값 : 11:출하지시후 부분취소, 21: 전체반품,  15:교환출고, 22: 교환회수
	private long syslastOrg;
	private String receiverSeqOrg;
	private String doFlagOrg;
	private String mappingSeq;
	private String receiverPost;
	
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
	public String getClaimSubject() {
		return claimSubject;
	}
	public void setClaimSubject(String claimSubject) {
		this.claimSubject = claimSubject;
	}
	public String getClaimShppFlag() {
		return claimShppFlag;
	}
	public void setClaimShppFlag(String claimShppFlag) {
		this.claimShppFlag = claimShppFlag;
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
}
