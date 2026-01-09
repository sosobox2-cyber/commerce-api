package com.cware.netshopping.panaver.counsel.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.panaver.counsel.process.PaNaverCounselProcess;
import com.cware.netshopping.panaver.counsel.repository.PaNaverCounselDAO;

@Service("panaver.counsel.paNaverCounselProcess")
public class PaNaverCounselProcessImpl extends AbstractService implements PaNaverCounselProcess{

	@Resource(name = "panaver.counsel.paNaverCounselDAO")
	private PaNaverCounselDAO paNaverCounselDAO;

	@Override
	public List<PaqnamVO> selectPaNaverAnsQna() throws Exception {
		return paNaverCounselDAO.selectPaNaverAnsQna();
	}

	@Override
	public List<PaqnamVO> selectPaNaverCustAnsQna() throws Exception {
		return paNaverCounselDAO.selectPaNaverCustAnsQna();
	}
}
