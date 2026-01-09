package com.cware.netshopping.patdeal.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("patdeal.cancel.paTdealCancelDAO")
public class PaTdealCancelDAO extends AbstractPaDAO {
	/**
	 * 원주문 데이터 유무 체크
	 * @param PaTdealClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int countOrderList(PaTdealClaimListVO vo) throws Exception {
		return (Integer) selectByPk("patdeal.cancel.countOrderList", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크
	 * @param PaTdealClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaTdealCancelListCount(PaTdealClaimListVO vo) throws Exception {
		return (Integer) selectByPk("patdeal.cancel.selectPaTdealCancelListCount", vo);
	}
	
	/**
	 * 취소 데이터 중복 체크(TPAORDERM)
	 * @param PaTdealClaimListVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaTdealCancelMListCount(PaTdealClaimListVO vo) throws Exception {
		return (Integer) selectByPk("patdeal.cancel.selectPaTdealCancelMListCount", vo);
	}
	// 취소 접수 -> 취소 완료  업데이트 
	public int updatePaTdealCancelComplete(PaTdealClaimListVO vo) throws Exception {
		return update("patdeal.cancel.updatePaTdealCancelComplete", vo);
	}
	
	public int insertPaTdealCancelList(PaTdealClaimListVO vo) throws Exception {
		return insert("patdeal.cancel.insertPaTdealCancelList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaTdealCancelInfo(PaTdealClaimListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("patdeal.cancel.selectPaTdealCancelInfo", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaTdealOrderCancelList() throws Exception {
		return list("patdeal.cancel.selectPaTdealOrderCancelList", null);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("patdeal.cancel.selectCancelInputTargetDtList", paramMap.get());
	}

	public int updatePaTdealCancelStatus(Paorderm paorderm) throws Exception {
		return update("patdeal.cancel.updatePaTdealCancelStatus", paorderm);
	}

	public int updatePaTdealCancelwithdrawYn(Map<String, Object> requestMap) throws Exception {
		return update("patdeal.cancel.updatePaTdealCancelwithdrawYn", requestMap);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSlipInfo(Map<String, Object> requestMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("patdeal.cancel.selectSlipInfo", requestMap);
	}

	public int selectOrderDoflag(PaTdealClaimListVO paTdealCancelVo) throws Exception {
		return (Integer) selectByPk("patdeal.cancel.selectOrderDoflag", paTdealCancelVo);
	}

	public int updatePaTdealCancelReject(Map<String, Object> requestMap) throws Exception {
		return update("patdeal.cancel.updatePaTdealCancelReject", requestMap);
	}
	
	public String selectCustCmpstnReason(HashMap<String, String> requestMap) throws Exception {
		
		return (String) selectByPk("patdeal.cancel.selectCustCmpstnReason", requestMap);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() {
		return list("patdeal.cancel.selectPaMobileOrderAutoCancelList", null);
	}
}
