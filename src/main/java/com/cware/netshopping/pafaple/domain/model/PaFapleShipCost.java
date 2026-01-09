package com.cware.netshopping.pafaple.domain.model;


import com.cware.framework.core.basic.AbstractModel;

public class PaFapleShipCost extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	private String paCode;
	private String entpCode;
	private String	brandId;
	private String	brandCode;
	private String	shipCostCode;
	private double ordCost;
	private double returnCost;
	private double islandCost;
	private double islandReturnCost;
	private int senderId;
	
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
	public double getIslandReturnCost() {
		return islandReturnCost;
	}
	public void setIslandReturnCost(double islandReturnCost) {
		this.islandReturnCost = islandReturnCost;
	}
	public String getPaCode() {
		return paCode;
	}
	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}	
	
}
