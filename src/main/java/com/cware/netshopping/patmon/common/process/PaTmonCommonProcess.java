package com.cware.netshopping.patmon.common.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;

public interface PaTmonCommonProcess {
	
	/**
	 * 전체카테고리저장
	 * @param List<PaGoodsKinds>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception;

	/**
	 * 티몬 출고지/반품지 조회
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지/반품지 저장
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public String savePaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 출고지/반품지 수정 조회
	 * @return Map
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception;

	/**
	 * 출고지/반품지 수정 조회
	 * @return Map
	 * @throws Exception
	 */
	public String updatePaTmonEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

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
	public String updatePaTmonShipCost(ParamMap tmonShipCostMap) throws Exception;

	/**
	 * 정산API  저장
	 * @param PaTmonSettlement 
	 * @return List
	 * @throws Exception
	 */
	public String savePaTmonSettlement(List<PaTmonSettlement> paTmonSettlementList, ParamMap paramMap) throws Exception;

}
