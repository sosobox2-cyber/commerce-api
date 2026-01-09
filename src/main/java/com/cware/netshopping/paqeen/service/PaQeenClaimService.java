package com.cware.netshopping.paqeen.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.paqeen.domain.ExchangeClaimList;
import com.cware.netshopping.paqeen.domain.Return;

public interface PaQeenClaimService {

	public ResponseMsg getExchangeList(String fromDate, String toDate, HttpServletRequest request, String claimStatus) throws Exception;

	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;

	public String compareAddress(ParamMap paramMap) throws Exception;

	public int updatePaOrdermChangeFlag(String string, String mappingSeq) throws Exception;

	public ResponseMsg getExchangeRecall(HttpServletRequest request, String claimGb) throws Exception;

	public ResponseMsg getExchangeDelivery(HttpServletRequest request) throws Exception;

	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public ResponseMsg getReturnList(String processFlag, String fromDate, String toDate, HttpServletRequest request)  throws Exception;

	public int savePaQeenReturnTx(Return returnItem, String paCode) throws Exception;

	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception;

	public ResponseMsg getReturnRecall(HttpServletRequest request, String string) throws Exception;

	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception;

	public ResponseMsg getReturnRecall20(HttpServletRequest request) throws Exception;

	public int savePaQeenExchangeTx(ExchangeClaimList claim, ParamMap paramMap2) throws Exception;
}
