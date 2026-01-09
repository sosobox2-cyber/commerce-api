package com.cware.netshopping.pa11st.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.delivery.process.Pa11stDeliveryProcess;
import com.cware.netshopping.pa11st.delivery.service.Pa11stDeliveryService;


@Service("pa11st.delivery.pa11stDeliveryService")
public class Pa11stDeliveryServiceImpl  extends AbstractService implements Pa11stDeliveryService {

	@Resource(name = "pa11st.delivery.pa11stDeliveryProcess")
    private Pa11stDeliveryProcess pa11stDeliveryProcess;
	
	@Override
	public String saveOrderConfirmProcTx(ParamMap paramMap) throws Exception{
		return pa11stDeliveryProcess.saveOrderConfirmProc(paramMap);
	}
	
	@Override
	public int selectOrderConfirmProcExists(ParamMap paramMap) throws Exception{
		return pa11stDeliveryProcess.selectOrderConfirmProcExists(paramMap);
	}
	
	@Override
	public List<Object> selectPa11stSlipProcList() throws Exception{
		return pa11stDeliveryProcess.selectPa11stSlipProcList();
	}
	
	@Override
	public ParamMap saveSlipOutProcTx(HashMap<String, Object> slipMap) throws Exception{
		return pa11stDeliveryProcess.saveSlipOutProc(slipMap);
	}
	
	@Override
	public String saveDeliveryCompleteListTx(List<Paorderm> arrPaordermlist) throws Exception{
		return pa11stDeliveryProcess.saveDeliveryCompleteList(arrPaordermlist);
	}
	
	@Override
	public String saveOrderConfirmListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		return pa11stDeliveryProcess.saveOrderConfirmList(arrPa11storderlist);
	}
	
	@Override
	public String updateTodayDeliveryListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		return pa11stDeliveryProcess.updateTodayDeliveryList(arrPa11storderlist);
	}
	
	@Override
	public List<Object> selectPa11stDelayList(String dateTime) throws Exception{
		return pa11stDeliveryProcess.selectPa11stDelayList(dateTime);
	}
	
	@Override
	public int updateTpaordermDelaySendDt(Map<String, Object> paramMap) throws Exception{
		return pa11stDeliveryProcess.updateTpaordermDelaySendDt(paramMap);
	}
	
}