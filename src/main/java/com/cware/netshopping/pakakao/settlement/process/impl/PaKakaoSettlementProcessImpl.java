package com.cware.netshopping.pakakao.settlement.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.model.PaKakaoSettlement;
import com.cware.netshopping.pakakao.settlement.process.PaKakaoSettlementProcess;
import com.cware.netshopping.pakakao.settlement.repository.PaKakaoSettlementDAO;

@Service("pakakao.settlement.paKakaoSettlementProcess")
public class PaKakaoSettlementProcessImpl extends AbstractService implements PaKakaoSettlementProcess{

	@Resource(name = "pakakao.settlement.paKakaoSettlementDAO")
	private PaKakaoSettlementDAO paKakaoSettlementDAO;

	@Override
	public String savePaKakaoSettlement(List<PaKakaoSettlement> paKakaoSettlementList) throws Exception {
		int count = 0;
		// 이미 저장된 정산데이터의 경우 update
		for(PaKakaoSettlement paKakaoSettlement : paKakaoSettlementList){
			paKakaoSettlementDAO.margePaKakaoSettlementList(paKakaoSettlement);
			count++;
		}
		log.error("정산데이터 저장 Success : " + count);
		return Constants.SAVE_SUCCESS;
	}

}
