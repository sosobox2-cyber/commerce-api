package com.cware.netshopping.panaver.v3.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaNaverV3CancelService {

	/**
	 * 네이버 취소요청승인
	 * 
	 * @param orderId
	 * @param productOrderId
	 * @param outBefClaimGb
	 * @param procId
	 * @param request
	 * @return
	 */
	public ResponseMsg approvalCancel(String orderId, String productOrderId, String outBefClaimGb, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 취소요청승인 처리결과 저장
	 * 
	 * @param cancelMap
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelApprovalProcTx(Map<String, Object> cancelMap) throws Exception;

	/**
	 * 네이버 취소 요청
	 * 
	 * @param productOrderId
	 * @param paOrderNo
	 * @param paOrderSeq
	 * @param cancelReasonCode
	 * @param paCode
	 * @param procId
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public ResponseMsg requestCancel(String productOrderId, String paOrderNo, String paOrderSeq, String paCode, int cancelReasonCode, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 취소요청 처리결과 저장
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String saveCancelSaleTx(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 기취소건 처리 (TPAORDERM UPDATE)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYnTx(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 주문 취소 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
}
