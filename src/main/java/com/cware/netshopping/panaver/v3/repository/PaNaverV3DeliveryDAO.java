package com.cware.netshopping.panaver.v3.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.v3.domain.OrderInfo;
import com.cware.netshopping.panaver.v3.domain.ProductOrderInfo;

@Repository("panaver.v3.delivery.paNaverV3DeliveryDAO")
public class PaNaverV3DeliveryDAO extends AbstractPaDAO {
	
	/**
	 * 발주처리  TPANAVERORDERCHANGE 업데이트
	 * @param String
	 * @return int
	 * 
	 */
	public int updateOrderChangePlaceOrder(String productOrderId) throws Exception { 
		return update("panaver.v3.delivery.updateOrderChangePlaceOrder", productOrderId);
	}
	
	/**
	 *  발주처리  TPANAVERORDERM MERGE
	 * @param OrderInfo
	 * @return int
	 * @throws Exception
	 */
	public int mergeNaverOrderm(OrderInfo order) throws Exception {
		return insert("panaver.v3.delivery.mergeNaverOrderm", order);
	}
	
	/**
	 *  발주처리  TPANAVERORDERLIST MERGE
	 * @param ProductOrderInfo
	 * @return int
	 * @throws Exception
	 */
	public int mergeOrderList(ProductOrderInfo productOrder) throws Exception {
		return insert("panaver.v3.delivery.mergeOrderList", productOrder);
	}
	
	/**
	 *  발주처리  TPANAVERORDERCHANGE PLACE_DATE IS NULL 조회
	 * @param String
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectNotPlacedAndPayedOrder(String productOrderId) throws Exception {
		return (HashMap<String, Object>) selectByPk("panaver.v3.delivery.selectNotPlacedAndPayedOrder", productOrderId);
	}
	
	/**
	 *  발송처리 대상 조회
	 * @param String
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectSlipProcList(ParamMap paramMap) throws Exception{
		return list("panaver.v3.delivery.selectSlipProcList", paramMap.get());
	}
	
	/**
	 *  발송처리 성공 업데이트 - TPAORDERM
	 * @param Paorderm
	 * @return int
	 * @throws Exception
	 */
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.v3.delivery.updatePaOrdermResult", paorderm);
	}
	
	/**
	 * @param Paorderm
	 * @return int
	 * @throws Exception
	 */
	public int selectOrderCancelCnt(Paorderm paorderm) throws Exception{
		return (int) selectByPk("panaver.v3.delivery.selectOrderCancelCnt", paorderm);
	}
	
	/**
	 * TPANAVERORDERLIST SHIPPING_DUE_DATE 업데이트
	 * @param Paorderm
	 * @return int
	 * @throws Exception
	 */
	public int updateShippingDueDate(ParamMap paramMap) throws Exception {
		return update("panaver.v3.delivery.updateShippingDueDate", paramMap.get());
	}
	/**
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String selectMappingSeqByProductOrderInfo(ParamMap paramMap) throws Exception {
		return (String) selectByPk("panaver.v3.delivery.selectMappingSeqByProductOrderInfo", paramMap.get());
	}

	/**
	 * 네이버 제휴 주문 조회 (TPAORDERM)
	 * 
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectOrderMappingInfoByMappingSeq(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("panaver.v3.delivery.selectOrderMappingInfoByMappingSeq", mappingSeq);
	}

	/**
	 * 네이버 주문접수 데이터 상세 조회
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectOrderInputTargetDtList(String orderId) {
		return (List<HashMap<String, Object>>) list("panaver.v3.delivery.selectOrderInputTargetDtList", orderId);
	}
	/**
	 * 발송지연 대상 조회
	 * 
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectDelayProcList(ParamMap paramMap) throws Exception{
		return list("panaver.v3.delivery.selectDelayProcList", paramMap.get());
	}
	
}