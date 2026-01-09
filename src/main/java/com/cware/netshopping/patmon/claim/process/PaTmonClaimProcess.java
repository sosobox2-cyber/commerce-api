package com.cware.netshopping.patmon.claim.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTmonCancelListVO;
import com.cware.netshopping.domain.PaTmonClaimListVO;
import com.cware.netshopping.domain.PaTmonRedeliveryListVO;

public interface PaTmonClaimProcess {

	/**
	 * C1 취소정보  저장
	 * @param List<PaTmonCancelListVO>
	 * @return int
	 * @throws Exception
	 */
	int saveTmonCancelList(List<PaTmonCancelListVO> paTmonCancelList) throws Exception;

	/**
	 * C8 취소철회 건 WITHDRAW_YN = 1 UPDATE
	 * @param List<PaTmonCancelListVO>
	 * @return int
	 * @throws Exception
	 */
	int saveTmonWithdrawCancelList(List<PaTmonCancelListVO> paTmonCancelList) throws Exception;
	
	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPaTmonOrderCancelList() throws Exception;

	/**
	 * 취소요청 승인 실패 시 procFlag update
	 * @param failData
	 * @return
	 * @throws Exception
	 */
	int updateProcFlag(Map<String, Object> failData) throws Exception;
	
	String updatePaTmonCancelList(ParamMap paramMap) throws Exception;
	
	int updatePaTmonCancelConfirm(ParamMap paramMap) throws Exception;

	/**
	 * BO 데이터 생성 위한 취소내역 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	/**
	 * Async 용 취소 목록 상세 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * TmonClaimList 데이터 생성
	 * @param paTmonClaimList
	 * @return
	 * @throws Exception
	 */
	public String saveTmonClaimList(List<PaTmonClaimListVO> paTmonClaimList) throws Exception;

	List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	String compareAddress(ParamMap paramMap) throws Exception;
	
	/**
	 * 클레임 승인 대상 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectPaTmonReturnExchangeApprovalList(ParamMap paramMap) throws Exception;
	
	/**
	 * PaOrderM doflag 수정
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception;
	
	/**
	 * 환불 상세 조회 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectClaimDtTargetList(String claimGb) throws Exception;
	
	/**
	 * TPATMONCLAIMLIST, TPAORDERM 환불 상세 조회 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String updatePaTmonClaimListDetail(PaTmonClaimListVO vo) throws Exception;

	/**
	 * 반송장 등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectReturnInvoiceTargetList() throws Exception;
	
	/**
	 * 반품타켓 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;
	
	/**
	 * 출고전 반품 건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	
	/**
	 * 반품 철회 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 교환 철회 데이터 상세 조회
	 * @return
	 * @throws Exception
	 */
	List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	int updatePaOrderm4PreChangeCancel(Map<String, Object> preCancelMap) throws Exception;

	/**
	 * 재고 없을 시 교환 거부 처리를 위한 change_flag update
	 * @param int
	 * @param mappingSeq
	 * @throws Exception
	 */
	int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;

	/**
	 * 교환 거부 목록 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPaTmonExchangeRefuseList() throws Exception;

	int updatePaOrderM4ChangeRefualReuslt(ParamMap apiDataMap) throws Exception;

	/**
	 * 교환 보류설정/해제 목록 조회
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPaTmonExchangeHoldProcList(ParamMap paramMap) throws Exception;

	int saveTmonRedelivaryList(List<PaTmonRedeliveryListVO> paTmonReDeliverylList) throws Exception;

	int updatePaTmonHoldYn(Map<String, Object> exchangeHoldData) throws Exception;

	/** 환불 거부 처리
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String updatePaTmonReturnList(ParamMap paramMap) throws Exception;

	List<Map<String, Object>> selectMappingSeq(ParamMap claimMap) throws Exception;
	
	/**
	 * 반품 요청일자 조회
	 * @return List
	 * @throws Exception
	 */
	List<String> selectPaTmonReturnRequestedDateList() throws Exception;
}