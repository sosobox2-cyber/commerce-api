package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Ordershipcost;

public class OrdershipcostVO extends Ordershipcost{

	private static final long serialVersionUID = 1L;
	
	private String shipCostCode;	//= type 에 따른 정보 셋팅. 10:주문배송비, 15:주문배송비환불, 20:반품배송비
	private double shipCost = 0;	//= 배송비 합계(주문번호, 배송 type group by)
	private String procDate;
	private long rowNum = 0;
	private long totalCount = 0;
	
	public String getShipCostCode() {
		return shipCostCode;
	}
	public void setShipCostCode(String shipCostCode) {
		this.shipCostCode = shipCostCode;
	}
	public double getShipCost() { 
		return this.shipCost;
	}
	public void setShipCost(double shipCost) { 
		this.shipCost = shipCost;
	}
	public String getProcDate() {
		return procDate;
	}
	public void setProcDate(String procDate) {
		this.procDate = procDate;
	}
	public long getRowNum() {
		return rowNum;
	}
	public void setRowNum(long rowNum) {
		this.rowNum = rowNum;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
