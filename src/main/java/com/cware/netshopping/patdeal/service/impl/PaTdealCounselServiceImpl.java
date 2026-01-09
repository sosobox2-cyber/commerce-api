package com.cware.netshopping.patdeal.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.patdeal.process.PaTdealCounselProcess;
import com.cware.netshopping.patdeal.service.PaTdealCounselService;

@Service("patdeal.counsel.paTdealCounselService")
public class PaTdealCounselServiceImpl extends AbstractService implements PaTdealCounselService{
	
	@Autowired
	@Qualifier("patdeal.counsel.paTdealCounselProcess")
	PaTdealCounselProcess paTdealCounselProcess;

	@Override
	public ResponseMsg getGoodsCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paTdealCounselProcess.getGoodsCounselList(fromDate, toDate, request);
	}
	
	@Override
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception{
		return paTdealCounselProcess.goodsCounselProc(paCounselNo, request);
	}
	
	@Override
	public void savePaTdealQnaTransTx(PaqnamVO paQna) throws Exception{
		 paTdealCounselProcess.savePaTdealQnaTrans(paQna);
	}

	@Override
	public ResponseMsg getTaskMessagesList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paTdealCounselProcess.getTaskMessagesList(fromDate, toDate, request);
	}
	
	@Override
	public ResponseMsg getTaskMessagesProc(String paCounselNo, HttpServletRequest request) throws Exception{
		return paTdealCounselProcess.getTaskMessagesProc(paCounselNo, request);
	}

}
