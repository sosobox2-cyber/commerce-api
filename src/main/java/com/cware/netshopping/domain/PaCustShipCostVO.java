package com.cware.netshopping.domain;

import java.sql.Timestamp;

import com.cware.netshopping.domain.model.PaCustShipCost;

public class PaCustShipCostVO extends PaCustShipCost {

    private static final long serialVersionUID = 1L;

    private String procGb;
    
    //티몬배송비정책
    private String productType;
    private String shipManSeq;
    private String returnManSeq;
    private String collectYn;
    private Timestamp applyDate;
    private String noShipIsland;

	public String getProcGb() {
		return procGb;
	}
	public void setProcGb(String procGb) {
		this.procGb = procGb;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
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
	public String getCollectYn() {
		return collectYn;
	}
	public void setCollectYn(String collectYn) {
		this.collectYn = collectYn;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}
	public String getNoShipIsland() {
		return noShipIsland;
	}
	public void setNoShipIsland(String noShipIsland) {
		this.noShipIsland = noShipIsland;
	}
}
