package com.cware.netshopping.patmon.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTmonCancelListVO;
import com.cware.netshopping.domain.PaTmonClaimListVO;
import com.cware.netshopping.domain.PaTmonRedeliveryListVO;
import com.cware.netshopping.patmon.claim.process.PaTmonClaimProcess;
import com.cware.netshopping.patmon.claim.service.PaTmonClaimService;
import com.cware.netshopping.patmon.delivery.process.PaTmonDeliveryProcess;

@Service("patmon.claim.paTmonClaimService")
public class PaTmonClaimServiceImpl extends AbstractService implements PaTmonClaimService {

	@Resource(name = "patmon.claim.paTmonClaimProcess")
    private PaTmonClaimProcess paTmonClaimProcess;
	@Autowired
	private PaTmonDeliveryProcess paTmonDeliveryProcess;

	@Override
	public int saveTmonCancelListTx(List<PaTmonCancelListVO> paTmonCancelList) throws Exception {
		return paTmonClaimProcess.saveTmonCancelList(paTmonCancelList);
	}

	@Override
	public int saveTmonWithdrawCancelList(List<PaTmonCancelListVO> paTmonCancelList) throws Exception {
		return paTmonClaimProcess.saveTmonWithdrawCancelList(paTmonCancelList);
	}

	@Override
	public List<Map<String, Object>> selectPaTmonOrderCancelList() throws Exception {
		return paTmonClaimProcess.selectPaTmonOrderCancelList();
	}

	@Override
	public int updateProcFlag(Map<String, Object> failData) throws Exception {
		return paTmonClaimProcess.updateProcFlag(failData);
	}
	
	@Override
	public String updatePaTmonCancelListTx(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.updatePaTmonCancelList(paramMap);
	}
	
	@Override
	public int updatePaTmonCancelConfirmTx(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.updatePaTmonCancelConfirm(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public String saveTmonClaimListTx(List<PaTmonClaimListVO> paTmonClaimList) throws Exception {
		return paTmonClaimProcess.saveTmonClaimList(paTmonClaimList);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.compareAddress(paramMap);
	}
	
	@Override
	public List<Map<String,Object>> selectPaTmonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectPaTmonReturnExchangeApprovalList(paramMap);
	}
	
	@Override
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		int result = 0;
		
		result = paTmonClaimProcess.updatePaOrderMDoFlag(paorderm);
		
		if(result < 1) throw new Exception();
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> selectClaimDtTargetList(String claimGb) throws Exception {
		return paTmonClaimProcess.selectClaimDtTargetList(claimGb);
	}
	
	@Override
	public String updatePaTmonClaimListDetailTx(PaTmonClaimListVO vo) throws Exception {
		return paTmonClaimProcess.updatePaTmonClaimListDetail(vo);
	}

	@Override
	public List<Map<String, Object>> selectReturnInvoiceTargetList() throws Exception {
		return paTmonClaimProcess.selectReturnInvoiceTargetList();
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public void preOrderChangeCancelTx(HashMap<String, Object> hmSheet) throws Exception {
		int executeRtn = 0;
		
		Map<String, Object> preCancelMap = new HashMap<String, Object>();
		preCancelMap.put("preCanYn"			, "1");
		preCancelMap.put("MAPPING_SEQ"		, String.valueOf(hmSheet.get("MAPPING_SEQ")));
		preCancelMap.put("apiResultMessage"	, getMessage("pa.before_change_create_cancel"));
		executeRtn = paTmonDeliveryProcess.updatePreCanYn(preCancelMap);
		if(executeRtn < 1) throw processException("msg.cannot_save", new String[] {"TPAORDERM BY preOrderChangeCancelTx "});
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paTmonClaimProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}

	@Override
	public List<Map<String, Object>> selectPaTmonExchangeRefuseList() throws Exception {
		return paTmonClaimProcess.selectPaTmonExchangeRefuseList();
	}

	@Override
	public int updatePaOrderM4ChangeRefualReuslt(ParamMap apiDataMap) throws Exception {
		return paTmonClaimProcess.updatePaOrderM4ChangeRefualReuslt(apiDataMap);
	}

	@Override
	public List<Map<String, Object>> selectPaTmonExchangeHoldProcList(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.selectPaTmonExchangeHoldProcList(paramMap);
	}

	@Override
	public int saveTmonRedelivaryListTx(List<PaTmonRedeliveryListVO> paTmonReDeliverylList) throws Exception {
		return paTmonClaimProcess.saveTmonRedelivaryList(paTmonReDeliverylList);
	}

	@Override
	public int updatePaTmonHoldYnTx(Map<String, Object> exchangeHoldData) throws Exception {
		return paTmonClaimProcess.updatePaTmonHoldYn(exchangeHoldData);
	}
	
	@Override
	public String updatePaTmonReturnListTx(ParamMap paramMap) throws Exception {
		return paTmonClaimProcess.updatePaTmonReturnList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectMappingSeq(ParamMap claimMap) throws Exception {
		return paTmonClaimProcess.selectMappingSeq(claimMap);
	}
	
	@Override
	public List<String> selectPaTmonReturnRequestedDateList() throws Exception {
		return paTmonClaimProcess.selectPaTmonReturnRequestedDateList();
	}
}
