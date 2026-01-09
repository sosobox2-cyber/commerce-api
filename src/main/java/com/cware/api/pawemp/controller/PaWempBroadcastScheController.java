package com.cware.api.pawemp.controller;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pawemp.broadcast.service.PaWempBroadcastService;
import com.cware.netshopping.pawemp.common.model.ReturnData;
import com.cware.netshopping.pawemp.common.service.PaWempApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Controller("com.cware.api.pawemp.PaWempBroadcastScheController")
@RequestMapping(value="/pawemp/broadcast")
public class PaWempBroadcastScheController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	@Resource(name = "pawemp.broadcast.paWempBroadcastService")
	private PaWempBroadcastService paWempBroadcastService;
	
	@Resource(name = "pawemp.common.paWempApiService")
	private PaWempApiService paWempApiService;

	/**
	 * 위메프 홈쇼핑 방송편성표 관리
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "/broadcastsche-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> broadcastscheInsert(HttpServletRequest request,
			@RequestParam(value="fromDate"    , required=true , defaultValue = "")    String fromDate,	
			@RequestParam(value="toDate"      , required=true , defaultValue = "")    String toDate
			) throws Exception{

		ParamMap paramMap = new ParamMap();
		HashMap<String, String> apiInfo = new HashMap<String, String>();
		
		String dateTime 		= "";
		String duplicateCheck	= "";
		String prg_id			= "IF_PAWEMPAPI_06_001";
		String rtnMsg			= Constants.SAVE_SUCCESS;
		
	    List<Pabroadreplace> alternativeInfo = null;
	    ResponseEntity<?> broadcastscheFalseResult = null;
		
		String infromDate 		  = "";
		String intoDate 	      = "";
		
		try{
			log.info("===== 홈쇼핑 방송편성표 관리 API Start=====");
			log.info("01.API 기본정보 세팅");
		    dateTime = systemService.getSysdatetimeToString();
			
		    /*system 기본필수세팅*/
		    paramMap.put("apiCode", prg_id);
	        paramMap.put("broadCode" , Constants.PA_WEMP_BROAD_CODE);
	        paramMap.put("onlineCode", Constants.PA_WEMP_ONLINE_CODE);
	        paramMap.put("startDate" , dateTime);
			paramMap.put("paCode"    , Constants.PA_WEMP_BROAD_CODE);
			paramMap.put("paName"	 , Constants.PA_BROAD);
			paramMap.put("siteGb"    , Constants.PA_WEMP_PROC_ID);
			paramMap.put("paGroupCode" , Constants.PA_WEMP_GROUP_CODE);

			if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				infromDate = fromDate;
				intoDate   = toDate;
			}else{
				infromDate = DateUtil.getCurrentDateAsString();
				intoDate   = DateUtil.addDay(infromDate, 1, DateUtil.GENERAL_DATE_FORMAT);
			}
			paramMap.put("bd_Bdate"    , infromDate);
			paramMap.put("bd_Edate"    , intoDate);
					    
		    log.info("02.API 중복실행검사");
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
		    
		    log.info("03.API정보 조회");
		    apiInfo = systemService.selectPaApiInfo(paramMap);
		    apiInfo.put("apiInfo", paramMap.getString("apiCode"));
		    
		    //대체편성 상품 설정. 매주 일요일마다 7일간 제일 많이 팔린 상품을 대체편성 상품으로 설정한다.
		    log.info("04.대체편성 상품 설정");
		    paWempBroadcastService.refreshBroadReplaceGoodsTx(paramMap);
		    
		    log.info("05.위메프 홈쇼핑 방송편성표 대상 조회");
		    //연동 대상 조회
		    List<Pabroadsche> broadInfoList = paWempBroadcastService.selectBroadcastScheList(paramMap);
		    
		    //대체상품 가지고오기
		    if(broadInfoList.size() > 0) {
		    	int idx = -1;			    	
		    	//대체편성상품 세팅
		    	alternativeInfo = paWempBroadcastService.selectAlternativeBroadcastLink(paramMap.getString("paGroupCode"));
		    	int idxsize = alternativeInfo.size();
		    	for(int j=0; j<alternativeInfo.size(); j++) {
		    		if("1".equals(alternativeInfo.get(j).getTargetYn())) {
		    			idx = j;
		    			break;
		    		}
		    	}
		    	if(idx < 0) {
		    		idx = 0;
		    	}
		    	
		    	for(Pabroadsche broadInfo : broadInfoList){
			    	
		    		if("D".equals(broadInfo.getModifyFlag())){
		    			broadInfo.setProgramUseYn("0");
						broadInfo.setGoodsUseYn("0");
		    		} else {
		    			broadInfo.setProgramUseYn("1");
						broadInfo.setGoodsUseYn("1");
		    		}
		    		
		    		//대채편성상품 처리
		    		if("".equals(broadInfo.getPaGoodsCode()) || broadInfo.getPaGoodsCode() == "" || broadInfo.getPaGoodsCode() == null) {
		    			broadInfo.setBroadcastProgramType("Alternative");
		    			broadInfo.setPaGoodsCode(alternativeInfo.get(idx%idxsize).getPaGoodsCode());
		    			broadInfo.setProgName(alternativeInfo.get(idx%idxsize).getGoodsName());
		    			broadInfo.setShManName				(alternativeInfo.get(idx%idxsize).getShManName());
		    			broadInfo.setGoodsCode 				(alternativeInfo.get(idx%idxsize).getGoodsCode());
		    			broadInfo.setGoodsName				(alternativeInfo.get(idx%idxsize).getGoodsName());
		    			broadInfo.setGoodsVodUrl			(alternativeInfo.get(idx%idxsize).getVodUrl());
		    			broadInfo.setVideoImage				(alternativeInfo.get(idx%idxsize).getVodImage());

		    			idx++;
			    	}else{
			    		broadInfo.setBroadcastProgramType("Live");
			    	}
			    	
	    			broadInfo.setProgramType("Broadcasting");
		    		broadInfo.setBroadcastProgramDetailType("AlwaysOnItem");
			    	
		    		log.info("06.홈쇼핑 방송편성표 처리 Start");
			    	broadcastscheFalseResult = transBroadcastsche(request, broadInfo, paramMap, apiInfo);				    	
			    	
			    	if(PropertyUtils.describe(broadcastscheFalseResult.getBody()).get("code").equals("200")){
			    		
						broadInfo.setTransYn("1");
						broadInfo.setTransDate(dateTime);
						broadInfo.setInsertId(paramMap.getString("siteGb"));
						
			    		rtnMsg = paWempBroadcastService.saveWempBroadScheTx(broadInfo);
	    				if(!rtnMsg.equals("000000")){
	    					log.info("[Error] 2.0 위메프 편성데이터 저장에 실패 하였습니다. : SEQ_FRAME_NO :"+broadInfo.getSeqFrameNo()+", MSG : "+rtnMsg);
	    					continue;
	    				}
	    			} else {
	    				paramMap.put("code"   , "500");
	    				paramMap.put("message", "ERROR");
	    			}
			    }
		    	
		    	//대체편성 TARGET UPDATE
		    	log.info("07.대체편성 TARGET UPDATE");
		    	paWempBroadcastService.updatePaBroadReplaceTx(alternativeInfo.get(idx%idxsize));
		    }
		    
		} catch (Exception e) {
			if (duplicateCheck.equals("1")) {
				paramMap.put("code", "490");
			} else {
				paramMap.put("code", "500");
			}
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
		}finally {
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request, paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 홈쇼핑 방송편성표 관리 API END =====");
		}
	   return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	public ResponseEntity<?> transBroadcastsche(HttpServletRequest request, Pabroadsche broadInfo, ParamMap paramMap, HashMap<String, String> apiInfo) throws Exception{
		
		ReturnData returnData = null;
		JsonObject jBroadcastsche = new JsonObject();
		JsonArray jBroadcastscheList = new JsonArray();
		JsonObject jBroadcastscheUnit = new JsonObject();
		JsonArray jProductList = new JsonArray();
		
		jBroadcastscheUnit.addProperty("programId", broadInfo.getSeqFrameNo());
		jBroadcastscheUnit.addProperty("programName", broadInfo.getProgName());
		
		if("D".equals(broadInfo.getModifyFlag())) {
			jBroadcastscheUnit.addProperty("useYn", "N");
		}else {
			jBroadcastscheUnit.addProperty("useYn", "Y");			
		}
		jBroadcastscheUnit.addProperty("broadcastChannel", "SK_STOA"); //0:채널없음, 1:GS Live Shop, 2:GS My Shop, SK_STOA: sk스토아
		jBroadcastscheUnit.addProperty("broadcastType", "V"); //L:LIVE, V:VOD, B:LIVE+VOD
		jBroadcastscheUnit.addProperty("liveUrl", "");
		jBroadcastscheUnit.addProperty("vodUrl", broadInfo.getGoodsVodUrl());
		jBroadcastscheUnit.addProperty("broadcastDate", broadInfo.getBroadCastDate());
		jBroadcastscheUnit.addProperty("displayStartDate", broadInfo.getBroadcastStartDate());
		jBroadcastscheUnit.addProperty("displayEndDate", broadInfo.getBroadcastEndDate());
		
		//필요하면 상품 여러개 넣어주기
		JsonObject jProduct = new JsonObject();
		jProduct.addProperty("productNo", broadInfo.getPaGoodsCode());
		jProduct.addProperty("mainProdYn", "Y");
		jProduct.addProperty("priority", 0);
		
		jProductList.add(jProduct);
		jBroadcastscheUnit.add("products", jProductList);
		jBroadcastscheList.add(jBroadcastscheUnit);
		jBroadcastsche.add("broadcastSchedules", jBroadcastscheList);
		
		try{			
			returnData = (ReturnData) paWempApiService.callWApiObejct(apiInfo, "POST", jBroadcastsche, ReturnData.class, paramMap.getString("paName"));
			
			if(returnData != null){
				paramMap.put("code", "200");
				paramMap.put("message", "편성 정보를 수정하였습니다.");
				
			} else {
				paramMap.put("code","404");
				paramMap.put("message",getMessage("partner.no_change_data"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		
		} catch (Exception e) {
			paramMap.put("code","500");
			paramMap.put("message",e.getMessage().length() > 3950 ? e.getMessage().substring(0, 3950) : e.getMessage());
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
		}finally {
			try{
				paramMap.put("resultCode", paramMap.getString("code"));
				paramMap.put("resultMsg", paramMap.getString("message"));
				paramMap.put("siteGb", Constants.PA_WEMP_PROC_ID);
				systemService.insertApiTrackingTx(request,paramMap);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}