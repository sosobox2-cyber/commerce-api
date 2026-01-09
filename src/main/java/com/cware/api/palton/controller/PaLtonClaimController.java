package com.cware.api.palton.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.cware.netshopping.domain.PaLtonCancelListVO;
import com.cware.netshopping.domain.PaLtonClaimListVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.model.PaLtonNotReceiveList;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.palton.claim.service.PaLtonClaimService;
import com.cware.netshopping.palton.delivery.service.PaLtonDeliveryService;
import com.cware.netshopping.palton.goods.service.PaLtonGoodsService;
import com.cware.netshopping.palton.util.PaLtonComUtill;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/palton/claim", description="롯데온 취소/반품/교환")
@Controller
@RequestMapping(value = "/palton/claim")
public class PaLtonClaimController extends AbstractController  {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private PaLtonAsyncController paLtonAsyncController;
	@Autowired
	private PaLtonConnectUtil paLtonConnectUtil;
	@Autowired
	private PaLtonClaimService paLtonClaimService;
	@Autowired
	private PaLtonDeliveryController paLtonDeliveryController;
	@Autowired
	private PaLtonDeliveryService paLtonDeliveryService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paorderService;
	
	@Resource(name = "palton.goods.paLtonGoodsService")
	private PaLtonGoodsService paLtonGoodsService;
	
	
	
	private transient static Logger log = LoggerFactory.getLogger(PaLtonClaimController.class);
	
	//***************************************************************************************************************************//
	//************************************************** 호출되는 API 영역 **********************************************************//	
	//***************************************************************************************************************************//

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문취소요청/완료/철회", notes = "cancelGb = 02 요청, 21 취소완료, 22 취소요청 철회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-list/{cancelGb}", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> cancelList(
			@PathVariable("cancelGb") String cancelGb,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request
			) throws Exception {
		
		Map<String, Object> map 			 	= new HashMap<String, Object>() ;
		List<Map<String, Object>> ltonDataList 	= new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> itemList 		= new ArrayList<Map<String,Object>>();
		
		String paCode						 	= "";
		String apiCode							= "";
		ParamMap				apiInfoMap	 	= new ParamMap();
		ParamMap				apiDataMap	 	= new ParamMap();
		String 					clmNo			= "";
		String					odNo			= "";		
		String					errMsg			= "";
		
		try {
			//=0) API Code Setting
			switch (cancelGb) {
			case "02": //요청
				apiCode = "IF_PALTONAPI_04_001";
				break;
			case "21": //취소완료
				apiCode = "IF_PALTONAPI_04_020";
				break;
			case "22": //취소요청철회
				apiCode = "IF_PALTONAPI_04_019";
				break;
			default:
				throw new Exception("cancelGb is wrong");
			}
			
			//=1) API Parameter Setting
			paLtonConnectUtil.getApiInfo(apiCode		, apiInfoMap);					//롯데온 통신에 필요한 정보 세팅(URL, Method등등)
			paLtonConnectUtil.checkDuplication(apiCode	, apiInfoMap);					//API 중복체크

			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT ; i++ ) {
				//=2) Connect LotteOn Server
				paCode = (i==0 )? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				settingParams("20" , cancelGb , fromDate, toDate, apiInfoMap, apiDataMap);		//롯데온 API에 필요한 Request 세팅 
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				if(!Constants.PA_LTON_SUCCESS_CODE.equals( map.get("returnCode"))) {
					log.info("cancel-list/" +  cancelGb +" ("+map.get("returnCode").toString() +")" + map.get("message").toString());
					continue;
				};	
				
				//=3) Insert TPALTONCLAIMLIST (TPAORDERM)
				ltonDataList = (List<Map<String, Object>>)map.get("data");
				for(Map<String, Object> dataMap :  ltonDataList ) {
					odNo = dataMap.get("odNo").toString();
					clmNo = dataMap.get("clmNo").toString();
					itemList =  (List<Map<String, Object>>)dataMap.get("itemList");
					
						for(Map<String, Object> cancelMap :  itemList ) {
							try {
								cancelMap.put("odNo"		, odNo);
								cancelMap.put("clmNo"		, clmNo);
								cancelMap.put("cancelGb"	, cancelGb);
								cancelMap.put("paCode"		, paCode);		
								//= Insert TLTONCANCELLIST
								saveLtonCacncelList(cancelMap);
							}catch (Exception e) {
								log.error("ERROR - SaveLtonCancelList :::::" + PaLtonComUtill.getErrorMessage(e));
								errMsg += "주문번호 : " + odNo + " - " + PaLtonComUtill.getErrorMessage(e, 500) + "\n";  
								continue;
							}
						}
					}
				}
			
			if(!"".equals(errMsg)) throw new Exception("ERROR - SaveLtonCancelList ::::: " + errMsg); // by 건당 실패에 대해서 ApiTracking에 노출하기 위해 
			

		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("02".equals(cancelGb)) { //주문취소(cancelGb == 02 일때만 주문 취소데이터 생성 및 BO데이터 생성 , 직권은 주문취소 API에서 BO데이터 생성)
			cancelConfirmProc(request); 	//=주문승인 (또는 거절, 대기)
			cancelInputMain(request);		//=BO 주문 데이터 생성
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
		
	/**
	 * 취소요청승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "취소요청승인처리", notes = "취소요청승인처리", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-confirm-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc(HttpServletRequest request) throws Exception {
	
		String prg_id 			= "IF_PALTONAPI_04_015";
		ParamMap paramMap		= new ParamMap();
		int doFlag 		 		= 0;
		String 	resultMsg 	 	= "취소 실패 주문번호 목록 : ";	
		
		try {
			log.info(prg_id + " - 01. 롯데온 취소요청 접수 내역 조회");
			paLtonConnectUtil.getApiInfo(prg_id, paramMap);
			
			log.info(prg_id + " - 02.프로그램 중복 실행 검사 [start]");
			paLtonConnectUtil.checkDuplication(prg_id, paramMap);
			
			List<Map<String, Object>> cancelList = paLtonClaimService.selectPaLtonOrderCancelList();
			
			log.info(prg_id + " - 03. 롯데온 취소요청 승인 및 거부 처리");
			for( Map<String, Object> cancel : cancelList) {
				doFlag = Integer.parseInt(String.valueOf(cancel.get("DO_FLAG")));

				if((doFlag > 30 && StringUtils.hasText(cancel.get("SLIP_NO").toString())) || (((BigDecimal)cancel.get("PARTIAL_QTY")).intValue() > 0)) { //송장나왔거나 주문수량<->취소수량 다르면 거절
					try {
						cancelRefuse(cancel);//취소 거절
					}catch (Exception e) {
						resultMsg = resultMsg + "/" + PaLtonComUtill.getErrorMessage(e);
						paramMap.put("code"		, "498");
						paramMap.put("message"	, resultMsg);
						continue;
					}			
				} else if(doFlag < 30) {
					try {
						cancel.put("OUT_CLAIM_GB", "0"); //출고전 취소
						cancelConfirm(cancel);
					}catch (Exception e) {
						resultMsg = resultMsg + "/" + PaLtonComUtill.getErrorMessage(e);
						paramMap.put("code"		, "498");
						paramMap.put("message"	, resultMsg);
						continue;
					}
				} else {
					continue; //BO 상담원 취소 승인/거절 프로그램에서 처리  //TODO 11번가처럼 특정시간 지나면 자동승인될경우 이곳에서 처리
				}
			}
		}catch (Exception e) {
			paLtonConnectUtil.checkException(paramMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, paramMap);
		}
				
		return new ResponseEntity<>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 판매자직접취소 (품절취소반품)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "판매자직접취소 (품절취소반품)", notes = "판매자직접취소 (품절취소반품)", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/soldOut-cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private void orderSoldOut(HttpServletRequest request) throws Exception {
		
		ParamMap				apiInfoMap	= new ParamMap();
		ParamMap				apiDataMap	= new ParamMap();
		String					prg_id		= "IF_PALTONAPI_04_018_BO"; //품절로 인한 판매자 직접취소
		String 					resultMsg	= "재고부족 판매자 취소처리(품절취소반품)";
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		String paGroupCode   				= "08";
		HashMap<String, String> map 		= new HashMap<String, String>();
		PaLtonGoodsdtMappingVO dtMapping    = new PaLtonGoodsdtMappingVO();
		String dateTime                     = "";
		ParamMap paramMap = new ParamMap(); 
		
		try {
			log.info(prg_id + "재고부족 판매자 취소처리(품절취소반품)");
			paLtonConnectUtil.getApiInfo	  (prg_id	, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			dateTime = systemService.getSysdatetimeToString();
			
			//=1) 판매자 주문 취소할 LIST를 생성해준다
			paramMap.put("paGroupCode", paGroupCode);
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			
			if(cancelList.size() > 0) {
				for (int i = 0; i < cancelList.size(); i++) {
					try {
						
						HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
						HashMap<String, Object> refusalMap = paLtonDeliveryService.selectRefusalInfo(cancelMap.get("MAPPING_SEQ"));
						
						List<Map<String, Object>> itemList  = new ArrayList<Map<String,Object>>();
						itemList.add(refusalMap);
						
						if(itemList.size() == 0) return;
						
						//=2) 롯데온 판매자 취소 API를 호출할 인자를 SETTING 한다.
						PaLtonComUtill.replaceCamelList(itemList);
						
						paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
						
						apiDataMap.put("odNo"		, cancelMap.get("PA_ORDER_NO"));
						apiDataMap.put("itemList"	, itemList);
						
						map.put("PA_GROUP_CODE", paGroupCode);
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("SITE_GB", cancelMap.get("SITE_GB"));
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN"));
						
						map.put("ORDER_CANCEL_YN", "10");
						map.put("RSLT_MESSAGE", "판매취소 성공");
						
						dtMapping.setGoodsCode(cancelMap.get("GOODS_CODE"));
						dtMapping.setGoodsdtCode(cancelMap.get("EITM_NO").substring(10,13));
						dtMapping.setGoodsdtSeq(cancelMap.get("EITM_NO").substring(13,16));
						dtMapping.setPaCode(cancelMap.get("PA_CODE"));
						dtMapping.setTransOrderAbleQty("0");
						dtMapping.setTransStockYn("1");
						dtMapping.setModifyId("PALTON");
						dtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						//=3) 판매자취소 API 호출과 통신후 결과를 TPAORDERM에 적용한다.
						connectResult = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
						if(!Constants.PA_LTON_SUCCESS_CODE.equals(connectResult.get("returnCode").toString())) 
							throw new Exception("판매자 주문 취소 실패 + " + connectResult.get("returnCode").toString());
					
					}catch(Exception e) {
						resultMsg = PaLtonComUtill.getErrorMessage(e);
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", resultMsg);
						continue;
					} finally {
						paorderService.updateOrderCancelYnTx(map);
						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
						
						if("05".equals(map.get("ORG_ORD_CAN_YN"))) { // 품절취소반품일 경우
							paLtonGoodsService.updateGoodsdtPaOptionCode(dtMapping);
						}
					}
				}
			} 
		} catch(Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
	}
	/*
	 * API Information	 : BO(상담원 취소 관리) 프로그램에서 취소 승인/거절 을 했을때 롯데온과 데이터 통신 후 내부 데이터(TPAORDERM) 생성
	 * Return Code - 200 : 통신 및 모든 데이터가 제대로 생성됨  , 100 : 취소거부(승인) 대상이 존재하지 않음 , 500 : 기타 모든 에러 
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "BO(상담원 취소 관리) 프로그램에서 취소 승인/거절 을 했을때 내부 데이터(TPAORDERM) 생성", notes = "Return Code - 200 : 통신 및 모든 데이터가 제대로 생성됨  , 100 : 취소거부(승인) 대상이 존재하지 않음 , 500 : 기타 모든 에러", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-approval-proc-bo", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> cancelConfirmProc2BO(
			@RequestParam(value = "odNo"		, required = true) 		String odNo,
			@RequestParam(value = "ordSeq"		, required = true)	 	String odSeq,
			@RequestParam(value = "clmreqSeq"	, required = true)	 	String clmNo,
			@RequestParam(value = "procSeq"		, required = true) 		String procSeq,
			@RequestParam(value = "paCode"		, required = true) 		String paCode,
			@RequestParam(value = "procFlag"	, required = true) 		String procFlag,
			@RequestParam(value = "mode"		, required = false 	,	defaultValue = "0"	) String mode, //통신은 건너뛰고 PAORDERM만 생성하고 싶을때..
			HttpServletRequest request) throws Exception {
		
		String rtnCode = "000";
		String rtnMsg  = "odNo="+ odNo  + " ordSeq="+ odSeq + " clmNo=" + clmNo  + " procSeq=" + procSeq + " procFlag=" + procFlag;
		Map<String , Object> resultMap = new HashMap<String, Object>();
		
		ParamMap apiInfoMap = new ParamMap();
		ParamMap apiDataMap = new ParamMap();
		List<Map<String, Object>> cancelDtList = new ArrayList<Map<String,Object>>();
		apiInfoMap.put("odNo"			, odNo);
		apiInfoMap.put("odSeq"			, odSeq);
		apiInfoMap.put("clmNo"			, clmNo);
		apiInfoMap.put("procSeq"		, procSeq);
		apiInfoMap.put("paCode"			, paCode);
		apiInfoMap.put("procFlag"		, procFlag);
		
		try {
			//=1) Request Data Setting
			HashMap<String, Object> cancelMap = paLtonClaimService.selectPaLtonOrderCancel(apiInfoMap);
			if(cancelMap == null) {
				rtnCode = "100";
				throw new Exception("이미 처리되었습니다.");
			}
			
			cancelMap = (HashMap<String, Object>)PaLtonComUtill.replaceCamel(cancelMap);
			apiDataMap.put("odNo"		, odNo);
			apiDataMap.put("clmNo"		, clmNo);
			
			if("10".equals(procFlag)) { //취소 승인
				paLtonConnectUtil.getApiInfo("IF_PALTONAPI_04_015", apiInfoMap);
				cancelDtList.add(cancelMap);
				apiDataMap.put("itemList"	, cancelDtList);
				
			}else if("20".equals(procFlag)) { //취소 거절
				paLtonConnectUtil.getApiInfo("IF_PALTONAPI_04_016", apiInfoMap);
				cancelMap.put("slrRsnCd"		, "901");
				cancelMap.put("slrRsnCnts"		, "이미 상품을 발송함");
				cancelDtList.add(cancelMap);
				apiDataMap.put("itemList"	, cancelDtList);
			}
			
			//=2) Connect LotteOn Server
			if("0".equals(mode)) {
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				validationCheckForCancel(resultMap, cancelDtList); 
			}
			
			//=3) Insert TPAORDERM 
			if("10".equals(procFlag)) { //취소 승인
				ParamMap param = new ParamMap();
				param.put("paCode", paCode);
				param.put("odNo", cancelMap.get("odNo"));
				param.put("clmNo", cancelMap.get("clmNo"));
				// 롯데온 취소승인 clmNo 기준으로 한번에 처리됨 (같은 clmNo 모두 데이터 처리 진행)
				List<Map<String, Object>> cancelDtListCheck = paLtonClaimService.selectPaLtonCancelApprovalListBO(param);
				PaLtonComUtill.replaceCamelList(cancelDtListCheck);
				
				for (Map<String, Object> cancelDt : cancelDtListCheck) {
					cancelDt.put("outClaimGb"	, "1");
					cancelDt.put("paOrderGb"	, "20");
					cancelDt.put("procFlag"	, procFlag);
					
					paLtonClaimService.saveCancelConfirmTx(cancelDt);
				}
			}
			
			rtnCode= "200";
			
		}catch (Exception e) {
			rtnCode = "500";
			rtnMsg = rtnMsg + PaLtonComUtill.getErrorMessage(e);
		}finally {
			apiInfoMap.put("code"		, rtnCode);
			apiInfoMap.put("message"	, rtnMsg);
			systemService.insertApiTrackingTx(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품/교환 처리", notes = "claimGb = 30  반품, 31 반품취소, 40 교환, 41 교환취소  ", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/claim-list/{claimGb}", method = RequestMethod.GET)  //30  반품, 31 반품취소, 40 교환, 41 교환취소 
	@ResponseBody
	public ResponseEntity<?> claimList(HttpServletRequest request, @PathVariable("claimGb") String claimGb,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "claimStep", required = false, defaultValue = "03") String claimStep //02 - 요청(사용X), 03 - 접수, 27 - 반품완료 >> 반품접수 조회시에만 사용
			) throws Exception{
		
		Map<String, Object>       map 		   = new HashMap<String, Object>() ;
		List<Map<String, Object>> ltonDataList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> itemList 	   = new ArrayList<Map<String,Object>>();
		ParamMap				  apiInfoMap   = new ParamMap();
		ParamMap				  apiDataMap   = new ParamMap();
		String 					  paCode	   = "";
		String					  prg_id	   = "";
		String 					  clmNo		   = "";
		String					  oldNo		   = "";
		String					  errMsg	   = "";
				
		try {
			//=1) API Parameter Setting
			prg_id = getClaimApiCode(claimGb, claimStep);								
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);				    //롯데온 통신에 필요한 정보 세팅(URL, Method등등)
			paLtonConnectUtil.checkDuplication(prg_id, apiInfoMap);				//API 중복체크
			
			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT; i++) {
				//=2) Connect LotteOn Server
                paCode = ( i==0 ) ? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
                apiInfoMap.put("paCode"		, paCode);
                apiInfoMap.put("apiCode"	, prg_id);
				settingParams(claimGb, claimStep, fromDate, toDate, apiInfoMap, apiDataMap);	//롯데온 API에 필요한 Request 세팅 
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				if(!Constants.PA_LTON_SUCCESS_CODE.equals( map.get("returnCode"))) { //Validation Check
					log.info("claim-list/" +  claimGb + " (" + map.get("returnCode").toString() + ")" + map.get("message").toString());
					continue;
				}
				
				//=3) Insert TPALTONCLAIMLIST ,TPAORDERM
				ltonDataList = (List<Map<String, Object>>)map.get("data");
		
				if(ltonDataList.size() < 1) continue;
				
				for(Map<String, Object> dataMap :  ltonDataList ) {
					oldNo = dataMap.get("odNo").toString();
					clmNo = dataMap.get("clmNo").toString();
					itemList = (List<Map<String, Object>>)dataMap.get("itemList");
					
					for(Map<String, Object> returnMap : itemList) {

						try {
							returnMap.put("paCode"		, paCode);
							returnMap.put("odNo"		, oldNo);
							returnMap.put("clmNo"		, clmNo);
							returnMap.put("claimGb"		, claimGb);
							returnMap.put("paOrderGb"	, claimGb);
							returnMap.put("claimStep"	, claimStep);
							saveLtonClaimList(returnMap);					
						
						}catch (Exception e) {
							log.error("ERROR - SaveLtonClaimList :::::" + PaLtonComUtill.getErrorMessage(e));
							errMsg += "주문번호 : " + oldNo + "(" + claimGb + ")"+ " - " + PaLtonComUtill.getErrorMessage(e, 500) + "\n";  
							continue;
						}
					}
				}
			}
			
			if(!"".equals(errMsg)) throw new Exception("ERROR - SaveLtonClaimList ::::: " + errMsg);
			
		} catch (Exception e) {	
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
	
		//=5) Save BO Data
		orderClaimMain(request, claimGb);	// BO데이터 생성
				

	    /* 롯데온 교환,반품 승인처리 불필요
	     * 
		if("30".equals(claimGb)  && "02".equals(claimStep)) {  //claimStep - 02:요청 , 03:접수(승인필요없음) 
			returnConfirm(request);  //반품승인처리
		} 
		if("40".equals(claimGb)) {
			exchangeConfirm(request);
			//exchangeRefuse(request);
		} */
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "회수확정/교환회수/교환출고/교환배송완료 처리", notes = "클레임상태통보", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/claim-slip-out-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> claimSlipOutProc(HttpServletRequest request) throws Exception{
				
		ParamMap			apiInfoMap		= new ParamMap();
		String 				api_code 		= "IF_PALTONAPI_03_008";
		String				errMsg			= "";

		try {
			paLtonConnectUtil.getApiInfo		(api_code	, apiInfoMap);
			paLtonConnectUtil.checkDuplication	(api_code	, apiInfoMap);
			
			//회수 확정건
			List<Map<String,Object>> returnSlipProcList = paLtonClaimService.selectReturnSlipProcList();

			for(  Map<String,Object> ro   :   returnSlipProcList ) {
				try {
					Map<String, Object> resultMap = paLtonDeliveryController.deliveryProgressToLton(ro, apiInfoMap);
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						paLtonDeliveryController.updatePaOrderM(ro, "60", "회수확정", resultCode);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMsg);
						paLtonDeliveryController.updatePaOrderM(ro, null , resultMsg, resultCode);
					}			
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "회수확정_주문번호 : " + ro.get("OD_NO") + " - " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
					continue;
				}
			}
			
			//교환 회수건
			List<Map<String,Object>> exchangeProcList = paLtonClaimService.selectExchangeSlipProcList();

			for(  Map<String,Object> ro   :   exchangeProcList ) {
				try {
					Map<String, Object> resultMap  = paLtonDeliveryController.deliveryProgressToLton(ro, apiInfoMap);
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						paLtonDeliveryController.updatePaOrderM(ro, "60", "교환회수확정", resultCode);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMsg);
						paLtonDeliveryController.updatePaOrderM(ro, null , resultMsg, resultCode);
					}			
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "교환회수_주문번호 : " + ro.get("OD_NO") + " - " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
					continue;
				}
			}
			
			//교환 출고건
			List<Map<String,Object>> exchangeSendList = paLtonClaimService.selectExchangeSendList();

			for(  Map<String,Object> ro   :   exchangeSendList ) {
				try {
					Map<String, Object> resultMap  = paLtonDeliveryController.deliveryProgressToLton(ro, apiInfoMap);
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						paLtonDeliveryController.updatePaOrderM(ro, "40", "교환출고완료", resultCode);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMsg);
						paLtonDeliveryController.updatePaOrderM(ro, null , resultMsg, resultCode);
					}			
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "교환출고_ 주문번호 : " + ro.get("OD_NO") + " - " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
					continue;
				}
			}
			
			//교환 배송완료건
			List<Map<String,Object>> exchangeCompleteList = paLtonClaimService.selectExchangeCompleteList();

			for(  Map<String,Object> ro   :   exchangeCompleteList ) {
				try {
					Map<String, Object> resultMap = paLtonDeliveryController.deliveryProgressToLton(ro, apiInfoMap);
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						paLtonDeliveryController.updatePaOrderM(ro, "80", "교환완료", resultCode);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMsg);
						paLtonDeliveryController.updatePaOrderM(ro, null , resultMsg, resultCode);
					}			
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "교환배송_ 주문번호 : " + ro.get("OD_NO") + " - " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
					continue;
				}
			}
			
			
			if(!"".equals(errMsg)) 	throw new Exception("slipOutProc :: " + errMsg); // by 건당 실패에 대해서 ApiTracking에 노출하기 위해 
			
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}

		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 교환요청거부처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	/* 22/06/28 부터 사용 불가
	@RequestMapping(value = "/exchange-refuse", method = {RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<?> exchangeRefuse(
			HttpServletRequest request) throws Exception {
		
		String 	 prg_id 			 = "IF_PALTONAPI_04_009";
		Map<String,Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap 		 = new ParamMap();
		ParamMap apiDataMap 		 = new ParamMap();
		int 	 excuteCnt 			 = 0;
		String   errMsg				 = "";

		try {
			//=1) API Parameter Setting
			paLtonConnectUtil.checkDuplication(prg_id, apiInfoMap);				//API 중복체크
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);				    //롯데온 통신에 필요한 정보 세팅(URL, Method등등)
						
			List<Map<String,Object>> exchangeRefuseList = paLtonClaimService.selectPaLtonExchangeRefuseList();
			
			for(Map<String,Object> exchangeRefuseMap : exchangeRefuseList) {
				
				try {
					//=setting request
					apiInfoMap.put("paCode"	, exchangeRefuseMap.get("PA_CODE").toString());
					apiDataMap.put("paCode"	, exchangeRefuseMap.get("PA_CODE").toString());
					apiDataMap.put("odNo"	, exchangeRefuseMap.get("PA_ORDER_NO").toString());
					apiDataMap.put("clmNo"	, exchangeRefuseMap.get("PA_CLAIM_NO").toString());
					List<Map<String,Object>> exchangeRefuseDtList = paLtonClaimService.selectPaLtonExchangeRefuseDtList(apiDataMap);
					PaLtonComUtill.replaceCamelList(exchangeRefuseDtList);
					apiDataMap.put("data", exchangeRefuseDtList);
					
					//=Connect Lton Server
					resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode").toString())) {
						apiDataMap.put("preCancelReason", "교환 거부 성공");
						apiDataMap.put("preCancelYn", "1");
					} else {
						apiInfoMap.put("code"			, resultMap.get("returnCode").toString());
						apiInfoMap.put("message"		, apiInfoMap.get("message").toString() + " " + resultMap.get("message").toString());
						apiDataMap.put("preCancelReason", resultMap.get("message").toString());
						apiDataMap.put("preCancelYn"	, "0");
					}				
					
					excuteCnt = paLtonClaimService.updateTPaOrderM4ChangeRefualReuslt(apiDataMap);
					if(excuteCnt < 1) throw processException("errors.process", new String[] {  "교환거부처리 UPDATE 오류 발생" });
					
				}catch (Exception e) {
					log.error(" 교환거부 " + exchangeRefuseMap.get("PA_ORDER_NO") + PaLtonComUtill.getErrorMessage(e) );
					errMsg += "주문번호 : " + exchangeRefuseMap.get("PA_ORDER_NO") + " - " + PaLtonComUtill.getErrorMessage(e, 500) + "\n";
					continue;
				}
				
				if(!"".equals(errMsg)) throw new Exception("ERROR - 교환거부 ::::: " + errMsg); // by 건당 실패에 대해서 ApiTracking에 노출하기 위해 
			}
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	*/
	
	
//****************************************************************************************************************************//
//************************************************** 내부적으로 사용하는 함수 영역 ****************************************************//	
//***************************************************************************************************************************//
	private void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PALTON_CANCEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paOrderGb" 	, "20");
		paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paLtonConnectUtil.checkDuplication	(prg_id		, paramMap);	
			
			log.info(prg_id + " - 02.주문취소내역 생성 서비스 호출");
			List<HashMap<String,Object>> cancelInputTargetList = paLtonClaimService.selectClaimTargetList(paramMap);
			
			for (HashMap<String,Object> cancelTargetList : cancelInputTargetList ) {
				try {
					//외부 API 를 호출하기에 건별로 transaction을 갖도록 controller를 호출 하도록 개발됨. 개별 rollback
					paLtonAsyncController.cancelInputAsync(cancelTargetList, request);
				}
				catch ( Exception e ) {
					log.error(prg_id + " - EE.주문 취소 내역 생성 오류", e);
					continue;
				}
			}
		}
		catch ( Exception e ) {
			log.error(prg_id + " - EE.주문취소내역 생성 오류", e);
			paLtonConnectUtil.checkException(paramMap, e);
		}
		finally {
			log.info(prg_id + " - 03.프로그램 중복 실행 검사 [end]");		
			paLtonConnectUtil.closeApi(request, paramMap);	
		}		
	}
	
	private void orderClaimMain(HttpServletRequest request, String claimGb) throws Exception {
		
		ParamMap paramMap 				= new ParamMap();
		String   prg_id					= "";

		switch (claimGb) {		
		case "30":
			prg_id = "PALTON_ORDER_CLAIM";
			break;
		case "31":
			prg_id = "PALTON_CLAIM_CANCEL";
			break;
		case "40":
			prg_id = "PALTON_ORDER_CHANGE";
			break;
		case "41":
			prg_id = "PALTON_CHANGE_CANCEL";
			break;
		default :	
			throw new Exception("PA_ORDER_GB ERROR");
		}
		
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paOrderGb"	, claimGb);
		paramMap.put("siteGb"		, Constants.PA_LTON_PROC_ID);
		
		try {

			paLtonConnectUtil.getApiInfo(prg_id, paramMap);
			paLtonConnectUtil.checkDuplication(prg_id, paramMap);
			
			log.info("========================= 롯데온 Order Claim (" + claimGb + ") Start =========================");
			// 2) Claim Target 추출
			List<HashMap<String, Object>> claimTargetList = paLtonClaimService.selectClaimTargetList(paramMap);
			
			for( HashMap<String, Object> claim : claimTargetList) {
				try {
					if("30".equals(claimGb)) { //반품
						paLtonAsyncController.orderClaimAsync(claim, request);
					}else if("31".equals(claimGb)) { //반취
						paLtonAsyncController.claimCancelAsync(claim, request);
					}else if("40".equals(claimGb)) { //교환
						paLtonAsyncController.orderChangeAsync(claim, request);
					}else if("41".equals(claimGb)) { //교취
						paLtonAsyncController.changeCancelAsync(claim, request);	
					}
				}catch (Exception e) {
					log.error("orderClaimMainError : "+e.getMessage());
					continue;
				}
			}
			log.info("========================= 롯데온 Order Claim (" + claimGb + ") END =========================");
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(paramMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, paramMap);	
		}
	}
	
	/**
	 * 반품요청승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "반품요청승인처리", notes = "반품요청승인처리", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> returnConfirm(HttpServletRequest request) throws Exception  {
		
		String 		prg_id 				= "IF_PALTONAPI_04_004";
		ParamMap	apiInfoMap	 		= new ParamMap();
		ParamMap	apiDataMap	 		= new ParamMap();
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		String      flag				= "RETURN";
		String		rtnMsg				= "";

		try {
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			apiDataMap.put("flag"       , flag);
				
			log.info(prg_id + " - 02.반품 승인 리스트 조회");
			List<Map<String, Object>> returnConfirmList = paLtonClaimService.selectPaLtonReturnExchangeApprovalList(apiDataMap);
			
			for(Map<String, Object> returnMap : returnConfirmList) {
				apiDataMap.put("paCode"    	, returnMap.get("PA_CODE"));
	            apiDataMap.put("odNo"    	, returnMap.get("OD_NO"));
	            apiDataMap.put("clmNo"      , returnMap.get("CLM_NO"));
	            List<Map<String, Object>> returnDtList = paLtonClaimService.selectPaLtonReturnExchangeApprovalDtList(apiDataMap);
	            PaLtonComUtill.replaceCamelList(returnDtList);
	            apiDataMap.put("itemList"    , returnDtList);
	            
	            log.info(prg_id + " 롯데온 반품요청 승인 처리" + "주문번호 : " + returnMap.get("OD_NO") +"  / "  + returnMap.get("CLM_NO"));
	            resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
	            
	            if(resultMap != null && Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode").toString())) {
	            	rtnMsg += paLtonDeliveryController.updatePaOrderM(returnDtList, "20", "반품승인완료", resultMap.get("returnCode").toString());
	            }else {
	            	//TPAAPITRACKING 메세지처리
	            	apiInfoMap.put("code", resultMap.get("returnCode").toString());
					apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMap.get("message").toString());
					rtnMsg += paLtonDeliveryController.updatePaOrderM(returnDtList, null, resultMap.get("message").toString(), resultMap.get("returnCode").toString());
	            }
			}
			
			if(!"".equals(rtnMsg)) throw new Exception("반품 승인 리스트 UPDATE_DO_FLAG 실패  : " + rtnMsg);
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
		
	/**
	 * 교환요청승인처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환요청승인처리", notes = "교환요청승인처리", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/exchange-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> exchangeConfirm(HttpServletRequest request) throws Exception  {
		
		String 		prg_id 				= "IF_PALTONAPI_04_008";
		ParamMap	apiInfoMap	 		= new ParamMap();
		ParamMap	apiDataMap	 		= new ParamMap();
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		String      flag				= "EXCHANGE";
		String 		rtnMsg				= "";

		try {
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			apiDataMap.put("flag"       , flag);
			
			log.info(prg_id + " - 02.교환 승인 리스트 조회");
			List<Map<String, Object>> exchangeConfirmList = paLtonClaimService.selectPaLtonReturnExchangeApprovalList(apiDataMap);
			
			for(Map<String, Object> exchangeMap : exchangeConfirmList) {
				apiDataMap.put("paCode"    	, exchangeMap.get("PA_CODE"));
	            apiDataMap.put("odNo"    	, exchangeMap.get("OD_NO"));
	            apiDataMap.put("clmNo"      , exchangeMap.get("CLM_NO"));
	            List<Map<String, Object>> exchagneDtList = paLtonClaimService.selectPaLtonReturnExchangeApprovalDtList(apiDataMap);
	            PaLtonComUtill.replaceCamelList(exchagneDtList);
	            apiDataMap.put("itemList"    , exchagneDtList);
	            
	            log.info(prg_id + " 롯데온 교환요청 승인 처리" + "주문번호 : " + exchangeMap.get("OD_NO") +"  / "  + exchangeMap.get("CLM_NO"));
	            resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
	            
	            if(resultMap != null && Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode").toString())) {
	            	rtnMsg += paLtonDeliveryController.updatePaOrderM(exchagneDtList, "20", "교환승인완료", resultMap.get("returnCode").toString());
	            } else if(resultMap.get("returnCode").toString().equals("8020") && resultMap.get("message").toString().contains("보류상태")) { //롯데ON : 고객이 교환 배송비를 결제 할때까지 보류 상태로 남아있음
	            	rtnMsg += paLtonDeliveryController.updatePaOrderM(exchagneDtList, "20", "교환비 결제대기(보류상태)", resultMap.get("returnCode").toString());
	            } else {
	            	//TPAAPITRACKING 메세지처리
					apiInfoMap.put("code", resultMap.get("returnCode").toString());
					apiInfoMap.put("message", apiInfoMap.get("message").toString() + " " + resultMap.get("message").toString());
					rtnMsg += paLtonDeliveryController.updatePaOrderM(exchagneDtList, null, resultMap.get("message").toString(), resultMap.get("returnCode").toString());
	            }
			}
			
			if(!"".equals(rtnMsg)) throw new Exception("교환 승인 리스트 UPDATE_DO_FLAG 실패  : " + rtnMsg);
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void cancelConfirm(Map<String, Object> cancel) throws Exception {
		
		if(cancel == null) return;
		
		String 		prg_id 			= "IF_PALTONAPI_04_015";
		ParamMap	apiInfoMap	 	= new ParamMap();
		ParamMap	apiDataMap	 	= new ParamMap();
		Map<String, Object> map		= new HashMap<String, Object>();
		paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String outClaimGb = cancel.get("OUT_CLAIM_GB").toString(); 
		String paCode 	  = cancel.get("PA_CODE").toString();
		String odNo 	  = cancel.get("OD_NO").toString();
		String clmNo 	  = cancel.get("CLM_NO").toString();
		log.info(prg_id + " - 03-1. 롯데온 취소요청 승인 처리" + "주문번호 : " + odNo);

		apiInfoMap.put("paCode"	, paCode);
		apiDataMap.put("odNo"	, odNo);
		apiDataMap.put("clmNo"	, clmNo);
		apiDataMap.put("paCode"	, paCode);
						
		List<Map<String, Object>> cancelDtList = paLtonClaimService.selectPaLtonCancelApprovalList(apiDataMap);
		PaLtonComUtill.replaceCamelList(cancelDtList);
		apiDataMap.put("itemList"	, cancelDtList);
		
		//= 롯데온 통신(취소승인)
		map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
		validationCheckForCancel(map, cancelDtList); //If connect Fail, Update TPALTONCANCELLIST.PROC_FLAG = 90 and throw exception

		//Insert TPAORDERM, Update TPALTONCANCELLIST.PROC_FLAG = 10
		for(Map<String, Object> cm : cancelDtList) {
			cm.put("procFlag"	, "00");
			cm.put("outClaimGb"	, outClaimGb);
			cm.put("paOrderGb"  , "20");
			paLtonClaimService.saveCancelConfirmTx(cm);
		}
	}
	
	private void cancelRefuse(Map<String, Object> cancel) throws Exception {

		if(cancel == null) return;
		
		String 		prg_id 			= "IF_PALTONAPI_04_016";
		ParamMap	apiInfoMap	 	= new ParamMap();
		ParamMap	apiDataMap	 	= new ParamMap();
		Map<String, Object> map		= new HashMap<String, Object>();
		paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
		
		String paCode 	= cancel.get("PA_CODE").toString();
		String odNo 	= cancel.get("OD_NO").toString();
		String clmNo 	= cancel.get("CLM_NO").toString();

		log.info(prg_id + " - 03-1. 롯데온 취소요청 거부 처리" + "주문번호 : " + odNo);
		apiInfoMap.put("paCode"	, paCode);
		apiDataMap.put("odNo"	, odNo);
		apiDataMap.put("clmNo"	, clmNo);
		apiDataMap.put("paCode" , paCode);
						
		List<Map<String, Object>> cancelDtList = paLtonClaimService.selectPaLtonCancelRefusalList(apiDataMap);
		PaLtonComUtill.replaceCamelList(cancelDtList);
		apiDataMap.put("itemList"	, cancelDtList);
		
		//= 롯데온 통신(취소거부)
		map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
		validationCheckForCancel(map, cancelDtList); //Update TPALTONCANCELLIST.PROC_FLAG = 90 and throw exception
		
		//Update TPALTONCANCELLIST
		for(Map<String, Object> c : cancelDtList) {
			c.put("procFlag" , "20");
			c.put("message"  , "취소거부완료");
			paLtonClaimService.updateProcFlag(c);  //Update TPALTONORDERLIST.PRO_FLAG = 20
		}
	}
	
	private void saveLtonCacncelList(Map<String, Object> map) throws Exception {
		if(!"20".equals(map.get("odTypCd"))) return; //취소만 처리한다. 20 주문취소 50 AS
		
		PaLtonCancelListVO vo 	= new PaLtonCancelListVO();
		String cancelGb			=  map.get("cancelGb").toString();
		
		//= 제휴 테이블에 데이터를 저장 (TPALTONCANCELLIST, TPAORDERM) 
		vo  = (PaLtonCancelListVO) PaLtonComUtill.map2VO( map , PaLtonCancelListVO.class);
		vo.setPaOrderGb("20");
		
		switch (cancelGb) {
		case "02": //취소
			vo.setProcFlag("00");
			paLtonClaimService.saveLtonCancelList(vo);			 // Insert TPALTONCANCELLIST
			break;
		case "21": //직권취소	
			vo.setProcFlag("10");
			paLtonClaimService.saveLtonCompleteCancelListTx(vo); //Insert TPALTONCANCELLIST, Insert TPARODERM
			break;
		case "22": //취소철회
			paLtonClaimService.saveLtonWithdrawCancelList(vo);   //Update TPALTONCANCELLIST
			break;

		default:
			break;
		}
		
	}	
	
	private void validationCheckForCancel(Map<String, Object> dataMap, List<Map<String, Object>> cancelDtList) throws Exception {
		if(cancelDtList.size() < 1) return;
		if(dataMap != null && Constants.PA_LTON_SUCCESS_CODE.equals(dataMap.get("returnCode").toString())) return;  //정상 케이스는 RETURN
		
		for(Map<String, Object> c : cancelDtList) { //통신실패
			c.put("procFlag" , "90");
			c.put("message"  , dataMap.get("message"));
			paLtonClaimService.updateProcFlag(c);  //Update TPALTONORDERLIST.PRO_FLAG = 90
		}
		
		throw new Exception(cancelDtList.get(0).get("odNo") +"- Connect(Return Value) Error " );
	}
	
	private void saveLtonClaimList(Map<String, Object> returnMap) throws Exception {
		PaLtonClaimListVO vo 	= new PaLtonClaimListVO();
		//= 제휴 테이블에 데이터를 저장 (TPALTONCLAIMLIST, TPAORDERM) 
		vo  = (PaLtonClaimListVO) PaLtonComUtill.map2VO( returnMap , PaLtonClaimListVO.class);
		
		//구매확정 후 취소 데이터처리
		if("30".equals(vo.getOdPrgsStepCd())) {
			ParamMap paramMap = new ParamMap();
			
			paramMap.put("PA_ORDER_NO", vo.getOdNo());
			paramMap.put("PA_ORDER_SEQ", vo.getOdSeq());
			int procSeq = ComUtil.objToInt(vo.getProcSeq());
			vo.setOrglProcSeq(ComUtil.objToStr(procSeq));
			procSeq++;
			vo.setProcSeq(ComUtil.objToStr(procSeq));
			vo.setRtngQty(paLtonClaimService.selectConfirmCancelQty(paramMap));
		}
		
		// 반품 회수지시 데이터처리
		if("40".equals(vo.getOdTypCd()) && "23".equals(vo.getOdPrgsStepCd())) {
			ParamMap paramMap = new ParamMap();

			paramMap.put("PA_ORDER_NO", vo.getOdNo());
			paramMap.put("PA_ORDER_SEQ", vo.getOdSeq());
			vo.setRtngQty(ComUtil.objToLong(returnMap.get("odQty")));
			vo.setItmSlPrc(ComUtil.objToDouble(returnMap.get("slPrc")));
			vo.setOdAccpDttm(ComUtil.objToStr(returnMap.get("odCmptDttm")));
			vo.setClmReqDttm(ComUtil.objToStr(returnMap.get("rtrpSiDttm")));
			vo.setClmAccpDttm(ComUtil.objToStr(returnMap.get("rtrpSiDttm")));
			vo.setSpicYn(returnMap.get("spicTypCd") == null ? "N" : "Y");
			vo.setRtrvCustNm(ComUtil.objToStr(returnMap.get("dvpCustNm")));
			vo.setRtrvTelNo(ComUtil.objToStr(returnMap.get("dvpTelNo")));
			vo.setRtrvMphnNo(ComUtil.objToStr(returnMap.get("dvpMphnNo")));
			vo.setRtrvZipNo(ComUtil.objToStr(returnMap.get("dvpZipNo")));
			vo.setRtrvStnmZipAddr(ComUtil.objToStr(returnMap.get("dvpStnmZipAddr")));
			vo.setRtrvStnmDtlAddr(ComUtil.objToStr(returnMap.get("dvpStnmDtlAddr")));
			vo.setSftNoUseYn(returnMap.get("sftNo") == null ? "N" : "Y");
			vo.setSftDvpMphnNo(ComUtil.objToStr(returnMap.get("sftNo")));
		}
		
		paLtonClaimService.savePaLtonClaimListTx(vo);
	}
	
	//미수령
	@ApiOperation(value = "미수령 처리", notes = "미수령 처리", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/notreceive-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> notReceiveList(HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate
			) throws Exception{		
		String apiCode 			 				= "IF_PALTONAPI_04_011";
		String paCode			 				= "";
		ParamMap apiInfoMap 	 				= new ParamMap();
		ParamMap apiDataMap 	 				= new ParamMap();
		Map<String, Object> resultMap 			= new HashMap<String, Object>() ;
		
		try {
			paLtonConnectUtil.getApiInfo(apiCode		, apiInfoMap);
			paLtonConnectUtil.checkDuplication(apiCode	, apiInfoMap);	//API 중복체크
			
			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT ; i++ ) {
				//= Setting Parameter
				paCode = (i==0 )? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				settingParams(fromDate, toDate, apiInfoMap, apiDataMap);

				//= Connect LOTTE ON
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				if(resultMap.get("data") == null) { //처리내역 없음
					apiInfoMap.put("code", "404");
					apiInfoMap.put("message", getMessage("pa.not_exists_process_list"));
					continue;
				} else if(!Constants.PA_LTON_SUCCESS_CODE.equals( resultMap.get("returnCode"))) {
					//TPAAPITRACKING 메세지처리
					apiInfoMap.put("code", resultMap.get("returnCode").toString());
					apiInfoMap.put("message", resultMap.get("message").toString());
					
					log.info("notreceive-list " +  "(" + resultMap.get("returnCode").toString() +")" + resultMap.get("message").toString());
					continue;
				}
  
				//= INSERT TPALTONNOTRECEIVELIST, TCUSTCOUNSELM, TCUSTCOUNSELDT
				saveLtonNotReceiveList(resultMap);
			}
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//미수령 철회
	@ApiOperation(value = "미수령 철회", notes = "미수령 철회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/withdraw-notreceive", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> withdrawNotReceive(HttpServletRequest request ) throws Exception {
		ParamMap apiInquiryoMap 		= new ParamMap();
		ParamMap apiWithDrawMap 		= new ParamMap();
		ParamMap apiDatMap				= new ParamMap();
		String 	 apiInquiryCode 		= "IF_PALTONAPI_04_011";  //미수령 조회
		String 	 apiWithDrawCode		= "IF_PALTONAPI_04_012";  //미수령 철회
		Map<String, Object> resultMap 	= new HashMap<String, Object>() ;
		boolean  isWithdrawYn			= false;
		
		
		try {
			//=1) API Parameter Setting
			paLtonConnectUtil.getApiInfo		(apiWithDrawCode	, apiWithDrawMap);		//롯데온 통신에 필요한 정보 세팅(URL, Method등등)
			paLtonConnectUtil.getApiInfo		(apiInquiryCode		, apiInquiryoMap);		//롯데온 통신에 필요한 정보 세팅(URL, Method등등)
			paLtonConnectUtil.checkDuplication	(apiWithDrawCode	, apiWithDrawMap);		//API 중복체크

			//=2) Select WithDraw Not ReceiveList 
			List<Map<String, Object>> notReceiveList = paLtonClaimService.selectWithDrawNotReceiveList();
			PaLtonComUtill.replaceCamelList(notReceiveList);
			
			//=3) Connect LOTTE ON~
			for(Map<String , Object> rMap : notReceiveList) {
				apiInquiryoMap.put("paCode", rMap.get("paCode").toString());
				apiWithDrawMap.put("paCode", rMap.get("paCode").toString());
				apiDatMap.setParamMap((HashMap<String, Object>)rMap);
				
				//= 미수령 조회
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInquiryoMap ,  apiDatMap);
				
    			//= 자체적으로 미수령 철회가  된 상황이라면 내부 값만 업데이트친다
				isWithdrawYn = isWithdrawNotReceiveList(resultMap , apiDatMap);
				if(isWithdrawYn) {
					apiDatMap.put("preWithdrawYn" , "Y");
					paLtonClaimService.updateNotReceiveList4WithDraw(apiDatMap); //UPDATE TPALTONNOTRECEIVELIST.PRE_WITHDRAW_YN = 'Y'
					continue;
				}
				
				//미수령 철회
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiWithDrawMap ,  apiDatMap);
				if(resultMap == null || !Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode")) ) {
					//TPAAPITRACKING 메세지처리
					apiWithDrawMap.put("code", resultMap.get("returnCode").toString());
					apiWithDrawMap.put("message", resultMap.get("message").toString());
					log.error( "미수령 철회 실패 ::::" + apiDatMap.getString("odNo") + " ," + apiDatMap.getString("odSeq") + "/"  +  resultMap.get("message").toString() );
					continue;
				}
				
				//= 최종 값을 저장한다.(TransDate)
				apiDatMap.put("transYn" , "Y");
				paLtonClaimService.updateNotReceiveList4WithDraw(apiDatMap); //UPDATE TPALTONNOTRECEIVELIST.TRANS_YN = 'Y'
			}
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiWithDrawMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiWithDrawMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiWithDrawMap.getString("code"), apiWithDrawMap.getString("message")), HttpStatus.OK);
	}	
	
	
	@SuppressWarnings("unchecked")
	private boolean isWithdrawNotReceiveList(Map<String, Object> resultMap, ParamMap apiDatMap) {
		//고객이 sk스토아에서 미수령 철회를 하지 않은 상태에서 프론트에서 미수령 철회버튼을 누른경우 returnCode를 3000으로 준다.
		if(!Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode"))) {
			return true;
		}
		
		//고객이 프론트에서 미수령 철회버튼을 
		String odNo   = apiDatMap.getString("odNo");
		String odSeq  = apiDatMap.getString("odSeq");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		resultList = (List<Map<String, Object>>)resultMap.get("data");
		
		for(Map<String,Object> result :  resultList) {
			
			if(!odNo.equals(result.get("odNo").toString())) continue;	
			if(!odSeq.equals(result.get("odSeq").toString())) continue;	
			
			if(result.get("nrcptDeclWhdrReqCnts") != null && !"".equals(result.get("nrcptDeclWhdrReqCnts") )) {  //미수령 철회 사유가 있음
				apiDatMap.put("nrcptDeclWhdrReqCnts"	, 	result.get("nrcptDeclWhdrReqCnts"));
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void saveLtonNotReceiveList(Map<String, Object> resultMap) {
		try {
			List<Map<String, Object>> ltonDataList 	= new ArrayList<Map<String,Object>>();
			PaLtonNotReceiveList notReceiveVO 		= new PaLtonNotReceiveList();

			ltonDataList = (List<Map<String, Object>>)resultMap.get("data");
						
			for(Map<String, Object> notReceive : ltonDataList) {
				notReceiveVO = (PaLtonNotReceiveList)PaLtonComUtill.map2VO(notReceive , PaLtonNotReceiveList.class);
				notReceiveVO.setPaCode(resultMap.get("paCode").toString());
			}
			paLtonClaimService.saveLtonNotReceiveListTx(notReceiveVO);
			
		}catch (Exception e) {
			log.error("Error saveLtonNotReceiveList::" + PaLtonComUtill.getErrorMessage(e));
		}
	}
	

	private String getClaimApiCode(String claimGb, String claimStep) {
		String prg_id = "";
		
		if("27".equals(claimStep)) {
			prg_id = "IF_PALTONAPI_04_023"; return prg_id;
		}
		
		switch (claimGb) {
		case "30"://반품
			prg_id = "IF_PALTONAPI_04_003";
			break;			
			
		case "31"://반품취소
			prg_id = "IF_PALTONAPI_04_006";
			break;			
			
		case "40"://교환
			prg_id = "IF_PALTONAPI_04_007";
			break;			

		case "41"://교환취소
			prg_id = "IF_PALTONAPI_04_010";
			break;			
		}
		return prg_id;
	}
	
	private void settingParams(String claimGb , String claimStep ,  String fromDate ,String toDate ,ParamMap apiInfoMap , ParamMap apiDataMap) {

		switch (claimGb) {
		case "20"://취소
		    apiDataMap.put("odTypCd"    	, "20" ); //주문유형코드(20 주문취소 50 AS) (NULL 인경우 전체조회)
		    apiDataMap.put("odPrgsStepCd" 	, claimStep );  //02 : 요청 , 21 : 취소완료 , 22: 취소요청철회 
		    apiDataMap.put("odlfPrgsStepCd" , "");
		    
		    if ("21".equals(claimStep) && ComUtil.NVL(fromDate).length() != 14) { // 취소완료 조회 시 default 기간 : D-2 ~ D
		    	fromDate = DateUtil.addDay( DateUtil.getCurrentDateTimeAsString() , -2, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		    }
		    break;
		
		case "30": //반품
			apiDataMap.put("odTypCd"		, "40"); 
			apiDataMap.put("odlfPrgsStepCd"	, null);
			apiDataMap.put("odPrgsStepCd"	, claimStep);  // odPrgsStepCd : 02 요청 , 03 : 접수  		
			break;
		case "31": //반품취소
			apiDataMap.put("odTypCd"		, "41"); 
			apiDataMap.put("odIfPrgsStepCd"	, null);
			break;
		case "40": //교환
			apiDataMap.put("odTypCd"		, "30"); 
			apiDataMap.put("odIfPrgsStepCd"	, null);
			break;
		case "41": //교환취소
			apiDataMap.put("odTypCd"		, "31"); 
			apiDataMap.put("odIfPrgsStepCd"	, null);
			break;		
		default:
			break;
		}
		settingParams(fromDate ,toDate ,apiInfoMap , apiDataMap);  //srchStrtDttm, srchEndDttm, lrtrNo 등등
	}
	
	private void settingParams(String fromDate ,String toDate ,ParamMap apiInfoMap , ParamMap apiDataMap) {
		String endDate   	= ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
		String startDate 	= ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateTimeAsString() , -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		String paCode		= apiInfoMap.getString("paCode");
		String lrtrNo		= (Constants.PA_LTON_BROAD_CODE.equals(paCode)) ? apiInfoMap.getString("paBroad"): apiInfoMap.getString("paOnline"); 
		
		apiDataMap.put("srchStrtDttm"	, startDate);
		apiDataMap.put("srchEndDttm"	, endDate  );
		apiDataMap.put("lrtrNo"     	, lrtrNo);
		apiDataMap.put("odNo"			, "");
		apiDataMap.put("odSeq"			, "");
		apiDataMap.put("procSeq"		, "");
		apiDataMap.put("clmNo"			, "");
	}
	
	/**
	 * 구매확정 후 취소목록 조회
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "구매확정 후 취소목록 조회", notes = "구매확정 후 취소목록 조회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/purchase-confirm-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> purCfrmCnclSearch(HttpServletRequest request,
			@RequestParam(value = "fromDate" , required = false) String fromDate,
			@RequestParam(value = "toDate"	 , required = false) String toDate
			) throws Exception {
		List<Map<String, Object>> cfrmCancelList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		ParamMap apiDataMap  = new ParamMap();
		String   prg_id 	 = "IF_PALTONAPI_04_014";
		String   paCode		 = "";
		String   odNo 		 = "";
		String   errMsg		 = "";
		
		try {
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				settingParams(fromDate ,toDate ,apiInfoMap , apiDataMap);
				
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				if(!Constants.PA_LTON_SUCCESS_CODE.equals( map.get("returnCode"))) {
					apiInfoMap.put("code", map.get("returnCode"));
					apiInfoMap.put("message", map.get("message").toString());
					continue;
				}
				
				cfrmCancelList = (List<Map<String, Object>>)map.get("data");
				
				if(cfrmCancelList.size() < 1) continue;
				
				for(Map<String, Object> returnMap : cfrmCancelList) {
					odNo = returnMap.get("odNo").toString();
					try {
						returnMap.put("paCode", paCode);
						returnMap.put("paOrderGb", "30");
						returnMap.put("odPrgsStepCd", "30"); // 주문진행단계 30 : 구매확정 후 취소
						returnMap.put("clmRsnCd", "316");
						returnMap.put("clmRsnCnts", "구매확정 후 취소");
						
						saveLtonClaimList(returnMap);
					} catch(Exception e) {
						log.error("ERROR - SaveLtonClaimList(Confirm-Cancel) :::::" + PaLtonComUtill.getErrorMessage(e));
						errMsg += "주문번호 : " + odNo + " - " + PaLtonComUtill.getErrorMessage(e, 500) + "\n";  
						continue;
					}
				}
			}
			if(!"".equals(errMsg)) throw new Exception("ERROR - Confirm-Cancel ::::: " + errMsg);
		} catch(Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 회수지시 목록 조회
	 * @param request
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "회수지시 목록 조회", notes = "회수지시 목록 조회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> returnList(
			HttpServletRequest request,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate
			) throws Exception {
		
		Map<String, Object> map 			= new HashMap<String, Object>() ;
		List<Map<String, Object>> claimList = new ArrayList<Map<String,Object>>();
		String paCode						= "";
		String irtrNo						= "";
		String prg_id 						= "IF_PALTONAPI_04_022";
		ParamMap				apiInfoMap	= new ParamMap();
		ParamMap				apiDataMap	= new ParamMap();
		String endDate   = ComUtil.NVL(toDate).length()   == 14 ? toDate   : DateUtil.getCurrentDateTimeAsString();
		String startDate = ComUtil.NVL(fromDate).length() == 14 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateTimeAsString() , -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		String errMsg	 = "";

		try {
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paLtonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info(prg_id + " - 02.파라미터 검증, [fromDate={}, toDate={}]", startDate, endDate);
			apiDataMap.put("srchStrtDt"		, startDate);
			apiDataMap.put("srchEndDt"		, endDate  );	
			apiDataMap.put("odNo"			, "");
			apiDataMap.put("odPrgsStepCd"	, "23");
			apiDataMap.put("odTypCd"		, "40");
			apiDataMap.put("ifCplYN"		, "");
			
			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT ; i++ ) {
				//=Connect Lton and Get Data	
				paCode = (i==0 )? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
				irtrNo = (i==0 )? apiInfoMap.getString("paBroad"): apiInfoMap.getString("paOnline");
				apiInfoMap.put("paCode", paCode);
				apiDataMap.put("lrtrNo", irtrNo);
				
				log.info(prg_id + " - 03.회수지시내역조회 API 호출");
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				//=Validation Check
				if(!Constants.PA_LTON_SUCCESS_CODE.equals( map.get("returnCode"))) {
					//TPAAPITRACKING 메세지처리
					apiInfoMap.put("code", map.get("returnCode"));
					apiInfoMap.put("message", map.get("message").toString());
					continue;
				};	
								
				//Insert TPALTONCLAIMLIST, TPAORDERM
				claimList = (List<Map<String, Object>>)((Map<String,Object>)map.get("data")).get("deliveryOrderList");
				for(Map<String, Object> m : claimList) {
					try {
						log.info("04.회수지시내역조회 API 호출 결과=" + m.toString());
						if(!"40".equals(m.get("odTypCd"))) continue; // 반품데이터만 처리
						
						m.put("paCode", paCode);
						m.put("paOrderGb"	, "30");
						m.put("claimGb"		, "30");
						saveLtonClaimList(m);
					}catch (Exception e) {
						log.error("ERROR - SaveLtonClaimList :::::" + PaLtonComUtill.getErrorMessage(e));
						errMsg +=  "주문번호 : " + m.get("odNo") + " - " + PaLtonComUtill.getErrorMessage(e, 500) + "\n";
						continue;
					}
				}
			}
						
			if(!"".equals(errMsg)) 	throw new Exception("ERROR - SaveLtonOrderList ::::: " + errMsg); // by 건당 실패에 대해서 ApiTracking에 노출하기 위해 
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderClaimMain(request, "30"); // BO데이터 생성
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiIgnore
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private void mobileOrderCancel(HttpServletRequest request) throws Exception {
		
		ParamMap				apiInfoMap	= new ParamMap();
		ParamMap				apiDataMap	= new ParamMap();
		String					prg_id		= "IF_PALTONAPI_04_024"; //품절로 인한 판매자 직접취소
		String paGroupCode   				= "08";
		String dateTime                     = "";
		String paCode						= "";
		String paOrderNo					= "";
		String paOrderSeq					= "";
		HashMap<String, String> map 		= new HashMap<String, String>();
		PaLtonGoodsdtMappingVO dtMapping    = new PaLtonGoodsdtMappingVO();
		Map<String, Object> connectResult	= new HashMap<String, Object>() ;
		
		try {
			log.info(prg_id + "모바일 자동취소 (품절취소반품) START");
			paLtonConnectUtil.getApiInfo	  (prg_id	, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			dateTime = systemService.getSysdatetimeToString();
			
			//=1) 판매자 주문 취소할 LIST를 생성해준다
			List<HashMap<String, String>> cancelList = paLtonClaimService.selectPaMobileOrderAutoCancelList();
			
			if(cancelList.size() > 0) {
				for (int i = 0; i < cancelList.size(); i++) {
					try {
						HashMap<String, String> cancelMap = (HashMap<String, String>)cancelList.get(i);
						HashMap<String, Object> refusalMap = paLtonDeliveryService.selectRefusalInfo(cancelMap.get("MAPPING_SEQ"));
						paCode = cancelMap.get("PA_CODE");
						paOrderNo = cancelMap.get("PA_ORDER_NO");
						paOrderSeq = cancelMap.get("PA_ORDER_SEQ");
						
						List<Map<String, Object>> itemList  = new ArrayList<Map<String,Object>>();
						itemList.add(refusalMap);
						
						if(itemList.size() == 0) return;
						
						//=2) 롯데온 판매자 취소 API를 호출할 인자를 SETTING 한다.
						PaLtonComUtill.replaceCamelList(itemList);
						
						paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
						
						apiDataMap.put("odNo", paOrderNo);
						apiDataMap.put("itemList", itemList);
						
						map.put("PA_GROUP_CODE", paGroupCode);
						map.put("PA_CODE", paCode);
						map.put("PA_ORDER_NO", paOrderNo);
						map.put("PA_ORDER_SEQ", paOrderSeq);
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
					 	map.put("PROC_ID", Constants.PA_COPN_PROC_ID);
						
						dtMapping.setGoodsCode(cancelMap.get("GOODS_CODE"));
						dtMapping.setGoodsdtCode(cancelMap.get("EITM_NO").substring(10,13));
						dtMapping.setGoodsdtSeq(cancelMap.get("EITM_NO").substring(13,16));
						dtMapping.setPaCode(paCode);
						dtMapping.setTransOrderAbleQty("0");
						dtMapping.setTransStockYn("1");
						dtMapping.setModifyId("PALTON");
						dtMapping.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						//=3) 판매자취소 API 호출과 통신후 결과를 TPAORDERM에 적용한다.
						connectResult = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
						if(Constants.PA_LTON_SUCCESS_CODE.equals(connectResult.get("returnCode").toString())) {
							log.info("롯데온 모바일자동취소 성공");
							
							map.put("REMARK3_N", "10");
							map.put("RSLT_MESSAGE", "모바일자동취소 성공");
							paorderService.updateRemark3NTx(map);
							
							//상담생성 & 문자발송 & 상품품절처리
							paCommonService.savePaMobileOrderCancelTx(map);
							paLtonGoodsService.updateGoodsdtPaOptionCode(dtMapping);
						} else {
							log.info("롯데온 모바일자동취소 실패");
							map.put("REMARK3_N", "90");
							map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + connectResult.get("returnCode").toString());
							paorderService.updateRemark3NTx(map);
						}
					}catch(Exception e) {
						log.info("롯데온 모바일자동취소 실패(에러발생)");
						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + PaLtonComUtill.getErrorMessage(e));
						paorderService.updateRemark3NTx(map);
					}
				}
				// 취소 승인 로직
				cancelList("21","","",request);
				// 반품 접수 로직 
				claimList(request,"30","","","");
				// 취소 요청 로직 - 나중에 해야지 업제지시가 바로 처리됨
				cancelList("02","","",request);
			} 
		} catch(Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
	}
}
