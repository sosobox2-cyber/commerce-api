package com.cware.netshopping.pakakao.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;
import com.cware.netshopping.pakakao.goods.process.PaKakaoGoodsProcess;
import com.cware.netshopping.pakakao.goods.service.PaKakaoGoodsService;

@Service("pakakao.goods.paKakaoGoodsService")
public class PaKakaoGoodsServiceImpl extends AbstractService implements PaKakaoGoodsService{
	
	@Autowired 
	PaKakaoGoodsProcess paKakaoGoodsProcess;

	@Override
	public PaKakaoGoodsVO selectPaKakaoGoodsInfo(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaKakaoGoodsInfo(paramMap);
	}
	
	@Override
	public List<PaKakaoGoodsVO> selectPaKakaoGoodsInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaKakaoGoodsInfoList(paramMap);
	}

	@Override
	public List<PaKakaoGoodsImage> selectKakaoGoodsImage(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectKakaoGoodsImage(paramMap);
	}

	@Override
	public int updatePakakaoGoodsImage(PaKakaoGoodsImage kakaoImage) throws Exception {
		return paKakaoGoodsProcess.updatePakakaoGoodsImage(kakaoImage);
	}
	
	@Override
	public int updatePakakaoGoodsImageCheckTx(PaKakaoGoodsImage kakaoImage) throws Exception {
		return paKakaoGoodsProcess.updatePakakaoGoodsImageCheck(kakaoImage);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaGoodsdtInfoList(paramMap);
	}
	
	@Override
	public String savePaKakaoGoodsTx(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> goodsdtMapping, PaKakaoTalkDealVO dealData) throws Exception {
		return paKakaoGoodsProcess.savePaKakaoGoods(paKakaoGoods, goodsdtMapping, dealData);
	}

	@Override
	public List<PaKakaoGoodsVO> selectPaKakaoSellStatusList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaKakaoSellStatusList(paramMap);
	}

	@Override
	public int updatePakakaoGoodsSellStatus(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsProcess.updatePakakaoGoodsSellStatus(paKakaoGoods);
	}
	
	@Override
	public List<PaKakaoGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaGoodsdtStockList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtStockMappingList(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsProcess.selectPaGoodsdtStockMappingList(paKakaoGoods);
	}
	
	@Override
	public void updatePaGoodsdtMappingQtyTx(List<PaGoodsdtMapping> paGoodsdtList) throws Exception {
		paKakaoGoodsProcess.updatePaGoodsdtMappingQtyTx(paGoodsdtList);
	}

	@Override
	public List<PaKakaoGoodsVO> selectPaGoodsdtMappingId(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaGoodsdtMappingId(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtMappingIdList(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsProcess.selectPaGoodsdtMappingIdList(paKakaoGoods);
	}

	@Override
	public void updatePaKakaoGoodsdtMappingIdTx(List<PaGoodsdtMapping> paGoodsdtMappingList) throws Exception {
		paKakaoGoodsProcess.updatePaKakaoGoodsdtMappingId(paGoodsdtMappingList);
	}

	@Override
	public void savePaKakaoGoodsError(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		paKakaoGoodsProcess.savePaKakaoGoodsError(paKakaoGoods);
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(PaKakaoGoodsVO vo) throws Exception {
		return paKakaoGoodsProcess.selectPaNoticeData(vo);
	}
	
	@Override
	public List<PaKakaoTalkDealVO> selectPaKakaoDealInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaKakaoDealInfoList(paramMap);
	}
	
	@Override
	public void updatePaKakaoTalkDealId(PaKakaoTalkDealVO dealData) throws Exception {
		paKakaoGoodsProcess.updatePaKakaoTalkDealId(dealData);
	}

	@Override
	public PaKakaoTalkDeal selectPaKakaoDealInfo(ParamMap paramMap) throws Exception {
		return paKakaoGoodsProcess.selectPaKakaoDealInfo(paramMap);
	}

	@Override
	public String selectKakaoOrderGoodsDtName(Map<String, Object> map) throws Exception {
		return paKakaoGoodsProcess.selectKakaoOrderGoodsDtName(map);
	}
	
}
