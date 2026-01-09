package com.cware.netshopping.pa11st.broadcast.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.MultiframescheVO;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;

@Service("pa11st.broadcast.pa11stBroadcastDAO")
@SuppressWarnings("unchecked")
public class Pa11stBroadcastDAO extends AbstractPaDAO {

	/**
	 * 11번가 편성내용 조회
	 * @param paramMap
	 * @return List<MultiframescheVO>
	 * @throws Exception
	 */
	public List<MultiframescheVO> selectBroadcastScheListOld(ParamMap paramMap) throws Exception{
		return list("pa11st.broadcast.selectBroadcastScheListOld", paramMap.get());
	}
	
	
	
	public int selectCheckReplaceGoodsYn(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pa11st.broadcast.selectCheckReplaceGoodsYn", paramMap.get());
	}
	
	public int deletePaBroadReplace(ParamMap paramMap) throws Exception {
		return delete("pa11st.broadcast.deletePaBroadReplace", paramMap.get());
	}
	
	public int insertPaBroadReplace(ParamMap paramMap) throws Exception {
		return insert("pa11st.broadcast.insertPaBroadReplace", paramMap.get());
	}
	
	public List<Pabroadsche> selectBroadcastScheList(ParamMap paramMap) throws Exception{
		return list("pa11st.broadcast.selectBroadcastScheList", paramMap.get());
	}
	
	public List<Pabroadreplace> selectAlternativeBroadcastLink(String paGroupCode) throws Exception{
		return list("pa11st.broadcast.selectAlternativeBroadcastLink", paGroupCode);
	}
	
	public int insertBroadSche(Pabroadsche pabroadshce) throws Exception{
		return insert("pa11st.broadcast.insertBroadSche",pabroadshce);
	}
	
	public int updateBroadSche(Pabroadsche pabroadshce) throws Exception{
		return update("pa11st.broadcast.updateBroadSche",pabroadshce);
	}
	
	public int deleteBroadSche(Pabroadsche pabroadshce) throws Exception{
		return delete("pa11st.broadcast.deleteBroadSche",pabroadshce);
	}
	
	public int updateCycleSeqUseEnd(Pabroadreplace pabroadreplace) throws Exception{
		return update("pa11st.broadcast.updateCycleSeqUseEnd",pabroadreplace);
	}

	public int updateCycleSeqUseStart(Pabroadreplace pabroadreplace) throws Exception{
		return update("pa11st.broadcast.updateCycleSeqUseStart",pabroadreplace);
	}
	
	public List<MultiframescheVO> select11stBroadcastScheList(ParamMap paramMap) throws Exception{
		return list("pa11st.broadcast.select11stBroadcastScheList", paramMap.get());
	}
}
