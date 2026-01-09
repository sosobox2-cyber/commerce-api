package com.cware.netshopping.pacopn.order.process;

import java.util.HashMap;
import java.util.List;

import com.cware.netshopping.domain.OrderpromoVO;

public interface PaCopnOrderProcess {
	
	/**
	 * 주문생성 대상 조회
	 * @param paOrderCreateCnt
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetList(int paOrderCreateCnt) throws Exception;
	
	/**
	 * 주문생성 대상 상세 조회
	 * @param paOrderNo
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderInputTargetDtList(HashMap<String, String> orderInputTarget) throws Exception;
	
	/**
	 * 주문생성 결과처리 - 상품 준비중 취소 parameter 조회
	 * @param mappingSeq
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectRefusalInfo(String mappingSeq) throws Exception;
	
	/**
	 * 쿠팡 프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPromo(HashMap<String, String> orderMap) throws Exception;
	
	/**
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * 쿠팡 제휴OUT프로모션 조회 - 조회
	 * @param HashMap<String, String>
	 * @return OrderpromoVO
	 * @throws Exception
	 */
	public OrderpromoVO selectOrderPaPromo(HashMap<String, String> orderMap) throws Exception;
}
