package com.cware.api.pahalf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.pahalf.claim.service.PaHalfClaimService;
import com.cware.netshopping.pahalf.order.service.PaHalfOrderService;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;
import com.cware.netshopping.pahalf.util.PaHalfConnectUtil;
import com.cware.netshopping.passg.util.PaSsgComUtill;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value="/pahalf/order", description="하프클럽 주문")
@Controller("com.cware.api.pahalf.PaHalfOrderController")
@RequestMapping(value="/pahalf/order")
public class PaHalfOrderController extends AbstractController{
	
	
	@Resource(name = "pahalf.order.paHalfOrderService")
	private PaHalfOrderService paHalfOrderService;
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pahalf.claim.paHalfClaimService")
	private PaHalfClaimService paHalfClaimService;
	
	@Resource(name = "com.cware.api.pahalf.PaHalfAsyncController")
	private PaHalfAsyncController asyncController;
	
	@Autowired
	private PaHalfConnectUtil paHalfConnectUtil;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService paCommonService;
	
	@Resource(name = "pacommon.order.paorderService")
	private PaOrderService paOrderService;
	
	@Resource(name = "com.cware.api.pahalf.PaHalfClaimController")
	private PaHalfClaimController paHalfClaimController;
		
	/**
	 * 하프클럽 주문내역 조회
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 주문내역 조회", notes = "하프클럽 주문내역 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderList(
		    @RequestParam(value = "fromDate", required = false	) String fromDate
		  , @RequestParam(value = "toDate"	, required = false	) String toDate
		  , HttpServletRequest request ) throws Exception {
		
		log.info("===== 하프클럽 주문 내역 조회 API Start =====");
		String endDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		String startDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateAsString() , -1, DateUtil.GENERAL_DATE_FORMAT);
		String prg_id 			= "IF_PAHALFAPI_03_001";
		ParamMap apiInfoMap		= new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		String	paCode			= "";
		String  errorMsg		= "";
		Map<String, Object> resultMap			= null; 
		List<Map<String, Object>> dataList  	= null;
		
		try {
			
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);			
			
			// =Step 3) Parameter Setting
			apiDataMap.put("fromYMD"	, PaHalfComUtill.makeHalfDateFormat(startDate));
			apiDataMap.put("toYMD"		, PaHalfComUtill.makeHalfDateFormat(endDate));
			apiDataMap.put("siteCd"		, "1");
			apiDataMap.put("deliState"	, "c");
			apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
			
			//=Step 4) 하프클럽 통신 및 데이터 저장(TPAHALFORDERLIST, TPAORDERM)
			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				//ㄴ1.통신
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				
				//ㄴ2.실패 Tracking 처리
				if (!"200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) {
					errorMsg = errorMsg +"(" + paCode + ")" + PaHalfComUtill.getApiResult(resultMap).get("message");
					apiInfoMap.put("code"	, PaHalfComUtill.getApiResult(resultMap).get("code"));
					apiInfoMap.put("message", errorMsg);
					continue;//실패건은 스킵
				}
				//ㄴ3.INSERT TPAHALFORDERLIST, TPAORDERM
				dataList= (List<Map<String, Object>>)PaHalfComUtill.getApiData(resultMap, "result");
				savePaHalfOrder(dataList, paCode);
			}
						
		}catch (Exception e) {
			log.info("{} : {}","하프클럽 주문 내역 조회 API 오류",PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderConfirm(request); 	//주문 승인
		orderInputMain(request);//주문 생성
		
		log.info("===== 하프클럽 주문 내역 조회 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 하프클럽 주문승인
	 * @param request
	 * @throws Exception
	 */
	@ApiOperation(value = "하프클럽 주문승인", notes = "하프클럽 주문승인", httpMethod = "GET", produces = "application/json")
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
		String 				prg_id 			= "IF_PAHALFAPI_03_002";
		String 				paCode			= "";
		try {
			paHalfConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paHalfConnectUtil.checkDuplication(prg_id	, apiInfoMap);		

			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				//1) 주문 승인 리스트 조회
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				List<Map<String, Object>> orderConfirmList =  paHalfOrderService.selectOrderConfirmList(paCode);
				PaHalfComUtill.replaceCamelList(orderConfirmList);
				
				List<Map<String, Object>> orderReadylist = new ArrayList<>();
				
				for( Map<String, Object> orderConfirm : orderConfirmList) {
					Map<String, Object> orderReady = new HashMap<String, Object>(); 
					orderReady.put("ordNo", orderConfirm.get("ordNo"));
					orderReady.put("ordNoNm", orderConfirm.get("ordNoNm"));
					orderReadylist.add(orderReady);
				}
				
				
				//2) 주문승인(하프클럽통신)
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, orderReadylist);
				
				//3) 실패 Tracking 처리
				if (!"200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) {
					updatePaOrderm(orderConfirmList, "10", PaHalfComUtill.getApiResult(resultMap).get("message"));
					continue;//실패건은 스킵
				}
				
				//4) UPDATE TPAORDERM.DO_FLAG
				updatePaOrderm(orderConfirmList, "30", "승인완료");
			}
			
		}catch (Exception e) {
			log.info("{} : {}" , "하프클럽 주문 승인 오류",PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 하프클럽 취소 내역 조회  (하프클럽 취소는 SK스토아의 승인/거부 없이 완료만 인입됨)
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 취소 내역 조회  (하프클럽 취소는 SK스토아의 승인/거부 없이 완료만 인입됨)", notes = "하프클럽 취소 내역 조회  (하프클럽 취소는 SK스토아의 승인/거부 없이 완료만 인입됨)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/cancel-list", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<ResponseMsg> cancelList(
			@RequestParam(value = "fromDate"		, required = false) String fromDate
		  , @RequestParam(value = "toDate"			, required = false) String toDate
		  , HttpServletRequest request ) throws Exception {
	
		log.info("===== 하프클럽 취소 내역 조회 API Start =====");
		String endDate   		= ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString();
		String startDate 		= ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay( DateUtil.getCurrentDateAsString() , -1, DateUtil.GENERAL_DATE_FORMAT);
		String prg_id 			= "IF_PAHALFAPI_04_001";
		ParamMap apiInfoMap		=   new ParamMap();
		ParamMap apiDataMap		= new ParamMap();
		String	paCode			= "";
		String  errorMsg		= "";
		Map<String, Object> resultMap		= null; 
		List<Map<String, Object>> dataList  = new ArrayList<Map<String,Object>>();
		
		try {
			
			// =Step 1)API Setting
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			// =Step 2)중복체크
			paHalfConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			// =Step 3) Parameter Setting
			apiDataMap.put("fromYMD"	, PaHalfComUtill.makeHalfDateFormat(startDate));
			apiDataMap.put("toYMD"		, PaHalfComUtill.makeHalfDateFormat(endDate));
			apiDataMap.put("siteCd"		, "1");
			
			
			//=Step 4) 하프클럽 통신 및 데이터 저장(TPAHALFORDERLIST, TPAORDERM)
			for(int i = 0; i < Constants.PA_HALF_CONTRACT_CNT ; i++ ) {
				paCode = (i==0 )? Constants.PA_HALF_BROAD_CODE : Constants.PA_HALF_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				dataList.clear();
				//ㄴ1.품절 취소 조회
				apiDataMap.put("deliState"	, "f");  //F : 품절 취소
				apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				//ㄴ1-2.실패 Tracking 처리
				if (!"200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) {
					errorMsg = errorMsg +"(" + paCode + ")" + PaHalfComUtill.getApiResult(resultMap).get("message");
					apiInfoMap.put("code"	, PaHalfComUtill.getApiResult(resultMap).get("code"));
					apiInfoMap.put("message", errorMsg);
					continue;//실패건은 스킵
				}
				List<Map<String, Object>> soldOutList= (List<Map<String, Object>>)PaHalfComUtill.getApiData(resultMap, "result");
				
				//ㄴ2.주문 취소 조회
				apiDataMap.put("deliState"	, "r");  //r : 주문취소
				apiInfoMap.put("queryString", PaHalfComUtill.makeQuryString(apiDataMap));
				resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataMap);
				
				//ㄴ2-1.실패 Tracking 처리
				if (!"200".equals(PaHalfComUtill.getApiResult(resultMap).get("code"))) {
					errorMsg = errorMsg +"(" + paCode + ")" + PaHalfComUtill.getApiResult(resultMap).get("message");
					apiInfoMap.put("code"	, PaHalfComUtill.getApiResult(resultMap).get("code"));
					apiInfoMap.put("message", errorMsg);
					continue;//실패건은 스킵
				}
				List<Map<String, Object>> canceltList= (List<Map<String, Object>>)PaHalfComUtill.getApiData(resultMap, "result");
				
				dataList.addAll(soldOutList);
				dataList.addAll(canceltList);
				
				//ㄴ3.INSERT TPAHALFORDERLIST, TPAORDERM
				savePaHalfCancel(dataList, paCode);
			}
			
		}catch (Exception e) {
			log.info("{} : {}","하프클럽 취소 내역 조회 API 오류",PaHalfComUtill.getErrorMessage(e));
			paHalfConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
		}
		
		cancelInputMain(request);		//=BO 주문 데이터 생성
		log.info("===== 하프클럽 취소 내역 조회 API End =====");
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 하프클럽 판매자 주문 취소(품절)
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "하프클럽 판매자 주문 취소(품절)", notes = "하프클럽 판매자 주문 취소(품절)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/refuse-order", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> refusalOrder(HttpServletRequest request
		  , @RequestParam(value = "mappingSeq"		, required = true) String mappingSeq
		  , @RequestParam(value = "memo"			, required = false) String memo
		  , @RequestParam(value = "status"			, required = false) String status ) throws Exception {		
			 
		String prg_id 					     = "IF_PAHALFAPI_03_006";
		ParamMap apiInfoMap				     = new ParamMap();
		Map<String, Object> resultMap	     = null; 
		Map<String, String> apiResultMap     = null;
		List<Map<String,Object>> apiDataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> cancelOrderMap   =  new HashMap<String, Object>();

		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			cancelOrderMap = paHalfOrderService.selectRefusalInfo(mappingSeq);
			
			cancelOrderMap = (Map<String, Object>) PaHalfComUtill.replaceCamel(cancelOrderMap);
			
			Map<String, Object> cancelOrder   =  new HashMap<String, Object>();
			cancelOrder.put("ordNo", cancelOrderMap.get("ordNo"));
			cancelOrder.put("ordNoNm", cancelOrderMap.get("ordNoNm"));
			cancelOrder.put("dlvTmpltSeq", cancelOrderMap.get("dlvTmpltSeq"));
			cancelOrder.put("stockNo", cancelOrderMap.get("stockNo"));
			cancelOrder.put("memo",   memo);
			cancelOrder.put("status", status);
			
			apiDataList.add(cancelOrder);
			apiInfoMap.put("paCode",cancelOrderMap.get("paCode"));
			//통신
			resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList);
			apiResultMap = PaHalfComUtill.getApiResult(resultMap);
			
			if(!"200".equals(apiResultMap.get("code"))) throw processException("pa.connect_error", new String[] { apiResultMap.get("message" ) } ); 
	
		} catch (Exception e) {
			log.info("{} : {} 제휴주문번호: {}, PA_CODE: {}" , "하프클럽 판매자 주문 취소 오류", PaHalfComUtill.getErrorMessage(e),cancelOrderMap.get("ordNo"),cancelOrderMap.get("paCode"));
			paHalfConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 품절취소반품
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "품절취소반품", notes = "품절취소반품", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/orderSoldOut", method = RequestMethod.GET)  
	@ResponseBody
	public ResponseEntity<?> orderSoldOut(HttpServletRequest request ) throws Exception {		
			 
		String prg_id 					     = "IF_PAHALFAPI_03_007";
		ParamMap apiInfoMap				     = new ParamMap();
		ParamMap paramMap					 = new ParamMap();
		Map<String, Object> resultMap	     = null; 
		Map<String, String> apiResultMap     = null;
		List<Map<String,Object>> apiDataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> cancelOrderMap   =  new HashMap<String, Object>();
		String 		rtnMsg				= "";


		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("broadCode", Constants.PA_HALF_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_HALF_ONLINE_CODE);
			paramMap.put("paGroupCode", Constants.PA_HALF_GROUP_CODE);
			
			List<Object> cancelList = paCommonService.selectPaSoldOutordList(paramMap);
			if (cancelList.size() > 0) {
				for (Object cancelItem : cancelList) {
					HashMap<String, String> map = new HashMap<String, String>();
					try {
						HashMap<String, String> cancelMap = (HashMap<String, String>) cancelItem;
						map.put("PA_GROUP_CODE", Constants.PA_HALF_GROUP_CODE);
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("SITE_GB", cancelMap.get("SITE_GB"));
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORG_ORD_CAN_YN", cancelMap.get("ORDER_CANCEL_YN"));
						
						cancelOrderMap = paHalfOrderService.selectRefusalInfo(cancelMap.get("MAPPING_SEQ"));
						cancelOrderMap = (Map<String, Object>) PaHalfComUtill.replaceCamel(cancelOrderMap);

						Map<String, Object> cancelOrder   =  new HashMap<String, Object>();
						cancelOrder.put("ordNo", cancelOrderMap.get("ordNo"));
						cancelOrder.put("ordNoNm", cancelOrderMap.get("ordNoNm"));
						cancelOrder.put("dlvTmpltSeq", cancelOrderMap.get("dlvTmpltSeq"));
						cancelOrder.put("stockNo", cancelOrderMap.get("stockNo"));
						cancelOrder.put("memo",   "재고없음으로 인한 판매자 취소");
						cancelOrder.put("status", "58"); //58:품절취소, 59:자동품절(출고지연 D+7)
						
						apiDataList.add(cancelOrder);
						apiInfoMap.put("paCode", cancelOrderMap.get("paCode"));
						// 통신
						resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList);
						apiResultMap = PaHalfComUtill.getApiResult(resultMap);

						if("200".equals(apiResultMap.get("code"))) {
							map.put("ORDER_CANCEL_YN", "10");
							map.put("RSLT_MESSAGE", "판매취소 성공");
						} else {
							map.put("ORDER_CANCEL_YN", "90");
							map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
						}
						
					} catch (Exception e) {
						map.put("ORDER_CANCEL_YN", "90");
						map.put("RSLT_MESSAGE", "판매취소 실패(api 연동 실패)");
						rtnMsg += "pa_order_no:"+map.get("PA_ORDER_NO") + " "+"판매취소 실패(api 연동 실패) "+PaSsgComUtill.getErrorMessage(e)+"/";
						apiInfoMap.put("code", "500");

					}finally {
						paOrderService.updateOrderCancelYnTx(map);
						//상담생성 & 문자발송
						paCommonService.saveOrderCancelCounselTx(map);
					}
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch (Exception e) {
			log.info("{} : {} 제휴주문번호: {}, PA_CODE: {}" , "하프클럽 판매자 주문 취소 오류", PaHalfComUtill.getErrorMessage(e),cancelOrderMap.get("ordNo"),cancelOrderMap.get("paCode"));
			paHalfConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}	
	
	/**
	 * 모바일 자동취소 (품절취소반품)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiIgnore
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "모바일 자동취소 (품절취소반품)", notes = "모바일 자동취소 (품절취소반품)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/mobile-order-cancel", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileOrderCancel(HttpServletRequest request ) throws Exception {				
			 
		String prg_id 					     = "IF_PAHALFAPI_03_008";
		ParamMap apiInfoMap				     = new ParamMap();
		ParamMap paramMap					 = new ParamMap();
		Map<String, Object> resultMap	     = null; 
		Map<String, String> apiResultMap     = null;
		HashMap<String, String> map			 = null;
		List<Map<String,Object>> apiDataList = new ArrayList<Map<String,Object>>();
		Map<String, Object> cancelOrderMap   =  new HashMap<String, Object>();
		String 		rtnMsg				= "";


		try {
			paHalfConnectUtil.getApiInfo(prg_id, apiInfoMap); // B710, 713등을 통해 URL, METHOD 등등 세팅
			
			paramMap.put("apiCode", prg_id);
			paramMap.put("startDate", systemService.getSysdatetimeToString());
			paramMap.put("broadCode", Constants.PA_HALF_BROAD_CODE);
			paramMap.put("onlineCode", Constants.PA_HALF_ONLINE_CODE);
			paramMap.put("paGroupCode", Constants.PA_HALF_GROUP_CODE);
			
			List<HashMap<String, String>> cancelList = paHalfOrderService.selectPaMobileOrderAutoCancelList();
			if (cancelList.size() > 0) {
				for (HashMap<String, String> cancelMap : cancelList) {
					map = new HashMap<String, String>();
					try {
						map.put("PA_GROUP_CODE", Constants.PA_HALF_GROUP_CODE);
						map.put("PA_CODE", cancelMap.get("PA_CODE"));
						map.put("PA_ORDER_NO", cancelMap.get("PA_ORDER_NO"));
						map.put("PA_ORDER_SEQ", cancelMap.get("PA_ORDER_SEQ"));
						map.put("ORDER_NO", cancelMap.get("ORDER_NO"));
						map.put("ORDER_G_SEQ", cancelMap.get("ORDER_G_SEQ"));
						map.put("PROC_ID", Constants.PA_HALF_PROC_ID);
						
						cancelOrderMap = paHalfOrderService.selectRefusalInfo(cancelMap.get("MAPPING_SEQ"));
						cancelOrderMap.put("memo", "재고없음으로 인한 판매자 취소");
						cancelOrderMap.put("status", "58"); //58:품절취소, 59:자동품절(출고지연 D+7)
						cancelOrderMap = (Map<String, Object>) PaHalfComUtill.replaceCamel(cancelOrderMap);
						cancelOrderMap.remove("paCode"); // 불필요한 파라미터 제거

						apiDataList.add(cancelOrderMap);
						apiInfoMap.put("paCode", cancelMap.get("PA_CODE"));
						// 통신
						resultMap = paHalfConnectUtil.connectHalfApi(apiInfoMap, apiDataList);
						apiResultMap = PaHalfComUtill.getApiResult(resultMap);

						if("200".equals(apiResultMap.get("code"))) {
							map.put("REMARK3_N", "10");
							map.put("RSLT_MESSAGE", "모바일자동취소 성공");
							paOrderService.updateRemark3NTx(map);
							
							//상담생성 & 문자발송 & 상품품절처리
							paCommonService.savePaMobileOrderCancelTx(map);
						} else {
							map.put("REMARK3_N", "90");
							map.put("RSLT_MESSAGE", "모바일자동취소 실패 ");
							paOrderService.updateRemark3NTx(map);
						}
						
					} catch (Exception e) {
						String errorMsg = PaSsgComUtill.getErrorMessage(e);
						map.put("REMARK3_N", "90");
						map.put("RSLT_MESSAGE", "모바일자동취소 실패 " + errorMsg);
						paOrderService.updateRemark3NTx(map);
						rtnMsg += "pa_order_no:" + map.get("PA_ORDER_NO") + " "+"모바일자동취소 실패 "+errorMsg+"/";
						apiInfoMap.put("code", "500");
					}
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch (Exception e) {
			log.info("{} : {} 제휴주문번호: {}, PA_CODE: {}" , "모바일 자동취소 (품절취소반품) 하프클럽 판매자 주문 취소 오류", PaHalfComUtill.getErrorMessage(e),cancelOrderMap.get("ordNo"),cancelOrderMap.get("paCode"));
			paHalfConnectUtil.checkException(apiInfoMap, e);
			
		} finally {
			paHalfConnectUtil.closeApi(request, apiInfoMap);
			
            //취소건 BO반영을 위한 취소완료 조회 호출 및 출고전반품 데이터 생성
			cancelList("","",request);
			paHalfClaimController.orderClaimMain(request,"30");
			
			
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void updatePaOrderm(List<Map<String, Object>> orderReadylist, String paDoFlag, String message) {
		for(Map<String, Object> order : orderReadylist) {
			try {
				String rtnCode = Constants.SAVE_FAIL;
				
				if("30".equals(paDoFlag)) {
					rtnCode = Constants.SAVE_SUCCESS;
				}
				
				order.put("paDoFlag"			, paDoFlag);
				order.put("resultMessage"	, message);
				order.put("resultCode"		, rtnCode);
				paHalfOrderService.updateTPaorderm(order);
			}catch (Exception e) {
				log.info("{} : {}", "하프클럽 제휴 주문 테이블 업데이트 오류",  PaHalfComUtill.getErrorMessage(e) );
				continue;
			}
		}
	}

	public void savePaHalfOrder(List<Map<String, Object>> orderList, String paCode) {
		
		for(Map<String, Object> order : orderList) {
			try {
				order.put("paCode", paCode);
				paHalfOrderService.savePaHalfOrderTx(order);
			}catch (Exception e) {
				log.info("{} : {} 제휴주문번호: {}, PA_CODE: {}" , "하프클럽 주문 저장 오류", PaHalfComUtill.getErrorMessage(e),order.get("ordNo"),order.get("paCode"));
				continue;
			}
		}
	}
	
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PAHALF_ORDER_INPUT";
		String duplicateCheck 	= "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			//주문 생성 대상 조회
			List<Map<String, String>> orderInputTargetList = paHalfOrderService.selectOrderInputTargetList(limitCount);
			
			for( Map<String, String> order : orderInputTargetList) {
				try {
					asyncController.orderInputAsync(order, request);
				}catch (Exception e) {
					log.info("{} : {} 제휴주문번호: {} ","하프클럽 주문생성 오류",order.get("PA_ORDER_NO"), PaHalfComUtill.getErrorMessage(e));
					continue;
				}
			}
			
		}catch (Exception e) {
			log.info("{} : {}","하프클럽 주문생성 오류", PaHalfComUtill.getErrorMessage(e));
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	private void savePaHalfCancel(List<Map<String, Object>> cancelList ,String paCode) {
		for(Map<String, Object> cancel : cancelList) {
			try {
				cancel.put("paCode", paCode);
				paHalfOrderService.savePaHalfCancelTx(cancel);
							
			}catch (Exception e) {
				log.info("{} : {} 제휴주문번호: {}" , "하프클럽 주문취소 내역 저장 오류", PaHalfComUtill.getErrorMessage(e),cancel.get("ordNo"));
				continue;
			}
		}
	}
	
	private void cancelInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PAHALF_CANCEL_INPUT";
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"		, prg_id);
		paramMap.put("paOrderGb" 	, "20");
		paramMap.put("siteGb"		, Constants.PA_HALF_PROC_ID);
		
		try {
			paHalfConnectUtil.checkDuplication	(prg_id		, paramMap);	
			
			List<HashMap<String,Object>> cancelInputTargetList = paHalfOrderService.selectClaimTargetList(paramMap);
			
			for (HashMap<String,Object> cancelTargetList : cancelInputTargetList ) {
				try {
					//외부 API 를 호출하기에 건별로 transaction을 갖도록 controller를 호출 하도록 개발됨. 개별 ROLLBACK
					asyncController.cancelInputAsync(cancelTargetList, request);
				}
				catch ( Exception e ) {
					log.info("{} : {} 주문번호: {}" , "하프클럽 주문취소 내역 생성 오류", PaHalfComUtill.getErrorMessage(e), cancelTargetList.get("ORDER_NO"));
					continue;
				}
			}
		}
		catch ( Exception e ) {
			log.info("{} : {}" , "하프클럽 주문취소 내역 생성 오류", PaHalfComUtill.getErrorMessage(e));		
			paHalfConnectUtil.checkException(paramMap, e);
		}
		finally {
			paHalfConnectUtil.closeApi(request, paramMap);	
		}		
	}
	
	
}
