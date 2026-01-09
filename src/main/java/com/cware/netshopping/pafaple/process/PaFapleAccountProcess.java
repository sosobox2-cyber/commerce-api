package com.cware.netshopping.pafaple.process;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;

public interface PaFapleAccountProcess {

	public ResponseMsg getOrderSettlementList(String stdYMD, HttpServletRequest request) throws Exception;

	public ResponseMsg getShipSettlementList(String stdYMD, HttpServletRequest request) throws Exception;

}
