package com.cware.netshopping.panaver.v3.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaNaverV3ExchangeService {

	/**
	 * 네이버 교환 수거 완료
	 * 
	 * @param productOrderId
	 * @param procId 
	 * @param request
	 * @return
	 */
	public ResponseMsg approvalCollect(String productOrderId, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 교환 수거 완료 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermResultTx(Paorderm paorderm) throws Exception;

	/**
	 * 네이버 교환 재배송 처리
	 * 
	 * @param productOrderId
	 * @param procId
	 * @param request
	 * @return
	 */
	public ResponseMsg dispatch(String productOrderId, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 교환 재배송 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updateExchangePaOrdermResultTx(Paorderm paorderm) throws Exception;

	/**
	 * 네이버 교환 거부(철회)
	 * 
	 * @param paorderm
	 * @return
	 */
	public ResponseMsg reject(String productOrderId, String rejectExchangeReason, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 교환 거부(철회) 처리결과 업데이트 (TPAORDERM)
	 * 
	 * @param paramMap
	 * @return
	 */
	public int updatePaOrdermPreCancelTx(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 주문 교환 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 교환 거부 주문정보 조회 (TPAORDERM)
	 * 
	 * @param mappingSeq
	 * @return
	 */
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception;

	/**
	 * 네이버 미처리 교환 데이터 여부 조회
	 * 
	 * @param orderId
	 * @param claimId
	 * @return
	 */
	public int checkOrderChangeTargetList(String orderId, String claimId) throws Exception;

	/**
	 * 네이버 교환 기취소 처리
	 * 
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermPreChangeCancelTx(Paorderm paorderm) throws Exception;

	/**
	 * 네이버 주문 교환 거부 데이터 생성 대상 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 네이버 교환 보류
	 * 
	 * @param productOrderId
	 * @param holdbackClassType  
	 * @param holdbackExchangeDetailReason
	 * @param extraExchangeFeeAmount
	 * @param procId 
	 * @param request
	 * @return
	 */
	public ResponseMsg holdback(String productOrderId, String holdbackClassType, String holdbackExchangeDetailReason, Double extraExchangeFeeAmount, String procId, HttpServletRequest request) throws Exception;

	/**
	 * 네이버 교환 보류 해제
	 * 
	 * @param productOrderId
	 * @param procId 
	 * @param request
	 * @return
	 */
	public ResponseMsg releaseHoldback(String productOrderId, String procId, HttpServletRequest request) throws Exception;
	
	/**
	 * 네이버 교환 데이터 상담원 처리건 데이터 미생성 여부 조회
	 * 
	 * @param orderId
	 * @param claimId
	 * @return
	 */
	public int checkOrderChangeInputTargetList(String orderId, String claimId) throws Exception;

}
