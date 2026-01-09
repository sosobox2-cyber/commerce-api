package com.cware.netshopping.pafaple.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaFapleDeliveryService {

	/**
	 * 패션플러스 주문목록조회
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 발송처리
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg slipOutProc(String order_id, String item_id, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 주문목록확인
	 	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg orderConfirm(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 공통 주문 저장 대상 조회 
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 공통 상품 풀절로 인한 취소 대상 조회
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public List<HashMap<String, Object>> selectRefusalInfo(List<HashMap<String, Object>> itemList)  throws Exception;

	/**
	 * 패션플러스 반품 기취소 건 
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception;


}
