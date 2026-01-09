package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktShipCostDt extends AbstractModel {

	private static final long serialVersionUID = 1L;
	private String gmktShipNo;
	private String entpCode;
	private String entpManSeq;
	private String shipCostCode;
	private double shipCostBaseAmt;
	private double ordCostAmt;
	private String transTargetYn;
	private String bundleNo;
	private double islandCost;
	private double jejuCost;

	public String getBundleNo() {
		return bundleNo;
	}

	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}

	public String getGmktShipNo() {
		return gmktShipNo;
	}

	public void setGmktShipNo(String gmktShipNo) {
		this.gmktShipNo = gmktShipNo;
	}

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

	public void setOrdCostAmt(int ordCostAmt) {
		this.ordCostAmt = ordCostAmt;
	}

	public String getTransTargetYn() {
		return transTargetYn;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
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

	@Override
	public String toString() {
		return "PaGmktShipCostDt [gmktShipNo=" + gmktShipNo + ", entpCode=" + entpCode + ", entpManSeq=" + entpManSeq
				+ ", shipCostCode=" + shipCostCode + ", shipCostBaseAmt=" + shipCostBaseAmt + ", ordCostAmt="
				+ ordCostAmt + ", transTargetYn=" + transTargetYn + ", bundleNo=" + bundleNo + ", islandCost="
				+ islandCost + ", jejuCost=" + jejuCost + "]";
	}

}
