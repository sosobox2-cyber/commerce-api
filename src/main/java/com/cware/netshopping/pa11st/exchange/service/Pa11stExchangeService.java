package com.cware.netshopping.pa11st.exchange.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stclaimlistVO;
import com.cware.netshopping.domain.model.Paorderm;

public interface Pa11stExchangeService {
	
	/**
	 * 교환신청목록조회 - 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 교환승인처리 - 교환출고 대상 조회
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectExchangeSlipList() throws Exception;
	
	/**
	 * 교환승인처리 - 교환승인처리
	 * @param HashMap<String, Object>
	 * @return String
	 * @throws Exception
	 */
	public ParamMap saveExchangeConfirmProcTx(HashMap<String, Object> returnMap) throws Exception;
	
	/**
	 * 교환거부처리 - 교환거부처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeRejectProcTx(ParamMap paramMap) throws Exception;
	
	/**
	 * 교환철회목록조회 - 저장
	 * @param List<Pa11stclaimlistVO>
	 * @return String
	 * @throws Exception
	 */
	public String saveExchangeCancelListTx(List<Pa11stclaimlistVO> arrPa11stclaimlist) throws Exception;
	
	/**
	 * 교환거부처리 - 교환취소건 존재여부 확인
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	public int selectExchangeRefusalExists(ParamMap paramMap) throws Exception;
	
	/**
	 * 11번가 교환접수 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetList() throws Exception;
	
	/**
	 * 11번가 교환취소 대상 조회
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetList() throws Exception;

	/**
	 * 11번가 교환접수 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	/**
	 * 11번가 교환접수 결과처리 - 교환거부처리 parameter 조회.
	 * @param String
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectRejectInfo(String mappingSeq) throws Exception;

	/**
	 * 11번가 반품취소 대상조회 - 상세
	 * @param ParamMap
	 * @return List<Object>
	 * @throws Exception
	 */
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	
	
	public int refindOrderChangeTargetList(String paOrderNo) throws Exception;
	
	public int updatePaOrderm4preChangeCancle(Paorderm paorderm) throws Exception;
	
}