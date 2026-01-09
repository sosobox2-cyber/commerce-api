package com.cware.netshopping.panaver.v3.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.message.v3.ChangedProductOrderInfoListMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.panaver.v3.domain.ChangedProductOrderInfo;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoAll;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfoMsg;
import com.cware.netshopping.panaver.v3.process.PaNaverV3InfoCommonProcess;
import com.cware.netshopping.panaver.v3.service.PaNaverV3InfoCommonService;

@Service("panaver.v3.infocommon.paNaverV3InfoCommonService")
public class PaNaverV3InfoCommonServiceImpl extends AbstractService implements PaNaverV3InfoCommonService {

	@Autowired
	@Qualifier("panaver.v3.infocommon.paNaverV3InfoCommonProcess")
	PaNaverV3InfoCommonProcess paNaverV3InfoCommonProcess;
	
	@Override
	public ChangedProductOrderInfoListMsg getChangedProductOrderInfoList(String lastChangedType, String fromDate, String toDate, String productOrderIds, int limitCount, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3InfoCommonProcess.getChangedProductOrderInfoList(lastChangedType, fromDate, toDate, productOrderIds, limitCount, procId, request);
	}

	@Override
	public List<ChangedProductOrderInfo> mergeChangeOrderListTx(List<ChangedProductOrderInfo> changedProductOrderInfoList, String productOrderId) throws Exception {
		return paNaverV3InfoCommonProcess.mergeChangeOrderListProc(changedProductOrderInfoList, productOrderId);
	}

	@Override
	public List<Object> selectOrderInputTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectOrderInputTargetList();
	}
	
	@Override
	public List<Object> selectCancelInputTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectOrderChangeTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paNaverV3InfoCommonProcess.selectChangeCancelTargetList();
	}

	@Override
	public int updateChangeApplied(ChangedProductOrderInfo order) throws Exception {
		return paNaverV3InfoCommonProcess.updateChangeApplied(order);
	}

	@Override
	public ProductOrderInfoAll insertCancelInfoTx(ProductOrderInfoAll cancelInfo) throws Exception {
		return paNaverV3InfoCommonProcess.insertCancelInfoProc(cancelInfo);
	}
	
	@Override
	public ProductOrderInfoMsg orderDetailInfo(String productOrderIds,String procId, HttpServletRequest request) throws Exception{
		return paNaverV3InfoCommonProcess.orderDetailInfo(productOrderIds, procId, request);
	}

	@Override
	public ProductOrderInfoAll insertExchangeInfoTx(ProductOrderInfoAll exchangeInfo) throws Exception {
		return paNaverV3InfoCommonProcess.insertExchangeInfo(exchangeInfo);
	}

	@Override
	public ProductOrderInfoAll insertReturnInfoTx(ProductOrderInfoAll returnInfo) throws Exception {
		return paNaverV3InfoCommonProcess.insertReturnInfo(returnInfo);
	}
	
	@Override
	public String getExistingClaimCount(String productOrderId, String claimType, String claimId) throws Exception {
		return paNaverV3InfoCommonProcess.getExistingClaimCount(productOrderId, claimType, claimId);
	}

	@Override
	public HashMap<String, String> selectClaimShipcostInfo(String productOrderId, String claimStatus) throws Exception {
		return paNaverV3InfoCommonProcess.selectClaimShipcostInfo(productOrderId, claimStatus);
	}

	@Override
	public ChangedProductOrderInfoListMsg getUnappliedChangedProductOrderInfoList(String productOrderId, String procId, HttpServletRequest request) throws Exception {
		return paNaverV3InfoCommonProcess.getUnappliedChangedProductOrderInfoList(productOrderId, procId, request);
	}

	@Override
	public int updateUnappliedChangedInfo(ChangedProductOrderInfo order) throws Exception {
		return paNaverV3InfoCommonProcess.updateUnappliedChangedInfo(order);
	}

	@Override
	public int selectExistingClaimReject(String orderId, String productOrderId, String claimType, String claimId) throws Exception {
		return paNaverV3InfoCommonProcess.selectExistingClaimReject(orderId, productOrderId, claimType, claimId);
	}

	@Override
	public int selectExistingCancelReject(String orderId, String productOrderId, String claimId) throws Exception {
		return paNaverV3InfoCommonProcess.selectExistingCancelReject(orderId, productOrderId, claimId);
	}
}
