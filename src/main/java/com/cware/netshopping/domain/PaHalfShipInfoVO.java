package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaHalfShipInfo;

public class PaHalfShipInfoVO extends PaHalfShipInfo {
	private static final long serialVersionUID = 1L; 

	private double ordCost;
	private double returnCost;
	private double changeCost;
	private double islandCost;
	private double jejuCost;
	private double shipCostBaseAmt;
	private String shipCostFlag;
	private String paCode;
	private String seq;
	private String noShipJejuIsland;
	private String shipCostReceipt;
	
	
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
	public double getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}
	public void setShipCostBaseAmt(double shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}
	public String getShipCostFlag() {
		return shipCostFlag;
	}
	public void setShipCostFlag(String shipCostFlag) {
		this.shipCostFlag = shipCostFlag;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getNoShipJejuIsland() {
		return noShipJejuIsland;
	}
	public void setNoShipJejuIsland(String noShipJejuIsland) {
		this.noShipJejuIsland = noShipJejuIsland;
	}
	public String getShipCostReceipt() {
		return shipCostReceipt;
	}
	public void setShipCostReceipt(String shipCostReceipt) {
		this.shipCostReceipt = shipCostReceipt;
	}

	
}
