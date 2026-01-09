package com.cware.netshopping.pacopn.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CopnGoodsDeleteVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacopn.goods.process.PaCopnGoodsProcess;
import com.cware.netshopping.pacopn.goods.service.PaCopnGoodsService;
import com.cware.netshopping.domain.model.PaCopnGoodsUserAttri;

@Service("pacopn.goods.paCopnGoodsService")
public class PaCopnGoodsServiceImpl extends AbstractService implements PaCopnGoodsService{

	@Resource(name = "pacopn.goods.paCopnGoodsProcess")
	private PaCopnGoodsProcess paCopnGoodsProcess;
	
	@Override
	public PaEntpSlip selectPaCopnEntpSlip(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnEntpSlip(paramMap);
	}
	@Override
	public PaCopnGoodsVO selectPaCopnGoodsInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsInfo(paramMap);
	}
	@Override
	public List<PaGoodsOfferVO> selectPaCopnGoodsOfferList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsOfferList(paramMap);
	}
	@Override
	public List<PaCopnGoodsAttri> selectPaCopnGoodsAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsAttriList(paramMap);
	}
	@Override
	public List<PaGoodsdtMapping> selectPaCopnGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsdtInfoList(paramMap);
	}
	@Override
	public int insertPaCopnGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return paCopnGoodsProcess.insertPaCopnGoodsTransLog(paGoodsTransLog); 
	}
	@Override
	public String savePaCopnGoodsAttriTx(List<PaCopnGoodsAttri> copnGoodsAttri) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoodsAttri(copnGoodsAttri);
	}
	@Override
	public String savePaCopnGoodsTx(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoods(paCopnGoods, goodsdtMapping, paPromoTargetList);
	}
	@Override
	public String savePaCopnGoodsFailTx(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoodsFail(paCopnGoods);
	}
	@Override
	public String savePaCopnGoodsDtOptionTx(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoodsDtOption(goodsdtMapping);
	}
	@Override
	public String savePaCopnApprovalStatus(PaCopnGoodsVO goodsMap) throws Exception {
		return paCopnGoodsProcess.savePaCopnApprovalStatus(goodsMap);
	}
	@Override
	public List<PaEntpSlip> selectPaCopnEntpSlipList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnEntpSlipList(paramMap);
	}
	@Override
	public List<PaCopnGoodsdtMappingVO> selectEmptyVendorId(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectEmptyVendorId(paramMap);
	}
	@Override
	public List<PaCopnGoodsdtMappingVO> selectEmptyProductId(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectEmptyProductId(paramMap);
	}
	@Override
	public List<PaCopnGoodsVO> selectRegisterEmpty(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectRegisterEmpty(paramMap);
	}
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsProductNo(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsProductNo(paramMap);
	}
	@Override
	public String savePaCopnGoodsSellTx(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoodsSell(paCopnGoodsdtMapping);
	}
	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsSaleStopList(paramMap);
	}
	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsSaleRestartList(paramMap);
	}
	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsInfoList(paramMap);
	}
	@Override
	public String savePaChangeOptionStatus(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsProcess.savePaChangeOptionStatus(paCopnGoods);
	}
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsdtMappingStockList(paramMap);
	}
	@Override
	public String updatePaCopnGoodsdtOrderTx(PaCopnGoodsdtMappingVO paCopnGoodsMapping) throws Exception {
		return paCopnGoodsProcess.updatePaCopnGoodsdtOrder(paCopnGoodsMapping);
	}
	@Override
	public List<PaGoodsPriceVO> selectCopnPriceModify(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectCopnPriceModify(paramMap);
	}
	@Override
	public List<PaCopnGoodsdtMappingVO> selectCopnPriceModifyVendorIdSearch(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectCopnPriceModifyVendorIdSearch(paramMap);
	}
	@Override
	public String updatePaCopnGoodsPriceDiscountTx(PaCopnGoodsdtMappingVO paCopnGoodsMapping, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paCopnGoodsProcess.updatePaCopnGoodsPriceDiscount(paCopnGoodsMapping, paPromoTargetList);
	}
	@Override
	public String savePaCopnProductId(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception {
		return paCopnGoodsProcess.savePaCopnProductId(goodsdtMapping);
	}
	@Override
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaPromoTarget(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception{
		return paCopnGoodsProcess.selectGoodsLimitCharList(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectGoodsForCopnPolicy(PaCopnGoodsVO paCopnGoods) throws Exception{
		return paCopnGoodsProcess.selectGoodsForCopnPolicy(paCopnGoods);
	}
	
	/* REQ_PRM_041 쿠팡 제휴OUT 딜 상품 등록 START */
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜  정보 조회 */
	@Override
	public HashMap<String,Object> selectPaCopnAlcoutDealInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealInfo(paramMap);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표상품 정보 조회 */
	@Override
	public PaCopnGoodsVO selectPaCopnAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsInfo(paramMap);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 상품/단품 조회 */
	/* 제휴OUT 딜 단품정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsdtInfoList(paramMap);
	}
	
	/* 제휴OUT 딜 상품정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsList(paramMap);
	}
	
	/* 제휴OUT 제휴사 상품정보 저장 */
	@Override
	public String savePaCopnAlcoutDealGoodsTx(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception {
		return paCopnGoodsProcess.savePaCopnAlcoutDealGoods(paCopnAlcoutDealInfo);
	}
	
	@Override
	public String saveAlcoutDealGoodsdtMappingPaOptionCodeTx(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception {
		return paCopnGoodsProcess.saveAlcoutDealGoodsdtMappingPaOptionCode(paCopnAlcoutDealInfo);
	}
	
	/* 제휴OUT 수정대상 딜 목록 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnModifyAlcoutDealList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnModifyAlcoutDealList(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealEmptyVendorId() throws Exception {
		return paCopnGoodsProcess.selectAlcoutDealEmptyVendorId();
	}
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealEmptyProductId() throws Exception {
		return paCopnGoodsProcess.selectAlcoutDealEmptyProductId();
	}
	@Override
	public List<HashMap<String,Object>> selectRegisterAlcoutDealEmpty() throws Exception {
		return paCopnGoodsProcess.selectRegisterAlcoutDealEmpty();
	}
	
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsProductNo(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsProductNo(paramMap);
	}
	
	@Override
	public String savePaCopnAlcoutDealGoodsSellTx(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception {
		return paCopnGoodsProcess.savePaCopnAlcoutDealGoodsSell(paCopnGoodsdtMapping);
	}
	
	@Override
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsdtMappingStockList(paramMap);
	}
	
	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsSaleStopList(paramMap);
	}
	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsSaleRestartList(paramMap);
	}
	
	/* 제휴OUT 수정대상 딜 단품 추가 정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPaCopnNotExistsGoodsdtList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnNotExistsGoodsdtList(paramMap);
	}
	
	/* 제휴OUT 수정대상 딜 단품 추가 */
	@Override
	public String insertPaCopnNotExistsGoodsdtTx(HashMap<String, Object> paCopnNotExistsGoodsdt)throws Exception {
		return paCopnGoodsProcess.insertPaCopnNotExistsGoodsdt(paCopnNotExistsGoodsdt);
	}
	
	/* 제휴OUT 기술서 목록 조회 */
	@Override
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnAlcoutDealGoodsDescribe(paramMap);
	}
	@Override
	public List<HashMap<String, String>> selectPaCopnGoodsTrans(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsTrans(paramMap);
	}
	@Override
	public int updatePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsProcess.updatePaCopnGoodsFail(paCopnGoods);
	}
	@Override
	public int updateMassTargetYn(PaCopnGoodsVO paCopnGoods) throws Exception {
		return paCopnGoodsProcess.updateMassTargetYn(paCopnGoods);
	}
	@Override
	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoListMass(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsInfoListMass(paramMap);
	}
	@Override
	public List<PaGoodsPriceVO> selectCopnPriceModifyMass(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectCopnPriceModifyMass(paramMap);
	}
	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return paCopnGoodsProcess.updateMassTargetYnByEpCode(massMap);
	}
	@Override
	public String savePaCopnGoodsTx(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping,
			List<PaPromoTarget> paPromoTargetList, PaGoodsPriceApply goodsPriceApply) throws Exception {
		return paCopnGoodsProcess.savePaCopnGoods(paCopnGoods, goodsdtMapping, paPromoTargetList, goodsPriceApply);
	}
	@Override
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsUserAttriList(paramMap);
	}
	@Override
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserSearchAttriList(ParamMap paramMap) throws Exception {
		return paCopnGoodsProcess.selectPaCopnGoodsUserSearchAttriList(paramMap);
	}
	@Override
	public List<HashMap<String, String>> selectDeleteGoodsSaleStatusList() throws Exception {
		return paCopnGoodsProcess.selectDeleteGoodsSaleStatusList();
	}
	@Override
	public int updateDeleteGoodsSaleStatus(HashMap<String, Object> goodsStatusMap) throws Exception {
		return paCopnGoodsProcess.updateDeleteGoodsSaleStatus(goodsStatusMap);
	}
	@Override
	public List<HashMap<String, String>> selectDeleteGoodsList(HashMap<String, Object> goodsStatusMap) throws Exception {
		return paCopnGoodsProcess.selectDeleteGoodsList(goodsStatusMap);
	}
	@Override
	public int insertDeleteGoodsHistory(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsProcess.insertDeleteGoodsHistory(cancelGoodsMap);
	}
	@Override
	public int deleteTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsProcess.deleteTempDeleteGoodsList(cancelGoodsMap);
	}
	@Override
	public int updateTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return paCopnGoodsProcess.updateTempDeleteGoodsList(cancelGoodsMap);
	}
	@Override
	public CompletableFuture<CopnGoodsDeleteVO> asyncGoodsDelete(List<HashMap<String, String>> asyncList, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception {
		return paCopnGoodsProcess.asyncGoodsDelete(asyncList, paramMap, apiInfo);
	}
}
