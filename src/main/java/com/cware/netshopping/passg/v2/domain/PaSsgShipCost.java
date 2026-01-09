package com.cware.netshopping.passg.v2.domain;

import java.sql.Timestamp;

public class PaSsgShipCost {

	private String paCode;
	private String shppcstAplUnitCd; // 배송비적용단위코드(10:주문금액합산, 30:상품수량별, 40:상품별주문금액합산)
	private String shppcstPlcyDivCd; // 고객배송비정책구분( 10:출고배송비, 20:반품배송비, 60:도서산간, 70:제주 )
	private String collectYn; // 착불여부 (1:착불, 0:선불)
	private String paShipPolicyNo; // SSG배송비정책번호
	private int shipCostBaseAmt;
	private int shipCost;

	private String insertId;
	private Timestamp insertDate;

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getShppcstAplUnitCd() {
		return shppcstAplUnitCd;
	}

	public void setShppcstAplUnitCd(String shppcstAplUnitCd) {
		this.shppcstAplUnitCd = shppcstAplUnitCd;
	}

	public String getShppcstPlcyDivCd() {
		return shppcstPlcyDivCd;
	}

	public void setShppcstPlcyDivCd(String shppcstPlcyDivCd) {
		this.shppcstPlcyDivCd = shppcstPlcyDivCd;
	}

	public String getCollectYn() {
		return collectYn;
	}

	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}

	public String getPaShipPolicyNo() {
		return paShipPolicyNo;
	}

	public void setPaShipPolicyNo(String paShipPolicyNo) {
		this.paShipPolicyNo = paShipPolicyNo;
	}

	public int getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}

	public void setShipCostBaseAmt(int shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}

	public int getShipCost() {
		return shipCost;
	}

	public void setShipCost(int shipCost) {
		this.shipCost = shipCost;
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

	@Override
	public String toString() {
		return "PaSsgShipCost [paCode=" + paCode + ", shppcstAplUnitCd=" + shppcstAplUnitCd + ", shppcstPlcyDivCd="
				+ shppcstPlcyDivCd + ", collectYn=" + collectYn + ", paShipPolicyNo=" + paShipPolicyNo
				+ ", shipCostBaseAmt=" + shipCostBaseAmt + ", shipCost=" + shipCost + ", insertId=" + insertId
				+ ", insertDate=" + insertDate + "]";
	}

}
