package com.cware.netshopping.panaver.v3.process;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;


public interface PaNaverV3DeliveryProcess {
	
	/**
	 * 발주 확인 처리
	 * @param changedProductOrderInfo
	 * @param procId
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg orderConfirmProc(String productOrderId, String procId, HttpServletRequest request) throws Exception;
	
	/**
	 * 발주처리 결과 저장
	 * @param ProductOrderInfoAll
	 * @param changedProductOrderInfo
	 * @return int
	 */
	public int updateOrderChangePlaceOrder(ProductOrderInfoAll productOrderInfoAll) throws Exception;
	
	/**
	 * 발송처리 
	 * @param procId
	 * @param productOrderId
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg slipOutProc(String procId, HttpServletRequest request, String productOrderId) throws Exception;
	
	/**
	 * 발송처리 결과 저장
	 * @param paramMap , HashMap<String, Object>
	 * @throws Exception
	 * @return
	 */
	public int updatePaOrdermResult(ParamMap paramMap, Map<String, Object> procMap) throws Exception;

	/**
	 * 네이버 제휴 주문 조회 (TPAORDERM)
	 * 
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception;

	/**
	 * 네이버 주문접수 데이터 상세 조회
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderId) throws Exception;
	
	/**
	 * 발송 지연 처리 
	 * @param procId
	 * @param productOrderId
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg deliveryDelayProc(String procId, HttpServletRequest request, String productOrderId) throws Exception;

}
