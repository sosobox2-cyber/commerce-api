package com.cware.netshopping.passg.claim.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgCancelListVO;
import com.cware.netshopping.domain.PaSsgClaimListVO;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.passg.claim.process.PaSsgClaimProcess;
import com.cware.netshopping.passg.claim.service.PaSsgClaimService;
import com.cware.netshopping.passg.delivery.process.PaSsgDeliveryProcess;

@Service("passg.claim.paSsgClaimService")
public class PaSsgClaimServiceImpl extends AbstractService implements PaSsgClaimService {

	@Resource(name = "passg.claim.paSsgClaimProcess")
    private PaSsgClaimProcess paSsgClaimProcess;
	@Resource(name = "passg.delivery.paSsgDeliveryProcessImpl")
	private PaSsgDeliveryProcess paSsgDeliveryProcess;
	
	@Override
	public int saveSsgCancelListTx(PaSsgCancelListVO vo, String claimStatus) throws Exception {
		return paSsgClaimProcess.saveSsgCancelList(vo, claimStatus);
	}
	
	@Override
	public int saveSsgCancelCompleteListTx(PaSsgCancelListVO vo, String claimStatus) throws Exception {
		return paSsgClaimProcess.saveSsgCancelCompleteList(vo, claimStatus);
	}
	
	@Override
	public String saveSsgClaimListTx(PaSsgClaimListVO vo) throws Exception {
		return paSsgClaimProcess.saveSsgClaimList(vo);
	}
	
	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectClaimTargetList(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.compareAddress(paramMap);
	}
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paSsgClaimProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public void preOrderChangeCancelTx(HashMap<String, Object> hmSheet) throws Exception {
		int executeRtn = 0;
		
		Map<String, Object> preCancelMap = new HashMap<String, Object>();
		preCancelMap.put("preCanYn"			, "1");
		preCancelMap.put("MAPPING_SEQ"		, String.valueOf(hmSheet.get("MAPPING_SEQ")));
		preCancelMap.put("apiResultMessage"	, getMessage("pa.before_change_create_cancel"));
		executeRtn = paSsgDeliveryProcess.updatePreCanYn(preCancelMap);
		if(executeRtn < 1) throw processException("msg.cannot_save", new String[] {"TPAORDERM BY preOrderChangeCancelTx "});
	}
	
	@Override
	public List<HashMap<String, Object>> selectPaSsgRecoveryList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectPaSsgRecoveryList(paramMap);
	}
	
	@Override
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		int result = 0;
		
		result = paSsgClaimProcess.updatePaOrderMDoFlag(paorderm);
		
		if(result < 1) throw new Exception();
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> selectPaSsgOrderCancelList() throws Exception {
		return paSsgClaimProcess.selectPaSsgOrderCancelList();
	}

	@Override
	public int updatePaSsgCancelConfirmTx(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.updatePaSsgCancelConfirm(paramMap);
	}

	@Override
	public int updatePaSsgCancelList(ParamMap paramMap) throws Exception {
		return  paSsgClaimProcess.updatePaSsgCancelList(paramMap);
	}

	@Override
	public int updatePaSsgCancelList4Withdraw(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.updatePaSsgCancelList4Withdraw(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return paSsgClaimProcess.selectPaMobileOrderAutoCancelList(paramMap);
	}

	@Override
	public PaSsgOrderListVO selectPaSsgOrderList(PaSsgCancelListVO paSsgCancelList) throws Exception {
		return paSsgClaimProcess.selectPaSsgOrderList(paSsgCancelList);
	}
}
