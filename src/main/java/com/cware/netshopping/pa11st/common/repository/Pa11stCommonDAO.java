package com.cware.netshopping.pa11st.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.Pa11stnodelyareamVO;
import com.cware.netshopping.domain.model.Pa11stOrigin;
import com.cware.netshopping.domain.model.Pa11stSettlement;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;


@Service("pa11st.common.pa11stCommonDAO")
public class Pa11stCommonDAO extends AbstractPaDAO{
	

	/**
	 * 11번가 출고지등록 - 대상 목록 조회
	 * @returnList
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectEntpShipInsertList(PaEntpSlip paEntpSlip) throws Exception {
	    return (HashMap<String, String>) selectByPk("pa11st.common.selectEntpShipInsertList", paEntpSlip);
	}
	
	/**
	 * 11번가 출고지등록
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return insert("pa11st.common.insertPa11stEntpSlip", paEntpSlip);
	}

	/**
	 * 11번가 출고지수정 - 대상 목록 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectEntpShipUpdateList(String paAddrGb) throws Exception {
	    return list("pa11st.common.selectEntpShipUpdateList", paAddrGb);
	}
	
	// 업체주소 키로 조회하므로 단건 반환 (11번가연동구조개선)
	@SuppressWarnings("unchecked")
	public Map<String, String> selectEntpShipUpdate(PaEntpSlip paEntpSlip) throws Exception {
	    return (Map<String, String>) selectByPk("pa11st.common.selectEntpShipUpdate", paEntpSlip);
	}
	
	/**
	 * 11번가 출고지수정
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int updatePa11stEntpSlip(PaEntpSlip paEntpSlip) throws Exception{
		return update("pa11st.common.updatePa11stEntpSlip", paEntpSlip);
	}
	
	/**
	 * 11번가 원산지삭제
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int deletePa11stOrigin() throws Exception{
		return delete("pa11st.common.deletePa11stOrigin", null);
	}
	
	
	/**
	 * 11번가 원산지저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stOrigin(Pa11stOrigin pa11stOrigin) throws Exception{
		return insert("pa11st.common.insertPa11stOrigin", pa11stOrigin);
	}
	
	/**
	 * 11번가 원산지매핑저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stOriginMapping(Pa11stOrigin pa11stOrigin) throws Exception{
		return insert("pa11st.common.insertPa11stOriginMapping", pa11stOrigin);
	}

	/**
	 * 11번가 카테고리별 인증여부 저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int updatePa11stCertGoodsKinds(PaGoodsKinds paGoodsKinds) throws Exception{
		return update("pa11st.common.updatePa11stCertGoodsKinds", paGoodsKinds);
	}
	
	/**
	 * 11번가 상품고시 임시저장 테이블 데이터삭제
	 * @param
	 * @return String
	 * @throws Exception
	 */
	public int deletePa11stOfferCodeMoment() throws Exception{
		return delete("pa11st.common.deletePa11stOfferCodeMoment", null);
	}
	
	/**
	 * 11번가 상품고시 임시저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stOfferCodeMoment(PaOfferCode pa11stOfferCode) throws Exception{
		return insert("pa11st.common.insertPa11stOfferCodeMoment", pa11stOfferCode);
	}
	
	/**
	 * 11번가 상품고시 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int insertPa11stOfferCodeList(PaOfferCode pa11stOfferCode) throws Exception{
		return insert("pa11st.common.insertPa11stOfferCodeList", pa11stOfferCode);
	}
	
	/**
	 * 11번가 상품고시 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int updatePa11stOfferCodeList(PaOfferCode pa11stOfferCode) throws Exception{
		return update("pa11st.common.updatePa11stOfferCodeList", pa11stOfferCode);
	}
	
	/**
	 * 11번가 상품고시 저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int updatePa11stOfferCodeListUseYn(PaOfferCode pa11stOfferCode) throws Exception{
		return update("pa11st.common.updatePa11stOfferCodeListUseYn", pa11stOfferCode);
	}
	
	/**
	 * 11번가 상품고시 - 유형코드 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaOfferCode> selectPaOfferTypeList(PaOfferCode offerType) throws Exception {
	    return list("pa11st.common.selectPaOfferTypeList", offerType);
	}
	
	/**
	 * 11번가 상품고시 - 유형코드 tcode[O506] 삭제
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	public int updatePaOfferType(PaOfferCode offerType) throws Exception{
		return update("pa11st.common.updatePaOfferType", offerType);
	}
	
	/**
	 * 11번가 상품고시 - 유형코드 tcode[O506] 저장
	 * @param PaOfferCode
	 * @return String
	 * @throws Exception
	 */
	public int insertPaOfferType(PaOfferCode offerType) throws Exception{
		return insert("pa11st.common.insertPaOfferType", offerType);
	}
	

	
	/**
	 * 11번가 배송불가지역등록 - 우편번호 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectTroadPostList(String paCode) throws Exception {
	    return list("pa11st.common.selectTroadPostList", paCode);
	}
	
	/**
	 * 11번가 배송불가지역등록 - tpa11stnodelyaream insert
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPa11stNoDelyAreaM(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return insert("pa11st.common.insertPa11stNoDelyAreaM", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역등록 - tpa11stnodelyareadt insert
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPa11stNoDelyAreaDt(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return insert("pa11st.common.insertPa11stNoDelyAreaDt", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역등록 - tpa11stnodelyareaapply insert
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPa11stNoDelyAreaApply(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return insert("pa11st.common.insertPa11stNoDelyAreaApply", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역조회 - 배송불가지역번호 저장
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePa11stNoDelyAreaDt(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return update("pa11st.common.updatePa11stNoDelyAreaDt", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역수정 - 우편번호 추가등록 및 배송불가지역 삭제할 대상 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectNoDelyAreaUpdateList(String paCode) throws Exception {
	    return list("pa11st.common.selectNoDelyAreaUpdateList", paCode);
	}
	
	/**
	 * 11번가 배송불가지역조회 - 삭제여부 처리
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePa11stNoDelyAreaDtDeleteYn(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return update("pa11st.common.updatePa11stNoDelyAreaDtDeleteYn", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역적용 - 배송불가지역 적용할 상품 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectNoDelyAreaApplyList(String paCode) throws Exception {
	    return list("pa11st.common.selectNoDelyAreaApplyList", paCode);
	}
	
	/**
	 * 11번가 배송불가지역적용 - Pa11stNoDelyAreaApply update
	 * @param Pa11stnodelyareamVO
	 * @return int
	 * @throws Exception
	 */
	public int updatePa11stNoDelyAreaApply(Pa11stnodelyareamVO pa11stnodelyaream) throws Exception{
		return update("pa11st.common.updatePa11stNoDelyAreaApply", pa11stnodelyaream);
	}
	
	/**
	 * 11번가 배송불가지역해제 - 배송불가지역 해제할 상품 조회
	 * @returnList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectNoDelyAreaDeleteList(String paCode) throws Exception {
	    return list("pa11st.common.selectNoDelyAreaDeleteList", paCode);
	}
	
	/**
	 * 11번가 배송불가지역적용 - 배송불가지역 순번 조회
	 * @returnList String
	 * @throws Exception
	 */
	public String selectNoDelyAreaCodeList(String paCode) throws Exception{
		return (String) selectByPk("pa11st.common.selectNoDelyAreaCodeList", paCode);
	}
	
	/**
	 * 11번가 배송불가지역M 데이터 중복 체크
	 * @returnList String
	 * @throws Exception
	 */
	public int checkTpa11stNoDelyAream(String paCode) throws Exception{
		return (Integer) selectByPk("pa11st.common.checkTpa11stNoDelyAream", paCode);
	}
	
	/**
	 * 11번가 사용하지 않는 카테고리 use_yn = 0 처리.
	 * @param PaGoodsKinds
	 * @return int
	 * @throws Exception
	 */
	public int updatePaGoodsKindsUnUseListUseYn(PaGoodsKinds paGoodsKinds) throws Exception{
		return update("pa11st.common.updatePaGoodsKindsUnUseListUseYn", paGoodsKinds);
	}
	
	/**
	 * 11번가 브랜드저장
	 * @param PaGoodsKinds
	 * @return String
	 * @throws Exception
	 */
	public int insertPaBrandList(PaBrand paBrand) throws Exception{
		return insert("pa11st.common.insertPaBrandList", paBrand);
	}
	/**
	 * 11번가 출고지별 배송비관리 할 항목 조회
	 * */
	public List<HashMap<?,?>> selectEntpSlipCost(String paCode) throws Exception{
		return (List<HashMap<?,?>>) list("pa11st.common.selectEntpSlipCost", paCode);
	}
	public int updateEntpSlipCost(HashMap<String,String> hashMap) throws Exception{
		return update("pa11st.common.updateEntpSlipCost", hashMap);
	}
	

	/**
	 * 정산데이터 확인
	 * @param Pa11stSettlement
	 * @return String
	 * @throws Exception
	 */
	public int selectPa11stSettlementList(Pa11stSettlement pa11stSettlement) throws Exception{
		return (int)selectByPk("pa11st.common.selectPa11stSettlementList", pa11stSettlement);
	}
}