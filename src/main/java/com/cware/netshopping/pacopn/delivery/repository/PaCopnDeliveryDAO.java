package com.cware.netshopping.pacopn.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PacopnorderlistVO;
import com.cware.netshopping.domain.model.Pacopnorderitemlist;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pacopn.delivery.paCopnDeliveryDAO")
public class PaCopnDeliveryDAO extends AbstractPaDAO{
	
	/**
	 * 상품준비중 주문 저장 - 데이터 중복 체크
	 * @param paCopnOrderList
	 * @return
	 */
	public int selectPaCopnOrderListExists(PacopnorderlistVO paCopnOrderList) throws Exception{
		return (Integer) selectByPk("pacopn.delivery.selectPaCopnOrderListExists", paCopnOrderList);
	}
	
	public int selectPaCopnOrderItemListExists(Pacopnorderitemlist paCopnOrderItemList) throws Exception{
		return (Integer) selectByPk("pacopn.delivery.selectPaCopnOrderItemListExists", paCopnOrderItemList);
	}
	
	/**
	 * 상품준비중 주문 저장 - TPACOPNORDERLIST INSERT 
	 * @param paCopnOrderList
	 * @return
	 */
	public int insertPaCopnOrderList(PacopnorderlistVO paCopnOrderList) throws Exception{
		return insert("pacopn.delivery.insertPaCopnOrderList", paCopnOrderList);
	}
	
	/**
	 * 상품준비중 주문 저장 - TPACOPNORDERITEMLIST INSERT
	 * @param paCopnOrderitemList
	 * @return
	 */
	public int insertPaCopnOrderitemList(Pacopnorderitemlist paCopnOrderitemList) throws Exception{
		return insert("pacopn.delivery.insertPaCopnOrderitemList", paCopnOrderitemList);
	}
	
	/**
	 * 송장업로드 처리 대상 조회
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectBeforeInvoiceList() throws Exception{
		return list("pacopn.delivery.selectBeforeInvoiceList", null);
	}
	
	/**
	 * 송장업로드 처리 대상 상세 조회
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectBeforeInvoiceDtList(String shipmentBoxId) throws Exception{
		return list("pacopn.delivery.selectBeforeInvoiceDtList", shipmentBoxId);
	}
	
	/**
	 * 송장업로드 이미 처리 여부 확인
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public int selectAlreadyExecuteShipping(String mappingSeq) throws Exception{
		return (Integer) selectByPk("pacopn.delivery.selectAlreadyExecuteShipping", mappingSeq);
	}
	
	/**
	 * 분리배송 대상 여부 확인
	 * @param shipmentBoxId
	 * @return
	 * @throws Exception
	 */
	public int selectInvoicesPerShipmentBoxId(String shipmentBoxId) throws Exception{
		return (Integer) selectByPk("pacopn.delivery.selectInvoicesPerShipmentBoxId", shipmentBoxId);
	}
	
	/**
	 * 송장업로드 처리 후 결과 업데이트 - TPAORDERM
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderResult(Paorderm paorderm) throws Exception{
		return update("pacopn.delivery.updatePaOrderResult", paorderm);
	}
	
	/**
	 * 장기미배송 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectShippingComplete(ParamMap paramMap) throws Exception{
		return list("pacopn.delivery.selectShippingComplete", paramMap.get());
	}
	
	/**
	 * 장기미배송 배송완료 결과 저장
	 * @return
	 * @throws Exception
	 */
	public int updateShippingComplete() throws Exception{
		return update("pacopn.delivery.updateShippingComplete", null);
	}
	
	/**
	 * 배송완료 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectDeliveryCompleteList() throws Exception{
		return list("pacopn.delivery.selectDeliveryCompleteList", null);
	}
	
	/**
	 * 배송완료 처리 내역 저장
	 * @param paOrderm
	 * @return
	 * @throws Exception
	 */
	public int updateDeliveryComplete(Paorderm paOrderm) throws Exception{
		return update("pacopn.delivery.updateDeliveryComplete", paOrderm);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectShippingUpdateList() {
		return list("pacopn.delivery.selectShippingUpdateList", null);
	}

	public int selectSendInvoicesPerShipmentBoxId(String shipmentBoxId) {
		return (Integer) selectByPk("pacopn.delivery.selectSendInvoicesPerShipmentBoxId", shipmentBoxId);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectUpdatingInvoiceDtList(String shipmentBoxId) {
		return list("pacopn.delivery.selectUpdatingInvoiceDtList", shipmentBoxId);
	}
	
}
