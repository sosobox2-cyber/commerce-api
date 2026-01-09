package com.cware.netshopping.palton.common.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaLtonAttrList;
import com.cware.netshopping.domain.model.PaLtonDispCtgr;
import com.cware.netshopping.domain.model.PaLtonDispList;
import com.cware.netshopping.domain.model.PaLtonPdItmsList;
import com.cware.netshopping.domain.model.PaLtonRetrieveCode;
import com.cware.netshopping.domain.model.PaLtonSettlement;
import com.cware.netshopping.domain.model.PaLtonStdCtgr;
import com.cware.netshopping.palton.common.process.PaLtonCommonProcess;
import com.cware.netshopping.palton.common.service.PaLtonCommonService;

@Service("palton.common.paLtonCommonService")
public class PaLtonCommonServiceImpl extends AbstractService implements PaLtonCommonService{

	@Autowired
	PaLtonCommonProcess paLtonCommonProcess;

	@Override
	public String insertPaDisplayCategory(PaLtonDispCtgr displayData) throws Exception {
		return paLtonCommonProcess.insertPaDisplayCategory(displayData);
	}

	@Override
	public String saveLtonStdCategoryInfoTx(List<PaLtonDispList> disInfoList, List<PaLtonAttrList> attrInfoList, List<PaLtonPdItmsList> pdItmsInfoList, PaLtonStdCtgr stdCtgrData) throws Exception {
		return paLtonCommonProcess.saveLtonStdCategoryInfo(disInfoList, attrInfoList, pdItmsInfoList, stdCtgrData);
	}

	@Override
	public String saveLtonBrandTx(List<PaBrand> paBrandList) throws Exception {
		return paLtonCommonProcess.saveLtonBrand(paBrandList);
	}

	@Override
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paLtonCommonProcess.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaLtonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paLtonCommonProcess.savePaLtonEntpSlip(paEntpSlip);
	}

	@Override
	public List<HashMap<String,Object>> selectEntpSlipUpdateList() throws Exception {
		return paLtonCommonProcess.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaLtonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paLtonCommonProcess.updatePaLtonEntpSlip(paEntpSlip);
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap paramMap) throws Exception{
		return paLtonCommonProcess.selectEntpSlipCost(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectAddShipCost(ParamMap paramMap) throws Exception{
		return paLtonCommonProcess.selectAddShipCost(paramMap);
	}

	@Override
	public String savePaLtonCustShipCostTx(ParamMap custShipCostMap) throws Exception{
		return paLtonCommonProcess.savePaLtonCustShipCost(custShipCostMap);
	}

	@Override
	public String insertPaLtonOrigin(List<PaLtonRetrieveCode> paLtonOrigin) throws Exception {
		return paLtonCommonProcess.insertPaLtonOrigin(paLtonOrigin);
	}
	
	@Override
	public String savePaLtonAddCustShipCostTx(ParamMap addShipCostMap) throws Exception{
		return paLtonCommonProcess.savePaLtonAddCustShipCost(addShipCostMap);
	}

	@Override
	public void saveLtonSettlementTx(PaLtonSettlement settlementData, ParamMap apiDataMap) throws Exception {
		paLtonCommonProcess.saveLtonSettlement(settlementData, apiDataMap);
	}
}
