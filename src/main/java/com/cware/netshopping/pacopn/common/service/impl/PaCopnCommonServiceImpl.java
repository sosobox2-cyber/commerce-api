package com.cware.netshopping.pacopn.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.netshopping.domain.model.PaCopnCertification;
import com.cware.netshopping.domain.model.PaCopnDocment;
import com.cware.netshopping.domain.model.PaCopnOption;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.pacopn.common.process.PaCopnCommonProcess;
import com.cware.netshopping.pacopn.common.service.PaCopnCommonService;

@Service("pacopn.common.paCopnCommonService")
public class PaCopnCommonServiceImpl implements PaCopnCommonService{

	@Resource(name = "pacopn.common.paCopnCommonProcess")
	private PaCopnCommonProcess paCopnCommonProcess;

	@Override
	public String savePaCopnCategoryMetaInfoTx(List<PaCopnOption> attrList, List<PaCopnDocment> docsList, List<PaCopnCertification> certiList) throws Exception {
		return paCopnCommonProcess.savePaCopnCategoryMetaInfo(attrList,docsList,certiList);
	}

	@Override
	public Map<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
		return paCopnCommonProcess.selectEntpShipInsertList(paEntpSlip);
	}

	@Override
	public String savePaCopnEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paCopnCommonProcess.savePaCopnEntpSlip(paEntpSlip);
	}

	@Override
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
		return paCopnCommonProcess.selectEntpShipUpdateList(paAddrGb);
	}

	@Override
	public String updatePaCopnEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception {
		return paCopnCommonProcess.updatePaCopnEntpSlip(paEntpSlip);
	}
}
