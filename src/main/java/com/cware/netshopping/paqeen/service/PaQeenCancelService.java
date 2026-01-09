package com.cware.netshopping.paqeen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaQeenCancelService {

	/**
	 * 퀸잇 취소목록조회
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getCancelList(String satetAt, String endAt, HttpServletRequest request, String cancelStatus) throws Exception;

	/**
	 * 퀸잇 BO 데이터 적재 대상조회
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 BO 데이터 적재 대상 상세조회
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 BO 데이터 적재 대상 상세조회
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cancelConfirmProc(HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 취소 반려 처리 
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cancelRefusalProc(Map<String, Object> requestMap, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 취소 반려 처리 
	 * @param HttpServletRequest
	 * @param HashMap
	 * @throws Exception
	 * @return
	 */
	public ParamMap cancelConfirmBo(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 취소 반려 처리 
	 * @param HttpServletRequest
	 * @param HashMap
	 * @throws Exception
	 * @return
	 */
	public List<HashMap<String, Object>> selectPaQeenSoldOutordList(ParamMap paramMap) throws Exception;
	
	/**
	 * 퀸잇 취소 요청
	 * @param HttpServletRequest
	 * @param HashMap
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cancelRequest(HashMap<String, Object> cancelItem, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 모바일 자동취소 (품절취소반품) 대상 조회
	 * @throws Exception
	 * @return List
	 */
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;

	/**
	 * 퀸잇 모바일 자동취소 (품절취소반품) 취소 요청
	 * @throws Exception
	 * @return List
	 */
	public ResponseMsg mobileOrderSoldOut(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception;
	
}
