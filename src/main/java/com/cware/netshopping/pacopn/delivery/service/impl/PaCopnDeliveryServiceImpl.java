package com.cware.netshopping.pacopn.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PacopnorderlistVO;
import com.cware.netshopping.domain.model.Pacopnorderitemlist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacopn.delivery.process.PaCopnDeliveryProcess;
import com.cware.netshopping.pacopn.delivery.service.PaCopnDeliveryService;

@Service("pacopn.delivery.paCopnDeliveryService")
public class PaCopnDeliveryServiceImpl extends AbstractService implements PaCopnDeliveryService{
	
	@Resource(name = "pacopn.delivery.paCopnDeliveryProcess")
	private PaCopnDeliveryProcess paCopnDeliveryProcess;
	
	@Override
	public String savePaCopnOrderTx(PacopnorderlistVO paCopnOrderList, List<Pacopnorderitemlist> paCopnOrderitemList) throws Exception{
		return paCopnDeliveryProcess.savePaCopnOrder(paCopnOrderList, paCopnOrderitemList);
	}
	
	@Override
	public List<Object> selectBeforeInvoiceList() throws Exception{
		return paCopnDeliveryProcess.selectBeforeInvoiceList();
	}
	
	@Override
	public ParamMap shippingInvoiceProc(HashMap<String, String> apiInfo, HashMap<String, Object> orderInvoice) throws Exception{
		return paCopnDeliveryProcess.shippingInvoiceProc(apiInfo, orderInvoice);
	}
	
	@Override
	public List<Object> selectShippingComplete(ParamMap paramMap) throws Exception{
		return paCopnDeliveryProcess.selectShippingComplete(paramMap);
	}
	
	@Override
	public ParamMap shippingCompleteProc(HashMap<String, String> apiInfo, HashMap<String, Object> shippingComplete) throws Exception{
		return paCopnDeliveryProcess.shippingCompleteProc(apiInfo, shippingComplete);
	}
	
	@Override
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return paCopnDeliveryProcess.selectDeliveryCompleteList();
	}
	
	@Override
	public String updateDeliveryCompleteTx(Paorderm paOrderm) throws Exception{
		return paCopnDeliveryProcess.updateDeliveryComplete(paOrderm);
	}

	@Override
	public ParamMap shippingInvoiceUpdateProc(HashMap<String, String> apiInfo, Map<String, Object> orderInvoice) throws Exception {
		return paCopnDeliveryProcess.shippingInvoiceUpdateProc(apiInfo, orderInvoice);
	}

	@Override
	public List<Map<String, Object>> selectShippingUpdateList() throws Exception {
		return paCopnDeliveryProcess.selectShippingUpdateList();
	}
}
