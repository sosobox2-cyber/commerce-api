package com.cware.netshopping.paintp.counsel.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.paintp.counsel.process.PaIntpCounselProcess;
import com.cware.netshopping.paintp.counsel.service.PaIntpCounselService;


@Service("paintp.counsel.paIntpCounselService")
public class PaIntpCounselServiceImpl  extends AbstractService implements PaIntpCounselService {

	@Resource(name = "paintp.counsel.paIntpCounselProcess")
    private PaIntpCounselProcess paIntpCounselProcess;
	
	@Override
	public List<PaqnamVO> selectPaIntpAnsQna(ParamMap paramMap) throws Exception{
		return paIntpCounselProcess.selectPaIntpAnsQna(paramMap);
	}
	
	@Override
	public String savePaIntpQnaTransTx(PaqnamVO paQna) throws Exception{
		return paIntpCounselProcess.savePaIntpQnaTransTx(paQna);
	}	
}