package com.cware.netshopping.pakakao.claim.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaKakaoOrderListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pakakao.claim.process.PaKakaoClaimProcess;
import com.cware.netshopping.pakakao.claim.repository.PaKakaoClaimDAO;
import com.cware.netshopping.pakakao.util.PaKakaoComUtill;

@Service("pakakao.claim.paKakaoClaimProcess")
public class PaKakaoClaimProcessImpl  extends AbstractService implements PaKakaoClaimProcess{
	
	@Autowired
	private PaKakaoClaimDAO paKakaoClaimDAO;
	@Autowired
	private PaCommonDAO paCommonDAO;

	@Override
	public void saveKakaoCancelList(PaKakaoOrderListVO vo) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
				
		// 원주문 데이터 체크
		checkCancelCnt = paKakaoClaimDAO.countOrderList(vo);
		if(checkCancelCnt < 1) return ;
		
		//카카오 주문번호로 다른 취소 건 체크 (존재하는 경우 철회된 것)
		checkCancelCnt = paKakaoClaimDAO.selectWithdrawCancelCount(vo);
		if(checkCancelCnt > 0) {
			executedRtn = paKakaoClaimDAO.updatePaKakaoCancelWithdrawYn(vo);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST UPDATE(updatePaKakaoCancelWithdrawYn)" });
		}
		
		// TPAKAKAOCANCELLIST 중복 데이터 유무 체크
		checkCancelCnt = paKakaoClaimDAO.selectPaKakaoCancelListCount(vo);
		if(checkCancelCnt > 0) {
			if("21".equals(vo.getClaimStatus())) {
				checkCancelCnt = paKakaoClaimDAO.selectPaKakaoCancelMListCount(vo);
				if(checkCancelCnt < 1) return;

				executedRtn = paKakaoClaimDAO.updatePaKakaoCancelComplete(vo);
				if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST UPDATE(updatePaKakaoCancelComplete)" });
			}
		}else {
			// INSERT TPAKAKAOCANCELLIST
			executedRtn = paKakaoClaimDAO.insertTpaKakaoCancelList(vo);
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST INSERT(insertTpaKakaoCancelList)" });
		}
		
		if("21".equals(vo.getClaimStatus())) {
			executedRtn = insertPaOrderm(vo, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAORDERM INSERT" });
		}
	}
	
	private int insertPaOrderm(AbstractModel abstractVo, Paorderm paorderm) throws Exception {
		
		if(paorderm == null) paorderm = new Paorderm();
		
		Map<String, String> voMap = null;
		
		voMap = BeanUtils.describe((PaKakaoOrderListVO)abstractVo);
		
		// 직권취소인 경우 doFlag 확인 후 outBefClaimGb setting
		if("21".equals(voMap.get("claimStatus"))) {
			HashMap<String, Object> cancel = paKakaoClaimDAO.selectPaKakaoCancelInfo((PaKakaoOrderListVO)abstractVo);
			int doFlag = Integer.parseInt(cancel.get("DO_FLAG").toString());
			if(doFlag < 30) {
				voMap.put("outBefClaimGb","0");
			} else {
				voMap.put("outBefClaimGb","1");
			}
		} else {
			if( voMap.get("outBefClaimGb") == null || "".equals(voMap.get("outBefClaimGb")) )voMap.put("outBefClaimGb","0");
		}
		
		
		int executedRtn   = 0;
		String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
		
		paorderm.setPaClaimNo  (voMap.get("claimId"));
		paorderm.setPaOrderNo  (voMap.get("paymentId"));
		paorderm.setPaOrderSeq (voMap.get("id"));
		paorderm.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
		paorderm.setPaOrderGb  (voMap.get("paOrderGb"));
		paorderm.setPaCode	   (voMap.get("paCode"));
		
		paorderm.setPaProcQty  (String.valueOf(voMap.get("quantity")));
		
		if("ReturnCancelComplete".equals(voMap.get("status").toString())) { //반품완료
			paorderm.setPaDoFlag("60");
		}else if("0".equals(voMap.get("outBefClaimGb"))) {
			paorderm.setPaDoFlag("20");
		} else {
			paorderm.setPaDoFlag("60");
		}
		
		paorderm.setOutBefClaimGb(voMap.get("outBefClaimGb"));
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId  (Constants.PA_KAKAO_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return executedRtn;
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoOrderCancelList() throws Exception {
		return paKakaoClaimDAO.selectPaKakaoOrderCancelList();
	}
	
	@Override
	public String updatePaKakaoCancelConfirm(ParamMap paramMap) throws Exception {
		
		int executedRtn = 0;
		String rtnMsg	= Constants.SAVE_SUCCESS;
		
		executedRtn = paKakaoClaimDAO.updatePaKakaoCancelList(paramMap);
		
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { paramMap.get("claimId")+":TPAKAKAOCANCELLIST INSERT" });
				
		PaKakaoOrderListVO vo = paKakaoClaimDAO.selectPaKakaoCancelList(paramMap);
		if("1".equals(ComUtil.objToStr(paramMap.get("outBefClaimGb")))) {
			vo.setOutBefClaimGb("1");
		}
		executedRtn = insertPaOrderm(vo, new Paorderm());
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { paramMap.get("claimId")+":TPAORDERM INSERT(CANCEL)" });
		
		
		return rtnMsg;
	}

	@Override
	public String updatePaKakaoCancelList(ParamMap paramMap) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
			
	    //=INSERT TPAKAKAOCANCELLIST
		executedRtn = paKakaoClaimDAO.updatePaKakaoCancelList(paramMap);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { paramMap.get("claimId")+":TPAKAKAOCANCELLIST UPDATE Fail(updatePaKakaoCancelList)" });
	
		//=UPDATE TPAORDERM 
		executedRtn = paKakaoClaimDAO.updatePaOrderMSlipOut(paramMap);
		if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { paramMap.get("claimId")+":TPAORDERM UPDATE Fail(updatePaOrderMSlipOut)" });
		
		return rtnMsg;
	}
	
	@Override
	public void saveKakaoWithdrawCancelList(PaKakaoOrderListVO vo) throws Exception {
		int executedRtn    = 0;
		int checkCancelCnt = 0;
		String withdrawYn = "";
		
		//카카오 주문번호로 다른 취소 건 체크 (존재하는 경우 철회된 것)
		checkCancelCnt = paKakaoClaimDAO.selectWithdrawCancelCount(vo);
		if(checkCancelCnt > 0) {
			executedRtn = paKakaoClaimDAO.updatePaKakaoCancelWithdrawYn(vo);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST UPDATE(updatePaKakaoCancelWithdrawYn)" });
		}
				
		//=1) Exist Original Cancel data for withdraw
		withdrawYn = paKakaoClaimDAO.selectPaKakaoWithdrawCancelListCheck(vo);
		if("1".equals(withdrawYn)) {
			return;
		}else if("0".equals(withdrawYn)) {
			//=2) Update TPAKAKAOCANCELLIST.WITHDRAW_YN = 1
			executedRtn = paKakaoClaimDAO.updatePaKakaoCancelList4Withdraw(vo);
			if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST UPDATE(updatePaKakaoCancelList4Withdraw)" });
		}else {
			// INSERT TPAKAKAOCANCELLIST WITHDRAW_YN = 1
			executedRtn = paKakaoClaimDAO.insertTpaKakaoCancelList(vo);
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCANCELLIST INSERT(insertTpaKakaoCancelList)" });
		}
	}
	
	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectClaimTargetList(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectCancelInputTargetDtList(paramMap);
	}

	@Override
	public String saveKakaoClaimList(PaKakaoOrderListVO vo) throws Exception {

		//안에 절대로 Try~catch 넣지 말것!!
		int executedRtn   = 0;
		int exists 		  = 0;
		int checkClaimCnt = 0;
		String rtnMsg	  = Constants.SAVE_SUCCESS;
		String claimGb	  = vo.getPaClaimGb();
		
		
		if("31".equals(claimGb) || "41".equals(claimGb)) {
			// 원클레임 데이터 체크
			checkClaimCnt = paKakaoClaimDAO.countOrgClaimList(vo);
			if(checkClaimCnt < 1) return "";
		}
		
		//중복체크
		exists = paKakaoClaimDAO.countClaimList(vo);
		if(exists > 0) return "";
		
		//카카오 주문번호로 클레임 번호 다른 반품, 교환 건 체크 (존재하는 경우 철회된 것)
		List<Map<String, Object>> claimList = paKakaoClaimDAO.selectWithdrawClaimCount(vo);
		for(Map<String, Object> claimMap : claimList) {
			PaKakaoOrderListVO claimVo = (PaKakaoOrderListVO)PaKakaoComUtill.dbMap2VO(claimMap, PaKakaoOrderListVO.class);
			claimVo.setInsertDate(vo.getInsertDate());
			claimVo.setInsertId(Constants.PA_KAKAO_PROC_ID);
			claimVo.setModifyDate(vo.getModifyDate());
			claimVo.setModifyId(Constants.PA_KAKAO_PROC_ID);
			
			executedRtn = paKakaoClaimDAO.insertPaKakaoClaimList(claimVo);
		    if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+ "|" +claimVo.getClaimId() + ":TPAKAKAOCLAIMLIST INSERT(WITHDRAWCLAIM)" });
		    
		    if("31".equals(claimVo.getPaOrderGb())) {
		    	executedRtn = insertPaOrderm(claimVo, new Paorderm());
		    }else {
		    	executedRtn = insertPaorderMForExchange(claimVo);
		    }
		    if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+ "|" +claimVo.getClaimId() + ":TPAORDERM INSERT(WITHDRAWCLAIM)" });
		}
		
		
	    //=INSERT TPAKAKAOCLAIMLIST
	    executedRtn = paKakaoClaimDAO.insertPaKakaoClaimList(vo);
	    if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAKAKAOCLAIMLIST INSERT" });
	    
	    //=INSERT TPAORDERM
	    switch(claimGb) {
	    case "30": case "31":
	    	executedRtn = insertPaOrderm(vo, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAORDERM INSERT(CLAIM30)" });
			break;
	    case "40": case "41":
	    	executedRtn = insertPaorderMForExchange(vo);
	    	break;
	    }
	    if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { vo.getClaimId()+":TPAORDERM INSERT" });
		
		return rtnMsg;
	}
		
	private int insertPaorderMForExchange(PaKakaoOrderListVO vo) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		Paorderm paorderm = null;
		int executedRtn = 0;
		int goodsCnt	= 0;
		
		paramMap.put("paCode", vo.getPaCode());
		/* 상품 등록 후 옵션번호 넣도록 수정해야함 */
		paramMap.put("sellerItemOptionCode", vo.getSellerItemOptionCode()); 
		
		HashMap<String, Object> goodsInfo = paKakaoClaimDAO.selectKakaoExchangeGoodsInfo(paramMap);
		/*/상품 등록 후 옵션번호 넣도록 수정해야함 */
		
		for(int i = 0; i < 2; i++) {
			paorderm = new Paorderm();
			
			switch(vo.getPaClaimGb()) {
			case "40" :				
				paramMap.put("goodsCode", goodsInfo.get("GOODS_CODE").toString());
				goodsCnt = paKakaoClaimDAO.selectGoodsdtInfoCount(paramMap);
				
				if(goodsCnt > 1) {//단품이 여러개이면 텍스트 보고 상담원이 직접 입력
					paorderm.setChangeFlag("00");
					paorderm.setChangeGoodsCode(goodsInfo.get("GOODS_CODE").toString());
					paorderm.setChangeGoodsdtCode("");
				}else if(goodsCnt == 1){
					paorderm.setChangeFlag("01");
					paorderm.setChangeGoodsCode(goodsInfo.get("GOODS_CODE").toString());
					paorderm.setChangeGoodsdtCode(goodsInfo.get("GOODSDT_CODE").toString());
				}else {
					throw processException("msg.msg.cannot_validity_good_code("+goodsInfo.get("GOODS_CODE").toString()+")");
				}
				
				paorderm.setPaOrderGb((i == 0) ? "40" : "45");
				break;
			case "41" :
				paorderm.setPaOrderGb((i == 0) ? "41" : "46");
				break;
			}
			
			paorderm.setPaCode		  (vo.getPaCode());
			paorderm.setPaGroupCode	  (Constants.PA_KAKAO_GROUP_CODE);
			paorderm.setPaOrderNo	  (vo.getPaymentId());
			paorderm.setPaOrderSeq	  (vo.getId());
			paorderm.setPaClaimNo	  (vo.getClaimId());
			paorderm.setPaProcQty	  (String.valueOf(vo.getQuantity()));
			paorderm.setPaDoFlag	  ("20");
			paorderm.setOutBefClaimGb ("0");
			paorderm.setInsertDate	  (vo.getInsertDate());
			paorderm.setModifyDate	  (vo.getModifyDate());
			paorderm.setModifyId	  (vo.getModifyId());
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0)  throw processException("msg.cannot_save", new String[] { "TPAORDERM(40, 41) INSERT" });
		}
		return executedRtn;
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.compareAddress(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}
	
	
	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		
		return paKakaoClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}
	
	@Override
	public HashMap<String, Object> selectRefusalInfo(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectRefusalInfo(paramMap);
	}
	
	@Override
	public int updatePreCanYn(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.updatePreCanYn(paramMap);
	}
	
	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimInvTgList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectPaKakaoClaimInvTgList(paramMap);
	}
	
	@Override
	public int updatePaOrderMDoFlag(Map<String, Object> map) throws Exception {
		return paKakaoClaimDAO.updatePaOrderMDoFlag(map);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimCollCmpTgList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectPaKakaoClaimCollCmpTgList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoReturnCmpTgList() throws Exception {
		return paKakaoClaimDAO.selectPaKakaoReturnCmpTgList();
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoExchangeSlipOutList() throws Exception {
		return paKakaoClaimDAO.selectPaKakaoExchangeSlipOutList();
	}
	
	@Override
	public int countCancelList(String orderId, String claimStatus) throws Exception {
		ParamMap param = new ParamMap();
		param.put("orderId", orderId);
		param.put("claimStatus", claimStatus);
		
		return paKakaoClaimDAO.countCancelList(param);
	}

	@Override
	public void updateKakaoExchangeComplete(PaKakaoOrderListVO vo) throws Exception {
		int executedRtn 	 = 0;
		
		Map<String, Object> map = setExchangeComplete(vo);
		
		executedRtn = paKakaoClaimDAO.selectExchangeCompleteCount(map);
		if(executedRtn > 0) return;
		
		executedRtn = paKakaoClaimDAO.updatePaOrderMDoFlag(map);
		if (executedRtn < 1) {		
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE(updateKakaoExchangeComplete)" });
		}
		
	}
	
	private Map<String, Object> setExchangeComplete(PaKakaoOrderListVO vo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PA_DO_FLAG","80");
		map.put("API_RESULT_CODE","000000");
		map.put("API_RESULT_MESSAGE","교환완료");
		map.put("PA_CLAIM_NO", vo.getClaimId());
		map.put("PA_CODE", vo.getPaCode());
		map.put("PA_ORDER_SEQ", vo.getId());
		map.put("PA_ORDER_GB", "40");
    
		return map;
	}
	
	@Override
	public List<Map<String, Object>> selectPaKakaoClaimHoldTargetList(ParamMap paramMap) throws Exception {
		return paKakaoClaimDAO.selectPaKakaoClaimHoldTargetList(paramMap);
	}
	
	@Override
	public int updatePaOrderMHoldYn(Map<String, Object> map) throws Exception {
		return paKakaoClaimDAO.updatePaOrderMHoldYn(map);
	}

	@Override
	public List<HashMap<String, String>> selectPaMobileOrderAutoCancelList() throws Exception {
		return paKakaoClaimDAO.selectPaMobileOrderAutoCancelList();
	}
}
