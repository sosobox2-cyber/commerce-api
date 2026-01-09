package com.cware.netshopping.panaver.cancel.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.cancel.process.PaNaverCancelProcess;
import com.cware.netshopping.panaver.cancel.service.PaNaverCancelService;

@Service("panaver.cancel.paNaverCancelService")
public class PaNaverCancelServiceImpl extends AbstractService implements PaNaverCancelService{

	@Autowired
	PaNaverCancelProcess paNaverCancelProcess;
	
	/**
	 * 판매불가처리 
	 */
	@Override
	public String saveCancelSaleTx(ParamMap paramMap) throws Exception{
		return paNaverCancelProcess.saveCancelSale(paramMap);
	}
	
	//19.09.25
	@Override
	public HashMap<String, Object> selectPaNaverOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return paNaverCancelProcess.selectPaNaverOrdCancelApprovalList(cancelMap);
	}
	
	@Override
	public String saveCancelApprovalProcTx(HashMap<String, Object> cancelMap) throws Exception {
		return paNaverCancelProcess.saveCancelApprovalProc(cancelMap);
	}

	@Override
	public List<Object> selectPaNaverOrdCancelListApprovalList() throws Exception {
		return paNaverCancelProcess.selectPaNaverOrdCancelListApprovalList();
	}
	
	@Override
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverCancelProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYnTx(ParamMap paramMap) throws Exception {
		return paNaverCancelProcess.updatePreCancelYn(paramMap);
	}
}
