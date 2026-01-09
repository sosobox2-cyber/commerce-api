package com.cware.api.pagmktv2.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.Pabroadreplace;
import com.cware.netshopping.domain.model.Pabroadsche;
import com.cware.netshopping.pagmkt.broadcast.service.PaGmktBroadcastService;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.pagmkt.util.PaGmktRestUtil;
import com.cware.netshopping.pagmkt.util.rest.PaGmktAbstractRest;
import com.cware.netshopping.pagmkt.util.rest.PaGmktBroadcastRest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/pagmktv2/broadcast", description="방송/편성")
@Controller("com.cware.api.pagmktv2.PaGmktBroadcastScheController")
@RequestMapping(value="/pagmktv2/broadcast")
public class PaGmktBroadcastScheController extends AbstractController {

	@Resource(name = "common.system.systemService")
	private SystemService systemService;
	
	@Resource(name = "pagmkt.broadcast.PaGmktBroadcastService")
	private PaGmktBroadcastService paGmktBroadcastService;
	
	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;

	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktRestUtil")
	private PaGmktRestUtil restUtil;
	
	/**
	 * 이베이 홈쇼핑 방송편성표 관리 - 2.0
	 * @return ResponseEntity
	 * @throws Exception
	 *
	 */
	@ApiOperation(value = "이베이 홈쇼핑 방송편성표 관리", notes = "이베이 홈쇼핑 방송편성표 관리", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/broadcastsche-V2-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> broadcastscheV2Insert(HttpServletRequest request,
			@ApiParam(name = "fromDate", value = "검색시작일", defaultValue = "") 	@RequestParam(value = "fromDate", required = true, defaultValue = "") String fromDate,
			@ApiParam(name = "toDate", value = "검색종료일", defaultValue = "") 	@RequestParam(value = "toDate", required = true, defaultValue = "") String toDate)
			throws Exception{

		ParamMap paramMap = new ParamMap();
		List<Pabroadreplace> alternativeInfo 	= null;
		String infromDate 		= "";
		String intoDate 	    = "";
		String duplicateCheck = "";
		
		try{
			log.info("===== 홈쇼핑 방송편성표 관리 API Start=====");
			paramMap.put("apiCode", 		"IF_PAGMKTAPI_V2_00_000_L");
			paramMap.put("siteGb", 			"afterset");
			paramMap.put("startDate", 		systemService.getSysdatetimeToString());
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
			
			log.info("01.API 중복실행검사");
		    //= 중복 실행 Check
		    duplicateCheck = systemService.checkCloseHistoryTx("start", paramMap.getString("apiCode"));
		    if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {paramMap.getString("apiCode")});
			
		    if(!(fromDate.isEmpty() || fromDate.equals("")) && !(toDate.isEmpty() || toDate.equals(""))){
				infromDate 	= fromDate;
				intoDate 	= toDate;
			}else{
				infromDate 	= DateUtil.getCurrentDateAsString();
				intoDate 	= DateUtil.addDay(infromDate,  1, DateUtil.GENERAL_DATE_FORMAT);
			}
		    
		    paramMap.put("bd_Bdate", 		infromDate);
			paramMap.put("bd_Edate", 		intoDate);
			
		    for(int i=0; i<2; i++) {
		    	paramMap.put("paGroupCode", (i==0)?"02":"03");
		    	
		    	//대체편성 상품 설정. 매주 일요일마다 7일간 제일 많이 팔린 상품을 대체편성 상품으로 설정한다.
		    	paGmktBroadcastService.refreshBroadReplaceGoodsTx(paramMap);
		    	
		    	log.info("02.홈쇼핑 방송편성표 2.0 대상 조회");
		    	List<Pabroadsche> broadInfoList = paGmktBroadcastService.selectBroadcastScheList(paramMap);
		    	
		    	if(broadInfoList.size() > 0) {
		    		
		    		int idx = -1;			    	
			    	//대체편성상품 세팅
			    	alternativeInfo = paGmktBroadcastService.selectAlternativeBroadcastLink(paramMap.getString("paGroupCode"));
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
			    					    		
			    		if("D".equals(broadInfo.getModifyFlag())) {
			    			deleteBroadcastsche(request, broadInfo);
			    		} else {
			    			//묶음상품
				    		List<Object> bundleGoodsInfoList = null;
				    		
			    			if("".equals(broadInfo.getPaGoodsCode()) || broadInfo.getPaGoodsCode() == "" || broadInfo.getPaGoodsCode() == null) {
			    				broadInfo.setBroadcastProgramType("Alternative");
			    				
			    				//대체상품 처리
			    				broadInfo.setPaGoodsCode			(alternativeInfo.get(idx%idxsize).getPaGoodsCode());
			    				broadInfo.setProgName				(alternativeInfo.get(idx%idxsize).getGoodsName());
			    				broadInfo.setShManName				(alternativeInfo.get(idx%idxsize).getShManName());
			    				broadInfo.setGoodsCode 				(alternativeInfo.get(idx%idxsize).getGoodsCode());
			    				broadInfo.setGoodsName				(alternativeInfo.get(idx%idxsize).getGoodsName());
				    			broadInfo.setGoodsVodUrl			(alternativeInfo.get(idx%idxsize).getVodUrl());
					    		broadInfo.setVideoImage				(alternativeInfo.get(idx%idxsize).getVodImage());
			    				
			    				idx++;
			    			} else {
			    				broadInfo.setBroadcastProgramType("Live");
			    				
			    				//묶음상품 처리
			    				bundleGoodsInfoList = paGmktBroadcastService.selectBraocastV2GoodsList(paramMap);
			    				if(bundleGoodsInfoList.size() > 0){
			    					broadInfo.setBundleGoodsYn("1");
			    				} 
			    			}
			    			
			    			broadInfo.setAlternativeVodUrl(CommonUtil.refineUrlForEncoding(ComUtil.objToStr(broadInfo.getGoodsVodUrl())));
			    			broadInfo.setProgramType("Broadcasting");
				    		broadInfo.setBroadcastProgramDetailType("AlwaysOnItem");
			    			
			    			log.info("03.홈쇼핑 방송편성표 2.0 처리 Start");
				    		if("I".equals(broadInfo.getModifyFlag())){
				    			postBroadcastsche(request, broadInfo, bundleGoodsInfoList);
				    		}else if("U".equals(broadInfo.getModifyFlag())){
				    			putBroadcastsche(request, broadInfo, bundleGoodsInfoList);
				    		}
			    		}				    	
				    }
		    		
		    		//대체편성 TARGET UPDATE
		    		paGmktBroadcastService.updateGmktBroadReplaceTx(alternativeInfo.get(idx%idxsize));
		    	}
		    	
		    }
		    
		} catch (Exception e) {
		    CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 홈쇼핑 방송편성표 2.0 관리 API END =====");
		}

	   return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);

	}
	
	/**
	 * 홈쇼핑 편성 2.0 - 삭제  
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "홈쇼핑 편성 2.0 - 삭제", notes = "홈쇼핑 편성 2.0 - 삭제", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/delete-broadcastsche", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> deleteBroadcastsche(HttpServletRequest request, Pabroadsche broadInfo) throws Exception{
		ParamMap paramMap 				= new ParamMap();
		PaGmktAbstractRest rest 		= new PaGmktBroadcastRest();
		
		String apiCode 					= "IF_PAGMKTAPI_V2_03_000";            
		int executedRtn 				= 0;
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("paGroupCode", broadInfo.getPaGroupCode());
		paramMap.put("paCode", "21");
		paramMap.put("siteGb", "02".equals(broadInfo.getPaGroupCode())?"PAG":"PAA");
		
		try{			
			
			paramMap.put("broadInfo", ComUtil.ConverttObjectToMap(broadInfo));
			
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> restMap = ComUtil.splitJson(response);
			if(!"".equals(ComUtil.NVL(restMap.get("programId")).toString())) {
				
				executedRtn = paGmktBroadcastService.deleteGmktBroadSche(broadInfo);
				if(executedRtn < 1) {
					paramMap.put("code", "500");
					paramMap.put("message", "데이터 삭제 실패");
				} else { 
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "Error");
			}
			
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
			log.error(paramMap.getString("message"), se);
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 홈쇼핑 편성 2.0 - 등록  
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "홈쇼핑 편성 2.0 - 등록", notes = "홈쇼핑 편성 2.0 - 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/post-broadcastsche", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> postBroadcastsche(HttpServletRequest request, Pabroadsche broadInfo, List<Object> bundleGoodsInfoList) throws Exception{
		ParamMap paramMap 				= new ParamMap();
		PaGmktAbstractRest rest 		= new PaGmktBroadcastRest();
		
		String apiCode 					= "IF_PAGMKTAPI_V2_01_000";            
		int executedRtn 				= 0;
		String dateTime 				= systemService.getSysdatetimeToString();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("paGroupCode", broadInfo.getPaGroupCode());
		paramMap.put("paCode", "21");
		paramMap.put("siteGb", "02".equals(broadInfo.getPaGroupCode())?"PAG":"PAA");
		
		try{			
			
			paramMap.put("broadInfo", ComUtil.ConverttObjectToMap(broadInfo));
			paramMap.put("seqFrameNo", broadInfo.getSeqFrameNo());
			paramMap.put("bundleGoodsInfoList", bundleGoodsInfoList);
			
			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> restMap = ComUtil.splitJson(response);
			if(!"".equals(ComUtil.NVL(restMap.get("programId")).toString())) {
				
				broadInfo.setProgramUseYn("1");
				broadInfo.setGoodsUseYn("1");
				broadInfo.setTransYn("1");
				broadInfo.setTransDate(dateTime);
				broadInfo.setInsertId(paramMap.getString("siteGb"));
				
				executedRtn = paGmktBroadcastService.insertGmktBroadSche(broadInfo);
				if(executedRtn < 1) {
					paramMap.put("code", "500");
					paramMap.put("message", "데이터 등록 실패");
				} else { 
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "Error");
			}
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 홈쇼핑 편성 2.0 - 수정  
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@ApiOperation(value = "홈쇼핑 편성 2.0 - 수정", notes = "홈쇼핑 편성 2.0 - 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
		@ApiResponse(code = 500, message = "시스템 오류가 발생하였습니다.")
	})
	@RequestMapping(value = "/put-broadcastsche", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> putBroadcastsche(HttpServletRequest request, Pabroadsche broadInfo, List<Object> bundleGoodsInfoList) throws Exception{
		ParamMap paramMap 				= new ParamMap();
		PaGmktAbstractRest rest 		= new PaGmktBroadcastRest();
		
		String apiCode 					= "IF_PAGMKTAPI_V2_02_000";            
		int executedRtn 				= 0;
		String dateTime 				= systemService.getSysdatetimeToString();
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", dateTime);
		paramMap.put("paGroupCode", broadInfo.getPaGroupCode());
		paramMap.put("paCode", "21");
		paramMap.put("siteGb", "02".equals(broadInfo.getPaGroupCode())?"PAG":"PAA");
		
		try{
			
			paramMap.put("broadInfo", ComUtil.ConverttObjectToMap(broadInfo));
			paramMap.put("seqFrameNo", broadInfo.getSeqFrameNo());
			paramMap.put("bundleGoodsInfoList", bundleGoodsInfoList);			

			String response = restUtil.getConnection(rest,  paramMap);
			Map<String,Object> restMap = ComUtil.splitJson(response);
			if(!"".equals(ComUtil.NVL(restMap.get("programId")).toString())) {
				
				broadInfo.setProgramUseYn("1");
				broadInfo.setGoodsUseYn("1");
				broadInfo.setTransYn("1");
				broadInfo.setTransDate(dateTime);
				broadInfo.setInsertId(paramMap.getString("siteGb"));
				
				executedRtn = paGmktBroadcastService.updateGmktBroadSche(broadInfo);
				if(executedRtn < 1) {
					paramMap.put("code", "500");
					paramMap.put("message", "데이터 수정 실패");
				} else { 
					paramMap.put("code", "200");
					paramMap.put("message", "OK");
				}
				
			} else {
				paramMap.put("code", "500");
				paramMap.put("message", "Error");
			}
		
		} catch (Exception se) {
			CommonUtil.dealException(se, paramMap);
		}finally {
			CommonUtil.dealSuccess(paramMap, request);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
