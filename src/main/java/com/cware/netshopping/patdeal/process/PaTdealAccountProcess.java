package com.cware.netshopping.patdeal.process;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public interface PaTdealAccountProcess {

	ResponseMsg getTdealAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception ;


}
