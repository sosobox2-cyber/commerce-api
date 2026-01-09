package com.cware.netshopping.pacopn.goods.process;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.CopnGoodsDeleteVO;
import com.cware.netshopping.domain.PaCopnGoodsVO;
import com.cware.netshopping.domain.PaCopnGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaCopnGoodsAttri;
import com.cware.netshopping.domain.model.PaCopnGoodsUserAttri;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;

public interface PaCopnGoodsProcess {

	/**
	 * 상품등록API, 출고지 유무 여부 조회
	 * 
	 * @param paramMap
	 * @return PaEntpSlip
	 * @throws Exception
	 */
	PaEntpSlip selectPaCopnEntpSlip(ParamMap paramMap) throws Exception;

	/**
	 * 상품 정보 조회
	 * 
	 * @param paramMap
	 * @return PaCopnGoodsVO
	 * @throws Exception
	 */
	PaCopnGoodsVO selectPaCopnGoodsInfo(ParamMap paramMap) throws Exception;

	/**
	 * 정보고시 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaGoodsOfferVO> selectPaCopnGoodsOfferList(ParamMap paramMap) throws Exception;

	/**
	 * 쿠팡 매핑 옵션 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsAttri> selectPaCopnGoodsAttriList(ParamMap paramMap) throws Exception;

	/**
	 * SK스토아 단품정보 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaGoodsdtMapping> selectPaCopnGoodsdtInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 상품전송이력
	 * 
	 * @param PaGoodsTransLog
	 * @return int
	 * @throws Exception
	 */
	int insertPaCopnGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception;

	/**
	 * 상품등록API, 옵션 UPDATE
	 * 
	 * @param List<PaCopnGoodsAttri>
	 * @return String
	 * @throws Exception
	 */
	String savePaCopnGoodsAttri(List<PaCopnGoodsAttri> copnGoodsAttri) throws Exception;

	/**
	 * 상품전송결과 저장
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param PaCopnGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	String savePaCopnGoods(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;
	String savePaCopnGoods(PaCopnGoodsVO paCopnGoods, List<PaGoodsdtMapping> goodsdtMapping, List<PaPromoTarget> paPromoTargetList, PaGoodsPriceApply goodsPriceApply) throws Exception;
	String savePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception;

	/**
	 * 상품조회 API 옵션코드 UPDATE
	 * 
	 * @param PaCopnGoodsdtMappingVO
	 * @return String
	 * @throws Exception
	 */
	String savePaCopnGoodsDtOption(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception;

	/**
	 * 상품조회API, 쿠팡 상품상태 저장
	 * 
	 * @param PaCopnGoodsVO
	 * @return String
	 * @throws Exception
	 */
	String savePaCopnApprovalStatus(PaCopnGoodsVO goodsMap) throws Exception;

	/**
	 * 상품수정API, TPAENTPSLIP에 없는 출고지 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaEntpSlip> selectPaCopnEntpSlipList(ParamMap paramMap) throws Exception;

	/**
	 * 상품수정API, VENDOR_ID(TPAGOODSDTMAPPING의 PA_OPTION_CODE)가 없는 상품 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsdtMappingVO> selectEmptyVendorId(ParamMap paramMap) throws Exception;
	List<PaCopnGoodsdtMappingVO> selectEmptyProductId(ParamMap paramMap) throws Exception;

	/**
	 * 상품수정API, 승인완료가 필요한 상품 조회
	 * 
	 * @param
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsVO> selectRegisterEmpty(ParamMap paramMap) throws Exception;

	/**
	 * 판매중지, 판매재개 상품 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsProductNo(ParamMap paramMap) throws Exception;

	/**
	 * 판매중지, 판매재개 상태 저장
	 * 
	 * @param PaCopnGoodsdtMappingVO
	 * @return String
	 * @throws Exception
	 */
	String savePaCopnGoodsSell(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception;

	/**
	 * 판매중지대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsVO> selectPaCopnGoodsSaleStopList(ParamMap paramMap) throws Exception;

	/**
	 * 판매재게 대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsVO> selectPaCopnGoodsSaleRestartList(ParamMap paramMap) throws Exception;

	/**
	 * 상품수정API, 수정대상 조회
	 * 
	 * @param PaCopnGoodsdtMappingVO
	 * @return String
	 * @throws Exception
	 */
	List<PaCopnGoodsVO> selectPaCopnGoodsInfoList(ParamMap paramMap) throws Exception;

	/**
	 * 상품수정API, 옵션타입이 변경되었을 경우 판매상태 저장
	 * 
	 * @param PaCopnGoodsVO
	 * @return String
	 * @throws Exception
	 */
	String savePaChangeOptionStatus(PaCopnGoodsVO paCopnGoods) throws Exception;

	/**
	 * 상품아이템 수량변경 API, 수량변경 대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsdtMappingVO> selectPaCopnGoodsdtMappingStockList(ParamMap paramMap) throws Exception;

	/**
	 * 상품아이템 수량변경 API, 수량변경 결과 저장
	 * 
	 * @param PaCopnGoodsdtMappingVO
	 * @return String
	 * @throws Exception
	 */
	String updatePaCopnGoodsdtOrder(PaCopnGoodsdtMappingVO paCopnGoodsMapping) throws Exception;

	/**
	 * 상품아이템 수정 API, 가격변경 대상 조회
	 * 
	 * @param
	 * @return List
	 * @throws Exception
	 */
	List<PaGoodsPriceVO> selectCopnPriceModify(ParamMap paramMap) throws Exception;

	/**
	 * 상품판매가,할인율 변경 API, 가격변경 상품 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsdtMappingVO> selectCopnPriceModifyVendorIdSearch(ParamMap paramMap) throws Exception;

	/**
	 * 상품 아이템별 할인율 기준가 변경 API, 가격변경 결과 저장
	 * 
	 * @param PaCopnGoodsdtMappingVO, PaPromoTarget
	 * @return String
	 * @throws Exception
	 */
	String updatePaCopnGoodsPriceDiscount(PaCopnGoodsdtMappingVO paCopnGoodsMapping, List<PaPromoTarget> paPromoTargetList) throws Exception;

	/**
	 * 노출상품ID 저장
	 * @param goodsdtMapping
	 * @return
	 * @throws Exception
	 */
	String savePaCopnProductId(PaCopnGoodsdtMappingVO goodsdtMapping) throws Exception;
	
	/**
	 * 프로모션 정보 조회
	 * 프로모션 별 운영관리 기능 효율화(REQ_PRM_040)
	 * @param paramMap
	 * @return PaPromoTarget
	 * @throws Exception
	 */
	List<PaPromoTarget> selectPaPromoTarget(ParamMap paramMap) throws Exception;
	
	/**
	 * 제휴 상품명 제한 문자 조회
	 * @param paramMap
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception;
	
	HashMap<String, String> selectGoodsForCopnPolicy(PaCopnGoodsVO paCopnGoods) throws Exception;
	/**
	 * 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 정보 조회
	 * 
	 * @param paramMap
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String,Object> selectPaCopnAlcoutDealInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 대표 상품 정보 조회
	 * 
	 * @param paramMap
	 * @return PaCopnGoodsVO
	 * @throws Exception
	 */
	PaCopnGoodsVO selectPaCopnAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 상품/단품 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 상품등록 - REQ_PRM_041  제휴OUT 딜 상품정보 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPaCopnAlcoutDealGoodsList(ParamMap paramMap) throws Exception;
	
	/**
	 * REQ_PRM_041  쿠팡 제휴OUT 상품전송결과 저장
	 * @param PaCopnGoodsVO, List<PaGoodsdtMapping>
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnAlcoutDealGoods(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception;
	
	/**
	 * REQ_PRM_041 쿠팡 제휴OUT 상품조회 API 옵션코드 UPDATE
	 * 
	 * @param HashMap
	 * @return String
	 * @throws Exception
	 */
	public String saveAlcoutDealGoodsdtMappingPaOptionCode(HashMap<String, Object> paCopnAlcoutDealInfo) throws Exception;
	
	/**
	 * REQ_PRM_041 쿠팡  제휴OUT 딜 수정대상 딜 목록
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPaCopnModifyAlcoutDealList(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품수정API, VENDOR_ID(TPAGOODSDTMAPPING의 PA_OPTION_CODE)가 없는 상품 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectAlcoutDealEmptyVendorId() throws Exception;
	public List<HashMap<String,Object>> selectAlcoutDealEmptyProductId() throws Exception;

	/**
	 * 상품수정API, 승인완료가 필요한 상품 조회
	 * 
	 * @param
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectRegisterAlcoutDealEmpty() throws Exception;
	
	/**
	 * 제휴OUT 딜 판매중지, 판매재개 상품 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsProductNo(ParamMap paramMap) throws Exception;
	
	/**
	 * 제휴 OUT 딜 판매중지, 판매재개 상태 저장
	 * 
	 * @param PaCopnGoodsdtMappingVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaCopnAlcoutDealGoodsSell(PaCopnGoodsdtMappingVO paCopnGoodsdtMapping) throws Exception;
	
	/**
	 * 제휴OUT 딜 상품아이템 수량변경 API, 수량변경 대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaCopnGoodsdtMappingVO> selectPaCopnAlcoutDealGoodsdtMappingStockList(ParamMap paramMap) throws Exception;
	
	
	/**
	 * 제휴OUT 딜 판매중지대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleStopList(ParamMap paramMap) throws Exception;

	/**
	 * 제휴OUT 딜 판매재게 대상 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsSaleRestartList(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 - REQ_PRM_041 제휴OUT 딜 단품 추가 정보 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPaCopnNotExistsGoodsdtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 - REQ_PRM_041 제휴OUT 딜 단품 추가
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public String insertPaCopnNotExistsGoodsdt(HashMap<String, Object> paCopnNotExistsGoodsdt) throws Exception;
	
	/**
	 * 제휴OUT 딜 기술서 목록 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<PaCopnGoodsVO> selectPaCopnAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectPaCopnGoodsTrans(ParamMap paramMap) throws Exception;

	public int updatePaCopnGoodsFail(PaCopnGoodsVO paCopnGoods) throws Exception;

	public int updateMassTargetYn(PaCopnGoodsVO paCopnGoods) throws Exception;

	public List<PaCopnGoodsVO> selectPaCopnGoodsInfoListMass(ParamMap paramMap) throws Exception;

	public List<PaGoodsPriceVO> selectCopnPriceModifyMass(ParamMap paramMap) throws Exception;

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) throws Exception;
	
	/**
	 * 쿠팡 구매옵션 조회
	 * 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserAttriList(ParamMap paramMap) throws Exception;
	
	/**
	 * 쿠팡 검색옵션 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<PaCopnGoodsUserAttri> selectPaCopnGoodsUserSearchAttriList(ParamMap paramMap) throws Exception;

	/**
	 * 쿠팡 판매중 상태인 삭제 상품 조회  
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	List<HashMap<String, String>> selectDeleteGoodsSaleStatusList() throws Exception;

	/**
	 * 쿠팡 판매중 상태인 삭제 상품
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	int updateDeleteGoodsSaleStatus(HashMap<String, Object> goodsStatusMap) throws Exception;
	
	/**
	 * 쿠팡 삭제 대상 상품 조회
	 * @param goodsStatusMap 
	 * @param 
	 * @return List
	 * @throws Exception
	 */
	List<HashMap<String, String>> selectDeleteGoodsList(HashMap<String, Object> goodsStatusMap) throws Exception;
	
	/**
	 * 쿠팡 삭제 상품 내역 저장 
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	int insertDeleteGoodsHistory(HashMap<String, String> cancelGoodsMap) throws Exception;

	/**
	 * 쿠팡 상품 삭제 temp 데이터 삭제
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	int deleteTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception;

	/**
	 * 쿠팡 상품 삭제 temp 데이터 업데이트
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	int updateTempDeleteGoodsList(HashMap<String, String> cancelGoodsMap) throws Exception;

	CompletableFuture<CopnGoodsDeleteVO> asyncGoodsDelete(List<HashMap<String, String>> asyncList, ParamMap paramMap, HashMap<String, String> apiInfo)  throws Exception;
}
