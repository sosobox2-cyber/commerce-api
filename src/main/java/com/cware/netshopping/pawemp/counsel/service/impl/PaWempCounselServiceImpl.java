package com.cware.netshopping.pawemp.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pawemp.counsel.model.NoticeData;
import com.cware.netshopping.pawemp.counsel.process.PaWempCounselProcess;
import com.cware.netshopping.pawemp.counsel.service.PaWempCounselService;


@Service("pawemp.counsel.paWempCounselService")
public class PaWempCounselServiceImpl  extends AbstractService implements PaWempCounselService {

	@Resource(name = "pawemp.counsel.paWempCounselProcess")
    private PaWempCounselProcess paWempCounselProcess;
	
	@Override
	public List<PaqnamVO> selectPaWempAnsQna() throws Exception{
		return paWempCounselProcess.selectPaWempAnsQna();
	}

	@Override
	public String savePaWempQnaTransTx(PaqnamVO paQna) throws Exception{
		return paWempCounselProcess.savePaWempQnaTrans(paQna);
	}

	@Override
	public String savePaWempNoticeTx(List<NoticeData> nData) throws Exception {
		return paWempCounselProcess.savePaWempNotice(nData);
	}

	@Override
	public List<String> selectCounselDate(Paqnamoment paqnamoment) throws Exception {
		return paWempCounselProcess.selectCounselDate(paqnamoment);
	}
	
}