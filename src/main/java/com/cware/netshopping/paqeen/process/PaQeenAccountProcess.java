package com.cware.netshopping.paqeen.process;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public interface PaQeenAccountProcess {

	ResponseMsg getQeenAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception ;

}
