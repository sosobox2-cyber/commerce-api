package com.cware.netshopping.palton.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.palton.counsel.process.PaLtonCounselProcess;
import com.cware.netshopping.palton.counsel.service.PaLtonCounselService;

@Service("palton.counsel.paLtonCounselService")
public class PaLtonCounselServiceImpl extends AbstractService implements PaLtonCounselService{

	@Resource(name = "palton.counsel.paLtonCounselProcess")
    private PaLtonCounselProcess paLtonCounselProcess;

	@Override
	public List<PaqnamVO> selectPaLtonAnsQna(ParamMap paramMap) throws Exception{
		return paLtonCounselProcess.selectPaLtonAnsQna(paramMap);
	}

	@Override
	public String savePaLtonQnaTransTx(PaqnamVO paQna) throws Exception {
		return paLtonCounselProcess.savePaLtonQnaTrans(paQna);
	}

	@Override
	public List<PaqnamVO> updateSellerContact(ParamMap paramMap) throws Exception {
		return paLtonCounselProcess.updateSellerContact(paramMap);
	}

	@Override
	public List<PaqnamVO> updateSellerInquiry(ParamMap paramMap) throws Exception {
		return paLtonCounselProcess.updateSellerInquiry(paramMap);
	}
	
}
