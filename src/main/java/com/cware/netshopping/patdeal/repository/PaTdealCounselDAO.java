package com.cware.netshopping.patdeal.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;

@Repository("patdeal.counsel.paTdealCounselDAO")
public class PaTdealCounselDAO extends AbstractPaDAO {

	/**
	 * 티딜 상품 상담 등록 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaTdealQna() throws Exception{
		return (List<PaqnamVO>) list("patdeal.counsel.selectPaTdealQna", null);
	}
	
	/**
	 * 티딜 상품 상담 답변처리 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaTdealAnsQna(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("patdeal.counsel.selectPaTdealAnsQna", paramMap.get());
	}
	
	/**
     * 티딜 상품문의답변처리 - 결과처리
     * @param PaqnamVO
     * @return int
     * @throws Exception
     */
	public int updatePaQnaTrans(PaqnamVO paQna) {
		return update("patdeal.counsel.updatePaQnaTrans", paQna);
	}
	/**
     * 티딜 상품문의답변처리 - TPAQNADT MAX SEQ 조회(PA_COUNSEL_SEQ + 1)
     * @param PaqnamVO
     * @return String
     * @throws Exception
     */
	public String selectPaQnaDtMaxSeq(PaqnamVO paQna) {
		return (String) selectByPk("patdeal.counsel.selectPaQnaDtMaxSeq", paQna);
	}
	
	/**
	 * 티딜 상품문의답변처리 - 상담완료 데이터 저장.
	 * @param PaqnamVO
	 * @return int
	 * @throws Exception
	 */
	public int insertPaQnaDt(PaqnamVO paQna) throws Exception{
		return insert("patdeal.counsel.insertPaQnaDt", paQna);
	}

	/**
	 * 티딜 업무 메시지 조회하기 - 몰 상품번호 조회  
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectMallProductNo(String orderProductOptionNo)  throws Exception{
		return (String) selectByPk("patdeal.counsel.selectMallProductNo", orderProductOptionNo);
	}

	/**
	 * 티딜 업무 메시지 답변 대상 조회
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectTaskMessagesAnsList(ParamMap paramMap) throws Exception{
		return (List<PaqnamVO>) list("patdeal.counsel.selectTaskMessagesAnsList", paramMap.get());
	}
	
	/**
	 * 티딜 업무 메시지 조회하기 - 제휴사코드조회  
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public String selectPaCode(String orderNo)  throws Exception{
		return (String) selectByPk("patdeal.counsel.selectPaCode", orderNo);
	}

	public String selectPaCounselNewSeq() throws Exception{
		return (String) selectByPk("patdeal.counsel.selectPaCounselNewSeq", null);
	}

	public int selectPaqnaCount(Paqnamoment paqnamVo) throws Exception{
		return (Integer) selectByPk("patdeal.counsel.selectPaqnaCount", paqnamVo);
	}

	public int insertPaQnaM(PaqnamVO paqnamVo) throws Exception{
		return insert("patdeal.counsel.insertPaQnaM", paqnamVo);
	}

	@SuppressWarnings("unchecked")
	public List<PaqnamVO> selectPaTdealTaskMessage() throws Exception{
		return (List<PaqnamVO>) list("patdeal.counsel.selectPaTdealTaskMessage", null);
	}
	
}