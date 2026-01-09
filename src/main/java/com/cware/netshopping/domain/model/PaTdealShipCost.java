package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaTdealShipCost extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String paCode;
	private String entpCode;
	private String shipManSeq;
	private String returnManSeq;
	private String shipCostCode;
	private double jejuCost;
	private double islandCost;
	private String transTargetYn;
	private String areaFeeNo;
	private String templateGroupNo;
	private String templateNo;
	
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
	public double getJejuCost() {
		return jejuCost;
	}
	public void setJejuCost(double jejuCost) {
		this.jejuCost = jejuCost;
	}
	public double getIslandCost() {
		return islandCost;
	}
	public void setIslandCost(double islandCost) {
		this.islandCost = islandCost;
	}
	public String getTransTargetYn() {
		return transTargetYn;
	}
	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	public String getAreaFeeNo() {
		return areaFeeNo;
	}
	public void setAreaFeeNo(String areaFeeNo) {
		this.areaFeeNo = areaFeeNo;
	}
	public String getTemplateGroupNo() {
		return templateGroupNo;
	}
	public void setTemplateGroupNo(String templateGroupNo) {
		this.templateGroupNo = templateGroupNo;
	}
	public String getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}
	
}
