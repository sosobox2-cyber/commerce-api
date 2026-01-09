package com.cware.netshopping.panaver.v3.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.product.type.PaCertificationVO;
import com.cware.api.panaver.product.type.PaDeliveryVO;
import com.cware.api.panaver.product.type.PaNaverGoodsVO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaCode;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaNaverGoodsImageVO;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaNaverGoodsImage;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.panaver.goods.repository.PaNaverGoodsDAO;
import com.cware.netshopping.panaver.goods.service.PaNaverGoodsService;
import com.cware.netshopping.panaver.v2.repository.PaNaverGoodsMapper;
import com.cware.netshopping.panaver.v2.service.PaNaverApiRequest;
import com.cware.netshopping.panaver.v2.service.PaNaverResultService;
import com.cware.netshopping.panaver.v3.domain.AfterServiceInfo;
import com.cware.netshopping.panaver.v3.domain.Bag;
import com.cware.netshopping.panaver.v3.domain.Books;
import com.cware.netshopping.panaver.v3.domain.CarArticles;
import com.cware.netshopping.panaver.v3.domain.CertificationKindType;
import com.cware.netshopping.panaver.v3.domain.CertificationTargetExcludeContent;
import com.cware.netshopping.panaver.v3.domain.ClaimDeliveryInfo;
import com.cware.netshopping.panaver.v3.domain.Contents;
import com.cware.netshopping.panaver.v3.domain.Cosmetic;
import com.cware.netshopping.panaver.v3.domain.CustomerBenefit;
import com.cware.netshopping.panaver.v3.domain.DeliveryFee;
import com.cware.netshopping.panaver.v3.domain.DeliveryFeeByArea;
import com.cware.netshopping.panaver.v3.domain.DeliveryInfo;
import com.cware.netshopping.panaver.v3.domain.DetailAttribute;
import com.cware.netshopping.panaver.v3.domain.DietFood;
import com.cware.netshopping.panaver.v3.domain.DigitalContents;
import com.cware.netshopping.panaver.v3.domain.DiscountMethod;
import com.cware.netshopping.panaver.v3.domain.Etc;
import com.cware.netshopping.panaver.v3.domain.EtcService;
import com.cware.netshopping.panaver.v3.domain.FashionItems;
import com.cware.netshopping.panaver.v3.domain.Food;
import com.cware.netshopping.panaver.v3.domain.Furniture;
import com.cware.netshopping.panaver.v3.domain.GeneralFood;
import com.cware.netshopping.panaver.v3.domain.HomeAppliances;
import com.cware.netshopping.panaver.v3.domain.ImageAppliances;
import com.cware.netshopping.panaver.v3.domain.Images;
import com.cware.netshopping.panaver.v3.domain.ImagesList;
import com.cware.netshopping.panaver.v3.domain.ImmediateDiscountPolicy;
import com.cware.netshopping.panaver.v3.domain.Jewellery;
import com.cware.netshopping.panaver.v3.domain.Kids;
import com.cware.netshopping.panaver.v3.domain.KitchenUtensils;
import com.cware.netshopping.panaver.v3.domain.MedicalAppliances;
import com.cware.netshopping.panaver.v3.domain.MicroElectronics;
import com.cware.netshopping.panaver.v3.domain.MobileDiscountMethod;
import com.cware.netshopping.panaver.v3.domain.MusicalInstrument;
import com.cware.netshopping.panaver.v3.domain.NaverShoppingSearchInfo;
import com.cware.netshopping.panaver.v3.domain.Navigation;
import com.cware.netshopping.panaver.v3.domain.OfficeAppliances;
import com.cware.netshopping.panaver.v3.domain.OpticsAppliances;
import com.cware.netshopping.panaver.v3.domain.OptionCombinationGroupNames;
import com.cware.netshopping.panaver.v3.domain.OptionCombinations;
import com.cware.netshopping.panaver.v3.domain.OptionInfo;
import com.cware.netshopping.panaver.v3.domain.OptionalImages;
import com.cware.netshopping.panaver.v3.domain.OriginAreaInfo;
import com.cware.netshopping.panaver.v3.domain.OriginProduct;
import com.cware.netshopping.panaver.v3.domain.Product;
import com.cware.netshopping.panaver.v3.domain.ProductCertificationInfos;
import com.cware.netshopping.panaver.v3.domain.ProductImages;
import com.cware.netshopping.panaver.v3.domain.ProductInfoProvidedNotice;
import com.cware.netshopping.panaver.v3.domain.ProductSearch;
import com.cware.netshopping.panaver.v3.domain.PurchaseQuantityInfo;
import com.cware.netshopping.panaver.v3.domain.PurchaseReviewInfo;
import com.cware.netshopping.panaver.v3.domain.RentalEtc;
import com.cware.netshopping.panaver.v3.domain.RepresentativeImage;
import com.cware.netshopping.panaver.v3.domain.SeasonAppliances;
import com.cware.netshopping.panaver.v3.domain.SellerCodeInfo;
import com.cware.netshopping.panaver.v3.domain.Shoes;
import com.cware.netshopping.panaver.v3.domain.SleepingGear;
import com.cware.netshopping.panaver.v3.domain.SmartstoreChannelProduct;
import com.cware.netshopping.panaver.v3.domain.SportsEquipment;
import com.cware.netshopping.panaver.v3.domain.Wear;
import com.cware.netshopping.panaver.v3.repository.PaNaverGoodsV3Mapper;
import com.cware.netshopping.panaver.v3.repository.PaNaverV3GoodsDAO;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ApiRequest;
import com.cware.netshopping.panaver.v3.util.PaNaverV3ConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class PaNaverV3ProductService {
		
	@Autowired
	private TransLogService transLogService;
	
	@Autowired
	private PaNaverV3ConnectUtil paNaverV3ConnectUtil;
	
	@Autowired
	PaNaverGoodsMapper paNaverGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;
	
	@Autowired
	@Qualifier("panaver.goods.paNaverGoodsDAO")
	private PaNaverGoodsDAO paNaverGoodsDAO;
	
	@Autowired
	@Qualifier("panaver.v3.goods.paNaverV3GoodsDAO")
	private PaNaverV3GoodsDAO paNaverV3GoodsDAO;
	
	@Autowired
	@Qualifier("panaver.goods.paNaverGoodsService")
	private PaNaverGoodsService paNaverGoodsService;
		
	@Autowired
	@Qualifier("panaver.v3.goods.paNaverV3GoodsService")
	private PaNaverV3GoodsService paNaverV3GoodsService;
	
	@Autowired
	PaNaverResultService paNaverResultService;
	
	@Autowired
	PaNaverResultV3Service paNaverResultV3Service;
	
	@Autowired
	PaNaverGoodsV3Mapper paNaverGoodsV3Mapper;
	
	@Autowired
	PaGoodsdtMappingMapper goodsdtMappingMapper;
	
	@Autowired
    ServletContext context;
	
	@Autowired
	SyncProductService syncProductService;
	
	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	NaverProductV3Service naverProductV3Service;
	
	// 이미지 경로
	@Value("${partner.naver.api.filePath}")
	String IMG_FILE_PATH;
	
	// 이미지 서버 환경
	@Value("${partner.naver.api.environment}")
	String IMG_SERVER;
	
		
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	// 네이버V3 디폴트 타겟 코드
	String defaultCode = "999999";
	// Pa_code
	String paCode = PaCode.NAVER.code();
	// Pa_group_code
	String paGroupCode = PaGroup.NAVER.code();
	
	/**
	 * 네이버 신규상품 입점
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg registerProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_002");
		serviceLog.setServiceNote("[V3]네이버-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paNaverGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;
	}


	/**
	 * 상품 이미지 등록
	 * 
	 * @param imageFilePath
	 * @param procId
	 * @return ImagesList
	 * @throws Exception 
	 */
	public ResponseMsg postImages(String goodsCode, String paCode, String procId, long transBatchNo){
		
		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_005");
		serviceLog.setServiceNote("[V3]네이버 상품-상품 이미지 다건 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 이미지 등록
		ResponseMsg result = callPostImages(goodsCode, paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}

	/**
	 * 상품 목록 조회
	 * 
	 * @param channelProductNo
	 * @param procId
	 * @return ProductSearch
	 */
	public ResponseMsg getChannelSearch(String startPage, String endPage, String procId) {
		
		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_007");
		serviceLog.setServiceNote("[V3]네이버 상품-상품 목록 조회");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		ResponseMsg result = callChannelSearch(startPage, endPage, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}
	
	public ResponseMsg getChannelSearchV2(String goodsCode, String paCode, String procId) {
		// 서비스 로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_007");
		serviceLog.setServiceNote("[V3]네이버 상품-상품 목록 조회v2");
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		ResponseMsg result = callChannelSearchV2(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	private ResponseMsg callChannelSearchV2(String goodsCode, String paCode, String procId, PaTransService serviceLog) {
        ResponseMsg result = new ResponseMsg("", "");

        log.info("===== 상품목록조회서비스 Start - {} =====", goodsCode);
        
        try {
            PaNaverGoodsVO paNaverGoodsVO = new PaNaverGoodsVO();
            paNaverGoodsVO.setGoodsCode(goodsCode);
            paNaverGoodsVO.setPaCode(paCode);

            // 상품 목록 조회 대상
            List<PaNaverGoodsVO> paNaverGoodsList = paNaverGoodsDAO.selectChannelSearchTarget(paNaverGoodsVO);

            if (paNaverGoodsList == null) {
                result.setCode("404");
                result.setMessage("처리할 내역이 없습니다.");
                return result;
            }

            ArrayList<String> channelProductNos = new ArrayList<String>();

            for (PaNaverGoodsVO paNaverGoods : paNaverGoodsList) {
                channelProductNos.add(paNaverGoods.getProductId());
            }

            // Body 세팅
            ParamMap apiDataObject = new ParamMap();
            apiDataObject.put("searchKeywordType", "CHANNEL_PRODUCT_NO");
            apiDataObject.put("channelProductNos", channelProductNos);
            apiDataObject.put("size", 499);

            // Path Parameters
            String pathParameters = "";
            // Query Parameters 세팅
            Map<String, String> queryParameters = new HashMap<String, String>();            
            Map<String, Object> productMap = new HashMap<String, Object>();
            
            //상품목록조회 통신
            productMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

            // VO 선언
            ProductSearch productSearch = new ProductSearch();
            
            // Map -> VO 변환
            ObjectMapper objectMapper = new ObjectMapper();
            productSearch = objectMapper.convertValue(productMap, ProductSearch.class);

            List<Contents> contentsList = productSearch.getContents();

            for (Contents contents : contentsList) {
                long channelProductNo = contents.getChannelProducts().get(0).getChannelProductNo();
                long originProductNo = contents.getChannelProducts().get(0).getOriginProductNo();
                paNaverGoodsV3Mapper.updateOrgProductNo(channelProductNo, originProductNo, procId);
            }

            result.setCode("200");
            result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);

        } catch (TransApiException ex) {
            result.setCode(ex.getCode());
            result.setMessage(ex.getMessage());
        } catch (Exception e) {
            result.setCode("500");
            result.setMessage(e.getMessage());
        }finally {
            log.info("===== 상품목록조회서비스 End - {} =====", goodsCode);
        }
            return result;
	}


	private ResponseMsg callChannelSearch(String startPage, String endPage, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		log.info("===== 상품목록조회서비스 Start =====");
		
		try {
			 
			int start = Integer.valueOf(startPage);
			int end   = Integer.valueOf(endPage);

			for(int page = start; page < end + 1; page++ ) {
				// Body 세팅
				ParamMap apiDataObject = new ParamMap();
				apiDataObject.put("size", 499);
				apiDataObject.put("page", page);
				apiDataObject.put("productStatusTypes", "SALE");
				
				// Path Parameters
				String pathParameters = "";
				// Query Parameters 세팅
				Map<String, String> queryParameters = new HashMap<String, String>();			
				Map<String, Object> productMap = new HashMap<String, Object>();
				
				// 채널 상품 조회 통신
				productMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
				
				// VO 선언
				ProductSearch productSearch = new ProductSearch();
				// Map -> VO 변환
				ObjectMapper objectMapper = new ObjectMapper();
				productSearch = objectMapper.convertValue(productMap, ProductSearch.class);
				
				List<Contents> contentsList = productSearch.getContents();
				
				for (Contents contents : contentsList) {
					long channelProductNo = contents.getChannelProducts().get(0).getChannelProductNo();
					long originProductNo = contents.getChannelProducts().get(0).getOriginProductNo();
					paNaverGoodsV3Mapper.updateOrgProductNo(channelProductNo, originProductNo, procId);
				}
			}

			result.setCode("200");
			result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
		}finally {
			log.info("===== 상품목록조회서비스 End =====");
		}
		return result;
	}



	/**
	 * 옵션매핑 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg manageOption(String goodsCode, String paCode ,String procId , long transBatchNo) {
		
		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_001");
		serviceLog.setServiceNote("[V3]네이버 상품-옵션 매핑");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 옵션 매핑
		ResponseMsg result = callGetOption(goodsCode, paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}
	
	
	private ResponseMsg callGetOption(String goodsCode, String paCode, String procId, PaTransService serviceLog) {

		ResponseMsg result = new ResponseMsg("", "");

		PaNaverGoodsVO goods = paNaverGoodsMapper.getGoods(goodsCode, paCode);
		
		if(goods.getProductId() == null) {
			result.setCode("404");
			result.setMessage("처리할 내역이 없습니다.");
			return result;
		}

		try {
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = goods.getProductId();

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			// VO 선언
			Product product = new Product();

			Map<String, Object> productMap = new HashMap<String, Object>();

			log.info("옵션매핑서비스 Start {}", goodsCode);

			// 채널 상품 조회 통신
			productMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			product = objectMapper.convertValue(productMap, Product.class);

			OptionInfo optionInfo = product.getOriginProduct().getDetailAttribute().getOptionInfo();

			for (OptionCombinations option : optionInfo.getOptionCombinations()) {
				String goodsdtCode = option.getSellerManagerCode();
				String paOptionCode = String.valueOf(option.getId());
				log.info("상품:{} 단품:{} 옵션코드:{}", goodsCode, goodsdtCode, paOptionCode);
				if (!"null".equals(paOptionCode)) {
					goodsdtMappingMapper.updateOptionCodeForV3Naver(goodsCode, goodsdtCode, paCode, procId, paOptionCode);
				}
			}

			result.setCode("200");
			result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 옵션매핑서비스 End - {} =====", goodsCode);
		}
		return result;
	}
	
	/**
	 * 모델ID매핑 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg manageModelId(String goodsCode, String paCode ,String procId , long transBatchNo) {
		
		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_001");
		serviceLog.setServiceNote("[V3]네이버 상품-모델 ID 매핑");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 옵션 매핑
		ResponseMsg result = callGetModelId(goodsCode, paCode, procId, serviceLog);
		
		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);
		
		return result;
	}
	
	
	private ResponseMsg callGetModelId(String goodsCode, String paCode, String procId, PaTransService serviceLog) {

		ResponseMsg result = new ResponseMsg("", "");

		PaNaverGoodsVO goods = paNaverGoodsMapper.getGoods(goodsCode, paCode);
		
		if(goods.getProductId() == null) {
			result.setCode("404");
			result.setMessage("처리할 내역이 없습니다.");
			return result;
		}

		try {
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = goods.getProductId();

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			// VO 선언
			Product product = new Product();

			Map<String, Object> productMap = new HashMap<String, Object>();

			log.info("모델ID매핑서비스 Start {}", goodsCode);

			// 채널 상품 조회 통신
			productMap = paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			product = objectMapper.convertValue(productMap, Product.class);
			NaverShoppingSearchInfo searchInfo =  product.getOriginProduct().getDetailAttribute().getNaverShoppingSearchInfo();
			
			if(searchInfo != null) {
				String modelId = String.valueOf(searchInfo.getModelId());
				if (!"null".equals(modelId)) {
					log.info("상품:{} 모델ID:{}", goodsCode, modelId);
					paNaverGoodsV3Mapper.updateModelId(goodsCode, modelId, procId);
				}
			}

			result.setCode("200");
			result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);

		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 모델ID매핑서비스 End - {} =====", goodsCode);
		}
		return result;
	}


	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {

		ResponseMsg result = new ResponseMsg("", "");

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);

			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			PaNaverGoodsVO paNaverGoods = paNaverGoodsDAO.selectPaNaverGoodsInfo(paramMap);

			if (paNaverGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paNaverGoods.getProductId() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 네이버 상품코드 : " + paNaverGoods.getProductId());
				return result;
			}
			
			if (paNaverGoods.getDescribeExt() == null) {
				paNaverGoods.setDescribeExt("");
				if (paNaverGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.NAVER.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			paramMap.put("groupCode", paNaverGoods.getPaGroupCode());
 			
			PaNaverGoodsImageVO paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap); 
			// 이미지 등록
			if (paNaverGoodsImage == null) {
				result = callPostImages(goodsCode, paCode, procId, serviceLog);
				if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
					if(result.getMessage().contains("올바른 이미지 파일이")) { // 이미지 등록 실패 시 입접 반려 처리 추가  2023/12/14 (nwkim)
						paNaverGoodsMapper.rejectTransTarget(goodsCode, paCode, procId, StringUtil.truncate(result.getMessage(), 500));
					}
					return result;
				}
				paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);
			}
			
			// 인증정보
			List<PaCertificationVO> paCertificationList = paNaverGoodsService.selectPaCertificationList(paramMap);			
			
			// 옵션 등록
			paramMap.put("paGoodsCode",paramMap.get("goodsCode"));
			List<PaGoodsdtMapping> goodsdtList = paNaverGoodsService.selectPaGoodsdt(paramMap);
			
			if (goodsdtList.size() < 1) {
				result.setCode("404");
                result.setMessage("처리할 내역이 없습니다.");		
				return result;
			}
			
			// 입점요청중
			paNaverGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			settingDescribeExt(paNaverGoods);

			paNaverGoods.setModifyId(procId);
			
			// 네이버 상품 전문 (OriginProduct 객체 생성)
			OriginProduct originProduct = createProduct(paNaverGoods, paNaverGoodsImage, goodsPrice, serviceLog.getTransServiceNo(), paCertificationList, goodsdtList);

			// 네이버 상품 전문 (SmartstoreChannelProduct 객체 생성)			
			SmartstoreChannelProduct smartstoreChannelProduct = new SmartstoreChannelProduct(); // 스마트스토어 채널 상품		
			// 네이버쇼핑 채널 상품 전용 상품명
			smartstoreChannelProduct.setChannelProductName(paNaverGoods.getPaGoodsName().replace("*", "X").replace("?", ""));
			// 네이버쇼핑 등록 여부
			smartstoreChannelProduct.setNaverShoppingRegistration("TRUE");
			// 전시 상태 코드(스마트스토어 채널 전용)
			smartstoreChannelProduct.setChannelProductDisplayStatusType("ON"); //WAIT(전시 대기), ON(전시 중), SUSPENSION(전시 중지)
						
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();		
			
			apiDataObject.put("originProduct", originProduct);
			apiDataObject.put("smartstoreChannelProduct", smartstoreChannelProduct);
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = "";
						
			Map<String, Object> productMap = new HashMap<String, Object>();	

			log.info("상품등록 API 호출 {}", goodsCode);
			
			// 상품등록API 통신
			productMap= paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			long orgProductNo = (long)productMap.get("originProductNo"); // 원상품코드
			long productId = (long) productMap.get("smartstoreChannelProductNo"); // 채널상품코드

			if (productId != 0) {
				result.setCode("200");
				result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);
				paNaverGoods.setOrgProductNo(Long.toString(orgProductNo)); 
				paNaverGoods.setProductId(Long.toString(productId));	
				paNaverResultV3Service.saveTransProduct(paNaverGoods, goodsPrice);
			} else {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다.");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
            paNaverGoodsMapper.rejectTransTarget(goodsCode, paCode, procId, StringUtil.truncate(result.getMessage(), 500));
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품등록서비스 End - {} =====", goodsCode);
		}
		return result;
	}
	
	
	private OriginProduct createProduct(PaNaverGoodsVO paNaverGoods, PaNaverGoodsImageVO paNaverGoodsImage,
			PaGoodsPriceApply goodsPrice, long transServiceNo, List<PaCertificationVO> paCertificationList, List<PaGoodsdtMapping> goodsdtList) throws Exception {
		
		/*객체생성*/
		OriginProduct originProduct = new OriginProduct(); // 원 상품
		DetailAttribute detailAttribute = new DetailAttribute(); // 원상품 상세 속성
		ProductInfoProvidedNotice productInfoProvidedNotice =  new ProductInfoProvidedNotice(); // 상품정보제공고시
		CertificationTargetExcludeContent certificationTargetExcludeContent = new CertificationTargetExcludeContent(); // 인증 대상 제외 여부 정보
		AfterServiceInfo afterServiceInfo = new AfterServiceInfo(); // A/S 정보
		CustomerBenefit customerBenefit = new CustomerBenefit(); // 상품 고객 혜택 정보
		ImmediateDiscountPolicy immediateDiscountPolicy = new ImmediateDiscountPolicy(); // 판매자 즉시 할인 정책
		DiscountMethod discountMethod = new DiscountMethod(); // PC 할인 혜택
		//MobileDiscountMethod mobileDiscountMethod = new MobileDiscountMethod(); // 모바일 할인 혜택 - 24.02.07 필드 지원 중단
		PurchaseQuantityInfo purchaseQuantityInfo = new PurchaseQuantityInfo(); // 구매 수량 설정 정보
		SmartstoreChannelProduct smartstoreChannelProduct = new SmartstoreChannelProduct(); // 스마트스토어 채널 상품
		PurchaseReviewInfo purchaseReviewInfo = new PurchaseReviewInfo(); // 구매평 정보
		OptionInfo optionInfo = new OptionInfo(); //옵션 정보		
		SellerCodeInfo sellerCodeInfo= new SellerCodeInfo(); // 판매자 코드 정보

		/*originProduct 객체 값 설정*/
		originProduct.setStockQuantity(paNaverGoods.getTransOrderAbleQty()); // 재고수량
		originProduct.setLeafCategoryId(paNaverGoods.getLmsdKey()); // 카테고리

//   	판매시작일,판매종료일 ISO 8601 형식으로 변경(yyyy-MM-dd'T'HH:mm[:ss][.SSS]XXX)
		Date StartDate = paNaverGoods.getSaleStartDate();
		Date EndDate = paNaverGoods.getSaleEndDate();		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");		
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		originProduct.setSaleStartDate(simpleDateFormat.format(StartDate)); // 판매시작일
		if(paNaverGoods.getSaleEndDate()==null) originProduct.setSaleEndDate("2999-12-31T00:00:00.000+09:00"); // 판매종료일
		else originProduct.setSaleEndDate(simpleDateFormat.format(EndDate)); // 판매종료일
		originProduct.setSalePrice((long)goodsPrice.getSalePrice()); // 판매가(원)

		// 판매상태 (판매중, 판매중지)
		originProduct.setStatusType(PaSaleGb.FORSALE.code().equals(paNaverGoods.getPaSaleGb()) ? "SALE" : "SUSPENSION");

		originProduct.setSaleType("NEW"); // 상품판매유형 (신상품, 중고, 진열, 리퍼)

		// 정보고시 세팅
		productInfoProvidedNotice = createProductSummaryInfo(paNaverGoods.getGoodsCode(), productInfoProvidedNotice);		
		detailAttribute.setProductInfoProvidedNotice(productInfoProvidedNotice);; // 상품 요약 정보	
		
		// 상품명 특수문자 처리 
		String paGoodsName = paNaverGoods.getPaGoodsName().replace("*", "X").replace("?", ""); 
		//	상품명에 브랜드명 없을 시 추가
		if(!"기타".equals(paNaverGoods.getBrandName())) {
			if(paGoodsName.startsWith(paNaverGoods.getBrandName())){			
				paGoodsName = paGoodsName.replaceFirst(paNaverGoods.getBrandName(), "[" + paNaverGoods.getBrandName() + "]");
			}else if(!paGoodsName.startsWith("["+paNaverGoods.getBrandName()+"]")){
				paGoodsName = "[" + paNaverGoods.getBrandName() + "]" + paGoodsName;
			}
		}		

		String collectYn = paNaverGoods.getCollectYn();
		
		if ("1".equals(collectYn)) {
			paGoodsName = "(착불)" + paGoodsName;
		}
		
		//	상품명
		originProduct.setName(paGoodsName);

		// 네이버 인증정보 세팅
		if(paNaverGoods.getExceptCode() != null){
			// 네이버 필수인증정보 카테고리 상품의 경우 인증대상을 모두 예외로 세팅하여 반영하고 인증번호는 세팅되지 않도록 주석처리한다. 2022.10.25 LEEJY
			if(paNaverGoods.getExceptYn() > 0) {	// 2023-02-01 GBJEONG 예외인증정보 유무
				detailAttribute.setProductCertificationInfos(createCertificationListInfo(paCertificationList)); // 인증정보
			} else {
				if("KC_CERTIFICATION".equals(CertificationKindType.valueOf(paNaverGoods.getExceptCode()).codeName())) {	// KC 인증대상
					certificationTargetExcludeContent.setKcCertifiedProductExclusionYn("TRUE");
				}else if("GREEN_PRODUCTS".equals(CertificationKindType.valueOf(paNaverGoods.getExceptCode()).codeName())) { // 친환경 인증대상
					certificationTargetExcludeContent.setGreenCertifiedProductExclusionYn("TRUE");
				}else if("CHILD_CERTIFICATION".equals(CertificationKindType.valueOf(paNaverGoods.getExceptCode()).codeName())) { // 어린이제품 인증대상
					certificationTargetExcludeContent.setChildCertifiedProductExclusionYn("TRUE");
				}
				detailAttribute.setCertificationTargetExcludeContent(certificationTargetExcludeContent);
			}
		}
		
		// 원산지 정보
		detailAttribute.setOriginAreaInfo(createOriginAreaInfo(paNaverGoods));
		
		// 부가세 타입 코드
		if ("1".equals(paNaverGoods.getTaxSmallYN())) {
			detailAttribute.setTaxType("SMALL");
		} else {
			detailAttribute.setTaxType("1".equals(paNaverGoods.getTaxYN()) ? "TAX" : "DUTYFREE");
		}

		// 미성년자 구매가능 여부
		detailAttribute.setMinorPurchasable("0".equals(paNaverGoods.getAdultYN()) ? "TRUE" : "FALSE");
		
		// 이미지 정보
		originProduct.setProductImages(createImageInfo(paNaverGoodsImage));

		 // 상품 상세 내용.
		originProduct.setDetailContent(paNaverGoods.getDescribeExt().replaceAll("\\p{C}", ""));
		
		// A/S 전화번호
		afterServiceInfo.setAfterServiceTelephoneNumber(paNaverGoods.getCsTel());
		
		// A/S 내용. HTML 입력 불가
		afterServiceInfo.setAfterServiceGuideContent(paNaverGoods.getCsDetail());
		
		detailAttribute.setAfterServiceInfo(afterServiceInfo);

		// 배송정보. 배송 없는 상품인 경우 아예 입력하지 않는다.
		originProduct.setDeliveryInfo(createDeliveryInfo(paNaverGoods)); 
		
		//맞춤제작
		detailAttribute.setCustomProductYn("1".equals(paNaverGoods.getOrderMakeYN()) ? "TRUE" : "FALSE");
		
		
		// SD쿠폰프로모션 적용
		// 할인 값을 나타내는 value의 값은 0이 아닌 1~10000000까지의 수로 입력
		// 할인 값 이용을 하지 않을 시 CustomerBenefit 입력하지 않음
		if ( 0 < (long)(goodsPrice.getSalePrice() - goodsPrice.getBestPrice())) {
			// PC 할인(모바일할인 동일)
			discountMethod.setValue((long)(goodsPrice.getSalePrice() - goodsPrice.getBestPrice()));
			discountMethod.setUnitType("WON");
			immediateDiscountPolicy.setDiscountMethod(discountMethod);
			
			// 모바일 할인(PC할인 동일) 24.02.07 필드 지원 중단
//			mobileDiscountMethod.setValue((long)(goodsPrice.getSalePrice() - goodsPrice.getBestPrice()));
//			mobileDiscountMethod.setUnitType("WON");				
//			immediateDiscountPolicy.setMobileDiscountMethod(mobileDiscountMethod);
			
			customerBenefit.setImmediateDiscountPolicy(immediateDiscountPolicy);		
			originProduct.setCustomerBenefit(customerBenefit);
		}		
		
		// 네이버쇼핑 등록 여부
		smartstoreChannelProduct.setNaverShoppingRegistration("TRUE");
		
		// 판매자 코드 정보
		sellerCodeInfo.setSellerManagementCode(paNaverGoods.getGoodsCode());
		detailAttribute.setSellerCodeInfo(sellerCodeInfo);
		
		// 리뷰 노출 여부
		purchaseReviewInfo.setPurchaseReviewExposure("TRUE");		
		detailAttribute.setPurchaseReviewInfo(purchaseReviewInfo);
		
		// 옵션등록
		optionInfo = createProductOption(goodsdtList, optionInfo);
		detailAttribute.setOptionInfo(optionInfo);
					
		// 1회 최대구매수량 
		purchaseQuantityInfo.setMaxPurchaseQuantityPerOrder(paNaverGoods.getOrderMaxQty() > 0 ? (long) paNaverGoods.getOrderMaxQty() : 100);		
		
		// 1인 최대구매수량
		// 1인 구매시 최대구매수량은 1회 구매시 최대구매수량 이상으로 입력
		if ("1".equals(paNaverGoods.getCustOrdQtyCheckYN()) && paNaverGoods.getTermOrderQty() > 0
				&& paNaverGoods.getTermOrderQty() > purchaseQuantityInfo.getMaxPurchaseQuantityPerOrder())			
			purchaseQuantityInfo.setMaxPurchaseQuantityPerId((long)paNaverGoods.getTermOrderQty());
		else
			purchaseQuantityInfo.setMaxPurchaseQuantityPerId(purchaseQuantityInfo.getMaxPurchaseQuantityPerOrder());
		
		if (paNaverGoods.getOrderMinQty() >= 2)
			purchaseQuantityInfo.setMinPurchaseQuantity(paNaverGoods.getOrderMinQty()); // 최소구매수량. 2개부터 설정 가능하다. 최소구매수량이 1개인 경우 아예 입력하지 않아야 한다.
			
		detailAttribute.setPurchaseQuantityInfo(purchaseQuantityInfo);
		
		//네이버쇼핑 검색 정보 , 기존(soap) 모델 정보가 NaverShoppingSearchInfo로 변경됨
		detailAttribute.setNaverShoppingSearchInfo(createNaverShoppingSearchInfo(paNaverGoods, productInfoProvidedNotice));
		originProduct.setDetailAttribute(detailAttribute);
		
		return originProduct;
	}
	
	private NaverShoppingSearchInfo createNaverShoppingSearchInfo(PaNaverGoodsVO paNaverGoods,ProductInfoProvidedNotice productInfoProvidedNotice) throws Exception {
		NaverShoppingSearchInfo naverShoppingSearchInfo = new NaverShoppingSearchInfo();
		
		naverShoppingSearchInfo.setBrandName(paNaverGoods.getBrandName());
		naverShoppingSearchInfo.setManufacturerName(paNaverGoods.getMakecoName());
		if (productInfoProvidedNotice == null) return naverShoppingSearchInfo;
		
		if(paNaverGoods.getModelNo() != null) {
			naverShoppingSearchInfo.setModelId(ComUtil.objToLong(paNaverGoods.getModelNo()));
		}else {
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", paNaverGoods.getGoodsCode());
			paramMap.put("paCode", paNaverGoods.getPaCode());
			
			String modelInputYn = paNaverGoodsService.selectModelInputYN(paramMap);
			if ("1".equals(modelInputYn)) {
				if (productInfoProvidedNotice.getFurniture() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getFurniture().getItemName());
				else if (productInfoProvidedNotice.getImageAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getImageAppliances().getItemName());
				else if (productInfoProvidedNotice.getHomeAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getHomeAppliances().getItemName());
				else if (productInfoProvidedNotice.getSeasonAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getSeasonAppliances().getItemName());
				else if (productInfoProvidedNotice.getOfficeAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getOfficeAppliances().getItemName());
				else if (productInfoProvidedNotice.getOpticsAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getOpticsAppliances().getItemName());
				else if (productInfoProvidedNotice.getMicroElectronics() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getMicroElectronics().getItemName());
				else if (productInfoProvidedNotice.getNavigation() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getNavigation().getItemName());
				else if (productInfoProvidedNotice.getCarArticles() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getCarArticles().getItemName());
				else if (productInfoProvidedNotice.getMedicalAppliances() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getMedicalAppliances().getItemName());
				else if (productInfoProvidedNotice.getKitchenUtensils() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getKitchenUtensils().getItemName());
				else if (productInfoProvidedNotice.getKids() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getKids().getItemName());
				else if (productInfoProvidedNotice.getMusicalInstrument() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getMusicalInstrument().getItemName());
				else if (productInfoProvidedNotice.getSportsEquipment() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getSportsEquipment().getItemName());
				else if (productInfoProvidedNotice.getRentalEtc() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getRentalEtc().getItemName());
				else if (productInfoProvidedNotice.getEtc() != null)
					naverShoppingSearchInfo.setModelName(productInfoProvidedNotice.getEtc().getItemName());
				else
					naverShoppingSearchInfo.setModelName("상품상세참조");
			}
		}
		
		
		
		return naverShoppingSearchInfo ;
	}
	
	

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paNaverGoods
	 */
	private void settingDescribeExt(PaNaverGoodsVO paNaverGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paNaverGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4>"
						+ paNaverGoods.getGoodsCom() + "</div></div>")
				: "";

		// 웹기술서
		paNaverGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + paNaverGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ (StringUtils.hasText(paNaverGoods.getCollectImage())
								? "<img alt='' src='" + paNaverGoods.getCollectImage() + "' /><br /><br /><br />"
								: "") // 착불이미지
						+ goodsCom // 상품구성
						+ paNaverGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + paNaverGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paNaverGoods.getNoticeExt())) {
			paNaverGoods.setDescribeExt(
					paNaverGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + paNaverGoods.getDescribeExt());
		}
	}
	
	/**
	 * 상품 요약 정보
	 * 
	 * @param goodsCode
	 * @return
	 */
	private ProductInfoProvidedNotice createProductSummaryInfo(String goodsCode, ProductInfoProvidedNotice productInfoProvidedNotice) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("groupCode", PaGroup.NAVER.code());
						
		String offerType = paNaverGoodsService.selectPaOfferType(paramMap);
		offerType = offerType.substring(2, 4);

		switch (offerType) {
		case "01": // 의류
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("WEAR");
			Wear wear = paNaverV3GoodsService.selectWearSummary(paramMap);
			productInfoProvidedNotice.setWear(wear);
			break;
		case "02": // 신발
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("SHOES");
			Shoes shoes = paNaverV3GoodsService.selectShoesSummary(paramMap);
			productInfoProvidedNotice.setShoes(shoes);
			break;
		case "03": // 가방
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("BAG");
			Bag bag = paNaverV3GoodsService.selectBagSummary(paramMap);
			productInfoProvidedNotice.setBag(bag);
			break;
		case "04": // 패션잡화
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("FASHION_ITEMS");
			FashionItems fashionItems = paNaverV3GoodsService.selectFashionItemsSummary(paramMap);
			productInfoProvidedNotice.setFashionItems(fashionItems);
			break;
		case "05": // 침구류/커튼
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("SLEEPING_GEAR");
			SleepingGear sleepingGear = paNaverV3GoodsService.selectSleepingGearSummary(paramMap);
			productInfoProvidedNotice.setSleepingGear(sleepingGear);
			break;
		case "06": // 가구
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("FURNITURE");
			Furniture furniture = paNaverV3GoodsService.selectFurnitureSummary(paramMap);
			productInfoProvidedNotice.setFurniture(furniture);
			break;
		case "07": // 영상가전
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("IMAGE_APPLIANCES");
			ImageAppliances imageAppliances = paNaverV3GoodsService.selectImageAppliancesSummary(paramMap);
			productInfoProvidedNotice.setImageAppliances(imageAppliances);
			break;
		case "08": // 가정용 전기제품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("HOME_APPLIANCES");
			HomeAppliances homeAppliances = paNaverV3GoodsService.selectHomeAppliancesSummary(paramMap);
			productInfoProvidedNotice.setHomeAppliances(homeAppliances);
			break;
		case "09": // 계절가전
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("SEASON_APPLIANCES");
			SeasonAppliances seasonAppliances = paNaverV3GoodsService.selectSeasonAppliancesSummary(paramMap);
			productInfoProvidedNotice.setSeasonAppliances(seasonAppliances);
			break;
		case "10": // 사무용기기
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("OFFICE_APPLIANCES");
			OfficeAppliances officeAppliances = paNaverV3GoodsService.selectOfficeAppliancesSummary(paramMap);
			productInfoProvidedNotice.setOfficeAppliances(officeAppliances);
			break;
		case "11": // 광학기기
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("OPTICS_APPLIANCES");
			OpticsAppliances opticsAppliances = paNaverV3GoodsService.selectOpticsAppliancesSummary(paramMap);
			productInfoProvidedNotice.setOpticsAppliances(opticsAppliances);
			break;
		case "12": // 소형전자
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("MICROELECTRONICS");
			MicroElectronics microElectronics = paNaverV3GoodsService.selectMicroElectronicsSummary(paramMap);
			productInfoProvidedNotice.setMicroElectronics(microElectronics);
			break;
		case "13": // 휴대폰
			break;
		case "14": // 네비게이션
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("NAVIGATION");
			Navigation navigation = paNaverV3GoodsService.selectNavigationSummary(paramMap);
			productInfoProvidedNotice.setNavigation(navigation);
			break;
		case "15": // 자동차용품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("CAR_ARTICLES");
			CarArticles carArticles = paNaverV3GoodsService.selectCarArticlesSummary(paramMap);
			productInfoProvidedNotice.setCarArticles(carArticles);
			break;
		case "16": // 의료기기
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("MEDICAL_APPLIANCES");
			MedicalAppliances medicalAppliances = paNaverV3GoodsService	.selectMedicalAppliancesSummary(paramMap);
			productInfoProvidedNotice.setMedicalAppliances(medicalAppliances);
			break;
		case "17": // 주방용품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("KITCHEN_UTENSILS");
			KitchenUtensils kitchenUtensils = paNaverV3GoodsService.selectKitchenUtensilsSummary(paramMap);
			if (kitchenUtensils.getSize() == null) {
				kitchenUtensils.setSize("상품상세 참조");
			} else if (kitchenUtensils.getSize().length() > 200) {
				kitchenUtensils.setSize((String) kitchenUtensils.getSize().subSequence(0, 200));
			}			
			productInfoProvidedNotice.setKitchenUtensils(kitchenUtensils);
			break;
		case "18": // 화장품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("COSMETIC");
			Cosmetic cosmetic = paNaverV3GoodsService.selectCosmeticSummary(paramMap);
			productInfoProvidedNotice.setCosmetic(cosmetic);
			break;
		case "19": // 귀금속/보석/시계류
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("JEWELLERY");
			Jewellery Jewellery = paNaverV3GoodsService.selectJewellerySummary(paramMap);
			productInfoProvidedNotice.setJewellery(Jewellery);
			break;
		case "20": // 식품(농수산물)
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("FOOD");
			Food food = paNaverV3GoodsService.selectFoodSummary(paramMap);
			productInfoProvidedNotice.setFood(food);
			break;
		case "21": // 가공식품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("GENERAL_FOOD");
			GeneralFood generalFood = paNaverV3GoodsService.selectGeneralFoodSummary(paramMap);
			productInfoProvidedNotice.setGeneralFood(generalFood);
			break;
		case "22": // 건강기능식품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("DIET_FOOD");
			DietFood dietFood = paNaverV3GoodsService.selectDietFoodSummary(paramMap);
			productInfoProvidedNotice.setDietFood(dietFood);
			break;
		case "23": // 어린이제품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("KIDS");
			Kids kids = paNaverV3GoodsService.selectKidsSummary(paramMap);
			productInfoProvidedNotice.setKids(kids);
			break;
		case "24": // 악기
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("MUSICAL_INSTRUMENT");
			MusicalInstrument musicalInstrument = paNaverV3GoodsService.selectMusicalInstrumentSummary(paramMap);
			productInfoProvidedNotice.setMusicalInstrument(musicalInstrument);
			break;
		case "25": // 스포츠용품
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("SPORTS_EQUIPMENT");
			SportsEquipment sportsEquipment = paNaverV3GoodsService.selectSportsEquipmentSummary(paramMap);
			productInfoProvidedNotice.setSportsEquipment(sportsEquipment);
			break;
		case "26": // 서적
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("BOOKS");
			Books books = paNaverV3GoodsService.selectBooksSummary(paramMap);
			productInfoProvidedNotice.setBooks(books);
			break;
		case "27": // 호텔/팬션 예약
			break;
		case "28": // 여행패키지
			break;
		case "29": // 항공권
			break;
		case "30": // 자동차 대여 서비스
			break;
		case "31": // 물품대여 서비스
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("RENTAL_ETC");
			RentalEtc rentalEtc1 = paNaverV3GoodsService.selectRentalEtcSummary(paramMap);
			productInfoProvidedNotice.setRentalEtc(rentalEtc1);
			break;
		case "32": // 물품대여 서비스
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("RENTAL_ETC");
			RentalEtc rentalEtc2 = paNaverV3GoodsService.selectRentalEtcSummary(paramMap);
			productInfoProvidedNotice.setRentalEtc(rentalEtc2);
			break;
		case "33": // 디지털 콘텐츠(음원/게임/인터넷강의 등)
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("DIGITAL_CONTENTS");
			DigitalContents digitalContents = paNaverV3GoodsService.selectDigitalContentsSummary(paramMap);
			productInfoProvidedNotice.setDigitalContents(digitalContents);
			break;
		case "34": // 상품권/쿠폰
			break;
		case "35": // 모바일쿠폰
			break;
		case "36": // 영화/공연
			break;
		case "37": // 기타 용역
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("ETC_SERVICE");
			EtcService EtcService = paNaverV3GoodsService.selectEtcServiceSummary(paramMap);
			productInfoProvidedNotice.setEtcService(EtcService);
			break;
		case "38": // 기타 재화
			productInfoProvidedNotice.setProductInfoProvidedNoticeType("ETC");
			Etc etc = paNaverV3GoodsService.selectEtcSummary(paramMap);
			productInfoProvidedNotice.setEtc(etc);
			break;
		default:
			break;
		}
		return productInfoProvidedNotice;
	}
	
	/**
	 * 원산지 정보 
	 * 상품에 입력할 원산지 정보를 생성한다.
	 *
	 * @return 원산지 정보
	 */
	private OriginAreaInfo createOriginAreaInfo(PaNaverGoodsVO paNaverGoodsVO) {
		OriginAreaInfo originArea = new OriginAreaInfo();
		originArea.setOriginAreaCode(paNaverGoodsVO.getNaverOriginCode()); // 원산지 코드
		originArea.setPlural("FALSE"); // 복수 원산지 여부
		
		if(!paNaverGoodsVO.getNaverOriginCode().startsWith("00")){
			 originArea.setImporter(paNaverGoodsVO.getMakecoName()); // 수입사. 원산지 코드가 수입산(ex: 미국, 중국 등)인 경우 입력한다.
		}
		
		// originArea.setContent("원산지 직접입력"); // 원산지 직접입력. 원산지코드가 04 (직접입력)인 경우에만 입력한다.
		return originArea;
	}
	
	/**
	 * 인증정보
	 *
	 * @return 인증정보 목록
	 */
	private static List<ProductCertificationInfos> createCertificationListInfo(List<PaCertificationVO> paCertificationList) {
		
		List<ProductCertificationInfos> productCertificationInfosList = null;
		ProductCertificationInfos productCertificationInfos = null;		
		
		for(PaCertificationVO list : paCertificationList){
			productCertificationInfos = new ProductCertificationInfos();
			productCertificationInfosList = new ArrayList<ProductCertificationInfos>();		
			productCertificationInfos.setCertificationInfoId(Long.parseLong(list.getCertiCode())); // 인증유형 ID. GetCategoryInfo에서 조회하는 CertificationCategoryListType의 code 값을 입력
			// 인증번호  : KC->KC_CERTIFICATION, CHI->CHILD_CERTIFICATION, GRN->GREEN_PRODUCTS
			productCertificationInfos.setCertificationKindType(CertificationKindType.valueOf(list.getExceptCode()).codeName()); // 인증 정보 종류
			productCertificationInfos.setCertificationMark("TRUE"); // 인증 마크 사용 여부
			
			if(list.getCertiAgencyYn().equals("1"))
				productCertificationInfos.setName(list.getCertiAgency()); // 인증기관
			if(list.getCertiNoYn().equals("1"))
				productCertificationInfos.setCertificationNumber(list.getCertiNo()); // 인증번호
			if(list.getCertiCompanyYn().equals("1"))
				productCertificationInfos.setCompanyName(list.getCertiCompany()); // 인증상호
			
			productCertificationInfosList.add(productCertificationInfos);
		}
		return productCertificationInfosList;
	}
		
	/**
	 * 이미지 정보 생성
	 * 상품 이미지 정보를 생성한다.
	 * 이미지 URL은 이미지 업로드 API(UploadImage)로 업로드하고 반환받은 이미지 URL을 입력해야 한다.
	 *
	 * @return 이미지 정보
	 */
	private ProductImages createImageInfo(PaNaverGoodsImage paNaverGoodsImage) {

		ProductImages image = new ProductImages();
		RepresentativeImage representativeImage = new RepresentativeImage();
		OptionalImages optionalImages;
		
		List<OptionalImages> optionalImagesList = new ArrayList<OptionalImages>();
		
		representativeImage.setUrl(paNaverGoodsImage.getImageNaverP());
		image.setRepresentativeImage(representativeImage);

		if (paNaverGoodsImage.getImageNaverAp() != null) {
			optionalImages = new OptionalImages();
			optionalImages.setUrl(paNaverGoodsImage.getImageNaverAp());
			optionalImagesList.add(optionalImages);
		}
		if (paNaverGoodsImage.getImageNaverBp() != null) {
			optionalImages = new OptionalImages();
			optionalImages.setUrl(paNaverGoodsImage.getImageNaverBp());
			optionalImagesList.add(optionalImages);
		}
		if (paNaverGoodsImage.getImageNaverCp() != null) {
			optionalImages = new OptionalImages();
			optionalImages.setUrl(paNaverGoodsImage.getImageNaverCp());
			optionalImagesList.add(optionalImages);			
		}
		if (paNaverGoodsImage.getImageNaverDp() != null) {
			optionalImages = new OptionalImages();
			optionalImages.setUrl(paNaverGoodsImage.getImageNaverDp());
			optionalImagesList.add(optionalImages);
		}
		
		image.setOptionalImages(optionalImagesList);
		
		return image;
	}
	
	/**
	 * 배송 정보
	 * 상품 배송정보를 입력한다.
	 * 배송 없는 상품은 DeliveryInfo 값을 null로 입력해야 한다.
	 *
	 * @return 배송 정보
	 */
	private DeliveryInfo createDeliveryInfo(PaNaverGoodsVO paNaverGoodsVO) throws Exception {
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		DeliveryFee deliveryFee = new DeliveryFee();
		DeliveryFeeByArea deliveryFeeByArea = new DeliveryFeeByArea();
		ClaimDeliveryInfo claimDeliveryInfo = new ClaimDeliveryInfo();
		
		// 배송 속성 타입 코드
		deliveryInfo.setDeliveryAttributeType("NORMAL");
		
		// 주문 제작 상품 여부 ( “Y” 또는 “N” ) //주문 제작 상품 발송 예정일 최대 세팅 14일 이여서 미사용 , 맞춤제작  detailAttribute.setCustomProductYn("TRUE") 만 사용하기로 함
		/*deliveryInfo.setCustomProductAfterOrderYn("1".equals(paNaverGoodsVO.getOrderMakeYN()) ? "TRUE" : "FALSE");
		if("1".equals(paNaverGoodsVO.getOrderMakeYN())) {
			deliveryInfo.setExpectedDeliveryPeriodType("FOURTEEN"); // 주문 제작 상품 발송 예정일 타입 코드(주문 제작 상품 일때 필수, ETC -사용불가)
		}*/
					 
		deliveryInfo.setDeliveryType("DELIVERY"); // 배송 방법 유형 코드 (DELIVERY(택배, 소포, 등기), DIRECT(직접배송(화물배달))
		deliveryInfo.setDeliveryBundleGroupUsable("FALSE"); // 묶음 배송 가능 여부		
		
		deliveryInfo.setVisitAddressId(0);
		
		switch (paNaverGoodsVO.getShipCostCode().substring(0, 2)) {
		case "FR": // 무료
			deliveryFee.setDeliveryFeeType("FREE");
			break;
		case "CN":
		case "PL": // 조건부
			deliveryFee.setDeliveryFeeType("CONDITIONAL_FREE");
			deliveryFee.setFreeConditionalAmount(paNaverGoodsVO.getShipCostBaseAmt());
			break;
		default: // 상품별
			deliveryFee.setDeliveryFeeType("PAID");
			break;
		}
		
		deliveryFee.setBaseFee(paNaverGoodsVO.getOrdCost()); // 기본 배송비
		deliveryFee.setDeliveryFeePayType("PREPAID"); // 배송비 결제 방식 타입 코드. COLLECT(착불), PREPAID(선결제), COLLECT_OR_PREPAID(착불 또는 선결제)
		
		// 지역별 추가 배송 권역 (2권역 - 내륙/제주 및 도서산간, 3권역 - 내륙/제주 외 도서 산간)
		int jejuCost = paNaverGoodsVO.getJejuCost() - paNaverGoodsVO.getOrdCost();
		int islandCost = paNaverGoodsVO.getIslandCost() - paNaverGoodsVO.getOrdCost();
		if (jejuCost > 0 || islandCost > 0) {
			deliveryFeeByArea.setDeliveryAreaType("AREA_3"); // 지역별 추가 배송 권역 (AREA_2(내륙/제주 및 도서산간 지역으로 구분(2권역)), AREA_3(내륙/제주/제주 외 도서산간 지역으로 구분(3권역)))
			deliveryFeeByArea.setArea2extraFee(jejuCost <= 0 ? 100 : jejuCost); // 2권역 배송비
			deliveryFeeByArea.setArea3extraFee(islandCost <= 0 ? 100 : islandCost); // 3권역 배송비
			deliveryFee.setDeliveryFeeByArea(deliveryFeeByArea);
		}
		
		deliveryInfo.setDeliveryFee(deliveryFee);
		
		//delivery.setReturnDeliveryCompanyPriority("0"); // 반품/교환 택배사. GetReturnsCompanyList API로 택배사 코드를 조회하여 입력한다. (0.우체국택배)
		
		 // 반품 배송비
		if(paNaverGoodsVO.getOrdCost()==0)
			claimDeliveryInfo.setReturnDeliveryFee(paNaverGoodsVO.getReturnCost()/2);
		else
			claimDeliveryInfo.setReturnDeliveryFee(paNaverGoodsVO.getReturnCost());	
			
		claimDeliveryInfo.setExchangeDeliveryFee(paNaverGoodsVO.getChangeCost()); // 교환 배송비
		
		// 반품안심케어 설정
		if ("1".equals(paNaverGoodsVO.getInstallYN()) 
				|| "1".equals(paNaverGoodsVO.getCollectYn()) 
				|| "1".equals(paNaverGoodsVO.getOrderMakeYN())) {
			claimDeliveryInfo.setFreeReturnInsuranceYn("FALSE"); // 설치/착불/주문제작 상품 반품안심케어 제외
		} else {
			claimDeliveryInfo.setFreeReturnInsuranceYn("TRUE");
		}

		deliveryInfo.setClaimDeliveryInfo(claimDeliveryInfo);		

		ParamMap paramMap = new ParamMap();
		paramMap.put("goodsCode", paNaverGoodsVO.getGoodsCode());
		paramMap.put("paCode", paNaverGoodsVO.getPaCode());
		
		PaDeliveryVO paDeliveryVO = paNaverGoodsService.selectPaDelivery(paramMap);
		
		// 대표 주소지로 설정
//		delivery.setReturnAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId())); 		// 반품/교환지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
//		if(paDeliveryVO.getShippingAddressId() != null)
//			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getShippingAddressId())); // 출고지 주소 코드. GetAddressBookList API로 주소 코드를 조회하여 입력한다.
//		else
//			delivery.setShippingAddressId(Long.parseLong(paDeliveryVO.getReturnAddressId()));	// 츨고지가 없으면 회수지 입력
		
		deliveryInfo.setDeliveryCompany(paDeliveryVO.getDeliveryCompany());
		
		return deliveryInfo;
	}
	
	/**
	 * 옵션 등록
	 *
	 */
	private OptionInfo createProductOption(List<PaGoodsdtMapping> paGoodsdtMappingList, OptionInfo optionInfo) throws Exception {
		
		optionInfo.setOptionCombinationSortType("CREATE"); // CREATE(등록순), ABC(가나다순)
		
		long MAX_STOCK = 99_999;
		List<OptionCombinations> optionCombinationsList = new ArrayList<OptionCombinations>();		
		
		// 조합형 옵션명
		OptionCombinationGroupNames optionCombinationGroupNames = new OptionCombinationGroupNames();
		optionCombinationGroupNames.setOptionGroupName1("옵션");		
		optionInfo.setOptionCombinationGroupNames(optionCombinationGroupNames);		
		
		// 조합형 옵션
		for(PaGoodsdtMapping optionItem : paGoodsdtMappingList) {
			OptionCombinations optionCombinations = new OptionCombinations();
			optionCombinations.setOptionName1(optionItem.getGoodsdtInfo().replace("*", "X").replace("?", "").replace("<", "(").replace(">", ")").replace("\"", "").replace("\\", ""));
			// 상품전체재고 최대 99,999,999개
			optionCombinations.setStockQuantity(Long.parseLong(optionItem.getTransOrderAbleQty()) > MAX_STOCK ? MAX_STOCK
					: Long.parseLong(optionItem.getTransOrderAbleQty()));
			optionCombinations.setSellerManagerCode(optionItem.getGoodsdtCode());
			optionCombinations.setUsable("TRUE");
			optionCombinationsList.add(optionCombinations);
		}

		optionInfo.setOptionCombinations(optionCombinationsList);
				
		return optionInfo;
	}
	
	private ResponseMsg callPostImages(String goodsCode, String paCode, String procId, PaTransService serviceLog) {
		
		// Query Parameters 세팅
		Map<String, String> queryParameters = new HashMap<String, String>();
			
		// Path Parameters
		String pathParameters = "";
		
		File file = null;
		PaNaverGoodsImage paNaverGoodsImage = new PaNaverGoodsImage();
		ResponseMsg result = new ResponseMsg("", "");
		String isLocalYn = "N";
		String imageFilePath = "";
				
		try {
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paGroupCode", paGroupCode);
			
			PaGoodsImage paGoodsImage = paNaverV3GoodsDAO.selectPaGoodsImage(paramMap);
			if (paGoodsImage == null) {
				result.setCode("404");
				result.setMessage("등록된 상품이미지가 없습니다.");
				return result;
			}
			
			// 로컬 체크
			if (("local").equals(IMG_SERVER)) {
				isLocalYn = "Y";
				imageFilePath = context.getRealPath("/uploadfile/img_test.png"); // 로컬 이미지 파일 경로
			} else {
				imageFilePath = IMG_FILE_PATH + paGoodsImage.getImageUrl();
			}
			
			log.info("이미지등록 API 호출 {}", goodsCode);
			List<File> fileList = new ArrayList<File>(); // 네이버 통신 시 넘길 정보
			if (paGoodsImage.getImageP() != null) {
				file = "Y".equals(isLocalYn) ? new File(imageFilePath) :  new File(imageFilePath + paGoodsImage.getImageP());
				fileList.add(file);
			}
			if (paGoodsImage.getImageAp() != null) {
				file = "Y".equals(isLocalYn) ? new File(imageFilePath) :  new File(imageFilePath + paGoodsImage.getImageAp());
				fileList.add(file);
			}
			if (paGoodsImage.getImageBp() != null) {
				file = "Y".equals(isLocalYn) ? new File(imageFilePath) :  new File(imageFilePath + paGoodsImage.getImageBp());
				fileList.add(file);
			}
			if (paGoodsImage.getImageCp() != null) {
				file = "Y".equals(isLocalYn) ? new File(imageFilePath) :  new File(imageFilePath + paGoodsImage.getImageCp());
				fileList.add(file);
			}
			if (paGoodsImage.getImageDp() != null) {
				file = "Y".equals(isLocalYn) ? new File(imageFilePath) :  new File(imageFilePath + paGoodsImage.getImageDp());
				fileList.add(file);
			}
			
			//통신
			ImagesList returnImageList = paNaverV3ConnectUtil.getImg(serviceLog, pathParameters, queryParameters, fileList);
			
			for(Images image : returnImageList.getImages()) { // 배열에 담긴 순서와 네이버에서 반환한 url 순서는 동일
				
				if(paGoodsImage.getImageP() != null && paNaverGoodsImage.getImageNaverP() == null) {
					paNaverGoodsImage.setImageNaverP(image.getUrl());
				}
				else if(paGoodsImage.getImageAp() != null && paNaverGoodsImage.getImageNaverAp() == null) {
					paNaverGoodsImage.setImageNaverAp(image.getUrl());
				}
				else if(paGoodsImage.getImageBp() != null && paNaverGoodsImage.getImageNaverBp() == null) {
					paNaverGoodsImage.setImageNaverBp(image.getUrl());
				}
				else if(paGoodsImage.getImageCp() != null && paNaverGoodsImage.getImageNaverCp() == null) {
					paNaverGoodsImage.setImageNaverCp(image.getUrl());
				}
				else if(paGoodsImage.getImageDp() != null && paNaverGoodsImage.getImageNaverDp() == null) {
					paNaverGoodsImage.setImageNaverDp(image.getUrl());
				}
			}
			
			result.setCode("200");
			result.setMessage(PaNaverV3ApiRequest.API_SUCCESS_CODE);

			paNaverGoodsImage.setGoodsCode(goodsCode);
			paNaverGoodsImage.setLastSyncDate(paGoodsImage.getLastSyncDate());
			paNaverGoodsImage.setInsertId(procId);
			paNaverGoodsImage.setModifyId(procId);
			
			paNaverResultService.saveTransImage(paNaverGoodsImage);

		} catch (TransApiException ex){
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paNaverGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 이미지등록서비스 End - {} =====", goodsCode);
		}
		return result;
	}
	
	/**
     * 수기중단대상 여부
     * 
     * @param rejectMsg
     * @return
     */
	private boolean isRejectMsg(String rejectMsg) {

		if (!StringUtils.hasText(rejectMsg))
			return false;

		String[] rejectMatch = new String[] { "등록불가인 단어"
				, "등록불가인 특수문자"
				, "사용하실 수 없습니다"
				, "입력해 주세요"
				, "중복된 조합형 옵션"
				, "인증대상"
				, "대분류"
				};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 네이버 상품 수정[V3]
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg updateProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PANAVERAPI_V3_01_003");
		serviceLog.setServiceNote("[V3]네이버-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.NAVER.code());
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);
		
		// 상품수정
		ResponseMsg result = callUpdateProduct(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		
		PaNaverGoodsVO paNaverGoods = null;
		
		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);
			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			
			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paNaverGoods = paNaverGoodsDAO.selectPaNaverGoodsInfo(paramMap);
						
			if (paNaverGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paNaverGoods.getProductId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}
			
			if (paNaverGoods.getDescribeExt() == null) {
				paNaverGoods.setDescribeExt("");
				if (paNaverGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}
			
			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.NAVER.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
			
			// 상품설명설정
			settingDescribeExt(paNaverGoods);
			
			paramMap.put("groupCode", paNaverGoods.getPaGroupCode());
			
			// 이미지 등록
			PaNaverGoodsImageVO paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);
			if (paNaverGoodsImage == null) {
				result = callPostImages(goodsCode, paCode, procId, serviceLog);
				if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
					return result;
				}
				paNaverGoodsImage = paNaverGoodsService.selectPaNaverGoodsImage(paramMap);
			}
			
			// 인증정보
			List<PaCertificationVO> paCertificationList = paNaverGoodsService.selectPaCertificationList(paramMap);

			// 옵션 등록
			paramMap.put("paGoodsCode",paramMap.get("goodsCode"));
			List<PaGoodsdtMapping> goodsdtList = paNaverGoodsService.selectPaGoodsdt(paramMap);
			
			if (goodsdtList.size() < 1) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			paNaverGoods.setModifyId(procId);
			
			// 네이버 상품 전문 (OriginProduct 객체 생성)
			OriginProduct originProduct = createProduct(paNaverGoods, paNaverGoodsImage, goodsPrice, serviceLog.getTransServiceNo(), paCertificationList, goodsdtList);

			// 네이버 상품 전문 (SmartstoreChannelProduct 객체 생성)
			SmartstoreChannelProduct smartstoreChannelProduct = new SmartstoreChannelProduct(); // 스마트스토어 채널 상품
			// 네이버쇼핑 채널 상품 전용 상품명
			smartstoreChannelProduct.setChannelProductName(paNaverGoods.getPaGoodsName().replace("*", "X").replace("?", ""));
			// 네이버쇼핑 등록 여부
			smartstoreChannelProduct.setNaverShoppingRegistration("TRUE");
			// 전시 상태 코드(스마트스토어 채널 전용)
			smartstoreChannelProduct.setChannelProductDisplayStatusType("ON"); //WAIT(전시 대기), ON(전시 중), SUSPENSION(전시 중지)
						
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			
			apiDataObject.put("originProduct", originProduct);
			apiDataObject.put("smartstoreChannelProduct", smartstoreChannelProduct);
			
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = paNaverGoods.getProductId().toString();
						
			Map<String, Object> productMap = new HashMap<String, Object>();	

			log.info("상품수정 API 호출 {}", goodsCode);
			
			// 상품수정API 통신
			productMap= paNaverV3ConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);
			
			long orgProductNo = (long)productMap.get("originProductNo"); // 원상품코드
			long productId = (long) productMap.get("smartstoreChannelProductNo"); // 채널상품코드

			if (productId != 0) {
				result.setCode("200");
				result.setMessage(PaNaverApiRequest.API_SUCCESS_CODE);
				paNaverGoods.setOrgProductNo(Long.toString(orgProductNo)); 
				paNaverGoods.setProductId(Long.toString(productId));	
				paNaverResultV3Service.saveTransProduct(paNaverGoods, goodsPrice);
				paNaverResultV3Service.saveTransProductOption(goodsdtList, goodsCode, paCode, procId); // 단품 재고만 업데이트, target 업데이트는 옵션 매핑 이후 처리
			} else {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다.");
			}
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paNaverGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품수정서비스 End - {} =====", goodsCode);
		}
		return result;
	}


	/**
	 * 네이버 상품 개별 전송
	 * 
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {
		
		ResponseMsg result = new ResponseMsg("", "");
		
		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.NAVER.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}
		
		// 스토아 상품 조회
		PaNaverGoodsVO paNaverGoods = paNaverGoodsMapper.getGoods(goodsCode, paCode);
		
		if(paNaverGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}
		
		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paNaverGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}
		
		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paNaverGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paNaverGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.NAVER.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}
		
		PaTransService transService;
		
		if ("1".equals(paNaverGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paNaverGoods.getPaSaleGb())) {
			// 판매중지
			transService = naverProductV3Service.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}
		
		boolean isUpdated = false;
		boolean isOption = false;
		
		if ("1".equals(paNaverGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				// 옵션등록/수정
				manageOption(goodsCode, paCode, procId, 0);
				result.setMessage("수정완료되었습니다.");
			} else if(HttpStatus.BAD_REQUEST.name().equals(updated.getCode()) && updated.getMessage().contains("modelId")) {
				manageModelId(goodsCode, paCode, procId, 0);
				result.setMessage("모델 ID 정보 없어 수정 실패한 상품, 모델 ID 조회 후 저장");
			} else
				return updated;
		} else {
			// 옵션등록/수정
			ResponseMsg option = manageOption(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(option.getCode())) {
				isOption = true;
				result.setMessage("옵션이 등록/수정되었습니다.");
			}
		}

		if (isUpdated || isOption) { // 상품이 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}
		
		return result;
	}
	
}
