package com.cware.api.patmon.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.AESCrypto;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTmonOrderListVO;
import com.cware.netshopping.pacommon.order.service.PaOrderService;
import com.cware.netshopping.patmon.delivery.service.PaTmonDeliveryService;
import com.cware.netshopping.patmon.util.PaTmonComUtill;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/delivery", description="주문")
@Controller
@RequestMapping(value = "/patmon/delivery")
public class PaTmonDeliveryController extends AbstractController  {
	
	@Autowired
	private PaTmonDeliveryService paTmonDeliveryService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private PaTmonAsyncController paTmonAsyncController;
	@Autowired
	private PaTmonConnectUtil paTmonConnectUtil;
	@Autowired
	private PaOrderService paOrderService;
	
	private transient static Logger log = LoggerFactory.getLogger(PaTmonDeliveryController.class);

	/**
	 * 배송 대상 수집 API
	 * @param fromDate
	 * @param toDate
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "배송 대상 수집", notes = "배송 대상 수집", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
	@ApiResponse(code = 500, message = "시스템 오류")})
	@RequestMapping(value = "/order-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseMsg> orderList(HttpServletRequest request,
			@ApiParam(name="fromDate", 	 value="시작일", 	defaultValue = "") @RequestParam(value = "fromDate", required = false) String fromDate,
			@ApiParam(name="toDate", 	 value="종료일", 	defaultValue = "") @RequestParam(value = "toDate", required = false) String toDate
			) throws Exception {
		
		Map<String, Object> map 				= new HashMap<String, Object>() ;
		List<Map<String, Object>> orderList 	= new ArrayList<Map<String,Object>>();	
		List<PaTmonOrderListVO>	paTmonOrderList	= null;
		String paCode							= "";
		String prg_id 							= "IF_PATMONAPI_03_001";
		ParamMap apiInfoMap						= new ParamMap();
		String deliveryStatus 					= "";
		String dateTime 						= systemService.getSysdatetimeToString();
		String rtnMsg							= "";
		
		try {
			
			ParamMap retrieveDate = paTmonConnectUtil.getRetrieveDate(fromDate, toDate, "1");
			fromDate  =  retrieveDate.getString("FROM_DATE");
			toDate = retrieveDate.getString("TO_DATE");
			
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();		
			
			for(int i = 0; i < 2; i++) {
				deliveryStatus = (i==0 )? "D1" : "D2";
				rtnMsg += deliveryStatus+"::";
				for(int j = 0; j < Constants.PA_TMON_CONTRACT_CNT ; j++) {
					paCode = (j==0 )? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
					rtnMsg += paCode + ":";
					
					apiInfoMap.put("paCode", paCode);
					
					apiInfoMap.put("url", url.replace("{startDate}", fromDate).replace("{endDate}", toDate).replace("{deliveryStatus}", deliveryStatus));
					
					map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
					
					if(!"null".equals(String.valueOf(map.get("error")))){
						apiInfoMap.put("code", "500");
						rtnMsg += map.get("error")+"/";
						continue;
					}
					
					if(((List<Map<String, Object>>)(map.get("Data"))).size() < 1) {
						apiInfoMap.put("code", "404");
						rtnMsg += getMessage("pa.not_exists_process_list")+"/";
						continue;
					}
					
					orderList = (List<Map<String, Object>>)(map.get("Data"));
					
					for(Map<String, Object> m : orderList) {
						try {
							paTmonOrderList = new ArrayList<PaTmonOrderListVO>();
							m.put("paCode", paCode);
							m.put("paOrderGb", "10");
							
							PaTmonOrderListVO vo 	= new PaTmonOrderListVO();
							
							//= 제휴 테이블에 데이터를 저장 (TPATMONORDERLIST, TPAORDERM) 
							vo  = (PaTmonOrderListVO) PaTmonComUtill.map2VO( m , PaTmonOrderListVO.class);
							
							vo.setInsertId(Constants.PA_TMON_PROC_ID);
							vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							paTmonOrderList = makeOrderList(vo);
							
							paTmonDeliveryService.saveTmonOrderListTx(paTmonOrderList);
						}catch (Exception e) {
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_ORDER_NO:"+m.get("tmonOrderNo").toString() +" "+PaTmonComUtill.getErrorMessage(e)+"/";
						}
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		orderConfirm(request); //do_flag 10인 주문 대상, 승인 후 do_flag 20 변경
		orderInitial(request); // 승인된 주문 조회 후 배송비, 프로모션 적용 정보 받아 do_flag 30으로 변경
		orderInputMain(request); // *** 재고 부족으로 인한 즉시 취소 처리 시 데이터 만들기 생각해봐야함 ( 배송번호는 딜 단위로 변경되는데 request에서 배송번호와 List딜 요구함 ) *** 
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 배송 대상 확인 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	
	@ApiOperation(value = "배송 대상 확인", notes = "배송 대상 확인", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/order-confirm", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderConfirm(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "IF_PATMONAPI_03_002";
		String				rtnMsg			= "";
		int					executedRtn		= 0;
		List<Object> dealOptionsList = null;
		Map<String,Object> dealOptions = null;
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> orderReadylist =  paTmonDeliveryService.selectDeliveryReadyList();
			for(int i=0; i<orderReadylist.size(); i++) {
				ParamMap paramMap	= new ParamMap();
				ParamMap apiDataMap	= new ParamMap();
				dealOptionsList = new ArrayList<Object>();
				
				paramMap.put("paCode", orderReadylist.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", orderReadylist.get(i).get("PA_ORDER_NO").toString());
				paramMap.put("paShipNo", orderReadylist.get(i).get("PA_SHIP_NO").toString());
				paramMap.put("tmonDealNo", orderReadylist.get(i).get("TMON_DEAL_NO").toString());
				paramMap.put("paDoFlagOrg", "10");

				apiInfoMap.put("paCode",orderReadylist.get(i).get("PA_CODE").toString());
				apiInfoMap.put("url",url.replace("{tmonOrderNo}", orderReadylist.get(i).get("PA_ORDER_NO").toString()));
				apiDataMap.put("tmonDealNo", orderReadylist.get(i).get("TMON_DEAL_NO").toString());
				
				List<Map<String, Object>> orderReadyDetaillist =  paTmonDeliveryService.selectDeliveryReadyDetailList(paramMap);
				for(int j=0; j<orderReadyDetaillist.size(); j++) {
					dealOptions = new HashMap<String, Object>();
					dealOptions.put("tmonDealOptionNo", orderReadyDetaillist.get(j).get("TMON_DEAL_OPTION_NO").toString());
					dealOptions.put("qty", ComUtil.objToLong(orderReadyDetaillist.get(j).get("QTY")));
					dealOptionsList.add(dealOptions);					
				}
				apiDataMap.put("tmonDealOptions", dealOptionsList);				
				
				resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
				
				if(!"null".equals(String.valueOf(resultMap.get("error")))){
					apiInfoMap.put("code", "500");
					rtnMsg += "배송대상확인 실패 :: PA_ORDER_NO:"+ paramMap.get("paOrderNo").toString() +" "+ String.valueOf(resultMap.get("error")+"/");
				} else {					
					executedRtn = updatePaOrderMDoFlag(paramMap, "20", "승인처리", "000000");
					if(executedRtn < 1){
						apiInfoMap.put("code", "500");
						rtnMsg +="PA_ORDER_NO:" + paramMap.get("paOrderNo").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail/";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 주문번호 최초정보 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "주문번호 최초정보 조회", notes = "주문번호 최초정보 조회", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/order-initial", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> orderInitial(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "IF_PATMONAPI_03_004";
		String				executedRtn		= "";	
		String				rtnMsg			= "";
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> orderConfirmlist =  paTmonDeliveryService.selectDeliveryConfirmList();
			for(int i=0; i<orderConfirmlist.size(); i++) {
				try {
					apiInfoMap.put("paCode",orderConfirmlist.get(i).get("PA_CODE").toString());
					apiInfoMap.put("url",url.replace("{tmonOrderNo}", orderConfirmlist.get(i).get("PA_ORDER_NO").toString()));
					
					resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
					
					if(!"null".equals(String.valueOf(resultMap.get("error")))){
						apiInfoMap.put("code", "500");
						rtnMsg += "PA_ORDER_NO:"+orderConfirmlist.get(i).get("PA_ORDER_NO").toString() +" "+ String.valueOf(resultMap.get("error"))+"/";
					} else {
						
						resultMap.put("paCode", orderConfirmlist.get(i).get("PA_CODE").toString());
						resultMap.put("paOrderGb", "10");
						resultMap.put("tmonOrderNo", orderConfirmlist.get(i).get("PA_ORDER_NO").toString());
						
						PaTmonOrderListVO vo 	= new PaTmonOrderListVO();
						vo  = (PaTmonOrderListVO) PaTmonComUtill.map2VO(resultMap, PaTmonOrderListVO.class);
						
						executedRtn = paTmonDeliveryService.updatePaTmonOrderListInitialTx(vo);
							
						if(!executedRtn.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code", "500");
							rtnMsg += "PA_ORDER_NO:"+orderConfirmlist.get(i).get("PA_ORDER_NO").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail"+"/";
						}
					}
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "PA_ORDER_NO:"+orderConfirmlist.get(i).get("PA_ORDER_NO").toString() + " "+e.getMessage()+"/";
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@ApiOperation(value = "주문번호로 배송 대상 조회", notes = "배송완료처리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
	@ApiResponse(code = 200, message = "OK"), 
	@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/delivery-complete", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deliveryComplete(HttpServletRequest request,
			@ApiParam(name="searchProcDate", value="조회기준일", defaultValue = "15") @RequestParam(value = "searchProcDate", required = false, defaultValue = "15") int searchProcDate) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "IF_PATMONAPI_03_005";
		String				executedRtn		= "";	
		String				rtnMsg			= "";
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> deliveryCompletelist =  paTmonDeliveryService.selectDeliveryCompleteList(searchProcDate);
			
			for(int i=0; i<deliveryCompletelist.size(); i++) {
				try {
					
					apiInfoMap.put("paCode",deliveryCompletelist.get(i).get("PA_CODE").toString());
					apiInfoMap.put("url",url.replace("{tmonOrderNo}", deliveryCompletelist.get(i).get("PA_ORDER_NO").toString()));
					
					resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, null);
					
					if(!"null".equals(String.valueOf(resultMap.get("error")))){
						apiInfoMap.put("code", "500");
						rtnMsg += "PA_ORDER_NO:"+deliveryCompletelist.get(i).get("PA_ORDER_NO").toString() +" "+ String.valueOf(resultMap.get("error"))+"/";
					} else {
						
						resultMap.put("paCode", deliveryCompletelist.get(i).get("PA_CODE").toString());
						resultMap.put("paOrderGb", "10");
						resultMap.put("tmonOrderNo", deliveryCompletelist.get(i).get("PA_ORDER_NO").toString());
						
						PaTmonOrderListVO vo 	= new PaTmonOrderListVO();
						vo  = (PaTmonOrderListVO) PaTmonComUtill.map2VO(resultMap, PaTmonOrderListVO.class);
						
						executedRtn = paTmonDeliveryService.updatePaTmonDeliveryCompleteTx(vo);
							
						if(!executedRtn.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code", "500");
							rtnMsg += "PA_ORDER_NO:"+deliveryCompletelist.get(i).get("PA_ORDER_NO").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail"+"/";
						}
					}
				}catch (Exception e) {
					apiInfoMap.put("code", "500");
					rtnMsg += "PA_ORDER_NO:"+deliveryCompletelist.get(i).get("PA_ORDER_NO").toString() + ":"+e.getMessage();
					continue;
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	private List<PaTmonOrderListVO> makeOrderList(PaTmonOrderListVO vo) throws Exception{
		
		AESCrypto aesc = new AESCrypto();
		List<PaTmonOrderListVO> paTmonOrderListArray = new ArrayList<PaTmonOrderListVO>();
					
		vo.setZipCode(aesc.decryption(vo.getReceiverAddress().get("zipCode").toString()));		
		vo.setAddress(aesc.decryption(vo.getReceiverAddress().containsKey("address")?vo.getReceiverAddress().get("address").toString():""));
		vo.setStreetAddress(aesc.decryption(vo.getReceiverAddress().containsKey("streetAddress")?vo.getReceiverAddress().get("streetAddress").toString():""));
		vo.setAddressDetail(aesc.decryption(vo.getReceiverAddress().get("addressDetail").toString()));
		vo.setUserId(aesc.decryption(vo.getUserId()));
		vo.setUserName(aesc.decryption(vo.getUserName()));
		vo.setUserPhone((vo.getUserPhone()!=null)?aesc.decryption(vo.getUserPhone()):"");
		vo.setReceiverName(aesc.decryption(vo.getReceiverName()));
		vo.setReceiverPhone(aesc.decryption(vo.getReceiverPhone()));
		
		ArrayList<Object> deals = vo.getDeals();
		for(int i=0; i < deals.size(); i++) {
			Map<String, Object> dealMap	= (Map<String, Object>) deals.get(i);
			List<Map<String, Object>> dealOptions  = new ArrayList<Map<String,Object>>();
			dealOptions = (List<Map<String, Object>>)(dealMap.get("dealOptions"));		
			
			for(int j=0; j<dealOptions.size(); j++) {
				
				PaTmonOrderListVO paTmonOrderList = (PaTmonOrderListVO)vo.clone();
									
				paTmonOrderList.setTmonDealNo(dealMap.get("tmonDealNo").toString());
				paTmonOrderList.setDealTitle(dealMap.get("dealTitle").toString());
				paTmonOrderList.setCustomsId(dealMap.containsKey("customsId")?dealMap.get("customsId").toString():"");
				paTmonOrderList.setDeliveryNo(dealMap.get("deliveryNo").toString());
				paTmonOrderList.setDeliveryStatus(dealMap.get("deliveryStatus").toString());
				/*배송예정일 전달되는지 확인 필요*/
				paTmonOrderList.setDeliveryScheduledDate(dealMap.containsKey("deliveryScheduledDate")?dealMap.get("deliveryScheduledDate").toString():"");
				paTmonOrderList.setUpdatedDate(dealMap.get("updatedDate").toString());
				
				paTmonOrderList.setTmonDealOptionNo(dealOptions.get(j).get("tmonDealOptionNo").toString());
				paTmonOrderList.setDealOptionTitle(dealOptions.get(j).get("dealOptionTitle").toString());
				paTmonOrderList.setSalesPrice(ComUtil.objToDouble(dealOptions.get(j).get("salesPrice")));
				paTmonOrderList.setPurchasePrice(ComUtil.objToDouble(dealOptions.get(j).get("purchasePrice")));
				paTmonOrderList.setQty(ComUtil.objToLong(dealOptions.get(j).get("qty")));
				paTmonOrderList.setDescriptions(dealOptions.get(j).containsKey("descriptions")?dealOptions.get(j).get("descriptions").toString():"");
				paTmonOrderList.setOptAdditionalMessage(dealOptions.get(j).containsKey("additionalMessage")?dealOptions.get(j).get("additionalMessage").toString():"");
				
				paTmonOrderListArray.add(paTmonOrderList);
			}
		}
		
		return paTmonOrderListArray;
	}
	
	//주문번호, 배송번호, 상품단위로 송장 등록 가능. (단품 001, 002 구매 시 부분 발송 불가)
	//주문번호, 배송번호, 상품단위로 한개라도 출고 되었다면 티몬 출고 처리. 티몬 출고 처리 되었지만 sk스토아 출고되지 않은 건 sdcheck 검증쿼리에 추가 하여 모니터링 필요. 
	//물건 없어서 못나간다면 OB처리 필요.
	@ApiOperation(value = "송장 등록", notes = "송장 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> slipOutProc(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "IF_PATMONAPI_03_003";
		String				rtnMsg			= "";
		String 				transYn			= "";
		int					executedRtn		= 0;
		List<Object> dealOptionsList = null;
		Map<String,Object> dealOptions = null;		
		Map<String,Object> invoice = null;
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> orderShippingList =  paTmonDeliveryService.selectSlipOutProcList();
			
			for(int i=0; i<orderShippingList.size(); i++) {
				transYn= "0";
				
				apiInfoMap.put("paCode",orderShippingList.get(i).get("PA_CODE").toString());
				apiInfoMap.put("url",url.replace("{tmonOrderNo}", orderShippingList.get(i).get("TMON_ORDER_NO").toString()));
				ParamMap paramMap	= new ParamMap();
				ParamMap apiDataMap	= new ParamMap();
				
				//update용
				paramMap.put("paCode", orderShippingList.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", orderShippingList.get(i).get("TMON_ORDER_NO").toString());
				paramMap.put("paShipNo", orderShippingList.get(i).get("DELIVERY_NO").toString());
				paramMap.put("tmonDealNo", orderShippingList.get(i).get("TMON_DEAL_NO").toString());
				paramMap.put("paDoFlagOrg", "30");
				paramMap.put("paDoFlag", "40");
				
				//운송장 변경
				paramMap.put("slipProc"		, orderShippingList.get(i).get("SLIP_FLAG").toString());
				
				apiDataMap.put("deliveryNo", orderShippingList.get(i).get("DELIVERY_NO").toString());
				apiDataMap.put("tmonDealNo", orderShippingList.get(i).get("TMON_DEAL_NO").toString());
				
				invoice = new HashMap<String, Object>();
				invoice.put("deliveryCorp", orderShippingList.get(i).get("PA_DELY_GB").toString());
				if("10099".equals(orderShippingList.get(i).get("PA_DELY_GB").toString())) {
					apiDataMap.put("deliveryScheduledDate", orderShippingList.get(i).get("DELIVERY_SCHEDULE_DATE").toString());
				} else {
					invoice.put("invoiceNo", orderShippingList.get(i).get("SLIP_NO").toString());
				}
				apiDataMap.put("invoice", invoice);
				
				List<Map<String, Object>> orderShippingOptionList =  paTmonDeliveryService.selectSlipOutProcOptionList(paramMap);
				dealOptionsList = new ArrayList<Object>();
				for(int j=0; j<orderShippingOptionList.size(); j++) {
					dealOptions = new HashMap<String, Object>();
					dealOptions.put("tmonDealOptionNo", orderShippingOptionList.get(j).get("TMON_DEAL_OPTION_NO").toString());
					dealOptions.put("qty", ComUtil.objToLong(orderShippingOptionList.get(j).get("QTY")));
					dealOptionsList.add(dealOptions);
				}				
				apiDataMap.put("tmonDealOptions", dealOptionsList);
				
				String slipProc = orderShippingList.get(i).get("SLIP_FLAG").toString();
				if("SLIP_UPDATE".equals(slipProc)) {
					apiInfoMap.put("method" , "PUT");
				}else {
					apiInfoMap.put("method" , "POST");
				}
				
				resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
								
				switch (slipProc) {
				case "SLIP_COMPLETE":
				case "SLIP_OUT":
					if(!"null".equals(String.valueOf(resultMap.get("error")))){
						if(resultMap.get("error").toString().contains("INVALID_INVOICE")) { //유효하지 않은 송장 번호일 경우 자체배송처리.
							Map<String, Object> invoiceSelf = new HashMap<String, Object>();
							invoiceSelf.put("deliveryCorp", "10099");
							apiDataMap.put("deliveryScheduledDate", orderShippingList.get(i).get("DELIVERY_SCHEDULE_DATE").toString());
							apiDataMap.put("invoice", invoiceSelf);
							resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
							
							if(!"null".equals(String.valueOf(resultMap.get("error")))) {
									apiInfoMap.put("code", "500");
									rtnMsg += "TMON_ORDER_NO : " + orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " | 송장 등록 실패(자체배송) :: ("+String.valueOf(resultMap.get("error"))+")";
							} else {
								executedRtn = updatePaOrderMDoFlag(paramMap, "40", "출고처리", "000000");
								if(executedRtn < 1){ //ERROR
									apiInfoMap.put("code", "500");
									rtnMsg += "TMON_ORDER_NO : "+orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " TPAORDERM(pa_do_flag) UPDATE Fail/";
								}else {
									transYn = "1";	
								}
							}
						} else {
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_ORDER_NO : " + orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " | " +  String.valueOf(resultMap.get("error"))+"/";
						}
					} else {// 정상적으로 송장 등록	
						executedRtn = updatePaOrderMDoFlag(paramMap, "40", "출고처리", "000000");
						if(executedRtn < 1){ //ERROR
							apiInfoMap.put("code", "500");
							rtnMsg += "TMON_ORDER_NO : " + orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " | 송장 등록 실패(자체배송) :: ("+String.valueOf(resultMap.get("error"))+")";
						}else {
							transYn = "1";
						}
					}
					
					//Insert TPASLIPINFO (운송장 연동이력 등록)
					for(Map<String, Object> os : orderShippingOptionList) {
						
						if("1".equals(transYn)  && !"80".equals(os.get("SLIP_PROC").toString() )) {
							if(!"null".equals(String.valueOf(resultMap.get("error")))){
								os.put("TRANS_PA_DELY_GB"	, "10099");
								os.put("TRANS_INVOICE_NO"	, orderShippingList.get(i).get("DELIVERY_SCHEDULE_DATE").toString());
								os.put("REMARK1_V"			, "운송장 연동 실패 후 재 연동");
									
							}else {
								os.put("TRANS_PA_DELY_GB"	, orderShippingList.get(i).get("PA_DELY_GB").toString());
								os.put("TRANS_INVOICE_NO"	, orderShippingList.get(i).get("SLIP_NO").toString());
								os.put("REMARK1_V"			, "출고완료");
							}
							os.put("PA_GROUP_CODE"	, "09");
							os.put("TRANS_YN"		, transYn);
							paOrderService.insertTpaSlipInfoTx(os);
						}		
					}
					break;
					
					
				case "SLIP_UPDATE": //운송장 변경
					String msg = "";
					if(!"null".equals(String.valueOf(resultMap.get("error")))) {
						transYn = "0";
						msg = resultMap.get("error").toString();
					}else {
						transYn = "1";
						msg = "운송장 수정 완료";						
					}
					
					//Insert TPASLIPINFO (운송장 연동이력 등록)
					for(Map<String, Object> osc : orderShippingOptionList) {
						osc.put("PA_GROUP_CODE"		, "09");
						osc.put("TRANS_PA_DELY_GB"	, orderShippingList.get(i).get("PA_DELY_GB").toString());
						osc.put("TRANS_INVOICE_NO"	, orderShippingList.get(i).get("SLIP_NO").toString());
						osc.put("REMARK1_V"			, msg);
						osc.put("TRANS_YN"			, transYn);
						paOrderService.insertTpaSlipInfoTx(osc);
					}		
					break;
					
				default:
					break;
				}				

			}//end of for  .. orderShippingList
			
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//취소거부 시 자체배송으로 처리 후 BO에서 출고한 경우 BO에 등록한 송장으로 변경해준다
	//취소거부 시 티몬 배송상태가 D4(배송중)으로 변하는데 D4부터는 송장 수정이 불가하여 api 만들기만하고 배치 생성 안한 상태
	@ApiOperation(value = "송장 등록 내용 수정", notes = "송장 등록 내용 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), 
							@ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/update-slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> updateSlipOutProc(HttpServletRequest request) throws Exception {
	
		Map<String, Object> resultMap 		= new HashMap<String, Object>() ;
		ParamMap			apiInfoMap		= new ParamMap();		
		String 				prg_id 			= "IF_PATMONAPI_03_006";
		String				rtnMsg			= "";
		int					executedRtn		= 0;
		List<Object> dealOptionsList = null;
		Map<String,Object> dealOptions = null;		
		Map<String,Object> invoice = null;
		
		try {
			paTmonConnectUtil.getApiInfo(prg_id			, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id	, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			List<Map<String, Object>> orderShippingList =  paTmonDeliveryService.selectUpdateSlipOutProcList();
			
			for(int i=0; i<orderShippingList.size(); i++) {
				
				apiInfoMap.put("paCode",orderShippingList.get(i).get("PA_CODE").toString());
				apiInfoMap.put("url",url.replace("{tmonOrderNo}", orderShippingList.get(i).get("TMON_ORDER_NO").toString()));
				ParamMap paramMap	= new ParamMap();
				ParamMap apiDataMap	= new ParamMap();
				
				paramMap.put("paCode", orderShippingList.get(i).get("PA_CODE").toString());
				paramMap.put("paOrderNo", orderShippingList.get(i).get("TMON_ORDER_NO").toString());
				paramMap.put("paShipNo", orderShippingList.get(i).get("DELIVERY_NO").toString());
				paramMap.put("tmonDealNo", orderShippingList.get(i).get("TMON_DEAL_NO").toString());
				paramMap.put("paDoFlagOrg", "40");
				paramMap.put("paDoFlag", "40");
				
				apiDataMap.put("deliveryNo", orderShippingList.get(i).get("DELIVERY_NO").toString());
				apiDataMap.put("tmonDealNo", orderShippingList.get(i).get("TMON_DEAL_NO").toString());
				
				invoice = new HashMap<String, Object>();
				invoice.put("deliveryCorp", orderShippingList.get(i).get("PA_DELY_GB").toString());
				if("10099".equals(orderShippingList.get(i).get("PA_DELY_GB").toString())) {
					apiDataMap.put("deliveryScheduledDate", orderShippingList.get(i).get("DELIVERY_SCHEDULE_DATE").toString());
				} else {
					invoice.put("invoiceNo", orderShippingList.get(i).get("SLIP_NO").toString());
				}
				apiDataMap.put("invoice", invoice);
				
				List<Map<String, Object>> orderShippingOptionList =  paTmonDeliveryService.selectUpdateSlipOutProcOptionList(paramMap);
				dealOptionsList = new ArrayList<Object>();
				for(int j=0; j<orderShippingOptionList.size(); j++) {
					dealOptions = new HashMap<String, Object>();
					dealOptions.put("tmonDealOptionNo", orderShippingOptionList.get(j).get("TMON_DEAL_OPTION_NO").toString());
					dealOptions.put("qty", ComUtil.objToLong(orderShippingOptionList.get(j).get("QTY")));
					dealOptionsList.add(dealOptions);
				}				
				apiDataMap.put("tmonDealOptions", dealOptionsList);
				
				resultMap = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
				
				if(!"null".equals(String.valueOf(resultMap.get("error")))){
					apiInfoMap.put("code", "500");
					rtnMsg += "TMON_ORDER_NO : " + orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " | " +  String.valueOf(resultMap.get("error"))+"/";
				} else {					
					executedRtn = updatePaOrderMDoFlag(paramMap, null, "송장 수정 성공", "000000");
					if(executedRtn < 1){
						apiInfoMap.put("code", "500");
						rtnMsg += "TMON_ORDER_NO : " + orderShippingList.get(i).get("TMON_ORDER_NO").toString() + " | 송장 수정 실패 :: ("+String.valueOf(resultMap.get("error"))+")";
					}
				}
			}
			apiInfoMap.put("message", rtnMsg);
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private void orderInputMain(HttpServletRequest request) throws Exception {
		String prg_id 			= "PATMON_ORDER_INPUT";
		String duplicateCheck 	= "";
		String promoAllowTerm   = ComUtil.NVL(systemService.getValRealTime("PAPROMO_ALLOW_TERM") , "0.1" );
		
		
		try {
			duplicateCheck = systemService.checkCloseHistoryTx("start",prg_id);
			if ("1".equals(duplicateCheck)) throw processException("msg.batch_process_duplicated", new String[] {prg_id});
			
			int limitCount = ConfigUtil.getInt("PA_ORDER_CREATE_CNT");
			//생성일이 5일 이전인 do_falg 30(배송비 정보 생성 완료)인 데이터 조회 
			List<Map<String, String>> orderInputTargetList = paTmonDeliveryService.selectOrderInputTargetList(limitCount);
			
			for( Map<String, String> order : orderInputTargetList) {
				try {
					order.put("PAPROMO_ALLOW_TERM"	,	promoAllowTerm);
					paTmonAsyncController.orderInputAsync(order, request);
				}catch (Exception e) {
					log.error(prg_id + " - 주문 내역 생성 오류" + order.get("PA_ORDER_NO") , e );
				}
			}
			
		}catch (Exception e) {
			log.error("error msg : " + e.getMessage());
		}finally {
			if("0".equals(duplicateCheck)) systemService.checkCloseHistoryTx("end", prg_id);
		}
	}
	
	private int updatePaOrderMDoFlag(ParamMap paramMap, String doFlag, String resultMessage, String resultCode) throws Exception {
		
		int executedRtn = 0;
		
		paramMap.put("PA_DO_FLAG", doFlag);
		paramMap.put("API_RESULT_CODE", resultCode);
		paramMap.put("API_RESULT_MESSAGE", resultMessage);
		
		executedRtn = paTmonDeliveryService.updatePaOrderMDoFlag(paramMap);
		
		return executedRtn;
	}
	
}