package com.cware.netshopping.panaver.v3.service;

import com.cware.netshopping.panaver.common.service.PaNaverCommonService;
import com.cware.netshopping.panaver.v3.domain.BrandsList;
import com.cware.netshopping.panaver.v3.domain.Categories;
import com.cware.netshopping.panaver.v3.domain.CategoriesList;
import com.cware.netshopping.panaver.v3.domain.CertificationInfos;
import com.cware.netshopping.panaver.v3.domain.ExceptionalCategoryType;
import com.cware.netshopping.panaver.v3.domain.KindType;
import com.cware.netshopping.panaver.v3.domain.Models;
import com.cware.netshopping.panaver.v3.domain.ModelsList;
import com.cware.netshopping.panaver.v3.domain.Origin;
import com.cware.netshopping.panaver.v3.domain.OriginAreaCodeNames;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ApiRequest;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaNaverV3CommonService {
		
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Autowired
	private PaNaverCommonService paNaverCommonService;
	
	@Autowired
	private SystemService systemService;

	// 네이버V3 디폴트 타겟 코드
	String defaultCode = "999999";
	// Pa_code
	String paCode = PaCode.NAVER.code();
	// Pa_group_code
	String paGroupCode = PaGroup.NAVER.code();

	
	/**
	 * 원산지 코드 정보 전체 조회
	 * 
	 * @param procId
	 * @return Origin
	 */
	public Origin getOrigin(String procId) {
		
		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_001");
		serviceLog.setServiceNote("[V3]네이버 공통-원산지 전체조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Origin origin = new Origin();
		
		Map<String, Object> originListMap	= new HashMap<String, Object>() ;
		List<PaNaverOrigin> paNaverOriginList = new ArrayList<PaNaverOrigin>();
		PaNaverOrigin paNaverOrigin = null;
				
		try {
			// 원산지 조회 통신
			originListMap= paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			origin = objectMapper.convertValue(originListMap, Origin.class);	
			
			Timestamp date = systemService.getSysdate();
			
			for (OriginAreaCodeNames originArea : origin.getOriginAreaCodeNames()) {
				paNaverOrigin = new PaNaverOrigin();
				paNaverOrigin.setOrignCode(originArea.getCode());
				paNaverOrigin.setOrignName(originArea.getName());
				paNaverOrigin.setInsertId(procId);
				paNaverOrigin.setInsertDate(date);
				
				paNaverOriginList.add(paNaverOrigin);
			}
			paNaverCommonService.saveMappingPaNaverOriginTx(paNaverOriginList);
					
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return origin;
	}
	
	
	/**
	 * 전체 카테고리 조회
	 * 
	 * @param procId
	 * @return CategoriesList
	 */
	public CategoriesList getCategories(String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_002");
		serviceLog.setServiceNote("[V3]네이버 공통-전체 카테고리 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		CategoriesList categoriesList = new CategoriesList();
		
		Map<String, Object> categoriesListMap	= new HashMap<String, Object>() ;
		List<Panavergoodskinds> paNaverGoodsKindsList = new ArrayList<Panavergoodskinds>();
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		Panavergoodskinds paNaverGoodsKinds = null;
		PaGoodsKinds paGoodsKinds = null;
				
		try {
			// 전체 카테고리 조회
			categoriesListMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			categoriesList = objectMapper.convertValue(categoriesListMap, CategoriesList.class);
			
			String lgroup = "";
			String lgroupName = "";
			String mgroup = "";
			String mgroupName = "";
			String sgroup = "";
			String sgroupName = "";
			String dgroup = "";
			String dgroupName = "";
			Timestamp date = systemService.getSysdate();
			
			for(Categories category : categoriesList.getCategories()) {
				paNaverGoodsKinds = new Panavergoodskinds();
				paNaverGoodsKinds.setCategoryId(category.getId());
				paNaverGoodsKinds.setCategoryName(category.getName());
				paNaverGoodsKinds.setCategoryFullName(category.getWholeCategoryName());
				paNaverGoodsKinds.setLast(category.isLast() ? "Y" : "N");
				paNaverGoodsKinds.setInsertId(procId);
				paNaverGoodsKinds.setInsertDate(date);
				
				paNaverGoodsKindsList.add(paNaverGoodsKinds);
				
				if(!category.isLast()) {
					if(category.getWholeCategoryName().split(">").length == 1){//대분류
						lgroup = category.getId();
						lgroupName = category.getName();
					}else if(category.getWholeCategoryName().split(">").length == 2 ){//중분류
						mgroup = category.getId();
						mgroupName = category.getName();
					}
					else if(category.getWholeCategoryName().split(">").length == 3 ){//소분류
						sgroup = category.getId();
						sgroupName = category.getName();
					}
				} else {
					dgroup = "";
					dgroupName = "";
					if(category.getWholeCategoryName().split(">").length == 3 ){//소분류
						sgroup = category.getId();
						sgroupName = category.getName();
					}else{
						dgroup = category.getId();
						dgroupName = category.getName();
					}
					
					paGoodsKinds = new PaGoodsKinds();
					
					paGoodsKinds.setPaGroupCode("04");
					paGoodsKinds.setPaLmsdKey(category.getId());
					
					paGoodsKinds.setPaLgroup(lgroup);
					paGoodsKinds.setPaMgroup(mgroup);
					paGoodsKinds.setPaSgroup(sgroup);
					paGoodsKinds.setPaDgroup(dgroup);
					paGoodsKinds.setPaLgroupName(lgroupName);
					paGoodsKinds.setPaMgroupName(mgroupName);
					paGoodsKinds.setPaSgroupName(sgroupName);
					paGoodsKinds.setPaDgroupName(dgroupName);
					paGoodsKinds.setUseYn("1");
					paGoodsKinds.setInsertDate(date);
					paGoodsKinds.setInsertId(procId);
					paGoodsKinds.setModifyDate(date);
					paGoodsKinds.setModifyId(procId);
					
					paGoodsKindsList.add(paGoodsKinds);
				}
			}
			
			paNaverCommonService.savePaNaverGoodsKindsTx(paNaverGoodsKindsList,paGoodsKindsList);
		
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return categoriesList;
	}
	
	
	/**
	 * 카테고리 조회
	 * 
	 * @param categoryId
	 * @param procId
	 * @return CategoriesList
	 */
	public Categories getCategories(String categoryId, String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_003");
		serviceLog.setServiceNote("[V3]네이버 공통-카테고리 상세 정보 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// VO 선언
		Categories categories = new Categories();
		
		Map<String, Object> categoriesListMap	= new HashMap<String, Object>() ;
		List<Panaverkindsexcept> paNaverKindsExceptList = new ArrayList<Panaverkindsexcept>();
		List<Panaverkindscerti> paNaverKindsCertiList = new ArrayList<Panaverkindscerti>();
		List<Panaverkindscertikinds> paNaverKindsCertiKindsList = new ArrayList<Panaverkindscertikinds>();
		List<Panavergoodskinds> paNaverGoodsKindsList = new ArrayList<Panavergoodskinds>();
		Panaverkindsexcept paNaverKindsExcept = null;
		Panaverkindscerti paNaverKindsCerti = null;
		Panaverkindscertikinds paNaverKindsCertiKinds = null;
				
		try {
			if("".equals(categoryId)) {
				paNaverGoodsKindsList = paNaverCommonService.selectPaNaverCategoryInfo();
			} else {
				Panavergoodskinds panavergoodskinds = new Panavergoodskinds();
				panavergoodskinds.setCategoryId(categoryId);
				paNaverGoodsKindsList.add(panavergoodskinds);
			}
			
			Timestamp date = systemService.getSysdate();
			
			for(Panavergoodskinds paNaverGoodsKinds : paNaverGoodsKindsList){
				// 카테고리 조회
				categoriesListMap = paNaverV3ConnectUtil.getCommon(serviceLog, paNaverGoodsKinds.getCategoryId(), queryParameters, null);	
				
				// Map -> VO 변환
				ObjectMapper objectMapper = new ObjectMapper();
				categories = objectMapper.convertValue(categoriesListMap, Categories.class);
				
				for(String exceptionalCategory : categories.getExceptionalCategories()) {
					paNaverKindsExcept = new Panaverkindsexcept();
					paNaverKindsExcept.setCategoryId(paNaverGoodsKinds.getCategoryId());
					paNaverKindsExcept.setExceptCode(ExceptionalCategoryType.valueOf(exceptionalCategory).toString());
					paNaverKindsExcept.setExceptName(ExceptionalCategoryType.valueOf(exceptionalCategory).codeName());
					paNaverKindsExcept.setInsertId(procId);
					paNaverKindsExcept.setInsertDate(date);
				
					paNaverKindsExceptList.add(paNaverKindsExcept);
				}
				
				for(CertificationInfos certificationInfo : categories.getCertificationInfos()) {
					paNaverKindsCerti = new Panaverkindscerti();
					paNaverKindsCerti.setCategoryId(paNaverGoodsKinds.getCategoryId());
					paNaverKindsCerti.setCertiCode(certificationInfo.getId());
					paNaverKindsCerti.setCertiName(certificationInfo.getName());
					paNaverKindsCerti.setInsertId(procId);
					paNaverKindsCerti.setInsertDate(date);
				
					paNaverKindsCertiList.add(paNaverKindsCerti);
					
					for(String kindType : certificationInfo.getKindTypes()) {
						if("ETC".equals(kindType)) {
							paNaverKindsCertiKinds = new Panaverkindscertikinds();
							paNaverKindsCertiKinds.setCategoryId(paNaverGoodsKinds.getCategoryId());
							paNaverKindsCertiKinds.setCertiCode(certificationInfo.getId());
							paNaverKindsCertiKinds.setCertiKindCode(KindType.valueOf(kindType).toString());
							paNaverKindsCertiKinds.setCertiKindName(KindType.valueOf(kindType).codeName());
							paNaverKindsCertiKinds.setInsertId(procId);
							paNaverKindsCertiKinds.setInsertDate(date);
						
							paNaverKindsCertiKindsList.add(paNaverKindsCertiKinds);
						}
					}
				}
				Thread.sleep(300);
			}
			
			paNaverCommonService.savePaNaverCategoryInfoTx(paNaverKindsExceptList, paNaverGoodsKindsList, paNaverKindsCertiList, paNaverKindsCertiKindsList);
		
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return categories;
	}
	
	
	/**
	 * 브랜드 조회
	 * 
	 * @param name
	 * @param procId
	 * @return BrandsList
	 */
	public BrandsList getBrands(String name, String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_004");
		serviceLog.setServiceNote("[V3]네이버 공통-브랜드 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();		
		queryParameters.put("name", name);
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		BrandsList BrandsList = new BrandsList();
		
		Map<String, Object> brandsListMap	= new HashMap<String, Object>() ;
				
		try {
			// 브랜드 조회
			brandsListMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			BrandsList = objectMapper.convertValue(brandsListMap, BrandsList.class);
			
			// 비지니스 로직 작성 예정
		
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return BrandsList;
	}
	
	
	/**
	 * 모델 조회
	 * 
	 * @param name
	 * @param page
	 * @param size
	 * @param procId
	 * @return ModelsList
	 */
	public ModelsList getModels(String name,  String page, String size, String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_006");
		serviceLog.setServiceNote("[V3]네이버 공통-모델 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();		
		queryParameters.put("name", name);
		queryParameters.put("page", page);
		queryParameters.put("size", size);
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		ModelsList modelsList = new ModelsList();
		
		Map<String, Object> modelsListMap	= new HashMap<String, Object>() ;
				
		try {
			// 모델 조회
			modelsListMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			modelsList = objectMapper.convertValue(modelsListMap, ModelsList.class);
			
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return modelsList;
	}
	
	/**
	 * 모델 단건 조회
	 * 
	 * @param id
	 * @param procId
	 * @return Models
	 */
	public Models getModelsSingle(String id, String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_00_007");
		serviceLog.setServiceNote("[V3]네이버 공통-모델 단건 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();		
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		Models model = new Models();
		
		Map<String, Object> modelsMap	= new HashMap<String, Object>() ;
		
		try {
			pathParameters = id;
			
			// 모델 조회
			modelsMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);	
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			model = objectMapper.convertValue(modelsMap, Models.class);
			
			serviceLog.setResultCode("200");
			serviceLog.setResultMsg(PaNaverV3ApiRequest.API_SUCCESS_CODE);
			
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}
		
		transLogService.logTransServiceEnd(serviceLog);
		
		return model;
	}
	

}
