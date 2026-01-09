package com.cware.netshopping.pakakao.counsel.repository;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Service("pakakao.counsel.paKakaoCounselDAO")
public class PaKakaoCounselDAO extends AbstractPaDAO{

	public int checkPaQnam(PaqnamVO paQnam) throws Exception{
    	return (Integer) selectByPk("pakakao.counsel.checkPaQnam", paQnam);
    }
	
	public int insertPaQnaM(PaqnamVO paQnam) throws Exception{
    	return insert("pakakao.counsel.insertPaQnaM", paQnam);
    }
	
	public int insertPaQnaDt(PaqnamVO paQnam) throws Exception{
    	return insert("pakakao.counsel.insertPaQnaDt", paQnam);
    }
	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectCustCounselList() throws Exception{
		return (List<PaqnamVO>) list("pakakao.counsel.selectCustCounselList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaAnserQnaList(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("pakakao.counsel.selectPaAnserQnaList", paramMap.get());
	}
	
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("pakakao.counsel.updatePaQnaTrans", paQna);
	}
	
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("pakakao.counsel.selectPaQnaDtMaxSeq", paQna);
	}
}
