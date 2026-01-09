package com.cware.netshopping.pahalf.counsel.service.impl;


import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pahalf.counsel.process.PaHalfCounselProcess;
import com.cware.netshopping.pahalf.counsel.service.PaHalfCounselService;

@Service("pahalf.counsel.paHalfCounselService")
public class PaHalfCounselServiceImpl  extends AbstractService implements PaHalfCounselService {

	@Resource(name = "pahalf.counsel.paHalfCounselProcess")
    private PaHalfCounselProcess paHalfCounselProcess;

	@Override
	public List<PaqnamVO> selectPaHalfAnsQna(ParamMap paramMap) throws Exception {
		return paHalfCounselProcess.selectPaHalfAnsQna(paramMap);
	}

	@Override
	public void savePaHalfQnaTransTx(PaqnamVO paQna) throws Exception {
		 paHalfCounselProcess.savePaHalfQnaTransTx(paQna);
	}
 
	
}