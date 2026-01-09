package com.cware.api.panaver.Customer;

import java.rmi.RemoteException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AccessCredentialsType;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryRequest;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryRequestE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.AnswerCustomerInquiryResponseE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListRequest;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListRequestE;
import com.cware.api.panaver.Customer.CustomerInquiry.CustomerInquiryCommon.GetCustomerInquiryListResponseE;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;

@SuppressWarnings("unused")
public class CustomerInquirySample {
	
	private static final String serviceName = "CustomerInquiryService"; // 

	//운영
	private static final String accessLicense = "01000100009ac825503f2dc477e5588638a1c26ffb6fe04363d98cabb7d5ca05e2b406b61c"; //accessLicense입력, PDF파일참조
	private static final String secretKey = "AQABAACX1iwPATgXB2uy9ERHZ0TL+tWe/0khzseJ/7uNmAKYew=="; //secretKey입력, PDF파일참조
	
	//개발
//	private static final String accessLicense = "010001000049a79ed9bec98d543aa384d2b38e2440e077d5ff6e2c3cdd2fff7de174080676"; //accessLicense입력, PDF파일참조
//	private static final String secretKey = "AQABAABtHMV1FOalGi833vagMCfm/4tbE4gqENSOvtYX2h6TTQ=="; //secretKey입력, PDF파일참조
	
	
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
	
	private static void getCustomerInquiryListTest() throws RemoteException{
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		CustomerInquiryCommon stub = new CustomerInquiryCommon();
		GetCustomerInquiryListRequest request = new GetCustomerInquiryListRequest();
		GetCustomerInquiryListRequestE requestE = new GetCustomerInquiryListRequestE();
		
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.set(2019, 9, 29, 0, 0, 0);
		to.set(2019, 10, 1, 0, 0, 0);
		
		generateSignature("GetCustomerInquiryList");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("1.0");
//		request.setServiceType(ServiceTypeCode.CHECKOUT);
		request.setServiceType("SHOPN");
		request.setMallID("ncp_1nsmwf_01"); // 
		request.setInquiryTimeFrom(from);
		request.setInquiryTimeTo(to);
		request.setIsAnswered(false);
		
		requestE.setGetCustomerInquiryListRequest(request);
		
		GetCustomerInquiryListResponseE responseE = stub.getCustomerInquiryList(requestE);
		
		if("SUCCESS".equals(responseE.getGetCustomerInquiryListResponse().getResponseType())){
			System.out.println("success : "+responseE.getGetCustomerInquiryListResponse().getCustomerInquiryList().getCustomerInquiry().length);
		}else{
			System.out.println("fail");
			System.out.println("msg : "+responseE.getGetCustomerInquiryListResponse().getError().getMessage());
		}
		
	}
	
	private static void answerCustomerInquiryTest() throws RemoteException{
		AccessCredentialsType accessCredentioalsType = new AccessCredentialsType();
		CustomerInquiryCommon stub = new CustomerInquiryCommon();
		AnswerCustomerInquiryRequest request = new AnswerCustomerInquiryRequest();
		AnswerCustomerInquiryRequestE requestE = new AnswerCustomerInquiryRequestE();
		
		generateSignature("AnswerCustomerInquiry");
		
		accessCredentioalsType.setAccessLicense(accessLicense);
		accessCredentioalsType.setSignature(signature);
		accessCredentioalsType.setTimestamp(timeStamp);
		request.setAccessCredentials(accessCredentioalsType);
		
		request.setRequestID(UUID.randomUUID().toString());
		request.setDetailLevel("Full");
		request.setVersion("1.0");
		request.setMallID("ncp_1nszz2_01");
		request.setServiceType("SHOPN");
		request.setInquiryID("234512143");
		request.setAnswerContent("답변 내용입니다.");
		request.setAnswerContentID("");
		request.setActionType("INSERT");
		request.setAnswerTempleteID("1");
		
		requestE.setAnswerCustomerInquiryRequest(request);
		
		AnswerCustomerInquiryResponseE responseE = stub.AnswerCustomerInquiry(requestE);
		
		if("SUCCESS".equals(responseE.getAnswerCustomerInquiryResponse().getResponseType())){
			System.out.println("success");
		}else{
			System.out.println("fail");
			System.out.println("msg : "+responseE.getAnswerCustomerInquiryResponse().getError().getMessage());
		}
		
	}
	
	public static void main(String[] args) throws Exception {
//		getCustomerInquiryListTest();
		answerCustomerInquiryTest();
		
	}
}
