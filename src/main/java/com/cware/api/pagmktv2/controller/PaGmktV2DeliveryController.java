package com.cware.api.pagmktv2.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaGmkNotRecive;
import com.cware.netshopping.domain.model.PaGmkOrder;
import com.cware.netshopping.pagmkt.delivery.service.PaGmktDeliveryService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktDateUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktDeliveryRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/pagmktv2/delivery", description="주문/배송/수취확인")
@Controller("com.cware.api.pagmktv2.PaGmktV2DeliveryController")
@RequestMapping(value="/pagmktv2/delivery")

public class PaGmktV2DeliveryController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Resource(name = "pagmkt.delivery.PaGmktDeliveryService")
	private PaGmktDeliveryService paGmktDeliveryService;

	@Resource(name = "com.cware.api.pagmktv2.PaGmktV2OrderController")
	private PaGmktV2OrderController paGmktOrderController;   

	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	
	private PaGmktCommonUtil CommonUtil;
	/***
	 *  5.0 Testing 함수
	 * @return ResponseEntity
	 * @throws Exception 
	 * 
	 **/
	@ApiOperation(value = "5.0 Testing 함수", notes = "5.0 Testing 함수", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-RequestOrders1", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> testing(HttpServletRequest request) throws Exception{
		String apiCode = "IF_PAGMKTAPI_V2_02_002";            
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PAG");	
		paramMap.put("ORDER_STATUS",0);
		paramMap.put("REQUEST_TYPE",1);
		paramMap.put("ORDER_NO","2906302088");
		paramMap.put("PAGE_INDEX",0);
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime()	,  -5 ));
		paramMap.put("TO_DATE",DateUtil.addDay(systemService.getSysdatetime()	,  +5 ));
		String response =  restUtil.getConnection(rest,  paramMap); 
		System.out.println(response);		
		return null;
	}
	
	/***
	 *  5.1 무통장 주문 조회
	 * @return ResponseEntity
	 * @throws Exception 
	 * 
	 **/
    @ApiIgnore
	@ApiOperation(value = "5.1 무통장 주문 조회", notes = "5.1 무통장 주문 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-PreRequestOrders/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingPreReqeustOrders(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		//= Setting Api Infomation
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_001";
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		paramMap.put("FROM_DATE"    , DateUtil.addDay(systemService.getSysdatetime()	,  -5 ));
		paramMap.put("TO_DATE"      , DateUtil.addDay(systemService.getSysdatetime()	,  +5 ));	
		CommonUtil.setParams(paramMap);
		
		//= Insert PAGMKTORDERLIST, PAORDERM
		makePreOrderList(paramMap);
		
		//= INSERT ORDERDT
		paGmktOrderController.preOrderInputMain(request, paGroupCode);
	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/***
	 *  5.2 주문 조회
	 * @return ResponseEntity
	 * @throws Exception 
	 * 
	 **/
	@ApiOperation(value = "5.2 주문 조회", notes = "5.2 주문 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = {"/post-shipping-RequestOrders/{paGroupCode}"}, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingReqeustOrders(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		ParamMap paramMap = new ParamMap();
		String   apiCode = "IF_PAGMKTAPI_V2_02_002";
		
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		//= Setting Api Infomation
		paramMap.put("ORDER_STATUS"	, 1);
		paramMap.put("REQUEST_TYPE"	, 5);
		paramMap.put("ORDER_NO"		, 0);
		paramMap.put("PAGE_INDEX"	, 0);
		paramMap.put("FROM_DATE"	, DateUtil.addDay(systemService.getSysdatetime()	,  -5 ));
		paramMap.put("TO_DATE"  	, DateUtil.addDay(systemService.getSysdatetime()	,  +1 ));

		//= Insert PAGMKTORDERLIST, PAORDERM
		makeOrderList(paramMap);
		
		//= INSERT ORDERDT
		paGmktOrderController.orderInputMain (request, paGroupCode);	 //일반주문, 무통장 미입금 주문	
		paGmktOrderController.orderUpdateMain(request, paGroupCode); 	 //무통장 입금 업데이트 처리
			
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.3 주문 확인(Inner) 
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "5.3 주문 확인(Inner)", notes = "5.3 주문 확인(Inner)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	//= confirmOrderProc랑 똑같은 함수지만 confirmOrderProc는 밖에서 호출 할수 있고, innerConfirmOrderProc는 주문조회에 내장되있는 함수
	public void innerConfirmOrderProc(PaGmkOrder order) {

		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		String apiCode 			= "IF_PAGMKTAPI_V2_02_003";
		String duplicateCheck 	= "";
		String response 		= null;
		ParamMap paramMap 		= new ParamMap();
		int executeCnt 			= 0;
		String message 			= null;
		String contrNo 			= order.getContrNo();
		String paCode  			= order.getPaCode();
		String payNo   			= order.getPayNo();
		String paGroupCode 		= order.getPaGroupCode();
		
		try{	
			paramMap.put("paGroupCode"	, paGroupCode);
			paramMap.put("apiCode"		, apiCode);
			paramMap.put("urlParameter"	, contrNo);
			paramMap.put("paCode"		, paCode);
			CommonUtil.setParams(paramMap);
				
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			//=  실질적으로 api를 호출하여 각 Order를  주문 확인(배송 준비중) 상태로 만들어 주는 부분 
			response = restUtil.getConnection(null,  paramMap);
			
			//문구 같은게 없으면 -1 
			//주문확인 통신 중 실패시 내부주문생성 로직 타지않도록 수정 (22.01.17)	
			if ( response.indexOf("Connect") == -1 ) {
				//= Response 결과 값 중 IsChanged 이 1인 경우 배송지 업데이트를 해줘야한다. 
				executeCnt = checkAddressChanged(response, paramMap, rest);
			}
			
			if(executeCnt == 1 ){
				message = "장바구니번호 : " + payNo + ", " + "주문번호 : " +  contrNo + " 주문 확인 성공";
			}else{
				message = "장바구니번호 : " + payNo + ", " + "주문번호 : " +  contrNo + " + 주문 확인 에러(주소 처리) " + "@FAIL";
			}
			CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap, " 장바구니 번호 :"+ payNo + " 주문번호 :" + contrNo + "/");
			log.error(paramMap.getString("message"), se);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}		
	}
	
	/**
	 * 5.3 주문 확인 (외부 호출용)
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "5.3 주문 확인 (외부 호출용)", notes = "5.3 주문 확인 (외부 호출용)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-OrderCheck/{paCode}/{paGroupCode}/{payNo}/{contrNo}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> confirmOrderProc(
			@ApiParam(name = "paCode", value = "제휴사코드", defaultValue = "") 			@PathVariable("paCode") 	 String paCode,
			@ApiParam(name = "paGroupCode", value = "제휴사그룹코드", defaultValue = "") 	@PathVariable("paGroupCode") String paGroupCode,
			@ApiParam(name = "payNo", value = "장바구니코드(결제번호)", defaultValue = "") 	@PathVariable("payNo") 		 String payNo,
			@ApiParam(name = "contrNo", value = "주문번호", defaultValue = "") 			@PathVariable("contrNo") 	 String contrNo				
			){

		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		String duplicateCheck = "";
		String apiCode = "IF_PAGMKTAPI_V2_02_003";
		String response = null;
		ParamMap paramMap = new ParamMap();
		String message = null;
		int executeCnt;
		try{
			log.info("===== 주문 확인 API START =====");
			paramMap.put("apiCode"			, apiCode);
			paramMap.put("paGroupCode"		, paGroupCode);
			paramMap.put("urlParameter"		, contrNo);
			paramMap.put("paCode"			, paCode);
			CommonUtil.setParams(paramMap);
			apiCode = paramMap.getString("apiCode");
				
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
	
			//=  실질적으로 api를 호출하여 각 Order를  주문 확인(배송 준비중) 상태로 만들어 주는 부분
			response = restUtil.getConnection(null,  paramMap);
			
			//= Response 결과 값 중 IsChanged 이 1인 경우 배송지 업데이트를 해줘야한다. 
			executeCnt = checkAddressChanged(response, paramMap ,rest);
			
			if(executeCnt == 1 ){
				message = "장바구니번호 : " + payNo + ", " + "주문번호 : " +  contrNo + " 주문 확인 성공";
			}else{
				message = "장바구니번호 : " + payNo + ", " + "주문번호 : " +  contrNo + " + 주문 확인 에러 " + "@FAIL";
			}
			
			CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap, " 장바구니 번호 :"+ payNo + " 주문번호 :" + contrNo + "/");
			log.error(paramMap.getString("message"), se);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			try{
				if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}catch(Exception e){
				log.info("===== 주문 확인처리 API END =====");
			}
		}		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.4 발송예정일 등록 및 갱신
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.4 발송예정일 등록 및 갱신", notes = "5.4 발송예정일 등록 및 갱신", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-ShippingExpectedDate/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody	
	public ResponseEntity<?> postShippingShippingExpectedDate(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_004";
		
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		//= Insert PAGMKTORDERLIST, PAORDERM
		changeDelyDate(paramMap);
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.5 배송 송장등록 
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.5 배송 송장등록", notes = "5.5 배송 송장등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-ShippingInfo/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingShippingInfo(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_005";
		
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		//= Insert PAGMKTORDERLIST, PAORDERM
		slipOutProc(paramMap);
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	

	/**
	 * 5.6 배송완료 처리 
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.6 배송완료 처리", notes = "5.6 배송완료 처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-AddShippingCompleteInfo/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingAddShippingCompleteInfo(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_006";
		
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		//= Insert PAGMKTORDERLIST, PAORDERM
		setDeliveryComplete(paramMap);
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.7 수취확인 목록 조회 
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.7 수취확인 목록 조회", notes = "5.7 수취확인 목록 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-RequestOrders-Complete/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postShippingRequestOrderComplete(HttpServletRequest request,  @PathVariable("paGroupCode") String paGroupCode) throws Exception{

		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_002C";
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("ORDER_STATUS"		, 5);
		paramMap.put("REQUEST_TYPE"		, 3);
		paramMap.put("ORDER_NO"			, 0);
		paramMap.put("PAGE_INDEX"		, 0);
		paramMap.put("FROM_DATE"		, DateUtil.addDay(systemService.getSysdatetime(),  -5  ));
		paramMap.put("TO_DATE"			, DateUtil.addDay(systemService.getSysdatetime(),  +10 ));
		paramMap.put("paGroupCode"		, paGroupCode);
		CommonUtil.setParams(paramMap);
		
		//= Insert PAGMKTORDERLIST, PAORDERM
		setCompleteList(paramMap);
				
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.9 미수령신고조회 (기간 조회)
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.9 미수령신고조회 (기간 조회)", notes = "5.9 미수령신고조회 (기간 조회)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-ClaimList/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postshippingClaimList(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_009";            
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);		
		paramMap.put("TYPE"				, 1);
		paramMap.put("CONTR_NO"			, 0);
		paramMap.put("FROM_DATE"		, DateUtil.addDay(systemService.getSysdatetime(),  -5 ));
		paramMap.put("TO_DATE"			, DateUtil.addDay(systemService.getSysdatetime(),  +1 ));		
		CommonUtil.setParams(paramMap);

		setNotreceiveList(paramMap);
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 5.10 미수령신고 철회요청 
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "5.10 미수령신고 철회요청", notes = "5.10 미수령신고 철회요청", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-shipping-ClaimRelease/{paGroupCode}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postshippingClaimRelease(HttpServletRequest request, @PathVariable("paGroupCode") String paGroupCode) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PAGMKTAPI_V2_02_010";            
		paramMap.put("apiCode"			, apiCode);
		paramMap.put("paGroupCode"		, paGroupCode);		
		CommonUtil.setParams(paramMap);
		
		setNotreceiveCancelList(paramMap);
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);

	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void makePreOrderList(ParamMap paramMap) throws Exception{
		String paCode 	  = null;
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		String broadCode  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
		int	   count 	  = Constants.PA_GMKT_CONTRACT_CNT;
		String message	  = "";
		String duplicateCheck = "";
		List<PaGmkOrder> paGmktOrderList = new ArrayList<PaGmkOrder>();
		String apiCode       	= paramMap.getString("apiCode"); 

		try{			
			log.info("===== 무통장 주문 조회 API Start =====");
			//= Step 0) 중복실행 검사
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) 무통장 리스트 조회
			for(int i = 0; i < count; i++){
				paCode = (i==0) ?broadCode:onlineCode;
				paramMap.put("paCode", paCode);
				getOrderListForPreOrder(rest, paramMap, paGmktOrderList);
			}
			
			//= Step 2) 1)의 조회결과로 조회된 주문번호로 상세 정보 조회 및 리스트 저장
			message += retrievePreOrderList(paGmktOrderList);
			CommonUtil.failCheck(message, paramMap);
		
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return;
		}finally {		
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 무통장 주문 조회 API END =====");
		}			
	}
	

	public void makeOrderList(ParamMap paramMap) throws Exception{
		List<PaGmkOrder> paGmktOrderList = new ArrayList<PaGmkOrder>();
		String broadCode  		= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode		= Constants.PA_GMKT_ONLINE_CODE;
		int	   count 	  		= Constants.PA_GMKT_CONTRACT_CNT;
		String paCode 	  		= null;
		String message	  		= "";
		String duplicateCheck 	= "";
		String apiCode       	= paramMap.getString("apiCode"); 
			
		try{			
			log.info("===== 주문 조회 API Start =====");
			//= Step 0)중복실행 검사
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for(int i = 0; i < count; i++ ){
				paCode = (i==0) ?broadCode:onlineCode;
				paramMap.put("paCode", paCode);
				//=Step 1) Connect Gmarket and Get Data For Insert TPAGMKTORDERLIST AND TPAORDERM
				Thread.sleep(5000); //이베이 호출량 제어 (5초당 1회)
				message += retrieveOrderList(paramMap, paGmktOrderList);
			}
			
			CommonUtil.failCheck(message, paramMap);

			//= Step 2) OrderConfrim - To change Gmarket Order statue "주문 확인"
			innerConfirmOrderProc(paGmktOrderList);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return;
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 주문 조회 API END =====");
		}
	}

	
	public void changeDelyDate(ParamMap paramMap) throws Exception{
		PaGmktAbstractRest rest 				= new PaGmktDeliveryRest();
	    List<Map<String,Object>> changeDelyList = new ArrayList<Map<String,Object>>();
	    String duplicateCheck					= "";
		String apiCode 							= paramMap.getString("apiCode");        
		int	   count   							= Constants.PA_GMKT_CONTRACT_CNT;
		String broadCode 						= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 						= Constants.PA_GMKT_ONLINE_CODE;
		int successCount 						= 0;
		int failCount							= 0;
	    String paCode  							= null;
		String message 							= "";

		try{	
			log.info("===== 발송예정일 등록 및 갱신 API Start =====");

			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
	
			for (int i= 0; i < count; i++){
				
				//= Step 1) Get to need Delivery List for Parameter
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode"		,	paCode);		
				changeDelyList = paGmktDeliveryService.selectChangingDelyList(paramMap);  

				//= Step 2) Send List to Gmarket And update TPAGMKTSHIPPINGDELAYLIST
				for(Map<String,Object> changeDely : changeDelyList){
					try{
						getConnectionForChangingDely(changeDely, rest, paramMap);
						successCount += updateChangingShippingDate(changeDely);
					}catch(Exception e){
						failCount++;
						failChangingShippingDate(changeDely, e);
						continue;
					}
				}
			}		
			message = CommonUtil.setResultMessage("발송 예정일 갱신", "", successCount, failCount);
			CommonUtil.failCheck(message, paramMap);
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
			return;
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
	
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));		
			log.info("===== 발송예정일 등록 및 갱신 API END =====");
		}	
	}
	
	
	public ResponseEntity<?> slipOutProc(ParamMap paramMap) throws Exception{
		
		PaGmktAbstractRest rest 					= new PaGmktDeliveryRest();
		String apiCode								= paramMap.getString("apiCode");
		List< Map<String, Object>> slipOutProcList  = null;
	    String duplicateCheck 						= "";
	    String paCode  								= null;
	    int	   count   								= Constants.PA_GMKT_CONTRACT_CNT;
	    String broadCode  							= Constants.PA_GMKT_BROAD_CODE;
		String onlineCode 							= Constants.PA_GMKT_ONLINE_CODE;
		String message 								= null;
		int successCount 							= 0;
		int totalSize 	 							= 0;
		
		List< Map<String, Object>> slipChangeProcList  = null;//운송장 변경기능
		
		try{	
			log.info("===== 배송 송장 등록  API Start =====");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			for (int i= 0; i < count; i++){
				
				//= Step 1) Get to need Delivery List for Parameter
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
			    slipOutProcList = paGmktDeliveryService.selectSlipOutProcList(paramMap);
			    totalSize += slipOutProcList.size();
				//= Step 2) Send List to Gmarket And update TpaOrderM
			    successCount +=  paGmktDeliveryService.sendShippingTx(rest, slipOutProcList, paramMap);
			}

			if(totalSize > 0){
				message = totalSize + "건 중 " + successCount + " 건 발송 처리 완료";
				message = totalSize - successCount > 0 ? message+ "@FAIL" : message;
			}else{
				message = "발송 처리 대상이 없습니다";
			}
			CommonUtil.failCheck(message, paramMap);
			
			
			//운송장 변경기능 체크
			for (int i= 0; i < count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				slipChangeProcList = paGmktDeliveryService.selectSlipChangeProcList(paramMap);
				paGmktDeliveryService.sendShipping4ChangeInvoiceTx(rest, slipChangeProcList, paramMap);
			}
						
			
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
			log.info("===== 배송 송장 등록 API END =====");
		}	
	    return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}

	public void setDeliveryComplete(ParamMap paramMap) throws Exception{
		
		String apiCode = paramMap.getString("apiCode");       
		String duplicateCheck = "";
	    List<Map<String,String>> deliveryCompleteList = new ArrayList<Map<String,String>>();
		int	   count   = Constants.PA_GMKT_CONTRACT_CNT;
		String broadCode  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
	    String paCode 	  = null; 
	    String message = null;
		int    successCount = 0;
	    int	   failCount  = 0;	 
	    
		try{			
			log.info("===== 배송 완료 처리  API Start =====");
			//= 중복체크
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});		

			//= Step 1) Get Delivery Complete list
			for(int i = 0 ; i< count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
		    	paramMap.put("paCode", paCode);
				deliveryCompleteList = paGmktDeliveryService.selectDeliveryCompleteList(paramMap);
			    
			    for(Map<String,String> rtnMap : deliveryCompleteList){
					paramMap.put("urlParameter", rtnMap.get("CONTR_NO"));
					rtnMap.put("SITE_GB", paramMap.getString("siteGb"));

					try{
						if (rtnMap.get("CONTENT").toString() == null || rtnMap.get("CONTENT").toString().equals("")){ // =굿스플로 미 연동건
							restUtil.getConnection(null, paramMap);  //=통신
							rtnMap.put("RESULT", "Success");
							rtnMap.put("COMMENT", "배송완료 처리성공");
						}else{ //= 굿스플로 연동건
							rtnMap.put("RESULT", "Success");
							rtnMap.put("COMMENT", "굿스플로 연동건 배송완료");
						}
						successCount++;

					}catch(Exception e){
						if(e.getMessage().contains("이미 배송완료 되었습니다.") || e.getMessage().contains("반품 상태")) {
							rtnMap.put("RESULT", "Success");
							rtnMap.put("COMMENT", "(자동완료)" + e.getMessage());
							successCount ++;
						} else {
							rtnMap.put("RESULT", "Fail");
							rtnMap.put("COMMENT", "배송 완료 처리실패" + e.getMessage());
							failCount ++;
						}
					}finally{
						paGmktDeliveryService.updateDeliveryCompleteProcTx(rtnMap);
					}
			    }//end of for			      			    
			}// end of for
				
			message = CommonUtil.setResultMessage("배송 완료", "", successCount, failCount);	
			CommonUtil.failCheck(message, paramMap);
			
		}catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
		
			try{
				CommonUtil.dealSuccess(paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 배송 완료 처리  API END =====");
		}
	}
	
	public void setCompleteList(ParamMap paramMap) throws Exception{
		String apiCode = paramMap.getString("apiCode");            
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		String broadCode  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  = null;
		int	   count 	  = Constants.PA_GMKT_CONTRACT_CNT;
		String message	  = "";
		String duplicateCheck = "";
		
		try{			
			//= 중복 실행 Check
			log.info("===== 수취확인 목록 조회 API Start =====");

			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
					
			for(int i = 0; i < count; i++ ){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				message += retrieveOrderCompleteList(rest, paramMap);
			}		
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
			log.info("===== 수취확인 목록 조회 API END =====");
		}	
	}

	public void setNotreceiveList(ParamMap paramMap) throws Exception{
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		List<PaGmkNotRecive> notReceiveList = new ArrayList<PaGmkNotRecive>();
		String duplicateCheck = "";
		int	   count  	  = Constants.PA_GMKT_CONTRACT_CNT;
		String broadCode  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  = null;
		String message	  = "";

		try{			
			log.info("===== 미수령신고조회 API Start =====");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
			//= Step 1) Connection and Get date -> Set notReceiveList
			for(int i = 0 ; i< count; i++){
				paCode = (i==0) ? broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				message += getNotReceiveList(rest,paramMap, notReceiveList);
			}
			
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
			log.info("===== 미수령신고조회 API END =====");
		}
	}
	
	public void setNotreceiveCancelList(ParamMap paramMap) throws Exception{
		
		String apiCode = paramMap.getString("apiCode");          
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		String duplicateCheck = "";
		List<Map<String,String>> notReceiveCancelList = new ArrayList<Map<String,String>>();		
		int	   count   = Constants.PA_GMKT_CONTRACT_CNT;
		String broadCode  = Constants.PA_GMKT_BROAD_CODE;
		String onlineCode = Constants.PA_GMKT_ONLINE_CODE;
		String paCode 	  = null;
		String message	  = null;
		
		try{
			
			log.info("===== 미수령신고 철회요청 API Start =====");
			//= 중복 실행 Check
			duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
		
			//= Step 1)미수령 철회 리스트 조회 
			for(int i = 0 ; i< count; i++){
				paCode = (i==0) ?broadCode:onlineCode;  //21, 22
				paramMap.put("paCode", paCode);
				notReceiveCancelList = paGmktDeliveryService.selectNotReceiveListForCancel(paramMap);  
				message += withdrawNotReceiveList(rest,paramMap,notReceiveCancelList);		
			}
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
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", apiCode);
			log.info("===== 미수령신고 철회요청 API END =====");
		}
	}


	/**해당 메소드는 Auction 개발이 진행되면 완벽하게 삭제되어야함.(리펙토링이 전혀 되지 않은 코딩임)**/
	private int preRetrieveOrderList(HttpServletRequest request, String paCode ,String orderNo, PaGmktAbstractRest rest) throws Exception{
		String message	  = "";
		int	   failCount  = 0;
		String apiCode    = "IF_PAGMKTAPI_V2_02_002";
		List<PaGmkOrder> paGmktOrderList = new ArrayList<PaGmkOrder>();
		ParamMap paramMap = new ParamMap();
		paramMap.put("ORDER_NO", orderNo);
		paramMap.put("ORDER_STATUS",0);
		paramMap.put("REQUEST_TYPE",1);
		paramMap.put("apiCode", apiCode);
		paramMap.put("paCode", paCode);
		paramMap.put("siteGb", "PAG");
		paramMap.put("PAGE_INDEX",0);
		paramMap.put("FROM_DATE",DateUtil.addDay(systemService.getSysdatetime()	,  -4 ));
		paramMap.put("TO_DATE",DateUtil.addDay(systemService.getSysdatetime()	,  +1 ));
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		message += retrieveOrderList(paramMap, paGmktOrderList);
		CommonUtil.failCheck(message, paramMap);
		
		if(paramMap.getString("code").equals("500")){
			failCount ++;
		}		
		return failCount;
	}

	//= Information 주문 조회 API 호출 후 수신한 데이터를 TPAGMKTORDERLIST, TPAORDERM Insert
	private void getOrderListForPreOrder(PaGmktAbstractRest rest, ParamMap paramMap, List<PaGmkOrder> paGmktOrderList ) throws Exception{			
		PaGmkOrder paGmktOrder = null;
		String response = null;
		Map<String, Object> allOrderMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		
		response =  restUtil.getConnection(rest,  paramMap); 
		allOrderMap = refineOrderMap(response);
		if(allOrderMap ==null || allOrderMap.size() < 1) return;
		orderList = ComUtil.map2List(allOrderMap, "RequestOrders");
			
		for( Map<String, Object > orderMap : orderList)	{
			try{
				paGmktOrder = new PaGmkOrder();
				paGmktOrder = setPaGmkOrder(orderMap, paramMap);
				paGmktOrderList.add(paGmktOrder);
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
				continue;
			}
		}//end of for
	}
	
	
	//= Information 주문 조회 API 호출 후 수신한 데이터를 TPAGMKTORDERLIST, TPAORDERM Insert
	private String retrieveOrderList(ParamMap paramMap, List<PaGmkOrder> paGmktOrderList ) throws Exception{			
		PaGmktAbstractRest rest = new PaGmktDeliveryRest();
		PaGmkOrder paGmktOrder = null;
		String response = null;
		int failCount = 0;
		int successYn = 0;
		int totalSuccessCount = 0;
		Map<String, Object> allOrderMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		
		try {
			response =  restUtil.getConnection(rest,  paramMap); 
			allOrderMap = refineOrderMap(response);
			if(allOrderMap ==null || allOrderMap.size() < 1) return "주문 조회 결과 없음";
			orderList = ComUtil.map2List(allOrderMap, "RequestOrders");
			
				for( Map<String, Object > orderMap : orderList)	{
					try{
						paGmktOrder = new PaGmkOrder();
						paGmktOrder = setPaGmkOrder(orderMap, paramMap);
						checkItemOptionCode(paGmktOrder);
						
						if( paGmktOrder.getPayDate() != null ) {
							//전화번호, 발송예정일 null인 경우 check
							checkNullResponse(paGmktOrder);
						}
						
						if(checkPaidPreOrder(paGmktOrder)){
							successYn = paGmktDeliveryService.updatePayDate(paGmktOrder);  //무통장 입금 결제 처리
						}else{
							successYn = paGmktDeliveryService.insertPaGmktOrderListTx(paGmktOrder);	//무통장 미입금 , 일반주문
						}
				
						if(successYn == 1){
							paGmktOrderList.add(paGmktOrder);
							totalSuccessCount++;
						}
						
					}catch(Exception e){
						systemService.insertPassingErrorToApitracking(paramMap, e);
						failCount++;
						continue;
					}
				}//end of for
		} catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
		}
		return CommonUtil.setResultMessage("주문 조회", paramMap.getString("paCode"), totalSuccessCount, failCount);	
	}
	
	
	private String retrievePreOrderList(List<PaGmkOrder> paGmktOrderList) throws Exception{					
		if(paGmktOrderList.size() < 1) return "무통장 조회 결과 없음";
		
		PaGmktAbstractRest rest 	= new PaGmktDeliveryRest();
		PaGmkOrder paGmktOrder 		= null;
		String paGroupCode 			= paGmktOrderList.get(0).getPaGroupCode();
		String response 			= null;
		int failCount 				= 0;
		int successYn 				= 0;
		
		Map<String, Object> allOrderMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		ParamMap paramMap = new ParamMap();
		paramMap.put("apiCode"			, "IF_PAGMKTAPI_V2_02_002");
		paramMap.put("paGroupCode"		, paGroupCode);
		paramMap.put("ORDER_STATUS"		, 0);
		paramMap.put("REQUEST_TYPE"		, 1);
		paramMap.put("PAGE_INDEX"		, 0);
		paramMap.put("FROM_DATE"		, DateUtil.addDay(systemService.getSysdatetime()	,  -4 ));
		paramMap.put("TO_DATE"			, DateUtil.addDay(systemService.getSysdatetime()	,  +1 ));
		CommonUtil.setParams(paramMap);
		
		for(PaGmkOrder order  : paGmktOrderList){
			
			try{
				paramMap.put("ORDER_NO"		, order.getContrNo());
				paramMap.put("paCode"		, order.getPaCode());
				response =  restUtil.getConnection(rest,  paramMap); 
				allOrderMap = refineOrderMap(response);
				
				if(allOrderMap ==null || allOrderMap.size() < 1){ //특정 주문번호로 조회
					failCount++;
					continue;
				}
				
				orderList = ComUtil.map2List(allOrderMap, "RequestOrders");
				
				orderList.get(0).put("PayDate", ""); //무통장 주문이지만 결제일이 있는 경우 대비(주문확인 로직 없으므로 무조건 접수단계로 생성)
				paGmktOrder = setPaGmkOrder(orderList.get(0), paramMap);
				checkItemOptionCode(paGmktOrder);
				successYn += paGmktDeliveryService.insertPaGmktOrderListTx(paGmktOrder);	//무통장 미입금 , 일반주문
		
			}catch(Exception e){
				systemService.insertPassingErrorToApitracking(paramMap, e);
				failCount++;
				continue;
			}		
		}//end of for
			
		return CommonUtil.setResultMessage("주문 조회", paramMap.getString("paCode"), successYn + failCount, failCount);	
	}
	
	private boolean checkPaidPreOrder(PaGmkOrder paGmktOrder) throws Exception{
		int checkPaGmktOrder  = paGmktDeliveryService.checkUnpaidPaGmktOrderList(paGmktOrder);
		if(checkPaGmktOrder > 0){
			return true;
		}else{
			return false;
		}	
	}
	
	//= 바로 입점된 상품을 주문 할 경우 ItemCode가 Null이기 때문에 일단 던지고 다음 배치에서 다시 받는것으로 한다. 
	private void checkItemOptionCode(PaGmkOrder paGmktOrder) throws Exception{
		if(paGmktOrder.getItemOptionCode() == null || paGmktOrder.getItemOptionCode().equals("")){
			throw processException("msg.cannot_save", new String[] { "ITEM OPTION CODE IS NULL. 제휴 주문번호: " + paGmktOrder.getContrNo()});
		}
	}
	
	//= 전화번호 혹은 발송예정일이 null인 경우 주문생성 안하기 위함.
	private void checkNullResponse(PaGmkOrder paGmktOrder) throws Exception{
		
		String buyerMobileTel = paGmktOrder.getBuyerMobile();
		String buyerTel = paGmktOrder.getBuyerTel();
		String hpNo = paGmktOrder.getReceiverMobile();
		String telNo = paGmktOrder.getReceiverTel();
		Timestamp transDueDate = paGmktOrder.getTransDueDate();
		
		if( (buyerMobileTel == null || buyerMobileTel.equals("")) && (buyerTel == null || buyerTel.equals("") ) 
			 && (hpNo == null || hpNo.equals("")) && (telNo == null || telNo.equals("")) ){
			throw processException("msg.cannot_save", new String[] { "전화번호 NULL. 제휴 주문번호: " + paGmktOrder.getContrNo()});
		}
		if( transDueDate == null ){
			throw processException("msg.cannot_save", new String[] { "발송예정일(transDueDate) NULL. 제휴 주문번호: " + paGmktOrder.getContrNo()});
		}
	}
	
	//= Information 수취 확인 조회 API 호출 후 수신한 데이터를  TPAORDERM Update
	private String retrieveOrderCompleteList(PaGmktAbstractRest rest, ParamMap paramMap ) throws Exception{			
		PaGmkOrder paGmktOrder = null;
		String response = null;
		int failCount = 0;
		int successYn = 0;
		int cnt = 0;
		Map<String, Object> allOrderMap = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		
		response =  restUtil.getConnection(rest,  paramMap); 
		allOrderMap = refineOrderMap(response);
		if(allOrderMap ==null || allOrderMap.size() < 1) return "수취 확인 조회 결과 없음";
		orderList = ComUtil.map2List(allOrderMap, "RequestOrders");
			
		for( Map<String, Object > orderMap : orderList)	{
			try{
				paGmktOrder = new PaGmkOrder();
				paGmktOrder = setPaGmkOrder(orderMap, paramMap);
				
				cnt = paGmktDeliveryService.checkPaGmktOrderList(paGmktOrder);
				if(cnt < 1) throw new Exception(); 
				
 				successYn += paGmktDeliveryService.updateTpaordermForCompleteOrderList(paGmktOrder);							
			
			}catch(Exception e){
				
				Map<String, Object > failMap = new HashMap<>();
				failMap.put("payNo"		  , ComUtil.NVL(orderMap.get("PayNo")).toString() );
				failMap.put("paCode"	  , paramMap.getString("paCode") );
				failMap.put("paGroupCode" , paramMap.getString("paGroupCode"));
				failMap.put("siteGb"	  , paramMap.getString("siteGb"));
				failMap.put("contrNo"  	  , ComUtil.NVL(orderMap.get("OrderNo")).toString() );
				failMap.put("message"	  , "수취확인 업데이트에 실패했습니다");
				cnt = paGmktDeliveryService.updatePaOrderMFail(failMap);
				if(cnt < 1) log.error(ComUtil.NVL(orderMap.get("ContrNo")).toString() + "ERROR When saving PaorderM For Fail - retrieveOrderCompleteList");
				
				continue;
			}
		}//end of for
			
		return CommonUtil.setResultMessage("수취 확인 목록 조회", paramMap.getString("paCode"), successYn, failCount);	
	}
	
	//= Information 주문 조회 API호출의 결과물로 받은 데이터를 vo로 변환
		
	private PaGmkOrder setPaGmkOrder(Map<String, Object> orderMap, ParamMap paramMap) throws Exception{
		if(orderMap == null) return null;
		
		Map<String, Object> subOptionMap = new HashMap<String, Object>();
		
		String sysdate  	   	= systemService.getSysdatetimeToString();
		String paDoFlag		   	= null; 
		PaGmkOrder paGmktOrder 	= null;
		Timestamp payDate 		= PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("PayDate")).toString());		
		String paCode 			= paramMap.getString("paCode");
		String paGroupCode		= paramMap.getString("paGroupCode");
		String insertId 		= paramMap.getString("siteGb");
		
		if(payDate == null){
			paDoFlag = "10"; //무통장
		}else if(payDate.equals("")){
			paDoFlag = "10"; //무통장
		}else{ 
			paDoFlag = "30";
		}
		
		paGmktOrder = new PaGmkOrder();
		
		paCode = CommonUtil.checkSourcingMediaForTestOrder	(ComUtil.NVL(orderMap.get("OutGoodsNo")).toString(),ComUtil.NVL(paCode).toString());
		paGmktOrder.setPaCode								(paCode);
		paGmktOrder.setPaGroupCode							(paGroupCode);
		paGmktOrder.setPaDoFlag								(paDoFlag);
		paGmktOrder.setPayNo								(ComUtil.NVL(orderMap.get("PayNo")).toString());				//장바구니 번호
		paGmktOrder.setGroupNo								(ComUtil.NVL(orderMap.get("GroupNo")).toString());				//묶음 배송비 정책번호(배송 번호)
		paGmktOrder.setContrNo								(ComUtil.NVL(orderMap.get("OrderNo").toString()).toString());	//주문번호 (Contr_No)
		paGmktOrder.setContrNoSeq							("001");	//주문번호 순번
		
		paGmktOrder.setOrderDate							(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("OrderDate")).toString()));	//주문일
		paGmktOrder.setPayDate								(payDate);		//결제일
		paGmktOrder.setOrderConfirmDate						(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("OrderConfirmDate")).toString())); //주문확인일
		paGmktOrder.setBuyCompleteDate						(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("BuyDecisionDate")).toString()));   //구매결정일
		
		paGmktOrder.setGoodsNo								(ComUtil.NVL(orderMap.get("GoodsNo")).toString());		//EMS상품번호
		paGmktOrder.setSiteGoodsNo							(ComUtil.NVL(orderMap.get("SiteGoodsNo")).toString());//사이트 상품번호
		paGmktOrder.setOutGoodsNo							(ComUtil.NVL(orderMap.get("OutGoodsNo")).toString());	//판매자관리코드(판매자상품번호)
		paGmktOrder.setGoodsName							(ComUtil.NVL(orderMap.get("GoodsName")).toString());	//상품이름
		paGmktOrder.setSalePrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SalePrice"), "0"))));		//판매단가
		paGmktOrder.setPaymentPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("OrderAmount"),"0"))));  //판매금액
		paGmktOrder.setAcntPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("AcntMoney"),"0"))));		//결제금액 
		paGmktOrder.setCostPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("CostPrice"),"0"))));		//공급원가
		paGmktOrder.setContrQty								(Integer.parseInt(String.valueOf(ComUtil.NVL(orderMap.get("ContrAmount"),"0"))));			//수량
		
		paGmktOrder.setSellerDiscountPrice					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SellerDiscountPrice"),"0"))));	 //판매자 할인 금액 총액  (sellerDiscountPrice1+sellerDiscountPrice2)
		paGmktOrder.setSellerDiscountPrice1					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SellerDiscountPrice1"),"0"))));	 //판매자 쿠폰 할인 금액1
		paGmktOrder.setSellerDiscountPrice2					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SellerDiscountPrice2"),"0"))));	 //판매자 쿠폰 할인 금액2
		paGmktOrder.setDirectDiscountPrice					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("DirectDiscountPrice"),"0"))));	 //Ebay 할인금액
		
		paGmktOrder.setSinglePayDcAmt						(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SinglePayDcAmnt"),"0"))));				 //일시불할인
		paGmktOrder.setMultiBuyDcAmt						(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("MultiBuyDcAmnt"),"0"))));			     //복수구매 할인(auction)
		paGmktOrder.setVipBuyDcAmt							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("GreatMembDcAmnt"),"0"))));				 //우수회원할인(auction)
		
		paGmktOrder.setFeeContidtion						(ComUtil.NVL(orderMap.get("DeliveryFeeCondition")).toString());									//배송비 조건	
		paGmktOrder.setShippingFee							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("ShippingFee"),"0"))));					//배송비
		paGmktOrder.setAddShippingFee						(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("BackwoodsAddDeliveryFee"),"0"))));		//도서 산간 추가 배송비
		paGmktOrder.setAddJejuShippingFee					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("JejuAddDeliveryFee"),"0"))));		//제주 추가 배송비
				
		paGmktOrder.setTransDate							(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("TransDate")).toString()));						//배송정보등록일
		paGmktOrder.setTransDueDate							(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("TransDueDate")).toString()));				//발송마감일
		paGmktOrder.setTransCompleteDate					(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("TransCompleteDate")).toString()));		//배송완료일
		paGmktOrder.setTransType							(ComUtil.NVL(orderMap.get("TransType")).toString());													//발송정책
		
		paGmktOrder.setSettlePrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SellerCashbackMoney"),"0"))));			//정산예정금액
		paGmktOrder.setServiceFee							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("SellerCommission"),"0"))));				//서비스이용료				
		paGmktOrder.setSellerCashBackMoney					(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("ShippingFee"),"0"))));			//판매자지급 스마일캐시
		paGmktOrder.setOptAddPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("OptSelPrice"),"0"))));					//주문옵션추가금액
		paGmktOrder.setOptSelPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("OptAddPrice"),"0"))));					//추가구성 금액
			
		paGmktOrder.setBuyerName							(ComUtil.NVL(orderMap.get("BuyerName")).toString());					//구매자명
		paGmktOrder.setBuyerId								(ComUtil.NVL(orderMap.get("BuyerId")).toString());						//구매자ID
		paGmktOrder.setBuyerMobile							(ComUtil.NVL(orderMap.get("BuyerMobileTel")).toString());			//구매자 휴대폰		
		paGmktOrder.setBuyerTel								(ComUtil.NVL(orderMap.get("BuyerTel")).toString());					//구매자 전화번호
		
		paGmktOrder.setReceiverName							(ComUtil.NVL(orderMap.get("ReceiverName")).toString().trim());			//수령인명
		paGmktOrder.setReceiverMobile						(ComUtil.NVL(orderMap.get("HpNo")).toString().trim());					//수령인휴대폰
		paGmktOrder.setReceiverTel							(ComUtil.NVL(orderMap.get("TelNo")).toString().trim());					//수령인 전화번호
		paGmktOrder.setReceiverZipCode						(ComUtil.NVL(orderMap.get("ZipCode")).toString());				//수령인 우편번호
		paGmktOrder.setReceiverAddress1						(ComUtil.NVL(orderMap.get("DelFrontAddress")).toString());	//수령인 주소1	
		paGmktOrder.setReceiverAddress2						(ComUtil.NVL(orderMap.get("DelBackAddress")).toString());		//수령인 주소2
		paGmktOrder.setReceiverAddress						(ComUtil.NVL(orderMap.get("DelFullAddress")).toString());		//수령인 주소(수령인주소1+2) // FullAddress 

		String note = ComUtil.NVL(orderMap.get("DelMemo")).toString();
		int len = note.getBytes("UTF-8").length;
		//배송메세지
		if( len > 200) {
			paGmktOrder.setDelMemo( ComUtil.subStringBytes(note, 185) + "…(내용잘림)");
		} else {
			paGmktOrder.setDelMemo							(ComUtil.NVL(orderMap.get("DelMemo")).toString());
		}
		paGmktOrder.setAllocationDate1						(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("AllocationStartDate")).toString()));		//배송요청시작시간
		paGmktOrder.setAllocationDate2						(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("AllocationEndDate")).toString()));		//배송요청종료시간
		paGmktOrder.setDelySlotId							(ComUtil.NVL(orderMap.get("DeliverySlotId")).toString());											//배송SlotID
		paGmktOrder.setBranchPrice							(Double.parseDouble(String.valueOf(ComUtil.NVL(orderMap.get("BranchPrice"),"0"))));					//지점 추가 금액
		paGmktOrder.setReplaceYn							(ComUtil.NVL(orderMap.get("ReplaceYn")).toString());													//대체상품동의여부
		
		paGmktOrder.setDelyName								(ComUtil.NVL(orderMap.get("TakbaeName")).toString());						//택배사명
		paGmktOrder.setDelyNo								(ComUtil.NVL(orderMap.get("NoSongjang")).toString());						//송장번호
		paGmktOrder.setOverseaTransYn						(ComUtil.NVL(orderMap.get("OverseaTransYn")).toString());			//해외배송여부
		paGmktOrder.setOutOrderNo							(ComUtil.NVL(orderMap.get("GlobalSellerYn")).toString());				//글로벌샵여부
		paGmktOrder.setInfoCin								(ComUtil.NVL(orderMap.get("InfoCin")).toString());							//개인통관고유번호
		
		paGmktOrder.setSkuNo								(ComUtil.NVL(orderMap.get("SKUNo")).toString());								//SKU번호
		//다음 컬럼은 G마켓 12월18일 배포시 삭제됩니다  = SKUAmount
		paGmktOrder.setSkuQty								(Integer.parseInt(String.valueOf(ComUtil.NVL(orderMap.get("SKUAmount"),"0"))));	//SKU수량
//		paGmktOrder.setOptionGoods							(ComUtil.NVL(orderMap.get("OptionGoods")).toString());  		 		//사은품
		paGmktOrder.setInventoryNo							(ComUtil.NVL(orderMap.get("InventoryNo")).toString());			  	 	//G마켓 히스토리 코드
			
		paGmktOrder.setModifyDate							(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmktOrder.setInsertDate							(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paGmktOrder.setInsertId								(insertId);
		
		subOptionMap =  ComUtil.map2map(orderMap,"ItemOptionSelectList",0);
		if( subOptionMap != null){
			paGmktOrder.setItemOptionCode					(ComUtil.NVL(subOptionMap.get("ItemOptionCode")).toString());
			paGmktOrder.setItemOptionOrderCnt				(Integer.parseInt(String.valueOf(ComUtil.NVL(subOptionMap.get("ItemOptionOrderCnt"),"0"))));
			paGmktOrder.setItemOptionValue					(ComUtil.NVL(subOptionMap.get("ItemOptionValue")).toString());
		}
		subOptionMap =  ComUtil.map2map(orderMap,"ItemOptionAdditionList",0);
		//subOptionMap = (Map<String, Object>) orderMap.get("ItemOptionAdditionList");
		if( subOptionMap != null){
			paGmktOrder.setItemAddOptionCode				(ComUtil.NVL(subOptionMap.get("ItemOptionCode")).toString());
			paGmktOrder.setItemAddOptionOrderCnt			(Integer.parseInt(String.valueOf(ComUtil.NVL(subOptionMap.get("ItemOptionOrderCnt"),"0"))));
			paGmktOrder.setItemAddOptionValue				(ComUtil.NVL(subOptionMap.get("ItemOptionValue")).toString());
		}	
		
		if("109".equals(ComUtil.NVL(orderMap.get("OutOrderNo")).toString())) {
			paGmktOrder.setG9OrderYn("1");
		}else {
			paGmktOrder.setG9OrderYn("0");
		}
		
		paGmktOrder.setIsGiftOrder(ComUtil.NVL(orderMap.get("IsGiftOrder")).toString()); // 선물하기 주문 구분
		paGmktOrder.setGiftConfirmDate(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("GiftConfirmDate")).toString())); // 선물수락일
		paGmktOrder.setGiftConfirmDueDate(PaGmktDateUtil.toTimestamp(ComUtil.NVL(orderMap.get("GiftConfirmDueDate")).toString())); // 선물수락기한
		
		return paGmktOrder;
	}
	
	
	
	//= Information 주문 확인 시작함수
    private void innerConfirmOrderProc(List<PaGmkOrder> paGmktOrderList){
		for( PaGmkOrder paGmkOrder : paGmktOrderList ){
			try{
				
				innerConfirmOrderProc(paGmkOrder);
				/*
				   if(!holdPreOrderYn(paGmkOrder)){
					innerConfirmOrderProc(paGmkOrder);
				}
				*/
				
			}catch(Exception e){
				continue;
			}
		}
	}
    
    private boolean holdPreOrderYn(PaGmkOrder paGmkOrder) throws Exception {
    	if(paGmkOrder.getOrderDate().equals(paGmkOrder.getPayDate())){
    		return false;
    	}
    	//=무통장
    	//전체 주문 - 주문(입금승인 Pay_date is not null ) - 취소 완료 > 0
    	//무언가 G마켓으로 부터 넘어오지 않은 취소건이 존재하면 unAttendedCount의 카운트는 0보다 큼
    	return CommonUtil.existUnAttendedCount(paGmkOrder.getPayNo());
    }
	//= Information 주문 확인시 주문 조회를 다시 한 후 주소가 바뀌었는지 확인 후 바뀌었으면 해당 주문 건 주소지 변경
    private int checkAddressChanged(String changeYnResponse , ParamMap paramMap, PaGmktAbstractRest rest) throws Exception{
		
		PaGmkOrder paGmkOrder = null;
		String response = null;
		int isChanged =0; 
		Map<String, Object> orderMap    = new HashMap<String, Object>();
		Map<String, Object> allOrderMap = new HashMap<String, Object>();
		
		try{			
			allOrderMap = refineOrderMap(changeYnResponse);
			isChanged = Integer.parseInt(String.valueOf(allOrderMap.get("IsChanged")));
			if (isChanged == 0) return 1;
			
			// = 주문 재 조회 ( 파라미터 세팅 + 통신)
			response = getConnectionAfterOrderConfrim(rest, paramMap); 
			// = 조회 데이터를 가지고 paGmktOrderList 세팅
			allOrderMap = refineOrderMap(response);
			orderMap = ComUtil.map2map(allOrderMap, "RequestOrders", 0);
			paGmkOrder = setPaGmkOrder(orderMap, paramMap);
			// = 해당 주문건 주소지 변경
			paGmktDeliveryService.updateReceiverInfo(paGmkOrder);
			
		}catch(Exception e ){
			return 0;
		}
		return 1;	
	}

	//= Information 주소 변경을 알아 보기 위해 다시한번 주문조회 API호출
    private String getConnectionAfterOrderConfrim(PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{
		
		ParamMap param = new ParamMap();
		param.put("apiCode"			, "IF_PAGMKTAPI_V2_02_002");
		param.put("siteGb"			, paramMap.getString("siteGb"));
		param.put("paCode"			, paramMap.getString("paCode"));
		param.put("paGroupCode"		, paramMap.getString("paGroupCode"));
		param.put("ORDER_NO"		, paramMap.getString("urlParameter"));
		param.put("ORDER_STATUS"	, 0);
		param.put("PAGE_INDEX"		, 0);
		param.put("REQUEST_TYPE"	, 2);
		param.put("FROM_DATE"		, DateUtil.addDay(systemService.getSysdatetime()	,  -2 ));
		param.put("TO_DATE"			, DateUtil.addDay(systemService.getSysdatetime()  	,  +1 ));
		CommonUtil.setParams(param);

		return restUtil.getConnection(rest,  param);
	}
	

	private int updateChangingShippingDate(Map<String,Object> changeDely) throws Exception{
		Map<String,Object> orderMap = new HashMap<String, Object>();
		int executeCnt = 0;
		
		orderMap.put("paCode"			, changeDely.get("PA_CODE").toString());
		orderMap.put("paGroupCode"		, changeDely.get("PA_GROUP_CODE").toString());
		orderMap.put("payNo"			, changeDely.get("PAY_NO").toString());
		orderMap.put("contrNo"			, changeDely.get("CONTR_NO").toString());
		orderMap.put("siteGb"			, changeDely.get("SITE_GB").toString());
		orderMap.put("apiResultCode"	, "000000");
		orderMap.put("apiResultMessage" , "발송예정일 연기");
		orderMap.put("remark1"			, "발송예정일 연기 " + changeDely.get("DELY_DATE").toString());
		
		executeCnt = paGmktDeliveryService.updateRemark1TpaorderM(orderMap);
		if(executeCnt != 1){
			throw processException("msg.cannot_save", new String[] { "TPAORDERM REMARK1 UPDATE" });
		}
		return executeCnt;
	}
	
	private void failChangingShippingDate(Map<String,Object> changeDely, Exception se) throws Exception{
		Map<String,Object> orderMap = new HashMap<String, Object>();
		int executeCnt = 0;
		
		orderMap.put("paCode"			, changeDely.get("PA_CODE").toString());
		orderMap.put("paGroupCode"		, changeDely.get("PA_GROUP_CODE").toString());
		orderMap.put("payNo"			, changeDely.get("PAY_NO").toString());
		orderMap.put("contrNo"			, changeDely.get("CONTR_NO").toString());
		orderMap.put("siteGb"			, changeDely.get("SITE_GB").toString());
		orderMap.put("apiResultCode"	, "999999");
		orderMap.put("apiResultMessage" , se.getMessage().length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage());
		orderMap.put("remark1"			, "");
		
		if(se.getMessage().contains("배송완료 상태입니다.")) {
			orderMap.put("apiResultMessage" , "(자동완료)" + (se.getMessage().length() > 3950 ? se.getMessage().substring(0, 3950) : se.getMessage()));
			orderMap.put("apiResultCode"	, "000000");
			orderMap.put("paDoFlag"	, "80");
		}
		
		executeCnt = paGmktDeliveryService.updateRemark1TpaorderM(orderMap);
		
		if(executeCnt != 1){
			log.error("Fail updateRemark1TpaorderM - " + se.getMessage());
		}
	}
	
	private String getConnectionForChangingDely( Map<String,Object> delyMap, PaGmktAbstractRest rest, ParamMap paramMap) throws Exception{

		paramMap.put("ORDER_NO"		, 	delyMap.get("CONTR_NO").toString());		
		paramMap.put("REASON_TYPE"  ,   delyMap.get("REASON_TYPE").toString());
		paramMap.put("REASON_DETAIL", 	delyMap.get("REASON_DETAIL").toString());
		paramMap.put("DELY_DATE"	, 	delyMap.get("DELY_DATE").toString());
				
		return restUtil.getConnection(rest,  paramMap);
	}
	
	//= Information Map 정제 
	@SuppressWarnings("unchecked")
	private Map<String, Object> refineOrderMap(String response) throws Exception{
		Map<String, Object> mainMap = new HashMap<String, Object>();
		
		mainMap = ComUtil.splitJson(response);
		return (Map<String, Object>) mainMap.get("Data");
	}
	
	//= Information 미수령 신고 조회 호출 후 PaGmktNotReceiveList 및 TCOUNSELM, TCOUNSELDT Insert 
	private String getNotReceiveList(PaGmktAbstractRest rest, ParamMap param,  List<PaGmkNotRecive> notReceiveList ) throws Exception{
		Map<String, Object> allResultMap = new HashMap<String, Object>();
		PaGmkNotRecive paNotRecive = null;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		int successCount = 0;
		int failCount 	 = 0;	
		
		try{
			String response = restUtil.getConnection(rest, param);
			allResultMap = ComUtil.splitJson(response);
			resultList = ComUtil.map2List(allResultMap, "Data");		
		}catch(Exception e){
			return CommonUtil.setResultMessage("미수령 조회 API 에러", param.getString("paCode"), 0, 1);	
		}
	
		
		for(Map<String, Object> resultMap : resultList){
			paNotRecive= new PaGmkNotRecive();
			try{
				resultMap.put("paCode"		, param.get("paCode"));
				resultMap.put("paGroupCode"	, param.get("paGroupCode"));
				resultMap.put("siteGb"		, param.get("siteGb"));
				paNotRecive = setPaGmktNotReceiverList(resultMap);
				paGmktDeliveryService.insertPaGmktNotReceiveListTx(paNotRecive);
				successCount ++;
			}catch(Exception e){
				failCount++;
				continue;
			}
		}//end of for
	
		return CommonUtil.setResultMessage("미수령 조회", param.getString("paCode"), successCount, failCount);	
	}
	
	private String withdrawNotReceiveList(PaGmktAbstractRest rest, ParamMap paramMap, List<Map<String,String>> notReceiveCancelList) {
		
		PaGmkNotRecive notreceive = null;
		int failCount 		= 0;
		int successCount 	= 0;

		
		for(Map<String,String> notReceiveCancel : notReceiveCancelList ){
			try{
				notreceive = new PaGmkNotRecive();
				paramMap.put("CONTR_NO", notReceiveCancel.get("CONTR_NO").toString());
				notreceive = getNotReceiveListForCancel(rest , paramMap);
				cancelNotReceiveList(rest, paramMap, notreceive);
				successCount++;
		
			}catch(Exception e){
				failCount ++;
			}
			
		}//end of for
		
		return CommonUtil.setResultMessage("미수령 철회 조회", paramMap.getString("paCode"), successCount, failCount);	

	}
	
	//= Information 미수령 철회를 하기 전, 해당 건의 미수령 상태조회 API 호출하여 받은 값을 VO객체로 리턴 
	//= Because     이미 철회 된 요청인지 아닌지  확인하기 위해
	private PaGmkNotRecive getNotReceiveListForCancel(PaGmktAbstractRest rest, ParamMap param) throws Exception{
		
		PaGmkNotRecive paNotRecive = null;
		Map<String, Object> allResultMap = new HashMap<String, Object>();
	
		param.put("apiCode"		, "IF_PAGMKTAPI_V2_02_009");
		param.put("TYPE"		, 0);
		param.put("FROM_DATE" 	, "");
		param.put("TO_DATE" 	, "");
		String response = restUtil.getConnection(rest, param);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		allResultMap = ComUtil.splitJson(response);
		resultMap = ComUtil.map2map(allResultMap, "Data", 0);
		resultMap.put("paCode"		, param.getString("paCode"));
		resultMap.put("paGroupCode" , param.getString("paGroupCode"));
		resultMap.put("siteGb" 		, param.getString("siteGb"));

		paNotRecive= new PaGmkNotRecive();
		paNotRecive = setPaGmktNotReceiverList(resultMap);
				
		return paNotRecive;
	
	}
	
	//= Information 지마켓에 미수령 철회 요청을 하고 TPAGMKTNOTRECEIVELIST의 상태값을 UPDATE해줌
	private void cancelNotReceiveList(PaGmktAbstractRest rest, ParamMap param, PaGmkNotRecive nR) throws Exception{
		
		if(nR == null) return;
		
		PaGmkNotRecive notreceive = null;
		notreceive = new PaGmkNotRecive();
		Map<String, Object> pMap = new HashMap<>();
		int excuteResult = 0;
		
		param.put("apiCode", "IF_PAGMKTAPI_V2_02_010");
		
		pMap.put("paCode"		, nR.getPaCode());
		pMap.put("paGroupCode"  , nR.getPaGroupCode());
		pMap.put("payNo"		, nR.getPayNo());
		pMap.put("contrNo" 		, nR.getContrNo());
		pMap.put("claimDate"	, nR.getClaimDate());
		pMap.put("transdate"	, systemService.getSysdatetime());
		CommonUtil.setParams(param);
				
		if(nR.getClaimSolveDate() == null || nR.getClaimSolveDate().equals("")){ //= 철회정보를 지마켓에 SEND
				
			notreceive = paGmktDeliveryService.selectNotReceiveListDetail(pMap);
						
			if(!(notreceive.getDelyNo() == null) && !"".equals(notreceive.getDelyNo())){
				param.put("ORDER_NO"		,notreceive.getContrNo());
				param.put("DELY_TYPE"		,"1");
				param.put("DELY_CODE"		,notreceive.getDelyCode());
				param.put("DELY_NO"			,notreceive.getDelyNo());
				param.put("REASON"			,"");
			}else{
				param.put("ORDER_NO"		,notreceive.getContrNo());
				param.put("DELY_TYPE"		,"2");
				param.put("DELY_CODE"		,"");
				param.put("DELY_NO"			,"");
				param.put("REASON"			,notreceive.getClaimCancelReason());
			}
			
			restUtil.getConnection(rest, param);
			
			pMap.put("claimSolveGb", "2");
			pMap.put("claimSolveDate", notreceive.getClaimCancelDate());

			
		}else{  //= 철회 대상이 아니기 때문에 우리쪽 데이터 Update
				
			pMap.put("claimSolveDate", nR.getClaimSolveDate());
			pMap.put("claimSolveGb"  , "1");
		}
			
		//= 통신의 결과로 받아온 데이터 Update TpagmktnotreceiveList 
		excuteResult = paGmktDeliveryService.updateUnReceiveListForCancle(pMap);  
		if(excuteResult != 1) throw processException("msg.cannot_save", new String[] { "PAGMKTNOTRECEIVELIST UPDATE" });
	}
	
	//= Information 지마켓 미수령 상태 조회로 얻은 RESPONSE를 VO로 리턴
	private PaGmkNotRecive setPaGmktNotReceiverList(Map<String, Object> orderMap) throws Exception{
		
		if(orderMap == null || orderMap.size() < 1) return null;
		
		HashMap<String, String> map = new HashMap<>();
		
		String sysdate = systemService.getSysdatetimeToString();
		String paCode  = orderMap.get("paCode").toString();
		String paGroupCode = orderMap.get("paGroupCode").toString();
		PaGmkNotRecive paNotRecive = null;
		//ParamMap param = new ParamMap();
		Map<String,String> paramMap = new HashMap<>();
		paNotRecive = new PaGmkNotRecive();
		
		paramMap.put("paCode" , paCode);
		paramMap.put("contrNo", orderMap.get("OrderNo").toString()); 
		map =  paGmktDeliveryService.selectTpaOrdermForNotReceive(paramMap);
			
		paNotRecive.setPayNo(map.get("PAY_NO"));
		paNotRecive.setPaCode(paCode);
		paNotRecive.setPaGroupCode(paGroupCode);
		paNotRecive.setOrderNo(map.get("ORDER_NO"));
		paNotRecive.setOrderGSeq(map.get("ORDER_G_SEQ"));
		paNotRecive.setOrderDSeq(map.get("ORDER_W_SEQ"));
		paNotRecive.setContrNo(orderMap.get("OrderNo").toString());				//주문번호 (Contr_No)

		paNotRecive.setSeq(systemService.getMaxNo("tpagmktnotreceivelist","SEQ", "PA_CODE = '" + paCode  + "' AND "
		+ "CONTR_NO = '" + orderMap.get("OrderNo").toString() + "' " , 3));
			
		
		paNotRecive.setTransYn("0");
		
		if(orderMap.get("ClaimSolveDate") != null && !orderMap.get("ClaimSolveDate").equals("")){
			paNotRecive.setPreCanYn("1");
			paNotRecive.setClaimSolveDate(PaGmktDateUtil.toTimestamp(orderMap.get("ClaimSolveDate").toString()));
		}else{
			paNotRecive.setPreCanYn("0");
			paNotRecive.setClaimSolveDate(null);
		}
			
		paNotRecive.setClaimDate(PaGmktDateUtil.toTimestamp(orderMap.get("ClaimDate").toString()));
		paNotRecive.setClaimReason(orderMap.get("ClaimReason").toString());
		paNotRecive.setClaimReasonDetail(ComUtil.NVL(orderMap.get("ClaimDetailReason")).toString());
		
		paNotRecive.setModifyId(orderMap.get("siteGb").toString());
		paNotRecive.setModifyDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		paNotRecive.setInsertId(orderMap.get("siteGb").toString());
		paNotRecive.setInsertDate(DateUtil.toTimestamp(sysdate,  "yyyy/MM/dd HH:mm:ss"));
		
		return paNotRecive;
	}
		
}
