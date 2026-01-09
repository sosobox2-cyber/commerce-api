package com.cware.netshopping.pagmkt.util.rest;

import java.util.HashMap;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;

public class PaGmktExchangeRest extends PaGmktAbstractRest {

	
	@Override
	public String getRequstBody(ParamMap param) throws Exception {
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		
		switch(apiCode){
		
		case "IF_PAGMKTAPI_V2_03_010": //교환 조회
			map  = createAskExchangeBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_03_011": //교환 수거 송장 등록
			map  = createRegistDelyInfoBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_03_012": //교환 수거 완료 처리
			map  = createDelyCompleteBody(param);
			break;	
		
		case "IF_PAGMKTAPI_V2_03_013": //교환보류 처리
			map  = createExchangeReserveBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_03_014": //교환 재발송 송장 등록
			map  = createExchangeShippingBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_03_015": //교환 재발송 배송 완료 처리
			map  = createExchangeShippingCompleteBody(param);
			break;
	
		case "IF_PAGMKTAPI_V2_03_016": //교환 보류 해제
			map  = createCancelExchangeReserveBody(param);
			break;

		case "IF_PAGMKTAPI_V2_03_017": //교환건 반품전환
			map  = createRevertChangeReturnBody(param);
			break;
			
		default :	
			break;
		
		}
		return mapToJson(map);
	}
	
	
	
	//6.10 교환 조회
	//[GET] https://sa.esmplus.com/claim/v1/sa/Exchanges - get의경우 body가 불필요
	//[POST]https://sa.esmplus.com/claim/v1/sa/Exchanges - post의경우에만 body 필요
	public Map<String,Object> createAskExchangeBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("PayNo"		, param.getLong("PAY_NO"));		//장바구니번호(결제번호) RequestExchange  	PackNo
		resultMap.put("OrderNo"		, param.getLong("CONTR_NO"));	//주문번호(신규)
		resultMap.put("Type"		, param.getInt("TYPE"));		//조회기준 구분	"0:주문번호, 1.장바구니번호	2:교환신청일,3:교환완료일,(교환 상태가 완료, 철회인 건)	4:교환보류일"(신규)
		resultMap.put("StartDate"	, yymmddDate((param.getString("FROM_DATE"))));		//조회기준 시작일	2018-02-01T08:43:21.825Z	RequestExchange  	SearchStartDate
		resultMap.put("EndDate"		, yymmddDate((param.getString("TO_DATE"))));		//조회기준 종료일 2018-02-01T08:43:21.825Z (조회 기간은 PD협의 후 처리 가능한 날짜 범위 결정)	RequestExchange  	SearchEndDate
		resultMap.put("SiteType"	, param.getInt("siteType"));						//사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("ExchangeStatus", param.getInt("EXCHANGE_STATUS"));	//교환상태,"0:전체,1:교환요청,	2:교환수거완료,	3:교환보류,	4:교환완료,	5:교환철회"	RequestExchange  	ClaimStatus
		
		return resultMap;
	}
	
	//6.11 교환수거 송장등록
	//[POST] https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/pickup
	public Map<String,Object> createRegistDelyInfoBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("DeliveryCompCode"	, param.getString("DELY_CODE"));//택배사 코드
		resultMap.put("InvoiceNo"			, param.getString("DELY_NO"));	//송장 번호
		resultMap.put("SiteType"			, param.getInt("siteType2")); 	//처리하려는 주문번호 사이트구분 선택 1:옥션 2:G마켓 +G9 3:G9 
				
		return resultMap;
	}
	
	//6.12 교환수거 완료처리
	//[POST] https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/pickup
	public Map<String,Object> createDelyCompleteBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("PickupCompleteDate"	, param.getString("DELY_COMPLETE_DATE")); //수거완료일 YYYY-MM-DD
		resultMap.put("SiteType"			, param.getInt("siteType2")); 			 //처리하려는 주문번호 사이트구분 선택 1:옥션 2:G마켓 +G9 3:G9 
				
		return resultMap;
	}
	
	//6.13 교환보류 처리
	//[POST]https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/hold
	public Map<String,Object> createExchangeReserveBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType"			, param.getInt("siteType2"));			//사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("HoldReason"			, param.getString("REASON"));			//유보사유 "0 : 기타유보사유 1 : 교환배송비청구	4 : 교환입고미확인"	AddReserveExchange	ReserveType
		resultMap.put("HoldReasonDetail"	, param.getString("REASON_DETAIL"));	//유보사유 "0 : 기타유보사유 1 : 교환배송비청구	4 : 교환입고미확인"	AddReserveExchange	ReserveType

		return resultMap;
	}
	
	//6.14 교환재발송 송장등록
	//[POST]https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/resend
	public Map<String,Object> createExchangeShippingBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType"		, param.getInt("siteType2"));//Y,사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("DeliveryCompCode", param.getInt("DELY_CODE"));//	
		resultMap.put("InvoiceNo"		, param.getString("DELY_NO"));//
		
		return resultMap;
	}
	
	//6.15 교환재발송 배송완료 처리
	//[PUT]https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/resend
	public Map<String,Object> createExchangeShippingCompleteBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("PickupCompleteDate", param.getString("DELY_COMPLETE_DATE")); //수거완료일 YYYY-MM-DD
		resultMap.put("SiteType"		  , param.getInt("siteType2"));				//사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)
		
		return resultMap;
	}
	
	//6.17 교환건 반품전환
	//[POST]https://sa.esmplus.com/claim/v1/sa/exchange/{orderno}/return
	public Map<String,Object> createRevertChangeReturnBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType", param.getInt("siteType2"));		//사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)			
		return resultMap;
	}
		
	//6.17 교환건 반품전환
	//[DELETE]https://sa.esmplus.com/claim/v1/sa/exchange/{orderNo}/hold
	public Map<String,Object> createCancelExchangeReserveBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType", param.getInt("siteType2"));		//사이트구분,"1:A 2:G/G9 3:G마켓 4:G9"(신규)
		return resultMap;
	}


}
