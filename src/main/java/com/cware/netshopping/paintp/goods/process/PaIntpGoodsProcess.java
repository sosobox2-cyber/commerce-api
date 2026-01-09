package com.cware.netshopping.paintp.goods.process;

import java.util.HashMap;
import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaIntpGoodsVO;
import com.cware.netshopping.domain.PaIntpGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;

public interface PaIntpGoodsProcess {

	/**
	 * 인터파크 상품등록 - 반품배송지조회
	 * @param ParamMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	public PaEntpSlip selectPaIntpEntpSlip(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품등록 - 상품정보 조회
	 * @param ParamMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	public PaIntpGoodsVO selectPaIntpGoodsInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품등록 - 상품정보고시list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsOffer> selectPaIntpGoodsOfferList(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품등록 - 상품단품list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPaIntpGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 오픈마켓 상품 - translog 저장
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int insertPaIntpGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception;
	
	/**
	 * 인터파크 상품등록 - 제휴사정보저장
	 * @param paIntpGoods
	 * @return
	 * @throws Exception
	 */
	public String savePaIntpGoods(PaIntpGoodsVO paIntpGoods, List<PaGoodsdtMapping> paGoodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;
	public String savePaIntpFailGoods(PaIntpGoodsVO paIntpGoods) throws Exception;
	
	/**
	 * 인터파크 상품수정 - 출고지조회
	 * @param ParamMap
	 * @return PaIntpGoodsVO
	 * @throws Exception
	 */
	public List<PaEntpSlip> selectPaIntpEntpSlipList(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품등록 - 상품수정대상list조회
	 * @param ParamMap
	 * @return List<PaIntpGoodsVO>
	 * @throws Exception
	 */
	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품재고수정 - 상품재고 수정list조회
	 * @param ParamMap
	 * @return List<PaIntpGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStockList(ParamMap paramMap) throws Exception;
	
	/**
	 * 인터파크 상품재고수정 - 제휴사상품정보저장
	 * @param PaIntpGoodsdtMappingVO
	 * @return
	 * @throws Exception
	 */
	public String savePaIntpGoodsStock(List<PaIntpGoodsdtMappingVO> paIntpGoodsMapping) throws Exception;
	
	/**
	 * 인터파크 상품재고조회
	 * @param ParamMap
	 * @return List<PaIntpGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<PaIntpGoodsdtMappingVO> selectEmptyPaOptionCodeList(ParamMap paramMap) throws Exception;
	
	/**
	 * 프로모션 정보 조회
	 * @param paramMap
	 * @return PaPromoTarget
	 * @throws Exception
	 */
	public List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception;

	/**
	 * 제휴 상품명 제한 문자 조회
	 * @param paramMap
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectGoodsLimitCharList(String paCode) throws Exception;

	public List<PaIntpGoodsdtMappingVO> selectPaIntpGoodsdtMappingStock(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectPaIntpGoodsTrans(ParamMap paramMap) throws Exception;

	public int updatePaIntpGoodsFail(HashMap<String, String> paGoodsTarget) throws Exception;

	public List<PaIntpGoodsVO> selectPaIntpGoodsInfoListMass(ParamMap paramMap)  throws Exception;

	public int updateMassTargetYn(PaIntpGoodsVO paIntpGoods)  throws Exception;

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap)  throws Exception;

	public HashMap<String, Object> selectPaNoticeData(ParamMap paramMap) throws Exception;
	
	
}
