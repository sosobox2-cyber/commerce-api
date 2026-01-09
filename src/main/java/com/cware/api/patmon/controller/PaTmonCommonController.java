package com.cware.api.patmon.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaTmonSettlement;
import com.cware.netshopping.patmon.common.service.PaTmonCommonService;
import com.cware.netshopping.patmon.util.PaTmonConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;
@ApiIgnore
@Api(value="/patmon/common", description="티몬 공통")
@Controller("com.cware.api.patmon.PaTmonCommonController")
@RequestMapping(value = "/patmon/common")
public class PaTmonCommonController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaTmonCommonController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Autowired
	PaTmonConnectUtil paTmonConnectUtil;
	
	@Resource(name = "patmon.common.paTmonCommonService")
	private PaTmonCommonService paTmonCommonService;
	
	/**
	 * 전시카테고리 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsKinds-display-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsDisplayList(HttpServletRequest request) throws Exception {
		String	paCode 						= Constants.PA_TMON_BROAD_CODE;
		String	key 						= "";
		String	prg_id 						= "IF_PATMONAPI_00_002";
		ParamMap	apiInfoMap				= new ParamMap();
		ParamMap	apiDataMap				= new ParamMap();
		PaGoodsKinds 	paGoodsKinds 		= null;
		Map<String, Object> map				= new HashMap<String, Object>() ;
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		apiInfoMap.put("paGroupCode", 	Constants.PA_TMON_GROUP_CODE);
		
		try {
			log.info(prg_id + " - 01.프로그램 중복 실행 검사 [start]");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			//통신
			apiInfoMap.put("paCode", paCode);
			map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
			
			//대분류 파싱
			List<HashMap<String, Object>> resLList = (List<HashMap<String, Object>>)map.get("Data");
			for(HashMap<String, Object> categoryL : resLList) {
				String lgroup 		= categoryL.get("no").toString();
				String lgroupName 	= categoryL.get("name").toString();
				
				//중분류 키값 뽑기
				List<?> mCateKeyList = getKeyList((HashMap<?, ?>)categoryL.get("subcategories"));
	            HashMap<String, Object> beforePaseCategoryM = (HashMap<String, Object>)categoryL.get("subcategories");
				HashMap<String, Object> categoryM = new HashMap<String, Object>();
				//중분류 파싱
	            for(int i = 0; i < mCateKeyList.size(); i++){
	            	key = (String) mCateKeyList.get(i);
	            	categoryM = (HashMap<String, Object>)beforePaseCategoryM.get(key);
					
					String mgroup 		= categoryM.get("no").toString();
					String mgroupName 	= categoryM.get("name").toString();
					
					//소분류 키값 뽑기
					List<?> sCateKeyList = getKeyList((HashMap<?, ?>)categoryM.get("subcategories"));
			        HashMap<String, Object> beforePaseCategoryS = (HashMap<String, Object>)categoryM.get("subcategories");
					HashMap<String, Object> categoryS = new HashMap<String, Object>();
					//소분류 파싱
					for(int k = 0; k < sCateKeyList.size(); k++){
		            	key = (String) sCateKeyList.get(k);
		            	categoryS = (HashMap<String, Object>)beforePaseCategoryS.get(key);
		            	
		            	String sgroup 		= categoryS.get("no").toString();
						String sgroupName 	= categoryS.get("name").toString();
						
						paGoodsKinds = new PaGoodsKinds();
						
						paGoodsKinds.setPaGroupCode				(apiInfoMap.getString("paGroupCode"));
						paGoodsKinds.setPaLmsdKey				(sgroup);
						paGoodsKinds.setPaLgroup				(lgroup);
						paGoodsKinds.setPaMgroup				(mgroup);
						paGoodsKinds.setPaSgroup				(sgroup);
						paGoodsKinds.setPaLgroupName			(lgroupName);
						paGoodsKinds.setPaMgroupName			(mgroupName);
						paGoodsKinds.setPaSgroupName			(sgroupName);
						paGoodsKinds.setInsertId				("BATCH");
						paGoodsKinds.setInsertDate				(DateUtil.toTimestamp(apiInfoMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
						paGoodsKinds.setModifyId				("BATCH");
						paGoodsKinds.setModifyDate				(DateUtil.toTimestamp(apiInfoMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
						
						if(categoryS.get("selectable").toString().equals("true")){
							paGoodsKinds.setPaDgroup				("");
							paGoodsKinds.setPaDgroupName			("");
							paGoodsKindsList.add(paGoodsKinds);
						}else{
							//세분류 키값 뽑기
							List<?> dCateKeyList = getKeyList((HashMap<?, ?>)categoryS.get("subcategories"));
				            HashMap<String, Object> beforePaseCategoryD = (HashMap<String, Object>)categoryS.get("subcategories");
							HashMap<String, Object> categoryD = new HashMap<String, Object>();
							//세분류 파싱
							for(int x = 0; x < dCateKeyList.size(); x++){
				            	key = (String) dCateKeyList.get(x);
				            	categoryD = (HashMap<String, Object>)beforePaseCategoryD.get(key);
				            	
				            	paGoodsKinds = new PaGoodsKinds();
								
								paGoodsKinds.setPaGroupCode				(apiInfoMap.getString("paGroupCode"));
								paGoodsKinds.setPaLmsdKey				(categoryD.get("no").toString());
								paGoodsKinds.setPaLgroup				(lgroup);
								paGoodsKinds.setPaMgroup				(mgroup);
								paGoodsKinds.setPaSgroup				(sgroup);
								paGoodsKinds.setPaDgroup				(categoryD.get("no").toString());
								paGoodsKinds.setPaLgroupName			(lgroupName);
								paGoodsKinds.setPaMgroupName			(mgroupName);
								paGoodsKinds.setPaSgroupName			(sgroupName);
								paGoodsKinds.setPaDgroupName			(categoryD.get("name").toString());
								paGoodsKinds.setInsertId				("BATCH");
								paGoodsKinds.setInsertDate				(DateUtil.toTimestamp(apiInfoMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
								paGoodsKinds.setModifyId				("BATCH");
								paGoodsKinds.setModifyDate				(DateUtil.toTimestamp(apiInfoMap.getString("startDate").toString(), "yyyy/MM/dd HH:mm:ss"));
								
								paGoodsKindsList.add(paGoodsKinds);
							}
						}
					}
	            }
			}
			paTmonCommonService.savePaGoodsKindsTx(paGoodsKindsList);
			
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//키를 모르는 Map을 키리스트 반환
	public List<?> getKeyList(HashMap<?,?> subcategories){
		ArrayList<String> keyList = new ArrayList<>();
		HashMap<?,?> cateMgroupKeyMap = subcategories;
		Iterator<?> iterator = (Iterator<?>) cateMgroupKeyMap.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next().toString();
            keyList.add(key);
        }
        
        return keyList;
	}
	
	/**
	 * 반품지,출고지 등록
	 * @param  entpCode
	 * @param  entpManSeq
	 * @param  addrGb
	 * @param  paCode
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지/반품지등록", notes = "출고지/반품지등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipInsert(HttpServletRequest request,
			@ApiParam(name="entpCode", 	 value="업체코드", 	defaultValue = "") @RequestParam(value="entpCode", required=true) String entpCode,
			@ApiParam(name="entpManSeq", value="업체담당자순번", defaultValue = "") @RequestParam(value="entpManSeq", required=true) String entpManSeq,
			@ApiParam(name="addrGb",	 value="등록구분", 	defaultValue = "") @RequestParam(value="addrGb", required=true) String addrGb,
			@ApiParam(name="paCode", 	 value="제휴사코드",   defaultValue = "") @RequestParam(value="paCode", required=true) String paCode) throws Exception{
		
		String	dateTime 		= "";
		String	rtnMsg 			= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		
		
		log.info("===== 반품지/출고지 등록 API Start =====");
		log.info("01.API 기본정보 세팅");
		String prg_id = "IF_PATMONAPI_00_003";
		dateTime = systemService.getSysdatetimeToString();
		
		try {
			log.info("02.반품지/출고지 등록 API 중복체크");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			
			try {
				PaEntpSlip paEntpSlip = new PaEntpSlip();
				paEntpSlip.setEntpCode(entpCode);
				paEntpSlip.setEntpManSeq(entpManSeq);
				paEntpSlip.setPaAddrGb(addrGb);
				paEntpSlip.setPaCode(paCode);
				
				log.info("03.반품지/출고지 등록 API 대상조회");
				Map<String,Object> entpSlipMap = paTmonCommonService.selectSlipInsertList(paEntpSlip);
				
				if(entpSlipMap == null) {
					apiInfoMap.put("code","404");
					apiInfoMap.put("message",getMessage("partner.no_change_data"));
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			    }
				
				log.info("04.반품지/출고지 등록 API ");				
				apiDataMap = entpSlipMapping(entpSlipMap);
		    
		    	log.info("05.반품지/출고지 등록 API 호출");
		    	map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
					
		    	if(!"null".equals(String.valueOf(map.get("no")))){
		    				    		
					// 출고지,회수지 INSERT
					log.info("05-1.반품지/출고지 등록 API 성공 : ");
					paEntpSlip.setPaAddrSeq(map.get("no").toString());
					paEntpSlip.setTransTargetYn("0");
					paEntpSlip.setInsertId(Constants.PA_TMON_PROC_ID);
					paEntpSlip.setModifyId(Constants.PA_TMON_PROC_ID);
					paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
					rtnMsg = paTmonCommonService.savePaTmonEntpSlipTx(paEntpSlip);					
					
					log.info("05-2.반품지/출고지 등록 API 저장 : "+rtnMsg);
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						apiInfoMap.put("code","500");
						apiInfoMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
					} else {
						apiInfoMap.put("code","200");
						apiInfoMap.put("message","OK");
					}
				} else {
					apiInfoMap.put("code","500");
					apiInfoMap.put("message", "연동실패");
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
				}
			} catch(Exception e){ 
				String errMsg = e.getMessage();
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", errMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}		
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);	
	}
	
	public ParamMap entpSlipMapping(Map<String, Object> entpSlipMap) {
		ParamMap Map = new ParamMap();
		Map<String, Object> address = new HashMap<String, Object>();
		String addressName  = entpSlipMap.get("PA_CODE").toString() + "-" + entpSlipMap.get("ENTP_CODE").toString() + "-" + entpSlipMap.get("ENTP_MAN_SEQ").toString() + "test";
		
		if("20".equals(entpSlipMap.get("ENTP_MAN_GB"))) {
			Map.put("type", "R");
		}else {
			Map.put("type", "D");
		}
		
		Map.put("addressName", addressName);
		address.put("zipCode", entpSlipMap.get("POST_NO"));
		address.put("address", entpSlipMap.get("ADDRESS1"));
		address.put("addressDetail", entpSlipMap.get("ADDRESS2"));
		Map.put("address", address);
		//address.put("streetAddress", entpSlipMap.get("ADDR"));	도로명 주소 안 넘기고 있음
		Map.put("managerName", entpSlipMap.get("ENTP_MAN_NAME"));
		Map.put("managerPhone", entpSlipMap.get("ENTP_MAN_TEL"));
		
		return Map;
	}
	
	/**
	 * 반품지,출고지 수정
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "출고지/반품지수정", notes = "출고지/반품지수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/entpslip-update", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpSlipUpdate(HttpServletRequest request) throws Exception{
		
		int	procCount									= 0;
		int	targetCount									= 0;
		String	dateTime 								= "";
		String	rtnMsg 									= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap							= new ParamMap();
		ParamMap	apiDataMap							= new ParamMap();
		Map<String, Object>	map							= new HashMap<String, Object>();
		PaEntpSlip	paEntpSlip							= new PaEntpSlip();
		Map<String, Object> entpSlipMap 				= null;
		List<HashMap<String,Object>> entpSlipUpdateList = null;
		
		log.info("===== 반품지/출고지 수정 API Start =====");
		log.info("01.API 기본정보 세팅");
		String prg_id = "IF_PATMONAPI_00_004";
		dateTime = systemService.getSysdatetimeToString();
		
		try {
			log.info("02. 반품지/출고지 수정 API 중복실행검사");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			log.info("03.반품지/출고지 수정 대상 리스트 조회");
			entpSlipUpdateList = paTmonCommonService.selectEntpSlipUpdateList();
			targetCount = entpSlipUpdateList.size();
			
			if(entpSlipUpdateList.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("partner.no_change_data"));
			}else {
				for(int i= 0; i< entpSlipUpdateList.size(); i++) {
					try {
						entpSlipMap = (HashMap<String, Object>) entpSlipUpdateList.get(i);
						
						apiInfoMap.put("url", url.replace("{addressNo}", entpSlipMap.get("PA_ADDR_SEQ").toString()));
						apiInfoMap.put("paCode", entpSlipMap.get("PA_CODE").toString());
		    
						apiDataMap = entpSlipMapping(entpSlipMap);
						
						log.info("04.반품지/출고지 수정 API 호출");
						map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);				
						
						if(!"null".equals(String.valueOf(map.get("no")))){
							// 출고지 INSERT
							log.info("05-1.반품지/출고지 등록 API 수정 성공 : ");
							paEntpSlip.setPaCode(entpSlipMap.get("PA_CODE").toString());
							paEntpSlip.setEntpCode(entpSlipMap.get("ENTP_CODE").toString());
							paEntpSlip.setEntpManSeq(entpSlipMap.get("ENTP_MAN_SEQ").toString());
							paEntpSlip.setPaAddrSeq(entpSlipMap.get("PA_ADDR_SEQ").toString());
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setModifyId(Constants.PA_TMON_PROC_ID);
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

							rtnMsg = paTmonCommonService.updatePaTmonEntpSlipTx(paEntpSlip);

							log.info("04-2.반품지/출고지 수정 동기화 저장 : "+ rtnMsg);
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								apiInfoMap.put("code","500");
								apiInfoMap.put("message",rtnMsg);
							} else {
								apiInfoMap.put("code","200");
								apiInfoMap.put("message","OK");
								procCount++;
							}
						} else {
							apiInfoMap.put("code","500");
							apiInfoMap.put("message","연동실패");
						}
					}catch(Exception e){
						String errMsg = e.getMessage();
						apiInfoMap.put("code","500");
						apiInfoMap.put("message", errMsg);
					}
				}
			}
		} catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);	
	}
	
	/**
	 * 배송템플릿 등록
	 * @param  paCode
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@ApiOperation(value = "배송템플릿 등록", notes = "배송템플릿 등록 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/entpcustshipcost-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpCustShipCostInsert(HttpServletRequest request,
			@ApiParam(name="paCode", 	 value="제휴사코드",   defaultValue = "") @RequestParam(value="paCode", required=false) String paCode ) throws Exception {
		int	targetCount			= 0;
		int	procCount			= 0 ;
		String	rtnMsg			= Constants.SAVE_SUCCESS;
		String	dateTime		= systemService.getSysdatetimeToString();
		String	prg_id			= "IF_PATMONAPI_00_005";
		ParamMap paramMap = new ParamMap();
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>() ;
		
		
		log.info("===== 배송템플릿 등록API Start =====");
		try {
			log.info("01. 배송템플릿 등록API 중복실행검사");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("paCode", "paCode");
			
			log.info("02. 배송템플릿 등록API 대상 리스트 조회");
			List<HashMap<String,Object>> entpShipCostMap = paTmonCommonService.selectEntpSlipCost(apiInfoMap);
			
			targetCount = entpShipCostMap.size();
			
			if(entpShipCostMap.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("partner.no_change_data"));
			}else {
				for(int i = 0; i < entpShipCostMap.size(); i++) {
					try {
						apiInfoMap.put("paCode", entpShipCostMap.get(i).get("PA_CODE").toString());
						
						apiDataMap = shipCostMapping(entpShipCostMap.get(i));				
						
						log.info("03.배송템플릿 등록API 호출");
						map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);	
						
						if(!"null".equals(String.valueOf(map.get("deliveryTemplateNo")))){
							String deliveryTemplateNo = map.get("deliveryTemplateNo").toString();
							
							ParamMap tmonShipCostMap = new ParamMap();
							tmonShipCostMap.put("deliveryTemplateNo", deliveryTemplateNo);
							tmonShipCostMap.put("paCode", entpShipCostMap.get(i).get("PA_CODE").toString());
							tmonShipCostMap.put("entpCode", entpShipCostMap.get(i).get("ENTP_CODE").toString());
							tmonShipCostMap.put("productType", entpShipCostMap.get(i).get("PRODUCT_TYPE").toString());
							tmonShipCostMap.put("shipManSeq", entpShipCostMap.get(i).get("SHIP_MAN_SEQ").toString());
							tmonShipCostMap.put("returnManSeq", entpShipCostMap.get(i).get("RETURN_MAN_SEQ").toString());
							tmonShipCostMap.put("shipCostCode", entpShipCostMap.get(i).get("SHIP_COST_CODE").toString());
							tmonShipCostMap.put("applyDate", entpShipCostMap.get(i).get("APPLY_DATE").toString());
							tmonShipCostMap.put("noShipIsland", entpShipCostMap.get(i).get("NO_SHIP_ISLAND").toString());
							tmonShipCostMap.put("modifyId", "BATCH");
							tmonShipCostMap.put("modifyDate", DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							rtnMsg = paTmonCommonService.updatePaTmonShipCostTx(tmonShipCostMap);
							
							log.info("04-2.반품지/출고지 수정 동기화 저장 : "+rtnMsg);
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								apiInfoMap.put("code","500");
								apiInfoMap.put("message",rtnMsg);
							} else {
								apiInfoMap.put("code","200");
								apiInfoMap.put("message","OK");
								procCount++;
							}
						} else {
							apiInfoMap.put("code","500");
							apiInfoMap.put("message","연동실패");
						}
					} catch(Exception e){
						String errMsg = e.getMessage();
						apiInfoMap.put("code","500");
						apiInfoMap.put("message", errMsg);
					}				
				}
			}
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);	
	}
	
	/**
	 * 정산데이터 조회 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "정산데이터 조회 API", notes = "정산데이터 조회 API", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류") 
			   })
	@RequestMapping(value = "/settlement-goods", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> settlementGoods(HttpServletRequest request, 
			@RequestParam(value = "Date" , required = false) String Date,
			@RequestParam(value = "delYn", required = false) String delYn) throws Exception{
		
		String	rtnMsg 			= Constants.SAVE_SUCCESS;
		String	prg_id			= "IF_PATMONAPI_00_006";
		String	paCode			= "";
		String searchDate 		= ComUtil.NVL(Date).length() == 10 ? Date : DateUtil.addDay(DateUtil.getCurrentNaverDateAsString(), -1, DateUtil.WEMP_DATE_FORMAT);
		ParamMap paramMap = new ParamMap();
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		Map<String, Object>	map = new HashMap<String, Object>();
		List<Map<String, Object>> settlementData = new ArrayList<Map<String, Object>>();
		
		paramMap.put("delYn", delYn);
		
		log.info("===== 정산데이터 조회 API Start =====");
		try {
			log.info("01. 정산데이터 조회 API 중복실행검사");
			paTmonConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paTmonConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.getString("url") + "?searchDate=" + searchDate;
			apiInfoMap.put("url", url);
			
			for(int i = 0; i < Constants.PA_TMON_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TMON_BROAD_CODE : Constants.PA_TMON_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				
				List<PaTmonSettlement> paTmonSettlementList	= new ArrayList<PaTmonSettlement>();
				
				log.info("02.정산데이터 조회 API 호출");
				map = paTmonConnectUtil.apiGetObjectByTmon(apiInfoMap, apiDataMap);
			
				settlementData = (List<Map<String, Object>>) map.get("settlementData");
				
				for(int j = 0; j < settlementData.size(); j++) {
			
					Map<String,Object> dealNo	  = (Map<String, Object>) settlementData.get(j).get("dealNo");
					Map<String,Object> orderNo	  = (Map<String, Object>) settlementData.get(j).get("orderNo");
					PaTmonSettlement settlementVO = new PaTmonSettlement();
					
					settlementVO.setTxSeqNo(settlementData.get(j).get("txSeqNo").toString());
					settlementVO.setTmonOrderNo( orderNo.get("tmonOrderNo") == null ? "" : orderNo.get("tmonOrderNo").toString());
					settlementVO.setTmonOrderSubNo(orderNo.get("tmonOrderSubNo") == null ? "" : orderNo.get("tmonOrderSubNo").toString());
					settlementVO.setIndividualOrderNo(orderNo.get("individualOrderNo") == null ? "" : orderNo.get("individualOrderNo").toString());
					settlementVO.setTmonDealNo(dealNo.get("tmonDealNo") == null ? "" : dealNo.get("tmonDealNo").toString());
					settlementVO.setTmonDealOptionNo(dealNo.get("tmonDealOptionNo") == null ? "" : dealNo.get("tmonDealOptionNo").toString());
					settlementVO.setManagedTitle(dealNo.get("managedTitle") == null ? "" : dealNo.get("managedTitle").toString());
					settlementVO.setDealOptionTitle(dealNo.get("dealOptionTitle") == null ? "" : dealNo.get("dealOptionTitle").toString());
					settlementVO.setPartnerNo(settlementData.get(j).get("partnerNo").toString());
					settlementVO.setSettleDealType(settlementData.get(j).get("settleDealType").toString());
					settlementVO.setSettleDealDetailType(settlementData.get(j).get("settleDealDetailType") == null ? "" : settlementData.get(j).get("settleDealDetailType").toString());
					settlementVO.setSalesDateTime(settlementData.get(j).get("salesDateTime").toString());
					settlementVO.setPayDueDate(settlementData.get(j).get("payDueDate").toString());
					settlementVO.setSellAmount(ComUtil.objToDouble(settlementData.get(j).get("sellAmount")));
					settlementVO.setPayBaseAmount(ComUtil.objToDouble(settlementData.get(j).get("payBaseAmount")));
					settlementVO.setPayRate(ComUtil.objToDouble(settlementData.get(j).get("payRate")));
					settlementVO.setVendorAmount(ComUtil.objToDouble(settlementData.get(j).get("vendorAmount")));
					settlementVO.setTmonAmount(ComUtil.objToDouble(settlementData.get(j).get("tmonAmount")));
					settlementVO.setDiscountPolicyNo(settlementData.get(j).get("discountPolicyNo") == null ? "" : settlementData.get(j).get("discountPolicyNo").toString());
					settlementVO.setDiscountPolicyName(settlementData.get(j).get("discountPolicyName") == null ? "" : settlementData.get(j).get("discountPolicyName").toString());
					
					paTmonSettlementList.add(settlementVO);
				
				}
				
				if(paTmonSettlementList.size() > 0 ) {
					rtnMsg = paTmonCommonService.savePaTmonSettlementTx(paTmonSettlementList,paramMap);
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						apiInfoMap.put("code","500");
						apiInfoMap.put("message",rtnMsg);
					} else {
						apiInfoMap.put("code","200");
						apiInfoMap.put("message","OK");
					}
				}else {
					apiInfoMap.put("code","404");
					apiInfoMap.put("message", getMessage("msg.no.select"));
				}
				log.info("===== 정산데이터 조회 API END =====");
			}
		}catch (Exception e) {
			paTmonConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paTmonConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);	
	}
		
/*
상품 Type
DP01 냉장냉동신선
DP02 해외직배송
DP03 해외구매대행
DP04 화물설치
DP05 주문제작
DP06 주문후발주
DP07 일반상품
DP08 예외상품

배송 Type
DD 당일배송
ND 익일배송
ED 예외발송
*/
	public ParamMap shipCostMapping(Map<String, Object> shipCostMap) {
		ParamMap template = new ParamMap();
		String deliveryFeePolicy			 = "";
		String longDistanceDeliveryAvailable = "";
		Integer deliveryFee					 = Integer.parseInt(shipCostMap.get("ORD_COST").toString());
		Integer jejuCost					 = Integer.parseInt(shipCostMap.get("JEJU_COST").toString()) - deliveryFee;
		Integer islandCost					 = Integer.parseInt(shipCostMap.get("ISLAND_COST").toString()) - deliveryFee;
		String deliveryTemplateName			 = shipCostMap.get("PA_CODE").toString()+shipCostMap.get("ENTP_CODE").toString() + shipCostMap.get("PRODUCT_TYPE").toString()
				+ shipCostMap.get("SHIP_MAN_SEQ").toString() + shipCostMap.get("RETURN_MAN_SEQ").toString()
				+ shipCostMap.get("SHIP_COST_CODE").toString() + shipCostMap.get("APPLY_DATE").toString() + shipCostMap.get("NO_SHIP_ISLAND").toString();	// 관리 배송비 템플릿 명
		
		
		if(shipCostMap.get("NO_SHIP_ISLAND").equals("1")) {	// 도서산간 배송비 결제여부 
			longDistanceDeliveryAvailable = "false";
		}else {
			longDistanceDeliveryAvailable = "true";
			template.put("longDistanceDeliveryPrepay", "true");	// 도서산간지역 배송비 주문시 결제 여부 
			template.put("longDistanceDeliveryFeeJeju", jejuCost > 0 ? jejuCost.toString() : "0");
			template.put("longDistanceDeliveryFeeExcludingJeju", islandCost > 0 ? islandCost.toString() : "0");
		}
		
		if(shipCostMap.get("PRODUCT_TYPE").equals("DP07")) {
			//일반상품
			template.put("deliveryType", "ND");
		} else {
			//주문제작, 설치배송상품
			template.put("deliveryType", "ED");
		}
		
		switch (shipCostMap.get("SHIP_COST_CODE").toString().substring(0,2)) {	// 배송비 종류 
		case "FR":
			deliveryFeePolicy = "FREE";
			deliveryFee = Integer.parseInt(shipCostMap.get("RETURN_COST").toString()) / 2 ;	// 무료배송일때만 반품비 적용( 임시적용 ) 
			
			break;
		case "CN":
		case "PL":
			deliveryFeePolicy = "CONDITION";
			template.put("deliveryFeeFreePrice", shipCostMap.get("SHIP_COST_BASE_AMT").toString());	// 조건부 무료배송 기준 값
			break;
		case "ID":
			deliveryFeePolicy = "PER";
			break;
		}	
		
		template.put("userDirectlyReturnDeliverySelectable", "false");
		template.put("deliveryTemplateName", deliveryTemplateName);
		template.put("bundledDeliveryAble", "false");
		template.put("deliveryFeePolicy", deliveryFeePolicy);
		template.put("deliveryFee", deliveryFee);
		template.put("productType", shipCostMap.get("PRODUCT_TYPE").toString());			
		template.put("longDistanceDeliveryAvailable", longDistanceDeliveryAvailable);
		template.put("partnerDeliveryAddressNo", shipCostMap.get("PA_SHIP_MAN_SEQ").toString());
		template.put("partnerReturnAddressNo", shipCostMap.get("PA_RETURN_MAN_SEQ").toString());
	
		return template;
	}
}
