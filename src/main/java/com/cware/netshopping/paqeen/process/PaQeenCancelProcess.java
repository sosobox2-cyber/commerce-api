package com.cware.netshopping.paqeen.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaQeenCancelProcess {

	/**
	 * 퀸잇 취소목록 조회 
	 * 
	 * @param String
	 * @param String
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	ResponseMsg getCancelList(String satetAt, String endAt, HttpServletRequest request, String cancelStatus) throws Exception;

	/**
	 * 퀸잇 취소 BO 데이터 적재 
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 취소 BO 데이터 상세조회 
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap)throws Exception;

	/**
	 * 퀸잇 취소 승인
	 * 
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	ResponseMsg cancelConfirmProc(HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 취소 반려
	 * 
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	ResponseMsg cancelRefusalProc(Map<String, Object> requestMap, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 취소 승인 BO 
	 * @param HttpServletRequest
	 * @param HashMap
	 * @return
	 * @throws Exception
	 */
	ParamMap cancelConfirmBo(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 일괄 취소 반품 대상 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectPaQeenSoldOutordList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 취소 요청
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	ResponseMsg cancelRequest(HashMap<String, Object> cancelItem, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 모바일 자동취소 (품절취소반품) 취소 대상 조회
	 * @throws Exception
	 * @return List
	 */
	List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;

	/**
	 * 퀸잇 모바일 자동취소 (품절취소반품) 취소 요청
	 * @throws Exception
	 * @return List
	 */
	ResponseMsg mobileOrderSoldOut(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception;
}
