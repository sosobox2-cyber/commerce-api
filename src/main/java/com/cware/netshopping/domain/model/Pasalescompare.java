package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Pasalescompare extends AbstractModel {
	private static final long serialVersionUID = 1L;

	private Timestamp gatherDate;
	private String compareNo;
	private String settleDt;
	private String slipProcDate;
	private String paCode;
	private String paAccountGb;
	private String paOrderNo;
	private String paOrderSeq;
	private String paShipNo;
	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String itemNo;
	private String goodsCode;
	private String optionNo;
	private String goodsdtCode;
	private int paSettleQty;
	private int settleQty;
	private double paSettleAmt;
	private double settleAmt;
	private double paSettleNet;
	private double settleNet;
	private double paSettleVat;
	private double settleVat;
	private double paSaleAmt;
	private double saleAmt;
	private double paDcAmt;
	private double dcAmt;
	private double paRsaleAmt;
	private double rsaleAmt;
	private String paDcRate;
	private String dcRate;
	private double paShppcostAmt;
	private double paShppcostNet;
	private double paShppcostVat;
	private String compareBase;
	private String compareGubun;
	private String compareCode;
	private String procYn;
	private String procReason;
	private String closeYn;
	private String transYn;
	private String remark;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;
	private String paStatusGb;
	private String userProcId;
	private Timestamp userProcDate;

	public Timestamp getGatherDate() {
		return gatherDate;
	}

	public void setGatherDate(Timestamp gatherDate) {
		this.gatherDate = gatherDate;
	}

	public String getCompareNo() {
		return compareNo;
	}

	public void setCompareNo(String compareNo) {
		this.compareNo = compareNo;
	}

	public String getSettleDt() {
		return settleDt;
	}

	public void setSettleDt(String settleDt) {
		this.settleDt = settleDt;
	}

	public String getSlipProcDate() {
		return slipProcDate;
	}

	public void setSlipProcDate(String slipProcDate) {
		this.slipProcDate = slipProcDate;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getPaAccountGb() {
		return paAccountGb;
	}

	public void setPaAccountGb(String paAccountGb) {
		this.paAccountGb = paAccountGb;
	}

	public String getPaOrderNo() {
		return paOrderNo;
	}

	public void setPaOrderNo(String paOrderNo) {
		this.paOrderNo = paOrderNo;
	}

	public String getPaOrderSeq() {
		return paOrderSeq;
	}

	public void setPaOrderSeq(String paOrderSeq) {
		this.paOrderSeq = paOrderSeq;
	}

	public String getPaShipNo() {
		return paShipNo;
	}

	public void setPaShipNo(String paShipNo) {
		this.paShipNo = paShipNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderGSeq() {
		return orderGSeq;
	}

	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}

	public String getOrderDSeq() {
		return orderDSeq;
	}

	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}

	public String getOrderWSeq() {
		return orderWSeq;
	}

	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getOptionNo() {
		return optionNo;
	}

	public void setOptionNo(String optionNo) {
		this.optionNo = optionNo;
	}

	public String getGoodsdtCode() {
		return goodsdtCode;
	}

	public void setGoodsdtCode(String goodsdtCode) {
		this.goodsdtCode = goodsdtCode;
	}

	public int getPaSettleQty() {
		return paSettleQty;
	}

	public void setPaSettleQty(int paSettleQty) {
		this.paSettleQty = paSettleQty;
	}

	public int getSettleQty() {
		return settleQty;
	}

	public void setSettleQty(int settleQty) {
		this.settleQty = settleQty;
	}

	public double getPaSettleAmt() {
		return paSettleAmt;
	}

	public void setPaSettleAmt(double paSettleAmt) {
		this.paSettleAmt = paSettleAmt;
	}

	public double getSettleAmt() {
		return settleAmt;
	}

	public void setSettleAmt(double settleAmt) {
		this.settleAmt = settleAmt;
	}

	public double getPaSettleNet() {
		return paSettleNet;
	}

	public void setPaSettleNet(double paSettleNet) {
		this.paSettleNet = paSettleNet;
	}

	public double getSettleNet() {
		return settleNet;
	}

	public void setSettleNet(double settleNet) {
		this.settleNet = settleNet;
	}

	public double getPaSettleVat() {
		return paSettleVat;
	}

	public void setPaSettleVat(double paSettleVat) {
		this.paSettleVat = paSettleVat;
	}

	public double getSettleVat() {
		return settleVat;
	}

	public void setSettleVat(double settleVat) {
		this.settleVat = settleVat;
	}

	public double getPaSaleAmt() {
		return paSaleAmt;
	}

	public void setPaSaleAmt(double paSaleAmt) {
		this.paSaleAmt = paSaleAmt;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public double getPaDcAmt() {
		return paDcAmt;
	}

	public void setPaDcAmt(double paDcAmt) {
		this.paDcAmt = paDcAmt;
	}

	public double getDcAmt() {
		return dcAmt;
	}

	public void setDcAmt(double dcAmt) {
		this.dcAmt = dcAmt;
	}

	public double getPaRsaleAmt() {
		return paRsaleAmt;
	}

	public void setPaRsaleAmt(double paRsaleAmt) {
		this.paRsaleAmt = paRsaleAmt;
	}

	public double getRsaleAmt() {
		return rsaleAmt;
	}

	public void setRsaleAmt(double rsaleAmt) {
		this.rsaleAmt = rsaleAmt;
	}

	public String getPaDcRate() {
		return paDcRate;
	}

	public void setPaDcRate(String paDcRate) {
		this.paDcRate = paDcRate;
	}

	public String getDcRate() {
		return dcRate;
	}

	public void setDcRate(String dcRate) {
		this.dcRate = dcRate;
	}

	public double getPaShppcostAmt() {
		return paShppcostAmt;
	}

	public void setPaShppcostAmt(double paShppcostAmt) {
		this.paShppcostAmt = paShppcostAmt;
	}

	public double getPaShppcostNet() {
		return paShppcostNet;
	}

	public void setPaShppcostNet(double paShppcostNet) {
		this.paShppcostNet = paShppcostNet;
	}

	public double getPaShppcostVat() {
		return paShppcostVat;
	}

	public void setPaShppcostVat(double paShppcostVat) {
		this.paShppcostVat = paShppcostVat;
	}

	public String getCompareBase() {
		return compareBase;
	}

	public void setCompareBase(String compareBase) {
		this.compareBase = compareBase;
	}

	public String getCompareGubun() {
		return compareGubun;
	}

	public void setCompareGubun(String compareGubun) {
		this.compareGubun = compareGubun;
	}

	public String getCompareCode() {
		return compareCode;
	}

	public void setCompareCode(String compareCode) {
		this.compareCode = compareCode;
	}

	public String getProcYn() {
		return procYn;
	}

	public void setProcYn(String procYn) {
		this.procYn = procYn;
	}

	public String getProcReason() {
		return procReason;
	}

	public void setProcReason(String procReason) {
		this.procReason = procReason;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getTransYn() {
		return transYn;
	}

	public void setTransYn(String transYn) {
		this.transYn = transYn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getPaStatusGb() {
		return paStatusGb;
	}

	public void setPaStatusGb(String paStatusGb) {
		this.paStatusGb = paStatusGb;
	}

	public String getUserProcId() {
		return userProcId;
	}

	public void setUserProcId(String userProcId) {
		this.userProcId = userProcId;
	}

	public Timestamp getUserProcDate() {
		return userProcDate;
	}

	public void setUserProcDate(Timestamp userProcDate) {
		this.userProcDate = userProcDate;
	}
}