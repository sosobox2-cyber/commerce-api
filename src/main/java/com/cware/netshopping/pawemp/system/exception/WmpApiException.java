package com.cware.netshopping.pawemp.system.exception;


public class WmpApiException extends RuntimeException {

	private static final long serialVersionUID = -8238486245582597884L;

	public WmpApiException(){
		super();
	}
	
	public WmpApiException(String message){
		super(message);
	}
	
	public WmpApiException(String message,Throwable cause){
		super(message, cause);
	}
	
	public WmpApiException(Throwable cause){
		super( cause);
	}

}
