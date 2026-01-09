package com.cware.netshopping.pakakao.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PaKakaoOrderListVO;

@Service("pakakao.delivery.paKakaoDeliveryDAO")
public class PaKakaoDeliveryDAO extends AbstractPaDAO{

	public int countOrderList(String orderId) throws Exception {
		return (Integer) selectByPk("pakakao.delivery.countOrderList", orderId);
	}
	
	public int insertPaKakaoOrderList(PaKakaoOrderListVO paKakaoOrderListVO) {
		return insert("pakakao.delivery.insertPaKakaoOrderList", paKakaoOrderListVO);
	}
		
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return update("pakakao.delivery.updatePreCanYn", map);
	}	

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("limitCount", limitCount);
		return list("pakakao.delivery.selectOrderInputTargetList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) {
		return list("pakakao.delivery.selectOrderInputTargetDtList", order);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryConfirmList() throws Exception {
		return list("pakakao.delivery.selectDeliveryConfirmList", null);
	}

	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return update("pakakao.delivery.updatePaOrderMDoFlag", map);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) {
		return (HashMap<String, Object>) selectByPk("pakakao.delivery.selectRefusalInfo", mappingSeq);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcList() {
		return list("pakakao.delivery.selectSlipOutProcList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcOptionList(Map<String, Object> saveMap) {
		return list("pakakao.delivery.selectSlipOutProcOptionList", saveMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return list("pakakao.delivery.selectDelayOrderList", null);
	}

	public int updatePaKakaoDeliveryExpectedAt(Map<String, Object> delayMap) throws Exception {
		return update("pakakao.delivery.updatePaKakaoDeliveryExpectedAt", delayMap);
	}

	public int countChkOrderComplete(Map<String, Object> completeOrder) {
		return (Integer) selectByPk("pakakao.delivery.countChkOrderComplete", completeOrder);
	}

	public int updateBuyDecision(PaKakaoOrderListVO vo) throws Exception {
		return update("pakakao.delivery.updateBuyDecision", vo);
	}

	public int checkUpdateBuyDecision(PaKakaoOrderListVO vo) {
		return (Integer) selectByPk("pakakao.delivery.checkUpdateBuyDecision", vo);
	}
}
