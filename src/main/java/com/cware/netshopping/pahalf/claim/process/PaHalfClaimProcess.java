package com.cware.netshopping.pahalf.claim.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;

public interface PaHalfClaimProcess {

	/**
	 * 교환회수처리 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception;

	/**
	 * 반품처리 리스트 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectReturnApprovalList() throws Exception;


	/**
	 * 하프클럽 클레임 리스트 저장
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public String savePaHalfClaim(Map<String, Object> claim) throws Exception;
	
	/**
	 * TPAORDERM UDPATE
	 * 
	 * @param orderMap
	 * @throws Exception
	 */
	public void updatePaOrderm(Map<String, Object> orderMap) throws Exception;
	/**
	 * 하프클럽 반품 상세 리스트 조회
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectOrderClaimTargetDt30(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 반품 상세 리스트 조회(출고전 반품)
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectOrderClaimTargetDt20(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 CS 주소 체크
	 * @param OrderClaimVO
	 * @return
	 * @throws Exception
	 */
	public String compareAddress(HashMap<String, Object> orderClaimVO) throws Exception;
	/**
	 * 하프클럽 반품 취소 상세 리스트 조회
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectClaimCancelTargetDt(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 교환 상세 리스트 조회
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 교환 취소 상세 리스트 조회
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 배송비 부과 여부 체크
	 * @param Map<String, Object>
	 * @return
	 * @throws Exception
	 */
	public String checkShpFeeYn(HashMap<String, Object> orderClaimTargetDt) throws Exception;
	
}
