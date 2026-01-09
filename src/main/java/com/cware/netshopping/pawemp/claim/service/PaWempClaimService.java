package com.cware.netshopping.pawemp.claim.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimItemList;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.pawemp.claim.model.Claim;

public interface PaWempClaimService {

	/**
	 * TPAWEMPCLAIMITEMLIST 조회
	 * @param paClaimNo
	 * @param paOrderNo
	 * @param paShipNo
	 * @param paOrderGb
	 * @param paOrderSeq
	 * @return
	 * @throws Exception
	 */
	public List<PaWempClaimItemList> selectPaWempClaimItemList(String paClaimNo, String paOrderNo, String paShipNo, String paOrderGb, String paOrderSeq) throws Exception;
	
	/**
	 * 위메프 반품접수 대상조회 - 상세(반품접수건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderReturnTargetDt30List(ParamMap paramMap) throws Exception;
	
	/**
	 * 위메프 반품접수 - 대상조회 - 상세(출하지시 이후 취소건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderReturnTargetDt20List(ParamMap paramMap) throws Exception;

	/**
	 * 위메프 반품접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderReturnTargetList() throws Exception;
	
	/**
	 * 위메프 클레임 데이터 생성
	 * @param paWempClaimList
	 * @param paWempClaimItemList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempClaimTx(PaWempClaimList paWempClaimList) throws Exception;
	
	/**
	 * 위메프 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 위메프 반품취소 대상조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnCancelTargetList() throws Exception;
	
	/**
	 * 위메프 반품취소 데이터 생성
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public String savePaWempReturnCancelTx(PaWempClaimList arrPaWempClaim) throws Exception;
	
	/**
	 * 위메프 반품수거등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	public List<Object> selectPickupList() throws Exception;
	
	/**
	 * 위메프 반품수거등록 후 결과 저장
	 * @return
	 * @throws Exception
	 */
	public int updatePickupResultTx(HashMap<String, Object> pickup) throws Exception;
	
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
	public int updateReceiveConfirmResultTx(HashMap<String, Object> receive) throws Exception;
	
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
	public int updateReturnApprovalResultTx(HashMap<String, Object> returnApproval) throws Exception;

	/**
	 * TPAWEMPCLAIMLISt 데이터 생성
	 * @param claim
	 * @param paOrderGb
	 * @return
	 * @throws Exception
	 */
	public PaWempClaimList makePaWempClaimList(Claim claim, String paOrderGb, String paCode) throws Exception;
	
	/**
	 * 위메프 취소요청 정보 DB 입력
	 * @param paWempClaimList
	 * @return
	 * @throws Exception
	 */
	public String savePaWempCancelTx(PaWempClaimList paWempClaimList) throws Exception;

	/**
	 * 기존 배송지와 비교
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String compareAddress(ParamMap paramMap) throws Exception;
}
