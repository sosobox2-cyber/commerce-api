package com.cware.api.pagmktv2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.model.PaGmkClaim;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pagmkt.claim.repository.PaGmktClaimDAO;
import com.cware.netshopping.pagmkt.claim.service.PaGmktClaimService;
import com.cware.netshopping.pagmkt.exchange.service.PaGmktExchangeService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;

@Controller("com.cware.api.pagmktv2.PaGmktV2ClaimController")
@RequestMapping(value="/pagmktv2/claim")
public class PaGmktV2ClaimController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimService")
	private PaGmktClaimService paGmktClaimService;

	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2ExchangeController")	
	private PaGmktV2ExchangeController paGmktExchangeController;	
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController asycController;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimDAO")
	private PaGmktClaimDAO PaGmktClaimDAO;
	
	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
		
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "pagmkt.exchange.PaGmktExchangeService")
	private PaGmktExchangeService paGmktExchangeService;
	
	//= Information 반품 교환 조회,철회 목록 저장
	@SuppressWarnings("unchecked")
	public String savePagmktClaimList(PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{
		int	   count 	  	  = Constants.PA_GMKT_CONTRACT_CNT;
		String broadCode  	  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 	  = Constants.PA_GMKT_ONLINE_CODE;
		String paCode     	  = null;
		String response		  = null;;
		int    errorCount	  = 0;
		int	   successCount   = 0;
		PaGmkClaim pagmkclaim = null;
		
		Map<String, Object> mainMap		 = new HashMap<String, Object>();
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();		
			
			
		for(int i = 0; i < count; i++){
			paCode = (i==0) ?broadCode:onlineCode;  //21, 22
			paramMap.put("paCode", paCode);
			
			// = Step1) get Connect and resonse
			response =  restUtil.getConnection(rest,  paramMap); 
			
			// = Step2) Refine Response
			mainMap 	= ComUtil.splitJson(response);
			returnList  =  (List<Map<String, Object>>) mainMap.get("Data");

			// = Step3) InsertpaGmktClaimList
			try{
				for(Map<String, Object> returnMap : returnList){
					pagmkclaim = paGmktClaimService.setPaGmkClaimVo(returnMap, paramMap);
					successCount +=  paGmktClaimService.saveClaimListTx(pagmkclaim);

					//= 교환은 보류 API를 호출해 줘야함.
					//checkReserveExchange(request,claimType,pagmkclaim);
				}
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
				errorCount++;
				continue; //에러가 발생해도 일단 무시..
			}
			   
		}//end of for
				
		return CommonUtil.setResultMessage(paramMap.getString("CLAIM_TYPE"), paCode, successCount, errorCount);
	}
	
	//= Information 교환 보류 api 호출
	private void checkReserveExchange(HttpServletRequest request, String claimType, PaGmkClaim pagmkclaim){
		
		try {
			
			String holdReason = "0"; //보류사유 코드 입력 0 : 기타유보사유 , 1 : 교환배송비청구,  4 : 교환입고미확인 * 교환배송비청구 시, G마켓은 Front에서 배송비 청구 요청되지만 옥션은 되지 않음
			String holdReasonDetail = "상담원 교환 처리 접수중";
			String paCode  = null;
			String contrNo = null;
			String payNo   = null;
			String contrNoSeq = null;
			
			if(!claimType.equals("Exchange")) return;
			String changeFlag = null;
			changeFlag = paGmktClaimService.selectGoodsdtChangeFlag(pagmkclaim);				
			
			if(!changeFlag.equals("00")) return;
		
			paCode  = pagmkclaim.getPaCode();	
			payNo	= pagmkclaim.getPayNo();
			contrNo = pagmkclaim.getContrNo();
			contrNoSeq	= pagmkclaim.getContrNoSeq();
			
			paGmktExchangeController.exchangeHoldProc(request, payNo, contrNo, contrNoSeq, paCode, holdReason,holdReasonDetail, "1","PAG");

		} catch (Exception e) {
			
		}
	}
	    
	//= Information 반품/교환 수거 송장 등록

	public String sendShippingInfoToGmaket(PaGmktAbstractRest rest, ParamMap paramMap ,List<Map<String,String>> returnPickList, String paDoFlag) throws Exception{
		
		int excuteCnt = 0;
		int failCount = 0;
		int successCount  =0;
		
		for(Map<String,String> rtnMap : returnPickList){
	
		    paramMap.put("urlParameter"	, rtnMap.get("CONTR_NO").toString());
		    paramMap.put("DELY_CODE"	, rtnMap.get("DELY_CODE").toString());
		    paramMap.put("DELY_NO"		, rtnMap.get("DELY_NO").toString());

		    try{
				restUtil.getConnection(rest,  paramMap); 
				successCount++;
			}catch(Exception e){
				
				if("03".equals(rtnMap.get("PA_GROUP_CODE").toString())){ //Auction의 경우 운송장 체크를 하기때문에 기타로 한번 더 보낸다.
					try{
						sendAgain(rest, paramMap, rtnMap, paDoFlag);  
					    successCount++;
					    continue;
					}catch(Exception ee){
						log.error("FAIL Again - Send Claim Shipping Info");
					}
				}	
				
				rtnMap.put("paCode"		, paramMap.getString("paCode"));
				rtnMap.put("paGroupCode", paramMap.getString("paGroupCode"));	
				rtnMap.put("modifyId"	, paramMap.getString("siteGb"));
				
				if (e.getMessage().contains("이미")) {
					rtnMap.put("apiResultCode"		, "000000");
					rtnMap.put("apiResultMessage"	, "(자동완료)" + (e.getMessage().length() > 1940 ?  e.getMessage().substring(1,1940) :  e.getMessage()));
					rtnMap.put("paDoFlag"			, "60");
					successCount++;
				} else {
					rtnMap.put("apiResultCode", "999999");
					rtnMap.put("apiResultMessage", e.getMessage().length() > 1940 ?  e.getMessage().substring(1,1940) :  e.getMessage());
					rtnMap.put("paDoFlag"	, rtnMap.get("PA_DO_FLAG").toString());	
					failCount++;
				}
		
				excuteCnt =  paGmktClaimService.updateReturnPickupTx(rtnMap);
				
				if(excuteCnt != 1) log.error("FAIL - UpdateReturnPickupTx");
				
				failCount++;
				continue;
			}
		    
		    rtnMap.put("apiResultCode"		, "000000");
		    rtnMap.put("apiResultMessage"	, "수거 송장 등록 완료");
			rtnMap.put("paCode"				, paramMap.getString("paCode"));
			rtnMap.put("paDoFlag"			, paDoFlag);
			rtnMap.put("paGroupCode"		, paramMap.getString("paGroupCode"));
			rtnMap.put("modifyId"			, paramMap.getString("siteGb"));
			
			excuteCnt =  paGmktClaimService.updateReturnPickupTx(rtnMap);
			if(excuteCnt != 1)	throw processException("errors.db.save", new String[] { "수거정보등록 처리 : - TPAORDERM FAIL-UPDATE 오류 발생" });

		}//end of for
		
		
		return CommonUtil.setResultMessage("수거송장 등록", paramMap.getString("paCode"), successCount, failCount);
	}
	
	private void sendAgain(PaGmktAbstractRest rest, ParamMap paramMap, Map<String,String> rtnMap, String paDoFlag) throws Exception{
		int excuteCnt = 0;
		paramMap.put("DELY_CODE"	, "10034");
	    paramMap.put("DELY_NO"		, rtnMap.get("SLIP_I_NO").toString());
		
	    restUtil.getConnection(rest ,  paramMap); 
		
		rtnMap.put("apiResultCode"		, "000000");
		rtnMap.put("apiResultMessage"	, "수거 송장 등록 완료");
		rtnMap.put("paCode"				, paramMap.getString("paCode"));
		rtnMap.put("paDoFlag"			, paDoFlag);
		rtnMap.put("paGroupCode"		, paramMap.getString("paGroupCode"));
		rtnMap.put("modifyId"			, paramMap.getString("siteGb"));
		
		excuteCnt =  paGmktClaimService.updateReturnPickupTx(rtnMap);
		if(excuteCnt != 1)	throw processException("errors.db.save", new String[] { "수거정보등록 처리 : - TPAORDERM FAIL-UPDATE 오류 발생" });
		
	}

	

	//= Information 반품 승인 Async 호출
	@SuppressWarnings("unchecked")
	public void orderClaimMain(HttpServletRequest request, String paGroupCode) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PAGMKT_ORDER_CLAIM";
		HashMap<String, Object> hmSheet = null;
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"			, prg_id);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		prg_id = paramMap.getString("apiCode");
		
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderClaimTargetList = paGmktClaimService.selectOrderClaimTargetList(paramMap);
			int procCnt = orderClaimTargetList.size();
			String isLocalYn = CommonUtil.getLocalOrNot(request);
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList.get(i);
					asycController.orderClaimAsync(hmSheet, isLocalYn);
				} catch (Exception e) {
					continue;
				}
			}//end of for
			
			paramMap.put("message","OK");
			paramMap.put("code", "200");
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}			
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}	
	}
	
	
	//= Information 반품 철회 Async 호출
	@SuppressWarnings("unchecked")
	public void claimCancelMain(HttpServletRequest request, String paGroupCode) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PAGMKT_CLAIM_CANCEL";
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> claimCancelTargetList = paGmktClaimService.selectClaimCancelTargetList(paramMap);
			HashMap<String, Object> hmSheet = null;
			int procCnt = claimCancelTargetList.size();
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) claimCancelTargetList.get(i);
					asycController.claimCancelAsync(hmSheet);
				} catch (Exception e) {
					continue;
				}
			}// end of for
			
			paramMap.put("message","OK");
			paramMap.put("code", "200");
		
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}			
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	
	//= Information 교환 Async 호출
	@SuppressWarnings("unchecked")
	public void orderExchangeMain(HttpServletRequest request, String paGroupCode) throws Exception{
		String duplicateCheck = "";
		String prg_id = "PAGMKT_ORDER_EXCHANGE";
		HashMap<String, Object> hmSheet = null;
		int procCnt= 0;
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		prg_id = paramMap.getString("apiCode");
		
		try {
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> orderClaimTargetList = paGmktExchangeService.selectOrderChangeTargetList(paramMap);
						
			procCnt = orderClaimTargetList.size();
			String isLocalYn = CommonUtil.getLocalOrNot(request);
			
			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) orderClaimTargetList.get(i);
					asycController.orderExchangeAsync(hmSheet, isLocalYn);
				} catch (Exception e) {
					continue;
				}
			}
			
			paramMap.put("message","OK");
			paramMap.put("code", "200");
		
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}			
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}	
	}
	
	
	//= Information 교환 철회 Async 호출
	@SuppressWarnings("unchecked")
	public void exchangeCancelMain(HttpServletRequest request, String paGroupCode) throws Exception{
		String duplicateCheck 	= "";
		String prg_id 			= "PAGMKT_EXCHANGE_CANCEL";
		ParamMap paramMap		 = new ParamMap();
		paramMap.put("apiCode", prg_id);
		paramMap.put("paGroupCode", paGroupCode);		
		CommonUtil.setParams(paramMap);
		prg_id = paramMap.getString("apiCode");

		try {
			//= 중복 실행 Check
			int procCnt = 0;
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			List<Object> claimCancelTargetList = paGmktExchangeService.selectChangeCancelTargetList(paramMap);
			
			HashMap<String, Object> hmSheet = null;
			procCnt = claimCancelTargetList.size();
			String isLocalYn = CommonUtil.getLocalOrNot(request);

			for(int i = 0; procCnt > i; i++){
				try{
					hmSheet = new HashMap<>();
					hmSheet = (HashMap<String, Object>) claimCancelTargetList.get(i);
					asycController.exchangeCancelAsync(hmSheet, isLocalYn);
				} catch (Exception e) {
					continue;
				}
			}//end of for
		
			paramMap.put("message","OK");
			paramMap.put("code", "200");
			
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}			
			//= Process End
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}		
	
}
