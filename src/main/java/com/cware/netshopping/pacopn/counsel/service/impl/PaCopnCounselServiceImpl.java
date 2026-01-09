package com.cware.netshopping.pacopn.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pacopn.counsel.process.PaCopnCounselProcess;
import com.cware.netshopping.pacopn.counsel.service.PaCopnCounselService;

@Service("pacopn.counsel.paCopnCounselService")
public class PaCopnCounselServiceImpl extends AbstractService implements PaCopnCounselService{
	
	@Resource(name = "pacopn.counsel.paCopnCounselProcess")
	private PaCopnCounselProcess paCopnCounselProcess;
		
	@Override
	public String savePaCopnCounselReplyTx(PaqnamVO qnaReply) throws Exception{
		return paCopnCounselProcess.savePaCopnCounselReply(qnaReply);
	}

	@Override
	public List<PaqnamVO> selectPaCopnCounselReply(ParamMap paramMap) throws Exception {
		return paCopnCounselProcess.selectPaCopnCounselReply(paramMap);
	}
}