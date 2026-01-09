package com.cware.netshopping.passg.counsel.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.OrderClaimVO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.pacommon.counsel.repository.PaCounselDAO;
import com.cware.netshopping.passg.counsel.process.PaSsgCounselProcess;
import com.cware.netshopping.passg.counsel.repository.PaSsgCounselDAO;

@Service("passg.counsel.paSsgCounselProcess")
public class PaSsgCounselProcessImpl extends AbstractService implements PaSsgCounselProcess{
		
	@Resource(name = "passg.counsel.paSsgCounselDAO")
	PaSsgCounselDAO paSsgCounselDAO;
	
	@Resource(name = "pacommon.counsel.paCounselDAO")
	PaCounselDAO paCounselDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

	@Override
	public String savePaQna(PaqnamVO paqnamVo) throws Exception {
		int executedRtn   	  = 0;
		int checkPaQnaCnt 	  = 0;
		String rtnMsg	      = Constants.SAVE_SUCCESS;
		String subQuestMoment = "";
		String paCounselSeq   = "";
		
		subQuestMoment = ComUtil.subStringBytes(paqnamVo.getProcNote(), 2000);
		paqnamVo.setProcNote(subQuestMoment);
		
		// TPAQNAM 중복 데이터 유무 체크
		checkPaQnaCnt = paSsgCounselDAO.selectPaqnaCount(paqnamVo);
		if(checkPaQnaCnt > 0) return "";
		
		paCounselSeq = paSsgCounselDAO.selectPaCounselNewSeq();
		paqnamVo.setPaCounselSeq(paCounselSeq);
		
		//= tpaqnam insert
		executedRtn = paSsgCounselDAO.insertPaQnaM(paqnamVo);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNAM INSERT" });
		
		//= tpaqnadt insert
		executedRtn = paSsgCounselDAO.insertPaQnaDt(paqnamVo);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNADT INSERT" });
		
		return rtnMsg;
	}
	
	@Override
	public List<PaqnamVO> selectPaSsgQna() throws Exception{
    	return paSsgCounselDAO.selectPaSsgQna();
    }
	
	@Override
	public String saveCustCounsel(PaqnamVO paqnam) throws Exception{
		int executedRtn   	  = 0;
		String rtnMsg	      = Constants.SAVE_SUCCESS;
		String counselSeq	  = "";
		Custcounselm custcounselm 	= null;
		Custcounseldt custcounseldt = null;
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("sequence_type", "COUNSEL_SEQ");

		counselSeq = (String)systemDAO.selectSequenceNo(hashMap);
		if (counselSeq.equals("")) {
			throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
		}

		custcounselm = new Custcounselm();
		custcounselm.setCounselSeq(counselSeq);
		custcounselm.setCustNo(paqnam.getCustNo());
		custcounselm.setDoFlag(Constants.PA_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())?"25":"10");
		custcounselm.setRefNo1(paqnam.getRefNo());
		custcounselm.setRefNo2("");
		custcounselm.setRefNo3("");
		custcounselm.setRefNo4("");
		custcounselm.setCsLgroup("60");
		custcounselm.setCsMgroup("10");
		custcounselm.setCsSgroup(paqnam.getCsSgroup());
		custcounselm.setCsLmsCode(paqnam.getCsLmsCode());
		
		custcounselm.setOutLgroupCode("99");
		custcounselm.setOutMgroupCode("99");
		custcounselm.setGoodsCode(paqnam.getGoodsCode());
		custcounselm.setGoodsdtCode(paqnam.getGoodsDtCode());
		custcounselm.setClaimNo(counselSeq);
		custcounselm.setTel(paqnam.getCustTel());
		custcounselm.setDdd("");
		custcounselm.setTel1("");
		custcounselm.setTel2("");
		custcounselm.setTel3("");
		custcounselm.setWildYn("0");
		custcounselm.setQuickYn("0");
		custcounselm.setQuickEndYn("0");
		custcounselm.setHcReqDate(null);
		custcounselm.setRemark("");
		custcounselm.setRefId1("");
		custcounselm.setCsSendYn("1");
		custcounselm.setSendEntpCode(paqnam.getEntpCode());
		custcounselm.setCounselMedia(paqnam.getPaCode());
		custcounselm.setInsertId(paqnam.getInsertId());
		custcounselm.setInsertDate(paqnam.getInsertDate());
		custcounselm.setProcId(paqnam.getModifyId());
		custcounselm.setProcDate(paqnam.getModifyDate());

		executedRtn = paCounselDAO.insertCounselCustcounselm(custcounselm);
		if (executedRtn < 1) {
			//= 오류
			throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELM INSERT" });
		}
		/*접수 데이터 생성*/
		custcounseldt = new Custcounseldt();
		custcounseldt.setCounselSeq(counselSeq);
		custcounseldt.setCounselDtSeq("100");
		custcounseldt.setDoFlag("10");
		custcounseldt.setTitle(paqnam.getTitle());
		custcounseldt.setDisplayYn("");

		String note = ComUtil.text2db("["+paqnam.getTitle()+"]"+paqnam.getProcNote());
		int len = note.getBytes("UTF-8").length;
		if(len > 2000) {
			custcounseldt.setProcNote( ComUtil.subStringBytes(note, 1950) + "[내용잘림]");
		} else {
			custcounseldt.setProcNote(note);
		}

		custcounseldt.setProcDate(paqnam.getInsertDate());
		custcounseldt.setProcId(paqnam.getInsertId());
		executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
		}

		if(Constants.PA_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
			/*업체이관 데이터 생성*/
			custcounseldt.setCounselDtSeq("101");
			custcounseldt.setDoFlag("25");
			custcounseldt.setDisplayYn("1");
			custcounseldt.setReceiverSeq("0000000001");
			executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
			}
		}

		paqnam.setCounselSeq(counselSeq);
		executedRtn = paCounselDAO.updatePaQnaCounselSeq(paqnam);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		}
		
		return rtnMsg;
	}
	
	@Override
	public List<PaqnamVO> selectPaSsgAnsQna(ParamMap paramMap) throws Exception {
		return paSsgCounselDAO.selectPaSsgAnsQna(paramMap);
	}
	
	@Override
	public String savePaSsgQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn = 0;
		String paCounselDtSeq = "";
		
		if(paQna.getMsgGb().equals("20")) {
			if("AUTO".equals(paQna.getStatus())) {
				executedRtn = paSsgCounselDAO.updatePaNoteAutoTrans(paQna);
			} else {
				executedRtn = paSsgCounselDAO.updatePaNoteTrans(paQna);
			}
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
			}
		} else {
			executedRtn = paSsgCounselDAO.updatePaQnaTrans(paQna);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
			}
		}
		
		if(!("20".equals(paQna.getMsgGb()) && "AUTO".equals(paQna.getStatus()))) { //자동답변건은 완료로 처리 안되게끔
			paCounselDtSeq = paSsgCounselDAO.selectPaQnaDtMaxSeq(paQna);
			
			paQna.setPaCounselDtSeq(paCounselDtSeq);
			executedRtn = paSsgCounselDAO.insertPaQnaDt(paQna);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQNADT insert" });
			}
		}
		return Constants.SAVE_SUCCESS;
	}

	@Override
	public HashMap<String, Object> selectPaSsgChangeCounselDt(OrderClaimVO claimVO) throws Exception {
		return paSsgCounselDAO.selectPaSsgChangeCounselDt(claimVO);
	}

	@Override
	public int selectPaCounselNoCheck(ParamMap paramMap) throws Exception {
		return paSsgCounselDAO.selectPaCounselNoCheck(paramMap);
	}

	@Override
	public List<PaqnamVO> selectPaSsgNote() throws Exception {
		return paSsgCounselDAO.selectPaSsgNote();
	}

	@Override
	public String saveNoteCounsel(PaqnamVO panotem) throws Exception {
		ParamMap paramMap = new ParamMap();
		int executedRtn = 0;
		String rtnMsg	= Constants.SAVE_SUCCESS;
		String ssgProcNote = "";
		Custcounselm custcounselm 	= null;
		Custcounseldt custcounseldt = null;

		custcounselm = new Custcounselm();
		custcounselm.setCounselSeq(panotem.getCounselSeq());
		custcounselm.setCustNo(panotem.getCustNo());
		custcounselm.setDoFlag("10");	// 접수
		custcounselm.setRefNo1(panotem.getRefNo());
		custcounselm.setRefNo2("");
		custcounselm.setRefNo3("");
		custcounselm.setRefNo4("");
		custcounselm.setCsLgroup("60");
		custcounselm.setCsMgroup("10");
		custcounselm.setCsSgroup(panotem.getCsSgroup());
		custcounselm.setCsLmsCode(panotem.getCsLmsCode());
		custcounselm.setOutLgroupCode("99");
		custcounselm.setOutMgroupCode("99");
		custcounselm.setGoodsCode(panotem.getGoodsCode());
		custcounselm.setGoodsdtCode(panotem.getGoodsDtCode());
		custcounselm.setClaimNo(panotem.getCounselSeq());
		custcounselm.setTel(panotem.getCustTel());
		custcounselm.setDdd("");
		custcounselm.setTel1("");
		custcounselm.setTel2("");
		custcounselm.setTel3("");
		custcounselm.setWildYn("0");
		custcounselm.setQuickYn("0");
		custcounselm.setQuickEndYn("0");
		custcounselm.setHcReqDate(null);
		custcounselm.setRemark("");
		custcounselm.setRefId1("");
		custcounselm.setCsSendYn("1");
		custcounselm.setSendEntpCode(panotem.getEntpCode());
		custcounselm.setCounselMedia(panotem.getPaCode());
		custcounselm.setInsertId(panotem.getInsertId());
		custcounselm.setInsertDate(panotem.getInsertDate());
		custcounselm.setProcId(panotem.getModifyId());
		custcounselm.setProcDate(panotem.getModifyDate());

		executedRtn = paCounselDAO.insertCounselCustcounselm(custcounselm);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELM INSERT" });
		}
		/*접수 데이터 생성*/
		custcounseldt = new Custcounseldt();
		custcounseldt.setCounselSeq(panotem.getCounselSeq());
		custcounseldt.setCounselDtSeq("100");
		custcounseldt.setDoFlag("10");
		custcounseldt.setTitle(panotem.getTitle());
		custcounseldt.setDisplayYn("");

		paramMap.put("paCounselNo", panotem.getPaCounselNo());
		paramMap.put("paCounselSeq", panotem.getPaCounselSeq());
		List<PaqnamVO> procNoteList = paSsgCounselDAO.selectPaSsgProcNoteList(paramMap);
		
		for(PaqnamVO procNote : procNoteList) {
			ssgProcNote += procNote.getProcNote() + "\n";
		}
		
		panotem.setProcNote(ssgProcNote);
		
		String note = ComUtil.text2db("[" + panotem.getTitle() + "]" + "\n" +  panotem.getProcNote());
		int len = note.getBytes("UTF-8").length;
		if(len > 2000) {
			custcounseldt.setProcNote( ComUtil.subStringBytes(note, 1950) + "[내용잘림]");
		} else {
			custcounseldt.setProcNote(note);
		}

		custcounseldt.setProcDate(panotem.getInsertDate());
		custcounseldt.setProcId(panotem.getInsertId());
		executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
		}

		for(PaqnamVO procNote : procNoteList) {
			panotem.setPaCounselSeq(procNote.getPaCounselSeq());
			panotem.setToken(procNote.getToken());
			executedRtn = paSsgCounselDAO.updatePaCounselSeq(panotem);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
			}
		}
		
		return rtnMsg;
	}

	@Override
	public List<PaqnamVO> selectPaNoteAnswerList(ParamMap paramMap) throws Exception {
		return paSsgCounselDAO.selectPaNoteAnswerList(paramMap);
	}
	
}
