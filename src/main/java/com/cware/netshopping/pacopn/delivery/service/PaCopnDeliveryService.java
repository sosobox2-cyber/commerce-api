package com.cware.netshopping.pacopn.delivery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PacopnorderlistVO;
import com.cware.netshopping.domain.model.Pacopnorderitemlist;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaCopnDeliveryService {
	
	/**
	 * 상품준비중 주문 저장
	 * @param paCopnOrderList
	 * @param paCopnOrderitemList
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnOrderTx(PacopnorderlistVO paCopnOrderList, List<Pacopnorderitemlist> paCopnOrderitemList) throws Exception;

	/**
	 * 송장업로드 처리 대상 조회
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectBeforeInvoiceList() throws Exception;
	
	/**
	 * 송장업로드 처리
	 * @param apiInfo
	 * @param orderInvoice
	 * @return ParamMap
	 * @throws Exception
	 */
	public ParamMap shippingInvoiceProc(HashMap<String, String> apiInfo, HashMap<String, Object> orderInvoice) throws Exception;
	
	/**
	 * 장기미배송 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectShippingComplete(ParamMap paramMap) throws Exception;
	
	/**
	 * 장기미배송 배송완료 처리 및 저장
	 * @param apiInfo
	 * @param shippingComplete
	 * @return
	 * @throws Exception
	 */
	public ParamMap shippingCompleteProc(HashMap<String, String> apiInfo, HashMap<String, Object> shippingComplete) throws Exception;
	
	/**
	 * 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception;
	
	/**
	 * 배송완료 처리 내역 저장
	 * @param paOrderm
	 * @return
	 * @throws Exception
	 */
	public String updateDeliveryCompleteTx(Paorderm paOrderm) throws Exception;

	public ParamMap shippingInvoiceUpdateProc(HashMap<String, String> apiInfo, Map<String, Object> orderInvoice) throws Exception;

	public List<Map<String, Object>> selectShippingUpdateList() throws Exception;
}
