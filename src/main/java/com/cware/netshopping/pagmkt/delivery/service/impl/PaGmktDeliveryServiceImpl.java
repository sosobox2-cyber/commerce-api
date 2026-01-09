package com.cware.netshopping.pagmkt.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkDeliveryComplete;
import com.cware.netshopping.domain.model.PaGmkNotRecive;
import com.cware.netshopping.domain.model.PaGmkOrder;
import com.cware.netshopping.pagmkt.delivery.process.PaGmktDeliveryProcess;
import com.cware.netshopping.pagmkt.delivery.service.PaGmktDeliveryService;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;


@Service("pagmkt.delivery.PaGmktDeliveryService")
public class PaGmktDeliveryServiceImpl  extends AbstractService implements PaGmktDeliveryService {

	@Resource(name = "pagmkt.delivery.PaGmktDeliveryProcess")
    private PaGmktDeliveryProcess PaGmktDeliveryProcess;
	
	@Override
	public int insertPaGmktOrderListTx(PaGmkOrder paGmktOrder) throws Exception{
		if (paGmktOrder == null) throw processException("null");
		return PaGmktDeliveryProcess.insertPaGmktOrderList(paGmktOrder);
	}	
	
	@Override
	public List<Map<String,String>> selectDeliveryCompleteList(ParamMap paramMap) throws Exception{
		return PaGmktDeliveryProcess.selectDeliveryCompleteList(paramMap);
	}
	
	@Override
	public int updateDeliveryCompleteProcTx(Map<?, ?> rtnMap) throws Exception{
		return PaGmktDeliveryProcess.updateDeliveryCompleteProc(rtnMap);
	}
	
	//@Override
	//public int updateDeliveryCompleteList(List<PaGmkDeliveryComplete> paGmkDeliveryCompleteList) throws Exception{
		//return PaGmktDeliveryProcess.updateDeliveryCompleteList(paGmkDeliveryCompleteList);
	//}
	
	@Override
	public List<Map<String,Object>> selectSlipOutProcList(ParamMap paramMap) throws Exception{
	    return PaGmktDeliveryProcess.selectSlipOutProcList(paramMap);
	}
	
	@Override
	public int updateSlipOutProcTx(Map<?,?> slipOut) throws Exception{
	    return PaGmktDeliveryProcess.updateSlipOutProc(slipOut);
	}
	
	@Override
	public int updatePaOrderMFail(Map<?,?> slipOut) throws Exception{
	    return PaGmktDeliveryProcess.updatePaOrderMFail(slipOut);
	}

	@Override
	public void insertPaGmktNotReceiveListTx(PaGmkNotRecive notReceive) throws Exception {
		  PaGmktDeliveryProcess.insertPaGmktNotReceiveList(notReceive);
	}

	@Override
	public HashMap<String, String> selectTpaOrdermForNotReceive(Map<String,String> paramMap) throws Exception {
		 return PaGmktDeliveryProcess.selectTpaOrdermForNotReceive(paramMap);
	}

	@Override
	public List<Map<String, String>> selectNotReceiveListForCancel(ParamMap paramMap) throws Exception{
		 return PaGmktDeliveryProcess.selectNotReceiveListForCancel(paramMap);
	}
	
	@Override
	public PaGmkNotRecive selectNotReceiveListDetail(Map<String, Object> paramMap) throws Exception{
		 return PaGmktDeliveryProcess.selectNotReceiveListDetail(paramMap);
	}

	@Override
	public int updateUnReceiveListForCancle(Map<String, Object> param) throws Exception {
		return PaGmktDeliveryProcess.updateUnReceiveListForCancle(param);
	}

	@Override
	public int updateTpaGmktShippingList(ParamMap param) throws Exception {
		return PaGmktDeliveryProcess.updateTpaGmktShippingList(param);
	}

	@Override
	public List<Map<String, Object>> selectChangingDelyList(ParamMap paramMap) throws Exception {
		return PaGmktDeliveryProcess.selectChangingDelyList(paramMap);
	}

	@Override
	public int sendShippingTx(PaGmktAbstractRest rest, List<Map<String, Object>> slipOutProcList, ParamMap paramMap) throws Exception {
		return PaGmktDeliveryProcess.sendShipping(rest,slipOutProcList,paramMap);
	}

	@Override
	public void updateReceiverInfo(PaGmkOrder paGmkOrder) throws Exception {
		PaGmktDeliveryProcess.updateReceiverInfo(paGmkOrder);
	}

	@Override
	public int updateRemark1TpaorderM(Map<String, Object> delyMap) throws Exception{
		return PaGmktDeliveryProcess.updateRemark1TpaorderM(delyMap);
	}

	@Override
	public int checkPaGmktOrderList(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryProcess.checkPaGmktOrderList(paGmktOrder);
	}

	@Override
	public int updateTpaordermForCompleteOrderList(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryProcess.updateTpaordermForCompleteOrderList(paGmktOrder);
	}

	@Override
	public int updatePayDate(PaGmkOrder paGmktOrder) throws Exception {
		return PaGmktDeliveryProcess.updatePayDate(paGmktOrder);
	}

	@Override
	public int checkUnpaidPaGmktOrderList(PaGmkOrder paGmktOrder)	throws Exception {
		return PaGmktDeliveryProcess.checkUnpaidPaGmktOrderList(paGmktOrder);
	}

	@Override
	public int sendShipping4ChangeInvoiceTx(PaGmktAbstractRest rest, List<Map<String, Object>> slipChangeProcList, ParamMap paramMap) throws Exception {
		return PaGmktDeliveryProcess.sendShipping4ChangeInvoice(rest, slipChangeProcList, paramMap);
	}

	@Override
	public List<Map<String, Object>> selectSlipChangeProcList(ParamMap paramMap) throws Exception {
		return PaGmktDeliveryProcess.selectSlipChangeProcList(paramMap);
	}
}