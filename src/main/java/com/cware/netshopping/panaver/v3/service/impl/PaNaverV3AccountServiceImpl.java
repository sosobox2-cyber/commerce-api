package com.cware.netshopping.panaver.v3.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;
import com.cware.netshopping.panaver.v3.process.PaNaverV3AccountProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3AccountService;

@Service("panaver.v3.account.paNaverV3AccountService")
public class PaNaverV3AccountServiceImpl extends AbstractService implements PaNaverV3AccountService {
	
	@Autowired
	@Qualifier("panaver.v3.account.paNaverV3AccountProcess")
	PaNaverV3AccountProcess paNaverV3AccountProcess;

	@Override
	public ResponseMsg getCaseSettleList(String searchDate, int pageNumber, int pageSize, String delYn, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3AccountProcess.getCaseSettleList(searchDate, pageNumber, pageSize, delYn, procId, request);
	}

	@Override
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception {
		return paNaverV3AccountProcess.selectChkPaNaverAccount(paramMap);
	}

	@Override
	public String saveSettlementListTx(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception {
		return paNaverV3AccountProcess.saveSettlementList(arrPaNaverAccountList);
	}
}
