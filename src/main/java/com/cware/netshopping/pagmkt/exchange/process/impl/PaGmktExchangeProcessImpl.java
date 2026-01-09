package com.cware.netshopping.pagmkt.exchange.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.ParamMap;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.PaGmktPaordermVO;
import com.cware.netshopping.domain.PaGmktSlipmVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.claim.repository.PaGmktClaimDAO;
import com.cware.netshopping.pagmkt.exchange.process.PaGmktExchangeProcess;
import com.cware.netshopping.pagmkt.exchange.repository.PaGmktExchangeDAO;


@Service("pagmkt.exchange.PaGmktExchangeProcess")
public class PaGmktExchangeProcessImpl extends AbstractService implements PaGmktExchangeProcess{
	
	@Resource(name = "pagmkt.claim.PaGmktClaimDAO")
	private PaGmktClaimDAO paGmktClaimDAO;
	
	@Resource(name = "pagmkt.exchange.PaGmktExchangeDAO")
	private PaGmktExchangeDAO paGmktExchangeDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	
	@Override
	public List<Object> selectOrderChangeTargetList(ParamMap paramMap) throws Exception{
		return paGmktExchangeDAO.selectOrderChangeTargetList(paramMap);
	}	
	
	@Override
	public List<Object> selectChangeCancelTargetList(ParamMap paramMap) throws Exception {
		return paGmktExchangeDAO.selectChangeCancelTargetList(paramMap);
	}	

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktExchangeDAO.selectOrderChangeTargetDtList(paramMap);
	}	
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paGmktExchangeDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<PaGmktSlipmVO> selectExchangeSlipOutTargetList(ParamMap paramMap) throws Exception {
	    return paGmktExchangeDAO.selectExchangeSlipOutTargetList(paramMap);
	}
	
	@Override
	public int checkPaGmktExchangeExchange(HashMap<String, Object> hashMap) throws Exception{
		return paGmktExchangeDAO.checkPaGmktExchangeExchange(hashMap);
	}
	
	@Override
	public List<PaGmktPaordermVO> selectGmktExchangeCompleteList(ParamMap paramMap) throws Exception {
	    return paGmktExchangeDAO.selectGmktExchangeCompleteList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectExchangePickup50List(ParamMap paramMap) throws Exception {
		return paGmktExchangeDAO.selectExchangePickup50List(paramMap);
	}

	@Override
	public List<Map<String, String>> selectExchangePickup60List(ParamMap paramMap) throws Exception {
		return paGmktExchangeDAO.selectExchangePickup60List(paramMap);
	}

	@Override
	public List<Object> selectExchangeReturnConfirmList(ParamMap paramMap) throws Exception{
		return paGmktExchangeDAO.selectExchangeReturnConfirmList(paramMap);
	}
	@Override
	public String updatePaOrdermResult(Paorderm paorderm) throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	    
   	   	executedRtn = paGmktExchangeDAO.updatePaOrdermResult(paorderm);
   		if (executedRtn < 0) {
			log.info("tpaorderm update fail");
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
    	return rtnMsg;
	}	
	
	@Override
	public String updateGmktPaOrderMForReserve(PaGmktPaordermVO paGmktPaordermVO)throws Exception {
	    String rtnMsg = Constants.SAVE_SUCCESS;
    	int executedRtn = 0;
    	executedRtn = paGmktExchangeDAO.updateGmktPaOrderMForReserve(paGmktPaordermVO);
    		
		if (executedRtn < 0) {
			log.error("ERROR - tpagmktclaimlist update fail");
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST UPDATE" });
		}
		return rtnMsg;
	}

	@Override
	public String selectOriginalExchangeMappingSeq(String mappingSeq) throws Exception {
		return paGmktExchangeDAO.selectOriginalExchangeMappingSeq(mappingSeq);
	}

	@Override
	public int updateExchangeStatue(String mappingSeq) throws Exception {
		return paGmktExchangeDAO.updateExchangeStatue(mappingSeq);
	}	  
}