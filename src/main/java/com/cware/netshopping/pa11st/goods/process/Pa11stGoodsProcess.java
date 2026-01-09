package com.cware.netshopping.pa11st.goods.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.Pa11stGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;


public interface Pa11stGoodsProcess {
	/**
	 * 11번가 상품등록 - 출고지조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public PaEntpSlip selectPa11stEntpSlip(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - 회수지조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public PaEntpSlip selectPa11stReturnSlip(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - 상품정보 조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stGoodsInfo(ParamMap paramMap) throws Exception;

	/**
	 * 11번가 상품등록 - 상품단품list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsdtMapping> selectPa11stGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - 상품정보고시list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMapping>
	 * @throws Exception
	 */
	public List<PaGoodsOffer> selectPa11stGoodsOfferList(ParamMap paramMap) throws Exception;
	
	
	/**
	 * 11번가 상품등록 - 제휴사정보저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoods(Pa11stGoodsVO pa11stGoods, List<PaGoodsdtMapping> pa11stGoodsdt, List<PaPromoTarget> paPromoTargetList, String prgId) throws Exception;
	
	/**
	 * 11번가 상품수정 - 출고지조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public List<PaEntpSlip> selectPa11stEntpSlipList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품수정 - 회수지조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public List<PaEntpSlip> selectPa11stReturnSlipList(ParamMap paramMap) throws Exception;
	
	
	/**
	 * 11번가 상품등록 - 상품수정대상list조회
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - 상품가격list조회
	 * @param ParamMap
	 * @return List<PaGoodsPrice>
	 * @throws Exception
	 */
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품가격 수정 - 상품가격 동기화 날짜 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsPrice(PaGoodsPriceVO paGoodsPrice, List<PaPromoTarget> paPromoTargetList) throws Exception;
	
	/**
	 * 11번가 상품옵션 수정 - 상품옵션 수정list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품옵션 수정 - 상품옵션 수정 사항 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsdtMapping(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception;
	
	/**
	 * 11번가 상품옵션 수정 - 상품옵션 재고 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsdtQty(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception;
	
	/**
	 * 11번가 상품상세설명 수정 - 상품상세설명 수정list조회
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public HashMap<String, String> selectPa11stGoodsDescribe(ParamMap paramMap) throws Exception;
	
	
	/**
	 * 11번가 상품상세설명 수정 - 상품상세설명 동기화 날짜 저장
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsDescribe(HashMap<String, String> goodsDesc) throws Exception;
	
	/**
	 * 오픈마켓 상품 - translog 저장
	 * @param paramMap
	 * @return int
	 * @throws Exception
	 */
	public int insertPa11stGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception;
	
	/**
	 * 11번가 상품재고번호저장
	 * @param List<Pa11stGoodsdtMappingVO>
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsdtMappingPaOptionCode(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception;
	
	/**
	 * 11번가 판매중지 - 11번가상품코드조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stGoodsProductNo(ParamMap paramMap) throws Exception;

	/**
	 * 11번가 판매중지 - 제휴사상품정보저장
	 * @param Pa11stGoodsVO
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsSell(Pa11stGoodsVO pa11stGoods) throws Exception;

	/**
	 * 11번가 상품등록 - 상품 판매 여부 조회
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public String checkPa11stCheckSaleGb(String goodsCode) throws Exception;
	
	/**
	 * 11번가 상품재고수정 - 상품재고 수정list조회
	 * @param ParamMap
	 * @return List<Pa11stGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingStockList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 재고번호 조회 대상 추출
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsDtStockList() throws Exception;
	
	/**
	 * 11번가 재고 품절조회-판매중지처리대상
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectedSoldOutPa11stGoodsList() throws Exception;

	public String updateSoldOutTransSaleYn(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	
	/**
	 * 11번가 단품재고 update
	 * @param Pa11stGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stGoodsdtMappingQty(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	/**
	 * 11번가 상품동기화 프로시저
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public HashMap<String,Object> procPa11stGoodsSync(ParamMap paramMap) throws Exception;

	/**
	 * 11번가 판매상태 폐기, 판매중지상품 조회 -판매중지처리대상
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleStopList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 판매중지해제처리대상
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleRestartList(ParamMap paramMap) throws Exception;
	
	public String savePa11stGoodsFail(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	public int updateTransSaleYn(ParamMap paramMap) throws Exception;
	
	public String selectPa11stGoodsDesc(String goodsCode) throws Exception;
	
	public int selectPa11stGoodsDescCnt998(ParamMap paramMap) throws Exception;
	public int selectPa11stGoodsDescCnt999(ParamMap paramMap) throws Exception;
	public String selectPa11stGoodsDesc998(ParamMap paramMap) throws Exception;
	public String selectPa11stGoodsDesc999(ParamMap paramMap) throws Exception;
	public int selectPa11stGoodsShipCnt(String goodsCode) throws Exception;
	
	public String saveStopMonitering(Pa11stGoodsVO pa11stGoods) throws Exception;
	public String saveRestartMonitering(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	public String saveShipCostPaGoods(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	/**
	 * 11번가 상품옵션 수정 - 단품매핑불가 주문list조회
	 * @param ParamMap
	 * @return List<PaGoodsdtMappingVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsdtMappingVO> selectPa11stOrderMappingList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 단품매핑불가 재고번호저장
	 * @param List<Pa11stGoodsdtMappingVO>
	 * @return
	 * @throws Exception
	 */
	public String savePa11stGoodsdtMappingOrder(List<Pa11stGoodsdtMappingVO> paGoodsdtMapping) throws Exception;
	
	public String insertCnCostMonitering(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	public String updateCnShipCostByMonitering(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	public String updatePaGoodsModifyFail(Pa11stGoodsVO pa11stGoods) throws Exception;
	/** 11번가  2019.03.18 판매가 기준 변경 : sale_price = sale_price - dc_amt - do_cost 조회 */
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception;
	/**
	 * 11번가 발송예정일 템플릿 조회
	 * @param pa11stGoods
	 * @return ParamMap
	 * @throws Exception
	 */
	public HashMap<String, String> selectGoodsFor11stPolicy(Pa11stGoodsVO pa11stGoods)throws Exception;
	
	public int updatePaStatus90(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 픽캐스트 연동 처리
	 * @param Pa11stGoodsVO
	 * @return String
	 * @throws Exception
	 */
	public String saveTpagoodsVodUrl(Pa11stGoodsVO pickCast) throws Exception;
	
	/**
	 * 11번가 상품영상 연동리스트 조회
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stVodUrlTransList() throws Exception;
	
	/**
	 * 11번가 상품영상 연동 성공 update
	 * @param Pa11stGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsVodUrlTransYn(Pa11stGoodsVO pa11stGoods) throws Exception;
	
	/**
	 * 11번가 상품영상 연동 실패 update
	 * @param Pa11stGoodsVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsVodUrlFailMsg(ParamMap paramMap) throws Exception;
	
	public int updateTransTargetYn(ParamMap paramMap) throws Exception;
	
	public int updatePa11stGoodsTransTargetYn(ParamMap paramMap) throws Exception;

	public HashMap<String, String> selectPa11stGoodsNoticeYn(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 정보 조회
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String,Object> selectPa11stAlcoutDealInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 대표 상품정보 조회
	 * @param ParamMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 상품정보 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectAlcoutDealGoodsList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품정보 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPa11stAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 프로모션대상상품 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	/* public List<HashMap<String,Object>> selectPaPromoTargetGoodsList(ParamMap paramMap) throws Exception; */
	
	/**
	 * 11번가 상품등록 - 제휴사 REQ_PRM_041 11번가 제휴OUT 딜 정보저장
	 * @param HashMap
	 * @return
	 * @throws Exception
	 */
	public String savePa11stAlcoutDeal(HashMap<String,Object> alcoutDealInfo) throws Exception;
	
	/**
	 * 11번가 REQ_PRM_041 11번가 제휴OUT 딜 상품재고번호저장
	 * @param List
	 * @return
	 * @throws Exception
	 */
	public String savePa11stAlcoutDealGoodsdtMappingPaOptionCode(List<HashMap<String,Object>> alcoutDealGoodsMappingList) throws Exception;
	
	/**
	 * 11번가 REQ_PRM_041 11번가 제휴OUT 딜 연동실패 저장
	 * @param List
	 * @return
	 * @throws Exception
	 */
	public String savePa11stAlcoutDealGoodsFail(HashMap<String,Object> alcoutDealInfo) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 수정대상 딜 목록
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPa11stModifyAlcoutDealList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품상세설명 수정 - 제휴OUT 딜 상품상세설명 수정list조회
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPa11stAlcoutDealGoodsDescribeModify(HashMap<String,Object> alcoutDealInfo) throws Exception;
	
	/**
	 * 11번가 제휴OUT 딜 상품영상 연동리스트 조회
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealVodUrlTransList() throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품 추가 정보 조회
	 * @param ParamMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPa11stNotExistsGoodsdtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품 추가
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public String insertPa11stNotExistsGoodsdt(HashMap<String, Object> pa11stNotExistsGoodsdt) throws Exception;
	
	/**
	 * 11번가 제휴OUT 딜 기술서 조회
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception;

	public int saveAlcoutDealPriceLog(List<HashMap> alcoutDealPriceLogList) throws Exception;
	
	public int selectOutDealGoodsCheck(Pa11stGoodsVO asyncPa11stGoods) throws Exception;
	
	public int updateOutDealGoodsTarget(Pa11stGoodsVO asyncPa11stGoods) throws Exception;

	public int updateMassTargetYn(Pa11stGoodsVO asyncPa11stGoods) throws Exception;

	public List<Pa11stGoodsVO> selectPa11stGoodsInfoListMass(ParamMap paramMap) throws Exception;

	public List<PaGoodsPriceVO> selectPa11stGoodsPriceListMass(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectPaGoodsTrans(ParamMap paramMap) throws Exception;

	public int updatePa11stGoodsFailInsert(HashMap<String, String> paGoods) throws Exception;

	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap)  throws Exception;

	public String savePa11stGoodsdtTargetTx(String goodsCode) throws Exception;

	public List<HashMap<String, Object>> selectPa11stModifyAlcoutDealTarget(ParamMap paramMap) throws Exception;

	public int insertAlcoutDealPriceLog(HashMap<String, Object> logMap) throws Exception;

	public String selectMaxRetentionSeq(String goodsCode, String paCode) throws Exception;
	
	public List<HashMap<String,Object>> selectPaGoodsPriceApply(ParamMap paramMap) throws Exception;

}	