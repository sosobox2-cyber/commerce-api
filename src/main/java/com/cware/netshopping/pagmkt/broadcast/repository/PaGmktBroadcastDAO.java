package com.cware.netshopping.pagmkt.broadcast.repository;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

@Service("pagmkt.broadcast.PagmktBroadcastDAO")
@SuppressWarnings("unchecked")
public class PaGmktBroadcastDAO extends AbstractPaDAO  {
	
	public int selectCheckReplaceGoodsYn(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pagmkt.broadcast.selectCheckReplaceGoodsYn", paramMap.get());
	}
	
	public int deletePaBroadReplace(ParamMap paramMap) throws Exception {
		return delete("pagmkt.broadcast.deletePaBroadReplace", paramMap.get());
	}
	
	public int insertPaBroadReplace(ParamMap paramMap) throws Exception {
		return insert("pagmkt.broadcast.insertPaBroadReplace", paramMap.get());
	}
	
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception{
		return list("pagmkt.broadcast.selectBroadcastScheList", paramMap.get());
	}

	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception{
		return list("pagmkt.broadcast.selectAlternativeBroadcastLink", paGroupCode);
	}
	
	public List<Object> selectBraocastV2GoodsList(ParamMap paramMap) throws Exception{
		return list("pagmkt.broadcast.selectBraocastV2GoodsList", paramMap.get());
	}
	
	public int deleteGmktBroadSche(Pabroadsche pabroadshce) throws Exception{
		return delete("pagmkt.broadcast.deleteGmktBroadSche",pabroadshce);
	}
	
	public int insertGmktBroadSche(Pabroadsche pabroadshce) throws Exception{
		return insert("pagmkt.broadcast.insertGmktBroadSche",pabroadshce);
	}
	
	public int updateGmktBroadSche(Pabroadsche pabroadshce) throws Exception{
		return update("pagmkt.broadcast.updateGmktBroadSche",pabroadshce);
	}
	
	public int updateCycleSeqUseEnd(Pabroadreplace pabroadreplace) throws Exception{
		return update("pagmkt.broadcast.updateCycleSeqUseEnd",pabroadreplace);
	}

	public int updateCycleSeqUseStart(Pabroadreplace pabroadreplace) throws Exception{
		return update("pagmkt.broadcast.updateCycleSeqUseStart",pabroadreplace);
	}
}
