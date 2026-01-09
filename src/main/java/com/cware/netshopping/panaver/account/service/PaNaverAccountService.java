package com.cware.netshopping.panaver.account.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;

public interface PaNaverAccountService {

	/**
	 * 정산 매출 저장 - 정산일자 중복데이터 체크
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception;

	/**
	 * 정산매출정보 조회 IF_PANAVERAPI_05_001
	 * @param List<PaNavrSalesVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveSettelmentListTx(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception;

}
