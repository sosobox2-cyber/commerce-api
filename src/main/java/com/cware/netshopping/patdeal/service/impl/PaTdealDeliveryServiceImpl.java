package com.cware.netshopping.patdeal.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.patdeal.message.OrderConfirmResoponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.patdeal.domain.OrderList;
import com.cware.netshopping.patdeal.process.PaTdealDeliveryProcess;
import com.cware.netshopping.patdeal.service.PaTdealDeliveryService;

@Service("patdeal.delivery.paTdealDeliveryService")
public class PaTdealDeliveryServiceImpl extends AbstractService implements PaTdealDeliveryService{
	
	@Autowired
	@Qualifier("patdeal.delivery.paTdealDeliveryProcess")
	PaTdealDeliveryProcess paTdealDeliveryProcess;

	@Override
	public ResponseMsg getOrderList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paTdealDeliveryProcess.getOrderList(fromDate, toDate, request);
	}
	@Override
	public String savePaTdealOrderTx(OrderList order, String paCode) throws Exception {
		return paTdealDeliveryProcess.savePaTdealOrder(order, paCode);
	}
	
	@Override
	public OrderConfirmResoponseMsg orderConfirmProc(String orderProductOptionNo, HttpServletRequest request) throws Exception{
		return paTdealDeliveryProcess.orderConfirmProc(orderProductOptionNo, request);
	}
	@Override
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception{
		return paTdealDeliveryProcess.selectOrderInputTargetList(limitCount);
	}
	@Override
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception{
		return paTdealDeliveryProcess.selectOrderInputTargetDtList(paramMap);
	}
	@Override
	public ResponseMsg slipOutProc(String orderProductOptionNo, HttpServletRequest request) throws Exception{
		return paTdealDeliveryProcess.slipOutProc(orderProductOptionNo, request);
	}
	
	@Override
	public ResponseMsg deliveryCompleteProc(String orderProductOptionNo, HttpServletRequest request) throws Exception{
		return paTdealDeliveryProcess.deliveryCompleteProc(orderProductOptionNo, request);
	}
	
	@Override
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception {
		return paTdealDeliveryProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public ResponseMsg cancelRejcectSlipOutProc(String paCode, String orderProductOptionNo, String deliveryCompanyType,String invoiceNo,HttpServletRequest request) throws Exception{
		return paTdealDeliveryProcess.cancelRejcectSlipOutProc(paCode, orderProductOptionNo, deliveryCompanyType, invoiceNo, request);
	}

}
