package com.cware.netshopping.pagmkt.util.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import com.cware.framework.core.basic.ParamMap;

public class PaGmktCommonRest extends PaGmktAbstractRest{
	
	@Override
	public String getRequstBody(ParamMap param) {
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_00_005" ://판매자주소록 등록
		case "IF_PAGMKTAPI_V2_00_006" ://판매자주소록 수정
			map = createSellerAddrInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_00_008" ://출하지 등록
		case "IF_PAGMKTAPI_V2_00_009" ://출하지 수정
			map = createShippingInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_00_011" ://묶음배송비 정책 등록
		case "IF_PAGMKTAPI_V2_00_012" ://묶음배송비 정책 수정
			map = createShippingPoliciesInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_04_001" ://정산 판매대금 조회
			map = createSettleOrderInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_04_002" ://정산 배송비 조회
			map = createSettleDeliveryInsertBody(param);
			break;
		default :	
			break;
		
		}
		return mapToJson(map);
	}
	
	
	//3.5 판매자 주소록 등록
	//[POST]https://sa.esmplus.com/item/v1/sellers/address
	private Map<String,Object> createSellerAddrInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		Map<String, Object> subMap = (Map<String, Object>) requestMap.get("map");
		
		params.put("addrName", requestMap.get("realYn") + "_" + subMap.get("ENTP_CODE")+"_"+subMap.get("ENTP_NAME")+"_"+subMap.get("ENTP_MAN_SEQ"));//Y,주소명,판매자가 관리하는 해당 주소 관리명,AddAddressBook	AddressTitle
		params.put("representativeName", "SK스토아");//Y,주소이름,고객에게 반품 수취인으로 노출되는 판매자명,AddAddressBook	Name
		params.put("zipCode", subMap.get("POST_NO"));//Y,우편번호,"우편번호 5자리/6자리 등록 가능(단, 5자리 우편번호로 입력 권장),우편번호 6자리 등록 시, 하이픈 제거하고 입력",AddAddressBook	Zipcode
		
		if(subMap.get("ROAD_ADDR_YN").equals("1")){
			params.put("addr1", subMap.get("ROAD_POST_ADDR"));//Y,주소1(우편번호기준주소),"주소등록 Validation Check 확정 후 기재우편번호 5자리 : 우정국 주소 기준 ""시도+시군구+도로명+신군구용건물명""",AddAddressBook	Address1
			params.put("addr2", subMap.get("ROAD_ADDR"));//Y,주소2(주소상세),AddAddressBook	Address2
		}else{
			params.put("addr1", subMap.get("POST_ADDR"));//Y,주소1(우편번호기준주소),"주소등록 Validation Check 확정 후 기재우편번호 5자리 : 우정국 주소 기준 ""시도+시군구+도로명+신군구용건물명""",AddAddressBook	Address1
			params.put("addr2", subMap.get("ADDR"));//Y,주소2(주소상세),AddAddressBook	Address2
		}
		
		if(subMap.get("ENTP_MAN_TEL").toString().length() >= 13 ) {
			params.put("homeTel", ((String) subMap.get("ENTP_MAN_TEL")).substring(0,13));//Y,전화번호, 하이픈 입력 필요	AddAddressBook	Phone1			
		}else {
			params.put("homeTel", (subMap.get("ENTP_MAN_TEL")));//Y,전화번호, 하이픈 입력 필요	AddAddressBook	Phone1			
		}
		
		params.put("cellPhone", subMap.get("ENTP_MAN_HP"));//Y,휴대폰번호,하이픈 입력 필요	AddAddressBook	Phone2
		//params.put("locationDescription", "string");//N,위치설명(신규)
		params.put("isVisitAndTakeAddr", false);//Y,기본방문수령지여부(신규)
		params.put("isReturnAddr", false);//Y,기본반품배송지주소여부(신규)
		
		//String body = restUtil.makeJson(params);
		
		return params;
	}
	
	//3.6 판매자 주소록 수정
	//[PUT] https://sa.esmplus.com/item/v1/sellers/address/{addrNo}
	/*public String createSellerAddrUpdateBody(){
		Map<String, Object> params = new HashMap<>();
		
		params.put("addrName", "string");//Y,주소명,판매자가 관리하는 해당 주소 관리명,AddAddressBook	AddressTitle
		params.put("representativeName", "string");//Y,주소이름,고객에게 반품 수취인으로 노출되는 판매자명,AddAddressBook	Name
		params.put("zipCode", "string");//Y,우편번호,"우편번호 5자리/6자리 등록 가능(단, 5자리 우편번호로 입력 권장),우편번호 6자리 등록 시, 하이픈 제거하고 입력",AddAddressBook	Zipcode
		params.put("addr1", "string");//Y,주소1(우편번호기준주소),"주소등록 Validation Check 확정 후 기재우편번호 5자리 : 우정국 주소 기준 ""시도+시군구+도로명+신군구용건물명""",AddAddressBook	Address1
		params.put("addr2", "string");//Y,주소2(주소상세),AddAddressBook	Address2
		params.put("homeTel", "string");//Y,전화번호,하이픈 입력 필요	AddAddressBook	Phone1
		params.put("cellPhone", "string");//Y,휴대폰번호,하이픈 입력 필요	AddAddressBook	Phone2
		params.put("locationDescription", "string");//N,위치설명(신규)
		params.put("isVisitAndTakeAddr", true);//Y,기본방문수령지여부(신규)
		params.put("isReturnAddr", true);//Y,기본반품배송지주소여부(신규)
		
		//String body = restUtil.makeJson(params);
		
		return null;
	}
	*/
	//3.8 출하지 등록
	//[POST] https://sa.esmplus.com/item/v1/shipping/places
	private Map<String,Object> createShippingInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> subMap = (Map<String, Object>) requestMap.get("map");
		
		String finalPlaceName = "";
		
		// 다음 로직은 지마켓 회수지 출고지 관련 분리 안정화가 마무리되면 모두 제거할것  KEY로는 USE_NEW_GMKT_SHIPCOST_YN를 찾으면 됨.. HSBAEK 
		String place = requestMap.getString("USE_NEW_GMKT_SHIPCOST_YN");
		if("Y".equals(place)) {
			DateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date nowDate = new Date();
			String date = sdFormat.format(nowDate);
			finalPlaceName = "_" + date;	
		}
		
		params.put("placeName", requestMap.get("realYn") + "_" + subMap.get("ENTP_CODE")+"_"+ subMap.get("ENTP_MAN_SEQ") + finalPlaceName);//Y, 출하지명
		params.put("addrNo", subMap.get("PA_ADDR_SEQ"));//Y, 판매자주소번호(AddressCode)
		
		int ordCost = Integer.parseInt(subMap.get("ORD_COST").toString());
		int islandCost = Integer.parseInt(subMap.get("ISLAND_COST").toString());
		int jejuCost = Integer.parseInt(subMap.get("JEJU_COST").toString());
		
		int realIslandCost = islandCost-ordCost;
		int realJejuCost = jejuCost-ordCost;
		
		if(realIslandCost <= 0){
			realIslandCost = 0;
		}
		if(realJejuCost <= 0){
			realJejuCost = 0;
		}
		
		if(realIslandCost == 0 && realJejuCost == 0){ 
			//G마켓 유동처리(버그로 false처리해도 금액이 0원이 되지않아 데이터를 넣어줌 - 2018.10.24 thjeon)
			params.put("isSetAdditionalShippingFee", true);//Y, 추가배송비 설정여부, "true : 설정  false : 미설정"(신규)
			params.put("backwoodsAdditionalShippingFee", realIslandCost);//N, 도서 및 기타 산간지방 추가배송비, 금액입력(G신규/A유지)
			params.put("jejuAdditionalShippingFee", realJejuCost);
		}else{
			params.put("isSetAdditionalShippingFee", true);//Y, 추가배송비 설정여부, "true : 설정  false : 미설정"(신규)
			params.put("backwoodsAdditionalShippingFee", realIslandCost);//N, 도서 및 기타 산간지방 추가배송비, 금액입력(G신규/A유지)
			params.put("jejuAdditionalShippingFee", realJejuCost);//N, 제주도 및 부속도서 추가배송비, 금액입력(G신규/A유지)
		}
		params.put("isDefaultShippingPlace", false);//Y, 기본 출하지 여부, "true : 설정	false : 미설정"(G신규/A유지)
		
		//String body = mapToJson(params);
		requestMap.put("backwoodsAdditionalShippingFee"	, realIslandCost 	== 0 ?  ordCost : ordCost + realIslandCost);
		requestMap.put("jejuAdditionalShippingFee"		, realJejuCost 		== 0 ?  ordCost : ordCost + realJejuCost);
		return params;
	}
	
	//3.9 출하지 수정
	//[PUT] https://sa.esmplus.com/item/v1/shipping/places/{placeNo}
	/*public String createShippingUpdateBody(){
		Map<String, Object> params = new HashMap<>();
		
		params.put("placeName", "string");
		params.put("addrNo", 0);
		params.put("isSetAdditionalShippingFee", true);
		params.put("backwoodsAdditionalShippingFee", 0);
		params.put("jejuAdditionalShippingFee", 0);
		params.put("isDefaultShippingPlace", true);
		
		String body = mapToJson(params);
		
		return body;
	}*/
	
	//3.11 묶음배송비 정책 등록
	//[POST] https://sa.esmplus.com/item/v1/shipping/policies
	private Map<String,Object> createShippingPoliciesInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> subMap = (Map<String, Object>) requestMap.get("map");
		
		String shipCostCode = subMap.get("SHIP_COST_CODE").toString();
		if(shipCostCode.substring(0, 2).equals("FR")){//무료
			params.put("feeType", 1);//Y, 정책구분, "무료배송인지 유료/조건부 배송인지 설정  1:무료, 2:유료, 3:조건부"(Shipping>NewItemShipping>FeeCondition)
			params.put("fee", 0);//Y, 배송비금액, 금액입력(Shipping>NewItemShipping>Fee)
		}else if(shipCostCode.substring(0, 2).equals("CN") || shipCostCode.substring(0, 2).equals("PL")){//조건부
			params.put("feeType", 3);//Y, 정책구분, "무료배송인지 유료/조건부 배송인지 설정  1:무료, 2:유료, 3:조건부"(Shipping>NewItemShipping>FeeCondition)
			params.put("fee", subMap.get("ORD_COST_AMT"));//Y, 배송비금액, 금액입력(Shipping>NewItemShipping>Fee)
		}else if(shipCostCode.substring(0, 2).equals("ID")){//상품별
			params.put("feeType", 2);//Y, 정책구분, "무료배송인지 유료/조건부 배송인지 설정  1:무료, 2:유료, 3:조건부"(Shipping>NewItemShipping>FeeCondition)
			params.put("fee", subMap.get("ORD_COST_AMT"));//Y, 배송비금액, 금액입력(Shipping>NewItemShipping>Fee)
		}
		
		params.put("isPrepayment", true);//Y, 배송비 선결제여부, "true : 선결제  false : 선결제아님"(Shipping>NewItemShipping>FeeCondition>PrepayableOnDelivery)
		params.put("isCashOnDelivery", false);//Y, 착불여부, "true : 착불	 false : 착불아님"(Shipping>NewItemShipping>FeeCondition>PayOnDelivery)
		params.put("placeNo", subMap.get("GMKT_SHIP_NO"));//Y, 출하지번호
		params.put("isDefault", false);//Y, 기본배송비 여부, "true : 기본배송비 false : 기본배송비아님"(G신규/A유지)
		
		List<Object> shippingFee = new ArrayList<>();
		Map<String, Object> shippingFeeMap = new HashMap<>();
		
		if(Integer.parseInt(subMap.get("SHIP_COST_BASE_AMT").toString()) < 1000){
			shippingFeeMap.put("condition", 1000);//Y, 조건부기준금액 0원인경우 최소금액으로 넘김
		}else if(Integer.parseInt(subMap.get("SHIP_COST_BASE_AMT").toString()) > 1000000){
			shippingFeeMap.put("condition", 1000000);//Y, 조건부기준금액 0원인경우 최소금액으로 넘김
		} else{
			shippingFeeMap.put("condition", subMap.get("SHIP_COST_BASE_AMT"));//Y, 조건부기준금액, 금액입력(Shipping>NewItemShipping>FeeBasePrice)
		}
		
		shippingFee.add(shippingFeeMap);
		
		params.put("shippingFee", shippingFee);
		
		//String body = mapToJson(params);
		
		return params;
	}
	
	//3.12 묶음배송비 정책 수정
	//[PUT] https://sa.esmplus.com/item/v1/shipping/policies/{policyNo}
	/*public String createShippingPoliciesUpdateBody(){
		Map<String, Object> params = new HashMap<>();
		
		params.put("feeType", 1);
		params.put("fee", 0);
		params.put("isPrepayment", true);
		params.put("isCashOnDelivery", true);
		params.put("placeNo", 0);
		params.put("isDefault", true);
		
		List<Object> shippingFee = new ArrayList<>();
		Map<String, Object> shippingFeeMap = new HashMap<>();
		
		shippingFeeMap.put("condition", 0);
		
		shippingFee.add(shippingFeeMap);
		
		params.put("shippingFee", shippingFee);
		
		String body = mapToJson(params);
		
		return body;
	}*/
	
	//3.14 발송 타입 정책 등록
	//[POST] https://sa.esmplus.com/item/v1/shipping/dispatch-policies
	/*public String createDispatchTypePoliciesBody(){
		Map<String, Object> params = new HashMap<>();
		
		params.put("dispatchType", "String");//Y, 발송정책타입, "A:당일배송OneDayDelivery B:순차발송DomesticInOrderDelivery C:해외발송GlobalInOrderDelivery 해외발송 (글로벌 셀러만 설정 가능)  D:요청일발송RequestDayDelivery E:주문제작 발송 CustomOrderDelivery 	F:발송일미정NotAppointment"
		params.put("readyDurationDay", 0);//N, 배송준비소요일
		params.put("dispatchCloseTime", "String");//N, 발송마감시간, 00:00(시:분), 30분 단위만 가능
		
		String body = mapToJson(params);
		
		return body;
	}*/
	
	//7.1 정산 판매대금 조회
	//[POST] https://sa.esmplus.com/account/v1/settle/getsettleorder
	
	private Map<String,Object> createSettleOrderInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		params.put("ContrNo", 0);//주문번호
		params.put("SrchType", "D9"); // D3: 배송완료일, D7: 환불일, D9:배송완료일+배송완료일있는환불일
		params.put("SrchStartDate", requestMap.getString("remitDate"));
		params.put("SrchEndDate", requestMap.getString("remitDate") + " 23:59:59");
		params.put("PageNo", requestMap.getInt("PageNo"));
		params.put("PageRowCnt", requestMap.getInt("PageRowCnt"));
		params.put("SiteType", requestMap.getString("siteType"));
		
		return params;
	}
	
	//7.2 정산 배송비 조회
	//[POST] https://sa.esmplus.com/account/v1/settle/getsettledeliveryfee
	
	private Map<String,Object> createSettleDeliveryInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		params.put("PackNo", 0);//장바구니번호
		params.put("SrchType", "D6"); // D1: 입금확인일, D3: 매출마감일, D6:송금일, D7:환불일, D8: 입금확인일
		params.put("SrchStartDate", requestMap.getString("remitDate"));
		params.put("SrchEndDate", requestMap.getString("remitDate") + " 23:59:59");
		params.put("DeliveryGroupNo", 0);//묶음배송번호
		params.put("PageNo", requestMap.getInt("PageNo"));
		params.put("PageRowCnt", requestMap.getInt("PageRowCnt"));
		params.put("SiteType", requestMap.getString("siteType"));
		
		return params;
	}
}
