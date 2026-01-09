package com.cware.netshopping.paintp.claim.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;

public interface PaIntpClaimProcess {

	/**
	 * 반품신청목록조회(반품 요청 목록조회) IF_PAINTPAPI_03_011 : 저장.
	 * @param PaIntpClaimlistVO
	 * @param paCode 제휴사코드
	 * @return String
	 * @throws Exception
	 */
	public void saveClaimList(PaIntpClaimVO claimVo) throws Exception;

	public void saveClaimDoneList(PaIntpClaimVO claimVo) throws Exception;
	
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	public String compareAddress(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectReturnOkList() throws Exception;

	public void updatePaOrdermResult(Paorderm paorderm) throws Exception;

	public List<Object> selectExchangeSlipList() throws Exception;

	public void createCancelList(PaIntpClaimVO claimVO) throws Exception;
}