package com.cware.netshopping.pafaple.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

@Repository("pafaple.broadcast.paFapleBroadcastDAO")
public class PaFapleBroadcastDAO extends AbstractPaDAO {
	
	public int selectCheckReplaceGoodsYn(ParamMap paramMap) throws Exception{
		return (int) selectByPk("pafaple.broadcast.selectCheckReplaceGoodsYn", paramMap.get());
	}
	
	public void deletePaBroadReplace(ParamMap paramMap) throws Exception{
		delete("pafaple.broadcast.deletePaBroadReplace", paramMap.get());
	}
	
	public void insertPaBroadReplace(ParamMap paramMap) throws Exception{
		insert("pafaple.broadcast.insertPaBroadReplace", paramMap.get());
	}
	
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception{
		return list("pafaple.broadcast.selectBroadcastScheList", paramMap.get());
	}
		
	@SuppressWarnings("unchecked")
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception{
		return (List<Pabroadreplace>)list("pafaple.broadcast.selectAlternativeBroadcastLink", paGroupCode);
	}
	
	public int insertBroadSche(Pabroadsche pabroadshce) throws Exception{
		return insert("pafaple.broadcast.insertBroadSche", pabroadshce);
	}
	
	public int updateBroadSche(Pabroadsche pabroadshce) throws Exception{
		return update("pafaple.broadcast.updateBroadSche", pabroadshce);
	}

	public int deleteBroadSche(Pabroadsche pabroadshce) throws Exception{
		return delete("pafaple.broadcast.deleteBroadSche", pabroadshce);
	}
	
	public void updateCycleSeqUseEnd(Pabroadreplace pabroadreplace) throws Exception{
	}

	public void updateCycleSeqUseStart(Pabroadreplace pabroadreplace) throws Exception{
		update("pafaple.broadcast.updateCycleSeqUseStart", pabroadreplace);
	}

	@SuppressWarnings("unchecked")
	public List<MultiframescheVO> selectFapleBroadcastScheList(ParamMap paramMap) throws Exception{
		return (List<MultiframescheVO>)list("pafaple.broadcast.selectFapleBroadcastScheList", paramMap.get());
	}
	
}
