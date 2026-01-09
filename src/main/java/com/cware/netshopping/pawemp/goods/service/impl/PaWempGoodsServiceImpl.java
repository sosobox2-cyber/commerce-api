package com.cware.netshopping.pawemp.goods.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.pawemp.goods.process.PaWempGoodsProcess;
import com.cware.netshopping.pawemp.goods.service.PaWempGoodsService;

@Service("pawemp.goods.paWempGoodsService")
public class PaWempGoodsServiceImpl extends AbstractService implements  PaWempGoodsService {
	@Resource(name = "pawemp.goods.paWempGoodsProcess")
	private PaWempGoodsProcess paWempGoodsProcess;

	public PaWempGoodsVO selectPaWempGoodsInfo(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsInfo(paramMap);
	}

	public List<PaGoodsdtMapping> selectPaWempGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsdtInfoList(paramMap);
	}
	
	public List<PaGoodsOfferVO> selectPaWempGoodsOfferList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsOfferList(paramMap);
	}

	public String savePaWempGoodsTx(PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paWempGoodsProcess.savePaWempGoods(paWempGoods, goodsdtMapping, paPromoTargetList);
	}

	public PaWempEntpSlip selectPaWempEntpSlip(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempEntpSlip(paramMap);
	}
	
	public List<PaWempEntpSlip> selectPaWempEntpSlipList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempEntpSlipList(paramMap);
	}

	public int insertPaWempGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paWempGoodsProcess.insertPaWempGoodsTransLog(paGoodsTransLog);
	}
	
	public List<PaWempGoodsVO> selectPaWempGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsSaleStopList(paramMap);
	}
	
	public List<PaWempGoodsVO> selectPaWempGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsSaleRestartList(paramMap);
	}
	
	@Override
	public PaWempGoodsVO selectPaWempGoodsProductNo(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsProductNo(paramMap);
	}

	@Override
	public String savePaWempGoodsSellTx(PaWempGoodsVO paWempGoods) throws Exception {
		return paWempGoodsProcess.savePaWempGoodsSell(paWempGoods);
	}

	@Override
	public List<PaWempGoodsdtMappingVO> selectPaWempGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public String savePaWempGoodsGroupNoticeNoTx(ParamMap paramMap)throws Exception{
		return paWempGoodsProcess.savePaWempGoodsGroupNoticeNoTx(paramMap);
	}

	@Override
	public String savePaWempGoodsdtOrderAbleQtyTx(List<PaGoodsdtMapping> paGoodsdtMappingList, ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.savePaWempGoodsdtOrderAbleQty(paGoodsdtMappingList, paramMap);
	}

	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsInfoList(ParamMap paramMap)throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsStockList(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsStockList(paramMap);
	}
	
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaPromoTarget(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaWempGoodsTrans(ParamMap paramMap) throws Exception{
		return paWempGoodsProcess.selectPaWempGoodsTrans(paramMap);
	}

	@Override
	public int updatePaWempGoodsFail(HashMap<String, String> paGoods) throws Exception {
		return paWempGoodsProcess.updatePaWempGoodsFail(paGoods);
	}

	@Override
	public List<PaWempGoodsVO> selectPaWempGoodsInfoListMass(ParamMap paramMap) throws Exception {
		return paWempGoodsProcess.selectPaWempGoodsInfoListMass(paramMap);
	}

	@Override
	public int updateMassTargetYn(PaWempGoodsVO paWempGoods) throws Exception {
		return paWempGoodsProcess.updateMassTargetYn(paWempGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paWempGoodsProcess.updateMassTargetYnByEpCode(massMap);
	}
	
}
