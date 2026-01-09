package com.cware.netshopping.pagmkt.util.rest;

import java.util.HashMap;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;

public class PaGmktClaimRest extends PaGmktAbstractRest {
	

	@Override
	public String getRequstBody(ParamMap param) throws Exception{
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		
		switch(apiCode){
		
		case "IF_PAGMKTAPI_V2_03_005": //반품 조회
			map  = createReturnListBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_03_006": //반품 수거송장 등록
			map  = createReturnPickupBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_03_007": //반품 보류 처리
			map  = createReturnHoldBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_03_008": //반품 승인
			map	 = createReturnBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_03_009": //반품건 교환전환
			map	 = null;//TODO API-명세서에 해당 내역없음
			break;
		
		default :	
			break;
		
		}
		return mapToJson(map);
		
	}
	
	
	//6.5 반품조회
	//[POST]https://sa.esmplus.com/claim/v1/sa/Returns
	private Map<String,Object> createReturnListBody(ParamMap param) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("PayNo", 		  param.getLong("PAY_NO"));			//장바구니번호(결제번호)(G유지/A신규)
		resultMap.put("OrderNo", 	  param.getLong("ORDER_NO"));	 	//주문번호G신규/A유지
		resultMap.put("ReturnStatus", param.getInt("RETURN_STATUS"));	//반품상태, "1:반품요청, 2:반품수거완료, 3:반품환불보류, 4:반품환불완료, 5:반품철회 6:이베이 직권환불건(반품완료건 중, 고객센터에서 환불처리한 Case)"(ClaimStatusEnum)
		resultMap.put("Type", 		  param.getInt("TYPE"));			//조회기준 구분, "0: 주문번호 1: 장바구니번호 2:반품신청일 3:반품완료일(반품상태가 완료/철회인건) 4.결제완료일"(신규)
		resultMap.put("SiteType", 	  param.getInt("siteType"));		//사이트구분, "1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("StartDate", 	  yymmddDate(param.getString("FROM_DATE")));	//조회기준 시작일
		resultMap.put("EndDate",   	  yymmddDate(param.getString("TO_DATE")));	//조회기준 종료일
		
		return resultMap;	
	}
	
	//6.6 반품수거 송장 등록
	//[POST]https://sa.esmplus.com/claim/v1/sa/return/{orderNo}/pickup
	private Map<String,Object> createReturnPickupBody(ParamMap param) {
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("SiteType", 	  param.getInt("siteType"));			//사이트구분, "1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("DeliveryCompCode", param.getString("DELY_CODE"));	//수거택배사코드(신규)
		resultMap.put("InvoiceNo", 		  param.getString("DELY_NO"));		//수거송장번호(InvoiceNo)
	
		return resultMap;	
	}
	
	//6.7 반품보류 처리
	//[POST]https://sa.esmplus.com/claim/v1/sa/return/{orderNo}/hold
	private Map<String,Object> createReturnHoldBody(ParamMap param) {
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("SiteType", 	  param.getInt("siteType"));			//사이트구분, "1:A 2:G/G9 3:G마켓 4:G9"(신규)
		resultMap.put("HoldReason", 	   param.getString("REASON"));		//유보사유, "0: 기타유보사유 	2 : 추가반품비청구(기타반품비) 4 : 반품미입고"(ReserveType)
		resultMap.put("HoldReasonDetail",  param.getString("REASON_DT"));	//유보상세사유(ReserveReason)
		resultMap.put("ReturnShippingFee", param.getString("ADD_SHPPING_FEE"));	//추가부과금액, "유보사유 > 추가반품비청구일 경우 필수	0원으로 입력 시, 기본 설정된 반품배송비가 청구됨"(ReserveFee)
		
		return resultMap;	
	}
	
	//6.8 반품승인
	//[POST]https://sa.esmplus.com/claim/v1/sa/return/{orderNo}/hold
	private Map<String,Object> createReturnBody(ParamMap param) {
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("SiteType", 	  param.getInt("siteType"));	// 사이트구분, "1:A 2:G/G9 3:G마켓 4:G9"(신규)
		return resultMap;	
	}
	
	
	
	

}
