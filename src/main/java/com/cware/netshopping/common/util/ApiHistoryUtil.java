package com.cware.netshopping.common.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.PaRequestMap;

@Component
public class ApiHistoryUtil {
	
	@Autowired
	private SystemService beanSystemService;
	private static SystemService systemService;
	
	@SuppressWarnings("static-access")
	@PostConstruct
	private void initialize() {
		this.systemService = beanSystemService;
	}
	
	public static String insertPaRequestMap(PaRequestMap paRequestMap) throws Exception {
		return systemService.insertPaRequestMapTx(paRequestMap);
	}

}
