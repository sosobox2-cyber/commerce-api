package com.cware.netshopping.pafaple.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaFapleCancelService {

	/**
	 * 패션플러스 취소조회
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	ResponseMsg getCancelList(String cancelStatus, String fromDate, String toDate, HttpServletRequest request) throws Exception;

	
	/**
	 * 패션플러스 BO 취소 데이터 대상 조회 
	 * @throws Exception
	 * @return
	 */
	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 BO 취소 데이터 대상 상세조회 
	 * @throws Exception
	 * @return
	 */
	HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 품절취소 처리 BO 호출용
	 * @param HashMap<String, Object>
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	ParamMap cancelApprovalProcBo(HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 모바일 자동취소 (품절취소반품)
	 * @param HashMap<String, Object>
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	ParamMap mobileOrderSoldOut(HttpServletRequest request) throws Exception;
}
