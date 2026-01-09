package com.cware.netshopping.palton.delivery.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderpromoVO;
import com.cware.netshopping.domain.PaLtonOrderListVO;

public interface PaLtonDeliveryProcess {

	public String saveLtonOrderList(PaLtonOrderListVO vo) throws Exception;

	public List<Map<String, Object>> selectDeliveryReadyList() throws Exception;

	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception;

	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;

	public List<Map<String, Object>> selectOrderInputTargetDtList(ParamMap paramMap) throws Exception;

	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;

	public List<Map<String, Object>> selectSlipOutProcList() throws Exception;

	public List<Map<String, Object>> selectDeliveryCompleteList() throws Exception;

	public int updatePreCanYn(Map<String, Object> map) throws Exception;

	public OrderpromoVO selectOrderPromo(Map<String, Object> map) throws Exception;

	public List<Map<String, Object>> selectExchangeHoldList() throws Exception;

	public int saveHoldInfo(ParamMap paramMap) throws Exception;

	public List<Map<String, Object>> selectRetrievalExceptList() throws Exception;

	public List<Map<String, Object>> selectSlipUpdateProcList() throws Exception;
	
	public List<Map<String, Object>> selectDeliveryDelayProcList() throws Exception;
	
	public int updatePaLtonSndAgrdDttm(Map<String, Object> map) throws Exception;

}
