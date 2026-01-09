package com.cware.api.pacommon.message.pacommon;

import com.cware.framework.core.basic.AbstractMessage;

public class ConfirmOrderMsg extends AbstractMessage{
	
	private static final long serialVersionUID = 1L;
		
	private String code;
	private String message;
	private String returnStatus;	
	private String receiverName;
	private String receiverPhone1;
	private String receiverPhone2;
	private String receiverZipcode;
	private String receiverAddress;

	public String getCode() {
	    return code;
	}
	public void setCode(String code) {
	    this.code = code;
	}
	public String getMessage() {
	    return message;
	}
	public void setMessage(String message) {
	    this.message = message;
	}	
	public String getReceiverName() {
	    return receiverName;
	}
	public void setReceiverName(String receiverName) {
	    this.receiverName = receiverName;
	}
	public String getReceiverPhone1() {
	    return receiverPhone1;
	}
	public void setReceiverPhone1(String receiverPhone1) {
	    this.receiverPhone1 = receiverPhone1;
	}
	public String getReceiverPhone2() {
	    return receiverPhone2;
	}
	public void setReceiverPhone2(String receiverPhone2) {
	    this.receiverPhone2 = receiverPhone2;
	}
	public String getReceiverZipcode() {
	    return receiverZipcode;
	}
	public void setReceiverZipcode(String receiverZipcode) {
	    this.receiverZipcode = receiverZipcode;
	}
	public String getReceiverAddress() {
	    return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
	    this.receiverAddress = receiverAddress;
	}
	public String getReturnStatus() {
	    return returnStatus;
	}
	public void setReturnStatus(String returnStatus) {
	    this.returnStatus = returnStatus;
	}

	
	
}

