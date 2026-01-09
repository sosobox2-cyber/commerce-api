package com.cware.netshopping.pawemp.common.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaWempBrand;
import com.cware.netshopping.domain.model.PaWempEntpSlip;
import com.cware.netshopping.domain.model.PaWempMaker;
import com.cware.netshopping.pawemp.common.process.PaWempCommonProcess;
import com.cware.netshopping.pawemp.common.service.PaWempCommonService;

@Service("pawemp.common.paWempCommonService")
public class PaWempCommonServiceImpl extends AbstractService implements PaWempCommonService{

	@Resource(name="pawemp.common.paWempCommonProcess")
	private PaWempCommonProcess paWempCommonProcess;
	
	@Override
	public String savePaWempGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		return paWempCommonProcess.savePaWempGoodsKinds(paGoodsKindsList);
	}

	@Override
	public String savePaWempOfferCodeTx(List<PaOfferCode> paOfferCodeList) throws Exception{	
		return paWempCommonProcess.savePaWempOfferCode(paOfferCodeList);
	}

	@Override
	public List<Brand> selectBrandList() throws Exception {
		return paWempCommonProcess.selectBrandList();
	}

	@Override
	public String savePaWempBrandTx(List<PaWempBrand> paWempBrandList) throws Exception {
		return paWempCommonProcess.savePaWempBrand(paWempBrandList);
	}

	@Override
	public List<Makecomp> selectMakerList() throws Exception {
		return paWempCommonProcess.selectMakerList();
	}

	@Override
	public String savePaWempMakerTx(List<PaWempMaker> paWempMakerList) throws Exception {
		return paWempCommonProcess.savePaWempMaker(paWempMakerList);
	}
	
	@Override
	public HashMap<String, Object> selectEntpShipInsertList(PaWempEntpSlip paEntpSlip) throws Exception {
		return paWempCommonProcess.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePaWempEntpSlipTx(PaWempEntpSlip paEntpSlip) throws Exception {
		return paWempCommonProcess.savePaWempEntpSlip(paEntpSlip);
	}
	
	@Override
	public List<Object> selectEntpShipUpdateList(ParamMap paramMap) throws Exception {
		return paWempCommonProcess.selectEntpShipUpdateList(paramMap);
	}

	@Override
	public String updatePaWempEntpSlipTx(PaWempEntpSlip paEntpSlip) throws Exception {
		return paWempCommonProcess.updatePaWempEntpSlip(paEntpSlip);
	}

}
