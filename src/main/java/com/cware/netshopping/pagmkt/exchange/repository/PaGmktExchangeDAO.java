package com.cware.netshopping.pagmkt.exchange.repository;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PaGmktPaordermVO;
import com.cware.netshopping.domain.PaGmktSlipmVO;
import com.cware.netshopping.domain.model.Paorderm;

@Service("pagmkt.exchange.PaGmktExchangeDAO")
public class PaGmktExchangeDAO extends AbstractPaDAO{
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.exchange.selectOrderChangeTargetList", paramMap.get());
	}	
	
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetList(ParamMap paramMap) throws Exception{
		return list("pagmkt.exchange.selectChangeCancelTargetList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception{
		return list("pagmkt.exchange.selectOrderChangeTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception{
		return list("pagmkt.exchange.selectChangeCancelTargetDtList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGmktSlipmVO> selectExchangeSlipOutTargetList(ParamMap paramMap) throws Exception{
	    return (List<PaGmktSlipmVO>)list("pagmkt.exchange.selectExchangeSlipOutTargetList", paramMap.get());
	}
	
	public int checkPaGmktExchangeExchange(HashMap<String, Object> hashMap) throws Exception{
		return (Integer)selectByPk("pagmkt.exchange.checkPaGmktExchangeExchange", hashMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaGmktPaordermVO> selectGmktExchangeCompleteList(ParamMap paramMap) throws Exception{
	    	return (List<PaGmktPaordermVO>)list("pagmkt.exchange.selectGmktExchangeCompleteList", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectExchangePickup50List(ParamMap paramMap) throws Exception{
    	return (List<Map<String, String>>)list("pagmkt.exchange.selectExchangePickup50List", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectExchangePickup60List(ParamMap paramMap) throws Exception{
    	return (List<Map<String, String>>)list("pagmkt.exchange.selectExchangePickup60List", paramMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> selectExchangeReturnConfirmList(ParamMap paramMap) throws Exception{
		return list("pagmkt.exchange.selectExchangeReturnConfirmList", paramMap.get());
	}
	
	public int updatePaOrdermResult(Paorderm paorderm) throws Exception{
	    return update("pagmkt.exchange.updatePaOrdermResult", paorderm);
	}
	
	public int updateGmktPaOrderMForReserve(PaGmktPaordermVO paGmktPaordermVO) throws Exception{
	    return update("pagmkt.exchange.updateGmktPaOrderMForReserve", paGmktPaordermVO);
	}

	public String selectOriginalExchangeMappingSeq(String mappingSeq) throws Exception{
		return (String)selectByPk("pagmkt.exchange.selectOriginalExchangeMappingSeq", mappingSeq);
	}

	public int updateExchangeStatue(String mappingSeq) throws Exception{
	    return update("pagmkt.exchange.updateExchangeStatue", mappingSeq);
	}

}