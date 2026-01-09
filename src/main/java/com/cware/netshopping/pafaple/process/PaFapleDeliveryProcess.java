package com.cware.netshopping.pafaple.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;

public interface PaFapleDeliveryProcess {

	/**
	 * 패션플러스 주문목록조회
	 * 
	 * @param String
	 * @param String
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 발송처리
	 * 
	 * @param String
	 * @param String
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	public ResponseMsg slipOutProc(String order_id, String item_id, HttpServletRequest request) throws Exception;
	
	/**
	 * 패션플러스 주문목록확인
	 * 
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public ResponseMsg orderConfirm(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 패션플러스 주문접수 데이터 상세 조회
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 패션플러스 공통 상품 풀절로 인한 취소 대상 조회
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectRefusalInfo(List<HashMap<String, Object>> itemList) throws Exception;

	/**
	 * 패션플러스 반품 기취소 여부 업데이트 
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception;

}
