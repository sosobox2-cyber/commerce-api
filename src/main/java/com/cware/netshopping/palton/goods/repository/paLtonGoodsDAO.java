package com.cware.netshopping.palton.goods.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaPromoTarget;

@Service("palton.goods.paLtonGoodsDAO")
public class paLtonGoodsDAO extends AbstractPaDAO{
	
	/**
	 * 인터파크 상품등록 - 반품배송지 조회
	 * @param paramMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	public PaEntpSlip selectPaLtonEntpSlip(ParamMap paramMap) throws Exception {
		return (PaEntpSlip) selectByPk("palton.goods.selectPaLtonEntpSlip", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaLtonEntpSlipList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonEntpSlip", paramMap.get());
	}

	/**
	 * 상품 정보 조회
	 * @param paramMap
	 * @return
	 */
	public PaLtonGoodsVO selectPaLtonGoodsInfo(ParamMap paramMap) throws Exception {
		return (PaLtonGoodsVO) selectByPk("palton.goods.selectPaLtonGoodsInfo", paramMap.get());
	}

	/**
	 * 정보고시 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsOffer> selectPaLtonGoodsOfferList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonGoodsOfferList", paramMap.get());
	}

	/**
	 * 상품 옵션 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonGoodsdtInfoList", paramMap.get());
	}

	public int updatePaLtonGoods(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoods", paLtonGoods);
	}

	public int updatePaLtonGoodsdt(PaLtonGoodsdtMappingVO paGoodsdtMapping) throws Exception {
		return update("palton.goods.updatePaLtonGoodsdt", paGoodsdtMapping);
	}

	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) throws Exception {
		return update("palton.goods.updatePaPromoTarget", paPromoTarget);
	}

	public int updatePaLtonGoodsImage(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoodsImage", paLtonGoods);
	}

	public int updatePaLtonGoodsPrice(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoodsPrice", paLtonGoods);
	}

	public int updatePaLtonGoodsTarget(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoodsTarget", paLtonGoods);
	}

	public int updatePaLtonCustShipCost(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonCustShipCost", paLtonGoods);
	}

	public int updatePaLtonGoodsOffer(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoodsOffer", paLtonGoods);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaApprovalStatusList(ParamMap paramMap)throws Exception {
		return (List<HashMap<String,String>>) list("palton.goods.selectPaApprovalStatusList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectEmptyPaOptionCodeList(ParamMap paramMap)throws Exception {
		return (List<HashMap<String,String>>) list("palton.goods.selectEmptyPaOptionCodeList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsVO> selectPaLtonSellStateList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonSellStateList", paramMap.get());
	}
	
	public int updatePaLtonGoodsSellState(PaLtonGoodsVO paLtonGoods) throws Exception {
		return update("palton.goods.updatePaLtonGoodsSellState", paLtonGoods);
	}
	

	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStockList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonGoodsdtMappingStockList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsVO> selectPaLtonGoodsInfoList(ParamMap paramMap) {
		return list("palton.goods.selectPaLtonGoodsInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectPaLtonGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonGoodsdtMappingStock", paramMap.get());
	}

	public int updatePaLtonApprovalStatus(PaLtonGoodsVO paltonGoodsVo) {
		return update("palton.goods.updatePaLtonApprovalStatus", paltonGoodsVo);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellState(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonDtSellState", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectPaLtonDtSellStateList(ParamMap paramMap) throws Exception {
		return list("palton.goods.selectPaLtonDtSellStateList", paramMap.get());
	}

	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) {
		return insert("palton.goods.insertNewPaPromoTarget", paPromoTarget);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return list("palton.goods.selectPaPromoTarget", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoDeleteTarget(ParamMap paramMap) {
		return list("palton.goods.selectPaPromoDeleteTarget", paramMap.get());
	}

	public int updateTPapromoTarget4ExceptMemo(PaPromoTarget paPromoTarget) throws Exception{
		return update("palton.goods.updateTPapromoTarget4ExceptMemo", paPromoTarget);
	}
	public PaPromoTarget selectPaPromoTargetOne(ParamMap paramMap) throws Exception{
		return (PaPromoTarget)selectByPk("palton.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) throws Exception{
		return insert("palton.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public int selectPaLtonGoodsModifyCheck(PaLtonGoodsVO paLtonGoods) {
		return (int)selectByPk("palton.goods.selectPaLtonGoodsModifyCheck", paLtonGoods);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaLtonGoodsTrans(ParamMap paramMap) {
		return list("palton.goods.selectPaLtonGoodsTrans", paramMap.get());
	}

	public int updatePaLtonGoodsFail(HashMap<String, String> paGoodsTarget) {
		return update("palton.goods.updatePaLtonGoodsFail", paGoodsTarget);
	}

	public int updateMassTargetYn(PaLtonGoodsVO paltonGoods) {
		return update("palton.goods.updateMassTargetYn", paltonGoods);
	}

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) {
		return update("palton.goods.updateMassTargetYnByEpCode", massMap);
	}

	public int updatePaLtonPaStatus(ParamMap paramMap) throws Exception {
		return update("palton.goods.updatePaLtonPaStatus", paramMap.get());
	}
}
