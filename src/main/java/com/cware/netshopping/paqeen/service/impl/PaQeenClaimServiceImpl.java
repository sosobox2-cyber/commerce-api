package com.cware.netshopping.paqeen.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.ExchangeClaimList;
import com.cware.netshopping.paqeen.domain.Return;
import com.cware.netshopping.paqeen.process.PaQeenClaimProcess;
import com.cware.netshopping.paqeen.service.PaQeenClaimService;

@Service("paqeen.claim.paQeenClaimService")
public class PaQeenClaimServiceImpl extends AbstractService implements PaQeenClaimService{
	
	@Autowired
	@Qualifier("paqeen.claim.paQeenClaimProcess")
	PaQeenClaimProcess paQeenClaimProcess;
	
	@Override
	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate,HttpServletRequest request) throws Exception {
		return paQeenClaimProcess.getReturnList(processFlag, fromDate, toDate, request);
	}

	@Override
	public ResponseMsg getExchangeList(String fromDate, String toDate, HttpServletRequest request, String claimStatus)
			throws Exception {
		return paQeenClaimProcess.getExchangeList(fromDate, toDate, request, claimStatus);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.compareAddress(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception {
		return paQeenClaimProcess.updatePaOrdermChangeFlag(string, mappingSeq);
	}

	@Override
	public ResponseMsg getExchangeRecall(HttpServletRequest request, String claimGb) throws Exception {
		return paQeenClaimProcess.getExchangeRecall(request, claimGb);
	}

	@Override
	public ResponseMsg getExchangeDelivery(HttpServletRequest request) throws Exception {
		return paQeenClaimProcess.getExchangeDelivery(request);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	
	@Override
	public int savePaQeenReturnTx(Return returnItem, String paCode) throws Exception {
		return paQeenClaimProcess.savePaQeenReturn(returnItem, paCode);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public ResponseMsg getReturnRecall(HttpServletRequest request, String string) throws Exception {
		return paQeenClaimProcess.getReturnRecall(request, string);
		
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paQeenClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public ResponseMsg getReturnRecall20(HttpServletRequest request) throws Exception {
		return paQeenClaimProcess.getReturnRecall20(request);
		
	}

	@Override
	public int savePaQeenExchangeTx(ExchangeClaimList claim, ParamMap paramMap2) throws Exception {
		return paQeenClaimProcess.savePaQeenExchange(claim, paramMap2);
	}
}
