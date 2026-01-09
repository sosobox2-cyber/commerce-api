package com.cware.netshopping.paqeen.service;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public interface PaQeenAccountService {

	public ResponseMsg getQeenAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception;
	
}
