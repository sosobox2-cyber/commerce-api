package com.cware.netshopping.patdeal.process;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.patdeal.domain.OrderDetail;

public interface PaTdealClaimProcess {

	public List<PaTdealClaimListVO> getTdealClaimList(String claimStatus, String fromDate, String toDate,HttpServletRequest request) throws Exception;

	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;
	
	public OrderDetail orderInfoDetail(String paCode, String orderNo,HttpServletRequest request) throws Exception;
	
	public int saveTdealClaimList(String claimStatus, PaTdealClaimListVO paTdealClaimVo)  throws Exception;

	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;

	public ResponseMsg procTdealClaimCancelList(String claimStatus, HttpServletRequest request)  throws Exception;

	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<HashMap<String, Object>> selectTdealReturnCompleList() throws Exception;

	public ResponseMsg returnCompleProc(HashMap<String, Object> tdealReturnCompleItem, HttpServletRequest request) throws Exception;

	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	public int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception;

	public List<HashMap<String, Object>> selectTdealExchangeReturnDoFlag60List() throws Exception;

	public ResponseMsg exchangeDeliveryProc(HashMap<String, Object> tdealExchangeReturnDoFlag60Item,HttpServletRequest request)throws Exception;

	public ResponseMsg returnApprovalProc(HashMap<String, Object> tdealReturnCompleItem, HttpServletRequest request) throws Exception;

	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public String compareAddress(ParamMap paramMap) throws Exception;

}
