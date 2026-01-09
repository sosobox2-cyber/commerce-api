package com.cware.netshopping.passg.delivery.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgOrderListVO;

@Service("passg.delivery.paSsgDeliveryDAO")
public class PaSsgDeliveryDAO extends AbstractPaDAO{

	public int countChangeOrderList(PaSsgOrderListVO vo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo",      vo.getOrdNo());
		paramMap.put("ordItemSeq", vo.getOrdItemSeq());
		paramMap.put("shppNo",	   vo.getShppNo());
		paramMap.put("shppSeq",	   vo.getShppSeq());
		return (Integer) selectByPk("passg.delivery.countChangeOrderList", paramMap.get());
	}
	
	public int countOrderList(PaSsgOrderListVO vo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("ordNo",      vo.getOrordNo());
		paramMap.put("ordItemSeq", vo.getOrordItemSeq());
		return (Integer) selectByPk("passg.delivery.countOrderList", paramMap.get());
	}

	public int insertPaSsgOrderList(PaSsgOrderListVO vo) throws Exception {
		return insert("passg.delivery.insertPaSsgOrderList", vo);
	}
	
	public int updatePreCanYn(Map<String, Object> map) {
		return update("passg.delivery.updatePreCanYn", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("limitCount", limitCount);
		return list("passg.delivery.selectOrderInputTargetList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) {
		return list("passg.delivery.selectOrderInputTargetDtList", order);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) {
		return (HashMap<String, Object>) selectByPk("passg.delivery.selectRefusalInfo", mappingSeq);
	}

	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		return update("passg.delivery.updatePaOrderMDoFlag", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPartialSlipList() {
		return list("passg.delivery.selectPartialSlipList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcList() {
		return list("passg.delivery.selectSlipOutProcList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectReleaseOrderList() {
		return list("passg.delivery.selectReleaseOrderList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderCompleteList() throws Exception {
		return list("passg.delivery.selectOrderCompleteList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaSsgChangeConfirmList() throws Exception {
		return list("passg.delivery.selectPaSsgChangeConfirmList", null);
	}

	public int updateChangeOrderDoFlag(PaSsgOrderListVO vo) throws Exception {
		return update("passg.delivery.updateChangeOrderDoFlag", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return list("passg.delivery.selectDelayOrderList", null);
	}

	public int updatePaSsgWhinExpcDt(ParamMap paramMap) throws Exception {
		return update("passg.delivery.updatePaSsgWhinExpcDt", paramMap.get());
	}
	
	public int updatePartialFlag(ParamMap paramMap) throws Exception {
		return update("passg.delivery.updatePartialFlag", paramMap.get());
	}

	public int updatePartialShhpNo(Map<String, Object> partialDataMap) throws Exception {
		return update("passg.delivery.updatePartialShhpNo", partialDataMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPartialDeliveryList() throws Exception {
		return list("passg.delivery.selectPartialDeliveryList", null);
	}
	
	public int updatePartialHoldYn(ParamMap paramMap) throws Exception {
		return update("passg.delivery.updatePartialHoldYn", paramMap.get());
	}
	
	public int countChangeWhOut(PaSsgOrderListVO vo) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("orordNo"	   , vo.getOrordNo());
		paramMap.put("orordItemSeq", vo.getOrordItemSeq());
		paramMap.put("ordNo",      vo.getOrdNo());
		paramMap.put("shppNo",	   vo.getShppNo());
		paramMap.put("shppSeq",	   vo.getShppSeq());
		return (Integer) selectByPk("passg.delivery.countChangeWhOut", paramMap.get());
	}

	public int updatePaOrderMRemark(ParamMap paramMap) throws Exception {
		return update("passg.delivery.updatePaOrderMRemark", paramMap.get());
	}

}
