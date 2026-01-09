package com.cware.netshopping.patmon.v2.domain;

import java.sql.Timestamp;

public class PaTmonShipCost {

	private String paCode;
	private String entpCode;
	private String productType; // 배송상품타입[DP04:화물설치,DP05:주문제작,DP07:일반상품]
	private String shipManSeq; // 출고지순번
	private String returnManSeq; // 회수지순번
	private String shipCostCode;
	private Timestamp applyDate;
	private String noShipIsland; // 도서산간배송부가여부

	private String paShipPolicyNo; // 티몬배송비정책번호
	private int shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double changeCost;
	private double islandCost;
	private double jejuCost;

	private String insertId;
	private Timestamp insertDate;
	private String modifyId;
	private Timestamp modifyDate;

	private String paShipManSeq; // 출고지번호
	private String paReturnManSeq; // 회수지번호

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getEntpCode() {
		return entpCode;
	}

	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getShipManSeq() {
		return shipManSeq;
	}

	public void setShipManSeq(String shipManSeq) {
		this.shipManSeq = shipManSeq;
	}

	public String getReturnManSeq() {
		return returnManSeq;
	}

	public void setReturnManSeq(String returnManSeq) {
		this.returnManSeq = returnManSeq;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public Timestamp getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}

	public String getNoShipIsland() {
		return noShipIsland;
	}

	public void setNoShipIsland(String noShipIsland) {
		this.noShipIsland = noShipIsland;
	}

	public int getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}

	public void setShipCostBaseAmt(int shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}

	public double getOrdCost() {
		return ordCost;
	}

	public void setOrdCost(double ordCost) {
		this.ordCost = ordCost;
	}

	public double getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(double returnCost) {
		this.returnCost = returnCost;
	}

	public double getChangeCost() {
		return changeCost;
	}

	public void setChangeCost(double changeCost) {
		this.changeCost = changeCost;
	}

	public double getIslandCost() {
		return islandCost;
	}

	public void setIslandCost(double islandCost) {
		this.islandCost = islandCost;
	}

	public double getJejuCost() {
		return jejuCost;
	}

	public void setJejuCost(double jejuCost) {
		this.jejuCost = jejuCost;
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

	public String getPaShipPolicyNo() {
		return paShipPolicyNo;
	}

	public void setPaShipPolicyNo(String paShipPolicyNo) {
		this.paShipPolicyNo = paShipPolicyNo;
	}

	public String getPaShipManSeq() {
		return paShipManSeq;
	}

	public void setPaShipManSeq(String paShipManSeq) {
		this.paShipManSeq = paShipManSeq;
	}

	public String getPaReturnManSeq() {
		return paReturnManSeq;
	}

	public void setPaReturnManSeq(String paReturnManSeq) {
		this.paReturnManSeq = paReturnManSeq;
	}

	@Override
	public String toString() {
		return "PaTmonShipCost [paCode=" + paCode + ", entpCode=" + entpCode + ", productType=" + productType
				+ ", shipManSeq=" + shipManSeq + ", returnManSeq=" + returnManSeq + ", shipCostCode=" + shipCostCode
				+ ", applyDate=" + applyDate + ", noShipIsland=" + noShipIsland + ", paShipPolicyNo=" + paShipPolicyNo
				+ ", shipCostBaseAmt=" + shipCostBaseAmt + ", ordCost=" + ordCost + ", returnCost=" + returnCost
				+ ", changeCost=" + changeCost + ", islandCost=" + islandCost + ", jejuCost=" + jejuCost + ", insertId="
				+ insertId + ", insertDate=" + insertDate + ", modifyId=" + modifyId + ", modifyDate=" + modifyDate
				+ ", paShipManSeq=" + paShipManSeq + ", paReturnManSeq=" + paReturnManSeq + "]";
	}

}
