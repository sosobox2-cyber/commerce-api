package com.cware.netshopping.panaver.infocommon.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.panaver.infocommon.process.PaNaverInfoCommonProcess;
import com.cware.netshopping.panaver.infocommon.service.PaNaverInfoCommonService;

@Service("panaver.infocommon.paNaverInfoCommonService")
public class PaNaverInfoCommonServiceImpl extends AbstractService implements PaNaverInfoCommonService {

	@Autowired
	PaNaverInfoCommonProcess paNaverInfoCommonProcess;
	
	@Override
	public ParamMap mergeChangeOrderListTx(GetChangedProductOrderListResponseE response, String fromDate, String toDate) throws Exception {
		return paNaverInfoCommonProcess.mergeChangeOrderListProc(response, fromDate, toDate);
	}
	
	@Override
	public List<ParamMap> insertCancelInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] cancelList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		return paNaverInfoCommonProcess.insertCancelInfoProc(httpServletRequest, cancelList, naverSignature, changedProductOrderInfo);
	}

	@Override
	public List<ParamMap> insertExchangeInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] exchangeList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		return paNaverInfoCommonProcess.insertExchangeInfo(httpServletRequest, exchangeList, naverSignature, changedProductOrderInfo);
	}

	@Override
	public List<ParamMap> insertReturnInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] returnList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		return paNaverInfoCommonProcess.insertReturnInfo(httpServletRequest, returnList, naverSignature, changedProductOrderInfo);
	}
	
	@Override
	public int checkExistingReturnClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception {
		return paNaverInfoCommonProcess.checkExistingReturnClaim(httpServletRequest, productOrderInfo, param);
	}
	
	@Override
	public int checkExistingExchangeClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception {
		return paNaverInfoCommonProcess.checkExistingExchangeClaim(httpServletRequest, productOrderInfo, param);
	}

	@Override
	public List<Object> selectOrderInputTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectOrderInputTargetList();
	}

	@Override
	public List<Object> selectCancelInputTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectOrderChangeTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectChangeCancelTargetList();
	}
	
	@Override
	public String selectRecentReturnClaimID(String productOrderID) throws Exception {
		return paNaverInfoCommonProcess.selectRecentReturnClaimID(productOrderID);
	}

	@Override
	public String selectRecentExchangeClaimID(String productOrderID) throws Exception {
		return paNaverInfoCommonProcess.selectRecentExchangeClaimID(productOrderID);
	}
	
	@Override
	public List<ParamMap> insertPayWaitingInfo(HttpServletRequest httpServletRequest, ProductOrderInfo[] payWaitingList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		return paNaverInfoCommonProcess.insertPayWaitingInfo(httpServletRequest, payWaitingList, naverSignature, changedProductOrderInfo);
	}
	
	@Override
	public List<Object> selectPreOrderInputTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectPreOrderInputTargetList();
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetList() throws Exception {
		return paNaverInfoCommonProcess.selectPreOrderUpdateTargetList();
	}
	
	@Override
	public int selectUnAttendedCount(String orderID) throws Exception {
		return paNaverInfoCommonProcess.selectUnAttendedCount(orderID);
	}
	
	@Override
	public double selectPaOrderShipCost(String paOrderNo) throws Exception {
		return paNaverInfoCommonProcess.selectPaOrderShipCost(paOrderNo);
	}
	
	@Override
	public HashMap<String, Object> selectPaAddrInfo(String paOrderNo) throws Exception {
		return paNaverInfoCommonProcess.selectPaAddrInfo(paOrderNo);
	}
	
	@Override
	public List<HashMap<String, Object>> selectNotTakenPresentList() throws Exception {
		return paNaverInfoCommonProcess.selectNotTakenPresentList();
	}

	@Override
	public int saveCancelNotTakenPresent(HashMap<String, Object> hmSheet) throws Exception {
		return paNaverInfoCommonProcess.saveCancelNotTakenPresent(hmSheet);
	}
	
	@Override
	public List<HashMap<String, String>> selectUnappliedChangedInfo() throws Exception {
		return paNaverInfoCommonProcess.selectUnappliedChangedInfo();
	}
	
	@Override
	public int deleteUnappliedChangedInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonProcess.deleteUnappliedChangedInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectClaimShipcostInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonProcess.selectClaimShipcostInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectExistingOngoingReturnInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonProcess.selectExistingOngoingReturnInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectExistingOngoingExchangeInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonProcess.selectExistingOngoingExchangeInfo(paramMap);
	}
	
	@Override
	public ParamMap selectUnappliedOrderChange(String orderId) throws Exception {
		return paNaverInfoCommonProcess.selectUnappliedOrderChange(orderId);
	}
}
