package com.cware.netshopping.domain.model;

import java.sql.Timestamp;

import com.cware.framework.core.basic.AbstractModel;

public class Slipm extends AbstractModel {
	private static final long serialVersionUID = 1L; 

	private String slipINo;
	private String custNo;
	private String receiverSeq;
	private String whCode;
	private String slipNo;
	private String slipFlag;
	private String slipGb;
	private String delyType;
	private String delyGb;
	private String mixpackFlag;
	private String createSeq;
	private Timestamp createDate;
	private String slipProc;
	private Timestamp slipProcDate;
	private String slipProcId;
	private Timestamp delyHopeDate;
	private String delyHopeYn;
	private String delyHopeTime;
	private String outCloseYn;
	private Timestamp outCloseDate;
	private String redelyYn;
	private String preDelyYn;
	private String receiptYn;
	private String realReceiver;
	private Timestamp realDelyDate;
	private Timestamp realOutDate;
	private String vVolumeResult;
	private String shipFeeYn;
	private double shipFee;
	private Timestamp shipFeeDate;
	private long delyBoxQty;
	private long delyBoxSeq;
	private String happyCardYn;
	private String packYn;
	private String anonyYn;
	private String invoiceNo;
	private String invoiceCode;
	private String printerNo;
	private String directYn;
	private String msg;
	private String msgNote;
	private String remark1V;
	private String remark2V;
	private long remark3N;
	private Timestamp delicompDelyDate;

	public String getSlipINo() { 
		return this.slipINo;
	}
	public String getCustNo() { 
		return this.custNo;
	}
	public String getReceiverSeq() { 
		return this.receiverSeq;
	}
	public String getWhCode() { 
		return this.whCode;
	}
	public String getSlipNo() { 
		return this.slipNo;
	}
	public String getSlipFlag() { 
		return this.slipFlag;
	}
	public String getSlipGb() { 
		return this.slipGb;
	}
	public String getDelyType() { 
		return this.delyType;
	}
	public String getDelyGb() { 
		return this.delyGb;
	}
	public String getMixpackFlag() { 
		return this.mixpackFlag;
	}
	public String getCreateSeq() { 
		return this.createSeq;
	}
	public Timestamp getCreateDate() { 
		return this.createDate;
	}
	public String getSlipProc() { 
		return this.slipProc;
	}
	public Timestamp getSlipProcDate() { 
		return this.slipProcDate;
	}
	public String getSlipProcId() { 
		return this.slipProcId;
	}
	public Timestamp getDelyHopeDate() { 
		return this.delyHopeDate;
	}
	public String getDelyHopeYn() { 
		return this.delyHopeYn;
	}
	public String getDelyHopeTime() { 
		return this.delyHopeTime;
	}
	public String getOutCloseYn() { 
		return this.outCloseYn;
	}
	public Timestamp getOutCloseDate() { 
		return this.outCloseDate;
	}
	public String getRedelyYn() { 
		return this.redelyYn;
	}
	public String getPreDelyYn() { 
		return this.preDelyYn;
	}
	public String getReceiptYn() { 
		return this.receiptYn;
	}
	public String getRealReceiver() { 
		return this.realReceiver;
	}
	public Timestamp getRealDelyDate() { 
		return this.realDelyDate;
	}
	public Timestamp getRealOutDate() { 
		return this.realOutDate;
	}
	public String getVVolumeResult() { 
		return this.vVolumeResult;
	}
	public String getShipFeeYn() { 
		return this.shipFeeYn;
	}
	public double getShipFee() { 
		return this.shipFee;
	}
	public Timestamp getShipFeeDate() { 
		return this.shipFeeDate;
	}
	public long getDelyBoxQty() { 
		return this.delyBoxQty;
	}
	public long getDelyBoxSeq() { 
		return this.delyBoxSeq;
	}
	public String getHappyCardYn() { 
		return this.happyCardYn;
	}
	public String getPackYn() { 
		return this.packYn;
	}
	public String getAnonyYn() { 
		return this.anonyYn;
	}
	public String getInvoiceNo() { 
		return this.invoiceNo;
	}
	public String getInvoiceCode() { 
		return this.invoiceCode;
	}
	public String getPrinterNo() { 
		return this.printerNo;
	}
	public String getDirectYn() { 
		return this.directYn;
	}
	public String getMsg() { 
		return this.msg;
	}
	public String getMsgNote() { 
		return this.msgNote;
	}
	public String getRemark1V() { 
		return this.remark1V;
	}
	public String getRemark2V() { 
		return this.remark2V;
	}
	public long getRemark3N() { 
		return this.remark3N;
	}
	public Timestamp getDelicompDelyDate() { 
		return this.delicompDelyDate;
	}

	public void setSlipINo(String slipINo) { 
		this.slipINo = slipINo;
	}
	public void setCustNo(String custNo) { 
		this.custNo = custNo;
	}
	public void setReceiverSeq(String receiverSeq) { 
		this.receiverSeq = receiverSeq;
	}
	public void setWhCode(String whCode) { 
		this.whCode = whCode;
	}
	public void setSlipNo(String slipNo) { 
		this.slipNo = slipNo;
	}
	public void setSlipFlag(String slipFlag) { 
		this.slipFlag = slipFlag;
	}
	public void setSlipGb(String slipGb) { 
		this.slipGb = slipGb;
	}
	public void setDelyType(String delyType) { 
		this.delyType = delyType;
	}
	public void setDelyGb(String delyGb) { 
		this.delyGb = delyGb;
	}
	public void setMixpackFlag(String mixpackFlag) { 
		this.mixpackFlag = mixpackFlag;
	}
	public void setCreateSeq(String createSeq) { 
		this.createSeq = createSeq;
	}
	public void setCreateDate(Timestamp createDate) { 
		this.createDate = createDate;
	}
	public void setSlipProc(String slipProc) { 
		this.slipProc = slipProc;
	}
	public void setSlipProcDate(Timestamp slipProcDate) { 
		this.slipProcDate = slipProcDate;
	}
	public void setSlipProcId(String slipProcId) { 
		this.slipProcId = slipProcId;
	}
	public void setDelyHopeDate(Timestamp delyHopeDate) { 
		this.delyHopeDate = delyHopeDate;
	}
	public void setDelyHopeYn(String delyHopeYn) { 
		this.delyHopeYn = delyHopeYn;
	}
	public void setDelyHopeTime(String delyHopeTime) { 
		this.delyHopeTime = delyHopeTime;
	}
	public void setOutCloseYn(String outCloseYn) { 
		this.outCloseYn = outCloseYn;
	}
	public void setOutCloseDate(Timestamp outCloseDate) { 
		this.outCloseDate = outCloseDate;
	}
	public void setRedelyYn(String redelyYn) { 
		this.redelyYn = redelyYn;
	}
	public void setPreDelyYn(String preDelyYn) { 
		this.preDelyYn = preDelyYn;
	}
	public void setReceiptYn(String receiptYn) { 
		this.receiptYn = receiptYn;
	}
	public void setRealReceiver(String realReceiver) { 
		this.realReceiver = realReceiver;
	}
	public void setRealDelyDate(Timestamp realDelyDate) { 
		this.realDelyDate = realDelyDate;
	}
	public void setRealOutDate(Timestamp realOutDate) { 
		this.realOutDate = realOutDate;
	}
	public void setVVolumeResult(String vVolumeResult) { 
		this.vVolumeResult = vVolumeResult;
	}
	public void setShipFeeYn(String shipFeeYn) { 
		this.shipFeeYn = shipFeeYn;
	}
	public void setShipFee(double shipFee) { 
		this.shipFee = shipFee;
	}
	public void setShipFeeDate(Timestamp shipFeeDate) { 
		this.shipFeeDate = shipFeeDate;
	}
	public void setDelyBoxQty(long delyBoxQty) { 
		this.delyBoxQty = delyBoxQty;
	}
	public void setDelyBoxSeq(long delyBoxSeq) { 
		this.delyBoxSeq = delyBoxSeq;
	}
	public void setHappyCardYn(String happyCardYn) { 
		this.happyCardYn = happyCardYn;
	}
	public void setPackYn(String packYn) { 
		this.packYn = packYn;
	}
	public void setAnonyYn(String anonyYn) { 
		this.anonyYn = anonyYn;
	}
	public void setInvoiceNo(String invoiceNo) { 
		this.invoiceNo = invoiceNo;
	}
	public void setInvoiceCode(String invoiceCode) { 
		this.invoiceCode = invoiceCode;
	}
	public void setPrinterNo(String printerNo) { 
		this.printerNo = printerNo;
	}
	public void setDirectYn(String directYn) { 
		this.directYn = directYn;
	}
	public void setMsg(String msg) { 
		this.msg = msg;
	}
	public void setMsgNote(String msgNote) { 
		this.msgNote = msgNote;
	}
	public void setRemark1V(String remark1V) { 
		this.remark1V = remark1V;
	}
	public void setRemark2V(String remark2V) { 
		this.remark2V = remark2V;
	}
	public void setRemark3N(long remark3N) { 
		this.remark3N = remark3N;
	}
	public void setDelicompDelyDate(Timestamp delicompDelyDate) { 
		this.delicompDelyDate = delicompDelyDate;
	}
}
