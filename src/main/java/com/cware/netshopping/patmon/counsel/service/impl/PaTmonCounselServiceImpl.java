package com.cware.netshopping.patmon.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.patmon.counsel.process.PaTmonCounselProcess;
import com.cware.netshopping.patmon.counsel.service.PaTmonCounselService;

@Service("patmon.counsel.paTmonCounselService")
public class PaTmonCounselServiceImpl extends AbstractService implements PaTmonCounselService {

	@Resource(name = "patmon.counsel.paTmonCounselProcess")
    private PaTmonCounselProcess paTmonCounselProcess;

	@Override
	public List<PaqnamVO> selectPaTmonAnsQna(ParamMap paramMap) throws Exception {
		return paTmonCounselProcess.selectPaTmonAnsQna(paramMap);
	}

	@Override
	public String savePaTmonQnaTransTx(PaqnamVO paQna) throws Exception {
		return paTmonCounselProcess.savePaTmonQnaTrans(paQna);
	}

	@Override
	public int selectPaCounselNoCheck(String pa_counsel_no) throws Exception {
		return paTmonCounselProcess.selectPaCounselNoCheck(pa_counsel_no);
	}
}
