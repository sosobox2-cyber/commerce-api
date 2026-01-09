package com.cware.netshopping.panaver.v3.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.v3.cancel.paNaverV3CancelDAO")
public class PaNaverV3CancelDAO extends AbstractPaDAO {

	/**
	 * 취소승인대상 조회
	 * 
	 * @param cancelMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaNaverOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("panaver.v3.cancel.selectPaNaverOrdCancelApprovalList", cancelMap.get());
	}

	/**
	 * 취소요청승인 처리결과 저장 (TPANAVERCLAIMLIST INSERT)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int insertCancelDoneClaim(ParamMap paramMap) throws Exception {
		return insert("panaver.v3.cancel.insertCancelDoneClaim", paramMap.get());
	}

	/**
	 * 취소요청승인 처리결과 저장 (TPAORDERM INSERT)
	 * 
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int insertPaOrderM(Paorderm paorderm) throws Exception {
		return insert("panaver.v3.cancel.insertPaOrderM", paorderm);
	}

	/**
	 * 취소요청 처리결과 저장 (TPAORDERM UPDATE)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateCancelSale(ParamMap paramMap) throws Exception {
		return update("panaver.v3.cancel.updateCancelSale", paramMap.get());
	}

	/**
	 * 네이버 기취소건 처리 (TPAORDERM UPDATE)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap paramMap) throws Exception {
		return update("panaver.v3.cancel.updatePreCancelYn", paramMap.get());
	}

	/**
	 * 네이버 주문 취소 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.v3.cancel.selectCancelInputTargetDtList", paramMap.get());
	}

	/**
	 * 취소완료일 UPDATE
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateCancelDoneClaim(ParamMap paramMap) throws Exception {
		return update("panaver.v3.cancel.updateCancelDoneClaim", paramMap.get());
	}
}
