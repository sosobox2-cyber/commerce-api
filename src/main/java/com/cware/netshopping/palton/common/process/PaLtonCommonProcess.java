package com.cware.netshopping.palton.common.process;

import java.util.HashMap;
import java.util.List;

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

public interface PaLtonCommonProcess {

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
	public String saveLtonStdCategoryInfo(List<PaLtonDispList> disInfoList, List<PaLtonAttrList> attrInfoList, List<PaLtonPdItmsList> pdItmsInfoList, PaLtonStdCtgr stdCtgrData) throws Exception;

	/**
	 * 롯데ON 브랜드 조회 API 결과 저장
	 * @param paBrandList
	 * @return
	 * @throws Exception
	 */
	public String saveLtonBrand(List<PaBrand> paBrandList) throws Exception;

	/**
	 * 롯데ON 출고지 /배송지 대상조회 리스트
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectSlipInsertList(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 롯데ON 출고지 /배송지 저장
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public String savePaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 롯데ON 출고지 /배송지 수정 리스트 조회
	 * @param entpManGb
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipUpdateList() throws Exception;

	/**
	 * 롯데ON 출고지 /배송지 수정 저장
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public String updatePaLtonEntpSlip(PaEntpSlip paEntpSlip) throws Exception;

	/**
	 * 롯데ON 배송비 정책 등록 대상 조회
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectEntpSlipCost(ParamMap paramMap) throws Exception;
	
	/**
	 * 롯데ON 추가 배송비 정책 등록 대상 조회
	 * @param paEntpSlip
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectAddShipCost(ParamMap paramMap) throws Exception;

	/**
	 * 롯데ON 배송비 정책 등록 저장
	 * @param custShipCostMap
	 * @return
	 * @throws Exception
	 */
	public String savePaLtonCustShipCost(ParamMap custShipCostMap) throws Exception;
	
	/**
	 * 롯데ON 추가 배송비 정책 등록 저장
	 * @param custShipCostMap
	 * @return
	 * @throws Exception
	 */
	public String savePaLtonAddCustShipCost(ParamMap addShipCostMap) throws Exception;

	/**
	 * 롯데ON 원산지 정보 insert
	 * @param paLtonOrigin
	 * @return
	 */
	public String insertPaLtonOrigin(List<PaLtonRetrieveCode> paLtonOrigin) throws Exception;

	/**
	 * 롯데ON 정산 insert
	 * @param settlementData
	 * @param apiDataMap 
	 * @throws Exception
	 */
	public void saveLtonSettlement(PaLtonSettlement settlementData, ParamMap apiDataMap) throws Exception;

}
