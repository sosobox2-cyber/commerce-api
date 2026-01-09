package com.cware.netshopping.pagmkt.v2.domain;

import com.cware.netshopping.domain.model.PaGmktShipCostDt;

public class EbayShipCost extends PaGmktShipCostDt {

	private static final long serialVersionUID = 8303789019162610536L;

	private String paAddrSeq;
	
	private String shipCostReceipt;

	public String getPaAddrSeq() {
		return paAddrSeq;
	}

	public void setPaAddrSeq(String paAddrSeq) {
		this.paAddrSeq = paAddrSeq;
	}

	@Override
	public String toString() {
		return "EbayShipCost [paAddrSeq=" + paAddrSeq + ", toString()=" + super.toString() + "]";
	}

	public String getShipCostReceipt() {
		return shipCostReceipt;
	}

	public void setShipCostReceipt(String shipCostReceipt) {
		this.shipCostReceipt = shipCostReceipt;
	}
	
}
