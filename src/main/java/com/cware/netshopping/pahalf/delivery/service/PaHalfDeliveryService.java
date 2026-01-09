package com.cware.netshopping.pahalf.delivery.service;

import java.util.List;
import java.util.Map;

public interface PaHalfDeliveryService {

	public List<Map<String, Object>> selectSlipOutProcList() throws Exception;

	public void updateSlipOutProc(Map<String, Object> oc) throws Exception;

	public void updateSlipOutProcFail(Map<String, Object> oc) throws Exception;
	 
}
