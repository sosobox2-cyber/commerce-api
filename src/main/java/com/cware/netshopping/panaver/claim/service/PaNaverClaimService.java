package com.cware.netshopping.panaver.claim.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaNaverClaimService {

	/**
	 * 제휴 - 네이버 반품승인대상 조회(리스트)
	 * @return List<Object>
	 */
	public List<Object> selectPaNaverClaimApprovalList() throws Exception;

	/**
	 * 반품승인처리 - 반품승인처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public ParamMap saveClaimApprovalProcTx(HashMap<String, String> claimMap) throws Exception;

	/**
	 * 반품보류처리 - 반품보류대상 조회
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnHoldList() throws Exception;

	/**
	 * 반품완료보류처리 - 반품완료보류 처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnHoldProcTx(HashMap<String, Object> claimMap) throws Exception;

	/**
	 * 제휴 - 네이버 반품보류 해제 대상 조회(리스트)
	 * @return List<Object>
	 */
	public List<Object> selectReleaseReturnHoldList() throws Exception;

	public int updatePaOrdermHoldYnTx(Paorderm paorderm) throws Exception;

	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception;

	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception;
	
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception;

}
