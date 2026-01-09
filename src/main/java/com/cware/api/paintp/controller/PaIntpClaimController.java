package com.cware.api.paintp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimListVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimProductVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimResultVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;
import com.cware.netshopping.paintp.claim.service.PaIntpClaimService;
import com.cware.netshopping.paintp.util.PaIntpComUtil;

import springfox.documentation.annotations.ApiIgnore;

import com.cware.netshopping.common.Constants;

@ApiIgnore
@Controller
@RequestMapping(value = "/paintp/claim")
public class PaIntpClaimController extends AbstractController {
	
	private transient static Logger log = LoggerFactory.getLogger(PaIntpClaimController.class);
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaIntpClaimService paIntpClaimService;
	
	@Autowired
	private PaIntpAsyncController asycController;
	
	@Autowired
	private PaIntpComUtil paIntpComUtil;
	
	private final static String OK_CODE	   		   = "200";  //특이사항 없음
	private final static String ERRORCODE1 		   = "500";  //TPAORDERM, TPACLAIMLIST 저장중 생긴 에러
	private final static String ERRORCODE2 		   = "400";  //통신의 과정에서 생긴 에러
	private final static String ERRORCODE3 		   = "490";  //API 중복 실행 에러 발생
	private final static String ERRORCODE4 		   = "499";  //기타 에러

	
	//09.클레임리스트 (반품,반취,교환,교취 데이터 생성)
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> claimList(HttpServletRequest request, @PathVariable("claimGb") String claimGb,
			@RequestParam(value = "fromDate", required = false)   String fromDate,
			@RequestParam(value = "toDate"	, required = false)   String toDate
			) throws Exception{	
		
		ParamMap paramMap  				= new ParamMap();
		PaIntpClaimListVO claimList		= null;
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params 		= new HashMap<>();
		String paCode 					= "";
		
		try {
			//API 코드 체크
			setParams(paramMap ,claimGb );
			//중복체크
			duplicateCheck(paramMap);
			
			//=0) Setting Parameter to interpark
			String endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
			String startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			log.info(paramMap.getString("apiCode") + " - 01.파라미터 검증, [fromDate={}, toDate={}]", startDate, endDate);
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo"			, paramMap.getString("apiCode"));			
			params.put("sc.entrId"			, ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
			params.put("sc.supplyEntrNo"	, apiInfo.get("INTP_ENTP_CODE").toString());
			params.put("sc.strDate"			, startDate);
			params.put("sc.endDate"			, endDate);

			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				
				try {
					//=1)실제 인터파크와 통신
					claimList  = paIntpComUtil.apiGetObjectByProgramId( apiInfo , params, PaIntpClaimListVO.class, paCode);		
					if (claimList == null || claimList.getResult() == null || !claimList.getResult().getCode().equals("000"))  throw new Exception("Result Data Error!!");
					if(claimList.getClaimList() == null || claimList.getClaimList().size() < 1) continue; //CS가 없는 경우...
					
					//=2) 1의 결과를 ClaimList()에 Insert
					for( PaIntpClaimVO vo :  claimList.getClaimList() ) {
						vo.setPaCode(paCode);
						vo.setPaOrderGb(claimGb);
						
						String clmPrdStt  =  ((PaIntpClaimProductVO)(vo.getClaimPrdList().get(0))).getCurrentClmPrdStat();
						
						switch (claimGb) {
						case "30": //반품
							if("02".equals(vo.getClmCrtTp()) && !"40".equals(clmPrdStt)) {							
								createClaimList(vo); //Insert TPAINTPCLAIMLIST, TPAORDERM
							}
							
							//직권취소 처리
							if("01".equals(vo.getClmCrtTp()) && "90".equals(clmPrdStt)) {	
								createCancelList(vo); //Insert TPAINTPORDERLIST
							}
							
							break;
						case "31": //반품철회
							if("02".equals(vo.getClmCrtTp()) && "40".equals(clmPrdStt)) {
								createClaimList(vo); //Insert TPAINTPCLAIMLIST, TPAORDERM
							}			
							break;
						case "40": //교환
							if("03".equals(vo.getClmCrtTp()) && !"40".equals(clmPrdStt)) {
								createClaimList(vo); //Insert TPAINTPCLAIMLIST, TPAORDERM
							}			
							break;						
						case "41": //교환철회
							if("03".equals(vo.getClmCrtTp()) && "40".equals(clmPrdStt)) {
								createClaimList(vo); //Insert TPAINTPCLAIMLIST, TPAORDERM
							}	
							break;
						default:
							break;
						}
					}
					paramMap.put("code"				,  OK_CODE);
					paramMap.put("message"			,  "OK");
					
					
				}catch (Exception e) {  //인터파크 통신에러에 대한 Catch문
					String errorCode = ERRORCODE2 ;
					String msg 		 = e.getMessage();
					
					if (claimList != null && claimList.getResult() != null) {
						msg = claimList.getResult().getMessage();
						msg = paCode + " : " + claimList.getResult().getMessage();
						errorCode = claimList.getResult().getCode();
					}
					//TODO ERROR 처리 필요
					log.error(msg);			
					paramMap.put("code"		,  errorCode);
					paramMap.put("message"	,  msg);
				}	
			}
			
		}catch(Exception e ) {
			String errCode = ERRORCODE4;
			String errMsg = e.toString();
			if(!"0".equals(paramMap.getString("duplicateCheck"))) {
				errMsg	= e.getMessage();
				errCode = ERRORCODE3;
			}
			paramMap.put("code"		, errCode );
			paramMap.put("message" 	, errMsg);
			
		}finally {
			// 5) API 종료 처리(duplication)
			duplicateEnd(request, paramMap);
		}
		// 6) BO-API를 호출한다.
		orderClaimMain(request, claimGb);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}

	//09.클레임리스트 (반품,교환 직권데이터 생성)
	@RequestMapping(value = "/claim-done-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> claimDoneList(HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false)   String fromDate,
			@RequestParam(value = "toDate"	, required = false)   String toDate
			) throws Exception{
		ParamMap paramMap  					= new ParamMap();
		PaIntpClaimListVO claimList			= new PaIntpClaimListVO();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params 		= new HashMap<>();
		String			    paCode		= "";

		try {
			setParams(paramMap , "60");
			
			//=0) Setting Parameter to interpark
			String endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
			String startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay(endDate, -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
			log.info(paramMap.getString("apiCode") + " - 01.파라미터 검증, [fromDate={}, toDate={}]", startDate, endDate);
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo"			, paramMap.getString("apiCode"));			
			params.put("sc.entrId"			, ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
			params.put("sc.supplyEntrNo"	, apiInfo.get("INTP_ENTP_CODE").toString());
			params.put("sc.strDate"			, startDate);
			params.put("sc.endDate"			, endDate);

			
			duplicateCheck(paramMap);
			
			for(int count = 0; count < Constants.PA_INTP_CONTRACT_CNT; count++) {
				if(count == 0) {
					paCode = Constants.PA_INTP_BROAD_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_BROAD").toString());
				} else {
					paCode = Constants.PA_INTP_ONLINE_CODE;
					params.put("sc.supplyCtrtSeq", apiInfo.get("PA_ONLINE").toString());
				}
				//=1)실제 인터파크와 통신
				claimList  = paIntpComUtil.apiGetObjectByProgramId( apiInfo , params, PaIntpClaimListVO.class, paCode);	
				if (claimList == null || claimList.getResult() == null || !claimList.getResult().getCode().equals("000"))  throw new Exception("Result Data Error!!");
				if(claimList.getClaimList() == null || claimList.getClaimList().size() < 1) continue; //CS가 없는 경우...
								
				for( PaIntpClaimVO vo :  claimList.getClaimList() ) {
					//=2) 교환인지 반품인지 확인
					String clmPrdStt  =  ((PaIntpClaimProductVO)(vo.getClaimPrdList().get(0))).getCurrentClmPrdStat();
					if( ("30".equals(clmPrdStt) || "50".equals(clmPrdStt) || "80".equals(clmPrdStt) || "90".equals(clmPrdStt) ) && !"40".equals(clmPrdStt) ) {
						if(("01".equals(vo.getClmCrtTp()) || "02".equals(vo.getClmCrtTp()))) {
							vo.setPaOrderGb("30"); //반품
						}else {
							vo.setPaOrderGb("40"); //교환
						}
					}
					vo.setPaCode(paCode);
					
					//=3) Update TPAORMDER.DO_FLAG
					try {
						paIntpClaimService.createClaimDoneListTx(vo); //*개별 단위로 트랜잭션 실행
					}catch (Exception e) {
						log.error(e.getMessage());
						continue;
					}
				}				
			}			
			paramMap.put("code"				,  OK_CODE);
			paramMap.put("message"			,  "OK");	
			
		}catch (Exception e) {
			String errCode = ERRORCODE4;
			String errMsg = e.toString();
			if(!"0".equals(paramMap.getString("duplicateCheck"))) {
				errMsg	= e.getMessage();
				errCode = ERRORCODE3;
			}
			paramMap.put("code"		, errCode );
			paramMap.put("message" 	, errMsg);

		}finally {
			duplicateEnd(request, paramMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
		
	}
	
	//10.반품/교환 입고확정
	@RequestMapping(value = "/return-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfirmProc(HttpServletRequest request) throws Exception{	
		ParamMap paramMap  				= new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params 		= new HashMap<>();
		List<Object> returnList			= null;
		
		try {
			//=0) Setting Parameter to interpark
			setParams(paramMap, "RETURN_CONFIRM" );
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			params.put("sc.entrId"			, ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
			params.put("sc.supplyEntrNo"	, apiInfo.get("INTP_ENTP_CODE").toString());
			params.put("sc.broad"			, apiInfo.get("PA_BROAD").toString());
			params.put("sc.online"			, apiInfo.get("PA_ONLINE").toString());
			paramMap.put("apiInfo"			, apiInfo);
			paramMap.put("intpParam"		, params);
			
			duplicateCheck(paramMap);
			log.info("03.회수확정 처리 건 조회");
			
			//= 1) Data SELECT
			returnList = paIntpClaimService.selectReturnOkList();	 //반품 리스트 조회
			
			//= 2) 인터파크 통신 및 TPAORDERM UPDATE
			saveReturnConfirm(returnList, paramMap);
			
		}catch (Exception e) {
			String errCode = ERRORCODE1;
			String errMsg = e.toString();
			if(!"0".equals(paramMap.getString("duplicateCheck"))) {
				errMsg	= e.getMessage();
				errCode = ERRORCODE3;
			}
			paramMap.put("code"		, errCode );
			paramMap.put("message" 	, errMsg);
		}finally {
			duplicateEnd(request, paramMap);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);	
	}
	
	//11. 교환확정
	@RequestMapping(value = "/exchange-confirm-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeConfirmProc(HttpServletRequest request) throws Exception{
		
		ParamMap paramMap  				= new ParamMap();
		List<Object> ExchangeList 		= new ArrayList<Object>();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		Map<String, String> params 		= new HashMap<>();

		try {
			//=0) Setting Parameter to interpark
			setParams(paramMap, "EXCHANGE_CONFIRM");
			apiInfo = systemService.selectPaApiInfo(paramMap);
			apiInfo.put("apiInfo", paramMap.getString("apiCode"));
			params.put("sc.entrId"			, ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
			params.put("sc.supplyEntrNo"	, apiInfo.get("INTP_ENTP_CODE").toString());
			paramMap.put("apiInfo"			, apiInfo);
			paramMap.put("intpParam"		, params);
			
			duplicateCheck(paramMap);
			log.info("03.교환확정 처리 건 조회");
			
			//= 1) Data SELECT
			ExchangeList = paIntpClaimService.selectExchangeSlipList();
			
			//= 2) 인터파크 통신 및 TPAORDERM UPDATE
			saveExchangeConfirm(ExchangeList, paramMap);
			
		}catch (Exception e) {
			String errCode = ERRORCODE1;
			String errMsg = e.toString();
			if(!"0".equals(paramMap.getString("duplicateCheck"))) {
				errMsg	= e.getMessage();
				errCode = ERRORCODE3;
			}
			paramMap.put("code"		, errCode );
			paramMap.put("message" 	, errMsg);
		}finally {
			duplicateEnd(request, paramMap);
		}	
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	public void orderClaimMain(HttpServletRequest request ,  @PathVariable("claimGb") String claimGb) throws Exception{
		
		ParamMap paramMap 				= new ParamMap();
		String   prg_id					= "";
		
		switch (claimGb) {		
		case "30":
			prg_id = "PAINTP_ORDER_CLAIM";
			break;
		case "31":
			prg_id = "PAINTP_CLAIM_CANCEL";
			break;
		case "40":
			prg_id = "PAINTP_ORDER_CHANGE";
			break;
		case "41":
			prg_id = "PAINTP_CHANGE_CANCEL";
			break;
		default :	
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"	, prg_id);
		paramMap.put("paOrderGb", claimGb);
		paramMap.put("siteGb"	, Constants.PA_INTP_PROC_ID);
		
		try {
			duplicateCheck(paramMap);
			log.info("=========================== 인터파크 Order Claim (" + claimGb   + ") Start =========================");
			// 2) Claim Target 추출
			List<HashMap<String, Object>> claimTargetList = paIntpClaimService.selectClaimTargetList(paramMap);
			
			for( HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb)) { //반품
						asycController.orderClaimAsync(claim, request);
					}else if("31".equals(claimGb)) { //반취
						asycController.claimCancelAsync(claim, request);
					}else if("40".equals(claimGb)) { //교환
						asycController.orderChangeAsync(claim, request);
					}else if("41".equals(claimGb)) { //교취
						asycController.changeCancelAsync(claim, request);	
					}
				}catch (Exception e) {
					log.error("orderClaimMainError : "+e.getMessage());
					continue;
				}
			}
			log.info("=========================== 인터파크 Order Claim (" + claimGb   + ") END =========================");
			
			paramMap.put("code"		, OK_CODE );
			paramMap.put("message" 	, "Suceessd");
			
		}catch (Exception e) {
			paramMap.put("code"		, ERRORCODE3 );
			paramMap.put("message" 	, e.toString());
		}finally {
			duplicateEnd(request, paramMap);
		}
	}

	@SuppressWarnings("unchecked")
	private void saveExchangeConfirm(List<Object> exchangeList, ParamMap paramMap) throws Exception {
		int failCnt 						= 0;
		String code 						= "";
		String message  					= "";
		Map<String, String>	tempMap   		= new HashMap<String, String>();
		String paCode						= "";
		Paorderm paorderm 					= null;
		HashMap<String, String> apiInfo 	= (HashMap<String, String>) paramMap.get("apiInfo");
		Map<String, String>		intpParam  	= (Map<String, String>)paramMap.get("intpParam");
		
		if(exchangeList.size() < 1) return;
		
		for(Object ob : exchangeList) {
			tempMap = (Map<String, String>)ob;
			paCode  = tempMap.get("PA_CODE");
			
			try {
				//intpParam.put("sc.entrId"		 , ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				intpParam.put("sc.ordclmNo"		 , tempMap.get("PA_ORDER_NO"));
				intpParam.put("sc.clmNo"		 , tempMap.get("CLM_NO"));
				intpParam.put("sc.clmSeq"		 , tempMap.get("CLM_SEQ"));
				intpParam.put("sc.delvEntrNo"	 , tempMap.get("PA_DELY_GB"));
				intpParam.put("sc.invoNo"		 , tempMap.get("SLIP_NO"));

				PaIntpClaimResultVO result = paIntpComUtil.apiGetObjectByProgramId(apiInfo, intpParam, PaIntpClaimResultVO.class, paCode);
				paorderm = setConfrimResult(result, tempMap);
				
			}catch (Exception e) {
				paorderm = setConfrimResult(null, tempMap);
				log.error(e.getMessage());
				failCnt++;
				continue;
			
			}finally {
				paIntpClaimService.updatePaOrdermResult(paorderm);
			}
		}
		
		if(failCnt == exchangeList.size() ) {
			code 	= ERRORCODE1;
			message = "교환승인 데이터가 1건도 발생하지 않았습니다.";
		}else if(failCnt != 0) {
			message = exchangeList.size() + "건 중" + failCnt + "건 실패";
			code = OK_CODE; 
		}else {
			message = exchangeList.size() + "건 성공";	
			code = OK_CODE; 			
		}
		
		paramMap.put("code"		, code	 );
		paramMap.put("message"	, message);		
	}
	
	@SuppressWarnings("unchecked")
	private void saveReturnConfirm(List<Object> returnList, ParamMap paramMap) throws Exception {
		int failCnt 						= 0;
		String code 						= "";
		String message  					= "";
		Map<String, String>	tempMap   		= new HashMap<String, String>();
		String paCode						= "";
		Paorderm paorderm 					= null;
		HashMap<String, String> apiInfo 	= (HashMap<String, String>) paramMap.get("apiInfo");
		Map<String, String>		intpParam  	= (Map<String, String>)paramMap.get("intpParam");
		
		if(returnList.size() < 1) return;
		
		for(Object ob : returnList) {
			tempMap = (Map<String, String>)ob;
			paCode  = tempMap.get("PA_CODE");
			
			try {
				//intpParam.put("sc.entrId"		 , ConfigUtil.getString(Constants.PA_INTP_ENTR_ID));
				intpParam.put("sc.ordclmNo"		 , tempMap.get("PA_ORDER_NO"));
				intpParam.put("sc.clmNo"		 , tempMap.get("CLM_NO"));
				intpParam.put("sc.clmSeq"		 , tempMap.get("CLM_SEQ"));
				//intpParam.put("sc.supplyEntrNo"  , apiInfo.get("INTP_ENTP_CODE"));
				intpParam.put("sc.supplyCtrtSeq" , (paCode.equals(Constants.PA_INTP_BROAD_CODE))? intpParam.get("sc.broad").toString() : intpParam.get("sc.online").toString());
				intpParam.put("sc.delvEntrNo"	 , tempMap.get("PA_DELY_GB"));
				intpParam.put("sc.invoNo"		 , tempMap.get("SLIP_NO"));
				intpParam.put("sc.clmCrtTp"		 , tempMap.get("PA_INTP_ORDER_GB"));

				PaIntpClaimResultVO result = paIntpComUtil.apiGetObjectByProgramId(apiInfo, intpParam, PaIntpClaimResultVO.class, paCode);
				paorderm = setConfrimResult(result, tempMap);
				
			}catch (Exception e) {
				paorderm = setConfrimResult(null, tempMap);
				log.error(e.getMessage());
				failCnt++;
				continue;
			
			}finally {
				paIntpClaimService.updatePaOrdermResult(paorderm);
			}
		}
		
		if(failCnt == returnList.size() ) {
			code 	= ERRORCODE1;
			message = "반품 교환 입고 완료 데이터가 1건도 연동되지 못했습니다.";
		}else if(failCnt != 0) {
			message = returnList.size() + "건 중" + failCnt + "건 실패";
			code = OK_CODE; 
		}else {
			message = returnList.size() + "건 성공";	
			code = OK_CODE; 			
		}
		paramMap.put("code"		, code	 );
		paramMap.put("message"	, message);		
	}
	
	private Paorderm setConfrimResult(PaIntpClaimResultVO result, Map<String, String> dataMap) throws Exception {
		Paorderm paorderm = new Paorderm();
		String sysdate 	  = DateUtil.getCurrentDateTimeAsString();
		
		switch (dataMap.get("PA_ORDER_GB")) {
		case "30":
			if (result != null && result.getResult().getCode().equals("000") ) {
				dataMap.put("result_code"	,	 "0");
				dataMap.put("result_text"	,	"반품승인처리 성공");
				dataMap.put("PA_DO_FLAG"	,	"60");
			}else {
				dataMap.put("result_code"	, 	"-999");
				dataMap.put("result_text"	,	"반품승인처리 실패");
				//dataMap.put("paDoFlag"		,	"");
			}
			break;
		case "45":
			if (result != null && result.getResult().getCode().equals("000") ) {
				dataMap.put("result_code"	,	 "0");
				dataMap.put("result_text"	,	"교환회수승인 성공");
				dataMap.put("PA_DO_FLAG"	,	"60");
			}else {
				dataMap.put("result_code"	, 	"-999");
				dataMap.put("result_text"	,	"교환회수승인 실패");
				//dataMap.put("paDoFlag"		,	"");
			}
			break;			
		case "40":
			if (result != null && result.getResult().getCode().equals("000") ) {
				dataMap.put("result_code"	,	 "0");
				dataMap.put("result_text"	,	"교환완료처리 성공");
				dataMap.put("PA_DO_FLAG"	,	"80");
			}else {
				dataMap.put("result_code"	, 	"-999");
				dataMap.put("result_text"	,	"교환완료처리 실패");
				//dataMap.put("paDoFlag"		,	"");
			}
			break;
		default:
			//Throws Exception
			break;
		}
		
		paorderm.setApiResultCode		(dataMap.get("result_code"));
		paorderm.setApiResultMessage	(dataMap.get("result_text"));
		paorderm.setPaDoFlag			(dataMap.get("PA_DO_FLAG"));
		paorderm.setPaOrderGb			(dataMap.get("PA_ORDER_GB"));
		paorderm.setProcDate			(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setPaOrderNo			(dataMap.get("PA_ORDER_NO"));
		paorderm.setPaClaimNo			(dataMap.get("CLM_NO"));
		paorderm.setPaCode				(dataMap.get("PA_CODE"));
		
		return paorderm;
	}
		
	
	private void createClaimList(PaIntpClaimVO claimVO) throws Exception {
		try {
			paIntpClaimService.createClaimListTx(claimVO); //개별 단위로 트랜잭션 실행
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	private void createCancelList(PaIntpClaimVO claimVO) throws Exception {
		try {
			paIntpClaimService.createCancelListTx(claimVO); //개별 단위로 트랜잭션 실행
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	

	private void setParams(ParamMap paramMap ,String claimGb ) throws Exception {
		
		String prg_id 	 = "";
		
		switch (claimGb) {
		case "30":
			prg_id = "IF_PAINTPAPI_05_001";  //반품조회
			break;
		case "31":
			prg_id = "IF_PAINTPAPI_05_002";  //반품철회
			break;
		case "40":
			prg_id = "IF_PAINTPAPI_04_001";  //교환조회
			break;
		case "41":
			prg_id = "IF_PAINTPAPI_04_003";  //교환철회
			break;
		case "60":
			prg_id = "IF_PAINTPAPI_05_004";  //반품(교환)완료 : 직권
			break;
		case "RETURN_CONFIRM":    
			prg_id = "IF_PAINTPAPI_05_003";  //반품,교환 입고확정
			break;
		case "EXCHANGE_CONFIRM":
			prg_id = "IF_PAINTPAPI_04_002";//교환승인
			break;
		default:
			throw new Exception("URL ERROR");
		
		}
		
		paramMap.put("apiCode"	  , prg_id);		
		paramMap.put("broadCode"  , Constants.PA_INTP_BROAD_CODE);
		paramMap.put("onlineCode" , Constants.PA_INTP_ONLINE_CODE);
		paramMap.put("siteGb"	  , Constants.PA_INTP_PROC_ID);
		paramMap.put("startDate"  , systemService.getSysdatetimeToString());
	}

	private void duplicateCheck(ParamMap paramMap) throws Exception  {
		String prg_id = paramMap.getString("apiCode");
		log.info(prg_id + " - 02.프로그램 중복 실행 검사 [start]");
		String duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
		if ("1".equals(duplicateCheck)) {
			log.info(prg_id + " - EE.프로그램 중복 실행 오류");
			throw processException("msg.batch_process_duplicated", new String[] {prg_id});
		}else {
			paramMap.put("duplicateCheck", 0);
		}
	}
	
	private void duplicateEnd(HttpServletRequest request, ParamMap paramMap) throws Exception {
		
		String duplicateCheck = paramMap.getString("duplicateCheck");
		String prg_id 		  = paramMap.getString("apiCode");
		
		log.info(paramMap.getString("apiCode") + " - 04.프로그램 중복 실행 검사 [end]");
		if("0".equals(duplicateCheck)){
			systemService.checkCloseHistoryTx("end", prg_id);
		}
		
		log.info(prg_id + " - 05.API trace information save 처리");
		systemService.insertApiTrackingTx(request, paramMap);
	
	}	
}
