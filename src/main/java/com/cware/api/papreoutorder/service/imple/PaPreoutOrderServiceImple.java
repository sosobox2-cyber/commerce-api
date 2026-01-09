package com.cware.api.papreoutorder.service.imple;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cware.api.papreoutorder.process.PaPreoutOrderProcess;
import com.cware.api.papreoutorder.service.PaPreoutOrderService;
import com.cware.framework.core.basic.AbstractController;

@Service("papreoutorder.papreoutorderService")

public class PaPreoutOrderServiceImple extends AbstractController implements PaPreoutOrderService {
	
	@Resource(name = "papreoutorder.papreoutorderProcess")
	private PaPreoutOrderProcess papreoutorderProcess;

	@Override
	public void preoutOrderInput(Map<String, String> order, HttpServletRequest request) throws Exception {
		papreoutorderProcess.preoutOrderInput(order,request);
		
	}
	

}
