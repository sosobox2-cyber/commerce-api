package com.cware.netshopping.domain.model;

public class PaWempEntpSlip extends PaEntpSlip {
	private static final long serialVersionUID = 1L; 
	
	private String shipCostCode;
	private String paShipPolicyNo;
	private String noShipIsland;
	private String installYn;
	private String goodsCode;

	public String getNoShipIsland() {
		return noShipIsland;
	}

	public void setNoShipIsland(String noShipIsland) {
		this.noShipIsland = noShipIsland;
	}

	public String getInstallYn() {
		return installYn;
	}

	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	public String getShipCostCode() {
		return shipCostCode;
	}

	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}

	public String getPaShipPolicyNo() {
		return paShipPolicyNo;
	}

	public void setPaShipPolicyNo(String paShipPolicyNo) {
		this.paShipPolicyNo = paShipPolicyNo;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
}
