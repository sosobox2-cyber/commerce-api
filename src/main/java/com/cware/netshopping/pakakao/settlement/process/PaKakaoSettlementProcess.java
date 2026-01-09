package com.cware.netshopping.pakakao.settlement.process;

import java.util.List;

import com.cware.netshopping.domain.model.PaKakaoSettlement;

public interface PaKakaoSettlementProcess {

	/**
	 * 카카오 정산데이터저장
	 * @param toDate 
	 * @param fromDate 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaKakaoSettlement(List<PaKakaoSettlement> paKakaoSettlementList) throws Exception;


}
