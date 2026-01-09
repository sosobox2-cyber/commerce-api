package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.PaQeenShipCost;

public class PaQeenShipCostVO extends PaQeenShipCost {

    private static final long serialVersionUID = 1L;
	
	private String registerYn; // 등록/수정 구분
	private String shipAddress; // 출고지 주소
	private String returnAddress; // 회수지 주소

	public String getRegisterYn() {
		return registerYn;
	}

	public void setRegisterYn(String registerYn) {
		this.registerYn = registerYn;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
	
	
	
}
