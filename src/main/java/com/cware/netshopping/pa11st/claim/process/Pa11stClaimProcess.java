package com.cware.netshopping.pa11st.claim.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;

public interface Pa11stClaimProcess {

	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PA11STAPI_03_011 : 저장.
	 * @param Pa11stclaimlistVO
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PA11STAPI_03_025 : 저장
	 * @param Pa11stclaimlistVO
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnCompleteList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 반품승인처리 - 회수확정대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnOkList() throws Exception;
	
	/**
	 * 반품승인처리 - 반품승인처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public ParamMap saveReturnConfirmProc(HashMap<String, Object> returnMap) throws Exception;
	
	/**
	 * 반품철회목록조회 - 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveReturnCancelList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 반품보류처리 - 반품보류대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnHoldList() throws Exception;
	
	/**
	 * 반품완료보류처리 - 반품완료보류 처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public ParamMap saveReturnHoldProc(HashMap<String, Object> returnMap) throws Exception;
	
	/**
	 * 수취확인후 직권취소목록조회 - 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveAuthCancelList(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 11번가 반품접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderClaimTargetList() throws Exception;
	
	/**
	 * 11번가 반품취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetList() throws Exception;
	
	/**
	 * 11번가 반품접수 대상조회 - 상세(반품접수건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 반품접수 대상조회 - 상세(출하지시 이후 취소건)
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	/**
	 * 기존 배송지와 비교
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String compareAddress(ParamMap paramMap) throws Exception;
	
	/**
	 * 반품송장입력 - 반품송장등록대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectReturnSlipProcList() throws Exception;
	
	/**
	 * 반품송장입력 - 반품송장등록처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public ParamMap saveReturnSlipProc(HashMap<String, Object> returnSlipMap) throws Exception;
}