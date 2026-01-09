package com.cware.netshopping.pacommon.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.PaNoticeApplyVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTargetRec;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.SpAuthTransInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDescInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDtInfo;
import com.cware.netshopping.domain.model.SpPaGoodsInfo;
import com.cware.netshopping.domain.model.SpPaOfferInfo;

public interface PaCommonService {
	/**
	 * 인입된 제휴사 인증코드 검증
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	String selectCheckOpenApiCode(ParamMap paramMap) throws Exception;

	/**
	 * 제휴 입점 대상 상품 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<PaGoodsTargetRec> selectPaGoodsInsertTarget(ParamMap paramMap) throws Exception;
	
	/**
	 * 제휴 고마진 마진 체크
	 * @param 
	 * @return
	 * @throws Exception
	 */
	int selectEtvMarginCheck() throws Exception;

	/**
	 * 정보고시 체크
	 * @param paGoodstarget
	 * @return
	 */
	String selectPaOfferCheck(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 네이버 원산지 매핑 체크
	 * @param originCode
	 * @return
	 */
	String selectOriginMappingNaver(String originCode) throws Exception;

	String selectOriginMapping(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 이미지등록여부 체크
	 * @param goodsCode
	 * @return
	 */
	String selectImageCheck(String goodsCode) throws Exception;

	/**
	 * 기술서등록여부 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	String selectPaDescribeCheck(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 상품재고 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	String selectPaStockCheck(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 배송비금액 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	String selectPaShipCostCheck(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 마진/최저판매가 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	String selectChkMinMarSale(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 11번가 브랜드 매핑 체크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	String selectBrandMapping11st(String brandCode) throws Exception;

	/**
	 * G마켓 브랜드 매핑 체크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	String selectBrandMappingGmkt(String brandCode) throws Exception;

	/**
	 * G마켓 제조사 매핑 체크
	 * @param makecoCode
	 * @return
	 * @throws Exception
	 */
	String selectMakerMappingGmkt(String makecoCode) throws Exception;

	/**
	 * 브랜드 매핑 체크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	String selectBrandMapping(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 원산지정보 TCODE 등록여부확인
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	String selectPaOriginCheck(ParamMap paramMap) throws Exception;

	/**
	 * 모바일이용권 미대상 확인
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	String selectPaMobGiftGbCheck(String goodsCode) throws Exception;

	/**
	 * 주소체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	String selectEntpuserCheck(ParamMap paramMap) throws Exception;

	/**
	 * 단품갯수체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String selectPaGoodsdtCnt(ParamMap paramMap) throws Exception;

	/**
	 * 쿠팡 옵션명 길이 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String selectPaGoodsdtLength(ParamMap paramMap) throws Exception;

	/**
	 * 연동제한 체크 후 데이터 처리
	 * @param paGoodstarget
	 * @param paGoodsInfo
	 * @return
	 * @throws Exception
	 */
	String saveTargetTx(PaGoodsTargetRec paGoodstarget, ParamMap paGoodsInfo) throws Exception;

	/**
	 * 11번가 원산지매핑 체크
	 * @param originCode
	 * @return
	 * @throws Exception
	 */
	String selectOriginMapping11st(String originCode) throws Exception;

	/**
	 * G마켓 원산지매핑 체크
	 * @param originCode
	 * @return
	 * @throws Exception
	 */
	String selectOriginMappingGmkt(String originCode) throws Exception;

	/**
	 * 쿠팡 정보고시 체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	int selectPaCopnCheckGoodsOffer(String goodsCode) throws Exception;

	/**
	 * SK stoa 정보고시 타입명 조회 (BO통해 들어왔을 경우)
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	String selectPaCopnPaOfferBo(String goodsCode) throws Exception;

	/**
	 * SK stoa 정보고시 타입명으로 쿠팡 정보고시 매칭되는것 있는지 비교
	 * @param paGoodsInfo
	 * @return
	 * @throws Exception
	 */
	int selectCheckPaCopnAttr(ParamMap paGoodsInfo) throws Exception;

	/**
	 * SK stoa 정보고시 타입명 조회
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	String selectPaCopnPaOffer(String goodsCode) throws Exception;

	int selectCheckPaCopnAttrEtc(ParamMap paGoodsInfo) throws Exception;

	String selectPaCopnAttrEtcName(ParamMap paGoodsInfo) throws Exception;

	int inertPaGoodsQaLog(PaGoodsTargetRec paGoodstarget) throws Exception;

	int deletePaGoodsTarget(PaGoodsTargetRec paGoodstarget) throws Exception;

	/**
	 * 정보고시 항목 변경건과 정보고시 유형변경건 검색
	 * @return
	 * @throws Exception
	 */
	List<SpPaOfferInfo> selectOfferInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 정보고시 동기화
	 * @param paOfferData
	 * @param String
	 * @return
	 * @throws Exception
	 */
	String saveOfferInfoTx(SpPaOfferInfo paOfferData, Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleStopSet) throws Exception;

	List<SpPaGoodsInfo> selectGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * Strp2. 상품 정보 동기화
	 * @param paGoodsData
	 * @return
	 * @throws Exception
	 */
	String saveGoodsInfoTx(SpPaGoodsInfo paGoodsData) throws Exception;

	List<SpPaGoodsDtInfo> selectGoodsDtInfoList(ParamMap paramMap) throws Exception;

	/**
	 * Step3. 단품 정보 동기화
	 * @param paGoodsDtInfo
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	String saveGoodsDtInfoTx(SpPaGoodsDtInfo paGoodsDtInfo, String flag) throws Exception;
	
	List<SpPaGoodsDescInfo> selectPaGoodsDescInfoList(ParamMap paramMap) throws Exception;

	/**
	 * Step4. 기술서 정보 동기화
	 * @param spPaGoodsDescInfo
	 * @return
	 * @throws Exception
	 */
	String saveGoodsDescInfoTx(SpPaGoodsDescInfo paGoodsDescInfo) throws Exception;

	List<HashMap<String, String>> selectSaleEndDateTargetList() throws Exception;

	/**
	 * Step6. 판매중인 상품 판매종료일 검증
	 * @param saleEndDateTarget
	 * @return
	 * @throws Exception
	 */
	String saveSaleEndDateTargetInfoTx(HashMap<String, String> saleEndDateTarget) throws Exception;

	List<SpAuthTransInfo> selectGoodsAuthTransList() throws Exception;

	/**
	 * 상품 자동재입점 선행
	 * @param authTransInfo
	 * @return
	 * @throws Exception
	 */
	String saveGoodsAuthTransInfoTx(SpAuthTransInfo authTransInfo) throws Exception;

	List<HashMap<String, String>> selectGoodsEventTransInfoList() throws Exception;

	/**
	 * 행사상품 재입점
	 * @param goodsEventTransData
	 * @return
	 * @throws Exception
	 */
	String saveGoodsEventTransInfoTx(HashMap<String, String> goodsEventTransData) throws Exception;

	List<PaPromoTarget> selectPaPromoTargetAllList() throws Exception;

	/**
	 * 판매가 기준변경을 위한 즉시할인 프로모션 동기화 (네이버, 쿠팡, 위메프, 인터파크)
	 * @param paPromotargetAllData
	 * @return
	 * @throws Exception
	 */
	String savePaPromoTargetAllTx(PaPromoTarget paPromotargetAllData) throws Exception;
	
	List<PaGoodsImage> selectCurImageInfoList(ParamMap paramMap) throws Exception;
	
	List<PaGoodsPriceVO> selectCurPriceInfoList(ParamMap paramMap) throws Exception;
	
	List<PaCustShipCostVO> selectCurShipCostInfoList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurShipStopSaleList(ParamMap paramMap) throws Exception;
	
	List<PaEntpSlip> selectCurEntpSlipInfoList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurSaleStopList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurEventMarginList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurCheckDtCntList(ParamMap paramMap) throws Exception;
	
	List<PaLtonGoodsVO> selectCurGoodsTransQtyList(ParamMap paramMap) throws Exception;
	
	List<PaLtonGoodsdtMappingVO> selectCurGoodsDtTransQtyList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurStockCheckList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurEpNameInfoList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurEntpSlipChangeInfoList(ParamMap paramMap) throws Exception;
	
	List<HashMap<String, String>> selectCurCnShipCostInfoList(ParamMap paramMap) throws Exception;
	List<HashMap<String, String>> selectCurCnShipCostDtInfoList(ParamMap paramMap) throws Exception;
	List<HashMap<String, String>> selectCurCnShipCostTransSingle(ParamMap paramMap) throws Exception;
	List<HashMap<String, String>> selectCurCnShipCostTransMulti(ParamMap paramMap) throws Exception;
	
	HashMap<String, String> selectMinMarginPrice(ParamMap paramMap) throws Exception;
	
	HashMap<String, String> selectApplyCnCostSeq(ParamMap paramMap) throws Exception;
	
	HashMap<String, String> selectMaxOrdCost(ParamMap paramMap) throws Exception;
	
	String saveCurImageInfoTx(PaGoodsImage curImageInfoTarget) throws Exception;
	
	String saveCurPriceInfoTx(PaGoodsPriceVO curPriceInfoTarget) throws Exception;
	
	String saveCurShipCostInfoTx(PaCustShipCostVO curShipCostInfoTarget) throws Exception;
	
	String saveCurEntpSlipInfoTx(PaEntpSlip curEntpSlipInfoTarget) throws Exception;
	
	String saveCurSaleStopInfoTx(ParamMap paramMap) throws Exception;
	
	String saveCurGoodsTransQtyTx(PaLtonGoodsVO curGoodsTransQtyTarget) throws Exception;
	
	String saveCurGoodsDtTransQtyTx(PaLtonGoodsdtMappingVO curGoodsDtTransQtyTarget) throws Exception;
	
	String saveCurStockCheckTx(ParamMap paramMap) throws Exception;
	
	String saveCurNaverEntpSlipTx(ParamMap paramMap) throws Exception;
	
	String savePaCustCnShipCostTx(ParamMap paramMap) throws Exception;
	
	String saveCnCostYnTx(ParamMap paramMap) throws Exception;
	
	String saveCnCostTransTx(ParamMap paramMap) throws Exception;
	
	String saveCnCostTrans2Tx(ParamMap paramMap) throws Exception;
	
	String saveStopSaleTx(ParamMap paramMap) throws Exception;

	/**
	 * 제휴연동제외관리 SELECT
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, String>> selectGoodsPaExceptList(ParamMap paramMap) throws Exception;

	String saveGoodsPaExceptTx(HashMap<String, String> goodsPaExceptData) throws Exception;

	/**
	 * TPAGOODSDT <-> TPALTONGOODSDTMAPPING (GOODSDT_INFO)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<SpPaGoodsDtInfo> selectPaLtonSsgGoodsDtInfoList(ParamMap paramMap) throws Exception;

	/**
	 * TPALTONGOODSDTMAPPING UPDATE
	 * @param paLtonGoodsDtInfo
	 * @return
	 * @throws Exception
	 */
	String updateLtonSsgGoodsDtInfoTx(SpPaGoodsDtInfo paLtonSsgGoodsDtInfo) throws Exception;

	List<SpPaOfferInfo> selectOfferInfoListInsert(ParamMap paramMap) throws Exception;

	List<SpPaOfferInfo> selectOfferInfoListUpdate(ParamMap paramMap) throws Exception;

	void saveOfferGoodsTarget(Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleGbStopSet) throws Exception;

	void queryTestFunction() throws Exception; //진짜 의미없는 쿼리 테스트용 함수

	/**
	 * TPATMONGOODSDTMAPPING UPDATE
	 * @param paTmonGoodsDtInfo
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, String>> selectTmonCurCheckDtCntList(ParamMap paramMap) throws Exception;

	String paExceptGoodsYn(ParamMap paramMap) throws Exception;

	/**
	 * TPATMONGOODS 상품명 체크
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, String>> selectTmonCurGoodsNameLengthCheckList(ParamMap paramMap) throws Exception;

	String selectPaGoodsNameLength(ParamMap paramMap)  throws Exception;
	void checkMassModifyGoods(String paGroupCode) throws Exception;

	HashMap<String, Object> selectDescData(ParamMap paramMap) throws Exception;

	/**
	 * 제휴사 상품 정보고시 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	List<PaGoodsOfferVO> selectPaGoodsOfferList(ParamMap paramMap) throws Exception;


	/**
	 * 제휴사 단품 정보 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	//프로모션
	public void savePaPromoTargetHistory(ParamMap paramMap) throws Exception;
	List<PaPromoGoodsPrice> selectPaPromoGoodsPriceList(ParamMap paramMap) throws Exception;
	public String savePaPromoGoodsPriceTx(PaPromoGoodsPrice paPromoGoodsPrice) throws Exception;
	
	
	public List<Object> selectPaSoldOutordList(ParamMap paramMap) throws Exception;
	void saveOrderCancelCounselTx(HashMap<String, String> reqMap) throws Exception;

	/**
	 * SSG 정보고시 체크 
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	String selectPaSsgGoodsOfferCodeCheck(ParamMap paramMap)  throws Exception;

	/**
	 * SSG 정보고시 체크 - 동기화 
	 * @param ParamMap
	 * @return List<PaSsgGoodsVO> 
	 * @throws Exception
	 */
	List<PaSsgGoodsVO> selectPaSsgGoodsOfferList(ParamMap paramMap) throws Exception;	
	
	List<PaNoticeApplyVO> selectPaNoticeTargetList() throws Exception;
	
	String savePaNoticeTargetAllTx(PaNoticeApplyVO paNoticeAllData) throws Exception;

	public int saveRetentionGoodsTx(ParamMap paramMap) throws Exception;

	/**
	 * 제휴 제조사 체크 - 동기화 
	 * @param PaGoodsTargetRec
	 * @return String 
	 * @throws Exception
	 */
	String selectMakerMapping(PaGoodsTargetRec paGoodstarget) throws Exception;
	
	String saveSsgFoodInfoTx(ParamMap paramMap) throws Exception;

	/**
	 * SSG 착불상품 체크
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	String selectPaSsgGoodsCollectYnCheck(ParamMap paramMap)  throws Exception;
	
	/**
	 * SSG 착불상품 체크 - 동기화 
	 * @param ParamMap
	 * @return List<PaSsgGoodsVO> 
	 * @throws Exception
	 */
	List<PaSsgGoodsVO> selectPaSsgCollectGoodsList(ParamMap paramMap) throws Exception;

	String savePaMobileOrderCancelTx(HashMap<String, String> map) throws Exception;

}
