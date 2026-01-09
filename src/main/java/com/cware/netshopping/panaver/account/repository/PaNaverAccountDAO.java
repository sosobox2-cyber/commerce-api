package com.cware.netshopping.panaver.account.repository;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;

@Service("panaver.account.PaNaverAccountDAO")
public class PaNaverAccountDAO extends AbstractPaDAO{

	public Integer selectChkPaNaverAccount(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("panaver.account.selectChkPaNaverAccount", paramMap.get());
	}

	public Integer deletePaNaverAccount(PanaversettlementVO paNaverSettlement) throws Exception{
		return delete("panaver.account.deletePaNaverAccount", paNaverSettlement);
	}

	public int insertPaNaverSettlement(PanaversettlementVO paNaverSettlement) throws Exception{
		return insert("panaver.account.insertPaNaverSettlement", paNaverSettlement);
	}

	/**
	 * 네이버 매출정보조회 - 매출데이터 받은 날짜 UPDATE
	 * @return int
	 * @throws Exception
	 */
	public int updatePaorderm(PanaversettlementVO paNaverSettlement) throws Exception{
		return update("panaver.account.updatePaorderm", paNaverSettlement);
	}

	public String selectPaDoFlagCheck(PanaversettlementVO paNaverSettlement) throws Exception{
		return (String) selectByPk("panaver.account.selectPaDoFlagCheck", paNaverSettlement);
	}

}
