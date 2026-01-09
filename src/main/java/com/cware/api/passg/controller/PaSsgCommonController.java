package com.cware.api.passg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaGoodsLimitChar;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.domain.model.PaSsgDisplayCategory;
import com.cware.netshopping.domain.model.PaSsgDisplayRecommendCategory;
import com.cware.netshopping.domain.model.PaSsgGoodsCert;
import com.cware.netshopping.domain.model.PaSsgSettlement;
import com.cware.netshopping.passg.common.service.PaSsgCommonService;
import com.cware.netshopping.passg.util.PaSsgConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/passg/common", description="SSG 공통")
@Controller("com.cware.api.passg.PaSsgCommonController")
@RequestMapping(value = "/passg/common")
public class PaSsgCommonController extends AbstractController  {

	private transient static Logger log = LoggerFactory.getLogger(PaSsgCommonController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;

	@Autowired
	PaSsgConnectUtil paSsgConnectUtil;
	
	@Resource(name = "passg.common.paSsgCommonService")
	private PaSsgCommonService paSsgCommonService;

	/**
	 * 업체 배송비 정책 등록 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "업체 배송비 정책 등록", notes = "업체 배송비 정책 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/entpcustshipcost-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> entpCustShipCostInsert(HttpServletRequest request) throws Exception {
		
		int	procCount						= 0;
		int	targetCount						= 0;
		String	prg_id 						= "IF_PASSGAPI_00_002";		
		String rtnMsg						= Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap				= new ParamMap();
		ParamMap	apiDataMap				= null;
		Map<String, Object> map				= new HashMap<String, Object>() ;
		Map<String, Object> result			= new HashMap<String, Object>();
		Map<String,Object> paramShipCost	= null;
		
		try {
			log.info(prg_id + " SSG 업체 배송비 정책 등록 - 01.프로그램 중복 실행 검사 [start]");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info(prg_id + " SSG 업체 배송비 정책 등록 - 02.대상 리스트 조회 ");
			List<HashMap<String,Object>> entpShipCostMap = paSsgCommonService.selectEntpSlipCost(apiInfoMap);
			
			if( entpShipCostMap == null || entpShipCostMap.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("partner.no_change_data"));
			}else {
				targetCount = entpShipCostMap.size();
				
				for(HashMap<String, Object> entpShipCost : entpShipCostMap){
					
					try {
						apiDataMap = new ParamMap();
						paramShipCost = new HashMap<String, Object>();
						
						log.info(prg_id + " SSG 업체 배송비 정책 등록 - 03.대상 정보 매핑 ");
						paramShipCost = shipCostMapping(entpShipCost);
						apiDataMap.put("requestShppcstPlcyInsert", paramShipCost);
						
						log.info(prg_id + " SSG 업체 배송비 정책 등록 - 04.통신 ");
						apiInfoMap.put("paCode", entpShipCost.get("PA_CODE").toString());
						map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
						result = (HashMap<String,Object>)map.get("result");
						
						if(result.get("resultCode").toString().equals("00")) {// 나중에 Constants.PA_SSG_SUCCESS_CODE 들어오면 바꿔주기
							
							ParamMap ShipCostMap = new ParamMap();
							ShipCostMap.put("paCode", entpShipCost.get("PA_CODE").toString());
							ShipCostMap.put("shppcstAplUnitCd", entpShipCost.get("SHPPCST_APL_UNIT_CD").toString());
							ShipCostMap.put("shppcstPlcyDivCd", entpShipCost.get("SHPPCST_PLCY_DIV_CD").toString());
							ShipCostMap.put("collectYn", entpShipCost.get("COLLECT_YN").toString());
							ShipCostMap.put("shipCostBaseAmt", entpShipCost.get("SHIP_COST_BASE_AMT").toString());
							ShipCostMap.put("shipCost", entpShipCost.get("SHIP_COST").toString());
							ShipCostMap.put("shppcstId", result.get("shppcstId").toString());
							
							log.info(prg_id + " SSG 업체 배송비 정책 등록 - 05.통신결과 저장 ");
							rtnMsg = paSsgCommonService.savePaSsgShipCostTx(ShipCostMap);
							
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
							apiInfoMap.put("message", result.get("resultDesc").toString());
						}
					} catch (Exception e) {
						apiInfoMap.put("code","500");
						apiInfoMap.put("message", e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	private Map<String, Object> shipCostMapping(HashMap<String, Object> entpShipCost) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String shppcstPlcyDivCd = entpShipCost.get("SHPPCST_PLCY_DIV_CD").toString();
		
		map.put("shppcstPlcyDivCd", shppcstPlcyDivCd);
		if ("10".equals(shppcstPlcyDivCd)) { //24.08.24 부로 추가배송비> 배송비 적용단위, 선착불 허용형태 항목 제거
			map.put("shppcstAplUnitCd", entpShipCost.get("SHPPCST_APL_UNIT_CD").toString());			
			map.put("prpayCodDivCd", entpShipCost.get("COLLECT_YN").toString().equals("0") ? "10" : "20");
		}
		
		/* 반품배송비가 상품 수량별이면 주문배송비도 상품 수량별이여야 등록 가능
		if("20".equals(shppcstPlcyDivCd)) { //반품배송비정책
			map.put("shppcstAplUnitCd", "30");
		} else {
			if("10".equals(shppcstPlcyDivCd)) {//출고배송비이면
				map.put("shppcstExmpCritnAmt", entpShipCost.get("SHIP_COST_BASE_AMT").toString());
			}
			map.put("shppcstAplUnitCd", "10");			
		}
		*/
		if("10".equals(shppcstPlcyDivCd)) {//출고배송비이면
			map.put("shppcstExmpCritnAmt", entpShipCost.get("SHIP_COST_BASE_AMT").toString());
		}
		map.put("shppcst", entpShipCost.get("SHIP_COST").toString());
		map.put("bascPlcyYn", "N");
		map.put("shppcstAplYn", "Y");
		
		return map;
	}

	/**
	 * 업체 배송지 주소/택배계약정보 등록 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "업체 배송지 주소/택배계약정보 등록", notes = "업체 배송지 주소/택배계약정보 등록", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/ssgentpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> ssgEntpSlipInsert(HttpServletRequest request,
			@ApiParam(name="entpCode", 	 value="업체코드", 	defaultValue = "") @RequestParam(value="entpCode", required=true) String entpCode,
			@ApiParam(name="entpManSeq", value="업체담당자순번", defaultValue = "") @RequestParam(value="entpManSeq", required=true) String entpManSeq,
			@ApiParam(name="paCode", 	 value="제휴사코드",   defaultValue = "") @RequestParam(value="paCode", required=true) String paCode) throws Exception{
		
		Matcher EntpManHp1							 = null;
		Matcher EntpManHp2							 = null;
		String prg_id								 = "IF_PASSGAPI_00_004";		
		String dateTime								 = systemService.getSysdatetimeToString();
		String rtnMsg								 = Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap						 = new ParamMap();
		ParamMap	apiDataMap						 = new ParamMap();
		Map<String, Object> map						 = new HashMap<String, Object>();
		Map<String, Object> result					 = new HashMap<String, Object>();
		Map<String, Object> requestMap				 = new HashMap<String, Object>();
		Map<String, Object> venAddrDelInfo			 = new HashMap<String, Object>();
		Map<String, Object> venAddrDelInfoDto		 = new HashMap<String, Object>();
		List<HashMap<String, Object>> venAddrDelInfoList = new ArrayList<HashMap<String, Object>>();
		Pattern passPattern = Pattern.compile("(\\w)\\1\\1\\1");
		
		try {
			
			log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 01.프로그램 중복 실행 검사 [start]");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			PaEntpSlip paEntpSlip = new PaEntpSlip();
			paEntpSlip.setEntpCode(entpCode);
			paEntpSlip.setEntpManSeq(entpManSeq);
			paEntpSlip.setPaCode(paCode);
			
			log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 02.대상 검색 ");
			Map<String,Object> entpSlipMap = paSsgCommonService.selectSlipInsertList(paEntpSlip);	
			
		    if(entpSlipMap == null) {
		    	apiInfoMap.put("code","404");
		    	apiInfoMap.put("message",getMessage("partner.no_change_data"));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
		    }
		    
		    
		    String entpManCheck = entpSlipMap.get("ENTP_MAN_HP1") == null ? "" : entpSlipMap.get("ENTP_MAN_HP1").toString();
		 
		    
		    if((entpSlipMap.get("ENTP_MAN_HP").toString()).length() < 11) {
		    	entpSlipMap.put("ENTP_MAN_HP" , "010-1234-5678");
		    }else {
		    	
		    	if("".equals(entpManCheck)) {
		    		EntpManHp1 = passPattern.matcher(entpSlipMap.get("ENTP_MAN_TEL1").toString());
			    	EntpManHp2 = passPattern.matcher(entpSlipMap.get("ENTP_MAN_TEL2").toString());
		    	}else {
		    		EntpManHp1 = passPattern.matcher(entpSlipMap.get("ENTP_MAN_HP2").toString());
			    	EntpManHp2 = passPattern.matcher(entpSlipMap.get("ENTP_MAN_HP3").toString());
		    	}
		    	
			    if(EntpManHp2.find() || EntpManHp1.find()) {
		    		 entpSlipMap.put("ENTP_MAN_HP" , "010-1234-5678");
		    	}
			    
		    }
		    entpSlipMap.put("PA_CODE", paCode);
		    
		    log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 03.파라미터 생성 ");
		    
		    requestMap = entpSlipMapping(entpSlipMap);	
		    apiDataMap.put("requestVenAddrInsert", requestMap);
		    
		    try {
		    	log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 04. 호출 ");
		    	apiInfoMap.put("paCode", paCode);
		    	map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
		    	
		    	result = (Map<String,Object>)map.get("result");
		    	
		    	if(result.get("resultCode").toString().equals("00")) {
		    		
		    		venAddrDelInfoList = (List<HashMap<String, Object>>)result.get("venAddrDelInfo");
		    		venAddrDelInfo = (Map<String, Object>)venAddrDelInfoList.get(0);
		    		venAddrDelInfoDto = (Map<String, Object>)venAddrDelInfo.get("venAddrDelInfoDto");
		    		
		    		log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 05. 통신 성공 ");
		    		paEntpSlip.setPaAddrGb(entpSlipMap.get("ENTP_MAN_GB").toString());
		    		paEntpSlip.setPaAddrSeq(venAddrDelInfoDto.get("grpAddrId").toString());
		    		paEntpSlip.setPaBundleNo(venAddrDelInfoDto.get("jibunAddrId").toString()); //지번주소ID
		    		paEntpSlip.setNewGmktShipNo(venAddrDelInfoDto.get("doroAddrId").toString()); //도로명주소ID
		    		paEntpSlip.setTransTargetYn("0");
		    		paEntpSlip.setInsertId(Constants.PA_SSG_PROC_ID);
					paEntpSlip.setModifyId(Constants.PA_SSG_PROC_ID);
					paEntpSlip.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					rtnMsg = paSsgCommonService.savePaSsgEntpSlipTx(paEntpSlip);
					log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 등록 - 05. 저장 ");
					
					if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
						apiInfoMap.put("code","404");
						apiInfoMap.put("message",rtnMsg);
						return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
					} else {
						apiInfoMap.put("code","200");
						apiInfoMap.put("message","OK");
					}
		    	}else {
		    		apiInfoMap.put("code","404");
		    		apiInfoMap.put("message", entpSlipMap.get("ENTP_CODE").toString() + "|" + entpSlipMap.get("ENTP_MAN_SEQ").toString() + "|" + result.get("resultDesc").toString());
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
		    	}
		    } catch(Exception e){ 
				String errMsg = e.getMessage();
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", errMsg);
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
			}
		} catch (Exception e) {
			apiInfoMap.put("code","500");
			apiInfoMap.put("message", e.getMessage());
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	/**
	 * 업체 배송지 주소/택배계약정보 수정 API
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "업체 배송지 주소/택배계약정보 수정", notes = "업체 배송지 주소/택배계약정보 수정", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"), 
			@ApiResponse(code = 404, message = "처리할 내역이 없습니다."),
			@ApiResponse(code = 490, message = "중복처리 오류 발생."),
		    @ApiResponse(code = 500, message = "시스템 오류")  })
	@RequestMapping(value = "/ssgentpslip-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> ssgEntpSlipModify(HttpServletRequest request) throws Exception{
		
		int procCount								 = 0;
		int targetCount								 = 0;
		String entpManCheck							 = "";
		String paCode								 = "";
		String prg_id								 = "IF_PASSGAPI_00_005";		
		String dateTime								 = systemService.getSysdatetimeToString();
		String rtnMsg								 = Constants.SAVE_SUCCESS;
		ParamMap	apiInfoMap						 = new ParamMap();
		ParamMap	apiDataMap						 = new ParamMap();
		Map<String, Object> map						 = new HashMap<String, Object>();
		Map<String, Object> result					 = new HashMap<String, Object>();
		Map<String, Object> requestMap				 = new HashMap<String, Object>();
		List<HashMap<String,Object>> entpSlipUpdateList = null;
		HashMap<String, Object> entpSlipMap = null;
		PaEntpSlip paEntpSlip				= null;
		Matcher EntpManHp1					= null;
		Matcher EntpManHp2					= null;
		Pattern passPattern = Pattern.compile("(\\w)\\1\\1\\1");
		try {
			
			log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 01.프로그램 중복 실행 검사 [start]");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 02. 대상 검색");
			entpSlipUpdateList = paSsgCommonService.selectEntpSlipUpdateList();
			
			targetCount += entpSlipUpdateList.size();
			
			if(entpSlipUpdateList.size() <= 0) {
				apiInfoMap.put("code", "404");
				apiInfoMap.put("message", getMessage("partner.no_change_data"));
			}else {
				for(int i= 0; i< entpSlipUpdateList.size(); i++) {
					
				    entpManCheck = entpSlipUpdateList.get(i).get("ENTP_MAN_HP1") == null ? "" : entpSlipUpdateList.get(i).get("ENTP_MAN_HP1").toString();
				   
				    if(entpSlipUpdateList.get(i).get("ENTP_MAN_HP").toString().length() < 11) {
				    	entpSlipUpdateList.get(i).put("ENTP_MAN_HP" , "010-1234-5678");
				    }else {
				    	if("".equals(entpManCheck)) {
					    	EntpManHp1 = passPattern.matcher(entpSlipUpdateList.get(i).get("ENTP_MAN_TEL1").toString());
					    	EntpManHp2 = passPattern.matcher(entpSlipUpdateList.get(i).get("ENTP_MAN_TEL2").toString());
					    }else {
					    	EntpManHp1 = passPattern.matcher(entpSlipUpdateList.get(i).get("ENTP_MAN_HP2").toString());
					    	EntpManHp2 = passPattern.matcher(entpSlipUpdateList.get(i).get("ENTP_MAN_HP3").toString());
					    }
				    	
				    	if(EntpManHp1.find() || EntpManHp2.find()) {
				    		entpSlipUpdateList.get(i).put("ENTP_MAN_HP" , "010-1234-5678");
				    	}
				    }
					
					try {
						entpSlipMap = (HashMap<String, Object>) entpSlipUpdateList.get(i);	
						
						paCode = entpSlipMap.get("PA_CODE").toString();
						
						log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 03.파라미터 생성 ");
						requestMap = entpSlipMapping(entpSlipMap);	
						apiDataMap.put("requestVenAddrInsert", requestMap);
						
						log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 04. 호출 ");
						apiInfoMap.put("paCode", paCode);
						map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);

				    	result = (Map<String,Object>)map.get("result");
				    	
				    	if(result.get("resultCode").toString().equals("00")) {
				    		log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 05. API 성공  ");
				    		paEntpSlip = new PaEntpSlip();
				    		paEntpSlip.setPaCode(entpSlipMap.get("PA_CODE").toString());
							paEntpSlip.setEntpCode(entpSlipMap.get("ENTP_CODE").toString());
							paEntpSlip.setPaAddrSeq(entpSlipMap.get("PA_ADDR_SEQ").toString());
							paEntpSlip.setTransTargetYn("0");
							paEntpSlip.setModifyId(Constants.PA_SSG_PROC_ID);
							paEntpSlip.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							rtnMsg = paSsgCommonService.updatePaSsgEntpSlipTx(paEntpSlip);
							
							log.info(prg_id + " SSG 업체 배송지 주소/택배계약정보 수정 - 06. 결과 저장   ");
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								apiInfoMap.put("code","500");
								apiInfoMap.put("message",rtnMsg);
							} else {
								apiInfoMap.put("code","200");
								apiInfoMap.put("message","OK");
								procCount++;
							}
				    	}else {
				    		apiInfoMap.put("code",result.get("resultCode").toString());
				    		apiInfoMap.put("message",result.get("resultDesc").toString());
						}
						
					}catch (Exception e) {
						apiInfoMap.put("code","500");
						apiInfoMap.put("message", e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			if(targetCount != procCount){
				apiInfoMap.put("code","500");
				apiInfoMap.put("message", "대상건수:" + targetCount + ", 성공건수:" + procCount);
			}
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	public Map<String, Object> entpSlipMapping(Map<String, Object> entpSlipMap){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String addrlcAntnmNm  = entpSlipMap.get("PA_CODE").toString() + "-" + entpSlipMap.get("ENTP_CODE").toString() + "-" + entpSlipMap.get("ENTP_MAN_SEQ").toString();
		
		if(!"".equals(entpSlipMap.get("PA_ADDR_SEQ")) && entpSlipMap.get("PA_ADDR_SEQ") != null) { //수정이면
			map.put("grpAddrId", entpSlipMap.get("PA_ADDR_SEQ").toString());
		}
		
		map.put("addrlcAntnmNm", addrlcAntnmNm);
		map.put("zipcd", entpSlipMap.get("POST_NO").toString());
		map.put("doroAddrBasc", entpSlipMap.get("STD_ROAD_POST_ADDR1").toString());
		map.put("doroAddrDtl", entpSlipMap.get("STD_ROAD_POST_ADDR2").toString());
		map.put("jibunAddrBasc", entpSlipMap.get("STD_POST_ADDR1").toString());
		map.put("jibunAddrDtl", entpSlipMap.get("STD_POST_ADDR2").toString());
		map.put("bascAddrYn", "N");
		map.put("cnts", entpSlipMap.get("ENTP_MAN_HP").toString());
		//업체 지정택배 기타로 등록. (등록 안할경우 SSG 자체 회수처리됨)
		map.put("delicoVenId", "0000033028");
		
		return map;
	}
	
	//상품 고시 분류 조회(수동으로 데이터 처리)
	@RequestMapping(value = "/offer-group-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> offerGroupList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_009";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		paCode		= "A1"; //하드코딩
				
		try {
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
						
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {
				List<Map<String, Object>> itemMngPropClss  = new ArrayList<Map<String,Object>>();
				itemMngPropClss = (List<Map<String, Object>>)(result.get("itemMngPropClss"));
				
				List<Map<String, Object>> itemMngPropCls  = new ArrayList<Map<String,Object>>();
				itemMngPropCls = (List<Map<String, Object>>)(itemMngPropClss.get(0).get("itemMngPropCls"));
				
				for(int i=0; i<itemMngPropCls.size(); i++) {
					offerDetailList(request, itemMngPropCls.get(i).get("itemMngPropClsId").toString());
				}
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//상품 고시 상세 항목 조회(수동으로 데이터 처리)
	@RequestMapping(value = "/offer-detail-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> offerDetailList(HttpServletRequest request,
			@ApiParam(name = "itemMngPropClsId", value = "정보고시번호",  defaultValue = "") @RequestParam(value = "itemMngPropClsId", required = true) String itemMngPropClsId) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_010";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		List<PaOfferCode> paOfferCodeList = new ArrayList<PaOfferCode>();
		PaOfferCode paOfferCode = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			String url = apiInfoMap.get("url").toString();
			apiInfoMap.put("url", url.replace("{itemMngPropClsId}", itemMngPropClsId));
			
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {	
				
				List<Map<String, Object>> itemMngProps  = new ArrayList<Map<String,Object>>();
				itemMngProps = (List<Map<String, Object>>)(result.get("itemMngProps"));
				
				List<Map<String, Object>> itemMngProp  = new ArrayList<Map<String,Object>>();
				itemMngProp = (List<Map<String, Object>>)(itemMngProps.get(0).get("itemMngProp"));
				
				for(int i=0; i<itemMngProp.size(); i++) {
					
					paOfferCode = new PaOfferCode();
					paOfferCode.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
					paOfferCode.setPaOfferType(itemMngProp.get(i).get("itemMngPropClsId").toString());
					paOfferCode.setPaOfferTypeName(itemMngProp.get(i).get("itemMngPropClsNm").toString());
					paOfferCode.setPaOfferCode(itemMngProp.get(i).get("itemMngPropId").toString());
					paOfferCode.setPaOfferCodeName(itemMngProp.get(i).get("itemMngPropNm").toString());
					paOfferCode.setRequiredYn("Y".equals(itemMngProp.get(i).get("mndtyYn").toString())?"1":"0");
					paOfferCode.setSortSeq("0");
					paOfferCode.setUseYn("1");
					paOfferCode.setUnitRequiredYn("0");
					paOfferCode.setIptMthdCd(itemMngProp.get(i).get("iptMthdCd").toString());					
					paOfferCode.setInsertId(Constants.PA_SSG_PROC_ID);
					paOfferCode.setModifyId(Constants.PA_SSG_PROC_ID);
					paOfferCode.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paOfferCode.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					paOfferCodeList.add(paOfferCode);
				}
				
				paSsgCommonService.savePaOfferCodeListTx(paOfferCodeList);
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//공통 콛 토회(수동으로 데이터 처리)
	
	//브팬드 코드 조회
	@RequestMapping(value = "/brand-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> brandList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_011";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		List<PaBrand> paBrandList = new ArrayList<PaBrand>();
		PaBrand		paBrand = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);						
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {
				List<Map<String, Object>> brands  = new ArrayList<Map<String,Object>>();
				brands = (List<Map<String, Object>>)(result.get("brands"));
								
				List<Map<String, Object>> brand  = new ArrayList<Map<String,Object>>();
				brand = (List<Map<String, Object>>)(brands.get(0).get("brand"));
				
				for(int i=0; i<brand.size(); i++) {
					
					paBrand = new PaBrand();
					paBrand.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
					paBrand.setPaBrandNo(brand.get(i).get("brandId").toString());
					paBrand.setPaBrandName(brand.get(i).get("brandNm").toString());
					paBrand.setUseYn("Y".equals(brand.get(i).get("useYn").toString())? "1":"0");
					paBrand.setInsertId(Constants.PA_SSG_PROC_ID);
					paBrand.setModifyId(Constants.PA_SSG_PROC_ID);
					paBrand.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paBrand.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					paBrandList.add(paBrand);
				}
				
				paSsgCommonService.savePaBrandTx(paBrandList);
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//원산지 조회
	@RequestMapping(value = "/origin-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> originList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_012";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		List<PaOrigin> paOriginList = new ArrayList<PaOrigin>();
		PaOrigin	paOrigin = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);						
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {
				List<Map<String, Object>> orplcs  = new ArrayList<Map<String,Object>>();
				orplcs = (List<Map<String, Object>>)(result.get("orplcs"));
								
				List<Map<String, Object>> orplc  = new ArrayList<Map<String,Object>>();
				orplc = (List<Map<String, Object>>)(orplcs.get(0).get("orplc"));
				
				for(int i=0; i<orplc.size(); i++) {
					
					paOrigin = new PaOrigin();
					
					paOrigin.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
					paOrigin.setPaOriginCode(orplc.get(i).get("orplcId").toString());
					paOrigin.setPaOriginName(orplc.get(i).get("orplcNm").toString());					
					paOrigin.setRemark1(orplc.get(i).get("orplcYn").toString());
					paOrigin.setRemark2(orplc.get(i).get("manufCntryYn").toString());
					paOrigin.setRemark3(orplc.get(i).get("orplcDivNm").toString());					
					paOrigin.setInsertId(Constants.PA_SSG_PROC_ID);
					paOrigin.setModifyId(Constants.PA_SSG_PROC_ID);
					paOrigin.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paOrigin.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					paOriginList.add(paOrigin);
				}
				
				paSsgCommonService.savePaOrigindTx(paOriginList);
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//금칙어 조회
	@RequestMapping(value = "/forbiddenword-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> forbiddenwordList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_013";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		List<PaGoodsLimitChar>	paLimitCharList = new ArrayList<PaGoodsLimitChar>();
		PaGoodsLimitChar		paLimitChar = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);						
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {
				
				List<Map<String, Object>> prohibitWords  = new ArrayList<Map<String,Object>>();
				prohibitWords = (List<Map<String, Object>>)(result.get("prohibitWords"));
								
				List<Map<String, Object>> prohibited  = new ArrayList<Map<String,Object>>();
				prohibited = (List<Map<String, Object>>)(prohibitWords.get(0).get("prohibited"));
				
				for(int i=0; i<prohibited.size(); i++) {					
					
					paLimitChar = new PaGoodsLimitChar();					
										
					paLimitChar.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
					paLimitChar.setLimitChar(prohibited.get(i).get("prohibitedWord").toString());
					paLimitChar.setUseYn("1");
					paLimitChar.setInsertId(Constants.PA_SSG_PROC_ID);
					paLimitChar.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					
					paLimitCharList.add(paLimitChar);
				}
				
				paSsgCommonService.savePaGoodsLimitCharTx(paLimitCharList);
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//표준분류별 인증정보 조회
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@RequestMapping(value = "/authenticationinfo-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> authenticationinfoList(HttpServletRequest request,
			@ApiParam(name="stdCtgId", 	 value="SSG 표준분류 ID", 	defaultValue = "") @RequestParam(value="stdCtgId", required=false) String stdCtgId) throws Exception {
		
		String		prg_id 		= "IF_PASSGAPI_00_021";
		String		paCode		= "A1"; //하드코딩
		String		dateTime	= "";
		String 		rtnMsg		= "";
		ParamMap	apiDataMap	= new ParamMap();
		ParamMap	apiInfoMap	= new ParamMap();
		Map<String, Object> map	= new HashMap<String, Object>();
		List<Map<String, Object>> itemAppeCert  = null;
		PaSsgGoodsCert paSsgGoodsCert			= null;
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			String url = apiInfoMap.get("url").toString();
			
			List<PaGoodsKinds> paGoodsKindsList = paSsgCommonService.selectSsgGoodsCertList();
			
			for(int i = 0; i < paGoodsKindsList.size(); i ++) {
				PaGoodsKinds paGoodsKinds = paGoodsKindsList.get(i);
				apiInfoMap.put("url", url.replace("{stdCtgId}", paGoodsKinds.getPaLmsdKey().toString()));
				
				try {
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					
					HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
					
					if(result.get("resultCode").toString().equals("00")) {
						List<PaSsgGoodsCert>	paSsgGoodsCertList = new ArrayList<PaSsgGoodsCert>();
						List<Map<String, Object>> itemAppeCerts  = new ArrayList<Map<String,Object>>();
						
						itemAppeCerts = (List<Map<String, Object>>)(result.get("itemAppeCerts"));
						
						if(!"".equals(itemAppeCerts.get(0))) {
							itemAppeCert  = new ArrayList<Map<String,Object>>();
							
							if("java.util.ArrayList".equals(itemAppeCerts.get(0).get("itemAppeCert").getClass().getName())) {
								itemAppeCert = (List<Map<String, Object>>) itemAppeCerts.get(0).get("itemAppeCert");
								
							} else {
								itemAppeCert.add((Map<String, Object>) itemAppeCerts.get(0).get("itemAppeCert"));
							}
							
							for(int j=0; j<itemAppeCert.size(); j++) {
								paSsgGoodsCert = new PaSsgGoodsCert();
								
								paSsgGoodsCert.setStdCtgId(paGoodsKinds.getPaLmsdKey().toString());
								paSsgGoodsCert.setItemAppePropClsId(itemAppeCert.get(j).get("itemAppePropClsId").toString());
								paSsgGoodsCert.setItemAppePropClsNm(itemAppeCert.get(j).get("itemAppePropClsNm").toString());
								paSsgGoodsCert.setItemAppePropId(itemAppeCert.get(j).get("itemAppePropId").toString());
								paSsgGoodsCert.setItemAppePropNm(itemAppeCert.get(j).get("itemAppePropNm").toString());
								paSsgGoodsCert.setItemAppePropTypeCd(itemAppeCert.get(j).get("itemAppePropTypeCd").toString());
								paSsgGoodsCert.setItemAppePropDtlTypeCd(itemAppeCert.get(j).get("itemAppePropDtlTypeCd").toString());
								paSsgGoodsCert.setRepPropTypeCd(itemAppeCert.get(j).containsKey("repPropTypeCd")?itemAppeCert.get(j).get("itemAppeCert").toString():"");
								paSsgGoodsCert.setRepPropCntt(itemAppeCert.get(j).containsKey("repPropCntt")?itemAppeCert.get(j).get("repPropCntt").toString():"");
								paSsgGoodsCert.setMaxIptVal(itemAppeCert.get(j).containsKey("maxIptVal")?itemAppeCert.get(j).get("maxIptVal").toString():"");
								paSsgGoodsCert.setMndtyYn(itemAppeCert.get(j).get("mndtyYn").toString());
								paSsgGoodsCert.setPrcdAppePropId(itemAppeCert.get(j).containsKey("prcdAppePropId")?itemAppeCert.get(j).get("prcdAppePropId").toString():"");
								paSsgGoodsCert.setPrcdAppePropCntt(itemAppeCert.get(j).containsKey("prcdAppePropCntt")?itemAppeCert.get(j).get("prcdAppePropCntt").toString():"");
								paSsgGoodsCert.setInsertId(Constants.PA_SSG_PROC_ID);
								paSsgGoodsCert.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								paSsgGoodsCertList.add(paSsgGoodsCert);
							}
							
							rtnMsg = paSsgCommonService.savePaSsgGoodsCertTx(paSsgGoodsCertList);
							if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
								apiInfoMap.put("code","500");
								apiInfoMap.put("message",rtnMsg);
							} else {
								apiInfoMap.put("code","200");
								apiInfoMap.put("message","OK");
							}
							
						}
					}
				} catch (Exception e) {
					System.out.println(paGoodsKinds.getPaLmsdKey().toString());
				}
			}
			
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//표준 카테고리 조회
	@RequestMapping(value = "/standard-category-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> standardCategoryList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_022";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		List<PaGoodsKinds>	paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		PaGoodsKinds		paGoodsKinds = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);						
			
			HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
			if("SUCCESS".equals(result.get("resultMessage"))) {
				
				List<Map<String, Object>> stdctgs  = new ArrayList<Map<String,Object>>();
				stdctgs = (List<Map<String, Object>>)(result.get("stdctgs"));
								
				List<Map<String, Object>> stdctg  = new ArrayList<Map<String,Object>>();
				stdctg = (List<Map<String, Object>>)(stdctgs.get(0).get("stdctg"));
				
				for(int i=0; i<stdctg.size(); i++) {					
					
					paGoodsKinds = new PaGoodsKinds();
					
					paGoodsKinds.setPaGroupCode(Constants.PA_SSG_GROUP_CODE);
					paGoodsKinds.setPaLmsdKey(stdctg.get(i).get("stdCtgDclsId").toString());
					paGoodsKinds.setPaLgroup(stdctg.get(i).get("stdCtgLclsId").toString());
					paGoodsKinds.setPaMgroup(stdctg.get(i).get("stdCtgMclsId").toString());
					paGoodsKinds.setPaSgroup(stdctg.get(i).get("stdCtgSclsId").toString());
					paGoodsKinds.setPaDgroup(stdctg.get(i).get("stdCtgDclsId").toString());
					paGoodsKinds.setPaLgroupName(stdctg.get(i).get("stdCtgLclsNm").toString());
					paGoodsKinds.setPaMgroupName(stdctg.get(i).get("stdCtgMclsNm").toString());
					paGoodsKinds.setPaSgroupName(stdctg.get(i).get("stdCtgSclsNm").toString());
					paGoodsKinds.setPaDgroupName(stdctg.get(i).get("stdCtgDclsNm").toString());
					paGoodsKinds.setUseYn("1");
					paGoodsKinds.setRemark(stdctg.get(i).get("itemMngPropClsNm").toString());
					paGoodsKinds.setInsertId(Constants.PA_SSG_PROC_ID);
					paGoodsKinds.setModifyId(Constants.PA_SSG_PROC_ID);
					paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	

					paGoodsKindsList.add(paGoodsKinds);
				}
				
				paSsgCommonService.savePaGoodsKindsTx(paGoodsKindsList);
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//전시 카테고리 조회
	@RequestMapping(value = "/display-category-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> displayCategoryList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_024";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		int			page		= 1;
		boolean 	flag = true;
		List<PaSsgDisplayCategory>	paSsgDisplayCategoryList = new ArrayList<PaSsgDisplayCategory>();
		PaSsgDisplayCategory		paSsgDisplayCategory = null;
				
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);	
			String url = apiInfoMap.get("url").toString();
			
			while(flag) {
				
				apiInfoMap.put("url", url + "&page=" + page);
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
				
				if("SUCCESS".equals(result.get("resultMessage"))) {
					
					List<Map<String, Object>> displayCategorys  = new ArrayList<Map<String,Object>>();
					displayCategorys = (List<Map<String, Object>>)(result.get("displayCategorys"));
										
					List<Map<String, Object>> category  = new ArrayList<Map<String,Object>>();
					category = (List<Map<String, Object>>)(displayCategorys.get(0).get("category"));
					
					for(int i=0; i<category.size(); i++) {
						
						paSsgDisplayCategory = new PaSsgDisplayCategory();
						
						paSsgDisplayCategory.setDispCtgId(category.get(i).get("dispCtgId").toString());
						paSsgDisplayCategory.setDispCtgNm(category.get(i).get("dispCtgNm").toString());
						paSsgDisplayCategory.setDispCtgClsCd(category.get(i).get("dispCtgClsCd").toString());
						paSsgDisplayCategory.setDispCtgPathNm(category.get(i).get("dispCtgPathNm").toString());
						paSsgDisplayCategory.setAplSiteNo(category.get(i).get("aplSiteNo").toString());
						paSsgDisplayCategory.setAplSiteNoNm(category.get(i).get("aplSiteNoNm").toString());
						paSsgDisplayCategory.setDispCtgLastLvlYn("Y".equals(category.get(i).get("dispCtgLastLvlYn").toString())?"1":"0");
						paSsgDisplayCategory.setDispYn("Y".equals(category.get(i).get("dispYn").toString())?"1":"0");
						paSsgDisplayCategory.setInsertId(Constants.PA_SSG_PROC_ID);
						paSsgDisplayCategory.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));

						paSsgDisplayCategoryList.add(paSsgDisplayCategory);
					}
					
					if(category.size() < 100) {
						flag = false;
					}
					
					page++;
				}
			}
			
			paSsgCommonService.savePaSsgDisplayCategoryTx(paSsgDisplayCategoryList);
			
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	//전시 추천  카테고리 조회
	@RequestMapping(value = "/recommend-category-list", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public ResponseEntity<?> recommendCategoryList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= new ParamMap();
		String		prg_id 		= "IF_PASSGAPI_00_023";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String 		rtnMsg		= "";
		String		dateTime	= "";
		String		paCode		= "A1"; //하드코딩
		
		PaSsgDisplayRecommendCategory paSsgDisplayRecommendCategory = null;
		List<PaSsgDisplayRecommendCategory>	paSsgDisplayRecommendCategoryList = null;
		List<Map<String, Object>> ctg  = null;
		
		try {
			dateTime = systemService.getSysdatetimeToString();
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);	
			String url = apiInfoMap.get("url").toString();
			
			List<PaGoodsKinds> goodsKindsList = paSsgCommonService.selectGoodsKindsList();
			
			for(int i = 0; i < goodsKindsList.size(); i ++) {
				
				PaGoodsKinds paGoodsKinds = goodsKindsList.get(i);
				apiInfoMap.put("url", url.replace("{stdCtgDclsId}", paGoodsKinds.getPaLmsdKey().toString()));
				
				try {
					map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
					
					HashMap<String, Object> result =  (HashMap<String, Object>) map.get("result");
					
					if(result.get("resultCode").toString().equals("00")) {
						
						paSsgDisplayRecommendCategoryList = new ArrayList<PaSsgDisplayRecommendCategory>();
						List<Map<String, Object>> dispCtgs  = new ArrayList<Map<String,Object>>();
						
						dispCtgs = (List<Map<String, Object>>)(result.get("dispCtgs"));
						
						if(!"".equals(dispCtgs.get(0))) {
							ctg  = new ArrayList<Map<String,Object>>();
							
							if("java.util.ArrayList".equals(dispCtgs.get(0).get("ctg").getClass().getName())) {
								ctg = (List<Map<String, Object>>) dispCtgs.get(0).get("ctg");
								
							} else {
								ctg.add((Map<String, Object>) dispCtgs.get(0).get("ctg"));
							}
							
							for(int j=0; j<ctg.size(); j++) {
								paSsgDisplayRecommendCategory = new PaSsgDisplayRecommendCategory();
								
								paSsgDisplayRecommendCategory.setSiteNo(ctg.get(j).get("siteNo").toString());							
								paSsgDisplayRecommendCategory.setStdCtgDclsId(paGoodsKinds.getPaLmsdKey().toString());							
								paSsgDisplayRecommendCategory.setDispCtgClsNm(ctg.get(j).get("dispCtgClsNm").toString());
								paSsgDisplayRecommendCategory.setDispCtgLclsId(ctg.get(j).get("dispCtgLclsId").toString());
								paSsgDisplayRecommendCategory.setDispCtgLclsNm(ctg.get(j).get("dispCtgLclsNm").toString());
								paSsgDisplayRecommendCategory.setDispCtgMclsId(ctg.get(j).get("dispCtgMclsId").toString());
								paSsgDisplayRecommendCategory.setDispCtgMclsNm(ctg.get(j).get("dispCtgMclsNm").toString());
								paSsgDisplayRecommendCategory.setDispCtgSclsId(ctg.get(j).containsKey("dispCtgSclsId")?ctg.get(j).get("dispCtgSclsId").toString():"");
								paSsgDisplayRecommendCategory.setDispCtgSclsNm(ctg.get(j).containsKey("dispCtgSclsNm")?ctg.get(j).get("dispCtgSclsNm").toString():"");							
								paSsgDisplayRecommendCategory.setDispCtgDclsId(ctg.get(j).containsKey("dispCtgDclsId")?ctg.get(j).get("dispCtgDclsId").toString():"");
								paSsgDisplayRecommendCategory.setDispCtgDclsNm(ctg.get(j).containsKey("dispCtgDclsNm")?ctg.get(j).get("dispCtgDclsNm").toString():"");
								paSsgDisplayRecommendCategory.setDispCtgSdclsId(ctg.get(j).containsKey("dispCtgSdclsId")?ctg.get(j).get("dispCtgSdclsId").toString():"");
								paSsgDisplayRecommendCategory.setDispCtgSdclsNm(ctg.get(j).containsKey("dispCtgSdclsNm")?ctg.get(j).get("dispCtgSdclsNm").toString():"");							
								paSsgDisplayRecommendCategory.setInsertId(Constants.PA_SSG_PROC_ID);
								paSsgDisplayRecommendCategory.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								paSsgDisplayRecommendCategoryList.add(paSsgDisplayRecommendCategory);
								
							}
							
						} else {
							paSsgDisplayRecommendCategory = new PaSsgDisplayRecommendCategory();
							
							paSsgDisplayRecommendCategory.setSiteNo("");							
							paSsgDisplayRecommendCategory.setStdCtgDclsId(paGoodsKinds.getPaLmsdKey().toString());							
							paSsgDisplayRecommendCategory.setDispCtgClsNm("");
							paSsgDisplayRecommendCategory.setDispCtgLclsId("");
							paSsgDisplayRecommendCategory.setDispCtgLclsNm("");
							paSsgDisplayRecommendCategory.setDispCtgMclsId("");
							paSsgDisplayRecommendCategory.setDispCtgMclsNm("");
							paSsgDisplayRecommendCategory.setDispCtgSclsId("");
							paSsgDisplayRecommendCategory.setDispCtgSclsNm("");							
							paSsgDisplayRecommendCategory.setDispCtgDclsId("");
							paSsgDisplayRecommendCategory.setDispCtgDclsNm("");
							paSsgDisplayRecommendCategory.setDispCtgSdclsId("");
							paSsgDisplayRecommendCategory.setDispCtgSdclsNm("");							
							paSsgDisplayRecommendCategory.setInsertId(Constants.PA_SSG_PROC_ID);
							paSsgDisplayRecommendCategory.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							
							paSsgDisplayRecommendCategoryList.add(paSsgDisplayRecommendCategory);
						}
						
						rtnMsg = paSsgCommonService.savePaSsgDisplayRecommendCategoryTx(paSsgDisplayRecommendCategoryList);
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code","500");
							apiInfoMap.put("message",rtnMsg);
						} else {
							apiInfoMap.put("code","200");
							apiInfoMap.put("message","OK");
						}
					}
				} catch (Exception e) {
					System.out.println(paGoodsKinds.getPaLmsdKey().toString());
				}
				
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	// 정산 API
	// 1. 테스트 전이라 테스트 할때 VO에서 NULL 값들어가는 에러 잡아야함.
	// 2. proscessimpl에서 DATE 형 변환 하는것도 에러 잡아야함. 
	// 3. 쿼리문도 키값으로 대체할만한 값들을 검사해서 다시 짜야함.
	@RequestMapping(value = "/settlement-goods", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> settlementGoods(HttpServletRequest request,
			@RequestParam(value = "Date" , required = false) String Date,
			@RequestParam(value = "delYn", required=false) String delYn) throws Exception {
		
		String	paCode	= "";
		String critnDt  = "";
		String rtnMsg	= Constants.SAVE_SUCCESS;
		String prg_id 	= "IF_PASSGAPI_00_025";
		ParamMap paramMap	= new ParamMap();
		ParamMap apiInfoMap	= new ParamMap();
		ParamMap apiDataMap	= new ParamMap();
		Map<String, Object> map	  = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		List<HashMap<String, Object>> resultData = new ArrayList<HashMap<String, Object>>();
		
		critnDt   = ComUtil.NVL(Date).length()   == 8 ? Date   : DateUtil.addDay(DateUtil.getCurrentDateAsString(), -1, DateUtil.GENERAL_DATE_FORMAT);
		
		log.info("===== SSG 정산 API Start =====");	
		paramMap.put("delYn", delYn);
		
		try {
			log.info(prg_id + " SSG 정산 API - 01.프로그램 중복 실행 검사 [start]");
			paSsgConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paSsgConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			//운영 중 변경 건이라 나중에 배포 시점에 url 관련 로직 수정하여 재배포 필요...
			String orgUrl = apiInfoMap.getString("url");
			
			int page = 1;
			while(true) {
				String url = orgUrl + "?critnDt=" + critnDt + "&page=" + page;
				apiInfoMap.put("url", url);
				
				paCode = Constants.PA_SSG_BROAD_CODE; //한개로 전부 조회됨.
				apiInfoMap.put("paCode", paCode);
				apiInfoMap.put("paBroad", ConfigUtil.getString("PASSG_API_KEY"));
				
				List<PaSsgSettlement> paSsgSettlementList = new ArrayList<PaSsgSettlement>();
				
				log.info("02.SSG 정산 API 호출");
				map = paSsgConnectUtil.apiGetObjectBySsg(apiInfoMap, apiDataMap);
				
				result = (HashMap<String,Object>)map.get("result");
				
				if(result.get("resultCode").toString().equals("00") && result.containsKey("resultData")) {
					if(result.get("resultData") instanceof Map<?, ?>) {
						resultData.add((HashMap<String, Object>)result.get("resultData"));
					} else {
						resultData = (List<HashMap<String, Object>>)result.get("resultData");
					}
					
					for(int j = 0; j < resultData.size(); j++) {
						PaSsgSettlement settlementVO = new PaSsgSettlement();
						
						settlementVO.setCritnDt(resultData.get(j).get("critnDt").toString());
						settlementVO.setSettlVenId(resultData.get(j).get("settlVenId").toString());
						settlementVO.setSettlVenNm(resultData.get(j).get("settlVenNm").toString());
						settlementVO.setLrnkVenId(resultData.get(j).get("lrnkVenId").toString());
						settlementVO.setLrnkVenNm(resultData.get(j).get("lrnkVenNm").toString());
						settlementVO.setSalestrNo(resultData.get(j).get("salestrNo").toString());
						settlementVO.setSalestrNo2(resultData.get(j).get("salestrNo2").toString());
						settlementVO.setTxnDivCd(resultData.get(j).get("txnDivCd").toString());
						settlementVO.setTxnDivNm(resultData.get(j).get("txnDivNm").toString());
						settlementVO.setOrdNo(resultData.get(j).get("ordNo").toString());
						settlementVO.setOrordNo(resultData.get(j).get("orordNo").toString());
						settlementVO.setOrdItemSeq(Integer.parseInt(resultData.get(j).get("ordItemSeq").toString()));
						settlementVO.setOrordItemSeq(Integer.parseInt(resultData.get(j).get("orordItemSeq").toString()));
						settlementVO.setOrdpeNm(resultData.get(j).get("ordpeNm").toString());
						settlementVO.setShppNo(resultData.get(j).containsKey("shppNo") ? resultData.get(j).get("shppNo").toString() : "확인필요");
						settlementVO.setWblNo(resultData.get(j).get("wblNo").toString());
						settlementVO.setDelicoNm(ComUtil.objToStr(resultData.get(j).get("delicoNm")));
						settlementVO.setItemId(resultData.get(j).get("itemId").toString());
						settlementVO.setItemNm(resultData.get(j).get("itemNm").toString());
						settlementVO.setUitemNm(resultData.get(j).get("uitemNm").toString());
						settlementVO.setSalesQty(Integer.parseInt(resultData.get(j).get("salesQty").toString()));
						settlementVO.setSettlAmt(ComUtil.objToDouble(resultData.get(j).get("settlAmt").toString()));
						settlementVO.setSellUprc(ComUtil.objToDouble(resultData.get(j).get("sellUprc").toString()));
						settlementVO.setTotSellAmt(ComUtil.objToDouble(resultData.get(j).get("totSellAmt").toString()));
						settlementVO.setSplVenBdnDcAmt(ComUtil.objToDouble(resultData.get(j).get("splVenBdnDcAmt").toString()));
						settlementVO.setOwncoBdnDcAmt(ComUtil.objToDouble(resultData.get(j).get("owncoBdnDcAmt").toString()));
						settlementVO.setTotDcAmt(ComUtil.objToDouble(resultData.get(j).get("totDcAmt").toString()));
						settlementVO.setNetAmt(ComUtil.objToDouble(resultData.get(j).get("netAmt").toString()));
						settlementVO.setSellFeeRt(resultData.get(j).containsKey("sellFeeRt") ? ComUtil.objToDouble(resultData.get(j).get("sellFeeRt").toString()) : 0);
						settlementVO.setCtaxAmt(ComUtil.objToDouble(resultData.get(j).get("ctaxAmt").toString()));
						settlementVO.setFrgSellVat(ComUtil.objToDouble(resultData.get(j).get("frgSellVat").toString()));
						settlementVO.setDvShppcstAmt(ComUtil.objToDouble(resultData.get(j).get("dvShppcstAmt").toString()));
						settlementVO.setDvShppcstVat(ComUtil.objToDouble(resultData.get(j).get("dvShppcstVat").toString()));
						settlementVO.setDvCstmrAmt(ComUtil.objToDouble(resultData.get(j).get("dvCstmrAmt").toString()));
						settlementVO.setVenDvShppcst(ComUtil.objToDouble(resultData.get(j).get("venDvShppcst").toString()));
						settlementVO.setVenDvShppcstVat(ComUtil.objToDouble(resultData.get(j).get("venDvShppcstVat").toString()));
						settlementVO.setOwncoDvShppcst(ComUtil.objToDouble(resultData.get(j).get("owncoDvShppcst").toString()));
						settlementVO.setFreeShppCpnVenBdnAmt(ComUtil.objToDouble(resultData.get(j).get("freeShppCpnVenBdnAmt").toString()));
						settlementVO.setFreeShppCpnOwncoBdnAmt(ComUtil.objToDouble(resultData.get(j).get("freeShppCpnVenBdnAmt").toString()));
						settlementVO.setAllnTypeNm(resultData.get(j).containsKey("allnTypeNm") ? resultData.get(j).get("allnTypeNm").toString() : "");
						
						paSsgSettlementList.add(settlementVO);
						
					}
					
					if(paSsgSettlementList.size() > 0 ) {
						rtnMsg = paSsgCommonService.savePaSsgSettlementTx(paSsgSettlementList,paramMap,page);
						
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
				
				if(ComUtil.objToInt(result.get("totalCount").toString()) <= page * 1000) {
					break;
				}
				page++;
			}
		} catch (Exception e) {
			paSsgConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paSsgConnectUtil.closeApi(request, apiInfoMap);
		}	
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
