package com.cware.netshopping.paintp.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaIntpTargetVO;
import com.cware.netshopping.domain.model.PaIntpCancellist;
import com.cware.netshopping.domain.model.PaIntpCancellistKey;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderVO;
import com.cware.netshopping.paintp.order.process.PaIntpOrderProcess;
import com.cware.netshopping.paintp.order.service.PaIntpOrderService;

@Service("paintp.order.paIntpOrderService")
public class PaIntpOrderServiceImpl extends AbstractService implements PaIntpOrderService {
	
	@Autowired
    private PaIntpOrderProcess paIntpOrderProcess;
	
	@Override
	public List<PaIntpTargetVO> selectOrderInputTargetList(int limitCount) throws Exception {
		return paIntpOrderProcess.selectOrderInputTargetList(limitCount);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpOrderProcess.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public Map<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return paIntpOrderProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public String saveOrderRejectProcTx(String ordNo, Integer ordSeq, boolean canceled, String apiResultCode, String apiResultMessage, String preCancelReason, String clmReqSeq, String paCode) throws Exception {
		return paIntpOrderProcess.saveOrderRejectProcTx(ordNo, ordSeq, canceled, apiResultCode, apiResultMessage, preCancelReason, clmReqSeq, paCode);
	}
	
	@Override
	public List<PaIntpCancellist> selectPaIntpOrderCancelList() throws Exception {
		return paIntpOrderProcess.selectPaIntpOrderCancelList();
	}
	
	@Override
	public int updatePreCancelYnTx(String mappingSeq, String preCancelYn, String preCancelReason) throws Exception {
		return paIntpOrderProcess.updatePreCancelYn(mappingSeq, preCancelYn, preCancelReason);
	}
	
	@Override
	public List<PaIntpTargetVO> selectCancelInputTargetList() throws Exception {
		return paIntpOrderProcess.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paIntpOrderProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public PaIntpCancellistKey selectPaIntpCancellistApproval(String ordNo, Integer ordSeq, Integer clmreqSeq, String paCode) throws Exception {
		return paIntpOrderProcess.selectPaIntpCancellistApproval(ordNo, ordSeq, clmreqSeq, paCode);
	}
	
	@Override
	public List<Map<String,String>> selectSlipOutProcList(ParamMap paramMap) throws Exception{
	    return paIntpOrderProcess.selectSlipOutProcList(paramMap);
	}
	
	@Override
	public int updateSlipOutProcTx(Map<?,?> slipOut) throws Exception{
	    return paIntpOrderProcess.updateSlipOutProc(slipOut);
	}
	
	@Override
	public int updateSlipOutProcFailTx(Map<?,?> slipOut) throws Exception{
	    return paIntpOrderProcess.updateSlipOutProcFail(slipOut);
	}
	
	@Override
	public String saveOrderConfirmListTx(PaIntpOrderVO orderList, String paCode) throws Exception {
		return paIntpOrderProcess.saveOrderConfirmList(orderList, paCode);
	}

	@Override
	public void saveCancelRequestOrWithdrawListTx(PaIntpCancelReqVO cancelVO) throws Exception {
		paIntpOrderProcess.saveCancelRequestOrWithdrawList(cancelVO);
		
	}

	@Override
	public String saveCancelConfirmTx(PaIntpCancellist cancel, String claimGb) throws Exception {
		return paIntpOrderProcess.saveCancelConfirmProc(cancel, claimGb);
	}

	@Override
	public String updatePaOrdermCancelRefusalTx(String ordNo, String ordSeq, String clmreqSeq, String apiResultCode, String apiResultMessage) throws Exception {
		return paIntpOrderProcess.updatePaOrdermCancelRefusal(ordNo, ordSeq, clmreqSeq, apiResultCode, apiResultMessage);
	}

	@Override
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception {
		return paIntpOrderProcess.updatePreCancelYn(preCancelMap);
	}

	@Override
	public PaIntpCancellist selectOrgItemInfoByCancelInfo(String ordNo, String ordSeq, String clmreqSeq) throws Exception {
		return paIntpOrderProcess.selectOrgItemInfoByCancelInfo(ordNo, ordSeq, clmreqSeq);
	}

	@Override
	public void saveDeliveryComplete(PaIntpOrderVO complete) throws Exception {
		paIntpOrderProcess.saveDeliveryComplete(complete);
	}

	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paIntpOrderProcess.selectOrderPromo(orderMap);
	}

	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paIntpOrderProcess.selectOrderPaPromo(orderMap);
	}

}