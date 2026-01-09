package com.cware.netshopping.pawemp.goods.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaWempEntpSlip;

@Service("pawemp.goods.paWempGoodsDAO")
public class PaWempGoodsDAO extends AbstractPaDAO {
	
	public PaWempGoodsVO selectPaWempGoodsInfo(ParamMap paramMap) throws Exception {
		return (PaWempGoodsVO) selectByPk("pawemp.goods.selectPaWempGoodsInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaWempGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return list("pawemp.goods.selectPaWempGoodsdtInfoList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsOfferVO> selectPaWempGoodsOfferList(ParamMap paramMap) throws Exception {
		return list("pawemp.goods.selectPaWempGoodsOfferList", paramMap.get());
	}
	
	public int insertPaWempGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsTransLog", paGoodsTransLog);
	}
	
	public PaWempEntpSlip selectPaWempEntpSlip(ParamMap paramMap) throws Exception {
		return (PaWempEntpSlip) selectByPk("pawemp.goods.selectPaWempEntpSlip", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempEntpSlip> selectPaWempEntpSlipList(ParamMap paramMap) throws Exception {
		return list("pawemp.goods.selectPaWempEntpSlip", paramMap.get());
	}
	
	public int updatePaWempGoods(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempGoods", paWempGoods);
	}

	public int updatePaWempGoodsdt(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsdt", paGoodsdtMapping);
	}
	
	public int updatePaWempGoodsImage(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsImage", paWempGoods);
	}

	public int updatePaWempGoodsPrice(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsPrice", paWempGoods);
	}

	public int updatePaWempCustShipCost(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempCustShipCost", paWempGoods);
	}

	public int updatePaWempGoodsOffer(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsOffer", paWempGoods);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempGoodsVO> selectPaWempGoodsSaleStopList(ParamMap paramMap) throws Exception {
		return list("pawemp.goods.selectPaWempGoodsSaleStopList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaWempGoodsVO> selectPaWempGoodsSaleRestartList(ParamMap paramMap) throws Exception {
		return list("pawemp.goods.selectPaWempGoodsSaleRestartList", paramMap.get());
	}
	
	/**
	 * 판매중지, 판매재개 상품 조회
	 * 
	 * @param paramMap
	 * @return PaWempGoodsVO
	 * @throws Exception
	 */
	public PaWempGoodsVO selectPaWempGoodsProductNo(ParamMap paramMap) throws Exception {
		return (PaWempGoodsVO)selectByPk("pawemp.goods.selectPaWempGoodsProductNo", paramMap.get());
	}
	
	public int updatePaWempGoodsSellStop(PaWempGoodsVO paWempGoods) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsSellStop", paWempGoods);
	}
	
	/**
	 * 상품재고수정 - 상품재고 수정list조회
	 * @param paramMap
	 * @return PaWempGoodsdtMappingVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaWempGoodsdtMappingVO> selectPaWempGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaWempGoodsdtMappingStockList", paramMap.get());
	}
	
	/**
	 * 제휴상품 정보고시 번호 업데이트
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaWempGoodsGroupNoticeNo(ParamMap paramMap) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsGroupNoticeNo", paramMap.get());
	}
	
	/**
	 * 상품 재고(수량) 업데이트
	 * @param PaGoodsdtMapping
	 * @return
	 * @throws Exception
	 */
	public int updatePaWempGoodsdtOrderAbleQty(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return update("pawemp.goods.updatePaWempGoodsdtOrderAbleQty", paGoodsdtMapping);
	}
	
	/**
	 * 상품수정대상list조회
	 * @param paramMap
	 * @return List<PaWempGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaWempGoodsVO> selectPaWempGoodsInfoList(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaWempGoodsInfo", paramMap.get());
	}
	
	/**
	 * 상품재고 동기화 - 상품정보 조회대상 조회
	 * @return List<PaWempGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaWempGoodsVO> selectPaWempGoodsStockList(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaWempGoodsStockList", paramMap.get());
	}

	/**
	 * 제휴사 상품코드 업데이트
	 * @param paWempGoods
	 * @return 
	 * @throws Exception
	 */
	public int updatePaGoodsTarget(PaWempGoodsVO paWempGoods) throws Exception{
		return update("pawemp.goods.updatePaGoodsTarget", paWempGoods);
	}
	
	/**
	 * 프로모션(자동적용쿠폰 + 제휴OUT) 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaPromoTarget", paramMap.get());
	}
	
	public int updatePaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return update("pawemp.goods.updatePaPromoTarget", paPromoTarget);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaPromoDeleteTarget", paramMap.get());
	}

	public int insertNewPaPromoTarget(PaPromoTarget paPromoTarget) throws Exception{
		return insert("pawemp.goods.insertNewPaPromoTarget", paPromoTarget);
	}

	public int updateTPapromoTarget4ExceptMemo(PaPromoTarget paPromoTarget) throws Exception {
		return update("pawemp.goods.updateTPapromoTarget4ExceptMemo", paPromoTarget);
	}
	public PaPromoTarget selectPaPromoTargetOne(ParamMap paramMap) throws Exception{
		return (PaPromoTarget)selectByPk("pawemp.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int insertNewPaPromoTargetAmt0(PaPromoTarget paPromoTarget) throws Exception{
		return insert("pawemp.goods.insertNewPaPromoTargetAmt0", paPromoTarget);
	}

	public int selectPaWempGoodsModifyCheck(PaWempGoodsVO paWempGoods) throws Exception {
		return (int)selectByPk("pawemp.goods.selectPaWempGoodsModifyCheck", paWempGoods);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaWempGoodsTrans(ParamMap paramMap) throws Exception{
		return list("pawemp.goods.selectPaWempGoodsTrans", paramMap.get());
	}

	public int updatePaWempGoodsFail(HashMap<String, String> paGoods) throws Exception{
		return update("pawemp.goods.updatePaWempGoodsFail", paGoods);
	}

	public int updateMassTargetYn(PaWempGoodsVO paWempGoods) throws Exception{
		return update("pawemp.goods.updateMassTargetYn", paWempGoods);
	}

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception{
		return update("pawemp.goods.updateMassTargetYnByEpCode", massMap);
	}
	
}
