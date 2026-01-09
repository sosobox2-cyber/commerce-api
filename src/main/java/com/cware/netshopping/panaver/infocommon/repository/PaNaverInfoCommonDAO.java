package com.cware.netshopping.panaver.infocommon.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.DecisionHoldbackInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ExchangeInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReturnInfo;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaNaverAddressVO;
import com.cware.netshopping.domain.PaNaverClaimListVO;
import com.cware.netshopping.domain.PaNaverOrderChangeVO;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.infocommon.paNaverInfoCommonDAO")
public class PaNaverInfoCommonDAO extends AbstractPaDAO {
	
	public int mergeChangeOrderList(ChangedProductOrderInfo order) throws Exception{
		return update("panaver.infocommon.mergeChangeOrderList", order);
	}
	
	public String selectAddressSeq() throws Exception{
		return (String) selectByPk("panaver.infocommon.selectAddressSeq", null);
	}
	
	public int insertAddress(PaNaverAddressVO addressVO) throws Exception{
		return insert("panaver.infocommon.insertAddress", addressVO);
	}
	
	public int updateOrderListAddress(ParamMap paramMap) throws Exception{
		return update("panaver.infocommon.updateOrderListAddress", paramMap.get());
	}
	
	public int updatePaOrdermOrderPlace(ParamMap paramMap) throws Exception{
		return update("panaver.infocommon.updatePaOrdermOrderPlaceQuantity", paramMap.get());
	}
	
	public int updateOrderListOrderID(ParamMap paramMap) throws Exception{
		return update("panaver.infocommon.updateOrderListOrderID", paramMap.get());
	}
	
	public int updateOrderListClaimInfo(ParamMap paramMap) throws Exception{
		return update("panaver.infocommon.updateOrderListClaimInfo", paramMap.get());
	}
	
	public int updateOrderListReturnInfo(ReturnInfo returnInfo) throws Exception{
		return update("panaver.infocommon.updateOrderListReturnInfo", returnInfo);
	}
	
	public int updateOrderListExchangeInfo(ExchangeInfo exchangeInfo) throws Exception{
		return update("panaver.infocommon.updateOrderListExchangeInfo", exchangeInfo);
	}
	
	public int updateOrderListDecisionHoldbackInfo(DecisionHoldbackInfo decisionHoldbackInfo) throws Exception{
		return update("panaver.infocommon.updateOrderInfoDecisionHoldbackInfo", decisionHoldbackInfo);
	}
	
	public String selectClaimSeq() throws Exception {
		return (String) selectByPk("panaver.infocommon.selectClaimSeq", null);
	}
	
	public int insertCancelClaim(PaNaverClaimListVO paNaverClaimListVO) throws Exception {
		return insert("panaver.infocommon.insertCancelClaim", paNaverClaimListVO);
	}

	public int insertExchangeClaim(PaNaverClaimListVO paNaverClaimListVO) throws Exception {
		return insert("panaver.infocommon.insertExchangeClaim", paNaverClaimListVO);
	}
	
	public int insertReturnClaim(PaNaverClaimListVO paNaverClaimListVO) throws Exception {
		return insert("panaver.infocommon.insertReturnClaim", paNaverClaimListVO);
	}
	
	public String selectOrderdtDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectOrderdtDoFlag", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectCancelDoneInfo(String productOrderID) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectCancelDoneInfo", productOrderID);
	}
	
	public int selectPaOrderMCancelCnt(Paorderm paorderm) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectPaOrderMCancelCnt", paorderm);
	}
	
	public String selectCancelDoneClaim(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectCancelDoneClaim", paramMap.get());
	}
	
	public int updateProcFlag(ParamMap paramMap) throws Exception {
		return update("panaver.infocommon.updateProcFlag", paramMap.get());
	}
	
	public int selectOrderListCnt(String productOrderID) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectOrderListCnt", productOrderID);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectOrdCancelList(String productOrderID) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectOrdCancelList", productOrderID);
	}

	public int selectCancelRefusalExists(String orderID) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectCancelRefusalExists", orderID);
	}
	
	public String selectExchangeCollectDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectExchangeCollectDoFlag", paramMap.get());
	}
	
	public String selectReturnCollectDoFlag(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectReturnCollectDoFlag", paramMap.get());
	}
	
	public int checkExistingClaim(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.infocommon.checkExistingClaim", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectOrderInputTargetList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectCancelInputTargetList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectOrderClaimTargetList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectClaimCancelTargetList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectOrderChangeTargetList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectChangeCancelTargetList", null);
	}
	
	public String selectRecentReturnClaimID(String productOrderID) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectRecentReturnClaimID", productOrderID);
	}
			
	public String selectRecentExchangeClaimID(String productOrderID) throws Exception {
		return (String) selectByPk("panaver.infocommon.selectRecentExchangeClaimID", productOrderID);
	}
	public int updatePaordermClaimID(ParamMap paramMap) throws Exception {
		return (int) update("panaver.infocommon.updatePaordermClaimID", paramMap.get());
	}
	public int selectWaitingPaordermCnt(ParamMap paramMap) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectWaitingPaordermCnt", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectPreOrderInputTargetList() throws Exception{
		return (List<Object>) list("panaver.infocommon.selectPreOrderInputTargetList", null);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectPreOrderUpdateTargetList() throws Exception {
		return (List<Object>) list("panaver.infocommon.selectPreOrderUpdateTargetList", null);
	}
	
	public int selectUnAttendedCount(String orderID) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectUnAttendedCount", orderID);
	}
	
	public double selectPaOrderShipCost(String paOrderNo) throws Exception {
		return (double) selectByPk("panaver.infocommon.selectPaOrderShipCost", paOrderNo);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaAddrInfo(String paOrderNo) throws Exception {
		return (HashMap<String, Object>) selectByPk("panaver.infocommon.selectPaAddrInfo", paOrderNo);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectNotTakenPresentList() throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.infocommon.selectNotTakenPresentList", null);
	}
	
	public int updateChangeApplied(ParamMap paramMap) throws Exception {
		return update("panaver.infocommon.updateChangeApplied", paramMap.get());
	}
	
	public int selectExchangeClaimDoneCnt(String productOrderID) throws Exception {
		return (int) selectByPk("panaver.infocommon.selectExchangeClaimDoneCnt", productOrderID);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectUnappliedChangedInfo() throws Exception {
		return (List<HashMap<String, String>>) list("panaver.infocommon.selectUnappliedChangedInfo", null);
	}
	
	public int deleteUnappliedChangedInfo(ParamMap paramMap) throws Exception {
		return delete("panaver.infocommon.deleteUnappliedChangedInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectClaimShipcostInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectClaimShipcostInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExistingOngoingReturnInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectExistingOngoingReturn", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExistingOngoingExchangeInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectExistingOngoingExchange", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectPreCancelExchangeTarget(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.infocommon.selectPreCancelExchangeTarget", paramMap.get());
	}
	
	public int updatePreCancelExchange(ParamMap paramMap) throws Exception {
		return update("panaver.infocommon.updatePreCancelExchange", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaNaverOrderChangeVO> selectUnappliedOrderChange(String orderId) throws Exception {
		return (List<PaNaverOrderChangeVO>) list("panaver.infocommon.selectUnappliedOrderChange", orderId);
	}
}
