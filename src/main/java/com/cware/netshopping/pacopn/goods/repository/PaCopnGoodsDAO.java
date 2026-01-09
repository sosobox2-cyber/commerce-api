package com.cware.netshopping.pacopn.goods.repository;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaCopnGoodsUserAttri;

@Service("pacopn.goods.paCopnGoodsDAO")
public class PaCopnGoodsDAO extends AbstractPaDAO{

	public PaEntpSlip selectPaCopnEntpSlip(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "30");
		return (PaEntpSlip) selectByPk("pacopn.goods.selectPaCopnEntpSlip", paramMap.get());
	}

	public PaCopnGoodsVO selectPaCopnGoodsInfo(ParamMap paramMap) throws Exception{
		return (PaCopnGoodsVO) selectByPk("pacopn.goods.selectPaCopnGoodsInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaGoodsOfferVO> selectPaCopnGoodsOfferList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsOfferList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsAttri> selectPaCopnGoodsAttriList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsAttriList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaCopnGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsdtInfoList", paramMap.get());
	}

	public int insertPaCopnGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception{
		return insert("pacommon.pacommon.insertPaGoodsTransLog", paGoodsTransLog);
	}

	public int updatePaCopnGoodsAttri(PaCopnGoodsAttri copnAttri) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsAttri", copnAttri);
	}

	public int updatePaCopnGoods(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnGoods", paCopnGoods);
	}
	
	public int updatePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsFail", paCopnGoods);
	}

	public int updatePaCopnGoodsdt(PaGoodsdtMapping paGoodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsdt", paGoodsdtMapping);
	}

	public int updatePaCopnGoodsdtTargetYn(PaGoodsdtMapping paGoodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsdtTargetYn", paGoodsdtMapping);
	}

	public int updatePaCopnGoodsImage(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsImage", paCopnGoods);
	}

	public int updatePaCopnGoodsPrice(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsPrice", paCopnGoods);
	}

	public int updatePaCopnCustShipCost(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnCustShipCost", paCopnGoods);
	}

	public int updatePaCopnGoodsOffer(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsOffer", paCopnGoods);
	}

	public int savePaCopnGoodsDtOption(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsdtOption", goodsdtMapping);
	}

	public int savePaCopnApprovalStatus(PaCopnGoodsVO goodsMap) throws Exception{
		return update("pacopn.goods.updatePaCopnApprovalStatus", goodsMap);
	}

	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaCopnEntpSlipList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnEntpSlip", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectEmptyVendorId(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectEmptyVendorId", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectEmptyProductId(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectEmptyProductId", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectRegisterEmpty(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectRegisterEmpty", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsProductNo(ParamMap paramMap)  throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsProductNo", paramMap.get());
	}

	public int updatePaCopnGoodsSellStop(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsSellStop", paCopnGoodsdtMapping);
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleStopList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsSaleStopList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnGoodsSaleRestartList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsSaleRestartList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoList(ParamMap paramMap)throws Exception {
		return list("pacopn.goods.selectPaCopnGoodsInfo", paramMap.get());
	}

	public int savePaChangeOptionStatus(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaChangeOptionStatus", paCopnGoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsdtMappingStockList", paramMap.get());
	}

	public int updatePaCopnGoodsdtOrder(PaCopnGoodsdtMappingVO paCopnGoodsMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsdtOrder", paCopnGoodsMapping);
	}

	@SuppressWarnings("unchecked")
	public List<PaGoodsPriceVO> selectCopnPriceModify(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectCopnPriceModify", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectCopnPriceModifyVendorIdSearch(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectCopnPriceModifyVendorIdSearch", paramMap.get());
	}

	public int updatePaCopnGoodsPriceDiscount(PaCopnGoodsdtMappingVO paCopnGoodsMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsPriceDiscount", paCopnGoodsMapping);
	}

	public int updatePaGoodsTarget(PaCopnGoodsVO paCopnGoods) throws Exception{
		return update("pacopn.goods.updatePaGoodsTarget", paCopnGoods);
	}

	public int savePaCopnProductId(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnGoodsdtProductId", goodsdtMapping);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaPromoTarget", paramMap.get());
	}
	
	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return update("pacopn.goods.updatePaPromoTarget", paPromoTarget);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pacopn.goods.selectGoodsLimitCharList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExceptEntpForCopnPolicy(String entpCode) throws Exception{
		return (HashMap<String, String>)  selectByPk("pacopn.goods.selectExceptEntpForCopnPolicy", entpCode);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectExceptCategoryForCopnPolicy(String lmsdCode) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.goods.selectExceptCategoryForCopnPolicy", lmsdCode);
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 정보 조회 */
	@SuppressWarnings("unchecked")
	public HashMap<String,Object> selectPaCopnAlcoutDealInfo(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>)selectByPk("pacopn.goods.selectPaCopnAlcoutDealInfo", paramMap.get());
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 상품 정보 조회 */
	public PaCopnGoodsVO selectPaCopnAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception{
		return (PaCopnGoodsVO) selectByPk("pacopn.goods.selectPaCopnAlcoutDealGoodsInfo", paramMap.get());
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 상품 정보 조회 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pacopn.goods.selectPaCopnAlcoutDealGoodsdtInfoList", paramMap.get());
	}
	
	/* 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 상품 정보 조회 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pacopn.goods.selectPaCopnAlcoutDealGoodsList", paramMap.get());
	}
	
	public int savePaCopnAlcoutDealGoods(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception{
		return update("pacopn.goods.updatePaCopnAlcoutDealGoods", paCopnAlcoutDealInfo);
	}
	
	public int saveAlcoutDealGoodsdtMappingPaOptionCode(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception{
		return update("pacopn.goods.updateAlcoutDealGoodsdtMappingPaOptionCode", paCopnAlcoutDealInfo);
	}
	
	/**
	 * REQ_PRM_041 쿠팡 제휴OUT 딜 수정대상 딜 목록
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectPaCopnModifyAlcoutDealList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pacopn.goods.selectPaCopnModifyAlcoutDealList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectAlcoutDealEmptyVendorId() throws Exception{
		return list("pacopn.goods.selectAlcoutDealEmptyVendorId", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectAlcoutDealEmptyProductId() throws Exception{
		return list("pacopn.goods.selectAlcoutDealEmptyProductId", null);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectRegisterAlcoutDealEmpty() throws Exception{
		return list("pacopn.goods.selectRegisterAlcoutDealEmpty", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsProductNo(ParamMap paramMap)  throws Exception{
		return list("pacopn.goods.selectPaCopnAlcoutDealGoodsProductNo", paramMap.get());
	}
	
	public int updatePaCopnAlcoutDealGoodsSell(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception{
		return update("pacopn.goods.updatePaCopnAlcoutDealGoodsSell", paCopnGoodsdtMapping);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnAlcoutDealGoodsdtMappingStockList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleStopList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnAlcoutDealGoodsSaleStopList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleRestartList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnAlcoutDealGoodsSaleRestartList", paramMap.get());
	}
	
	/**
	 * 쿠팡 - REQ_PRM_041 쿠팡 제휴OUT 딜 단품 추가 정보 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectPaCopnNotExistsGoodsdtList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pacopn.goods.selectPaCopnNotExistsGoodsdtList", paramMap.get());
	}
	
	/**
	 * 쿠팡 - REQ_PRM_041 쿠팡 제휴OUT 딜 단품 추가
	 * @param hashMap
	 * @return int
	 * @throws Exception
	 */
	public int insertPaCopnNotExistsGoodsdt(HashMap<String, Object> paCopnNotExistsGoodsdt) throws Exception{
		return insert("pacopn.goods.insertPaCopnNotExistsGoodsdt", paCopnNotExistsGoodsdt);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnAlcoutDealGoodsDescribe", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaPromoDeleteTarget", paramMap.get());
	}

	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return insert("pacopn.goods.insertNewPaPromoTarget", paPromoTarget);
	}

	public int updateTPapromoTarget4ExceptMemo(PaPromoTarget paPromoTarget) throws Exception{
		return update("pacopn.goods.updateTPapromoTarget4ExceptMemo", paPromoTarget);
	}
	
	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) throws Exception{
		return insert("pacopn.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public PaPromoTarget selectPaPromoTargetOne(ParamMap paramMap) throws Exception{
		return (PaPromoTarget)selectByPk("pacopn.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int selectExistsPaPromoLastestTransDate(PaPromoTarget paPromoTarget) throws Exception {
		return (int)selectByPk("pacopn.goods.selectExistsPaPromoLastestTransDate", paPromoTarget);
	}
	
	public int selectPaCopnGoodsModifyCheck(PaCopnGoodsVO paCopnGoods) throws Exception {
		return (int)selectByPk("pacopn.goods.selectPaCopnGoodsModifyCheck", paCopnGoods);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaCopnGoodsTrans(ParamMap paramMap) throws Exception {
		return list("pacopn.goods.selectPaCopnGoodsTrans", paramMap.get());
	}

	public int updateMassTargetYn(PaCopnGoodsVO paCopnGoods) {
		return update("pacopn.goods.updateMassTargetYn", paCopnGoods);
	}
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) {
		return update("pacopn.goods.updateMassTargetYnByEpCode", massMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserAttriList(ParamMap paramMap) throws Exception{
		return list("pacopn.goods.selectPaCopnGoodsUserAttriList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserSearchAttriList(ParamMap paramMap) throws Exception {
		return list("pacopn.goods.selectPaCopnGoodsUserSearchAttriList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectDeleteGoodsSaleStatusList() throws Exception {
		return list("pacopn.goods.selectDeleteGoodsSaleStatusList", null);
	}

	public int updateDeleteGoodsSaleStatus(HashMap<String, Object> goodsStatusMap) throws Exception {
		return update("pacopn.goods.updateDeleteGoodsSaleStatus", goodsStatusMap);
	}	
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectDeleteGoodsList(HashMap<String, Object> goodsStatusMap) throws Exception {
		return list("pacopn.goods.selectDeleteGoodsList", goodsStatusMap);
	}

	public int insertDeleteGoodsHistory(HashMap<String, String> cancelGoodsMap) throws Exception {
		return insert("pacopn.goods.insertDeleteGoodsHistory", cancelGoodsMap);
	}

	public int deleteTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return delete("pacopn.goods.deleteTempDeleteGoodsList",cancelGoodsMap);
	}

	public int updateTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception {
		return update("pacopn.goods.updateTempDeleteGoodsList", cancelGoodsMap);
	}
}
