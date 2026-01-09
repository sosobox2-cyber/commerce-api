package com.cware.netshopping.paintp.claim.service;

import java.util.HashMap;
import java.util.List;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;

public interface PaIntpClaimService {
	
	/**
	 * 제휴 테이블에 저장
	 */
	public void createClaimListTx(PaIntpClaimVO claimList) throws Exception;

	public void createClaimDoneListTx(PaIntpClaimVO claimVO) throws Exception;
	
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	public String compareAddress(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;
	
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectReturnOkList() throws Exception;

	public List<Object> selectExchangeSlipList() throws Exception;	
	
	public void updatePaOrdermResult(Paorderm paorderm) throws Exception;

	public void createCancelListTx(PaIntpClaimVO claimVO) throws Exception;	
}