package com.cware.netshopping.pagmkt.goods.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGmktGoodsCertiVO;
import com.cware.netshopping.domain.PaGmktGoodsOfferVO;
import com.cware.netshopping.domain.PaGmktGoodsPriceVO;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.PaGmktGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGmktDelGoodsHis;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaProhibitWord;
import com.cware.netshopping.domain.model.PaPromoTarget;

public interface PaGmktGoodsProcess {
	/** G마켓 상품수정2.0 - 수정대상 리스트 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsModifyList(HashMap<String,Object> paramMap) throws Exception;
	public List<HashMap<String,String>> selectGmktGoodsInsertList(HashMap<String,Object> paramMap) throws Exception;
	public int selectChkShipCost(String goodsCode) throws Exception;
	public HashMap<String,Object> selectPaEbayCheckGoods(String goodsCode) throws Exception;
	/** G마켓 상품수정2.0 - 상품가격/재고/판매상태 변경건 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsPriceSaleModifyList(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 상품일괄수정 UPDATE */
	public String savePaGmktGoodsModify(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception;
	/** G마켓 상품수정2.0 - 상품일괄수정 UPDATE */
//	public String savePaGmktGoodsModify(PaGmktGoodsVO paGmktGoods) throws Exception;
	/** G마켓 상품수정2.0 - 상품가격/재고/판매상태 변경건 수정 */
	public String saveGmktGoodsPriceSaleModify(PaGmktGoods paGmktGoods) throws Exception;
	/** G마켓 상품수정2.0 - 상품명 변경건 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsNameModifyList(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 상품명 변경건 수정 */
	public String saveGmktGoodsNameModify(PaGmktGoods paGmktGoods) throws Exception;
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 리스트 조회 */
	public List<HashMap<String,Object>> selectGoodsOptionList(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 리스트 조회 */
	public List<HashMap<String,Object>> selectGoodsOption(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 리스트 조회(BO호출용) */
	public List<HashMap<String,Object>> selectGoodsOptionBO(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 저장 */
	public String saveGoodsOption(List<PaGoodsdtMapping> paGoodsdtMapping) throws Exception;
	/** G마켓 상품수정2.0 - 상품등록성공/실패건(옵션)*/
	public String savePaGmktGoodsInsertSuccessFail(PaGmktGoodsVO paGmktGoods) throws Exception;
	/** G마켓 상품수정2.0 - 상품등록 반려건*/
	public String savePaGmktGoodsInsertReject(PaGmktGoods paGmktGoods) throws Exception;
	
	
	/** G마켓 상품등록2.0 - 상품정보 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsInsertOne(ParamMap paramMap) throws Exception;
	
	/** G마켓 상품등록2.0 - 2019.02.21 판매가 기준 변경 : sale_price = sale_price - dc_amt - do_cost 조회 */
//	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception;
	/** G마켓 상품등록2.0 - 2019.02.21 판매가 기준 변경 : sale_price = sale_price - dc_amt - do_cost 조회 */
	public List<HashMap<String,Object>> selectPaGmktPromoTarget(ParamMap paramMap) throws Exception;
	/** G마켓 상품등록2.0 - 2019.02.21 판매가 기준 변경 : sale_price = sale_price - dc_amt - do_cost 조회 */
	public List<HashMap<String,Object>> selectPaIacPromoTarget(ParamMap paramMap) throws Exception;
	
	
	/** G마켓 상품등록2.0 - 상품고시정보 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsOfferInsertOne(ParamMap paramMap) throws Exception;
	/** G마켓 상품등록2.0 - 상품기술서정보 조회 */
	public HashMap<String,Object> selectGmktGoodsDescribeInsertOne(ParamMap paramMap) throws Exception;
	/** G마켓 상품등록2.0 - 상품발송정책 조회 */
	public HashMap<String,Object> selectGoodsForGmktPolicy(ParamMap paramMap) throws Exception;
	//** G마켓 상품등록2.0 - 상품등록후 테이블 update*/
	public String savePaGmktGoodsInsert(PaGmktGoodsVO paGmktGoods, List<PaPromoTarget> paPromoTargetList) throws Exception;
//	public String savePaGmktGoodsInsert(PaGmktGoodsVO paGmktGoods) throws Exception;
	/** G마켓 상품등록2.0 - 등록후 siteGoodsNo 조회대상 조회/ 미등록건 전체 대상조회 */
	public List<HashMap<String,Object>> selectSiteGoodsNoTarget(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품등록2.0 - 등록후 siteGoodsNo 업데이트 */
	/*public String saveSiteGoodsNoTarget(PaGmktGoods paGmktGoods) throws Exception;*/
	//TODO 상품입점QA 테스트용
	public HashMap<String,Object> procPaGmktAutoInsert(ParamMap paramMap) throws Exception; 
		
	/**
	 * 오픈마켓 상품 - translog 저장
	 * @param PaGoodsTransLog
	 * @return String
	 * @throws Exception
	 */
	public String insertPaGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception;
	
	/**
	 * G마켓 상품 판매자 부담할인 수정 - G마켓 상품 코드 조회
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectEsmGoodsNo(ParamMap paramMap) throws Exception;
	
	/**
	 * G마켓 상품 판매자 부담할인 수정 - 최신 가격 조회
	 * @param ParamMap
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String,Object> selectGmktGoodsPrice(ParamMap paramMap) throws Exception;
	
	/**
	 * G마켓 이미지 수정 - 상품 이미지 조회
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectGmktGoodsImageModify(ParamMap paramMap) throws Exception;
	
	/**
	 * G마켓 이미지 수정 - 타겟 업데이트
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String updatePaGmktGoodsImage(PaGmktGoods paGmktGoods) throws Exception;
	
	/**
	 * G마켓 상품 삭제
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktGoodsStatus(PaGmktDelGoodsHis paGmktDelGoodsHis) throws Exception;
	
	/**
	 * G마켓 상품 삭제 대상조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectGmktGoodsDeleteList(HashMap<String,Object> paramMap) throws Exception;
	
	/**
	 * G마켓 판매자 부담할인 대상조회
	 * @param null
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectGmktGoodsPriceList() throws Exception;
	
	/**
	 * G마켓 판매자 부담할인 - DISCOUNT_YN 업데이트
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktTransDiscount(PaGmktGoods paGmktGoods) throws Exception;
	/** G마켓 상품가격/재고/판매상태 현재상태 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsPriceSaleRealTimeList(HashMap<String,Object> paramMap) throws Exception;
	
	/** G마켓 CN/ID 배송비 조회 */
	public int selectCheckDeliveryFee(ParamMap paramMap) throws Exception;
	
	/**
	 * 지마켓+옥션 sellerId 체크
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	public String selectGmktSellerId(ParamMap paramMap) throws Exception;
	
	/**
	 * Esm 판매기한 변경 대상 상품 조회
	 * @param null
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectSalesDayModifyTarget() throws Exception;
	
	/**
	 * TPAGMKTGOODS TargetYn = 1 update
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int updateGmktGoodsforSalesDayModify(ParamMap paramMap) throws Exception;
	
	/**
	 * API코드로 TCODE에서 해당 API 타킷 건수 가져오기
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectCodeContentForTargetCnt(String apiCode) throws Exception;
	
	/**
	 * 제휴 상품명 제한 문자 조회
	 * @param paramMap
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception;
	
	/**
	 * 팔자 주문 처리 대상 상품 리스트 조회
	 * @param HashMap<String, Object>
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectGmktGoodsSellingOrderList(HashMap<String, Object> paramMap) throws Exception;
	
	/**
	 * 팔자 주문 처리 대상 상품조회
	 * @param HashMap<String, Object>
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */
	public HashMap<String, Object> selectGmktGoodsSellingOrder(HashMap<String, Object> paramMap) throws Exception;
	
	/**
	 * 팔자 주문 처리 상품 통신 후 처리
	 * @param PaGmktGoods
	 * @return  String
	 * @throws Exception
	 */
	public String saveGmktGoodsSellingOrder(PaGmktGoods paGmktGoods) throws Exception;
	
	/**
	 * 팔자 주문 처리 상품 통신 Fail Message 처리
	 * @param PaGmktGoods
	 * @return  String
	 * @throws Exception
	 */
	public String savePaGoodsSellingOrderInsertFail(PaGmktGoods paGmktGoods) throws Exception;
	
	/**
	 * 상품 옵션 등록 LIST 조회
	 * @param HashMap<String, Object>
	 * @return  List<HashMap<String, Object>>
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectGoodsOptionListForResister(HashMap<String, Object> paramMap) throws Exception;
	
	/** G마켓 상품수정2.0 - G9 전시여부 */
	public List<HashMap<String,Object>> selectG9DisplayTransList(HashMap<String,Object> paramMap) throws Exception;
	/** G마켓 상품수정2.0 - 상품명 변경건 수정 */
	public String saveGmktG9GoodsDisplay(ParamMap paramMap) throws Exception;

 
	public String updateGmktGoodsdtMappingforGoodsModify(ParamMap paramMap) throws Exception;
	public List<HashMap<String, String>> selectPagmktGoodsInfoListMass(HashMap<String, Object> target) throws Exception;
	public int updateMassTargetYn(HashMap<String, String> targetGoods) throws Exception;

	/** G마켓 리텐션 관련 정책 반영**/
	public void checkRetentionGoodsModify(ParamMap paramMap) throws Exception;
	public String getGmktSiteGb(String paCode, String goodsCode, String checkType) throws Exception;
	public List<HashMap<String, Object>> selectGmktGoodsRetentionExtendList(ParamMap paramMap) throws Exception;
	public int updateGmktSaleStopDate(ParamMap paramMap) throws Exception;
	
	/** G마켓 상품수정2.0 - 이베이 상품 유효기간 갱신 대상 조회 */
	public HashMap<String,Object> selecGoodsExpiry(HashMap<String,Object> paramMap) throws Exception;
	
	/** G마켓 상품수정2.0 - 이베이 상품 유효기간 조회 */
	public HashMap<String,String> selecGoodsExpiryDateYn(String goodsCode) throws Exception;
	
	/** G마켓 상품수정2.0 - 이베이 상품 유효기간 업데이트 */
	public int updateGoodsExpiryDate(ParamMap paramMap) throws Exception;
	
	public int selectOptionErrorChk(ParamMap paramMap) throws Exception;
	public HashMap<String, String> selectEmsCodeItemNo(PaGmktGoodsVO paGmktGoods) throws Exception;
}
