package com.cware.netshopping.pagmkt.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaEsmGoodsKinds;
import com.cware.netshopping.domain.model.PaGmktOrigin;
import com.cware.netshopping.domain.model.PaGmktPolicy;
import com.cware.netshopping.domain.model.PaGmktSettlement;
import com.cware.netshopping.domain.model.PaGmktShipCostDt;
import com.cware.netshopping.domain.model.PaGmktShipCostM;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaSiteGoodsKinds;

@Service("pagmkt.common.paGmktCommonDAO")
@SuppressWarnings("unchecked")
public class PaGmktCommonDAO extends AbstractPaDAO {
	
	//TODO 시작 2018.10.23 THJEON
	/**	 * G마켓 - 판매자주소 배송지, 건물관리번호 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipInsertList", paEntpSlip);
	}
	// 업체주소 키로 조회하므로 단건 반환 (이베이연동구조개선)
	public Map<String, String> selectEntpShipInsert(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, String>) selectByPk("pagmkt.common.selectEntpShipInsertList", paEntpSlip);
	}
	/**	 * G마켓 - 판매자주소 배송지, 건물관리번호 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipModifyList", paEntpSlip);
	}
	// 업체주소 키로 조회하므로 단건 반환 (이베이연동구조개선)
	public Map<String, String> selectEntpShipModify(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, String>) selectByPk("pagmkt.common.selectEntpShipModifyList", paEntpSlip);
	}
	
	/**	 * G마켓 - 판매자주소 배송지, 건물관리번호 등록 후 출고지(pagmktshipcostm) 조회	- STEP1 */
	public List<PaGmktShipCostM> selectPaGmktShipCostM(PaEntpSlip paEntpSlip) throws Exception {
	    return (List<PaGmktShipCostM>) list("pagmkt.common.selectPaGmktShipCostM", paEntpSlip);
	}
	/**  * G마켓 - 판매자주소 배송지, 건물관리번호 등록 후 출고지(pagmktshipcostm) 추가 - STEP1*/
	public int insertPaGmktShipCostM(PaGmktShipCostM paGmktShipCostM) throws Exception{
		return insert("pagmkt.common.insertPaGmktShipCostM", paGmktShipCostM);
	}
	/**  * G마켓 - 판매자주소 배송지, 건물관리번호 등록 후 출고지(pagmktshipcostm) max가져오기 - STEP2*/
	public HashMap<String,String> selectMaxPaGmktShipCostM(PaEntpSlip paEntpSlip) throws Exception {
	    return (HashMap<String,String>) selectByPk("pagmkt.common.selectMaxPaGmktShipCostM", paEntpSlip);
	}
	/**  * G마켓 - 판매자주소 등록후 gmktshipcostm update max selected check , 출고지 동일 가격 개선 쿼리, NULL체크 필수 STEP 2-1  */
	public List<HashMap<String, String>> selectMaxTransEndDateCompare(HashMap<String,String> paramMap) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectMaxTransEndDateCompare", paramMap);
	}
	/**  * G마켓 - 판매자주소 배송지, 건물관리번호 등록 후 출고지(pagmktshipcostm) max값 update - STEP3*/
	public int updateMaxPaGmktShipCostM(HashMap<String,String> paramMap) throws Exception {
	    return update("pagmkt.common.updateMaxPaGmktShipCostM", paramMap);
	}
	/**  * G마켓 - 판매자주소 배송지, 건물관리번호 등록 후 출고지(pagmktshipcostm) 해당업체 플래그 down - STEP4*/
	public int updateNotMaxPaGmktShipCostM(HashMap<String,String> paramMap) throws Exception {
	    return update("pagmkt.common.updateNotMaxPaGmktShipCostM", paramMap);
	}
	
	
	/**	 * G마켓 - 출하지 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipCostInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipCostInsertList", paEntpSlip);
	}
	/** 출하지 등록 update-1*/
	public Integer updatePagmktshipcostmForInsert(PaGmktShipCostM paGmktShipCostM) throws Exception {
	    return update("pagmkt.common.updatePagmktshipcostmForInsert", paGmktShipCostM);
	}
	/** 출하지 등록 update-2*/
	public Integer updatePaentpslipForInsert(PaGmktShipCostM paGmktShipCostM) throws Exception {
	    return update("pagmkt.common.updatePaentpslipForInsert", paGmktShipCostM);
	}
	/** 출하지 등록 후 묶음배송(pagmktshipcostdt) 검색(insert/update대상)*/
	public List<PaGmktShipCostDt> selectPaGmktShipCostDt(PaEntpSlip paEntpSlip) throws Exception{
		return (List<PaGmktShipCostDt>) list("pagmkt.common.selectPaGmktShipCostDt", paEntpSlip);
	}
	/** 출하지 등록 후 묶음배송(pagmktshipcostdt) chkSelect*/
	public int selectChkShipDt(PaGmktShipCostDt paGmktShipCostDt) throws Exception{
		return (int)selectByPk("pagmkt.common.selectChkShipDt", paGmktShipCostDt);
	}
	/** 출하지 등록 후 묶음배송(pagmktshipcostdt) update*/
	public int updatePaGmktShipCostDt(PaGmktShipCostDt paGmktShipCostDt) throws Exception{
		return update("pagmkt.common.updatePaGmktShipCostDt", paGmktShipCostDt);
	}
	/** 출하지 등록 후 묶음배송(pagmktshipcostdt) insert*/
	public int insertPaGmktShipCostDt(PaGmktShipCostDt paGmktShipCostDt) throws Exception{
		return insert("pagmkt.common.insertPaGmktShipCostDt", paGmktShipCostDt);
	}
	/**  * G마켓 - 출하지 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipCostModifyList(PaEntpSlip paEntpSlip) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipCostModifyList", paEntpSlip);
	}
	/** 출하지 수정 update*/
	public Integer updatePagmktshipcostmForUpdate(PaGmktShipCostM paGmktShipCostM) throws Exception {
	    return update("pagmkt.common.updatePagmktshipcostmForUpdate", paGmktShipCostM);
	}
	/**	 * G마켓 - 묶음배송 등록 조회	 */
	public List<HashMap<String, String>> selectEntpShipPoliciesInsertList(String gmktShipNo) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipPoliciesInsertList", gmktShipNo);
	}
	/**	 * G마켓 - 묶음배송 수정 조회	 */
	public List<HashMap<String, String>> selectEntpShipPoliciesModifyList(String gmktShipNo) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpShipPoliciesModifyList", gmktShipNo);
	}
	
	/** 발송정책 제거 */
	public int deletePaGmktPolicy(PaGmktPolicy policy) throws Exception{
		return delete("pagmkt.common.deletePaGmktPolicy", policy);
	}
	/** 발송정책 추가*/
	public int insertPaGmktPolicy(PaGmktPolicy policy) throws Exception{
		return insert("pagmkt.common.insertPaGmktPolicy", policy);
	}
	/** 묶음배송 등록 update*/
	public Integer updateEntpShipPoliciesInsert(HashMap<String,Object> paramMap) throws Exception {
	    return update("pagmkt.common.updateEntpShipPoliciesInsert", paramMap);
	}
	/** 묶음배송 수정 update*/
	public Integer updateEntpShipPoliciesModify(HashMap<String,Object> paramMap) throws Exception {
	    return update("pagmkt.common.updateEntpShipPoliciesModify", paramMap);
	}
	/**	 * G마켓 - before-insert 이전 기초업체정보 조회	 */
	public HashMap<String, String> selectBeforeInsertGoodsBaseInfo(String goodsCode) throws Exception {
	    return (HashMap<String, String>) selectByPk("pagmkt.common.selectBeforeInsertGoodsBaseInfo", goodsCode);
	}
	/**	 * G마켓 - 판매자주소 등록 전 업체 select by goodsCode	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsEntp(String goodsCode) throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectBeforeInsertGoodsEntp", goodsCode);
	}
	/**	 * G마켓 - 판매자주소 수정	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsEntpModify(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectBeforeInsertGoodsEntpModify", paramMap.get());
	}
	/**	 * G마켓 - 묶음배송 등록 전 출고지리스트 select	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsShip() throws Exception {
	    return (List<HashMap<String,String>>) list("pagmkt.common.selectBeforeInsertGoodsShip", null);
	}
	/**	 * G마켓 - 묶음배송 수정 전 출고지리스트 select	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsShipModify() throws Exception {
	    return (List<HashMap<String,String>>) list("pagmkt.common.selectBeforeInsertGoodsShipModify", null);
	}
	/**	 * G마켓 - 묶음배송 등록을 위한 출고지리스트 select	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsBundle() throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectBeforeInsertGoodsBundle", null);
	}
	/**	 * G마켓 - 묶음배송 수정을 위한 출고지리스트 select	 */
	public List<HashMap<String, String>> selectBeforeInsertGoodsBundleModify() throws Exception {
	    return (List<HashMap<String, String>>) list("pagmkt.common.selectBeforeInsertGoodsBundleModify", null);
	}
	
	//TODO 끝 2018.10.23 THJEON
	
	/**
	 * G마켓 - 판매자주소 배송지 등록
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */        
	public int insertPaGmktEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("pagmkt.common.insertPaGmktEntpSlip", paEntpSlip);
	}
	
	/**
	 * G마켓 - 판매자주소 배송지 수정
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("pagmkt.common.updatePaGmktEntpSlip", paEntpSlip);
	}
	
	/**
	 * ESM 카테고리 저장
	 * @param PaEsmGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaEsmGoodsKindsList(PaEsmGoodsKinds paEsmGoodsKinds) throws Exception{
		return insert("pagmkt.common.insertPaEsmGoodsKindsList", paEsmGoodsKinds);
	}
	
	/**
	 * G마켓 - ESM 카테고리 그룹명 수정
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int updatePaEsmGoodsKindsListGroupName() throws Exception{
		return update("pagmkt.common.updatePaEsmGoodsKindsListGroupName", null);
	}
	
	/**
	 * SITE 카테고리 저장
	 * @param PaSiteGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaSiteGoodsKindsList(PaSiteGoodsKinds paSiteGoodsKinds) throws Exception{
		return insert("pagmkt.common.insertPaSiteGoodsKindsList", paSiteGoodsKinds);
	}
	
	/**
	 * 제휴 G마켓 - ESM 카테고리 LMSDN 코드 조회
	 * @param paramMap
	 * @return List<Brand>
	 * @throws Exception
	 */
	public List<String> selectLmsdnCodeList() throws Exception{
	    return list("pagmkt.common.selectLmsdnCodeList", null);
	}
	
	/**
	 * 제휴 G마켓 - ESM 카테고리 삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaEsmGoodsKindsList() throws Exception{
		return delete("pagmkt.common.deletePaEsmGoodsKindsList", null);
	}
	
	/**
	 * 제휴 G마켓 - ESM-SITE 매칭
	 * @param PaSiteGoodsKinds
	 * @return int
	 * @throws Exception
	 */
	public int insertPaMatchingGoodsKindsList(PaSiteGoodsKinds paSiteGoodsKinds) throws Exception{
		return insert("pagmkt.common.insertPaMatchingGoodsKindsList", paSiteGoodsKinds);
	}
	
	/**
	 * 제휴 G마켓 - ESM-SITE 카테고리 매칭 삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaMatchingGoodsKindsList() throws Exception{
		return delete("pagmkt.common.deletePaMatchingGoodsKindsList", null);
	}
	
	/**
	 * 제휴 G마켓 - 브랜드 저장
	 * @param PaBrand
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktBrandList(PaBrand paBrand) throws Exception{
		return insert("pagmkt.common.insertPaGmktBrandList", paBrand);
	}
	
	/**
	 * 제휴 G마켓 - 제조사 저장
	 * @param PaMaker
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktMakerList(PaMaker paMaker) throws Exception{
		return insert("pagmkt.common.insertPaGmktMakerList", paMaker);
	}
	
	/**
	 * 제휴 G마켓 - 원산지 삭제
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int deletePaGmktOriginList() throws Exception{
		return insert("pagmkt.common.deletePaGmktOriginList", null);
	}
	
	/**
	 * 제휴 G마켓 - 원산지 저장
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktOriginList(PaGmktOrigin paGmktOrigin) throws Exception{
		return insert("pagmkt.common.insertPaGmktOriginList", paGmktOrigin);
	}
	
	/**
	 * 제휴 G마켓 - 정보고시 삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaGmktOfferCodeList(String paGroupCode) throws Exception{
		return delete("pagmkt.common.deletePaGmktOfferCodeList", paGroupCode);
	}
	
	/**
	 * 제휴 G마켓 - 정보고시 저장
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktOfferCodeList(PaOfferCode paOfferCode) throws Exception{
		return insert("pagmkt.common.insertPaGmktOfferCodeList", paOfferCode);
	}
	
	/**
	 * 제휴 G마켓 - SITE 카테고리 체크
	 * @param PaSiteGoodsKinds
	 * @return int
	 * @throws Exception
	 */
	public int selectChkPaSitGoodsKinds(PaSiteGoodsKinds paSiteGoodsKinds) throws Exception{
		return (Integer) selectByPk("pagmkt.common.selectChkPaSitGoodsKinds", paSiteGoodsKinds);
	}
	
	/**
	 * 제휴 G마켓 - 정산 판매대금 저장
	 * @param PaGmktSettlement
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktSettleOrder(PaGmktSettlement paGmktSettlement) throws Exception{
		return insert("pagmkt.common.insertPaGmktSettleOrder", paGmktSettlement);
	}
	
	/**
	 * 제휴 G마켓 - 정산 배송비 저장
	 * @param PaGmktSettlement
	 * @return int
	 * @throws Exception
	 */
	public int insertPaGmktSettleDelivery(PaGmktSettlement paGmktSettlement) throws Exception{
		return insert("pagmkt.common.insertPaGmktSettleDelivery", paGmktSettlement);
	}
	
	/**
	 * 제휴 G마켓 - 정산 삭제
	 * @param PaGmktSettlement
	 * @return Integer
	 * @throws Exception
	 */
	public Integer deletePaGmktSettlementList(ParamMap paramMap) throws Exception{
		return delete("pagmkt.common.deletePaGmktSettlementList", paramMap.get());
	}
	
	/**
	 * 제휴 G마켓 - 정산 체크
	 * @param PaGmktSettlement
	 * @return Int
	 * @throws Exception
	 */
	public int selectChkGmktSettlement(PaGmktSettlement paGmktSettlement) throws Exception{
		return (int)selectByPk("pagmkt.common.selectChkGmktSettlement", paGmktSettlement);
	}
	
	/**
	 * 제휴 G마켓 - 정산(배송비) 체크
	 * @param PaGmktSettlement
	 * @return Int
	 * @throws Exception
	 */
	public int selectChkGmktShipSettlement(PaGmktSettlement paGmktSettlement) throws Exception{
		return (int)selectByPk("pagmkt.common.selectChkGmktShipSettlement", paGmktSettlement);
	}
	
	/**
	 * 제휴 이베이 - 출하지 미사용 처리
	 * @param paEntpSlip
	 * @return Int
	 * @throws Exception
	 */
	public int updatePaGmktShipCostMUseYn(PaEntpSlip paEntpSlip)  throws Exception{
		return update("pagmkt.common.updatePaGmktShipCostMUseYn", paEntpSlip);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록후 신규 gmktShipNo select
	 * @param paEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String selectPaentpslipGmktShipNo(PaEntpSlip paEntpSlip) throws Exception{
		return (String) selectByPk("pagmkt.common.selectPaentpslipGmktShipNo", paEntpSlip);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 TPAGMKTSHIPCOSTM select
	 * @param paGmktShipCostM
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String,String> selectPaGmktShipCostMFor500(PaGmktShipCostM paGmktShipCostM) throws Exception {
	    return (HashMap<String,String>) selectByPk("pagmkt.common.selectPaGmktShipCostMFor500", paGmktShipCostM);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록후 gmktshipcostdt gmktShipNo update
	 * @param paEntpSlip
	 * @return Int
	 * @throws Exception
	 */
	public int updatePaGmktShipCostDtGmktShipNo(PaEntpSlip paEntpSlip) throws Exception{
		return update("pagmkt.common.updatePaGmktShipCostDtGmktShipNo", paEntpSlip);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록후 TPAENTPSLIP gmktShipNo update
	 * @param paEntpSlip
	 * @return Int
	 * @throws Exception
	 */
	
	public int updatePaentpslipForInsert500(PaGmktShipCostM paGmktShipCostM) throws Exception {
		 return update("pagmkt.common.updatePaentpslipForInsert500", paGmktShipCostM);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 TPAGMKTSHIPCOSTM TRANS_ERROR_YN 0처리
	 * @param paGmktShipCostM
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktShipCostMErrorYn(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return update("pagmkt.common.updatePaGmktShipCostMErrorYn", paGmktShipCostM);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후  gmktshipcostdt TRANS_TARGET_YN 1처리
	 * @param paGmktShipCostDt
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmktShipCostDtFor500(PaGmktShipCostDt paGmktShipCostDt) throws Exception {
		return update("pagmkt.common.updatePaGmktShipCostDtFor500", paGmktShipCostDt);
	}
	
	/**
	 * 500에러 출고지 재 등록 후  gmktshipcostm GMKT_SHIP_NO update
	 * @param paGmktShipCostM
	 * @return int
	 * @throws Exception
	 */
	public int updatePagmktshipcostmForInsert500(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return update("pagmkt.common.updatePagmktshipcostmForInsert500", paGmktShipCostM);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 상품 재연동 리스트 조회
	 * @param paEntpSlip
	 * @return List<HashMap<String, String>>
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPaGmtkGoodsTargetList(PaEntpSlip paEntpSlip) throws Exception {
		return (List<HashMap<String,String>>) list("pagmkt.common.selectPaGmtkGoodsTargetList", paEntpSlip);
	}
	
	/**
	 * 제휴 이베이 - 500에러 출고지 재 등록 후 해당 출하지에 속한 상품 TARGET ON
	 * @param goodsCode
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGmtkGoodsTargetOn(String goodsCode) throws Exception {
		return update("pagmkt.common.updatePaGmtkGoodsTargetOn", goodsCode);
	}
	
	
	/** 출하지 등록 후 묶음배송(pagmktshipcostdt) update*/
	public int updateTPaGmktShipCostDt(PaGmktShipCostDt paGmktShipCostDt) throws Exception{
		return update("pagmkt.common.updateTPaGmktShipCostDt", paGmktShipCostDt);
	}
	/**	 * 이베이(공통) - 출하지 번호 조회  */
	public String selectGmktShipNo(PaGmktShipCostDt pagmktShipCost) throws Exception {
		return (String) selectByPk("pagmkt.common.selectGmktShipNo", pagmktShipCost);
	}
	/**	 * 이베이(공통) - 출하지 번호 저장(업체)  */
	public int updatePaEntpSlipGmktShipNo(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return update("pagmkt.common.updatePaEntpSlipGmktShipNo", paGmktShipCostM);
	}
	/**	 * 이베이(공통) - 출하지 번호 저장(배송) */
	public int updatePaGmktShipCostGmktShipNo(PaGmktShipCostM paGmktShipCostM) throws Exception {
		return update("pagmkt.common.updatePaGmktShipCostGmktShipNo", paGmktShipCostM);
	}
	/**	 * 이베이(공통) - 묶음배송비 정책 번호	 저장 */
	public int updatePaGmktShipCostGmktBundleNo(ParamMap paramMap) throws Exception {
		return update("pagmkt.common.updatePaGmktShipCostGmktBundleNo", paramMap.get());
	}
	/**	 * 이베이(공통) - 공통 배송비 테이블 타겟 OFF*/
	public int updatePaCustShipCost(ParamMap paramMap) throws Exception {
		return update("pagmkt.common.updatePaCustShipCost", paramMap.get());
	}
	/**	 * 이베이(공통) - 공통 배송비 테이블 타겟 OFF*/
	public List<HashMap<String, String>> selectNewGoodsEntpTarget(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectNewGoodsEntpTarget", paramMap.get());
	}
	/**	 * 이베이(공통) - 신규 판매자 주소 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipCost(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpSlipCost", paramMap.get());
	}
	public HashMap<String, String> selectEntpSlipCostByEntpCodeNSeq(ParamMap paramMap) throws Exception {
		return (HashMap<String,String>) selectByPk("pagmkt.common.selectEntpSlipCostByEntpCodeNSeq", paramMap.get());
	}
	/**	 * 이베이(공통) - 신규 출하지 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipShip4Insert(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpSlipShip4Insert", paramMap.get());
	}
	/**	 * 이베이(공통) - 출하지 수정 리스트 조회 **/
	public List<HashMap<String, String>> selectEntpSlipShip4Modify(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectEntpSlipShip4Modify", paramMap.get());
	}
	/**	 * 이베이(공통) - 배송비 등록/수정 리스트 조회 **/
	public List<HashMap<String, String>> selectPaGmktShipCostTargetList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectPaGmktShipCostTargetList", paramMap.get());
	}
	
	public HashMap<String, String> selectMaxShipCostFee(ParamMap paramMap) throws Exception {
		return (HashMap<String,String>) selectByPk("pagmkt.common.selectMaxShipCostFee", paramMap.get());
	}
	/**	 * 이베이(공통) - 배송비 등록/수정 DT 리스트 조회 **/
	public List<HashMap<String, String>> selectPaGmktShipCostDtTargetList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("pagmkt.common.selectPaGmktShipCostDtTargetList", paramMap.get());
	}
	public String selectTConfigVal(String item) {
		return (String)selectByPk("pagmkt.common.selectTConfigVal", item);
	}


}
