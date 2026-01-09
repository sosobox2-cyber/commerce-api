package com.cware.netshopping.pakakao.settlement.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.model.PaKakaoSettlement;

@Service("pakakao.settlement.paKakaoSettlementDAO")
public class PaKakaoSettlementDAO extends AbstractPaDAO{
	
	/**
	 * 정산저장
	 * @param PaKakaoSettlement
	 * @return String
	 * @throws Exception
	 */
	public int margePaKakaoSettlementList(PaKakaoSettlement paKakaoSettlement) {
		return update("pakakao.settlement.margePaKakaoSettlementList", paKakaoSettlement);
	}
}
