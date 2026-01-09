package com.cware.netshopping.pawemp.counsel.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pawemp.counsel.model.NoticeData;
import com.cware.netshopping.pawemp.counsel.process.PaWempCounselProcess;
import com.cware.netshopping.pawemp.counsel.repository.PaWempCounselDAO;

@Service("pawemp.counsel.paWempCounselProcess")
public class PaWempCounselProcessImpl extends AbstractService implements PaWempCounselProcess{

	@Resource(name = "pawemp.counsel.paWempCounselDAO")
	private PaWempCounselDAO paWempCounselDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	/**
	 * 상품Q&A답변처리 대상 조회
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	public List<PaqnamVO> selectPaWempAnsQna() throws Exception {
		return paWempCounselDAO.selectPaWempAnsQna();
	}
	
	/**
	 * 상품Q&A답변처리 결과저장.
	 * @param PaqnamVO
	 * @return String
	 * @throws Exception
	 */
	public String savePaWempQnaTrans(PaqnamVO paQna) throws Exception{
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paWempCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNAM update" });
		}
		paCounselDtSeq = paWempCounselDAO.selectPaQnaDtMaxSeq(paQna);
		
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		executedRtn = paWempCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save",
					new String[] { "TPAQNADT insert" });
		}
		
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public String savePaWempNotice(List<NoticeData> nData) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		String paNoticeSeq = ""; 
		HashMap<String, Object> hashMap = null;

		for(NoticeData noticeItem : nData){
			ParamMap paramMap      = new ParamMap();
			paramMap.put("paType", noticeItem.getPaType());
			paramMap.put("paTitle", noticeItem.getPaTitle());
			paramMap.put("paRegDate", noticeItem.getPaRegDate());
			// 중복데이터 있는지 체크
			executedRtn = paWempCounselDAO.selectChkPaNotice(paramMap);
			
			if(executedRtn == 1){ // 중복데이터 있을 경우 넘어감
				continue;
			}
			
			hashMap = new HashMap<String, Object>();
    		hashMap.put("sequence_type", "EMERNOTI_SEQ");
    		paNoticeSeq = (String)systemDAO.selectSequenceNo(hashMap);
    		if (paNoticeSeq.equals("")) {
    		    throw processException("msg.cannot_create", new String[] { "EMERNOTI_SEQ" });
    		}
			
    		noticeItem.setNoticeSeq(paNoticeSeq);
			executedRtn = paWempCounselDAO.insertPaNotice(noticeItem);	
			if (executedRtn < 1) {
				//= 오류
				throw processException("msg.cannot_save", new String[] { "TPAWEMPNOTILIST INSERT" });
			}
		}
		return rtnMsg;
	}

	@Override
	public List<String> selectCounselDate(Paqnamoment paqnamoment) throws Exception {
		return paWempCounselDAO.selectCounselDate(paqnamoment);
	}
	
}