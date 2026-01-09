
package com.cware.netshopping.pawemp.counsel.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pawemp.counsel.model.NoticeData;

@Service("pawemp.counsel.paWempCounselDAO")
public class PaWempCounselDAO extends AbstractPaDAO{
	
	/**
     * 상품Q&A 조회 - 상담 등록 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaWempQna() throws Exception{
    	return (List<PaqnamVO>) list("pawemp.counsel.selectPaWempQna", null);
    }

    /**
     * 상품Q&A답변처리 대상 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaWempAnsQna() throws Exception{
		return (List<PaqnamVO>) list("pawemp.counsel.selectPaWempAnsQna", null);
	}
	
	/**
     * 상품Q&A답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) throws Exception{
		return update("pawemp.counsel.updatePaQnaTrans", paQna);
	}
	
	/**
     * 상품Q&A답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) throws Exception{
		return (String) selectByPk("pawemp.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 상품Q&A답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("pawemp.counsel.insertPaQnaDt", paQna);
	}
	
	/**
	 * 파트너 공지사항 저장
	 * @param NoticeData
	 * @return int
	 * @throws Exception
	 */
	public int insertPaNotice(NoticeData nData) throws Exception{
		return insert("pawemp.counsel.insertPaNotice", nData);
	}
	
	/**
	 * 파트너 공지사항 중복체크
	 * @param NoticeData
	 * @return int
	 * @throws Exception
	 */
	public Integer selectChkPaNotice(ParamMap paramMap) throws Exception {
		return (Integer) selectByPk("pawemp.counsel.selectChkPaNotice", paramMap.get());
	}

	/**
     * 공지사항 조회
     * @param 
     * @return Integer
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaWempNotice() throws Exception{
    	return (List<PaqnamVO>) list("pawemp.counsel.selectPaWempNotice", null);
    }

	@SuppressWarnings("unchecked")
	public List<String> selectCounselDate(Paqnamoment paqnamoment) throws Exception {
	    return list("pawemp.counsel.selectCounselDate", paqnamoment);
	}

}