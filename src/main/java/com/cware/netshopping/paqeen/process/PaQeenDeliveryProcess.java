package com.cware.netshopping.paqeen.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.OrderList;

public interface PaQeenDeliveryProcess {

	/**
	 * 퀸잇 주문목록조회
	 * 
	 * @param String
	 * @param String
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 주문저장
	 * 
	 * @param OrderList
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public String savePaQeenOrderTx(OrderList order, String paCode) throws Exception;

	/**
	 * 퀸잇 주문저장
	 * 
	 * @param String
	 * @param HttpServletRequest
	 * @return
	 * @throws Exception
	 */
	public ResponseMsg putOrderState(String deliveryStateEnum, HttpServletRequest request) throws Exception;

	/**
	 * 퀸잇 BO 데이터 저장 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 퀸잇 취소대상 조회
	 * @param List<HashMap<String, Object>>
	 * @return
	 * @throws Exception
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
	 * 퀸잇 기취소 여부 
	 * @param Map<String, Object>
	 * @throws Exception
	 * @return
	 */
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception;

}
