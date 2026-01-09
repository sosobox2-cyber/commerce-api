package com.cware.netshopping.pagmkt.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaEsmGoodsKinds;
import com.cware.netshopping.domain.model.PaGmktOrigin;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGmktSettlement;
import com.cware.netshopping.domain.model.PaGmktShipCostM;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaSiteGoodsKinds;

public interface PaGmktCommonService {

	/**
	 * G마켓 전체카테고리저장(ESM)
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktGoodsKindsEsmTx(List<PaEsmGoodsKinds> paEsmGoodsKindsList) throws Exception;
	
	/**
	 * G마켓 전체카테고리저장(SITE)
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktGoodsKindsSiteTx(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 전체 제조사 정보 저장
	 * @param List<PaGmktMaker>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktMakerListTx(List<PaMaker> paMakerList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 전체 브랜드 정보 저장
	 * @param List<PaBrand>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktBrandListTx(List<PaBrand> paBrandList) throws Exception;
	
	/**	 * 제휴 G마켓 - 판매자주소 배송지, 건물관리번호 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception;
	/**	 * 제휴 G마켓 - 판매자주소 배송지, 건물관리번호 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipModifyList(PaEntpSlip paEntpSlip) throws Exception;
	
	public String savePaGmktShipCostMTx(PaEntpSlip paEntpSlip) throws Exception;
	public String savePaGmktShipCostDtTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**	 * 제휴 G마켓 - 출하지 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipCostInsertList(PaEntpSlip paEntpSlip) throws Exception;
	/**	 * 제휴 G마켓 - 출하지 등록 update	 */
	public String savePaGmktShipCostMInsertTx(PaGmktShipCostM paGmktShipCostM) throws Exception;
	
	/**	 * 제휴 G마켓 - 출하지 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipCostModifyList(PaEntpSlip paEntpSlip) throws Exception;
	/**	 * 제휴 G마켓 - 출하지 수정 update	 */
	public String savePaGmktShipCostMUpdateTx(PaGmktShipCostM paGmktShipCostM) throws Exception;
	
	/**	 * 제휴 G마켓 - 묶음배송 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipPoliciesInsertList(String gmktShipNo) throws Exception;
	/**	 * 제휴 G마켓 - 묶음배송 등록 update	 */
	public String updateEntpShipPoliciesInsertTx(HashMap<String,Object> paramMap) throws Exception;
	
	/**	 * 제휴 G마켓 - 묶음배송 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipPoliciesModifyList(String gmktShipNo) throws Exception;
	/**	 * 제휴 G마켓 - 묶음배송 수정 update	 */
	public String updateEntpShipPoliciesModifyTx(HashMap<String,Object> paramMap) throws Exception;
	
	/** G마켓 발송정책 조회 */
	public String savePaGmktPolicyTx(List<PaGmktPolicy> policies) throws Exception;
	
	/** before select goods insert 상품코드기준= 판매자주소-출고지-묶음배송 등록 */
	public HashMap<String,String> selectBeforeInsertGoodsBaseInfo(String goodsCode) throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntp(String goodsCode) throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsEntpModify(ParamMap paramMap) throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsShip() throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsShipModify() throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundle() throws Exception;
	public List<HashMap<String,String>> selectBeforeInsertGoodsBundleModify() throws Exception;
	
	
	
	/**
	 * 제휴 G마켓 - 판매자주소 배송지 등록
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 제휴 G마켓 - 판매자주소 배송지 수정
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktEntpSlipUpdateTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 제휴 G마켓 - ESM 카테고리 LMSDN 코드 조회
	 * @param 
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> selectLmsdnCodeList() throws Exception;
	
	/**
	 * G마켓 전체카테고리저장(ESM-SITE 매칭)
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktGoodsKindsMatchingTx(List<PaSiteGoodsKinds> paSiteGoodsKindsList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 전체 원산지 정보 저장
	 * @param List<PaGmktOrigin>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktOriginListTx(List<PaGmktOrigin> paGmktOriginList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 전체 정보고시 정보 저장
	 * @param List<PaOfferCode>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktOfferCodeListTx(List<PaOfferCode> paOfferCodeList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 정산 판매대금 정보 저장
	 * @param List<PaOfferCode>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktSettleOrderTx(List<PaGmktSettlement> paGmktSettlementList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 정산 배송비 정보 저장
	 * @param List<PaOfferCode>
	 * @return String
	 * @throws Exception
	 */
	public String savePaGmktSettleDeliveryTx(List<PaGmktSettlement> paGmktSettlementList) throws Exception;
	
	/**
	 * 제휴 G마켓 - 정산 삭제
	 * @param List<PaOfferCode>
	 * @return String
	 * @throws Exception
	 */
	public String deletePaGmktSettleTx(ParamMap paramMap) throws Exception;
	
	/**
	 * 제휴 이베이 - 출하지 미사용 처리
	 * @param paEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String updatePaGmktShipCostMUseYnTx(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 제휴 이베이 - 500에러 출고지 TPAGMKTSHIPCOSTM select
	 * @param paGmktShipCostM
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectPaGmktShipCostMFor500(PaGmktShipCostM paGmktShipCostM) throws Exception;

	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 TPAGMKTSHIPCOSTM TRANS_ERROR_YN 0처리
	 * @param paGmktShipCostM
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktShipCostMErrorYn(PaGmktShipCostM paGmktShipCostM) throws Exception;

	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 상품 재연동 리스트 조회
	 * @param paEntpSlip
	 * @return List<HashMap<String, String>>
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPaGmtkGoodsTargetList(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 해당 출하지에 속한 상품 TARGET ON
	 * @param goodsCode
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmtkGoodsTargetOn(String goodsCode)throws Exception;
	
	/**	 * 이베이(공통) - TPAGMKTSHIPCOSTDT 생성 **/
	public int saveTPaGmktShipCostdtTx(HashMap<String, String> custshipcost) throws Exception;
	/**	 * 이베이(공통) - 출하지 번호 저장 **/
	public int saveGmktShipNoTx(HashMap<String, String> templetMap) throws Exception;
	/**	 * 이베이(공통) - 묶음배송비 정책 번호	 **/
	public int savePaShipCostTx(Map<String, String> requestMap) throws Exception;
	/**	 * 이베이(공통) - 신규 판매자 주소 리스트 조회 **/
	public List<HashMap<String, String>> selectNewGoodsEntpTarget(ParamMap paramMap) throws Exception;
	/**	 * 이베이(공통) - 판매자 주소 수정 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipCost(ParamMap paramMap) throws Exception;
	public HashMap<String, String> selectEntpSlipCostByEntpCodeNSeq(HashMap<String, String> entpInfo) throws Exception;
	/**	 * 이베이(공통) - 신규 출하지 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipShip4Insert(ParamMap paramMap) throws Exception;
	/**	 * 이베이(공통) - 출하지 수정 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipShip4Modify(ParamMap paramMap) throws Exception;
	/**	 * 이베이(공통) - 배송비 등록/수정 리스트 조회 **/
	public List<HashMap<String, String>> selectPaGmktShipCostTargetList(ParamMap paramMap) throws Exception;
	/**	 * 이베이(공통) - 출하지 연동 대상 제주/도서 최대 배송비 조회 **/
	public HashMap<String, String> selectMaxShipCostFee(HashMap<String, String> templetMap) throws Exception;
	/**	 * 이베이(공통) - 배송비 등록/수정 DT 리스트 조회 **/
	public List<HashMap<String, String>> selectPaGmktShipCostDtTargetList(ParamMap paramMap) throws Exception;

	public String selectTConfigVal(String string) throws Exception;

}
