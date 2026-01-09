package com.cware.netshopping.paqeen.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pacommon.v2.repository.PaCommonMapper;
import com.cware.netshopping.paqeen.domain.Announcement;
import com.cware.netshopping.paqeen.domain.AnnouncementList;
import com.cware.netshopping.paqeen.domain.BrandVO;
import com.cware.netshopping.paqeen.domain.CategoryOfferList;
import com.cware.netshopping.paqeen.repository.PaQeenCommonMapper;
import com.cware.netshopping.paqeen.util.PaQeenApiRequest;
import com.cware.netshopping.paqeen.util.PaQeenComUtil;
import com.cware.netshopping.paqeen.util.PaQeenConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaQeenCommonService {
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaQeenConnectUtil paQeenConnectUtil;
	
	@Autowired
	PaQeenApiRequest paQeenApiRequest;
	
	@Autowired
	PaCommonMapper paCommonMapper;
	
	@Autowired
	PaQeenCommonMapper paQeenCommonMapper;
	
	@Autowired
	PaQeenResultService paQeenResultService;
	// 퀸잇 디폴트 타겟 코드
	String defaultCode = "999999";
		
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ResponseMsg registerBrand(String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAQEENAPI_00_006");
		serviceLog.setServiceNote("[API]퀸잇-브랜드 조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 조회
		ResponseMsg result = callRegisterBrand(paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg registerCategory(String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAQEENAPI_00_007");
		serviceLog.setServiceNote("[API]퀸잇-카테고리 조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 조회
		ResponseMsg result = callRegisterCategory(paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
public ResponseMsg registerOffer(String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAQEENAPI_00_004");
		serviceLog.setServiceNote("[API]퀸잇-정보고시 조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.QEEN.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 조회
		ResponseMsg result = callRegisterOffer(paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

public ResponseMsg registerCategoryOffer(String paCode, String procId, long transBatchNo) {
	
	// 서비스로그 생성
	PaTransService serviceLog = new PaTransService();
	serviceLog.setTransCode(defaultCode);
	serviceLog.setTransType(TransType.SHIPCOST.name());
	serviceLog.setServiceName("IF_PAQEENAPI_00_010");
	serviceLog.setServiceNote("[API]퀸잇-카테고리별 정보고시 조회");
	serviceLog.setTransBatchNo(transBatchNo);
	serviceLog.setPaGroupCode(PaGroup.QEEN.code());
	serviceLog.setPaCode(paCode);
	serviceLog.setProcessId(procId);
	transLogService.logTransServiceStart(serviceLog);

	// 조회
	ResponseMsg result = callRegisterCategoryOffer(paCode, procId, serviceLog);

	serviceLog.setResultCode(result.getCode());
	serviceLog.setResultMsg(result.getMessage());
	transLogService.logTransServiceEnd(serviceLog);

	return result;
}
	
	@SuppressWarnings("unchecked")
	private ResponseMsg callRegisterBrand(String paCode, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		PaBrand paBrand = new PaBrand();
		BrandVO brand = new BrandVO();
		String dateTime = "";
		try {
			log.info("===== 퀸잇 브랜드조회서비스 Start -=====");

			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", "");
			
			Map<String, Object> productMap = new HashMap<String, Object>();	
			productMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			dateTime = systemService.getSysdatetimeToString();
			result.setCode("200");
			result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			List<Map<String, Object>> brandList = (List<Map<String, Object>>) productMap.get("Data");
			for (Map<String, Object> brandInfoMap : brandList) {
				paBrand = new PaBrand();
				brand = (BrandVO) PaQeenComUtil.map2VO((Map<String, Object>) brandInfoMap.get("brand"), BrandVO.class);
				paBrand.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
				paBrand.setPaBrandNo(brandInfoMap.get("code").toString());
				if(paQeenCommonMapper.selectPaBrand(paBrand) > 0) {
					continue;
				}
				paBrand.setPaBrandName(brand.getName());
				paBrand.setUseYn("Y");
				paBrand.setPaCode(paCode);
				paBrand.setInsertId(Constants.PA_QEEN_PROC_ID);
				paBrand.setModifyId(Constants.PA_QEEN_PROC_ID);
				paBrand.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
				paBrand.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
				
				paQeenCommonMapper.insertPaBrand(paBrand);
			}
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 퀸잇 브랜드조회서비스 End - =====");
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private ResponseMsg callRegisterCategory(String paCode, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		PaGoodsKinds paGoodsKinds= new PaGoodsKinds();
		List<PaGoodsKinds>paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		String dateTime = "";
		try {
			log.info("===== 퀸잇 카테고리조회서비스 Start -=====");

			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", "");
			
			Map<String, Object> productMap = new HashMap<String, Object>();	
			productMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			dateTime = systemService.getSysdatetimeToString();
			result.setCode("200");
			result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			//paCommonMapper.deletePaGoodsKindsMoment("15");
			String note = "";
			List<Map<String, Object>> categoryLgroupList = (List<Map<String, Object>>) productMap.get("Data");
			for (Map<String, Object> categoryLgroupMap : categoryLgroupList) {
				
				
				if(categoryLgroupMap.get("subCategories") != null) {
					List<Map<String, Object>> categoryMgroupList = (List<Map<String, Object>>) categoryLgroupMap.get("subCategories") ;
						for (Map<String, Object> categoryMgroupMap : categoryMgroupList) {
							if(categoryMgroupMap.get("subCategories") != null) {
								List<Map<String, Object>> categorySgroupList = (List<Map<String, Object>>) categoryMgroupMap.get("subCategories") ;
									for (Map<String, Object> categorySgroupMap : categorySgroupList) {
										if(categorySgroupMap.get("subCategories") != null) {
											List<Map<String, Object>> categoryDgroupList = (List<Map<String, Object>>) categorySgroupMap.get("subCategories") ;
												for (Map<String, Object> categoryDgroupMap : categoryDgroupList) {
													paGoodsKinds= new PaGoodsKinds();
													paGoodsKinds.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
													paGoodsKinds.setPaLgroup(categoryLgroupMap.get("id").toString());
													note = (!"".equals(categoryLgroupMap.get("note").toString()) &&
															!categoryLgroupMap.get("title").toString().equals(categoryLgroupMap.get("note").toString())) ? "("+ categoryLgroupMap.get("note").toString()+")" : "";
													paGoodsKinds.setPaLgroupName(categoryLgroupMap.get("title").toString() + note );
													
													paGoodsKinds.setPaMgroup(categoryMgroupMap.get("id").toString());
													note = (!"".equals(categoryMgroupMap.get("note").toString()) &&
															!categoryMgroupMap.get("title").toString().equals(categoryMgroupMap.get("note").toString())) ? "("+ categoryMgroupMap.get("note").toString()+")" : "";
													paGoodsKinds.setPaMgroupName(categoryMgroupMap.get("title").toString() + note);
													
													paGoodsKinds.setPaSgroup(categorySgroupMap.get("id").toString());
													note = (!"".equals(categorySgroupMap.get("note").toString()) &&
															!categorySgroupMap.get("title").toString().equals(categorySgroupMap.get("note").toString())) ? "("+ categorySgroupMap.get("note").toString()+")" : "";
													paGoodsKinds.setPaSgroupName(categorySgroupMap.get("title").toString() + note);
													
													paGoodsKinds.setPaDgroup(categoryDgroupMap.get("id").toString());
													note = (!"".equals(categoryDgroupMap.get("note").toString()) &&
															!categoryDgroupMap.get("title").toString().equals(categoryDgroupMap.get("note").toString())) ? "("+ categoryDgroupMap.get("note").toString()+")" : "";
													paGoodsKinds.setPaDgroupName(categoryDgroupMap.get("title").toString() + note);
													
													paGoodsKinds.setPaLmsdKey(categoryDgroupMap.get("id").toString());
													paGoodsKinds.setUseYn("Y");
													paGoodsKinds.setInsertId(Constants.PA_QEEN_PROC_ID);
													paGoodsKinds.setModifyId(Constants.PA_QEEN_PROC_ID);
													paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
													paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
													
													paGoodsKindsList.add(paGoodsKinds);
												}
										} else {
											paGoodsKinds= new PaGoodsKinds();
											paGoodsKinds.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);paGoodsKinds.setPaLgroup(categoryLgroupMap.get("id").toString());
											note = (!"".equals(categoryLgroupMap.get("note").toString()) &&
													!categoryLgroupMap.get("title").toString().equals(categoryLgroupMap.get("note").toString())) ? "("+ categoryLgroupMap.get("note").toString()+")" : "";
											paGoodsKinds.setPaLgroupName(categoryLgroupMap.get("title").toString() + note );
											
											paGoodsKinds.setPaMgroup(categoryMgroupMap.get("id").toString());
											note = (!"".equals(categoryMgroupMap.get("note").toString()) &&
													!categoryMgroupMap.get("title").toString().equals(categoryMgroupMap.get("note").toString())) ? "("+ categoryMgroupMap.get("note").toString()+")" : "";
											paGoodsKinds.setPaMgroupName(categoryMgroupMap.get("title").toString() + note);
											
											paGoodsKinds.setPaSgroup(categorySgroupMap.get("id").toString());
											note = (!"".equals(categorySgroupMap.get("note").toString()) &&
													!categorySgroupMap.get("title").toString().equals(categorySgroupMap.get("note").toString())) ? "("+ categorySgroupMap.get("note").toString()+")" : "";
											paGoodsKinds.setPaSgroupName(categorySgroupMap.get("title").toString() + note);
											
											paGoodsKinds.setPaLmsdKey(categorySgroupMap.get("id").toString());
											paGoodsKinds.setUseYn("Y");
											paGoodsKinds.setInsertId(Constants.PA_QEEN_PROC_ID);
											paGoodsKinds.setModifyId(Constants.PA_QEEN_PROC_ID);
											paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
											paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
											
											paGoodsKindsList.add(paGoodsKinds);
											
										}
									}

							}else {
								paGoodsKinds= new PaGoodsKinds();
								paGoodsKinds.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
								paGoodsKinds.setPaLgroup(categoryLgroupMap.get("id").toString());
								note = (!"".equals(categoryLgroupMap.get("note").toString()) &&
										!categoryLgroupMap.get("title").toString().equals(categoryLgroupMap.get("note").toString())) ? "("+ categoryLgroupMap.get("note").toString()+")" : "";
								paGoodsKinds.setPaLgroupName(categoryLgroupMap.get("title").toString() + note );
								
								paGoodsKinds.setPaMgroup(categoryMgroupMap.get("id").toString());
								note = (!"".equals(categoryMgroupMap.get("note").toString()) &&
										!categoryMgroupMap.get("title").toString().equals(categoryMgroupMap.get("note").toString())) ? "("+ categoryMgroupMap.get("note").toString()+")" : "";
								paGoodsKinds.setPaMgroupName(categoryMgroupMap.get("title").toString() + note);
								
								paGoodsKinds.setPaLmsdKey(categoryMgroupMap.get("id").toString());
								paGoodsKinds.setUseYn("Y");
								paGoodsKinds.setInsertId(Constants.PA_QEEN_PROC_ID);
								paGoodsKinds.setModifyId(Constants.PA_QEEN_PROC_ID);
								paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
								paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
								
								paGoodsKindsList.add(paGoodsKinds);
							}
							
						}
				}else {
					paGoodsKinds= new PaGoodsKinds();
					paGoodsKinds.setPaGroupCode(Constants.PA_QEEN_GROUP_CODE);
					paGoodsKinds.setPaLgroup(categoryLgroupMap.get("id").toString());
					note = (!"".equals(categoryLgroupMap.get("note").toString()) &&
							!categoryLgroupMap.get("title").toString().equals(categoryLgroupMap.get("note").toString())) ? "("+ categoryLgroupMap.get("note").toString()+")" : "";
					paGoodsKinds.setPaLgroupName(categoryLgroupMap.get("title").toString() + note );
					
					paGoodsKinds.setPaLmsdKey(categoryLgroupMap.get("id").toString());
					paGoodsKinds.setUseYn("Y");
					paGoodsKinds.setInsertId(Constants.PA_QEEN_PROC_ID);
					paGoodsKinds.setModifyId(Constants.PA_QEEN_PROC_ID);
					paGoodsKinds.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
					paGoodsKinds.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));	
					
					paGoodsKindsList.add(paGoodsKinds);
					
				}
				
			}
			
			paQeenResultService.savePaGoodsKinds(paGoodsKindsList);
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 퀸잇 카테고리조회서비스 End - =====");
		}

		return result;
	}
	
	private ResponseMsg callRegisterOffer(String paCode, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		List<PaOfferCode> paOfferCodeList = new ArrayList<PaOfferCode>();
		PaOfferCode paOfferCode = null;
		String paOfferTypeName = "";
		try {
			log.info("===== 퀸잇 정보고시조회서비스 Start -=====");

			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", "");
			// VO 선언
			AnnouncementList announcementList = new AnnouncementList();
			Map<String, Object> announcementListMap = new HashMap<String, Object>();
			announcementListMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			result.setCode("200");
			result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			Timestamp date = systemService.getSysdate();
			
			
			
			ObjectMapper objectMapper = new ObjectMapper();
			announcementList = objectMapper.convertValue(announcementListMap, AnnouncementList.class);
			
			for (Announcement announcement : announcementList.getAnnouncements()) {
				for(String attribute:announcement.getAttributes()) {
					int sortSeqCount = 0;
					sortSeqCount++;
					paOfferCode = new PaOfferCode();
					paOfferCode.setPaGroupCode(PaGroup.QEEN.code());
					paOfferCode.setPaOfferType(announcement.getAnnouncementType());
					switch (announcement.getAnnouncementType()) {
						case "APPAREL" :
							paOfferTypeName = "의류";
							break;
						case "BEAUTY" :
							paOfferTypeName = "뷰티";
							break;
						case "HEALTH_FUNCTIONAL_FOOD" :
							paOfferTypeName = "건강기능식품";
							break;
						case "FOOD" :
							paOfferTypeName = "식품";
							break;
						case "FURNITURE" :
							paOfferTypeName = "가구";
							break;
						case "KITCHEN" :
							paOfferTypeName = "주방용품";
							break;
						case "HOUSEHOLD_GOODS" :
							paOfferTypeName = "생활용품";
							break;
						default:
							paOfferTypeName = announcement.getAnnouncementType();
							break;
					}
					paOfferCode.setPaOfferTypeName(paOfferTypeName);
					paOfferCode.setRequiredYn("1");
					paOfferCode.setUseYn("1");
					paOfferCode.setUnitRequiredYn("0");
					paOfferCode.setInsertDate(date);
					paOfferCode.setInsertId(procId);
					paOfferCode.setModifyDate(date);
					paOfferCode.setModifyId(procId);
					
					paOfferCode.setPaOfferCode(attribute);
					paOfferCode.setPaOfferCodeName(attribute);
					paOfferCode.setSortSeq(ComUtil.objToStr(sortSeqCount));
					paOfferCodeList.add(paOfferCode);
				}
				
				
			}
			paQeenResultService.savePaOfferCode(paOfferCodeList);
			
			serviceLog.setResultCode("200");
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 퀸잇 정보고시조회서비스 End - =====");
		}

		return result;
	}

	private ResponseMsg callRegisterCategoryOffer(String paCode, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 퀸잇 카테고리별정보고시조회서비스 Start -=====");

			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.QEEN.code());
			// Body 세팅
			ParamMap apiRequestObject = new ParamMap();
			apiRequestObject.put("body", "");
			// VO 선언
			CategoryOfferList categoryOfferList = new CategoryOfferList();
			Map<String, Object> categoryOfferListMap = new HashMap<String, Object>();
			categoryOfferListMap = paQeenConnectUtil.callPaQeenAPI(paramMap, apiRequestObject, serviceLog, null);
			result.setCode("200");
			result.setMessage(PaQeenApiRequest.API_SUCCESS_CODE);
			
			ObjectMapper objectMapper = new ObjectMapper();
			categoryOfferList = objectMapper.convertValue(categoryOfferListMap, CategoryOfferList.class);
			
			paQeenResultService.saveQeenCategoryOffer(categoryOfferList);
			
			serviceLog.setResultCode("200");
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 퀸잇 카테고리별정보고시조회서비스 End - =====");
		}

		return result;
	}
	
}
