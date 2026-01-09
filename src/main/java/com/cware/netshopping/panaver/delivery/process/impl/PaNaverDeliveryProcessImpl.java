package com.cware.netshopping.panaver.delivery.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.GiftReceivingStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaNaverAddressVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.panaver.delivery.process.PaNaverDeliveryProcess;
import com.cware.netshopping.panaver.delivery.repository.PaNaverDeliveryDAO;
import com.cware.netshopping.panaver.infocommon.repository.PaNaverInfoCommonDAO;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;

@Service("panaver.delivery.paNaverDeliveryProcess")
public class PaNaverDeliveryProcessImpl extends AbstractProcess implements PaNaverDeliveryProcess {
	
	@Autowired
	private PaNaverDeliveryDAO paNaverDeliveryDAO;
	
	@Autowired
	private PaNaverInfoCommonDAO paNaverInfoCommonDAO;
	
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private SystemDAO systemDAO;
	
	/**
	 * 발주 처리 결과 저장
	 */
	@Override
	public int updateOrderChangePlaceOrder(ProductOrderInfo productOrderInfo, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception {
		int resultCnt = 0;
		ParamMap paramMap = new ParamMap();
		
		if(paNaverDeliveryDAO.updateOrderChangePlaceOrder(productOrderInfo.getProductOrder().getProductOrderID()) > 0) {
			if(productOrderInfo.getOrder() != null) {
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
					addressVO.setAddress(productOrderInfo.getProductOrder().getTakingAddress());
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
				paramMap.put("quantity", productOrderInfo.getProductOrder().getQuantity());
				paramMap.put("productOrderID", productOrderInfo.getProductOrder().getProductOrderID());
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String sysdate = systemDAO.getSysdatetime();
				Paorderm paorderm = new Paorderm();
				paorderm.setPaOrderGb("10");
				paorderm.setPaCode("41");
				paorderm.setPaOrderNo(productOrderInfo.getOrder().getOrderID());
				paorderm.setPaOrderSeq(productOrderInfo.getProductOrder().getProductOrderID());
				paorderm.setPaShipNo("");
				paorderm.setPaProcQty(String.valueOf(productOrderInfo.getProductOrder().getQuantity()));
				if(productOrderInfo.getProductOrder().getGiftReceivingStatus() == null) {
					paorderm.setPaDoFlag("30");
				}
				else {
					if(productOrderInfo.getProductOrder().getGiftReceivingStatus().equals(GiftReceivingStatusType.WAIT_FOR_RECEIVING)) paorderm.setPaDoFlag("10");
					else paorderm.setPaDoFlag("30");
				}
				paorderm.setOutBefClaimGb("0");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				paorderm.setInsertDate(new Timestamp(dateFormat.parse(sysdate).getTime()));
				paorderm.setModifyDate(new Timestamp(dateFormat.parse(sysdate).getTime()));
				paorderm.setPaGroupCode("04");
				paorderm.setModifyId("PANAVER");
				paorderm.setApiResultCode("000000");
				paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
				
				if(paNaverInfoCommonDAO.selectWaitingPaordermCnt(paramMap) < 1) {
					if(paNaverDeliveryDAO.selectNotPlacedAndPayedOrder(productOrderInfo.getProductOrder().getProductOrderID()) == null) {
						resultCnt = paCommonDAO.insertPaOrderM(paorderm);
					}
				}
				
				if(paNaverInfoCommonDAO.updatePaOrdermOrderPlace(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPAORDERM PA_PROC_QTY UPDATE" });
				}
				if(paNaverInfoCommonDAO.updateOrderListOrderID(paramMap) < 0) {
					throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST ORDER_ID UPDATE" });
				}
				
			}
			
			paramMap.put("lastChangedStatus", changedProductOrderInfo.getLastChangedStatus().getValue());
			paramMap.put("lastChangedDate", new Timestamp(changedProductOrderInfo.getLastChangedDate().getTimeInMillis()));
			if(paNaverInfoCommonDAO.updateChangeApplied(paramMap) < 0) {
				throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE CHANGE_APPLIED UPDATE" });
			}
			
			return resultCnt;
		}
		else {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERCHANGE UPDATE" });
		}
	}
	
	@Override
	public HashMap<String, Object> selectNotPlacedAndPayedOrder(String productOrderID) throws Exception {
		return paNaverDeliveryDAO.selectNotPlacedAndPayedOrder(productOrderID);
	}
	
	@Override
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderID) throws Exception {
		return paNaverDeliveryDAO.selectOrderInputTargetDtList(orderID);
	}
	
	/**
	 * 배송지 주소 정보 조회
	 */
	@Override
	public PaNaverAddressVO selectShippingAddressByProductOrderID(String productOrderID) throws Exception {
		return paNaverDeliveryDAO.selectShippingAddressByProductOrderID(productOrderID);
	}
	
	/**
	 * 발송처리(출고) 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectSlipProcList() throws Exception {
		return paNaverDeliveryDAO.selectSlipProcList();
	}

	@Override
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception {
		return paNaverDeliveryDAO.selectMappingSeqByProductOrderInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return paNaverDeliveryDAO.selectOrderMappingInfoByMappingSeq(mappingSeq);
	}
	@Override
	public int updatePaOrdermResult(ParamMap paramMap, HashMap<String, Object> procMap) throws Exception {
		try {
			Paorderm paorderm = new Paorderm();
			paorderm.setMappingSeq(procMap.get("MAPPING_SEQ").toString());
			paorderm.setApiResultCode(paramMap.getString("apiResultCode"));
			paorderm.setApiResultMessage(paramMap.getString("resultMessage"));
			paorderm.setPaDoFlag(ComUtil.objToStr(procMap.get("PA_DO_FLAG")));
			paorderm.setProcDate(DateUtil.toTimestamp(DateUtil.getCurrentDateTimeAsString(), "yyyyMMddHHmss"));
			paorderm.setPaOrderNo(procMap.get("PA_ORDER_NO").toString());
			paorderm.setPaOrderSeq(procMap.get("PRODUCT_ORDER_ID").toString());
			if(paramMap.get("slipDelayed") != null) paorderm.setRemark1V("발송예정일 연기 " + procMap.get("DELY_DATE").toString());
			if(null != paramMap.get("isAll")) {
				paorderm.setIsAll(paramMap.get("isAll").toString());
			}
			
			if(paNaverDeliveryDAO.updatePaOrdermResult(paorderm) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			else {
				if(paNaverDeliveryDAO.selectOrderCancelCnt(paorderm) > 0) {
					paorderm.setPaOrderGb("20");
					paorderm.setPreCancelYn("1");
					if(paNaverDeliveryDAO.updatePaOrdermResult(paorderm) != 1) {
						throw processException("msg.cannot_save", new String[] { "TPAORDERM PRE_CANCEL_YN UPDATE" });
					}
				}
				paramMap.put("productOrderID", procMap.get("PRODUCT_ORDER_ID").toString());
				paramMap.put("shippingDueDate", procMap.get("DELY_DATE").toString());
				if(paNaverDeliveryDAO.updateShippingDueDate(paramMap) != 1) {
					throw processException("msg.cannot_save", new String[] { "TPNAVERORDERLIST UPDATE" });
				}
				else {
					return 1;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;			
		}
	}
	/**
	 * 발송 지연 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectDelayProcList() throws Exception {
		return paNaverDeliveryDAO.selectDelayProcList();
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, Object> orderMap) throws Exception {
		return paNaverDeliveryDAO.selectOrderPromo(orderMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, Object> orderMap) throws Exception {
		return paNaverDeliveryDAO.selectOrderPaPromo(orderMap);
	}
}