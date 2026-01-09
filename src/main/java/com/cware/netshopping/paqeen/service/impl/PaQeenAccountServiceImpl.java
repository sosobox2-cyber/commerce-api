package com.cware.netshopping.paqeen.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.paqeen.process.PaQeenAccountProcess;
import com.cware.netshopping.paqeen.service.PaQeenAccountService;

@Service("paqeen.account.paQeenAccountService")
public class PaQeenAccountServiceImpl extends AbstractService implements PaQeenAccountService {
	
	@Autowired
	@Qualifier("paqeen.account.paQeenAccountProcess")
	PaQeenAccountProcess paQeenAccountProcess;

	@Override
	public ResponseMsg getQeenAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		return paQeenAccountProcess.getQeenAccountSettlementList(fromDate, toDate, request);
	}

	
}
