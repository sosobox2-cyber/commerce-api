package com.cware.netshopping.pa11st.common.process;

import java.util.HashMap;
import java.util.List;

import com.cware.netshopping.domain.Pa11stnodelyareamVO;
import com.cware.netshopping.domain.model.Pa11stOrigin;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;

public interface Pa11stCommonProcess {
	
	/**
	 * 11번가 전체카테고리저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stGoodsKinds(List<PaGoodsKinds> paGoodsKindsList) throws Exception;
	
	/**
	 * 11번가 원산지매핑
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveMappingPa11stOrigin(List<Pa11stOrigin> pa11stOriginList) throws Exception;
	
	/**
	 * 11번가 출고지저장 - 대상 목록 조회
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 11번가 출고지등록
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stEntpSlip(PaEntpSlip paEntpSlip) throws Exception;
	

	/**
	 * 11번가 출고지수정 - 수정대상목록조회
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception;

	/**
	 * 11번가 출고지수정
	 * @param PaEntpSlip
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stEntpSlipUpdate(PaEntpSlip paEntpSlip) throws Exception;
	
	/**
	 * 11번가 인증여부 저장
	 * @param List<PaGoodsKinds>
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stCertGoodsKinds(List<PaGoodsKinds> paGoodsKinds) throws Exception;
	
	/**
	 * 11번가 정보고시 저장
	 * @param List<PaOfferCode>
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception;
	
	/**
	 * 11번가 배송불가지역등록 - 우편번호 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectTroadPostList(String paCode) throws Exception;
	
	/**
	 * 11번가 배송불가지역등록 - 배송불가지역 저장
	 * @param List<Pa11stnodelyareamVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveNoDelyAreaInsert(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception;
	
	/**
	 * 11번가 배송불가지역조회 - 배송불가지역번호 저장
	 * @param List<Pa11stnodelyareamVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveNoDelyAreaSelect(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception;
	
	/**
	 * 11번가 배송불가지역수정 - 우편번호 추가등록 및 배송불가지역 삭제할 대상 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectNoDelyAreaUpdateList(String paCode) throws Exception;
	
	/**
	 * 11번가 배송불가지역수정 - 배송불가지역 저장
	 * @param List<Pa11stnodelyareamVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveNoDelyAreaUpdate(List<Pa11stnodelyareamVO> arrPa11stNoDelyAreaList) throws Exception;
	
	/**
	 * 11번가 배송불가지역적용 - 배송불가지역 적용할 상품 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectNoDelyAreaApplyList(String paCode) throws Exception;
	
	/**
	 * 11번가 배송불가지역적용 - 상품 배송불가지역 적용 저장
	 * @param Pa11stnodelyareamVO
	 * @return String
	 * @throws Exception
	 */
	public String saveNoDelyAreaApply(Pa11stnodelyareamVO arrPa11stNoDelyArea) throws Exception;
	
	/**
	 * 11번가 배송불가지역해제 - 배송불가지역 해제할 상품 조회
	 * @param String
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectNoDelyAreaDeleteList(String paCode) throws Exception;
	
	/**
	 * 11번가 배송불가지역적용 - 배송불가지역 속성 상품 추가
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String saveNoDelyAreaApplyGoods(String paCode) throws Exception;
	
	/**
	 * 11번가 배송불가지역M 데이터 중복 체크
	 * @param String
	 * @return int
	 * @throws Exception
	 */
	public int checkTpa11stNoDelyAream(String paCode) throws Exception;
	
	/**
	 * 11번가 브랜드 저장
	 * @param List<PaBrand> paBrandList
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stBrand(List<PaBrand> paBrandList) throws Exception;
	/**
	 * 11번가 출고지별 배송비관리 할 항목 조회
	 * */
	public List<HashMap<?,?>> selectEntpSlipCost(String paCode) throws Exception;
	
	public int updateEntpSlipCost(HashMap<String,String> hashMap) throws Exception;
	
	/**
	 * 11번가 정산저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String savePa11stSettlement(List<Pa11stSettlement> pa11stSettlementList) throws Exception;
}
