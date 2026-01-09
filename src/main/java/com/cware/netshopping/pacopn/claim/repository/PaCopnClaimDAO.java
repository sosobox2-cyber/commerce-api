package com.cware.netshopping.pacopn.claim.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnclaimitemlist;
import com.cware.netshopping.domain.model.Pacopnclaimlist;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pacopn.claim.paCopnClaimDAO")
public class PaCopnClaimDAO extends AbstractPaDAO{

	/**
	 * 반품요청목록 저장 - 데이터 중복 체크
	 * @param paCopnClaimList
	 * @return
	 * @throws Exception
	 */
	public int selectPaCopnClaimListExists(Pacopnclaimlist paCopnClaimList) throws Exception{
		return (Integer) selectByPk("pacopn.claim.selectPaCopnClaimListExists", paCopnClaimList);
	}
	
	/**
	 * 반품요청목록 저장 - TPACOPNCLAIMLIST INSERT
	 * @param paCopnClaimList
	 * @return
	 * @throws Exception
	 */
	public int insertPaCopnClaimList(Pacopnclaimlist paCopnClaimList) throws Exception{
		return insert("pacopn.claim.insertPaCopnClaimList", paCopnClaimList);
	}
	
	/**
	 * 반품요청목록 저장 - TPACOPNCLAIMITEMLIST INSERT
	 * @param paCopnClaimitemList
	 * @return
	 * @throws Exception
	 */
	public int insertPaCopnClaimitemList(Pacopnclaimitemlist paCopnClaimitemList) throws Exception{
		return insert("pacopn.claim.insertPaCopnClaimitemList", paCopnClaimitemList);
	}

	/**
	 * 취소처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelList() throws Exception{
		return list("pacopn.claim.selectCancelList", null);
	}

	/**
	 * 품절취소반품 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCopnSoldOutordList() throws Exception{
		return list("pacopn.claim.selectCopnSoldOutordList", null);
	}
	
	/**
	 * 취소처리(출고중지완료) 후 결과 업데이트 - TPAORDERM
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderResult(Paorderm paorderm) throws Exception{
		return update("pacopn.claim.updatePaOrderResult", paorderm);
	}
	
	/**
	 * 취소생성 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetList() throws Exception{
		return list("pacopn.claim.selectCancelInputTargetList", null);
	}
	
	/**
	 * 취소생성 대상 상세 조회
	 * @param paramMap
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception{
		return list("pacopn.claim.selectCancelInputTargetDtList", paramMap.get());
	}
	
	/**
	 * 기 취소처리 후 결과 저장
	 * @param preCancelMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception{
		return update("pacopn.claim.updatePreCancelYn", preCancelMap);
	}
	
	/**
	 * 반품처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimList() throws Exception{
		return list("pacopn.claim.selectClaimList", null);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectClaimDetailList(String paClaimNo) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.claim.selectClaimDetailList", paClaimNo);
	}
	
	/**
	 * 반품처리(이미출고처리) 후 결과 업데이트 - TPAORDERM
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderResultForClaim(Paorderm paorderm) throws Exception{
		return update("pacopn.claim.updatePaOrderResultForClaim", paorderm);
	}
	
	/**
	 * 반품생성 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetList() throws Exception{
		return list("pacopn.claim.selectOrderClaimTargetList", null);
	}
	
	/**
	 * 반품생성 대상 상세 조회(반품)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderClaimTargetDtList(ParamMap paramMap) throws Exception{
		return list("pacopn.claim.selectOrderClaimTargetDtList", paramMap.get());
	}
	
	/**
	 * 배송사 코드 조회
	 * @param companyName
	 * @return
	 * @throws Exception
	 */
	public String selectClaimDelyGb(String companyName) throws Exception{
		return (String) selectByPk("pacopn.claim.selectClaimDelyGb", companyName);
	}
	
	/**
	 * 수거송장등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectPickupList() throws Exception{
		return list("pacopn.claim.selectPickupList", null);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectPickupDetailList(String paClaimNo) throws Exception{
		return (HashMap<String, String>) selectByPk("pacopn.claim.selectPickupDetailList",paClaimNo);
	}
	
	/**
	 * 수거송장등록 후 결과 저장
	 * @param pickup
	 * @return
	 * @throws Exception
	 */
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception{
		return update("pacopn.claim.updatePickupResult", pickup);
	}
	
	/**
	 * 반품상품 입고 확인처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReceiveConfirmList() throws Exception{
		return list("pacopn.claim.selectReceiveConfirmList", null);
	}
	
	/**
	 * 반품상품 입고 확인처리 후 결과 저장
	 * @param receive
	 * @return
	 * @throws Exception
	 */
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception{
		return update("pacopn.claim.updateReceiveConfirmResult", receive);
	}
	
	/**
	 * 반품요청 승인 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnApprovalList() throws Exception{
		return list("pacopn.claim.selectReturnApprovalList", null);
	}
	
	/**
	 * 반품요청 승인 처리 후 결과 저장
	 * @param returnApproval
	 * @return
	 * @throws Exception
	 */
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception{
		return update("pacopn.claim.updateReturnApprovalResult", returnApproval);
	}
	
	/**
	 * 반품철회 조회 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectReturnWithdrawList(String paCode) throws Exception{
		return list("pacopn.claim.selectReturnWithdrawList", paCode);
	}
	
	/**
	 * 반품철회 저장 - INSERT TPACOPNCLAIMLIST
	 * @param resultWithdraw
	 * @return
	 * @throws Exception
	 */
	public int insertClaimCancel(HashMap<String, Object> resultWithdraw) throws Exception{
		return insert("pacopn.claim.insertClaimCancel", resultWithdraw);
	}
	
	/**
	 * 반품철회 저장 - INSERT TPAORDERM
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int insertClaimCancelOrderM(Paorderm paorderm) throws Exception{
		return insert("pacopn.claim.insertClaimCancelOrderM", paorderm);
	}
	
	/**
	 * 반품철회 데이터 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetList() throws Exception{
		return list("pacopn.claim.selectClaimCancelTargetList", null);
	}
	
	/**
	 * 반품철회 데이터 상세 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pacopn.claim.selectClaimCancelTargetDtList", paramMap.get());
	}
	
	/**
	 * 반품생성 후 PA_DO_FLAG 업데이트
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaDoFlag(ParamMap paramMap) throws Exception{
		return update("pacopn.claim.updatePaDoFlag", paramMap.get());
	}
	
	/**
	 * 상품준비중취소 처리 - TPAORDERM UPDATE
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrdermPreCancel(ParamMap paramMap) throws Exception{
		return update("pacopn.claim.updatePaOrdermPreCancel", paramMap.get());
	}
	
	/**
	 * 주문 없는 반품 건 확인
	 * @param paCopnClaimList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPrentsOrderCreated(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>) selectByPk("pacopn.claim.selectPrentsOrderCreated", paramMap.get());
	}
	
	/**
	 * 주문 상품 순번 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception {
		return (HashMap<String, String>) selectByPk("pacopn.claim.selectOrgShipmentBoxId", paramMap.get());
	}
	
	public int updatePaCopnClaimListProcFlag(ParamMap paramMap) throws Exception {
		return update("pacopn.claim.updatePaCopnClaimListProcFlag", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectOrgItemInfoByCancelInfo(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacopn.claim.selectOrgItemInfoByCancelInfo", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectClaimInputTargetList() throws Exception {
		return (List<HashMap<String, Object>>) list("pacopn.claim.selectClaimInputTargetList", null);
	}
	
	public String compareAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pacopn.claim.compareAddress", paramMap.get());
	}

	public String compareExAddress(ParamMap paramMap) throws Exception {
		return (String) selectByPk("pacopn.claim.compareExAddress", paramMap.get());
	}

	public int selectCopnClaimList4AdminClaim(Pacopnclaimlist paCopnClaim) {
		return (Integer) selectByPk("pacopn.claim.selectCopnClaimList4AdminClaim", paCopnClaim);
	}

	public int updatePaCopnClaimListReceiptStatus(Pacopnclaimlist paCopnClaim) {
		return update("pacopn.claim.updatePaCopnClaimListReceiptStatus", paCopnClaim);
	}

	/**
	 * 업체 회수 담당자에게 메일 전송
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectMailAlertEntpList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("pacopn.claim.selectMailAlertEntpList", paramMap.get());
	}

	/**
	 * 메일 전송 INSERT_TUMS
	 * @param mailMap
	 * @return
	 * @throws Exception
	 */
	public int insertUms(ParamMap mailMap) throws Exception {
		return insert("pacopn.claim.insertUms", mailMap.get());
	}

	/**
	 * 메일 전송 이력 UPDATE
	 * @param mailMap
	 * @return
	 * @throws Exception
	 */
	public int updateAlertStatus(ParamMap mailMap) throws Exception {
		return update("pacopn.claim.updateAlertStatus", mailMap.get());
	}

	/**
	 * 기취소여부 조회
	 * @param paCopnClaimitem
	 * @return
	 */
	public int selectPreCancelYn(Pacopnclaimitemlist paCopnClaimitem) throws Exception {
		return (Integer) selectByPk("pacopn.claim.selectPreCancelYn", paCopnClaimitem);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectOrderGoodsInfo(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>) selectByPk("pacopn.claim.selectOrderGoodsInfo", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception {
		return list("pacopn.claim.selectPaMobileOrderAutoCancelList", paramMap.get());
	}
}
