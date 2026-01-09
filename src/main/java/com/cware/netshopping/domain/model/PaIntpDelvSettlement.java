package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaIntpDelvSettlement extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String delvSettleNo;
	private String revLogNm;
	private String cnclYn;
	private String setlModTp;
	private String revDt;
	private String ordclmNo;
	private String ordSeq;
	private String oriOrdclmNo;
	private String clmSeq;
	private String ordNm;
	private String rcvrNm;
	private double delvAmt;
	private double delvRtnAmt;
	private String fee24;
	private String fee22;
	private String transferDt;

	public String getDelvSettleNo() {
		return delvSettleNo;
	}

	public void setDelvSettleNo(String delvSettleNo) {
		this.delvSettleNo = delvSettleNo;
	}

	public String getRevLogNm() {
		return revLogNm;
	}

	public void setRevLogNm(String revLogNm) {
		this.revLogNm = revLogNm;
	}

	public String getCnclYn() {
		return cnclYn;
	}

	public void setCnclYn(String cnclYn) {
		this.cnclYn = cnclYn;
	}

	public String getSetlModTp() {
		return setlModTp;
	}

	public void setSetlModTp(String setlModTp) {
		this.setlModTp = setlModTp;
	}

	public String getRevDt() {
		return revDt;
	}

	public void setRevDt(String revDt) {
		this.revDt = revDt;
	}

	public String getOrdclmNo() {
		return ordclmNo;
	}

	public void setOrdclmNo(String ordclmNo) {
		this.ordclmNo = ordclmNo;
	}

	public String getOrdSeq() {
		return ordSeq;
	}

	public void setOrdSeq(String ordSeq) {
		this.ordSeq = ordSeq;
	}

	public String getOriOrdclmNo() {
		return oriOrdclmNo;
	}

	public void setOriOrdclmNo(String oriOrdclmNo) {
		this.oriOrdclmNo = oriOrdclmNo;
	}

	public String getClmSeq() {
		return clmSeq;
	}

	public void setClmSeq(String clmSeq) {
		this.clmSeq = clmSeq;
	}

	public String getOrdNm() {
		return ordNm;
	}

	public void setOrdNm(String ordNm) {
		this.ordNm = ordNm;
	}

	public String getRcvrNm() {
		return rcvrNm;
	}

	public void setRcvrNm(String rcvrNm) {
		this.rcvrNm = rcvrNm;
	}

	public double getDelvAmt() {
		return delvAmt;
	}

	public void setDelvAmt(double delvAmt) {
		this.delvAmt = delvAmt;
	}

	public double getDelvRtnAmt() {
		return delvRtnAmt;
	}

	public void setDelvRtnAmt(double delvRtnAmt) {
		this.delvRtnAmt = delvRtnAmt;
	}

	public String getFee24() {
		return fee24;
	}

	public void setFee24(String fee24) {
		this.fee24 = fee24;
	}

	public String getFee22() {
		return fee22;
	}

	public void setFee22(String fee22) {
		this.fee22 = fee22;
	}

	public String getTransferDt() {
		return transferDt;
	}

	public void setTransferDt(String transferDt) {
		this.transferDt = transferDt;
	}

}
