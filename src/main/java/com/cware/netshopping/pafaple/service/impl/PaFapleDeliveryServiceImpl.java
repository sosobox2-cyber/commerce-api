package com.cware.netshopping.pafaple.service.impl;

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
import com.cware.netshopping.pafaple.process.PaFapleDeliveryProcess;
import com.cware.netshopping.pafaple.service.PaFapleDeliveryService;

@Service("pafaple.delivery.paFapleDeliveryService")
public class PaFapleDeliveryServiceImpl extends AbstractService implements PaFapleDeliveryService {
	
	@Autowired
	@Qualifier("pafaple.delivery.paFapleDeliveryProcess")
	PaFapleDeliveryProcess paFapleDeliveryProcess;

	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paFapleDeliveryProcess.getOrderList(fromDate, toDate, request);
	}

	@Override
	public ResponseMsg slipOutProc(String order_id, String item_id, HttpServletRequest request) throws Exception {
		return paFapleDeliveryProcess.slipOutProc(order_id, item_id, request);
	}

	@Override
	public ResponseMsg orderConfirm(String fromDate, String toDate, HttpServletRequest request) throws Exception {
		return paFapleDeliveryProcess.orderConfirm(fromDate, toDate, request);
	}

	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleDeliveryProcess.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectRefusalInfo(List<HashMap<String, Object>> itemList)
			throws Exception {
		return paFapleDeliveryProcess.selectRefusalInfo(itemList);
	}

	@Override
	public int updatePreCanYn(Map<String, Object> preCancelMap) throws Exception {
		return paFapleDeliveryProcess.updatePreCanYn(preCancelMap);
	}

}
