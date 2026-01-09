package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;
import java.sql.Timestamp;

public class Ordershipcost extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String orderNo;
	private String seq;
	private String receiverSeq;
	private String type;
	private String orderGSeq;
	private String orderDSeq;
	private String orderWSeq;
	private String entpCode;
	private String delyType;
	private String shpfeeCode;
	private double shpfeeCost;
	private String shipCostNo;
	private String groupShipCostNo;
	private String shipCostReceipt;
	private double manualCancelAmt;
	private String manualCancelId;
	private Timestamp manualCancelDate;
	

	public String getOrderNo() { 
		return this.orderNo;
	}
	public String getSeq() { 
		return this.seq;
	}
	public String getReceiverSeq() { 
		return this.receiverSeq;
	}
	public String getType() { 
		return this.type;
	}
	public String getOrderGSeq() { 
		return this.orderGSeq;
	}
	public String getOrderDSeq() { 
		return this.orderDSeq;
	}
	public String getOrderWSeq() { 
		return this.orderWSeq;
	}
	public String getShpfeeCode() { 
		return this.shpfeeCode;
	}
	public double getShpfeeCost() { 
		return this.shpfeeCost;
	}
	public void setOrderNo(String orderNo) { 
		this.orderNo = orderNo;
	}
	public void setSeq(String seq) { 
		this.seq = seq;
	}
	public void setReceiverSeq(String receiverSeq) { 
		this.receiverSeq = receiverSeq;
	}
	public void setType(String type) { 
		this.type = type;
	}
	public void setOrderGSeq(String orderGSeq) { 
		this.orderGSeq = orderGSeq;
	}
	public void setOrderDSeq(String orderDSeq) { 
		this.orderDSeq = orderDSeq;
	}
	public void setOrderWSeq(String orderWSeq) { 
		this.orderWSeq = orderWSeq;
	}
	public void setShpfeeCode(String shpfeeCode) { 
		this.shpfeeCode = shpfeeCode;
	}
	public void setShpfeeCost(double shpfeeCost) { 
		this.shpfeeCost = shpfeeCost;
	}
	public String getDelyType() {
		return delyType;
	}
	public void setDelyType(String delyType) {
		this.delyType = delyType;
	}
	public String getEntpCode() {
		return entpCode;
	}
	public void setEntpCode(String entpCode) {
		this.entpCode = entpCode;
	}
	public String getShipCostNo() {
		return shipCostNo;
	}
	public void setShipCostNo(String shipCostNo) {
		this.shipCostNo = shipCostNo;
	}	
	public String getGroupShipCostNo() {
		return groupShipCostNo;
	}
	public void setGroupShipCostNo(String groupShipCostNo) {
		this.groupShipCostNo = groupShipCostNo;
	}
	public String getShipCostReceipt() {
		return shipCostReceipt;
	}
	public void setShipCostReceipt(String shipCostReceipt) {
		this.shipCostReceipt = shipCostReceipt;
	}
	public double getManualCancelAmt() {
		return manualCancelAmt;
	}
	public void setManualCancelAmt(double manualCancelAmt) {
		this.manualCancelAmt = manualCancelAmt;
	}
	public String getManualCancelId() {
		return manualCancelId;
	}
	public void setManualCancelId(String manualCancelId) {
		this.manualCancelId = manualCancelId;
	}
	public Timestamp getManualCancelDate() {
		return manualCancelDate;
	}
	public void setManualCancelDate(Timestamp manualCancelDate) {
		this.manualCancelDate = manualCancelDate;
	}
}
