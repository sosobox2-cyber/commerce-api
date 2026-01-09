package com.cware.netshopping.pa11st.broadcast.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pa11st.broadcast.process.Pa11stBroadcastProcess;
import com.cware.netshopping.pa11st.broadcast.service.Pa11stBroadcastService;

@Service("pa11st.broadcast.pa11stBroadcastService")
public class Pa11stBroadcastServiceImpl  extends AbstractService implements Pa11stBroadcastService {

	@Resource(name = "pa11st.broadcast.pa11stBroadcastProcess")
    private Pa11stBroadcastProcess pa11stBroadcastProcess;

	@Override
	public List<MultiframescheVO> selectBroadcastScheListOld(ParamMap paramMap) throws Exception {
		return pa11stBroadcastProcess.selectBroadcastScheListOld(paramMap);
	}
	
	
	
	
	@Override
	public void refreshBroadReplaceGoodsTx(ParamMap paramMap) throws Exception {
		pa11stBroadcastProcess.refreshBroadReplaceGoods(paramMap);
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return pa11stBroadcastProcess.selectBroadcastScheList(paramMap);
	}
	
	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return pa11stBroadcastProcess.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public String save11stBroadScheTx(Pabroadsche pabroadshce) throws Exception {
		return pa11stBroadcastProcess.save11stBroadSche(pabroadshce);
	}
	
	@Override
	public void updatePaBroadReplaceTx(Pabroadreplace pabroadreplace) throws Exception {
		pa11stBroadcastProcess.updatePaBroadReplace(pabroadreplace);
	}
	
	@Override
	public List<MultiframescheVO> select11stBroadcastScheList(ParamMap paramMap) throws Exception {
		return pa11stBroadcastProcess.select11stBroadcastScheList(paramMap);
	}
}
