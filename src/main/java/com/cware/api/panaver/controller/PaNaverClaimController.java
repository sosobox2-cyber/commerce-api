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
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.message.ProductOrderInfoMsg;
import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub;
import com.cware.api.panaver.order.seller.SellerServiceStub.AccessCredentialsType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimStatusType;
import com.cware.api.panaver.order.seller.SellerServiceStub.HoldbackClassType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnResponseE;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.panaver.claim.service.PaNaverClaimService;

@Controller
@RequestMapping("/panaver/claim")
public class PaNaverClaimController extends AbstractController {
	
	@Autowired
	PaNaverInfoCommonController paNaverInfoCommonController;

	@Autowired
	SystemService systemService;
	
	@Autowired
	PaNaverClaimService paNaverClaimService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "반품 요청 승인", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/return-confirm-proc", method = RequestMethod.GET)//반품 요청 승인
	@ResponseBody
	public ResponseEntity<?> approveReturnApplication(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, String> claimMap = null;
		String tx = "";
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_008");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		log.info("======= 반품 요청 승인  API Start =======");
		
		try{
			//중복 확인
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			//반품 요청 건 조회
			List<Object> claimList = paNaverClaimService.selectPaNaverClaimApprovalList();
			
			for(int i=0; i<claimList.size(); i++){
				claimMap = (HashMap<String, String>) claimList.get(i);
				
				//Security.addProvider(new BouncyCastleProvider());
				AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
				ApproveReturnApplicationRequestE requestE = new ApproveReturnApplicationRequestE();
				ApproveReturnApplicationRequest request = new ApproveReturnApplicationRequest();
//				SellerServiceStub stub = new SellerServiceStub();
				
				NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ApproveReturnApplication");
				
				accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
				accessCredentialsType.setSignature(naverSignature.getSignature());
				accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
				
				request.setAccessCredentials(accessCredentialsType);
				request.setRequestID(UUID.randomUUID().toString());
				request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
				request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
				request.setProductOrderID(claimMap.get("PRODUCT_ORDER_ID").toString());
				
				requestE.setApproveReturnApplicationRequest(request);
				
				ApproveReturnApplicationResponseE responseE = stub.approveReturnApplication(requestE);
				
				if("SUCCESS".equals(responseE.getApproveReturnApplicationResponse().getResponseType())){
					paramMap.put("productOrderIds", claimMap.get("PRODUCT_ORDER_ID").toString());
					paramMap.put("rodPrdSeq", claimMap.get("PA_ORDER_SEQ").toString());
					paramMap.put("apiResultCode", "0");
					paramMap.put("resultCode", "00");
					paramMap.put("paCode", "41");
					paramMap.put("code","00");
					
					paNaverClaimService.saveClaimApprovalProcTx(claimMap);
				}else{
					if(responseE.getApproveReturnApplicationResponse().getError().getMessage().contains("반품 진행중인 주문이 아니므로")) {
						ResponseEntity<ProductOrderInfoMsg> response = paNaverInfoCommonController.retrieveOrderDetailInfo(httpServletRequest, claimMap.get("PRODUCT_ORDER_ID"));
						if(response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
							if(response.getBody().getProductOrderInfo()[0].getReturnInfo() != null && response.getBody().getProductOrderInfo()[0].getReturnInfo().getClaimStatus().equals(ClaimStatusType.RETURN_DONE)) {
								paramMap.put("productOrderIds", claimMap.get("PRODUCT_ORDER_ID").toString());
								paramMap.put("rodPrdSeq", claimMap.get("PA_ORDER_SEQ").toString());
								paramMap.put("apiResultCode", "0");
								paramMap.put("resultCode", "00");
								paramMap.put("paCode", "41");
								paramMap.put("code","00");
								paNaverClaimService.saveClaimApprovalProcTx(claimMap);																
							}
						}
					}
					else {
						log.info("GetApproveReturnApplicationResponse Error Msg : {}", responseE.getApproveReturnApplicationResponse().getError().getMessage());
						paramMap.put("code", "404");
						paramMap.put("resultCode", "99");
						paramMap.put("message", "(naverProductOrderID " + claimMap.get("PRODUCT_ORDER_ID").toString() + ") GetApproveReturnApplicationResponse request failed");
						paramMap.put("apiResultCode", "99");
						paramMap.put("resultMessage", paramMap.getString("message"));						
					}
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
				if(paramMap.getString("code") != null) {
					paramMap.put("code", "200");
					paramMap.put("resultCode", "000000");
					paramMap.put("message", "수거완료처리 대상 없음");
					paramMap.put("resultMessage", paramMap.getString("message"));
				}
				systemService.insertApiTrackingTx(httpServletRequest, paramMap);
			}catch(Exception e) {
				log.error("ApiTracking Insertion Error : {}", e.getMessage());
			}
			if (tx.equals("0")) {
				systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			}
			
			stub = null;
			
			log.info("======= 반품 요청 승인 API End =======");
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "환불 보류 설정", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/return-hold-proc", method = RequestMethod.GET)//환불 보류 설정
	@ResponseBody
	public ResponseEntity<?> withholdReturn(HttpServletRequest httpServletRequest) throws Exception{
		
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> claimMap = null;
		Paorderm paorderm = new Paorderm();
		StringBuffer sb = new StringBuffer();

		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();
		
		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_011");
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		
		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if (tx.equals("1")) {
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}
			
			List<Object> claimList = paNaverClaimService.selectReturnHoldList();
			
			for(int i=0; i<claimList.size(); i++){
				try{
					claimMap = (HashMap<String, Object>) claimList.get(i);
					//Security.addProvider(new BouncyCastleProvider());
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					WithholdReturnRequestE requestE = new WithholdReturnRequestE();
					WithholdReturnRequest request = new WithholdReturnRequest();
//					SellerServiceStub stub = new SellerServiceStub();
					
					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("WithholdReturn");
					
					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());
					
					request.setAccessCredentials(accessCredentialsType);
					request.setRequestID(UUID.randomUUID().toString());
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setProductOrderID(claimMap.get("PRODUCT_ORDER_ID").toString());
					request.setReturnHoldCode(HoldbackClassType.ETC);
					request.setReturnHoldDetailContent("DELIVERY");
					
					requestE.setWithholdReturnRequest(request);
					
					WithholdReturnResponseE responseE = stub.withholdReturn(requestE);
					
					if("SUCCESS".equals(responseE.getWithholdReturnResponse().getResponseType())){
						if((claimList != null)&&(claimList.size() > 0)){
							paramMap.put("code", "00");
							paramMap.put("apiResultCode", "0");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", claimMap.get("PA_CODE").toString());
							paramMap.put("result_text", "반품완료보류처리 성공");
							paramMap.put("pa_hold_yn", "0");
						}else{
							log.info("Error Msg : No Data Selected");
							break;
						}
					}else{
						log.debug("GetWithholdReturnResponse Error Msg : ", responseE.getWithholdReturnResponse().getError().getMessage());
						paramMap.put("code", "404");
						paramMap.put("resultCode", "99");
						paramMap.put("message", "GetWithholdReturnResponse request failed");
						paramMap.put("result_text", "반품완료보류처리 실패");
						log.info(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류처리 실패");
						sb.append(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류처리 실패");
					}
					paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("result_code"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					paorderm.setPaHoldYn(paramMap.getString("pa_hold_yn"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					
					paNaverClaimService.updatePaOrdermHoldYnTx(paorderm);
				}catch(Exception e){
					log.info(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류처리 실패" + e.getMessage());
					sb.append(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류처리 실패" + e.getMessage());
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
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "환불 보류 해제", httpMethod = "GET", response = ResponseMsg.class, produces = "application/json")
	@RequestMapping(value = "/return-hold-release-proc", method = RequestMethod.GET)//환불 보류 해제
	@ResponseBody
	public ResponseEntity<?> releaseReturnHold(HttpServletRequest httpServletRequest) throws Exception{
		ParamMap paramMap = new ParamMap();
		HashMap<String, Object> claimMap = null;
		StringBuffer sb = new StringBuffer();
		Paorderm paorderm = new Paorderm();
		
		Security.addProvider(new BouncyCastleProvider());
		SellerServiceStub stub = new SellerServiceStub();

		String tx = "";
		paramMap.put("siteGb", "PANAVER");
		paramMap.put("apiCode", "IF_PANAVERAPI_03_012");
		paramMap.put("startDate", systemService.getSysdatetimeToString());

		try{
			tx = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
			if(tx.equals("1")){
				throw processException("msg.batch_process_duplicated", new String[] { paramMap.getString("apiCode") });
			}

			List<Object> claimList = paNaverClaimService.selectReleaseReturnHoldList();

			for(int i=0; i<claimList.size(); i++){
				try{		
					claimMap = (HashMap<String, Object>) claimList.get(i);
					//Security.addProvider(new BouncyCastleProvider());
					AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
					ReleaseReturnHoldRequestE requestE = new ReleaseReturnHoldRequestE();
					ReleaseReturnHoldRequest request = new ReleaseReturnHoldRequest();
//					SellerServiceStub stub = new SellerServiceStub();

					NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("ReleaseReturnHold");

					accessCredentialsType.setAccessLicense(naverSignature.getAccessLicense());
					accessCredentialsType.setSignature(naverSignature.getSignature());
					accessCredentialsType.setTimestamp(naverSignature.getTimeStamp());

					request.setAccessCredentials(accessCredentialsType);
					request.setRequestID(UUID.randomUUID().toString());
					request.setDetailLevel(ConfigUtil.getString("PANAVER_SERVICE_DETAIL_LEVEL"));
					request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
					request.setProductOrderID(claimMap.get("PRODUCT_ORDER_ID").toString());

					requestE.setReleaseReturnHoldRequest(request);

					ReleaseReturnHoldResponseE responseE = stub.releaseReturnHold(requestE);
					if("SUCCESS".equals(responseE.getReleaseReturnHoldResponse().getResponseType())){
						if((claimList != null)&&(claimList.size() > 0)){
							paramMap.put("code", "00");
							paramMap.put("apiResultCode", "0");
							paramMap.put("resultCode", "00");
							paramMap.put("paCode", claimMap.get("PA_CODE").toString());
							paramMap.put("result_text", "반품완료보류해제 성공");
							paramMap.put("pa_hold_yn", "1");
						}else{
							log.info("Error msg : No Data Selected");
							break;
						}
					}else{
						log.debug("GetReleaseReturnHoldResponse Error Msg : ", responseE.getReleaseReturnHoldResponse().getError().getMessage());
						paramMap.put("code", "404");
						paramMap.put("resultCode", "99");
						paramMap.put("message", "GetReleaseReturnHoldResponse request failed");
						paramMap.put("result_text", "반품환료보류해제 실패");
						log.info(paramMap.get("MAPPING_SEQ").toString() + " : 반품완료보류해제 실패");
						sb.append(paramMap.get("MAPPING_SEQ").toString() + " : 반품완료보류해제 실패");
					}
					paorderm.setMappingSeq(claimMap.get("MAPPING_SEQ").toString());
					paorderm.setApiResultCode(paramMap.getString("result_code"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));
					paorderm.setPaHoldYn(paramMap.getString("pa_hold_yn"));
					paorderm.setApiResultMessage(paramMap.getString("result_text"));

					paNaverClaimService.updatePaOrdermHoldYnTx(paorderm);
				}catch(Exception e){
					log.info(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류해제 실패" + e.getMessage());
					sb.append(paramMap.get("MAPPING_SEQ").toString()+" : 반품완료보류해제 실패" + e.getMessage());
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
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg("200", "OK"), HttpStatus.OK);
	}
}
