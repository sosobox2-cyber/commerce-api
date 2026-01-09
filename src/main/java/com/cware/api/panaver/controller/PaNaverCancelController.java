package com.cware.api.panaver.controller;

import io.swagger.annotations.ApiOperation;

import java.security.Security;
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
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimRequestReasonType;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.panaver.cancel.service.PaNaverCancelService;

@Controller("com.cware.api.panaver.PaNaverCancelController")
@RequestMapping("/panaver/cancel")
public class PaNaverCancelController extends AbstractController {
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	PaNaverCancelService paNaverCancelService;

	@ApiOperation(value = "판매 취소 처리", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/order-cancel-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelSale(
			
		HttpServletRequest httpServletRequest,
		@RequestParam(value = "paOrderNo", required = false) String paOrderNo,
		@RequestParam(value = "paOrderSeq", required = false) String paOrderSeq,
		@RequestParam(value = "productOrderId", required = true, defaultValue="") String productOrderId,
		@RequestParam(value = "cancelReasonCode",required = true, defaultValue="2") int cancelReasonCode,
		@RequestParam(value = "paCode",required = false) String paCode) throws Exception{
			
		ParamMap paramMap = new ParamMap();
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_006");
		
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			
			AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
			CancelSaleRequestE requestE = new CancelSaleRequestE();
			CancelSaleRequest request = new CancelSaleRequest();
			NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("CancelSale");
			
			accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
			accessCredentialsType.setSignature(naverSignature.getSignature());
			accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
			
			request.setAccessCredentials(accessCredentialsType);
			request.setRequestID(UUID.randomUUID().toString());
			request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setProductOrderID(productOrderId);
			
			switch(cancelReasonCode) {
				case 0: request.setCancelReasonCode(ClaimRequestReasonType.PRODUCT_UNSATISFIED); break;
				case 1: request.setCancelReasonCode(ClaimRequestReasonType.DELAYED_DELIVERY); break;
				case 2: request.setCancelReasonCode(ClaimRequestReasonType.SOLD_OUT); break;
			}
			
			requestE.setCancelSaleRequest(request);
			
			CancelSaleResponseE responseE = stub.cancelSale(requestE);
			
			if("SUCCESS".equals(responseE.getCancelSaleResponse().getResponseType())) {
				paramMap.put("paOrderNo", paOrderNo);
				paramMap.put("paOrderSeq", ComUtil.objToStr(paOrderSeq));
				paramMap.put("productOrderId", productOrderId);
				paramMap.put("preCancelReason", request.getCancelReasonCode());
				paramMap.put("paCode",paCode);
				paramMap.put("apiResultCode", "000000");
				paramMap.put("code", "00");
				paramMap.put("apiResultMessage",paramMap.getString("result_text"));
				paNaverCancelService.saveCancelSaleTx(paramMap);
			}
			else {
				log.error("(naverProductOrderID " + productOrderId + ") GetCancelSaleResponse Error Msg : ", responseE.getCancelSaleResponse().getError().getMessage());
				paramMap.put("code", "404");
				paramMap.put("resultCode", "99");
				paramMap.put("message", "(naverProductOrderID " + productOrderId + ") GetCancelSaleResponse request failed");
			}
		}catch(Exception e){
			log.error("message", e.getMessage());
			paramMap.put("code", "500");
			paramMap.put("apiResultCode", "0");
			paramMap.put("message", e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			paramMap.put("resultCode", "99");
			paramMap.put("resultMessage", paramMap.getString("message"));
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			stub = null;
		}	
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	//취소 승인 (단건)
	@ApiOperation(value = "취소 요청 승인(단건)", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/cancel-approval-unit-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalProc(
			@RequestParam(value = "orderID", required = false) String orderID,
			@RequestParam(value = "productOrderID", required = false) String productOrderID,
			@RequestParam(value = "outBefClaimGb", required = false) String outBefClaimGb,
			HttpServletRequest httpServletRequest) throws Exception{

		String tx = "";
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> cancelMap = null;
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_015");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		try{
			//API 중복실행 체크
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
			ApproveCancelApplicationRequestE requestE = new ApproveCancelApplicationRequestE();
			ApproveCancelApplicationRequest request = new ApproveCancelApplicationRequest();
			NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ApproveCancelApplication");
			
			accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
			accessCredentialsType.setSignature(naverSignature.getSignature());
			accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
			
			request.setAccessCredentials(accessCredentialsType);
			request.setRequestID(UUID.randomUUID().toString());
			request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setProductOrderID(productOrderID);
			
			requestE.setApproveCancelApplicationRequest(request);
			
			ApproveCancelApplicationResponseE responseE = stub.approveCancelApplication(requestE);
			
			if("SUCCESS".equals(responseE.getApproveCancelApplicationResponse().getResponseType())) {
				paramMap.put("code", "200");
				paramMap.put("message", "(naverProductOrderID " + productOrderID + ") 취소승인 성공");
				paramMap.put("productOrderID", productOrderID);
				paramMap.put("orderID", orderID);
				paramMap.put("apiResultCode", "000000");
				paramMap.put("resultCode", "00");
				paramMap.put("paCode", "41");
				paramMap.put("resultMessage",paramMap.getString("message"));
				cancelMap = paNaverCancelService.selectPaNaverOrdCancelApprovalList(paramMap);
				cancelMap.put("resultCode", paramMap.get("apiResultCode"));
				cancelMap.put("resultMessage", paramMap.getString("resultMessage"));
				cancelMap.put("outBefClaimGb", outBefClaimGb);
				paNaverCancelService.saveCancelApprovalProcTx(cancelMap);
			}
			else {
				log.debug("GetCancelSaleResponse Error Msg : ", responseE.getApproveCancelApplicationResponse().getError().getMessage());
				paramMap.put("code", "400");
				paramMap.put("message", "(naverProductOrderID " + productOrderID + ") ID : "+responseE.getApproveCancelApplicationResponse().getRequestID() +"/ Msg : "+ responseE.getApproveCancelApplicationResponse().getError().getMessage() +"취소승인 실패");
				paramMap.put("resultCode", "99");
				paramMap.put("resultMessage", paramMap.getString("message"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
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
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}
			catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	//19.09.30 취소 승인(리스트)
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "취소 요청 승인(리스트)", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/cancel-approval-proc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> cancelApprovalListProc(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "sendApprovalRequest") boolean sendApprovalRequest) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> cancelMap = null;
		String resultMsg = "SUCCESS";
		ApproveCancelApplicationResponseE responseE = null;
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_007");
		paramMap.put("startDate", systemService.getSysdatetimeToString());

		try{
			//API 중복실행 체크
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			List<Object> cancelList = paNaverCancelService.selectPaNaverOrdCancelListApprovalList();
			
			for(int i = 0; i < cancelList.size(); i++){
				cancelMap = (HashMap<String, Object>) cancelList.get(i);

				if(sendApprovalRequest) {
					
					//Security.addProvider(new BouncyCastleProvider());
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					ApproveCancelApplicationRequestE requestE = new ApproveCancelApplicationRequestE();
					ApproveCancelApplicationRequest request = new ApproveCancelApplicationRequest();
					//SellerServiceStub stub = new SellerServiceStub();
				
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ApproveCancelApplication");
				
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
				
					request.setAccessCredentials(accessCredentialsType);
					request.setRequestID(UUID.randomUUID().toString());
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setProductOrderID(cancelMap.get("PRODUCT_ORDER_ID").toString());
				
					requestE.setApproveCancelApplicationRequest(request);
				
					responseE = stub.approveCancelApplication(requestE);
					resultMsg = responseE.getApproveCancelApplicationResponse().getResponseType();
				}
			
				if("SUCCESS".equals(resultMsg)) {
					if(paNaverCancelService.saveCancelApprovalProcTx(cancelMap).equals("000000")) {
						paramMap.put("code", "200");
						paramMap.put("resultCode", "00");
						paramMap.put("message", "주문 취소 처리 성공");
						paramMap.put("resultMessage", paramMap.getString("message"));
					}
					else {
						paramMap.put("code", "500");
						paramMap.put("resultCode", "99");
						paramMap.put("message", ComUtil.objToStr(cancelMap.get("PRODUCT_ORDER_ID")) + "주문 취소 처리 실패");
					}
				}
				else {
					paramMap.put("code", "400");
					paramMap.put("message", responseE.getApproveCancelApplicationResponse().getRequestID() + "주문 취소 승인 실패");
					paramMap.put("resultCode", "99");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				
				try {
					systemService.insertApiTrackingTx(httpServletRequest, paramMap);
				} catch (Exception e) {
					log.error("ApiTracking Insertion Error : {}", e.getMessage());
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
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			stub = null;
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);		
	}
}
