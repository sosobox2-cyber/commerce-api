package com.cware.netshopping.pagmkt.order.process;

import java.util.*;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;

public interface PaGmktOrderProcess {
	
	public List<Object> selectOrderInputTargetList(ParamMap paramMap) throws Exception;
	public List<Object> selectCancelInputTargetList(ParamMap paramMap) throws Exception;
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderMap) throws Exception;
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception;
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception;
	public String getSourcingMediaForTest(String goodsCode) throws Exception;
	public String getSourcingMediaForTest(Map<String, String> map) throws Exception;
	public List<Object> selectPreOrderUpdateTargetDtList(String paOrderNo) throws Exception;
	public List<Object> selectPreOrderUpdateTargetList(ParamMap paramMap) throws Exception;
	public int selectUnAttendedCount(String payNo) throws Exception;
	public double selectPaOrderShipCost(ParamMap paramMap) throws Exception;
	public List<Object> selectPreOrderInputTargetList(ParamMap paramMap) throws Exception;
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception;
	public HashMap<String, Object> selectPaAddrInfo(ParamMap paramMap) throws Exception;
	/**
	 * 제휴OUT프로모션 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040) : S
	 * @param orderMap
	 * @return
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception;
}