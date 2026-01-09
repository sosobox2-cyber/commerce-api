package com.cware.netshopping.pacommon.counsel.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pacommon.counsel.process.PaCounselProcess;
import com.cware.netshopping.pacommon.counsel.service.PaCounselService;

@Service("pacommon.counsel.paCounselService")
public class PaCounselServiceImpl  extends AbstractService implements PaCounselService {

    @Resource(name = "pacommon.counsel.paCounselProcess")
    private PaCounselProcess paCounselProcess;
    
    @Override
    public String savePaQnaTx(List<Paqnamoment> paQnaMomentList, String msgGb) throws Exception {
        return paCounselProcess.savePaQna(paQnaMomentList, msgGb);
    }
}
