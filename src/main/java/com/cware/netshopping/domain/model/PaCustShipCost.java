package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaCustShipCost extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String paCode;
	private String entpCode;
	private String shipCostCode;
	private String groupCode;
	private double shipCostBaseAmt;
	private double ordCost;
	private double returnCost;
	private double changeCost;
	private double islandCost;
	private double jejuCost;
	private String transTargetYn;
	private Timestamp lastSyncDate;
	private String transCnCostYn;
	
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
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public double getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}
	public void setShipCostBaseAmt(double shipCostBaseAmt) {
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
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public Timestamp getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(Timestamp lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getTransCnCostYn() {
		return transCnCostYn;
	}
	public void setTransCnCostYn(String transCnCostYn) {
		this.transCnCostYn = transCnCostYn;
	}
}
