package com.cware.netshopping.panaver.v3.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.v3.process.PaNaverV3CancelProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3CancelService;

@Service("panaver.v3.cancel.paNaverV3CancelService")
public class PaNaverV3CancelServiceImpl extends AbstractService implements PaNaverV3CancelService {

	@Autowired
	@Qualifier("panaver.v3.cancel.paNaverV3CancelProcess")
	PaNaverV3CancelProcess paNaverV3CancelProcess;

	@Override
	public ResponseMsg approvalCancel(String orderId, String productOrderId, String outBefClaimGb, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3CancelProcess.approvalCancel(orderId, productOrderId, outBefClaimGb, procId, request);
	}

	@Override
	public String saveCancelApprovalProcTx(Map<String, Object> cancelMap) throws Exception {
		return paNaverV3CancelProcess.saveCancelApprovalProc(cancelMap);
	}

	@Override
	public ResponseMsg requestCancel(String productOrderId, String paOrderNo, String paOrderSeq, String paCode, int cancelReasonCode, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3CancelProcess.requestCancel(productOrderId, paOrderNo, paOrderSeq, paCode, cancelReasonCode, procId, request);
	}

	@Override
	public String saveCancelSaleTx(ParamMap paramMap) throws Exception {
		return paNaverV3CancelProcess.saveCancelSale(paramMap);
	}

	@Override
	public int updatePreCancelYnTx(ParamMap paramMap) throws Exception {
		return paNaverV3CancelProcess.updatePreCancelYnTx(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverV3CancelProcess.selectCancelInputTargetDtList(paramMap);
	}
}
