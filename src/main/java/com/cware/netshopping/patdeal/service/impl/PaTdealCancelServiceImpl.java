package com.cware.netshopping.patdeal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.patdeal.process.PaTdealCancelProcess;
import com.cware.netshopping.patdeal.service.PaTdealCancelService;

@Service("patdeal.cancel.paTdealCancelService")
public class PaTdealCancelServiceImpl extends AbstractService implements PaTdealCancelService {

	@Autowired
	@Qualifier("patdeal.cancel.paTdealCancelProcess")
	PaTdealCancelProcess paTdealCancelProcess;
	
	@Override
	public int saveTdealCancelListTx(String claimStatus, PaTdealClaimListVO paTdealCancelVOList) throws Exception {
		return paTdealCancelProcess.saveTdealCancelList(claimStatus, paTdealCancelVOList);
	}
	
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception{
		return paTdealCancelProcess.cancelConfirmProc(request);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealCancelProcess.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public ParamMap cancelConfirm(Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		return paTdealCancelProcess.cancelConfirm(requestMap, request);
	}

	@Override
	public Map<String, Object> cancelRequest(Object cancelItem, HttpServletRequest request) throws Exception {
		return paTdealCancelProcess.cancelRequest(cancelItem, request);
	}
	
	@Override
	public ResponseMsg soldOutCancelProc(List<Map<String, Object>> soldOutList, HttpServletRequest request) throws Exception{
		return paTdealCancelProcess.soldOutCancelProc(soldOutList, request);
	}
	
	@Override
	public ResponseMsg cancelApprovalProc(List<Map<String, Object>> cancelList, HttpServletRequest request) throws Exception{
		return paTdealCancelProcess.cancelApprovalProc(cancelList, request);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paTdealCancelProcess.selectPaMobileOrderAutoCancelList();
	}

	@Override
	public Map<String, Object> mobliecancelRequest(HashMap<String, String> cancelItem, HttpServletRequest request) throws Exception {
		return paTdealCancelProcess.mobliecancelRequest(cancelItem, request);
	}
	
}
