package com.cware.netshopping.paintp.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.paintp.goods.process.PaIntpGoodsProcess;
import com.cware.netshopping.paintp.goods.service.PaIntpGoodsService;

@Service("paintp.goods.paIntpGoodsService")
public class paIntpGoodsServiceImpl extends AbstractService implements PaIntpGoodsService {

	@Resource(name = "paintp.goods.paIntpGoodsProcess")
    private PaIntpGoodsProcess paIntpGoodsProcess;
	
	@Override
	public PaEntpSlip selectPaIntpEntpSlip(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpEntpSlip(paramMap);
	}
	
	@Override
	public PaIntpGoodsVO selectPaIntpGoodsInfo(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsInfo(paramMap);
	}
	
	@Override
	public List<PaGoodsOffer> selectPaIntpGoodsOfferList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaIntpGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsdtInfoList(paramMap);
	}
	
	@Override
	public int insertPaIntpGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paIntpGoodsProcess.insertPaIntpGoodsTransLog(paGoodsTransLog);
	}
	
	@Override
	public String savePaIntpGoodsTx(PaIntpGoodsVO paIntpGoods, List<PaGoodsdtMapping> paGoodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paIntpGoodsProcess.savePaIntpGoods(paIntpGoods, paGoodsdtMapping, paPromoTargetList);
	}
	
	@Override
	public String savePaIntpFailGoodsTx(PaIntpGoodsVO paIntpGoods) throws Exception {
		return paIntpGoodsProcess.savePaIntpFailGoods(paIntpGoods);
	}
	
	@Override
	public List<PaEntpSlip> selectPaIntpEntpSlipList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpEntpSlipList(paramMap);
	}
	
	@Override
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsInfoList(paramMap);
	}
	
	@Override
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public String savePaIntpGoodsStockTx(List<PaIntpGoodsdtMappingVO> paIntpGoodsMapping) throws Exception {
		return paIntpGoodsProcess.savePaIntpGoodsStock(paIntpGoodsMapping);
	}
	
	@Override
	public List<PaIntpGoodsdtMappingVO> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectEmptyPaOptionCodeList(paramMap);
	}
	
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaPromoTarget(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(String paCode) throws Exception {
		return paIntpGoodsProcess.selectGoodsLimitCharList(paCode);
	}

	@Override
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsdtMappingStock(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaIntpGoodsTrans(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsTrans(paramMap);
	}

	@Override
	public int updatePaIntpGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception {
		return paIntpGoodsProcess.updatePaIntpGoodsFail(paGoodsTarget);
	}

	@Override
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoListMass(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaIntpGoodsInfoListMass(paramMap);
	}

	@Override
	public int updateMassTargetYn(PaIntpGoodsVO paIntpGoods) throws Exception {
		return paIntpGoodsProcess.updateMassTargetYn(paIntpGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paIntpGoodsProcess.updateMassTargetYnByEpCode(massMap);
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return paIntpGoodsProcess.selectPaNoticeData(paramMap);
	}
	
}