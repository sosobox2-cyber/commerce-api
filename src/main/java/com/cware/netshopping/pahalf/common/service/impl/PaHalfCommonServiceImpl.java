package com.cware.netshopping.pahalf.common.service.impl;


import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfShipInfoVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaHalfBrand;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pahalf.common.process.PaHalfCommonProcess;
import com.cware.netshopping.pahalf.common.service.PaHalfCommonService;


@Service("pahalf.common.paHalfCommonService")
public class PaHalfCommonServiceImpl extends AbstractService implements PaHalfCommonService{
	
	@Resource(name="pahalf.common.paHalfCommonProcess")
	private PaHalfCommonProcess paHalfCommonProcess;

	
	@Override
	public String saveStdItemList(List<PaGoodsKinds> stdItemList) throws Exception {
		return paHalfCommonProcess.saveStdItemList(stdItemList);
	}

	public String savePaHalfBrand(List<PaHalfBrand> paHalfBrandList) throws Exception {
		return paHalfCommonProcess.savePaHalfBrand(paHalfBrandList);
	}

	@Override
	public String savePaHalfOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception {
		return paHalfCommonProcess.savePaHalfOfferCode(paOfferCodeList);
	}

	
	@Override
	public void setPaShipCostInfo(PaHalfShipInfoVO shipInfo, ParamMap apiDataMap) throws Exception {
		paHalfCommonProcess.setPaShipCostInfo(shipInfo, apiDataMap);
	}

	@Override
	public int insertTpaHalfShipInfo(ParamMap paramMap) throws Exception {
		return paHalfCommonProcess.insertTpaHalfShipInfo(paramMap);
	}

	@Override
	public int updateTpaHalfShipInfo(PaHalfShipInfoVO halfShipCostInfo) throws Exception {
		return paHalfCommonProcess.updateTpaHalfShipInfo(halfShipCostInfo);
	}

	@Override
	public PaHalfShipInfoVO selectHalfShipCostInfo(ParamMap paramMap) throws Exception {
		return paHalfCommonProcess.selectHalfShipCostInfo(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectPaHalfSlipInfoList(ParamMap paramMap) throws Exception {
		return paHalfCommonProcess.selectPaHalfSlipInfoList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectPaShipCostCode(ParamMap paramMap) throws Exception {
		return paHalfCommonProcess.selectPaShipCostCode(paramMap);
	}

	@Override
	public void insertPaHalfBrandMapping() throws Exception {
		paHalfCommonProcess.insertPaHalfBrandMapping();
	}

	@Override
	public List<HashMap<String, Object>> selectBrandFilterGoods() throws Exception {
		return paHalfCommonProcess.selectBrandFilterGoods();
	}

	@Override
	public void savePaHalfGoodsTarget(List<HashMap<String, Object>> brandFilterGoodsList) throws Exception {
		paHalfCommonProcess.savePaHalfGoodsTarget(brandFilterGoodsList);
	}

}

