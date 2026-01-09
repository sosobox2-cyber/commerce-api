package com.cware.netshopping.patdeal.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.patdeal.message.OrderConfirmResoponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.patdeal.domain.OrderList;

public interface PaTdealDeliveryService {
	
	/**
	 * 주문목록조회
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception;
	
	
	
	public String savePaTdealOrderTx(OrderList order, String paCode) throws Exception;
	
	
	
	/**
	 * 티딜 주문승인
	 * @param orderProductOptionNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public OrderConfirmResoponseMsg orderConfirmProc(String orderProductOptionNo, HttpServletRequest request) throws Exception;
	
	
	/**
	 * 티딜 주문생성 리스트 조회
	 * @param limitCount
	 * @return
	 * @throws Exception
	 * @return
	 */
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;
	
	/**
	 * 티딜 주문접수 데이터 상세 조회
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 티딜 발송처리
	 * @param orderProductOptionNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg slipOutProc(String orderProductOptionNo, HttpServletRequest request) throws Exception;
	
	
	/**
	 * 티딜 주문상품 수취확인처리 요청하기
	 * @param orderProductOptionNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg deliveryCompleteProc(String orderProductOptionNo, HttpServletRequest request) throws Exception;
	
	
	/**
	 * 판매거절 주문 정보 조회
	 * @param mappingSeq
	 * @return
	 */
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;
	
	
	/**
	 * 티딜 취소철회 대상건 발송처리 
	 * @param paCode
	 * @param orderProductOptionNo
	 * @param deliveryCompanyType
	 * @param invoiceNo
	 * @param request
	 * @throws Exception
	 * @return
	 */
	public ResponseMsg cancelRejcectSlipOutProc(String paCode, String orderProductOptionNo, String deliveryCompanyType,String invoiceNo,HttpServletRequest request) throws Exception;
	
}
