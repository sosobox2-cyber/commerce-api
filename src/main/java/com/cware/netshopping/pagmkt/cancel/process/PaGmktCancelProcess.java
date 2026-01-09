package com.cware.netshopping.pagmkt.cancel.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaGmkCancel;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

public interface PaGmktCancelProcess {

	public List<Object> selectPaGmktOrdCancelList(ParamMap paramMap) throws Exception;
	public List<Object> selectPaGmktOrdCancelListForBo() throws Exception;
	public HashMap<String, Object> selectPaGmktOrdCancel(HashMap<String,Object> paramMap) throws Exception;
	public String saveCancelConfirmProc(HashMap<String, Object> cancelMap, PaGmktAbstractRest rest, ParamMap param) throws Exception;
	public int updatePaGmktOrderListProcFlag(HashMap<String, Object> cancelMap) throws Exception;
	public int saveCancelConfirm(PaGmktAbstractRest rest , HashMap<String, Object> cancelMap, ParamMap param) throws Exception;
	public int saveCancelWithdrawList(PaGmkCancel paGmktCancel) throws Exception;
	public int saveCancelCompleteList(PaGmkCancel paGmktCancel) throws Exception;
	public int saveCancelReqList(PaGmkCancel paGmktCancel) throws Exception;
	public PaGmkCancel setPagmktCancelVo(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception;
	public PaGmkCancel setPagmktCancelVo2(HashMap<String, Object> cancelMap, ParamMap paramMap) throws Exception;
	public List<HashMap<String, Object>> selectUnpaidPreOrderList(ParamMap paramMap) throws Exception;
	public List<Object> selectPaMobileOrderAutoCancelList(ParamMap paramMap) throws Exception;

}