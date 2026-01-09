package com.cware.netshopping.pahalf.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfGoodsVO;
import com.cware.netshopping.domain.PaHalfGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaHalfGoods;
import com.cware.netshopping.domain.model.PaHalfGoodsdtMapping;

public interface PaHalfGoodsService {

	/**
	 * 판매상태 변경된 상품리스트 조회
	 * @param apiInfoMap
	 * @return 
	 * @throws Exception
	 */
	public List<PaHalfGoods> selectPaHalfSellStateList(ParamMap paramMap) throws Exception;

	/**
	 * 판매상태 변경 후 결과 업데이트
	 * @param paHalfGoods
	 * @return
	 * @throws Exception
	 */
	public int savePaHalfGoodsSell(PaHalfGoods paHalfGoods) throws Exception;
	
	/**
	 * 하프클럽 제휴사 단품 코드 리스트 조회
	 * 
	 * @param temp
	 * @return
	 * @throws Exception
	 */
	public PaHalfGoodsdtMapping selectCheckPaOptionCode(ParamMap paramMap) throws Exception;

	/**
	 * 하프클럽 제휴사 단품 코드 업데이트
	 * 
	 * @param paGoodsdtMapping
	 * @throws Exception
	 */
	public void updateTpaGoodsdtMapping(PaHalfGoodsdtMapping paGoodsdtMapping) throws Exception;

	

	/**
	 * 재고 수정된 상품리스트 조회
	 * @param paramMap
	 * @return
	 */
	public List<PaHalfGoodsdtMappingVO> selectPaHalfGoodsdtMappingStockList(ParamMap paramMap) throws Exception;

	
	/**
	 * 단품 재고 정보 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectPaHalfStockInfoList(ParamMap paramMap) throws Exception;

	
	/**
	 * 단품 재고 업데이트
	 * @param paHalfGoodsdtMappingList
	 */
	public void savePaHalfGoodsdtStock(List<Map<String, Object>> paHalfGoodsdtMappingList) throws Exception;

	/**
	 * 상품 관련 전송기록 저장
	 * @param paGoodsTransLog
	 * @return
	 */
	public int insertPaHalfGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception;
	
	/**
	 * 하프클럽 입점 리스트 조회
	 * @param paGoodsTransLog
	 * @return
	 */
	public List<HashMap<String, String>> selectPaHalfGoodsTrans(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 상품 입점 조건 체크
	 * @param paGoodsTransLog
	 * @return
	 */
	public int goodsValidationCheck(String paCode, String goodsCode) throws Exception;
	/**
	 * 하프클럽 배송 템플릿 번호 저장
	 * @param paGoodsTransLog
	 * @return
	 */
	public int saveHalfGoodsShipCostCode(Map<String, String> goodsSlipMap) throws Exception;
	/**
	 * 하프클럽 상품 정보 조회
	 * @param paGoodsTransLog
	 * @return
	 */
	public List<PaHalfGoodsVO> selectPaHalfGoodsInfo(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 단품(옵션) 정보 조회
	 * @param paGoodsTransLog
	 * @return
	 */
	public List<PaHalfGoodsdtMapping> selectPaHalfGoodsdtInfoList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 정보고시 정보 조회
	 * @param paGoodsTransLog
	 * @return
	 */
	public List<PaGoodsOffer> selectPaHalfGoodsOfferList(ParamMap paramMap) throws Exception;
	/**
	 * 하프클럽 상품 정책 정보 세팅
	 * @param paGoodsTransLog
	 * @return
	 */
	public void setGoodsDtInfo(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping,ParamMap paramMap, boolean isUpdate) throws Exception;
	/**
	 * 하프클럽 상품 컨텐츠 정보 세팅
	 * @param paGoodsTransLog
	 * @return
	 */
	public void setGoodsContentsInfo(PaHalfGoodsVO paHalfGoods, List<PaGoodsOffer> paHalfGoodsOffer, ParamMap apiDataMap, boolean isUpdate) throws Exception;

	/**
	 * 이미지 수정된 상품 리스트 조회
	 * @param paramMap
	 * @return
	 */
	public List<PaHalfGoodsVO> selectPaHalfGoodsImageList(ParamMap paramMap) throws Exception;

	/**
	 * 하프클럽 이미지 수정 후 업데이트
	 * @param paHalfGoods
	 * @throws Exception
	 */
	public void updatePaHalfGoodsImage(PaHalfGoodsVO paHalfGoods) throws Exception;
	/**
	 * 하프클럽 상품 컨텐츠 정보 조회
	 * @param paGoodsTransLog
	 * @return
	 */
	public PaHalfGoodsVO selectPaHalfGoodsContentsInfo(ParamMap paramMap) throws Exception;

	/** TGOODS기준 배송템플릿 인자 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> selectGoodsEntpSlip(ParamMap paramMap) throws Exception;


	/**
	 * 하프클럽 상품등록 저장
	 * 
	 * @param paHalfGoods
	 * @param goodsdtMapping
	 * @param paPromoTargetList
	 * @return
	 */
	public String savePaHalfGoodsTx(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping,  ParamMap paramMap) throws Exception;

	/**
	 * 하프클럽 상품 등록 실패시 입점 상태 변경
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public void checkGoodsInsertFail(ParamMap paramMap) throws Exception;

	/**
	 * 하프클럽 상품 컨텐츠정보 수정 저장
	 * 
	 * @param paHalfGoods
	 * @return
	 * @throws Exception
	 */
	public void updatePaHalfGoodsOffer(PaHalfGoodsVO paHalfGoods) throws Exception;

	/**
	 * 하프클럽 배송템플릿 연동 실패 저장
	 * 
	 * @param paHalfGoods
	 * @throws Exception
	 */
	public void updateEntpSlipInsertFail(PaHalfGoodsVO paHalfGoods) throws Exception;

	//
//	public String selectPaHalfBrandNo(PaHalfGoodsVO paHalfGoods) throws Exception;

	/**
	 * 하프클럽 상품정책코드 리스트 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public PaHalfGoodsVO selectPrdPrcNoList(ParamMap paramMap) throws Exception;

	/**
	 * 하프클럽 상품정책코드 리스트 저장
	 * 
	 * @param paHalfGoodsVO
	 */
	public void updatePrdPrcNo(PaHalfGoodsVO paHalfGoodsVO) throws Exception;

	/**
	 * 상품 등록 리셋 저장
	 * @param paramMap
	 */
	public void updatePaHalfGoodsReset(ParamMap paramMap) throws Exception;

	/**
	 * 상품 정책정보 수정 저장
	 * 
	 * @param paHalfGoods
	 * @param goodsdtMappingList
	 * @throws Exception
	 */
	public void savePrdPlcyDtInfoModifyTx(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMappingList) throws Exception;

	/**
	 * 상품 리턴노트 업데이트
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public void updatePaHalfGoodsReturnNote(PaHalfGoodsVO paHalfGoods,ParamMap paramMap) throws Exception;

	/**
	 * 상품의 도서산간 제주 배송유무 확인
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public String selectDelyNoAreaGb(ParamMap paramMap) throws Exception;

	public PaHalfGoodsVO selectPaHalfGoodsStatus(ParamMap paramMap) throws Exception;

	/**
	 * 입점요청중 업데이트
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public void updateProceeding(ParamMap paramMap) throws Exception;

	/**
	 * 입점요청중 해제
	 * 
	 * @param paramMap
	 * @throws Exception
	 */
	public void updateClearProceeding(ParamMap paramMap) throws Exception;

   
}
