package com.cware.netshopping.pakakao.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaKakaoOrderListVO;
import com.cware.netshopping.pakakao.delivery.process.PaKakaoDeliveryProcess;
import com.cware.netshopping.pakakao.delivery.service.PaKakaoDeliveryService;

@Service("pakakao.delivery.paKakaoDeliveryService")
public class PaKakaoDeliveryServiceImpl extends AbstractService implements PaKakaoDeliveryService{

	@Autowired
	PaKakaoDeliveryProcess paKakaoDeliveryProcess;
	
	@Override
	public int countOrderList(String orderId) throws Exception {
		return paKakaoDeliveryProcess.countOrderList(orderId);
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paKakaoDeliveryProcess.updatePreCanYn(map);
	}

	@Override
	public String saveKakaoOrderListTx(PaKakaoOrderListVO paKakaoOrderListVO) throws Exception {
		return paKakaoDeliveryProcess.saveKakaoOrderList(paKakaoOrderListVO);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paKakaoDeliveryProcess.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) throws Exception {
		return paKakaoDeliveryProcess.selectOrderInputTargetDtList(order);
	}

	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paKakaoDeliveryProcess.updatePaOrderMDoFlag(map);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String string) throws Exception {
		return paKakaoDeliveryProcess.selectRefusalInfo(string);
	}

	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paKakaoDeliveryProcess.selectSlipOutProcList();
	}

	@Override
	public List<Map<String, Object>> selectSlipOutProcOptionList(HashMap<String, Object> saveMap) throws Exception {
		return paKakaoDeliveryProcess.selectSlipOutProcOptionList(saveMap);
	}

	@Override
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return paKakaoDeliveryProcess.selectDelayOrderList();
	}

	@Override
	public int updatePaKakaoDeliveryExpectedAt(Map<String, Object> delayMap) throws Exception {
		return paKakaoDeliveryProcess.updatePaKakaoDeliveryExpectedAt(delayMap);
	}

	@Override
	public void updatePaKakaoDeliveryCompleteTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoDeliveryProcess.updatePaKakaoDeliveryComplete(vo);
	}

	@Override
	public void updateBuyDecisionTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoDeliveryProcess.updateBuyDecision(vo);
	}
	
}
