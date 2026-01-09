package com.cware.api.passg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaSsgOrderListVO;
import com.cware.netshopping.passg.delivery.service.PaSsgDeliveryService;
import com.cware.netshopping.passg.util.PaSsgComUtill;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/passg/delivery", description="주문")
@Controller
@RequestMapping(value = "/passg/delivery")
public class PaSsgDeliveryController extends AbstractController  {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PaSsgConnectUtil paSsgConnectUtil;
	
	@Autowired
	private PaSsgDeliveryService paSsgDeliveryService;
	
	@Autowired
	private PaSsgAsyncController paSsgAsyncController;
	
	/**
	 * SSG 배송지시 목록조회
	 * @param request
	 * @param perdType
	 * @param fromDate
	 * @param toDate
	 * @param orderGb
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송지시 목록조회", notes = "10:정상 / 30:대기", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-list/{orderGb}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderList(HttpServletRequest request,
			@ApiParam(name = "perdType", value = "기간타입",  defaultValue = "") @RequestParam(value = "perdType", required = false, defaultValue = "01") String perdType, //01:배송지시일, 02:주문완료일, 03:출고예정일
			@ApiParam(name = "fromDate", value = "기간시작일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate,
			@PathVariable("orderGb") String orderGb //10:배송, 40:교환배송
			) throws Exception {
		
		List<Map<String, Object>> orderList   = null;
		List<Map<String, Object>> apiResult   = new ArrayList<Map<String, Object>>();
		Map<String, Object>       map  	      = new HashMap<String, Object>();
		Map<String, Object>    	  mapData     = new HashMap<String, Object>();
		ParamMap 			   	  apiInfoMap  = new ParamMap();
		ParamMap 			   	  apiDataMap  = new ParamMap();
		String 				   	  prg_id 	  = "";
		String 				   	  rtnMsg 	  = "";
		String 				   	  paCode	  = "";
		
		try {
			toDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString(); // 조회종료일
			fromDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -7, DateUtil.GENERAL_DATE_FORMAT); // 조회시작일
			
			prg_id = "10".equals(orderGb) ? "IF_PASSGAPI_02_001" : "IF_PASSGAPI_02_015";
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			mapData.put("perdType",   perdType);				// 기간타입
			mapData.put("perdStrDts", fromDate);				// 기간시작일
			mapData.put("perdEndDts", toDate);					// 기간종료일
			if("10".equals(orderGb)) {
				mapData.put("shppStatCd", "10");					// 배송상태코드( 10:정상  30:대기 )
			}else {
				mapData.put("shppStatCd", "30");
				mapData.put("shppDivDtlCd", "15");				
			}
			apiDataMap.put("requestShppDirection", mapData);	// OBJECT
			
			for(int i = 0; i < Constants.PA_SSG_CONTRACT_CNT; i++) {
				orderList = new ArrayList<Map<String, Object>>();
				
				paCode = (i == 0) ? Constants.PA_SSG_BROAD_CODE : Constants.PA_SSG_ONLINE_CODE;
				rtnMsg += paCode + ":";
						
				apiInfoMap.put("paCode", paCode);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)map.get("result")).get("resultMessage").toString())) {
					apiInfoMap.put("code", "500");
					rtnMsg += ((Map<String, Object>)map.get("result")).get("resultDesc") + " | ";
					continue;
				}
				
				apiResult = (List<Map<String, Object>>) ((Map<String, Object>)map.get("result")).get("shppDirections");
				if(String.valueOf(apiResult.get(0)).equals("")) continue;	// 주문 목록 없을 시, String을 반환해주므로 이를 위한 처리
				
				if(apiResult.get(0).get("shppDirection") instanceof Map<?, ?>) { // 목록이 하나일 경우 List가 아닌 Map 형식으로 넘겨주므로 형변환 오류 방지를 위함
					orderList.add((Map<String, Object>) apiResult.get(0).get("shppDirection"));
				} else {
					orderList = (List<Map<String, Object>>)apiResult.get(0).get("shppDirection");
				}
				
				for(Map<String, Object> orderListMap : orderList) {
					try {
						orderListMap.put("paCode", paCode);

						if("10".equals(orderGb) && !"15".equals(ComUtil.objToStr(orderListMap.get("shppDivDtlCd")))) {
							orderConfirm(request, orderListMap, orderGb);
						}else {
							orderListMap.put("paOrderGb", "40");
								
							PaSsgOrderListVO vo = new PaSsgOrderListVO();
							vo = (PaSsgOrderListVO) PaSsgComUtill.map2VO(orderListMap, PaSsgOrderListVO.class);
								
							paSsgDeliveryService.saveSsgChangeOrderListTx(vo);
							
						}
					} catch (Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "SSG주문번호 : " + orderListMap.get("ordNo") + " - " + PaSsgComUtill.getErrorMessage(e) + "\n";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		if("40".equals(orderGb)) {
			orderConfirm(request, null, orderGb);
		}
		
		listWarehouseOut(request, "01", null, null); // 조회 기간 설정이 다르므로 null
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	
	/**
	 * SSG 주문확인처리 API
	 * @param request
	 * @param orderListMap
	 * @param orderGb
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "주문확인처리", notes = "주문확인처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/order-confirm", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> orderConfirm(HttpServletRequest request, Map<String, Object> orderListMap, String orderGb) throws Exception {
		
		Map<String, Object> requestOrderSubjectManage = new HashMap<String, Object>();
		Map<String, Object> resultMap   = new HashMap<String, Object>();
		ParamMap 			apiDataMap  = new ParamMap();
		ParamMap 			apiInfoMap  = new ParamMap();
		ParamMap 			paramMap  	= null;
		String 	 			prg_id 	    = "IF_PASSGAPI_02_002";
		String   			rtnMsg	    = "";
		int					executedRtn = 0;
		List<Map<String, Object>> orderConfirmList = new ArrayList<Map<String,Object>>();
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			if("40".equals(orderGb)) {//교환배송
				orderConfirmList = paSsgDeliveryService.selectPaSsgChangeConfirmList();
			    PaSsgComUtill.replaceCamelList(orderConfirmList);
			}else {
				orderConfirmList.add(orderListMap);
			}
			
			for(Map<String, Object> orderMap : orderConfirmList) {
				try {
					apiInfoMap.put("paCode", orderMap.get("paCode"));
					
					paramMap = new ParamMap();
					paramMap.put("mappingSeq" , orderMap.get("mappingSeq"));
					paramMap.put("paCode"	  , orderMap.get("paCode"));
					paramMap.put("paDoFlagOrg", "10");
					
					requestOrderSubjectManage.put("shppNo", orderMap.get("shppNo"));
					requestOrderSubjectManage.put("shppSeq", orderMap.get("shppSeq"));
					apiDataMap.put("requestOrderSubjectManage", requestOrderSubjectManage);
					
					resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					
					if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
						apiInfoMap.put("code", "500");
						rtnMsg += "SHPP_NO:"+orderMap.get("shppNo") + " SHPP_SEQ:"+ orderMap.get("shppSeq") + " :: " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc");
						continue;
					} 
					
					if("40".equals(orderGb)) {
						paramMap.put("paOrderGb", "40");
						executedRtn = updatePaOrderMDoFlag(paramMap, "20", "주문확인(교환배송)", Constants.PA_SSG_SUCCESS_CODE);
				        if(executedRtn < 1){
				        	rtnMsg += "SHPP_NO:"+orderMap.get("shppNo") + " SHPP_SEQ:"+ orderMap.get("shppSeq") + " TPAORDERM UPDATE Fail : updatePaOrderMDoFlag/";
				            apiInfoMap.put("code", "500");
				        }
					}
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "SHPP_NO:"+orderMap.get("shppNo") + " SHPP_SEQ:"+ orderMap.get("shppSeq") + " :: " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc");
				}
			}
			
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * SSG 출고대상 목록조회 API
	 * @param request
	 * @param perdType
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고대상 목록조회", notes = "출고대상 목록조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/out-shipping-list", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> listWarehouseOut(HttpServletRequest request,
			@ApiParam(name = "perdType", value = "기간타입",  defaultValue = "") @RequestParam(value = "perdType", required = false, defaultValue = "01") String perdType, //01 주문확인일, 02 배송지시일일, 03 주문완료일, 04 출고예정일
			@ApiParam(name = "fromDate", value = "기간시작일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name = "toDate",   value = "기간종료일[YYYYMMDD]", defaultValue = "") @RequestParam(value = "toDate",   required = false) String toDate) throws Exception {
		
		List<Map<String, Object>> apiResult = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderList = null;
		Map<String, Object> mapData   = new HashMap<String, Object>();
		Map<String, Object> map  	  = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		ParamMap apiDataMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_005";
		String   rtnMsg		 = "";
		String   paCode 	 = "";
		
		try {
			toDate   = ComUtil.NVL(toDate).length()   == 8 ? toDate   : DateUtil.getCurrentDateAsString(); // 조회종료일
			fromDate = ComUtil.NVL(fromDate).length() == 8 ? fromDate : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT); // 조회시작일
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			mapData.put("perdType",   perdType);			// 기간타입
			mapData.put("perdStrDts", fromDate);			// 기간시작일
			mapData.put("perdEndDts", toDate);				// 기간종료일
			apiDataMap.put("requestWarehouseOut", mapData);	// OBJECT
			
			for(int i = 0; i < Constants.PA_SSG_CONTRACT_CNT; i++) {
				orderList = new ArrayList<Map<String, Object>>();
				
				paCode = (i == 0) ? Constants.PA_SSG_BROAD_CODE : Constants.PA_SSG_ONLINE_CODE;
				rtnMsg += paCode + ":";
				
				apiInfoMap.put("paCode", paCode);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)map.get("result")).get("resultMessage").toString())) {
					apiInfoMap.put("code", "500");
					rtnMsg += ((Map<String, Object>)map.get("result")).get("resultDesc") + " | ";
					continue;
				}
				
				apiResult = (List<Map<String, Object>>) ((Map<String, Object>)map.get("result")).get("warehouseOuts");
				if(String.valueOf(apiResult.get(0)).equals("")) continue;
				
				if(apiResult.get(0).containsKey("warehouseOut")) {
					if(apiResult.get(0).get("warehouseOut") instanceof Map<?, ?>) {
						orderList.add((Map<String, Object>) apiResult.get(0).get("warehouseOut"));
					} else {
						orderList = (List<Map<String, Object>>)apiResult.get(0).get("warehouseOut");
					}
				}
				
				for(Map<String, Object> orderListMap : orderList) {
					try {
						orderListMap.put("paCode", paCode);
						orderListMap.put("paOrderGb", "10");
						
						PaSsgOrderListVO vo = new PaSsgOrderListVO();
						vo = (PaSsgOrderListVO) PaSsgComUtill.map2VO(orderListMap, PaSsgOrderListVO.class);
						
						paSsgDeliveryService.saveSsgOrderListTx(vo);
					} catch(Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "SSG주문번호 : " + orderListMap.get("orordNo") + " - " + PaSsgComUtill.getErrorMessage(e) + "\n";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderInputMain(request);
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id = "PASSG_ORDER_INPUT";
		String duplicateCheck = "";
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start", prg_id);
			if("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			
			//여러 곳 한번에 주문으로 주문 시 1개 pa_order_no에 여러개의 다른 주소 생성 가능하여 주소별로 다르게 sk주문 생성 
			List<Map<String, String>> orderInputTargetList = paSsgDeliveryService.selectOrderInputTargetList(limitCount);
			
			for(Map<String, String> order : orderInputTargetList) {
				try {
					paSsgAsyncController.orderInputAsync(order, request);
				} catch(Exception e) {
					log.error(prg_id + " - 주문 내역 생성 오류" + order.get("PA_ORDER_NO"), e);
				}
			}
		} catch(Exception e) {
			log.error("error msg : " + e.getMessage());
		} finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	
	private int updatePaOrderMDoFlag(ParamMap paramMap, String doFlag, String resultMessage, String resultCode) throws Exception {
		
		int executedRtn = 0;
		
		paramMap.put("PA_DO_FLAG", doFlag);
		paramMap.put("API_RESULT_CODE", resultCode);
		paramMap.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paSsgDeliveryService.updatePaOrderMDoFlag(paramMap);
		
		return executedRtn;
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "부분출고", notes = "부분출고", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/partial-release-proc", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> partialReleaseProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_008";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> partialShippingList = paSsgDeliveryService.selectPartialSlipList();
			
			String compareGoodsCode = "";
			int paShipCnt = 0;
			int slipNoCnt = 0;
			for(int i = 0; i < partialShippingList.size(); i++) {
				try {
					apiInfoMap.put("paCode", partialShippingList.get(i).get("PA_CODE").toString());
					
					ParamMap paramMap = new ParamMap();
					ParamMap apiDataMap = new ParamMap();
					//부분출고대상여부
					String potionYn = "0";
					if("".equals(compareGoodsCode) || !compareGoodsCode.equals(partialShippingList.get(i).get("SHPP_NO").toString())) {
						compareGoodsCode = partialShippingList.get(i).get("SHPP_NO").toString();
						paShipCnt = Integer.parseInt(String.valueOf(partialShippingList.get(i).get("PA_SHIP_CNT")));
						slipNoCnt = Integer.parseInt(String.valueOf(partialShippingList.get(i).get("SLIP_NO_CNT")));
					} else {
						slipNoCnt += Integer.parseInt(String.valueOf(partialShippingList.get(i).get("SLIP_NO_CNT")));
					}
					
					if(slipNoCnt < paShipCnt) {
						potionYn = "1";
					}
					
					//부분출고면
					if("1".equals(potionYn)) {
						Map<String, Object> requestWhOutCompleteProcess = new HashMap<String, Object>();
						
						paramMap.put("paCode", partialShippingList.get(i).get("PA_CODE").toString());
						paramMap.put("paOrderNo", partialShippingList.get(i).get("ORORD_NO").toString());
						paramMap.put("paOrderSeq", partialShippingList.get(i).get("ORORD_ITEM_SEQ").toString());
						paramMap.put("mappingSeq", partialShippingList.get(i).get("MAPPING_SEQ").toString());
						paramMap.put("paHoldYn", "1");
						
						requestWhOutCompleteProcess.put("shppNo", partialShippingList.get(i).get("SHPP_NO").toString());			// 배송번호
						requestWhOutCompleteProcess.put("shppSeq", partialShippingList.get(i).get("SHPP_SEQ").toString());			// 배송순번
						requestWhOutCompleteProcess.put("procItemQty", Integer.parseInt(String.valueOf(partialShippingList.get(i).get("PA_PROC_QTY"))));	// 처리수량
						apiDataMap.put("requestWhOutCompleteProcess", requestWhOutCompleteProcess);
						
						resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
						if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
							apiInfoMap.put("code", "500");
							rtnMsg += paramMap.get("paOrderNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
						} else {
												
							executedRtn = paSsgDeliveryService.updatePartialFlagTx(paramMap);
							if(executedRtn < 1) {
								apiInfoMap.put("code", "500");
								rtnMsg += paramMap.get("paOrderNo").toString() + " : TPAORDERM(FLAG) UPDATE Fail /";
							}
						}
					}
				} catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += partialShippingList.get(i).get("ORORD_NO").toString() + " : " + e.getMessage() + " / ";
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		//부분출고건 배송번호 조회
		listWarehouseOut4Partial(request);
		//운송장등록
		registrationWbINo(request);
		//출고처리
		slipOutProc(request);
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고대상 목록조회(부분출고)", notes = "출고대상 목록조회(부분출고)", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/partial-list", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> listWarehouseOut4Partial(HttpServletRequest request) throws Exception {
		
		List<Map<String, Object>> apiResult = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapData = null;
		Map<String, Object> map  	= null;
		ParamMap apiInfoMap  = new ParamMap();
		ParamMap apiDataMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_016";
		String   rtnMsg		 = "";
		String   paCode 	 = "";
		String	 paOrderNo	 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> partialDeliveryList = paSsgDeliveryService.selectPartialDeliveryList();
			
			for(int i = 0; i < partialDeliveryList.size(); i++) {
				mapData = new HashMap<String, Object>();
				map  	= new HashMap<String, Object>();
				
				Map<String, Object> partialDelivery = new HashMap<String, Object>();
				partialDelivery = partialDeliveryList.get(i);
				
				paCode = partialDelivery.get("PA_CODE").toString();
				paOrderNo = partialDelivery.get("ORORD_NO").toString();
				
				mapData.put("commType",  "01");		 // 조회타입 01 : [원주문번호]
				mapData.put("commValue", paOrderNo); // 원주문번호
				apiDataMap.put("requestWarehouseOut", mapData);	// OBJECT
				
				apiInfoMap.put("paCode", paCode);
				
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)map.get("result")).get("resultMessage").toString())) {
					apiInfoMap.put("code", "500");
					rtnMsg += "SSG주문번호 : " + paOrderNo;
					apiInfoMap.put("message", rtnMsg);
					continue;
				}
				
				apiResult = (List<Map<String, Object>>) ((Map<String, Object>)map.get("result")).get("warehouseOuts");
				
				if(String.valueOf(apiResult.get(0)).equals("")) {
					apiInfoMap.put("code", "500");
					rtnMsg += ((Map<String, Object>)map.get("result")).get("resultDesc") + " | ";
					apiInfoMap.put("message", rtnMsg);
					continue;
				} else {
					if(apiResult.get(0).containsKey("warehouseOut")) {
						if(apiResult.get(0).get("warehouseOut") instanceof Map<?, ?>) {
							orderList.add((Map<String, Object>) apiResult.get(0).get("warehouseOut"));
						} else {
							orderList = (List<Map<String, Object>>)apiResult.get(0).get("warehouseOut");
						}
					}
				}
				
				for(Map<String, Object> orderListMap : orderList) {
					try {
						PaSsgOrderListVO vo = new PaSsgOrderListVO();
						vo = (PaSsgOrderListVO) PaSsgComUtill.map2VO(orderListMap, PaSsgOrderListVO.class);
						
						if(vo.getOrordNo().equals(partialDelivery.get("ORORD_NO").toString()) && vo.getOrordItemSeq().equals(partialDelivery.get("ORORD_ITEM_SEQ").toString())) {
							if(!vo.getShppNo().equals(partialDelivery.get("SHPP_NO").toString()) || !vo.getShppSeq().equals(partialDelivery.get("SHPP_SEQ").toString())) {
								partialDelivery.put("PARTIAL_SHPP_NO", vo.getShppNo().toString());
								partialDelivery.put("PARTIAL_SHPP_SEQ", vo.getShppSeq().toString());
								
								executedRtn = paSsgDeliveryService.updatePartialShhpNoTx(partialDelivery);
								if(executedRtn < 1) {
									apiInfoMap.put("code", "500");
									rtnMsg += "SSG주문번호 : " + partialDelivery.get("ORORD_NO").toString();
									continue;
								}
							}
						}
					} catch(Exception e) {
						apiInfoMap.put("code", "500");
						rtnMsg += "SSG주문번호 : " + orderListMap.get("orordNo") + " - " + PaSsgComUtill.getErrorMessage(e) + "\n";
						apiInfoMap.put("message", rtnMsg);
						continue;
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 운송장등록
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "운송장등록", notes = "운송장등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/slipNo-insert", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> registrationWbINo(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_006";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> orderShippingList = paSsgDeliveryService.selectSlipOutProcList();
			
			for(int i = 0; i < orderShippingList.size(); i++) {
				Map<String, Object> requestWhOutCompleteProcess = new HashMap<String, Object>();
				
				ParamMap paramMap = new ParamMap();
				ParamMap apiDataMap = new ParamMap();
				
				apiInfoMap.put("paCode", orderShippingList.get(i).get("PA_CODE").toString());
				
				paramMap.put("paCode", orderShippingList.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", orderShippingList.get(i).get("ORORD_NO").toString());
				paramMap.put("paOrderSeq", orderShippingList.get(i).get("ORORD_ITEM_SEQ").toString());
				paramMap.put("mappingSeq", orderShippingList.get(i).get("MAPPING_SEQ").toString());
				paramMap.put("paOrderGb", orderShippingList.get(i).get("PA_ORDER_GB").toString());
				paramMap.put("paDoFlagOrg", "30");
				paramMap.put("paDoFlag", "35");
				
				requestWhOutCompleteProcess.put("shppNo", orderShippingList.get(i).get("SHPP_NO").toString());			//배송번호
				requestWhOutCompleteProcess.put("shppSeq", orderShippingList.get(i).get("SHPP_SEQ").toString());
				
				if("0000033028".equals(ComUtil.objToStr(orderShippingList.get(i).get("PA_DELY_GB")))) { //자체배송
					requestWhOutCompleteProcess.put("shppTypeCd", "10");									
					requestWhOutCompleteProcess.put("shppTypeDtlCd", "14");
				} else {
					requestWhOutCompleteProcess.put("wblNo", orderShippingList.get(i).get("SLIP_NO").toString().trim());			//운송장번호
					requestWhOutCompleteProcess.put("delicoVenId", orderShippingList.get(i).get("PA_DELY_GB").toString());	//택배사
					requestWhOutCompleteProcess.put("shppTypeCd", "20");									//배송유형 : 20[택배배송], 10 [자사배송]
					requestWhOutCompleteProcess.put("shppTypeDtlCd", "22");					 				//배송유형 : 22[업체택배배송], 14 [업체자사배송
				}

				apiDataMap.put("requestWhOutCompleteProcess", requestWhOutCompleteProcess);
				
				resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
					
					if(((Map<String, Object>)resultMap.get("result")).get("resultDesc").toString().contains("운송장 번호가 유효하지 않습니다") ||
					   ((Map<String, Object>)resultMap.get("result")).get("resultDesc").toString().contains("기등록된 운송장번호입니다")) { // 자체 배송처리 경우 택배사코드, 운송장 번호 입력시 오류 던져줌
						requestWhOutCompleteProcess = new HashMap<String, Object>();
						
						requestWhOutCompleteProcess.put("shppNo", orderShippingList.get(i).get("SHPP_NO").toString());			//배송번호
						requestWhOutCompleteProcess.put("shppSeq", orderShippingList.get(i).get("SHPP_SEQ").toString());		//배송순번
						requestWhOutCompleteProcess.put("shppTypeCd", "10");									//배송유형 : 20[택배배송], 10 [자사배송]
						requestWhOutCompleteProcess.put("shppTypeDtlCd", "14"); 								//배송유형 : 22[업체택배배송], 14 [업체자사배송]
						apiDataMap.put("requestWhOutCompleteProcess", requestWhOutCompleteProcess);
						resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
						if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
							apiInfoMap.put("code", "500");
							rtnMsg += paramMap.get("paOrderNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
							continue;
						} else {
							executedRtn = updatePaOrderMDoFlag(paramMap, paramMap.get("paDoFlag").toString(), "운송장등록 성공", Constants.SAVE_SUCCESS);
							if(executedRtn < 1) {
								apiInfoMap.put("code", "500");
								rtnMsg += paramMap.get("paOrderNo").toString() + " : TPAORDERM(pa_do_flag) UPDATE Fail/";
							}
						}
					} else {
						apiInfoMap.put("code", "500");
						rtnMsg += paramMap.get("paOrderNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
						continue;
					}
				} else {
					executedRtn = updatePaOrderMDoFlag(paramMap, paramMap.get("paDoFlag").toString(), "운송장등록 성공", Constants.SAVE_SUCCESS);
					if(executedRtn < 1) {
						apiInfoMap.put("code", "500");
						rtnMsg += paramMap.get("paOrderNo").toString() + " : TPAORDERM(pa_do_flag) UPDATE Fail/";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 출고처리(부분 출고처리 포함)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고처리", notes = "출고처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_009";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> releaseOrderList = paSsgDeliveryService.selectReleaseOrderList();
			
			for(int i = 0; i < releaseOrderList.size(); i++) {
				Map<String, Object> requestWhOutCompleteProcess = new HashMap<String, Object>();
				
				ParamMap paramMap = new ParamMap();
				ParamMap apiDataMap = new ParamMap();
				
				apiInfoMap.put("paCode", releaseOrderList.get(i).get("PA_CODE").toString());
				
				paramMap.put("paCode", releaseOrderList.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", releaseOrderList.get(i).get("ORORD_NO").toString());
				paramMap.put("paOrderSeq", releaseOrderList.get(i).get("ORORD_ITEM_SEQ").toString());
				paramMap.put("mappingSeq", releaseOrderList.get(i).get("MAPPING_SEQ").toString());
				paramMap.put("paOrderGb", releaseOrderList.get(i).get("PA_ORDER_GB").toString());
				paramMap.put("paDoFlagOrg", "35");
				paramMap.put("paDoFlag", "40");
				
				requestWhOutCompleteProcess.put("shppNo", releaseOrderList.get(i).get("SHPP_NO").toString());			// 배송번호
				requestWhOutCompleteProcess.put("shppSeq", releaseOrderList.get(i).get("SHPP_SEQ").toString());			// 배송순번
				requestWhOutCompleteProcess.put("procItemQty", releaseOrderList.get(i).get("PA_PROC_QTY").toString());	// 처리수량
				apiDataMap.put("requestWhOutCompleteProcess", requestWhOutCompleteProcess);
				
				resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
					apiInfoMap.put("code", "500");
					rtnMsg += paramMap.get("paOrderNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
					continue;
				} else {
					executedRtn = updatePaOrderMDoFlag(paramMap, "40", "출고처리", Constants.SAVE_SUCCESS);
					if(executedRtn < 1) {
						apiInfoMap.put("code", "500");
						rtnMsg += paramMap.get("paOrderNo").toString() + " : TPAORDERM(pa_do_flag) UPDATE Fail/";
						continue;
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송완료처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송완료처리", notes = "배송완료처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/order-complete-proc", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> orderCmpProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_011";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> completeOrderList = paSsgDeliveryService.selectOrderCompleteList();
			
			for(int i = 0; i < completeOrderList.size(); i++) {
				Map<String, Object> requestDeliveryEnd = new HashMap<String, Object>();
				
				ParamMap paramMap = new ParamMap();
				ParamMap apiDataMap = new ParamMap();
				
				apiInfoMap.put("paCode", completeOrderList.get(i).get("PA_CODE").toString());
				
				paramMap.put("paCode", completeOrderList.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", completeOrderList.get(i).get("ORORD_NO").toString());
				paramMap.put("paOrderSeq", completeOrderList.get(i).get("ORORD_ITEM_SEQ").toString());
				paramMap.put("mappingSeq", completeOrderList.get(i).get("MAPPING_SEQ").toString());
				paramMap.put("paOrderGb", completeOrderList.get(i).get("PA_ORDER_GB").toString());
				paramMap.put("paDoFlagOrg", "40");
				paramMap.put("paDoFlag", "80");
				
				requestDeliveryEnd.put("shppNo", completeOrderList.get(i).get("PA_SHIP_NO").toString());	// 배송번호
				requestDeliveryEnd.put("shppSeq", completeOrderList.get(i).get("PA_SHIP_SEQ").toString());	// 배송순번
				apiDataMap.put("requestDeliveryEnd", requestDeliveryEnd);
				
				resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString()) && 
				   !((Map<String, Object>)resultMap.get("result")).get("resultDesc").toString().contains("이미 배송완료 된 배송번호")) {
					apiInfoMap.put("code", "500");
					rtnMsg += paramMap.get("paOrderNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
					continue;
				} else {
					executedRtn = updatePaOrderMDoFlag(paramMap, "80", "배송완료", Constants.SAVE_SUCCESS);
					if(executedRtn < 1) {
						apiInfoMap.put("code", "500");
						rtnMsg += paramMap.get("paOrderNo").toString() + " : TPAORDERM(pa_do_flag) UPDATE Fail/";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 출고지연처리
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "출고지연처리", notes = "출고지연처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류") })
	@RequestMapping(value = "/shipping-delay", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> shippingDelayProc(HttpServletRequest request) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap  = new ParamMap();
		String 	 prg_id 	 = "IF_PASSGAPI_02_003";
		String   rtnMsg		 = "";
		int		 executedRtn = 0;
		
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			List<Map<String, Object>> delayOrderList = paSsgDeliveryService.selectDelayOrderList();
			
			for(int i = 0; i < delayOrderList.size(); i++) {
				
				try {
					Map<String, Object> requestShppingDelay = new HashMap<String, Object>();
					ParamMap paramMap   = new ParamMap();
					ParamMap apiDataMap = new ParamMap();
					
					apiInfoMap.put("paCode", delayOrderList.get(i).get("PA_CODE").toString());
					
					paramMap.put("paCode", 	     delayOrderList.get(i).get("PA_CODE").toString());
					paramMap.put("orordNo", 	 delayOrderList.get(i).get("PA_ORDER_NO").toString());
					paramMap.put("orordItemSeq", delayOrderList.get(i).get("PA_ORDER_SEQ").toString());
					paramMap.put("shppNo", 	     delayOrderList.get(i).get("SHPP_NO").toString());
					paramMap.put("shppSeq",      delayOrderList.get(i).get("SHPP_SEQ").toString());
					paramMap.put("whinExpcDt",   delayOrderList.get(i).get("WHOUT_CRITN_DT").toString());
					
					requestShppingDelay.put("shppNo",  delayOrderList.get(i).get("SHPP_NO").toString());	// 배송번호
					requestShppingDelay.put("shppSeq", delayOrderList.get(i).get("SHPP_SEQ").toString());	// 배송순번
					requestShppingDelay.put("shortgRsnCd", "01");	// 출고지연사유코드 : 01[공급지연/출고지연]  03[제작상품]
					requestShppingDelay.put("whinExpcDt", delayOrderList.get(i).get("WHOUT_CRITN_DT").toString()); //출고예정일
					requestShppingDelay.put("itemId", delayOrderList.get(i).get("ITEM_ID").toString());		// 상품번호
					apiDataMap.put("requestShppingDelay", requestShppingDelay);
					
					resultMap = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					
					if(!"SUCCESS".equals(((Map<String, Object>)resultMap.get("result")).get("resultMessage").toString())) {
						apiInfoMap.put("code", "500");
						rtnMsg += paramMap.get("orordNo").toString() + " : " + ((Map<String, Object>)resultMap.get("result")).get("resultDesc") + " / ";
						continue;
					} else {
						paramMap.put("dateTime", DateUtil.getCurrentDateAsString());
						executedRtn = paSsgDeliveryService.updatePaSsgWhinExpcDtTx(paramMap);
						if(executedRtn < 1) {
							apiInfoMap.put("code", "500");
							rtnMsg += paramMap.get("paOrderNo").toString() + " : TPASSGORDERLIST(whin_expc_dt),TPAORDERM UPDATE Fail/";
							continue;
						}
					}
				} catch (Exception e) {
					rtnMsg += delayOrderList.get(i).get("PA_ORDER_NO").toString() + PaSsgComUtill.getErrorMessage(e)+"/";
					apiInfoMap.put("code", "500");
				}
			}
			apiInfoMap.put("message", rtnMsg);
		} catch(Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}