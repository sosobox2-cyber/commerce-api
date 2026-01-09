package com.cware.netshopping.pagmkt.util.rest;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import com.cware.framework.core.basic.ParamMap;

public class PaGmktDeliveryRest extends PaGmktAbstractRest {

	public String getRequstBody(ParamMap param) throws Exception{
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_02_001": 	//입급확인 전 주문
			map  = createAskPreOrderInfoBody(param);
			break;

		case "IF_PAGMKTAPI_V2_02_002":  //주문 조회
		case "IF_PAGMKTAPI_V2_02_002C": //수추 확인 조회
			map  = createAskOrderInfoBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_02_003": //주문 확인
			map  = createConfirmOrderInfoBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_02_004": //발송예정일 등록 및 갱신
			map  = createChangingDelyDateBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_02_005": //배송 송장등록
			map	 = createAddingDelyNoBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_02_006": //배송 완료등록
			map	 = createToCompleteDelyBody(param);
			break;
		
		case "IF_PAGMKTAPI_V2_02_009": //미수령신고 조회
			map	 = createNotReceivedBody(param);
			break;
			
		case "IF_PAGMKTAPI_V2_02_010": //미수령신고 철회요청
			map	 = createNotReceivedReleseBody(param);
			break;	
			
		default :	
			break;
		
		}
		return mapToJson(map);
		
	}
	
	//5.2 주문 조회
	//[POST]https://sa.esmplus.com/shipping/v1/Order/PreRequestOrders
	private Map<String,Object> createAskPreOrderInfoBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("siteType"		, param.getString("siteType"));
		resultMap.put("requestDateFrom"	, gDate(param.getString("FROM_DATE")));
		resultMap.put("requestDateTo"	, gDate(param.getString("TO_DATE")));
		resultMap.put("orderNo"			, 0);
		resultMap.put("branchCode"		, 0);
		resultMap.put("pageSize"		, 0);
		resultMap.put("pageIndex"		, 0);

		return resultMap;
	}
		
	//5.2 주문 조회
	//[POST]https://sa.esmplus.com/shipping/v1/Order/RequestOrders
	private Map<String,Object> createAskOrderInfoBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		
		//Timestamp fromDate = DateUtil.toTimestamp((param.getString("FROM_DATE")), "yyyy/MM/dd HH:mm:ss");
		resultMap.put("requestDateFrom" , gDate(param.getString("FROM_DATE")));
		resultMap.put("requestDateTo"   , gDate(param.getString("TO_DATE")));
		resultMap.put("siteType"		, param.getString("siteType")); 	 //TODO - 이것에 대한 처리 필요  //사이트 조회기준 구분  //1 : A , 2 : G/G9 , 3 - G,  4- G9
		resultMap.put("orderStatus"     , param.getInt("ORDER_STATUS"));//param.getString("ORDER_STATUS")//주문 상태별 조회구분  //0:주문번호 별 조회, 1:신규주문(주문확인 안된건만  조회), 2:발송 처리 대상(주문 확인건) , 3:배송중 주문 , 4:배송완료상태인 주문만 조회 , 5: 구매결정완료 주문
		resultMap.put("requestDateType" , param.getInt("REQUEST_TYPE"));//param.getString("REQUEST_TYPE")  // 조회기준 구분 1: 주문일, 2: 결제완료일, 3.발송마감일, 4.선물수락일, 5.일반주문:결제완료일 /선물주문:선물수락일
		resultMap.put("orderNo"			, param.getLong("ORDER_NO") );
		resultMap.put("pageIndex"		, param.getInt(("PAGE_INDEX")));
		resultMap.put("branchCode"		, 0 );
		resultMap.put("pageSize"		, 0);
		return resultMap;
	}
		
	//5.3 주문 확인
	//[POST]https://sa.esmplus.com/shipping/v1/Order/OrderCheck/{OrderNo}
	private Map<String,Object> createConfirmOrderInfoBody(ParamMap param){
		return null;
	}
	
	//5.4 발송예정일 등록 및 갱신
	//[POST]https://sa.esmplus.com/shipping/v1/Order/ShippingExpectedDate
	private Map<String,Object> createChangingDelyDateBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("OrderNo"					, param.getLong("ORDER_NO")); 			    //주문 번호
		resultMap.put("ReasonType"				, param.getInt("REASON_TYPE"));         	//지연 사유 - 1:상품준비중, 2:고객요청, 3:기타
		resultMap.put("ShippingExpectedDate"	, (param.getString("DELY_DATE")));//발송 예정일
		resultMap.put("ReasonDetail"			, param.getString("REASON_DETAIL"));	//상세 사유
		
		return resultMap;
	}
	
	//5.5 배송 송장등록
	//[POST]https://sa.esmplus.com/shipping/v1/Delivery/ShippingInfo
	public Map<String,Object> createAddingDelyNoBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("OrderNo"				, param.getLong("ORDER_NO"));		//
		resultMap.put("ShippingDate"		, param.getString("DELY_DATE"));	//	
		resultMap.put("DeliveryCompanyCode"	, param.getInt("DELY_COMPANY"));//	
		resultMap.put("InvoiceNo"			, param.getString("DELY_NO"));//
		
		return resultMap;
	}
	
	//5.6 배송완료 처리
	//[POST]https://sa.esmplus.com/shipping/v1/Delivery/AddShippingCompleteInfo/{OrderNo}
	public Map<String, Object> createToCompleteDelyBody(ParamMap param){
		return null;
	}
			
	//5.9 미수령신고조회
	//[POST]https://sa.esmplus.com/shipping/v1/Delivery/ClaimList
	public Map<String, Object> createNotReceivedBody(ParamMap param) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("SearchType"		, param.getString("TYPE"));	// 조회기준구분 - 주문번호 조회 : 0 , 미수령 신고일 조회 : 1
		resultMap.put("OrderNo"			, param.getString("CONTR_NO"));
		resultMap.put("StartDate"		, yymmddDate(param.getString("FROM_DATE")));//
		resultMap.put("EndDate"			, yymmddDate(param.getString("TO_DATE")));//
		
		
		return resultMap;
	}
	
	//5.10 미수령신고 철회요청
	//[POST]https://sa.esmplus.com/shipping/v1/Delivery/ClaimRelease
	public Map<String, Object> createNotReceivedReleseBody(ParamMap param){
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("OrderNo"			, param.getLong("ORDER_NO"));
		resultMap.put("ClaimCancelType"	, param.getInt("DELY_TYPE"));// 1: 송장이 들어감, 2: 송장이들어가지않음
		resultMap.put("DeliveryCompCode", param.getInt("DELY_CODE"));// 택배사 코드
		resultMap.put("InvoiceNo"		, param.getString("DELY_NO"));// 송장 이름
		resultMap.put("CancelComment"	, param.getString("REASON"));// 불만 사항
				
		return resultMap;
	}
	
	//= 단위테스트용
	public String getJsonObject4Testing() throws JSONException{
		
		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		
		JSONObject sObject1 = new JSONObject();
		sObject1.put("OrderStatus","T1234");
		sObject1.put("PayNo","10000");     //primary Key
		sObject1.put("GroupNo","1");
		sObject1.put("OrderNo","01062987099");
		
		sObject1.put("OrderDate",		"2018-06-14T07:53:19.093Z");
		sObject1.put("PayDate",  		"2018-06-14T07:53:19.093Z");
		sObject1.put("OrderConfirmDate","2018-06-14T07:53:19.093Z");
		sObject1.put("TransDate",		"2018-06-14T07:53:19.093Z");
		sObject1.put("TransDueDate",	"2018-06-14T07:53:19.093Z");
		sObject1.put("TransCompleteDate","2018-06-14T07:53:19.093Z");
		sObject1.put("BuyDecisionDate",  "2018-06-14T07:53:19.093Z");
		sObject1.put("TransType","TT");
		sObject1.put("GoodsNo","GN");
		sObject1.put("SiteGoodsNo","SGN");
		sObject1.put("OutGoodsNo", "OGN");
		sObject1.put("GoodsName", "GN");
		sObject1.put("SalePrice","1");
		sObject1.put("ContrAmount",1);
		sObject1.put("OrderAmount","1");
		
		
		sObject1.put("AcntMoney","1");
		sObject1.put("SellerDiscountPrice1","1");
		sObject1.put("SellerDiscountPrice2","1");
		sObject1.put("SellerDiscountPrice", "1");
		sObject1.put("DirectDiscountPrice", "1");
		sObject1.put("SellerCommission",	"1");
		sObject1.put("CostPrice",			"1");
		sObject1.put("ShippingFee",			"1");
		sObject1.put("DeliveryFeeCondition","DF");
		sObject1.put("BackwoodsAddDeliveryFee", "1");
		sObject1.put("JejuAddDeliveryFee",		"1");
		sObject1.put("SettlementPrice",			"1");
		sObject1.put("ServiceFee","전시훈");
		sObject1.put("SellerCashbackMoney","1");
		sObject1.put("SinglePayDcAmnt","1");
		sObject1.put("MultiBuyDcAmnt","1");
		sObject1.put("GreatMembDcAmnt","1");
		sObject1.put("OptSelPrice","1");
		sObject1.put("OptAddPrice","1");
		
		sObject1.put("BuyerName","전태환");
		sObject1.put("BuyerId","BI");
		sObject1.put("BuyerMobileTel","01062987099");
		sObject1.put("BuyerTel","01062987099");
		sObject1.put("ReceiverName","전태환");
		sObject1.put("HpNo","01062987099");
		sObject1.put("TelNo","01062987099");
		sObject1.put("ZipCode","0900");
		
		sObject1.put("DelFrontAddress","서울시 강북구");
		sObject1.put("DelBackAddress","삼각산동 프로즌 스론");
		sObject1.put("DelFullAddress","서울시 강북구 삼각산동 프로즌 스론");
		sObject1.put("DelMemo","DEL");
		sObject1.put("AllocationStartDate","2018-06-14T07:53:19.093Z");
		sObject1.put("AllocationEndDate",  "2018-06-14T07:53:19.093Z");
		sObject1.put("DeliverySlotId",0);
		sObject1.put("BranchPrice","1");
		sObject1.put("ReplaceYn","Y");
		sObject1.put("TakbaeName","DELYNAM");
		sObject1.put("NoSongjang","DELYNO");
		sObject1.put("OverseaTransYn","N");
		sObject1.put("GlobalSellerYn","N");
		sObject1.put("OutOrderNo","OON");
		sObject1.put("InfoCin","IFC");
		sObject1.put("OptionGoods","OG");
		sObject1.put("InventoryNo","IVN");
		sObject1.put("SKUNo","SKU");
		sObject1.put("SKUAmount",0);
		
		JSONObject sObject2 = new JSONObject();
		sObject2.put("ItemOptionValue","IOV");
		sObject2.put("ItemOptionOrderCnt","0");
		sObject2.put("ItemOptionCode","IOC");
		sObject1.put("ItemOptionSelectList", sObject2);
		jArray.put(sObject1);
		
		JSONObject sObject3 = new JSONObject();
		sObject3.put("ItemOptionValue","IOV");
		sObject3.put("ItemOptionOrderCnt","0");
		sObject3.put("ItemOptionCode","IOC");
		sObject1.put("ItemOptionAdditionList", sObject3);
		jArray.put(sObject1);
		
		
		obj.put("RequestOrders", jArray);
		
		ResponseEntity<String> response = getJSONResponse(obj);
		return response.getBody();
	}
	
}
