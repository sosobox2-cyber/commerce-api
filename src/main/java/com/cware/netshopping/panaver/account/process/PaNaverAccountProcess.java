package com.cware.netshopping.panaver.account.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;

public interface PaNaverAccountProcess {

	/**
	 * 정산 매출 저장 - 정산일자 중복데이터 체크
	 * @param ParamMap
	 * @return int 
	 * @throws Exception
	 */
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception;

	/**
	 * 정산매출정보 조회 IF_PANAVRAPI_04_001
	 * @param List<PaNavrSalesVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveSettelmentList(List<PanaversettlementVO> arrPaNaverAccountList) throws Exception;

}
