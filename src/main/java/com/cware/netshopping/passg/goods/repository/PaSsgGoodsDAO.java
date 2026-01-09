package com.cware.netshopping.passg.goods.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaSsgDisplayMapping;
import com.cware.netshopping.domain.model.PaSsgGoodsdtMapping;

@Service("passg.goods.paSsgGoodsDAO")
public class PaSsgGoodsDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaSsgEntpSlip(ParamMap paramMap) {
		return list("passg.goods.selectPaSsgEntpSlip", paramMap.get());
	}
	
	public PaSsgGoodsVO selectPaSsgGoodsInfo(ParamMap paramMap) {
		return (PaSsgGoodsVO) selectByPk("passg.goods.selectPaSsgGoodsInfo", paramMap.get());
	}

	public int updatepaSsgGoods(PaSsgGoodsVO paSsgGoods) throws Exception {
		return update("passg.goods.updatePaSsgGoods", paSsgGoods);
	}

	public int updatepaSsgGoodsdt(PaSsgGoodsdtMapping paGoodsdtMapping)  throws Exception {
		return update("passg.goods.updatePaSsgGoodsdt", paGoodsdtMapping);
	}

	public int updatepaSsgGoodsImage(PaSsgGoodsVO paSsgGoods) {
		return update("passg.goods.updatePaSsgGoodsImage", paSsgGoods);
	}

	public int updatepaSsgGoodsPrice(PaSsgGoodsVO paSsgGoods) {
		return update("passg.goods.updatePaSsgGoodsPrice", paSsgGoods);
	}
	
	public int updatepaSsgPromoGoodsPrice(PaSsgGoodsVO paSsgGoods) {
		return update("passg.goods.updatepaSsgPromoGoodsPrice", paSsgGoods);
	}

	public int updatepaGoodsTarget(PaSsgGoodsVO paSsgGoods) {
		return update("passg.goods.updatePaGoodsTarget", paSsgGoods);
	}

	public int updatepaSsgGoodsOffer(PaSsgGoodsVO paSsgGoods) {
		return update("passg.goods.updatePaSsgGoodsOffer", paSsgGoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaSsgGoodsInfoList(ParamMap paramMap) throws Exception {
		return list("passg.goods.selectPaSsgGoodsInfo", paramMap.get());
	}

	public int updatePaSsgGoodsError(PaSsgGoodsVO paSsgGoods) throws Exception {
		return update("passg.goods.updatePaSsgGoodsError", paSsgGoods);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaSsgGoodsApprovalList(ParamMap paramMap) throws Exception {
		return list("passg.goods.selectPaSsgGoodsApprovalList", paramMap.get());
	}
	
	public int updatePaSsgGoodsApproval(PaSsgGoodsVO paSsgGoods) throws Exception {
		return update("passg.goods.updatePaSsgGoodsApproval", paSsgGoods);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception {
		return list("passg.goods.selectPaGoodsdtStockList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsdtMapping> selectPaGoodsdtStockMappingList(PaSsgGoodsVO paSsgGoods)  throws Exception {
		return list("passg.goods.selectPaGoodsdtStockMappingList", paSsgGoods);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaSellStateList(ParamMap paramMap) {
		return list("passg.goods.selectPaSellStateList", paramMap.get());
	}
	
	public int updatePaGoodsStatus(PaSsgGoodsVO sellStateTarget) {
		return update("passg.goods.updatePaGoodsStatus", sellStateTarget);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaSsgDisplayMapping> selectPaSsgDisplayList(ParamMap paramMap) {
		return list("passg.goods.selectPaSsgDisplayList",paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsdtMapping> selectPaSsgGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return list("passg.goods.selectPaSsgGoodsdtInfoList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.goods.selectPaNoticeData", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectSsgFoodInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("passg.goods.selectSsgFoodInfo", paramMap.get());
	}
}
