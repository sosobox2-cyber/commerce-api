package com.cware.netshopping.patdeal.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.process.PaTdealClaimProcess;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;

@Service("patdeal.claim.paTdealClaimService")
public class PaTdealClaimServiceImpl extends AbstractService implements PaTdealClaimService {
	
	@Autowired
	@Qualifier("patdeal.claim.paTdealClaimProcess")
	PaTdealClaimProcess paTdealClaimProcess;

	@Override
	public List<PaTdealClaimListVO> getTdealClaimList(String claimStatus, String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paTdealClaimProcess.getTdealClaimList(claimStatus, fromDate, toDate, request);
	}
	
	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public int saveTdealClaimListTx(String claimStatus, PaTdealClaimListVO paTdealClaimVo) throws Exception {
		return paTdealClaimProcess.saveTdealClaimList(claimStatus, paTdealClaimVo);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public ResponseMsg procTdealClaimCancelList(String claimStatus, HttpServletRequest request) throws Exception{
		return paTdealClaimProcess.procTdealClaimCancelList(claimStatus, request);
		
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectTdealReturnCompleList() throws Exception {
		return paTdealClaimProcess.selectTdealReturnCompleList();
	}

	@Override
	public ResponseMsg returnCompleProc(HashMap<String, Object> tdealReturnCompleItem,HttpServletRequest request) throws Exception {
		return paTdealClaimProcess.returnCompleProc(tdealReturnCompleItem, request);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception {
		return paTdealClaimProcess.updatePaOrdermChangeFlag(string, mappingSeq);
	}

	@Override
	public List<HashMap<String, Object>> selectTdealExchangeReturnDoFlag60List() throws Exception {
		return paTdealClaimProcess.selectTdealExchangeReturnDoFlag60List();
	}

	@Override
	public ResponseMsg exchangeDeliveryProc(HashMap<String, Object> tdealExchangeReturnDoFlag60Item, HttpServletRequest request) throws Exception {
		return paTdealClaimProcess.exchangeDeliveryProc(tdealExchangeReturnDoFlag60Item, request);
	}

	@Override
	public ResponseMsg returnApprovalProc(HashMap<String, Object> tdealReturnCompleItem, HttpServletRequest request)
			throws Exception {
		return paTdealClaimProcess.returnApprovalProc(tdealReturnCompleItem, request);
		}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paTdealClaimProcess.compareAddress(paramMap);
	}

	@Override
	public OrderDetail orderInfoDetail(String paCode, String orderNo, HttpServletRequest request) throws Exception {
		return paTdealClaimProcess.orderInfoDetail(paCode,orderNo,request);
	}

}
