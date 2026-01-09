package com.cware.netshopping.pahalf.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfGoodsVO;
import com.cware.netshopping.domain.PaHalfGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaHalfGoods;
import com.cware.netshopping.domain.model.PaHalfGoodsdtMapping;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pahalf.goods.process.PaHalfGoodsProcess;
import com.cware.netshopping.pahalf.goods.service.PaHalfGoodsService;

@Service("paHalf.goods.paHalfGoodsService")
public class PaHalfGoodsServiceImpl implements PaHalfGoodsService {

	@Resource(name = "pahalf.goods.paHalfGoodsProcess")
	private PaHalfGoodsProcess paHalfGoodsProcess;

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;

	@Override
	public List<PaHalfGoods> selectPaHalfSellStateList(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfSellStateList(paramMap);
	}

	@Override
	public int savePaHalfGoodsSell(PaHalfGoods paHalfGoods) throws Exception {
		return paHalfGoodsProcess.savePaHalfGoodsSell(paHalfGoods);
	}

	@Override
	public List<PaHalfGoodsdtMappingVO> selectPaHalfGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return paHalfGoodsProcess.selectPaHalfGoodsdtMappingStockList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaHalfStockInfoList(ParamMap paramMap) throws Exception{
		return paHalfGoodsProcess.selectPaHalfStockInfoList(paramMap);
	}
    
	@Override
	public PaHalfGoodsdtMapping selectCheckPaOptionCode(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectCheckPaOptionCode(paramMap);
	}

	@Override
	public void updateTpaGoodsdtMapping(PaHalfGoodsdtMapping paGoodsdtMapping) throws Exception {
		paHalfGoodsProcess.updateTpaGoodsdtMapping(paGoodsdtMapping);
	}

	@Override
	public void savePaHalfGoodsdtStock(List<Map<String, Object>> paHalfGoodsdtMappingList) throws Exception{
		paHalfGoodsProcess.savePaHalfGoodsdtStock(paHalfGoodsdtMappingList);
		
	}

	@Override
	public int insertPaHalfGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception{
		return paHalfGoodsProcess.insertPaHalfGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public List<HashMap<String, String>> selectPaHalfGoodsTrans(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsTrans(paramMap);
	}

	@Override
	public int goodsValidationCheck(String paCode, String goodsCode) throws Exception {
		return paHalfGoodsProcess.goodsValidationCheck(paCode, goodsCode);
	}

	@Override
	public int saveHalfGoodsShipCostCode(Map<String, String> goodsSlipMap) throws Exception {
		return paHalfGoodsProcess.saveHalfGoodsShipCostCode(goodsSlipMap);
	}

	@Override
	public List<PaHalfGoodsVO> selectPaHalfGoodsInfo(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsInfo(paramMap);
	}

	@Override
	public List<PaHalfGoodsdtMapping> selectPaHalfGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return paHalfGoodsProcess.selectPaHalfGoodsdtInfoList(paramMap);
	}

	@Override
	public List<PaGoodsOffer> selectPaHalfGoodsOfferList(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsOfferList(paramMap);
	}

	@Override
	public void setGoodsDtInfo(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping, ParamMap paramMap ,boolean isUpdate) throws Exception {
		paHalfGoodsProcess.setGoodsDtInfo(paHalfGoods, goodsdtMapping, paramMap , isUpdate);
	}

	@Override
	public void setGoodsContentsInfo(PaHalfGoodsVO paHalfGoods, List<PaGoodsOffer> paHalfGoodsOffer, ParamMap apiDataMap, boolean isUpdate) throws Exception {
		paHalfGoodsProcess.setGoodsContentsInfo(paHalfGoods,paHalfGoodsOffer , apiDataMap, isUpdate );
	}

	@Override
	public List<PaHalfGoodsVO> selectPaHalfGoodsImageList(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsImageList(paramMap);
	}

	@Override
	public void updatePaHalfGoodsImage(PaHalfGoodsVO paHalfGoods) throws Exception {
		paHalfGoodsProcess.updatePaHalfGoodsImage(paHalfGoods);
	}
	
	@Override
	public Map<String, String> selectGoodsEntpSlip(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectGoodsEntpSlip(paramMap);
	}

	@Override
	public String savePaHalfGoodsTx(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping, ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.savePaHalfGoods(paHalfGoods, goodsdtMapping, paramMap);
	}

	@Override
	public PaHalfGoodsVO selectPaHalfGoodsContentsInfo(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsContentsInfo(paramMap);
	}

	@Override
	public void checkGoodsInsertFail(ParamMap paramMap) throws Exception {
		paHalfGoodsProcess.checkGoodsInsertFail(paramMap);
	}

	@Override
	public void updatePaHalfGoodsOffer(PaHalfGoodsVO paHalfGoods) throws Exception {
		paHalfGoodsProcess.updatePaHalfGoodsOffer(paHalfGoods);
	}

	@Override
	public void updateEntpSlipInsertFail(PaHalfGoodsVO paHalfGoods) throws Exception {
		paHalfGoodsProcess.updateEntpSlipInsertFail(paHalfGoods);
	}

	/*
	 *
	@Override
	public String selectPaHalfBrandNo(PaHalfGoodsVO paHalfGoods) throws Exception {
		return paHalfGoodsProcess.selectPaHalfBrandNo(paHalfGoods);
	}
	*/

	 @Override
	public PaHalfGoodsVO selectPrdPrcNoList(ParamMap paramMap) throws Exception{
		return paHalfGoodsProcess.selectPrdPrcNoList(paramMap);
	}
	

	@Override
	public void updatePrdPrcNo(PaHalfGoodsVO paHalfGoodsVO) throws Exception {
		paHalfGoodsProcess.updatePrdPrcNo(paHalfGoodsVO);
		
	}

	@Override
	public void updatePaHalfGoodsReset(ParamMap paramMap) throws Exception {
		paHalfGoodsProcess.updatePaHalfGoodsReset(paramMap);
	}

	@Override
	public void savePrdPlcyDtInfoModifyTx(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMappingList) throws Exception {
		paHalfGoodsProcess.savePrdPlcyDtInfoModify(paHalfGoods, goodsdtMappingList);
	}

	@Override
	public void updatePaHalfGoodsReturnNote(PaHalfGoodsVO paHalfGoods, ParamMap paramMap) throws Exception {
		paHalfGoodsProcess.updatePaHalfGoodsReturnNote(paHalfGoods, paramMap);
	}

	@Override
	public String selectDelyNoAreaGb(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectDelyNoAreaGb(paramMap);
	}

	@Override
	public PaHalfGoodsVO selectPaHalfGoodsStatus(ParamMap paramMap) throws Exception {
		return paHalfGoodsProcess.selectPaHalfGoodsStatus(paramMap);
	}

	@Override
	public void updateProceeding(ParamMap paramMap) throws Exception {
		paHalfGoodsProcess.updateProceeding(paramMap);
		
	}

	@Override
	public void updateClearProceeding(ParamMap paramMap) throws Exception {
		paHalfGoodsProcess.updateClearProceeding(paramMap);
		
	}


}
