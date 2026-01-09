package com.cware.netshopping.pakakao.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;

public interface PaKakaoGoodsService {
	/**
	 * KAKAO 상품등록 - 상품 등록 대상 정보 조회 
	 * @param ParamMap
	 * @return PaKakaoGoodsVO
	 * @throws Exception
	 */
	PaKakaoGoodsVO selectPaKakaoGoodsInfo(ParamMap paramMap) throws Exception;
	List<PaKakaoGoodsVO> selectPaKakaoGoodsInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * KAKAO 이미지업로드요청 - 업로드 대상 이미지 조회
	 * @param String
	 * @return PaKakaoGoodsVO
	 * @throws Exception
	 */
	List<PaKakaoGoodsImage> selectKakaoGoodsImage(ParamMap paramMap) throws Exception;
	
	/**
	 * KAKAO 이미지업로드요청 - 카카오이미지URL UPDATE
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	int updatePakakaoGoodsImage(PaKakaoGoodsImage kakaoImage) throws Exception;
	
	/**
	 * KAKAO 이미지업로드요청 - 카카오이미지URL CHECK
	 * @param PaKakaoGoodsImage
	 * @return int
	 * @throws Exception
	 */
	int updatePakakaoGoodsImageCheckTx(PaKakaoGoodsImage kakaoImage) throws Exception;
	
	/**
	 * KAKAO 상품등록/수정 - 단품상세 조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * KAKAO 상품등록/수정 - 제휴상품정보 저장
	 * @param PaKakaoGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	String savePaKakaoGoodsTx(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> goodsdtMapping, PaKakaoTalkDealVO dealData) throws Exception;
	
	/**
	 * KAKAO 상품판매상태수정 - 상품판매상태 조회
	 * @param ParamMap
	 * @return PaKakaoGoodsVO
	 * @throws Exception
	 */
	List<PaKakaoGoodsVO> selectPaKakaoSellStatusList(ParamMap paramMap) throws Exception;
	
	
	/**
	 * KAKAO 상품판매상태수정 - SALE_YN UPDATE
	 * @param PaKakaoGoodsVO
	 * @return int
	 * @throws Exception
	 */
	int updatePakakaoGoodsSellStatus(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	List<PaKakaoGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception;
	
	List<PaGoodsdtMapping> selectPaGoodsdtStockMappingList(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	void updatePaGoodsdtMappingQtyTx(List<PaGoodsdtMapping> paGoodsdtList) throws Exception;
	
	List<PaKakaoGoodsVO> selectPaGoodsdtMappingId(ParamMap paramMap) throws Exception;
	
	List<PaGoodsdtMapping> selectPaGoodsdtMappingIdList(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	void updatePaKakaoGoodsdtMappingIdTx(List<PaGoodsdtMapping> paGoodsdtMappingList) throws Exception;
	
	void savePaKakaoGoodsError(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	HashMap<String, Object> selectPaNoticeData(PaKakaoGoodsVO vo) throws Exception;
	
	List<PaKakaoTalkDealVO> selectPaKakaoDealInfoList(ParamMap paramMap) throws Exception;
	
	void updatePaKakaoTalkDealId(PaKakaoTalkDealVO dealData) throws Exception;
	
	PaKakaoTalkDeal selectPaKakaoDealInfo(ParamMap paramMap) throws Exception;
	
	String selectKakaoOrderGoodsDtName(Map<String, Object> map) throws Exception;
	
}
