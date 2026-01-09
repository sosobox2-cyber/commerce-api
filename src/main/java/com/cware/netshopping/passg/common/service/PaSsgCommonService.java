package com.cware.netshopping.passg.common.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


public interface PaSsgCommonService {

	/**
	 * SSG 업체 배송비 정책 등록 정보 조회
	 * @param apiInfoMap
	 * @return List<HashMap<String, Object>>
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectEntpSlipCost(ParamMap apiInfoMap)  throws Exception;

	/**
	 * SSG 업체 배송비 정책 등록 정보 저장
	 * @param apiInfoMap
	 * @return ParamMap
	 * @throws Exception
	 */
	String savePaSsgShipCostTx(ParamMap shipCostMap) throws Exception;
	
	public void savePaOfferCodeListTx(List<PaOfferCode> paOfferCodeList) throws Exception;
	public void savePaBrandTx(List<PaBrand> paBrandList) throws Exception;
	public void savePaOrigindTx(List<PaOrigin> paOriginList) throws Exception;
	public void savePaGoodsLimitCharTx(List<PaGoodsLimitChar> paGoodsLimitCharList) throws Exception;
	public void savePaGoodsKindsTx(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	//public List<HashMap<String, Object>> selectSsgStandardCategoryList() throws Exception;
	public void savePaSsgDisplayCategoryTx(List<PaSsgDisplayCategory> paSsgDisplayCategoryList) throws Exception;

	/**
	 * SSG 업체 배송지 주소/택배계약정보 등록 정보 - 조회
	 * @param PaEntpSlip
	 * @return Map
	 * @throws Exception
	 */
	Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * SSG 업체 배송지 주소/택배계약정보 등록 정보 - 저장
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	String savePaSsgEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * SSG 업체 배송지 주소/택배계약정보 수정 정보 - 조회
	 * @param
	 * @return List<HashMap<String, Object>> 
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception;

	/**
	 * SSG 업체 배송지 주소/택배계약정보 수정 정보 - 저장
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	String updatePaSsgEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * SSG 전시 추천 카테고리 대상  - 조회
	 * @param 
	 * @return List<PaGoodsKinds>
	 * @throws Exception
	 */
	List<PaGoodsKinds> selectGoodsKindsList() throws Exception;

	/**
	 * SSG 전시 추천 카테고리 대상  - 저장
	 * @param List<PaSsgDisplayRecommendCategory>
	 * @return String
	 * @throws Exception
	 */
	String savePaSsgDisplayRecommendCategoryTx(List<PaSsgDisplayRecommendCategory> paSsgDisplayRecommendCategoryList) throws Exception;

	/**
	 * SSG 표준분류별 인증정보 조회  - 조회
	 * @param 
	 * @return List<PaGoodsKinds>
	 * @throws Exception
	 */
	List<PaGoodsKinds> selectSsgGoodsCertList() throws Exception;

	/**
	 * SSG 표준분류별 인증정보 조회  - 저장 
	 * @param List<PaSsgGoodsCert>
	 * @return String
	 * @throws Exception
	 */
	String savePaSsgGoodsCertTx(List<PaSsgGoodsCert> paSsgGoodsCertList)  throws Exception;
	
	/**
	 * SSG 정산 API   - 저장 
	 * @param List<PaSsgSettlement>
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	String savePaSsgSettlementTx(List<PaSsgSettlement> paSsgSettlementList, ParamMap paramMap, int page) throws Exception;
		
}
