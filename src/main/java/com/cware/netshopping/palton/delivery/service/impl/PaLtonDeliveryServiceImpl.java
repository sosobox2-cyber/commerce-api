package com.cware.netshopping.palton.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaLtonOrderListVO;
import com.cware.netshopping.palton.delivery.process.PaLtonDeliveryProcess;
import com.cware.netshopping.palton.delivery.service.PaLtonDeliveryService;

@Service("palton.delivery.paLtonDeliveryService")
public class PaLtonDeliveryServiceImpl extends AbstractService implements PaLtonDeliveryService{

	@Autowired
	PaLtonDeliveryProcess paLtonDeliveryProcess;

	@Override
	public String saveLtonOrderListTx(PaLtonOrderListVO vo) throws Exception {
		return paLtonDeliveryProcess.saveLtonOrderList(vo);
	}

	@Override
	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception {
		return paLtonDeliveryProcess.selectDeliveryReadyList();
	}

	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paLtonDeliveryProcess.updatePaOrderMDoFlag(map);
	}

	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception {
		return paLtonDeliveryProcess.selectOrderInputTargetList(limitCount);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonDeliveryProcess.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paLtonDeliveryProcess.selectRefusalInfo(mappingSeq);
	}

	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paLtonDeliveryProcess.selectSlipOutProcList();
	}

	@Override
	public List<Map<String, Object>> selectDeliveryCompleteList() throws Exception {
		return paLtonDeliveryProcess.selectDeliveryCompleteList();
	}

	@Override
	public int updatePreCanYn(Map<String, Object> map) throws Exception {
		return paLtonDeliveryProcess.updatePreCanYn(map);
	}

	@Override
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception {
		return paLtonDeliveryProcess.selectOrderPromo(map);
	}

	@Override
	public List<Map<String, Object>> selectExchangeHoldList() throws Exception {
		return paLtonDeliveryProcess.selectExchangeHoldList();
	}

	@Override
	public int saveHoldInfoTx(ParamMap paramMap) throws Exception {
		return paLtonDeliveryProcess.saveHoldInfo(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectRetrievalExceptList() throws Exception {
		return paLtonDeliveryProcess.selectRetrievalExceptList();
	}

	@Override
	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception {
		return paLtonDeliveryProcess.selectSlipUpdateProcList();
	}

	@Override
	public List<Map<String, Object>> selectDeliveryDelayProcList() throws Exception {
		return paLtonDeliveryProcess.selectDeliveryDelayProcList();
	}
	
	@Override
	public int updatePaLtonSndAgrdDttm(Map<String, Object> map) throws Exception {
		return paLtonDeliveryProcess.updatePaLtonSndAgrdDttm(map);
	}
	
}
