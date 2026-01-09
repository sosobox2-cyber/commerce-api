package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class PaOrderShipCost extends AbstractModel implements Cloneable{
	private static final long serialVersionUID = 1L;

	private String orderNo;
	private String seq;
	private String custNo;
	private String outReceiverSeq;
	private String returnReceiverSeq;
	private Timestamp orderDate;
	private String receiptNo;
	private String type;
	private long paShpFeeAmt;
    private long shpFeeAmt;
    private int  shpFeeYn;
    private long manualCancelAmt;
    
    
    

    
	public String getOutReceiverSeq() {
		return outReceiverSeq;
	}


	public void setOutReceiverSeq(String outReceiverSeq) {
		this.outReceiverSeq = outReceiverSeq;
	}


	public String getReturnReceiverSeq() {
		return returnReceiverSeq;
	}


	public void setReturnReceiverSeq(String returnReceiverSeq) {
		this.returnReceiverSeq = returnReceiverSeq;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getSeq() {
		return seq;
	}


	public void setSeq(String seq) {
		this.seq = seq;
	}


	public String getCustNo() {
		return custNo;
	}


	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}


	public Timestamp getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}


	public String getReceiptNo() {
		return receiptNo;
	}


	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public long getPaShpFeeAmt() {
		return paShpFeeAmt;
	}


	public void setPaShpFeeAmt(long paShpFeeAmt) {
		this.paShpFeeAmt = paShpFeeAmt;
	}


	public long getShpFeeAmt() {
		return shpFeeAmt;
	}


	public void setShpFeeAmt(long shpFeeAmt) {
		this.shpFeeAmt = shpFeeAmt;
	}


	public int getShpFeeYn() {
		return shpFeeYn;
	}


	public void setShpFeeYn(int shpFeeYn) {
		this.shpFeeYn = shpFeeYn;
	}


	public long getManualCancelAmt() {
		return manualCancelAmt;
	}


	public void setManualCancelAmt(long manualCancelAmt) {
		this.manualCancelAmt = manualCancelAmt;
	}


	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

}
