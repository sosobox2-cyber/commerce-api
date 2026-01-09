package com.cware.netshopping.pagmkt.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.pagmkt.claim.process.PaGmktClaimProcess;
import com.cware.netshopping.pagmkt.claim.service.PaGmktClaimService;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;


@Service("pagmkt.claim.PaGmktClaimService")
public class PaGmktClaimServiceImpl  extends AbstractService implements PaGmktClaimService {

	@Resource(name = "pagmkt.claim.PaGmktClaimProcess")
    private PaGmktClaimProcess PaGmktClaimProcess;
	
	@Override
	public int saveClaimListTx(PaGmkClaim paGmktClaim) throws Exception {
		return PaGmktClaimProcess.saveClaimList(paGmktClaim);
	}

	@Override
	public PaGmkClaim setPaGmkClaimVo(Map<String, Object> returnMap , ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.setPaGmkClaimVo(returnMap, paramMap);
	}
	
	@Override
	public int updateReturnPickupTx(Map<String, String> rtnMap) throws Exception {
		return PaGmktClaimProcess.updateReturnPickup(rtnMap);
	}
	
	@Override
	public List<Object> selectReturnConfirmList(ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.selectReturnConfirmList(paramMap);
	}
	
	@Override
	public int saveReturnConfirmProcTx(PaGmktAbstractRest rest,ParamMap paramMap, HashMap<String, Object> returnMap) throws Exception {
		return PaGmktClaimProcess.saveReturnConfirmProc(rest, paramMap, returnMap);		
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList(ParamMap paramMap) throws Exception{
		return PaGmktClaimProcess.selectOrderClaimTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList(ParamMap paramMap) throws Exception{
		return PaGmktClaimProcess.selectClaimCancelTargetList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectReturnPickup50List(ParamMap paramMap) throws Exception {
	    return PaGmktClaimProcess.selectReturnPickup50List(paramMap);
	}

	@Override
	public List<Map<String, String>> selectReturnPickup60List(ParamMap paramMap) throws Exception {
	    return PaGmktClaimProcess.selectReturnPickup60List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}

	@Override
	public String selectGoodsdtChangeFlag(PaGmkClaim pagmkclaim) throws Exception {
		return PaGmktClaimProcess.selectGoodsdtChangeFlag(pagmkclaim);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return PaGmktClaimProcess.compareAddress(paramMap);
	}


}