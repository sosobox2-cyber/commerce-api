package com.cware.netshopping.paqeen.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.OrderList;
import com.cware.netshopping.paqeen.process.PaQeenDeliveryProcess;
import com.cware.netshopping.paqeen.service.PaQeenDeliveryService;

@Service("paqeen.delivery.paQeenDeliveryService")
public class PaQeenDeliveryServiceImpl extends AbstractService implements PaQeenDeliveryService {
	
	@Autowired
	@Qualifier("paqeen.delivery.paQeenDeliveryProcess")
	PaQeenDeliveryProcess paQeenDeliveryProcess;

	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paQeenDeliveryProcess.getOrderList(fromDate, toDate, request);
	}

	@Override
	public String savePaQeenOrderTx(OrderList order, String paCode) throws Exception {
		return paQeenDeliveryProcess.savePaQeenOrderTx(order, paCode);
	}

	@Override
	public ResponseMsg putOrderState(String deliveryStateEnum, HttpServletRequest request) throws Exception {
		return paQeenDeliveryProcess.putOrderState(deliveryStateEnum, request);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenDeliveryProcess.selectOrderInputTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paQeenDeliveryProcess.selectRefusalInfo(mappingSeq);
	}

	@Override
	public ResponseMsg slipOutProc(HttpServletRequest request) throws Exception {
		return paQeenDeliveryProcess.slipOutProc(request);
	}

	@Override
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return paQeenDeliveryProcess.updatePreCanYn(preCancelMap);
	}

}
