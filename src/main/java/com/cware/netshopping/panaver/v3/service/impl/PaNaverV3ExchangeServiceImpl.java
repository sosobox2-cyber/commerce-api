package com.cware.netshopping.panaver.v3.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.process.PaNaverV3ExchangeProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ExchangeService;

@Service("panaver.v3.exchange.paNaverV3ExchangeService")
public class PaNaverV3ExchangeServiceImpl extends AbstractService implements PaNaverV3ExchangeService {

	@Autowired
	@Qualifier("panaver.v3.exchange.paNaverV3ExchangeProcess")
	PaNaverV3ExchangeProcess paNaverV3ExchangeProcess;

	@Override
	public ResponseMsg approvalCollect(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ExchangeProcess.approvalCollect(productOrderId, procId, request);
	}
	
	@Override
	public int updatePaOrdermResultTx(Paorderm paorderm) throws Exception {
		return paNaverV3ExchangeProcess.updatePaOrdermResult(paorderm);
	}

	@Override
	public ResponseMsg dispatch(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ExchangeProcess.dispatch(productOrderId, procId, request);
	}

	@Override
	public int updateExchangePaOrdermResultTx(Paorderm paorderm) throws Exception {
		return paNaverV3ExchangeProcess.updateExchangePaOrdermResult(paorderm);
	}

	@Override
	public ResponseMsg reject(String productOrderId, String rejectExchangeReason, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ExchangeProcess.reject(productOrderId, rejectExchangeReason, procId, request);
	}

	@Override
	public int updatePaOrdermPreCancelTx(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeProcess.updatePaOrdermPreCancel(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeProcess.selectChangeTargetDtList(paramMap);
	}

	@Override
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return paNaverV3ExchangeProcess.selectExchangeRejectInfo(mappingSeq);
	}

	@Override
	public int checkOrderChangeTargetList(String orderId, String claimId) throws Exception {
		return paNaverV3ExchangeProcess.checkOrderChangeTargetList(orderId, claimId);
	}

	@Override
	public int updatePaOrdermPreChangeCancelTx(Paorderm paorderm) throws Exception {
		return paNaverV3ExchangeProcess.updatePaOrdermPreChangeCancel(paorderm);
	}

	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ExchangeProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public ResponseMsg holdback(String productOrderId, String holdbackClassType, String holdbackExchangeDetailReason, Double extraExchangeFeeAmount, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ExchangeProcess.holdback(productOrderId, holdbackClassType, holdbackExchangeDetailReason, extraExchangeFeeAmount, procId, request);
	}

	@Override
	public ResponseMsg releaseHoldback(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ExchangeProcess.releaseHoldback(productOrderId, procId, request);
	}

	@Override
	public int checkOrderChangeInputTargetList(String orderId, String claimId) throws Exception {
		return paNaverV3ExchangeProcess.checkOrderChangeInputTargetList(orderId, claimId);
	}
}
