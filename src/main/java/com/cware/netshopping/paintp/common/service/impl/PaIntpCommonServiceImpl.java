package com.cware.netshopping.paintp.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaIntpDelvSettlement;
import com.cware.netshopping.domain.model.PaIntpSettlement;
import com.cware.netshopping.paintp.common.process.PaIntpCommonProcess;
import com.cware.netshopping.paintp.common.service.PaIntpCommonService;


@Service("paintp.common.paIntpCommonService")
public class PaIntpCommonServiceImpl  extends AbstractService implements PaIntpCommonService {

	@Resource(name = "paintp.common.paIntpCommonProcess")
    private PaIntpCommonProcess PaIntpCommonProcess;
	
	@Override
	public String savePaIntpGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		return PaIntpCommonProcess.savePaIntpGoodsKinds(paGoodsKindsList);
	}	
	
	@Override
	public String savePaIntpBrandTx(List<PaBrand> paBrandList) throws Exception {	
		return PaIntpCommonProcess.savePaIntpBrand(paBrandList);
	}
	
	@Override
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return PaIntpCommonProcess.selectEntpShipInsertList(paEntpSlip);
	}
	
	@Override
	public String savePaIntpEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return PaIntpCommonProcess.savePaIntpEntpSlip(paEntpSlip);
	}
	
	@Override
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
		return PaIntpCommonProcess.selectEntpShipUpdateList(paAddrGb);
	}
	
	@Override
	public String savePaIntpEntpSlipUpdateTx(PaEntpSlip paEntpSlip) throws Exception {
		return PaIntpCommonProcess.savePaIntpEntpSlipUpdate(paEntpSlip);
	}
	
	@Override
	public List<HashMap<?,?>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
		return PaIntpCommonProcess.selectEntpSlipCost(paramMap);
	}
	
	@Override
	public String savePaIntpCustShipCostTx(ParamMap paramMap)  throws Exception {
		return PaIntpCommonProcess.savePaIntpCustShipCost(paramMap) ;
	}

	@Override
	public String savePaIntpSettlementTx(List<PaIntpSettlement> paIntpSettlementList, ParamMap paramMap) throws Exception {
		return PaIntpCommonProcess.savePaIntpSettlement(paIntpSettlementList, paramMap) ;
	}

	@Override
	public String savePaIntpDelvSettlementTx(List<PaIntpDelvSettlement> paIntpSettleDelvSettlements,ParamMap paramMap) throws Exception {
		return PaIntpCommonProcess.savePaIntpDelvSettlement(paIntpSettleDelvSettlements, paramMap) ;
	}
	
}