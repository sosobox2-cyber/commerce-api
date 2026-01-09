package com.cware.netshopping.pahalf.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pahalf.order.process.PaHalfOrderProcess;
import com.cware.netshopping.pahalf.order.service.PaHalfOrderService;

@Service("pahalf.order.paHalfOrderService")
public class PaHalfOrderServiceImpl extends AbstractService implements PaHalfOrderService {
	
	@Resource(name = "pahalf.order.paHalfOrderProcess")
	private PaHalfOrderProcess paHalfOrderProcess;

	@Override
	public String savePaHalfOrderTx(Map<String, Object> order) throws Exception {
		return paHalfOrderProcess.savePaHalfOrder(order);
	}

	@Override
	public List<Map<String, Object>> selectOrderConfirmList(String paCode) throws Exception {
		return paHalfOrderProcess.selectOrderConfirmList(paCode);
	}

	@Override
	public int updateTPaorderm(Map<String, Object> order) throws Exception {
		return paHalfOrderProcess.updateTPaorderm(order);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paHalfOrderProcess.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfOrderProcess.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paHalfOrderProcess.selectRefusalInfo(mappingSeq);
	}

	public String savePaHalfCancelTx(Map<String, Object> cancel) throws Exception {
		return paHalfOrderProcess.savePaHalfCancel(cancel);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paHalfOrderProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfOrderProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paHalfOrderProcess.selectPaMobileOrderAutoCancelList();
	}
	 
	
}
