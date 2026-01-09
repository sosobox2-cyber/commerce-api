package com.cware.netshopping.pacommon.common.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.PaTmonGoodsVO;
import com.cware.netshopping.domain.model.Mobileordercancelhis;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsTargetRec;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.model.SaleNoGoods;
import com.cware.netshopping.domain.model.SpAuthTransInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDescInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDtInfo;
import com.cware.netshopping.domain.model.SpPaGoodsInfo;
import com.cware.netshopping.domain.model.SpPaOfferInfo;
import com.cware.netshopping.domain.PaNoticeApplyVO;


@Service("pacommon.common.paCommonDAO")
public class PaCommonDAO extends AbstractPaDAO{
		
	/**
	 * TPAORDERM INSERT
	 * @param Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPaOrderM(Paorderm paOrderm) throws Exception{
		return insert("pacommon.pacommon.insertPaOrderM", paOrderm);
	}
	
	/**
	 * 전체카테고리moment삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaGoodsKindsListMoment(String paGroupCode) throws Exception{
		return delete("pacommon.pacommon.deletePaGoodsKindsListMoment", paGroupCode);
	}
	
	/**
	 * 전체카테고리moment저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaGoodsKindsListMoment(PaGoodsKinds paGoodsKinds) throws Exception{
		return insert("pacommon.pacommon.insertPaGoodsKindsListMoment", paGoodsKinds);
	}
	/**
	 * 전체카테고리저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaGoodsKindsList(PaGoodsKinds paGoodsKinds) throws Exception{
		return insert("pacommon.pacommon.insertPaGoodsKindsList", paGoodsKinds);
	}
	/**
	 * 전체카테고리변경저장
	 * @param PaGoodsKinds
	 * @return Integer
	 * @throws Exception
	 */
	public Integer updatePaGoodsKindsList(PaGoodsKinds paGoodsKinds) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsKindsList", paGoodsKinds);
	}
	
	/**
	 * 전체카테고리삭제
	 * @param PaGoodsKinds
	 * @return Integer
	 * @throws Exception
	 */
	public Integer updatePaGoodsKindsListUseYn(PaGoodsKinds paGoodsKinds) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsKindsListUseYn", paGoodsKinds);
	}
	
	/**
	 * 상품정보 전송 이력 관리
	 * @param PaGoodsTransLog
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPaGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception{
		return insert("pacommon.pacommon.insertPaGoodsTransLog", paGoodsTransLog);
	}
	
	/**
	 * 인입된 제휴사 인증코드 검증
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	public String selectCheckOpenApiCode(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectCheckOpenApiCode", paramMap.get());
	}
	

	/**
	 * 상품 판매 여부 조회
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectGoodsSaleGb(String goodsCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectGoodsSaleGb", goodsCode);
	}

	/**
	 * TPAORDERM UPDATE
	 * @param Paorderm
	 * @return Integer
	 * @throws Exception
	 */
	public int updatePaOrderM(Paorderm paOrderm) throws Exception{
		return insert("pacommon.pacommon.updatePaOrderM", paOrderm);
	}
	
	/**
	 * 정산삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePa11stSettlementList(String gatherDate) throws Exception{
		return delete("pacommon.pacommon.deletePa11stSettlementList", gatherDate);
	}
	
	/**
	 * 정산저장
	 * @param Pa11stSettlement
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stSettlementList(Pa11stSettlement pa11stSettlement) throws Exception{
		return insert("pacommon.pacommon.insertPa11stSettlementList", pa11stSettlement);
	}

	/**
	 * 제휴 입점 대상 상품 정보 조회
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsTargetRec> selectPaGoodsInsertTarget(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaGoodsInsertTarget", paramMap.get());
	}
	
	/**
	 * 제휴 모바일 고마진 마진 체크
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int selectEtvMarginCheck() throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectEtvMarginCheck", null);
	}
	
	
	/**
	 * 정보고시 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int selectPaOfferCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaOfferCheck", paGoodstarget);
	}

	/**
	 * 네이버 원산지 매핑 체크
	 * @param originCode
	 * @return
	 * @throws Exception
	 */
	public String selectOriginMappingNaver(String originCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectOriginMappingNaver", originCode);
	}

	public String selectOriginMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectOriginMapping", paGoodstarget);
	}

	/**
	 * 이미지등록여부 체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectImageCheck(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectImageCheck", goodsCode);
	}

	/**
	 * 기술서등록여부 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int selectPaDescribeCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaDescribeCheck", paGoodstarget);
	}

	/**
	 * 상품재고 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int selectPaStockCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaStockCheck", paGoodstarget);
	}

	/**
	 * 배송비금액 체크
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int selectPaShipCostCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaShipCostCheck", paGoodstarget);
	}

	/**
	 * 마진/최소판매가 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectChkMinMarSale(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectChkMinMarSale", paramMap.get());
	}

	/**
	 * 11번가 브랜드 매핑 치크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	public String selectBrandMapping11st(String brandCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectBrandMapping11st", brandCode);
	}

	/**
	 * G마켓 브랜드 매핑 체크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	public String selectBrandMappingGmkt(String brandCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectBrandMappingGmkt", brandCode);
	}

	/**
	 * G마켓 제조사 매핑 체크
	 * @param makecoCode
	 * @return
	 * @throws Exception
	 */
	public String selectMakerMappingGmkt(String makecoCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectMakerMappingGmkt", makecoCode);
	}

	/**
	 * 브랜드 매핑 체크
	 * @param brandCode
	 * @return
	 * @throws Exception
	 */
	public String selectBrandMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectBrandMapping", paGoodstarget);
	}

	/**
	 * 원산지정보 TCODE 등록여부확인
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaOriginCheck(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaOriginCheck", goodsCode);
	}

	/**
	 * 모바일이용권 미대상 확인
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaMobGiftGbCheck(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaMobGiftGbCheck", goodsCode);
	}

	/**
	 * 쿠팡 주소체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaEntpuserCheckCopn(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaEntpuserCheckCopn", goodsCode);
	}

	/**
	 * 주소체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectEntpuserCheck(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectEntpuserCheck", goodsCode);
	}
	
	public int selectEntpuserCheckSsg(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectEntpuserCheckSsg", goodsCode);
	}

	/**
	 * 위메프 단품갯수체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaGoodsdtCnt(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaGoodsdtCnt", goodsCode);
	}

	/**
	 * 쿠팡 옵션명 길이 체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaGoodsdtLength(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaGoodsdtLength", goodsCode);
	}

	/**
	 * 11번가 원산지매핑 체크
	 * @param originCode
	 * @return
	 * @throws Exception
	 */
	public String selectOriginMapping11st(String originCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectOriginMapping11st", originCode);
	}

	/**
	 * G마켓 원산지매핑 체크
	 * @param originCode
	 * @return
	 * @throws Exception
	 */
	public String selectOriginMappingGmkt(String originCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectOriginMappingGmkt", originCode);
	}

	/**
	 * TPA11STGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPa11stGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPa11stGoods", paGoodstarget);
	}

	/**
	 * TPAGMKTGOODS INSERT
	 * @param paGoodstarget
	 * @throws Exception
	 */
	public int insertPaGmktGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGmktGoods", paGoodstarget);
	}

	/**
	 * TPANAVERGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaNaverGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaNaverGoods", paGoodstarget);
	}

	/**
	 * TPACOPNGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaCopnGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaCopnGoods", paGoodstarget);
	}

	/**
	 * TPAWEMPGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaWempGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaWempGoods", paGoodstarget);
	}

	/**
	 * TPAINTPGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaIntpGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaIntpGoods", paGoodstarget);
	}

	/**
	 * TPALTONGOODS INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaLtonGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaLtonGoods", paGoodstarget);
	}

	/**
	 * TPAGOODSPRICE INSERT (for GMKT)
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaGmktGoodsPrice(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGmktGoodsPrice", paGoodstarget);
	}

	/**
	 * TPAGOODSPRICE INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaGoodsPrice(PaGoodsTargetRec paGoodstarget) throws Exception{
		return insert("pacommon.pacommon.insertPaGoodsPrice", paGoodstarget);
	}

	/**
	 * 11번가 TPAPROMOTARGET INSERT
	 * @param paGoodstarget
	 * @throws Exception
	 */
	public int insertPaPromoTarget(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaPromoTarget", paGoodstarget);
	}


//	/**
//	 * Ebay TPAPROMOTARGET INSERT
//	 * @param paGoodstarget
//	 * @throws Exception
//	 */
//	public int insertPaPromoTargetEbay(PaGoodsTargetRec paGoodstarget) throws Exception {
//		return insert("pacommon.pacommon.insertPaPromoTargetEbay", paGoodstarget);
//	}
	
	/**
	 * TPAPROMOTARGET INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaPromoTargetCommon(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaPromoTargetCommon", paGoodstarget);
	}

	/**
	 * TPAGOODSIMAGE INSERT
	 * @param paGoodstarget
	 * @throws Exception
	 */
	public int insertPaGoodsImage(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsImage", paGoodstarget);
	}

	/**
	 * TPAGOODSDT INSERT
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int insertPaGoodsDt(String goodsCode) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsDt", goodsCode);
	}

	/**
	 * TPAGOODSDTMAPPING INSERT
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaGoodsDtMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsDtMapping", paGoodstarget);
	}

	/**
	 * 쿠팡 정보고시 체크
	 * @param goodsCode
	 * @return
	 * @throws Exception
	 */
	public int selectPaCopnCheckGoodsOffer(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaCopnCheckGoodsOffer", goodsCode);
	}

	public String selectPaCopnPaOfferBo(String goodsCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPaCopnPaOfferBo", goodsCode);
	}

	public int selectCheckPaCopnAttr(ParamMap paGoodsInfo) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectCheckPaCopnAttr", paGoodsInfo.get());
	}

	public String selectPaCopnPaOffer(String goodsCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPaCopnPaOffer", goodsCode);
	}

	public int selectCheckPaCopnAttrEtc(ParamMap paGoodsInfo) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectCheckPaCopnAttrEtc", paGoodsInfo.get());
	}

	public String selectPaCopnAttrEtcName(ParamMap paGoodsInfo) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPaCopnAttrEtcName", paGoodsInfo.get());
	}

	public int insertPaGoodsOfferCopnEtc(String goodsCode) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsOfferCopnEtc", goodsCode);
	}

	public int insertPaGoodsOfferCopnName(ParamMap paGoodsInfo) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsOfferCopnName", paGoodsInfo.get());
	}

	public int insertPaGoodsOffer(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsOffer", paGoodstarget);
	}

	public int insertPaCopnAttriNoLimit(String goodsCode) throws Exception {
		return insert("pacommon.pacommon.insertPaCopnAttriNoLimit", goodsCode);
	}

	public int selectChkPaCustShipCostYn(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectChkPaCustShipCostYn", paGoodstarget);
	}
	
	public int selectChkPaTmonCustShipCostYn(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectChkPaTmonCustShipCostYn", paGoodstarget);
	}

	public int insertPaCustShipCost(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaCustShipCost", paGoodstarget);
	}
	
	public int insertPaTmonCustShipCost(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaTmonCustShipCost", paGoodstarget);
	}

	public int updateTransCnCostYn(PaGoodsTargetRec paGoodstarget) throws Exception {
		return update("pacommon.pacommon.updateTransCnCostYn", paGoodstarget);
	}

	public int updateTpaGoodsTarget(PaGoodsTargetRec paGoodstarget) throws Exception {
		return update("pacommon.pacommon.updateTpaGoodsTarget", paGoodstarget);
	}

	public int inertPaGoodsQaLog(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.inertPaGoodsQaLog", paGoodstarget);
	}

	public int deletePaGoodsTarget(PaGoodsTargetRec paGoodstarget) throws Exception {
		return delete("pacommon.pacommon.deletePaGoodsTarget", paGoodstarget);
	}

	public int selectChkPaGoodsYn(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectChkPaGoodsYn", goodsCode);
	}

	public int updatePaGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return update("pacommon.pacommon.updatePaGoods", paGoodstarget);
	}

	public int insertPaGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaGoods", paGoodstarget);
	}

	@SuppressWarnings("unchecked")
	public List<SpPaOfferInfo> selectOfferInfoList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectOfferInfoList", paramMap.get());
	}

	public int updatePaOfferData(SpPaOfferInfo paOfferData) throws Exception {
		return update("pacommon.pacommon.updatePaOfferData", paOfferData);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectOfferTypeCheck(SpPaOfferInfo paOfferData) throws Exception {
		return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectOfferTypeCheck", paOfferData);
	}

	public int updatePaOfferUseYn(SpPaOfferInfo paOfferData) throws Exception {
		return update("pacommon.pacommon.updatePaOfferUseYn", paOfferData);
	}

	public int insertPaofferData(SpPaOfferInfo paOfferData) throws Exception {
		return insert("pacommon.pacommon.insertPaofferData", paOfferData);
	}

	public int updatePaoffergoodsData(SpPaOfferInfo paOfferData) throws Exception {
		return update("pacommon.pacommon.updatePaoffergoodsData", paOfferData);
	}

	public int updatePa11stGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePa11stGoodsSaleGb", paramMap.get());
	}

	public int updatePaGoodsTargetAutoYnOff(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsTargetAutoYnOff", paramMap.get());
	}

	public int insertPaGoodsAutoYnOffLog(ParamMap paramMap) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsAutoYnOffLog", paramMap);
	}

	public int updatePaGmktGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaGmktGoodsSaleGb", paramMap.get());
	}

	public int updatePaNaverGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaNaverGoodsSaleGb", paramMap.get());
	}

	public int updatePaCopnGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaCopnGoodsSaleGb", paramMap.get());
	}

	public int updatePaWempGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaWempGoodsSaleGb", paramMap.get());
	}

	public int updatePaIntpGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaIntpGoodsSaleGb", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<SpPaGoodsInfo> selectGoodsInfoList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectGoodsInfoList", paramMap.get());
	}

	public int updatePaGoodsData(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsData", paGoodsData);
	}

	public int updatePa11stGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePa11stGoodsSync", paGoodsData);		
	}

	public int updatePaGmktGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaGmktGoodsSync", paGoodsData);
	}

	public int updatePaNaverGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaNaverGoodsSync", paGoodsData);
	}

	public int updatePaCopnGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaCopnGoodsSync", paGoodsData);
	}

	public int updatePaWempGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaWempGoodsSync", paGoodsData);
	}

	public int updatePaIntpGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaIntpGoodsSync", paGoodsData);
	}

	public int updatePaGoodsDtMappingTrans(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updatePaGoodsDtMappingTrans", paramMap.get());
	}
	
	public int updateNaverMappingTrans(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateNaverMappingTrans", paramMap.get());
	}

	public int insertPaEntpSlipChange(ParamMap paramMap) throws Exception {
		return insert("pacommon.pacommon.insertPaEntpSlipChange", paramMap.get());
	}

	public int updatePaGoodsSyncDate(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsSyncDate", paGoodsData);
	}

	@SuppressWarnings("unchecked")
	public List<SpPaGoodsDtInfo> selectGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectGoodsDtInfoList", paramMap.get());
	}

	public int updatePaGoodsDtData(SpPaGoodsDtInfo paGoodsDtInfo) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsDtData", paGoodsDtInfo);
	}
	
	public int insertGoodsDtMappingData(SpPaGoodsDtInfo paGoodsDtInfo) throws Exception {
		return insert("pacommon.pacommon.insertGoodsDtMappingData", paGoodsDtInfo);
	}
	
	public int insertPaGoodsDtData(SpPaGoodsDtInfo paGoodsDtInfo) throws Exception {
		return insert("pacommon.pacommon.insertPaGoodsDtData", paGoodsDtInfo);
	}
	
	public int updatePaGoodsDtMappingData(SpPaGoodsDtInfo paGoodsDtInfo) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsDtMappingData", paGoodsDtInfo);
	}
	
	public int update11stTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.update11stTrans", paramMap.get());
	}
	
	public int updateGmktTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateGmktTrans", paramMap.get());
	}
	
	public int updateNaverTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateNaverTrans", paramMap.get());
	}
	
	public int updateCopnTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateCopnTrans", paramMap.get());
	}
	
	public int updateWempTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateWempTrans", paramMap.get());
	}
	
	public int updateIntpTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateIntpTrans", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<SpPaGoodsDescInfo> selectPaGoodsDescInfoList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaGoodsDescInfoList", paramMap.get());
	}

	public int updatePaGoodsDescribe(SpPaGoodsDescInfo paGoodsDescInfo) throws Exception {
		return update("pacommon.pacommon.updatePaGoodsDescribe", paGoodsDescInfo);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectSaleEndDateTargetList() throws Exception {
		return (List<HashMap<String, String>>) list("pacommon.pacommon.selectSaleEndDateTargetList", null);
	}

	@SuppressWarnings("unchecked")
	public List<SpAuthTransInfo> selectGoodsAuthTransList() throws Exception {
		return list("pacommon.pacommon.selectGoodsAuthTransList", null);
	}

	public int update11stGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.update11stGoodsAuthTrans", authTransInfo);
	}

	public int updateGmktGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateGmktGoodsAuthTrans", authTransInfo);
	}

	public int updateNaverGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateNaverGoodsAuthTrans", authTransInfo);
	}

	public int updateCopnGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateCopnGoodsAuthTrans", authTransInfo);
	}

	public int updateWempGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateWempGoodsAuthTrans", authTransInfo);
	}

	public int updateIntpGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateIntpGoodsAuthTrans", authTransInfo);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectGoodsEventTransInfoList() throws Exception {
		return list("pacommon.pacommon.selectGoodsEventTransInfoList", null);
	}

	public int update11stGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.update11stGoodsEventTrans", paramMap.get());
	}

	public int updateGmktGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateGmktGoodsEventTrans", paramMap.get());
	}

	public int updateNaverGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateNaverGoodsEventTrans", paramMap.get());
	}

	public int updateCopnGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateCopnGoodsEventTrans", paramMap.get());
	}

	public int updateWempGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateWempGoodsEventTrans", paramMap.get());
	}

	public int updateIntpGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateIntpGoodsEventTrans", paramMap.get());
	}

	public int updateOfferTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateOfferTrans", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaPromoTarget> selectPaPromoTargetAllList() throws Exception {
		return list("pacommon.pacommon.selectPaPromoTargetAllList", null);
	}

	public int insertPaPromoTargetAll(PaPromoTarget paPromotargetAllData) throws Exception {
		return insert("pacommon.pacommon.insertPaPromoTargetAll", paPromotargetAllData);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsImage> selectCurImageInfoList(ParamMap paramMap) throws Exception{
		if("02".equals(paramMap.get("paGroupCode").toString())) {
			//이베이 딜 상품 이미지는 TGOODSINFOIMAGE (INFO_IMAGE_TYPE 200) 으로 관리
			return list("pacommon.pacommon.selectCurEbayImageInfoList", paramMap.get());
		}else {
			return list("pacommon.pacommon.selectCurImageInfoList", paramMap.get());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGoodsPriceVO> selectCurPriceInfoList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurPriceInfoList", paramMap.get());			
	}
	
	@SuppressWarnings("unchecked")
	public List<PaCustShipCostVO> selectCurShipCostInfoList(ParamMap paramMap) throws Exception{
		if("09".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectCurTmonShipCostInfoList", paramMap.get());	
		} else {
			return list("pacommon.pacommon.selectCurShipCostInfoList", paramMap.get());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurShipStopSaleList(ParamMap paramMap) throws Exception{
		if("11".equals(paramMap.get("paCode").toString()) || "12".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCur11stShipStopSaleList", paramMap.get());	
		} else if("21".equals(paramMap.get("paCode").toString()) || "22".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurGmktShipStopSaleList", paramMap.get());	
		} else if("41".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurNaverShipStopSaleList", paramMap.get());	
		} else if("51".equals(paramMap.get("paCode").toString()) || "52".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurCopnShipStopSaleList", paramMap.get());	
		} else if("61".equals(paramMap.get("paCode").toString()) || "62".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurWempShipStopSaleList", paramMap.get());	
		} else if("71".equals(paramMap.get("paCode").toString()) || "72".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurIntpShipStopSaleList", paramMap.get());			
		} else if("81".equals(paramMap.get("paCode").toString()) || "82".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurLtonShipStopSaleList", paramMap.get());			
		} else if("A1".equals(paramMap.get("paCode").toString()) || "A2".equals(paramMap.get("paCode").toString())) {
			return list("pacommon.pacommon.selectCurSsgShipStopSaleList", paramMap.get());			
		} else if("B1".equals(paramMap.get("paCode").toString()) || "B2".equals(paramMap.get("paCode").toString())) { 
			return list("pacommon.pacommon.selectCurKakaoShipStopSaleList", paramMap.get());
		} else {
			return list("pacommon.pacommon.selectCurTmonShipStopSaleList", paramMap.get());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectCurEntpSlipInfoList(ParamMap paramMap) throws Exception{
		if("06".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectCurWempEntpSlipInfoList", paramMap.get());
		} else {
			return list("pacommon.pacommon.selectCurEntpSlipInfoList", paramMap.get());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurSaleStopList(ParamMap paramMap) throws Exception{
		if("01".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.select11stCurSaleStopList", paramMap.get());	
		} else if("02".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectGmktCurSaleStopList", paramMap.get());	
		} else if("04".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectNaverCurSaleStopList", paramMap.get());	
		} else if("05".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectCopnCurSaleStopList", paramMap.get());	
		} else if("06".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectWempCurSaleStopList", paramMap.get());
		} else if("07".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectIntpCurSaleStopList", paramMap.get());			
		} else if("08".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectLtonCurSaleStopList", paramMap.get());			
		} else if("09".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectTmonCurSaleStopList", paramMap.get());			
		} else if("10".equals(paramMap.get("paGroupCode").toString())){
			return list("pacommon.pacommon.selectSsgCurSaleStopList", paramMap.get());			
		} else {
			return list("pacommon.pacommon.selectKakaoCurSaleStopList", paramMap.get());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurEventMarginList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurEventMarginList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurCheckDtCntList(ParamMap paramMap) throws Exception{
		if("08".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectCurCheckDtCntList", paramMap.get());			
		} else {
			return list("pacommon.pacommon.selectCurSsgCheckDtCntList", paramMap.get());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsVO> selectCurGoodsTransQtyList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurGoodsTransQtyList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaLtonGoodsdtMappingVO> selectCurGoodsDtTransQtyList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurGoodsDtTransQtyList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurStockCheckList(ParamMap paramMap) throws Exception{
		if("02".equals(paramMap.get("paGroupCode").toString())) {
			return list("pacommon.pacommon.selectGmktCurStockCheckList", paramMap.get());	
		} else {
			return list("pacommon.pacommon.selectNaverCurStockCheckList", paramMap.get());			
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurEpNameInfoList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurEpNameInfoList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurEntpSlipChangeInfoList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurEntpSlipChangeInfoList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurCnShipCostInfoList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurCnShipCostInfoList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurCnShipCostDtInfoList(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurCnShipCostDtInfoList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurCnShipCostTransSingle(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurCnShipCostTransSingle", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectCurCnShipCostTransMulti(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectCurCnShipCostTransMulti", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectMinMarginPrice(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("pacommon.pacommon.selectMinMarginPrice", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectApplyCnCostSeq(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("pacommon.pacommon.selectApplyCnCostSeq", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectMaxOrdCost(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("pacommon.pacommon.selectMaxOrdCost", paramMap.get());
	}
	
	public String selectCnCostSeq(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectCnCostSeq", paramMap.get());
	}
	
	public int updateTpaGoodsImage(PaGoodsImage curImageInfoTarget) throws Exception {
		return update("pacommon.pacommon.updateTpaGoodsImage", curImageInfoTarget);
	}
	
	public int insertTpaGoodsPrice(PaGoodsPriceVO curPriceInfoTarget) throws Exception{
		return insert("pacommon.pacommon.insertTpaGoodsPrice", curPriceInfoTarget);
	}
	
	public int updateTpaCustShipCost(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		return update("pacommon.pacommon.updateTpaCustShipCost", curShipCostInfoTarget);
	}
	
	public int insertTpaCustShipCost(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		return insert("pacommon.pacommon.insertTpaCustShipCost", curShipCostInfoTarget);
	}
	
	public int insertTpaTmonCustShipCost(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		return insert("pacommon.pacommon.insertTpaTmonCustShipCost", curShipCostInfoTarget);
	}
	
	public int updateTransCnCostYn2(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		return update("pacommon.pacommon.updateTransCnCostYn2", curShipCostInfoTarget);	
	}
	
	public int updateTpaEntpSlip(PaEntpSlip curEntpSlipInfoTarget) throws Exception {
		if("61".equals(curEntpSlipInfoTarget.getPaCode()) || "62".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateTpaWempEntpSlip", curEntpSlipInfoTarget);	
		} else {
			return update("pacommon.pacommon.updateTpaEntpSlip", curEntpSlipInfoTarget);			
		}
	}
	
	public int updateTransEntpSlip(PaEntpSlip curEntpSlipInfoTarget) throws Exception {
		if("11".equals(curEntpSlipInfoTarget.getPaCode()) || "12".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.update11stTransEntpSlip", curEntpSlipInfoTarget);	
		} else if("21".equals(curEntpSlipInfoTarget.getPaCode()) || "22".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateGmktTransEntpSlip", curEntpSlipInfoTarget);	
		} else if("41".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateNaverTransEntpSlip", curEntpSlipInfoTarget);	
		} else if("51".equals(curEntpSlipInfoTarget.getPaCode()) || "52".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateCopnTransEntpSlip", curEntpSlipInfoTarget);	
		} else if("61".equals(curEntpSlipInfoTarget.getPaCode()) || "62".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateWempTransEntpSlip", curEntpSlipInfoTarget);
		} else if("71".equals(curEntpSlipInfoTarget.getPaCode()) || "72".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateIntpTransEntpSlip", curEntpSlipInfoTarget);			
		} else if("81".equals(curEntpSlipInfoTarget.getPaCode()) || "82".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateLtonTransEntpSlip", curEntpSlipInfoTarget);			
		} else if("91".equals(curEntpSlipInfoTarget.getPaCode()) || "92".equals(curEntpSlipInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateTmonTransEntpSlip", curEntpSlipInfoTarget);			
		} else {
			return update("pacommon.pacommon.updateSsgTransEntpSlip", curEntpSlipInfoTarget);			
		}
	}	
	
	public int updateStopSale(ParamMap paramMap) throws Exception {
		if("01".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.update11stStopSale", paramMap.get());	
		} else if("02".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateEbayStopSale", paramMap.get());	
		} else if("04".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateNaverStopSale", paramMap.get());	
		} else if("05".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateCopnStopSale", paramMap.get());	
		} else if("06".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateWempStopSale", paramMap.get());
		} else if("07".equals(paramMap.get("paGroupCode").toString())){
			return update("pacommon.pacommon.updateIntpStopSale", paramMap.get());			
		} else if("08".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateLtonStopSale", paramMap.get());			
		} else if("09".equals(paramMap.get("paGroupCode").toString())) {
			return update("pacommon.pacommon.updateTmonStopSale", paramMap.get());			
		} else if("10".equals(paramMap.get("paGroupCode").toString())){
			return update("pacommon.pacommon.updateSsgStopSale", paramMap.get());			
		} else {
			return update("pacommon.pacommon.updateKakaoStopSale", paramMap.get());
		}
	}
	
	public int updateTpaLtonGoodsQty(PaLtonGoodsVO curGoodsTransQtyTarget) throws Exception {
		return update("pacommon.pacommon.updateTpaLtonGoodsQty", curGoodsTransQtyTarget);			
	}
	
	public int updateTpaLtonGoodsDtQty(PaLtonGoodsdtMappingVO curGoodsDtTransQtyTarget) throws Exception {
		return update("pacommon.pacommon.updateTpaLtonGoodsDtQty", curGoodsDtTransQtyTarget);			
	}
	
	public int insertPaEntpSlipChange2(ParamMap paramMap) throws Exception{
		return insert("pacommon.pacommon.insertPaEntpSlipChange2", paramMap.get());
	}
	
	public int insertPaCustCnShipCost(ParamMap paramMap) throws Exception{
		return insert("pacommon.pacommon.insertPaCustCnShipCost", paramMap.get());
	}
	
	public int updateCnTransYn(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateCnTransYn", paramMap.get());
	}
	
	public int updateCnCostYn(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateCnCostYn", paramMap.get());
	}
	
	public int updateCnCostTrans(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateCnCostTrans", paramMap.get());
	}
	
	public int updateCnt2CnCost(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateCnt2CnCost", paramMap.get());
	}
	
	public int updateCnt2Target(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateCnt2Target", paramMap.get());
	}
	
	public int insertSaleNoGoods(ParamMap paramMap) throws Exception{
		return insert("pacommon.pacommon.insertSaleNoGoods", paramMap.get());
	}
	
	public int updatePagoodstargetAutoYn(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePagoodstargetAutoYn", paramMap.get());
	}
	
	public int insertPagoodsAuthYnLog(ParamMap paramMap) throws Exception{
		return insert("pacommon.pacommon.insertPagoodsAuthYnLog", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectGoodsPaExceptList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectGoodsPaExceptList", paramMap.get());
	}

	public int update11stGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.update11stGoodsExceptTrans", paramMap.get());
	}

	public int updateGmktGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateGmktGoodsExceptTrans", paramMap.get());
	}

	public int updateNaverGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateNaverGoodsExceptTrans", paramMap.get());
	}

	public int updateCopnGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateCopnGoodsExceptTrans", paramMap.get());
	}

	public int updateWempGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateWempGoodsExceptTrans", paramMap.get());
	}

	public int updateIntpGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateIntpGoodsExceptTrans", paramMap.get());
	}
	
	/**
	 * TPALTONGOODSDTMAPPING INSERT
	 * 롯데ON은 단품 옵션 수정이 불가하므로 전용 테이블 생성했으며, 기존의 TPAGOODSDTMAPPING 은 불필요해 보임
	 * @param paGoodstarget
	 * @return
	 * @throws Exception
	 */
	public int insertPaLtonGoodsDtMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaLtonGoodsDtMapping", paGoodstarget);
	}

//	롯데ON 처리 추가 2021-05-13
	public int updateLtonGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateLtonGoodsAuthTrans", authTransInfo);
	}

	public int updateLtonGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateLtonGoodsExceptTrans", paramMap.get());
	}

	public int updateLtonGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateLtonGoodsEventTrans", paramMap.get());
	}

	public int updatePaLtonGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaLtonGoodsSaleGb", paramMap.get());
	}

	public int updateLtonTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateLtonTrans", paramMap.get());
	}

	public int updatePaLtonGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaLtonGoodsSync", paGoodsData);
	}

	public int insertPaLtonAddShipCost() throws Exception {
		return insert("pacommon.pacommon.insertPaLtonAddShipCost", null);
	}
	
	public int insertPaSsgShipCost() throws Exception {
		return insert("pacommon.pacommon.insertPaSsgShipCost", null);
	}

	/**
	 * 단품 정보 동기화 중 "I", "M" case의 롯데ON TPALTONGOODSDTMAPPING INSERT 처리
	 * @param paGoodsDtInfo
	 * @return
	 * @throws Exception
	 */
	public int insertPaLtonGoodsDtMappingData(SpPaGoodsDtInfo paGoodsDtInfo) throws Exception {
		return insert("pacommon.pacommon.insertPaLtonGoodsDtMappingData", paGoodsDtInfo);
	}

	public int selectPaLtonGoodsdtCnt(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaLtonGoodsdtCnt", goodsCode);
	}
	
	public int selectPaSsgGoodsdtCnt(String goodsCode) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaSsgGoodsdtCnt", goodsCode);
	}

	/**
	 * TPAGOODSDT <-> TPALTONGOODSDTMAPPING (GOODSDT_INFO)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SpPaGoodsDtInfo> selectPaLtonSsgGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaLtonSsgGoodsDtInfoList", paramMap.get());
	}

	public int updateLtonGoodsDtInfo(SpPaGoodsDtInfo paLtonGoodsDtInfo) throws Exception {
		return update("pacommon.pacommon.updateLtonGoodsDtInfo", paLtonGoodsDtInfo);
	}

	public int insertPaLtonGoodsDtMappingUdata(SpPaGoodsDtInfo paLtonGoodsDtInfo) throws Exception {
		return insert("pacommon.pacommon.insertPaLtonGoodsDtMappingUdata", paLtonGoodsDtInfo);
	}

	public int updateTargetAytoYn(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateTargetAytoYn", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectLtonDisplayCategoryList(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (List<HashMap<String, String>>) list("pacommon.pacommon.selectLtonDisplayCategoryList", paGoodstarget);
	}
	
	public double selectGetPromoMargin(ParamMap paramMap) {
		return (double) selectByPk("pacommon.pacommon.selectGetPromoMargin", paramMap.get());
	}

	public int insertPaTmonGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaTmonGoods", paGoodstarget);
	}
	
	public int insertPaSsgGoods(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaSsgGoods", paGoodstarget);
	}

	public String selectGetMaxPromoTarget(ParamMap paramMap) {
		return (String) selectByPk("pacommon.pacommon.selectGetMaxPromoTarget", paramMap.get());
	}	

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectLastPromoTransDate(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectLastPromoTransDate", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaPromoTargetOverMarginList(ParamMap paramMap) throws Exception {
		//goodsCode, paCode, transDate, paGroupCode, limitMarginAmt
		return list("pacommon.pacommon.selectPaPromoTargetOverMarginList", paramMap.get());
	}
	
	public int updateExceptReason(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateExceptReason", paramMap.get());
	}
	public int updateExceptReasonByGoodsCode(ParamMap paramMap) throws Exception{
		return update("pacommon.pacommon.updateExceptReasonByGoodsCode", paramMap.get());
	}
	
	// 티몬 동기화
	public int updateTmonGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateTmonGoodsAuthTrans", authTransInfo);
	}
	
	public int updateTmonGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateTmonGoodsExceptTrans", paramMap.get());
	}
	
	public int updateTmonGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateTmonGoodsEventTrans", paramMap.get());
	}
	
	public int updatePaTmonGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaTmonGoodsSaleGb", paramMap.get());
	}
	
	public int updatePaTmonGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaTmonGoodsSync", paGoodsData);
	}
	
	public int updateTmonTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateTmonTrans", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaTmonGoodsVO> selectTmonGoodsSync(ParamMap paramMap) throws Exception{
		return list("pacommon.pacommon.selectTmonGoodsSync", paramMap.get());
	}

	public int updateTmonGoodsSync(PaTmonGoodsVO paTmonGoodsVoTarget)  throws Exception {
		return update("pacommon.pacommon.updateTmonGoodsSync", paTmonGoodsVoTarget);
	}

	@SuppressWarnings("unchecked")
	public List<SpPaOfferInfo> selectOfferInfoListInsert(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectOfferInfoListInsert", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<SpPaOfferInfo> selectOfferInfoListUpdate(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectOfferInfoListUpdate", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectTmonCurCheckDtCntList(ParamMap paramMap) {
		return list("pacommon.pacommon.selectTmonCurCheckDtCntList", paramMap.get());
	}

	public int selectChkShipCost(String goodsCode) throws Exception{
		return (Integer) selectByPk("pacommon.pacommon.selectChkShipCost", goodsCode);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaCheckGoods(ParamMap paramMap) throws Exception{
		
		String paCode = paramMap.getString("paCode");
		
		if("11".equals(paCode) || "12".equals(paCode)) {
			return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectPa11stCheckGoods", paramMap.get());
		}else if("61".equals(paCode) || "62".equals(paCode)) {
			return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectPaWempCheckGoods", paramMap.get());
		}else {
			return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectPaCheckGoods", paramMap.get());
		}
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectTmonCurGoodsNameLengthCheckList(ParamMap paramMap) {
		return list("pacommon.pacommon.selectTmonCurGoodsNameLengthCheckList", paramMap.get());
	}

	public int selectPaGoodsNameLength(ParamMap paramMap) throws Exception{
		return (Integer) selectByPk("pacommon.pacommon.selectPaGoodsNameLength", paramMap.get());
	}

	public int selectCheckMassSourcingCode(String sourcingCode) {
		return (int) selectByPk("pacommon.pacommon.selectCheckMassSourcingCode", sourcingCode);
	}

	public int updateTransShipCost(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		
		if("11".equals(curShipCostInfoTarget.getPaCode()) || "12".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.update11stTransShipCost", curShipCostInfoTarget);	
		} else if("21".equals(curShipCostInfoTarget.getPaCode()) || "22".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateGmktTransShipCost", curShipCostInfoTarget);	
		} else if("41".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateNaverTransShipCost", curShipCostInfoTarget);	
		} else if("51".equals(curShipCostInfoTarget.getPaCode()) || "52".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateCopnTransShipCost", curShipCostInfoTarget);	
		} else if("61".equals(curShipCostInfoTarget.getPaCode()) || "62".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateWempTransShipCost", curShipCostInfoTarget);	
		} else if("71".equals(curShipCostInfoTarget.getPaCode()) || "72".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateIntpTransShipCost", curShipCostInfoTarget);			
		} else if("81".equals(curShipCostInfoTarget.getPaCode()) || "82".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateLtonTransShipCost", curShipCostInfoTarget);			
		} else if("A1".equals(curShipCostInfoTarget.getPaCode()) || "A2".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateSsgTransShipCost", curShipCostInfoTarget);			
		} else if("B1".equals(curShipCostInfoTarget.getPaCode()) || "B2".equals(curShipCostInfoTarget.getPaCode())) {
			return update("pacommon.pacommon.updateKakaoTransShipCost", curShipCostInfoTarget);			
		} else {
			return update("pacommon.pacommon.updateTmonTransShipCost", curShipCostInfoTarget);			
		}
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGoodsCodeByEntpCode(PaEntpSlip paEntpSlip) throws Exception {
		return list("pacommon.pacommon.selectGoodsCodeByEntpCode", paEntpSlip);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGoodsCodeByShipCostCode(PaCustShipCostVO paCustShipCostVO)  throws Exception {
		if("91".equals(paCustShipCostVO.getPaCode()) || "92".equals(paCustShipCostVO.getPaCode())) {
			return list("pacommon.pacommon.selectGoodsCodeByShipCostCode4Tmon", paCustShipCostVO);
		}else {
			return list("pacommon.pacommon.selectGoodsCodeByShipCostCode", paCustShipCostVO);
		}
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectGoodsInfoBySourcingCode(ParamMap paramMap) throws Exception {
		String paGroupCode = paramMap.getString("paGroupCode");
		
		switch (paGroupCode) {
		case "01":
			return  list("pacommon.pacommon.selectMassTargetYn11stBySourcingCode", paramMap.get());
		case "02":
			return  list("pacommon.pacommon.selectMassTargetYnGmktBySourcingCode", paramMap.get());
		case "05":
			return  list("pacommon.pacommon.selectMassTargetYnCopnBySourcingCode", paramMap.get());
		case "06":
		    return  list("pacommon.pacommon.selectMassTargetYnWempBySourcingCode", paramMap.get());
		case "07":
		    return  list("pacommon.pacommon.selectMassTargetYnIntpBySourcingCode", paramMap.get());		    
		case "08":
		    return  list("pacommon.pacommon.selectMassTargetYnLtonBySourcingCode", paramMap.get());		  		    
		default:
			break;
		}
		
		return new ArrayList<HashMap<String,Object>>();
	}

	public int updateMassTargetYnBySourcingCode( HashMap<String, Object> goods, String paGroupCode)  throws Exception {
		switch (paGroupCode) {
		case "01":
			return update("pacommon.pacommon.updateMassTargetYn11stBySourcingCode", goods);		
		case "02":
			update("pacommon.pacommon.updateMassTargetYnGMKTBySourcingCode1", goods);
			return update("pacommon.pacommon.updateMassTargetYnGMKTBySourcingCode2", goods);		
		case "05":
			return update("pacommon.pacommon.updateMassTargetYnCopnBySourcingCode", goods);	
		case "06":
			return update("pacommon.pacommon.updateMassTargetYnWempBySourcingCode", goods);		
		case "07":
			return update("pacommon.pacommon.updateMassTargetYnIntpBySourcingCode", goods);		
		case "08":
			return update("pacommon.pacommon.updateMassTargetYnLtonBySourcingCode", goods);
		default:
			break;
		}
		
		return 0;
	}
	
	public int execSP_PA_MASS_GOODS_UPDATE(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.execSP_PA_MASS_GOODS_UPDATE", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectDescData(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pacommon.pacommon.selectDescData", paramMap.get());
	}
	
	// 신세계 동기화
	public int updateSsgGoodsAuthTrans(SpAuthTransInfo authTransInfo) throws Exception {
		return update("pacommon.pacommon.updateSsgGoodsAuthTrans", authTransInfo);
	}
	
	public int updateSsgGoodsExceptTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateSsgGoodsExceptTrans", paramMap.get());
	}
	
	public int updateSsgGoodsEventTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateSsgGoodsEventTrans", paramMap.get());
	}
	
	public int updatePaSsgGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception {
		return update("pacommon.pacommon.updatePaSsgGoodsSync", paGoodsData);
	}
	
	public int updateSsgTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateSsgTrans", paramMap.get());
	}
	
	public int updatePaSsgGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaSsgGoodsSaleGb", paramMap.get());
	}

	/**
	 * 제휴사 상품 정보고시 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsOfferVO> selectPaGoodsOfferList(ParamMap paramMap) {
		return list("pacommon.pacommon.selectPaGoodsOfferList",paramMap.get());
	}

	/**
	 * 제휴사 단품 정보 조회
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) {
		return list("pacommon.pacommon.selectPaGoodsdtInfoList",paramMap.get());
	}
	
	//프로모션
	public void savePaPromoTargetHistory(ParamMap paramMap) {
		insert("pacommon.pacommon.savePaPromoTargetHistory", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaPromoGoodsPrice> selectPaPromoGoodsPriceList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaPromoGoodsPriceList", paramMap.get());
	}

	public int insertPaPromoGoodsPrice(PaPromoGoodsPrice paPromoGoodsPrice) throws Exception {
		return insert("pacommon.pacommon.insertPaPromoGoodsPrice", paPromoGoodsPrice);
	}

	/**
	 * 판매취소 상담생성 - 제휴 센터 담당자 조회
	 * @param ParamMap
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPaSoldOutordList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaSoldOutordList", paramMap.get());
	}
	
	public String selectPaRefIdForCounsel(String mediaCode) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPaRefIdForCounsel", mediaCode);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectCounselInfo(HashMap<String, String> reqMap) throws Exception {
		return (HashMap<String, String>) selectByPk("pacommon.pacommon.selectCounselInfo", reqMap);
	}
	
	public String selectReceiverSmsHp(HashMap<String, String> reqMap) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectReceiverSmsHp", reqMap);
	}
	
	/**
	 * SSG 단품 정보 저장 
	 * @param PaGoodsTargetRec
	 * @return 
	 */
	public int insertPaSsgGoodsDtMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaSsgGoodsDtMapping", paGoodstarget);
	}

	public int insertPaSsgGoodsDtMappingData(SpPaGoodsDtInfo paGoodsDtInfo) {
		return insert("pacommon.pacommon.insertPaSsgGoodsDtMappingData", paGoodsDtInfo);
	}

	public int updateSsgGoodsDtInfo(SpPaGoodsDtInfo paSsgGoodsDtInfo) throws Exception {
		return update("pacommon.pacommon.updateSsgGoodsDtInfo", paSsgGoodsDtInfo);
	}
	
	public int insertPaSsgGoodsDtMappingUdata(SpPaGoodsDtInfo paSsgGoodsDtInfo) {
		return insert("pacommon.pacommon.insertPaSsgGoodsDtMappingUdata", paSsgGoodsDtInfo);
	}

	/**
	 * SSG 정보고시 체크 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectPaSsgGoodsOfferCodeCheck(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaSsgGoodsOfferCodeCheck", paramMap.get());
	}

	/**
	 * SSG 정보고시 체크 - 동기화 
	 * @param ParamMap
	 * @return List<PaSsgGoodsVO> 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaSsgGoodsOfferList(ParamMap paramMap) throws Exception {
		return list("pacommon.pacommon.selectPaSsgGoodsOfferList", paramMap.get());
	}

	/**
	 * 22-01-12 [위메프 정보고시 Type 수정시 null update]
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaWempGoodsGroupNoticeNo(HashMap<String, String> paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaWempGoodsGroupNoticeNo", paramMap);
	}

	/**
	 * 동기화 - 가격변경여부 확인
	 * @param PaGoodsPriceVO
	 * @return String
	 * @throws Exception
	 */
	public String selectPriceChangeYn(PaGoodsPriceVO curPriceInfoTarget) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPriceChangeYn", curPriceInfoTarget);
	}
	
	public int updateKakaoTrans(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateKakaoTrans", paramMap.get());
	}
	public int updatePaKakaoGoodsSync(SpPaGoodsInfo paGoodsData) throws Exception{
		return update("pacommon.pacommon.updatePaKakaoGoodsSync", paGoodsData);
	}

	public String selectMakerMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectMakerMapping", paGoodstarget);
	}
	
	/**
	 * 동기화 - 공지사항 동기화 
	 * @param 
	 * @return List<PaNoticeApplyVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaNoticeApplyVO> selectPaNoticeTargetList() throws Exception {
		return list("pacommon.pacommon.selectPaNoticeTargetList", null);
	}
		
	/**
	 * 동기화 - PaNoticeApply INSERT
	 * @param PaNoticeApplyVO
	 * @return Integer
	 * @throws Exception
	 */
	public int insertPaNoticeApply(PaNoticeApplyVO paNoticeAllData) throws Exception{
		return insert("pacommon.pacommon.insertPaNoticeApply", paNoticeAllData);
	}
	
	/**
	 * 동기화 - PaNoticeApply DELETE
	 * @param PaNoticeApplyVO
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaNoticeApply(PaNoticeApplyVO paNoticeAllData) throws Exception{
		return delete("pacommon.pacommon.deletePaNoticeApply", paNoticeAllData);
	}	
	public int execSP_PA_GOODS_RESET(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.execSP_PA_GOODS_RESET", paramMap.get());
	}
	
	public int insertTPaRetentionGoods(ParamMap paramMap) throws Exception {
		return insert("pacommon.pacommon.insertTPaRetentionGoods", paramMap.get());
	}
	
	public int insertPaKakaoGoodsImage(PaGoodsTargetRec paGoodstarget) throws Exception {
		return insert("pacommon.pacommon.insertPaKakaoGoodsImage", paGoodstarget);
	}

	public int updatePaKakaoGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaKakaoGoodsSaleGb", paramMap.get());
	}

	public int deletePaKakaoGoodsImage(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.deletePaKakaoGoodsImage", paramMap.get());
	}
	
	public int insertPaKakaoGoodsImage(ParamMap paramMap) throws Exception {
		return insert("pacommon.pacommon.insertPaKakaoGoodsImage", paramMap.get());
	}
	
	public int updatePaSsgGoodsFoodSync(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updatePaSsgGoodsFoodSync", paramMap.get());
	}
	
	public int selectPromoMarginExceptYn(ParamMap paramMap) throws Exception{
		return (int) selectByPk("pacommon.pacommon.selectPromoMarginExceptYn", paramMap.get());
	}

	public String selectPaMinMarginRate(ParamMap minMarginMap) throws Exception {
		return (String) selectByPk("pacommon.pacommon.selectPaMinMarginRate", minMarginMap.get());
	}
	
	/**
	 * SSG 착불상품 체크 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectPaSsgGoodsCollectYnCheck(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.selectPaSsgGoodsCollectYnCheck", paramMap.get());
	}
	
	/**
	 * SSG 착불상품 체크 - 동기화 
	 * @param ParamMap
	 * @return List<PaSsgGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaSsgGoodsVO> selectPaSsgCollectGoodsList(ParamMap paramMap) {
		return list("pacommon.pacommon.selectPaSsgCollectGoodsList", paramMap.get());
	}
	
	/**
	 * 자동주문취소 - 주문DT조회
	 * @return List
	 * @throws Exception
	 */
	public List<?> selectOrderGoodsListDt(HashMap<String, String> map) throws Exception{
		return list("pacommon.pacommon.selectOrderGoodsListDt", map);
	}
	
	/**
	 * 판매불가처리등록 - 상품상세수정
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsdt(SaleNoGoods salenogoods) throws Exception{
		return update("pacommon.pacommon.updateSalesNoGoodsdt", salenogoods);

	}
	
	/**
	 * 판매불가처리등록 - 상품상세저장
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int insertSalenogoods(SaleNoGoods salenogoods) throws Exception{
		return insert("pacommon.pacommon.insertSalenogoods", salenogoods);

	}

	/**
	 * 판매불가처리등록 - 판매구분조회
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public String getSaleGb(String goods_code) throws Exception{
		return (String) selectByPk("pacommon.pacommon.getSaleGb", goods_code);
	}

	/**
	 * 판매불가처리등록 - 상품의 판매구분수정
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsSaleGb(ParamMap paramMap) throws Exception {
		return update("pacommon.pacommon.updateGoodsSaleGb", paramMap.get());
	}

	/**
	 * 판매불가처리등록 - 카테고리 상품수조회(미전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int getCgCnt(String goods_code) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.getCgCnt", goods_code);
	}

	/**
	 * 판매불가처리등록 - 판매불가처리내역조회(미전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectCgList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.pacommon.selectCgList", paramMap.get());
	}

	/**
	 * 판매불가처리등록 - 카테고리 전시상품수 수정(미전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updateCategory(String category_code) throws Exception {
		return update("pacommon.pacommon.updateCategory", category_code);
	}

	
	/**
	 * 판매불가처리등록 - 카테고리 전시수정(미전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateCategorygoods(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateCategorygoods", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - 프리젠테이션전시수정(미전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updatePresentation(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updatePresentation", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 상품수조회(미전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int getScgCnt(String goods_code) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.getScgCnt", goods_code);
	}

	/**
	 * 판매불가처리등록 - S판매불가처리내역조회(미전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectScgList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.pacommon.selectScgList", paramMap.get());
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 전시상품수 수정(미전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStcategory(String category_code) throws Exception {
		return update("pacommon.pacommon.updateStcategory", category_code);
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 전시수정(미전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStcategorygoods(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateStcategorygoods", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - S프리젠테이션전시수정(미전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStpresentation(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateStpresentation", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - 카테고리 상품수조회(전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int getCgCnt2(String goods_code) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.getCgCnt2", goods_code);
	}
	
	/**
	 * 판매불가처리등록 - 판매불가처리내역조회(전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectCgList2(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.pacommon.selectCgList2", paramMap.get());
	}
	
	/**
	 * 판매불가처리등록 - 카테고리 전시상품수 수정(전시로 수정할때) 
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int updateCategory2(String category_code) throws Exception {
		return update("pacommon.pacommon.updateCategory2", category_code);
	}
	
	/**
	 * 판매불가처리등록 - 카테고리 전시수정(전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateCategorygoods2(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateCategorygoods2", salenogoods);
	}

	/**
	 * 판매불가처리등록 - 프리젠테이션전시수정(전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updatePresentation2(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updatePresentation2", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 상품수조회(전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	public int getScgCnt2(String goods_code) throws Exception {
		return (Integer) selectByPk("pacommon.pacommon.getScgCnt2", goods_code);
	}
	
	/**
	 * 판매불가처리등록 - 판매불가처리내역조회(전시로 수정할때)
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectScgList2(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.pacommon.selectScgList2", paramMap.get());
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 전시상품수 수정(전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStcategory2(String category_code) throws Exception {
		return update("pacommon.pacommon.updateStcategory2", category_code);
	}
	
	/**
	 * 판매불가처리등록 - S카테고리 전시수정(전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStcategorygoods2(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateStcategorygoods2", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - S프리젠테이션전시수정(전시로 수정할때)
	 * 
	 * @param Salenogoods
	 * @return
	 * @throws Exception
	 */
	public int updateStpresentation2(SaleNoGoods salenogoods) throws Exception {
		return update("pacommon.pacommon.updateStpresentation2", salenogoods);
	}
	
	/**
	 * 판매불가처리등록 - 반영할 sale_gb조회
	 * 
	 * @param ParamMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getSetgoods(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacommon.pacommon.getSetgoods", paramMap.get());
	}

	/**
	 * 배송경과일 취소 히스토리 저장
	 * 
	 * @param Mobileordercancelhis
	 * @return
	 * @throws Exception
	 */
    public int insertMobileOrderCancelHis(Mobileordercancelhis mobileordercancelhis) throws Exception {
    	return insert("pacommon.pacommon.insertMobileOrderCancelHis", mobileordercancelhis);
    }
    
	public int execSP_PAGOODS_RETENTION(HashMap<String, Object> resultMap) {
		return update("pacommon.pacommon.execSP_PAGOODS_RETENTION", resultMap);
	}
}