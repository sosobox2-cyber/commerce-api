package com.cware.netshopping.pa11st.exchange.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.exchange.process.Pa11stExchangeProcess;
import com.cware.netshopping.pa11st.exchange.service.Pa11stExchangeService;


@Service("pa11st.exchange.pa11stExchangeService")
public class Pa11stExchangeServiceImpl  extends AbstractService implements Pa11stExchangeService {

	@Resource(name = "pa11st.exchange.pa11stExchangeProcess")
    private Pa11stExchangeProcess pa11stExchangeProcess;
	
	@Override
	public String saveExchangeListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stExchangeProcess.saveExchangeList(arrPa11stclaimlist);
	}
	
	@Override
	public List<Object> selectExchangeSlipList() throws Exception{
		return pa11stExchangeProcess.selectExchangeSlipList();
	}
	
	@Override
	public ParamMap saveExchangeConfirmProcTx(HashMap<String, Object> returnMap) throws Exception{
		return pa11stExchangeProcess.saveExchangeConfirmProc(returnMap);
	}
	
	@Override
	public String saveExchangeRejectProcTx(ParamMap paramMap) throws Exception{
		return pa11stExchangeProcess.saveExchangeRejectProc(paramMap);
	}
	
	@Override
	public String saveExchangeCancelListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception{
		return pa11stExchangeProcess.saveExchangeCancelList(arrPa11stclaimlist);
	}
	
	@Override
	public int selectExchangeRefusalExists(ParamMap paramMap) throws Exception{
		return pa11stExchangeProcess.selectExchangeRefusalExists(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception{
		return pa11stExchangeProcess.selectOrderChangeTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return pa11stExchangeProcess.selectChangeCancelTargetList();
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stExchangeProcess.selectOrderChangeTargetDtList(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectRejectInfo(String mappingSeq) throws Exception {
		return pa11stExchangeProcess.selectRejectInfo(mappingSeq);
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stExchangeProcess.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public int refindOrderChangeTargetList(String paOrderNo) throws Exception {
		return pa11stExchangeProcess.refindOrderChangeTargetList(paOrderNo);
	}

	@Override
	public int updatePaOrderm4preChangeCancle(Paorderm paorderm) throws Exception {
		return pa11stExchangeProcess.updatePaOrderm4preChangeCancle(paorderm);
	}
	
	
}