package com.cware.netshopping.paqeen.repository;


import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Repository("paqeen.counsel.paQeenCounselDAO")
public class PaQeenCounselDAO extends AbstractPaDAO {
	
	public String selectProposalId(String goodsCode)  throws Exception{
		return (String) selectByPk("paqeen.counsel.selectProposalId", goodsCode);
	}
	
	public int selectPaQnaCount(ParamMap chkMap) throws Exception{
		return (Integer) selectByPk("paqeen.counsel.selectPaQnaCount", chkMap.get());
	}
	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaQeenCsQna() throws Exception{
		return (List<PaqnamVO>) list("paqeen.counsel.selectPaQeenCsQna", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectCsAnsList(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("paqeen.counsel.selectCsAnsList", paramMap.get());
	}
	
	/**
     * 퀸잇 상담 CS답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("paqeen.counsel.updatePaQnaTrans", paQna);
	}
	/**
     * 퀸잇 상담 CS답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return String
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("paqeen.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 퀸잇 상담 CS답변처리- 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("paqeen.counsel.insertPaQnaDt", paQna);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaQeenOrderLineGoodsInfo(ParamMap paramMap) throws Exception{
		return (HashMap<String, Object>) selectByPk("paqeen.counsel.selectPaQeenOrderLineGoodsInfo", paramMap.get());
	}
	
}
