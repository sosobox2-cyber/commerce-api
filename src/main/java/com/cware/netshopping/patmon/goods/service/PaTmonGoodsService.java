package com.cware.netshopping.patmon.goods.service;

import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;

public interface PaTmonGoodsService {

	/**
	 * 티몬 상품등록 - 반품배송지 조회
	 * @param ParamMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	List<PaEntpSlip> selectPaTmonEntpSlip(ParamMap paramMap)  throws Exception;

	/**
	 * 티몬 상품등록 - 상품정보 조회
	 * @param ParamMap
	 * @return PaTmonGoodsVO
	 * @throws Exception
	 */
	PaTmonGoodsVO selectPaTmonGoodsInfo(ParamMap paramMap) throws Exception;

	/**
	 * 정보고시 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<PaGoodsOfferVO> selectPaTmonGoodsOfferList(ParamMap paramMap)  throws Exception;
	
	/**
	 * 상품단품list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPaTmonGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 입점 성공 상품 티몬 정보 저장 
	 * @param paTmonGoods
	 * @param goodsdtMapping
	 * @param paPromoTargetList
	 * @return
	 */
	public String savePaTmonGoodsTx(PaTmonGoodsVO paTmonGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;

	/**
	 * TPAGOODSTRANSLOG INSERT
	 * @param PaGoodsTransLog
	 */
	public int insertPaTmonGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception;

	/**
	 * 상품판매상태 변경 판매자 상품번호 검색 
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsVO> selectPaTmonSellStateList(ParamMap paramMap) throws Exception;

	/**
	 * 상품판매상태 변경 완료
	 * @param PaTmonGoodsVO
	 * @return
	 * @throws Exception 
	 */
	String updateTmonGoodsStatus(PaTmonGoodsVO sellStateTarget) throws Exception;
	
	/**
	 * 상품 재고 수량 변경 대상 검색
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockMappingList(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품 수정 회수지 등록 대상 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaEntpSlip> selectPaTmonEntpSlipList(ParamMap paramMap) throws Exception;

	/**
	 * 상품 수정  대상 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsVO> selectPaTmonGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 단품 개수 수정 대상 상품 검색 
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockList(ParamMap paramMap) throws Exception;

	/**
	 * 단품 개수 수정 대상 상품 저장
	 * @param PaTmonGoodsdtMappingVO
	 * @return
	 * @throws Exception 
	 */
	String updatePaTmonGoodsdtMappingQtyTx(List<PaTmonGoodsdtMappingVO> paGoodsdtList) throws Exception;

	/**
	 * 단품이 추가된 상품 조회 
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtAddedList(ParamMap paramMap) throws Exception;
	
	/**
	 * 단품이 추가된 상품 저장
	 * @param List<PaTmonGoodsdtMappingVO>
	 * @param Map<String, Object> 
	 * @return
	 * @throws Exception 
	 */
	String updatePaTmonGoodsdtMappingAddedTx(List<PaTmonGoodsdtMappingVO> goodsAddedMapping) throws Exception;
	
	/**
	 * 옵션등록  대상 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception 
	 */
	List<PaTmonGoodsVO> selectPaTmonModifyOptionList(ParamMap paramMap) throws Exception;

	List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception;

	/**
	 * 반려된 상품 상품등록 조회 제외
	 * @param ParamMap
	 * @return String
	 * @throws Exception 
	 */
	String savePaTmonGoodsErrorTx(PaTmonGoodsVO paTmonGoods) throws Exception;
	
	/**
	 * 티몬 배송템플릿 오류 상품 재연동(ex : 일반상품 > 설치상품)
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	int selectPaTmonExceptShipPolicy(String goodsCode) throws Exception;
}
