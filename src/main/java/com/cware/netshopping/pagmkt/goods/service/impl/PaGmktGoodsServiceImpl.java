package com.cware.netshopping.pagmkt.goods.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.model.PaGmktDelGoodsHis;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pagmkt.goods.process.PaGmktGoodsProcess;
import com.cware.netshopping.pagmkt.goods.service.PaGmktGoodsService;

@Service("pagmkt.goods.paGmktGoodsService")
public class PaGmktGoodsServiceImpl extends AbstractService implements PaGmktGoodsService {

	@Resource(name = "pagmkt.goods.paGmktGoodsProcess")
	private PaGmktGoodsProcess paGmktGoodsProcess;
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsModifyList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsModifyList(paramMap);
	}
	@Override
	public List<HashMap<String, String>> selectGmktGoodsInsertList(HashMap<String,Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsInsertList(paramMap);
	}
	@Override
	public int selectChkShipCost(String goodsCode) throws Exception {
		return paGmktGoodsProcess.selectChkShipCost(goodsCode);
	}
	@Override
	public HashMap<String,Object> selectPaEbayCheckGoods(String goodsCode) throws Exception{
		return paGmktGoodsProcess.selectPaEbayCheckGoods(goodsCode);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceSaleModifyList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsPriceSaleModifyList(paramMap);
	}
	@Override
	public String savePaGmktGoodsModifyTx(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception {
		return paGmktGoodsProcess.savePaGmktGoodsModify(paGmktGoods, paPromoTargetList);
	}
//	@Override
//	public String savePaGmktGoodsModifyTx(PaGmktGoodsVO paGmktGoods) throws Exception {
//		return paGmktGoodsProcess.savePaGmktGoodsModify(paGmktGoods);
//	}
	
	@Override
	public String saveGmktGoodsPriceSaleModifyTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.saveGmktGoodsPriceSaleModify(paGmktGoods);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsNameModifyList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsNameModifyList(paramMap);
	}
	@Override
	public String saveGmktGoodsNameModifyTx(PaGmktGoods paGmktGoods) throws Exception {	
		return paGmktGoodsProcess.saveGmktGoodsNameModify(paGmktGoods);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsOptionList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGoodsOptionList(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsOption(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGoodsOption(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsOptionBO(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGoodsOptionBO(paramMap);
	}
	@Override
	public String saveGoodsOptionTx(List<PaGoodsdtMapping> paGoodsdtMapping) throws Exception {
		return paGmktGoodsProcess.saveGoodsOption(paGoodsdtMapping);
	}
	@Override
	public String savePaGmktGoodsInsertSuccessFailTx(PaGmktGoodsVO paGmktGoods) throws Exception{
		return paGmktGoodsProcess.savePaGmktGoodsInsertSuccessFail(paGmktGoods);
	}
	@Override
	public String savePaGmktGoodsInsertRejectTx(PaGmktGoods paGmktGoods) throws Exception{
		return paGmktGoodsProcess.savePaGmktGoodsInsertReject(paGmktGoods);
	}
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsInsertOne(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsInsertOne(paramMap);
	}
	
//	@Override
//	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
//		return paGmktGoodsProcess.selectPaPromoTarget(paramMap);
//	}
	@Override
	public List<HashMap<String,Object>> selectPaGmktPromoTarget(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectPaGmktPromoTarget(paramMap);
	}
	@Override
	public List<HashMap<String,Object>> selectPaIacPromoTarget(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectPaIacPromoTarget(paramMap);
	}
	
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsOfferInsertOne(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsOfferInsertOne(paramMap);
	}
	@Override
	public HashMap<String,Object> selectGmktGoodsDescribeInsertOne(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsDescribeInsertOne(paramMap);
	}
	@Override
	public HashMap<String,Object> selectGoodsForGmktPolicy(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGoodsForGmktPolicy(paramMap);
	}
	@Override
	public String savePaGmktGoodsInsertTx(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception{
		return paGmktGoodsProcess.savePaGmktGoodsInsert(paGmktGoods, paPromoTargetList);
	}
	/*@Override
	public String savePaGmktGoodsInsertTx(PaGmktGoodsVO paGmktGoods) throws Exception{
		return paGmktGoodsProcess.savePaGmktGoodsInsert(paGmktGoods);
	}*/
	@Override
	public String insertPaGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception {
	    return paGmktGoodsProcess.insertPaGoodsTransLog(paGoodsTransLog);
	}
	@Override
	public List<HashMap<String, Object>> selectSiteGoodsNoTarget(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectSiteGoodsNoTarget(paramMap);
	}
	/*@Override
	public String saveSiteGoodsNoTargetTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.saveSiteGoodsNoTarget(paGmktGoods);
	}*/
	@Override
	public HashMap<String,Object> procPaGmktAutoInsert(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.procPaGmktAutoInsert(paramMap);
	}
	@Override
	public String selectEsmGoodsNo(ParamMap paramMap) throws Exception {
	    return paGmktGoodsProcess.selectEsmGoodsNo(paramMap);
	}
	@Override
	public HashMap<String,Object> selectGmktGoodsPrice(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsPrice(paramMap);
	}
	@Override
	public List<HashMap<String,Object>> selectGmktGoodsImageModify(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsImageModify(paramMap);
	}
	@Override
	public String updatePaGmktGoodsImageTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.updatePaGmktGoodsImage(paGmktGoods);
	}
	@Override
	public String savePaGmktGoodsStatusTx(PaGmktDelGoodsHis paGmktDelGoodsHis) throws Exception {
		return paGmktGoodsProcess.savePaGmktGoodsStatus(paGmktDelGoodsHis);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsDeleteList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsDeleteList(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceList() throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsPriceList();
	}
	@Override
	public String savePaGmktTransDiscountTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.savePaGmktTransDiscount(paGmktGoods);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsPriceSaleRealTimeList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsPriceSaleRealTimeList(paramMap);
	}
	@Override
	public int selectCheckDeliveryFee(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.selectCheckDeliveryFee(paramMap);
	}
	@Override
	public String selectGmktSellerId(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktSellerId(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectSalesDayModifyTarget() throws Exception{
		return paGmktGoodsProcess.selectSalesDayModifyTarget();
	}
	@Override
	public int updateGmktGoodsforSalesDayModify(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.updateGmktGoodsforSalesDayModify(paramMap);
	}
	@Override
	public String selectCodeContentForTargetCnt(String apiCode) throws Exception {
		return paGmktGoodsProcess.selectCodeContentForTargetCnt(apiCode);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception{
		return paGmktGoodsProcess.selectGoodsLimitCharList(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsSellingOrderList(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsSellingOrderList(paramMap);
	}
	@Override
	public HashMap<String, Object> selectGmktGoodsSellingOrder(HashMap<String, Object> paramMap) throws Exception{
		return paGmktGoodsProcess.selectGmktGoodsSellingOrder(paramMap);
	}
	@Override
	public String saveGmktGoodsSellingOrderTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.saveGmktGoodsSellingOrder(paGmktGoods);
	}
	@Override
	public String savePaGoodsSellingOrderInsertFailTx(PaGmktGoods paGmktGoods) throws Exception {
		return paGmktGoodsProcess.savePaGoodsSellingOrderInsertFail(paGmktGoods);
	}
	@Override
	public List<HashMap<String, Object>> selectGoodsOptionListForResister(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectGoodsOptionListForResister(paramMap);
	}
	@Override
	public List<HashMap<String, Object>> selectG9DisplayTransList(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selectG9DisplayTransList(paramMap);
	}
	@Override
	public String saveGmktG9GoodsDisplay(ParamMap paramMap) throws Exception {	
		return paGmktGoodsProcess.saveGmktG9GoodsDisplay(paramMap);
	}
	@Override
	public String updateGmktGoodsdtMappingforGoodsModify(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.updateGmktGoodsdtMappingforGoodsModify(paramMap);
	}

	public List<HashMap<String, String>> selectPagmktGoodsInfoListMass(HashMap<String, Object> target) throws Exception {
		return paGmktGoodsProcess.selectPagmktGoodsInfoListMass(target);
	}
	@Override
	public int updateMassTargetYn(HashMap<String, String> targetGoods) throws Exception {
		return paGmktGoodsProcess.updateMassTargetYn(targetGoods);
	}

	@Override
	public void checkRetentionGoodsModify(ParamMap paramMap) throws Exception {
		paGmktGoodsProcess.checkRetentionGoodsModify(paramMap);
	}
	@Override
	public String getGmktSiteGb(String paCode, String goodsCode, String checkType) throws Exception {
		return paGmktGoodsProcess.getGmktSiteGb(paCode, goodsCode, checkType);
	}
	@Override
	public List<HashMap<String, Object>> selectGmktGoodsRetentionExtendList(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.selectGmktGoodsRetentionExtendList(paramMap);
	}
	@Override
	public int updateGmktSaleStopDate(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.updateGmktSaleStopDate(paramMap);
	}
	@Override
	public HashMap<String, Object> selecGoodsExpiry(HashMap<String, Object> paramMap) throws Exception {
		return paGmktGoodsProcess.selecGoodsExpiry(paramMap);
	}	
	@Override
	public HashMap<String, String> selecGoodsExpiryDateYn(String goodsCode) throws Exception {
		return paGmktGoodsProcess.selecGoodsExpiryDateYn(goodsCode);
	}
	@Override
	public int updateGoodsExpiryDateTx(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.updateGoodsExpiryDate(paramMap);
	}
	@Override
	public int selectOptionErrorChk(ParamMap paramMap) throws Exception {
		return paGmktGoodsProcess.selectOptionErrorChk(paramMap);
	}
	@Override
	public HashMap<String, String> selectEmsCodeItemNo(PaGmktGoodsVO paGmktGoods) throws Exception {
		return paGmktGoodsProcess.selectEmsCodeItemNo(paGmktGoods);
	}
	
}
