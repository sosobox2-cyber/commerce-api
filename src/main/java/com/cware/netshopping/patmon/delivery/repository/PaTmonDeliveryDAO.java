package com.cware.netshopping.patmon.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaTmonOrderListVO;

@Service("patmon.delivery.paTmonDeliveryDAO")
public class PaTmonDeliveryDAO extends AbstractPaDAO{

	public int insertPaTmonOrderList(PaTmonOrderListVO patmonorderlist) {
		return insert("patmon.delivery.insertPaTmonOrderList", patmonorderlist);
	}
	
	public int countOrderList(PaTmonOrderListVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmonOrderNo", vo.getTmonOrderNo());
		map.put("tmonDealNo", vo.getTmonDealNo());
		
		return (Integer) selectByPk("patmon.delivery.countOrderList", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryReadyList() {
		return list("patmon.delivery.selectDeliveryReadyList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryReadyDetailList(ParamMap paramMap) {
		return list("patmon.delivery.selectDeliveryReadyDetailList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcList() {
		return list("patmon.delivery.selectSlipOutProcList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectUpdateSlipOutProcList() {
		return list("patmon.delivery.selectUpdateSlipOutProcList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcOptionList(ParamMap paramMap) {
		return list("patmon.delivery.selectSlipOutProcOptionList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectUpdateSlipOutProcOptionList(ParamMap paramMap) {
		return list("patmon.delivery.selectUpdateSlipOutProcOptionList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("limitCount", limitCount);
		return list("patmon.delivery.selectOrderInputTargetList", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) {
		return list("patmon.delivery.selectOrderInputTargetDtList", paramMap.get());
	}
	
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) {
		return  (OrderpromoVO)(selectByPk("patmon.delivery.selectOrderPromo", map));
	}
	
	public int countOrder(String tmonOrderNo, String deliveryNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmonOrderNo", tmonOrderNo);
		map.put("deliveryNo", deliveryNo);
		
		return (Integer) selectByPk("patmon.delivery.countOrder", map);
	}
	
	public int selectTmonOrderSeq(String tmonOrderNo, String deliveryNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmonOrderNo", tmonOrderNo);
		map.put("deliveryNo", deliveryNo);
		
		return (Integer) selectByPk("patmon.delivery.selectTmonOrderSeq", map);
	}
	
	public int countPaOrderList(PaTmonOrderListVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmonOrderNo", vo.getTmonOrderNo());
		map.put("paCode", vo.getPaCode());
		return (Integer) selectByPk("patmon.delivery.countPaOrderList", map);
	}
	
	public int updatePaOrderMPreCancelYn(String tmonOrderNo) throws Exception{
		return update("patmon.delivery.updatePaOrderMPreCancelYn", tmonOrderNo);
	}
	
	public int updatePreCanYn(Map<String, Object> map) {
		return update("patmon.delivery.updatePreCanYn", map);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) {
		return (HashMap<String, Object>) selectByPk("patmon.delivery.selectRefusalInfo", mappingSeq);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryConfirmList() {
		return list("patmon.delivery.selectDeliveryConfirmList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryCompleteList(int searchProcDate) {
		return list("patmon.delivery.selectDeliveryCompleteList", searchProcDate);
	}
	
	public int updatePaOrderMDoFlag30(PaTmonOrderListVO vo) {
		return update("patmon.delivery.updatePaOrderMDoFlag30", vo);
	}
	
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		return update("patmon.delivery.updatePaOrderMDoFlag", paramMap.get()); 
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderTmonDealOptionList(PaTmonOrderListVO vo) throws Exception {
		return list("patmon.delivery.selectOrderTmonDealOptionList", vo );
	}
	
	public int updateOrderInitial(PaTmonOrderListVO vo) {
		return update("patmon.delivery.updateOrderInitial", vo);
	}

	public int countChkOrderComplete(ParamMap paramMap) {
		return (Integer) selectByPk("patmon.delivery.countChkOrderComplete", paramMap.get());
	}

}
