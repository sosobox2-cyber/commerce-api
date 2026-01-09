package com.cware.api.papreoutorder.process;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface PaPreoutOrderProcess {

	public void preoutOrderInput(Map<String, String> order, HttpServletRequest request) throws Exception;

}
