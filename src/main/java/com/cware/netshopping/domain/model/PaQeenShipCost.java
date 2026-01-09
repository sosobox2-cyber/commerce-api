package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaQeenShipCost extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String paCode;
	private String entpCode;
	private String paShipcostId;
	private String paShipcostName;
	private String brandCodes;
	private String shipManSeq;
	private String returnManSeq;
	private String shipCostCode;
	private double shipCosetBaseAmt;
	private double ordCost;
	private double returnCost;
	private double islandCost;
	private double jejuCost;
	private double islandReturnCost;
	private double jejuReturnCost;
	
	private String transTargetYn;
	
	private Timestamp insertDate;
	private String insertId;
	private Timestamp modifyDate;
	private String modifyId;
	private String lastSyncDate;
	private String lastEntpSyncDate;
	
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
	public String getPaShipcostId() {
		return paShipcostId;
	}
	public void setPaShipcostId(String paShipcostId) {
		this.paShipcostId = paShipcostId;
	}
	public String getPaShipcostName() {
		return paShipcostName;
	}
	public void setPaShipcostName(String paShipcostName) {
		this.paShipcostName = paShipcostName;
	}
	public String getBrandCodes() {
		return brandCodes;
	}
	public void setBrandCodes(String brandCodes) {
		this.brandCodes = brandCodes;
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
	public double getShipCosetBaseAmt() {
		return shipCosetBaseAmt;
	}
	public void setShipCosetBaseAmt(double shipCosetBaseAmt) {
		this.shipCosetBaseAmt = shipCosetBaseAmt;
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
	public double getIslandReturnCost() {
		return islandReturnCost;
	}
	public void setIslandReturnCost(double islandReturnCost) {
		this.islandReturnCost = islandReturnCost;
	}
	public double getJejuReturnCost() {
		return jejuReturnCost;
	}
	public void setJejuReturnCost(double jejuReturnCost) {
		this.jejuReturnCost = jejuReturnCost;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public String getLastSyncDate() {
		return lastSyncDate;
	}
	public void setLastSyncDate(String lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	public String getLastEntpSyncDate() {
		return lastEntpSyncDate;
	}
	public void setLastEntpSyncDate(String lastEntpSyncDate) {
		this.lastEntpSyncDate = lastEntpSyncDate;
	}
	
}
