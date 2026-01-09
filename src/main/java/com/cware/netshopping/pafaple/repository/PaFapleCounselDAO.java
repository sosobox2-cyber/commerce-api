package com.cware.netshopping.pafaple.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.pafaple.domain.PaFapleQnaVO;

@Repository("pafaple.counsel.paFapleCounselDAO")
public class PaFapleCounselDAO extends AbstractPaDAO {

	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaFapleCs() throws Exception{
		return (List<PaqnamVO>) list("pafaple.counsel.selectPaFapleCs", null);
	}
	
	public String selectPaGoodsCode(String goodsCode)  throws Exception{
		return (String) selectByPk("pafaple.counsel.selectPaGoodsCode", goodsCode);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectCsNoticeAnsList(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("pafaple.counsel.selectCsNoticeAnsList", paramMap.get());
	}
	
	/**
     * 패플 상담(고객문의,CS알리미)답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("pafaple.counsel.updatePaQnaTrans", paQna);
	}
	/**
     * 패플 상담(고객문의,CS알리미)답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return String
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("pafaple.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 패플 상담(고객문의,CS알리미)답변처리- 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("pafaple.counsel.insertPaQnaDt", paQna);
	}

	/**
	 * 패플 상품 상담 등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaFapleQna() throws Exception{
		return (List<PaqnamVO>) list("pafaple.counsel.selectPaFapleQna", null);
	}

	/**
	 * 패플 상품 상담 답변처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaFapleAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("pafaple.counsel.selectPaFapleAnsQna", paramMap.get());
	}

	
	public int selectCountPaFapleQna(PaFapleQnaVO paFapleQna) throws Exception{
		return (int) selectByPk("pafaple.counsel.selectCountPaFapleQna", paFapleQna);
	}
	
	public int insertPaFapleQna(PaFapleQnaVO paFapleQna)throws Exception{
		return insert("pafaple.counsel.insertPaFapleQna", paFapleQna);
	}

	public PaFapleQnaVO selectPaFapleQnaDt(PaqnamVO paQna) throws Exception{
		return (PaFapleQnaVO) selectByPk("pafaple.counsel.selectPaFapleQnaDt", paQna);
	}
}
