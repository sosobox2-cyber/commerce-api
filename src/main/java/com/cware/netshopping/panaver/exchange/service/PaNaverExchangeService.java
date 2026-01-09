package com.cware.netshopping.panaver.exchange.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaNaverExchangeService {

	public List<HashMap<String, Object>> selectExchangeReturnConfirmList() throws Exception;

	public int updatePaOrdermResultTx(Paorderm paorderm) throws Exception;

	public List<HashMap<String, Object>> selectRejectExchangeList() throws Exception;

	public int updatePaOrdermPreCancelTx(ParamMap paorderm) throws Exception;

	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception;

	public int updateExchangePaOrdermResultTx(Paorderm paorderm) throws Exception;

	public List<Object> selectReturnHoldList() throws Exception;

	public int updatePaOrdermHoldYnTx(Paorderm paorderm) throws Exception;

	public List<Object> selectReleaseReturnHoldList() throws Exception;

	public int checkOrderChangeTargetList(String orderID) throws Exception;

	public int updatePaOrdermPreChangeCancelTx(Paorderm paorderm) throws Exception;

	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception;

}
