package com.cware.netshopping.panaver.claim.process.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.claim.process.PaNaverClaimProcess;
import com.cware.netshopping.panaver.claim.repository.PaNaverClaimDAO;

@Service("panaver.claim.paNaverClaimProcess")
public class PaNaverClaimProcessImpl extends AbstractProcess implements PaNaverClaimProcess{

	@Autowired
	PaNaverClaimDAO paNaverClaimDAO;
	
	@Override
	public List<Object> selectPaNaverClaimApprovalList() throws Exception {
		return paNaverClaimDAO.selectPaNaverClaimApprovalList();
	}

	/**
	 * 제휴 - 네이버 반품승인처리 - 반품승인처리
	 * @param ParamMap
	 * @return String
	 * @throws Exception
	 */
	@Override
	public ParamMap saveClaimApprovalProc(HashMap<String, String> claimMap) throws Exception {
		Paorderm paorderm = null;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		ParamMap resultMap = new ParamMap();
		try{//반품접수대상 : '1' , 출고전 반품처리대상 : '2' ==> 실제 데이터 통신시 값 확인할 것
			if( claimMap.get("OUT_BEF_CLAIM_GB").equals("1")) {
				resultMap.put("result_code","0");				
				resultMap.put("paDoFlag","60");
				resultMap.put("result_text","출고전 반품 성공");
			} else if( claimMap.get("PA_DO_FLAG").equals("90")) {
				resultMap.put("result_code","0");				
				resultMap.put("paDoFlag","60");
				resultMap.put("result_text","직권취소 성공");
			} else {
				resultMap.put("result_text", "반품승인처리 성공");
				resultMap.put("paDoFlag", "60");
			}
			paorderm = new Paorderm();
			paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ"));
			paorderm.setPaCode(claimMap.get("PA_CODE"));
			paorderm.setPaOrderGb(claimMap.get("PA_ORDER_GB"));
			paorderm.setPaOrderNo(claimMap.get("PA_ORDER_NO"));
			paorderm.setPaOrderSeq(claimMap.get("PA_ORDER_SEQ"));
			paorderm.setPaClaimNo(claimMap.get("PA_CLAIM_NO"));
			paorderm.setPaDoFlag(resultMap.getString("paDoFlag"));
			paorderm.setApiResultCode("000000");
			paorderm.setApiResultMessage(resultMap.getString("result_text"));
			paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
			paorderm.setModifyId("PANAVER");
			
			if (paNaverClaimDAO.updatePaOrdermResult(paorderm) != 1) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
			}
			else {
				resultMap.put("result_text", "반품승인처리 성공");
				resultMap.put("paDoFlag", "60");
			}
			
		}catch(Exception e){
			log.info(getMessage("errors.process") + " : fail ["+claimMap.get("PA_ORDER_NO")+"/"+claimMap.get("PA_ORDER_SEQ")+"]");
		}
		return resultMap;
	}

	@Override
	public List<Object> selectReturnHoldList() throws Exception {
		return paNaverClaimDAO.selectReturnHoldList();
	}

	@Override
	public String saveReturnHoldProc(HashMap<String, Object> claimMap) throws Exception {
//		Paorderm paorderm = new Paorderm();
//		ParamMap resultMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
//		int executeRtn = 0;
//			executeRtn = paNaverClaimDAO.updatePaOrdermHoldYn(paorderm);
//			if(executeRtn != 1){
//				throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
//			}
			log.info(getMessage("errors.process") + " : IF_PA11STAPI_03_008 fail ["+claimMap.get("PA_ORDER_NO")+"/"+claimMap.get("PA_ORDER_SEQ")+"]");
			claimMap.put("result_code", "-9999");
//			claimMap.put("result_text", e.getMessage());
			claimMap.put("PROC_FLAG","90");
//			rtnMsg = e.getMessage();	
		return rtnMsg;
	}

	@Override
	public List<Object> selectReleaseReturnHoldList() throws Exception {
		return paNaverClaimDAO.selectReleaseReturnHoldList();
	}

	@Override
	public int updatePaOrdermHoldYn(Paorderm paorderm) throws Exception {
		return paNaverClaimDAO.updatePaOrdermHoldYn(paorderm);
	}
	
	@Override
	public HashMap<String, String> selectExchangeRejectInfo(String mappingSeq) throws Exception {
		return paNaverClaimDAO.selectExchangeRejectInfo(mappingSeq);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetList(ParamMap paramMap) throws Exception {
		return paNaverClaimDAO.selectReturnClaimTargetList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt30List(ParamMap paramMap) throws Exception {
		return paNaverClaimDAO.selectReturnClaimTargetDt30List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnClaimTargetDt20List(ParamMap paramMap) throws Exception {
		return paNaverClaimDAO.selectReturnClaimTargetDt20List(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectReturnCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paNaverClaimDAO.selectReturnCancelTargetDtList(paramMap);
	}
	
}
