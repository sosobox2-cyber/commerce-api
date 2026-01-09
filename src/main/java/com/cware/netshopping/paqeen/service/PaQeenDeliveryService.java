package com.cware.netshopping.paqeen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.OrderList;

public interface PaQeenDeliveryService {

	/**
	 * 퀸잇 주문목록조회
	 * @param HttpServletRequest
	 * @param String
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 주문 저장 
	 * @param OrderList
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public String savePaQeenOrderTx(OrderList order, String paCode) throws Exception;

	/**
	 * 퀸잇 배송 상태 변경
	 * @param deliveryStateEnum
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg putOrderState(String deliveryStateEnum, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 BO 데이터 저장 
	 * @param deliveryStateEnum
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 품절취소 대상 조회 
	 * @param deliveryStateEnum
	 * @param String
	 * @throws Exception
	 * @return
	 */
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;

	/**
	 * 퀸잇 송장 입력  
	 * @param HttpServletRequest
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg slipOutProc(HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 기취소 여부 업데이트 
	 * @param Map<String, Object>
	 * @throws Exception
	 * @return
	 */
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception;



}
