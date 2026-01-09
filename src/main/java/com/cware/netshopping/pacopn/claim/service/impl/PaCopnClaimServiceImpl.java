package com.cware.netshopping.pacopn.claim.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnclaimitemlist;
import com.cware.netshopping.domain.model.Pacopnclaimlist;
import com.cware.netshopping.pacopn.claim.process.PaCopnClaimProcess;
import com.cware.netshopping.pacopn.claim.service.PaCopnClaimService;

@Service("pacopn.claim.paCopnClaimService")
public class PaCopnClaimServiceImpl extends AbstractService implements PaCopnClaimService{
	
	@Resource(name = "pacopn.claim.paCopnClaimProcess")
	private PaCopnClaimProcess paCopnClaimProcess;
	
	@Override
	public String savePaCopnClaimTx(Pacopnclaimlist paCopnClaim, List<Pacopnclaimitemlist> paCopnClaimitemList, ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.savePaCopnClaim(paCopnClaim, paCopnClaimitemList, paramMap);
	}

	@Override
	public List<Object> selectCancelList() throws Exception{
		return paCopnClaimProcess.selectCancelList();
	}

	@Override
	public List<Object> selectCopnSoldOutordList() throws Exception{
		return paCopnClaimProcess.selectCopnSoldOutordList();
	}
	
	@Override
	public ParamMap makeCancelListProc(HashMap<String, String> apiInfo, HashMap<String, Object> cancelMap) throws Exception{
		return paCopnClaimProcess.makeCancelListProc(apiInfo, cancelMap);
	}
	
	@Override
	public List<Object> selectCancelInputTargetList() throws Exception{
		return paCopnClaimProcess.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return paCopnClaimProcess.updatePreCancelYn(preCancelMap);
	}
	
	@Override
	public List<Object> selectClaimList() throws Exception{
		return paCopnClaimProcess.selectClaimList();
	}
	
	@Override
	public ParamMap makeClaimListProc(HashMap<String, String> apiInfo, HashMap<String, Object> claimMap) throws Exception{
		return paCopnClaimProcess.makeClaimListProc(apiInfo, claimMap);
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return paCopnClaimProcess.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.selectOrderClaimTargetDtList(paramMap);
	}
	
	@Override
	public String selectClaimDelyGb(String companyName) throws Exception{
		return paCopnClaimProcess.selectClaimDelyGb(companyName);
	}
	
	@Override
	public List<Object> selectPickupList() throws Exception{
		return paCopnClaimProcess.selectPickupList();
	}
	
	@Override
	public HashMap<String, String> selectPickupDetailList(String paClaimNo) throws Exception{
		return paCopnClaimProcess.selectPickupDetailList(paClaimNo);
	}
	
	@Override
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception{
		return paCopnClaimProcess.updatePickupResult(pickup);
	}
	
	@Override
	public List<Object> selectReceiveConfirmList() throws Exception{
		return paCopnClaimProcess.selectReceiveConfirmList();
	}
	
	@Override
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception{
		return paCopnClaimProcess.updateReceiveConfirmResult(receive);
	}
	
	@Override
	public List<Object> selectReturnApprovalList() throws Exception{
		return paCopnClaimProcess.selectReturnApprovalList();
	}
	
	@Override
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception{
		return paCopnClaimProcess.updateReturnApprovalResult(returnApproval);
	}
	
	@Override
	public List<Object> selectReturnWithdrawList(String paCode) throws Exception{
		return paCopnClaimProcess.selectReturnWithdrawList(paCode);
	}
	
	@Override
	public String saveWithdrawListTx(HashMap<String, Object> resultWithdraw) throws Exception{
		return paCopnClaimProcess.saveWithdrawList(resultWithdraw);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return paCopnClaimProcess.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	public int updatePaDoFlag(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.updatePaDoFlag(paramMap);
	}
	
	@Override
	public String saveOrderRejectProcTx(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.saveOrderRejectProc(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.selectOrgShipmentBoxId(paramMap);
	}
	
	@Override
	public ParamMap makeClaimPaorderm() throws Exception {
		return paCopnClaimProcess.makeClaimPaorderm();
	}
	
	@Override
	public ParamMap makeCancelProc(HashMap<String, String> apiInfo, ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.makeCancelProc(apiInfo, paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.compareAddress(paramMap);
	}

	@Override
	public String compareExAddress(ParamMap paramMap) throws Exception{
		return paCopnClaimProcess.compareExAddress(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectMailAlertEntpList(ParamMap paramMap) throws Exception {
		return paCopnClaimProcess.selectMailAlertEntpList(paramMap);
	}

	@Override
	public String saveMailAlertTx(ParamMap mailMap) throws Exception {
		return paCopnClaimProcess.saveMailAlert(mailMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return paCopnClaimProcess.selectPaMobileOrderAutoCancelList(paramMap);
	}

}
