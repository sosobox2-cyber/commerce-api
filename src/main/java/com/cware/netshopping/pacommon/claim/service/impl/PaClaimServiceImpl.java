package com.cware.netshopping.pacommon.claim.service.impl;

import java.util.HashMap;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.pacommon.claim.process.PaClaimProcess;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;


@Service("pacommon.claim.paclaimService")

public class PaClaimServiceImpl  extends AbstractService implements PaClaimService {

	@Resource(name = "pacommon.claim.paclaimProcess")
	private PaClaimProcess claimProcess;


	@Override
	public HashMap<String, Object> saveOrderClaimTx(OrderClaimVO orderClaimVO) throws Exception {
		return claimProcess.saveOrderClaim(orderClaimVO);
	}
	
	@Override
	public HashMap<String, Object> saveClaimCancelTx(OrderClaimVO orderClaimVO) throws Exception {
		return claimProcess.saveClaimCancel(orderClaimVO);
	}

	@Override
	public HashMap<String, Object>[] saveOrderChangeTx(OrderClaimVO[] orderClaimVO) throws Exception {
		return claimProcess.saveOrderChange(orderClaimVO);
	}
	
	@Override
	public HashMap<String, Object>[] saveChangeCancelTx(OrderClaimVO[] orderClaimVO) throws Exception {
		return claimProcess.saveChangeCancel(orderClaimVO);
	}



	
}