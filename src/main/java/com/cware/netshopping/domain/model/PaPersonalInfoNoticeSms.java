package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaPersonalInfoNoticeSms extends AbstractModel {

	private static final long serialVersionUID = 118586365888929867L;
	 
	private String orderNo;
	private String custNo;
	private String receiverHp;
	private String smsSendNo;
	private String mediaCode;
	private String paName;
	private String sendNo;
	private String msg;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getReceiverHp() {
		return receiverHp;
	}
	public void setReceiverHp(String receiverHp) {
		this.receiverHp = receiverHp;
	}
	public String getSmsSendNo() {
		return smsSendNo;
	}
	public void setSmsSendNo(String smsSendNo) {
		this.smsSendNo = smsSendNo;
	}
	public String getMediaCode() {
		return mediaCode;
	}
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}
	public String getPaName() {
		return paName;
	}
	public void setPaName(String paName) {
		this.paName = paName;
	}
	public String getSendNo() {
		return sendNo;
	}
	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	

}
