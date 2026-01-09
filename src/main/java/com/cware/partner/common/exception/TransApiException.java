package com.cware.partner.common.exception;


public class TransApiException extends RuntimeException {

	private static final long serialVersionUID = -8368393106620316887L;

	String code;

	public TransApiException() {
    }

    public TransApiException(String message) {
        super(message);
    }

    public TransApiException(String message, String code) {
        super(message);
        this.code = code;
    }

    public TransApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransApiException(Throwable cause) {
        super(cause);
    }

    public TransApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getCode() {
    	return this.code;
    }
}
