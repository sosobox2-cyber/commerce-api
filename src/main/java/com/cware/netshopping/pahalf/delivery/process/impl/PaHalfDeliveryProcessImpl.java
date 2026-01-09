package com.cware.netshopping.pahalf.delivery.process.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.pahalf.delivery.process.PaHalfDeliveryProcess;
import com.cware.netshopping.pahalf.delivery.repository.PaHalfDeliveryDAO;

@Service("pahalf.delivery.paHalfDeliveryProcess")
public class PaHalfDeliveryProcessImpl extends AbstractService implements PaHalfDeliveryProcess{

	@Resource(name = "pahalf.delivery.paHalfDeliveryDAO")
	private PaHalfDeliveryDAO paHalfDeliveryDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	@Override
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception {
		return paHalfDeliveryDAO.selectSlipOutProcList();
	}

	@Override
	public void updateSlipOutProc(Map<String, Object> oc) throws Exception {	
		int executedRtn = 0;
		oc.put("modifyId", Constants.PA_HALF_PROC_ID);

		executedRtn = paHalfDeliveryDAO.updateSlipOutProc(oc);
		
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
	}

	@Override
	public void updateSlipOutProcFail(Map<String, Object> oc) throws Exception {
		int executedRtn = 0;
		oc.put("modifyId", Constants.PA_HALF_PROC_ID);

		executedRtn = paHalfDeliveryDAO.updateSlipOutProcFail(oc);
		
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
	}


}