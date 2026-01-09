package com.cware.netshopping.pafaple.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.pafaple.service.PaFapleBroadcastService;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pafaple.process.PaFapleBroadcastProcess;


@Service("pafaple.broadcast.PaFapleBroadcastService")
public class PaFapleBroadcastServicelmpl extends AbstractService implements PaFapleBroadcastService {
	
	@Resource(name = "pafaple.broadcast.paFapleBroadcastProcess")
    private PaFapleBroadcastProcess paFapleBroadcastProcess;
	
	@Override
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception {
		paFapleBroadcastProcess.refreshBroadReplaceGoods(paramMap);
	}
	
    @Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paFapleBroadcastProcess.selectBroadcastScheList(paramMap);
	}
    
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paFapleBroadcastProcess.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String saveFapleBroadScheTx(Pabroadsche pabroadshce) throws Exception {
		return paFapleBroadcastProcess.saveFapleBroadSche(pabroadshce);
	}
	
	@Override
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception {
		paFapleBroadcastProcess.updatePaBroadReplace(pabroadreplace);
	}
	
	@Override
	public List<MultiframescheVO> selectFapleBroadcastScheList(ParamMap paramMap) throws Exception {
		return paFapleBroadcastProcess.selectFapleBroadcastScheList(paramMap);
	}

}

