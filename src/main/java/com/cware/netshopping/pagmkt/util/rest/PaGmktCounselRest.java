package com.cware.netshopping.pagmkt.util.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cware.framework.core.basic.ParamMap;

public class PaGmktCounselRest extends PaGmktAbstractRest {
	
	
	@Override
	public String getRequstBody(ParamMap param) {
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_05_001" ://ESM공지사항
			map = createNoticeBody(param);
			break;
		case "IF_PAGMKTAPI_V2_05_002" ://고객게시판 문의 조회
			map = createQnaBody(param);
			break;
		case "IF_PAGMKTAPI_V2_05_003" ://고객게시판 문의 답변
			map = createQnaAnswerBody(param);
			break;
		case "IF_PAGMKTAPI_V2_05_004" ://고객 긴급메세지 조회
		case "IF_PAGMKTAPI_V2_05_006" :
			map = createEmerQnaBody();
			break;
		case "IF_PAGMKTAPI_V2_05_005" ://고객 긴급메세지 답변
		case "IF_PAGMKTAPI_V2_05_007" :
			map = createEmerQnaAnswerBody(param);
			break;
		default :	
			break;
		
		}
		return mapToJson(map);
	}
	
	//8.1 ESM공지사항
	//[POST]https://sa.esmplus.com/item/v1/communications/notices
	private Map<String,Object> createNoticeBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		params.put("siteType", 1);//Y, 사이트구분, 1.ESM+(전체) 2.옥션 3.G마켓
		params.put("startDate", gDate(requestMap.getString("FROM_DATE")));//Y,조회기준 시작일 1개월 단위로 조회 가능
		params.put("endDate", gDate(requestMap.getString("TO_DATE")));//Y,조회기준 종료일

		return params;
	}
	
	
	//8.2 고객게시판 문의 조회
	//[POST]https://sa.esmplus.com/item/v1/communications/customer/bulletin-board
	private Map<String,Object> createQnaBody(ParamMap requestMap){
		Date d = new Date();
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -6);
		
		String endDate = date.format(d);
		String startDate = date.format(cal.getTime());  
		
		Map<String, Object> params = new HashMap<>();
		params.put("status", 2);//Y,처리상태,1.전체 2: 미처리 3: 처리완료(신규)
		if (requestMap.get("count").toString().equals("0")) {
			params.put("qnaType", 3);//Y,조회구분,1:옥션게시판 2:옥션쪽지 3:G마켓게시판(신규)
		} else if (requestMap.get("count").toString().equals("1")) {
			params.put("qnaType", 1);//Y,조회구분,1:옥션게시판 2:옥션쪽지 3:G마켓게시판(신규)
		} else if (requestMap.get("count").toString().equals("2")) {
			params.put("qnaType", 2);//Y,조회구분,1:옥션게시판 2:옥션쪽지 3:G마켓게시판(신규)
		}
		params.put("type", 1);//Y,조회기준구분,1:접수일(신규)
		params.put("startDate", startDate);//Y,조회기준 시작일 7일 단위로 조회 가능,RequestSellerQnA 	StartDate
		params.put("endDate", endDate);//Y,조회기준 종료일,RequestSellerQnA 	EndDate
		
		/*
		통신테스트
		ResponseEntity<String> response = restUtil.connectServer("https://sa.esmplus.com/item/v1/communications/customer/bulletin-board",HttpMethod.POST,body);
		Map<String,Object> resultMap = restUtil.splitJson(response.getBody());
		*/
		return params;
	}
	
	//8.3 고객게시판 문의 답변
	//[POST]https://sa.esmplus.com/item/v1/communications/customer/bulletin-board/qna
	private Map<String,Object> createQnaAnswerBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		params.put("title", "답변입니다");//2.3버전 추가
		params.put("comments", requestMap.get("procNote").toString());//Y,답변내용,ResponseSellerQnA	Contents
		params.put("token", requestMap.get("token").toString());//Y,조회 시, 내려오는 토큰번호를 저장하여 답변 시 문의번호와 함께 요청되어야 함 (신규)
		params.put("messageNo", requestMap.get("messageNo").toString());//Y,문의번호,ResponseSellerQnA	MessageNo
		params.put("answerstatus", 2);//Y,답변상태	"1.처리중2.처리완료"(신규)

		return params;
	}
	
	//8.4 고객 긴급메세지 조회
	//[POST]https://sa.esmplus.com/item/v1/communications/customer/emergency-notification
	private Map<String,Object> createEmerQnaBody(){
		Date d = new Date();
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -6);
		
		String endDate = date.format(d);
		String startDate = date.format(cal.getTime()); 
		
		Map<String, Object> params = new HashMap<>();
		
		params.put("status", 1);//Y,처리상태,1.전체 2: 미처리 3: 처리완료(신규)
		params.put("type", 1);//Y,조회기준구분,1:접수일(신규)
		params.put("startDate", startDate);//Y,조회기준 시작일 7일 단위로 조회 가능,RequestUrgency 	StartDate
		params.put("endDate", endDate);//Y,조회기준 종료일,RequestUrgency 	EndDate

		return params;
	}
	
	//8.5 고객 긴급메세지 답변
	//[POST]https://sa.esmplus.com/item/v1/communications/customer/emergency-notification/qna
	private Map<String,Object> createEmerQnaAnswerBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		params.put("comments", requestMap.get("procNote").toString());//Y,답변내용,ResponseUrgency 	Comments
		if (requestMap.get("count").toString().equals("0")) {
			params.put("token", requestMap.get("token").toString());//Y,조회 시, 내려오는 토큰번호를 저장하여 답변 시 문의번호와 함께 요청되어야 함 (신규)
		}
		params.put("emerMessageNo", requestMap.get("emerMessageNo").toString());//Y,문의번호,ResponseUrgency 	MessageNo
		params.put("answerstatus", 2);//Y,답변상태	"1.처리중2.처리완료"(신규)
		
		return params;
	}
}
