package com.cware.netshopping.pacopn.exchange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnexchangelist;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacopn.exchange.process.PaCopnExchangeProcess;
import com.cware.netshopping.pacopn.exchange.service.PaCopnExchangeService;

@Service("pacopn.exchange.paCopnExchangeService")
public class PaCopnExchangeServiceImpl extends AbstractService implements PaCopnExchangeService {

	@Resource(name = "pacopn.exchange.paCopnExchangeProcess")
	private PaCopnExchangeProcess paCopnExchangeProcess;

	@Override
	public String saveExchangeListTx(List<Pacopnexchangelist> exchangeListArr) throws Exception {
		return paCopnExchangeProcess.saveExchangeList(exchangeListArr);
	}

	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return paCopnExchangeProcess.selectOrderChangeTargetList();
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paCopnExchangeProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paCopnExchangeProcess.selectChangeCancelTargetList();
	}

	@Override
	public List<Object> selectOrderChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paCopnExchangeProcess.selectOrderChangeCancelTargetDtList(paramMap);
	}

	@Override
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception {
		return paCopnExchangeProcess.updatePreCancelYn(preCancelMap);
	}

	@Override
	public String saveExchangeRejectProcTx(ParamMap paramMap) throws Exception {
		return paCopnExchangeProcess.saveExchangeRejectProc(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectExchangeReturn(String paCode) throws Exception {
		return paCopnExchangeProcess.selectExchangeReturn(paCode);
	}

	@Override
	public int updateExchangeReturnConfirmTx(ParamMap paramMap) throws Exception {
		return paCopnExchangeProcess.updateExchangeReturnConfirm(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectExchangeSlipOutTargetList() throws Exception {
		return paCopnExchangeProcess.selectExchangeSlipOutTargetList();
	}

	@Override
	public int updateExchangeCompleteResultTx(ParamMap paramMap) throws Exception {
		return paCopnExchangeProcess.updateExchangeCompleteResult(paramMap);
	}
	
	@Override
	public String updateExchangeDeliveryCompleteTx(Paorderm paOrderm) throws Exception{
		return paCopnExchangeProcess.updateExchangeDeliveryComplete(paOrderm);
	}
	
	@Override
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return paCopnExchangeProcess.selectExchangeCompleteList();
	}
	
	@Override
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception{
		return paCopnExchangeProcess.selectOrgShipmentBoxId(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectExchangeDetail(String paClaimNo) throws Exception {
		return paCopnExchangeProcess.selectExchangeDetail(paClaimNo);
	}

	@Override
	public List<String> selectExchangeCreatedDate() throws Exception {
		return paCopnExchangeProcess.selectExchangeCreatedDate();
	}
}
