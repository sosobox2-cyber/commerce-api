
package com.cware.netshopping.pahalf.delivery.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;


@Service("pahalf.delivery.paHalfDeliveryDAO")
public class PaHalfDeliveryDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSlipOutProcList() throws Exception{
		return list("pahalf.delivery.selectSlipOutProcList", null);
	}

	public int updateSlipOutProc(Map<String, Object> oc) throws Exception{
		return update("pahalf.delivery.updateSlipOutProc", oc);
	}

	public int updateSlipOutProcFail(Map<String, Object> oc) throws Exception{
		return update("pahalf.delivery.updateSlipOutProcFail", oc);
	}

	
}