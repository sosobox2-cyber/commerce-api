package com.cware.netshopping.pafaple.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pafaple.process.PaFapleCounselProcess;
import com.cware.netshopping.pafaple.service.PaFapleCounselService;

@Service("pafaple.counsel.paFapleCounselService")
public class PaFapleCounselServiceImpl extends AbstractService implements PaFapleCounselService {
	
	@Autowired
	@Qualifier("pafaple.counsel.paFapleCounselProcess")
	PaFapleCounselProcess paFapleCounselProcess;
	
	@Override
	public ResponseMsg getCsNoticeList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paFapleCounselProcess.getCsNoticeList(fromDate, toDate, request);
	}
	
	@Override
	public ResponseMsg csNoticeProc(String paCounselNo, HttpServletRequest request) throws Exception{
		return paFapleCounselProcess.csNoticeProc(paCounselNo,request);
	}
	
	@Override
	public void savePaFapleQnaTransTx(PaqnamVO paQna) throws Exception{
		 paFapleCounselProcess.savePaFapleQnaTrans(paQna);
	}

	@Override
	public ResponseMsg getBbsList(String fromDate, String toDate, HttpServletRequest request)throws Exception {
		return paFapleCounselProcess.getBbsList(fromDate, toDate, request);
	}

	@Override
	public ResponseMsg goodsCounselProc(String paCounselNo, HttpServletRequest request) throws Exception {
		return paFapleCounselProcess.goodsCounselProc(paCounselNo, request);
	}
}
