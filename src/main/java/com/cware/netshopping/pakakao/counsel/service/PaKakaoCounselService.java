package com.cware.netshopping.pakakao.counsel.service;

import java.util.List;

import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

public interface PaKakaoCounselService {

	public void savePaQnaTx(List<PaqnamVO> paQnaList) throws Exception;
	
	public void saveCustCounselTx() throws Exception;
	
	public List<PaqnamVO> selectPaAnserQnaList(ParamMap paramMap) throws Exception;
	
	public void savePaQnaTransTx(PaqnamVO paQna) throws Exception;
}
