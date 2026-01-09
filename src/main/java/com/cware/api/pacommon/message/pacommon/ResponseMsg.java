package com.cware.api.pacommon.message.pacommon;

import com.cware.framework.core.basic.AbstractMessage;

public class ResponseMsg extends AbstractMessage {

    private static final long serialVersionUID = -3986379890855916624L;

    public ResponseMsg(String code, String message) {
    	this.setCode(code);
    	this.setMessage(message);
    }

    public ResponseMsg(String code, String message, String type) {
    	this.setCode(code);
    	this.setMessage(message);
    	this.setType(type);
    }

    public ResponseMsg(int status, String code, String message) {
    	this.setStatus(status);
    	this.setCode(code);
    	this.setMessage(message);
    }

    public ResponseMsg(int status, String code, String message, String type) {
    	this.setStatus(status);
    	this.setCode(code);
    	this.setMessage(message);
    	this.setType(type);
    }
}
