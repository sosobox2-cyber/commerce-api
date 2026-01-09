package com.cware.netshopping.palton.goods.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.palton.goods.process.PaLtonGoodsProcess;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;

@Service("palton.goods.paLtonGoodsService")
public class PaLtonGoodsServiceImpl extends AbstractService implements PaLtonGoodsService{
	
	@Autowired 
	PaLtonGoodsProcess paLtonGoodsProcess;

	@Override
	public PaEntpSlip selectPaLtonEntpSlip(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonEntpSlip(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectPaLtonEntpSlipList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonEntpSlipList(paramMap);
	}

	@Override
	public PaLtonGoodsVO selectPaLtonGoodsInfo(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsOffer> selectPaLtonGoodsOfferList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsOfferList(paramMap);
	}

	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsdtInfoList(paramMap);
	}

	@Override
	public int insertPaLtonGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paLtonGoodsProcess.insertPaLtonGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePaLtonGoodsTx(PaLtonGoodsVO paLtonGoods, List<PaLtonGoodsdtMappingVO> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paLtonGoodsProcess.savePaLtonGoods(paLtonGoods, goodsdtMapping, paPromoTargetList);
	}

	@Override
	public List<HashMap<String, String>> selectPaApprovalStatusList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaApprovalStatusList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectEmptyPaOptionCodeList(paramMap);
	}

	@Override
	public String updateGoodsdtPaOptionCode(PaLtonGoodsdtMappingVO ltonGoodsDtMapping) throws Exception {
		return paLtonGoodsProcess.updateGoodsdtPaOptionCode(ltonGoodsDtMapping);
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonSellStateList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonSellStateList(paramMap);
	}
	
	@Override
	public String savePaLtonGoodsSellTx(PaLtonGoodsVO paLtonGoods) throws Exception {
		return paLtonGoodsProcess.savePaLtonGoodsSell(paLtonGoods);
	}

	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsdtMappingStockList(paramMap);
	}

	@Override
	public String savePaLtonGoodsStockTx(List<PaLtonGoodsdtMappingVO> paGoodsdtList) throws Exception {
		return paLtonGoodsProcess.savePaLtonGoodsStock(paGoodsdtList);
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsdtMappingStock(paramMap);
	}

	@Override
	public String updatePaLtonApprovalStatus(PaLtonGoodsVO paltonGoodsVo) throws Exception {
		return paLtonGoodsProcess.updatePaLtonApprovalStatus(paltonGoodsVo);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellState(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonDtSellState(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellStateList(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonDtSellStateList(paramMap);
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaPromoTarget(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaLtonGoodsTrans(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsTrans(paramMap);
	}

	@Override
	public int updatePaLtonGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception {
		return paLtonGoodsProcess.updatePaLtonGoodsFail(paGoodsTarget);
	}

	@Override
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoListMass(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.selectPaLtonGoodsInfoListMass(paramMap);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paLtonGoodsProcess.updateMassTargetYnByEpCode(massMap);
	}

	@Override
	public int updateMassTargetYn(PaLtonGoodsVO paltonGoods) throws Exception {
		return paLtonGoodsProcess.updateMassTargetYn(paltonGoods);
	}

	@Override
	public int updatePaLtonPaStatus(ParamMap paramMap) throws Exception {
		return paLtonGoodsProcess.updatePaLtonPaStatus(paramMap);
	}
}
