package com.cware.netshopping.pakakao.delivery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.netshopping.domain.PaKakaoOrderListVO;

public interface PaKakaoDeliveryService {

	public int countOrderList(String orderId) throws Exception;
	
	public int updatePreCanYn(Map<String, Object> map) throws Exception;
	
	public String saveKakaoOrderListTx(PaKakaoOrderListVO  paKakaoOrderListVO) throws Exception;

	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;

	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order)  throws Exception;

	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception;

	public HashMap<String, Object> selectRefusalInfo(String string) throws Exception;

	public List<Map<String, Object>> selectSlipOutProcList() throws Exception;

	public List<Map<String, Object>> selectSlipOutProcOptionList(HashMap<String, Object> saveMap) throws Exception;

	public List<Map<String, Object>> selectDelayOrderList() throws Exception;

	public int updatePaKakaoDeliveryExpectedAt(Map<String, Object> delaymap) throws Exception;

	public void updatePaKakaoDeliveryCompleteTx(PaKakaoOrderListVO vo) throws Exception;

	public void updateBuyDecisionTx(PaKakaoOrderListVO vo)  throws Exception;
	
}
