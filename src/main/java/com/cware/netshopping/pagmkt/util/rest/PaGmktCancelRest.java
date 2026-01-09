package com.cware.netshopping.pagmkt.util.rest;
import java.util.HashMap;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;

public class PaGmktCancelRest extends PaGmktAbstractRest{
	

	
	public String getRequstBody(ParamMap param) throws Exception{
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		
		switch(apiCode){
			case "IF_PAGMKTAPI_V2_03_001": //취소 조회
				map  = createAskCancelInfo(param);
				break;
			
			case "IF_PAGMKTAPI_V2_03_002": //취소 승인
				map  = createConfrimCancelInfo(param);
				break;
				
			case "IF_PAGMKTAPI_V2_03_002B"://취소 승인(BO호출)
				map  = createConfrimCancelInfo(param);
				break;
			
			case "IF_PAGMKTAPI_V2_03_003": //6.3 판매 취소-상품품절처리
				//map  = createRefusalOrderForSoldOutInfo(param);
				map  = createRefusalOrder(param);
				break;
				
			case "IF_PAGMKTAPI_V2_03_004": //6.4 판매 취소-상품비품절처리
				map  = createRefusalOrder(param);
				break;
			
			case "IF_PAGMKTAPI_V2_03_021": // 모바일자동취소
				map  = createRefusalOrder(param);
				break;

			default :
				break;
		}		
		
		return mapToJson(map);

	
	}
	
	//6.1 취소 조회
	//[GET/POST]https://sa.esmplus.com/claim/v1/sa/Cancels
	private Map<String,Object> createAskCancelInfo(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("CancelStatus",    param.getInt("CLAIM_TYPE")); //취소상태별 조회  0:전체, 1:취소요청,	2:취소중, 3:취소완료, 4:취소철회 5:이베이 직권환불건(취소완료건 중, 고객센터에서 환불처리한 Case) 6.옥션송금후취소(구매결정 후, 바로 환불건) 
		resultMap.put("OrderNo", 		 param.getLong("ORDER_NO"));
		resultMap.put("PayNo"  ,		 param.getLong("PAY_NO"));
		resultMap.put("Type", 			 param.getInt("TYPE"));
		resultMap.put("StartDate", 		 param.getString("FROM_DATE").substring(0,10));
		resultMap.put("EndDate",		 param.getString("TO_DATE").substring(0,10));
		resultMap.put("SiteType", 		 param.getString("siteType")); 	 //TODO - 이것에 대한 처리 필요  //사이트 조회기준 구분  //1 : A , 2 : G/G9 , 3 - G,  4- G9
		return resultMap;
	}
	
	
	//6.2 취소 승인
	//[PUT]https://sa.esmplus.com/claim/v1/sa/Cancel/{OrderNo}
	private Map<String,Object> createConfrimCancelInfo(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType"	,	param.getString("siteType2")); //처리하려는 주문번호 사이트구분 선택 1:옥션 2:G마켓 +G9 3:G9 
		return resultMap;
		
	}
	
	//6.3 판매 취소
	//[POST]https://sa.esmplus.com/claim/v1/sa/Cancel/{OrderNo}   + /SoldOut
	private Map<String,Object> createRefusalOrder(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType"	, 	param.getString("siteType2")); //처리하려는 주문번호 사이트구분 선택 1:옥션 2:G마켓 +G9 3:G9 
		return resultMap;
	}
		
		
	/*
	private Map<String,Object> createRefusalOrderForSoldOutInfo(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SiteType",2); //처리하려는 주문번호 사이트구분 선택 1:옥션 2:G마켓 +G9 3:G9 
		return resultMap;
	}
	*/
}
