package com.cware.netshopping.pa11st.v2.domain;

import java.sql.Timestamp;

public class Pa11stCnShipCost {

	private String entpCode;
	private String entpManSeq;
	private String paCode;
	private String shipCostCode;
	private double shipCostBaseAmt;
	private double ordCostAmt;
	private String paAddrSeq;
	private String paAddrNm;
	private String transTargetYn;
	private Timestamp transDate;
	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getEntpManSeq() {
		return entpManSeq;
	}

	public void setEntpManSeq(String entpManSeq) {
		this.entpManSeq = entpManSeq;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public double getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}

	public void setShipCostBaseAmt(double shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}

	public double getOrdCostAmt() {
		return ordCostAmt;
	}

	public void setOrdCostAmt(double ordCostAmt) {
		this.ordCostAmt = ordCostAmt;
	}

	public String getPaAddrSeq() {
		return paAddrSeq;
	}

	public void setPaAddrSeq(String paAddrSeq) {
		this.paAddrSeq = paAddrSeq;
	}

	public String getPaAddrNm() {
		return paAddrNm;
	}

	public void setPaAddrNm(String paAddrNm) {
		this.paAddrNm = paAddrNm;
	}

	public String getTransTargetYn() {
		return transTargetYn;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}

	public Timestamp getTransDate() {
		return transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
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

	@Override
	public String toString() {
		return "Pa11stCnShipCost [entpCode=" + entpCode + ", entpManSeq=" + entpManSeq + ", paCode=" + paCode
				+ ", shipCostCode=" + shipCostCode + ", shipCostBaseAmt=" + shipCostBaseAmt + ", ordCostAmt="
				+ ordCostAmt + ", paAddrSeq=" + paAddrSeq + ", paAddrNm=" + paAddrNm + ", transTargetYn="
				+ transTargetYn + ", transDate=" + transDate + ", insertId=" + insertId + ", insertDate=" + insertDate
				+ ", modifyId=" + modifyId + ", modifyDate=" + modifyDate + "]";
	}

}
