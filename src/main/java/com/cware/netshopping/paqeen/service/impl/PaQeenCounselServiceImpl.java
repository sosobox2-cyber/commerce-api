package com.cware.netshopping.paqeen.service.impl;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.paqeen.message.CounselCommentsResoponseMsg;
import com.cware.netshopping.paqeen.message.CounselDetailResoponseMsg;
import com.cware.netshopping.paqeen.process.PaQeenCounselProcess;
import com.cware.netshopping.paqeen.service.PaQeenCounselService;

@Service("paqeen.counsel.paQeenCounselService")
public class PaQeenCounselServiceImpl extends AbstractService implements PaQeenCounselService{
	
	@Autowired
	@Qualifier("paqeen.counsel.paQeenCounselProcess")
	PaQeenCounselProcess paQeenCounselProcess;

	
	@Override
	public ResponseMsg getCxCounselList(String fromDate, String toDate, HttpServletRequest request) throws Exception{
		return paQeenCounselProcess.getCxCounselList(fromDate, toDate, request);
	}
	
	@Override
	public CounselDetailResoponseMsg getCxCounselDetail(String paCode, String inquiryId, HttpServletRequest request) throws Exception{
		return paQeenCounselProcess.getCxCounselDetail(paCode, inquiryId, request);
	}
	
	@Override
	public CounselCommentsResoponseMsg getCxCounselComments(String paCode, String inquiryId, HttpServletRequest request) throws Exception{
		return paQeenCounselProcess.getCxCounselComments(paCode, inquiryId, request);
	}
	
	@Override
	public ResponseMsg cxCounselProc(String paCounselNo, HttpServletRequest request) throws Exception{
		return paQeenCounselProcess.cxCounselProc(paCounselNo, request);
	}
	
	@Override
	public void savePaQeenQnaTransTx(PaqnamVO paQna) throws Exception{
		paQeenCounselProcess.savePaQeenQnaTrans(paQna);
	}
	
}
