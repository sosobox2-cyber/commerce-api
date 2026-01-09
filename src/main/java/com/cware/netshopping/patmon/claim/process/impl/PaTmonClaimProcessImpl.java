package com.cware.netshopping.patmon.claim.process.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractModel;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTmonCancelListVO;
import com.cware.netshopping.domain.PaTmonClaimListVO;
import com.cware.netshopping.domain.PaTmonRedeliveryListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.patmon.claim.process.PaTmonClaimProcess;
import com.cware.netshopping.patmon.claim.repository.PaTmonClaimDAO;

@Service("patmon.claim.paTmonClaimProcess")
public class PaTmonClaimProcessImpl extends AbstractService implements PaTmonClaimProcess{
		
	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "patmon.claim.paTmonClaimDAO")
	PaTmonClaimDAO paTmonClaimDAO;
	
	@Autowired
    private SystemService systemService;

	@Override
	public int saveTmonCancelList(List<PaTmonCancelListVO> paTmonCancelList) throws Exception {
		int executedRtn 	 = 0;
		int checkCancelCnt 	 = 0;
		int doFlag			 = 0;
		String outClaimGb	 = "";
		
		// 원주문 데이터 체크
		checkCancelCnt = paTmonClaimDAO.countOrderList(paTmonCancelList.get(0));
		if(checkCancelCnt < 1) return 0;
		
		// TPATMONCANCELLIST 중복 데이터 유무 체크
		checkCancelCnt = paTmonClaimDAO.selectPaTmonCancelListCount(paTmonCancelList.get(0));
		if(checkCancelCnt > 0) return 0;
		
		for(int i = 0; i < paTmonCancelList.size(); i++) {
			checkCancelCnt = paTmonClaimDAO.selectPaTmonCancelDtCount(paTmonCancelList.get(i));
			if(checkCancelCnt != 1) continue; 
			
			HashMap<String, Object> map = paTmonClaimDAO.selectPaTmonCancelInfo(paTmonCancelList.get(i));
			paTmonCancelList.get(i).setDeliveryNo(map.get("DELIVERY_NO").toString());
			
			if("C3".equals(paTmonCancelList.get(i).getClaimStatus())) { // 취소 완료건인 경우
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				if(paTmonCancelList.get(i).getOrderDate().length() == 16) { // Date : 00초일 경우 초단위 제외되고 들어옴
					paTmonCancelList.get(i).setOrderDate(paTmonCancelList.get(i).getOrderDate() + ":00");
				}
				if(paTmonCancelList.get(i).getCompletedDate().length() == 16) {
					paTmonCancelList.get(i).setCompletedDate(paTmonCancelList.get(i).getCompletedDate() + ":00");
				}
				
				Date orderDate  = format.parse(paTmonCancelList.get(i).getOrderDate());
				Date completedDate = format.parse(paTmonCancelList.get(i).getCompletedDate());
				
				// 취소일시와 주문일시 차이가 30분 미만일 경우 주문 생성 전 취소 건으로 판단
				// 취소 데이터 생성 필요할 경우 주문번호 넣어서 로컬처리
				if((completedDate.getTime() - orderDate.getTime()) / (60*1000) < 30) continue;
				
				// MERGE TPATMONCANCELLIST
				executedRtn = paTmonClaimDAO.mergePaTmonCancelList(paTmonCancelList.get(i));
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPATMONCANCELLIST" });
				}
				// INSERT TPAORDERM
				doFlag = Integer.parseInt(map.get("DO_FLAG").toString());
				if(doFlag < 30) {
					outClaimGb = "0";
				}else {
					outClaimGb = "1";
				}
			
				paTmonCancelList.get(i).setOutBefClaimGb(outClaimGb);
				executedRtn = insertPaOrderm(paTmonCancelList.get(i), new Paorderm());
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
				}
			} else {
				// INSERT TPATMONCANCELLIST
				executedRtn = paTmonClaimDAO.insertTpaTmonCancelList(paTmonCancelList.get(i));
				if(executedRtn != 1){
					throw processException("msg.cannot_save", new String[] { "TPATMONCANCELLIST INSERT" });
				}
			}
		}
		
		return 0;
	}

	@Override
	public int saveTmonWithdrawCancelList(List<PaTmonCancelListVO> paTmonCancelList) throws Exception {
		int executedRtn    = 0;
		int checkCancelCnt = 0;
		
		//=1) Exist Original Cancel data for withdraw
		checkCancelCnt = paTmonClaimDAO.selectPaTmonWithdrawCancelListCount(paTmonCancelList.get(0));
		if(checkCancelCnt < 1) return 0;
		
		//=2) Update TPATMONCANCELLIST.WITHDRAW_YN = 1
		executedRtn = paTmonClaimDAO.updatePaTmonCancelList4Withdraw(paTmonCancelList.get(0));
		
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCANCELLIST UPDATE" });
		
		return executedRtn;
	}

	private int insertPaOrderm(AbstractModel abstractVo, Paorderm paorderm) throws Exception {
		
		if(paorderm == null) paorderm = new Paorderm();
		
		Map<String, String> voMap = null;
		
		if(abstractVo instanceof PaTmonCancelListVO) {
			voMap = BeanUtils.describe((PaTmonCancelListVO)abstractVo);
		} else { //TODO 교환, 환불 추가 예정 (PaTmonClaimListVO) 
			voMap = BeanUtils.describe((PaTmonClaimListVO)abstractVo);
			voMap.put("outBefClaimGb","0");
		}
		voMap.put("claimQty", voMap.get("qty"));
		
		int executedRtn   = 0;
		String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb  = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");
		
		paorderm.setPaGroupCode(Constants.PA_TMON_GROUP_CODE);
		paorderm.setPaOrderGb  (paOrderGb);
		paorderm.setPaCode	   (voMap.get("paCode"));
		paorderm.setPaOrderNo  (voMap.get("tmonOrderNo"));
		paorderm.setPaOrderSeq (voMap.get("tmonDealOptionNo"));
		paorderm.setPaClaimNo  (voMap.get("claimNo"));
		paorderm.setPaShipNo   (voMap.get("deliveryNo"));
		paorderm.setPaProcQty  (String.valueOf(voMap.get("claimQty")));
		
		if("0".equals(outClaimGb)) {
			paorderm.setPaDoFlag("20");
			
		} else {
			paorderm.setPaDoFlag("60");
		}
		
		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId  (Constants.PA_TMON_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		
		return executedRtn;
	}

	@Override
	public List<Map<String, Object>> selectPaTmonOrderCancelList() throws Exception {
		return paTmonClaimDAO.selectPaTmonOrderCancelList();
	}

	@Override
	public int updateProcFlag(Map<String, Object> failData) throws Exception {
		return paTmonClaimDAO.updateProcFlag(failData);
	}
	
	@Override
	public String updatePaTmonCancelList(ParamMap paramMap) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
			
	    //=INSERT TPATMONCANCELLIST
		executedRtn = paTmonClaimDAO.updatePaTmonCancelList(paramMap);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCANCELLIST UPDATE Fail : updatePaTmonCancelList" });
	
		//=UPDATE TPAORDERM 
		executedRtn = paTmonClaimDAO.updatePaOrderMSlipOut(paramMap);
		if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE Fail : updatePaOrderMSlipOut" });
		
		return rtnMsg;
	}
	
	@Override
	public int updatePaTmonCancelConfirm(ParamMap paramMap) throws Exception {
		
		int executedRtn = 0;
		
		executedRtn = paTmonClaimDAO.updatePaTmonCancelList(paramMap);
		
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCANCELLIST INSERT" });
				
		List<PaTmonCancelListVO> voList = paTmonClaimDAO.selectPaTmonCancelList(paramMap);
		for(PaTmonCancelListVO vo : voList) {
			executedRtn = insertPaOrderm(vo, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CANCEL)" });
		}
		
		return executedRtn;
	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectCancelInputTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectCancelInputTargetDtList(paramMap);
	}
	
	@Override
	public String saveTmonClaimList(List<PaTmonClaimListVO> paTmonClaimList) throws Exception {
		//안에 절대로 Try~catch 넣지 말것!!
		int executedRtn = 0;
		int exists 		= 0;
		String rtnMsg	= Constants.SAVE_SUCCESS;
		String claimGb	= paTmonClaimList.get(0).getPaClaimGb();
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp systemTime  = DateUtil.toTimestamp(dateTime);
		
		exists = paTmonClaimDAO.countClaimList(paTmonClaimList.get(0));
		if(exists > 0) return "";
		
		for(PaTmonClaimListVO vo : paTmonClaimList) {
			vo.setInsertDate(systemTime);
			vo.setModifyDate(systemTime);
			vo.setInsertId(Constants.PA_TMON_PROC_ID);
			vo.setModifyId(Constants.PA_TMON_PROC_ID);
			
			
			HashMap<String, Object> map = paTmonClaimDAO.selectPaTmonClaimInfo(vo);
			vo.setDeliveryNo(map.get("DELIVERY_NO").toString());
			
			//=INSERT TPATMONCLAIMLIST
			executedRtn = paTmonClaimDAO.insertPaTmonClaimList(vo);
			if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATMONCLAIMLIST INSERT" });
			
			//=INSERT TPAORDERM
			switch(claimGb) {
			case "30": case "31":
				executedRtn = insertPaOrderm(vo, new Paorderm());
				if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM30)" });
				break;
			case "40": case "41":
				executedRtn = insertPaorderMForExchange(vo);
				break;
			}

			if (executedRtn < 1)  throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		return rtnMsg;
	}

	public List<Map<String,Object>> selectPaTmonReturnExchangeApprovalList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectPaTmonReturnExchangeApprovalList(paramMap);
	}
	
	public int updatePaOrderMDoFlag(Map<String,Object> paorderm) throws Exception {
		return paTmonClaimDAO.updatePaOrderMDoFlag(paorderm);
	}

	private int insertPaorderMForExchange(PaTmonClaimListVO vo) throws Exception {
		
		ParamMap paramMap = new ParamMap();
		Paorderm paorderm = null;
		int executedRtn = 0;
		
		paramMap.put("paCode", vo.getPaCode());
		paramMap.put("tmonDealOptionNo", vo.getTmonDealOptionNo());
		
		HashMap<String, Object> goodsInfo = paTmonClaimDAO.selectTmonExchangeGoodsInfo(paramMap);
		
		for(int i = 0; i < 2; i++) {
			paorderm = new Paorderm();
			
			switch(vo.getPaClaimGb()) {
			case "40" :
				paramMap = new ParamMap();
				paramMap.put("paCode", vo.getPaCode());
				paramMap.put("tmonDealNo", vo.getTmonDealNo());
				
				paorderm.setChangeFlag("01");
				paorderm.setChangeGoodsCode(goodsInfo.get("GOODS_CODE").toString());
				paorderm.setChangeGoodsdtCode(goodsInfo.get("GOODSDT_CODE").toString());
				paorderm.setPaOrderGb((i == 0) ? "40" : "45");
				break;
			case "41" :
				paorderm.setPaOrderGb((i == 0) ? "41" : "46");
				break;
			}
			
			paorderm.setPaCode		  (vo.getPaCode());
			paorderm.setPaGroupCode	  (Constants.PA_TMON_GROUP_CODE);
			paorderm.setPaOrderNo	  (vo.getTmonOrderNo());
			paorderm.setPaOrderSeq	  (vo.getTmonDealOptionNo());
			paorderm.setPaClaimNo	  (vo.getClaimNo());
			paorderm.setPaShipNo   	  (vo.getDeliveryNo());
			paorderm.setPaProcQty	  (String.valueOf(vo.getQty()));
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
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.compareAddress(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectClaimDtTargetList(String claimGb) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("claimGb", claimGb);
		
		return paTmonClaimDAO.selectClaimDtTargetList(paramMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String updatePaTmonClaimListDetail(PaTmonClaimListVO vo) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		
		if(vo.getPictures() != null && vo.getPictures().size() > 0) {
			//=UPDATE TPATMONCLAIMLIST(Pictures)
			
			List<Map<String, Object>> dealList = paTmonClaimDAO.selectClaimTmonDealList(vo);	
			for(Map<String, Object> deal : dealList) {
				vo.setTmonDealNo(deal.get("TMON_DEAL_NO").toString());
				
			    for(Entry<String, Object> elem : vo.getPictures().entrySet()){
			    	if(elem.getKey().toString().equals(vo.getTmonDealNo())) {
						List<String> pictures = (List<String>) elem.getValue();
						
						setPictures(vo, elem, pictures);
			    	}
				}
			}
			//사진 포함한 detail update
			executedRtn = paTmonClaimDAO.updatePaTmonClaimListPictures(vo);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCLAIMLIST UPDATE(DETAIL(FULL)" });
		}else {
			//=UPDATE TPATMONCLAIMLIST(Detail)
			executedRtn = paTmonClaimDAO.updatePaTmonClaimListDetail(vo);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCLAIMLIST UPDATE(DETAIL)" });
		}
		
		//=UPDATE TPAORDERM
		executedRtn = paTmonClaimDAO.updatePaOrderMDoFlag30(vo);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });

		return rtnMsg;
	}

	@Override
	public List<Map<String, Object>> selectReturnInvoiceTargetList() throws Exception {
		return paTmonClaimDAO.selectReturnInvoiceTargetList();
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrderm4PreChangeCancel(Map<String, Object> preCancelMap) throws Exception {
		return paTmonClaimDAO.updatePaOrderm4PreChangeCancel(preCancelMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		return paTmonClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaTmonExchangeRefuseList() throws Exception {
		return paTmonClaimDAO.selectPaTmonExchangeRefuseList();
	}

	@Override
	public int updatePaOrderM4ChangeRefualReuslt(ParamMap apiDataMap) throws Exception {
		return paTmonClaimDAO.updatePaOrderM4ChangeRefualReuslt(apiDataMap);
	}
	
	private void setPictures(PaTmonClaimListVO vo, Entry<String, Object> elem, List<String> pictures) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		for(int j = 0; j < pictures.size(); j++) {
			map.put(j, pictures.get(j));
		}
		
		vo.setPictures1(map.containsKey(0)?map.get(0).toString():"");
        vo.setPictures2(map.containsKey(1)?map.get(1).toString():"");
        vo.setPictures3(map.containsKey(2)?map.get(2).toString():"");
        vo.setPictures4(map.containsKey(3)?map.get(3).toString():"");
        vo.setPictures5(map.containsKey(4)?map.get(4).toString():"");
	}
	
	@Override
	public String updatePaTmonReturnList(ParamMap paramMap) throws Exception {
		int executedRtn 	 = 0;
		String rtnMsg		 = Constants.SAVE_SUCCESS;
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp systemTime  = DateUtil.toTimestamp(dateTime);
		PaTmonClaimListVO vo = new PaTmonClaimListVO();
		
	    //=INSERT TPATMONCLAIMLIST (reject_yn = 1)
		executedRtn = paTmonClaimDAO.updatePaTmonClaimList(paramMap);
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPATMONCLAIMLIST UPDATE Fail : updatePaTmonClaimList" });

		vo.setInsertDate(systemTime);
		vo.setModifyDate(systemTime);
		vo.setInsertId(Constants.PA_TMON_PROC_ID);
		vo.setModifyId(Constants.PA_TMON_PROC_ID);
		
		vo.setPaCode(paramMap.get("paCode").toString());
		vo.setClaimNo(paramMap.get("paClaimNo").toString());
		vo.setTmonOrderNo(paramMap.get("paOrderNo").toString());
		
		List<Map<String, Object>> returnRefuseList = paTmonClaimDAO.selectReturnRefuseList(paramMap);
		
		for(Map<String, Object> returnRefuse : returnRefuseList) {
			PaTmonClaimListVO claimVo = (PaTmonClaimListVO) vo.clone();
			claimVo.setPaOrderGb("31"); //환불철회
			claimVo.setDeliveryNo(returnRefuse.get("DELIVERY_NO").toString());
			claimVo.setTmonDealOptionNo(returnRefuse.get("TMON_DEAL_OPTION_NO").toString());
			claimVo.setQty(ComUtil.objToLong(returnRefuse.get("QTY")));
			
			//=INSERT TPAORDERM
			executedRtn = insertPaOrderm(claimVo, new Paorderm());
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM30)" });
		}
		
		return rtnMsg;
	}

	@Override
	public List<Map<String, Object>> selectPaTmonExchangeHoldProcList(ParamMap paramMap) throws Exception {
		return paTmonClaimDAO.selectPaTmonExchangeHoldProcList(paramMap);
	}

	@Override
	public int saveTmonRedelivaryList(List<PaTmonRedeliveryListVO> paTmonReDeliverylList) throws Exception {
		int executedRtn = 0;
		int exists 		= 0;
		String dateTime = systemService.getSysdatetimeToString();
		Timestamp systemTime  = DateUtil.toTimestamp(dateTime); 
		
		exists = paTmonClaimDAO.countRedeliveryList(paTmonReDeliverylList.get(0));
		if(exists > 0) return 0;
		
		for(PaTmonRedeliveryListVO vo : paTmonReDeliverylList) {
			vo.setInsertDate(systemTime);
			vo.setModifyDate(systemTime);
			
			HashMap<String, Object> map = paTmonClaimDAO.selectPaTmonClaimInfo4Redelivery(vo);
			vo.setDeliveryNo(map.get("DELIVERY_NO").toString());
			
			executedRtn = paTmonClaimDAO.insertPaTmonRedeliveryList(vo);
			if (executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPATMONREDELIVERYLIST INSERT" });
		}
		return 0;
	}

	@Override
	public int updatePaTmonHoldYn(Map<String, Object> exchangeHoldData) throws Exception {
		return paTmonClaimDAO.updatePaTmonHoldYn(exchangeHoldData);
	}

	@Override
	public List<Map<String, Object>> selectMappingSeq(ParamMap claimMap) throws Exception {
		return paTmonClaimDAO.selectMappingSeq(claimMap);
	}
	
	@Override
	public List<String> selectPaTmonReturnRequestedDateList() throws Exception {
		return paTmonClaimDAO.selectPaTmonReturnRequestedDateList();
	}

}
