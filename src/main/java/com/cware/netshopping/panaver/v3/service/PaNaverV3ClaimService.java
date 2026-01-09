package com.cware.netshopping.panaver.v3.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaNaverV3ClaimService {
	
	/**
	 * 반품승인처리 호출
	 * @param  procId
	 * @param  productOrderId
	 * @return ResponseMsg
	 * @throws Exception
	 */
	public ResponseMsg returnConfirmProc(String procId, HttpServletRequest request, String productOrderId) throws Exception;
	
	/**
	 * 반품승인처리 - 반품승인처리 저장
	 * @param Map<String, Object>
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap saveClaimApprovalProcTx(Map<String, Object> claimMap) throws Exception;
	
	/** returnInputAsync
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception;
	
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception;
	
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	
	/**
	 * 반품보류처리 호출
	 * @param  productOrderId
	 * @param  holdbackClassType
	 * @param  holdbackReturnDetailReason
	 * @param  extraReturnFeeAmount
	 * @param  procId
	 * @return ResponseMsg
	 * @throws Exception
	 */
	public ResponseMsg returnHoldbackProc(String productOrderId, String holdbackClassType, String holdbackReturnDetailReason, Double extraReturnFeeAmount, String procId, HttpServletRequest request) throws Exception;
	
	/**
	 * 반품보류해제 호출
	 * @param  productOrderId
	 * @param  procId
	 * @return ResponseMsg
	 * @throws Exception
	 */
	public ResponseMsg returnHoldbackReleaseProc(String productOrderId,  String procId, HttpServletRequest request) throws Exception;

}
