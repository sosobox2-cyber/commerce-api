package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Receiver;


public class ReceiverVO extends Receiver{

	private static final long serialVersionUID = 1L;

	private String receiverEtcAddr;

	public String getReceiverEtcAddr() {
		return receiverEtcAddr;
	}

	public void setReceiverEtcAddr(String receiverEtcAddr) {
		this.receiverEtcAddr = receiverEtcAddr;
	}
	
	
}
