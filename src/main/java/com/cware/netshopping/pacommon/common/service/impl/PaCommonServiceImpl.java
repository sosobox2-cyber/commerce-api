package com.cware.netshopping.pacommon.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTargetRec;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.SpAuthTransInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDescInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDtInfo;
import com.cware.netshopping.domain.model.SpPaGoodsInfo;
import com.cware.netshopping.domain.model.SpPaOfferInfo;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.domain.PaNoticeApplyVO;

@Service("pacommon.common.pacommonService")
public class PaCommonServiceImpl extends AbstractService implements PaCommonService {

	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Override
	public String selectCheckOpenApiCode(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCheckOpenApiCode(paramMap);
	}

	@Override
	public List<PaGoodsTargetRec> selectPaGoodsInsertTarget(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsInsertTarget(paramMap);
	}

	@Override
	public int selectEtvMarginCheck() throws Exception {
		return pacommonProcess.selectEtvMarginCheck();
	}

	@Override
	public String selectPaOfferCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectPaOfferCheck(paGoodstarget);
	}

	@Override
	public String selectOriginMappingNaver(String originCode) throws Exception {
		return pacommonProcess.selectOriginMappingNaver(originCode);
	}

	@Override
	public String selectOriginMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectOriginMapping(paGoodstarget);
	}

	@Override
	public String selectImageCheck(String goodsCode) throws Exception {
		return pacommonProcess.selectImageCheck(goodsCode);
	}

	@Override
	public String selectPaDescribeCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectPaDescribeCheck(paGoodstarget);
	}

	@Override
	public String selectPaStockCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectPaStockCheck(paGoodstarget);
	}

	@Override
	public String selectPaShipCostCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectPaShipCostCheck(paGoodstarget);
	}

	@Override
	public String selectChkMinMarSale(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectChkMinMarSale(paGoodstarget);
	}

	@Override
	public String selectBrandMapping11st(String brandCode) throws Exception {
		return pacommonProcess.selectBrandMapping11st(brandCode);
	}

	@Override
	public String selectBrandMappingGmkt(String brandCode) throws Exception {
		return pacommonProcess.selectBrandMappingGmkt(brandCode);
	}

	@Override
	public String selectMakerMappingGmkt(String makecoCode) throws Exception {
		return pacommonProcess.selectMakerMappingGmkt(makecoCode);
	}

	@Override
	public String selectBrandMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectBrandMapping(paGoodstarget);
	}

	@Override
	public String selectPaOriginCheck(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaOriginCheck(paramMap);
	}

	@Override
	public String selectPaMobGiftGbCheck(String goodsCode) throws Exception {
		return pacommonProcess.selectPaMobGiftGbCheck(goodsCode);
	}

	@Override
	public String selectEntpuserCheck(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectEntpuserCheck(paramMap);
	}

	@Override
	public String selectPaGoodsdtCnt(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsdtCnt(paramMap);
	}

	@Override
	public String selectPaGoodsdtLength(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsdtLength(paramMap);
	}

	@Override
	public String saveTargetTx(PaGoodsTargetRec paGoodstarget, ParamMap paGoodsInfo) throws Exception {
		return pacommonProcess.saveTarget(paGoodstarget, paGoodsInfo);
	}

	@Override
	public String selectOriginMapping11st(String originCode) throws Exception {
		return pacommonProcess.selectOriginMapping11st(originCode);
	}

	@Override
	public String selectOriginMappingGmkt(String originCode) throws Exception {
		return pacommonProcess.selectOriginMappingGmkt(originCode);
	}

	@Override
	public int selectPaCopnCheckGoodsOffer(String goodsCode) throws Exception {
		return pacommonProcess.selectPaCopnCheckGoodsOffer(goodsCode);
	}

	@Override
	public String selectPaCopnPaOfferBo(String goodsCode) throws Exception {
		return pacommonProcess.selectPaCopnPaOfferBo(goodsCode);
	}

	@Override
	public int selectCheckPaCopnAttr(ParamMap paGoodsInfo) throws Exception {
		return pacommonProcess.selectCheckPaCopnAttr(paGoodsInfo);
	}

	@Override
	public String selectPaCopnPaOffer(String goodsCode) throws Exception {
		return pacommonProcess.selectPaCopnPaOffer(goodsCode);
	}

	@Override
	public int selectCheckPaCopnAttrEtc(ParamMap paGoodsInfo) throws Exception {
		return pacommonProcess.selectCheckPaCopnAttrEtc(paGoodsInfo);
	}

	@Override
	public String selectPaCopnAttrEtcName(ParamMap paGoodsInfo) throws Exception {
		return pacommonProcess.selectPaCopnAttrEtcName(paGoodsInfo);
	}

	@Override
	public int inertPaGoodsQaLog(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.inertPaGoodsQaLog(paGoodstarget);
	}

	@Override
	public int deletePaGoodsTarget(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.deletePaGoodsTarget(paGoodstarget);
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectOfferInfoList(paramMap);
	}

	@Override
	public String saveOfferInfoTx(SpPaOfferInfo paOfferData, Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleStopSet) throws Exception {
		return pacommonProcess.saveOfferInfo(paOfferData, transTargetSet, saleStopSet);
	}

	@Override
	public List<SpPaGoodsInfo> selectGoodsInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectGoodsInfoList(paramMap);
	}

	@Override
	public String saveGoodsInfoTx(SpPaGoodsInfo paGoodsData) throws Exception {
		return pacommonProcess.saveGoodsInfo(paGoodsData);
	}

	@Override
	public List<SpPaGoodsDtInfo> selectGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectGoodsDtInfoList(paramMap);
	}

	@Override
	public String saveGoodsDtInfoTx(SpPaGoodsDtInfo paGoodsDtInfo, String flag) throws Exception {
		return pacommonProcess.saveGoodsDtInfo(paGoodsDtInfo, flag);
	}
	
	@Override
	public List<SpPaGoodsDescInfo> selectPaGoodsDescInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsDescInfoList(paramMap);
	}

	@Override
	public String saveGoodsDescInfoTx(SpPaGoodsDescInfo paGoodsDescInfo) throws Exception {
		return pacommonProcess.saveGoodsDescInfo(paGoodsDescInfo);
	}

	@Override
	public List<HashMap<String, String>> selectSaleEndDateTargetList() throws Exception {
		return pacommonProcess.selectSaleEndDateTargetList();
	}

	@Override
	public String saveSaleEndDateTargetInfoTx(HashMap<String, String> saleEndDateTarget) throws Exception {
		return pacommonProcess.saveSaleEndDateTargetInfo(saleEndDateTarget);
	}

	@Override
	public List<SpAuthTransInfo> selectGoodsAuthTransList() throws Exception {
		return pacommonProcess.selectGoodsAuthTransList();
	}

	@Override
	public String saveGoodsAuthTransInfoTx(SpAuthTransInfo authTransInfo) throws Exception {
		return pacommonProcess.saveGoodsAuthTransInfo(authTransInfo);
	}

	@Override
	public List<HashMap<String, String>> selectGoodsEventTransInfoList() throws Exception {
		return pacommonProcess.selectGoodsEventTransInfoList();
	}

	@Override
	public String saveGoodsEventTransInfoTx(HashMap<String, String> goodsEventTransData) throws Exception {
		return pacommonProcess.saveGoodsEventTransInfo(goodsEventTransData);
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTargetAllList() throws Exception {
		return pacommonProcess.selectPaPromoTargetAllList();
	}

	@Override
	public String savePaPromoTargetAllTx(PaPromoTarget paPromotargetAllData) throws Exception {
		return pacommonProcess.savePaPromoTargetAll(paPromotargetAllData);
	}
	
	@Override
	public List<PaGoodsImage> selectCurImageInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurImageInfoList(paramMap);
	}
	
	@Override
	public List<PaGoodsPriceVO> selectCurPriceInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurPriceInfoList(paramMap);
	}
	
	@Override
	public List<PaCustShipCostVO> selectCurShipCostInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurShipCostInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurShipStopSaleList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurShipStopSaleList(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectCurEntpSlipInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurEntpSlipInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurSaleStopList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurSaleStopList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEventMarginList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurEventMarginList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCheckDtCntList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurCheckDtCntList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsVO> selectCurGoodsTransQtyList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurGoodsTransQtyList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectCurGoodsDtTransQtyList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurGoodsDtTransQtyList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurStockCheckList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurStockCheckList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEpNameInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurEpNameInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEntpSlipChangeInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurEntpSlipChangeInfoList(paramMap);
	}
	
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurCnShipCostInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurCnShipCostDtInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostTransSingle(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurCnShipCostTransSingle(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostTransMulti(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectCurCnShipCostTransMulti(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectMinMarginPrice(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectMinMarginPrice(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectApplyCnCostSeq(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectApplyCnCostSeq(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectMaxOrdCost(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectMaxOrdCost(paramMap);
	}
	
	@Override
	public String saveCurImageInfoTx(PaGoodsImage curImageInfoTarget) throws Exception {
		return pacommonProcess.saveCurImageInfo(curImageInfoTarget);
	}
	
	@Override
	public String saveCurPriceInfoTx(PaGoodsPriceVO curPriceInfoTarget) throws Exception {
		return pacommonProcess.saveCurPriceInfo(curPriceInfoTarget);
	}
	
	@Override
	public String saveCurShipCostInfoTx(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		return pacommonProcess.saveCurShipCostInfo(curShipCostInfoTarget);
	}
	
	@Override
	public String saveCurEntpSlipInfoTx(PaEntpSlip curEntpSlipInfoTarget) throws Exception {
		return pacommonProcess.saveCurEntpSlipInfo(curEntpSlipInfoTarget);
	}
	
	@Override
	public String saveCurSaleStopInfoTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCurSaleStopInfo(paramMap);
	}
	
	@Override
	public String saveCurGoodsTransQtyTx(PaLtonGoodsVO curGoodsTransQtyTarget) throws Exception {
		return pacommonProcess.saveCurGoodsTransQty(curGoodsTransQtyTarget);
	}
	
	@Override
	public String saveCurGoodsDtTransQtyTx(PaLtonGoodsdtMappingVO curGoodsDtTransQtyTarget) throws Exception {
		return pacommonProcess.saveCurGoodsDtTransQty(curGoodsDtTransQtyTarget);
	}
	
	@Override
	public String saveCurStockCheckTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCurStockCheck(paramMap);
	}	
	
	@Override
	public String saveCurNaverEntpSlipTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCurNaverEntpSlip(paramMap);
	}	
	
	@Override
	public String savePaCustCnShipCostTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.savePaCustCnShipCost(paramMap);
	}
	
	@Override
	public String saveCnCostYnTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCnCostYn(paramMap);
	}
	
	@Override
	public String saveCnCostTransTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCnCostTrans(paramMap);
	}
	
	@Override
	public String saveCnCostTrans2Tx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveCnCostTrans2(paramMap);
	}
	
	@Override
	public String saveStopSaleTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveStopSale(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectGoodsPaExceptList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectGoodsPaExceptList(paramMap);
	}

	@Override
	public String saveGoodsPaExceptTx(HashMap<String, String> goodsPaExceptData) throws Exception {
		return pacommonProcess.saveGoodsPaExcept(goodsPaExceptData);
	}

	@Override
	public List<SpPaGoodsDtInfo> selectPaLtonSsgGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaLtonSsgGoodsDtInfoList(paramMap);
	}

	@Override
	public String updateLtonSsgGoodsDtInfoTx(SpPaGoodsDtInfo paLtonSsgGoodsDtInfo) throws Exception {
		return pacommonProcess.updateLtonSsgGoodsDtInfo(paLtonSsgGoodsDtInfo);
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoListInsert(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectOfferInfoListInsert(paramMap);
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoListUpdate(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectOfferInfoListUpdate(paramMap);
	}

	@Override
	public void saveOfferGoodsTarget(Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleGbStopSet) throws Exception {
		pacommonProcess.saveOfferGoodsTarget(transTargetSet, saleGbStopSet);
	}

	@Override
	public void queryTestFunction() throws Exception {
		pacommonProcess.queryTestFunction();
	}

	@Override
	public List<HashMap<String, String>> selectTmonCurCheckDtCntList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectTmonCurCheckDtCntList(paramMap);
	}

	@Override
	public String paExceptGoodsYn(ParamMap paramMap) throws Exception {
		return pacommonProcess.paExceptGoodsYn(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectTmonCurGoodsNameLengthCheckList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectTmonCurGoodsNameLengthCheckList(paramMap);
	}

	@Override
	public String selectPaGoodsNameLength(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsNameLength(paramMap);
	}

	@Override
	public void checkMassModifyGoods(String paGroupCode) throws Exception {
		pacommonProcess.checkMassModifyGoods(paGroupCode);
	}

	@Override
	public HashMap<String, Object> selectDescData(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectDescData(paramMap);
	}

	@Override
	public List<PaGoodsOfferVO> selectPaGoodsOfferList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsOfferList(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaGoodsdtInfoList(paramMap);
	}
	
	//프로모션
	@Override
	public void savePaPromoTargetHistory(ParamMap paramMap) throws Exception {
		pacommonProcess.savePaPromoTargetHistory(paramMap);
	}
	
	@Override
	public List<PaPromoGoodsPrice> selectPaPromoGoodsPriceList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaPromoGoodsPriceList(paramMap);
	}

	@Override
	public String savePaPromoGoodsPriceTx(PaPromoGoodsPrice paPromoGoodsPrice) throws Exception {
		return pacommonProcess.savePaPromoGoodsPrice(paPromoGoodsPrice);
	}
	
	@Override
	public List<Object> selectPaSoldOutordList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaSoldOutordList(paramMap);
	}
	
	@Override
	public void saveOrderCancelCounselTx(HashMap<String, String> reqMap) throws Exception {
		pacommonProcess.saveOrderCancelCounsel(reqMap);
	}

	@Override
	public String selectPaSsgGoodsOfferCodeCheck(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaSsgGoodsOfferCodeCheck(paramMap);
	}

	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsOfferList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaSsgGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaNoticeApplyVO> selectPaNoticeTargetList() throws Exception {
		return pacommonProcess.selectPaNoticeTargetList();
	}
	
	@Override
	public String savePaNoticeTargetAllTx(PaNoticeApplyVO paNoticeAllData) throws Exception {
		return pacommonProcess.savePaNoticeTargetAll(paNoticeAllData);
	}

	@Override
	public int saveRetentionGoodsTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveRetentionGoods(paramMap);
	}

	@Override
	public String selectMakerMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonProcess.selectMakerMapping(paGoodstarget);
	}
	
	@Override
	public String saveSsgFoodInfoTx(ParamMap paramMap) throws Exception {
		return pacommonProcess.saveSsgFoodInfo(paramMap);
	}

	public String selectPaSsgGoodsCollectYnCheck(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaSsgGoodsCollectYnCheck(paramMap);
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaSsgCollectGoodsList(ParamMap paramMap) throws Exception {
		return pacommonProcess.selectPaSsgCollectGoodsList(paramMap);

	}

	@Override
	public String savePaMobileOrderCancelTx(HashMap<String, String> map) throws Exception {
		return pacommonProcess.savePaMobileOrderCancel(map);
		
	}

}
