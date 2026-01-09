package com.cware.netshopping.patmon.goods.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;

@Service("patmon.goods.paTmonGoodsDAO")
public class PaTmonGoodsDAO extends AbstractPaDAO{
	
	/**
	 * 티몬 상품등록 - 반품배송지 조회
	 * @param paramMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaTmonEntpSlip(ParamMap paramMap) {
		return list("patmon.goods.selectPaTmonEntpSlip", paramMap.get());
	}

	/**
	 * 티몬 상품등록 - 상품 정보 검색
	 * @param ParamMap
	 * @return PaTmonGoodsVO
	 * @throws Exception
	 */
	public PaTmonGoodsVO selectPaTmonGoodsInfo(ParamMap paramMap) {
		return (PaTmonGoodsVO) selectByPk("patmon.goods.selectPaTmonGoodsInfo", paramMap.get());
	}
	
	/**
	 * 정보고시 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsOfferVO> selectPaTmonGoodsOfferList(ParamMap paramMap) {
		return list("patmon.goods.selectPaTmonGoodsOfferList",paramMap.get());
	}
	
	/**
	 * 상품 단품정보 조회
	 * @param paramMap
	 * @returnList<PaGoodsdtMapping>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaTmonGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("patmon.goods.selectPaTmonGoodsdtInfoList", paramMap.get());
	}

	/**
	 * 상품 옵션 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatepaTmonGoods(PaTmonGoodsVO paTmonGoods)  throws Exception {
		return update("patmon.goods.updatePaTmonGoods", paTmonGoods);
	}

	public int updatepaTmonGoodsdt(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return update("patmon.goods.updatePaTmonGoodsdt", paGoodsdtMapping);
	}

	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) {
		return insert("patmon.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) {
		return update("patmon.goods.updatePaPromoTarget", paPromoTarget);
	}

	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) {
		return insert("patmon.goods.insertNewPaPromoTarget", paPromoTarget);
	}

	public int updatepaTmonGoodsImage(PaTmonGoodsVO paTmonGoods) {
		return update("patmon.goods.updatePaTmonGoodsImage", paTmonGoods);
	}

	public int updatepaTmonGoodsPrice(PaTmonGoodsVO paTmonGoods) {
		return update("patmon.goods.updatePaTmonGoodsPrice", paTmonGoods);
	}

	public int updatepaGoodsTarget(PaTmonGoodsVO paTmonGoods) {
		return update("patmon.goods.updatePaGoodsTarget", paTmonGoods);
	}

	public int updatepaTmonGoodsOffer(PaTmonGoodsVO paTmonGoods) {
		return update("patmon.goods.updatePaTmonGoodsOffer", paTmonGoods);
	}

	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsVO> selectPaTmonSellStateList(ParamMap paramMap) {
		return list("patmon.goods.selectPaTmonSellStateList", paramMap.get());
	}

	public int updateTmonGoodsStatus(PaTmonGoodsVO sellStateTarget) {
		return update("patmon.goods.updateTmonGoodsStatus", sellStateTarget);
	}

	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockMappingList(ParamMap paramMap)  throws Exception {
		return list("patmon.goods.selectPaTmonGoodsdtStockMappingList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaTmonEntpSlipList(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaTmonEntpSlip", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsVO> selectPaTmonGoodsInfoList(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaTmonGoodsInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtStockList(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaTmonGoodsdtStockList", paramMap.get());
	}

	public int updatePaTmonGoodsdtMappingQty(PaTmonGoodsdtMappingVO paTmonGoodsdtMappingVO) throws Exception {
		return update("patmon.goods.updatePaTmonGoodsdtMappingQty", paTmonGoodsdtMappingVO);
	}

	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsdtMappingVO> selectPaTmonGoodsdtAddedList(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaTmonGoodsdtAddedList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsVO> selectPaTmonModifyOptionList(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaTmonModifyOptionList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaPromoTarget", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception {
		return list("patmon.goods.selectPaPromoDeleteTarget", paramMap.get());
	}

	public PaPromoTarget selectPaPromoTargetOne(ParamMap paramMap) throws Exception {
		return (PaPromoTarget)selectByPk("patmon.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int updatePaTmonGoodsError(PaTmonGoodsVO paTmonGoods) throws Exception {
		return update("patmon.goods.updatePaTmonGoodsError", paTmonGoods);
	}

	public int selectPaTmonExceptShipPolicy(String goodsCode) throws Exception {
		return (Integer) selectByPk("patmon.goods.selectPaTmonExceptShipPolicy", goodsCode);
	}

}
