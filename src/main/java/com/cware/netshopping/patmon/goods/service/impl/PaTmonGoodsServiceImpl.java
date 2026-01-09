package com.cware.netshopping.patmon.goods.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.patmon.goods.process.PaTmonGoodsProcess;
import com.cware.netshopping.patmon.goods.service.PaTmonGoodsService;

@Service("patmon.goods.paTmonGoodsService")
public class PaTmonGoodsServiceImpl extends AbstractService implements PaTmonGoodsService{

	@Autowired 
	PaTmonGoodsProcess paTmonGoodsProcess;
	
	@Override
	public List<PaEntpSlip> selectPaTmonEntpSlip(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonEntpSlip(paramMap);
	}

	@Override
	public PaTmonGoodsVO selectPaTmonGoodsInfo(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsOfferVO> selectPaTmonGoodsOfferList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaTmonGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsdtInfoList(paramMap);
	}
	
	@Override
	public String savePaTmonGoodsTx(PaTmonGoodsVO paTmonGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paTmonGoodsProcess.savePaTmonGoods(paTmonGoods, goodsdtMapping, paPromoTargetList);
	}

	@Override
	public int  insertPaTmonGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paTmonGoodsProcess.insertPaTmonGoodsTransLog(paGoodsTransLog);
	}
	
	@Override
	public List<PaTmonGoodsVO> selectPaTmonSellStateList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonSellStateList(paramMap);
	}

	@Override
	public String updateTmonGoodsStatus(PaTmonGoodsVO sellStateTarget) throws Exception {
		return paTmonGoodsProcess.updateTmonGoodsStatus(sellStateTarget);
	}
	
	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockMappingList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsdtStockMappingList(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectPaTmonEntpSlipList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonEntpSlipList(paramMap);
	}

	@Override
	public List<PaTmonGoodsVO> selectPaTmonGoodsInfoList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsInfoList(paramMap);
	}

	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsdtStockList(paramMap);
	}

	@Override
	public String updatePaTmonGoodsdtMappingQtyTx(List<PaTmonGoodsdtMappingVO> paGoodsdtList) throws Exception {
		return paTmonGoodsProcess.updatePaTmonGoodsdtMappingQty(paGoodsdtList);
	}

	@Override
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtAddedList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonGoodsdtAddedList(paramMap);
	}

	@Override
	public String updatePaTmonGoodsdtMappingAddedTx(List<PaTmonGoodsdtMappingVO> goodsAddedMapping) throws Exception {
		return paTmonGoodsProcess.updatePaTmonGoodsdtMappingAdded(goodsAddedMapping);
	}
	
	@Override
	public List<PaTmonGoodsVO> selectPaTmonModifyOptionList(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaTmonModifyOptionList(paramMap);
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return paTmonGoodsProcess.selectPaPromoTarget(paramMap);
	}

	@Override
	public String savePaTmonGoodsErrorTx(PaTmonGoodsVO paTmonGoods) throws Exception {
		return paTmonGoodsProcess.savePaTmonGoodsError(paTmonGoods);
	}

	@Override
	public int selectPaTmonExceptShipPolicy(String goodsCode) throws Exception {
		return paTmonGoodsProcess.selectPaTmonExceptShipPolicy(goodsCode);
	}
}
