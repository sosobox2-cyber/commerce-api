package com.cware.netshopping.panaver.v3.repository;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PanaversettlementVO;

@Repository("panaver.v3.account.paNaverV3AccountDAO")
public class PaNaverV3AccountDAO extends AbstractPaDAO {

	/**
	 * 정산일자 중복 데이터 체크
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectChkPaNaverAccount(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.v3.account.selectChkPaNaverAccount", paramMap.get());
	}
	
	/**
	 * 
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Integer deletePaNaverAccount(PanaversettlementVO paNaverSettlement) throws Exception{
		return delete("panaver.v3.account.deletePaNaverAccount", paNaverSettlement);
	}

	/**
	 * 정산 매출정보 저장
	 * 
	 * @param paNaverSettlement
	 * @return
	 * @throws Exception
	 */
	public int insertPaNaverSettlement(PanaversettlementVO paNaverSettlement) throws Exception {
		return insert("panaver.v3.account.insertPaNaverSettlement", paNaverSettlement);
	}
	
	/**
	 * 제휴사 배송단계 조회
	 * 
	 * @param paNaverSettlement
	 * @return
	 * @throws Exception
	 */
	public String selectPaDoFlagCheck(PanaversettlementVO paNaverSettlement) throws Exception{
		return (String) selectByPk("panaver.v3.account.selectPaDoFlagCheck", paNaverSettlement);
	}

	/**
	 * 정산 매출정보 업데이트
	 * 
	 * @param paNaverSettlement
	 * @return
	 * @throws Exception
	 */
	public int updatePaorderm(PanaversettlementVO paNaverSettlement) throws Exception{
		return update("panaver.v3.account.updatePaorderm", paNaverSettlement);
	}
}
