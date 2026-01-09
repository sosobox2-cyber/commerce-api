package com.cware.netshopping.patdeal.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.patdeal.process.PaTdealAccountProcess;
import com.cware.netshopping.patdeal.service.PaTdealAccountService;

@Service("patdeal.account.paTdealAccountService")
public class PaTdealAccountServiceImpl extends AbstractService implements PaTdealAccountService {
	
	@Autowired
	@Qualifier("patdeal.account.paTdealAccountProcess")
	PaTdealAccountProcess paTdealAccountProcess;

	@Override
	public ResponseMsg getTdealAccountSettlementList(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		return paTdealAccountProcess.getTdealAccountSettlementList(fromDate, toDate, request);
	}

	
}
