package com.cware.netshopping.panaver.v3.service.impl;


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
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.process.PaNaverV3DeliveryProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3DeliveryService;

@Service("panaver.v3.delivery.paNaverV3DeliveryService")
public class PaNaverV3DeliveryServiceImpl extends AbstractService implements PaNaverV3DeliveryService{
	
	@Autowired
	@Qualifier("panaver.v3.delivery.paNaverV3DeliveryProcess")
	PaNaverV3DeliveryProcess paNaverV3DeliveryProcess;

	@Override
	public ResponseMsg orderConfirmProc(String productOrderId, String procId, HttpServletRequest request) throws Exception{
		return paNaverV3DeliveryProcess.orderConfirmProc(productOrderId, procId,request);
	}
	
	@Override
	public int updateOrderChangePlaceOrderTx(ProductOrderInfoAll productOrderInfoAll) throws Exception{
		return paNaverV3DeliveryProcess.updateOrderChangePlaceOrder(productOrderInfoAll);
	}
	
	@Override
	public ResponseMsg slipOutProc( String procId, HttpServletRequest request, String productOrderId) throws Exception{
		return paNaverV3DeliveryProcess.slipOutProc( procId , request, productOrderId);
	}
	
	@Override
	public int updatePaOrdermResultTx(ParamMap paramMap, Map<String, Object> procMap) throws Exception {
		return paNaverV3DeliveryProcess.updatePaOrdermResult(paramMap, procMap);
	}

	@Override
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return paNaverV3DeliveryProcess.selectOrderMappingInfoByMappingSeq(mappingSeq);
	}

	@Override
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderId) throws Exception {
		return paNaverV3DeliveryProcess.selectOrderInputTargetDtList(orderId);
	}

	@Override
	public ResponseMsg deliveryDelayProc( String procId, HttpServletRequest request, String productOrderId) throws Exception{
		return paNaverV3DeliveryProcess.deliveryDelayProc( procId , request, productOrderId);
	}
}
