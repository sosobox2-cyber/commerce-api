package com.cware.netshopping.panaver.claim.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.claim.process.PaNaverClaimProcess;
import com.cware.netshopping.panaver.claim.service.PaNaverClaimService;

@Service("panaver.claim.paNaverClaimService")
public class PaNaverClaimServiceImpl extends AbstractService implements PaNaverClaimService{

	@Autowired
	PaNaverClaimProcess paNaverClaimProcess;

	@Override
	public List<Object> selectPaNaverClaimApprovalList() throws Exception {
		return paNaverClaimProcess.selectPaNaverClaimApprovalList();
	}

	@Override
	public ParamMap saveClaimApprovalProcTx(HashMap<String, String> claimMap) throws Exception {
		return paNaverClaimProcess.saveClaimApprovalProc(claimMap);
	}

	@Override
	public List<Object> selectReturnHoldList() throws Exception {
		return paNaverClaimProcess.selectReturnHoldList();
	}

	@Override
	public String saveReturnHoldProcTx(HashMap<String, Object> claimMap) throws Exception {
		return paNaverClaimProcess.saveReturnHoldProc(claimMap);
	}

	@Override
	public List<Object> selectReleaseReturnHoldList() throws Exception {
		return paNaverClaimProcess.selectReleaseReturnHoldList();
	}

	@Override
	public int updatePaOrdermHoldYnTx(Paorderm paorderm) throws Exception {
		return paNaverClaimProcess.updatePaOrdermHoldYn(paorderm);
	}

	@Override
	public HashMap<String ,String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return paNaverClaimProcess.selectExchangeRejectInfo(mappingSeq);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return paNaverClaimProcess.selectReturnClaimTargetList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paNaverClaimProcess.selectReturnClaimTargetDt30List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return paNaverClaimProcess.selectReturnClaimTargetDt20List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverClaimProcess.selectReturnCancelTargetDtList(paramMap);
	}
}
