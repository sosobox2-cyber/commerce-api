package com.cware.netshopping.pacopn.order.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pacopn.order.process.PaCopnOrderProcess;
import com.cware.netshopping.pacopn.order.repository.PaCopnOrderDAO;

@Service("pacopn.order.paCopnOrderProcess")
public class PaCopnOrderProcessImpl extends AbstractService implements PaCopnOrderProcess{
	
	@Resource(name = "pacopn.order.paCopnOrderDAO")
	private PaCopnOrderDAO paCopnOrderDAO;
	
	@Override
	public List<Object> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception{
		return paCopnOrderDAO.selectOrderInputTargetList(paOrderCreateCnt);
	}

	@Override
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderInputTarget) throws Exception{
		return paCopnOrderDAO.selectOrderInputTargetDtList(orderInputTarget);
	}
	
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception{
		return paCopnOrderDAO.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paCopnOrderDAO.selectOrderPromo(orderMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paCopnOrderDAO.selectOrderPaPromo(orderMap);
	}
}
