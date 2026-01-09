package com.cware.netshopping.pagmkt.cancel.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;


public interface PaGmktCancelService {
	
	public int saveCancelReqListTx(PaGmkCancel paGmktCancel) throws Exception;
	public List<Object> selectPaGmktOrdCancelList(ParamMap paramMap) throws Exception;
	public List<Object> selectPaGmktOrdCancelListForBo() throws Exception;
	public HashMap<String, Object> selectPaGmktOrdCancel(HashMap<String,Object> paramMap) throws Exception;
	public String saveCancelConfirmProcTx(HashMap<String, Object> cancelMap, PaGmktAbstractRest rest, ParamMap param) throws Exception;
	public int updatePaGmktOrderListProcFlag(HashMap<String, Object> cancelMap) throws Exception;
	public int saveCancelConfirmTx(PaGmktAbstractRest rest, HashMap<String, Object> cancelMap, ParamMap param) throws Exception;
	public int saveCancelWithdrawList(PaGmkCancel paGmktCancel) throws Exception;
	public int saveCancelCompleteListTx(PaGmkCancel paGmktCancel) throws Exception;
	public PaGmkCancel setPagmktCancelVo(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception;
	public PaGmkCancel setPagmktCancelVo2(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception;
	public List<HashMap<String, Object>> selectUnpaidPreOrderList(ParamMap paramMap) throws Exception;
	public List<Object> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception;
}