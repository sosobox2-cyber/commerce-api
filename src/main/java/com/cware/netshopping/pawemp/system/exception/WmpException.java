package com.cware.netshopping.pawemp.system.exception;

public class WmpException extends RuntimeException {

	private static final long serialVersionUID = -8487086730288568462L;
	
	public WmpException(){
		super();
	}
	
	public WmpException(String message){
		super(message);
	}
	
	public WmpException(String message,Throwable cause){
		super(message, cause);
	}
	
	public WmpException(Throwable cause){
		super( cause);
	}

}
