package com.cware.netshopping.passg.goods.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;

public interface PaSsgGoodsService {
	
	List<PaEntpSlip> selectPaSsgEntpSlip(ParamMap paramMap)  throws Exception;
	
	PaSsgGoodsVO selectPaSsgGoodsInfo(ParamMap paramMap) throws Exception;

	/**
	 * SSG 상품등록 - 상품 정보 저장
	 * @param PaSsgGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	String savePaSsgGoodsTx(PaSsgGoodsVO paSsgGoods, List<PaSsgGoodsdtMapping> goodsdtMapping) throws Exception;

	/**
	 * SSG 상품수정 - 수정 상품 대상 조회 
	 * @param ParamMap
	 * @return List<PaSsgGoodsVO>
	 * @throws Exception
	 */
	List<PaSsgGoodsVO> selectPaSsgGoodsInfoList(ParamMap paramMap)  throws Exception;

	/**
	 * 반려된 상품 상품등록 조회 제외
	 * @param ParamMap
	 * @return String
	 * @throws Exception 
	 */
	String savePaSsgGoodsErrorTx(PaSsgGoodsVO paSsgGoods) throws Exception;
	
	List<PaSsgGoodsVO> selectPaSsgGoodsApprovalList(ParamMap paramMap) throws Exception;
	String updatePaSsgGoodsApproval(PaSsgGoodsVO paSsgGoods) throws Exception;
	List<PaSsgGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception;
	List<PaSsgGoodsdtMapping> selectPaGoodsdtStockMappingList(PaSsgGoodsVO paSsgGoods) throws Exception;
	String updatePaGoodsdtMappingQtyTx(List<PaSsgGoodsdtMapping> paGoodsdtList) throws Exception;
	List<PaSsgGoodsVO> selectPaSellStateList(ParamMap paramMap) throws Exception;
	String updatePaGoodsStatus(PaSsgGoodsVO sellStateTarget) throws Exception;
	List<PaSsgDisplayMapping> selectPaSsgDisplayList(ParamMap paramMap) throws Exception;

	/**
	 * 상품 등록,수정 API 단품 정보 조회 
	 * @param ParamMap
	 * @return String
	 * @throws Exception 
	 */
	List<PaSsgGoodsdtMapping> selectPaSsgGoodsdtInfoList(ParamMap paramMap) throws Exception;

	HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception;

	HashMap<String, Object> selectSsgFoodInfo(ParamMap paramMap) throws Exception;
}
