package com.cware.netshopping.pacopn.exchange.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnexchangelist;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaCopnExchangeProcess {

	/**
	 * 교환신청/취소 목록 중계 데이터 저장
	 *
	 * @param exchangeListArr
	 * @return
	 * @throws Exception
	 */
	public String saveExchangeList(List<Pacopnexchangelist> exchangeListArr) throws Exception;

	/**
	 * 교환접수 대상 중계 데이터 조회
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetList() throws Exception;

	/**
	 * 교환접수 대상조회 - 상세
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 교환취소 대상 조회
	 *
	 * @param
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetList() throws Exception;

	/**
	 * 11번가 반품취소 대상조회 - 상세
	 *
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 주문취소 처리 - 기취소
	 *
	 * @param preCancelMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception;

	/**
	 * 교환거부처리 - 교환거부처리
	 *
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeRejectProc(ParamMap paramMap) throws Exception;

	/**
	 * 교환요청상품 입고 확인처리 대상 조회
	 *
	 * @param paCode
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectExchangeReturn(String paCode) throws Exception;

	/**
	 * 교환요청상품 입고 확인처리 내역 저장
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeReturnConfirm(ParamMap paramMap) throws Exception;

	/**
	 * 교환상품 송장 업로드 처리 대상 조회
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectExchangeSlipOutTargetList() throws Exception;

	/**
	 * 교환상품 송장 업로드 처리 내역 저장
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateExchangeCompleteResult(ParamMap paramMap) throws Exception;
	
	/**
	 * 교환배송완료 처리 내역 저장
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String updateExchangeDeliveryComplete(Paorderm paOrderm) throws Exception;
	
	/**
	 * 교환배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception;

	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception;
	public HashMap<String, String> selectExchangeDetail(String paClaimNo) throws Exception;

	/**
	 * 교환철회 조회 대상 날짜 조회
	 * @return
	 * @throws Exception
	 */
	public List<String> selectExchangeCreatedDate() throws Exception;
}
