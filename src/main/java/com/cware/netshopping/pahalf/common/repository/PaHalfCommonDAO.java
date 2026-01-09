package com.cware.netshopping.pahalf.common.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfShipInfoVO;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;


@Service("pahalf.common.paHalfCommonDAO")
public class PaHalfCommonDAO extends AbstractPaDAO {

	public int insertPaHalfBrand(PaBrand paBrand) throws Exception {
		return insert("pahalf.common.insertPaHalfBrand", paBrand);
	}

	public int insertStdItemList(PaGoodsKinds paGoodsKinds)  throws Exception  {
		return insert("pahalf.common.insertStdItemList", paGoodsKinds);
	}


	public int insertPaHalfOfferCode(PaOfferCode paOffer) throws Exception  {
		return insert("pahalf.common.insertPaHalfOfferCode", paOffer);
	}

	public int insertTpaHalfShipInfo(ParamMap paramMap) throws Exception  {
		return insert("pahalf.common.insertTpaHalfShipInfo", paramMap.get());
	}

	public int updateTpaHalfShipInfo(PaHalfShipInfoVO halfShipCostInfo) throws Exception  {
		return update("pahalf.common.updateTpaHalfShipInfo", halfShipCostInfo);
	}

	public PaHalfShipInfoVO selectHalfShipCostInfo(ParamMap paramMap) {
		return  (PaHalfShipInfoVO) selectByPk("pahalf.common.selectHalfShipCostInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaHalfSlipInfoList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String, Object>>)list("pahalf.common.selectPaHalfSlipInfoList",paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaShipCostCode(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>) selectByPk("pahalf.common.selectPaShipCostCode", paramMap.get());
	}

	public int selectNullShipCostCode(ParamMap paramMap)  throws Exception{
		return (int) selectByPk("pahalf.common.selectNullShipCostCode", paramMap.get());
	}

	public int insertPaHalfShipInfoHistory(PaHalfShipInfoVO halfShipCostInfo)  throws Exception{
		return insert("pahalf.common.insertPaHalfShipInfoHistory", halfShipCostInfo);
	}


	public int insertPaHalfBrandMapping() throws Exception {
		return insert("pahalf.common.insertPaHalfBrandMapping", null);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectBrandFilterGoods() {
		return (List<HashMap<String, Object>>) list("pahalf.common.selectBrandFilterGoods", null);
	}

	public int insertPaHalfGoodsTarget(HashMap<String, Object> brandFilterGoods) {
		return insert("pahalf.common.insertPaHalfGoodsTarget", brandFilterGoods);
	}


	 
}
