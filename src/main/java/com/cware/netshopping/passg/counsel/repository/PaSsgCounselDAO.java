package com.cware.netshopping.passg.counsel.repository;



import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.PaqnamVO;

@Service("passg.counsel.paSsgCounselDAO")
public class PaSsgCounselDAO extends AbstractPaDAO {

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaSsgQna() throws Exception{
    	return (List<PaqnamVO>) list("passg.counsel.selectPaSsgQna",null);
    }

	/**
	* SSG 상품문의 답변처리 대상 조회
	* @param 
	* @return List<PaqnamVO>
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaSsgAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("passg.counsel.selectPaSsgAnsQna", paramMap.get());
	}
	
	/**
     * SSG 상품문의 답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("passg.counsel.updatePaQnaTrans", paQna);
	}

	/**
     * SSG 상품문의 답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("passg.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * tpaqna 데이터 유무 체크
	 * @param paqnamoment
	 * @return
	 * @throws Exception
	 */
	public int selectPaqnaCount(PaqnamVO paqnamVo) throws Exception{
    	return (Integer) selectByPk("passg.counsel.selectPaqnaCount", paqnamVo);
    }
	
	/**
	 * paCounselSeq 생성
	 * @return
	 */
	public String selectPaCounselNewSeq() {
		return (String) selectByPk("passg.counsel.selectPaCounselNewSeq", null);
	}
	
	/**
     * TPAQNAM INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaM(PaqnamVO paqnamVo) throws Exception{
    	return insert("passg.counsel.insertPaQnaM", paqnamVo);
    }
    
    /**
     * TPAQNADT INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaDt(PaqnamVO paqnamVo) throws Exception{
    	return insert("passg.counsel.insertPaQnaDt", paqnamVo);
    }
    
    /**
     * SSG 교환거부 대상 정보 조회
     * @param mappingSeq
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaSsgChangeCounselDt(OrderClaimVO claimVO) throws Exception{
    	return (HashMap<String, Object>) selectByPk("passg.counsel.selectPaSsgChangeCounselDt", claimVO);
    }

	public int selectPaCounselNoCheck(ParamMap paramMap) throws Exception {
		return (int) selectByPk("passg.counsel.selectPaCounselNoCheck", paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaSsgNote() throws Exception {
		return (List<PaqnamVO>) list("passg.counsel.selectPaSsgNote",null);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaSsgProcNoteList(ParamMap paramMap) throws Exception {
		return (List<PaqnamVO>) list("passg.counsel.selectPaSsgProcNoteList", paramMap.get());
	}

	public int updatePaCounselSeq(PaqnamVO procNote) throws Exception {
		return update("passg.counsel.updatePaCounselSeq", procNote);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaNoteAnswerList(ParamMap paramMap) {
		return (List<PaqnamVO>) list("passg.counsel.selectPaNoteAnswerList", paramMap.get());
	}

	public int updatePaNoteTrans(PaqnamVO paQna) {
		return update("passg.counsel.updatePaNoteTrans", paQna);
	}

	public int updatePaNoteAutoTrans(PaqnamVO paQna) throws Exception {
		return update("passg.counsel.updatePaNoteAutoTrans", paQna);
	}
}
