package com.cware.netshopping.panaver.exchange.process.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.exchange.process.PaNaverExchangeProcess;
import com.cware.netshopping.panaver.exchange.repository.PaNaverExchangeDAO;

@Service("panaver.exchange.paNaverExchangeProcess")
public class PaNaverExchangeProcessImpl extends AbstractProcess implements PaNaverExchangeProcess{
	
	@Autowired
	private PaNaverExchangeDAO paNaverExchangeDAO;
	
	/**
	 * 교환 수거 완료 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectExchangeReturnConfirmList() throws Exception {
		return paNaverExchangeDAO.selectExchangeReturnConfirmList();
	}

	@Override
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception {
		
		String resultCode = paorderm.getApiResultCode();
		String resultText = "";
		String resultDoFlag = "60";
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		switch(paorderm.getPaOrderGb()) {
			case "20":
				if(paorderm.getOutBefClaimGb().equals("1")) resultText = "출고전 반품 완료 성공";
				if(paorderm.getOutBefClaimGb().equals("2")) resultText = "출고 후 취소 완료 성공";
				if(paorderm.getOutBefClaimGb().equals("0") && paorderm.getPaDoFlag().equals("90")) resultText = "직권 취소 완료 성공";
				break;
			case "30":
				resultText = "반품 승인 완료";
				//반품승인완료 처리
				break;
			case "40":
			case "45":
				resultText = "교환 수거 완료 성공";
				//교환수거완료처리
				break;
			default:
				resultDoFlag = paorderm.getPaDoFlag();
				resultCode = "999999";
				resultText = "반품/교환 수거처리 실패";
				break;
		}
		
		paorderm.setApiResultCode(resultCode);
		paorderm.setApiResultMessage(resultText);
		paorderm.setPaDoFlag(resultDoFlag);
		paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmss"));
		
		if(paNaverExchangeDAO.updatePaOrdermResult(paorderm) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
		
		if(resultCode.equals("999999")) {
			return 0;
		}
		else {
			return 1;
		}
	}

	/**
	 * 교환 거부 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectRejectExchangeList() throws Exception {
		return paNaverExchangeDAO.selectRejectExchangeList();
	}

	@Override
	public int updatePaOrdermPreCancel(ParamMap paorderm) throws Exception {
		return paNaverExchangeDAO.updatePaOrdermPreCancel(paorderm);
	}

	/**
	 * 교환 상품 재발송 처리 대상 조회
	 */
	@Override
	public List<HashMap<String, Object>> selectRedeliveryExchangeList() throws Exception {
		return paNaverExchangeDAO.selectRedeliveryExchangeList();
	}

	@Override
	public int updateExchangePaOrdermResult(Paorderm paorderm) throws Exception {
		return paNaverExchangeDAO.updateExchangePaOrdermResult(paorderm);
	}

	/**
	 * 교환 보류 설정 대상 조회
	 */
	@Override
	public List<Object> selectReturnHoldList() throws Exception {
		return paNaverExchangeDAO.selectReturnHoldList();
	}

	@Override
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception {
		return paNaverExchangeDAO.updatePaOrdermHoldYn(paorderm);
	}

	/**
	 * 교환 보류 해제 대상 조회
	 */
	@Override
	public List<Object> selectReleaseReturnHoldList() throws Exception {
		return paNaverExchangeDAO.selectReleaseReturnHoldList();
	}
	
	@Override
	public int checkOrderChangeTargetList(String orderID) throws Exception {
		return paNaverExchangeDAO.checkOrderChangeTargetList(orderID);
	}
	
	@Override
	public int updatePaOrdermPreChangeCancel(Paorderm paorderm) throws Exception {
		return paNaverExchangeDAO.updatePaOrdermPreChangeCancel(paorderm);
	}
	
	@Override
	public List<HashMap<String, Object>> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverExchangeDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverExchangeDAO.selectChangeTargetDtList(paramMap);
	}

}
