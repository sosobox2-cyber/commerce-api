package com.cware.netshopping.pagmkt.claim.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.claim.service.PaClaimService;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.claim.process.PaGmktClaimProcess;
import com.cware.netshopping.pagmkt.claim.repository.PaGmktClaimDAO;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktDateUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

@Service("pagmkt.claim.PaGmktClaimProcess")
public class PaGmktClaimProcessImpl extends AbstractService implements PaGmktClaimProcess{

	@Resource(name = "pagmkt.claim.PaGmktClaimDAO")
	private PaGmktClaimDAO PaGmktClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Resource(name = "common.system.systemDAO")
	private SystemDAO systemDAO;
	
	@Resource(name = "pacommon.claim.paclaimService")
	private PaClaimService paclaimService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Override
	public List<Object> selectOrderClaimTargetList(ParamMap paramMap) throws Exception{
		return PaGmktClaimDAO.selectOrderClaimTargetList(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetList(ParamMap paramMap) throws Exception{
		return PaGmktClaimDAO.selectClaimCancelTargetList(paramMap);
	}

	@Override
	public int updateReturnPickup(Map<String, String> rtnMap)	throws Exception {
	    return PaGmktClaimDAO.updateReturnPickup(rtnMap);
	}
	
	@Override
	public List<Object> selectReturnConfirmList(ParamMap paramMap) throws Exception {
	    return PaGmktClaimDAO.selectReturnConfirmList(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectReturnPickup50List(ParamMap paramMap) throws Exception {
	    return PaGmktClaimDAO.selectReturnPickup50List(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectReturnPickup60List(ParamMap paramMap) throws Exception {
	    return PaGmktClaimDAO.selectReturnPickup60List(paramMap);
	}
	
	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return PaGmktClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return PaGmktClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}
	
	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return PaGmktClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}
	
	@Override
	public String selectGoodsdtChangeFlag(PaGmkClaim pagmkclaim) throws Exception{
		return PaGmktClaimDAO.selectGoodsdtChangeFlag(pagmkclaim);
	}

	@Override
	public int saveReturnConfirmProc(PaGmktAbstractRest rest,	ParamMap paramMap, HashMap<String, Object> returnMap) throws Exception {
		if(returnMap == null || returnMap.size() < 1) return 0;
		
		String returnStatus = "";
		int    executedRtn  = 0;
		String resultCode = "0";
		String resultText = "";
		String resultDoFlag = "60";
		
		String outClaimGb = returnMap.get("OUT_BEF_CLAIM_GB").toString();
		String originalDoFlag = returnMap.get("PA_DO_FLAG").toString();
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		
		switch(returnMap.get("PA_ORDER_GB").toString()){
		case "20": 			
			if(outClaimGb.equals("1")) returnStatus = "ReturnBeforeShipping";
			if(outClaimGb.equals("2")) returnStatus = "CancleAfterShipping";
			if(outClaimGb.equals("0") && originalDoFlag.equals("90")) returnStatus = "CancelByCS"; // 이경우를 타는 경우가 있을까??
			break;			
		case "30": 	
			returnStatus =  "Return";
			break;
		case "40": case"45":
			returnStatus =  "ExchangeReturn";
			break;
		}
		
		
		switch(returnStatus){
	
			case "Return":
				resultText = "반품 승인 완료";
				
				paramMap.put("paCode"		, returnMap.get("PA_CODE"));
				paramMap.put("urlParameter"	, returnMap.get("PA_ORDER_SEQ"));
				
				try{
					String response = restUtil.getConnection(rest,  paramMap); 
					if(response.contains("반품수거완료(환불대기)상태로")) throw processException("errors.process_fail", new String[] { response });
				}catch(Exception e){
					resultCode = "999999"; 
					resultDoFlag = originalDoFlag;
					resultText = e.getMessage().length() > 1950 ? e.getMessage().substring(0,1950) : e.getMessage() ;
				}
				
				break;
				
			case "ReturnBeforeShipping" :
				
				resultText = "출고전 반품 완료 성공";
				break;

			case "CancleAfterShipping" :
				resultText = "출고 후 취소 완료 성공";
				break;

			case "CancelByCS" :
				resultText = "직권 취소 완료 성공";
				break;
				
			case "ExchangeReturn" :
				resultText = "교환 수거 완료 성공";
				
				paramMap.put("paCode"				, returnMap.get("PA_CODE").toString());
				paramMap.put("DELY_COMPLETE_DATE"	, ComUtil.NVL(returnMap.get("SLIP_PROC_DATE")).toString());
				paramMap.put("urlParameter"			, returnMap.get("PA_ORDER_SEQ").toString());
				
				try{
					restUtil.getConnection(rest,  paramMap); 
				}catch(Exception e){
					resultCode = "999999";  
					resultDoFlag = originalDoFlag;
					resultText = e.getMessage().length() > 1950 ? e.getMessage().substring(0,1950) : e.getMessage() ;
				}
				break;

			default :	
				resultDoFlag = originalDoFlag;
				resultText = "실패";
				resultCode = "999999";
				resultText = "실패 default 영역";
		}
		
		//= Update paOrderM
		Paorderm paorderm = new Paorderm();
		paorderm.setMappingSeq(returnMap.get("MAPPING_SEQ").toString());
		paorderm.setApiResultCode(resultCode);
		paorderm.setApiResultMessage(resultText);
		paorderm.setPaDoFlag(resultDoFlag);
		paorderm.setProcDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		
		if(resultText.contains("이미")) {
			paorderm.setApiResultCode("0");
			paorderm.setApiResultMessage("(자동완료)" + resultText);
			paorderm.setPaDoFlag("60");
		}

		if(resultText.contains("반품수거완료(환불대기)상태로")) {
			paorderm.setApiResultCode("999999");
			paorderm.setApiResultMessage(resultText);
			paorderm.setPaDoFlag("50");
		}
		
		
		//= TPAORDERM UPDATE
		executedRtn = PaGmktClaimDAO.updatePaOrdermResult(paorderm);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM UPDATE" });
		}
		
		if(resultCode.equals("999999")){
			return 0;
		}else{
			return executedRtn;
		}
		
	}
	
	private boolean checkValidationForInsertingClaimInfo(PaGmkClaim pagmkclaim) throws Exception{
		
		int existsExchange = 0;
		int existsReturn   = 0;
		int existsCnt   = 0;
		int availbleRtnCnt	= 1;
		
		existsCnt = PaGmktClaimDAO.checkPaGmktClaimList(pagmkclaim);
		if(existsCnt > 0) return false; //= 이미 수집된 데이터 일 경우 skip.
		
	
		switch(pagmkclaim.getPaOrderGb()){
		
			case "30" :
				existsExchange = PaGmktClaimDAO.check40DatePaGmktClaimList(pagmkclaim);
				existsReturn   = PaGmktClaimDAO.check30DatePaGmktClaimList(pagmkclaim);
				break;
				
			case "31":
				availbleRtnCnt = PaGmktClaimDAO.checkPaGmktClaimCancelList(pagmkclaim);
				break;
		
			case "40":
				existsExchange = PaGmktClaimDAO.check40DatePaGmktClaimList(pagmkclaim);
				existsReturn   = PaGmktClaimDAO.check30DatePaGmktClaimList(pagmkclaim);
				break;
		}
		
		if(existsExchange > 0){
			dealExchangeCancelByReturnWithOutExchangeCancel(pagmkclaim);
			return false; 
		}
		if(existsReturn > 0){
			dealReturnCancelByReturnWithOutReturnCancel(pagmkclaim);
			return false; 
		}
		if(availbleRtnCnt < 1) return false; 
		
		return true;
	}
	
	@Override
	public int saveClaimList(PaGmkClaim pagmkclaim) throws Exception {	
		int executedRtn = 0;
		//= 1. 데이터 중복 및 교환건 반품전환, 반품건 교환 전환시 취소 처리 및 데이터 CHECK.
		if(!checkValidationForInsertingClaimInfo(pagmkclaim)) return 0;
			
		//= 2. TPAGMKTCLAIMLISTINSERT INSERT
		executedRtn = PaGmktClaimDAO.insertPaGmktClaimList(pagmkclaim);
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST INSERT" });
		}
		
		//= 3. Insert TPAORDERM
		switch(pagmkclaim.getPaOrderGb()){
			
		case"30": case"31":
			insertPaorderMForReturn(pagmkclaim);
			break;
			
		case"40": case"41":
			insertPaorderMForExchange(pagmkclaim);	
			break;
		}

		return executedRtn;
	}
	
	private PaGmkClaim setExchangeCancelVo(PaGmkClaim pagmkclaim) throws Exception {
		
		PaGmkClaim exchangeCancel = new PaGmkClaim();
		exchangeCancel.setPaOrderGb("41");
		exchangeCancel.setPaCode(pagmkclaim.getPaCode());
		exchangeCancel.setPaGroupCode(pagmkclaim.getPaGroupCode());
		exchangeCancel.setPayNo(pagmkclaim.getPayNo());
		exchangeCancel.setContrNo(pagmkclaim.getContrNo());
		exchangeCancel.setInsertDate(pagmkclaim.getInsertDate());
		exchangeCancel.setModifyDate(pagmkclaim.getModifyDate());
		exchangeCancel.setContrNoSeq(PaGmktClaimDAO.selectPaorderMWseqForClaimCancel(exchangeCancel));
		return exchangeCancel;
	}

	private PaGmkClaim setReturnCancelVo(PaGmkClaim pagmkclaim) throws Exception {
		
		PaGmkClaim returnCancel = new PaGmkClaim();
		returnCancel.setPaOrderGb("31");
		returnCancel.setPaCode(pagmkclaim.getPaCode());
		returnCancel.setPaGroupCode(pagmkclaim.getPaGroupCode());
		returnCancel.setPayNo(pagmkclaim.getPayNo());
		returnCancel.setContrNo(pagmkclaim.getContrNo());
		returnCancel.setInsertDate(pagmkclaim.getInsertDate());
		returnCancel.setModifyDate(pagmkclaim.getModifyDate());
		returnCancel.setContrNoSeq(PaGmktClaimDAO.selectPaorderMWseqForClaimCancel(returnCancel));
		//2019-01-17 h.s baek  최초상태 반품에서 반품 취소 -> 반품 -> 배치가 돌아서 반품 취소 데이터 생김 ->다시 지마켓에서 반품 취소 
		//이 상황에서는 request_date가 중복데이터 척도가 되지 않는다. 그래서 아래줄은 주석처리하고 따로 해당 체크 로직을 삽입 하였다.		
		//returnCancel.setRequestDate(DateUtil.toTimestamp(PaGmktClaimDAO.selectPaorderRequestDateForClaimCancel(returnCancel), "yyyy-MM-dd HH:mm:ss"));
		returnCancel.setClaimStatus("5");
		return returnCancel;
	}
	
	private void dealExchangeCancelByReturnWithOutExchangeCancel(PaGmkClaim pagmkclaim) throws Exception{
		
		int existsCnt = 0;
		
		try{
			PaGmkClaim exchangeCancel = new PaGmkClaim();
			exchangeCancel = setExchangeCancelVo(pagmkclaim);
			existsCnt 	= PaGmktClaimDAO.checkPaGmktClaimListForClaimCancel(exchangeCancel);
			if(existsCnt < 1){
				PaGmktClaimDAO.insertPaGmktClaimList(exchangeCancel);
				insertPaorderMForExchange(exchangeCancel);
			}
			
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST-TPAORDERM INSERT(For Exhchange Cancel)" });
		}
	}
	
	private void dealReturnCancelByReturnWithOutReturnCancel(PaGmkClaim pagmkclaim) throws Exception{
		
		int existsCnt = 0;
		
		try{
			PaGmkClaim returnCancel = new PaGmkClaim();
			returnCancel = setReturnCancelVo(pagmkclaim);
			existsCnt 	= PaGmktClaimDAO.checkPaGmktClaimListForClaimCancel(returnCancel);
			if(existsCnt < 1){
				PaGmktClaimDAO.insertPaGmktClaimList(returnCancel);
				insertPaorderMForReturn(returnCancel);
			}
			
		}catch(Exception e){
			throw processException("msg.cannot_save", new String[] { "TPAGMKTCLAIMLIST-TPAORDERM INSERT(For Exhchange Cancel)" });
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaGmkClaim setPaGmkClaimVo(Map<String, Object> returnMap, ParamMap paramMap) throws Exception {
		if(returnMap ==null || returnMap.size() < 1) return null;
		
		String claimType   	= paramMap.getString("CLAIM_TYPE");
		String paCode		= paramMap.getString("paCode");
		String paGroupCode	= paramMap.getString("paGroupCode");
		String insertId		= paramMap.getString("siteGb");
		
		Map<String, Object> claimMap     = new HashMap<String, Object>();
		Map<String, Object> holdInfoMap  	= new HashMap<String, Object>();
		Map<String, Object> ShippingInfoMap = new HashMap<String, Object>();
		Map<String, Object> PickupInfoMap 	= new HashMap<String, Object>();
		Map<String, Object> SenderInfoMap   = new HashMap<String, Object>();
		Map<String, Object> ReceiverInfoMap = new HashMap<String, Object>();
		Map<String, Object> resendInfoMap   = new HashMap<String, Object>();
		Map<String, Object> resendReceiverInfoMap   = new HashMap<String, Object>();
		
		String sysdate = systemService.getSysdatetimeToString();
		PaGmkClaim paGmkClaim = null;
		
	    paGmkClaim	=  new PaGmkClaim();
     	claimMap	= returnMap;

     	
		if(claimMap.get("HoldInfo") != null){
			holdInfoMap  		=  (Map<String, Object>) claimMap.get("HoldInfo");
		}
		
		if(claimMap.get("ShippingInfo") != null){
			ShippingInfoMap 	=  (Map<String, Object>) claimMap.get("ShippingInfo");
		}
		
		if(claimMap.get("PickupInfo") != null){
			PickupInfoMap		=  (Map<String, Object>) claimMap.get("PickupInfo");
			
			if(PickupInfoMap.get("SenderInfo") != null){
				SenderInfoMap		=  (Map<String, Object>) PickupInfoMap.get("SenderInfo");	 
			}
			if(PickupInfoMap.get("ReceiverInfo") != null){
				ReceiverInfoMap		=  (Map<String, Object>) PickupInfoMap.get("ReceiverInfo");	
			}
		}
		
		if(claimMap.get("ResendInfo") != null){
			resendInfoMap		=  (Map<String, Object>) claimMap.get("ResendInfo");	 
		
			if(resendInfoMap.get("ReceiverInfo") != null){
				resendReceiverInfoMap =(Map<String, Object>) resendInfoMap.get("ReceiverInfo");	
			}	
			
		}
		
	
		paGmkClaim.setPayNo						(ComUtil.NVL(claimMap.get("PayNo")).toString());
		paGmkClaim.setGroupNo					(ComUtil.NVL(claimMap.get("GroupNo")).toString());
		paGmkClaim.setContrNo					(ComUtil.NVL(claimMap.get("OrderNo")).toString());
		
		paCode = CommonUtil.checkSourcingMediaForTestClaim(paGmkClaim.getPayNo(),paGmkClaim.getContrNo(),ComUtil.NVL(paCode).toString());
		paGmkClaim.setPaCode					(paCode);
		paGmkClaim.setPaGroupCode				(paGroupCode);
		switch(claimType){
			case "Return":
				paGmkClaim.setPaOrderGb			("30");
				paGmkClaim.setContrNoSeq		(CommonUtil.getMaxPaOrderWSeq(paGmkClaim.getPayNo(),paGmkClaim.getContrNo()));
				break;
			case "ReturnCancel": 
				paGmkClaim.setPaOrderGb			("31");	
				paGmkClaim.setContrNoSeq		(PaGmktClaimDAO.selectPaorderMWseqForClaimCancel(paGmkClaim));
				break;
			case "ReturnDone":
				paGmkClaim.setPaOrderGb			("30");
				paGmkClaim.setContrNoSeq		(CommonUtil.getMaxPaOrderWSeq(paGmkClaim.getPayNo(),paGmkClaim.getContrNo()));
				break;
			case "Exchange":
				paGmkClaim.setPaOrderGb			("40");
				paGmkClaim.setContrNoSeq		(CommonUtil.getMaxPaOrderWSeq(paGmkClaim.getPayNo(),paGmkClaim.getContrNo()));
				break;
			case "ExchangeCancel":
				paGmkClaim.setPaOrderGb			("41");	
				paGmkClaim.setContrNoSeq		(PaGmktClaimDAO.selectPaorderMWseqForClaimCancel(paGmkClaim));
				break;
		}
		
		paGmkClaim.setGoodsNo					(ComUtil.NVL(claimMap.get("GoodsNo")).toString());
		paGmkClaim.setSiteGoodsNo				(ComUtil.NVL(claimMap.get("SiteGoodsNo")).toString());
		paGmkClaim.setRequestUser				(ComUtil.NVL(claimMap.get("RequestUser")).toString());
		paGmkClaim.setGoodsStatus				(ComUtil.NVL(claimMap.get("GoodsStatus")).toString());
		paGmkClaim.setReason					(ComUtil.NVL(claimMap.get("Reason")).toString());
		paGmkClaim.setReasonCode				(ComUtil.NVL(claimMap.get("ReasonCode")).toString()); //클레임 요청 사유 코드 추가
		paGmkClaim.setReasonDetail				(ComUtil.NVL(claimMap.get("ReasonDetail")).toString());
		paGmkClaim.setApproveUser				(ComUtil.NVL(claimMap.get("ApproveUser")).toString());
		paGmkClaim.setClaimStatus				(ComUtil.NVL(claimMap.get("ReturnStatus")).toString());
		paGmkClaim.setIsFastRefund				(ComUtil.NVL(claimMap.get("IsFastRefund")).toString());
		paGmkClaim.setOrderDate					(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("OrderDate")).toString()));
		paGmkClaim.setPayDate					(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("PayDate")).toString()));
		paGmkClaim.setRequestDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("RequestDate")).toString()));
		paGmkClaim.setWithDrawDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("WithdrawDate")).toString()));
		paGmkClaim.setDeliveryEndDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(ShippingInfoMap.get("CompleteDate")).toString()));   //배송완료  ShippingInfo.CompleteDate
		paGmkClaim.setPickupEndDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(PickupInfoMap.get("CompleteDate")).toString()));     //PickupInfo.CompleteDate
		paGmkClaim.setHoldDate					(PaGmktDateUtil.toTimestamp(ComUtil.NVL(holdInfoMap.get("HoldDate")).toString()));
		paGmkClaim.setIsHold					(ComUtil.NVL(holdInfoMap.get("IsHold")).toString());
		paGmkClaim.setHoldReason				(ComUtil.NVL(holdInfoMap.get("Reason")).toString());
		paGmkClaim.setHoldFreeDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(holdInfoMap.get("FreeDate")).toString()));
		paGmkClaim.setApproveDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("ApproveDate")).toString()));
		paGmkClaim.setApproveEndDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("CompleteDate")).toString()));
		paGmkClaim.setShippingFee				(Double.parseDouble(String.valueOf(ComUtil.NVL(ShippingInfoMap.get("ShippingFee"),"0"))));
		paGmkClaim.setWhoShippingFee			(ComUtil.NVL(ShippingInfoMap.get("WhoShippingFee")).toString());
		paGmkClaim.setDeliveryCompCode			(ComUtil.NVL(ShippingInfoMap.get("DeliveryCompCode")).toString());
		paGmkClaim.setInvoiceNo					(ComUtil.NVL(ShippingInfoMap.get("InvoiceNo")).toString());
		paGmkClaim.setPickupStatus				(ComUtil.NVL(PickupInfoMap.get("Status")).toString());
		paGmkClaim.setDelyName					(ComUtil.NVL(PickupInfoMap.get("DeliveryCompName")).toString());
		paGmkClaim.setDelyNo					(ComUtil.NVL(PickupInfoMap.get("InvoiceNo")).toString());
		paGmkClaim.setDelyDuty					(ComUtil.NVL(claimMap.get("WhoReturnShippingFee")).toString());
		paGmkClaim.setReturnShippingFee			(Double.parseDouble(String.valueOf(ComUtil.NVL(claimMap.get("ReturnShippingFee"),"0"))));
		paGmkClaim.setReturnShippingFeeWay		(ComUtil.NVL(claimMap.get("ReturnShippingFeeWay")).toString());;
		paGmkClaim.setWhoAddReturnShippingFee	(ComUtil.NVL(claimMap.get("WhoAddReturnShippingFee")).toString());
		paGmkClaim.setAddReturnShippingFee		(Double.parseDouble(String.valueOf(ComUtil.NVL(claimMap.get("AddReturnShippingFee"),"0"))));
		paGmkClaim.setAddReturnShippingFeeWay	(ComUtil.NVL(claimMap.get("AddReturnShippingFeeWay")).toString());
		paGmkClaim.setFastRefundStatus			(ComUtil.NVL(claimMap.get("FastRefundRewardStatus")).toString());	
		paGmkClaim.setReceiverName				(ComUtil.NVL(ReceiverInfoMap.get("Name")).toString());
		paGmkClaim.setReceiverHpNo				(ComUtil.NVL(ReceiverInfoMap.get("HpNo")).toString().trim());
		paGmkClaim.setReceiverTelNo				(ComUtil.NVL(ReceiverInfoMap.get("TelNo")).toString().trim());
		paGmkClaim.setReceiverZipCode			(ComUtil.NVL(ReceiverInfoMap.get("ZipCode")).toString());
		paGmkClaim.setReceiverAddr				(ComUtil.NVL(ReceiverInfoMap.get("Address")).toString());
		paGmkClaim.setReceiverAddr1				(ComUtil.NVL(ReceiverInfoMap.get("AddressFront")).toString());
		paGmkClaim.setReceiverAddr2				(ComUtil.NVL(ReceiverInfoMap.get("AddressBack")).toString());	
		paGmkClaim.setSenderName				(ComUtil.NVL(SenderInfoMap.get("Name")).toString());
		paGmkClaim.setSenderHpNo				(ComUtil.NVL(SenderInfoMap.get("HpNo")).toString().trim());
		paGmkClaim.setSenderTelNo				(ComUtil.NVL(SenderInfoMap.get("TelNo")).toString().trim());
		paGmkClaim.setSenderZipCode				(ComUtil.NVL(SenderInfoMap.get("ZipCode")).toString());
		paGmkClaim.setSenderAddr				(ComUtil.NVL(SenderInfoMap.get("Address")).toString());
		paGmkClaim.setSenderAddr1				(ComUtil.NVL(SenderInfoMap.get("AddressFront")).toString());
		paGmkClaim.setSenderAddr2				(ComUtil.NVL(SenderInfoMap.get("AddressBack")).toString());
		paGmkClaim.setExchStatus				(ComUtil.NVL(claimMap.get("ExchangeStatus")).toString());
		paGmkClaim.setExchResendDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(resendInfoMap.get("ResendDate")).toString()));
		paGmkClaim.setExchResendEndDate			(PaGmktDateUtil.toTimestamp(ComUtil.NVL(resendInfoMap.get("CompleteDate")).toString()));
		paGmkClaim.setExchEndDate				(PaGmktDateUtil.toTimestamp(ComUtil.NVL(claimMap.get("CompleteDate")).toString()));
		paGmkClaim.setExchShippingFee			(Double.parseDouble(String.valueOf(ComUtil.NVL(claimMap.get("ExchangeShippingFee"),0))));
		paGmkClaim.setExchWhoShippingFee		(ComUtil.NVL(claimMap.get("WhoExchangeShippingFee")).toString());
		paGmkClaim.setExchShippingFeeWay		(ComUtil.NVL(claimMap.get("ExchangeShippingFeeWay")).toString());
		paGmkClaim.setExchDeliveryEndDate		(PaGmktDateUtil.toTimestamp(ComUtil.NVL(ShippingInfoMap.get("CompleteDate")).toString()));
		paGmkClaim.setExchDeliveryCompName		(ComUtil.NVL(resendInfoMap.get("DeliveryCompName")).toString());
		paGmkClaim.setExchInvoiceNo				(ComUtil.NVL(resendInfoMap.get("InvoiceNo")).toString());
		paGmkClaim.setExchReceiverName			(ComUtil.NVL(resendReceiverInfoMap.get("Name")).toString());
		paGmkClaim.setExchReceiverHpNo			(ComUtil.NVL(resendReceiverInfoMap.get("HpNo")).toString().trim());
		paGmkClaim.setExchReceiverTelNo			(ComUtil.NVL(resendReceiverInfoMap.get("TelNo")).toString().trim());
		paGmkClaim.setExchReceiverZipCode		(ComUtil.NVL(resendReceiverInfoMap.get("ZipCode")).toString());
		paGmkClaim.setExchReceiverAddr			(ComUtil.NVL(resendReceiverInfoMap.get("Address")).toString());
		paGmkClaim.setExchReceiverAddr1			(ComUtil.NVL(resendReceiverInfoMap.get("AddressFront")).toString());
		paGmkClaim.setExchReceiverAddr2			(ComUtil.NVL(resendReceiverInfoMap.get("AddressBack")).toString());
		paGmkClaim.setInsertDate				(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmkClaim.setModifyDate				(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmkClaim.setInsertId					(insertId);
		return paGmkClaim;
	}

	
	private void insertPaorderMForReturn(PaGmkClaim paGmktClaim) throws Exception{
		
		Paorderm paorderm = null;
		int executedRtn = 0;
		
		//= 3. TPORDERM
		paorderm = new Paorderm();
		
		paorderm.setPaCode			(paGmktClaim.getPaCode());
		paorderm.setPaGroupCode		(paGmktClaim.getPaGroupCode());
		paorderm.setPaOrderGb		(paGmktClaim.getPaOrderGb());
		paorderm.setPaOrderNo		(paGmktClaim.getPayNo());
		paorderm.setPaOrderSeq		(paGmktClaim.getContrNo());
		paorderm.setPaClaimNo		(paGmktClaim.getContrNoSeq());
		paorderm.setPaProcQty		(PaGmktClaimDAO.selectPaGmktClaimReqQty(paGmktClaim));
		
		if(paGmktClaim.getClaimStatus().equals("2") || paGmktClaim.getClaimStatus().equals("4") || paGmktClaim.getClaimStatus().equals("6")){
			paorderm.setPaDoFlag	("60"); //직권취소(상담원) 로 발생한 반품 케이스 
		}else{
			paorderm.setPaDoFlag	("20");
		}
		
		paorderm.setOutBefClaimGb	("0");
		paorderm.setInsertDate		(paGmktClaim.getInsertDate());
		paorderm.setModifyDate		(paGmktClaim.getModifyDate());
		paorderm.setModifyId		(paGmktClaim.getInsertId());
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		
		if (executedRtn != 1) {
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void insertPaorderMForExchange(PaGmkClaim paGmktClaim) throws Exception{
		
		List<Object> goodsInfoList = null;
		HashMap<String, Object> goodsInfoMap = null;
		Paorderm paorderm = null;
		int executedRtn  = 0;
		
		goodsInfoList = PaGmktClaimDAO.selectGoodsdtInfo(paGmktClaim); //= 교환대상 상품의 연동된 단품 갯수 및 단품코드 조회
		goodsInfoMap = new HashMap<>();
		goodsInfoMap = (HashMap<String, Object>) goodsInfoList.get(0);
		
		for(int j = 0; j < 2; j++){
			
			paorderm = new Paorderm();	
			
			switch(paGmktClaim.getPaOrderGb()){
			case "40":
				
				paorderm.setChangeGoodsCode(goodsInfoMap.get("GOODS_CODE").toString());
				
				if(goodsInfoList.size() == 1 ){
					paorderm.setChangeGoodsdtCode(goodsInfoMap.get("GOODSDT_CODE").toString());
					paorderm.setChangeFlag("01");
				}else{
					paorderm.setChangeGoodsdtCode("");
					paorderm.setChangeFlag("00");
				}
			
				if(j == 0){
					paorderm.setPaOrderGb("40");
				}else {
					paorderm.setPaOrderGb("45");
				}	
				
				break;
				
			case "41":
			
				if(j == 0){
					paorderm.setPaOrderGb("41");
				}else {
					paorderm.setPaOrderGb("46");
				}	
				
				break;
			}
			
			paorderm.setPaCode			(paGmktClaim.getPaCode());
			paorderm.setPaGroupCode		(paGmktClaim.getPaGroupCode());
			paorderm.setPaOrderNo		(paGmktClaim.getPayNo());
			paorderm.setPaOrderSeq		(paGmktClaim.getContrNo());
			paorderm.setPaClaimNo		(paGmktClaim.getContrNoSeq());
			paorderm.setPaProcQty		(PaGmktClaimDAO.selectPaGmktClaimReqQty(paGmktClaim));
			paorderm.setPaDoFlag		("20");
			paorderm.setOutBefClaimGb	("0");
			paorderm.setInsertDate		(paGmktClaim.getInsertDate());
			paorderm.setModifyDate		(paGmktClaim.getModifyDate());
			paorderm.setModifyId		(paGmktClaim.getInsertId());
			
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
			if (executedRtn < 0) {
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });
			}
		} //end of for
	}

	@Override
	public String compareAddress(ParamMap param) throws Exception{
		return PaGmktClaimDAO.compareAddress(param);
	}
}