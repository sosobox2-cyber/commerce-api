package com.cware.netshopping.pagmkt.order.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cware.framework.core.basic.ParamMap;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pagmkt.order.process.PaGmktOrderProcess;
import com.cware.netshopping.pagmkt.order.repository.PaGmktOrderDAO;;

@Service("pagmkt.order.PaGmktOrderProcess")
public class PaGmktOrderProcessImpl extends AbstractService implements PaGmktOrderProcess{

	@Resource(name = "pagmkt.order.PaGmktOrderDAO")
	private PaGmktOrderDAO paGmktOrderDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	
	@Resource(name = "common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
		
	public List<Object> selectOrderInputTargetList(ParamMap paramMap) throws Exception{
		return paGmktOrderDAO.selectOrderInputTargetList(paramMap);
	}

	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return paGmktOrderDAO.selectOrderInputTargetDtList(orderMap);
	}	

	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktOrderDAO.selectCancelInputTargetDtList(paramMap);
	}

	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception {
		return paGmktOrderDAO.updatePreCancelYn(preCancelMap);
	}

	public List<Object> selectCancelInputTargetList(ParamMap paramMap) throws Exception{
		return paGmktOrderDAO.selectCancelInputTargetList(paramMap);
	}

	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return paGmktOrderDAO.selectRefusalInfo(mappingSeq);
	}

	@Override
	public String getSourcingMediaForTest(String goodsCode) throws Exception {
		return paGmktOrderDAO.getSourcingMediaForTest(goodsCode);
	}

	@Override
	public String getSourcingMediaForTest(Map<String, String> map) throws Exception{
	return paGmktOrderDAO.getSourcingMediaForTest(map);
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetDtList(String paOrderNo) throws Exception {
		return paGmktOrderDAO.selectPreOrderUpdateTargetDtList(paOrderNo);
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetList(ParamMap paramMap) throws Exception {
		return paGmktOrderDAO.selectPreOrderUpdateTargetList(paramMap);
	}

	@Override
	public int selectUnAttendedCount(String payNo) throws Exception {
		return paGmktOrderDAO.selectUnAttendedCount(payNo);
	}

	@Override
	public double selectPaOrderShipCost(ParamMap paramMap) throws Exception {
		return paGmktOrderDAO.selectPaOrderShipCost(paramMap);
	}

	@Override
	public List<Object> selectPreOrderInputTargetList(ParamMap paramMap) throws Exception {
		return paGmktOrderDAO.selectPreOrderInputTargetList(paramMap);
	}

	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paGmktOrderDAO.selectOrderPromo(orderMap);
	}

	@Override
	public HashMap<String, Object> selectPaAddrInfo(ParamMap paramMap)	throws Exception {
		return paGmktOrderDAO.selectPaAddrInfo(paramMap);

	}	

	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paGmktOrderDAO.selectOrderPaPromo(orderMap);
	}
}