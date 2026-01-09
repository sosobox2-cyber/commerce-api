package com.cware.netshopping.passg.claim.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgCancelListVO;
import com.cware.netshopping.domain.PaSsgClaimListVO;
import com.cware.netshopping.domain.PaSsgOrderListVO;

public interface PaSsgClaimProcess {
	
	/**
	 * 취소정보  저장
	 * @param List<PaSsgCancelListVO>
	 * @return int
	 * @throws Exception
	 */
	public int saveSsgCancelList(PaSsgCancelListVO vo, String claimStatus) throws Exception;
	
	/**
	 * 취소완료정보  저장
	 * @param vo
	 * @param claimStatus
	 * @return
	 * @throws Exception
	 */
	public int saveSsgCancelCompleteList(PaSsgCancelListVO vo, String claimStatus) throws Exception;

	/**
	 * 클레임 데이터 생성
	 * @param PaSsgClaimListVO
	 * @return
	 * @throws Exception
	 */
	public String saveSsgClaimList(PaSsgClaimListVO vo) throws Exception;
	
	/**
	 * BO 데이터 생성 위한 클레임내역 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;
	
	/**
	 * Async 용 취소 목록 상세 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;
	
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	
	public String compareAddress(ParamMap paramMap) throws Exception;
	
	/**
	 * 재고 없을 시 교환 거부 처리를 위한 change_flag update
	 * @param int
	 * @param mappingSeq
	 * @throws Exception
	 */
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception;
	
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
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 회수확인,회수완료 대상 데이터 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectPaSsgRecoveryList(ParamMap paramMap) throws Exception;
	
	/**
	 * PaOrderM doflag 수정
	 * @param paorderm
	 * @return
	 * @throws Exception
	 */
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception;
	
	/**
	 * 취소 승인/거부 처리 대상 조회
	 * @return List
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPaSsgOrderCancelList() throws Exception;
	
	public int updatePaSsgCancelConfirm(ParamMap paramMap) throws Exception;
	
	public int updatePaSsgCancelList(ParamMap paramMap) throws Exception;
	
	public int updatePaSsgCancelList4Withdraw(ParamMap paramMap) throws Exception;

	/**
	 * 모바일 자동취소 (품절취소반품)
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception;;

	public PaSsgOrderListVO selectPaSsgOrderList(PaSsgCancelListVO paSsgCancelList) throws Exception;

}