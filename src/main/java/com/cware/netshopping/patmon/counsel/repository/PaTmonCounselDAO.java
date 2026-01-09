package com.cware.netshopping.patmon.counsel.repository;


import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;

@Service("patmon.counsel.paTmonCounselDAO")
public class PaTmonCounselDAO extends AbstractPaDAO {

	 @SuppressWarnings("unchecked")
		public List<PaqnamVO> selectPaTmonQna(String msgGb) throws Exception{
	    	return (List<PaqnamVO>) list("patmon.counsel.selectPaTmonQna",msgGb);
	    }

	/**
	* 티몬 상품문의 답변처리 대상 조회
	* @param 
	* @return List<PaqnamVO>
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaTmonAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("patmon.counsel.selectPaTmonAnsQna", paramMap.get());
	}
	/**
     * 티몬 상품문의 답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("patmon.counsel.updatePaQnaTrans", paQna);
	}

	/**
     * 티몬 상품문의 답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("patmon.counsel.selectPaQnaDtMaxSeq", paQna);
	}

	/**
	 * 티몬 상품문의 답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) {
		return insert("patmon.counsel.insertPaQnaDt", paQna);
	}

	/**
	 * 티몬 CS문의 중복확인 
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int selectPaCounselNoCheck(String paCounselNo) {
		return (int)selectByPk("patmon.counsel.selectPaCounselNoCheck", paCounselNo);
	}

}
