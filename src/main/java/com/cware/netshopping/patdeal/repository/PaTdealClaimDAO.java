package com.cware.netshopping.patdeal.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;


@Repository("patdeal.claim.paTdealClaimDAO")
public class PaTdealClaimDAO extends AbstractPaDAO {
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return list("patdeal.claim.selectClaimTargetList", paramMap.get());
	}

	public int selectPaTdealClaimListCount(PaTdealClaimListVO paTdealClaimVo) throws Exception {
		return (Integer) selectByPk("patdeal.claim.selectPaTdealClaimListCount", paTdealClaimVo);
	}

	public int insertPaTdealClaimList(PaTdealClaimListVO paTdealClaimVo) throws Exception {
		return insert("patdeal.claim.insertPaTdealClaimList", paTdealClaimVo);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaTdealClaimInfo(PaTdealClaimListVO vo) throws Exception {
		return (HashMap<String, Object>) selectByPk("patdeal.claim.selectPaTdealClaimInfo", vo);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return list("patdeal.claim.selectOrderCalimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return list("patdeal.claim.selectOrderCalimTargetDt20List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectNotEndClaimList(String status) throws Exception {
		return list("patdeal.claim.selectNotEndClaimList",status);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return list("patdeal.claim.selectClaimCancelTargetDtList", paramMap.get());
	}

	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		return update("patdeal.claim.updatePaOrderMDoFlag", paorderm); 
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectTdealReturnCompleList() throws Exception {
		return list("patdeal.claim.selectTdealReturnCompleList",null);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception  {
		return list("patdeal.claim.selectOrderChangeTargetDtList",paramMap.get());
	}

	public int updatePaOrdermChangeFlag(ParamMap paramMap)  throws Exception {
		return update("patdeal.claim.updatePaOrdermChangeFlag", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectTdealExchangeReturnDoFlag60List() throws Exception  {
		return list("patdeal.claim.selectTdealExchangeReturnDoFlag60List",null);
	}

	public int updatePaOrdermPaShipNo(HashMap<String, Object> tdealExchangeReturnDoFlag60Item) {
		return update("patdeal.claim.updatePaOrdermPaShipNo", tdealExchangeReturnDoFlag60Item);
	}

	public int updatePaOrderMPreCancel(Map<String, Object> map) throws Exception {
		return update("patdeal.claim.updatePaOrderMPreCancel", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception  {
		return list("patdeal.claim.selectChangeCancelTargetDtList",paramMap.get());
	}

	public int updateTdealClaimListOrderOprtionNo(HashMap<String, Object> tdealExchangeReturnDoFlag60Item) {
		return update("patdeal.claim.updateTdealClaimListOrderOprtionNo", tdealExchangeReturnDoFlag60Item);
	}

	public String compareAddress(ParamMap paramMap) {
		return (String) selectByPk("patdeal.claim.compareAddress", paramMap.get());
	}
	
}