package com.cware.netshopping.paintp.goods.repository;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;

@Service("paintp.goods.paIntpGoodsDAO")
public class paIntpGoodsDAO extends AbstractPaDAO{
	
	/**
	 * 인터파크 상품등록 - 반품배송지 조회
	 * @param paramMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	public PaEntpSlip selectPaIntpEntpSlip(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "20");
		return (PaEntpSlip)selectByPk("paintp.goods.selectPaIntpEntpSlip", paramMap.get());
	}
	
	/**
	 * 인터파크 상품등록 - 상품정보 조회(묶음 배송)
	 * @param paramMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	public PaIntpGoodsVO selectPaIntpGoodsInfo(ParamMap paramMap) throws Exception{
		return (PaIntpGoodsVO)selectByPk("paintp.goods.selectPaIntpGoodsInfo", paramMap.get());
	}
	
	/**
	 * 인터파크 상품등록 - 상품정보고시조회
	 * @param paramMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsOffer> selectPaIntpGoodsOfferList(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaIntpGoodsOfferList", paramMap.get());
	}
	
	/**
	 * 인터파크 상품등록 - 상품 단품정보 조회
	 * @param paramMap
	 * @returnList<PaGoodsdtMapping>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaIntpGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaIntpGoodsdtInfoList", paramMap.get());
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpGoods(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaIntpGoods", paIntpGoods);
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpGoodsdt(PaGoodsdtMapping paGoodsdtMapping) throws Exception{
		return update("paintp.goods.updatePaIntpGoodsdt", paGoodsdtMapping);
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpGoodsImage(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaIntpGoodsImage", paIntpGoods);
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpGoodsPrice(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaIntpGoodsPrice", paIntpGoods);
	}
	
	/**
	 * 제휴사 상품코드 업데이트
	 * @param paIntpGoods
	 * @return 
	 * @throws Exception
	 */
	public int updatePaGoodsTarget(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaGoodsTarget", paIntpGoods);
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpCustShipCost(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaIntpCustShipCost", paIntpGoods);
	}
	
	/**
	 * 인터파크 상품등록 - 인터파크 상품정보 저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePaIntpGoodsOffer(PaIntpGoodsVO paIntpGoods) throws Exception{
		return update("paintp.goods.updatePaIntpGoodsOffer", paIntpGoods);
	}
	
	/**
	 * 인터파크 상품등록 - 상품출고지 조회
	 * @param paramMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPaIntpEntpSlipList(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "20");
		return list("paintp.goods.selectPaIntpEntpSlip", paramMap.get());
	}
	
	/**
	 * 인터파크 상품등록 - 상품수정대상list조회
	 * @param paramMap
	 * @return List<PaIntpGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoList(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaIntpGoodsInfo", paramMap.get());
	}
	
	/**
	 * 인터파크 상품재고수정 - 상품재고 수정list조회
	 * @param paramMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaIntpGoodsdtMappingStockList", paramMap.get());
	}	
	
	/**
	 * 인터파크 판매중지 - 상품 재고 저장
	 * @param PaIntpGoodsdtMappingVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaIntpGoodsdtMappingQty(PaIntpGoodsdtMappingVO paIntpGoodsMapping) throws Exception{
		return update("paintp.goods.updatePaIntpGoodsdtMappingQty", paIntpGoodsMapping);
	}
	
	/**
	 * 인터파크 상품재고조회
	 * @param paramMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaIntpGoodsdtMappingVO> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectEmptyPaOptionCodeList", paramMap.get());
	}	
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaPromoTarget", paramMap.get());
	}
	
	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return update("paintp.goods.updatePaPromoTarget", paPromoTarget);
	}
	
	/**
	 * 인터파크 상품명 제한 문자 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGoodsLimitCharList(String paCode) throws Exception{
		return (List<HashMap<String,Object>>) list("paintp.goods.selectGoodsLimitCharList",paCode);
	}

	@SuppressWarnings("unchecked")
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStock(ParamMap paramMap) throws Exception {
		return list("paintp.goods.selectPaIntpGoodsdtMappingStock", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return list("paintp.goods.selectPaPromoDeleteTarget", paramMap.get());
	}
	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return insert("paintp.goods.insertNewPaPromoTarget", paPromoTarget);
	}

	public int updateTPapromoTarget4ExceptMemo(PaPromoTarget paPromoTarget) throws Exception{
		return update("paintp.goods.updateTPapromoTarget4ExceptMemo", paPromoTarget);
	}
	public PaPromoTarget selectPaPromoTargetOne(ParamMap paramMap) throws Exception{
		return (PaPromoTarget)selectByPk("paintp.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) throws Exception{
		return insert("paintp.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public int selectPaIntpGoodsModifyCheck(PaIntpGoodsVO paIntpGoods) {
		return (int)selectByPk("paintp.goods.selectPaIntpGoodsModifyCheck", paIntpGoods);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaIntpGoodsTrans(ParamMap paramMap) {
		return (List<HashMap<String,String>>) list("paintp.goods.selectPaIntpGoodsTrans", paramMap.get());
	}

	public int updatePaIntpGoodsFail(HashMap<String, String> paGoodsTarget) {
		return update("paintp.goods.updatePaIntpGoodsFail", paGoodsTarget);
	}


	public int updateMassTargetYn(PaIntpGoodsVO paIntpGoods) {
		return update("paintp.goods.updateMassTargetYn", paIntpGoods);
	}

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) {
		return update("paintp.goods.updateMassTargetYnByEpCode", massMap);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("paintp.goods.selectPaNoticeData", paramMap.get());
	}
	
}
