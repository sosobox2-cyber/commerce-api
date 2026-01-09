package com.cware.netshopping.pagmkt.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pagmkt.counsel.process.PaGmktCounselProcess;
import com.cware.netshopping.pagmkt.counsel.service.PaGmktCounselService;

@Service("pagmkt.counsel.paGmktCounselService")
public class PaGmktCounselServiceImpl  extends AbstractService implements PaGmktCounselService {

    @Resource(name = "pagmkt.counsel.paGmktCounselProcess")
    private PaGmktCounselProcess paGmktCounselProcess;
    
    @Override
	public List<PaqnamVO> selectPaGmktAnsQna(ParamMap paramMap) throws Exception{
		return paGmktCounselProcess.selectPaGmktAnsQna(paramMap);
	}
    
    @Override
	public String savePaGmktQnaTransTx(PaqnamVO paQna) throws Exception{
		return paGmktCounselProcess.savePaGmktQnaTrans(paQna);
	}
}
