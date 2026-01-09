package com.cware.netshopping.pahalf.delivery.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.pahalf.delivery.process.PaHalfDeliveryProcess;
import com.cware.netshopping.pahalf.delivery.service.PaHalfDeliveryService;

@Service("pahalf.delivery.paHalfDeliveryService")
public class PaHalfDeliveryServiceImpl extends AbstractService implements PaHalfDeliveryService{
	
	@Resource(name = "pahalf.delivery.paHalfDeliveryProcess")
	private PaHalfDeliveryProcess paHalfDeliveryProcess;

	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paHalfDeliveryProcess.selectSlipOutProcList();
	}

	@Override
	public void updateSlipOutProc(Map<String, Object> oc) throws Exception {
		paHalfDeliveryProcess.updateSlipOutProc(oc);
	}

	@Override
	public void updateSlipOutProcFail(Map<String, Object> oc) throws Exception {
		paHalfDeliveryProcess.updateSlipOutProcFail(oc);
	}

	 

}
