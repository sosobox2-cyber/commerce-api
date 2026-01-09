package com.cware.netshopping.pafaple.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.process.PaFapleAccountProcess;
import com.cware.netshopping.pafaple.service.PaFapleAccountService;

@Service("pafaple.account.paFapleAccountService")
public class PaFapleAccountServiceImpl extends AbstractService implements PaFapleAccountService {

	@Autowired
	@Qualifier("pafaple.account.PaFapleAccountProcess")
	PaFapleAccountProcess paFapleAccountProcess;
	
	@Override
	public ResponseMsg getOrderSettlementList(String stdYMD, HttpServletRequest request) throws Exception {
		return paFapleAccountProcess.getOrderSettlementList(stdYMD, request);
	}
	
	@Override
	public ResponseMsg getShipSettlementList(String stdYMD, HttpServletRequest request) throws Exception {
		return paFapleAccountProcess.getShipSettlementList(stdYMD, request);
	}

}
