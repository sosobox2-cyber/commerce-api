package com.cware.netshopping.pagmkt.broadcast.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pagmkt.broadcast.process.PaGmktBroadcastProcess;
import com.cware.netshopping.pagmkt.broadcast.service.PaGmktBroadcastService;

@Service("pagmkt.broadcast.PaGmktBroadcastService")
public class PaGmktBroadcastServiceImpl  extends AbstractService implements PaGmktBroadcastService {

    @Resource(name = "pagmkt.broadcast.PaGmktBroadcastProcess")
    private PaGmktBroadcastProcess paGmktBroadcastProcess;
    
    @Override
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception {
		paGmktBroadcastProcess.refreshBroadReplaceGoods(paramMap);
	}
    
    @Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return paGmktBroadcastProcess.selectBroadcastScheList(paramMap);
	}

	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return paGmktBroadcastProcess.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public List<Object> selectBraocastV2GoodsList(ParamMap paramMap)throws Exception {
		return paGmktBroadcastProcess.selectBraocastV2GoodsList(paramMap);
	}
	
	@Override
	public int deleteGmktBroadSche(Pabroadsche pabroadshce)throws Exception {
		return paGmktBroadcastProcess.deleteGmktBroadSche(pabroadshce);
	}
	
	@Override
	public int insertGmktBroadSche(Pabroadsche pabroadshce)throws Exception {
		return paGmktBroadcastProcess.insertGmktBroadSche(pabroadshce);
	}
	
	@Override
	public int updateGmktBroadSche(Pabroadsche pabroadshce)throws Exception {
		return paGmktBroadcastProcess.updateGmktBroadSche(pabroadshce);
	}
	
	@Override
	public void updateGmktBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception {
		paGmktBroadcastProcess.updateGmktBroadReplace(pabroadreplace);
	}
}
