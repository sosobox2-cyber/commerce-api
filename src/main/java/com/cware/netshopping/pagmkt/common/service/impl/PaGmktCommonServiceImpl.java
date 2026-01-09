package com.cware.netshopping.pagmkt.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaEsmGoodsKinds;
import com.cware.netshopping.domain.model.PaGmktOrigin;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGmktSettlement;
import com.cware.netshopping.domain.model.PaGmktShipCostM;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaSiteGoodsKinds;
import com.cware.netshopping.pagmkt.common.process.PaGmktCommonProcess;
import com.cware.netshopping.pagmkt.common.service.PaGmktCommonService;

@Service("pagmkt.common.paGmktCommonService")
public class PaGmktCommonServiceImpl  extends AbstractService implements PaGmktCommonService{

	@Resource(name = "pagmkt.common.paGmktCommonProcess")
	private PaGmktCommonProcess paGmktCommonProcess;
    
	@Override
	public String savePaGmktGoodsKindsEsmTx(List<PaEsmGoodsKinds> paEsmGoodsKindsList) throws Exception {
	    return paGmktCommonProcess.savePaGmktGoodsKindsEsm(paEsmGoodsKindsList);
	}
	
	@Override
	public String savePaGmktGoodsKindsSiteTx(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception {
	    return paGmktCommonProcess.savePaGmktGoodsKindsSite(paSiteGoodsKindsList);
	}
	
	@Override
	public String savePaGmktMakerListTx(List<PaMaker> paMakerList) throws Exception {
	    return paGmktCommonProcess.savePaGmktMakerList(paMakerList);
	}
	
	@Override
	public String savePaGmktBrandListTx(List<PaBrand> paBrandList) throws Exception {
	    return paGmktCommonProcess.savePaGmktBrandList(paBrandList);
	}
	
	@Override
	public List<HashMap<String, String>> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonProcess.selectEntpShipInsertList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonProcess.selectEntpShipModifyList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipCostInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonProcess.selectEntpShipCostInsertList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipCostModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonProcess.selectEntpShipCostModifyList(paEntpSlip);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipPoliciesInsertList(String gmktShipNo) throws Exception {
	    return paGmktCommonProcess.selectEntpShipPoliciesInsertList(gmktShipNo);
	}
	@Override
	public List<HashMap<String, String>> selectEntpShipPoliciesModifyList(String gmktShipNo) throws Exception {
	    return paGmktCommonProcess.selectEntpShipPoliciesModifyList(gmktShipNo);
	}
	@Override
	public String updateEntpShipPoliciesInsertTx(HashMap<String,Object> paramMap) throws Exception{
		return paGmktCommonProcess.updateEntpShipPoliciesInsert(paramMap);
	}
	@Override
	public String updateEntpShipPoliciesModifyTx(HashMap<String,Object> paramMap) throws Exception{
		return paGmktCommonProcess.updateEntpShipPoliciesModify(paramMap);
	}
	@Override
	public String savePaGmktPolicyTx(List<PaGmktPolicy> policies) throws Exception{
		return paGmktCommonProcess.savePaGmktPolicy(policies);
	}
	@Override
	public String savePaGmktEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
	    return paGmktCommonProcess.savePaGmktEntpSlip(paEntpSlip);
	}
	
	@Override
	public String savePaGmktEntpSlipUpdateTx(PaEntpSlip paEntpSlip)
	    throws Exception {
	    return paGmktCommonProcess.savePaGmktEntpSlipUpdate(paEntpSlip);
	}
	
	@Override
	public List<String> selectLmsdnCodeList() throws Exception {
	    return paGmktCommonProcess.selectLmsdnCodeList();
	}
	
	@Override
	public String savePaGmktGoodsKindsMatchingTx(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception {
	    return paGmktCommonProcess.savePaGmktGoodsKindsMatching(paSiteGoodsKindsList);
	}

	@Override
	public String savePaGmktShipCostMInsertTx(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return paGmktCommonProcess.savePaGmktShipCostMInsert(paGmktShipCostM);
	}

	@Override
	public String savePaGmktShipCostMUpdateTx(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return paGmktCommonProcess.savePaGmktShipCostMUpdate(paGmktShipCostM);
	}
	@Override
	public String savePaGmktShipCostMTx(PaEntpSlip paEntpSlip) throws Exception{
		return paGmktCommonProcess.savePaGmktShipCostM(paEntpSlip);
	}
	@Override
	public String savePaGmktShipCostDtTx(PaEntpSlip paEntpSlip) throws Exception{
		return paGmktCommonProcess.savePaGmktShipCostDt(paEntpSlip);
	}
	@Override
	public HashMap<String,String> selectBeforeInsertGoodsBaseInfo(String goodsCode) throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsBaseInfo(goodsCode);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntp(String goodsCode) throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsEntp(goodsCode);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntpModify(ParamMap paramMap) throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsEntpModify(paramMap);
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsShip() throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsShip();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsShipModify() throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsShipModify();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundle() throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsBundle();
	}
	@Override
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundleModify() throws Exception{
		return paGmktCommonProcess.selectBeforeInsertGoodsBundleModify();
	}
	@Override
	public String savePaGmktOriginListTx(List<PaGmktOrigin> paGmktOriginList) throws Exception{
		return paGmktCommonProcess.savePaGmktOriginList(paGmktOriginList);
	}
	@Override
	public String savePaGmktOfferCodeListTx(List<PaOfferCode> paOfferCodenList) throws Exception{
		return paGmktCommonProcess.savePaGmktOfferCodeList(paOfferCodenList);
	}
	
	@Override
	public String savePaGmktSettleOrderTx(List<PaGmktSettlement> paGmktSettlementList) throws Exception{
		return paGmktCommonProcess.savePaGmktSettleOrder(paGmktSettlementList);
	}
	
	@Override
	public String savePaGmktSettleDeliveryTx(List<PaGmktSettlement> paGmktSettlementList) throws Exception{
		return paGmktCommonProcess.savePaGmktSettleDelivery(paGmktSettlementList);
	}
	
	@Override
	public String deletePaGmktSettleTx(ParamMap paramMap) throws Exception{
		return paGmktCommonProcess.deletePaGmktSettle(paramMap);
	}
	
	@Override
	public String updatePaGmktShipCostMUseYnTx(PaEntpSlip paEntpSlip) throws Exception{
		return paGmktCommonProcess.updatePaGmktShipCostMUseYn(paEntpSlip);
	}
	
	@Override
	public HashMap<String,String> selectPaGmktShipCostMFor500(PaGmktShipCostM paGmktShipCostM) throws Exception{
		return paGmktCommonProcess.selectPaGmktShipCostMFor500(paGmktShipCostM);
	}
	
	@Override
	public int updatePaGmktShipCostMErrorYn(PaGmktShipCostM paGmktShipCostM) throws Exception{
		return paGmktCommonProcess.updatePaGmktShipCostMErrorYn(paGmktShipCostM);
	}
	
	@Override
	public List<HashMap<String, String>> selectPaGmtkGoodsTargetList(PaEntpSlip paEntpSlip) throws Exception{
		return paGmktCommonProcess.selectPaGmtkGoodsTargetList(paEntpSlip);
	}
	
	@Override
	public int updatePaGmtkGoodsTargetOn(String goodsCode) throws Exception{
		return paGmktCommonProcess.updatePaGmtkGoodsTargetOn(goodsCode);
	}

	@Override
	public int saveTPaGmktShipCostdtTx(HashMap<String, String> custshipcost) throws Exception {
		return paGmktCommonProcess.saveTPaGmktShipCostdt(custshipcost);
	}

	@Override
	public int saveGmktShipNoTx(HashMap<String, String> templetMap) throws Exception {
		return paGmktCommonProcess.saveGmktShipNo(templetMap);
	}

	@Override
	public int savePaShipCostTx(Map<String, String> requestMap) throws Exception {
		return paGmktCommonProcess.savePaShipCost(requestMap);
	}

	@Override
	public List<HashMap<String, String>> selectNewGoodsEntpTarget(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectNewGoodsEntpTarget(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectEntpSlipCost(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipShip4Insert(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectEntpSlipShip4Insert(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectEntpSlipShip4Modify(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectEntpSlipShip4Modify(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGmktShipCostTargetList(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectPaGmktShipCostTargetList(paramMap);
	}

	@Override
	public HashMap<String, String> selectMaxShipCostFee(HashMap<String, String> templetMap) throws Exception {
		return paGmktCommonProcess.selectMaxShipCostFee(templetMap);
	}

	@Override
	public List<HashMap<String, String>> selectPaGmktShipCostDtTargetList(ParamMap paramMap) throws Exception {
		return paGmktCommonProcess.selectPaGmktShipCostDtTargetList(paramMap);
	}

	@Override
	public HashMap<String, String> selectEntpSlipCostByEntpCodeNSeq(HashMap<String, String> entpInfo) throws Exception {
		return paGmktCommonProcess.selectEntpSlipCostByEntpCodeNSeq(entpInfo);
	}

	@Override
	public String selectTConfigVal(String item) throws Exception {
		return paGmktCommonProcess.selectTConfigVal(item);
	}
}
