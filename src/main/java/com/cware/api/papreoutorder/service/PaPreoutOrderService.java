package com.cware.api.papreoutorder.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface PaPreoutOrderService  {
	
	public void preoutOrderInput(Map<String, String> order, HttpServletRequest request) throws Exception;

}
