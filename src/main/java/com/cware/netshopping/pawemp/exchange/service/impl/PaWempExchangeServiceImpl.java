package com.cware.netshopping.pawemp.exchange.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimList;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pawemp.exchange.process.PaWempExchangeProcess;
import com.cware.netshopping.pawemp.exchange.service.PaWempExchangeService;

@Service("pawemp.exchange.paWempExchangeService")
public class PaWempExchangeServiceImpl extends AbstractService implements PaWempExchangeService{

	@Resource(name ="pawemp.exchange.paWempExchangeProcess")
	public PaWempExchangeProcess paWempExchangeProcess;
	
	@Override
	public String saveExchangeListTx(PaWempClaimList paWempClaim) throws Exception {
		return paWempExchangeProcess.saveExchangeList(paWempClaim);
	}

	@Override
	public List<Paorderm> selectOrderChangeTargetList() throws Exception {
		return paWempExchangeProcess.selectOrderChangeTargetList();
	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paWempExchangeProcess.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		return paWempExchangeProcess.updatePaOrdermChangeFlag(changeFlag, mappingSeq);
	}

	@Override
	public Paorderm selectPaOrdermInfo(String mappingSeq) throws Exception {
		return paWempExchangeProcess.selectPaOrdermInfo(mappingSeq);
	}
	
	@Override
	public String saveExchangeCancelListTx(PaWempClaimList paWempClaim) throws Exception {
		return paWempExchangeProcess.saveExchangeCancelList(paWempClaim);
	}
	
	@Override
	public List<Paorderm> selectChangeCancelTargetList() throws Exception {
		return paWempExchangeProcess.selectChangeCancelTargetList();
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paWempExchangeProcess.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectExchangePickupList() throws Exception {
		return paWempExchangeProcess.selectExchangePickupList();
	}
	
	@Override
	public int updatePickupProcTx(String mappingSeq) throws Exception {
		return paWempExchangeProcess.updatePickupProc(mappingSeq);
	}
	
	@Override
	public int updateProcFailTx(String paClaimNo, String apiResultMessage, String paOrderGb, String mappingSeq) throws Exception {
		return paWempExchangeProcess.updateProcFail(paClaimNo, apiResultMessage, paOrderGb, mappingSeq);
	}
	
	@Override
	public List<Object> selectPickupCompleteList() throws Exception {
		return paWempExchangeProcess.selectPickupCompleteList();
	}

	@Override
	public int updatePickupCompleteProcTx(HashMap<String, Object> receive) throws Exception {
		return paWempExchangeProcess.updatePickupCompleteProc(receive);
	}
	
	@Override
	public List<Object> selectExchangeSlipOutTargetList() throws Exception {
		return paWempExchangeProcess.selectExchangeSlipOutTargetList();
	}
	
	@Override
	public ParamMap slipOutProc(HashMap<String, Object> exchangeDeliveryVo, HashMap<String, String> apiInfo) throws Exception {
		return paWempExchangeProcess.slipOutProc(exchangeDeliveryVo, apiInfo);
	}
	
	@Override
	public int updateDeliveryProcTx(HashMap<String, Object> exchageDeliveryVo) throws Exception {
		return paWempExchangeProcess.updateDeliveryProc(exchageDeliveryVo);
	}
	
	@Override
	public ParamMap setClaimCompleteProc(HashMap<String, Object> exchangeCompleteVo, HashMap<String, String> apiInfo) throws Exception {
		return paWempExchangeProcess.setClaimCompleteProc(exchangeCompleteVo, apiInfo);
	}
	
	@Override
	public int updateExchangeEndTx(HashMap<String, Object> exchangeCompleteVo) throws Exception {
		return paWempExchangeProcess.updateExchangeEnd(exchangeCompleteVo);
	}
	
	@Override
	public List<HashMap<String, Object>> selectExchangeCompleteList() throws Exception{
		return paWempExchangeProcess.selectExchangeCompleteList();
	}
}
