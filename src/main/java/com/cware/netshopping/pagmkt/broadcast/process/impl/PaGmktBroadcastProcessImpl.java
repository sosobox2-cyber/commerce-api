package com.cware.netshopping.pagmkt.broadcast.process.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pagmkt.broadcast.process.PaGmktBroadcastProcess;
import com.cware.netshopping.pagmkt.broadcast.repository.PaGmktBroadcastDAO;

@Service("pagmkt.broadcast.PaGmktBroadcastProcess")
public class PaGmktBroadcastProcessImpl extends AbstractService implements PaGmktBroadcastProcess {

	@Resource(name = "pagmkt.broadcast.PagmktBroadcastDAO")
	private PaGmktBroadcastDAO pagmktBroadcastDAO;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;

	@Override
	public void refreshBroadReplaceGoods(ParamMap paramMap) throws Exception {
		int checkReplace = 0;
		try {
			checkReplace = pagmktBroadcastDAO.selectCheckReplaceGoodsYn(paramMap);
			if(checkReplace > 0) {
				pagmktBroadcastDAO.deletePaBroadReplace(paramMap);
				pagmktBroadcastDAO.insertPaBroadReplace(paramMap);				
			}
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE REFRESH FAIL" });
		}
	}
	
	@Override
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception {
		return pagmktBroadcastDAO.selectBroadcastScheList(paramMap);
	}

	@Override
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception {
		return pagmktBroadcastDAO.selectAlternativeBroadcastLink(paGroupCode);
	}
	
	@Override
	public List<Object> selectBraocastV2GoodsList(ParamMap paramMap) throws Exception {
		return pagmktBroadcastDAO.selectBraocastV2GoodsList(paramMap);
	}
	
	@Override
	public int deleteGmktBroadSche(Pabroadsche pabroadshce) throws Exception {
		return pagmktBroadcastDAO.deleteGmktBroadSche(pabroadshce);
	}
	
	@Override
	public int insertGmktBroadSche(Pabroadsche pabroadshce) throws Exception {
		return pagmktBroadcastDAO.insertGmktBroadSche(pabroadshce);
	}
	
	@Override
	public int updateGmktBroadSche(Pabroadsche pabroadshce) throws Exception {
		return pagmktBroadcastDAO.updateGmktBroadSche(pabroadshce);
	}
	
	@Override
	public void updateGmktBroadReplace(Pabroadreplace pabroadreplace) throws Exception {
		try {
			pagmktBroadcastDAO.updateCycleSeqUseEnd(pabroadreplace);
			pagmktBroadcastDAO.updateCycleSeqUseStart(pabroadreplace);
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPABROADREPLACE UPDATE" });
		}
	}
}
