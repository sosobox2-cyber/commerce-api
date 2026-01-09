package com.cware.netshopping.pacopn.claim.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pacopnclaimitemlist;
import com.cware.netshopping.domain.model.Pacopnclaimlist;

public interface PaCopnClaimService {
	
	/**
	 * 반품요청 목록 저장
	 * @param paCopnClaim
	 * @param paCopnClaimitemList
	 * @return
	 * @throws Exception
	 */
	public String savePaCopnClaimTx(Pacopnclaimlist paCopnClaim, List<Pacopnclaimitemlist> paCopnClaimitemList, ParamMap paramMap) throws Exception;
	
	
	/**
	 * 취소처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectCancelList() throws Exception;
	
	/**
	 * 품절취소반품 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectCopnSoldOutordList() throws Exception;
	
	/**
	 * 취소처리 및 결과 저장
	 * @return
	 * @throws Exception
	 */
	public ParamMap makeCancelListProc(HashMap<String, String> apiInfo, HashMap<String, Object> cancelMap) throws Exception;
	
	/**
	 * 취소생성 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetList() throws Exception;
	
	/**
	 * 취소생성 대상 상세 조회
	 * @param paramMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 기 취소처리 후 결과 저장
	 * @param preCancelMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYn(ParamMap preCancelMap) throws Exception;
	
	/**
	 * 반품처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectClaimList() throws Exception;
	
	/**
	 * 반품처리 및 결과 저장 (이미출고 처리 포함)
	 * @param apiInfo
	 * @param claimMap
	 * @return
	 * @throws Exception
	 */
	public ParamMap makeClaimListProc(HashMap<String, String> apiInfo, HashMap<String, Object> claimMap) throws Exception;
	
	/**
	 * 반품생성 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderClaimTargetList() throws Exception;
	
	/**
	 * 반품생성 대상 상세 조회(반품)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderClaimTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 배송사 코드 조회
	 * @param companyName
	 * @return
	 * @throws Exception
	 */
	public String selectClaimDelyGb(String companyName) throws Exception;
	
	/**
	 * 수거송장등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectPickupList() throws Exception;
	public HashMap<String, String> selectPickupDetailList(String paClaimNo) throws Exception;
	
	/**
	 * 수거송장등록 후 결과 저장
	 * @param pickup
	 * @return
	 * @throws Exception
	 */
	public int updatePickupResult(HashMap<String, Object> pickup) throws Exception;
	
	/**
	 * 반품상품 입고 확인처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectReceiveConfirmList() throws Exception;
	
	/**
	 * 반품상품 입고 확인처리 후 결과 저장
	 * @param receive
	 * @return
	 * @throws Exception
	 */
	public int updateReceiveConfirmResult(HashMap<String, Object> receive) throws Exception;
	
	/**
	 * 반품요청 승인 처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectReturnApprovalList() throws Exception;
	
	/**
	 * 반품요청 승인 처리 후 결과 저장
	 * @param returnApproval
	 * @return
	 * @throws Exception
	 */
	public int updateReturnApprovalResult(HashMap<String, Object> returnApproval) throws Exception;
	
	/**
	 * 반품철회 조회 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectReturnWithdrawList(String paCode) throws Exception;
	
	/**
	 * 반품철회 데이터 생성
	 * @param resultWithdraw
	 * @return
	 * @throws Exception
	 */
	public String saveWithdrawListTx(HashMap<String, Object> resultWithdraw) throws Exception;
	
	/**
	 * 반품철회 데이터 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetList() throws Exception;
	
	/**
	 * 반품철회 데이터 상세 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 반품생성 후 PA_DO_FLAG 업데이트
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaDoFlag(ParamMap paramMap) throws Exception;
	
	/**
	 * 상품준비중취소 처리
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String saveOrderRejectProcTx(ParamMap paramMap) throws Exception;


	/**
	 * 주문 상품 순번 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> selectOrgShipmentBoxId(ParamMap paramMap) throws Exception;

	public ParamMap makeClaimPaorderm() throws Exception;
	
	/**
	 * 주문취소승인처리
	 * @return
	 * @throws Exception
	 */
	public ParamMap makeCancelProc(HashMap<String, String> apiInfo, ParamMap paramMap) throws Exception;
	
	/**
	 * 기존 배송지와 비교 - 반품
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String compareAddress(ParamMap paramMap) throws Exception;
	
	/**
	 * 기존 배송지와 비교 - 교환
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String compareExAddress(ParamMap paramMap) throws Exception;

	/**
	 * 업체 회수 담당자에게 메일 전송
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectMailAlertEntpList(ParamMap paramMap) throws Exception;


	public String saveMailAlertTx(ParamMap mailMap) throws Exception;

	/**
	 * 모바일 자동취소 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception;

}
