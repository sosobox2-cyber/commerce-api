package com.cware.netshopping.panaver.cancel.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.cancel.paNaverCancelDAO")
public class PaNaverCancelDAO extends AbstractPaDAO {


	/**
	 * 제휴 - 네이버 판매불가처리
	 * @param ParamMap
	 * @return Integer
	 * @throws Exception
	 */
	public int updateCancelSale(ParamMap paramMap) throws Exception{
		return update("panaver.cancel.updateCancelSale", paramMap.get());
	}
	
	//10.09.25
	/**
	 * 제휴 - 네이버 취소승인처리 - 취소승인대상조회
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaNaverOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("panaver.cancel.selectPaNaverOrdCancelApprovalList", cancelMap.get());
	}
	
	/**
	 * TPAORDERM INSERT
	 * @param Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPaOrderM(Paorderm paOrderm) throws Exception{
		return insert("panaver.cancel.insertPaOrderM", paOrderm);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectPaNaverOrdCancelListApprovalList() throws Exception{
		return list("panaver.cancel.selectPaNaverOrdCancelListApprovalList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.cancel.selectCancelInputTargetDtList", paramMap.get());
	}
	
	public int updatePreCancelYn(ParamMap paramMap) throws Exception {
		return update("panaver.cancel.updatePreCancelYn", paramMap.get());
	}
	
	public int insertCancelDoneClaim(ParamMap paramMap) throws Exception {
		return insert("panaver.cancel.insertCancelDoneClaim", paramMap.get());
	}
	
	public int updateCancelDoneClaim(ParamMap paramMap) throws Exception {
		return update("panaver.cancel.updateCancelDoneClaim", paramMap.get());
	}
	
	public int checkExistOrgOrder(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.cancel.checkExistOrgOrder", paramMap.get());
	}
	
	public int checkCancelDoneList(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.cancel.checkCancelDoneList", paramMap.get());
	}
	
}