package com.cware.netshopping.panaver.v3.repository;


import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;


@Repository("panaver.v3.claim.paNaverV3ClaimDAO")
public class PaNaverV3ClaimDAO extends AbstractPaDAO {
	
	/**
	 * 네이버 반품승인처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaNaverClaimApprovalList(ParamMap paramMap) throws Exception{
		return list("panaver.v3.claim.selectPaNaverClaimApprovalList",paramMap.get());
	}
	
	/**
	 * 네이버 반품승인처리 - TPAORDERM 반품승이처리 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.v3.claim.updatePaOrdermResult", paorderm);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.claim.selectReturnClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.claim.selectReturnClaimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.claim.selectReturnClaimTargetDt20List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.v3.claim.selectReturnCancelTargetDtList", paramMap.get());
	}
	
	
}