package com.cware.netshopping.panaver.delivery.process;

import java.util.HashMap;
import java.util.List;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub.ChangedProductOrderInfo;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderInfo;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaNaverAddressVO;

public interface PaNaverDeliveryProcess {
	
	public int updateOrderChangePlaceOrder(ProductOrderInfo productOrderInfo, NaverSignature naverSignature, ChangedProductOrderInfo changedProductOrderInfo) throws Exception;
	public PaNaverAddressVO selectShippingAddressByProductOrderID(String addressSeq) throws Exception;
	public List<HashMap<String, Object>> selectSlipProcList() throws Exception;
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception;
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception;
	public int updatePaOrdermResult(ParamMap paramMap, HashMap<String, Object> procMap) throws Exception;
	public List<HashMap<String, Object>> selectDelayProcList() throws Exception;
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderID) throws Exception;
	public HashMap<String, Object> selectNotPlacedAndPayedOrder(String productOrderID) throws Exception;
	public OrderpromoVO selectOrderPromo(HashMap<String, Object> orderMap) throws Exception;
	/**
	 * 제휴OUT프로모션 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param orderMap
	 * @return
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, Object> orderMap) throws Exception;
	
}
