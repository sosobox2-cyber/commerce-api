package com.cware.netshopping.panaver.cancel.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;

public interface PaNaverCancelService {

	public String saveCancelSaleTx(ParamMap paramMap) throws Exception;
	
	//19.09.25
	/**
	 * 제휴 - 네이버 취소승인처리 - 취소승인대상조회
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, Object> selectPaNaverOrdCancelApprovalList(ParamMap cancelMap) throws Exception;

	/**
	 * 제휴 - 네이버 취소승인처리 - 저장
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveCancelApprovalProcTx(HashMap<String, Object> cancelMap) throws Exception;

	/**
	 * 제휴 - 네이버 취소승인대상 조회(리스트)
	 * @return List<Object>
	 */
	public List<Object> selectPaNaverOrdCancelListApprovalList() throws Exception;

	/**
	 * 제휴 - 네이버 주문 취소 데이터 생성 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 제휴 - 네이버 기취소 대상 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updatePreCancelYnTx(ParamMap paramMap) throws Exception;


}