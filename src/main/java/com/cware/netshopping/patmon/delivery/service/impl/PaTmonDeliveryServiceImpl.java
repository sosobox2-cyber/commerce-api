package com.cware.netshopping.patmon.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaTmonOrderListVO;
import com.cware.netshopping.patmon.delivery.process.PaTmonDeliveryProcess;
import com.cware.netshopping.patmon.delivery.service.PaTmonDeliveryService;

@Service("paTmon.delivery.paTmonDeliveryService")
public class PaTmonDeliveryServiceImpl extends AbstractService implements PaTmonDeliveryService{

	@Autowired
	PaTmonDeliveryProcess paTmonDeliveryProcess;

	@Override
	public String saveTmonOrderListTx(List<PaTmonOrderListVO> paTmonOrderList) throws Exception {
		return paTmonDeliveryProcess.saveTmonOrderList(paTmonOrderList);
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception {
		return paTmonDeliveryProcess.selectDeliveryReadyList();
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryReadyDetailList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryProcess.selectDeliveryReadyDetailList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paTmonDeliveryProcess.selectSlipOutProcList();
	}
	
	@Override
	public List<Map<String, Object>> selectUpdateSlipOutProcList() throws Exception {
		return paTmonDeliveryProcess.selectUpdateSlipOutProcList();
	}
	
	@Override
	public List<Map<String, Object>> selectSlipOutProcOptionList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryProcess.selectSlipOutProcOptionList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectUpdateSlipOutProcOptionList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryProcess.selectUpdateSlipOutProcOptionList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paTmonDeliveryProcess.selectOrderInputTargetList(limitCount);
	}
	
	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonDeliveryProcess.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception {
		return paTmonDeliveryProcess.selectOrderPromo(map);
	}
	
	@Override
	public int updatePaOrderMPreCancelYn(String tmonOrderNo) throws Exception{
		return paTmonDeliveryProcess.updatePaOrderMPreCancelYn(tmonOrderNo);
	}
	
	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception{
		return paTmonDeliveryProcess.updatePreCanYn(map);
	}
	
	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paTmonDeliveryProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryConfirmList() throws Exception {
		return paTmonDeliveryProcess.selectDeliveryConfirmList();
	}
	
	@Override
	public List<Map<String, Object>> selectDeliveryCompleteList(int searchProcDate) throws Exception {
		return paTmonDeliveryProcess.selectDeliveryCompleteList(searchProcDate);
	}
	
	@Override
	public String updatePaTmonOrderListInitialTx(PaTmonOrderListVO vo) throws Exception {
		return paTmonDeliveryProcess.updatePaTmonOrderListInitial(vo);
	}
	
	@Override
	public String updatePaTmonDeliveryCompleteTx(PaTmonOrderListVO vo) throws Exception {
		return paTmonDeliveryProcess.updatePaTmonDeliveryComplete(vo);
	}
	
	@Override
	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception {
		int result = 0;
		
		result = paTmonDeliveryProcess.updatePaOrderMDoFlag(paramMap);
		
		if(result < 1) throw new Exception();
		
		return result;
	}
	
}
