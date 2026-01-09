package com.cware.netshopping.pafaple.service.impl;

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
import com.cware.netshopping.pafaple.process.PaFapleClaimProcess;
import com.cware.netshopping.pafaple.service.PaFapleClaimService;

@Service("pafaple.claim.paFapleClaimService")
public class PaFapleClaimServiceImpl  extends AbstractService implements PaFapleClaimService {

	@Autowired
	@Qualifier("pafaple.claim.paFapleClaimProcess")
	PaFapleClaimProcess paFapleClaimProcess;
	
	@Override
	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate, HttpServletRequest request) throws Exception {
		return paFapleClaimProcess.getReturnList(processFlag, fromDate, toDate, request);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectOrderClaimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.compareAddress(paramMap);
	}

	@Override
	public ResponseMsg returnCompleProc(HttpServletRequest request) throws Exception {
		return paFapleClaimProcess.returnCompleProc(request);
	}

	@Override
	public ResponseMsg getReturnCancelList(String fromDate, String toDate, HttpServletRequest request)
			throws Exception {
		return paFapleClaimProcess.getReturnCancelList(request, fromDate, toDate);
	}

	@Override
	public ResponseMsg getExchangeList(String fromDate, String toDate, String claimGb, HttpServletRequest request) throws Exception {
		return paFapleClaimProcess.getExchangeList(request, fromDate, toDate, claimGb);
	}

	@Override
	public ResponseMsg getExchangeCancelList(String fromDate, String toDate, HttpServletRequest request)
			throws Exception {
		return paFapleClaimProcess.getExchangeCancelList(request, fromDate, toDate);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paFapleClaimProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public ResponseMsg exchangeSlipOutProc(HttpServletRequest request) throws Exception {
		return paFapleClaimProcess.exchangeSlipOutProc(request);
	}

	@Override
	public ResponseMsg exchangeSlipOutProcStart(HttpServletRequest request) throws Exception {
		return paFapleClaimProcess.exchangeSlipOutProcStart(request);
	}

	@Override
	public String compareAddressExchange(ParamMap paramMap) throws Exception {
		return paFapleClaimProcess.compareAddressExchange(paramMap);
	}

	@Override
	public int savePaFapleReturnTx(Map<String, Object> returnMap, ParamMap param) throws Exception {
		return paFapleClaimProcess.savePaFapleReturn(returnMap, param);
		
	}

	@Override
	public int savePaFapleReturnCancelTx(Map<String, Object> returnWithdraw, ParamMap param) throws Exception {
		return paFapleClaimProcess.savePaFapleReturnCancel(returnWithdraw, param);
	}

	@Override
	public int savePaFapleExchangeTx(Map<String, Object> exchangeMap, ParamMap apiInfoMap) throws Exception {
		return paFapleClaimProcess.savePaFapleExchange(exchangeMap, apiInfoMap);

	}

	

}
