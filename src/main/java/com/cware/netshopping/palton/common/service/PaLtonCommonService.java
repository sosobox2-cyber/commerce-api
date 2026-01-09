package com.cware.netshopping.palton.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaLtonAttrList;
import com.cware.netshopping.domain.model.PaLtonDispCtgr;
import com.cware.netshopping.domain.model.PaLtonDispList;
import com.cware.netshopping.domain.model.PaLtonPdItmsList;
import com.cware.netshopping.domain.model.PaLtonRetrieveCode;
import com.cware.netshopping.domain.model.PaLtonSettlement;
import com.cware.netshopping.domain.model.PaLtonStdCtgr;

public interface PaLtonCommonService {

	/**
	 * 롯데ON 전시카테고리 조회 정보 insert
	 * @param displayData
	 * @return String
	 * @throws Exception
	 */
	public String insertPaDisplayCategory(PaLtonDispCtgr displayData) throws Exception;

	/**
	 * 롯데ON 표준카테고리 조회 정보 save
	 * @param disInfoList	 : 전시카테고리 리스트 정보
	 * @param attrInfoList	 : 속성유형 리스트 정보
	 * @param pdItmsInfoList : 상품품목고시 리스트 정보
	 * @param stdCtgrData	 : 표준카테고리 Data 정보
	 * @return String
	 * @throws Exception
	 */
	public String saveLtonStdCategoryInfoTx(List<PaLtonDispList> disInfoList, List<PaLtonAttrList> attrInfoList, List<PaLtonPdItmsList> pdItmsInfoList, PaLtonStdCtgr stdCtgrData) throws Exception;

	/**
	 * 롯데ON 브랜드 조회 API 결과 저장
	 * @param PaBrand
	 * @return
	 * @throws Exception
	 */
	public String saveLtonBrandTx(List<PaBrand> paBrandList) throws Exception;

	/**
	 * 롯데ON 출고지/반품지 조회
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip)throws Exception;

	/**
	 * 롯데ON 출고지/반품지 저장
	 * @param PaEntpSlip 
	 * @return Map
	 * @throws Exception
	 */
	public String savePaLtonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 롯데ON 출고지/반품지 수정 조회
	 * @param entpManGb 
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception;

	/**
	 * 롯데ON 출고지/반품지 수정 저장
	 * @param paEntpSlip 
	 * @return String
	 * @throws Exception
	 */
	public String updatePaLtonEntpSlipTx(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 롯데ON 배송비정책 등록 대상조회
	 * @param paramMap 
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap paramMap) throws Exception;

	/**
	 * 롯데ON 추가 배송비정책 등록 대상조회
	 * @param paramMap 
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectAddShipCost(ParamMap paramMap) throws Exception;

	/**
	 * 롯데ON 배송비정책 등록 저장
	 * @param paramMap 
	 * @return String
	 * @throws Exception
	 */
	public String savePaLtonCustShipCostTx(ParamMap custShipCostMap) throws Exception;
	
	/**
	 * 롯데ON 추가 배송비정책 등록 저장
	 * @param paramMap 
	 * @return String
	 * @throws Exception
	 */
	public String savePaLtonAddCustShipCostTx(ParamMap addShipCostMap) throws Exception;

	/**
	 * 롯데ON 원산지 정보 insert
	 * @param paLtonOrigin
	 * @return
	 * @throws Exception
	 */
	public String insertPaLtonOrigin(List<PaLtonRetrieveCode> paLtonOrigin) throws Exception;

	/**
	 * 롯데ON 정산 insert
	 * @param settlementData
	 * @param apiDataMap 
	 */
	public void saveLtonSettlementTx(PaLtonSettlement settlementData, ParamMap apiDataMap) throws Exception;

}
