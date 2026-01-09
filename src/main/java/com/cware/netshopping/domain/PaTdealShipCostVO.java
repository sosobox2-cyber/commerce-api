package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaTdealShipCost;

public class PaTdealShipCostVO extends PaTdealShipCost {

    private static final long serialVersionUID = 1L;
    
	private double ordCost; // 주문배송비
	private double returnCost; // 반품배송비
	private double shipCostBaseAmt; // 조건부무료 기준금액
	
	private String paShipManSeq; // 출고지번호
	private String paReturnManSeq; // 회수지번호
	
	private String registerYn; // 등록/수정 구분
	
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
	public double getShipCostBaseAmt() {
		return shipCostBaseAmt;
	}
	public void setShipCostBaseAmt(double shipCostBaseAmt) {
		this.shipCostBaseAmt = shipCostBaseAmt;
	}
	public String getPaShipManSeq() {
		return paShipManSeq;
	}
	public void setPaShipManSeq(String paShipManSeq) {
		this.paShipManSeq = paShipManSeq;
	}
	public String getPaReturnManSeq() {
		return paReturnManSeq;
	}
	public void setPaReturnManSeq(String paReturnManSeq) {
		this.paReturnManSeq = paReturnManSeq;
	}
	public String getRegisterYn() {
		return registerYn;
	}
	public void setRegisterYn(String registerYn) {
		this.registerYn = registerYn;
	}
	
}
