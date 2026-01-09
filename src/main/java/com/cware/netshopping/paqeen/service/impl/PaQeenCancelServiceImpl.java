package com.cware.netshopping.paqeen.service.impl;



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
import com.cware.netshopping.paqeen.process.PaQeenCancelProcess;
import com.cware.netshopping.paqeen.service.PaQeenCancelService;

@Service("paqeen.cancel.paQeenCancelService")
public class PaQeenCancelServiceImpl extends AbstractService implements PaQeenCancelService{
	
	@Autowired
	@Qualifier("paqeen.cancel.paQeenCancelProcess")
	PaQeenCancelProcess paQeenCancelProcess;

	@Override
	public ResponseMsg getCancelList(String satetAt, String endAt, HttpServletRequest request, String cancelStatus) throws Exception {
		return paQeenCancelProcess.getCancelList(satetAt, endAt, request, cancelStatus);
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paQeenCancelProcess.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paQeenCancelProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public ResponseMsg cancelConfirmProc(HttpServletRequest request) throws Exception {
		return paQeenCancelProcess.cancelConfirmProc(request);
	}

	@Override
	public ResponseMsg cancelRefusalProc(Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		return paQeenCancelProcess.cancelRefusalProc(requestMap, request);
	}

	@Override
	public ParamMap cancelConfirmBo(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		return paQeenCancelProcess.cancelConfirmBo(requestMap ,request);
	}

	@Override
	public List<HashMap<String, Object>> selectPaQeenSoldOutordList(ParamMap paramMap) throws Exception {
		return paQeenCancelProcess.selectPaQeenSoldOutordList(paramMap);
	}

	@Override
	public ResponseMsg cancelRequest(HashMap<String, Object> cancelItem, HttpServletRequest request) throws Exception {
		return paQeenCancelProcess.cancelRequest(cancelItem, request);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paQeenCancelProcess.selectPaMobileOrderAutoCancelList();
	}

	@Override
	public ResponseMsg mobileOrderSoldOut(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception {
		return paQeenCancelProcess.mobileOrderSoldOut(cancelItem, request);
	}
	
}
