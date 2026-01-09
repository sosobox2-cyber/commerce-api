package com.cware.netshopping.pahalf.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;

public interface PaHalfOrderService {
	/**
	 * 하프클럽 주문저장 (TPAHALFORDERLIST, TPAORDERM)
	 * @param PaHalfOrderListVO
	 * @return
	 * @throws Exception
	 */
	public String savePaHalfOrderTx(Map<String, Object> order) throws Exception;
	/**
	 * 하프클럽 주문 승인 리스트 조회
	 * @param PaHalfOrderListVO
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderConfirmList(String paCode) throws Exception;
	/**
	 * 제휴 주문 테이블 업데이트 
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public int updateTPaorderm(Map<String, Object> order) throws Exception;
	/**
	 * 하프클럽 주문생성 리스트 조회
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;
	/**
	 * 하프클럽 주문생성 - 주문 상세정보 리스트 조회
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 판매거절 주문 정보 조회
	 * @param mappingSeq
	 * @return
	 */
	public Map<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;
	/**
	 * 하프클럽 취소저장 (TPAHALFORDERLIST, TPAORDERM)
	 * @param PaHalfOrderListVO
	 * @return
	 * @throws Exception
	 */
	public String savePaHalfCancelTx(Map<String, Object> cancel) throws Exception;
	/**
	 * 하프클럽  클레임(취소,반품,교환) 리스트 조회
	 * @param PaHalfOrderListVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽  - 주문 취소 상세정보 리스트 조회
	 * @param PaHalfOrderListVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽  - 모바일 자동취소(품절취소반품)
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception;

}
