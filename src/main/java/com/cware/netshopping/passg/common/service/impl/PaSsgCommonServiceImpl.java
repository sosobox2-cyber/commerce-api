package com.cware.netshopping.passg.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsLimitChar;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.domain.model.PaSsgDisplayCategory;
import com.cware.netshopping.domain.model.PaSsgDisplayRecommendCategory;
import com.cware.netshopping.domain.model.PaSsgGoodsCert;
import com.cware.netshopping.domain.model.PaSsgSettlement;
import com.cware.netshopping.passg.common.process.PaSsgCommonProcess;
import com.cware.netshopping.passg.common.service.PaSsgCommonService;

@Service("passg.common.paSsgCommonService")
public class PaSsgCommonServiceImpl  extends AbstractService implements PaSsgCommonService {

	@Resource(name = "passg.common.paSsgCommonProcess")
    private PaSsgCommonProcess paSsgCommonProcess;

	@Override
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception {
		return paSsgCommonProcess.selectEntpSlipCost(apiInfoMap);
	}

	@Override
	public String savePaSsgShipCostTx(ParamMap shipCostMap) throws Exception {
		return paSsgCommonProcess.savePaSsgShipCost(shipCostMap);
	}
	
	@Override
	public void savePaOfferCodeListTx(List<PaOfferCode> paOfferCodenList) throws Exception{
		paSsgCommonProcess.savePaOfferCodeList(paOfferCodenList);
	}
	
	@Override
	public void savePaBrandTx(List<PaBrand> paBrandList) throws Exception {
		paSsgCommonProcess.savePaBrand(paBrandList);
	}
	
	@Override
	public void savePaOrigindTx(List<PaOrigin> paOriginList) throws Exception {
		paSsgCommonProcess.savePaOrigin(paOriginList);
	}
	
	@Override
	public void savePaGoodsLimitCharTx(List<PaGoodsLimitChar> paGoodsLimitCharList) throws Exception {
		paSsgCommonProcess.savePaGoodsLimitChar(paGoodsLimitCharList);
	}
	
	@Override
	public void savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		paSsgCommonProcess.savePaGoodsKinds(paGoodsKindsList);
	}
	
	//@Override
	//public List<HashMap<String, Object>> selectSsgStandardCategoryList( ) throws Exception {
	//	return paSsgCommonProcess.selectSsgStandardCategoryList();
	//}
	
	@Override
	public void savePaSsgDisplayCategoryTx(List<PaSsgDisplayCategory> paSsgDisplayCategoryList) throws Exception {
		paSsgCommonProcess.savePaSsgDisplayCategory(paSsgDisplayCategoryList);
	}

	@Override
	public Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paSsgCommonProcess.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaSsgEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paSsgCommonProcess.savePaSsgEntpSlip(paEntpSlip);
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception {
		return paSsgCommonProcess.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaSsgEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paSsgCommonProcess.updatePaSsgEntpSlip(paEntpSlip);
	}

	@Override
	public List<PaGoodsKinds> selectGoodsKindsList() throws Exception {
		return paSsgCommonProcess.selectGoodsKindsList();
	}

	@Override
	public String savePaSsgDisplayRecommendCategoryTx(
			List<PaSsgDisplayRecommendCategory> paSsgDisplayRecommendCategoryList) throws Exception {
		return paSsgCommonProcess.savePaSsgDisplayRecommendCategory(paSsgDisplayRecommendCategoryList);
	}

	@Override
	public List<PaGoodsKinds> selectSsgGoodsCertList() throws Exception {
		return paSsgCommonProcess.selectSsgGoodsCertList();
	}

	@Override
	public String savePaSsgGoodsCertTx(List<PaSsgGoodsCert> paSsgGoodsCertList) throws Exception {
		return paSsgCommonProcess.savePaSsgGoodsCert(paSsgGoodsCertList);
	}

	@Override
	public String savePaSsgSettlementTx(List<PaSsgSettlement> paSsgSettlementList, ParamMap paramMap, int page) throws Exception {
		return paSsgCommonProcess.savePaSsgSettlement(paSsgSettlementList,paramMap, page);
	}
	
}
