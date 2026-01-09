package com.cware.netshopping.pakakao.settlement.service;

import java.util.List;

import com.cware.netshopping.domain.model.PaKakaoSettlement;

public interface PaKakaoSettlementService {
	/**
	 * 카카오 정산데이터저장
	 * @param toDate 
	 * @param fromDate 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaKakaoSettlementTx(List<PaKakaoSettlement> paKakaoSettlementList) throws Exception;
}
