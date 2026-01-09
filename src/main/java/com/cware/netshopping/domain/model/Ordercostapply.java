package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Ordercostapply extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orderNo;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String applyCostSeq;
	private int shipCostNo;
	private String insertId;
	private Timestamp insertDate; 
	private String modifyId;
	private Timestamp modifyDate;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderGSeq() {
		return orderGSeq;
	}
	public void setOrderGSeq(String orderGSeq) {
		this.orderGSeq = orderGSeq;
	}
	public String getOrderDSeq() {
		return orderDSeq;
	}
	public void setOrderDSeq(String orderDSeq) {
		this.orderDSeq = orderDSeq;
	}
	public String getOrderWSeq() {
		return orderWSeq;
	}
	public void setOrderWSeq(String orderWSeq) {
		this.orderWSeq = orderWSeq;
	}
	public String getApplyCostSeq() {
		return applyCostSeq;
	}
	public void setApplyCostSeq(String applyCostSeq) {
		this.applyCostSeq = applyCostSeq;
	}
	public int getShipCostNo() {
		return shipCostNo;
	}
	public void setShipCostNo(int shipCostNo) {
		this.shipCostNo = shipCostNo;
	}
	public String getInsertId() {
		return insertId;
	}
	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	public Timestamp getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
	
}
