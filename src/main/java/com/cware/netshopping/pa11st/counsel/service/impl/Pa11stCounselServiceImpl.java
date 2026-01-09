package com.cware.netshopping.pa11st.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pa11st.counsel.process.Pa11stCounselProcess;
import com.cware.netshopping.pa11st.counsel.service.Pa11stCounselService;


@Service("pa11st.counsel.pa11stCounselService")
public class Pa11stCounselServiceImpl  extends AbstractService implements Pa11stCounselService {

	@Resource(name = "pa11st.counsel.pa11stCounselProcess")
    private Pa11stCounselProcess pa11stCounselProcess;
	
	@Override
	public List<PaqnamVO> selectPa11stAnsQna(ParamMap paramMap) throws Exception{
		return pa11stCounselProcess.selectPa11stAnsQna(paramMap);
	}

	@Override
	public String savePa11stQnaTransTx(PaqnamVO paQna) throws Exception{
		return pa11stCounselProcess.savePa11stQnaTrans(paQna);
	}
	
	@Override
	public String saveQnaMonitering(PaqnamVO paQna) throws Exception{
		return pa11stCounselProcess.saveQnaMonitering(paQna);
	}
	
	@Override
	public String selectCheckPacounsel(ParamMap paramMap) throws Exception{	
		return pa11stCounselProcess.selectCheckPacounsel(paramMap);
	}
	
	@Override
	public String selectGetMaxPacounselSeq(ParamMap paramMap) throws Exception{	
		return pa11stCounselProcess.selectGetMaxPacounselSeq(paramMap);
	}
	
}