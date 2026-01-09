package com.cware.netshopping.pakakao.goods.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;

public interface PaKakaoGoodsProcess {

	PaKakaoGoodsVO selectPaKakaoGoodsInfo(ParamMap paramMap) throws Exception;
	
	List<PaKakaoGoodsVO> selectPaKakaoGoodsInfoList(ParamMap paramMap) throws Exception;

	List<PaKakaoGoodsImage> selectKakaoGoodsImage(ParamMap paramMap) throws Exception;

	int updatePakakaoGoodsImage(PaKakaoGoodsImage kakaoImage) throws Exception;
	
	int updatePakakaoGoodsImageCheck(PaKakaoGoodsImage kakaoImage) throws Exception;

	List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	String savePaKakaoGoods(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> goodsdtMapping, PaKakaoTalkDealVO dealData)  throws Exception;

	List<PaKakaoGoodsVO> selectPaKakaoSellStatusList(ParamMap paramMap) throws Exception;

	int updatePakakaoGoodsSellStatus(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	List<PaKakaoGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception;
	
	List<PaGoodsdtMapping> selectPaGoodsdtStockMappingList(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	void updatePaGoodsdtMappingQtyTx(List<PaGoodsdtMapping> paGoodsdtList) throws Exception;

	List<PaKakaoGoodsVO> selectPaGoodsdtMappingId(ParamMap paramMap) throws Exception;
	
	List<PaGoodsdtMapping> selectPaGoodsdtMappingIdList(PaKakaoGoodsVO paKakaoGoods) throws Exception;

	void updatePaKakaoGoodsdtMappingId(List<PaGoodsdtMapping> paGoodsdtMappingList) throws Exception;

	void savePaKakaoGoodsError(PaKakaoGoodsVO paKakaoGoods) throws Exception;
	
	HashMap<String, Object> selectPaNoticeData(PaKakaoGoodsVO vo) throws Exception;

	List<PaKakaoTalkDealVO> selectPaKakaoDealInfoList(ParamMap paramMap) throws Exception;

	void updatePaKakaoTalkDealId(PaKakaoTalkDealVO dealData) throws Exception;
	
	PaKakaoTalkDeal selectPaKakaoDealInfo(ParamMap paramMap) throws Exception;

	String selectKakaoOrderGoodsDtName(Map<String, Object> map) throws Exception;

}
