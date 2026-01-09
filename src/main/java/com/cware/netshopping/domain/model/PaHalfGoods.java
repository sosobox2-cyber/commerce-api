package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaHalfGoods extends AbstractModel {
	private static final long serialVersionUID = 1L; 
	
	private String paCode;
	private String goodsCode;
	private String paGroupCode;
	private String productNo;
	private String goodsName;
	private String paSaleGb;
	private String paStatus;
	private String transOrderAbleQty;
	private String paLmsdKey;
	private String transTargetYn;
	private String transSaleYn;
	private String returnNote;
	private String paBrandNo;
	private String manufcoNm;
	private String originCode;
	private String originType;
	private String mdNo;
	private String procStatCd;
	private String massTargetYn;
	private String massTargetCode;
	private String massTargetEp;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	private String lastSyncDate;
	private String paShipCostCode;
	private String prdPrcNo;
	
	private double arsDcAmt;
	private double lumpSumDcAmt;
	private double couponDcAmt;
	
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getPaGroupCode() {
		return paGroupCode;
	}
	public void setPaGroupCode(String paGroupCode) {
		this.paGroupCode = paGroupCode;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPaSaleGb() {
		return paSaleGb;
	}
	public void setPaSaleGb(String paSaleGb) {
		this.paSaleGb = paSaleGb;
	}
	public String getPaStatus() {
		return paStatus;
	}
	public void setPaStatus(String paStatus) {
		this.paStatus = paStatus;
	}
	public String getTransOrderAbleQty() {
		return transOrderAbleQty;
	}
	public void setTransOrderAbleQty(String transOrderAbleQty) {
		this.transOrderAbleQty = transOrderAbleQty;
	}
	public String getPaLmsdKey() {
		return paLmsdKey;
	}
	public void setPaLmsdKey(String paLmsdKey) {
		this.paLmsdKey = paLmsdKey;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public String getTransSaleYn() {
		return transSaleYn;
	}
	public void setTransSaleYn(String transSaleYn) {
		this.transSaleYn = transSaleYn;
	}
	public String getReturnNote() {
		return returnNote;
	}
	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}

	public String getManufcoNm() {
		return manufcoNm;
	}
	public void setManufcoNm(String manufcoNm) {
		this.manufcoNm = manufcoNm;
	}
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	public String getOriginType() {
		return originType;
	}
	public void setOriginType(String originType) {
		this.originType = originType;
	}
	public String getMdNo() {
		return mdNo;
	}
	public void setMdNo(String mdNo) {
		this.mdNo = mdNo;
	}
	public String getProcStatCd() {
		return procStatCd;
	}
	public void setProcStatCd(String procStatCd) {
		this.procStatCd = procStatCd;
	}
	public String getMassTargetYn() {
		return massTargetYn;
	}
	public void setMassTargetYn(String massTargetYn) {
		this.massTargetYn = massTargetYn;
	}
	public String getMassTargetCode() {
		return massTargetCode;
	}
	public void setMassTargetCode(String massTargetCode) {
		this.massTargetCode = massTargetCode;
	}
	public String getMassTargetEp() {
		return massTargetEp;
	}
	public void setMassTargetEp(String massTargetEp) {
		this.massTargetEp = massTargetEp;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(String lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPaShipCostCode() {
		return paShipCostCode;
	}
	public void setPaShipCostCode(String paShipCostCode) {
		this.paShipCostCode = paShipCostCode;
	}
	public String getPrdPrcNo() {
		return prdPrcNo;
	}
	public void setPrdPrcNo(String prdPrcNo) {
		this.prdPrcNo = prdPrcNo;
	}
	public double getArsDcAmt() {
		return arsDcAmt;
	}
	public void setArsDcAmt(double arsDcAmt) {
		this.arsDcAmt = arsDcAmt;
	}
	public double getLumpSumDcAmt() {
		return lumpSumDcAmt;
	}
	public void setLumpSumDcAmt(double lumpSumDcAmt) {
		this.lumpSumDcAmt = lumpSumDcAmt;
	}
	public double getCouponDcAmt() {
		return couponDcAmt;
	}
	public void setCouponDcAmt(double couponDcAmt) {
		this.couponDcAmt = couponDcAmt;
	}
	public String getPaBrandNo() {
		return paBrandNo;
	}
	public void setPaBrandNo(String paBrandNo) {
		this.paBrandNo = paBrandNo;
	}
	
	
}
