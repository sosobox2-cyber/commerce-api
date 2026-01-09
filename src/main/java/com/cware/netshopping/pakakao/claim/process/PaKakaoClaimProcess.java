package com.cware.netshopping.pakakao.claim.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoOrderListVO;

public interface PaKakaoClaimProcess {

	/**
	 * 취소정보  저장
	 * @param PaKakaoCancelListVO
	 * @return int
	 * @throws Exception
	 */
	public void saveKakaoCancelList(PaKakaoOrderListVO vo) throws Exception;
	
	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPaKakaoOrderCancelList() throws Exception;

	public String updatePaKakaoCancelConfirm(ParamMap paramMap) throws Exception;
	
	public String updatePaKakaoCancelList(ParamMap paramMap) throws Exception;
	
	public void saveKakaoWithdrawCancelList(PaKakaoOrderListVO vo) throws Exception;
	
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;
	
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	
	public String saveKakaoClaimList(PaKakaoOrderListVO vo) throws Exception;
	
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;
	
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	
	public String compareAddress(ParamMap paramMap) throws Exception;
	
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;
	
	public HashMap<String, Object> selectRefusalInfo(ParamMap paramMap) throws Exception;
	
	public int updatePreCanYn(ParamMap paramMap) throws Exception;
	
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	public List<Map<String, Object>> selectPaKakaoClaimInvTgList(ParamMap paramMap) throws Exception;
	
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception;
	
	public List<Map<String, Object>> selectPaKakaoClaimCollCmpTgList(ParamMap paramMap) throws Exception;
	
	public List<Map<String, Object>> selectPaKakaoReturnCmpTgList() throws Exception;
	
	public List<Map<String, Object>> selectPaKakaoExchangeSlipOutList() throws Exception;
	
	public int countCancelList(String orderId, String claimStatus) throws Exception;
	
	public void updateKakaoExchangeComplete(PaKakaoOrderListVO vo) throws Exception;
	
	public List<Map<String, Object>> selectPaKakaoClaimHoldTargetList(ParamMap paramMap) throws Exception;
	
	public int updatePaOrderMHoldYn(Map<String, Object> map) throws Exception;

	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;
}
