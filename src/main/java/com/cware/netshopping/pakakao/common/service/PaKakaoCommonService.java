package com.cware.netshopping.pakakao.common.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;

public interface PaKakaoCommonService {

	public void savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	public void savePaOrigindTx(List<PaOrigin> paOriginList) throws Exception;
	
	List<Brand> selectBrandList() throws Exception;
	
	public void savePaBrandTx(List<PaBrand> paBrandList) throws Exception;
	
	List<Makecomp> selectMakerList() throws Exception;
	
	public void savePaMakerTx(List<PaMaker> paMakerList) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoEntpSlip(ParamMap paramMap)  throws Exception;
	
	String savePaEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoEntpSlipUpdate(ParamMap paramMap)  throws Exception;
	
	String updatePaEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoModifyEntpSlip(ParamMap paramMap)  throws Exception;
}
