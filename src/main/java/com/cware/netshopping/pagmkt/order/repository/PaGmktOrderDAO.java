package com.cware.netshopping.pagmkt.order.repository;

import java.util.*;

import com.cware.framework.core.basic.ParamMap;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.OrderpromoVO;

@Service("pagmkt.order.PaGmktOrderDAO")
public class PaGmktOrderDAO extends AbstractPaDAO{	

	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.order.selectOrderInputTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception{
		return list("pagmkt.order.selectOrderInputTargetDtList", orderMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.order.selectCancelInputTargetList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("pagmkt.order.selectCancelInputTargetDtList", paramMap.get());
	}

	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("pagmkt.order.updatePreCancelYn", preCancelMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception {
		return (HashMap<String, String>) selectByPk("pagmkt.order.selectRefusalInfo", mappingSeq);
	}

	public String getSourcingMediaForTest(String goodsCode) throws Exception{
		return (String)(selectByPk("pagmkt.order.getSourcingMediaForTest", goodsCode));
	}

	public String getSourcingMediaForTest(Map<String, String> map) throws Exception{
		return (String)(selectByPk("pagmkt.order.getSourcingMediaForClaimTest", map));
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectPreOrderUpdateTargetDtList(String paOrderNo) throws Exception{
		return list("pagmkt.order.selectPreOrderUpdateTargetDtList", paOrderNo);
	}
	@SuppressWarnings("unchecked")
	public List<Object> selectPreOrderUpdateTargetList(ParamMap paramMap) throws Exception {
		return list("pagmkt.order.selectPreOrderUpdateTargetList", paramMap.get());
	}

	public int selectUnAttendedCount(String payNo) throws Exception {
		return (int)(selectByPk("pagmkt.order.selectUnAttendedCount", payNo));
	}

	public double selectPaOrderShipCost(ParamMap paramMap) throws Exception{
		return (double)(selectByPk("pagmkt.order.selectPaOrderShipCost", paramMap.get()));
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectPreOrderInputTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.order.selectPreOrderInputTargetList", paramMap.get());
	}

	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception{
		return (OrderpromoVO)(selectByPk("pagmkt.order.selectOrderPromo", orderMap));
	}	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaAddrInfo(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>)(selectByPk("pagmkt.order.selectPaAddrInfo", paramMap.get()));
	}
	
	/**
	 * 제휴OUT프로모션 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
	 * @param orderMap
	 * @return
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception{
		return (OrderpromoVO)(selectByPk("pagmkt.order.selectOrderPaPromo", orderMap));
	}	
}