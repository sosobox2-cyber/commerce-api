package com.cware.netshopping.pawemp.broadcast.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pawemp.broadcast.process.PaWempBroadcastProcess;
import com.cware.netshopping.pawemp.broadcast.service.PaWempBroadcastService;

@Service("pawemp.broadcast.paWempBroadcastService")
public class PaWempBroadcastServiceImpl  extends AbstractService implements PaWempBroadcastService {

    @Resource(name = "pawemp.broadcast.paWempBroadcastProcess")
    private PaWempBroadcastProcess paWempBroadcastProcess;

    @Override
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception {
    	paWempBroadcastProcess.refreshBroadReplaceGoods(paramMap);
	}
    
    @Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paWempBroadcastProcess.selectBroadcastScheList(paramMap);
	}

	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paWempBroadcastProcess.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String saveWempBroadScheTx(Pabroadsche pabroadshce) throws Exception {
		return paWempBroadcastProcess.saveWempBroadSche(pabroadshce);
	}
	
	@Override
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception {
		paWempBroadcastProcess.updatePaBroadReplace(pabroadreplace);
	}
}
