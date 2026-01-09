package com.cware.api.panaver.order;

import java.rmi.RemoteException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.cware.api.panaver.order.seller.NaverSignature;
import com.cware.api.panaver.order.seller.SellerServiceStub;
import com.cware.api.panaver.order.seller.SellerServiceStub.AccessCredentialsType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCancelApplicationResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveCollectedExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ApproveReturnApplicationResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.CancelSaleResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ClaimRequestReasonType;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayProductOrderResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.DelayedDispatchReasonType;
import com.cware.api.panaver.order.seller.SellerServiceStub.DeliveryMethodType;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetChangedProductOrderListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.GetProductOrderInfoListResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.HoldbackClassType;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceProductOrderResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ProductOrderChangeType;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReDeliveryExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectReturnRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectReturnRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RejectReturnResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseExchangeHoldResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ReleaseReturnHoldResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RequestReturnRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.RequestReturnRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.RequestReturnResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.ShipProductOrderResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdExchangeResponseE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnRequest;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnRequestE;
import com.cware.api.panaver.order.seller.SellerServiceStub.WithholdReturnResponseE;
import com.cware.netshopping.common.util.ComUtil;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;

@SuppressWarnings("unused")
public class GetChangedProductOrderList {

	private static final String serviceName = "SellerService41";

	private static final String accessLicense = "010001000049a79ed9bec98d543aa384d2b38e2440e077d5ff6e2c3cdd2fff7de174080676"; //accessLicense입력, PDF파일참조
	private static final String secretKey = "AQABAABtHMV1FOalGi833vagMCfm/4tbE4gqENSOvtYX2h6TTQ=="; //secretKey입력, PDF파일참조
	private static String timeStamp;
	private static String signature;
	private static byte[] encryptKey = null; 
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	// 서명생성
	private static void generateSignature(String operationName) {
		timeStamp = SimpleCryptLib.getTimestamp();
		String data = timeStamp + serviceName + operationName;

		try {
			signature = SimpleCryptLib.generateSign(data, secretKey);
			encryptKey = SimpleCryptLib.generateKey(timeStamp, secretKey); 
		} catch (SignatureException e) {
			//서명정보 실패
			System.out.println(e.getMessage());
		}
	}	
	
	private static void getChangedProductOrderListTest() throws RemoteException {//5.3.2(기존에 있던거)
		Security.addProvider(new BouncyCastleProvider());
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		GetChangedProductOrderListRequestE requestE = new GetChangedProductOrderListRequestE();
		GetChangedProductOrderListRequest request = new GetChangedProductOrderListRequest();
		SellerServiceStub stub = new SellerServiceStub();
		
		//서명생성
		generateSignature("GetChangedProductOrderList");
		
		//인증정보
		accessCredentialsType.setAccessLicense(accessLicense);
		accessCredentialsType.setSignature(signature);
		accessCredentialsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentialsType);
		
		//GetChangedProductOrderList 파라메터 설정
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.set(2012, 0, 1, 0, 0, 0);
		to.set(2012, 0, 2, 0, 0, 0);
		//from.set(2012, 1, 1, 0, 0, 0);
		//to.set(2012, 1, 2, 0, 0, 0);
		
		request.setDetailLevel("Full");
		request.setVersion("4.0");
		request.setRequestID(UUID.randomUUID().toString());
		request.setMallID("ncp_1njgo6_02"); // ncp_1njgo6_02 , skstoa , sonmars@SK.COM 	
		
		request.setInquiryTimeFrom(from);
		request.setInquiryTimeTo(to);
		request.setLastChangedStatusCode(ProductOrderChangeType.PAY_WAITING);
		
		requestE.setGetChangedProductOrderListRequest(request);
		
		//GetChangedProductOrderList Response 수신
		GetChangedProductOrderListResponseE responseE = stub.getChangedProductOrderList(requestE);
		
		//결과 출력
		if("SUCCESS".equals(responseE.getGetChangedProductOrderListResponse().getResponseType())) {
			//응답메시지 처리 getxxx
			System.out.println(responseE.getGetChangedProductOrderListResponse().getRequestID());
			System.out.println(responseE.getGetChangedProductOrderListResponse().getChangedProductOrderInfoList());
		} else {
			System.out.println("Code : " + responseE.getGetChangedProductOrderListResponse().getError().getCode());
			System.out.println("Message : " + responseE.getGetChangedProductOrderListResponse().getError().getMessage());
			System.out.println("Detail : " + responseE.getGetChangedProductOrderListResponse().getError().getDetail());
		}
	}
	
	
	private static void placeProductOrderRequestTest() throws RemoteException{//5.3.3
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		PlaceProductOrderRequest request = new PlaceProductOrderRequest();
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		PlaceProductOrderRequestE requestE = new PlaceProductOrderRequestE();
		SellerServiceStub stub = new SellerServiceStub();
		
		
		//서명생성
		generateSignature("PlaceProductOrder");
				
		//인증정보
		accessCredentialsType.setAccessLicense(accessLicense);
		accessCredentialsType.setSignature(signature);
		accessCredentialsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentialsType);
		
		//테스트용 값들
		request.setRequestID(UUID.randomUUID().toString());
		request.setVersion("2.0");
		request.setDetailLevel("Full");
		request.setProductOrderID("PONO200000000722");
		
		requestE.setPlaceProductOrderRequest(request);
		
		//placeProductOrder Response 수신
		PlaceProductOrderResponseE responseE = stub.placeProductOrder(requestE);
		
		//결과 출력
		if("SUCCESS".equals(responseE.getPlaceProductOrderResponse().getResponseType())){
			System.out.println(responseE.getPlaceProductOrderResponse().getRequestID());
			System.out.println(accessCredentialsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getPlaceProductOrderResponse().getError().getCode());
			System.out.println("Message : "+responseE.getPlaceProductOrderResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getPlaceProductOrderResponse().getError().getDetail());			
		}
	}
	

	private static void delayProductOrderTest()throws RemoteException{//5.3.4
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		DelayProductOrderRequest request = new DelayProductOrderRequest();
		SellerServiceStub stub = new SellerServiceStub();
		DelayProductOrderRequestE requestE = new DelayProductOrderRequestE();
		
		//서명생성
		generateSignature("DelayProductOrder");
		
		//인증정보
		accessCredentialsType.setAccessLicense(accessLicense);
		accessCredentialsType.setSignature(signature);
		accessCredentialsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentialsType);
		
		Calendar from = Calendar.getInstance();
		from.set(2019, 0, 1, 0, 0, 0);
		
		//테스트용 값
		request.setRequestID("skstoaTest");
		request.setVersion("2.0");
		request.setDetailLevel("Full");
		request.setProductOrderID("PONO200000000702");
		request.setDispatchDueDate(from);
		request.setDispatchDelayReasonCode(DelayedDispatchReasonType.ETC);
		
		requestE.setDelayProductOrderRequest(request);
		
		//DelayProductOrder Response 수신
		DelayProductOrderResponseE responseE = stub.delayProductOrder(requestE);
		
		//결과 출력
		if("SUCCESS".equals(responseE.getDelayProductOrderResponse().getResponseType())){
			System.out.println(responseE.getDelayProductOrderResponse().getRequestID());
			System.out.println(accessCredentialsType.getTimestamp());
			System.out.println(request.getDispatchDueDate().getTime());
		}else{
			System.out.println("Code : "+responseE.getDelayProductOrderResponse().getError().getCode());
			System.out.println("Message : "+responseE.getDelayProductOrderResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getDelayProductOrderResponse().getError().getDetail());			
		}
		
	}
	
	
	private static void shipProductOrderTest() throws RemoteException{//5.3.5
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		ShipProductOrderRequest request = new ShipProductOrderRequest();
		ShipProductOrderRequestE requestE = new ShipProductOrderRequestE();
		SellerServiceStub stub = new SellerServiceStub();
		
		generateSignature("ShipProductOrder");
		
		accessCredentialsType.setAccessLicense(accessLicense);
		accessCredentialsType.setSignature(signature);
		accessCredentialsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentialsType);
		
		Calendar from = Calendar.getInstance();
		from.set(2019, 0, 1, 0, 0, 0);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000701");
		request.setDeliveryMethodCode(DeliveryMethodType.DELIVERY);
		request.setDeliveryCompanyCode("12341234");
		request.setTrackingNumber("101010");
		request.setDispatchDate(from);
		
		requestE.setShipProductOrderRequest(request);
		
		ShipProductOrderResponseE responseE = stub.shipProductOrder(requestE);
		
		if("SUCCESS".equals(responseE.getShipProductOrderResponse().getResponseType())){
			System.out.println(responseE.getShipProductOrderResponse().getRequestID());
			System.out.println(accessCredentialsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getShipProductOrderResponse().getError().getCode());
			System.out.println("Message : "+responseE.getShipProductOrderResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getShipProductOrderResponse().getError().getDetail());			
		}
	}
	
	
	private static void cancelSaleTest() throws RemoteException{//5.3.6
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentialsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		CancelSaleRequest request = new CancelSaleRequest();
		CancelSaleRequestE requestE = new CancelSaleRequestE();
		
		generateSignature("CancelSale");
		
		accessCredentialsType.setAccessLicense(accessLicense);
		accessCredentialsType.setSignature(signature);
		accessCredentialsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentialsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000703");
		request.setCancelReasonCode(ClaimRequestReasonType.SOLD_OUT);
		
		requestE.setCancelSaleRequest(request);
		
		CancelSaleResponseE responseE = stub.cancelSale(requestE);
		
		if("SUCCESS".equals(responseE.getCancelSaleResponse().getResponseType())){
			System.out.println("RequestID : " + responseE.getCancelSaleResponse().getRequestID());
			System.out.println(accessCredentialsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getCancelSaleResponse().getError().getCode());
			System.out.println("Message : "+responseE.getCancelSaleResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getCancelSaleResponse().getError().getDetail());			
		}
	}
	
	
	private static void approveCancelApplicationTest() throws RemoteException{//5.3.7
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ApproveCancelApplicationRequest request = new ApproveCancelApplicationRequest();
		ApproveCancelApplicationRequestE requestE = new ApproveCancelApplicationRequestE();
		
		generateSignature("ApproveCancelApplication");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000481");
		
		requestE.setApproveCancelApplicationRequest(request);
		
		ApproveCancelApplicationResponseE responseE = stub.approveCancelApplication(requestE);
		
		if("SUCCESS".equals(responseE.getApproveCancelApplicationResponse().getResponseType())){
			System.out.println("RequestID : " + responseE.getApproveCancelApplicationResponse().getRequestID());
			System.out.println(accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getApproveCancelApplicationResponse().getError().getCode());
			System.out.println("Message : "+responseE.getApproveCancelApplicationResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getApproveCancelApplicationResponse().getError().getDetail());
		}
	}
	
	
	private static void requestReturnTest() throws RemoteException{//5.3.8
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		RequestReturnRequest request = new RequestReturnRequest();
		RequestReturnRequestE requestE = new RequestReturnRequestE();
		
		generateSignature("RequestReturn");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000286");
		request.setReturnReasonCode(ClaimRequestReasonType.SOLD_OUT);
		request.setCollectDeliveryMethodCode(DeliveryMethodType.DELIVERY);
		request.setCollectDeliveryCompanyCode("CJGLS");
		
		requestE.setRequestReturnRequest(request);
		
		RequestReturnResponseE responseE = stub.requestReturn(requestE);
		
		if("SUCCESS".equals(responseE.getRequestReturnResponse().getResponseType())){
			System.out.println(responseE.getRequestReturnResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getRequestReturnResponse().getError().getCode());
			System.out.println("Message : "+responseE.getRequestReturnResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getRequestReturnResponse().getError().getDetail());
		}
	}
	
	
	private static void approveReturnApplicationTest() throws RemoteException{//5.3.9(41버전으로)
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ApproveReturnApplicationRequest request = new ApproveReturnApplicationRequest();
		ApproveReturnApplicationRequestE requestE = new ApproveReturnApplicationRequestE();
		
		generateSignature("ApproveReturnApplication");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000286");
		
		requestE.setApproveReturnApplicationRequest(request);
		
		ApproveReturnApplicationResponseE responseE = stub.approveReturnApplication(requestE);
		
		if("SUCCESS".equals(responseE.getApproveReturnApplicationResponse().getResponseType())){
			System.out.println(responseE.getApproveReturnApplicationResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getApproveReturnApplicationResponse().getError().getCode());
			System.out.println("Message : "+responseE.getApproveReturnApplicationResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getApproveReturnApplicationResponse().getError().getDetail());
		}
	}
	
	
	private static void approveCollectedExchangeTest() throws RemoteException{//5.3.10(문서에는  CollectDeliveryMethodCode 없음)
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ApproveCollectedExchangeRequest request = new ApproveCollectedExchangeRequest();
		ApproveCollectedExchangeRequestE requestE = new ApproveCollectedExchangeRequestE();
		
		generateSignature("ApproveCollectedExchange");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO600000000009");
		request.setCollectDeliveryMethodCode(DeliveryMethodType.DELIVERY);
		
		requestE.setApproveCollectedExchangeRequest(request);
		
		ApproveCollectedExchangeResponseE responseE = stub.approveCollectedExchange(requestE);
		
		if("SUCCESS".equals(responseE.getApproveCollectedExchangeResponse().getResponseType())){
			System.out.println(responseE.getApproveCollectedExchangeResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getApproveCollectedExchangeResponse().getError().getCode());
			System.out.println("Message : "+responseE.getApproveCollectedExchangeResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getApproveCollectedExchangeResponse().getError().getDetail());
//			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
	}
	
	
	private static void reDeliveryExchangeTest() throws RemoteException{//5.3.11
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ReDeliveryExchangeRequest request = new ReDeliveryExchangeRequest();
		ReDeliveryExchangeRequestE requestE = new ReDeliveryExchangeRequestE();
		
		generateSignature("ReDeliveryExchange");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트용 값
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO600000000009");
		request.setReDeliveryMethodCode(DeliveryMethodType.DELIVERY);
		request.setReDeliveryCompanyCode("CJGLS");
		request.setReDeliveryTrackingNumber("12341234");
		
		requestE.setReDeliveryExchangeRequest(request);
		
		ReDeliveryExchangeResponseE responseE = stub.reDeliveryExchange(requestE);
		
		if("SUCCESS".equals(responseE.getReDeliveryExchangeResponse().getResponseType())){
			System.out.println(responseE.getReDeliveryExchangeResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getReDeliveryExchangeResponse().getError().getCode());
			System.out.println("Message : "+responseE.getReDeliveryExchangeResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getReDeliveryExchangeResponse().getError().getDetail());
		}
	}
	
	
	private static void rejectReturnTest() throws RemoteException{//5.3.12
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		RejectReturnRequest request = new RejectReturnRequest();
		RejectReturnRequestE requestE = new RejectReturnRequestE();
		
		generateSignature("RejectReturn");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO300000000004");
		request.setRejectDetailContent("10001");
		
		requestE.setRejectReturnRequest(request);
		
		RejectReturnResponseE responseE = stub.rejectReturn(requestE);
		
		if("SUCCESS".equals(responseE.getRejectReturnResponse().getResponseType())){
			System.out.println(responseE.getRejectReturnResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getRejectReturnResponse().getError().getCode());
			System.out.println("Message : "+responseE.getRejectReturnResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getRejectReturnResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
	}

	
	private static void withholdReturnTest() throws RemoteException{//5.3.13
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		WithholdReturnRequest request = new WithholdReturnRequest();
		WithholdReturnRequestE requestE = new WithholdReturnRequestE();
		
		generateSignature("WithholdReturn");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO200000000687");
		request.setReturnHoldCode(HoldbackClassType.ETC);
		request.setReturnHoldDetailContent("delivery");
		
		
		requestE.setWithholdReturnRequest(request);
		
		WithholdReturnResponseE responseE = stub.withholdReturn(requestE);
		
		if("SUCCESS".equals(responseE.getWithholdReturnResponse().getResponseType())){
			System.out.println(responseE.getWithholdReturnResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getWithholdReturnResponse().getError().getCode());
			System.out.println("Message : "+responseE.getWithholdReturnResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getWithholdReturnResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
	}
	
	
	private static void releaseReturnHoldTest() throws RemoteException{//5.3.14
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ReleaseReturnHoldRequest request = new ReleaseReturnHoldRequest();
		ReleaseReturnHoldRequestE requestE = new ReleaseReturnHoldRequestE();
		
		generateSignature("ReleaseReturnHold");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO300000000003");
		
		requestE.setReleaseReturnHoldRequest(request);
		
		ReleaseReturnHoldResponseE responseE = stub.releaseReturnHold(requestE);
		
		if("SUCCESS".equals(responseE.getReleaseReturnHoldResponse().getResponseType())){
			System.out.println(responseE.getReleaseReturnHoldResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getReleaseReturnHoldResponse().getError().getCode());
			System.out.println("Message : "+responseE.getReleaseReturnHoldResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getReleaseReturnHoldResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
		
	}
	
	
	private static void rejectExchangeTest() throws RemoteException{//5.3.15
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		RejectExchangeRequest request = new RejectExchangeRequest();
		RejectExchangeRequestE requestE = new RejectExchangeRequestE();
		
		generateSignature("RejectExchange");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO600000000012");
		request.setRejectDetailContent("재고 부족");
		
		requestE.setRejectExchangeRequest(request);
		
		RejectExchangeResponseE responseE = stub.rejectExchange(requestE);
		
		if("SUCCESS".equals(responseE.getRejectExchangeResponse().getResponseType())){
			System.out.println(responseE.getRejectExchangeResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getRejectExchangeResponse().getError().getCode());
			System.out.println("Message : "+responseE.getRejectExchangeResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getRejectExchangeResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
		
	}

	
	private static void withholdExchangeTest() throws RemoteException{//5.3.16
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		WithholdExchangeRequest request = new WithholdExchangeRequest();
		WithholdExchangeRequestE requestE = new WithholdExchangeRequestE();
		
		generateSignature("WithholdExchange");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO600000000012");
		request.setExchangeHoldCode(HoldbackClassType.EXCHANGE_PRODUCT_NOT_DELIVERED);
		request.setExchangeHoldDetailContent("delivery");
		request.setEtcFeeDemandAmount(0);
		
		requestE.setWithholdExchangeRequest(request);
		
		WithholdExchangeResponseE responseE = stub.withholdExchange(requestE);
		
		if("SUCCESS".equals(responseE.getWithholdExchangeResponse().getResponseType())){
			System.out.println(responseE.getWithholdExchangeResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getWithholdExchangeResponse().getError().getCode());
			System.out.println("Message : "+responseE.getWithholdExchangeResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getWithholdExchangeResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
	}

	
	private static void releaseExchangeHoldTest() throws RemoteException{//5.3.17

		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		ReleaseExchangeHoldRequest request = new ReleaseExchangeHoldRequest();
		ReleaseExchangeHoldRequestE requestE = new ReleaseExchangeHoldRequestE();
		
		generateSignature("ReleaseExchangeHold");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderID("PONO600000000012");
		
		requestE.setReleaseExchangeHoldRequest(request);
		
		ReleaseExchangeHoldResponseE responseE = stub.releaseExchangeHold(requestE);
		
		if("SUCCESS".equals(responseE.getReleaseExchangeHoldResponse().getResponseType())){
			System.out.println(responseE.getReleaseExchangeHoldResponse().getRequestID());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}else{
			System.out.println("Code : "+responseE.getReleaseExchangeHoldResponse().getError().getCode());
			System.out.println("Message : "+responseE.getReleaseExchangeHoldResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getReleaseExchangeHoldResponse().getError().getDetail());
			System.out.println("TIMSTAMP : " + accessCredentioalsType.getTimestamp());
		}
	}
	
	
	private static void getProductOrderInfoListTest() throws Exception{//5.3.1
		
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		SellerServiceStub stub = new SellerServiceStub();
		GetProductOrderInfoListRequest request = new GetProductOrderInfoListRequest();
		GetProductOrderInfoListRequestE requestE = new GetProductOrderInfoListRequestE();
		byte[] decryptedData = null;
		
		NaverSignature naverSignature = ComUtil.paNaverGenerateSignature("GetProductOrderInfoList");
		
		accessCredentioalsType.setAccessLicense(naverSignature.getAccessLicense());
		accessCredentioalsType.setSignature(naverSignature.getSignature());
		accessCredentioalsType.setTimestamp(naverSignature.getTimeStamp());
		
		request.setAccessCredentials(accessCredentioalsType);
		
		//테스트
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("2.0");
		request.setProductOrderIDList(("PONO300000000011").split(","));
		
		requestE.setGetProductOrderInfoListRequest(request);
		
		GetProductOrderInfoListResponseE responseE = stub.getProductOrderInfoList(requestE);
		
		if("SUCCESS".equals(responseE.getGetProductOrderInfoListResponse().getResponseType())){
			System.out.println(responseE.getGetProductOrderInfoListResponse().getRequestID());
			
			System.out.println("ProductID : "+responseE.getGetProductOrderInfoListResponse().getProductOrderInfoList()[0].getProductOrder().getProductID());
			System.out.println("ProductOrderID : "+responseE.getGetProductOrderInfoListResponse().getProductOrderInfoList()[0].getProductOrder().getProductOrderID());
			System.out.println("ProductName : "+responseE.getGetProductOrderInfoListResponse().getProductOrderInfoList()[0].getProductOrder().getProductName());
		
			decryptedData = SimpleCryptLib.decrypt(naverSignature.getEncryptKey(), responseE.getGetProductOrderInfoListResponse().getProductOrderInfoList()[0].getOrder().getOrdererTel1());
			System.out.println("decryptedData : ["     + new String(decryptedData, "UTF-8") + "]"); 
		}else{
			System.out.println("Code : "+responseE.getGetProductOrderInfoListResponse().getError().getCode());
			System.out.println("Message : "+responseE.getGetProductOrderInfoListResponse().getError().getMessage());
			System.out.println("Detail : "+responseE.getGetProductOrderInfoListResponse().getError().getDetail());
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		getChangedProductOrderListTest();
//		placeProductOrderRequestTest();
//		delayProductOrderTest();
//		shipProductOrderTest();
//		cancelSaleTest();
//		approveCancelApplicationTest();
//		requestReturnTest();
//		approveReturnApplicationTest();//41버전
//		approveCollectedExchangeTest();
//		reDeliveryExchangeTest();
//		rejectReturnTest();//5.3.12
//		withholdReturnTest();
//		releaseReturnHoldTest();
//		rejectExchangeTest();
//		withholdExchangeTest();
//		releaseExchangeHoldTest();
//		getProductOrderInfoListTest();
		
	}
}