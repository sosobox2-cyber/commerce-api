package com.cware.netshopping.pakakao.counsel.process;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaKakaoCounselProcess {

	public void savePaQna(List<PaqnamVO> paQnaList) throws Exception;
	
	public void saveCustCounsel() throws Exception;
	
	public List<PaqnamVO> selectPaAnserQnaList(ParamMap paramMap) throws Exception;
	
	public void savePaQnaTrans(PaqnamVO paQna) throws Exception;
}
