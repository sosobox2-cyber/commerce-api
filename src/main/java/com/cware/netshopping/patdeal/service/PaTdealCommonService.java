package com.cware.netshopping.patdeal.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.model.Brand;
import com.cware.netshopping.domain.model.PaBrand;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.domain.model.PaTdealDisplayCategory;
import com.cware.netshopping.domain.model.PaTdealShipArea;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ApiRequest;
import com.cware.netshopping.patdeal.domain.Areas;
import com.cware.netshopping.patdeal.domain.AreasList;
import com.cware.netshopping.patdeal.domain.Brands;
import com.cware.netshopping.patdeal.domain.BrandsList;
import com.cware.netshopping.patdeal.domain.Categories;
import com.cware.netshopping.patdeal.domain.CategoriesList;
import com.cware.netshopping.patdeal.domain.DisplayCategories;
import com.cware.netshopping.patdeal.domain.DisplayCategoriesList;
import com.cware.netshopping.patdeal.domain.DutyCategories;
import com.cware.netshopping.patdeal.domain.DutyCategoriesList;
import com.cware.netshopping.patdeal.domain.DutyCategoryContents;
import com.cware.netshopping.patdeal.repository.PaTdealCommonMapper;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaTdealCommonService {
		
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Autowired
	private PaTdealResultService paTdealResultService;
		
	@Autowired
	private SystemService systemService;
	
    @Autowired
    PaTdealCommonMapper paTdealCommonMapper;

    private Logger log = LoggerFactory.getLogger(this.getClass());

	// 티딜 디폴트 타겟 코드
	String defaultCode = "999999";
    // Pa_code
    String paCode = PaCode.TDEAL_TV.code();    
	// Pa_group_code
	String paGroupCode = PaGroup.TDEAL.code();


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
		serviceLog.setServiceName("IF_PATDEALAPI_00_001");
		serviceLog.setServiceNote("[API]티딜 공통-전체 카테고리 조회");
		serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		CategoriesList categoriesList = new CategoriesList();
		
		Map<String, Object> categoriesListMap	= new HashMap<String, Object>();
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>();
		PaGoodsKinds paGoodsKinds = null;
		
		try {
			// 전체 카테고리 조회
			categoriesListMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			categoriesList = objectMapper.convertValue(categoriesListMap, CategoriesList.class);
			
			Timestamp date = systemService.getSysdate();
			
			for(Categories category : categoriesList.getCategories()) {
				paGoodsKinds = new PaGoodsKinds();
				
				paGoodsKinds.setPaGroupCode(PaGroup.TDEAL.code());
				paGoodsKinds.setPaLmsdKey(category.getDepth4CategoryNo());
				paGoodsKinds.setPaLgroup(category.getDepth1CategoryNo());
				paGoodsKinds.setPaMgroup(category.getDepth2CategoryNo());
				paGoodsKinds.setPaSgroup(category.getDepth3CategoryNo());
				paGoodsKinds.setPaDgroup(category.getDepth4CategoryNo());
				paGoodsKinds.setPaLgroupName(category.getDepth1CategoryName());
				paGoodsKinds.setPaMgroupName(category.getDepth2CategoryName());
				paGoodsKinds.setPaSgroupName(category.getDepth3CategoryName());
				paGoodsKinds.setPaDgroupName(category.getDepth4CategoryName());
				paGoodsKinds.setUseYn("1");
				paGoodsKinds.setCertYn("0");
				paGoodsKinds.setInsertDate(date);
				paGoodsKinds.setInsertId(procId);
				paGoodsKinds.setModifyDate(date);
				paGoodsKinds.setModifyId(procId);
				
				paGoodsKindsList.add(paGoodsKinds);
			}

			paTdealResultService.savePaGoodsKinds(paGoodsKindsList);
			
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
	 * 전체 전시카테고리 조회
	 * 
	 * @param procId
	 * @return DisplayCategoriesList
	 */
	public DisplayCategoriesList getDisplayCategories(String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_002");
		serviceLog.setServiceNote("[API]티딜 공통-전체 전시카테고리 조회");
		serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		DisplayCategoriesList displayCategoriesList = new DisplayCategoriesList();
		
		Map<String, Object> displayCategoriesListMap = new HashMap<String, Object>();
		List<PaTdealDisplayCategory> paTdealDispCategoryList = new ArrayList<PaTdealDisplayCategory>();
		PaTdealDisplayCategory paTdealDispCategory 	 = null;
		
		try {
			// 전체 전시카테고리 조회
			displayCategoriesListMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			displayCategoriesList = objectMapper.convertValue(displayCategoriesListMap, DisplayCategoriesList.class);
			
			Timestamp date = systemService.getSysdate();
			
			for(DisplayCategories displayCategory : displayCategoriesList.getDisplayCategories()) {
				paTdealDispCategory = new PaTdealDisplayCategory();
				
				paTdealDispCategory.setDispCatId(displayCategory.getDepth5DisplayCategoryNo());
				paTdealDispCategory.setDepth1CatNm(displayCategory.getDepth1DisplayCategoryName());
				paTdealDispCategory.setDepth2CatNm(displayCategory.getDepth2DisplayCategoryName());
				paTdealDispCategory.setDepth3CatNm(displayCategory.getDepth3DisplayCategoryName());
				paTdealDispCategory.setDepth4CatNm(displayCategory.getDepth4DisplayCategoryName());
				paTdealDispCategory.setDepth5CatNm(displayCategory.getDepth5DisplayCategoryName());
				paTdealDispCategory.setFullCatNm(displayCategory.getFullCategoryName());
				paTdealDispCategory.setDispOrder(displayCategory.getDepth5DisplayOrder());
				paTdealDispCategory.setDispYn(displayCategory.getDepth5DisplayYn());
				paTdealDispCategory.setDeleteYn(displayCategory.getDeleteYn());
				paTdealDispCategory.setInsertDate(date);
				paTdealDispCategory.setInsertId(procId);
				paTdealDispCategory.setModifyDate(date);
				paTdealDispCategory.setModifyId(procId);
				
				paTdealDispCategoryList.add(paTdealDispCategory);
			}
			
			paTdealResultService.savePaTdealDispCategory(paTdealDispCategoryList);
			
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
		
		return displayCategoriesList;
	}
	
	
	/**
	 * 전체 정보고시 항목 조회
	 * 
	 * @param procId
	 * @return DutyCategoriesList
	 */
	public DutyCategoriesList getOffer(String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_006");
		serviceLog.setServiceNote("[API]티딜 공통-전체 정보고시 항목 조회");
		serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		DutyCategoriesList dutyCategoriesList = new DutyCategoriesList();
		
		Map<String, Object> dutyCategoriesListMap = new HashMap<String, Object>();
		List<PaOfferCode> paOfferCodeList = new ArrayList<PaOfferCode>();
		PaOfferCode paOfferCode = null;
		
		try {
			// 전체 정보고시 항목 조회
			dutyCategoriesListMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			dutyCategoriesList = objectMapper.convertValue(dutyCategoriesListMap, DutyCategoriesList.class);
			
			Timestamp date = systemService.getSysdate();
			
			for(DutyCategories dutyCategory : dutyCategoriesList.getDutyCategories()) {
				for(DutyCategoryContents dutyCategoryContents: dutyCategory.getDutyCategoryContents()) {
					paOfferCode = new PaOfferCode();
					
					paOfferCode.setPaGroupCode(PaGroup.TDEAL.code());
					paOfferCode.setPaOfferType(dutyCategory.getDutyCategoryNo());
					paOfferCode.setPaOfferTypeName(dutyCategory.getDutyCategoryName());
					paOfferCode.setPaOfferCode(dutyCategoryContents.getDisplayOrder());
					paOfferCode.setPaOfferCodeName(dutyCategoryContents.getContentName());
					paOfferCode.setRequiredYn("1");
					paOfferCode.setSortSeq(dutyCategoryContents.getDisplayOrder());
					paOfferCode.setUseYn("1");
					paOfferCode.setRemark(dutyCategoryContents.getDescriptions() == null ? "" :dutyCategoryContents.getDescriptions().toString());
					paOfferCode.setUnitRequiredYn("0");
					paOfferCode.setInsertDate(date);
					paOfferCode.setInsertId(procId);
					paOfferCode.setModifyDate(date);
					paOfferCode.setModifyId(procId);
					
					paOfferCodeList.add(paOfferCode);
				}
			}
			
			paTdealResultService.savePaOfferCode(paOfferCodeList);
			
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
		
		return dutyCategoriesList;
	}
	
	
	/**
	 * 전체 브랜드 조회
	 * 
	 * @param procId
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBrands(String procId) {
        
        // 서비스 로그 생성
        PaTransService serviceLog = new PaTransService();
        serviceLog.setTransCode(defaultCode);
        serviceLog.setTransType(TransType.COMMON.name());
        serviceLog.setServiceName("IF_PATDEALAPI_00_003");
        serviceLog.setServiceNote("[API]티딜 공통-전체 브랜드 조회");
        serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
        serviceLog.setProcessId(procId);
        transLogService.logTransServiceStart(serviceLog);

        // Query Parameters 세팅
        Map<String, String> queryParameters = new HashMap<String, String>();

        // Path Parameters
        String pathParameters = "";
        
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List <PaBrand> paBrandList = new ArrayList<PaBrand>();
        List<Map<String, Object>> brandMapList = new ArrayList<Map<String, Object>>();

        try {
            // 전체 브랜드 조회
            resultMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);
            brandMapList = (List<Map<String, Object>>) resultMap.get("Data");
            
            for(Map<String, Object> brands : brandMapList) {
                PaBrand paBrand = new PaBrand();
                paBrand.setPaBrandNo(brands.get("brandNo").toString());
                paBrand.setPaBrandName(brands.get("name").toString());
                paBrand.setPaGroupCode(paGroupCode);
                paBrand.setInsertId(procId);
                paBrand.setModifyId(procId);
                paBrand.setUseYn("1");
                
                paBrandList.add(paBrand);
                                
            }
            
            paTdealResultService.savePaTdealBrand(paBrandList);
            
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
        
        return resultMap;
    }


    /**
	 * 브랜드 생성
	 * 
	 * @param procId
	 * @return
	 */
    public ResponseMsg registerBrand(String brandCode, String procId) {
        
        // 서비스 로그 생성
        PaTransService serviceLog = new PaTransService();
        serviceLog.setTransCode(defaultCode);
        serviceLog.setTransType(TransType.COMMON.name());
        serviceLog.setServiceName("IF_PATDEALAPI_00_004"); 
        serviceLog.setServiceNote("[API]티딜 공통-브랜드 생성");
        serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
        serviceLog.setProcessId(procId);
        transLogService.logTransServiceStart(serviceLog);

        ResponseMsg result = callRegisterBrand(brandCode, procId, serviceLog);

        serviceLog.setResultCode(result.getCode());
        serviceLog.setResultMsg(result.getMessage());
        transLogService.logTransServiceEnd(serviceLog);
        
        return result;
    }
    
    
    /**
	 * 배송비 지역 조회
	 * 
	 * @param procId
	 * @return AreasList
	 */
	public AreasList getAreas(String procId) {
		
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.COMMON.name());
		serviceLog.setServiceName("IF_PATDEALAPI_00_009");
		serviceLog.setServiceNote("[API]티딜 공통-배송비 지역 조회");
		serviceLog.setPaGroupCode(paGroupCode);
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
		
		// Path Parameters
		String pathParameters = "";
		
		// VO 선언
		AreasList areasList = new AreasList();
		
		Map<String, Object> areasListMap = new HashMap<String, Object>();
		List<PaTdealShipArea> patdealshipareaList = new ArrayList<PaTdealShipArea>();
		PaTdealShipArea patdealshiparea = null;
		
		try {
			// 배송비 지역 조회
			areasListMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, null);
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			areasList = objectMapper.convertValue(areasListMap, AreasList.class);
			
			Timestamp date = systemService.getSysdate();
			
			for(Areas areas : areasList.getAreas()) {
				patdealshiparea = new PaTdealShipArea();
				
				patdealshiparea.setAreaNo(areas.getAreaNo());
				patdealshiparea.setAddress(areas.getAddress());
				patdealshiparea.setCountryCd(areas.getCountryCd());
				patdealshiparea.setDefaultAreaYn(areas.isDefaultArea() ? "1" : "0");
				patdealshiparea.setInsertDate(date);
				patdealshiparea.setInsertId(procId);
				patdealshiparea.setModifyDate(date);
				patdealshiparea.setModifyId(procId);
				
				patdealshipareaList.add(patdealshiparea);
			}
			
			paTdealResultService.savePaTdealShipArea(patdealshipareaList);
			
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
		
		return areasList;
	}


    private ResponseMsg callRegisterBrand(String brandCode, String procId, PaTransService serviceLog) {
        
        ResponseMsg result = new ResponseMsg("", "");

        log.info("===== 브랜드 생성 서비스 Start - {} =====", brandCode);        
        
        try {
            // 브랜드 생성 대상 조회
            List<Brand> targetList = paTdealCommonMapper.selectRegisterBrandTarget(brandCode);

            if (targetList == null) {
                result.setCode("404");
                result.setMessage("처리할 내역이 없습니다.");
                return result;
            }
            
            List<Brands> brandsData =  new ArrayList<Brands>();

            for(Brand target : targetList) {
                Brands brands = new Brands();
                brands.setBrandNameKo(target.getBrandName());
                brands.setBrandNameType("NAME_KO"); //[NAME_KO: Korean, NAME_EN: English]
                
                brandsData.add(brands);
            }
            HashMap<String , Object> brandsMap = new  HashMap<String , Object>();
            brandsMap.put("brands", brandsData);
            
            // Body 세팅
            ParamMap apiDataObject = new ParamMap();
            apiDataObject.put("body", brandsMap);
            
            // Path Parameters
            String pathParameters = "";
            // Query Parameters 세팅
            Map<String, String> queryParameters = new HashMap<String, String>();            
            Map<String, Object> brandsListMap = new HashMap<String, Object>();
            
            //브랜드 생성 통신
            brandsListMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

            // VO 선언
            BrandsList brandsList = new BrandsList();
            // Map -> VO 변환
             ObjectMapper objectMapper = new ObjectMapper();
             brandsList = objectMapper.convertValue(brandsListMap, BrandsList.class);
             List<Brands> brands = brandsList.getBrands();
             
            List <PaBrand> paBrandList = new ArrayList<PaBrand>();

             for(Brands brand : brands) {
                 PaBrand paBrand = new PaBrand();
                paBrand.setPaBrandNo(brand.getBrandNo());
                paBrand.setPaBrandName(brand.getBrandName());
                paBrand.setPaGroupCode(paGroupCode);
                paBrand.setInsertId(procId);
                paBrand.setModifyId(procId);
                
                paBrandList.add(paBrand);
             }
             
             paTdealResultService.savePaTdealBrand(paBrandList);
            
             result.setCode("200");
             result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);
             
             
        } catch (TransApiException ex) {
            result.setCode(ex.getCode());
            result.setMessage(ex.getMessage());
        } catch (Exception e) {
            result.setCode("500");
            result.setMessage(e.getMessage());
        }finally {
            log.info("===== 브랜드 생성 서비스 End - {} =====", brandCode);
        }
        
        return result;
    }
	
}
