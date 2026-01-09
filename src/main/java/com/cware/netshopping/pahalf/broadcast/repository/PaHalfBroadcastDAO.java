package com.cware.netshopping.pahalf.broadcast.repository;

import java.util.List;

import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

@Service("pahalf.broadcast.paHalfBroadcastDAO")
public class PaHalfBroadcastDAO extends AbstractPaDAO {

	public int selectCheckReplaceGoodsYn(ParamMap paramMap) throws Exception{
		return (int) selectByPk("pahalf.broadcast.selectCheckReplaceGoodsYn", paramMap.get());
	}
	
	public void deletePaBroadReplace(ParamMap paramMap) throws Exception{
		delete("pahalf.broadcast.deletePaBroadReplace", paramMap.get());
	}

	public void insertPaBroadReplace(ParamMap paramMap) throws Exception{
		insert("pahalf.broadcast.insertPaBroadReplace", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<MultiframescheVO> selectHalfBroadcastScheList(ParamMap paramMap) throws Exception{
		return (List<MultiframescheVO>)list("pahalf.broadcast.selectHalfBroadcastScheList", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception{
		return (List<Pabroadreplace>)list("pahalf.broadcast.selectAlternativeBroadcastLink", paGroupCode);
	}

	@SuppressWarnings("unchecked")
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception{
		return (List<Pabroadsche>)list("pahalf.broadcast.selectBroadcastScheList", paramMap.get());
	}

	public int insertBroadSche(Pabroadsche pabroadshce) throws Exception{
		return insert("pahalf.broadcast.insertBroadSche", pabroadshce);
	}

	public int updateBroadSche(Pabroadsche pabroadshce) throws Exception{
		return update("pahalf.broadcast.updateBroadSche", pabroadshce);
	}

	public int deleteBroadSche(Pabroadsche pabroadshce) throws Exception{
		return delete("pahalf.broadcast.deleteBroadSche", pabroadshce);
	}

	public void updateCycleSeqUseEnd(Pabroadreplace pabroadreplace) throws Exception{
		update("pahalf.broadcast.updateCycleSeqUseEnd", pabroadreplace);
	}

	public void updateCycleSeqUseStart(Pabroadreplace pabroadreplace) throws Exception{
		update("pahalf.broadcast.updateCycleSeqUseStart", pabroadreplace);
	}

}
