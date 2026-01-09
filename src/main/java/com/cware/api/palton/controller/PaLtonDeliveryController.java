package com.cware.api.palton.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaLtonOrderListVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.palton.delivery.service.PaLtonDeliveryService;
import com.cware.netshopping.palton.util.PaLtonComUtill;
import com.cware.netshopping.palton.util.PaLtonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/palton/delivery", description="롯데온 주문")
@Controller
@RequestMapping(value = "/palton/delivery")
public class PaLtonDeliveryController extends AbstractController  {
	
	@Autowired
	private PaLtonDeliveryService paLtonDeliveryService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private PaLtonAsyncController paLtonAsyncController;
	@Autowired
	private PaLtonConnectUtil paLtonConnectUtil;
	@Autowired
	private PaOrderService paOrderService;
		
	private transient static Logger log = LoggerFactory.getLogger(PaLtonDeliveryController.class);
		
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문조회", notes = "주문조회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderList(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletRequest request
			) throws Exception {
		
		Map<String, Object> map 			= new HashMap<String, Object>() ;
		List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
		String paCode						= "";
		String irtrNo						= "";
		String prg_id 						= "IF_PALTONAPI_03_001";
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
			apiDataMap.put("odPrgsStepCd"	, "11");
			apiDataMap.put("odTypCd"		, "10");
			apiDataMap.put("ifCplYN"		, "");
			
			for(int i = 0; i < Constants.PA_LTON_CONTRACT_CNT ; i++ ) {
				//=Connect Lton and Get Data	
				paCode = (i==0 )? Constants.PA_LTON_BROAD_CODE : Constants.PA_LTON_ONLINE_CODE;
				irtrNo = (i==0 )? apiInfoMap.getString("paBroad"): apiInfoMap.getString("paOnline");
				apiInfoMap.put("paCode", paCode);
				apiDataMap.put("lrtrNo", irtrNo);
				
				log.info(prg_id + " - 03.주문내역조회 API 호출");
				map = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
				
				//=Validation Check
				if(!Constants.PA_LTON_SUCCESS_CODE.equals( map.get("returnCode"))) {
					//TPAAPITRACKING 메세지처리
					apiInfoMap.put("code", map.get("returnCode"));
					apiInfoMap.put("message", map.get("message").toString());
					continue;
				};	
								
				//Insert TPALTONORDERLIST, TPAORDERM
				orderList = (List<Map<String, Object>>)((Map<String,Object>)map.get("data")).get("deliveryOrderList");
				for(Map<String, Object> m : orderList) {
					try {
						log.info("04.주문내역조회 API 호출 결과=" + m.toString());
						m.put("paCode", paCode);
						saveLtonOrderList(m);
					}catch (Exception e) {
						log.error("ERROR - SaveLtonOrderList :::::" + PaLtonComUtill.getErrorMessage(e));
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
		
		log.info(prg_id + " - 07. 주문내역조회2(발주확인) 호출");
		orderConfirm(request);
		orderInputMain(request);
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문 승인 리스트 조회", notes = "주문 승인 리스트 조회", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-confirm-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderConfirm(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();
		ParamMap			apiDataMap		= new ParamMap();		
		String 				prg_id 			= "IF_PALTONAPI_03_002";
		String				rtnMsg			= "";

		try {
			paLtonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id	, apiInfoMap);		
			
			log.info(prg_id + " - 02.주문 승인 리스트 조회");
			List<Map<String, Object>> orderReadylist =  paLtonDeliveryService.selectDeliveryReadyList();
			if(orderReadylist.size() == 0) return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			PaLtonComUtill.replaceCamelList(orderReadylist);
			apiDataMap.put("ifCompleteList", orderReadylist);
			
			log.info(prg_id + " - 03.주문내역조회 API 호출  :: " + orderReadylist.size() + " 건" );
			resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
			
			log.info(prg_id + " - 04.주문내역조회 API 호출결과 처리 ");
			if(Constants.PA_LTON_SUCCESS_CODE.equals(((Map<String, String>)(resultMap.get("data"))).get("rsltCd"))) {
				rtnMsg += updatePaOrderM(orderReadylist, "30", "승인완료", ((Map<String, String>)(resultMap.get("data"))).get("rsltCd"));
			}else {
				//TPAAPITRACKING 메세지처리
				apiInfoMap.put("code", ((Map<String, String>)(resultMap.get("data"))).get("rsltCd"));
				apiInfoMap.put("message", ((Map<String, String>)(resultMap.get("data"))).get("rsltMsg"));
				rtnMsg += updatePaOrderM(orderReadylist, null , ((Map<String, String>)(resultMap.get("data"))).get("rsltMsg"), ((Map<String, String>)(resultMap.get("data"))).get("rsltCd"));
			}
			
			if(!"".equals(rtnMsg)) throw new Exception("주문 승인 리스트 UPDATE_DO_FLAG 실패  : " + rtnMsg);
			
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}	
	
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PALTON_ORDER_INPUT";
		String duplicateCheck 	= "";
		String promoAllowTerm   = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			log.info(prg_id + " - 02.주문내역 생성 서비스 호출");
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			List<Map<String, String>> orderInputTargetList = paLtonDeliveryService.selectOrderInputTargetList(limitCount);
			
			for( Map<String, String> order : orderInputTargetList) {
				try {
					order.put("PAPROMO_ALLOW_TERM"	,	promoAllowTerm);
					paLtonAsyncController.orderInputAsync(order, request);
				}catch (Exception e) {
					log.error(prg_id + " - 주문 내역 생성 오류" + order.get("PA_ORDER_NO") , e );
					continue;
				}
			}
			
		}catch (Exception e) {
			log.error("error msg : " + e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
			log.info(prg_id + " - 03.프로그램 중복 실행 검사 [End]");
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송통보(출고처리)", notes = "배송통보", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception{
				
		ParamMap			apiInfoMap		= new ParamMap();
		String 				api_code 		= "IF_PALTONAPI_03_003";
		String 				errMsg			= "";
		
		try {
			paLtonConnectUtil.getApiInfo		(api_code, apiInfoMap);
			paLtonConnectUtil.checkDuplication	(api_code, apiInfoMap);		
			
			//출고건
			List<Map<String,Object>> orderShippingList = paLtonDeliveryService.selectSlipOutProcList();

			for(  Map<String,Object> oc   :   orderShippingList ) {
				try {
					Map<String, Object> resultMap = deliveryProgressToLton(oc, apiInfoMap);
					
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						updatePaOrderM(oc, "40", "발송완료", resultCode);
						
						//INSERT TPASLIPINFO 운송장 이력 저장
						oc.put("PA_GROUP_CODE"	, "08");
						oc.put("REMARK1_V"		, "발송완료");
						oc.put("TRANS_YN"		, "1");
						paOrderService.insertTpaSlipInfoTx(oc);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", "PA_ORDER_NO : " + oc.get("OD_NO") + " | " + resultMsg + "\n");
						
						updatePaOrderM(oc, "30" , resultMsg, resultCode);
					}		
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "주문번호 : " + oc.get("OD_NO") + " 내용: " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
					continue;
				}
			}
			
			//배송완료 건
			List<Map<String,Object>> orderCompleteList = paLtonDeliveryService.selectDeliveryCompleteList();

			for(  Map<String,Object> oc   :   orderCompleteList ) {
				try {
					Map<String, Object> resultMap = deliveryProgressToLton(oc, apiInfoMap);
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(Constants.PA_LTON_SUCCESS_CODE.equals(resultCode)) {
						updatePaOrderM(oc, "80", "배송완료", resultCode);
					}else {
						//TPAAPITRACKING 메세지처리
						apiInfoMap.put("code", resultCode);
						apiInfoMap.put("message", "PA_ORDER_NO : " + oc.get("OD_NO") + " | " + resultMsg + "\n");
						
						updatePaOrderM(oc, "40" , resultMsg, resultCode);
					}	
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					errMsg += "주문번호 : " + oc.get("OD_NO") + " 내용: " + PaLtonComUtill.getErrorMessage(e , 500)   + "\n";
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
	
	public Map<String, Object> deliveryProgressToLton(Map<String,Object> slipmMap , ParamMap apiInfo) throws Exception {
		
		List<Map<String, Object>> slipOutList = new ArrayList<Map<String,Object>>();
		slipOutList.add(slipmMap);
		
		ParamMap apiDataMap 			= new ParamMap();
		Map<String ,Object > resultMap 	= new HashMap<String, Object>();
		PaLtonComUtill.replaceCamelList(slipOutList);
		
		if(String.valueOf(apiInfo.get("flag")).equals("RETRIEVAL")) { //회수예외 통보
			apiDataMap.put("retrievalExceptionList", slipOutList);
		} else {
			apiDataMap.put("deliveryProgressStateList", slipOutList);
		}
		
		resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfo, apiDataMap);
		
		return resultMap;
	}	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> deliveryUpdateToLton(Map<String,Object> slipmMap , ParamMap apiInfo) throws Exception {
		Map<String ,Object > resultMap 			= new HashMap<String, Object>();
		Map<String ,Object > dataMap 			= new HashMap<String, Object>();
		ArrayList<Map<String, Object>> list 	= new ArrayList<Map<String,Object>>();
		
		try {
			//롯데온 규격에 맞게 데이터 세팅
			dataMap = (Map<String, Object>) PaLtonComUtill.replaceCamel(slipmMap);
			list.add(dataMap);
			resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfo, list);
			
		}catch(Exception e) {
			log.error(PaLtonComUtill.getErrorMessage(e));
		}

		return resultMap;

	}

	//****************************************************************************************************************************//
	//************************************************** 내부적으로 사용하는 함수 영역 ****************************************************//	
	//***************************************************************************************************************************//

	private void saveLtonOrderList(Map<String, Object> map) throws Exception {
		if(!"10".equals(map.get("odTypCd"))) return; //주문만 처리한다.

		String rtnCode			= "";
		PaLtonOrderListVO vo 	= new PaLtonOrderListVO();
		
		//= 제휴 테이블에 데이터를 저장 (TPALTONORDERLIST, TPAORDERM) 
		vo  = (PaLtonOrderListVO) PaLtonComUtill.map2VO( map , PaLtonOrderListVO.class);
		rtnCode = paLtonDeliveryService.saveLtonOrderListTx(vo);
		if(!Constants.SAVE_SUCCESS.equals(rtnCode)) return;
	}
	
	public void updatePaOrderM(Map<String,Object> paorderm , String doFlag, String message, String resultCode) throws Exception {
			
		if(doFlag == null) doFlag = paorderm.get("paDoFlag").toString();
		
		paorderm.put("paDoFlag"			, doFlag);
		paorderm.put("apiResultMesage"	, message);
		paorderm.put("apiResultCode"	, resultCode);
		paorderm.put("mappingSeq"		, paorderm.get("MAPPING_SEQ"));
		
		paLtonDeliveryService.updatePaOrderMDoFlag(paorderm);	

	}
	
	public String updatePaOrderM(List<Map<String,Object>> paordermList , String doFlag, String message, String resultCode) throws Exception {

		String rtnMsg = "";
		
		for(Map<String, Object> m : paordermList) {
				
			if(doFlag == null) doFlag = m.get("paDoFlag").toString();
			if(message.contains("보류상태")) m.put("paHoldYn", "1");
			
			m.put("paDoFlag"		, doFlag);
			m.put("apiResultMesage"	, message);
			m.put("apiResultCode"	, resultCode);
			m.put("mappingSeq"		, m.get("mappingSeq"));
			try {
				paLtonDeliveryService.updatePaOrderMDoFlag(m);	
			}catch (Exception e) {
				rtnMsg +=  "UpdatePaOrderM :: MAPPING_SEQ : " +  m.get("mappingSeq").toString() + ", DO_FLAG : " + doFlag  + "\n";
				continue;
			}
		}
		
		return  rtnMsg;
	}
	
	/**
	 * 롯데ON배송상태조회
	 * @param request
	 * @return
	 * @throws Exception
	 * 
	 * 보류상태 해제, 교환배송비 UPDATE
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "롯데ON배송상태조회", notes = "보류상태 해제, 교환배송비 UPDATE", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/deli-proc-state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> deliProgressState(HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> deliveryProgressStateList  = null;
		Map<String, Object> 	  resultMap  = new HashMap<String, Object>() ;
		ParamMap				  apiInfoMap = new ParamMap();
		ParamMap				  apiDataMap = new ParamMap();		
		ParamMap 				  paramMap   = null;
		String 					  prg_id 	 = "IF_PALTONAPI_03_007";
		
		String endDate   	= DateUtil.getCurrentDateTimeAsString();
		String startDate 	= DateUtil.addDay( DateUtil.getCurrentDateTimeAsString() , -1, DateUtil.DEFAULT_JAVA_DATE_FORMAT);
		
		try {
			paLtonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paLtonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			//교환보류건 조회
			List<Map<String, Object>> exchangeHoldList = paLtonDeliveryService.selectExchangeHoldList();
			for(Map<String, Object> holdList : exchangeHoldList) {
				apiDataMap.put("srchStrtDt", startDate);
				apiDataMap.put("srchEndDt",  endDate);
				apiDataMap.put("odNo",  	 holdList.get("OD_NO").toString());
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
			
				if(!Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode").toString())) {
					apiInfoMap.put("code", resultMap.get("returnCode").toString());
					apiInfoMap.put("message", resultMap.get("message").toString());
					continue;
				} else {// API 조회 성공했으면 교환배송비 UPDATE, TPAORDERM.PA_HOLD_YN = 0 UPDATE
					paramMap = new ParamMap();
					paramMap.setParamMap((HashMap<String, Object>) holdList);
					deliveryProgressStateList = new ArrayList<Map<String,Object>>();
					deliveryProgressStateList = (List<Map<String, Object>>)((Map<String, Object>)resultMap.get("data")).get("deliveryProgressStateList"); // API 결과
					for(Map<String, Object> stateData : deliveryProgressStateList) {
						
						if(paramMap.get("PA_CLAIM_NO").equals(String.valueOf(stateData.get("clmNo")))) {
							paramMap.put("XCHG_DV_CST", stateData.get("dvCst").toString()); // XCHG_DV_CST : TPALTONCLAIMLIST.교환배송비, resultMap.get("dvCst") : 배송상태조회시 받아오는 교환배송비
							paramMap.put("PA_HOLD_YN", "0");
							paramMap.put("API_RESULT_CODE", resultMap.get("returnCode").toString());
							paramMap.put("API_RESULT_MESSAGE", resultMap.get("message").toString());
							paramMap.put("flag", "EXCHANGE");
							paramMap.put("PA_CODE", holdList.get("PA_CODE").toString());
							paramMap.put("PA_ORDER_SEQ", stateData.get("odSeq").toString());
							paramMap.put("PA_ORDER_NO", stateData.get("odNo").toString());
							paLtonDeliveryService.saveHoldInfoTx(paramMap);
						}
					}
				}
			}
			
			//밑으로는 필요한 것들 있으면 계속 추가될 예정입니다.
			//배송중 업데이트 필요건
			/*
			 for(){
			 	apiDataMap.put("srchStrtDt", startDate);
				apiDataMap.put("srchEndDt",  endDate);
				apiDataMap.put("odNo",  	 odNo);
				resultMap = paLtonConnectUtil.apiGetObjectByLtn(apiInfoMap, apiDataMap);
			
				if(!Constants.PA_LTON_SUCCESS_CODE.equals(resultMap.get("returnCode").toString())) {
					apiInfoMap.put("code", resultMap.get("returnCode").toString());
					apiInfoMap.put("message", resultMap.get("message").toString());
					return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				}
				
				deliveryProgressStateList = (List<Map<String, Object>>)((Map<String, Object>)resultMap.get("data")).get("deliveryProgressStateList");
				//없으면 패스
				 //있으면 업데이트
				for(Map<String, Object> stateData : deliveryProgressStateList) {
					//TODO 여기서 PA_HOLD_YN UPDATE 를 수행할까
				}
			 }
			 */
			
		} catch(Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	/**
	 * 회수예외 통보
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "회수예외 통보", notes = "회수예외 통보", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/return-hold-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> returnHoldProc(HttpServletRequest request) throws Exception{
				
		ParamMap  apiInfoMap = new ParamMap();
		ParamMap  paramMap   = null;
		String 	  api_code 	 = "IF_PALTONAPI_03_006";
		StringBuffer sb		 = new StringBuffer();

		try {
			paLtonConnectUtil.getApiInfo		(api_code, apiInfoMap);
			paLtonConnectUtil.checkDuplication	(api_code, apiInfoMap);		
			
			apiInfoMap.put("flag", "RETRIEVAL");
			
			List<Map<String,Object>> retrievalExceptList = paLtonDeliveryService.selectRetrievalExceptList();

			for(Map<String, Object> retrievalData : retrievalExceptList) {
				try {
					Map<String, Object> resultMap = deliveryProgressToLton(retrievalData, apiInfoMap);
					
					String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
					String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
					
					if(resultCode.equals(Constants.PA_LTON_SUCCESS_CODE)) {
						paramMap = new ParamMap();
						paramMap.put("PA_HOLD_YN", "1");
						paramMap.put("API_RESULT_CODE",    resultCode);
						paramMap.put("API_RESULT_MESSAGE", resultMsg);
						paramMap.put("flag", "RETRIEVE");
						paramMap.put("PA_CODE", retrievalData.get("PA_CODE").toString());
						paramMap.put("PA_ORDER_NO", retrievalData.get("OD_NO").toString());
						paramMap.put("PA_ORDER_SEQ", retrievalData.get("OD_SEQ").toString());
						paramMap.put("PA_CLAIM_NO", retrievalData.get("CLM_NO").toString());
						paLtonDeliveryService.saveHoldInfoTx(paramMap);
					} else {
						sb.append("OD_NO : " + String.valueOf(retrievalData.get("OD_NO")) + " | ");
						apiInfoMap.put("code", "500");
						apiInfoMap.put("message", sb.toString());
						continue;
					}
				} catch(Exception e) {
					log.error(PaLtonComUtill.getErrorMessage(e));
					continue;
				}
			}
		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		slipUpdateProc(request);//운송장 수정 (추후에 스프링 배치로 분리하던 현상태 유지하던 결정해야함)
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
		
	/**
	 * 운송장 변경 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "운송장 변경 ", notes = "운송장 변경 ", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/slip-update-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> slipUpdateProc(HttpServletRequest request) throws Exception{
		ParamMap  apiInfoMap = new ParamMap();
		String 	  api_code 	 = "IF_PALTONAPI_03_009";
		
		try {
			paLtonConnectUtil.getApiInfo		(api_code, apiInfoMap);
			paLtonConnectUtil.checkDuplication	(api_code, apiInfoMap);		
					
			List<Map<String,Object>> slipUpdateList = paLtonDeliveryService.selectSlipUpdateProcList(); 
			
			for(Map<String,Object> slipUpdate :  slipUpdateList) {
				//1) 운송장 수정 API 호출
				Map<String, Object> resultMap = deliveryUpdateToLton(slipUpdate, apiInfoMap);
				
				//2) 호출 결과 세팅
				String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
				String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
				
				if(resultCode.equals(Constants.PA_LTON_SUCCESS_CODE)) {
					slipUpdate.put("REMARK1_V"	, "운송장 수정 완료");
					slipUpdate.put("TRANS_YN"	, "1");
				}else {

					slipUpdate.put("REMARK1_V"	, resultMsg);
					slipUpdate.put("TRANS_YN"	, "0");
				}
				
				slipUpdate.put("PA_GROUP_CODE"	, "08");
				slipUpdate.put("INVC_NO"		, slipUpdate.get("MOD_INVC_NO")); 
				slipUpdate.put("DV_CO_CD"		, slipUpdate.get("MOD_DV_CO_CD")); 			
				
				//3) 운송장 수정 이력 테이블 저장 (INSERT TPASLIPINFO)
				paOrderService.insertTpaSlipInfoTx(slipUpdate);
			}

		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 발송약정일 통보
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "발송약정일 통보 ", notes = "발송약정일 통보 ", httpMethod = "GET")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delivery-delay-proc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> deliveryDelayProc(HttpServletRequest request) throws Exception{
		ParamMap  apiInfoMap = new ParamMap();
		StringBuffer sb		 = new StringBuffer();
		String 	  api_code 	 = "IF_PALTONAPI_03_004";
		int executedRtn    	 = 0;
		
		try {
			paLtonConnectUtil.getApiInfo		(api_code, apiInfoMap);
			paLtonConnectUtil.checkDuplication	(api_code, apiInfoMap);		
			
			// 설치/주문제작 상품 중 발송예정일까지 출고안된 주문 조회
			List<Map<String,Object>> deliveryDelayList = paLtonDeliveryService.selectDeliveryDelayProcList();
			
			for(Map<String,Object> deliveryDelay :  deliveryDelayList) {
				//1) API 호출
				Map<String, Object> resultMap = deliveryUpdateToLton(deliveryDelay, apiInfoMap);
				
				//2) 호출 결과 세팅
				String resultCode = ((Map<String ,String>)resultMap.get("data")).get("rsltCd");
				String resultMsg  = ((Map<String ,String>)resultMap.get("data")).get("rsltMsg");
				
				if(resultCode.equals(Constants.PA_LTON_SUCCESS_CODE)) {
					deliveryDelay.put("REMARK1_V"	, "발송약정일(" + deliveryDelay.get("SND_AGRD_DTTM").toString() + ") 통보 완료");
					
					// 발송약정일(TPAORDERM.REAMARK1_V) UPDATE
					executedRtn = paLtonDeliveryService.updatePaLtonSndAgrdDttm(deliveryDelay);
					if(executedRtn < 1) {
						apiInfoMap.put("code", "500");
						sb.append(deliveryDelay.get("OD_NO").toString() + " : TPAORDERM UPDATE Fail/");
						continue;
					}
				}else {
					apiInfoMap.put("code", "500");
					sb.append("OD_NO : " + deliveryDelay.get("OD_NO").toString() + " " + resultMsg + " | ");
					continue;
				}
			}
			apiInfoMap.put("message", sb.toString());

		}catch (Exception e) {
			paLtonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paLtonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
}
