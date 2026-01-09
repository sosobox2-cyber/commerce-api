package com.cware.netshopping.panaver.infocommon.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.ParamMap;

public interface PaNaverInfoCommonService {

	public ParamMap mergeChangeOrderListTx(GetChangedProductOrderListResponseE response, String fromDate, String toDate) throws Exception;

	public List<ParamMap> insertCancelInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] cancelList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception;

	public List<ParamMap> insertExchangeInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] exchangeList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception;

	public List<ParamMap> insertReturnInfoTx(HttpServletRequest httpServletRequest, ProductOrderInfo[] returnList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception;

	public int checkExistingReturnClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception;

	public int checkExistingExchangeClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception;

	public List<Object> selectOrderInputTargetList() throws Exception;

	public List<Object> selectCancelInputTargetList() throws Exception;

	public List<Object> selectOrderClaimTargetList() throws Exception;

	public List<Object> selectClaimCancelTargetList() throws Exception;

	public List<Object> selectOrderChangeTargetList() throws Exception;

	public List<Object> selectChangeCancelTargetList() throws Exception;

	public String selectRecentReturnClaimID(String productOrderID) throws Exception;

	public String selectRecentExchangeClaimID(String productOrderID) throws Exception;

	public List<ParamMap> insertPayWaitingInfo(HttpServletRequest httpServletRequest, ProductOrderInfo[] payWaitingList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception;

	public List<Object> selectPreOrderInputTargetList() throws Exception;

	public List<Object> selectPreOrderUpdateTargetList() throws Exception;

	public int selectUnAttendedCount(String orderID) throws Exception;
	
	public double selectPaOrderShipCost(String paOrderNo) throws Exception;

	public HashMap<String, Object> selectPaAddrInfo(String paOrderNo) throws Exception;

	public List<HashMap<String, Object>> selectNotTakenPresentList() throws Exception;

	public int saveCancelNotTakenPresent(HashMap<String, Object> hmSheet) throws Exception;

	public List<HashMap<String, String>> selectUnappliedChangedInfo() throws Exception;

	public int deleteUnappliedChangedInfo(ParamMap paramMap) throws Exception;

	public HashMap<String, String> selectClaimShipcostInfo(ParamMap paramMap) throws Exception;

	public HashMap<String, String> selectExistingOngoingReturnInfo(ParamMap paramMap) throws Exception;

	public HashMap<String, String> selectExistingOngoingExchangeInfo(ParamMap paramMap) throws Exception;

	public ParamMap selectUnappliedOrderChange(String orderId) throws Exception;

}
