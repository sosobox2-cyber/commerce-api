package com.cware.netshopping.passg.delivery.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaSsgOrderListVO;

public interface PaSsgDeliveryProcess {

	public String saveSsgOrderList(PaSsgOrderListVO vo) throws Exception;
	
	public String saveSsgChangeOrderList(PaSsgOrderListVO vo) throws Exception;
	
	public int updatePreCanYn(Map<String, Object> map) throws Exception;

	public List<Map<String, String>> selectOrderInputTargetList(int limitCount) throws Exception;

	public List<Map<String, Object>> selectOrderInputTargetDtList(Map<String, String> order) throws Exception;

	public HashMap<String, Object> selectRefusalInfo(String mappingSeq) throws Exception;

	public int updatePaOrderMDoFlag(ParamMap paramMap) throws Exception;

	public List<Map<String, Object>> selectPartialSlipList() throws Exception;
	
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception;

	public List<Map<String, Object>> selectReleaseOrderList() throws Exception;

	public List<Map<String, Object>> selectOrderCompleteList() throws Exception;
	
	public List<Map<String, Object>> selectPaSsgChangeConfirmList() throws Exception;
	
	public List<Map<String, Object>> selectDelayOrderList() throws Exception;

	public int updatePaSsgWhinExpcDt(ParamMap paramMap) throws Exception;
	
	public int updatePartialFlag(ParamMap paramMap) throws Exception;

	public int updatePartialShhpNo(Map<String, Object> partialDataMap) throws Exception;
	
	public List<Map<String, Object>> selectPartialDeliveryList() throws Exception;
}
