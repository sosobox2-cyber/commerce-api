package com.cware.netshopping.pagmkt.goods.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGmktGoodsOfferVO;
import com.cware.netshopping.domain.PaGmktGoodsPriceVO;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.model.PaGmktDelGoodsHis;
import com.cware.netshopping.domain.model.PaGmktGoods;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;

@Service("pagmkt.goods.paGmktGoodsDAO")
@SuppressWarnings("unchecked")
public class PaGmktGoodsDAO extends AbstractPaDAO {
	/** G마켓 상품수정2.0 - 판매상태/가격/재고변경건(각target=1) 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsModifyList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsModifyList",paramMap);
	}
	public List<HashMap<String,String>> selectGmktGoodsInsertList(HashMap<String,Object> paramMap) throws Exception{
		return (List<HashMap<String,String>>) list("pagmkt.goods.selectGmktGoodsInsertList",paramMap);
	}
	public int selectChkShipCost(String goodsCode) throws Exception {
		return (int)selectByPk("pagmkt.goods.selectChkShipCost", goodsCode);
	}
	public HashMap<String,Object> selectPaEbayCheckGoods(String goodsCode) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectPaEbayCheckGoods",goodsCode);
	}
	/** G마켓 상품수정2.0 - 판매상태/가격/재고변경건(각target=1) 조회 */
    public List<HashMap<String,Object>> selectGmktGoodsPriceSaleModifyList(HashMap<String, Object> paramMap) throws Exception{
        return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsPriceSaleModifyList",paramMap);
    }
	/** G마켓 상품수정2.0 - 일괄수정 플래그 변경 */
	public int updateGmktGoodsModify(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsModify",paGmktGoods);
	}
	/** G마켓 상품수정2.0 - 판매상태 플래그 변경 */
	public int updateGmktGoodsSaleYn(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsSaleYn",paGmktGoods);
	}
	/** G마켓 상품수정2.0 - 가격정보 플래그 변경 */
	public int updateGmktGoodsPrice(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsPrice",paGmktGoods);
	}
	/** G마켓 상품수정2.0 - 가격정보 플래그 변경 */
	public int updateGmktGoodsdtMappingforGoodsModify(ParamMap paramMap) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsdtMappingforGoodsModify", paramMap.get());
	}
	/** G마켓 상품수정2.0 - 상품명 변경건 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsNameModifyList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsNameModifyList",paramMap);
	}
	/** G마켓 상품수정2.0 - 상품명 변경건 플래그 변경 */
	public int updateGmktGoodsNameYn(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsNameYn",paGmktGoods);
	}
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 리스트 조회 */
	public List<HashMap<String,Object>> selectGoodsOptionList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGoodsOptionList",paramMap);
	}
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 조회 */
	public List<HashMap<String,Object>> selectGoodsOption(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGoodsOption",paramMap);
	}
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 조회( BO호출용) */
	public List<HashMap<String,Object>> selectGoodsOptionBO(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGoodsOptionBO",paramMap);
	}
	/** G마켓 상품수정2.0 - 옵션 등록/수정건 update */
	public int updateGoodsOption(PaGoodsdtMapping paGoodsdtMapping) throws Exception {
		return update("pagmkt.goods.updateGoodsOption",paGoodsdtMapping);
	}
	/** G마켓 상품등록2.0 - 성공/실패시 update */
	public int updatePaGmktGoodsInsertSuccessFail(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsInsertSuccessFail",paGmktGoods);
	}
	/** G마켓 상품등록2.0 - 반려시 paStatus 체크 */
	public int selectChkPaStatus(PaGmktGoods paGmktGoods) throws Exception{
		return (int)selectByPk("pagmkt.goods.selectChkPaStatus",paGmktGoods);
	}
	/** G마켓 상품등록2.0 - 반려시 update */
	public int updatePaGmktGoodsInsertReject(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsInsertReject",paGmktGoods);
	}
	/** G마켓 상품등록2.0 - 상품정보 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsInsertOne(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsInsertOne",paramMap.get());
	}
	/** G마켓 상품등록2.0 - 상품정보 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsOfferInsertOne(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsOfferInsertOne",paramMap.get());
	}
	/** G마켓 상품등록2.0 - 상품정보 조회 */
	public HashMap<String,Object> selectGmktGoodsDescribeInsertOne(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectGmktGoodsDescribeInsertOne",paramMap.get());
	}
	/** G마켓 상품등록2.0 - 등록후 테이블 update */
	public int updatePaGmktGoodsInsert(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsInsert",paGmktGoods);
	}
	/** G마켓 상품등록2.0 - 등록후 siteGoodsNo 조회대상 조회/ 미등록건 전체 대상조회 */
	public List<HashMap<String,Object>> selectSiteGoodsNoTarget(HashMap<String,Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectSiteGoodsNoTarget",paramMap);
	}
	
	/** G마켓 상품등록2.0 - 등록후 siteGoodsNoExtra 업데이트
	 * promotion (auto casting)을 이용한 메서드로, VO에 존재하는 itemNoExtra를 VO가 아니어도 넘길수 있어 유동처리하게함.. 2019.02.22 thjeon  */
	public int updateSiteGoodsNoExtraTarget(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateSiteGoodsNoExtraTarget",paGmktGoods);
	}
	/** G마켓 상품등록2.0 - 등록후 siteGoodsNo 업데이트 */
	public int updateSiteGoodsNoTarget(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateSiteGoodsNoTarget",paGmktGoods);
	}
	public int selectSiteGoodsNoCheck(PaGmktGoods paGmktGoods) throws Exception{
		return (int)selectByPk("pagmkt.goods.selectSiteGoodsNoCheck",paGmktGoods);
	}
	public int updateEsmGoodsCode(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateEsmGoodsCode",paGmktGoods);
	}
	/**
	 * G마켓 상품가격 적용대상 조회
	 * @param PaGmktGoodsPriceVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaGmktGoodsPriceUpdateTargetCnt(PaGmktGoodsPriceVO paGmktGoodsPriceVO) throws Exception{
		return (int)selectByPk("pagmkt.goods.selectPaGmktGoodsPriceUpdateTargetCnt",paGmktGoodsPriceVO);
	}
	/**
	 * G마켓 상품가격,재고 수정 - 가격변경 수정
	 * @param PaGmktGoodsPriceVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktGoodsPrice(PaGmktGoodsPriceVO paGmktGoodsPriceVO) throws Exception{
	    return update("pagmkt.goods.updatePaGmktGoodsPrice", paGmktGoodsPriceVO);
	}
	/**
	 * 프로모션이 적용된 항목건 supply_price 동기화처리
	 * @param paGmktGoodsPriceVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktGoodsPrice(PaGmktGoodsPriceVO paGmktGoodsPriceVO) throws Exception{
		return insert("pagmkt.goods.insertPaGmktGoodsPrice", paGmktGoodsPriceVO);
	}
	public PaGmktGoodsPriceVO selectPaGmktGoodsPriceOne(PaGmktGoods paGmktGoods) throws Exception{
		return (PaGmktGoodsPriceVO) selectByPk("pagmkt.goods.selectPaGmktGoodsPriceOne", paGmktGoods);
	}
	
	//이베이 프로모션 조회
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectPaPromoTarget", paramMap.get());
	}
	public List<HashMap<String, Object>> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectPaPromoDeleteTarget", paramMap.get());

	}
	
	public List<HashMap<String,Object>> selectPaGmktPromoTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectPaGmktPromoTarget", paramMap.get());
	}
	public List<HashMap<String,Object>> selectPaIacPromoTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectPaIacPromoTarget", paramMap.get());
	}
	
	
	public int updatePaPromoTargetCalc(PaPromoTarget paPromoTarget) throws Exception{
		return update("pagmkt.goods.updatePaPromoTargetCalc", paPromoTarget);
	}
	public int updatePaPromoTarget(PaGmktGoodsVO paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaPromoTarget", paGmktGoods);
	}

	/**
	 * G마켓 상품등록/수정 - 고시정보 수정
	 * @param PaGmktGoodsOfferVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktGoodsOffer(PaGmktGoodsOfferVO paGmktGoodsOfferVO) throws Exception{
	    return update("pagmkt.goods.updatePaGmktGoodsOffer", paGmktGoodsOfferVO);
	}	
	
	//TARGET에서 읽어 입점QA
	public int execSP_PA_GOODS_AUTO_INSERT1(HashMap<String,Object> resultMap) throws Exception {
		return update("pagmkt.goods.execSP_PA_GOODS_AUTO_INSERT1", resultMap);
	}
	
	/**
	 * G마켓 상품등록2.0 - ESM번호 조회
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	public String selectEsmGoodsNo(ParamMap paramMap) throws Exception{
		return (String) selectByPk("pagmkt.goods.selectEsmGoodsNo", paramMap.get());
	}
	
	/**
	 * G마켓 상품등록2.0 - 상품가격 조회
	 * @param paramMap
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String,Object> selectGmktGoodsPrice(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectGmktGoodsPrice",paramMap.get());
	}
	
	/** G마켓 상품등록2.0 - 상품이미지정보 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsImageModify(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsImageModify",paramMap.get());
	}
	
	/** G마켓 상품이미지수정2.0 - 타겟 변경 */
	public int updatePaGmktGoodsImage(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsImage", paGmktGoods);
	}
	
	/** G마켓 상품삭제 - 상품번호 null로 수정 */
	public int updatePaGmktGoodsStatus(PaGmktDelGoodsHis paGmktDelGoodsHis) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsStatus", paGmktDelGoodsHis);
	}
	
	/** G마켓 상품삭제 - 삭제 대상조회 */
	public List<HashMap<String,Object>> selectGmktGoodsDeleteList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsDeleteList",paramMap);
	}
	
	/** G마켓 상품삭제 - 삭제 이력저장 */
	public int insertPaGmktGoodsHistory(PaGmktDelGoodsHis paGmktDelGoodsHis) throws Exception {
		return insert("pagmkt.goods.insertPaGmktGoodsHistory", paGmktDelGoodsHis);
	}
	
	/** G마켓 상품 판매자 부담할인- 할인 대상조회 */
	public List<HashMap<String,Object>> selectGmktGoodsPriceList() throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsPriceList", null);
	}
	
	/** G마켓 상품 판매자 부담할인 - DISCOUNT_YN 0으로 수정 */
	public int updatePaGmktTransDiscount(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktTransDiscount", paGmktGoods);
	}
	/** G마켓 상품가격/재고/판매상태 현재상태 조회 */
	public List<HashMap<String,Object>> selectGmktGoodsPriceSaleRealTimeList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsPriceSaleRealTimeList", paramMap);
	}
	/** G마켓 SITE 상품 번호 - TPAGOODSTARGET 업데이트 extra*/
	public int updatePaGoodsExtraTarget(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGoodsExtraTarget", paGmktGoods);
	}
	/** G마켓 SITE 상품 번호 - TPAGOODSTARGET 업데이트 */
	public int updatePaGoodsTarget(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGoodsTarget", paGmktGoods);
	}
	/** G마켓 + 옥션 PA_STATUS 업데이트 */
	public int updatePaGmktGoodsPaStatus(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsPaStatus", paGmktGoods);
	}
	/** 판매중지이력관리 salegb 조회 */
	public int selectPaSaleNoGoodsSaleGb(PaSaleNoGoods paSaleNoGoods) throws Exception {
		return (int)selectByPk("pagmkt.goods.selectPaSaleNoGoodsSaleGb", paSaleNoGoods);
	}
	/** 판매중지이력관리 */
	public int insertPaSaleNoGoods(PaSaleNoGoods paSaleNoGoods) throws Exception {
		return insert("pagmkt.goods.insertPaSaleNoGoods", paSaleNoGoods);
	}
	
	/** G마켓 상품등록2.0 - 발송정책 조회를 위한 상품정보 조회 */
	public HashMap<String,Object> selectGoodsForGmktPolicy(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectGoodsForGmktPolicy",paramMap.get());
	}
	/** G마켓 상품등록2.0 - 발송정책 조회 */
	public HashMap<String,Object> selectPolicyForGmktPolicy(PaGmktPolicy paGmktPolicy) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectPolicyForGmktPolicy",paGmktPolicy);
	}
	/** G마켓 상품등록2.0 - 예외대상 업체 조회 */
	public HashMap<String,String> selectExceptEntpForGmktPolicy(String entpCode, String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		paramMap.put("entpCode", entpCode);
		paramMap.put("paGroupCode", paGroupCode);
		return (HashMap<String,String>) selectByPk("pagmkt.goods.selectExceptEntpForGmktPolicy", paramMap.get());
	}
	/** G마켓 상품등록2.0 - 예외대상 카테고리[중분류전용] 조회 */
	public HashMap<String,String> selectExceptCategoryForGmktPolicy(String lmsdCode) throws Exception{
		return (HashMap<String,String>) selectByPk("pagmkt.goods.selectExceptCategoryForGmktPolicy",lmsdCode);
	}
	/** G마켓 CN/ID 배송비 조회 */
	public int selectCheckDeliveryFee(ParamMap paramMap) throws Exception {
		return (int)selectByPk("pagmkt.goods.selectCheckDeliveryFee", paramMap.get());
	}
	/** G마켓 CN/ID 배송비 조회 */
	public String selectGmktSellerId(ParamMap paramMap) throws Exception {
		return (String)selectByPk("pagmkt.goods.selectGmktSellerId", paramMap.get());
	}
	/** 이베이 그룹코드 조회 */
	public List<HashMap<String,Object>> selectPaGmktGroupCode(PaGmktGoods paGmktGoods) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectPaGmktGroupCode", paGmktGoods);
	}
	/** Esm 판매기한 변경 대상 상품 조회*/
	public List<HashMap<String,Object>> selectSalesDayModifyTarget() throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectSalesDayModifyTarget", null);
	}
	/**  TPAGMKTGOODS TargetYn = 1 update */
	public int updateGmktGoodsforSalesDayModify(ParamMap paramMap) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsforSalesDayModify", paramMap.get());
	}
	public String selectCodeContentForTargetCnt(String apiCode) throws Exception{
		return (String)selectByPk("pagmkt.goods.selectCodeContentForTargetCnt", apiCode);
	}
	/** 제휴 상품명 제한 문자 조회 */
	public List<HashMap<String, Object>> selectGoodsLimitCharList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGoodsLimitCharList", paramMap.get());
	}
	/** 팔자 주문 처리 대상 상품 리스트 조회*/
	public List<HashMap<String, Object>> selectGmktGoodsSellingOrderList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGmktGoodsSellingOrderList", paramMap);
	}
	/** 팔자 주문 처리 대상 상품 조회*/
	public HashMap<String, Object> selectGmktGoodsSellingOrder(HashMap<String, Object> paramMap) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selectGmktGoodsSellingOrder", paramMap);
	}
	/** 팔자 주문 처리 상품 통신 후 처리*/
	public int updateGmktGoodsSellingOrder(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsSellingOrder",paGmktGoods);
	}
	/** 팔자 주문 처리 상품 통신 후 FAIL MESSAGE 처리*/
	public int updateGmktGoodsSellingOrderFail(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateGmktGoodsSellingOrderFail",paGmktGoods);
	}
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) LOG성 데이터 INSERT */
	public int insertPaGoodsAuthYnLog(PaGoodsAuthYnLog paGoodsAuthYnLog) throws Exception{
		return insert("pagmkt.goods.insertPaGoodsAuthYnLog", paGoodsAuthYnLog);
	}
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) UPDATE*/
	public int updatePaGoodsTargetForAuthYn(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGoodsTargetForAuthYn",paGmktGoods);
	}
	/** 옵션 등록 리스트 조회*/
	public List<HashMap<String, Object>> selectGoodsOptionListForResister (HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectGoodsOptionListForResister",paramMap);
	}
	/** G마켓 상품수정2.0 - G9 전시여부 */
	public List<HashMap<String,Object>> selectG9DisplayTransList(HashMap<String, Object> paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pagmkt.goods.selectG9DisplayTransList",paramMap);
	}
	/** G마켓 상품수정2.0 - G9전시 완료 */
	public int updateGmktG9DisplayYn(ParamMap paramMap) throws Exception{
		return update("pagmkt.goods.updateGmktG9DisplayYn",paramMap.get());
	}
	/** G마켓 상품등록2.0 - 상품수정 중 500에러 발생 시 TPAGOODSDTMAPPING trans_target_yn = 1 처리 */
	public int updatePaGmktGoodsdtMappingTrans(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGmktGoodsdtMappingTrans",paGmktGoods);
	}
	public int insertNewPaPromoTarget(PaGmktGoodsVO paGmktGoods) throws Exception{
		return insert("pagmkt.goods.insertNewPaPromoTarget", paGmktGoods);
	}
	public HashMap<String, Object> selectLastPaPromoTarget(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>)selectByPk("pagmkt.goods.selectLastPaPromoTarget", paramMap.get());
	}
	public int updateTPapromoTarget4ExceptMemo(HashMap<String, Object> promo) throws Exception{
		return update("pagmkt.goods.updateTPapromoTarget4ExceptMemo", promo);
	}
	public HashMap<String, Object> selectPaPromoTargetOne(ParamMap paramMap) {
		return (HashMap<String, Object>)selectByPk("pagmkt.goods.selectPaPromoTargetOne", paramMap.get());
	}
	public int insertNewPaPromoTargetAmt0(PaGmktGoodsVO paGmktGoods) throws Exception{
		return insert("pagmkt.goods.insertNewPaPromoTargetAmt0", paGmktGoods);
	}
	public int selectGmktGoodsModifyCheck(PaGmktGoodsVO paGmktGoods) {
		return (int)selectByPk("pagmkt.goods.selectPaGmktGoodsModifyCheck", paGmktGoods);
	}
	public int updateMassEpGoodsModify(HashMap<String, Object> paramMap) {
		return update("pagmkt.goods.updateMassEpGoodsModify", paramMap);
	}
	public List<HashMap<String, String>> selectMassGoodsModify(HashMap<String, Object> paramMap) {
		return (List<HashMap<String,String>>) list("pagmkt.goods.selectMassGoodsModify", paramMap);
	}
	public int updateMassTargetYn(HashMap<String, String> targetGoods) {
		return update("pagmkt.goods.updateMassTargetYn", targetGoods);
	}
	public int selectOptionErrorChk(ParamMap paramMap) {
		return (int)selectByPk("pagmkt.goods.selectOptionErrorChk", paramMap.get());
	}
	public int updateReturnNote(ParamMap paramMap) {
		return update("pagmkt.goods.updateReturnNote", paramMap.get());
	}
	/*리텐션 관련 함수*/
	public HashMap<String, String> selectEmsCodeItemNo(PaGmktGoodsVO paGmktGoods) throws Exception {
		return (HashMap<String, String>)selectByPk("pagmkt.goods.selectEmsCodeItemNo", paGmktGoods);
	}
	public int updateEsmGoodsCode4Retention(PaGmktGoods paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateEsmGoodsCode4Retention",paGmktGoods);
	}
	public int updateSiteGoodsNoTarget4Retention(PaGmktGoodsVO paGmktGoods) throws Exception{
		return update("pagmkt.goods.updateSiteGoodsNoTarget4Retention", paGmktGoods);
	}
	public int updatePaGoodsTarget4Retention(PaGmktGoodsVO paGmktGoods) throws Exception{
		return update("pagmkt.goods.updatePaGoodsTarget4Retention", paGmktGoods);
	}
	/*
	public List<HashMap<String, String>> selectGmktEmsGoodsCode(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,String>>)list("pagmkt.goods.selectGmktEmsGoodsCode", paramMap.get());
	}*/
	public String selectGmktEmsGoodsCode(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pagmkt.goods.selectGmktEmsGoodsCode", paramMap.get());
	}
	public int selectliveGmktGoodsCount(ParamMap paramMap) throws Exception{
		return (int)selectByPk("pagmkt.goods.selectliveGmktGoodsCount", paramMap.get());
	}
	public List<HashMap<String, Object>> selectGmktGoodsRetentionExtendList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String, Object>>) list("pagmkt.goods.selectGmktGoodsRetentionExtendList", paramMap.get());
	}
	public int updateGmktSaleStopDate(ParamMap paramMap) throws Exception{
		return update("pagmkt.goods.updateGmktSaleStopDate", paramMap.get());
	}	
	public HashMap<String,Object> selecGoodsExpiry(HashMap<String, Object> paramMap) throws Exception{
		return (HashMap<String,Object>) selectByPk("pagmkt.goods.selecGoodsExpiry",paramMap);
	}
	public HashMap<String,String> selecGoodsExpiryDateYn(String goodsCode) throws Exception{
		return (HashMap<String,String>) selectByPk("pagmkt.goods.selecGoodsExpiryDateYn",goodsCode);
	}
	public int updateGoodsExpiryDate(ParamMap paramMap) throws Exception{
		return update("pagmkt.goods.updateGoodsExpiryDate", paramMap.get());
	}	
}
