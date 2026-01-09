package com.cware.netshopping.pakakao.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoOrderListVO;
import com.cware.netshopping.pakakao.claim.process.PaKakaoClaimProcess;
import com.cware.netshopping.pakakao.claim.service.PaKakaoClaimService;

@Service("pakakao.claim.paKakaoClaimService")
public class PaKakaoClaimServiceImpl extends AbstractService implements PaKakaoClaimService{

	@Autowired
	private PaKakaoClaimProcess paKakaoClaimProcess;
	
	@Override
	public void saveKakaoCancelListTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoClaimProcess.saveKakaoCancelList(vo);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoOrderCancelList() throws Exception {
		return paKakaoClaimProcess.selectPaKakaoOrderCancelList();
	}
	
	@Override
	public void updatePaKakaoCancelConfirmTx(ParamMap paramMap) throws Exception {
		paKakaoClaimProcess.updatePaKakaoCancelConfirm(paramMap);
	}

	@Override
	public void updatePaKakaoCancelListTx(ParamMap paramMap) throws Exception {
		paKakaoClaimProcess.updatePaKakaoCancelList(paramMap);
	}
	
	@Override
	public void saveKakaoWithdrawCancelListTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoClaimProcess.saveKakaoWithdrawCancelList(vo);
	}
	
	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectClaimTargetList(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public void saveKakaoClaimListTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoClaimProcess.saveKakaoClaimList(vo);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.compareAddress(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paKakaoClaimProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}
	
	@Override
	public HashMap<String, Object> selectRefusalInfo(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectRefusalInfo(paramMap);
	}
	
	@Override
	public int updatePreCanYn(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.updatePreCanYn(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimInvTgList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectPaKakaoClaimInvTgList(paramMap);
	}
	
	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paKakaoClaimProcess.updatePaOrderMDoFlag(map);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimCollCmpTgList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectPaKakaoClaimCollCmpTgList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoReturnCmpTgList() throws Exception {
		return paKakaoClaimProcess.selectPaKakaoReturnCmpTgList();
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoExchangeSlipOutList() throws Exception {
		return paKakaoClaimProcess.selectPaKakaoExchangeSlipOutList();
	}
	
	@Override
	public int countCancelList(String orderId, String claimStatus) throws Exception {
		return paKakaoClaimProcess.countCancelList(orderId, claimStatus);
	}

	@Override
	public void updateKakaoExchangeCompleteTx(PaKakaoOrderListVO vo) throws Exception {
		paKakaoClaimProcess.updateKakaoExchangeComplete(vo);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimHoldTargetList(ParamMap paramMap) throws Exception {
		return paKakaoClaimProcess.selectPaKakaoClaimHoldTargetList(paramMap);
	}
	
	@Override
	public int updatePaOrderMHoldYn(Map<String, Object> map) throws Exception {
		return paKakaoClaimProcess.updatePaOrderMHoldYn(map);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paKakaoClaimProcess.selectPaMobileOrderAutoCancelList();
	}
}
