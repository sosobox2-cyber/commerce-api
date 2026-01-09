package com.cware.netshopping.patdeal.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;

public interface PaTdealCancelProcess {

	public int saveTdealCancelList(String claimStatus, PaTdealClaimListVO paTdealCancelVOList) throws Exception;
	
	public ResponseEntity<?> cancelConfirmProc( HttpServletRequest request) throws Exception;

	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	public ParamMap cancelConfirm(Map<String, Object> requestMap, HttpServletRequest request) throws Exception;

	public Map<String, Object> cancelRequest(Object cancelItem, HttpServletRequest request) throws Exception;

	/**
	 * 티딜 품절 취소처리
	 * @param soldOutList
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg soldOutCancelProc(List<Map<String, Object>> soldOutList, HttpServletRequest request) throws Exception;
	
	
	/**
	 * 티딜 취소처리
	 * @param cancelList
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cancelApprovalProc(List<Map<String, Object>> cancelList, HttpServletRequest request) throws Exception;

	/**
	 * 모바일 자동취소 (품절취소반품) 조회
	 * @throws Exception
	 * @return List
	 */
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;

	/**
	 * 모바일 자동취소 (품절취소반품) 처리
	 * @throws Exception
	 * @return List
	 */
	public Map<String, Object> mobliecancelRequest(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception;

}
