package com.cware.netshopping.pagmkt.exchange.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaGmktPaordermVO;
import com.cware.netshopping.domain.PaGmktSlipmVO;
import com.cware.netshopping.domain.model.Paorderm;

public interface PaGmktExchangeProcess {

	public List<Object> selectOrderChangeTargetList(ParamMap paramMap) throws Exception;
	public List<Object> selectChangeCancelTargetList(ParamMap paramMap) throws Exception;
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception;
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception;
	public List<PaGmktSlipmVO> selectExchangeSlipOutTargetList(ParamMap paramMap) throws Exception;
	public String updatePaOrdermResult(Paorderm paorderm) throws Exception;
	public int checkPaGmktExchangeExchange(HashMap<String, Object> hashMap) throws Exception;
	public String updateGmktPaOrderMForReserve(PaGmktPaordermVO paGmktPaordermVO) throws Exception;
	public List<PaGmktPaordermVO> selectGmktExchangeCompleteList(ParamMap paramMap) throws Exception;
	public List<Map<String, String>> selectExchangePickup50List(ParamMap paramMap) throws Exception;
	public List<Map<String, String>> selectExchangePickup60List(ParamMap paramMap) throws Exception;
	public List<Object> selectExchangeReturnConfirmList(ParamMap paramMap) throws Exception;
	public String selectOriginalExchangeMappingSeq(String mappingSeq) throws Exception;
	public int updateExchangeStatue(String mappingSeq) throws Exception;
}