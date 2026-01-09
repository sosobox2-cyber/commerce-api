package com.cware.netshopping.panaver.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;
import com.cware.netshopping.panaver.account.process.PaNaverAccountProcess;
import com.cware.netshopping.panaver.account.service.PaNaverAccountService;

@Service("panaver.account.PaNaverAccountService")
public class PaNaverAccountServiceImpl extends AbstractService implements PaNaverAccountService{

	@Resource(name = "panaver.account.PaNaverAccountProcess")
	private PaNaverAccountProcess paNaverAccountProcess;
	@Override
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception {
		return paNaverAccountProcess.selectChkPaNaverAccount(paramMap);
	}
	@Override
	public String saveSettelmentListTx(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception {
		return paNaverAccountProcess.saveSettelmentList(arrPaNaverAccountList);
	}

}
