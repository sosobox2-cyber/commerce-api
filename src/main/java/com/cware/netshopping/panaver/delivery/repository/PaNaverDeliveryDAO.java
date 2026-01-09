package com.cware.netshopping.panaver.delivery.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.api.panaver.order.seller.SellerServiceStub.Order;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrder;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaNaverAddressVO;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.delivery.paNaverDeliveryDAO")
public class PaNaverDeliveryDAO extends AbstractPaDAO {
	
	public int updateOrderChangePlaceOrder(String productOrderId) throws Exception {
		return update("panaver.delivery.updateOrderChangePlaceOrder", productOrderId);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectNotPlacedAndPayedOrder(String productOrderID) throws Exception {
		return (HashMap<String, Object>) selectByPk("panaver.delivery.selectNotPlacedAndPayedOrder", productOrderID);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderID) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.delivery.selectOrderInputTargetDtList", orderID);
	}
	
	public int insertOrderm(Order order) throws Exception {
		return insert("panaver.delivery.insertOrderm", order);
	}
	
	public int mergeOrderList(ProductOrder productOrder) throws Exception {
		return insert("panaver.delivery.mergeOrderList", productOrder);
	}
	
	public PaNaverAddressVO selectShippingAddressByProductOrderID(String productOrderID) throws Exception{
		return (PaNaverAddressVO) selectByPk("panaver.delivery.selectShippingAddressByProductOrderID", productOrderID);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.delivery.selectOrderMappingInfoByMappingSeq", mappingSeq);
	}
	
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.delivery.selectMappingSeqByProductOrderInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectSlipProcList() throws Exception{
		return list("panaver.delivery.selectSlipProcList", null);
	}

	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.delivery.updatePaOrdermResult", paorderm);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectDelayProcList() throws Exception{
		return list("panaver.delivery.selectDelayProcList", null);
	}
	
	public int updateShippingDueDate(ParamMap paramMap) throws Exception {
		return update("panaver.delivery.updateShippingDueDate", paramMap.get());
	}
	
	public int selectOrderCancelCnt(Paorderm paorderm) throws Exception{
		return (int) selectByPk("panaver.delivery.selectOrderCancelCnt", paorderm);
	}
	
	public OrderpromoVO selectOrderPromo(HashMap<String, Object> orderMap) throws Exception{
		return (OrderpromoVO) selectByPk("panaver.delivery.selectOrderPromo", orderMap);
	}
	
	public OrderpromoVO selectOrderPaPromo(HashMap<String, Object> orderMap) throws Exception{
		return (OrderpromoVO) selectByPk("panaver.delivery.selectOrderPaPromo", orderMap);
	}
}