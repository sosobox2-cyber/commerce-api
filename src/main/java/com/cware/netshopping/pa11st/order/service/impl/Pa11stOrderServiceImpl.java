package com.cware.netshopping.pa11st.order.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11storderlistVO;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pa11st.order.process.Pa11stOrderProcess;
import com.cware.netshopping.pa11st.order.service.Pa11stOrderService;

@Service("pa11st.order.pa11stOrderService")
public class Pa11stOrderServiceImpl  extends AbstractService implements Pa11stOrderService {

	@Resource(name = "pa11st.order.pa11stOrderProcess")
    private Pa11stOrderProcess pa11stOrderProcess;

	
	@Override
	public String saveOrderRejectProcTx(ParamMap paramMap) throws Exception{
		return pa11stOrderProcess.saveOrderRejectProc(paramMap);
	}
	
	@Override
	public String saveCancelListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		return pa11stOrderProcess.saveCancelList(arrPa11storderlist);
	}
	
	@Override
	public List<Object> selectPa11stOrdCancelList() throws Exception{
		return pa11stOrderProcess.selectPa11stOrdCancelList();
	}

	@Override
	public ParamMap saveCancelConfirmProcTx(HashMap<String, Object> cancelMap) throws Exception {
		return pa11stOrderProcess.saveCancelConfirmProc(cancelMap);
	}
	
	@Override
	public String saveCancelWithdrawListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		return pa11stOrderProcess.saveCancelWithdrawList(arrPa11storderlist);
	}
	
	@Override
	public String saveCancelCompleteListTx(List<Pa11storderlistVO> arrPa11storderlist) throws Exception{
		return pa11stOrderProcess.saveCancelCompleteList(arrPa11storderlist);
	}
	
	@Override
	public List<Object> selectOrderInputTargetList() throws Exception{
		return pa11stOrderProcess.selectOrderInputTargetList();
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return pa11stOrderProcess.selectOrderInputTargetDtList(orderMap);
	}
	
	@Override
	public List<Object> selectCancelInputTargetList() throws Exception{
		return pa11stOrderProcess.selectCancelInputTargetList();
	}
	
	@Override
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return pa11stOrderProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return pa11stOrderProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception {
		return pa11stOrderProcess.updatePreCancelYn(preCancelMap);
	}

	@Override
	public HashMap<String, Object> selectPa11stOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return pa11stOrderProcess.selectPa11stOrdCancelApprovalList(cancelMap);
	}

	@Override
	public String saveCancelApprovalProcTx(HashMap<String, Object> cancelMap) throws Exception {
		return pa11stOrderProcess.saveCancelApprovalProc(cancelMap);
	}
	
	@Override
	public List<Object> selectCancelWithoutOrderList(ParamMap paramMap) throws Exception {
		return pa11stOrderProcess.selectCancelWithoutOrderList(paramMap);
	}
	
	@Override
	public int updatePreCancelReason(Paorderm paorderm) throws Exception {
		return pa11stOrderProcess.updatePreCancelReason(paorderm);
	}	
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return pa11stOrderProcess.selectOrderPromo(orderMap);
	}	

	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return pa11stOrderProcess.selectOrderPaPromo(orderMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return pa11stOrderProcess.selectPaMobileOrderAutoCancelList(paramMap);

	}	
}