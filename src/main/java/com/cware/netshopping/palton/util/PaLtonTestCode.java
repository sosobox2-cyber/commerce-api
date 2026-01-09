package com.cware.netshopping.palton.util;


public class PaLtonTestCode {
	
	/*
	public JSONObject test() throws Exception {
		String jsonString = "{"
				+"    \"returnCode\": \"0000\", "
				+"    \"message\": 정상 처리되었습니다., "
				+"    \"subMessages\": null, "
				+"    \"dataCount\":1, "
				+"    \"data\": { "
				+"            \"deliveryOrderList\": [ "
				+"                                    { "
				+"                                        \"odNo\": \"od001\", "
				+"                                        \"clmNo\": \"clm001\", "
				+"                                        \"odTypCd\": \"3\", "
				+"                                        \"odTypDtlCd\": \"1\", "
				+"                                        \"dvRtrvDvsCd\": \"5\", "
				+"                                        \"dvProcTypCd\": \"6\", "
				+"                                        \"pkupRefcNo\": \"\" "
				+"                                    } "
				+"                                ] "
				+"            } "
				+" } " ;

		
	 	 System.out.println("jsonString : " + jsonString);
		
		 JSONParser parser = new JSONParser();
		 Object obj = parser.parse(new StringReader(jsonString));  
		 JSONObject jsonObj = (JSONObject) obj;

		return jsonObj;
		
	}
	
	@SuppressWarnings("unchecked")
	public String test2() throws Exception {
		
		
		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();
		JSONObject sObject1 = new JSONObject();
		JSONObject sObject2 = new JSONObject();
		
		obj.put("returnCode"	, "0000");
		obj.put("message"		, "정상 처리되었습니다.");
		obj.put("subMessages"	, null);
		obj.put("dataCount"		, 1);
		
		sObject2.put("odNo"			,"od001");
		sObject2.put("clmNo"		,"clm001");
		sObject2.put("odTypCd"		,"3");
		sObject2.put("odTypDtlCd"	,"1");
		sObject2.put("dvRtrvDvsCd"	,"5");
		sObject2.put("dvProcTypCd"	,"6");
		sObject2.put("pkupRefcNo"	,"");
		jArray.put(sObject2);	
		
		sObject1.put("deliveryOrderList", jArray);
		
		obj.put("data", sObject1);
		
		return obj.toString();
		
	}*/
	

}
