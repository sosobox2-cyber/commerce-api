package com.cware.netshopping.panaver.v3.service.impl;



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
import com.cware.netshopping.panaver.v3.process.PaNaverV3ClaimProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3ClaimService;

@Service("panaver.v3.claim.paNaverV3ClaimService")
public class PaNaverV3ClaimServiceImpl extends AbstractService implements PaNaverV3ClaimService{
	
	@Autowired
	@Qualifier("panaver.v3.claim.paNaverV3ClaimProcess")
	PaNaverV3ClaimProcess paNaverV3ClaimProcess;

	@Override
	public ResponseMsg returnConfirmProc(String procId, HttpServletRequest request, String productOrderId) throws Exception{
		return paNaverV3ClaimProcess.returnConfirmProc(procId, request, productOrderId);
	}
	
	@Override
	public ParamMap saveClaimApprovalProcTx(Map<String, Object> claimMap) throws Exception {
		return paNaverV3ClaimProcess.saveClaimApprovalProc(claimMap);
	}

	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimProcess.selectReturnClaimTargetList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimProcess.selectReturnClaimTargetDt30List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimProcess.selectReturnClaimTargetDt20List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3ClaimProcess.selectReturnCancelTargetDtList(paramMap);
	}
	
	@Override
	public ResponseMsg returnHoldbackProc(String productOrderId, String holdbackClassType, String holdbackReturnDetailReason, Double extraReturnFeeAmount, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ClaimProcess.returnHoldbackProc(productOrderId, holdbackClassType, holdbackReturnDetailReason, extraReturnFeeAmount, procId, request);
	}
	
	@Override
	public ResponseMsg returnHoldbackReleaseProc(String productOrderId,  String procId, HttpServletRequest request) throws Exception {
		return paNaverV3ClaimProcess.returnHoldbackReleaseProc(productOrderId, procId, request);
	}
}
