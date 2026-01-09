package com.cware.netshopping.pawemp.broadcast.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

@Service("pawemp.broadcast.paWempBroadcastDAO")
@SuppressWarnings("unchecked")
public class PaWempBroadcastDAO extends AbstractPaDAO  {
	
	public int selectCheckReplaceGoodsYn(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pawemp.broadcast.selectCheckReplaceGoodsYn", paramMap.get());
	}
	
	public int deletePaBroadReplace(ParamMap paramMap) throws Exception {
		return delete("pawemp.broadcast.deletePaBroadReplace", paramMap.get());
	}
	
	public int insertPaBroadReplace(ParamMap paramMap) throws Exception {
		return insert("pawemp.broadcast.insertPaBroadReplace", paramMap.get());
	}
	
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception{
		return list("pawemp.broadcast.selectBroadcastScheList", paramMap.get());
	}

	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception{
		return list("pawemp.broadcast.selectAlternativeBroadcastLink", paGroupCode);
	}
	
	public int insertWempBroadSche(Pabroadsche pabroadshce) throws Exception{
		return insert("pawemp.broadcast.insertWempBroadSche",pabroadshce);
	}
	
	public int updateWempBroadSche(Pabroadsche pabroadshce) throws Exception{
		return update("pawemp.broadcast.updateWempBroadSche",pabroadshce);
	}

	public int updateCycleSeqUseEnd(Pabroadreplace pabroadreplace) throws Exception{
		return update("pawemp.broadcast.updateCycleSeqUseEnd",pabroadreplace);
	}

	public int updateCycleSeqUseStart(Pabroadreplace pabroadreplace) throws Exception{
		return update("pawemp.broadcast.updateCycleSeqUseStart",pabroadreplace);
	}
}
