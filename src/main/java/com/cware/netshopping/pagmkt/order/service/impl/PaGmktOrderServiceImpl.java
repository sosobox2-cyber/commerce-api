package com.cware.netshopping.pagmkt.order.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.ParamMap;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.pagmkt.order.process.PaGmktOrderProcess;
import com.cware.netshopping.pagmkt.order.service.PaGmktOrderService;


@Service("pagmkt.order.PaGmktOrderService")
public class PaGmktOrderServiceImpl  extends AbstractService implements PaGmktOrderService {

	@Resource(name = "pagmkt.order.PaGmktOrderProcess")
    private PaGmktOrderProcess paGmktOrderProcess;
	
	@Override
	public List<Object> selectOrderInputTargetList(ParamMap paramMap) throws Exception{
		return paGmktOrderProcess.selectOrderInputTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectCancelInputTargetList(ParamMap paramMap) throws Exception{
		return paGmktOrderProcess.selectCancelInputTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return paGmktOrderProcess.selectOrderInputTargetDtList(orderMap);
	}
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktOrderProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception {
		return paGmktOrderProcess.updatePreCancelYn(preCancelMap);
	}
	
	@Override
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return paGmktOrderProcess.selectRefusalInfo(mappingSeq);
	}

	@Override
	public String getSourcingMediaForTest(String goodsCode) throws Exception{
		return paGmktOrderProcess.getSourcingMediaForTest(goodsCode);
	}

	@Override
	public String getSourcingMediaForTest(Map<String, String> map) throws Exception{
		return paGmktOrderProcess.getSourcingMediaForTest(map);
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetDtList(String paOrderNo) throws Exception {
		return paGmktOrderProcess.selectPreOrderUpdateTargetDtList(paOrderNo);
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetList(ParamMap paramMap) throws Exception {
		return paGmktOrderProcess.selectPreOrderUpdateTargetList(paramMap);
	}

	@Override
	public int selectUnAttendedCount(String payNo) throws Exception {
		return paGmktOrderProcess.selectUnAttendedCount(payNo);
	}

	@Override
	public double selectPaOrderShipCost(ParamMap paramMap) throws Exception {
		return paGmktOrderProcess.selectPaOrderShipCost(paramMap);
	}

	@Override
	public List<Object> selectPreOrderInputTargetList(ParamMap paramMap) throws Exception {
		return paGmktOrderProcess.selectPreOrderInputTargetList(paramMap);
	}

	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paGmktOrderProcess.selectOrderPromo(orderMap);
	}	

	@Override
	public HashMap<String, Object> selectPaAddrInfo(ParamMap paramMap) throws Exception {
		return paGmktOrderProcess.selectPaAddrInfo(paramMap);
	}

	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paGmktOrderProcess.selectOrderPaPromo(orderMap);
	}	
}