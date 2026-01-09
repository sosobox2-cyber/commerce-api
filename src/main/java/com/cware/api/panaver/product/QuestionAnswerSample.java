package com.cware.api.panaver.product;

import java.security.SignatureException;
import java.util.UUID;

import com.cware.api.panaver.product.type.GetQuestionAnswerListRequestType;
import com.cware.api.panaver.product.type.GetQuestionAnswerListResponseType;
import com.cware.api.panaver.product.type.ManageQuestionAnswerRequestType;
import com.cware.api.panaver.product.type.ManageQuestionAnswerResponseType;
import com.cware.api.panaver.product.type.QuestionAnswerService;
import com.cware.api.panaver.product.type.QuestionAnswerServicePortType;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;

@SuppressWarnings("unused")
public class QuestionAnswerSample extends SampleBase{
	
	private static String mallId = "ncp_1nsmwf_01";//개발 : ncp_1njgo6_02 , 운영 : ncp_1nsmwf_01
			
	private static void getQuestionAnswerListTest() throws SignatureException{
		String startDate = "";
		String endDate = "";
		endDate = DateUtil.getCurrentNaverDateAsString();
		startDate = DateUtil.addDay(endDate, -2, DateUtil.NAVER_DATE_FORMAT);
		
		GetQuestionAnswerListRequestType request = new GetQuestionAnswerListRequestType();
		request.setRequestID(UUID.randomUUID().toString());
		request.setVersion("2.0");
		request.setSellerId(mallId);
//		request.setFromDate(startDate);
//		request.setToDate(endDate);
		request.setFromDate("2019-10-21");
		request.setToDate("2019-10-23");
//		request.setAnswered("N");
//		request.setPage(1);
		request.setAccessCredentials(createAccessCredentials("QuestionAnswerService", "GetQuestionAnswerList"));
		
		QuestionAnswerService questionAnswerService = new QuestionAnswerService();
		QuestionAnswerServicePortType port = questionAnswerService.getQuestionAnswerServiceSOAP11PortHttp();
		GetQuestionAnswerListResponseType response = port.getQuestionAnswerList(request);
		
		if("SUCCESS".equals(response.getResponseType())){
			System.out.println("getQuestionAnswerList : " + response.getQuestionAnswerList().get(0));
		}else{
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	private static void manageQuestionAnswerTest() throws SignatureException{
		ManageQuestionAnswerRequestType request = new ManageQuestionAnswerRequestType();
		request.setRequestID(UUID.randomUUID().toString());
		request.setVersion("2.0");
		request.setSellerId("ncp_1nsmwf_01");
		request.setQuestionAnswerId(93244408);
		request.setAnswer("고객님 답변 완료 처리함");
		request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("QuestionAnswerService", "ManageQuestionAnswer"));
		
		QuestionAnswerService questionAnswerService = new QuestionAnswerService();
		QuestionAnswerServicePortType port = questionAnswerService.getQuestionAnswerServiceSOAP11PortHttp();
		ManageQuestionAnswerResponseType response = port.manageQuestionAnswer(request);
		
		if("SUCCESS".equals(response.getResponseType())){
			System.out.println("success");
		}else{
			System.out.println("에러 메시지 : [" + response.getError().getMessage() + "]");
			System.out.println("에러 코드 : [" + response.getError().getCode() + "]");
			System.out.println("에러 상세정보 : [" + response.getError().getDetail() + "]");
		}
	}
	
	public static void main(String[] args) throws SignatureException{
//		getQuestionAnswerListTest();
		manageQuestionAnswerTest();
	}
}
