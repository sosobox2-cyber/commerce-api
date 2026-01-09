package com.cware.netshopping.pakakao.settlement.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.PaKakaoSettlement;
import com.cware.netshopping.pakakao.settlement.process.PaKakaoSettlementProcess;
import com.cware.netshopping.pakakao.settlement.service.PaKakaoSettlementService;

@Service("pakakao.settlement.paKakaoSettlementService")
public class PaKakaoSettlementServiceImpl extends AbstractService implements PaKakaoSettlementService{

	@Resource(name = "pakakao.settlement.paKakaoSettlementProcess")
    private PaKakaoSettlementProcess paKakaoSettlementProcess;

	@Override
	public String savePaKakaoSettlementTx(List<PaKakaoSettlement> paKakaoSettlementList) throws Exception {
		return paKakaoSettlementProcess.savePaKakaoSettlement(paKakaoSettlementList);
	}

}
