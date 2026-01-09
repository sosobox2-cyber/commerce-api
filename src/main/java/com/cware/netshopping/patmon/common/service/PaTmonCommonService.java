package com.cware.netshopping.patmon.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;

public interface PaTmonCommonService {
	
	/**
	 * 티몬 카테고리 저장
	 * @param List
	 * @return 
	 * @throws Exception
	 */
	public String savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	/**
	 * 출고지/반품지 조회
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지/반품지 저장
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public String savePaTmonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 출고지/반품지 수정 조회
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception;

	/**
	 * 출고지/반품지 수정 저장
	 * @param paEntpSlip 
	 * @return List
	 * @throws Exception
	 */
	public String updatePaTmonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 배송템플릿 등록 리스트 조회
	 * @param ParamMap 
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception;

	/**
	 * 배송템플릿 등록 리스트 저장
	 * @param ParamMap 
	 * @return List
	 * @throws Exception
	 */
	public String updatePaTmonShipCostTx(ParamMap tmonShipCostMap) throws Exception;

	/**
	 * 정산API  저장
	 * @param PaTmonSettlement 
	 * @return List
	 * @throws Exception
	 */
	public String savePaTmonSettlementTx(List<PaTmonSettlement> paTmonSettlementList, ParamMap paramMap) throws Exception;

}
