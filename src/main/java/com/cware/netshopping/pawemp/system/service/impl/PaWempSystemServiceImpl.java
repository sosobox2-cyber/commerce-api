package com.cware.netshopping.pawemp.system.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.pawemp.system.service.PaWempSystemService;

@Service("pawemp.system.paWempSystemService")
public class PaWempSystemServiceImpl implements PaWempSystemService{
	@Resource(name = "common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Override
	public String getSysdatetimeToString() throws Exception{
		return systemProcess.getSysdatetimeToString();
	}
	
	@Override
	public HashMap<String, String> selectPaApiInfo(ParamMap paramMap) throws Exception {
		return systemProcess.selectPaApiInfo(paramMap);
	}
}	
