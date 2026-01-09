package com.cware.netshopping.paqeen.process;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.ExchangeClaimList;
import com.cware.netshopping.paqeen.domain.Return;

public interface PaQeenClaimProcess {

	ResponseMsg getExchangeList(String fromDate, String toDate, HttpServletRequest request, String claimStatus) throws Exception;

	List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	String compareAddress(ParamMap paramMap) throws Exception;

	int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception;

	ResponseMsg getExchangeRecall(HttpServletRequest request, String claimGb) throws Exception;

	ResponseMsg getExchangeDelivery(HttpServletRequest request) throws Exception;

	List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate,HttpServletRequest request) throws Exception;
	
	public int savePaQeenReturn(Return returnItem, String paCode)  throws Exception;

	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap)  throws Exception;

	public ResponseMsg getReturnRecall(HttpServletRequest request, String claimGb) throws Exception;

	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;

	public ResponseMsg getReturnRecall20(HttpServletRequest request) throws Exception;

	int savePaQeenExchange(ExchangeClaimList claim, ParamMap paramMap2) throws Exception;
}
