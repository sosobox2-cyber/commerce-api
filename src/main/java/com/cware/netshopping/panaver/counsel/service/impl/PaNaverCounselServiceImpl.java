package com.cware.netshopping.panaver.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.panaver.counsel.process.PaNaverCounselProcess;
import com.cware.netshopping.panaver.counsel.service.PaNaverCounselService;

@Service("panaver.counsel.paNaverCounselService")
public class PaNaverCounselServiceImpl extends AbstractService implements PaNaverCounselService{

	@Resource(name = "panaver.counsel.paNaverCounselProcess")
	private PaNaverCounselProcess paNaverCounselProcess;

	@Override
	public List<PaqnamVO> selectPaNaverAnsQna() throws Exception {
		return paNaverCounselProcess.selectPaNaverAnsQna();
	}

	@Override
	public List<PaqnamVO> selectPaNaverCustAnsQna() throws Exception {
		return paNaverCounselProcess.selectPaNaverCustAnsQna();
	}
}
