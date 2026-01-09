package com.cware.netshopping.patmon.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;
import com.cware.netshopping.patmon.common.service.PaTmonCommonService;
import com.cware.netshopping.patmon.common.process.PaTmonCommonProcess;

@Service("patmon.common.paTmonCommonService")
public class PaTmonCommonServiceImpl  extends AbstractService implements PaTmonCommonService {

	@Resource(name = "patmon.common.paTmonCommonProcess")
    private PaTmonCommonProcess paTmonCommonProcess;
	
	@Override
	public String savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		  return paTmonCommonProcess.savePaGoodsKinds(paGoodsKindsList);
	}

	@Override
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paTmonCommonProcess.selectSlipInsertList(paEntpSlip);
	}

	@Override
	public String savePaTmonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paTmonCommonProcess.savePaTmonEntpSlip(paEntpSlip);
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipUpdateList( ) throws Exception {
		return paTmonCommonProcess.selectEntpSlipUpdateList();
	}

	@Override
	public String updatePaTmonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paTmonCommonProcess.updatePaTmonEntpSlip(paEntpSlip);
	}

	@Override
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception {
		return paTmonCommonProcess.selectEntpSlipCost(apiInfoMap);
	}

	@Override
	public String updatePaTmonShipCostTx(ParamMap tmonShipCostMap) throws Exception {
		return paTmonCommonProcess.updatePaTmonShipCost(tmonShipCostMap);
	}

	@Override
	public String savePaTmonSettlementTx(List<PaTmonSettlement> paTmonSettlementList, ParamMap paramMap) throws Exception {
		return paTmonCommonProcess.savePaTmonSettlement(paTmonSettlementList, paramMap);
	}


}
