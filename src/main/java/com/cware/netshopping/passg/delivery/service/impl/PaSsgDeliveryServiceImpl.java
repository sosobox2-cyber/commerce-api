package com.cware.netshopping.passg.delivery.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.passg.delivery.process.PaSsgDeliveryProcess;
import com.cware.netshopping.passg.delivery.service.PaSsgDeliveryService;

@Service("passg.delivery.paSsgDeliveryService")
public class PaSsgDeliveryServiceImpl extends AbstractService implements PaSsgDeliveryService{

	@Autowired
	PaSsgDeliveryProcess paSsgDeliveryProcess;

	@Override
	public String saveSsgOrderListTx(PaSsgOrderListVO vo) throws Exception {
		return paSsgDeliveryProcess.saveSsgOrderList(vo);
	}
	
	@Override
	public String saveSsgChangeOrderListTx(PaSsgOrderListVO vo) throws Exception {
		return paSsgDeliveryProcess.saveSsgChangeOrderList(vo);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paSsgDeliveryProcess.selectOrderInputTargetList(limitCount);
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception{
		return paSsgDeliveryProcess.updatePreCanYn(map);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) throws Exception {
		return paSsgDeliveryProcess.selectOrderInputTargetDtList(order);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paSsgDeliveryProcess.selectRefusalInfo(mappingSeq);
	}

	@Override
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		return paSsgDeliveryProcess.updatePaOrderMDoFlag(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPartialSlipList() throws Exception {
		return paSsgDeliveryProcess.selectPartialSlipList();
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paSsgDeliveryProcess.selectSlipOutProcList();
	}

	@Override
	public List<Map<String, Object>> selectReleaseOrderList() throws Exception {
		return paSsgDeliveryProcess.selectReleaseOrderList();
	}

	@Override
	public List<Map<String, Object>> selectOrderCompleteList() throws Exception {
		return paSsgDeliveryProcess.selectOrderCompleteList();
	}

	@Override
	public List<Map<String, Object>> selectPaSsgChangeConfirmList() throws Exception {
		return paSsgDeliveryProcess.selectPaSsgChangeConfirmList();
	}
	
	@Override
	public List<Map<String, Object>> selectDelayOrderList() throws Exception {
		return paSsgDeliveryProcess.selectDelayOrderList();
	}

	@Override
	public int updatePaSsgWhinExpcDtTx(ParamMap paramMap) throws Exception {
		return paSsgDeliveryProcess.updatePaSsgWhinExpcDt(paramMap);
	}
	
	@Override
	public int updatePartialFlagTx(ParamMap paramMap) throws Exception {
		return paSsgDeliveryProcess.updatePartialFlag(paramMap);
	}

	@Override
	public int updatePartialShhpNoTx(Map<String, Object> partialDataMap) throws Exception {
		return paSsgDeliveryProcess.updatePartialShhpNo(partialDataMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPartialDeliveryList() throws Exception {
		return paSsgDeliveryProcess.selectPartialDeliveryList();
	}
}
