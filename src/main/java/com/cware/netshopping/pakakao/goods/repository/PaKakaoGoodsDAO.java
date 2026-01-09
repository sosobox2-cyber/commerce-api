package com.cware.netshopping.pakakao.goods.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;

@Service("pakakao.goods.paKakaoGoodsDAO")
public class PaKakaoGoodsDAO extends AbstractPaDAO{
	
	public PaKakaoGoodsVO selectPaKakaoGoodsInfo(ParamMap paramMap) {
		return (PaKakaoGoodsVO) selectByPk("pakakao.goods.selectPaKakaoGoodsInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaKakaoGoodsVO> selectPaKakaoGoodsInfoList(ParamMap paramMap) {
		return list("pakakao.goods.selectPaKakaoGoodsInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaKakaoGoodsImage> selectKakaoGoodsImage(ParamMap paramMap) {
		return list ("pakakao.goods.selectKakaoGoodsImage", paramMap.get());
	}

	public int updatePakakaoGoodsImage(PaKakaoGoodsImage kakaoImage) {
		return  update("pakakao.goods.updatePakakaoGoodsImage", kakaoImage);
	}
	
	public int updatePakakaoGoodsImageCheck(PaKakaoGoodsImage kakaoImage) {
		return  update("pakakao.goods.updatePakakaoGoodsImageCheck", kakaoImage);
	}
	
	public int updatePakakaoGoodsForImage(PaKakaoGoodsImage kakaoImage) {
		return  update("pakakao.goods.updatePakakaoGoodsForImage", kakaoImage);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("pakakao.goods.selectPaGoodsdtInfoList", paramMap.get());
	}
	
	public int updatePaKakaoGoods(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaKakaoGoods", paKakaoGoods);
	}
	
	public int updatePaGoodsImage(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaGoodsImage", paKakaoGoods);
	}
	
	public int updatePaGoodsPriceApply(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaGoodsPriceApply", paKakaoGoods);
	}
	
	public int updatePaGoodsPrice(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaGoodsPrice", paKakaoGoods);
	}
	
	public int updatePaGoodsTarget(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaGoodsTarget", paKakaoGoods);
	}
	
	public int updatePaGoodsOffer(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaGoodsOffer", paKakaoGoods);
	}
	
	public int updatePaPromoGoodsPrice(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return update("pakakao.goods.updatePaPromoGoodsPrice", paKakaoGoods);
	}
	
	public int updatePaKakaoGoodsdt(PaGoodsdtMapping goodsdtMapping) throws Exception {
		return update("pakakao.goods.updatePaKakaoGoodsdt", goodsdtMapping);
	}

	@SuppressWarnings("unchecked")
	public List<PaKakaoGoodsVO> selectPaKakaoSellStatusList(ParamMap paramMap) {
		return list ("pakakao.goods.selectPaKakaoSellStatus", paramMap.get());
	}

	public int updatePakakaoGoodsSellStatus(PaKakaoGoodsVO paKakaoGoods) {
		return update("pakakao.goods.updatePakakaoGoodsSellStatus", paKakaoGoods);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaKakaoGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) {
		return list("pakakao.goods.selectPaGoodsdtStockList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaGoodsdtStockMappingList(PaKakaoGoodsVO paKakaoGoods) {
		return list("pakakao.goods.selectPaGoodsdtStockMappingList", paKakaoGoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaKakaoGoodsVO> selectPaGoodsdtMappingId(ParamMap paramMap) {
		return list("pakakao.goods.selectPaGoodsdtMappingId", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaGoodsdtMappingIdList(PaKakaoGoodsVO paKakaoGoods) {
		return list("pakakao.goods.selectPaGoodsdtMappingIdList", paKakaoGoods);
	}

	public int updatePaKakaoGoodsdtMappingId(PaGoodsdtMapping paGoodsdtMapping) {
		return update("pakakao.goods.updatePaKakaoGoodsdtMappingId", paGoodsdtMapping);
	}

	public int updatePaKakaoGoodsError(PaKakaoGoodsVO paKakaoGoods) {
		return update("pakakao.goods.updatePaKakaoGoodsError", paKakaoGoods);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaNoticeData(PaKakaoGoodsVO vo){
		return (HashMap<String, Object>) selectByPk("pakakao.goods.selectPaNoticeData", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaKakaoTalkDealVO> selectPaKakaoDealInfoList(ParamMap paramMap){
		return list("pakakao.goods.selectPaKakaoDealInfo", paramMap.get());
	}
	
	public int updatePaKakaoTalkDeal(PaKakaoTalkDealVO dealData) {
		return update("pakakao.goods.updatePaKakaoTalkDeal", dealData);
	}
	
	public int updatePaKakaoTalkDealEnd(PaKakaoGoodsVO paKakaoGoods) {
		return update("pakakao.goods.updatePaKakaoTalkDealEnd", paKakaoGoods);
	}
	
	public int updatePaKakaoTalkDealId(PaKakaoTalkDealVO dealData) {
		return update("pakakao.goods.updatePaKakaoTalkDealId", dealData);
	}

	public PaKakaoTalkDeal selectPaKakaoDealInfo(ParamMap paramMap) {
		return (PaKakaoTalkDeal) selectByPk("pakakao.goods.selectPaKakaoDealInfo", paramMap.get());
	}

	public String selectKakaoOrderGoodsDtName(Map<String, Object> map) throws Exception {
		return (String) selectByPk("pakakao.goods.selectKakaoOrderGoodsDtName", map);
	}

}
