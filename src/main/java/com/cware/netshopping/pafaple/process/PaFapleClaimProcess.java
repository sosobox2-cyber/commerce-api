package com.cware.netshopping.pafaple.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaFapleClaimProcess {

	/**
	 * 패션플러스 반품조회
	 * @param processFlag
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	ResponseMsg getReturnList(String processFlag, String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 BO 반품 데이터 조회 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 BO 반품 데이터 조회 디테일 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	List<Object> selectOrderClaimTargetDt30List(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 BO 반품 데이터 조회 디테일 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 주문 반품 주소 데이터 체크 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	String compareAddress(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 반품완료 및 회수송장등록  API
	 * @param request
	 * @throws Exception
	 * @return
	 */
	ResponseMsg returnCompleProc(HttpServletRequest request) throws Exception;

	
	/**
	 * 패션플러스 반품 철회 조회  API
	 * @param request
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	ResponseMsg getReturnCancelList(HttpServletRequest request, String fromDate, String toDate) throws Exception;

	/**
	 * 패션플러스 교환 조회  API
	 * @param request
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	ResponseMsg getExchangeList(HttpServletRequest request, String fromDate, String toDate, String claimGb) throws Exception;

	/**
	 * 패션플러스 교환철회 대상 조회  API
	 * @param request
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	ResponseMsg getExchangeCancelList(HttpServletRequest request, String fromDate, String toDate) throws Exception;

	/**
	 * 패션플러스 교환 대상 BO 대상생성 데이터 조회  API
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 교환 대상 BO 대상생성시 재고부족으로 인한 UPDATE 
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;

	/**
	 * 패션플러스 반품 철회 대상 BO 데이터 조회
	 * @param paramMap
	 * @throws Exception
	 * @return
	 */
	List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 교환 철회 대상 BO 데이터 조회
	 * @param paramMap
	 * @throws Exception
	 * @return
	 */
	List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 교환 회수 송장 등록 API 
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	ResponseMsg exchangeSlipOutProc(HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 교환 발송  API
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	ResponseMsg exchangeSlipOutProcStart(HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 교환 주소 일치 검증 로직 
	 * @param ParamMap
	 * @throws Exception
	 * @return
	 */
	String compareAddressExchange(ParamMap paramMap) throws Exception;

	int savePaFapleReturn(Map<String, Object> returnMap, ParamMap param) throws Exception;

	int savePaFapleReturnCancel(Map<String, Object> returnWithdraw, ParamMap param) throws Exception;

	int savePaFapleExchange(Map<String, Object> exchangeMap, ParamMap apiInfoMap) throws Exception;

	
}
