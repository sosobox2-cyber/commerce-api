package com.cware.netshopping.passg.common.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsLimitChar;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.domain.model.PaSsgDisplayCategory;
import com.cware.netshopping.domain.model.PaSsgDisplayRecommendCategory;
import com.cware.netshopping.domain.model.PaSsgGoodsCert;
import com.cware.netshopping.domain.model.PaSsgSettlement;


@Service("passg.common.paSsgCommonDAO")
public class PaSsgCommonDAO extends AbstractPaDAO {
	
	public int insertPaOfferCodeList(PaOfferCode paOfferCode) throws Exception{
		return insert("passg.common.insertPaOfferCodeList", paOfferCode);
	}
	
	public int insertPaBrand(PaBrand paBrand) throws Exception{
		return insert("passg.common.insertPaBrand", paBrand);
	}
	
	public int insertPaOrigin(PaOrigin paOrigin) throws Exception{
		return insert("passg.common.insertPaOrigin", paOrigin);
	}
	
	public int insertPaGoodsLimitChar(PaGoodsLimitChar paGoodsLimitChar) throws Exception{
		return insert("passg.common.insertPaGoodsLimitChar", paGoodsLimitChar);
	}
	
	public int deletePaGoodsKindsMomentList(PaGoodsKinds paGoodsKinds) {
		return delete("passg.common.deletePaGoodsKindsMomentList", paGoodsKinds);
	}

	public int insertPaGoodsKindMomentsList(PaGoodsKinds paGoodsKinds) {
		return insert("passg.common.insertPaGoodsKindMomentsList", paGoodsKinds);
	}

	public int insertPaGoodsKindsList(PaGoodsKinds paGoodsKinds) {
		return insert("passg.common.insertPaGoodsKindsList", paGoodsKinds);
	}
	
	//@SuppressWarnings("unchecked")
	//public List<HashMap<String, Object>> selectSsgStandardCategoryList() throws Exception {
	//	return (List<HashMap<String,Object>>) list("passg.common.selectSsgStandardCategoryList", null);
	//}
	
	public int insertPaSsgDisplayCategory(PaSsgDisplayCategory paSsgDisplayCategory) throws Exception{
		return insert("passg.common.insertPaSsgDisplayCategory", paSsgDisplayCategory);
	}

	/**
	 * SSG 업체 배송비 정책 등록 정보 - 조회
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap) throws Exception{
		return (List<HashMap<String,Object>>) list("passg.common.selectEntpSlipCost", apiInfoMap.get());
	}
	
	/**
	 * SSG 업체 배송비 정책 등록 정보 - 저장 
	 * @return int
	 * @throws Exception
	 */
	public int updatePaSsgShipCost(ParamMap shipCostMap) throws Exception{
		return update("passg.common.updatePaSsgShipCost", shipCostMap.get());
	}

	/**
	 * SSG 업체 배송지 주소/택배계약정보 등록 정보 - 조회
	 * @param PaEntpSlip
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception{
		return (HashMap<String, Object>) selectByPk("passg.common.selectSlipInsertList", paEntpSlip);
	}

	/**
	 * SSG 업체 배송지 주소/택배계약정보 등록 정보 - 저장
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int insertPaSsgEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("passg.common.insertPaSsgEntpSlip", paEntpSlip);
	}

	/**
	 * SSG 업체 배송지 주소/택배계약정보 수정 정보 - 조회
	 * @param
	 * @return List<HashMap<String, Object>> 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception{
		return (List<HashMap<String,Object>>) list("passg.common.selectEntpSlipUpdateList", null);
	}

	// 업체주소 키로 조회하므로 단건 반환 (SSG연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, Object>) selectByPk("passg.common.selectEntpSlipUpdate", paEntpSlip);
	}
	
	/**
	 * SSG 업체 배송지 주소/택배계약정보 수정 정보 - 저장
	 * @param PaEntpSlip
	 * @return int
	 * @throws Exception
	 */
	public int updatePaSsgEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("passg.common.updatePaSsgEntpSlip", paEntpSlip);
	}

	/**
	 * SSG 전시 추천 카테고리 대상  - 조회
	 * @param 
	 * @return List<PaGoodsKinds>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsKinds> selectGoodsKindsList() throws Exception{
		return list("passg.common.selectGoodsKindsList", null);
	}

	public int savePaSsgDisplayRecommendCategory(PaSsgDisplayRecommendCategory paSsgDisplayRecommendCategory) {
		return insert("passg.common.savePaSsgDisplayRecommendCategory", paSsgDisplayRecommendCategory);
	}

	/**
	 * SSG 표준분류별 인증정보 조회  - 조회
	 * @param 
	 * @return List<PaGoodsKinds>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsKinds> selectSsgGoodsCertList() throws Exception{
		return list("passg.common.selectSsgGoodsCertList", null);
	}
	
	/**
	 * SSG 표준분류별 인증정보 조회  - 저장
	 * @param paSsgGoodsCert
	 * @return int
	 * @throws Exception
	 */
	public int savePaSsgGoodsCert(PaSsgGoodsCert paSsgGoodsCert) {
		return insert("passg.common.savePaSsgGoodsCert", paSsgGoodsCert);
	}

	/**
	 * SSG 정산 API  - 삭제
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int deletePaSsgSettlement(ParamMap paramMap) throws Exception {
		return delete("passg.common.deletePaSsgSettlement", paramMap.get());
	}

	/**
	 * SSG 정산 API  - 조회 
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectPaSsgSettlementExists(ParamMap paramMap) {
		return (Integer) selectByPk("passg.common.selectPaSsgSettlementExists", paramMap.get());
	}

	/**
	 * SSG 정산 API  - 저장  
	 * @param PaSsgSettlement
	 * @return int
	 * @throws Exception
	 */
	public int insertPaSsgSettlement(PaSsgSettlement paSsgSettlement) throws Exception {
		return insert("passg.common.insertPaSsgSettlement", paSsgSettlement );
	}
}
