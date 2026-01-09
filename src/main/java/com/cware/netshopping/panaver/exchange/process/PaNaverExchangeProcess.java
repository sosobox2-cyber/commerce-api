package com.cware.netshopping.panaver.exchange.process;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaNaverExchangeProcess {

	public List<HashMap<String, Object>> selectExchangeReturnConfirmList() throws Exception;

	public int updatePaOrdermResult(Paorderm paorderm) throws Exception;

	public List<HashMap<String, Object>> selectRejectExchangeList()throws Exception;

	public int updatePaOrdermPreCancel(ParamMap paorderm) throws Exception;

	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception;

	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception;

	public List<Object> selectReturnHoldList() throws Exception;

	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception;

	public List<Object> selectReleaseReturnHoldList() throws Exception;

	public int checkOrderChangeTargetList(String orderID) throws Exception;

	public int updatePaOrdermPreChangeCancel(Paorderm paorderm) throws Exception;

	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;

	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception;

}
