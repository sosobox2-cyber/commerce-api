package com.cware.netshopping.panaver.exchange.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;

@Repository("panaver.exchange.paNaverExchangeDAO")
public class PaNaverExchangeDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectExchangeReturnConfirmList() throws Exception{
		return list("panaver.exchange.selectExchangeReturnConfirmList", null);
	}

	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.exchange.updatePaOrdermResult", paorderm);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectRejectExchangeList() throws Exception{
		return list("panaver.exchange.selectRejectExchangeList", null);
	}

	public int updatePaOrdermPreCancel(ParamMap paorderm) throws Exception{
		return update("panaver.exchange.updatePaOrdermPreCancel", paorderm.get());
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception{
		return list("panaver.exchange.selectRedeliveryExchangeList", null);
	}

	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception{
		return update("panaver.exchange.updateExchangePaOrdermResult", paorderm);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectReturnHoldList() throws Exception{
		return list("panaver.exchange.selectReturnHoldList", null);
	}

	/**
	 * 교환 보류 - TPAORDERM 보류 여부 결과 UPDATE
	 * @param paorderm
	 * @return
	 */
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception{
		return update("panaver.exchange.updatePaOrdermHoldYn", paorderm);
	}

	@SuppressWarnings("unchecked")
	public List<Object> selectReleaseReturnHoldList() throws Exception{
		return list("panaver.exchange.selectReleaseReturnHoldList", null);
	}
	
	public int checkOrderChangeTargetList(String orderID) throws Exception {
		return (int) selectByPk("panaver.exchange.checkOrderChangeTargetList", orderID);
	}
	
	public int updatePaOrdermPreChangeCancel(Paorderm paorderm) throws Exception {
		return update("panaver.exchange.updatePaOrdermPreChangeCancel", paorderm);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, Object>>) list("panaver.exchange.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.exchange.selectChangeTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectGoodsdtInfo(String productID) throws Exception {
		return (List<HashMap<String, String>>) list("panaver.exchange.selectGoodsdtInfo", productID);
	}
}
