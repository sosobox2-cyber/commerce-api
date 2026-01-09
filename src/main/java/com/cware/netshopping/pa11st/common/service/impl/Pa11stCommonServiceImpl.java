package com.cware.netshopping.pa11st.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.Pa11stnodelyareamVO;
import com.cware.netshopping.domain.model.Pa11stOrigin;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pa11st.common.process.Pa11stCommonProcess;
import com.cware.netshopping.pa11st.common.service.Pa11stCommonService;


@Service("pa11st.common.pa11stCommonService")
public class Pa11stCommonServiceImpl  extends AbstractService implements Pa11stCommonService {

	@Resource(name = "pa11st.common.pa11stCommonProcess")
    private Pa11stCommonProcess pa11stCommonProcess;
	
	@Override
	public String savePa11stGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		return pa11stCommonProcess.savePa11stGoodsKinds(paGoodsKindsList);
	}

	
	
	@Override
	public String saveMappingPa11stOriginTx(List<Pa11stOrigin> pa11stOriginList) throws Exception {
		return pa11stCommonProcess.saveMappingPa11stOrigin(pa11stOriginList);
	}



	@Override
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return pa11stCommonProcess.selectEntpShipInsertList(paEntpSlip);

	}

	@Override
	public String savePa11stEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return pa11stCommonProcess.savePa11stEntpSlip(paEntpSlip);

	}

	@Override
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
		return pa11stCommonProcess.selectEntpShipUpdateList(paAddrGb);
	}

	@Override
	public String savePa11stEntpSlipUpdateTx(PaEntpSlip paEntpSlip) throws Exception {
		return pa11stCommonProcess.savePa11stEntpSlipUpdate(paEntpSlip);

	}

	@Override
	public String savePa11stCertGoodsKindsTx(List<PaGoodsKinds> paGoodsKinds) throws Exception {
		return pa11stCommonProcess.savePa11stCertGoodsKinds(paGoodsKinds);
	}



	@Override
	public String savePa11stOfferCodeTx(List<PaOfferCode> paOfferCodeList)
			throws Exception {
		return pa11stCommonProcess.savePa11stOfferCode(paOfferCodeList);
	}
	
	@Override
	public List<Object> selectTroadPostList(String paCode) throws Exception {
		return pa11stCommonProcess.selectTroadPostList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaInsertTx(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		return pa11stCommonProcess.saveNoDelyAreaInsert(arrPa11stNoDelyAreaList);
	}
	
	@Override
	public String saveNoDelyAreaSelectTx(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		return pa11stCommonProcess.saveNoDelyAreaSelect(arrPa11stNoDelyAreaList);
	}

	@Override
	public List<Object> selectNoDelyAreaUpdateList(String paCode) throws Exception {
		return pa11stCommonProcess.selectNoDelyAreaUpdateList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaUpdateTx(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception {
		return pa11stCommonProcess.saveNoDelyAreaUpdate(arrPa11stNoDelyAreaList);
	}
	
	@Override
	public List<Object> selectNoDelyAreaApplyList(String paCode) throws Exception {
		return pa11stCommonProcess.selectNoDelyAreaApplyList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaApplyTx(Pa11stnodelyareamVO arrPa11stNoDelyArea) throws Exception{
		return pa11stCommonProcess.saveNoDelyAreaApply(arrPa11stNoDelyArea);
	}
	
	@Override
	public List<Object> selectNoDelyAreaDeleteList(String paCode) throws Exception {
		return pa11stCommonProcess.selectNoDelyAreaDeleteList(paCode);
	}
	
	@Override
	public String saveNoDelyAreaApplyGoodsTx(String paCode) throws Exception {
		return pa11stCommonProcess.saveNoDelyAreaApplyGoods(paCode);
	}
	
	@Override
	public int checkTpa11stNoDelyAream(String paCode) throws Exception {
		return pa11stCommonProcess.checkTpa11stNoDelyAream(paCode);
	}
	
	@Override
	public String savePa11stBrandTx(List<PaBrand> paBrandList) throws Exception {
		return pa11stCommonProcess.savePa11stBrand(paBrandList);
	}
	/**
	 * 11번가 출고지별 배송비관리 할 항목 조회
	 * */
	@Override
	public List<HashMap<?,?>> selectEntpSlipCost(String paCode) throws Exception {
		return pa11stCommonProcess.selectEntpSlipCost(paCode);
	}
	@Override
	public int updateEntpSlipCost(HashMap<String,String> hashMap) throws Exception{
		return pa11stCommonProcess.updateEntpSlipCost(hashMap);
	}
	
	@Override
	public String savePa11stSettlementTx(List<Pa11stSettlement> pa11stSettlementList) throws Exception {
		return pa11stCommonProcess.savePa11stSettlement(pa11stSettlementList);
	}
}