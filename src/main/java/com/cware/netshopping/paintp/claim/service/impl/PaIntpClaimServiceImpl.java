package com.cware.netshopping.paintp.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;
import com.cware.netshopping.paintp.claim.process.PaIntpClaimProcess;
import com.cware.netshopping.paintp.claim.service.PaIntpClaimService;

@Service("paintp.claim.paIntpClaimService")
public class PaIntpClaimServiceImpl  extends AbstractService implements PaIntpClaimService {

	@Autowired
    private PaIntpClaimProcess paIntpClaimProcess;
	
	@Override
	public void createClaimListTx(PaIntpClaimVO claimVo) throws Exception {		
		paIntpClaimProcess.saveClaimList(claimVo);
	}
	@Override
	public void createClaimDoneListTx(PaIntpClaimVO claimVo) throws Exception {		
		paIntpClaimProcess.saveClaimDoneList(claimVo);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.compareAddress(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectOrderChangeTargetDtList(paramMap);
		
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpClaimProcess.selectChangeCancelTargetDtList(paramMap);
		
	}
	@Override
	public List<Object> selectReturnOkList() throws Exception {
		return paIntpClaimProcess.selectReturnOkList();
	}
	@Override
	public void updatePaOrdermResult(Paorderm paorderm) throws Exception {
		paIntpClaimProcess.updatePaOrdermResult(paorderm);		
	}
	@Override
	public List<Object> selectExchangeSlipList() throws Exception {
		return paIntpClaimProcess.selectExchangeSlipList();
	}
	@Override
	public void createCancelListTx(PaIntpClaimVO claimVO) throws Exception {
		paIntpClaimProcess.createCancelList(claimVO);
	}
}