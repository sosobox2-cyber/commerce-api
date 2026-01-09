package com.cware.netshopping.palton.counsel.process.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.palton.counsel.process.PaLtonCounselProcess;
import com.cware.netshopping.palton.counsel.repository.PaLtonCounselDAO;

@Service("palton.counsel.paLtonCounselProcess")
public class PaLtonCounselProcessImpl extends AbstractService implements PaLtonCounselProcess{

	@Resource(name = "palton.counsel.paLtonCounselDAO")
	private PaLtonCounselDAO paLtonCounselDAO;

	/**
	 * 롯데온 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaLtonAnsQna(ParamMap paramMap) throws Exception {
		return paLtonCounselDAO.selectPaLtonAnsQna(paramMap);
	}

	/**
	 * 롯데온 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String savePaLtonQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paLtonCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = paLtonCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paLtonCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		
		return Constants.SAVE_SUCCESS;
	}
	
	/**
	 * 롯데온 판매자 연락답변 등록 처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@Override
	public List<PaqnamVO> updateSellerContact(ParamMap paramMap) throws Exception {
		return paLtonCounselDAO.updateSellerContact(paramMap);
	}

	/**
	 * 롯데온 판매자 문의답변 등록 처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@Override
	public List<PaqnamVO> updateSellerInquiry(ParamMap paramMap) throws Exception {
		return paLtonCounselDAO.updateSellerInquiry(paramMap);
	}
}
