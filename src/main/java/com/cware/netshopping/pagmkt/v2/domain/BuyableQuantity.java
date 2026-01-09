package com.cware.netshopping.pagmkt.v2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BuyableQuantity {

	// 구매수량제한 타입 설정
	// 0. nolimit : 구매수량 제한 없음,
	// 1. OneTime : 1회당 최대 구매수량,
	// 2.max : ID당 최대 구매수량,
	// 3.Day : 기간당 최대 구매수량,
	private int type;

	// 수량
	private Integer qty;

	// 제한기간
	private Integer unitDate;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getUnitDate() {
		return unitDate;
	}

	public void setUnitDate(Integer unitDate) {
		this.unitDate = unitDate;
	}

	@Override
	public String toString() {
		return "BuyableQuantity [type=" + type + ", qty=" + qty + ", unitDate=" + unitDate + "]";
	}

}
