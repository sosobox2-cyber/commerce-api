package com.cware.netshopping.pawemp.claim.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.pawemp.claim.model.Claim;
import com.cware.netshopping.pawemp.claim.process.PaWempClaimProcess;
import com.cware.netshopping.pawemp.claim.service.PaWempClaimService;

@Service("pawemp.claim.paWempClaimService")
public class PaWempClaimServiceImpl extends AbstractService implements PaWempClaimService{
	
	@Resource(name = "pawemp.claim.paWempClaimProcess")
	private PaWempClaimProcess paWempClaimProcess;

	@Override
	public List<PaWempClaimItemList> selectPaWempClaimItemList(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String paOrderSeq) throws Exception {
		return paWempClaimProcess.selectPaWempClaimItemList(paClaimNo, paOrderNo, paShipNo, paOrderGb, paOrderSeq);
	}

	@Override
	public List<Object> selectOrderReturnTargetDt30List(ParamMap paramMap) throws Exception {
		return paWempClaimProcess.selectOrderReturnTargetDt30List(paramMap); 
	}
	
	@Override
	public List<Object> selectOrderReturnTargetDt20List(ParamMap paramMap) throws Exception {
		return paWempClaimProcess.selectOrderReturnTargetDt20List(paramMap); 
	}


	@Override
	public List<Object> selectOrderReturnTargetList() throws Exception{
		return paWempClaimProcess.selectOrderReturnTargetList();
	}

	@Override
	public String savePaWempClaimTx(PaWempClaimList paWempClaimList)throws Exception{
		return paWempClaimProcess.savePaWempClaim(paWempClaimList);
	}

	@Override
	public List<Object> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paWempClaimProcess.selectReturnCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectReturnCancelTargetList() throws Exception{
		return paWempClaimProcess.selectReturnCancelTargetList();
	}

	@Override
	public String savePaWempReturnCancelTx(PaWempClaimList arrPaWempClaim) throws Exception {
		return paWempClaimProcess.savePaWempReturnCancel(arrPaWempClaim);
	}

	@Override
	public List<Object> selectPickupList() throws Exception {
		return paWempClaimProcess.selectPickupList();
	}

	@Override
	public int updatePickupResultTx(HashMap<String, Object> pickup) throws Exception {
		return paWempClaimProcess.updatePickupResult(pickup);
	}

	@Override
	public List<Object> selectReceiveConfirmList() throws Exception {
		return paWempClaimProcess.selectReceiveConfirmList();
	}

	@Override
	public int updateReceiveConfirmResultTx(HashMap<String, Object> receive) throws Exception {
		return paWempClaimProcess.updateReceiveConfirmResult(receive);
	}

	@Override
	public List<Object> selectReturnApprovalList() throws Exception{
		return paWempClaimProcess.selectReturnApprovalList();
	}
	
	@Override
	public int updateReturnApprovalResultTx(HashMap<String, Object> returnApproval) throws Exception{
		return paWempClaimProcess.updateReturnApprovalResult(returnApproval);
	}

	@Override
	public PaWempClaimList makePaWempClaimList(Claim claim, String paOrderGb, String paCode) throws Exception {
		return paWempClaimProcess.makePaWempClaimList(claim, paOrderGb, paCode);
	}
	
	@Override
	public String savePaWempCancelTx(PaWempClaimList paWempClaimList) throws Exception {
		return paWempClaimProcess.savePaWempCancel(paWempClaimList);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception{
		return paWempClaimProcess.compareAddress(paramMap);
	}
}
