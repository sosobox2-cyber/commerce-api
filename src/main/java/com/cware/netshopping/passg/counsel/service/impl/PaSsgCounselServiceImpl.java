package com.cware.netshopping.passg.counsel.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.passg.counsel.process.PaSsgCounselProcess;
import com.cware.netshopping.passg.counsel.service.PaSsgCounselService;

@Service("passg.counsel.paSsgCounselService")
public class PaSsgCounselServiceImpl extends AbstractService implements PaSsgCounselService {

	@Resource(name = "passg.counsel.paSsgCounselProcess")
    private PaSsgCounselProcess paSsgCounselProcess;

	@Override
	public String savePaQnaTx(PaqnamVO paqnamVo) throws Exception {
		return paSsgCounselProcess.savePaQna(paqnamVo);
	}
	
	@Override
	public List<PaqnamVO> selectPaSsgQna() throws Exception{
    	return paSsgCounselProcess.selectPaSsgQna();
    }
	
	@Override
	public String saveCustCounselTx(PaqnamVO paqnam) throws Exception{
		return paSsgCounselProcess.saveCustCounsel(paqnam);
	}
	
	@Override
	public List<PaqnamVO> selectPaSsgAnsQna(ParamMap paramMap) throws Exception {
		return paSsgCounselProcess.selectPaSsgAnsQna(paramMap);
	}
	
	@Override
	public String savePaSsgQnaTransTx(PaqnamVO paQna) throws Exception {
		return paSsgCounselProcess.savePaSsgQnaTrans(paQna);
	}

	@Override
	public HashMap<String, Object> selectPaSsgChangeCounselDt(OrderClaimVO claimVO) throws Exception {
		return paSsgCounselProcess.selectPaSsgChangeCounselDt(claimVO);
	}

	@Override
	public int selectPaCounselNoCheck(ParamMap paramMap) throws Exception {
		return paSsgCounselProcess.selectPaCounselNoCheck(paramMap);
	}

	@Override
	public List<PaqnamVO> selectPaSsgNote() throws Exception {
		return paSsgCounselProcess.selectPaSsgNote();
	}

	@Override
	public String saveNoteCounselTx(PaqnamVO panotem) throws Exception {
		return paSsgCounselProcess.saveNoteCounsel(panotem);
	}

	@Override
	public List<PaqnamVO> selectPaNoteAnswerList(ParamMap paramMap) throws Exception {
		return paSsgCounselProcess.selectPaNoteAnswerList(paramMap);
	}
	
}
