package com.cware.netshopping.pawemp.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaWempDeliveryVO;
import com.cware.netshopping.domain.PaWempTargetVO;
import com.cware.netshopping.domain.PawemporderlistVO;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.PaWempOrderItemList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pawemp.order.process.PaWempOrderProcess;
import com.cware.netshopping.pawemp.order.service.PaWempOrderService;

@Service("pawemp.order.paWempOrderService")
public class PaWempOrderServiceImpl extends AbstractService implements PaWempOrderService {
	
	@Resource(name = "pawemp.order.paWempOrderProcess")
	private PaWempOrderProcess paWempOrderProcess;
	
	@Override
	public String savePaWempOrderTx(PawemporderlistVO paWempOrderList, List<PaWempOrderItemList> paWempOrderItemList) throws Exception {
		return paWempOrderProcess.savePaWempOrder(paWempOrderList, paWempOrderItemList);
	}

	@Override
	public List<PaWempTargetVO> selectOrderInputTargetList(int targetCnt) throws Exception {
		return paWempOrderProcess.selectOrderInputTargetList(targetCnt);
	}
	
	@Override
	public List<Object> selectOrderInputTargetDtList (ParamMap paramMap) throws Exception {
		return paWempOrderProcess.selectOrderInputTargetDtList(paramMap);
	}
	
	@Override
	public PaWempTargetVO selectRefusalInfo(String mappingSeq) throws Exception {
		return paWempOrderProcess.selectRefusalInfo(mappingSeq);
	}
	
	@Override
	public int updatePreCancelYnTx(ParamMap preCancelMap) throws Exception {
		return paWempOrderProcess.updatePreCancelYn(preCancelMap);
	}
	
	@Override
	public ParamMap orderRefusalProc(PaWempTargetVO targetVo) throws Exception {
		return paWempOrderProcess.orderRefusalProc(targetVo);
	}

	@Override
	public List<Object> selectSlipOutProcList() throws Exception {
		return paWempOrderProcess.selectSlipOutProcList();
	}
	
	@Override
	public ParamMap slipOutProc(HashMap<String, Object> deliveryVo, HashMap<String, String> apiInfo) throws Exception {
		return paWempOrderProcess.slipOutProc(deliveryVo, apiInfo);
	}
	
	@Override
	public List<Object> selectSlipOutProcDtList (String paShipNo) throws Exception {
		return paWempOrderProcess.selectSlipOutProcDtList(paShipNo);
	}
	
	@Override
	public int updateSlipOutProcTx(HashMap<String, Object> deliveryVo) throws Exception {
		return paWempOrderProcess.updateSlipOutProc(deliveryVo);
	}
	
	@Override
	public int updateSlipOutProcFailTx(HashMap<String, Object> deliveryVo, String apiMsg) throws Exception {
		return paWempOrderProcess.updateSlipOutProcFail(deliveryVo, apiMsg);
	}
	
	@Override
	public int updateInvoiceInfoTx(HashMap<String, Object> deliveryVo, String deliveryStatus) throws Exception {
		return paWempOrderProcess.updateInvoiceInfo(deliveryVo, deliveryStatus);
	}
	
	@Override
	public String updateDeliveryCompleteTx(Paorderm paOrderm) throws Exception {
		return paWempOrderProcess.updateDeliveryComplete(paOrderm);
	}
	
	@Override
	public List<PaWempClaimList> selectCancelRequestList() throws Exception {
		return paWempOrderProcess.selectCancelRequestList();
	}
	
	@Override
	public List<PaWempDeliveryVO> selectCancelRequestDtList(ParamMap paramMap) throws Exception {
		return paWempOrderProcess.selectCancelRequestDtList(paramMap);
	}
	
	@Override
	public ParamMap cancelApproveProc(long claimBundleNo, String paCode) throws Exception {
		return paWempOrderProcess.cancelApproveProc(claimBundleNo, paCode);
	}
	
	@Override
	public ParamMap cancelRejectProc(PaWempDeliveryVO deliveryVo) throws Exception {
		return paWempOrderProcess.cancelRejectProc(deliveryVo);
	}
	
	@Override
	public ParamMap saveCancelApproveTx(List<PaWempDeliveryVO> deliveryVoList, String outBefClaimGb, boolean isUpdate) throws Exception {
		return paWempOrderProcess.saveCancelApprove(deliveryVoList, outBefClaimGb, isUpdate);
	}
	
	@Override
	public ParamMap saveCancelRejectTx(PaWempDeliveryVO deliveryVo) throws Exception {
		return paWempOrderProcess.saveCancelReject(deliveryVo);
	}
	
	@Override
	public ParamMap saveCancelFailTx(PaWempDeliveryVO deliveryVo) throws Exception {
		return paWempOrderProcess.saveCancelFail(deliveryVo);
	}
	
	@Override
	public int updateCancelWithdrawYnTx(PaWempClaimList paWempClaimList) throws Exception {
		return paWempOrderProcess.updateCancelWithdrawYn(paWempClaimList);
	}
	
	@Override
	public List<PaWempTargetVO> selectCancelInputTargetList() throws Exception {
		return paWempOrderProcess.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paWempOrderProcess.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updateClaimItemProcFlag(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String procFlag) throws Exception {
		return paWempOrderProcess.updateClaimItemProcFlag(paClaimNo, paOrderNo, paShipNo, paOrderGb, procFlag);
	}
	
	@Override
	public int selectBusinessDayAccount(String paClaimNo, String delyGb) throws Exception {
		return paWempOrderProcess.selectBusinessDayAccount(paClaimNo, delyGb);
	}
	
	@Override
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return paWempOrderProcess.selectDeliveryCompleteList();
	}
	
	@Override
	public List<PaWempDeliveryVO> selectOrgItemInfoByCancelInfo(String paClaimNo) throws Exception{
		return paWempOrderProcess.selectOrgItemInfoByCancelInfo(paClaimNo);
	}

	@Override
	public int updateDeliveryStatusTx(String paShipNo, String deliveryStatus) throws Exception{
		return paWempOrderProcess.updateDeliveryStatus(paShipNo, deliveryStatus);
	}
	
	@Override
	public int selectPaWempOrderListExists(PawemporderlistVO paWempOrderList) throws Exception {
		return paWempOrderProcess.selectPaWempOrderListExists(paWempOrderList);
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception {
		return paWempOrderProcess.selectOrderPromo(orderMap);
	}	
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception {
		return paWempOrderProcess.selectOrderPaPromo(orderMap);
	}

	@Override
	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception {
		return paWempOrderProcess.selectSlipUpdateProcList();
	}	
}
