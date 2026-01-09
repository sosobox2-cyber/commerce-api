
package com.cware.netshopping.pahalf.counsel.repository;


import java.util.List;

import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;


@Service("pahalf.counsel.paHalfCounselDAO")
public class PaHalfCounselDAO extends AbstractPaDAO{

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaHalfCs() {
		return (List<PaqnamVO>) list("pahalf.counsel.selectPaHalfCs", null);
	}
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaHalfQna() {
		return (List<PaqnamVO>) list("pahalf.counsel.selectPaHalfQna", null);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaHalfAnsQna(ParamMap paramMap) {
		return (List<PaqnamVO>) list("pahalf.counsel.selectPaHalfAnsQna", paramMap.get());
	}

	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("pahalf.counsel.updatePaQnaTrans", paQna);
	}

	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("pahalf.counsel.selectPaQnaDtMaxSeq", paQna);
	}

	public int insertPaQnaDt(PaqnamVO paQna) {
		return insert("pahalf.counsel.insertPaQnaDt", paQna);
	}
    
}