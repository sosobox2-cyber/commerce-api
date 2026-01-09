package com.cware.netshopping.pagmkt.exchange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaGmktPaordermVO;
import com.cware.netshopping.domain.PaGmktSlipmVO;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pagmkt.exchange.process.PaGmktExchangeProcess;
import com.cware.netshopping.pagmkt.exchange.service.PaGmktExchangeService;


@Service("pagmkt.exchange.PaGmktExchangeService")
public class PaGmktExchangeServiceImpl  extends AbstractService implements PaGmktExchangeService {
	
	@Resource(name = "pagmkt.exchange.PaGmktExchangeProcess")
    private PaGmktExchangeProcess paGmktExchangeProcess;

	@Override
	public List<Object> selectOrderChangeTargetList(ParamMap paramMap) throws Exception{
		return paGmktExchangeProcess.selectOrderChangeTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectChangeCancelTargetList(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectChangeCancelTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectOrderChangeTargetDtList(paramMap);
	}
		
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<PaGmktSlipmVO> selectExchangeSlipOutTargetList(ParamMap paramMap) throws Exception {
	    return paGmktExchangeProcess.selectExchangeSlipOutTargetList(paramMap);
	}
	
	@Override
	public String updatePaOrdermResultTx(Paorderm paorderm) throws Exception {
	    return paGmktExchangeProcess.updatePaOrdermResult(paorderm);
	}
	
	@Override
	public int checkPaGmktExchangeExchange(HashMap<String, Object> hashMap) throws Exception{
		return paGmktExchangeProcess.checkPaGmktExchangeExchange(hashMap);
	}
	
	@Override
	public String updateGmktPaOrderMForReserveTx(PaGmktPaordermVO paGmktPaordermVO) throws Exception {
	    return paGmktExchangeProcess.updateGmktPaOrderMForReserve(paGmktPaordermVO);
	}
	
	@Override
	public List<PaGmktPaordermVO> selectGmktExchangeCompleteList(ParamMap paramMap) throws Exception {
	    return paGmktExchangeProcess.selectGmktExchangeCompleteList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectExchangePickup60List(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectExchangePickup60List(paramMap);
	}

	@Override
	public List<Map<String, String>> selectExchangePickup50List(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectExchangePickup50List(paramMap);
	}

	@Override
	public List<Object> selectExchangeReturnConfirmList(ParamMap paramMap) throws Exception {
		return paGmktExchangeProcess.selectExchangeReturnConfirmList(paramMap);
	}

	@Override
	public String selectOriginalExchangeMappingSeq(String mappingSeq) throws Exception{
		return paGmktExchangeProcess.selectOriginalExchangeMappingSeq(mappingSeq);
	}

	@Override
	public int updateExchangeStatue(String mappingSeq) throws Exception {
		return paGmktExchangeProcess.updateExchangeStatue(mappingSeq);
	}
	
}