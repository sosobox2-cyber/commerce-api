package com.cware.netshopping.pa11st.goods.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.Pa11stGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pa11st.goods.process.Pa11stGoodsProcess;
import com.cware.netshopping.pa11st.goods.service.Pa11stGoodsService;

@Service("pa11st.goods.pa11stGoodsService")
public class Pa11stGoodsServiceImpl extends AbstractService implements  Pa11stGoodsService {
	
	@Resource(name = "pa11st.goods.pa11stGoodsProcess")
	private Pa11stGoodsProcess pa11stGoodsProcess;	
	
	@Override
	public PaEntpSlip selectPa11stEntpSlip(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stEntpSlip(paramMap);
	}

	@Override
	public PaEntpSlip selectPa11stReturnSlip(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stReturnSlip(paramMap);
	}

	@Override
	public Pa11stGoodsVO selectPa11stGoodsInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsInfo(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPa11stGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsdtInfoList(paramMap);
	}
	
	
	@Override
	public List<PaGoodsOffer> selectPa11stGoodsOfferList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsOfferList(paramMap);
	}

	@Override
	public String savePa11stGoodsTx(Pa11stGoodsVO pa11stGoods, List<PaGoodsdtMapping> pa11stGoodsdt, List<PaPromoTarget> paPromoTargetList, String prgId) throws Exception {
		return pa11stGoodsProcess.savePa11stGoods(pa11stGoods, pa11stGoodsdt, paPromoTargetList, prgId);
	}

	@Override
	public List<PaEntpSlip> selectPa11stEntpSlipList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stEntpSlipList(paramMap);

	}

	@Override
	public List<PaEntpSlip> selectPa11stReturnSlipList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stReturnSlipList(paramMap);

	}

	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsInfoList(paramMap);
	}

	@Override
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceList(ParamMap paramMap)throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsPriceList(paramMap);
	}

	@Override
	public String savePa11stGoodsPriceTx(PaGoodsPriceVO paGoodsPrice, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsPrice(paGoodsPrice, paPromoTargetList);
	}

	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsdtMappingList(paramMap);
	}

	@Override
	public String savePa11stGoodsdtMappingTx(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtMapping(paGoodsdtMappingVO);
	}
	
	@Override
	public String savePa11stGoodsdtQtyTx(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtQty(paGoodsdtMappingVO);
	}

	@Override
	public HashMap<String, String> selectPa11stGoodsDescribe(ParamMap paramMap)throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsDescribe(paramMap);
	}

	@Override
	public String savePa11stGoodsDescribeTx(HashMap<String, String> goodsDesc)throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsDescribe(goodsDesc);
	}
	

	@Override
	public int insertPa11stGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return pa11stGoodsProcess.insertPa11stGoodsTransLog(paGoodsTransLog);
	}

	@Override
	public String savePa11stGoodsdtMappingPaOptionCodeTx(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtMappingPaOptionCode(paGoodsdtMapping);

	}

	@Override
	public Pa11stGoodsVO selectPa11stGoodsProductNo(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsProductNo(paramMap);
	}

	@Override
	public String savePa11stGoodsSellTx(Pa11stGoodsVO pa11stGoods) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsSell(pa11stGoods);
	}

	@Override
	public String checkPa11stCheckSaleGb(String goodsCode) throws Exception {
		return pa11stGoodsProcess.checkPa11stCheckSaleGb(goodsCode);
	}

	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsdtMappingStockList(paramMap);

	}

	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsDtStockList() throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsDtStockList();
	}

	@Override
	public List<Pa11stGoodsVO> selectedSoldOutPa11stGoodsList() throws Exception {
		return pa11stGoodsProcess.selectedSoldOutPa11stGoodsList();
	}
	@Override
	public String updateSoldOutTransSaleYn(Pa11stGoodsVO pa11stGoods) throws Exception {
		return pa11stGoodsProcess.updateSoldOutTransSaleYn(pa11stGoods);
	}

	@Override
	public String savePa11stGoodsdtMappingQtyTx(Pa11stGoodsVO pa11stGoods)
			throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtMappingQty(pa11stGoods);

	}

	@Override
	public HashMap<String,Object> procPa11stGoodsSync(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.procPa11stGoodsSync(paramMap);
	}

	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsSaleStopList(paramMap);
	}

	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsSaleRestartList(paramMap);
	}
	
	@Override
	public String savePa11stGoodsFailTx(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.savePa11stGoodsFail(pa11stGoods);
	}
	
	@Override
	public int updateTransSaleYn(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.updateTransSaleYn(paramMap);
	}

	@Override
	public String selectPa11stGoodsDesc(String goodsCode) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsDesc(goodsCode);
	}
	
	public int selectPa11stGoodsDescCnt998(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsDescCnt998(paramMap);
	}
	public int selectPa11stGoodsDescCnt999(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsDescCnt999(paramMap);
	}
	public String selectPa11stGoodsDesc998(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsDesc998(paramMap);
	}
	public String selectPa11stGoodsDesc999(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsDesc999(paramMap);
	}
	public int selectPa11stGoodsShipCnt(String goodsCode) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsShipCnt(goodsCode);
	}
	public String saveStopMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.saveStopMonitering(pa11stGoods);
	}
	public String saveRestartMonitering(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.saveRestartMonitering(pa11stGoods);
	}
	@Override
	public String saveShipCostPaGoodsTx(Pa11stGoodsVO pa11stGoods) throws Exception {
		return pa11stGoodsProcess.saveShipCostPaGoods(pa11stGoods);
	}
	
	@Override
	public List<Pa11stGoodsdtMappingVO> selectPa11stOrderMappingList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stOrderMappingList(paramMap);
	}
	
	@Override
	public String savePa11stGoodsdtMappingOrderTx(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtMappingOrder(paGoodsdtMapping);

	}
	@Override
	public String insertCnCostMoniteringTx(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.insertCnCostMonitering(pa11stGoods);
	}
	@Override
	public String updateCnShipCostByMoniteringTx(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.updateCnShipCostByMonitering(pa11stGoods);
	}
	
	public String savePaGoodsModifyFailTx(Pa11stGoodsVO pa11stGoods) throws Exception{
		return pa11stGoodsProcess.updatePaGoodsModifyFail(pa11stGoods);
	}
	@Override
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPaPromoTarget(paramMap);
	}

	@Override
	public HashMap<String, String> selectGoodsFor11stPolicy(Pa11stGoodsVO pa11stGoods) throws Exception {
		return pa11stGoodsProcess.selectGoodsFor11stPolicy(pa11stGoods);
	}
	@Override
	public int updatePaStatus90(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.updatePaStatus90(paramMap);
	}

	@Override
	public String saveTpagoodsVodUrlTx(Pa11stGoodsVO pickCast) throws Exception {
		return pa11stGoodsProcess.saveTpagoodsVodUrl(pickCast);
	}
	
	@Override
	public List<Pa11stGoodsVO> selectPa11stVodUrlTransList() throws Exception {
		return pa11stGoodsProcess.selectPa11stVodUrlTransList();
	}
	
	@Override
	public int updatePaGoodsVodUrlTransYn(Pa11stGoodsVO pa11stGoods) throws Exception {
		return pa11stGoodsProcess.updatePaGoodsVodUrlTransYn(pa11stGoods);
	}
	
	@Override
	public int updatePaGoodsVodUrlFailMsg(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.updatePaGoodsVodUrlFailMsg(paramMap);
	}
	
	@Override
	public int updateTransTargetYnTx(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.updateTransTargetYn(paramMap);
	}
	
	@Override
	public int updatePa11stGoodsTransTargetYnTx(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.updatePa11stGoodsTransTargetYn(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectPa11stGoodsNoticeYn(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPa11stGoodsNoticeYn(paramMap);
	}
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품 등록 START */
	/* 제휴OUT 딜 정보 조회 */
	@Override
	public HashMap<String,Object> selectPa11stAlcoutDealInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealInfo(paramMap);
	}
	
	/* 제휴OUT 딜 대표 상품정보 조회 */
	@Override
	public Pa11stGoodsVO selectPa11stAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealGoodsInfo(paramMap);
	}
	
	/* 제휴OUT 딜 상품정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectAlcoutDealGoodsList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectAlcoutDealGoodsList(paramMap);
	}
	
	/* 제휴OUT 딜 단품정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPa11stAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealGoodsdtInfoList(paramMap);
	}
	
	/* 제휴OUT 딜 프로모션대상상품 조회 */
	/*@Override
	public List<HashMap<String,Object>> selectPaPromoTargetGoodsList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPaPromoTargetGoodsList(paramMap);
	}*/
	
	/* 제휴사 제휴OUT 딜 정보 저장*/
	@Override
	public String savePa11stAlcoutDealTx(HashMap<String,Object> alcoutDealInfo) throws Exception {
		return pa11stGoodsProcess.savePa11stAlcoutDeal(alcoutDealInfo);
	}
	
	/* 제휴OUT딜 상품 재고정보 저장 */
	@Override
	public String savePa11stAlcoutDealGoodsdtMappingPaOptionCodeTx(List<HashMap<String,Object>> alcoutDealGoodsMappingList) throws Exception {
		return pa11stGoodsProcess.savePa11stAlcoutDealGoodsdtMappingPaOptionCode(alcoutDealGoodsMappingList);

	}
	
	/* 11번가 제휴OUT 딜 연동실패 저장*/
	@Override
	public String savePa11stAlcoutDealGoodsFailTx(HashMap<String,Object> alcoutDealInfo) throws Exception{
		return pa11stGoodsProcess.savePa11stAlcoutDealGoodsFail(alcoutDealInfo);
	}
	
	/* 제휴OUT 수정대상 딜 목록 조회 */
	@Override
	public List<HashMap<String,Object>> selectPa11stModifyAlcoutDealList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stModifyAlcoutDealList(paramMap);
	}
	
	/* 11번가 옵션수정 - 상품상세설명 수정대상 조회 */
	@Override
	public List<HashMap<String, String>> selectPa11stAlcoutDealGoodsDescribeModify(HashMap<String,Object> alcoutDealInfo)throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealGoodsDescribeModify(alcoutDealInfo);
	}
	
	/* 11번가 제휴OUT 딜 상품영상 연동리스트 조회 */
	@Override
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealVodUrlTransList() throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealVodUrlTransList();
	}
	
	/* 제휴OUT 수정대상 딜 단품 추가 정보 조회 */
	@Override
	public List<HashMap<String,Object>> selectPa11stNotExistsGoodsdtList(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stNotExistsGoodsdtList(paramMap);
	}
	
	/* 제휴OUT 수정대상 딜 단품 추가 */
	@Override
	public String insertPa11stNotExistsGoodsdtTx(HashMap<String, Object> pa11stNotExistsGoodsdt)throws Exception {
		return pa11stGoodsProcess.insertPa11stNotExistsGoodsdt(pa11stNotExistsGoodsdt);
	}
	
	/* 제휴OUT 딜 기술서 조회 */
	@Override
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stAlcoutDealGoodsDescribe(paramMap);
	}
	
	@Override
	public int saveAlcoutDealPriceLogTx(List<HashMap> alcoutDealPriceLogList) throws Exception {
		return pa11stGoodsProcess.saveAlcoutDealPriceLog(alcoutDealPriceLogList);
	}
	
	@Override
	public int selectOutDealGoodsCheck(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsProcess.selectOutDealGoodsCheck(asyncPa11stGoods);
	}
	
	@Override
	public int updateOutDealGoodsTarget(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsProcess.updateOutDealGoodsTarget(asyncPa11stGoods);
	}
	/* REQ_PRM_041 11번가 제휴OUT 딜 상품 등록 END */

	@Override
	public int updateMassTargetYn(Pa11stGoodsVO asyncPa11stGoods) throws Exception {
		return pa11stGoodsProcess.updateMassTargetYn(asyncPa11stGoods);
	}

	@Override
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoListMass(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsInfoListMass(paramMap);
	}

	@Override
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceListMass(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stGoodsPriceListMass(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGoodsTrans(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPaGoodsTrans(paramMap);
	}

	@Override
	public int updatePa11stGoodsFailInsert(HashMap<String, String> paGoods) throws Exception {
		return pa11stGoodsProcess.updatePa11stGoodsFailInsert(paGoods);
	}

	@Override
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception {
		return pa11stGoodsProcess.updateMassTargetYnByEpCode(massMap);
	}
	
	@Override
	public String savePa11stGoodsdtTargetTx(String goodsCode) throws Exception {
		return pa11stGoodsProcess.savePa11stGoodsdtTargetTx(goodsCode);
	}

	@Override
	public List<HashMap<String, Object>> selectPa11stModifyAlcoutDealTarget(ParamMap paramMap) throws Exception {
		return pa11stGoodsProcess.selectPa11stModifyAlcoutDealTarget(paramMap);
	}

	@Override
	public int insertAlcoutDealPriceLog(HashMap<String, Object> logMap) throws Exception {
		return pa11stGoodsProcess.insertAlcoutDealPriceLog(logMap);
	}

	@Override
	public String selectMaxRetentionSeq(String goodsCode, String paCode) throws Exception {
		return pa11stGoodsProcess.selectMaxRetentionSeq(goodsCode, paCode);
	}
	
	@Override
	public List<HashMap<String,Object>> selectPaGoodsPriceApply(ParamMap paramMap) throws Exception{
		return pa11stGoodsProcess.selectPaGoodsPriceApply(paramMap);
	}
}