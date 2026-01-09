package com.cware.netshopping.pacopn.order.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pacopn.order.process.PaCopnOrderProcess;
import com.cware.netshopping.pacopn.order.service.PaCopnOrderService;

@Service("pacopn.order.paCopnOrderService")
public class PaCopnOrderServiceImpl extends AbstractService implements PaCopnOrderService{
	
	@Resource(name = "pacopn.order.paCopnOrderProcess")
	private PaCopnOrderProcess paCopnOrderProcess;
	
	@Override
	public List<Object> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception{
		return paCopnOrderProcess.selectOrderInputTargetList(paOrderCreateCnt);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderInputTarget) throws Exception{
		return paCopnOrderProcess.selectOrderInputTargetDtList(orderInputTarget);
	}
	
	@Override
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception{
		return paCopnOrderProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paCopnOrderProcess.selectOrderPromo(orderMap);
	}	
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paCopnOrderProcess.selectOrderPaPromo(orderMap);
	}	
}
