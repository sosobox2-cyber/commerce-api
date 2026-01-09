package com.cware.netshopping.palton.claim.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonClaimListVO;
import com.cware.netshopping.domain.model.PaLtonNotReceiveList;
import com.cware.netshopping.palton.claim.process.PaLtonClaimProcess;
import com.cware.netshopping.palton.claim.service.PaLtonClaimService;
import com.cware.netshopping.palton.delivery.process.PaLtonDeliveryProcess;

@Service("palton.claim.paLtonClaimService")
public class PaLtonClaimServiceImpl extends AbstractService implements PaLtonClaimService{

	@Autowired
	private PaLtonClaimProcess paLtonClaimProcess;
	@Autowired
	private PaLtonDeliveryProcess paLtonDeliveryProcess;
	
	
	@Override
	public List<Map<String, Object>> selectPaLtonOrderCancelList() throws Exception {
		return paLtonClaimProcess.selectPaLtonOrderCancelList();
	}

	@Override
	public List<Map<String, Object>> selectPaLtonCancelApprovalList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonCancelApprovalList(paramMap);
	}

	@Override
	public int saveCancelConfirmTx(Map<String, Object> cancelMap) throws Exception {
		return paLtonClaimProcess.saveCancelConfirm(cancelMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonCancelRefusalList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonCancelRefusalList(apiDataMap);
	}

	@Override
	public int saveLtonCancelList(PaLtonCancelListVO vo) throws Exception {
		return paLtonClaimProcess.saveLtonCancelList(vo);
	}

	@Override
	public HashMap<String, Object> selectPaLtonOrderCancel(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonOrderCancel(paramMap);
	}

	@Override
	public int updateProcFlag(Map<String, Object> cancelMap) throws Exception{
		return paLtonClaimProcess.updateProcFlag(cancelMap);
	}

	@Override
	public int saveLtonCompleteCancelListTx(PaLtonCancelListVO cancelVo) throws Exception {
		return paLtonClaimProcess.saveLtonCompleteCancelList(cancelVo);
	}

	@Override
	public int saveLtonWithdrawCancelList(PaLtonCancelListVO cancelVo) throws Exception {
		return paLtonClaimProcess.saveLtonWithdrawCancelList(cancelVo);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public void savePaLtonClaimListTx(PaLtonClaimListVO vo) throws Exception {
		paLtonClaimProcess.savePaLtonClaimList(vo);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalDtList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonReturnExchangeApprovalDtList(apiDataMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonReturnExchangeApprovalList(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.compareAddress(paramMap);
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectReturnSlipProcList() throws Exception {
		return paLtonClaimProcess.selectReturnSlipProcList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeSlipProcList() throws Exception {
		return paLtonClaimProcess.selectExchangeSlipProcList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeSendList() throws Exception {
		return paLtonClaimProcess.selectExchangeSendList();
	}

	@Override
	public List<Map<String, Object>> selectExchangeCompleteList() throws Exception {
		return paLtonClaimProcess.selectExchangeCompleteList();
	}

	@Override
	public List<Map<String, Object>> selectPaLtonExchangeRefuseList() throws Exception {
		return paLtonClaimProcess.selectPaLtonExchangeRefuseList();
	}

	@Override
	public int updateTPaOrderM4ChangeRefualReuslt(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.updateTPaOrderM4ChangeRefualReuslt(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaLtonExchangeRefuseDtList(ParamMap apiDataMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonExchangeRefuseDtList(apiDataMap);
	}

	@Override
	public int saveLtonNotReceiveListTx(PaLtonNotReceiveList notReceiveVO) throws Exception {
		return paLtonClaimProcess.saveLtonNotReceiveList(notReceiveVO);
	}

	@Override
	public List<Map<String, Object>> selectWithDrawNotReceiveList() throws Exception {
		return paLtonClaimProcess.selectWithDrawNotReceiveList();
	}

	@Override
	public int updateNotReceiveList4WithDraw(ParamMap apiDatMap) throws Exception {
		return paLtonClaimProcess.updateNotReceiveList4WithDraw(apiDatMap);
	}

	@Override
	public void preOrderChangeCancelTx(HashMap<String, Object> hmSheet) throws Exception {

		int executeRtn = 0;
		
		//1) 41,46의 precancelYn = 1처리
		Map<String , Object> preCancelMap = new HashMap<String, Object>();
		preCancelMap.put("preCanYn"			, "1");
		preCancelMap.put("mappingSeq"		, String.valueOf(hmSheet.get("MAPPING_SEQ")));
		preCancelMap.put("apiResultMessage"	, getMessage("pa.before_change_create_cancel"));
		executeRtn = paLtonDeliveryProcess.updatePreCanYn(preCancelMap);
		if(executeRtn < 1) throw processException("msg.cannot_save", new String[] {"TPAORDERM BY preOrderChangeCancelTx "});
		
		//2) 40,45의 precancelYn = 1 처리
		preCancelMap.put("paCode"			, String.valueOf(hmSheet.get("PA_CODE")));
		preCancelMap.put("paOrderNo"		, String.valueOf(hmSheet.get("PA_ORDER_NO")));
		preCancelMap.put("paOrderSeq"		, String.valueOf(hmSheet.get("PA_ORDER_SEQ")));
		preCancelMap.put("paShipSeq"		, String.valueOf(hmSheet.get("PA_SHIP_SEQ")));
		
		executeRtn = paLtonClaimProcess.updatePaOrderm4PreChangeCancel(preCancelMap);
		if(executeRtn < 1) throw processException("msg.cannot_save", new String[] {"TPAORDERM BY preOrderChangeCancelTx "});
	}
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paLtonClaimProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}

	@Override
	public int selectConfirmCancelQty(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectConfirmCancelQty(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaLtonCancelApprovalListBO(ParamMap paramMap) throws Exception {
		return paLtonClaimProcess.selectPaLtonCancelApprovalListBO(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paLtonClaimProcess.selectPaMobileOrderAutoCancelList();
	}

}
