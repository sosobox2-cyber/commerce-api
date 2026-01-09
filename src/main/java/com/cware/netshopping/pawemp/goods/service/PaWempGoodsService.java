package com.cware.netshopping.pawemp.goods.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaWempGoodsVO;
import com.cware.netshopping.domain.PaWempGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaWempEntpSlip;

public interface PaWempGoodsService {
	/**
	 * 상품 정보 조회
	 * 
	 * @param paramMap
	 * @return PaWempGoodsVO
	 * @throws Exception
	 */
	public PaWempGoodsVO selectPaWempGoodsInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품 수정 대상list조회
	 * @param ParamMap
	 * @return List<PaWempGoodsVO>
	 * @throws Exception
	 */
	public List<PaWempGoodsVO> selectPaWempGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * SK스토아 단품정보 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPaWempGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 정보고시 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaGoodsOfferVO> selectPaWempGoodsOfferList(ParamMap paramMap) throws Exception;

	/**
	 * 상품전송결과 저장
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param PaWempGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempGoodsTx(PaWempGoodsVO paWempGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;

	/**
	 * 상품등록API, 출고지 유무 여부 조회
	 * 
	 * @param paramMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	public PaWempEntpSlip selectPaWempEntpSlip(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품수정API, TPAWEMPENTPSLIP에 없는 출고지 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaWempEntpSlip> selectPaWempEntpSlipList(ParamMap paramMap) throws Exception;

	/**
	 * 상품전송이력
	 * 
	 * @param PaGoodsTransLog
	 * @return int
	 * @throws Exception
	 */
	public int insertPaWempGoodsTransLogTx(PaGoodsTransLog paGoodsTransLog) throws Exception;
	
	/**
	 * 판매재게 대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaWempGoodsVO> selectPaWempGoodsSaleRestartList(ParamMap paramMap) throws Exception;
	
	/**
	 * 판매중지대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaWempGoodsVO> selectPaWempGoodsSaleStopList(ParamMap paramMap) throws Exception;

	/**
	 * 판매중지, 판매재개 상품 조회
	 * 
	 * @param paramMap
	 * @return PaWempGoodsVO
	 * @throws Exception
	 */
	public PaWempGoodsVO selectPaWempGoodsProductNo(ParamMap paramMap) throws Exception;
	
	/**
	 * 판매중지, 판매재개 상태 저장
	 * 
	 * @param PaWempGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempGoodsSellTx(PaWempGoodsVO paWempGoods) throws Exception;
	
	/**
	 * 상품재고수정 - 상품재고 수정list조회
	 * @param ParamMap
	 * @return List<PaWempGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<PaWempGoodsdtMappingVO> selectPaWempGoodsdtMappingStockList(ParamMap paramMap) throws Exception;
		
	/**
	 * 제휴상품 정보고시 번호 업데이트
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String savePaWempGoodsGroupNoticeNoTx(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품 재고(수량) 업데이트
	 * @param List<PaGoodsdtMapping>
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public String savePaWempGoodsdtOrderAbleQtyTx(List<PaGoodsdtMapping> paGoodsdtMappingList, ParamMap paramMap) throws Exception;
	
	/**
	 * 상품재고 동기화 - 상품정보 조회대상 조회
	 * @return List<PaWempGoodsVO>
	 * @throws Exception
	 */
	public List<PaWempGoodsVO> selectPaWempGoodsStockList(ParamMap paramMap) throws Exception;
	
	/**
	 * 프로모션 정보 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param paramMap
	 * @return PaPromoTarget
	 * @throws Exception
	 */
	List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectPaWempGoodsTrans(ParamMap paramMap) throws Exception;

	public int updatePaWempGoodsFail(HashMap<String, String> paGoods) throws Exception;

	public List<PaWempGoodsVO> selectPaWempGoodsInfoListMass(ParamMap paramMap) throws Exception;

	public int updateMassTargetYn(PaWempGoodsVO paWempGoods) throws Exception;

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception;
	
}
