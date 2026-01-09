package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaGmktShipCostM extends AbstractModel {

	private static final long serialVersionUID = 1L;
	private String gmktShipSeq;
	private String gmktShipNo;
	private String entpCode;
	private String entpManSeq;
	private String shipCostCode;
	private double ordCost;
	private double islandCost;
	private double jejuCost;
	private String transCheckYn;
	private Timestamp transEndDate;
	private String applyGmktShipSeq;
	private String transTargetYn;
	private String useYn;
	private String transErrorYn;
	
	public double getOrdCost() {
		return ordCost;
	}

	public void setOrdCost(double ordCost) {
		this.ordCost = ordCost;
	}

	public String getGmktShipSeq() {
		return gmktShipSeq;
	}

	public void setGmktShipSeq(String gmktShipSeq) {
		this.gmktShipSeq = gmktShipSeq;
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

	public String getTransCheckYn() {
		return transCheckYn;
	}

	public void setTransCheckYn(String transCheckYn) {
		this.transCheckYn = transCheckYn;
	}

	public Timestamp getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(Timestamp transEndDate) {
		this.transEndDate = transEndDate;
	}

	public String getApplyGmktShipSeq() {
		return applyGmktShipSeq;
	}

	public void setApplyGmktShipSeq(String applyGmktShipSeq) {
		this.applyGmktShipSeq = applyGmktShipSeq;
	}

	public String getTransTargetYn() {
		return transTargetYn;
	}

	public void setTransTargetYn(String transTargetYn) {
		this.transTargetYn = transTargetYn;
	}
	
	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getTransErrorYn() {
		return transErrorYn;
	}

	public void setTransErrorYn(String transErrorYn) {
		this.transErrorYn = transErrorYn;
	}
}
