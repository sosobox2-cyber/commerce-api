package com.cware.netshopping.pakakao.counsel.process.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.pacommon.counsel.repository.PaCounselDAO;
import com.cware.netshopping.pakakao.counsel.process.PaKakaoCounselProcess;
import com.cware.netshopping.pakakao.counsel.repository.PaKakaoCounselDAO;

@Service("pakakao.counsel.paKakaoCounselProcess")
public class PaKakaoCounselProcessImpl extends AbstractService implements PaKakaoCounselProcess{

	@Resource(name = "pakakao.counsel.paKakaoCounselDAO")
	private PaKakaoCounselDAO paKakaoCounselDAO;
	
	@Resource(name = "pacommon.counsel.paCounselDAO")
    private PaCounselDAO paCounselDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;


	@Override
	public void savePaQna(List<PaqnamVO> paQnaList) throws Exception {
		int executedRtn   	  = 0;
		
		for (PaqnamVO paQnam : paQnaList) {
			String subQuestMoment = ComUtil.subStringBytes(paQnam.getProcNote(), 2000);
			paQnam.setProcNote(subQuestMoment);
			
			int checkPaQnaCnt = paKakaoCounselDAO.checkPaQnam(paQnam);
			if(checkPaQnaCnt > 0) continue;
			
			String paCounselSeq = paCounselDAO.selectPaCounselNewSeq();
			paQnam.setPaCounselSeq(paCounselSeq);
			
			//= tpaqnam insert
			executedRtn = paKakaoCounselDAO.insertPaQnaM(paQnam);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNAM INSERT" });
			
			//= tpaqnadt insert
			executedRtn = paKakaoCounselDAO.insertPaQnaDt(paQnam);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNADT INSERT" });
		}
	}
	
	@Override
	public void saveCustCounsel() throws Exception {
		List<PaqnamVO> custCounselList = paKakaoCounselDAO.selectCustCounselList();
		
		Custcounselm custcounselm = null;
		Custcounseldt custcounseldt = null;
		int executedRtn = 0;
		
		for(int i=0; i<custCounselList.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("sequence_type", "COUNSEL_SEQ");
			String counselSeq = (String)systemDAO.selectSequenceNo(hashMap);
			if (counselSeq.equals("")) {
				throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
			}
			
			custcounselm = new Custcounselm();
			custcounselm.setCounselSeq(counselSeq);
			custcounselm.setCustNo(custCounselList.get(i).getCustNo());
			custcounselm.setDoFlag("25");
			custcounselm.setRefNo1(custCounselList.get(i).getRefNo());
			custcounselm.setCsLgroup(custCounselList.get(i).getCsLgroup());
			custcounselm.setCsMgroup(custCounselList.get(i).getCsMgroup());
			custcounselm.setCsSgroup(custCounselList.get(i).getCsSgroup());
			custcounselm.setCsLmsCode(custCounselList.get(i).getCsLmsCode());
			custcounselm.setOutLgroupCode("99");
			custcounselm.setOutMgroupCode("99");
			custcounselm.setGoodsCode(custCounselList.get(i).getGoodsCode());
			custcounselm.setGoodsdtCode("");
			custcounselm.setClaimNo(counselSeq);
			custcounselm.setTel("");
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
			custcounselm.setSendEntpCode(custCounselList.get(i).getEntpCode());
			custcounselm.setCounselMedia(custCounselList.get(i).getPaCode());
			custcounselm.setInsertId(custCounselList.get(i).getInsertId());
			custcounselm.setInsertDate(custCounselList.get(i).getInsertDate());
			custcounselm.setProcId(custCounselList.get(i).getInsertId());
			custcounselm.setProcDate(custCounselList.get(i).getInsertDate());
			
			executedRtn = paCounselDAO.insertCounselCustcounselm(custcounselm);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELM INSERT" });
			}
			
			custcounseldt = new Custcounseldt();
			custcounseldt.setCounselSeq(counselSeq);
			custcounseldt.setCounselDtSeq("100");
			custcounseldt.setDoFlag("10");
			custcounseldt.setTitle(custCounselList.get(i).getCsMgroupName() + "[" + custCounselList.get(i).getCsSgroupName() + "]");
			custcounseldt.setDisplayYn("");

			String note = custCounselList.get(i).getProcNote();
			int len = note.getBytes("UTF-8").length;
			if(len > 2000) {
				custcounseldt.setProcNote( ComUtil.subStringBytes(note, 1950) + "[내용잘림]");
			} else {
				custcounseldt.setProcNote(note);
			}
			
			custcounseldt.setProcDate(custCounselList.get(i).getInsertDate());
			custcounseldt.setProcId(custCounselList.get(i).getInsertId());
			executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
			}
			
			custcounseldt.setCounselDtSeq("101");
			custcounseldt.setDoFlag("25");
			custcounseldt.setDisplayYn("1");
			custcounseldt.setReceiverSeq("0000000001");
			executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
			}
			
			custCounselList.get(i).setCounselSeq(counselSeq);
			executedRtn = paCounselDAO.updatePaQnaCounselSeq(custCounselList.get(i));
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
			}
		}		
	}
	
	@Override
	public List<PaqnamVO> selectPaAnserQnaList(ParamMap paramMap) throws Exception {
		return paKakaoCounselDAO.selectPaAnserQnaList(paramMap);
	}
	
	@Override
	public void savePaQnaTrans(PaqnamVO paQna) throws Exception {
		int executedRtn   	  = 0;
		String paCounselDtSeq = "";
		
		executedRtn = paKakaoCounselDAO.updatePaQnaTrans(paQna);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
		
		paCounselDtSeq = paKakaoCounselDAO.selectPaQnaDtMaxSeq(paQna);
		paQna.setPaCounselDtSeq(paCounselDtSeq);
		
		executedRtn = paKakaoCounselDAO.insertPaQnaDt(paQna);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAQNADT INSERT" });
	}
}
