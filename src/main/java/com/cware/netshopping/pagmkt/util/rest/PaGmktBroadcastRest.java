package com.cware.netshopping.pagmkt.util.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.util.ComUtil;

public class PaGmktBroadcastRest extends PaGmktAbstractRest {

	@Override
	public String getRequstBody(ParamMap param) throws Exception{
		String apiCode = param.getString("apiCode");
		Map<String,Object> map = new HashMap<>();
		switch(apiCode){
		case "IF_PAGMKTAPI_V2_01_000" ://편성 등록
		case "IF_PAGMKTAPI_V2_02_000" ://편성 수정
			map = createBroadcastInsertBody(param);
			break;
		case "IF_PAGMKTAPI_V2_03_000" ://편성 삭제
			map = createBroadcastDeleteBody(param);
			break;
		default :	
			break;
		}
		return mapToJson(map);
	}
	
	//편성 등록/수정
	@SuppressWarnings("unchecked")
	private Map<String,Object> createBroadcastInsertBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		
		Map<String, Object> homeShoppingTimeTable = new HashMap<>();
		Map<String, Object> homeShoppingSubInfo = new HashMap<>();
		Map<String, Object> homeShoppingAlternativeUrl = new HashMap<>();
		Map<String, Object> homeShoppingDailyExposeProgram = new HashMap<>();
		
		HashMap<String, Object> broadInfoMap = (HashMap<String, Object>) requestMap.get("broadInfo");
		
		String site =  "02".equals(requestMap.get("paGroupCode").toString())?"gmkt":"iac";
		
		params.put("siteType", "02".equals(requestMap.get("paGroupCode").toString())?"2":"1"); // 1: 옥션, 2: G마켓
		
		Map<String, Object> siteGoodsNo  = new HashMap<>();
		siteGoodsNo.put(site, broadInfoMap.get("paGoodsCode"));			
		homeShoppingTimeTable.put("siteGoodsNo", siteGoodsNo);
		
		
		Map<String, Object> bundleGoodsYn = new HashMap<>();
		Map<String, Object> bundleGoods = new HashMap<>();
		
		Map<String, Object> siteBundleGoods  = new HashMap<>();
		List<Object> bundleGoodsList = new ArrayList<>();
		if("1".equals(ComUtil.NVL(broadInfoMap.get("bundleGoodsYn")).toString())) {
			bundleGoodsYn.put(site, true);
			
			List<HashMap<String, Object>> bundleGoodsInfoList = (List<HashMap<String, Object>>) requestMap.get("bundleGoodsInfoList");
			for(HashMap<String, Object> bundleGoodsInfoMap : bundleGoodsInfoList){
				siteBundleGoods.put("siteGoodsNo", bundleGoodsInfoMap.get("ITEM_NO"));
				siteBundleGoods.put("bundleGoodsUseYn", true);
				bundleGoodsList.add(siteBundleGoods);
			}
			bundleGoods.put(site, bundleGoodsList);
			
		} else {
			bundleGoodsYn.put(site, false);
			
			siteBundleGoods.put("siteGoodsNo", "미사용");
			siteBundleGoods.put("bundleGoodsUseYn", false);
			bundleGoodsList.add(siteBundleGoods);
			bundleGoods.put(site, bundleGoodsList);
		}
		homeShoppingTimeTable.put("bundleGoodsYn", bundleGoodsYn);
		homeShoppingTimeTable.put("bundleGoods", bundleGoods);
		
		homeShoppingTimeTable.put("programId", broadInfoMap.get("seqFrameNo"));
		homeShoppingTimeTable.put("programType", broadInfoMap.get("programType").equals("Broadcasting") ? 1 : 2);
		homeShoppingTimeTable.put("broadcastProgramType", broadInfoMap.get("broadcastProgramType").equals("Alternative") ? 2 : 1);
		homeShoppingTimeTable.put("broadcastProgramDetailType", 2);
		homeShoppingTimeTable.put("broadcastDate", broadInfoMap.get("broadCastDate"));
		homeShoppingTimeTable.put("broadcastHour", broadInfoMap.get("broadcastHour"));
		homeShoppingTimeTable.put("broadcastStartDate", broadInfoMap.get("broadcastStartDate"));
		homeShoppingTimeTable.put("broadcastEndDate", broadInfoMap.get("broadcastEndDate"));
		homeShoppingTimeTable.put("programName", broadInfoMap.get("progName"));
		homeShoppingTimeTable.put("hostName", broadInfoMap.get("shManName"));
		
		homeShoppingSubInfo.put("useYn", false);
		homeShoppingSubInfo.put("promotionFreeGiftYn", false);
		
		/*
		String promotionImageUrl = null;
		if(broadInfoMap.get("videoImage") != null && !broadInfoMap.get("videoImage").equals("")){
			promotionImageUrl = broadInfoMap.get("videoImage").toString();
			promotionImageUrl = promotionImageUrl.substring(broadInfoMap.get("videoImage").toString().indexOf("//"));
		}
		
		homeShoppingSubInfo.put("promotionImageUrl", promotionImageUrl);
		homeShoppingSubInfo.put("promotionText", broadInfoMap.get("goodsName"));
		homeShoppingSubInfo.put("promotionSpecText", "");
		homeShoppingSubInfo.put("promotionFreeGiftYn", false);
		*/
		
		homeShoppingAlternativeUrl.put("useYn", true);
		homeShoppingAlternativeUrl.put("alternativeVodUrl", broadInfoMap.get("alternativeVodUrl"));
		
		homeShoppingDailyExposeProgram.put("useYn", false);
		//homeShoppingDailyExposeProgram.put("dailyExposeUrl", null);
		
		params.put("homeShoppingTimeTable", homeShoppingTimeTable);
		params.put("homeShoppingSubInfo", homeShoppingSubInfo);
		params.put("homeShoppingAlternativeUrl", homeShoppingAlternativeUrl);
		params.put("homeShoppingDailyExposeProgram", homeShoppingDailyExposeProgram);
		
		return params;
	}
	
	//편성 삭제
	@SuppressWarnings("unchecked")
	private Map<String,Object> createBroadcastDeleteBody(ParamMap requestMap){
		Map<String, Object> params = new HashMap<>();
		HashMap<String, Object> broadInfoMap = (HashMap<String, Object>) requestMap.get("broadInfo");
		
		params.put("siteType", "02".equals(requestMap.get("paGroupCode").toString())?"2":"1"); // 1: 옥션, 2: G마켓
		params.put("programId", broadInfoMap.get("seqFrameNo")); 

		return params;
	}
}
