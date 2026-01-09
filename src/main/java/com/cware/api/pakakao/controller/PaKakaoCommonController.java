package com.cware.api.pakakao.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.Makecomp;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaMaker;
import com.cware.netshopping.domain.model.PaOrigin;
import com.cware.netshopping.pakakao.common.service.PaKakaoCommonService;
import com.cware.netshopping.pakakao.util.PaKakaoConnectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api(value="/pakakao/common", description="카카오 공통")
@Controller("com.cware.api.pakakao.PaKakaoCommonController")
@RequestMapping(value = "/pakakao/common")
public class PaKakaoCommonController extends AbstractController  {
	
	private transient static Logger log = LoggerFactory.getLogger(PaKakaoCommonController.class);
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;

	@Autowired
	private PaKakaoConnectUtil paKakaoConnectUtil;
	
	@Resource(name = "pakakao.common.paKakaoCommonService")
	private PaKakaoCommonService paKakaoCommonService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/category-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> categoryList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_006";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= systemService.getSysdatetimeToString();
		String		paCode		= "B1"; //하드코딩
		List<PaGoodsKinds>	paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		PaGoodsKinds		paGoodsKinds = null;
				
		try {
			log.info(prg_id + " 카카오 카테고리 조회 START");
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			String url = apiInfoMap.get("url").toString();
			
			apiInfoMap.put("paCode", paCode);
			
			//대분류
			apiInfoMap.put("url", url.replace("{categoryId}", ""));
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
			
			if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
				List<Map<String, Object>> categoryLList  = new ArrayList<Map<String,Object>>();
				categoryLList = (List<Map<String, Object>>)(map.get("Data"));
				
				for(int i=0; i<categoryLList.size(); i++) {
					
					if(!(boolean) categoryLList.get(i).get("lastLevel")) {
						
						//중분류
						apiInfoMap.put("url", url.replace("{categoryId}", categoryLList.get(i).get("id").toString()));
						map	= new HashMap<String, Object>();
						map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
						
						if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
							List<Map<String, Object>> categoryMList  = new ArrayList<Map<String,Object>>();
							categoryMList = (List<Map<String, Object>>)(map.get("Data"));
							for(int j=0; j<categoryMList.size(); j++) {								
								
								if(!(boolean) categoryMList.get(j).get("lastLevel")) {
									//소분류
									apiInfoMap.put("url", url.replace("{categoryId}", categoryMList.get(j).get("id").toString()));
									map	= new HashMap<String, Object>();
									map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
									
									if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
										List<Map<String, Object>> categorySList  = new ArrayList<Map<String,Object>>();
										categorySList = (List<Map<String, Object>>)(map.get("Data"));
										for(int k=0; k<categorySList.size(); k++) {
											
											if(!(boolean) categorySList.get(k).get("lastLevel")) {
												//세분류
												apiInfoMap.put("url", url.replace("{categoryId}", categorySList.get(k).get("id").toString()));
												map	= new HashMap<String, Object>();
												map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
												
												if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
													List<Map<String, Object>> categoryDList  = new ArrayList<Map<String,Object>>();
													categoryDList = (List<Map<String, Object>>)(map.get("Data"));
													for(int l=0; l<categoryDList.size(); l++) {
														
														if(!(boolean) categoryDList.get(l).get("lastLevel")) {
															apiInfoMap.put("code", "500");
															apiInfoMap.put("message", "더있으면 안됨......");
														} else {
															paGoodsKinds = new PaGoodsKinds();
															
															paGoodsKinds.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
															paGoodsKinds.setPaLgroup(categoryLList.get(i).get("id").toString());
															paGoodsKinds.setPaLgroupName(categoryLList.get(i).get("name").toString());
															paGoodsKinds.setPaMgroup(categoryMList.get(j).get("id").toString());
															paGoodsKinds.setPaMgroupName(categoryMList.get(j).get("name").toString());
															paGoodsKinds.setPaSgroup(categorySList.get(k).get("id").toString());
															paGoodsKinds.setPaSgroupName(categorySList.get(k).get("name").toString());
															paGoodsKinds.setPaDgroup(categoryDList.get(l).get("id").toString());
															paGoodsKinds.setPaDgroupName(categoryDList.get(l).get("name").toString());
															paGoodsKinds.setPaLmsdKey(categoryDList.get(l).get("id").toString());
															paGoodsKinds.setUseYn("1");
															paGoodsKinds.setInsertId(Constants.PA_KAKAO_PROC_ID);
															paGoodsKinds.setModifyId(Constants.PA_KAKAO_PROC_ID);
															paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
															paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
															if(categoryDList.get(l).get("certKc") != null && "REQUIRED".equals(categoryDList.get(l).get("certKc"))) {
																paGoodsKinds.setCertYn("1");
															}
															paGoodsKindsList.add(paGoodsKinds);
														}
													}										
												} else {
													errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
													apiInfoMap.put("code", "500");
													apiInfoMap.put("message", errorMap.get("errorMsg").toString());
												}												
											} else {
												paGoodsKinds = new PaGoodsKinds();
												
												paGoodsKinds.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
												paGoodsKinds.setPaLgroup(categoryLList.get(i).get("id").toString());
												paGoodsKinds.setPaLgroupName(categoryLList.get(i).get("name").toString());
												paGoodsKinds.setPaMgroup(categoryMList.get(j).get("id").toString());
												paGoodsKinds.setPaMgroupName(categoryMList.get(j).get("name").toString());
												paGoodsKinds.setPaSgroup(categorySList.get(k).get("id").toString());
												paGoodsKinds.setPaSgroupName(categorySList.get(k).get("name").toString());
												paGoodsKinds.setPaLmsdKey(categorySList.get(k).get("id").toString());
												paGoodsKinds.setUseYn("1");
												paGoodsKinds.setInsertId(Constants.PA_KAKAO_PROC_ID);
												paGoodsKinds.setModifyId(Constants.PA_KAKAO_PROC_ID);
												paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
												paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
												if(categorySList.get(k).get("certKc") != null && "REQUIRED".equals(categorySList.get(k).get("certKc"))) {
													paGoodsKinds.setCertYn("1");
												}
												paGoodsKindsList.add(paGoodsKinds);
											}
										}										
									} else {
										errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
										apiInfoMap.put("code", "500");
										apiInfoMap.put("message", errorMap.get("errorMsg").toString());
									}
								} else {
									paGoodsKinds = new PaGoodsKinds();
									
									paGoodsKinds.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
									paGoodsKinds.setPaLgroup(categoryLList.get(i).get("id").toString());
									paGoodsKinds.setPaLgroupName(categoryLList.get(i).get("name").toString());
									paGoodsKinds.setPaMgroup(categoryMList.get(j).get("id").toString());
									paGoodsKinds.setPaMgroupName(categoryMList.get(j).get("name").toString());
									paGoodsKinds.setPaLmsdKey(categoryMList.get(j).get("id").toString());
									paGoodsKinds.setUseYn("1");
									paGoodsKinds.setInsertId(Constants.PA_KAKAO_PROC_ID);
									paGoodsKinds.setModifyId(Constants.PA_KAKAO_PROC_ID);
									paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
									if(categoryMList.get(j).get("certKc") != null && "REQUIRED".equals(categoryMList.get(j).get("certKc"))) {
										paGoodsKinds.setCertYn("1");
									}
									paGoodsKindsList.add(paGoodsKinds);
								}
							}
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							apiInfoMap.put("message", errorMap.get("errorMsg").toString());
						}
					} else {
						paGoodsKinds = new PaGoodsKinds();
						
						paGoodsKinds.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
						paGoodsKinds.setPaLgroup(categoryLList.get(i).get("id").toString());
						paGoodsKinds.setPaLgroupName(categoryLList.get(i).get("name").toString());
						paGoodsKinds.setPaLmsdKey(categoryLList.get(i).get("id").toString());
						paGoodsKinds.setUseYn("1");
						paGoodsKinds.setInsertId(Constants.PA_KAKAO_PROC_ID);
						paGoodsKinds.setModifyId(Constants.PA_KAKAO_PROC_ID);
						paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						if(categoryLList.get(i).get("certKc") != null && "REQUIRED".equals(categoryLList.get(i).get("certKc"))) {
							paGoodsKinds.setCertYn("1");
						}
						paGoodsKindsList.add(paGoodsKinds);
					}
				}
				
			} else {
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", errorMap.get("errorMsg").toString());
			}
			
			paKakaoCommonService.savePaGoodsKindsTx(paGoodsKindsList);
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
			log.info(prg_id + " 카카오 카테고리 조회 END");
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/origin-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> originList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_008";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= systemService.getSysdatetimeToString();
		String		paCode		= "B1"; //하드코딩
		List<PaOrigin> paOriginList = new ArrayList<PaOrigin>();
		PaOrigin	paOrigin = null;
				
		try {
			log.info(prg_id + "카카오 원산지 조회 START");
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			
			map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
			
			if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {
				
				List<Map<String, Object>> originList  = new ArrayList<Map<String,Object>>();
				originList = (List<Map<String, Object>>)(map.get("Data"));
				
				for(int i=0; i<originList.size(); i++) {
					
					paOrigin = new PaOrigin();
					
					paOrigin.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
					paOrigin.setPaOriginCode(originList.get(i).get("code").toString());
					paOrigin.setPaOriginName(originList.get(i).get("name").toString());					
					paOrigin.setRemark1(originList.get(i).get("level").toString());
					paOrigin.setInsertId(Constants.PA_KAKAO_PROC_ID);
					paOrigin.setModifyId(Constants.PA_KAKAO_PROC_ID);
					paOrigin.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paOrigin.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					paOriginList.add(paOrigin);
				}
				
			} else {
				errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
				apiInfoMap.put("code", "500");
				apiInfoMap.put("message", errorMap.get("errorMsg").toString());
			}
			
			paKakaoCommonService.savePaOrigindTx(paOriginList);
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
			log.info(prg_id + " 카카오 원산지 조회 END");
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/brand-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> brandList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_010";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= systemService.getSysdatetimeToString();
		String		paCode		= "B1"; //하드코딩
		List<PaBrand> paBrandList = new ArrayList<PaBrand>();
		PaBrand	paBrand = null;
				
		try {
			log.info(prg_id + "카카오 브랜드 조회 START");
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			String url = apiInfoMap.get("url").toString();
			
			log.info(prg_id + "SK스토아 브랜드 리스트 조회");
			List<Brand> brandList = paKakaoCommonService.selectBrandList();
			
			for(int i=0; i<brandList.size(); i++) {
				
				apiInfoMap.put("url", url.replace("{brand}", brandList.get(i).getBrandName()));
				map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
				
				if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {					
					List<Map<String, Object>> kakaoBrandList  = new ArrayList<Map<String,Object>>();
					kakaoBrandList = (List<Map<String, Object>>)(map.get("contents"));
					
					for(int j=0; j<kakaoBrandList.size(); j++) {
						
						paBrand = new PaBrand();
						
						paBrand.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
						paBrand.setPaBrandNo(kakaoBrandList.get(j).get("brandId").toString());
						paBrand.setPaBrandName(kakaoBrandList.get(j).get("brandName").toString());
						paBrand.setUseYn("1");
						paBrand.setInsertId(Constants.PA_KAKAO_PROC_ID);
						paBrand.setModifyId(Constants.PA_KAKAO_PROC_ID);
						paBrand.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paBrand.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paBrandList.add(paBrand);
					}					
					
				} else {
					errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", errorMap.get("errorMsg").toString());
				}
			}
			
			paKakaoCommonService.savePaBrandTx(paBrandList);
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
			log.info(prg_id + " 카카오 브랜드 조회 END");
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/maker-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> makerList(HttpServletRequest request) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_011";
		Map<String, Object> map	= new HashMap<String, Object>();
		
		String		dateTime	= systemService.getSysdatetimeToString();
		String		paCode		= "B1"; //하드코딩
		List<PaMaker> paMakerList = new ArrayList<PaMaker>();
		PaMaker	paMaker = null;
				
		try {
			log.info(prg_id + "카카오 제조사 조회 START");
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			apiInfoMap.put("paCode", paCode);
			String url = apiInfoMap.get("url").toString();
			
			log.info(prg_id + "SK스토아 제조사 리스트 조회");
			List<Makecomp> makerList = paKakaoCommonService.selectMakerList();
			
			for(int i=0; i<makerList.size(); i++) {
				
				apiInfoMap.put("url", url.replace("{manufacturer}", makerList.get(i).getMakecoName()));
				map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, null);
				
				if("200".equals(ComUtil.objToStr(map.get("statusCode")))) {					
					List<Map<String, Object>> kakaoMakerList  = new ArrayList<Map<String,Object>>();
					kakaoMakerList = (List<Map<String, Object>>)(map.get("contents"));
					
					for(int j=0; j<kakaoMakerList.size(); j++) {
						
						paMaker = new PaMaker();
						
						paMaker.setPaGroupCode(Constants.PA_KAKAO_GROUP_CODE);
						paMaker.setPaMakerNo(kakaoMakerList.get(j).get("manufacturerId").toString());
						paMaker.setPaMakerName(kakaoMakerList.get(j).get("manufactureName").toString());
						paMaker.setUseYn("1");
						paMaker.setInsertId(Constants.PA_KAKAO_PROC_ID);
						paMaker.setModifyId(Constants.PA_KAKAO_PROC_ID);
						paMaker.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						paMaker.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
						
						paMakerList.add(paMaker);
					}					
					
				} else {
					errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
					apiInfoMap.put("code", "500");
					apiInfoMap.put("message", errorMap.get("errorMsg").toString());
				}
			}
			
			paKakaoCommonService.savePaMakerTx(paMakerList);
			
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
			log.info(prg_id + " 카카오 제조사 조회 END");
		}
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> kakaoEntpslipInsert(HttpServletRequest request,
			@ApiParam(name="goodsCode",	value="상품",	defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= null;
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_002";
		Map<String, Object> map	= new HashMap<String, Object>();
		ParamMap paramMap = new ParamMap();
		List<PaEntpSlip> entpSlipList = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String errMsg = "";
		
		String		dateTime	= systemService.getSysdatetimeToString();
				
		log.info(prg_id + "카카오 판매자 주소록 등록 START");
		try {
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("goodsCode", goodsCode );
			
			entpSlipList = paKakaoCommonService.selectPaKakaoEntpSlip(paramMap);
			
			for(int i=0; i<entpSlipList.size(); i++) {
				try {
					apiInfoMap.put("paCode", entpSlipList.get(i).getPaCode());
					
					apiDataMap = new ParamMap();
					
					apiDataMap.put("name", entpSlipList.get(i).getPaCode() + entpSlipList.get(i).getEntpCode() + entpSlipList.get(i).getEntpManSeq());
					apiDataMap.put("postNo", entpSlipList.get(i).getPostNo());
					apiDataMap.put("contact", entpSlipList.get(i).getEntpManDdd() + "-" + entpSlipList.get(i).getEntpManTel1() + "-" + entpSlipList.get(i).getEntpManTel2());
					apiDataMap.put("addressPost", entpSlipList.get(i).getAddress1());
					apiDataMap.put("addressDetail", entpSlipList.get(i).getAddress2());
					apiDataMap.put("returnFeeAmount", 0);
					apiDataMap.put("exchangeFeeAmount", 0);
					apiDataMap.put("basicReturnAddress", false);
					apiDataMap.put("basicSenderAddress", false);
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);

					if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage"))) ) {
						
						entpSlipList.get(i).setPaAddrSeq(map.get("id").toString());
						entpSlipList.get(i).setTransTargetYn("0");
						entpSlipList.get(i).setInsertId(Constants.PA_KAKAO_PROC_ID);
						entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
						entpSlipList.get(i).setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						rtnMsg = paKakaoCommonService.savePaEntpSlipTx(entpSlipList.get(i));
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code","500");
							errMsg += rtnMsg;
						}						
						
					} else {
						
						if (ComUtil.objToStr(map.get("validation")).contains("contact의 입력 가능한 포멧")) { 
							//업체 전화번호 연동 실패 시 휴대폰번호로 한번 더 연동.
							apiDataMap.put("contact", entpSlipList.get(i).getEntpManHp1() + "-" + entpSlipList.get(i).getEntpManHp2() + "-" + entpSlipList.get(i).getEntpManHp3());
							map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
							
							if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage"))) ) {
								
								entpSlipList.get(i).setPaAddrSeq(map.get("id").toString());
								entpSlipList.get(i).setTransTargetYn("0");
								entpSlipList.get(i).setInsertId(Constants.PA_KAKAO_PROC_ID);
								entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
								entpSlipList.get(i).setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paKakaoCommonService.savePaEntpSlipTx(entpSlipList.get(i));
								
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									apiInfoMap.put("code","500");
									errMsg += rtnMsg;
								}	
							} else {
								
								apiInfoMap.put("code", "500");
								errMsg += map.get("validation").toString();
							}
							
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							errMsg += errorMap.get("errorMsg").toString();
						}
					}
				} catch(Exception e){ 
					errMsg += e.getMessage();
					apiInfoMap.put("code","500");
				}
			}
			apiInfoMap.put("message",errMsg);
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info(prg_id + " 카카오 판매자 주소록 등록 END");
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/entpslip-modify", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> kakaoEntpslipModify(HttpServletRequest request,
			@ApiParam(name="goodsCode",	value="상품",	defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= null;
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_003";
		Map<String, Object> map	= new HashMap<String, Object>();
		ParamMap paramMap = new ParamMap();
		List<PaEntpSlip> entpSlipList = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String errMsg = "";
		
		String		dateTime	= systemService.getSysdatetimeToString();
				
		log.info(prg_id + "카카오 판매자 주소록 수정 START");
		try {
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("goodsCode", goodsCode );
						
			entpSlipList = paKakaoCommonService.selectPaKakaoEntpSlipUpdate(paramMap);
			
			for(int i=0; i<entpSlipList.size(); i++) {
				try {
					apiInfoMap.put("paCode", entpSlipList.get(i).getPaCode());
					
					apiDataMap = new ParamMap();
					apiDataMap.put("id", entpSlipList.get(i).getPaAddrSeq());
					apiDataMap.put("name", entpSlipList.get(i).getPaCode() + entpSlipList.get(i).getEntpCode() + entpSlipList.get(i).getEntpManSeq());
					apiDataMap.put("postNo", entpSlipList.get(i).getPostNo());
					apiDataMap.put("contact", entpSlipList.get(i).getEntpManDdd() + "-" + entpSlipList.get(i).getEntpManTel1() + "-" + entpSlipList.get(i).getEntpManTel2());
					apiDataMap.put("addressPost", entpSlipList.get(i).getAddress1());
					apiDataMap.put("addressDetail", entpSlipList.get(i).getAddress2());
					apiDataMap.put("returnFeeAmount", 0);
					apiDataMap.put("exchangeFeeAmount", 0);
					apiDataMap.put("basicReturnAddress", false);
					apiDataMap.put("basicSenderAddress", false);
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);

					if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage")))) {
						
						entpSlipList.get(i).setTransTargetYn("0");
						entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
						entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						rtnMsg = paKakaoCommonService.updatePaEntpSlipTx(entpSlipList.get(i));
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code","500");
							errMsg += rtnMsg;
						}						
						
					} else {
						
						if (ComUtil.objToStr(map.get("validation")).contains("contact의 입력 가능한 포멧")) {  
							//업체 전화번호 연동 실패 시 휴대폰번호로 한번 더 연동.
							apiDataMap.put("contact", entpSlipList.get(i).getEntpManHp1() + "-" + entpSlipList.get(i).getEntpManHp2() + "-" + entpSlipList.get(i).getEntpManHp3());
							map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);

							if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage"))) ) {
								
								entpSlipList.get(i).setTransTargetYn("0");
								entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
								entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paKakaoCommonService.updatePaEntpSlipTx(entpSlipList.get(i));
								
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									apiInfoMap.put("code","500");
									errMsg += rtnMsg;
								}						
								
							} else {
								apiInfoMap.put("code", "500");
								errMsg += map.get("validation").toString();
							}
							
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							errMsg += errorMap.get("errorMsg").toString();							
						}
					}
				} catch(Exception e){ 
					errMsg += e.getMessage();
					apiInfoMap.put("code","500");
				}
			}
			apiInfoMap.put("message",errMsg);
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
			
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info(prg_id + " 카카오 판매자 주소록 수정 END");
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/modify_entpslip-insert", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> kakaoModifyEntpslipInsert(HttpServletRequest request,
			@ApiParam(name="goodsCode",	value="상품",	defaultValue = "") @RequestParam(value="goodsCode", required=false) String goodsCode) throws Exception {
		
		ParamMap	apiInfoMap	= new ParamMap();
		ParamMap	apiDataMap	= null;
		ParamMap 	errorMap	= null;
		String		prg_id 		= "IF_PAKAKAOAPI_00_012";
		Map<String, Object> map	= new HashMap<String, Object>();
		ParamMap paramMap = new ParamMap();
		List<PaEntpSlip> entpSlipList = null;
		String rtnMsg = Constants.SAVE_SUCCESS;
		String errMsg = "";
		
		String		dateTime	= systemService.getSysdatetimeToString();
				
		log.info(prg_id + "카카오 판매자 주소록 등록(상품수정용) START");
		try {
			
			paKakaoConnectUtil.getApiInfo(prg_id, apiInfoMap);
			paKakaoConnectUtil.checkDuplication(prg_id, apiInfoMap);
			
			paramMap.put("goodsCode", goodsCode );
			
			entpSlipList = paKakaoCommonService.selectPaKakaoModifyEntpSlip(paramMap);
			
			for(int i=0; i<entpSlipList.size(); i++) {
				try {
					apiInfoMap.put("paCode", entpSlipList.get(i).getPaCode());
					
					apiDataMap = new ParamMap();
					
					apiDataMap.put("name", entpSlipList.get(i).getPaCode() + entpSlipList.get(i).getEntpCode() + entpSlipList.get(i).getEntpManSeq());
					apiDataMap.put("postNo", entpSlipList.get(i).getPostNo());
					apiDataMap.put("contact", entpSlipList.get(i).getEntpManDdd() + "-" + entpSlipList.get(i).getEntpManTel1() + "-" + entpSlipList.get(i).getEntpManTel2());
					apiDataMap.put("addressPost", entpSlipList.get(i).getAddress1());
					apiDataMap.put("addressDetail", entpSlipList.get(i).getAddress2());
					apiDataMap.put("returnFeeAmount", 0);
					apiDataMap.put("exchangeFeeAmount", 0);
					apiDataMap.put("basicReturnAddress", false);
					apiDataMap.put("basicSenderAddress", false);
					
					map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);

					if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage"))) ) {
						
						entpSlipList.get(i).setPaAddrSeq(map.get("id").toString());
						entpSlipList.get(i).setTransTargetYn("0");
						entpSlipList.get(i).setInsertId(Constants.PA_KAKAO_PROC_ID);
						entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
						entpSlipList.get(i).setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
						
						rtnMsg = paKakaoCommonService.savePaEntpSlipTx(entpSlipList.get(i));
						
						if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
							apiInfoMap.put("code","500");
							errMsg += rtnMsg;
						}						
						
					} else {
						
						if (ComUtil.objToStr(map.get("validation")).contains("contact의 입력 가능한 포멧")) { 
							//업체 전화번호 연동 실패 시 휴대폰번호로 한번 더 연동.
							apiDataMap.put("contact", entpSlipList.get(i).getEntpManHp1() + "-" + entpSlipList.get(i).getEntpManHp2() + "-" + entpSlipList.get(i).getEntpManHp3());
							map = paKakaoConnectUtil.apiGetObjectByKakao(apiInfoMap, apiDataMap);
							
							if("200".equals(ComUtil.objToStr(map.get("statusCode"))) && "".equals(ComUtil.objToStr(map.get("errorMessage"))) ) {
								
								entpSlipList.get(i).setPaAddrSeq(map.get("id").toString());
								entpSlipList.get(i).setTransTargetYn("0");
								entpSlipList.get(i).setInsertId(Constants.PA_KAKAO_PROC_ID);
								entpSlipList.get(i).setModifyId(Constants.PA_KAKAO_PROC_ID);
								entpSlipList.get(i).setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								entpSlipList.get(i).setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								
								rtnMsg = paKakaoCommonService.savePaEntpSlipTx(entpSlipList.get(i));
								
								if(!rtnMsg.equals(Constants.SAVE_SUCCESS)){
									apiInfoMap.put("code","500");
									errMsg += rtnMsg;
								}	
							} else {
								
								apiInfoMap.put("code", "500");
								errMsg += map.get("validation").toString();
							}
							
						} else {
							errorMap = paKakaoConnectUtil.parseKakaoErrorResponse(map);
							apiInfoMap.put("code", "500");
							errMsg += errorMap.get("errorMsg").toString();
						}
					}
				} catch(Exception e){ 
					errMsg += e.getMessage();
					apiInfoMap.put("code","500");
				}
			}
			apiInfoMap.put("message",errMsg);
		} catch (Exception e) {
			paKakaoConnectUtil.checkException(apiInfoMap, e);
		}finally {
			paKakaoConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info(prg_id + " 카카오 판매자 주소록 등록(상품수정용) END");
		
		return new ResponseEntity<>(new ResponseMsg(apiInfoMap.getString("code"), apiInfoMap.getString("message")), HttpStatus.OK);
	}
}
