package com.cware.netshopping.palton.delivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonOrderListVO;

@Service("palton.delivery.paLtonDeliveryDAO")
public class PaLtonDeliveryDAO extends AbstractPaDAO{

	public int insertPaltonOrderList(PaLtonOrderListVO paltonorderlist) {
		return insert("palton.delivery.insertPaLtonOrderList", paltonorderlist);
	}

	public int countOrderList(PaLtonOrderListVO vo) {
		Map<String , String > map = new HashMap<String, String>();
		map.put("odNo"		, vo.getOdNo());
		map.put("odSeq"		, vo.getOdSeq());
		map.put("paOrderGb"	, vo.getPaOrderGb());
		map.put("procSeq"	, vo.getProcSeq());
		return (Integer) selectByPk("palton.delivery.countOrderList", map);
	}
	
	public int countOrderList(PaLtonCancelListVO vo) {
		Map<String , String > map = new HashMap<String, String>();
		map.put("odNo"		, vo.getOdNo());
		map.put("odSeq"		, vo.getOdSeq());
		map.put("paOrderGb"	, "10");
		return (Integer) selectByPk("palton.delivery.countOrderList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryReadyList() {
		return list("palton.delivery.selectDeliveryReadyList", null);
	}

	public int updatePaOrderMDoFlag(Map<String, Object> map) {
		return update("palton.delivery.updatePaOrderMDoFlag", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("limitCount", limitCount);
		return list("palton.delivery.selectOrderInputTargetList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) {
		return list("palton.delivery.selectOrderInputTargetDtList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) {
		return (HashMap<String, Object>) selectByPk("palton.delivery.selectRefusalInfo", mappingSeq);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcList() {
		return list("palton.delivery.selectSlipOutProcList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryCompleteList() {
		return list("palton.delivery.selectDeliveryCompleteList", null);
	}

	public int updatePreCanYn(Map<String, Object> map) {
		return update("palton.delivery.updatePreCanYn", map);
	}
	
	public OrderpromoVO selectOrderPromo(Map<String, Object> map) {
		return  (OrderpromoVO)(selectByPk("palton.delivery.selectOrderPromo", map));
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectExchangeHoldList() throws Exception {
		return list("palton.delivery.selectExchangeHoldList", null);
	}

	public int updateExchangeAmt(ParamMap paramMap) throws Exception {
		return update("palton.delivery.updateExchangeAmt", paramMap.get());
	}

	public int updatePaHoldYn(ParamMap paramMap) throws Exception {
		return update("palton.delivery.updatePaHoldYn", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectRetrievalExceptList() throws Exception {
		return list("palton.delivery.selectRetrievalExceptList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipUpdateProcList() {
		return list("palton.delivery.selectSlipUpdateProcList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectDeliveryDelayProcList() {
		return list("palton.delivery.selectDeliveryDelayProcList", null);
	}
	
	public int updatePaLtonSndAgrdDttm(Map<String, Object> map) {
		return update("palton.delivery.updatePaLtonSndAgrdDttm", map);
	}

}
