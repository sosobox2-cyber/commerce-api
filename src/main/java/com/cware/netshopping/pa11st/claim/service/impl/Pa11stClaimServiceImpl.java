package com.cware.netshopping.pa11st.claim.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.pa11st.claim.process.Pa11stClaimProcess;
import com.cware.netshopping.pa11st.claim.service.Pa11stClaimService;


@Service("pa11st.claim.pa11stClaimService")
public class Pa11stClaimServiceImpl  extends AbstractService implements Pa11stClaimService {

	@Resource(name = "pa11st.claim.pa11stClaimProcess")
    private Pa11stClaimProcess pa11stClaimProcess;
	
	@Override
	public String saveReturnListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stClaimProcess.saveReturnList(arrPa11stclaimlist);
	}
	
	@Override
	public String saveReturnCompleteListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stClaimProcess.saveReturnCompleteList(arrPa11stclaimlist);
	}
	
	@Override
	public List<Object> selectReturnOkList() throws Exception{
		return pa11stClaimProcess.selectReturnOkList();
	}
	
	@Override
	public ParamMap saveReturnConfirmProcTx(HashMap<String, Object> returnMap) throws Exception{
		return pa11stClaimProcess.saveReturnConfirmProc(returnMap);
	}
	
	@Override
	public String saveReturnCancelListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stClaimProcess.saveReturnCancelList(arrPa11stclaimlist);
	}
	
	@Override
	public List<Object> selectReturnHoldList() throws Exception{
		return pa11stClaimProcess.selectReturnHoldList();
	}
	
	@Override
	public ParamMap saveReturnHoldProcTx(HashMap<String, Object> returnMap) throws Exception{
		return pa11stClaimProcess.saveReturnHoldProc(returnMap);
	}
	
	@Override
	public String saveAuthCancelListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stClaimProcess.saveAuthCancelList(arrPa11stclaimlist);
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return pa11stClaimProcess.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return pa11stClaimProcess.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return pa11stClaimProcess.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return pa11stClaimProcess.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stClaimProcess.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception{
		return pa11stClaimProcess.compareAddress(paramMap);
	}
	
	@Override
	public List<Object> selectReturnSlipProcList() throws Exception{
		return pa11stClaimProcess.selectReturnSlipProcList();
	}
	
	@Override
	public ParamMap saveReturnSlipProcTx(HashMap<String, Object> returnSlipMap) throws Exception{
		return pa11stClaimProcess.saveReturnSlipProc(returnSlipMap);
	}
}