package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaIntpSettlement extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String paIntpSettlementNo;
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
	private long parentPrdNo;
	private String parentPrdNm;
	private long prdNo;
	private String optFnm;
	private double oldSaleUnitcost;
	private double realSaleUnitcost;
	private double saleFeeRt;
	private double inDcCouponAmt;
	private double entrDirectDcAmt;
	private double entrDcCouponAmt;
	private long saleQty;
	private double saleAmt;
	private double saleFee;
	private String delvEndDt;
	private String transferDt;
	private double preUseUnitcost;
	private double ipointDcAmt;
	private double inpkOnusAmt;
	private double depositAmt;

	public String getPaIntpSettlementNo() {
		return paIntpSettlementNo;
	}

	public void setPaIntpSettlementNo(String paIntpSettlementNo) {
		this.paIntpSettlementNo = paIntpSettlementNo;
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

	public long getParentPrdNo() {
		return parentPrdNo;
	}

	public void setParentPrdNo(long parentPrdNo) {
		this.parentPrdNo = parentPrdNo;
	}

	public String getParentPrdNm() {
		return parentPrdNm;
	}

	public void setParentPrdNm(String parentPrdNm) {
		this.parentPrdNm = parentPrdNm;
	}

	public long getPrdNo() {
		return prdNo;
	}

	public void setPrdNo(long prdNo) {
		this.prdNo = prdNo;
	}

	public String getOptFnm() {
		return optFnm;
	}

	public void setOptFnm(String optFnm) {
		this.optFnm = optFnm;
	}

	public double getOldSaleUnitcost() {
		return oldSaleUnitcost;
	}

	public void setOldSaleUnitcost(double oldSaleUnitcost) {
		this.oldSaleUnitcost = oldSaleUnitcost;
	}

	public double getRealSaleUnitcost() {
		return realSaleUnitcost;
	}

	public void setRealSaleUnitcost(double realSaleUnitcost) {
		this.realSaleUnitcost = realSaleUnitcost;
	}

	public double getSaleFeeRt() {
		return saleFeeRt;
	}

	public void setSaleFeeRt(double saleFeeRt) {
		this.saleFeeRt = saleFeeRt;
	}

	public double getInDcCouponAmt() {
		return inDcCouponAmt;
	}

	public void setInDcCouponAmt(double inDcCouponAmt) {
		this.inDcCouponAmt = inDcCouponAmt;
	}

	public double getEntrDirectDcAmt() {
		return entrDirectDcAmt;
	}

	public void setEntrDirectDcAmt(double entrDirectDcAmt) {
		this.entrDirectDcAmt = entrDirectDcAmt;
	}

	public double getEntrDcCouponAmt() {
		return entrDcCouponAmt;
	}

	public void setEntrDcCouponAmt(double entrDcCouponAmt) {
		this.entrDcCouponAmt = entrDcCouponAmt;
	}

	public long getSaleQty() {
		return saleQty;
	}

	public void setSaleQty(long saleQty) {
		this.saleQty = saleQty;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public double getSaleFee() {
		return saleFee;
	}

	public void setSaleFee(double saleFee) {
		this.saleFee = saleFee;
	}

	public String getDelvEndDt() {
		return delvEndDt;
	}

	public void setDelvEndDt(String delvEndDt) {
		this.delvEndDt = delvEndDt;
	}

	public String getTransferDt() {
		return transferDt;
	}

	public void setTransferDt(String transferDt) {
		this.transferDt = transferDt;
	}

	public double getPreUseUnitcost() {
		return preUseUnitcost;
	}

	public void setPreUseUnitcost(double preUseUnitcost) {
		this.preUseUnitcost = preUseUnitcost;
	}

	public double getIpointDcAmt() {
		return ipointDcAmt;
	}

	public void setIpointDcAmt(double ipointDcAmt) {
		this.ipointDcAmt = ipointDcAmt;
	}

	public double getInpkOnusAmt() {
		return inpkOnusAmt;
	}

	public void setInpkOnusAmt(double inpkOnusAmt) {
		this.inpkOnusAmt = inpkOnusAmt;
	}

	public double getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(double depositAmt) {
		this.depositAmt = depositAmt;
	}
}