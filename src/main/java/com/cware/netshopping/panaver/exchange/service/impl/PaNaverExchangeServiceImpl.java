package com.cware.netshopping.panaver.exchange.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.exchange.process.PaNaverExchangeProcess;
import com.cware.netshopping.panaver.exchange.service.PaNaverExchangeService;

@Service("panaver.exchange.paNaverExchangeService")
public class PaNaverExchangeServiceImpl extends AbstractService implements PaNaverExchangeService{
	
	@Autowired
	PaNaverExchangeProcess paNaverExchangeProcess;

	/**
	 * 교환 수거 완료 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectExchangeReturnConfirmList() throws Exception {
		return paNaverExchangeProcess.selectExchangeReturnConfirmList();
	}

	@Override
	public int updatePaOrdermResultTx(Paorderm paorderm) throws Exception {
		return paNaverExchangeProcess.updatePaOrdermResult(paorderm);
	}

	/**
	 * 교환 거부 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectRejectExchangeList() throws Exception {
		return paNaverExchangeProcess.selectRejectExchangeList();
	}

	@Override
	public int updatePaOrdermPreCancelTx(ParamMap paorderm) throws Exception {
		return paNaverExchangeProcess.updatePaOrdermPreCancel(paorderm);
	}

	/**
	 * 교환 상품 재발송 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception {
		return paNaverExchangeProcess.selectRedeliveryExchangeList();
	}

	@Override
	public int updateExchangePaOrdermResultTx(Paorderm paorderm) throws Exception {
		return paNaverExchangeProcess.updateExchangePaOrdermResult(paorderm);
	}

	/**
	 * 교환 보류 설정 대상 조회
	 */
	@Override
	public List<Object> selectReturnHoldList() throws Exception {
		return paNaverExchangeProcess.selectReturnHoldList();
	}

	@Override
	public int updatePaOrdermHoldYnTx(Paorderm paorderm) throws Exception {
		return paNaverExchangeProcess.updatePaOrdermHoldYn(paorderm);
	}

	/**
	 * 교환 보류 해제 대상 조회
	 */
	@Override
	public List<Object> selectReleaseReturnHoldList() throws Exception {
		return paNaverExchangeProcess.selectReleaseReturnHoldList();
	}
	
	@Override
	public int checkOrderChangeTargetList(String orderID) throws Exception {
		return paNaverExchangeProcess.checkOrderChangeTargetList(orderID);
	}
	
	@Override
	public int updatePaOrdermPreChangeCancelTx(Paorderm paorderm) throws Exception {
		return paNaverExchangeProcess.updatePaOrdermPreChangeCancel(paorderm);
	}
	
	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverExchangeProcess.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverExchangeProcess.selectChangeTargetDtList(paramMap);
	}

}
