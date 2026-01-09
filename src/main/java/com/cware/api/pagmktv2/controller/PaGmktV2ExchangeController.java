package com.cware.api.pagmktv2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaGmktPaordermVO;
import com.cware.netshopping.domain.PaGmktSlipmVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pagmkt.claim.service.PaGmktClaimService;
import com.cware.netshopping.pagmkt.exchange.service.PaGmktExchangeService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktExchangeRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "/pagmktv2/exchange", description="교환")
@Controller("com.cware.api.pagmktv2.PaGmktV2ExchangeController")
@RequestMapping(value="/pagmktv2/exchange")
public class PaGmktV2ExchangeController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.exchange.PaGmktExchangeService")
	private PaGmktExchangeService paGmktExchangeService;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2AsycController")
	private PaGmktV2AsycController asycController;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	@Resource(name = "pagmkt.claim.PaGmktClaimService")
	private PaGmktClaimService paGmktClaimService;
	
	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2ClaimController")
	private PaGmktV2ClaimController paGmktClaimController;
	
	
	/**
	 * 교환신청/취소 목록조회
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "교환신청/취소 목록조회", notes = "교환신청/취소 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-exchange-Exchanges/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeList(HttpServletRequest request, 
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") @PathVariable("paGroupCode") String paGroupCode, 
			@ApiParam(name = "claimStatus", value = "교환상태", defaultValue = "") @RequestParam(value="claimStatus", required=false, defaultValue = "ClaimReady") String claimStatus) 
			throws Exception{
		
		PaGmktAbstractRest rest = new PaGmktExchangeRest();
		String duplicateCheck 	= "";
		String apiCode 			= "IF_PAGMKTAPI_V2_03_010";
		String message 			= null;
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("claimType"	, claimStatus);
		paramMap.put("paGroupCode"  , paGroupCode);
		paramMap.put("PAY_NO"	, 0);
		paramMap.put("CONTR_NO"	, 0);
		paramMap.put("FROM_DATE"	, DateUtil.addDay(systemService.getSysdate(),  -2 ));
		paramMap.put("TO_DATE"		, DateUtil.addDay(systemService.getSysdate(),    +1 ));
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
				
		try{	
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			switch(claimStatus){
			
				case "ClaimReady":
					log.info("===== 교환 신청 목록 조회 API START =====");
					paramMap.put("TYPE"				, 2);
					paramMap.put("EXCHANGE_STATUS"	, 1);
					paramMap.put("CLAIM_TYPE"		, "Exchange");
					break;
				
				case "ClaimReject":
					log.info("===== 교환 철회 목록 조회 API START =====");
					paramMap.put("TYPE"				, 3);
					paramMap.put("EXCHANGE_STATUS"	, 5);
					paramMap.put("CLAIM_TYPE"		, "ExchangeCancel");
					break;
			
				default:	
					throw processException( "errors.cannot  ", new String[] {"해당 API는 준비되지 않았습니다."} );
			}	
			
			message = paGmktClaimController.savePagmktClaimList(rest ,paramMap);

			CommonUtil.failCheck(message, paramMap);

			
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 교환 처리 API END =====");
		}	
		
		switch(claimStatus){
		
		case "ClaimReady":
			//=IF_PAGMKTAPI_03_012 교환 생성 호출
			paGmktClaimController.orderExchangeMain(request, paGroupCode);     
			break;
		
		case "ClaimReject":
		  	//=IF_PAGMKTAPI_03_013 교환 취소생성 조회 호출
			paGmktClaimController.exchangeCancelMain(request, paGroupCode);
	       	break;	
		}
			
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);

	}
	
	/**
	 * 6.6 교환 수거정보등록
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "6.6 교환 수거정보등록", notes = "6.6 교환 수거정보등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-exchange-pickup/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnPickupProc(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		String apiCode 							= "IF_PAGMKTAPI_V2_03_011";            
		PaGmktAbstractRest rest 				= new PaGmktExchangeRest();
	    List<Map<String,String>> returnPickList = new ArrayList<Map<String,String>>();
		String broadCode  						= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 						= Constants.PA_GMKT_ONLINE_CODE;
		int	   count 							= Constants.PA_GMKT_CONTRACT_CNT;
		String paCode 	  						= null;
		String duplicateCheck 					= "";
		ParamMap paramMap 						= new ParamMap();
		String messageFor50 					= "";
		String messageFor55 					= "";

		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode" 		, paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");

		try{			
			//= 중복 실행 Check
			log.info("===== 교환 수거정보등록 START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			
			for(int i = 0; i < count; i++  ){
				
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				// = Get  List 
				returnPickList = paGmktExchangeService.selectExchangePickup50List(paramMap);
				messageFor50   = paGmktClaimController.sendShippingInfoToGmaket(rest, paramMap, returnPickList, "50");

				returnPickList = paGmktExchangeService.selectExchangePickup60List(paramMap);
				messageFor55   = paGmktClaimController.sendShippingInfoToGmaket(rest, paramMap, returnPickList, "55");
			 
			}//end of for
			
			CommonUtil.failCheck(messageFor50 + messageFor55, paramMap);
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}		
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 교환 수거정보등록 END =====");
		}
		
		returnConfrim(paGroupCode);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	
	/**
	 * 6.8 교환 수거 승인
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("unchecked")
	public void returnConfrim(String paGroupCode) throws Exception{		
		PaGmktAbstractRest rest = new PaGmktExchangeRest();
		String apiCode						   = "IF_PAGMKTAPI_V2_03_012";            
		String duplicateCheck 				   = "";
		List<Object> exchangeReturnConfirmList = new ArrayList<>();
		HashMap<String, Object> returnMap 	   = null;
		ParamMap paramMap 					   = new ParamMap();
		String broadCode  					   = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 					   = Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  					   = null;
		String message 	  					   = "";
		int	   count 	  					   = Constants.PA_GMKT_CONTRACT_CNT;
		int	   successCnt 					   = 0;
		int    totalCnt  					   = 0;
		
		paramMap.put("apiCode"		, apiCode);
		paramMap.put("paGroupCode"	, paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
		
		try{
			log.info("===== 교환 수거 승인 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			
			for(int i = 0 ; i < count ; i++){
		
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				
				exchangeReturnConfirmList = paGmktExchangeService.selectExchangeReturnConfirmList(paramMap);
				
				for(Object exchangeReturnConfrim : exchangeReturnConfirmList){
					totalCnt++;
					returnMap = (HashMap<String, Object>) exchangeReturnConfrim;
					successCnt += paGmktClaimService.saveReturnConfirmProcTx(rest , paramMap, returnMap);
				}	
			}
			
			message = CommonUtil.setResultMessage("교환 수거 승인","" , successCnt, totalCnt - successCnt);
			CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 교환 수거 승인 API END =====");
		}
	}
	
	@ApiOperation(value = "교환 수거 승인 API(사용안함)", notes = "교환 수거 승인 API(사용안함)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-exchange-pickup", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfrimOld(HttpServletRequest request){
		log.info("교환 수거 승인 API는 사용하지 않을것입니다.");
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "해당 API는 사용하지 않을 예정입니다."), HttpStatus.OK);	
	}
	
	
	/**
	 * 6.8 교환 보류   - G마켓은 개발 후 테스트 진행 하지 않음, Auction은 개발 안함
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	//H.S.Beak - 일단 사용안하기로 해서 테스트를 하지 않았음.. + Auction은 개발 다시해야함..
	@ApiOperation(value = "6.8 교환 보류   - G마켓은 개발 후 테스트 진행 하지 않음, Auction은 개발 안함", notes = "6.8 교환 보류   - G마켓은 개발 후 테스트 진행 하지 않음, Auction은 개발 안함", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-exchange-hold", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeHoldProc(HttpServletRequest request,
			@ApiParam(name = "payNo", value = "장바구니코드(결제번호)", defaultValue = "") 	@RequestParam(value="payNo", required=true) String payNo,
			@ApiParam(name = "contrNo", value = "주문번호", defaultValue = "") 			@RequestParam(value="contrNo", required=true) String contrNo,
			@ApiParam(name = "contrNoSeq", value = "주문번호순번", defaultValue = "") 		@RequestParam(value="contrNoSeq", required=true) String contrNoSeq,
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@RequestParam(value="paCode", required=true) String paCode,
			@ApiParam(name = "holdReason", value = "클레임번호", defaultValue = "") 		@RequestParam(value="holdReason", required=false, defaultValue="0") String holdReason,
			@ApiParam(name = "holdReasonDetail", value = "교환보류코드", defaultValue = "") @RequestParam(value="holdReasonDetail", required=false, defaultValue="0") String holdReasonDetail,
			@ApiParam(name = "holdYn", value = "교환보류여부", defaultValue = "") 		 	@RequestParam(value="holdYn", required=false) String holdYn,
			@ApiParam(name = "procId", value = "처리자ID", defaultValue = "") 			@RequestParam(value="procId", required=false, defaultValue="PAG") String procId) 
			throws Exception{
		PaGmktAbstractRest rest = new PaGmktExchangeRest();
		String apiCode = null;
		
		if(holdYn.equals("1")){ //1이면 홀드 0이면 해제
			apiCode = "IF_PAGMKTAPI_V2_03_013";
		}else{
			apiCode = "IF_PAGMKTAPI_V2_03_016";
		}
		
		
		String duplicateCheck = "";
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");
		
		paramMap.put("urlParameter"		,contrNo);
		paramMap.put("paCode"			,paCode);
		paramMap.put("REASON"			,holdReason);
		paramMap.put("REASON_DETAIL"	,holdReasonDetail);
		
		try{
			
			log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
	
			checkValidation(payNo, contrNo, contrNoSeq, holdReason,holdReasonDetail, paCode , holdYn);
			
			restUtil.getConnection(rest,  paramMap);
			updateGmktPaOrderMForReserve(payNo, contrNo, contrNoSeq, holdReason, holdYn, holdReasonDetail);
		
			paramMap.put("message","OK");
			paramMap.put("code", "200");
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			
			
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 주문정보조회 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	
	
	/**
	 * 6.8 교환 재발송 처리
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "6.8 교환 재발송 처리", notes = "6.8 교환 재발송 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-exchange-resend/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeSlipOutProc(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		
		PaGmktAbstractRest rest 						= new PaGmktExchangeRest();
		String apiCode 									= "IF_PAGMKTAPI_V2_03_014";            
		String duplicateCheck 							= "";
		ParamMap paramMap 								= new ParamMap();
		List<PaGmktSlipmVO> exchangeSlipOutTargetList	= null;
		int totalCount 									= 0;
		int successCount  								= 0;
		String message 									= null;
		
		paramMap.put("apiCode"			, 	apiCode);
		paramMap.put("paGroupCode"		,	paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");
		
		try{			
			log.info("===== 교환 재발송 처리 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			exchangeSlipOutTargetList = paGmktExchangeService.selectExchangeSlipOutTargetList(paramMap);
			totalCount = (exchangeSlipOutTargetList != null) ? exchangeSlipOutTargetList.size() : 0 ;
				
	    	for(PaGmktSlipmVO slipmVo : exchangeSlipOutTargetList){
	    		successCount += reSendShippingForExchange(rest, paramMap, slipmVo, paGroupCode);
	    	}
	    		
	    	message = CommonUtil.setResultMessage("교환 재발송 처리", "", successCount, (totalCount - successCount));
			CommonUtil.failCheck(message, paramMap);
	    	
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 교환 재발송 처리 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	

	/**
	 * 6.8 교환 재발송 완료 처리
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "6.8 교환 재발송 완료 처리", notes = "6.8 교환 재발송 완료 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-exchange-resend/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeCompleteProc(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		PaGmktAbstractRest rest 					= new PaGmktExchangeRest();
		String apiCode 								= "IF_PAGMKTAPI_V2_03_015";            
		String duplicateCheck 						= "";
		ParamMap paramMap 							= new ParamMap();
	   	List<PaGmktPaordermVO> exchangeCompleteList = null;
		int totalCount 								= 0;
		int successCount  							= 0;
		String message 								= null;
		
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		apiCode = paramMap.getString("apiCode");

		try{
			
			log.info("===== 교환 재발송 완료 처리 API START =====");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			exchangeCompleteList = paGmktExchangeService.selectGmktExchangeCompleteList(paramMap);
			totalCount = (exchangeCompleteList != null) ? exchangeCompleteList.size() : 0 ;
				
			//reSendCompleteForExchangeTest(rest,paramMap);
			
	    	for(PaGmktPaordermVO paOrderM : exchangeCompleteList){
	    		successCount += reSendCompleteForExchange(rest,paramMap,paOrderM);
	    	}
	    	
	    	message = CommonUtil.setResultMessage("교환 재발송 완료 처리 ", "", successCount, totalCount - successCount);
	    	CommonUtil.failCheck(message, paramMap);	

			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 교환 재발송 완료 처리 API END =====");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
		
	
	private int reSendCompleteForExchange(PaGmktAbstractRest rest , ParamMap paramMap,  PaGmktPaordermVO paOrderM) throws Exception{
		int SuccessCount = 0;
  	
    	try{
			paramMap.put("urlParameter"			, paOrderM.getPaOrderSeq()); //contrNo
			paramMap.put("DELY_COMPLETE_DATE"	, paOrderM.getApiResultMessage());
			paramMap.put("paCode"				, paOrderM.getPaCode());
			
			restUtil.getConnection(rest, paramMap);
			
			paOrderM.setApiResultCode("000000");
			paOrderM.setApiResultMessage("교환 발송 성공");
			paOrderM.setPaDoFlag("80");
	     	
			SuccessCount ++;
			
    	}catch(Exception e){
    		paOrderM.setApiResultCode("999999");
    		paOrderM.setApiResultMessage(ComUtil.subStringBytes("교환 완료 실패" + e.toString(), 2000));
    		paOrderM.setPaDoFlag("40");
    		
    		if(e.toString().contains("이미")) {
    			paOrderM.setApiResultCode("000000");
        		paOrderM.setApiResultMessage("(자동완료)" + ComUtil.subStringBytes("교환 완료 실패" + e.toString(), 2000));
        		paOrderM.setPaDoFlag("80");
        		SuccessCount ++;
    		}
	  	
    		return SuccessCount;
    	}finally{
    		paGmktExchangeService.updatePaOrdermResultTx(paOrderM);
        }
    	
    	return SuccessCount;
	}
	
	private int reSendCompleteForExchangeTest(PaGmktAbstractRest rest , ParamMap paramMap) throws Exception{
		int SuccessCount = 0;
  	
    	try{
			paramMap.put("urlParameter"			, "2889343963"); //contrNo
			paramMap.put("DELY_COMPLETE_DATE"	, "2019-01-23");
			paramMap.put("paCode"				, "21");
			restUtil.getConnection(rest, paramMap);
			SuccessCount ++;
			
    	}catch(Exception e){
    		return SuccessCount;
    	}finally{
    		//paGmktExchangeService.updatePaOrdermResultTx(paOrderM);
        }
    	return SuccessCount;
	}
	
	
	private int reSendShippingForExchange(PaGmktAbstractRest rest , ParamMap paramMap,  PaGmktSlipmVO slipmVo, String paGroupCode) throws Exception{

		Paorderm paorderm = null;
	
  	
    	try{
			paorderm = new Paorderm();
			paramMap.put("urlParameter"	, slipmVo.getPaOrderSeq()); //contrNo
			paramMap.put("DELY_CODE"	, slipmVo.getPaDelyGbName());
			paramMap.put("DELY_NO"		, slipmVo.getSlipNo());
			paramMap.put("paCode"		, slipmVo.getPaCode());
			
			restUtil.getConnection(rest, paramMap);
			
			paorderm.setApiResultCode("000000");
			paorderm.setApiResultMessage("교환 재발송 정보(운송장) 등록 성공");
			//paorderm.setPaDoFlag(slipmVo.getPaDoFlag());
			paorderm.setPaDoFlag("40");
	     	paorderm.setMappingSeq(slipmVo.getMappingSeq());
	    	paorderm.setPaOrderNo(slipmVo.getPaOrderNo());
	      	paorderm.setPaOrderSeq(slipmVo.getPaOrderSeq());
	       	paorderm.setPaCode(slipmVo.getPaCode());
			
    	}catch(Exception e){
    		
			if("03".equals(paGroupCode)){ //Auction의 경우 운송장 체크를 하기때문에 기타로 한번 더 보낸다.
				try{
					paorderm = new Paorderm();
					paramMap.put("DELY_CODE"	, "10034");
					paramMap.put("DELY_NO"		, slipmVo.getSlipINo());					
					restUtil.getConnection(rest, paramMap);
					
					paorderm.setApiResultCode("000000");
					paorderm.setApiResultMessage("교환 재발송 정보(운송장) 등록 성공");
					paorderm.setPaDoFlag("40");
			     	paorderm.setMappingSeq(slipmVo.getMappingSeq());
			    	paorderm.setPaOrderNo(slipmVo.getPaOrderNo());
			      	paorderm.setPaOrderSeq(slipmVo.getPaOrderSeq());
			       	paorderm.setPaCode(slipmVo.getPaCode());
			       	return  1;
				}catch(Exception ee){
					log.error("FAIL Again - Send Claim Shipping Info");
				}
			}	
    		paorderm.setApiResultCode("999999");
			paorderm.setApiResultMessage(ComUtil.subStringBytes("교환 재발송 정보(운송장) 등록 실패" + e.toString(), 2000));
			paorderm.setPaDoFlag("20");
	     	paorderm.setMappingSeq(slipmVo.getMappingSeq());
	    	paorderm.setPaOrderNo(slipmVo.getPaOrderNo());
	      	paorderm.setPaOrderSeq(slipmVo.getPaOrderSeq());
	       	paorderm.setPaCode(slipmVo.getPaCode());
	       	
	       	if(e.toString().contains("이미 완료되었습니다")) {
	       		paorderm.setApiResultCode("000000");
				paorderm.setApiResultMessage("(자동완료)" + ComUtil.subStringBytes("교환 재발송 정보(운송장) 등록 실패" + e.toString(), 2000));
				paorderm.setPaDoFlag("80");
	       	}
    		
    	}finally{
    		 paGmktExchangeService.updatePaOrdermResultTx(paorderm);
        }
    	
    	if(paorderm.getApiResultCode().equals("999999")){
    		return	0;//실패    		
    	}else{
        	return  1;//성공
    	}
	}
	
	
	private void updateGmktPaOrderMForReserve(String paCode, String payNo, String contrNo, String contrNoSeq, String holdYn, String holdReason) throws Exception{
		PaGmktPaordermVO paorderm = new PaGmktPaordermVO();

		paorderm.setPaCode(paCode);
		paorderm.setPaOrderNo(payNo);
		paorderm.setPaOrderSeq(contrNo);
		paorderm.setPaClaimNo(contrNoSeq);
		paorderm.setPaHoldYn(holdYn);
		paorderm.setPaHoldCode(holdReason);
		paGmktExchangeService.updateGmktPaOrderMForReserveTx(paorderm);
	}
	
	private void checkValidation(String payNo, String contrNo,String contrNoSeq, String holdReason, String holdReasonDetail, String paCode, String holdYn) throws Exception{
		
		if(StringUtils.isBlank(payNo)){
			throw processException("errors.invalid", new String[] {"payNo"} );
		}
		
		if(StringUtils.isBlank(contrNo)){
			throw processException("errors.invalid", new String[] {"paOrderNo"} );
		}
		
		if(StringUtils.isBlank(contrNoSeq)){
			throw processException("errors.invalid", new String[] {"contrNoSeq"} );
		}
		
		
		if(StringUtils.isBlank(paCode)){
			throw processException("errors.invalid", new String[] {"paCode"} );
		}
		
		if(!Constants.PA_GMKT_BROAD_CODE.equals(paCode) && !Constants.PA_GMKT_ONLINE_CODE.equals(paCode)){
			throw processException("errors.invalid", new String[] {"paCode"} );
		}
		
		if(holdYn.equals("1")){  //0은 교환보류 1은 교환 보류 해제 
			return;
		}
		
		if( !(holdReason.equals("0")) && !(holdReason.equals("1")) && !(holdReason.equals("4")) ){
			throw processException("errors.invalid", new String[] {"holdReason"} );
		}
		
		if(StringUtils.isBlank(holdReasonDetail)){
			throw processException("errors.invalid", new String[] {"holdReasonDetail"} );
		}
	}
	
	
}
