package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiOperation;

import java.security.Security;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub;
import com.cware.api.panaver.order.seller.SellerServiceStub.AccessCredentialsType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.DeliveryMethodType;
import com.cware.api.panaver.order.seller.SellerServiceStub.HoldbackClassType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeResponseE;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.delivery.service.PaNaverDeliveryService;
import com.cware.netshopping.panaver.exchange.service.PaNaverExchangeService;

@Controller
@RequestMapping("/panaver/exchange")
public class PaNaverExchangeController extends AbstractController{
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	PaNaverDeliveryService paNaverDeliveryService;
	
	@Autowired
	PaNaverExchangeService paNaverExchangeService;
	
	/**
	 * 교환 수거 완료 처리
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환 수거 완료 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/return-complete-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> approveCollectedExchange(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_009");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		HashMap<String, Object> exchangeMap = null;
		StringBuffer sb = new StringBuffer();
		Paorderm paorderm = new Paorderm();
		
		SellerServiceStub stub = new SellerServiceStub();
		
		log.info("======= 교환 수거 완료 처리 API Start =======");
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(tx.equals("1")){
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			List<HashMap<String, Object>> exchangeList = paNaverExchangeService.selectExchangeReturnConfirmList();
			
			for(int i=0; i<exchangeList.size(); i++){
				try{
					exchangeMap = (HashMap<String, Object>) exchangeList.get(i);
//					SellerServiceStub stub = new SellerServiceStub();
					ApproveCollectedExchangeRequestE requestE = new ApproveCollectedExchangeRequestE();
					ApproveCollectedExchangeRequest request = new ApproveCollectedExchangeRequest();
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ApproveCollectedExchange");
					
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
					
					request.setAccessCredentials(accessCredentialsType);
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setRequestID(UUID.randomUUID().toString());
					request.setProductOrderID(exchangeMap.get("PRODUCT_ORDER_ID").toString());
					request.setCollectDeliveryMethodCode(DeliveryMethodType.RETURN_DELIVERY);
					
					requestE.setApproveCollectedExchangeRequest(request);
					
					ApproveCollectedExchangeResponseE responseE = stub.approveCollectedExchange(requestE);
					
					if("SUCCESS".equals(responseE.getApproveCollectedExchangeResponse().getResponseType())){
						if((exchangeList != null)&&(exchangeList.size() > 0)){
							paramMap.put("code", "200");
							paramMap.put("apiResultCode", "000000");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", exchangeMap.get("PA_CODE").toString());
							paramMap.put("message", "(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") 수거 완료 처리 성공");
						}else{
							log.info("Error msg : No Data Selected");
							break;
						}
					}else{
						if(responseE.getApproveCollectedExchangeResponse().getError().getCode().equals("ERR-NC-UNKNOWN") && responseE.getApproveCollectedExchangeResponse().getError().getMessage().contains("처리상태")) {
							paramMap.put("code", "200");
							paramMap.put("apiResultCode", "000000");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", exchangeMap.get("PA_CODE").toString());
							paramMap.put("message", "(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") 수거 완료 처리 성공");
						}
						else {
							log.info("Error msg : " + responseE.getApproveCollectedExchangeResponse().getError().getMessage());
							log.debug("ApproveCollectedExchange requestId :: {} failed", responseE.getApproveCollectedExchangeResponse().getRequestID());
							paramMap.put("code", "400");
							paramMap.put("apiResultCode", "999999");
							paramMap.put("message", responseE.getApproveCollectedExchangeResponse().getError().getMessage() + " " + responseE.getApproveCollectedExchangeResponse().getRequestID());
							paramMap.put("resultCode", "99");
							paramMap.put("resultMessage", "ApproveCollectedExchange request failed");
							log.info("(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") " + exchangeMap.get("MAPPING_SEQ").toString() + " 수거 완료 처리 실패 ");
							sb.append("(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") " + exchangeMap.get("MAPPING_SEQ").toString() + " : 수거 완료 처리 실패 ");	
						}
					}
					paorderm.setPaOrderNo(exchangeMap.get("PA_ORDER_NO").toString());
					paorderm.setPaOrderSeq(exchangeMap.get("PA_ORDER_SEQ").toString());
					paorderm.setPaOrderGb(exchangeMap.get("PA_ORDER_GB").toString());
					paorderm.setPaGroupCode(exchangeMap.get("PA_GROUP_CODE").toString());
					paorderm.setMappingSeq(exchangeMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("resultCode"));
					paorderm.setApiResultMessage(paramMap.getString("message"));
					paorderm.setPaDoFlag(exchangeMap.get("PA_DO_FLAG").toString());
					paorderm.setApiResultCode(paramMap.getString("apiResultCode"));
					paorderm.setProcDate(new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(exchangeMap.get("SLIP_PROC_DATE").toString()).getTime()));
					paNaverExchangeService.updatePaOrdermResultTx(paorderm);
				}catch(Exception e){
					paramMap.put("code", "500");
					paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
					paramMap.put("resultCode", "99");
					paramMap.put("resultMessage", paramMap.getString("message"));
					log.info("(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") " + exchangeMap.get("MAPPING_SEQ").toString() + " : 수거 완료 처리 실패");
					sb.append("(naverProductOrderID " + exchangeMap.get("PRODUCT_ORDER_ID").toString() + ") " + exchangeMap.get("MAPPING_SEQ").toString() + " : 수거 완료 처리 실패");
					continue;
				}
			} 
		}catch(Exception e){
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try {
				if(null == paramMap.get("code")) {
					paramMap.put("code", "200");
					paramMap.put("message", "처리 대상 없음");
					paramMap.put("resultCode", "00");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				else {
					paramMap.put("message", sb.toString());
				}
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
			
			log.info("======= 교환 수거 완료 처리 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", paramMap.getString("message")), HttpStatus.OK); 
	}
	
	/**
	 * 교환 거부 처리
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환 거부 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/reject-exchange-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> rejectExchange(HttpServletRequest httpServletRequest,
			@RequestParam(value = "productOrderID",		  required = true) String productOrderID,
			@RequestParam(value = "rejectDetailContent",  required = true) String rejectDetailContent) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_016");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		StringBuffer sb = new StringBuffer();
		log.info("======= 교환 거부 처리 API Start =======");
		
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			try{
				//SellerServiceStub stub = new SellerServiceStub();
				RejectExchangeRequestE requestE = new RejectExchangeRequestE();
				RejectExchangeRequest request = new RejectExchangeRequest();
				AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
				NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("RejectExchange");
				
				accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
				accessCredentialsType.setSignature(naverSignature.getSignature());
				accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
				
				request.setAccessCredentials(accessCredentialsType);
				request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
				request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
				request.setRequestID(UUID.randomUUID().toString());
				request.setProductOrderID(productOrderID);
				request.setRejectDetailContent(rejectDetailContent);
				
				requestE.setRejectExchangeRequest(request);
				
				RejectExchangeResponseE responseE = stub.rejectExchange(requestE);
				
				if("SUCCESS".equals(responseE.getRejectExchangeResponse().getResponseType())){
					paramMap.put("code","00");
					paramMap.put("resultCode", "00");
					paramMap.put("paCode", "41");
					paramMap.put("message", "(naverProductOrderID " + productOrderID + ") " + "교환 거부 처리 완료");
					paramMap.put("resultText", "교환 거부 처리 완료");
					paramMap.put("preCancelReason","교환거부 처리");
				}else{
					log.info("RejectExchange Error msg : " + responseE.getRejectExchangeResponse().getError().getMessage());
					log.info("productOrderID[{}] : 교환 거부 처리 실패 ", productOrderID);
					sb.append("productOrderID[" + productOrderID + "] : 교환 거부 처리 실패 ");
					paramMap.put("code", "400");
					paramMap.put("message", "(naverProductOrderID " + productOrderID + ") " + responseE.getRejectExchangeResponse().getError().getMessage() + " " + responseE.getRejectExchangeResponse().getRequestID());
					paramMap.put("resultCode", "99");
					paramMap.put("resultMessage", "DelayProductOrder request failed");
					paramMap.put("resultText", "교환 거부 처리 실패");
				}
				
				
				paramMap.put("productOrderID", productOrderID);
				paramMap.put("paOrderGb", "40");
				paramMap.put("mappingSeq", paNaverDeliveryService.selectMappingSeqByProductOrderInfo(paramMap));
				HashMap<String, String> orderMap = paNaverDeliveryService.selectOrderMappingInfoByMappingSeq(paramMap.getString("mappingSeq"));
				if(orderMap != null){
					paramMap.put("claimID", orderMap.get("PA_CLAIM_NO").toString());
				}
				paramMap.put("apiResultCode", paramMap.getString("resultCode"));
				paramMap.put("apiResultMessage", paramMap.getString("resultText"));
				
				paNaverExchangeService.updatePaOrdermPreCancelTx(paramMap);
			}catch(Exception e){
				log.info("productOrderID[{}] : 교환 거부 처리 실패 ", productOrderID);
				sb.append("productOrderID[" + productOrderID + "] : 교환 거부 처리 실패 ");
			}
		}catch(Exception e){
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally{
			try {
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			log.info("======= 교환 거부 처리 API End =======");
			
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 교환 상품 재발송 처리
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환 상품 재발송 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/slip-out-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> reDeliveryExchange(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_010");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		HashMap<String, Object> reDeliveryMap = null;
		StringBuffer sb = new StringBuffer();
		Paorderm paorderm = new Paorderm();

		SellerServiceStub stub = new SellerServiceStub();
		
		log.info("======= 교환 상품 재발송 API Start =======");
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			List<HashMap<String, Object>> reDeliveryList = paNaverExchangeService.selectRedeliveryExchangeList();
			
			for(int i=0; i<reDeliveryList.size(); i++){			
				try{

					reDeliveryMap = (HashMap<String, Object>)reDeliveryList.get(i);
//					SellerServiceStub stub = new SellerServiceStub();
					ReDeliveryExchangeRequestE requestE = new ReDeliveryExchangeRequestE();
					ReDeliveryExchangeRequest request = new ReDeliveryExchangeRequest();
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ReDeliveryExchange");

					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

					request.setAccessCredentials(accessCredentialsType);
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setRequestID(UUID.randomUUID().toString());
					request.setProductOrderID(reDeliveryMap.get("PRODUCT_ORDER_ID").toString());
					request.setReDeliveryMethodCode(DeliveryMethodType.DELIVERY);
					request.setReDeliveryCompanyCode(reDeliveryMap.get("PA_DELY_GB").toString());
					request.setReDeliveryTrackingNumber(reDeliveryMap.get("SLIP_NO").toString());

					requestE.setReDeliveryExchangeRequest(request);

					ReDeliveryExchangeResponseE responseE = stub.reDeliveryExchange(requestE);

					if("SUCCESS".equals(responseE.getReDeliveryExchangeResponse().getResponseType())){
						if((reDeliveryList != null)&&(reDeliveryList.size() > 0)){
							paramMap.put("code","200");
							paramMap.put("message", "(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") 교환재발송 성공");
							paramMap.put("apiResultCode", "000000");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", reDeliveryMap.get("PA_CODE").toString());
							paramMap.put("mappingSeq", reDeliveryMap.get("MAPPING_SEQ").toString());
							paramMap.put("result_text", "교환재발송 성공");
							paramMap.put("paOrderGb", "40");
							paorderm.setPaOrderGb("40");
							paorderm.setPaDoFlag("40");
							paorderm.setApiResultCode(paramMap.getString("apiResultCode"));
							paorderm.setMappingSeq(reDeliveryMap.get("MAPPING_SEQ").toString());
						}else{
							log.info("Error msg : No Data Selected");
							break;
						}
					}else{
						
//						stub = new SellerServiceStub();
						requestE = new ReDeliveryExchangeRequestE();
						request = new ReDeliveryExchangeRequest();
						accessCredentialsType = new AccessCredentialsType();
						naverSignature = ComUtil.paNaverGenerateSignature("ReDeliveryExchange");

						accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
						accessCredentialsType.setSignature(naverSignature.getSignature());
						accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

						request.setAccessCredentials(accessCredentialsType);
						request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
						request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
						request.setRequestID(UUID.randomUUID().toString());
						request.setProductOrderID(reDeliveryMap.get("PRODUCT_ORDER_ID").toString());
						request.setReDeliveryMethodCode(DeliveryMethodType.DIRECT_DELIVERY);
						request.setReDeliveryCompanyCode("CH1");
						request.setReDeliveryTrackingNumber(reDeliveryMap.get("SLIP_NO").toString());

						requestE.setReDeliveryExchangeRequest(request);

						responseE = stub.reDeliveryExchange(requestE);

						if("SUCCESS".equals(responseE.getReDeliveryExchangeResponse().getResponseType())){
							if((reDeliveryList != null)&&(reDeliveryList.size() > 0)){
								paramMap.put("code","200");
								paramMap.put("message", "(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") 교환재발송 성공");
								paramMap.put("apiResultCode", "000000");
								paramMap.put("resultCode", "00");
								paramMap.put("paCode", reDeliveryMap.get("PA_CODE").toString());
								paramMap.put("mappingSeq", reDeliveryMap.get("MAPPING_SEQ").toString());
								paramMap.put("result_text", "교환재발송 성공");
								paramMap.put("paOrderGb", "40");
								paorderm.setPaDoFlag("40");
								paorderm.setPaOrderGb("40");
								paorderm.setApiResultCode(paramMap.getString("apiResultCode"));
								paorderm.setMappingSeq(reDeliveryMap.get("MAPPING_SEQ").toString());
							}else{
								log.info("Error msg : No Data Selected");
								break;
							}
						}else{
							log.info("ReDeliveryExchange Error Msg : " + responseE.getReDeliveryExchangeResponse().getError().getMessage());
							log.info("(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") " + reDeliveryMap.get("MAPPING_SEQ").toString() + " : 교환재발송 실패");
							sb.append("(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") " + reDeliveryMap.get("MAPPING_SEQ").toString() + " : 교환재발송 실패");
							paramMap.put("code", "400");
							paramMap.put("message", responseE.getReDeliveryExchangeResponse().getError().getMessage() + " " + responseE.getReDeliveryExchangeResponse().getRequestID());
							paramMap.put("resultCode", "99");
							paramMap.put("resultMessage", "DelayProductOrder request failed");
							paramMap.put("mappingSeq", reDeliveryMap.get("MAPPING_SEQ").toString());
							paramMap.put("result_text", "교환재발송 실패");
							paramMap.put("paOrderGb", "40");
							paorderm.setPaOrderGb("40");
							paorderm.setApiResultCode("99");
							paorderm.setMappingSeq(reDeliveryMap.get("MAPPING_SEQ").toString());
						}
					}
					paorderm.setMappingSeq(reDeliveryMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					
					paNaverExchangeService.updateExchangePaOrdermResultTx(paorderm);
				}catch(Exception e){
					log.info("(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") " + reDeliveryMap.get("MAPPING_SEQ").toString() + " : 교환재발송 실패 ");
					log.info(e.getMessage());
					sb.append("(naverProductOrderID " + reDeliveryMap.get("PRODUCT_ORDER_ID").toString() + ") " + reDeliveryMap.get("MAPPING_SEQ").toString() + " : 교환재발송 실패 ");
				}
			}
		}catch(Exception e){
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg("500", paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try {
				if(paramMap.get("code") == null) {
					paramMap.put("code", "200");
					paramMap.put("message", "재발송 처리 대상 없음");
					paramMap.put("resultCode", "00");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				else {
					paramMap.put("code", "500");
					paramMap.put("message", sb.toString());
					paramMap.put("resultCode", "99");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
			
			log.info("======= 교환 상품 재발송 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", paramMap.getString("message")), HttpStatus.OK);
	}

	/**
	 * 교환 보류 설정
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환 보류 설정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/return-hold-proc", method = RequestMethod.GET)//환불 보류 설정
	@ResponseBody
	public ResponseEntity<?> withholdExchange(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> claimMap = null;
		Paorderm paorderm = new Paorderm();
		StringBuffer sb = new StringBuffer();
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_013");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		log.info("======= 교환 보류 설정 API Start =======");
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			List<Object> claimList = paNaverExchangeService.selectReturnHoldList();
			
			for(int i=0; i<claimList.size(); i++){
				try{
					claimMap = (HashMap<String, Object>) claimList.get(i);
					//Security.addProvider(new BouncyCastleProvider());
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					WithholdExchangeRequestE requestE = new WithholdExchangeRequestE();
					WithholdExchangeRequest request = new WithholdExchangeRequest();
//					SellerServiceStub stub = new SellerServiceStub();
					
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("WithholdExchange");
					
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
					
					request.setAccessCredentials(accessCredentialsType);
					request.setRequestID(UUID.randomUUID().toString());
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setProductOrderID(claimMap.get("PRODUCT_ORDER_ID").toString());
					request.setExchangeHoldCode(HoldbackClassType.ETC);
					request.setExchangeHoldDetailContent("DELIVERY");
					
					requestE.setWithholdExchangeRequest(request);
					
					WithholdExchangeResponseE responseE = stub.withholdExchange(requestE);
					
					if("SUCCESS".equals(responseE.getWithholdExchangeResponse().getResponseType())){
						if((claimList != null)&&(claimList.size() > 0)){
							paramMap.put("code", "00");
							paramMap.put("apiResultCode", "0");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", claimMap.get("PA_CODE").toString());
							paramMap.put("result_text", "교환 보류 처리 성공");
							paramMap.put("pa_hold_yn", "0");
						}else{
							log.info("Error Msg : No Data Selected");
							break;
						}
					}else{
						log.info("GetWithholdExchange Error Msg : " + responseE.getWithholdExchangeResponse().getError().getMessage());
						paramMap.put("code", "404");
						paramMap.put("resultCode", "99");
						paramMap.put("message", "GetWithholdExchange request failed");
						paramMap.put("result_text", "교환 보류 처리 실패");
						paramMap.put("message", responseE.getWithholdExchangeResponse().getError().getMessage() + " " + responseE.getWithholdExchangeResponse().getRequestID());
						log.info(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 처리 실패");
						sb.append(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 처리 실패");
					}
					paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("resultCode"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					paorderm.setPaHoldYn(paramMap.getString("pa_hold_yn"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					
//					paNaverExchangeService.updatePaOrdermHoldYn(paorderm);
				}catch(Exception e){
					log.info(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 처리 실패" + e.getMessage());
					sb.append(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 처리 실패" + e.getMessage());
					continue;
				}
			}
		}catch(Exception e){
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try {
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
			
			log.info("======= 교환 보류 설정 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	/**
	 * 교환 보류 해제
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "교환 보류 해제", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/return-hold-release-proc", method = RequestMethod.GET)//환불 보류 해제
	@ResponseBody
	public ResponseEntity<?> releaseReturnHold(HttpServletRequest httpServletRequest) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> claimMap = null;
		StringBuffer sb = new StringBuffer();
		Paorderm paorderm = new Paorderm();
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_014");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		log.info("======= 교환 보류 해제 API Start =======");
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();

		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(tx.equals("1")){
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}

			List<Object> claimList = paNaverExchangeService.selectReleaseReturnHoldList();

			for(int i=0; i<claimList.size(); i++){
				try{		
					claimMap = (HashMap<String, Object>) claimList.get(i);
					//Security.addProvider(new BouncyCastleProvider());
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					ReleaseExchangeHoldRequestE requestE = new ReleaseExchangeHoldRequestE();
					ReleaseExchangeHoldRequest request = new ReleaseExchangeHoldRequest();
//					SellerServiceStub stub = new SellerServiceStub();

					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ReleaseExchangeHold");

					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

					request.setAccessCredentials(accessCredentialsType);
					request.setRequestID(UUID.randomUUID().toString());
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setProductOrderID(claimMap.get("PRODUCT_ORDER_ID").toString());

					requestE.setReleaseExchangeHoldRequest(request);

					ReleaseExchangeHoldResponseE responseE = stub.releaseExchangeHold(requestE);
					if("SUCCESS".equals(responseE.getReleaseExchangeHoldResponse().getResponseType())){
						if((claimList != null)&&(claimList.size() > 0)){
							paramMap.put("code", "00");
							paramMap.put("apiResultCode", "0");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", claimMap.get("PA_CODE").toString());
							paramMap.put("result_text", "교환 보류 해제 성공");
							paramMap.put("pa_hold_yn", "1");
						}else{
							log.info("Error msg : No Data Selected");
							break;
						}
					}else{
						log.debug("GetReleaseReturnHoldResponse Error Msg : " + responseE.getReleaseExchangeHoldResponse().getError().getMessage());
						paramMap.put("code", "404");
						paramMap.put("resultCode", "99");
						paramMap.put("message", "GetReleaseReturnHoldResponse request failed");
						paramMap.put("result_text", "교환 보류 해제 실패");
						log.info(claimMap.get("MAPPING_SEQ").toString() + " : 교환 보류 해제 실패");
						sb.append(claimMap.get("MAPPING_SEQ").toString() + " : 교환 보류 해제 실패");
					}
					paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("result_code"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					paorderm.setPaHoldYn(paramMap.getString("pa_hold_yn"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));

//					paNaverExchangeService.updatePaOrdermHoldYn(paorderm);
				}catch(Exception e){
					log.info(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 해제 실패" + e.getMessage());
					sb.append(claimMap.get("MAPPING_SEQ").toString()+" : 교환 보류 해제 실패" + e.getMessage());
					continue;
				}
			}
		}catch(Exception e){
			log.error("message", e);
			paramMap.put("code", "500");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try {
				paramMap.put("message", sb.toString());
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
			
			log.info("======= 교환 보류 해제 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
}
