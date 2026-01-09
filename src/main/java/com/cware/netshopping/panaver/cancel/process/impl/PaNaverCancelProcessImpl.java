package com.cware.netshopping.panaver.cancel.process.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.cancel.process.PaNaverCancelProcess;
import com.cware.netshopping.panaver.cancel.repository.PaNaverCancelDAO;
import com.cware.netshopping.panaver.delivery.repository.PaNaverDeliveryDAO;
import com.cware.netshopping.panaver.infocommon.repository.PaNaverInfoCommonDAO;


@Service("panaver.cancel.paNaverCancelProcess")
public class PaNaverCancelProcessImpl extends AbstractProcess implements PaNaverCancelProcess {

	@Autowired
	private PaNaverCancelDAO paNaverCancelDAO;
	
	@Autowired
	private PaNaverDeliveryDAO paNaverDeliveryDAO;
	
	@Autowired
	private PaNaverInfoCommonDAO paNaverInfoCommonDAO;
	
	
	/**
	 * 제휴 - 네이버 판매불가처리 - 처리결과 update
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String saveCancelSale(ParamMap paramMap) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			
			executedRtn = paNaverCancelDAO.updateCancelSale(paramMap);
			
			if(executedRtn == 1){
				log.info("TPAORDERM update success");
			} else {
				log.info("TPAORDERM update fail");
				log.info("ordNo : " + paramMap.getString("ordNo") + ", ordPrdSeq : " + paramMap.getString("ordPrdSeq"));
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}

		return rtnMsg;
	}
	
	//19.09.25
	@Override
	public HashMap<String, Object> selectPaNaverOrdCancelApprovalList(ParamMap cancelMap) throws Exception {
		return paNaverCancelDAO.selectPaNaverOrdCancelApprovalList(cancelMap);
	}
	
	@Override
	public String saveCancelApprovalProc(HashMap<String, Object> cancelMap) throws Exception {
		Paorderm paorderm = null;
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String rtnMsg = Constants.SAVE_SUCCESS;

		String claimSeq = paNaverInfoCommonDAO.selectClaimSeq();
		ParamMap paramMap = new ParamMap();
		paramMap.put("productOrderID", cancelMap.get("PA_ORDER_SEQ"));
		paramMap.put("claimSeq", claimSeq);
		paramMap.put("claimID", cancelMap.get("CLAIM_ID"));
		if(paNaverCancelDAO.insertCancelDoneClaim(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERCLAIMLIST INSERT" });
		}
		paramMap.put("claimType", "CANCEL");
		paramMap.put("claimStatus", "CANCEL_DONE");
		if(paNaverInfoCommonDAO.updateOrderListClaimInfo(paramMap) != 1) {
			throw processException("msg.cannot_save", new String[] { "TPANAVERORDERLIST UPDATE" });
		}
		
		paorderm = new Paorderm();
		paorderm.setPaCode(cancelMap.get("PA_CODE").toString());
		paorderm.setPaOrderGb("20");
		paorderm.setPaOrderNo(cancelMap.get("PA_ORDER_NO").toString());
		paorderm.setPaOrderSeq(cancelMap.get("PA_ORDER_SEQ").toString());
		paorderm.setPaShipNo(cancelMap.get("PA_SHIP_NO").toString());
		paorderm.setPaClaimNo(cancelMap.get("CLAIM_ID").toString());
		paorderm.setPaProcQty(cancelMap.get("PA_PROC_QTY").toString());
		paorderm.setApiResultCode(cancelMap.get("resultCode").toString());
		paorderm.setApiResultMessage(cancelMap.get("resultMessage").toString());
		paorderm.setPaDoFlag("20");
		paorderm.setOutBefClaimGb(cancelMap.get("outBefClaimGb").toString());
		paorderm.setPaGroupCode("04");
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId("PANAVER");
		//= TPAORDERM INSERT
		//= 취소승인처리 결과 INSERT
		executedRtn = paNaverCancelDAO.insertPaOrderM(paorderm);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
		return rtnMsg;
	}

	/**
	 * 제휴 - 네이버 취소승인대상 조회(리스트)
	 * @param 
	 * @return List<Object>
	 * @throws Exception
	 */
	@Override
	public List<Object> selectPaNaverOrdCancelListApprovalList() throws Exception{
		return paNaverCancelDAO.selectPaNaverOrdCancelListApprovalList();
	}
	
	@Override
	public List<HashMap<String, Object>> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverCancelDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public int updatePreCancelYn(ParamMap paramMap) throws Exception {
		return paNaverCancelDAO.updatePreCancelYn(paramMap);
	}
}
