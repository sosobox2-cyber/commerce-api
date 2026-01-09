package com.cware.netshopping.panaver.delivery.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaNaverAddressVO;
import com.cware.netshopping.panaver.delivery.process.PaNaverDeliveryProcess;
import com.cware.netshopping.panaver.delivery.service.PaNaverDeliveryService;

@Service("panaver.delivery.paNaverDeliveryService")
public class PaNaverDeliveryServiceImpl extends AbstractService implements
		PaNaverDeliveryService {
	
	@Autowired
	PaNaverDeliveryProcess paNaverDeliveryProcess;
	
	/**
	 * 발주처리 결과 저장
	 * @param productOrderId
	 * @return
	 */
	@Override
	public int updateOrderChangePlaceOrderTx(ProductOrderInfo productOrderInfo, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception{
		return paNaverDeliveryProcess.updateOrderChangePlaceOrder(productOrderInfo, naverSignature, changedProductOrderInfo);
	}
	
	@Override
	public HashMap<String, Object> selectNotPlacedAndPayedOrder(String productOrderID) throws Exception {
		return paNaverDeliveryProcess.selectNotPlacedAndPayedOrder(productOrderID);
	}
	
	@Override
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderID) throws Exception {
		return paNaverDeliveryProcess.selectOrderInputTargetDtList(orderID);
	}
	
	/**
	 * 주소 정보 조회
	 */
	@Override
	public PaNaverAddressVO selectShippingAddressByProductOrderID(String productOrderID) throws Exception {
		return paNaverDeliveryProcess.selectShippingAddressByProductOrderID(productOrderID);
	}
	
	/**
	 * 발송처리(출고) 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectSlipProcList() throws Exception {
		return paNaverDeliveryProcess.selectSlipProcList();
	}

	@Override
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception {
		return paNaverDeliveryProcess.selectMappingSeqByProductOrderInfo(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return paNaverDeliveryProcess.selectOrderMappingInfoByMappingSeq(mappingSeq);
	}

	@Override
	public int updatePaOrdermResultTx(ParamMap paramMap, HashMap<String, Object> procMap) throws Exception {
		return paNaverDeliveryProcess.updatePaOrdermResult(paramMap, procMap);
	}

	/**
	 * 발송지연 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectDelayProcList() throws Exception {
		return paNaverDeliveryProcess.selectDelayProcList();
	}
	
	@Override
	public OrderpromoVO selectOrderPromo(HashMap<String, Object> orderMap) throws Exception {
		return paNaverDeliveryProcess.selectOrderPromo(orderMap);
	}
	
	@Override
	public OrderpromoVO selectOrderPaPromo(HashMap<String, Object> orderMap) throws Exception {
		return paNaverDeliveryProcess.selectOrderPaPromo(orderMap);
	}

}
