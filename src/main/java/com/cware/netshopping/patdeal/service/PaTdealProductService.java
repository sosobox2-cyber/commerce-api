package com.cware.netshopping.patdeal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.SaleGb;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaTdealGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaTdealEvent;
import com.cware.netshopping.domain.model.PaTdealGoodsDtImage;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsdtMappingMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.patdeal.domain.CustomProperty;
import com.cware.netshopping.patdeal.domain.FlatOption;
import com.cware.netshopping.patdeal.domain.OptionData;
import com.cware.netshopping.patdeal.domain.OptionImages;
import com.cware.netshopping.patdeal.domain.OptionInfo;
import com.cware.netshopping.patdeal.domain.Options;
import com.cware.netshopping.patdeal.domain.Product;
import com.cware.netshopping.patdeal.domain.ProductImages;
import com.cware.netshopping.patdeal.domain.ProductNoMap;
import com.cware.netshopping.patdeal.domain.PropValue;
import com.cware.netshopping.patdeal.domain.RefundableInfo;
import com.cware.netshopping.patdeal.repository.PaTdealGoodsMapper;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaTdealProductService {
	
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;

	@Autowired
	TransLogService transLogService;

	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;
	
	@Autowired
	PaTdealResultService paTdealResultService;
	
	@Autowired
	PaTdealGoodsMapper paTdealGoodsMapper;

	@Autowired
	PaGoodsdtMappingMapper goodsdtMappingMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;
	
	@Autowired
	SyncProductService syncProductService;

	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	TdealProductService tdealProductService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public ResponseMsg registerProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PATDEALAPI_01_001");
		serviceLog.setServiceNote("[API]티딜-상품입점");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		// 입점요청중 해제
		paTdealGoodsMapper.updateClearProceeding(goodsCode, paCode, procId);

		return result;
	}

	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaTdealGoodsVO paTdealGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("paGroupCode", PaGroup.TDEAL.code());
			
			// 상품정보 조회
			paramMap.put("modCase", "INSERT");
			paTdealGoods = paTdealGoodsMapper.selectPaTdealGoodsInfo(paramMap.get());

			if (paTdealGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paTdealGoods.getMallProductNo() != null) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 티딜 상품코드 : " + paTdealGoods.getMallProductNo());
				return result;
			}

			if (paTdealGoods.getDescribeExt() == null) {
				paTdealGoods.setDescribeExt("");
				if (paTdealGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paTdealGoodsMapper.selectPaTdealGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.TDEAL.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
			
			// 티딜 옵션 이미지 조회
			List<PaTdealGoodsDtImage> goodsDtImageList = paTdealGoodsMapper.selectPaTdealGoodsDtImageList(goodsCode);

			// 입점요청중
			paTdealGoodsMapper.updateProceeding(goodsCode, paCode, procId);

			// 상품설명설정
			//settingDescribeExt(paTdealGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paTdealGoodsMapper.selectPaTdealGoodsdtInfoList(paramMap.get());

			paTdealGoods.setModifyId(procId);

			// 티딜 상품 전문
			Product product = createProduct(paTdealGoods, goodsdtMapping, goodsOffer, goodsPrice, goodsDtImageList, serviceLog.getTransServiceNo());

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			apiDataObject.put("body", product);

			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();

			// Path Parameters
			String pathParameters = "";

			log.info("상품등록 API 호출 {}", goodsCode);
			
			Map<String, Object> productMap = new HashMap<String, Object>();	

			productMap = paTdealConnectUtil.getProduct(serviceLog, pathParameters, queryParameters, apiDataObject);

			//VO 선언
			ProductNoMap productNoMap = new ProductNoMap();
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			productNoMap = objectMapper.convertValue(productMap, ProductNoMap.class);
			
			String mallProductNo = productNoMap.getMallProductNo();
			
			if(mallProductNo != null && !"".equals(mallProductNo)) {
				result.setCode("200");
				result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
				paTdealGoods.setMallProductNo(mallProductNo);
				paTdealResultService.saveTransProduct(paTdealGoods, goodsPrice, goodsdtMapping, productNoMap.getMallProductOptionNos());
				
				//심사승인
				String successYn = confirmProduct(goodsCode,mallProductNo, paCode, procId, 0);
				//승인상태업데이트
				if("1".equals(successYn)) {
					paTdealGoodsMapper.updateConfirmComplete(goodsCode, paCode, procId);
				}
			}else {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다");
			}
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 반려처리 제외 메시지 체크
			if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
				paTdealGoodsMapper.rejectTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품등록서비스 End - {} =====", goodsCode);
		}

		return result;
	}

	/**
	 * 반려제외여부
	 * 
	 * @param resultCode
	 * @param rejectMsg
	 * @return
	 */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if (!StringUtils.hasText(rejectMsg) )
            return false;

        String[] rejectNotMatch =
                new String[] {};

        return Arrays.stream(rejectNotMatch).anyMatch(s -> rejectMsg.contains(s));
    }

	private Product createProduct(PaTdealGoodsVO paTdealGoods, List<PaGoodsdtMapping> goodsdtMapping,
			List<PaGoodsOffer> goodsOffer, PaGoodsPriceApply goodsPrice, List<PaTdealGoodsDtImage> goodsDtImageList, long transServiceNo) throws Exception {

		Product product = new Product();
		
		PaTdealEvent paTdealEvent = null;
		
		// 티딜 행사 상품 조회
		paTdealEvent = paTdealGoodsMapper.selectPaTdealEvent(paTdealGoods.getGoodsCode());
		
		if(paTdealGoods.getMallProductNo() != null) {
			product.setMallProductNo(paTdealGoods.getMallProductNo());
		}
		
		// 판매방식 ( default - CONSIGNMENT ) [ PURCHASE: purchase, CONSIGNMENT: Consignment ]
		product.setSaleMethodType("CONSIGNMENT"); //위탁
		
		// 판매 수수료 타입 - 개발 : 상품별, 운영 : 파트너 계약 수수료
		product.setCommissionRateType("PRODUCT"); 
		
		// 판매수수료 비율 - 파트너 수수료인 경우는 파트너 계약 수수료로 들어갑니다
		product.setCommissionRate(paTdealGoods.getCommission());
		
		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paTdealGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paTdealGoods.getGoodsCom() + "</pre></div></div>")
				: "";
		
		String contentHeader = /*"<div align='center'><img alt='' src='" + paTdealGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지 연동하지 않음
				+*/ (StringUtils.hasText(paTdealGoods.getCollectImage()) ? "<img alt='' src='"
						+ paTdealGoods.getCollectImage() + "' /><br /><br /><br />" : "") // 착불이미지
				+ goodsCom + "</div>"; // 상품구성
		
		// 제휴 공지사항
		if (StringUtils.hasText(paTdealGoods.getNoticeExt())) {
			contentHeader = paTdealGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://")	+ contentHeader;
		}	
		
		String contentFooter = "<div>"+ paTdealGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") //기술서
							 + "<br /><br /><br />"
							 + "<div style=\"text-align : center;\"><img alt='' src='" + paTdealGoods.getBottomImage() + "' /></div></div>"; //하단이미지
		
		// 상품상세 헤더(HTML)
		product.setContentHeader(contentHeader);
		
		//if(goodsDtImageList.size() == 0) {			
			// 상품상세 본문
		//	product.setContent(paTdealGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" );
		//}
		// 상품상세 푸터(HTML)
		product.setContentFooter(contentFooter);

		//공급/매입가 - 사입(공급가), 위탁(매입가) nullable
		//product.setPurchasePrice(purchasePrice); 
		
		String paGoodsName = paTdealGoods.getGoodsName();
		paGoodsName = paGoodsName.replace("[", "").replace("]", "");
		
		String brandName = "["+paTdealGoods.getBrandName()+"] " ;
		paGoodsName = brandName + paGoodsName;
		
		if ("1".equals(paTdealGoods.getCollectYn())) {
			paGoodsName = "(착불)" + paGoodsName;
		}
		
		if(paTdealEvent != null) { //티딜 행사 상품명 우선 적용
			paGoodsName = paTdealEvent.getGoodsEventName();
		}
		
		//상품명
		product.setProductName(paGoodsName);
		
		//배송여부
		product.setDeliveryYn("Y");
		
		//가격비교 사이트 
		//product.setComparingPriceSiteTypes(comparingPriceSiteTypes);
		
		//미성년자 구매가능 여부
		product.setMinorPurchaseYn("1".equals(paTdealGoods.getAdultYn()) ? "N" : "Y");
		
		//표준카테고리
		product.setCategoryNo((paTdealEvent !=null && paTdealEvent.getPaLmsdKey()!=null ) ? paTdealEvent.getPaLmsdKey() : paTdealGoods.getPaLmsdKey());
		//장바구니 사용 여부 
		product.setCartUseYn((paTdealEvent !=null && "0".equals(paTdealEvent.getCartUseYn()) ) ? "N" : "Y");
		
		//검색어
		/*String keyWord = null;
		
		if(paTdealGoods.getKeyWord() != null) {
			String[] keyWords = paTdealGoods.getKeyWord().replace(", ", ",").split(",");
			
			List<String> keyWordList = new ArrayList<String>(10);
			for (String keyword : keyWords) {
				if (keyWordList.size() == 10) break; // 검색어는 10개까지만 허용
				if (keyword.length() <= 20) { // 키워드 길이가 20자내인 경우만 허용
					keyWordList.add(keyword);
				}
			}
			keyWord = String.join(",", keyWordList);
			
		}
		product.setKeyword((keyWord == null) ? paTdealGoods.getGoodsName() : keyWord);*/
		
		//브랜드 번호 (default : 0 /	null 또는 0이 아닌 경우, brandName보다 우선으로 반영
		product.setBrandNo((paTdealEvent !=null && paTdealEvent.getPaBrandNo()!=null ) ? paTdealEvent.getPaBrandNo() : paTdealGoods.getPaBrandNo());
		//브랜드명
		//product.setBrandName(brandName);
		
		//단위
		//product.setUnitNameType(unitNameType);
		//단위가격
		//product.setUnitPrice(unitPrice);
		//단위명 ex.개
		//product.setUnitName(unitName);
		
		
		if(paTdealEvent !=null && paTdealEvent.getMaxBuyPersonCnt() != 0 ) {
			// 1인최대구매수량 (없으면 0)
			product.setMaxBuyPersonCnt(paTdealEvent.getMaxBuyPersonCnt());
		}else if("1".equals(paTdealGoods.getCustOrdQtyCheckYn())) { //우선적용하도록 협의
			// 최대구매기간(수량, 없으면 0)
			product.setMaxBuyPeriodCnt(paTdealGoods.getTermOrderQty() > 999 ? 999 : paTdealGoods.getTermOrderQty() ); //최대 구매 수량은 999까지 입력 가능
			// 최대구매기간(일, 없으면 0)
			product.setMaxBuyDays(paTdealGoods.getCustOrdQtyCheckTerm());
		}else {
			//1회 최대 구매수량 (없으면 0)
			product.setMaxBuyTimeCnt(paTdealGoods.getOrderMaxQty() > 999 ? 999 : paTdealGoods.getOrderMaxQty()); //최대 구매 수량은 999까지 입력 가능
		}
		
		// 최소구매수량 (2이상만 입력하세요. 없으면 0)
		product.setMinBuyCnt(paTdealGoods.getOrderMinQty() > 1 ? paTdealGoods.getOrderMinQty() : 0);
		
		//원산지
		product.setPlaceOrigin("상세설명참조");
		
		//적립금 사용 여부 
		product.setAccumulationUseYn("Y");
		
		//출고유형
		product.setShippingAreaType("PARTNER_SHIPPING_AREA");
		
		//즉시할인단위
		product.setImmediateDiscountUnitType("WON");
		
		// 환불가능 세부정보
		/*if (!"0".equals(paTdealGoods.getReturnNoYn()) || "1".equals(paTdealGoods.getFreshYn())) { //신선식품일 경우 교환반품불가 (쿠팡 신선냉동 분류 기준)
			RefundableInfo refundableInfo = new RefundableInfo();
			List<Object> nonRefundableInfo = new ArrayList<Object>();
			nonRefundableInfo.add("RETURN");
			nonRefundableInfo.add("EXCHANGE");
			refundableInfo.setNonRefundableInfo(nonRefundableInfo);
			refundableInfo.setRefundableYn("N");
			product.setRefundableInfo(refundableInfo);
			//환불가능여부
			product.setRefundableYn("N");
		}else {
			//환불가능여부
			product.setRefundableYn("Y");
		}*/
		
		product.setRefundableYn("Y");
		
		//재입고 알림 사용설정 (Y: 사용, N: 미사용 - default)
		//product.setUseRestockNotiYn(useRestockNotiYn);
		
		// 판매기간설정  [ REGULAR: regular sale, PERIOD: Period Sale ]
		product.setSalePeriodType("PERIOD");
		//판매시작일(YYYY-MMDD HH:00:00)
		product.setSaleStartYmdt((paTdealEvent != null && paTdealEvent.getSaleStartDate()!=null) ? paTdealEvent.getSaleStartDate() : paTdealGoods.getSaleStartDate());
		// 판매종료일(YYYY-MMDD HH:00:00)
		String saleEndDate = (paTdealEvent != null && paTdealEvent.getSaleEndDate()!=null) ? paTdealEvent.getSaleEndDate() : paTdealGoods.getSaleEndDate();
		product.setSaleEndYmdt(saleEndDate == null ? "2999-12-31 00:00:00" : saleEndDate);
		
		// 상품분류 [ DEFAULT: General product, OFFLINE: Offline product, RENTAL: Rental Product ]
		product.setClassType("DEFAULT");
		
		//추가정보
		//product.setExtraInfo(extraInfo);
		
		//상품항목추가정보
		List<CustomProperty> customProperties= new ArrayList<CustomProperty>();
		CustomProperty customProperty = new CustomProperty();
		List<PropValue> propValues = new ArrayList<PropValue>();
		PropValue propValue = new PropValue();
		
		propValue.setPropValue("해당없음");
		propValues.add(propValue);
		
		customProperty.setPropName("친환경");
		customProperty.setPropValues(propValues);
		customProperties.add(customProperty);
		product.setCustomProperty(customProperties);
		
		// 판매자 관리코드
		product.setProductManagementCd(paTdealGoods.getGoodsCode());
		
		// 상품군 (default :DELIVERY) [ DELIVERY: Delivery Product Group, SERVICE: Service Product Group ]
		product.setGroupType("DELIVERY");

		// 상품정보고시(json 문자열)
		Map<String ,Object > dutyInfoMap = new HashMap<String, Object>();
		List<Map<String, String>> contentsList 	= new ArrayList<Map<String,String>>();
		for (PaGoodsOffer paGoodsOffer : goodsOffer) {
			Map<String, String> notiItemMap = new HashMap<String, String>();
			notiItemMap.put(paGoodsOffer.getPaOfferCodeName() , paGoodsOffer.getPaOfferExt().replaceAll("\\p{C}", "")); //고시정보 내용
			contentsList.add(notiItemMap);
		}
		dutyInfoMap.put("contents", contentsList);
		dutyInfoMap.put("categoryNo", goodsOffer.get(0).getPaOfferType());
		dutyInfoMap.put("categoryName",goodsOffer.get(0).getPaOfferTypeName());
		
		product.setDutyInfo(StringUtil.objectToJson(dutyInfoMap));
		
		// 옵션타입이 조합형일 경우 옵션 재고의 합이 자동으로 입력됨(단독형일 경우에만 전송, 조합형일 경우 0 / default : 0)
		product.setProductStockCnt(Integer.parseInt(paTdealGoods.getTransOrderAbleQty()));
		
		// 추가관리코드(옵션없음 상품의 추가관리코드를 설정하는 필드)
		//product.setExtraManagementCd(extraManagementCd);
		
		// 플랫폼 노출 설정 여부('Y’일 경우 전체, 'N’일 경우 개별 default : N)
		product.setPlatformDisplayYn("Y");
		// 플랫폼 - 모바일 앱 노출 여부
		product.setPlatformDisplayMobileYn("Y");
		// 플랫폼 - 검색엔진 노출 여부 (PC, 모바일 웹, 모바일 앱, 검색엔진 중 적어도 하나는 노출되어야 함. default : N)
		product.setSearchengineDisplayYn("Y");
		// 플랫폼 - 모바일 웹 노출 여부
		product.setPlatformDisplayMobileWebYn("Y");
		// 플랫폼 - PC 노출 여부
		product.setPlatformDisplayPcYn("Y");
		
		// 묶음배송 가능 여부
		product.setDeliveryCombinationYn("N");
		
		// 비회원 구매가능 여부
		product.setNonmemberPurchaseYn("Y");
		
		// 전시카테고리
		List<String> displayCategoryId = new ArrayList<>();
		displayCategoryId.add((paTdealEvent !=null && paTdealEvent.getDispCatId()!=null ) ? paTdealEvent.getDispCatId() : paTdealGoods.getDisplayCategoryId());
		product.setDisplayCategoryNo(displayCategoryId);
		
		// 정산시 파트너 분담금 (default : 0)
		//product.setPartnerChargeAmt(partnerChargeAmt);
		
		// 리스트 이미지 정보(외부 이미지 여부, 이미지 타입 여부 설정)
		//product.setProductListImageInfo(productListImageInfo);
		
		// 배송관련 판매자 특이사항/고객안내사항
		//product.setDeliveryCustomerInfo(deliveryCustomerInfo);
		
		// 등록된 옵션이미지사용. default = N
		product.setAddOptionImageYn((goodsDtImageList.size() == 0) ? "N" : "Y");
		
		// 배송비 템플릿 번호
		product.setDeliveryTemplateNo(paTdealGoods.getDeliveryTemplateNo());

		// 즉시할인 값 (default: 0)
		product.setImmediateDiscountValue((long) (goodsPrice.getSalePrice() - goodsPrice.getBestPrice()));
		// 즉시할인 기간 설정 여부 (default : N)
		product.setImmediateDiscountPeriodYn("N");
		// 즉시할인 시작일자(YYYYMM-DD HH:00:00)
		// product.setImmediateDiscountStartYmdt(immediateDiscountStartYmdt);
		// 즉시할인 종료일자(YYYYMM-DD HH:00:00)
		// product.setImmediateDiscountEndYmdt(immediateDiscountEndYmdt);


		// 프로모션 적용 가능여부
		if((paTdealEvent != null) && "1".equals(paTdealEvent.getPromoYn())) {
			product.setPromotionYn("Y");
		}else {
			product.setPromotionYn("N");
		}
		// 프로모션 홍보문구 노출 시작일(YYYY-MM-DD HH:00:00) - 프로모션 사용 시 입력
		// product.setPromotionTextStartYmdt(promotionTextStartYmdt);
		// 프로모션 홍보문구 노출 종료
		// product.setPromotionTextEndYmdt(promotionTextEndYmdt);
		// 홍보문구
		// product.setPromotionText(promotionText);
		// 프로모션 기간 사용 여부 (default : N)(nullable)
		product.setPromotionTextYn("N");

		// 원본 이미지 url 그대로 사용하는지 여부(nhn cdn 사용 X) default : false
		product.setUseOriginProductImageUrl("false");

		// 유효기간(유통기한) (YYYY-MM-DD HH:00:00)
		// product.setExpirationYmdt(expirationYmdt);

		// 판매가
		product.setSalePrice((long) paTdealGoods.getSalePrice());
		
		// 제조일자
		//product.setManufactureYmdt(manufactureYmdt);
		// 상품이미지
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
		String imagePath = imageServer + paTdealGoods.getImageUrl();

		List<ProductImages> productImages = new ArrayList<ProductImages>();
				
		//기본이미지
		if(paTdealGoods.getInfoImageG()!=null) {
			productImages.add(createImage(imagePath + paTdealGoods.getInfoImageG() , "Y" , 1));
		}else {			
			productImages.add(createImage(imagePath + paTdealGoods.getImageP() , "Y" , 1));
		}
		
		// 추가이미지
		if(paTdealGoods.getImageAP()!=null) {
			productImages.add(createImage(imagePath + paTdealGoods.getImageAP() , "N" , 2));
		}
		if(paTdealGoods.getImageBP()!=null) {
			productImages.add(createImage(imagePath + paTdealGoods.getImageBP() , "N" , 3));
		}
		if(paTdealGoods.getImageCP()!=null) {
			productImages.add(createImage(imagePath + paTdealGoods.getImageCP() , "N" , 4));
		}
		product.setProductImages(productImages);
		product.setIsOptionUsed(true);
		
		//리스트 이미지
		if(paTdealGoods.getImageList() != null) {
			product.setProductListImage(imagePath + paTdealGoods.getImageList());
		}else if(paTdealGoods.getImageMC() != null){
			product.setProductListImage(imagePath + paTdealGoods.getImageMC() + "/dims/resize/720X320");
		}else {
			product.setProductListImage(imagePath + paTdealGoods.getImageP() + "/dims/resize/320X320/background/720X320");

		}
			
		OptionData optionData = new OptionData();
		
		List<Options> options = new ArrayList<Options>();
		int order = 1;
		for (PaGoodsdtMapping goodsdtmapping : goodsdtMapping) {
			Options option = new Options();
			option.setMallOptionNo((goodsdtmapping.getPaOptionCode() != null) ? goodsdtmapping.getPaOptionCode() : null );
			option.setOptionValue(goodsdtmapping.getGoodsdtInfo());
			option.setAddPrice("0");
			option.setStockCnt(Integer.parseInt(goodsdtmapping.getTransOrderAbleQty()));
			option.setOptionName(goodsdtmapping.getGoodsdtInfoKind());
			option.setOptionSelectType("FLAT");
			option.setOptionManagementCd(goodsdtmapping.getGoodsCode()+"_"+goodsdtmapping.getGoodsdtCode());
			option.setOptionType("COMBINATION");
			option.setUseYn(SaleGb.FORSALE.code().equals(goodsdtmapping.getSaleGb()) ? "Y" : "N");
			option.setOrder(order);
			
			if(goodsdtmapping.getImageFile() != null) {
				List<OptionImages> optionImages = new ArrayList<OptionImages>();
				OptionImages optionImage = new OptionImages();
				optionImage.setImageUrl(imagePath + goodsdtmapping.getImageFile());
				optionImage.setMainYn("Y");
				optionImage.setOrder(1);
				optionImages.add(optionImage);
				
				OptionImages optionImage2 = new OptionImages();
				optionImage2.setImageUrl("http://image.skstoa.com/guide/background.jpg");
				optionImage2.setMainYn("N");
				optionImage2.setOrder(2);
				optionImages.add(optionImage2);
				option.setOptionImages(optionImages);
			}
			
			options.add(option);
			order++;
		}
		optionData.setOptions(options);
		
		product.setOptionData(optionData);
		
		// 과세 적용 기준 [ DUTY: Tax, DUTYFREE: dutyfree, SMALL: small ]
		product.setValueAddedTaxType( "1".equals(paTdealGoods.getTaxYn()) ? "DUTY" : "DUTYFREE");

		//product.setHsCode(hsCode);
		
		//영문상품명
		//product.setProductNameEn(productNameEn);
		
		//해외배송 여부
		product.setDeliveryInternationalYn("N");
		
		return product;
	}

	private ProductImages createImage(String imageUrl, String mainYn, int order) {
		
		ProductImages productImage = new ProductImages();
		productImage.setExternal(false);
		productImage.setImageUrl(imageUrl);
		productImage.setImageUrlType("IMAGE_URL");
		productImage.setMainYn(mainYn);
		productImage.setOrder(order);
		
		return productImage;
	}

	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paTdealGoods
	 */
	private void settingDescribeExt(PaTdealGoodsVO paTdealGoods) {

		// 상품구성템플릿
		String goodsCom = StringUtils.hasText(paTdealGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ paTdealGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		paTdealGoods
				.setDescribeExt(
						"<div align='center'><img alt='' src='" + paTdealGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
								+ (StringUtils.hasText(paTdealGoods.getCollectImage()) ? "<img alt='' src='"
										+ paTdealGoods.getCollectImage() + "' /><br /><br /><br />" : "") // 착불이미지
								+ goodsCom // 상품구성
								+ paTdealGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://")
								+ "<br /><br /><br />" // 기술서
								+ "<img alt='' src='" + paTdealGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(paTdealGoods.getNoticeExt())) {
			paTdealGoods.setDescribeExt(paTdealGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://")
					+ paTdealGoods.getDescribeExt());
		}
	}

	public String confirmProduct(String goodsCode,String mallProductNo, String paCode, String procId, long transBatchNo) {
	
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PATDEALAPI_01_003");
		serviceLog.setServiceNote("[API]티딜-상품승인");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		String successYn = "";
		
		try {
			List<String> productNos = new ArrayList<String>();
			productNos.add(mallProductNo);
			
			HashMap<String , Object> productNosMap = new HashMap<String , Object>();
			productNosMap.put("productNos", productNos);
			
			// Body 세팅 
            ParamMap apiDataObject = new ParamMap();
            apiDataObject.put("body", productNosMap);
            
            successYn = paTdealConnectUtil.confirmProduct(serviceLog, apiDataObject);
            
            serviceLog.setResultCode("200");
            serviceLog.setResultMsg(PaTdealApiRequest.API_SUCCESS_CODE);
            
		} catch (TransApiException ex){
			serviceLog.setResultCode(ex.getCode());
			serviceLog.setResultMsg(ex.getMessage());
		} catch (Exception e) {
			serviceLog.setResultCode("500");
			serviceLog.setResultMsg(e.getMessage());
		}  
		
		transLogService.logTransServiceEnd(serviceLog);

		return successYn;
	}

	public ResponseMsg updateProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PATDEALAPI_01_002");
		serviceLog.setServiceNote("[API]티딜-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
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
		String rtnMsg = Constants.SAVE_SUCCESS;

		ResponseMsg result = new ResponseMsg("", "");

		PaTdealGoodsVO paTdealGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("paGroupCode", PaGroup.TDEAL.code());
			
			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paTdealGoods = paTdealGoodsMapper.selectPaTdealGoodsInfo(paramMap.get());

			if (paTdealGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paTdealGoods.getMallProductNo() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}

			if (paTdealGoods.getDescribeExt() == null) {
				paTdealGoods.setDescribeExt("");
				if (paTdealGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paTdealGoodsMapper.selectPaTdealGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.TDEAL.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}

			// 상품설명설정
		//	settingDescribeExt(paTdealGoods);

			// 티딜 옵션 이미지 조회
			List<PaTdealGoodsDtImage> goodsDtImageList = paTdealGoodsMapper.selectPaTdealGoodsDtImageList(goodsCode);
			
			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paTdealGoodsMapper.selectPaTdealGoodsdtInfoList(paramMap.get());

			if (goodsdtMapping.size() < 1) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			paTdealGoods.setModifyId(procId); 

			// 티딜 상품 전문
			Product product = createProduct(paTdealGoods, goodsdtMapping, goodsOffer, goodsPrice, goodsDtImageList, serviceLog.getTransServiceNo());

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			apiDataObject.put("body", product);

			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();

			// Path Parameters
			String pathParameters = "";

			log.info("상품수정 API 호출 {}", goodsCode);
			
			Map<String, Object> productMap = new HashMap<String, Object>();	

			productMap = paTdealConnectUtil.getProduct(serviceLog, pathParameters, queryParameters, apiDataObject);

			//VO 선언
			ProductNoMap productNoMap = new ProductNoMap();
			
			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			productNoMap = objectMapper.convertValue(productMap, ProductNoMap.class);
			
			String mallProductNo = productNoMap.getMallProductNo();
			
			if(mallProductNo != null && !"".equals(mallProductNo)) {
				result.setCode("200");
				result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);
				paTdealGoods.setMallProductNo(mallProductNo);
				paTdealResultService.saveTransProduct(paTdealGoods, goodsPrice, goodsdtMapping, productNoMap.getMallProductOptionNos());
				paTdealResultService.saveTransProductOption(goodsdtMapping, goodsCode, paCode, procId); // 단품 재고, target 업데이트. 옵션코드는 옵션 매핑 이후 처리
				//승인 대기 상태 업데이트
				paTdealGoodsMapper.updateConfirmWait(goodsCode, paCode, procId);
				
				//심사승인
				String successYn = confirmProduct(goodsCode,mallProductNo, paCode, procId, 0);
				
				//승인 완료 상태업데이트
				if("1".equals(successYn)) {
					paTdealGoodsMapper.updateConfirmComplete(goodsCode, paCode, procId);
				}
			}else {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다");
			}
			
		} catch (TransApiException ex) {
			
			if (paTdealResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}
			
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paTdealGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
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
	 * 수기중단대상 여부
	 * 
	 * @param rejectMsg
	 * @return
	 */
	private boolean isRejectMsg(String rejectMsg) {

		if (!StringUtils.hasText(rejectMsg))
			return false;

        String[] rejectMatch = new String[] {"수정이 불가능한 상태"};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	/**
	 * 옵션매핑
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @param transBatchNo
	 * @return
	 */
	public ResponseMsg manageOption(String goodsCode, String paCode, String procId, long transBatchNo) {

		// 서비스 로그 생성		
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PATDEALAPI_01_006");
		serviceLog.setServiceNote("[API]티딜-상품 옵션 매핑");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.TDEAL.code());
		serviceLog.setPaCode(paCode);
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
		
		PaTdealGoodsVO goods = paTdealGoodsMapper.getGoods(goodsCode,paCode);
		
		if(goods.getMallProductNo() == null) {
			result.setCode("404");
			result.setMessage("처리할 내역이 없습니다.");
			return result;
		}
		
		try {
			// Query Parameters 세팅
			Map<String, String> queryParameters = new HashMap<String, String>();
			
			// Path Parameters
			String pathParameters = goods.getMallProductNo();

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			// VO 선언
			OptionInfo optionInfo = new OptionInfo();

			Map<String, Object> optionInfoMap = new HashMap<String, Object>();

			log.info("옵션매핑서비스 Start {}", goodsCode);

			// 옵션 조회 통신
			optionInfoMap = paTdealConnectUtil.getCommon(serviceLog, pathParameters, queryParameters, apiDataObject);

			// Map -> VO 변환
			ObjectMapper objectMapper = new ObjectMapper();
			optionInfo = objectMapper.convertValue(optionInfoMap, OptionInfo.class);

			for(FlatOption flatOption : optionInfo.getFlatOptions()) {
				String goodsdtCode = flatOption.getOptionManagementCd().split("_")[1];
				String paOptionCode = String.valueOf(flatOption.getOptionNo());
				log.info("상품:{} 단품:{} 옵션코드:{}", goodsCode, goodsdtCode, paOptionCode);
				if (!"null".equals(paOptionCode)) {
					goodsdtMappingMapper.updateOptionCode(goodsCode, goodsdtCode, paCode, procId, paOptionCode);
				}
			}
			
			result.setCode("200");
			result.setMessage(PaTdealApiRequest.API_SUCCESS_CODE);

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
	 * 티딜 상품 개별 전송
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {
		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.TDEAL.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		PaTdealGoodsVO paTdealGoods = paTdealGoodsMapper.getGoods(goodsCode,paCode);
		
		if(paTdealGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paTdealGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paTdealGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paTdealGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.TDEAL.code()) < 1) {
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

		if ("1".equals(paTdealGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paTdealGoods.getPaSaleGb())) {
			// 판매중지
			transService = tdealProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}
		
		boolean isUpdated = false;
		boolean isStock = false;
		boolean isResume = false;
		
		if ("1".equals(paTdealGoods.getTransTargetYn())) {
			// 상품수정
			ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
			if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
				isUpdated = true;
				// 옵션등록/수정
				manageOption(goodsCode, paCode, procId, 0);
				result.setMessage("수정완료되었습니다.");
			} else {
				return updated;
			}
		} else {
			//재고변경
			transService = tdealProductService.updateStockProduct(goodsCode, paCode, procId);
			
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage((isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
		}
		
		if ("1".equals(paTdealGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paTdealGoods.getPaSaleGb())) {
			// 판매재개
			transService = tdealProductService.resumeSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("판매재개실패: " + transService.getResultMsg());
				return result;
			}
			if ("1".equals(transService.getSuccessYn())) {
				isResume = true;
				result.setMessage(
						(isStock || isUpdated ? result.getMessage() + " " : "") + transService.getResultMsg());
			}
		}
		
		if (isStock || isUpdated || isResume) { // 상품/재고가 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}
		return result;
	}

}
