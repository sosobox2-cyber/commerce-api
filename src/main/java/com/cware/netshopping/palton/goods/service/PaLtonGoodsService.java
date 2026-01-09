package com.cware.netshopping.palton.goods.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaPromoTarget;

public interface PaLtonGoodsService {
	
	/**
	 * 롯데온 상품등록 - 반품배송지 조회
	 * @param ParamMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	public PaEntpSlip selectPaLtonEntpSlip(ParamMap paramMap) throws Exception;
	public List<PaEntpSlip> selectPaLtonEntpSlipList(ParamMap paramMap) throws Exception;

	/**
	 * 상품 정보 조회
	 * @param paramMap
	 * @return
	 */
	public PaLtonGoodsVO selectPaLtonGoodsInfo(ParamMap paramMap) throws Exception;

	/**
	 * 정보고시 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<PaGoodsOffer> selectPaLtonGoodsOfferList(ParamMap paramMap) throws Exception;

	/**
	 * 상품 옵션 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtInfoList(ParamMap paramMap) throws Exception;

	/**
	 * TPAGOODSTRANSLOG INSERT
	 * @param PaGoodsTransLog
	 */
	public int insertPaLtonGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception;

	/**
	 * 입점 성공 상품 롯데ON 정보 저장 
	 * @param paLtonGoods
	 * @param goodsdtMapping
	 * @param paPromoTargetList
	 * @return
	 */
	public String savePaLtonGoodsTx(PaLtonGoodsVO paLtonGoods, List<PaLtonGoodsdtMappingVO> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;

	/**
	 * 상품상세조회 판매자 상품번호 검색 
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<HashMap<String, String>> selectPaApprovalStatusList(ParamMap paramMap) throws Exception;
	public List<HashMap<String, String>> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception;

	/**
	 * 상품상세조회 판매자 단품번호 업데이트
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public String updateGoodsdtPaOptionCode(PaLtonGoodsdtMappingVO ltonGoodsDtMapping) throws Exception;

	/**
	 * 상품판매상태 변경 판매자 상품번호 검색 
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<PaLtonGoodsVO> selectPaLtonSellStateList(ParamMap paramMap) throws Exception;
	
	
	public String savePaLtonGoodsSellTx(PaLtonGoodsVO paLtonGoods) throws Exception;

	/**
	 * 재고 API 상품검색 
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStockList(ParamMap paramMap) throws Exception;

	/**
	 * 재고 API 상품업데이트
	 * @param PaLtonGoodsdtMappingVO
	 * @return
	 * @throws Exception 
	 */
	public String savePaLtonGoodsStockTx(List<PaLtonGoodsdtMappingVO> paGoodsdtList) throws Exception;
	
	/**
	 * 상품수정 API 상품 수정 대상 조회
	 * @param PaLtonGoodsVO
	 * @return
	 * @throws Exception 
	 */
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 재고 수정 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStock(ParamMap paramMap) throws Exception;
	
	/**
	 * 상세 정보 조회 승인상태 UPDATE
	 * @param PaLtonGoodsVO
	 * @return
	 * @throws Exception 
	 */
	public String updatePaLtonApprovalStatus(PaLtonGoodsVO paltonGoodsVo) throws Exception;
	
	/**
	 * 단품판매상태 변경대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellState(ParamMap paramMap) throws Exception;
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellStateList(ParamMap paramMap) throws Exception;
	
	/**
	 * 프로모션 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception;
	public List<HashMap<String, String>> selectPaLtonGoodsTrans(ParamMap paramMap) throws Exception;
	public int updatePaLtonGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception;
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoListMass(ParamMap paramMap) throws Exception;
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception;
	public int updateMassTargetYn(PaLtonGoodsVO paltonGoods) throws Exception;
	public int updatePaLtonPaStatus(ParamMap paramMap) throws Exception;
	
	
}
