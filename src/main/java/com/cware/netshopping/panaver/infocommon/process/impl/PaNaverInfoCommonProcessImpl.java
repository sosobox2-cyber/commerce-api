package com.cware.netshopping.panaver.infocommon.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.DeliveryMethodType;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GiftReceivingStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.HoldbackStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderChangeType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaNaverAddressVO;
import com.cware.netshopping.domain.PaNaverClaimListVO;
import com.cware.netshopping.domain.PaNaverOrderChangeVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.cancel.repository.PaNaverCancelDAO;
import com.cware.netshopping.panaver.delivery.repository.PaNaverDeliveryDAO;
import com.cware.netshopping.panaver.exchange.repository.PaNaverExchangeDAO;
import com.cware.netshopping.panaver.infocommon.process.PaNaverInfoCommonProcess;
import com.cware.netshopping.panaver.infocommon.repository.PaNaverInfoCommonDAO;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;

@Service("panaver.infocommon.paNaverInfoCommonProcess")
public class PaNaverInfoCommonProcessImpl extends AbstractProcess implements PaNaverInfoCommonProcess {
	
	@Autowired
	PaNaverInfoCommonDAO paNaverInfoCommonDAO;
	
	@Autowired
	PaNaverDeliveryDAO paNaverDeliveryDAO;
	
	@Autowired
	PaNaverCancelDAO paNaverCancelDAO;
	
	@Autowired
	PaNaverExchangeDAO paNaverExchangeDAO;
	
	@Autowired
	PaCommonDAO paCommonDAO;
	
	@Autowired
	SystemDAO systemDAO;
	
	/**
	 * 변경 주문내역 적재
	 */
	@Override
	public ParamMap mergeChangeOrderListProc(GetChangedProductOrderListResponseE response, String fromDate, String toDate) throws Exception {
		
		int saveCnt = 0;
		int result = 0;
		ParamMap resultMap = new ParamMap();
		ParamMap paramMap = new ParamMap();
		boolean addedToList = false;
		List<ChangedProductOrderInfo> payedList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, canceledList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRejectList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRejectList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRejectList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeCollectedList = new ArrayList<ChangedProductOrderInfo>()
									, payWaitingList = new ArrayList<ChangedProductOrderInfo>()
									, purchaseDecidedList = new ArrayList<ChangedProductOrderInfo>();
		
		ChangedProductOrderInfo[] orders = response.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList();
		
		for(ChangedProductOrderInfo order : orders) {
			addedToList = false;
			try {
				result = paNaverInfoCommonDAO.mergeChangeOrderList(order);
				saveCnt += result;
				if(result > 0) {
					if(order.getGiftReceivingStatus() != null && (order.getGiftReceivingStatus().equals(GiftReceivingStatusType.WAIT_FOR_RECEIVING))) {
						paramMap.put("orderID", order.getOrderID());
						paramMap.put("productOrderID", order.getProductOrderID());
						paramMap.put("lastChangedStatus", order.getLastChangedStatus().getValue());
						paramMap.put("lastChangedDate", new Timestamp(order.getLastChangedDate().getTimeInMillis()));
						paNaverInfoCommonDAO.updateChangeApplied(paramMap);
						continue;
					}
					if(order.getLastChangedStatus().equals(ProductOrderChangeType.PAYED)) {
						if(order.getPaymentDate() == null) addedToList = payWaitingList.add(order);
						if(null != order.getClaimStatus()) {
							if(order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT)) addedToList = cancelRejectList.add(order);
						}
						else addedToList = payedList.add(order);
					} 
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.DISPATCHED)) {
						if(null != order.getClaimStatus()) {
							if(order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT)) addedToList = cancelRejectList.add(order);
							if(order.getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) addedToList = returnRejectList.add(order);
							if(order.getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) addedToList = exchangeRejectList.add(order);
						}
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.CANCEL_REQUESTED)) {
						addedToList = cancelRequestedList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.RETURN_REQUESTED)) {
						if(!order.getClaimStatus().equals(ClaimStatusType.COLLECTING)) addedToList = returnRequestedList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.EXCHANGE_REQUESTED)) {
						if(!order.getClaimStatus().equals(ClaimStatusType.COLLECTING)) addedToList = exchangeRequestedList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.CANCELED)) {
						addedToList = canceledList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.RETURNED)) {
						addedToList = returnedList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.EXCHANGE_REDELIVERY_READY)) {
						if(order.getClaimStatus().equals(ClaimStatusType.COLLECT_DONE)) addedToList = exchangeCollectedList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.PAY_WAITING)) {
//						addedToList = payWaitingList.add(order);
					}
					else if(order.getLastChangedStatus().equals(ProductOrderChangeType.PURCHASE_DECIDED)) {
						addedToList = purchaseDecidedList.add(order);
					}
					
					if(!addedToList) {
						paramMap.put("orderID", order.getOrderID());
						paramMap.put("productOrderID", order.getProductOrderID());
						paramMap.put("lastChangedStatus", order.getLastChangedStatus().getValue());
						paramMap.put("lastChangedDate", new Timestamp(order.getLastChangedDate().getTimeInMillis()));
						paNaverInfoCommonDAO.updateChangeApplied(paramMap);
					}
				}
				
			} catch (Exception e) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE MERGE" });
			}
		}
		if(payedList.size() > 0) resultMap.put("payedList", payedList);
		if(cancelRequestedList.size() > 0) resultMap.put("cancelRequestedList", cancelRequestedList);
		if(returnRequestedList.size() > 0) resultMap.put("returnRequestedList", returnRequestedList);
		if(exchangeRequestedList.size() > 0) resultMap.put("exchangeRequestedList", exchangeRequestedList);
		if(canceledList.size() > 0) resultMap.put("canceledList", canceledList);
		if(returnedList.size() > 0) resultMap.put("returnedList", returnedList);
		if(exchangeCollectedList.size() > 0) resultMap.put("exchangeCollectedList", exchangeCollectedList);
		if(returnRejectList.size() > 0) resultMap.put("returnRejectList", returnRejectList);
		if(exchangeRejectList.size() > 0) resultMap.put("exchangeRejectList", exchangeRejectList);
		if(cancelRejectList.size() > 0) resultMap.put("cancelRejectList", cancelRejectList);
		if(payWaitingList.size() > 0) resultMap.put("payWaitingList", payWaitingList);
		if(purchaseDecidedList.size() > 0) resultMap.put("purchaseDecidedList", purchaseDecidedList);
		resultMap.put("saveCnt", saveCnt);
		
		return resultMap;
	}

	/**
	 * 취소 내역 상세 적재
	 */
	@Override
	public List<ParamMap> insertCancelInfoProc(HttpServletRequest httpServletRequest, ProductOrderInfo[] cancelInfoList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		List<ParamMap> returnMapList = new ArrayList<ParamMap>();
		ParamMap paramMap = new ParamMap();
		PaNaverClaimListVO claim; 
		String sysdate;
		boolean preCanceled = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		for(ProductOrderInfo productOrderInfo : cancelInfoList) {
			try{
				preCanceled = false;
				if(productOrderInfo.getCancelInfo() != null) {
					if(paNaverInfoCommonDAO.selectOrderListCnt(productOrderInfo.getProductOrder().getProductOrderID()) < 1) {
						
						preCanceled = true;
						productOrderInfo.getOrder().setOrdererTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererTel1()), "UTF-8"));
						productOrderInfo.getOrder().setOrdererID(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererID()), "UTF-8"));
						productOrderInfo.getOrder().setOrdererName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererName()), "UTF-8"));
						if(paNaverDeliveryDAO.insertOrderm(productOrderInfo.getOrder()) < 1) {
							throw processException("msg.cannot_save", new String[] { "TPANAVERORDERM INSERT" });
						}
						if(paNaverDeliveryDAO.mergeOrderList(productOrderInfo.getProductOrder()) < 1) {
							throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST INSERT" });
						}
						paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
						if(null != productOrderInfo.getProductOrder().getShippingAddress()) {
							paramMap.put("shippingAddressSeq", paNaverInfoCommonDAO.selectAddressSeq());
							PaNaverAddressVO addressVO = new PaNaverAddressVO();
							if(null != productOrderInfo.getProductOrder().getShippingAddress().getBaseAddress()) productOrderInfo.getProductOrder().getShippingAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getBaseAddress()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getShippingAddress().getDetailedAddress()) productOrderInfo.getProductOrder().getShippingAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getDetailedAddress()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getShippingAddress().getTel1()) productOrderInfo.getProductOrder().getShippingAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getTel1()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getShippingAddress().getTel2()) productOrderInfo.getProductOrder().getShippingAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getTel2()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getShippingAddress().getName()) productOrderInfo.getProductOrder().getShippingAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getName()), "UTF-8"));
							addressVO.setAddressSeq(paramMap.getString("shippingAddressSeq"));
							addressVO.setAddress(productOrderInfo.getProductOrder().getShippingAddress());
							if(paNaverInfoCommonDAO.insertAddress(addressVO) < 1) {
								throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS SHIPPINGADDRESS INSERT" });
							}
						}
						if(null != productOrderInfo.getProductOrder().getTakingAddress()) {
							paramMap.put("takingAddressSeq", paNaverInfoCommonDAO.selectAddressSeq());
							PaNaverAddressVO addressVO = new PaNaverAddressVO();
							if(null != productOrderInfo.getProductOrder().getTakingAddress().getBaseAddress()) productOrderInfo.getProductOrder().getTakingAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getBaseAddress()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getTakingAddress().getDetailedAddress()) productOrderInfo.getProductOrder().getTakingAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getDetailedAddress()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getTakingAddress().getTel1()) productOrderInfo.getProductOrder().getTakingAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getTel1()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getTakingAddress().getTel2()) productOrderInfo.getProductOrder().getTakingAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getTel2()), "UTF-8"));
							if(null != productOrderInfo.getProductOrder().getTakingAddress().getName()) productOrderInfo.getProductOrder().getTakingAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getName()), "UTF-8"));
							addressVO.setAddressSeq(paramMap.getString("takingAddressSeq"));
							addressVO.setAddress(productOrderInfo.getProductOrder().getShippingAddress());
							if(paNaverInfoCommonDAO.insertAddress(addressVO) < 1) {
								throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS TAKINGADDRESS INSERT" });
							}
						}
						if(paramMap.get("shippingAddressSeq") != null || paramMap.get("takingAddressSeq") != null) {
							if(paNaverInfoCommonDAO.updateOrderListAddress(paramMap) < 0) {
								throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ADDRESS UPDATE" });
							}
						}
						paramMap.put("orderID", productOrderInfo.getOrder().getOrderID());
						if(paNaverInfoCommonDAO.updateOrderListOrderID(paramMap) < 0) {
							throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ORDERID UPDATE" });
						}
					}
					
					if(productOrderInfo.getCancelInfo().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE) && !preCanceled) {
						paramMap = new ParamMap();
						paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
						paramMap.put("cancelCompletedDate", productOrderInfo.getCancelInfo().getCancelCompletedDate());
						String claimSeq = paNaverInfoCommonDAO.selectCancelDoneClaim(paramMap);
						if(claimSeq != null) {
							paramMap.put("claimSeq", claimSeq);
							if(paNaverCancelDAO.updateCancelDoneClaim(paramMap) != 1) {
								throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST UPDATE" });
							}
							continue;
						}
					}
					claim = new PaNaverClaimListVO();
					claim.setClaimSeq(paNaverInfoCommonDAO.selectClaimSeq());
					claim.setProductOrderID(productOrderInfo.getProductOrder().getProductOrderID());
					claim.setCancelInfo(productOrderInfo.getCancelInfo());
					sysdate = systemDAO.getSysdatetime();
					claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
					claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
					if(paNaverInfoCommonDAO.insertCancelClaim(claim) == 1) {
						paramMap = new ParamMap();
						paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
						paramMap.put("claimType", "CANCEL");
						paramMap.put("claimStatus", claim.getClaimStatus());
						paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
						paramMap.put("orderID", productOrderInfo.getOrder().getOrderID());
						paramMap.put("claimID", productOrderInfo.getProductOrder().getClaimID());
						if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE)) {
							paramMap.put("procFlag", "10");
						}
						if(paNaverInfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
							throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST UPDATE" });
						}
						else {
							if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_DONE) ||
									productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE)) {
								Paorderm paorderm = new Paorderm();
								if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE) && paNaverInfoCommonDAO.selectExchangeClaimDoneCnt(productOrderInfo.getProductOrder().getProductOrderID()) > 0) {
									paorderm = new Paorderm();
									paorderm.setPaCode("41");
									paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
									paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
									paorderm.setPaClaimNo(paNaverInfoCommonDAO.selectRecentExchangeClaimID(paramMap.getString("productOrderID")));
									paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
									paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
									paorderm.setPaDoFlag("20");
									paorderm.setPaOrderGb("30");
									paorderm.setOutBefClaimGb("0");
									paorderm.setPaGroupCode("04");
									paorderm.setInsertDate(claim.getInsertDate());
									paorderm.setModifyDate(claim.getModifyDate());
									paorderm.setModifyId("PANAVER");
									
									if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
										throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
									}
									returnMapList.add(paramMap);
								}
								else {
									HashMap<String, String> cancelDoneInfo = paNaverInfoCommonDAO.selectCancelDoneInfo(productOrderInfo.getProductOrder().getProductOrderID());
									if(cancelDoneInfo == null) {
										paorderm.setPaCode("41");
										paorderm.setPaOrderGb("20");
										paorderm.setPaGroupCode("04");
										paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
										paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
										paorderm.setPaShipNo("");
										paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
										paorderm.setPaProcQty(ComUtil.objToStr(productOrderInfo.getProductOrder().getQuantity()));
										paorderm.setPaDoFlag("20");
										String doFlag = paNaverInfoCommonDAO.selectOrderdtDoFlag(paramMap);
										if(doFlag != null && Integer.parseInt(doFlag) >= 30) {
	//										if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.ADMIN_CANCEL_DONE)) {
	//											paorderm.setOutBefClaimGb("0");
	//										}
	//										else {
												paorderm.setOutBefClaimGb("1");
	//										}
											
										}
										else {
											paorderm.setOutBefClaimGb("0");
										}
										if("0".equals(paorderm.getOutBefClaimGb())){
											paorderm.setPaDoFlag	 	("20");			
										}else{
											
											paorderm.setPaDoFlag	 	("60");
										}
										paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
										paorderm.setInsertDate(claim.getInsertDate());
										paorderm.setModifyDate(claim.getModifyDate());
										paorderm.setModifyId("PANAVER");
										
										if(paNaverInfoCommonDAO.selectPaOrderMCancelCnt(paorderm) < 1) {
											if(paCommonDAO.insertPaOrderM(paorderm) != 1) {
												throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
											}
											if(preCanceled) {
												paramMap.put("paOrderGb", "20");
												paramMap.put("mappingSeq", paNaverDeliveryDAO.selectMappingSeqByProductOrderInfo(paramMap));
												paramMap.put("preCancelYn", "1");
												paramMap.put("preCancelReason", productOrderInfo.getCancelInfo().getCancelReason().toString());
												paNaverCancelDAO.updatePreCancelYn(paramMap);
											}
										}
										returnMapList.add(paramMap);
									}
								}
							}
							else if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_REQUEST)) {
								Paorderm paorderm = new Paorderm();
								paorderm.setPaCode("41");
								paorderm.setPaOrderGb("20");
								paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
								paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
								paorderm.setPaShipNo("");
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
								paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
								paorderm.setPaDoFlag("00");
								paorderm.setPaGroupCode("04");
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								String doFlag = paNaverInfoCommonDAO.selectOrderdtDoFlag(paramMap);
								
								if(doFlag != null && Integer.parseInt(doFlag) >= 30) {
									paorderm.setOutBefClaimGb("1");
								}
								else {
									paorderm.setOutBefClaimGb("0");
								}
								if("0".equals(paorderm.getOutBefClaimGb())){
									paorderm.setPaDoFlag("20");			
								}else{
									paorderm.setPaDoFlag("60");
								}
								paorderm.setInsertDate(claim.getInsertDate());
								paorderm.setModifyDate(claim.getModifyDate());
								paorderm.setModifyId("PANAVER");
								
								if(productOrderInfo.getCancelInfo().getRequestChannel().equals("API")) paorderm.setPreCancelYn("1");
								
								if(paNaverInfoCommonDAO.selectPaOrderMCancelCnt(paorderm) < 1) {
									if(paCommonDAO.insertPaOrderM(paorderm) != 1) {
										throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
									}
								}
								else if(productOrderInfo.getProductOrder().getClaimStatus().equals(ClaimStatusType.CANCEL_REQUEST)) {
									paramMap.put("paOrderGb", "20");
									if(paNaverInfoCommonDAO.updatePaordermClaimID(paramMap) != 1) {
										throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
									}
								}
								
								if(paorderm.getOutBefClaimGb().equals("0")) returnMapList.add(paramMap);
							}
						}
					}
					else {
						throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
					}
				}
				
				paramMap.put("lastChangedStatus", changedProductOrderInfo.getLastChangedStatus().getValue());
				paramMap.put("lastChangedDate", new Timestamp(changedProductOrderInfo.getLastChangedDate().getTimeInMillis()));
				if(paNaverInfoCommonDAO.updateChangeApplied(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
				}
			}catch (Exception e) {
				log.error("message", e);
			}
		} 
		
		return returnMapList;
	}

	@Override
	public List<ParamMap> insertExchangeInfo(HttpServletRequest httpServletRequest, ProductOrderInfo[] exchangeList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		List<ParamMap> returnMap = new ArrayList<ParamMap>();
		ParamMap paramMap = null;
		PaNaverClaimListVO claim; 
		String sysdate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		PaNaverAddressVO address;
		Paorderm paorderm = null;
		for(ProductOrderInfo productOrderInfo : exchangeList) {
			try{
				paramMap = new ParamMap();
				claim = new PaNaverClaimListVO();
				claim.setClaimSeq(paNaverInfoCommonDAO.selectClaimSeq());
				claim.setProductOrderID(productOrderInfo.getProductOrder().getProductOrderID());
				claim.setExchangeInfo(productOrderInfo.getExchangeInfo());
				if(productOrderInfo.getExchangeInfo().getCollectAddress() != null) {
					address = new PaNaverAddressVO();
					if(null != productOrderInfo.getExchangeInfo().getCollectAddress().getBaseAddress()) productOrderInfo.getExchangeInfo().getCollectAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getCollectAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getCollectAddress().getDetailedAddress()) productOrderInfo.getExchangeInfo().getCollectAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getCollectAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getCollectAddress().getTel1()) productOrderInfo.getExchangeInfo().getCollectAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getCollectAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getCollectAddress().getTel2()) productOrderInfo.getExchangeInfo().getCollectAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getCollectAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getCollectAddress().getName()) productOrderInfo.getExchangeInfo().getCollectAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getCollectAddress().getName()), "UTF-8"));
					address.setAddress(productOrderInfo.getExchangeInfo().getCollectAddress());
					address.setAddressSeq(paNaverInfoCommonDAO.selectAddressSeq());
					if(paNaverInfoCommonDAO.insertAddress(address) == 1) claim.setCollectAddressSeq(address.getAddressSeq());;
				}
				if(productOrderInfo.getExchangeInfo().getReturnReceiveAddress() != null) {
					address = new PaNaverAddressVO();
					if(null != productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getBaseAddress()) productOrderInfo.getExchangeInfo().getReturnReceiveAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getDetailedAddress()) productOrderInfo.getExchangeInfo().getReturnReceiveAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getTel1()) productOrderInfo.getExchangeInfo().getReturnReceiveAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getTel2()) productOrderInfo.getExchangeInfo().getReturnReceiveAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getName()) productOrderInfo.getExchangeInfo().getReturnReceiveAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getExchangeInfo().getReturnReceiveAddress().getName()), "UTF-8"));
					address.setAddress(productOrderInfo.getExchangeInfo().getReturnReceiveAddress());
					address.setAddressSeq(paNaverInfoCommonDAO.selectAddressSeq());
					if(paNaverInfoCommonDAO.insertAddress(address) == 1) claim.setReturnReceiveAddress(address.getAddressSeq());
				}
				sysdate = systemDAO.getSysdatetime();
				claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
				claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
				if(paNaverInfoCommonDAO.insertExchangeClaim(claim) == 1) {
					paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
					paramMap.put("claimType", "EXCHANGE");
					paramMap.put("claimStatus", claim.getClaimStatus());
					paramMap.put("orderID", productOrderInfo.getOrder().getOrderID());
					paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
					paramMap.put("claimID", productOrderInfo.getProductOrder().getClaimID());
					if(paNaverInfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST EXCHANGEINFO UPDATE" });
					}
					else {
						
						if(productOrderInfo.getExchangeInfo().getHoldbackStatus() != null && productOrderInfo.getExchangeInfo().getHoldbackStatus().equals(HoldbackStatusType.HOLDBACK)) {
							continue;
						}
						
						HashMap<String, String> preCancelTargetInfo = paNaverInfoCommonDAO.selectPreCancelExchangeTarget(paramMap);
						
						if(preCancelTargetInfo != null) {
							paNaverInfoCommonDAO.updatePreCancelExchange(paramMap);
						}
						
						List<HashMap<String, String>> goodsInfo = paNaverExchangeDAO.selectGoodsdtInfo(productOrderInfo.getProductOrder().getProductID());
						String collectDoFlag = paNaverInfoCommonDAO.selectExchangeCollectDoFlag(paramMap);
						if(collectDoFlag == null && !productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) {
							paorderm = new Paorderm();
							if(goodsInfo.size() == 1) {
								paorderm.setChangeGoodsdtCode(goodsInfo.get(0).get("GOODSDT_CODE"));
								paorderm.setChangeFlag("01");
							}
							else {
								paorderm.setChangeGoodsdtCode("");
								paorderm.setChangeFlag("00");
							}
							paorderm.setChangeGoodsCode(goodsInfo.get(0).get("GOODS_CODE"));
							paorderm.setPaCode("41");
							
							paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
							paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
							paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
							paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
							paorderm.setPaGroupCode("04");
							paorderm.setOutBefClaimGb("0");
							paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
							paorderm.setInsertDate(claim.getInsertDate());
							paorderm.setModifyDate(claim.getModifyDate());
							paorderm.setModifyId("PANAVER");
							
							if(productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.COLLECT_DONE)) {
								paorderm.setPaDoFlag("60");
							}
							else if(productOrderInfo.getExchangeInfo().getCollectDeliveryMethod() != null &&
									productOrderInfo.getExchangeInfo().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL)) {
								paorderm.setPaDoFlag("50");
							}
							else {
								paorderm.setPaDoFlag("20");
							}
							
							if(productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) {
								paorderm.setPaOrderGb("46");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
							}
							else paorderm.setPaOrderGb("45");
							
							if(paCommonDAO.insertPaOrderM(paorderm) < 0) {
								throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
							}
							
							if(productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) {
								paorderm.setPaOrderGb("41");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
							}
							else {
								paorderm.setPaDoFlag("20");
								paorderm.setPaOrderGb("40");
							}
							
							if(paCommonDAO.insertPaOrderM(paorderm) < 0) {
								throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
							}
							
							returnMap.add(paramMap);
						}
						else if((collectDoFlag != null && collectDoFlag.equals("20")) || (collectDoFlag == null && productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT))) {
							if(productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) {
								paorderm = new Paorderm();
								if(goodsInfo.size() == 1) {
									paorderm.setChangeGoodsdtCode(goodsInfo.get(0).get("GOODSDT_CODE"));
									paorderm.setChangeFlag("01");
								}
								else {
									paorderm.setChangeGoodsdtCode("");
									paorderm.setChangeFlag("00");
								}
								paorderm.setChangeGoodsCode(goodsInfo.get(0).get("GOODS_CODE"));
								paorderm.setPaCode("41");
								
								paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
								paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
								paorderm.setPaClaimNo(paNaverInfoCommonDAO.selectRecentExchangeClaimID(paramMap.getString("productOrderID")));
								paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
								paorderm.setPaGroupCode("04");
								paorderm.setOutBefClaimGb("0");
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								paorderm.setInsertDate(claim.getInsertDate());
								paorderm.setModifyDate(claim.getModifyDate());
								paorderm.setModifyId("PANAVER");
								
								if(productOrderInfo.getExchangeInfo().getRequestChannel().equals("API")) paorderm.setPreCancelYn("1");
								
								paorderm.setPaOrderGb("46");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
								
								if(paCommonDAO.insertPaOrderM(paorderm) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
								
								paorderm.setPaOrderGb("41");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
								
								if(paCommonDAO.insertPaOrderM(paorderm) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
								
								returnMap.add(paramMap);
							}
							else if(productOrderInfo.getExchangeInfo().getClaimStatus().equals(ClaimStatusType.EXCHANGE_REQUEST)) {
								paorderm = new Paorderm();
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
								paorderm.setOutBefClaimGb("0");
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								
								if(productOrderInfo.getExchangeInfo().getCollectDeliveryMethod() != null &&
								   productOrderInfo.getExchangeInfo().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL)) {
									paorderm.setPaDoFlag("50");
								}
								else {
									paorderm.setPaDoFlag("20");
								}
								
								paramMap.put("paOrderGb", "45");
								if(paNaverInfoCommonDAO.updatePaordermClaimID(paramMap) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
								}
								
								paramMap.put("paOrderGb", "40");
								
								if(paNaverInfoCommonDAO.updatePaordermClaimID(paramMap) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
								}
								
								returnMap.add(paramMap);
							}
						}
					}
				}
				else {
					throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
				}
				
				paramMap.put("lastChangedStatus", changedProductOrderInfo.getLastChangedStatus().getValue());
				paramMap.put("lastChangedDate", new Timestamp(changedProductOrderInfo.getLastChangedDate().getTimeInMillis()));
				if(paNaverInfoCommonDAO.updateChangeApplied(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
				}
			}catch (Exception e) {
				log.error("message", e);
			}
		}
		
		return returnMap;
	}

	@Override
	public List<ParamMap> insertReturnInfo(HttpServletRequest httpServletRequest, ProductOrderInfo[] returnList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		List<ParamMap> returnMap = new ArrayList<ParamMap>();
		ParamMap paramMap = null;
		PaNaverClaimListVO claim; 
		String sysdate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		PaNaverAddressVO address;
		Paorderm paorderm = null;
		
		for(ProductOrderInfo productOrderInfo : returnList) {
			try{
				paramMap = new ParamMap();
				claim = new PaNaverClaimListVO();
				claim.setClaimSeq(paNaverInfoCommonDAO.selectClaimSeq());
				claim.setProductOrderID(productOrderInfo.getProductOrder().getProductOrderID());
				claim.setReturnInfo(productOrderInfo.getReturnInfo());
				if(productOrderInfo.getReturnInfo().getCollectAddress() != null) {
					address = new PaNaverAddressVO();
					if(null != productOrderInfo.getReturnInfo().getCollectAddress().getBaseAddress()) productOrderInfo.getReturnInfo().getCollectAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getCollectAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getCollectAddress().getDetailedAddress()) productOrderInfo.getReturnInfo().getCollectAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getCollectAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getCollectAddress().getTel1()) productOrderInfo.getReturnInfo().getCollectAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getCollectAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getCollectAddress().getTel2()) productOrderInfo.getReturnInfo().getCollectAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getCollectAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getCollectAddress().getName()) productOrderInfo.getReturnInfo().getCollectAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getCollectAddress().getName()), "UTF-8"));
					address.setAddress(productOrderInfo.getReturnInfo().getCollectAddress());
					address.setAddressSeq(paNaverInfoCommonDAO.selectAddressSeq());
					if(paNaverInfoCommonDAO.insertAddress(address) == 1) claim.setCollectAddressSeq(address.getAddressSeq());
				}
				if(productOrderInfo.getReturnInfo().getReturnReceiveAddress() != null) {
					address = new PaNaverAddressVO();
					if(null != productOrderInfo.getReturnInfo().getReturnReceiveAddress().getBaseAddress()) productOrderInfo.getReturnInfo().getReturnReceiveAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getReturnReceiveAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getReturnReceiveAddress().getDetailedAddress()) productOrderInfo.getReturnInfo().getReturnReceiveAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getReturnReceiveAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getReturnReceiveAddress().getTel1()) productOrderInfo.getReturnInfo().getReturnReceiveAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getReturnReceiveAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getReturnReceiveAddress().getTel2()) productOrderInfo.getReturnInfo().getReturnReceiveAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getReturnReceiveAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getReturnInfo().getReturnReceiveAddress().getName()) productOrderInfo.getReturnInfo().getReturnReceiveAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getReturnInfo().getReturnReceiveAddress().getName()), "UTF-8"));
					address.setAddress(productOrderInfo.getReturnInfo().getReturnReceiveAddress());
					address.setAddressSeq(paNaverInfoCommonDAO.selectAddressSeq());
					if(paNaverInfoCommonDAO.insertAddress(address) == 1) claim.setReturnReceiveAddress(address.getAddressSeq());
				}
				sysdate = systemDAO.getSysdatetime();
				claim.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
				claim.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
				if(paNaverInfoCommonDAO.insertReturnClaim(claim) == 1) {
					paramMap.put("claimSeq", String.valueOf(claim.getClaimSeq()));
					paramMap.put("claimType", "RETURN");
					paramMap.put("claimStatus", claim.getClaimStatus());
					paramMap.put("orderID", productOrderInfo.getOrder().getOrderID());
					paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
					paramMap.put("claimID", productOrderInfo.getProductOrder().getClaimID());				
					if(paNaverInfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST RETURNINFO UPDATE" });
					}
					else {
						String collectDoFlag = paNaverInfoCommonDAO.selectReturnCollectDoFlag(paramMap);
						if(collectDoFlag == null ) {  //|| (collectDoFlag.equals("20") && productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REQUEST))
							
							paorderm = new Paorderm();
							paorderm.setPaCode("41");
							paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
							paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
							if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) {
								paorderm.setPaClaimNo(paNaverInfoCommonDAO.selectRecentReturnClaimID(paramMap.getString("productOrderID")));
							} else {
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
							}
							paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
							paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
							
							
							if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_DONE)) {
								paorderm.setPaDoFlag("60");
							} else {
								if(productOrderInfo.getReturnInfo().getCollectDeliveryMethod() != null && 
										productOrderInfo.getReturnInfo().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL)) {
									paorderm.setPaDoFlag("50");
									paorderm.setPaShipNo("직접 발송");
								} 
								else {
									paorderm.setPaDoFlag("20");
								}
							}
							
							if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) {
								paorderm.setPaOrderGb("31");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
							}
							else paorderm.setPaOrderGb("30");
							
							paorderm.setOutBefClaimGb("0");
							paorderm.setPaGroupCode("04");
							paorderm.setInsertDate(claim.getInsertDate());
							paorderm.setModifyDate(claim.getModifyDate());
							paorderm.setModifyId("PANAVER");
							
							if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
								throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
							}
							returnMap.add(paramMap);
						}
						else if(collectDoFlag.equals("20") || collectDoFlag.equals("50")) {
							if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_DONE)) {
								paramMap.put("procFlag", "60");
								if (paNaverInfoCommonDAO.updateProcFlag(paramMap) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
								}
								returnMap.add(paramMap);
							}
							else if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) {
								paorderm = new Paorderm();
								paorderm.setPaCode("41");
								paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
								paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
								paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								
								paorderm.setPaOrderGb("31");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
								
								paorderm.setOutBefClaimGb("0");
								paorderm.setPaGroupCode("04");
								paorderm.setInsertDate(claim.getInsertDate());
								paorderm.setModifyDate(claim.getModifyDate());
								paorderm.setModifyId("PANAVER");
								
								if(productOrderInfo.getReturnInfo().getRequestChannel().equals("API")) paorderm.setPreCancelYn("1");
								
								if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
								returnMap.add(paramMap);
							}/*
							else if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REQUEST)) {
								paorderm = new Paorderm();
								paorderm.setPaCode("41");
								paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
								paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
								paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								
								if(productOrderInfo.getReturnInfo().getCollectDeliveryMethod() != null && 
										productOrderInfo.getReturnInfo().getCollectDeliveryMethod().equals(DeliveryMethodType.RETURN_INDIVIDUAL)) {
									paorderm.setPaDoFlag("50");
									paorderm.setPaShipNo("직접 발송");
								}
								else {
									paorderm.setPaDoFlag("20");
								}
								
								paorderm.setPaOrderGb("30");
								
								paorderm.setOutBefClaimGb("0");
								paorderm.setPaGroupCode("04");
								paorderm.setInsertDate(claim.getInsertDate());
								paorderm.setModifyDate(claim.getModifyDate());
								paorderm.setModifyId("PANAVER");
								
								if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
								returnMap.add(paramMap);
							}*/
						}
						else if(productOrderInfo.getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) {
							if(collectDoFlag != null) {
								paorderm = new Paorderm();
								paorderm.setPaCode("41");
								paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
								paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
								paorderm.setPaClaimNo(productOrderInfo.getProductOrder().getClaimID());
								paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
								paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
								
								paorderm.setPaOrderGb("31");
								paorderm.setPaDoFlag(null);
								paorderm.setProcDate(null);
								
								paorderm.setOutBefClaimGb("0");
								paorderm.setPaGroupCode("04");
								paorderm.setInsertDate(claim.getInsertDate());
								paorderm.setModifyDate(claim.getModifyDate());
								paorderm.setModifyId("PANAVER");
								
								if(productOrderInfo.getReturnInfo().getRequestChannel().equals("API")) paorderm.setPreCancelYn("1");
								
								if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
									throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
								}
								returnMap.add(paramMap);
							}
						}
					}
				}
				else {
					throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
				}
				
				paramMap.put("lastChangedStatus", changedProductOrderInfo.getLastChangedStatus().getValue());
				paramMap.put("lastChangedDate", new Timestamp(changedProductOrderInfo.getLastChangedDate().getTimeInMillis()));
				if(paNaverInfoCommonDAO.updateChangeApplied(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
				}
			}catch (Exception e) {
				log.error("message", e);
			}
		}
		
		return returnMap;
	}
	
	@Override
	public int checkExistingReturnClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception {
		ParamMap paramMap = param;
		paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
		paramMap.put("claimType", productOrderInfo.getProductOrder().getClaimType().getValue());
		return paNaverInfoCommonDAO.checkExistingClaim(paramMap);
	}
	
	@Override
	public int checkExistingExchangeClaim(HttpServletRequest httpServletRequest, ProductOrderInfo productOrderInfo, ParamMap param) throws Exception {
		ParamMap paramMap = param;
		paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
		paramMap.put("claimType", productOrderInfo.getProductOrder().getClaimType().getValue());
		return paNaverInfoCommonDAO.checkExistingClaim(paramMap);
	}

	@Override
	public List<Object> selectOrderInputTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectOrderInputTargetList();
	}

	@Override
	public List<Object> selectCancelInputTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectCancelInputTargetList();
	}
	
	@Override
	public List<Object> selectOrderClaimTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectOrderClaimTargetList();
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectClaimCancelTargetList();
	}
	
	@Override
	public List<Object> selectOrderChangeTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectOrderChangeTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectChangeCancelTargetList();
	}
	
	@Override
	public String selectRecentReturnClaimID(String productOrderID) throws Exception {
		return paNaverInfoCommonDAO.selectRecentReturnClaimID(productOrderID);
	}
	
	@Override
	public String selectRecentExchangeClaimID(String productOrderID) throws Exception {
		return paNaverInfoCommonDAO.selectRecentExchangeClaimID(productOrderID);
	}
	
	@Override
	public List<ParamMap> insertPayWaitingInfo(HttpServletRequest httpServletRequest, ProductOrderInfo[] payWaitingList, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		List<ParamMap> returnMap = new ArrayList<ParamMap>();
		ParamMap paramMap = null;
		String sysdate = systemDAO.getSysdatetime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Paorderm paorderm = null;
		
		for(ProductOrderInfo productOrderInfo : payWaitingList) {
			try{
				paramMap = new ParamMap();
				productOrderInfo.getOrder().setOrdererTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererTel1()), "UTF-8"));
				productOrderInfo.getOrder().setOrdererID(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererID()), "UTF-8"));
				productOrderInfo.getOrder().setOrdererName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(),productOrderInfo.getOrder().getOrdererName()), "UTF-8"));
				if(paNaverDeliveryDAO.insertOrderm(productOrderInfo.getOrder()) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERM INSERT" });
				}
				if(paNaverDeliveryDAO.mergeOrderList(productOrderInfo.getProductOrder()) < 1) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST INSERT" });
				}
				paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
				if(null != productOrderInfo.getProductOrder().getShippingAddress()) {
					paramMap.put("shippingAddressSeq", paNaverInfoCommonDAO.selectAddressSeq());
					PaNaverAddressVO addressVO = new PaNaverAddressVO();
					if(null != productOrderInfo.getProductOrder().getShippingAddress().getBaseAddress()) productOrderInfo.getProductOrder().getShippingAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getShippingAddress().getDetailedAddress()) productOrderInfo.getProductOrder().getShippingAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getShippingAddress().getTel1()) productOrderInfo.getProductOrder().getShippingAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getShippingAddress().getTel2()) productOrderInfo.getProductOrder().getShippingAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getShippingAddress().getName()) productOrderInfo.getProductOrder().getShippingAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getShippingAddress().getName()), "UTF-8"));
					addressVO.setAddressSeq(paramMap.getString("shippingAddressSeq"));
					addressVO.setAddress(productOrderInfo.getProductOrder().getShippingAddress());
					if(paNaverInfoCommonDAO.insertAddress(addressVO) < 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS SHIPPINGADDRESS INSERT" });
					}
				}
				if(null != productOrderInfo.getProductOrder().getTakingAddress()) {
					paramMap.put("takingAddressSeq", paNaverInfoCommonDAO.selectAddressSeq());
					PaNaverAddressVO addressVO = new PaNaverAddressVO();
					if(null != productOrderInfo.getProductOrder().getTakingAddress().getBaseAddress()) productOrderInfo.getProductOrder().getTakingAddress().setBaseAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getBaseAddress()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getTakingAddress().getDetailedAddress()) productOrderInfo.getProductOrder().getTakingAddress().setDetailedAddress(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getDetailedAddress()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getTakingAddress().getTel1()) productOrderInfo.getProductOrder().getTakingAddress().setTel1(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getTel1()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getTakingAddress().getTel2()) productOrderInfo.getProductOrder().getTakingAddress().setTel2(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getTel2()), "UTF-8"));
					if(null != productOrderInfo.getProductOrder().getTakingAddress().getName()) productOrderInfo.getProductOrder().getTakingAddress().setName(new String(SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), productOrderInfo.getProductOrder().getTakingAddress().getName()), "UTF-8"));
					addressVO.setAddressSeq(paramMap.getString("takingAddressSeq"));
					addressVO.setAddress(productOrderInfo.getProductOrder().getShippingAddress());
					if(paNaverInfoCommonDAO.insertAddress(addressVO) < 1) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERADDRESS TAKINGADDRESS INSERT" });
					}
				}
				if(paramMap.get("shippingAddressSeq") != null || paramMap.get("takingAddressSeq") != null) {
					if(paNaverInfoCommonDAO.updateOrderListAddress(paramMap) < 0) {
						throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ADDRESS UPDATE" });
					}
				}
				paramMap.put("orderID", productOrderInfo.getOrder().getOrderID());
				if(paNaverInfoCommonDAO.updateOrderListOrderID(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ORDERID UPDATE" });
				}
				
				if(paNaverInfoCommonDAO.selectWaitingPaordermCnt(paramMap) < 1) {
					paorderm = new Paorderm();
					paorderm.setPaCode("41");
					paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
					paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
					paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
					paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
					
					paorderm.setPaOrderGb("10");
					paorderm.setPaDoFlag("10");
					
					paorderm.setOutBefClaimGb("0");
					paorderm.setPaGroupCode("04");
					paorderm.setInsertDate(new Timestamp(sdf.parse(sysdate).getTime()));
					paorderm.setModifyDate(new Timestamp(sdf.parse(sysdate).getTime()));
					paorderm.setModifyId("PANAVER");
					
					if (paCommonDAO.insertPaOrderM(paorderm) < 0) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
					}
					returnMap.add(paramMap);
				}
				
				paramMap.put("lastChangedStatus", changedProductOrderInfo.getLastChangedStatus().getValue());
				paramMap.put("lastChangedDate", new Timestamp(changedProductOrderInfo.getLastChangedDate().getTimeInMillis()));
				if(paNaverInfoCommonDAO.updateChangeApplied(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
				}
			}catch (Exception e) {
				log.error("message", e);
			}
		}
		
		return returnMap;
	}
	
	@Override
	public List<Object> selectPreOrderInputTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectPreOrderInputTargetList();
	}

	@Override
	public List<Object> selectPreOrderUpdateTargetList() throws Exception {
		return paNaverInfoCommonDAO.selectPreOrderUpdateTargetList();
	}
	
	@Override
	public int selectUnAttendedCount(String orderID) throws Exception {
		return paNaverInfoCommonDAO.selectUnAttendedCount(orderID);
	}
	
	@Override
	public double selectPaOrderShipCost(String paOrderNo) throws Exception {
		return paNaverInfoCommonDAO.selectPaOrderShipCost(paOrderNo);
	}
	
	@Override
	public HashMap<String, Object> selectPaAddrInfo(String paOrderNo) throws Exception {
		return paNaverInfoCommonDAO.selectPaAddrInfo(paOrderNo);
	}
	
	@Override
	public List<HashMap<String ,Object>> selectNotTakenPresentList() throws Exception {
		return paNaverInfoCommonDAO.selectNotTakenPresentList();
	}

	@Override
	public int saveCancelNotTakenPresent(HashMap<String, Object> cancelMap) throws Exception {
		
		int executedRtn = 0;
//		int checkPaGmktCancel = 0;
//		int checkOrgOrderCnt  = 0;
//		ParamMap paramMap = new ParamMap();
//		paramMap.setParamMap(cancelMap);
//		
//		checkOrgOrderCnt = paNaverCancelDAO.checkExistOrgOrder(paramMap);
//		if (checkOrgOrderCnt < 1) return 0;
//		
//		checkPaGmktCancel = paNaverCancelDAO.checkCancelDoneList(paramMap);
//		if (checkPaGmktCancel > 0) return 0;
//
//		executedRtn = PaGmktCancelDAO.insertPaGmktCancelList(paGmktCancel);
//			
//		if(executedRtn != 1){
//			log.info("pagmkt tpagmktorderlist insert fail");
//			log.info("PayNo : " + paGmktCancel.getPayNo() + ", ContrNo : " + paGmktCancel.getContrNo());
//			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST INSERT" });
//		}
//				
//			//= 3) InsertPaOrderM
//		executedRtn = insertPaOrderMForComplete(paGmktCancel);
//		
//		if(executedRtn != 1){
//			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
//		}
		return executedRtn; 
	}
	
	@Override
	public List<HashMap<String, String>> selectUnappliedChangedInfo() throws Exception {
		return paNaverInfoCommonDAO.selectUnappliedChangedInfo();
	}
	
	@Override
	public int deleteUnappliedChangedInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonDAO.deleteUnappliedChangedInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectClaimShipcostInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonDAO.selectClaimShipcostInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectExistingOngoingReturnInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonDAO.selectExistingOngoingReturnInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectExistingOngoingExchangeInfo(ParamMap paramMap) throws Exception {
		return paNaverInfoCommonDAO.selectExistingOngoingExchangeInfo(paramMap);
	}
	
	@Override
	public ParamMap selectUnappliedOrderChange(String orderId) throws Exception {
		ParamMap paramMap = new ParamMap();
		ParamMap resultMap = new ParamMap();
		int saveCnt = 0;
		
		boolean addedToList = false;
		List<ChangedProductOrderInfo> payedList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, canceledList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRequestedList = new ArrayList<ChangedProductOrderInfo>()
									, returnedList = new ArrayList<ChangedProductOrderInfo>()
									, returnRejectList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeRejectList = new ArrayList<ChangedProductOrderInfo>()
									, cancelRejectList = new ArrayList<ChangedProductOrderInfo>()
									, exchangeCollectedList = new ArrayList<ChangedProductOrderInfo>()
									, payWaitingList = new ArrayList<ChangedProductOrderInfo>()
									, purchaseDecidedList = new ArrayList<ChangedProductOrderInfo>();
		
		List<PaNaverOrderChangeVO> paNaverOrderChangeList = paNaverInfoCommonDAO.selectUnappliedOrderChange(orderId);
		
		for(PaNaverOrderChangeVO orderChange : paNaverOrderChangeList) {
			addedToList = false;
			ChangedProductOrderInfo order = convertVOtoType(orderChange);
			if(order.getGiftReceivingStatus() != null && (order.getGiftReceivingStatus().equals(GiftReceivingStatusType.WAIT_FOR_RECEIVING))) {
				paramMap.put("orderID", order.getOrderID());
				paramMap.put("productOrderID", order.getProductOrderID());
				paramMap.put("lastChangedStatus", order.getLastChangedStatus());
				paramMap.put("lastChangedDate", new Timestamp(order.getLastChangedDate().getTimeInMillis()));
				paNaverInfoCommonDAO.updateChangeApplied(paramMap);
				continue;
			}
			if(order.getLastChangedStatus().equals(ProductOrderChangeType.PAYED)) {
				if(order.getPaymentDate() == null) addedToList = payWaitingList.add(order);
				if(null != order.getClaimStatus()) {
					if(order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT)) addedToList = cancelRejectList.add(order);
				}
				else addedToList = payedList.add(order);
			} 
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.DISPATCHED)) {
				if(null != order.getClaimStatus()) {
					if(order.getClaimStatus().equals(ClaimStatusType.CANCEL_REJECT)) addedToList = cancelRejectList.add(order);
					if(order.getClaimStatus().equals(ClaimStatusType.RETURN_REJECT)) addedToList = returnRejectList.add(order);
					if(order.getClaimStatus().equals(ClaimStatusType.EXCHANGE_REJECT)) addedToList = exchangeRejectList.add(order);
				}
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.CANCEL_REQUESTED)) {
				addedToList = cancelRequestedList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.RETURN_REQUESTED)) {
				if(!order.getClaimStatus().equals(ClaimStatusType.COLLECTING)) addedToList = returnRequestedList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.EXCHANGE_REQUESTED)) {
				if(!order.getClaimStatus().equals(ClaimStatusType.COLLECTING)) addedToList = exchangeRequestedList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.CANCELED)) {
				addedToList = canceledList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.RETURNED)) {
				addedToList = returnedList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.EXCHANGE_REDELIVERY_READY)) {
				if(order.getClaimStatus().equals(ClaimStatusType.COLLECT_DONE)) addedToList = exchangeCollectedList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.PAY_WAITING)) {
				//addedToList = payWaitingList.add(order);
			}
			else if(order.getLastChangedStatus().equals(ProductOrderChangeType.PURCHASE_DECIDED)) {
				addedToList = purchaseDecidedList.add(order);
			}
			
			if(!addedToList) {
				paramMap.put("orderID", order.getOrderID());
				paramMap.put("productOrderID", order.getProductOrderID());
				paramMap.put("lastChangedStatus", order.getLastChangedStatus().getValue());
				paramMap.put("lastChangedDate", new Timestamp(order.getLastChangedDate().getTimeInMillis()));
				paNaverInfoCommonDAO.updateChangeApplied(paramMap);
			}
			else saveCnt++;
		}
		
		if(payedList.size() > 0) resultMap.put("payedList", payedList);
		if(cancelRequestedList.size() > 0) resultMap.put("cancelRequestedList", cancelRequestedList);
		if(returnRequestedList.size() > 0) resultMap.put("returnRequestedList", returnRequestedList);
		if(exchangeRequestedList.size() > 0) resultMap.put("exchangeRequestedList", exchangeRequestedList);
		if(canceledList.size() > 0) resultMap.put("canceledList", canceledList);
		if(returnedList.size() > 0) resultMap.put("returnedList", returnedList);
		if(exchangeCollectedList.size() > 0) resultMap.put("exchangeCollectedList", exchangeCollectedList);
		if(returnRejectList.size() > 0) resultMap.put("returnRejectList", returnRejectList);
		if(exchangeRejectList.size() > 0) resultMap.put("exchangeRejectList", exchangeRejectList);
		if(cancelRejectList.size() > 0) resultMap.put("cancelRejectList", cancelRejectList);
		if(payWaitingList.size() > 0) resultMap.put("payWaitingList", payWaitingList);
		if(purchaseDecidedList.size() > 0) resultMap.put("purchaseDecidedList", purchaseDecidedList);
		resultMap.put("saveCnt", saveCnt);
		
		return resultMap;
	}
	
	public ChangedProductOrderInfo convertVOtoType(PaNaverOrderChangeVO vo) {
		ChangedProductOrderInfo type = new ChangedProductOrderInfo();
		type.setOrderID(vo.getOrderId());
		type.setProductOrderID(vo.getProductOrderId());
		type.setLastChangedStatus(vo.getLastChangedStatus());
		type.setLastChangedDate(vo.getLastChangedDate());
		type.setPaymentDate(vo.getPaymentDate());
		type.setClaimType(vo.getClaimType());
		type.setClaimStatus(vo.getClaimStatus());
		type.setProductOrderStatus(vo.getProductOrderStatus());
		type.setGiftReceivingStatus(vo.getGiftReceivingStatus());
		type.setIsReceiverAddressChanged(vo.getIsReceiverAddressChanged());
		return type;
	}

}
