package com.cware.netshopping.pahalf.goods.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaHalfGoodsVO;
import com.cware.netshopping.domain.PaHalfGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaHalfGoods;
import com.cware.netshopping.domain.model.PaHalfGoodsdtMapping;

@Service("pahalf.goods.paHalfGoodsDAO")
public class PaHalfGoodsDAO extends AbstractPaDAO {

	@SuppressWarnings("unchecked")
	public List<PaHalfGoods> selectPaHalfSellStateList(ParamMap paramMap) throws Exception {
		return list("pahalf.goods.selectPaHalfSellStateList", paramMap.get());
	}

	public int updatePaHalfGoodsSellState(PaHalfGoods paHalfGoods) throws Exception {
		return update("pahalf.goods.updatePaHalfGoodsSellState",paHalfGoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaHalfGoodsdtMappingVO> selectPaHalfGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfGoodsdtMappingStockList", paramMap.get());
	}


	@SuppressWarnings("unchecked")
	public PaHalfGoodsdtMapping selectCheckPaOptionCode(ParamMap paramMap) throws Exception{
		return (PaHalfGoodsdtMapping)selectByPk("pahalf.goods.selectCheckPaOptionCode", paramMap.get());
		
	}
	
	public int updateTpaGoodsdtMapping(PaHalfGoodsdtMapping paGoodsdtMapping) throws Exception{
		return update("pahalf.goods.updateTpaGoodsdtMapping", paGoodsdtMapping);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPaHalfStockInfoList(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfStockInfoList", paramMap.get());
	}

	public int updatePaHalfGoodsdtStock(Map<String, Object> paHalfGoodsdtMapping) throws Exception{
		return update("pahalf.goods.updatePaHalfGoodsdtStock", paHalfGoodsdtMapping);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaHalfGoodsTrans(ParamMap paramMap) throws Exception {
		return  list("pahalf.goods.selectPaHalfGoodsTrans", paramMap.get());
	}

	public int updatePaHalfGoods(PaHalfGoods halfgoods) throws Exception{
		return update("pahalf.goods.updatePaHalfGoods", halfgoods);
	}

	public String selectPaShipCostCode(PaHalfGoods halfgoods) throws Exception{
		return (String)selectByPk("pahalf.goods.selectPaShipCostCode", halfgoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaHalfGoodsVO> selectPaHalfGoodsInfo(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfGoodsInfo", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<PaHalfGoodsdtMapping> selectPaHalfGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfGoodsdtInfoList", paramMap.get());
	}
	@SuppressWarnings("unchecked")
	public List<PaGoodsOffer> selectPaHalfGoodsOfferList(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfGoodsOfferList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaHalfGoodsVO> selectPaHalfGoodsImageList(ParamMap paramMap) throws Exception{
		return list("pahalf.goods.selectPaHalfGoodsImageList", paramMap.get());
	}

	public int updatePaHalfGoodsImage(PaHalfGoodsVO paHalfGoods) throws Exception{
		return update("pahalf.goods.updatePaHalfGoodsImage", paHalfGoods);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> selectGoodsEntpSlip(ParamMap paramMap) throws Exception{
		return (Map<String,String>)selectByPk("pahalf.goods.selectGoodsEntpSlip", paramMap.get());
	}

	public int updatePaGoodsTarget(PaHalfGoodsVO paHalfGoods) throws Exception{
		return update("pahalf.goods.updatePaGoodsTarget", paHalfGoods);
	}

	public PaHalfGoodsVO selectPaHalfGoodsContentsInfo(ParamMap paramMap) throws Exception{
		return (PaHalfGoodsVO)selectByPk("pahalf.goods.selectPaHalfGoodsContentsInfo", paramMap.get());
	}

	public int selectGoodsResetCount(ParamMap paramMap) throws Exception{
		return (Integer)selectByPk("pahalf.goods.selectGoodsResetCount", paramMap.get());
	}

	public int updatePaHalfGoodsOffer(PaHalfGoodsVO paHalfGoods) throws Exception{
		return update("pahalf.goods.updatePaHalfGoodsOffer", paHalfGoods);
		
	}

	/*
	 * public String selectPaHalfBrandNo(PaHalfGoodsVO paHalfGoods) throws Exception
	 * { return (String)selectByPk("pahalf.goods.selectPaHalfBrandNo", paHalfGoods);
	 * }
	 */

	public PaHalfGoodsVO selectPrdPrcNoList(ParamMap paramMap) throws Exception{
		return  (PaHalfGoodsVO)selectByPk("pahalf.goods.selectPrdPrcNoList", paramMap.get());
	}

	public int updatePaHalfGoodsReset(PaHalfGoodsVO halfGoods) throws Exception{
		return  update("pahalf.goods.updatePaHalfGoodsReset", halfGoods);
	}

	public int updatePaGoodsPrice(PaHalfGoodsVO paHalfGoods) throws Exception{
		return  update("pahalf.goods.updatePaGoodsPrice", paHalfGoods);
	}

	public int updatePaGoodsdtMappingReset(ParamMap paramMap) throws Exception{
		return  update("pahalf.goods.updatePaGoodsdtMappingReset", paramMap.get());
	}

	public String selectDelyNoAreaGb(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pahalf.goods.selectDelyNoAreaGb", paramMap.get()); 
	}

	public PaHalfGoodsVO selectPaHalfGoodsStatus(ParamMap paramMap) throws Exception{
		return (PaHalfGoodsVO)selectByPk("pahalf.goods.selectPaHalfGoodsStatus", paramMap.get()); 
	}

	public int updateProceeding(ParamMap paramMap) throws Exception{
		return update("pahalf.goods.updateProceeding", paramMap.get());

	}

	public int updateClearProceeding(ParamMap paramMap) throws Exception{
		return update("pahalf.goods.updateClearProceeding", paramMap.get());
	
	}
    public int rejectTransTarget(PaHalfGoodsVO halfGoods) {
        return  update("pahalf.goods.rejectTransTarget", halfGoods);
    }

    public int updatePaGoodsTargetReset(PaHalfGoodsVO halfGoods) {
        return  update("pahalf.goods.updatePaGoodsTargetReset", halfGoods);
    }

	public int stopTransTarget(PaHalfGoodsVO halfGoods) {
        return  update("pahalf.goods.stopTransTarget", halfGoods);
		
	}



}
