package com.cware.netshopping.panaver.claim.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Service("panaver.claim.paNaverClaimDAO")
public class PaNaverClaimDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<Object> selectPaNaverClaimApprovalList() throws Exception{
		return list("panaver.claim.selectPaNaverClaimApprovalList",null);
	}

	/**
	 * 제휴 - 네이버 반품승인처리 - TPAORDERM 발송처리 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.claim.updatePaOrdermResult", paorderm);
	}

	/**
	 * 제휴 - 네이버 회수확정대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnHoldList() throws Exception{
		return list("panaver.claim.selectReturnHoldList",null);
	}
	
	/**
	 * 제휴 - 네이버 반품완료보류 - TPAORDERM 보류여부 결과 UPDATE
	 * @param  Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception{
		return update("panaver.claim.updatePaOrdermHoldYn", paorderm);
	}

	/**
	 * 제휴 - 네이버 반품 보류 해제 대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReleaseReturnHoldList() throws Exception{
		return list("panaver.claim.selectReleaseReturnHoldList",null);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.claim.selectExchangeRejectInfo", mappingSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.claim.selectReturnClaimTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.claim.selectReturnClaimTargetDt30List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.claim.selectReturnClaimTargetDt20List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.claim.selectReturnCancelTargetDtList", paramMap.get());
	}
	
}
