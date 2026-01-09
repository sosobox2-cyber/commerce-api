package com.cware.netshopping.pakakao.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pakakao.counsel.process.PaKakaoCounselProcess;
import com.cware.netshopping.pakakao.counsel.service.PaKakaoCounselService;

@Service("pakakao.counsel.paKakaoCounselService")
public class PaKakaoCounselServiceImpl extends AbstractService implements PaKakaoCounselService{

	@Resource(name = "pakakao.counsel.paKakaoCounselProcess")
    private PaKakaoCounselProcess paKakaoCounselProcess;

	@Override
	public void savePaQnaTx(List<PaqnamVO> paQnaList) throws Exception {
		paKakaoCounselProcess.savePaQna(paQnaList);
	}
	
	@Override
	public void saveCustCounselTx() throws Exception {
		paKakaoCounselProcess.saveCustCounsel();
	}
	
	@Override
	public List<PaqnamVO> selectPaAnserQnaList(ParamMap paramMap) throws Exception {
		return paKakaoCounselProcess.selectPaAnserQnaList(paramMap);
	}
	
	@Override
	public void savePaQnaTransTx(PaqnamVO paQna) throws Exception {
		paKakaoCounselProcess.savePaQnaTrans(paQna);
	}

}
