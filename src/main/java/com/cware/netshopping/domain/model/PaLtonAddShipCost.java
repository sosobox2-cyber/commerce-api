package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaLtonAddShipCost extends AbstractModel {

	private static final long serialVersionUID = 1L;

	private String paCode;
	private double islandCost;
	private double jejuCost;
	private String dvCstPolNo;

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
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

	public String getDvCstPolNo() {
		return dvCstPolNo;
	}

	public void setDvCstPolNo(String dvCstPolNo) {
		this.dvCstPolNo = dvCstPolNo;
	}

}
