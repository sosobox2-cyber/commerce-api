package com.cware.netshopping.pawemp.system.service;

import java.util.HashMap;
import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.PaWempClaimList;

public interface PaWempSystemService {
	
	public String getSysdatetimeToString() throws Exception;
	
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception;
}
