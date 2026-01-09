package com.cware.netshopping.pakakao.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.pakakao.common.process.PaKakaoCommonProcess;
import com.cware.netshopping.pakakao.common.service.PaKakaoCommonService;

@Service("pakakao.common.paKakaoCommonService")
public class PaKakaoCommonServiceImpl  extends AbstractService implements PaKakaoCommonService {

	@Resource(name = "pakakao.common.paKakaoCommonProcess")
    private PaKakaoCommonProcess paKakaoCommonProcess;

	@Override
	public void savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		paKakaoCommonProcess.savePaGoodsKinds(paGoodsKindsList);
	}
	
	@Override
	public void savePaOrigindTx(List<PaOrigin> paOriginList) throws Exception {
		paKakaoCommonProcess.savePaOrigin(paOriginList);
	}
	
	@Override
	public List<Brand> selectBrandList() throws Exception {
		return paKakaoCommonProcess.selectBrandList();
	}
	
	@Override
	public void savePaBrandTx(List<PaBrand> paBrandList) throws Exception {
		paKakaoCommonProcess.savePaBrand(paBrandList);
	}
	
	@Override
	public List<Makecomp> selectMakerList() throws Exception {
		return paKakaoCommonProcess.selectMakerList();
	}
	
	@Override
	public void savePaMakerTx(List<PaMaker> paMakerList) throws Exception {
		paKakaoCommonProcess.savePaMaker(paMakerList);
	}
	
	@Override
	public List<PaEntpSlip> selectPaKakaoEntpSlip(ParamMap paramMap) throws Exception {
		return paKakaoCommonProcess.selectPaKakaoEntpSlip(paramMap);
	}
	
	@Override
	public String savePaEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paKakaoCommonProcess.savePaEntpSlip(paEntpSlip);
	}
	
	@Override
	public List<PaEntpSlip> selectPaKakaoEntpSlipUpdate(ParamMap paramMap) throws Exception {
		return paKakaoCommonProcess.selectPaKakaoEntpSlipUpdate(paramMap);
	}
	
	@Override
	public String updatePaEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paKakaoCommonProcess.updatePaEntpSlip(paEntpSlip);
	}
	
	@Override
	public List<PaEntpSlip> selectPaKakaoModifyEntpSlip(ParamMap paramMap) throws Exception {
		return paKakaoCommonProcess.selectPaKakaoModifyEntpSlip(paramMap);
	}
}
