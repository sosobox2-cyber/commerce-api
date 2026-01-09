package com.cware.netshopping.pafaple.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.process.PaFapleCancelProcess;
import com.cware.netshopping.pafaple.service.PaFapleCancelService;

@Service("pafaple.cancel.paFapleCancelService")
public class PaFapleCancelServiceImpl  extends AbstractService implements PaFapleCancelService {

	@Autowired
	@Qualifier("pafaple.cancel.paFapleCancelProcess")
	PaFapleCancelProcess paFapleCancelProcess;
	
	@Override
	public ResponseMsg getCancelList(String cancelStatus, String fromDate, String toDate, HttpServletRequest request) throws Exception {
		return paFapleCancelProcess.getCancelList(cancelStatus, fromDate, toDate, request);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paFapleCancelProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paFapleCancelProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public ParamMap cancelApprovalProcBo(HttpServletRequest request) throws Exception {
		return paFapleCancelProcess.cancelApprovalProcBo(request);
	}

	@Override
	public ParamMap mobileOrderSoldOut(HttpServletRequest request) throws Exception {
		return paFapleCancelProcess.mobileOrderSoldOut(request);
	}
}
