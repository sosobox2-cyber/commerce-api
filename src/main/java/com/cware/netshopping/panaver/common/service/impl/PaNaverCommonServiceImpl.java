package com.cware.netshopping.panaver.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panaverentpaddressmoment;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;
import com.cware.netshopping.panaver.common.process.PaNaverCommonProcess;
import com.cware.netshopping.panaver.common.service.PaNaverCommonService;

@Service("panaver.common.paNaverCommonService")
public class PaNaverCommonServiceImpl extends AbstractService implements PaNaverCommonService{
	
	@Resource(name = "panaver.common.paNaverCommonProcess")
	private PaNaverCommonProcess paNaverCommonProcess;

	@Override
	public String savePaNaverOriginListTx(List<PaNaverOrigin> paNaverOriginList) throws Exception {
		return paNaverCommonProcess.savePaNaverOriginList(paNaverOriginList);
	}
	
	@Override
	public String saveMappingPaNaverOriginTx(List<PaNaverOrigin> paNaverOriginList) throws Exception {
		return paNaverCommonProcess.saveMappingPaNaverOrigin(paNaverOriginList);
	}

	@Override
	public String savePaNaverGoodsKindsTx(List<Panavergoodskinds> paNaverGoodsKindsList, List<PaGoodsKinds> paGoodsKindsList) throws Exception {
		return paNaverCommonProcess.savePaNaverGoodsKinds(paNaverGoodsKindsList, paGoodsKindsList);
	}

	@Override
	public String savePaNaverCategoryInfoTx(List<Panaverkindsexcept> paNaverKindsExceptList, List<Panavergoodskinds> paNaverGoodsKindsList, 
											List<Panaverkindscerti> paNaverKindsCertiList, List<Panaverkindscertikinds> paNaverKindsCertiKindsList) throws Exception {
		return paNaverCommonProcess.savePaNaverCategoryInfo(paNaverKindsExceptList, paNaverGoodsKindsList, paNaverKindsCertiList, paNaverKindsCertiKindsList);
	}

	@Override
	public List<Panavergoodskinds> selectPaNaverCategoryInfo() throws Exception {
		return paNaverCommonProcess.selectPaNaverCategoryInfo();
	}
	
	@Override
	public String savePaNaverAddressListTx(List<Panaverentpaddressmoment> paNaverEntpAddressList) throws Exception {
		return paNaverCommonProcess.savePaNaverAddressList(paNaverEntpAddressList);
	}

}
