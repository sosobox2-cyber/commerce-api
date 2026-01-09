package com.cware.netshopping.pahalf.broadcast.service.impl;


import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pahalf.broadcast.process.PaHalfBroadcastProcess;
import com.cware.netshopping.pahalf.broadcast.service.PaHalfBroadcastService;

@Service("pahalf.broadcast.paHalfBroadcastService")
public class PaHalfBroadcastServiceImpl  extends AbstractService implements PaHalfBroadcastService {

	@Resource(name = "pahalf.broadcast.paHalfBroadcastProcess")
    private PaHalfBroadcastProcess paHalfBroadcastProcess;
 
	@Override
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception {
		paHalfBroadcastProcess.refreshBroadReplaceGoods(paramMap);
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paHalfBroadcastProcess.selectBroadcastScheList(paramMap);
	}
	
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paHalfBroadcastProcess.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String saveHalfBroadScheTx(Pabroadsche pabroadshce) throws Exception {
		return paHalfBroadcastProcess.saveHalfBroadSche(pabroadshce);
	}
	
	@Override
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception {
		paHalfBroadcastProcess.updatePaBroadReplace(pabroadreplace);
	}
	
	@Override
	public List<MultiframescheVO> selectHalfBroadcastScheList(ParamMap paramMap) throws Exception {
		return paHalfBroadcastProcess.selectHalfBroadcastScheList(paramMap);
	}
}
