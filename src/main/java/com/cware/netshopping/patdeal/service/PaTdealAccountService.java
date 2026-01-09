package com.cware.netshopping.patdeal.service;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public interface PaTdealAccountService {

	public ResponseMsg getTdealAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	
	
}
