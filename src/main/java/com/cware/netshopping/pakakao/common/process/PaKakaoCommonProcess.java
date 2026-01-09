package com.cware.netshopping.pakakao.common.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;

public interface PaKakaoCommonProcess {

	public void savePaGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	public void savePaOrigin(List<PaOrigin> paOriginList) throws Exception;
	
	List<Brand> selectBrandList() throws Exception;
	
	public void savePaBrand(List<PaBrand> paBrandList) throws Exception;
	
	List<Makecomp> selectMakerList() throws Exception;
	
	public void savePaMaker(List<PaMaker> paMakerList) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoEntpSlip(ParamMap paramMap) throws Exception;
	
	String savePaEntpSlip(PaEntpSlip paEntpSlip) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoEntpSlipUpdate(ParamMap paramMap) throws Exception;
	
	String updatePaEntpSlip(PaEntpSlip paEntpSlip) throws Exception;
	
	List<PaEntpSlip> selectPaKakaoModifyEntpSlip(ParamMap paramMap) throws Exception;
}
