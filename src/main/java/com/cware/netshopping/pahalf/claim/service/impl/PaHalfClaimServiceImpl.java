package com.cware.netshopping.pahalf.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pahalf.claim.process.PaHalfClaimProcess;
import com.cware.netshopping.pahalf.claim.service.PaHalfClaimService;

@Service("pahalf.claim.paHalfClaimService")
public class PaHalfClaimServiceImpl extends AbstractService implements PaHalfClaimService{
	
	@Resource(name = "pahalf.claim.paHalfClaimProcess")
	private PaHalfClaimProcess paHalfClaimProcess;

	@Override
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception {
		return paHalfClaimProcess.selectExchangeCompleteList();
	}

	@Override
	public void updatePaOrderm(Map<String, Object> orderMap) throws Exception {
		paHalfClaimProcess.updatePaOrderm(orderMap);
	}

	@Override
	public List<Map<String, Object>> selectReturnApprovalList() throws Exception {
		return paHalfClaimProcess.selectReturnApprovalList();
	}	 
	@Override
	public String savePaHalfClaimTx(Map<String, Object> claim) throws Exception {
		return paHalfClaimProcess.savePaHalfClaim(claim);
	}

	@Override
	public HashMap<String, Object> selectOrderClaimTargetDt30(ParamMap paramMap) throws Exception {
		return paHalfClaimProcess.selectOrderClaimTargetDt30(paramMap);
	}

	@Override
	public HashMap<String, Object> selectOrderClaimTargetDt20(ParamMap paramMap) throws Exception {
		return paHalfClaimProcess.selectOrderClaimTargetDt20(paramMap);
	}

	@Override
	public String compareAddress(HashMap<String, Object> orderClaimVO) throws Exception {
		return paHalfClaimProcess.compareAddress(orderClaimVO);
	}

	@Override
	public HashMap<String, Object> selectClaimCancelTargetDt(ParamMap paramMap) throws Exception {
		return paHalfClaimProcess.selectClaimCancelTargetDt(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paHalfClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return  paHalfClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public String checkShpFeeYn(HashMap<String, Object> orderClaimTargetDt) throws Exception {
		return paHalfClaimProcess.checkShpFeeYn(orderClaimTargetDt);
	}


}
